/* Copyright 2011-2013 Google Inc.
 * Copyright 2013 mike wakerly <opensource@hoho.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * Project home page: https://github.com/mik3y/usb-serial-for-android
 */

package com.wave.mzpad.usbserial;

import android.hardware.usb.UsbRequest;
import com.wave.mzpad.common.Log;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.wave.mzpad.common.Utility;

/**
 * Utility class which services a {@link UsbSerialPort} in its {@link #run()}
 * method.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class SerialInputOutputManager implements Runnable {

    private static final String TAG = SerialInputOutputManager.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final int READ_WAIT_MILLIS = 50;
    private static final int BUFSIZ = 2048;

    private final UsbSerialPort mDriver;

    private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUFSIZ);

    // Synchronized by 'mWriteBuffer'
    private final ByteBuffer mWriteBuffer = ByteBuffer.allocate(BUFSIZ);
    
    private StringBuffer stringBuffer = new StringBuffer() ;

    private enum State {
        STOPPED,
        RUNNING,
        STOPPING
    }

    // Synchronized by 'this'
    private State mState = State.STOPPED;

    // Synchronized by 'this'
    private Listener mListener;

    public interface Listener {
        /**
         * Called when new incoming data is available.
         */
        public void onNewData(byte[] data);

        /**
         * Called when {@link SerialInputOutputManager#run()} aborts due to an
         * error.
         */
        public void onRunError(Exception e);
    }

    /**
     * Creates a new instance with no listener.
     */
    public SerialInputOutputManager(UsbSerialPort driver) {
        this(driver, null);
    }

    /**
     * Creates a new instance with the provided listener.
     */
    public SerialInputOutputManager(UsbSerialPort driver, Listener listener) {
        mDriver = driver;
        mListener = listener;
    }

    public synchronized void setListener(Listener listener) {
        mListener = listener;
    }

    public synchronized Listener getListener() {
        return mListener;
    }

    public void writeAsync(byte[] data) {
        synchronized (mWriteBuffer) {
            mWriteBuffer.put(data);
            write();
        }
    }

    public synchronized void stop() {
        if (getState() == State.RUNNING) {
            Log.i(TAG, "Stop requested");
            mState = State.STOPPING;
        }
    }

    private synchronized State getState() {
        return mState;
    }

    /**
     * Continuously services the read and write buffers until {@link #stop()} is
     * called, or until a driver exception is raised.
     *
     * NOTE(mikey): Uses inefficient read/write-with-timeout.
     * TODO(mikey): Read asynchronously with {@link UsbRequest#queue(ByteBuffer, int)}
     */
    @Override
    public void run() {
        synchronized (this) {
            if (getState() != State.STOPPED) {
                throw new IllegalStateException("Already running.");
            }
            mState = State.RUNNING;
        }

        Log.i(TAG, "Running ..");
        try {
            while (true) {
                if (getState() != State.RUNNING) {
                    Log.i(TAG, "Stopping mState=" + getState());
                    break;
                }
                step();
            }
        } catch (Exception e) {
            Log.w(TAG, "Run ending due to exception: " + e.getMessage(), e);
            final Listener listener = getListener();
            if (listener != null) {
              listener.onRunError(e);
            }
        } finally {
            synchronized (this) {
                mState = State.STOPPED;
                Log.i(TAG, "Stopped.");
            }
        }
    }

    private void step() throws IOException, InterruptedException {
        // Handle incoming data.
    	if(Utility.isEmpty(mDriver)){
    		Log.i(TAG, "USB driver is null");
    		return;
    	}
        int len = mDriver.read(mReadBuffer.array(), READ_WAIT_MILLIS);
//    	String buffer = "0x55,0x05,1343834,1200,1820,3,8435,0xff,0x52,0xaa";
//    	int len = buffer.getBytes().length;
        if(len>0){
        	  if (DEBUG) Log.d(TAG, "Read data len=" + len);
        	  byte[] data = new byte[len];
              mReadBuffer.get(data, 0, len);
              stringBuffer.append(new String(data));
              mReadBuffer.clear();
              sendData();    
        }else{
        	if(stringBuffer.length()>0){
        		 sendData() ;
        	}
        }
    }

	private void sendData() {
		int startIndex = stringBuffer.indexOf("0x55") ;
		  int endIndex = stringBuffer.indexOf("0xaa") ;
		  Log.i(TAG, "startIndex:"+startIndex + " endIndex:"+ endIndex + "StringBuffer:"+ stringBuffer.toString());
		  if(startIndex>=0 && endIndex>startIndex){
		    	String dataStr = stringBuffer.substring(startIndex, endIndex+4);
		    	final Listener listener = getListener();
		      if (listener != null) {
		           listener.onNewData(dataStr.getBytes());
		      }
		      stringBuffer.replace(0, endIndex+4, "");
		  }
	}
   // Handle outgoing data.
    public void write() {
    	int len = 0 ;
    	byte[] outBuff = null;
        synchronized (mWriteBuffer) {
            if (mWriteBuffer.position() > 0) {
                len = mWriteBuffer.position();
                outBuff = new byte[len];
                mWriteBuffer.rewind();
                mWriteBuffer.get(outBuff, 0, len);
                mWriteBuffer.clear();
            }
        }
        if (outBuff != null) {
            if (DEBUG) {
                Log.d(TAG, "Writing data len=" + len);
            }
            try {
				mDriver.write(outBuff, READ_WAIT_MILLIS);
			} catch (IOException e) {
				
				 Log.d(TAG, "IOException  error:" + e.getMessage());
			}
        }
    }
    
}
