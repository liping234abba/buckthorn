package com.wave.mzpad.bluetooth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.wave.mzpad.common.Contants;
import com.wave.mzpad.common.Log;

/**
 * 这是一个服务类，负责本机蓝牙与其他远程蓝牙设备的连接, 以及连接后的数据传输处理。 ，主要构建了监听连接，连接，管理连接三个线程。
 */
public class BluetoothService {
	// Debugging（用于调试）
	private static  String TAG = BluetoothService.class.getSimpleName();
	// 唯一通用标示符
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// 本机蓝牙设备
	private final BluetoothAdapter mAdapter;

	// 消息机制的handle
	private Handler mHandler;

	// 蓝牙所处状态
	private int mState;

	/* 构建监听连接蓝牙设备的线程，正在连接蓝牙设备的线程，数据传输（接收与发送）的线程的对象 */
	private ConnectThread mConnectThread;

	/* 四种状态：不确定状态，寻找连接，正在连接，已经连接 */
	public static final int STATE_NONE = 0; // 不确定状态

	public static final int STATE_LISTEN = 1; // 寻找连接

	public static final int STATE_CONNECTING = 2; // 正在连接

	public static final int STATE_NOT_CONNECTING = 4; // 连接失败

	public static final int STATE_DISCONECT = 5; // 断开连接

	public static final int STATE_CONNECTED = 3;//已经连接

	private static BluetoothService bluetoothService;

	/* BluetoothChatService构造函数 */
	private BluetoothService() {
		// 获得本机蓝牙设备
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		// 赋给初始状态
		mState = STATE_NONE;
	}

	public static BluetoothService getInstance() {
		if (bluetoothService == null) {
			synchronized (BluetoothService.class) {
				bluetoothService = new BluetoothService();
			}
		}
		return bluetoothService;
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	/* State属性的set方法 */
	public synchronized void setState(int state) {
		mState = state;
	}

	/* State属性的get方法 */
	public synchronized int getState() {
		return mState;
	}
	
	/**
	 * 开始ConnectThread，触发一个连接到一个远程设备的线程
	 */
	public synchronized void connect(BluetoothDevice device) {
		// 取消任何试图建立连接的线程
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		// 创建和启动给定的设备连接的线程
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * 开始经营一个蓝牙连接 ，进行数据传输（接收数据与发送数据）
	 */
	public synchronized void connectSucessed() {
		mHandler.sendMessage(mHandler.obtainMessage(Contants.REQUEST_SUCCESSED));
		setState(STATE_CONNECTED);
	}

	/**
	 * 关闭所有线程
	 */
	public synchronized void stop() {
		// 试图建立连接的线程
		if (mConnectThread != null) {
			try {
				btSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mConnectThread.cancel();
			mConnectThread = null;
		}
		setState(STATE_NONE);
	}
	
	/**
	 * 连接失败并在主界面通知
	 */
	private void connectionFailed() {
		setState(STATE_NOT_CONNECTING);
		Message msg = mHandler.obtainMessage(Contants.REQUEST_FAILED);
		mHandler.sendMessage(msg);
	}
	
	
	private  BluetoothSocket btSocket; 
	
	public BluetoothSocket getBTSocket() {
		return btSocket;
	}
	/**
	 * 连接线程
	 */
	private class ConnectThread extends Thread {
		// 声明蓝牙套接字接口以及远程蓝牙设备的变量
		private  BluetoothDevice mmDevice;
		// ConnectThread构造函数
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
		}
		@SuppressLint("NewApi")
		@Override
		public void run() {
			Log.i(TAG, "BEGIN mConnectThread device"+mmDevice.getAddress() + " " + mmDevice.getName() + "  " + mmDevice.getBondState());
			// 停止设备发现过程
			mAdapter.cancelDiscovery();
			// 连接到BluetoothSocket
			try {
				BluetoothSocket tmp = null;
				// 从连接到的蓝牙设备得到蓝牙套接字
				//判断山寨机，创建非安全连接，2.3以后支持
				if (Build.BRAND.equals("yusu")) {
					btSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
				} else {
					Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
					btSocket = (BluetoothSocket) m.invoke(mmDevice, Integer.valueOf(1));
				}
				// 连接
				if (!btSocket.isConnected()) {
					btSocket.connect();
				}
				connectSucessed();
			} catch (Exception e) {
				Log.i(TAG, "ConnectThread exception:"+e.getMessage());
				// 关闭socket
				if(btSocket != null){
					try {
						btSocket.close();
					} catch (IOException e1) {
						Log.i(TAG, "message:"+e1.getMessage()) ;
					}
					btSocket = null ;
				}
				connectionFailed();
			}
		}

		/* 关闭Socket */
		public void cancel() {
			try {
				if (btSocket != null) {
					btSocket.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "关闭连接socket失败"+e.getMessage());
			}
		}
	}
}

	
