package com.wave.mzpad.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.wave.mzpad.common.Contants;

import android.os.Handler;
import com.wave.mzpad.common.Log;

/**
 * 心跳线程
 * @author wave.li
 * @date   Dec 25, 2013
 *
 */
public class HeartBeatThread extends Thread{

	
	private long startTime = 0 ;
	
	private Timer timer = null ;
	
	private TimerTask  timerTask = null ;
	
	private CommunicateServer communicateServer ;
	
	private Handler handler ;
	
	private int status; //0 :停止计时，1:正在计时
	
	public static int START = 1 ,STOP = 0 ;

	private String TAG = "HeartBeatThread";
	
	public HeartBeatThread(CommunicateServer cs,Handler handler){
		this.communicateServer = cs ;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		startTime();//启动定时
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	private void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 开始计时
	 */
	public void startTime(){
		if(status==START){
			Log.i(TAG , "HeartBeatThread is started ");
			return ;
		}
		setStatus(START);
		startTime = System.currentTimeMillis() ;
		init();
		timer.schedule(timerTask, new Date(), Contants.REQUEST_TIMEOUT);
		Log.i(TAG , "HeartBeatThread is running ");
	}
	
	/**
	 * 停止计时
	 */
	public void stopTime(){
		if(status==STOP){
			Log.i(TAG , "HeartBeatThread is runned ");
			return ;
		}
		setStatus(STOP);
		timer.purge();
		timer.cancel();
		timerTask.cancel();
		timer = null;
		timerTask = null;
		Log.i(TAG , "HeartBeatThread is stopping ");
	}
	
	/**
	 * 初始化定时器
	 */
	private void init(){
		timer = new Timer() ;
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if(status<=0){
					return ;
				}
				long currentTime = System.currentTimeMillis();
				if(currentTime-startTime>Contants.REPEAT_TIME*Contants.REQUEST_TIMEOUT){
					Log.i(TAG, "连接失败：current:"+currentTime+ " startTime:"+startTime );
					handler.sendEmptyMessage(Contants.REQUEST_FAILED);
					stopTime();
					communicateServer.stopIoManager();
					return ;
				}
				communicateServer.sendHeartBeat();
			}
		};
	}

}
