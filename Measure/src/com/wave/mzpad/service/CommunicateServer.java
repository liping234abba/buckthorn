package com.wave.mzpad.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wave.mzpad.activity.MParamDetailsFragment;
import com.wave.mzpad.common.Contants;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.usbserial.SerialInputOutputManager;
import com.wave.mzpad.usbserial.UsbSerialDriver;
import com.wave.mzpad.usbserial.UsbSerialPort;
import com.wave.mzpad.usbserial.UsbSerialProber;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import com.wave.mzpad.common.Log;
import android.widget.Toast;

public class CommunicateServer {

	private static String TAG = "CommunicateServer";

	/**
	 * 处理前端数据返回消息
	 */
	private Handler mHandler;

	/**
	 * 当前服务连接状态
	 */
	private int currStatus = -1;

	/**
	 * 上下文
	 */
	private Activity context;

	/**
	 * 连接状态 开始，暂停，停止
	 */
	public static final int START = 1, PAUSE = 2, STOP = 3,CONNECTED = 4;

	/**
	 * 连接对象
	 */
	private static CommunicateServer commServer;

	/**
	 * USB通讯对象
	 */
	private static UsbManager mUsbManager;

	/**
	 * USB 串口对象
	 */
	private static UsbSerialPort sPort = null;

	/**
	 * 串口传输对象
	 */
	private SerialInputOutputManager mSerialIoManager;

	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	
	private HeartBeatThread heartBeatThread = null ;

	private final SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {

		@Override
		public void onRunError(Exception e) {
			Log.d(TAG, "Runner stopped.");
		}

		@Override
		public void onNewData(final byte[] data) {
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String result = new String(data);
					Log.i(TAG, "返回数据 data:"+result);
					String[] checkResult = checkData(result);
					if(Utility.isEmpty(checkResult)){
						Log.i(TAG, "返回数据验证失败, data:"+checkResult);
					   	return ;
					}
					int cmdFirst = Integer.parseInt(checkResult[1].replace("0x", ""));
					Log.i(TAG, "cmdFirst:"+cmdFirst);
					switch (cmdFirst) {
						case Contants.COMMAND_ASK :
							int cmdSecond = Integer.parseInt(checkResult[2].replace("0x", ""));
							Log.i(TAG, "接收成功：Command:"+cmdSecond);
							switch (cmdSecond) {
								case Contants.COMMAND_HEARTBEAT:
									//处理心跳方法
									if(heartBeatThread!=null){
										heartBeatThread.setStartTime(System.currentTimeMillis());
									}else{
										heartBeatThread = new HeartBeatThread(commServer, mHandler);
										heartBeatThread.start();
									}
									break;
								case Contants.COMMAND_IMMEDIATELY:
									mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "立即测量接收成功"));
									break;
								case Contants.COMMAND_PAUSE:
									mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "已暂停……"));
									break;
								case Contants.COMMAND_SAMPLE_INTERVAL:
									break;
								case Contants.COMMAND_START:
									mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "已开始……"));
									break;
								case Contants.COMMAND_STOP:
									mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "已停止……"));
									break;
								default :
									break;
							}
							break;
						case Contants.COMMAND_UPLOAD_DATA://出来返回方法
							int length = checkResult.length ;
							String[] desData = new String[length-4];
							System.arraycopy(checkResult, 2, desData, 0, length-4);//去掉开头，命令码，0xff和校验码
							mHandler.sendMessage(mHandler.obtainMessage(Contants.REQUEST_GETDATA, desData));
							break;
						default :
							break;
					}
				}
			});
		}
	};

	private CommunicateServer(Activity ctx, Handler handler) {
		this.context = ctx;
		this.mHandler = handler;
		if(sPort == null){
			initSerialDevice(ctx,mHandler);
		}
		startIoManager();	
	}

	public static CommunicateServer getInstance(Activity ctx, Handler handler) {
		if (commServer == null) {
			commServer = new CommunicateServer(ctx, handler);
		}else{
			commServer.context = ctx ;
			commServer.mHandler = handler;
		}
		return commServer;
	}

	public static void initSerialDevice(Context ctx,Handler handler){
		Log.i(TAG, "initSerialDevice starting");
		mUsbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);
		 List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
		 List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
		for ( UsbSerialDriver driver : drivers) {
		    List<UsbSerialPort> ports = driver.getPorts();
			Log.d(TAG, String.format("+ %s: %s port%s", driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
			result.addAll(ports);
		}
		if (Utility.isEmpty(result)) {
			Log.d(TAG, "can not serial device!");
			return ;
		}
		sPort = result.get(0);
		UsbDeviceConnection connection = mUsbManager.openDevice(sPort.getDriver().getDevice());
		if (connection == null) {
			Log.d(TAG, "Opening device failed");
			handler.sendMessage(handler.obtainMessage(Contants.TOAST_MSG, "串口连接失败，请检查设备,或者重新打开应用"));
			return ;
		}
		try {
			sPort.open(connection);
			sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
			Log.e(TAG, "Serial device: " + sPort.getClass().getSimpleName());
			handler.sendMessage(handler.obtainMessage(Contants.SHOW_MSG, "连接成功！"));
			return ;
		} catch (IOException e) {
			Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
			try {
				sPort.close();
			} catch (IOException e2) {
				Log.e(TAG, "Error setting up device: " + e2.getMessage(), e);
			}
			sPort = null;
		}
		Log.i(TAG, "initSerialDevice end");
	}
	
	public void resetSerialDevice(){
		stopIoManager();
		sPort = null;
	}
	
	/**
	 * 启动连接
	 * @return
	 */
	public void sendStart() {
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_START));
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		if(sendCommand(cmd)){
			mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "连接成功！"));	
		}
		if(!Utility.isEmpty(heartBeatThread)){
			heartBeatThread.startTime();
		}
		//发送采样间隔
		if(Utility.isEmpty(MParamDetailsFragment.measureParam)){
			mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "当前设置参数为空!"));
			Log.e(TAG, "当前设置参数为空!");
			return;
		}
		if(MParamDetailsFragment.measureParam.getSampleInterval()<=0){
			mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "采样间隔为空!"));
			Log.e(TAG, "采样间隔为空!");
			return;
		}
		String cmdSample = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_SAMPLE_INTERVAL)+","+MParamDetailsFragment.measureParam.getSampleInterval());
		cmdSample = cmdSample.replace("%S", Utility.getCheckCodeHexByString(cmdSample));
		sendCommand(cmdSample);
	}

	/**
	 *暂停连接
	 * @return
	 */
	public void sendPause() {
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_PAUSE));
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
		if(!Utility.isEmpty(heartBeatThread)){
			heartBeatThread.stopTime();
		}
	}

	/**
	 * 停止连接
	 * @return
	 */
	public void sendStop() {
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_STOP));
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
		if(!Utility.isEmpty(heartBeatThread)){
			heartBeatThread.stopTime();
		}
	}
	
	/**
	 * 立即测量
	 * @return
	 */
	public void sendImmediately() {
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_IMMEDIATELY));
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
	}
	
	/**
	 * 发送心跳数据
	 * @return
	 */
	public void sendHeartBeat() {
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_HEARTBEAT));
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
	}

	/**
	 * 发送命令
	 * @param command
	 */
	public boolean sendCommand(String command) {
		if (Utility.isEmpty(command)) {
			Log.e(TAG, "send command is null");
			return false;
		}
		if(Utility.isEmpty(sPort)){
			Log.e(TAG, "sPort is null");
			initSerialDevice(context,mHandler);
			return false;
		}
		if(Utility.isEmpty(mSerialIoManager)){
			startIoManager();
			return false;
		}
		try {
			Log.e(TAG, "send command Start command:"+command);
			mSerialIoManager.writeAsync(command.getBytes());
			Log.e(TAG, "send command End");
		} catch (Exception e) {
			Log.e(TAG, "send command Exception:" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 获取当前状态
	 * @return
	 */
	public int getCurrStatus() {
		return currStatus;
	}

	public void stopIoManager() {
		if (mSerialIoManager != null) {
			Log.i(TAG, "Stopping io manager ..");
			mSerialIoManager.stop();
			mSerialIoManager = null;
		}
	}

	public void startIoManager() {
		if (sPort != null) {
			Log.i(TAG, "Starting io manager ..");
			mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
			mExecutor.submit(mSerialIoManager);
			heartBeatThread = new HeartBeatThread(this, mHandler);
			heartBeatThread.start() ;
		}
	}
	
	/**
	 * 验证数据是否争取
	 */
	public String[] checkData(String result){
		if(Utility.isEmpty(result)){
			Log.i(TAG, "checkData result is null");
			return null;
		}
		if(!result.startsWith("0x55") || !result.endsWith("0xaa")){
			Log.i(TAG, "checkData result is incorrect start & end");
			return null;
		}
		
		int endIndex = result.indexOf("0xaa");
		String data = result.substring(0, endIndex);
		Log.i(TAG, "data"+data);
		String[] dataArray =  data.split(",");
		int length = dataArray.length ;
		if(length<3){
			Log.i(TAG, "checkData result is incorrect middle");
			return null;
		}
		
		if(!dataArray[length-2].equals("0xff")){
			Log.i(TAG, "checkData result is incorrect middle not ff charator ");	
		}
		//校验码
		String checkStr = data.substring(0, data.indexOf("0xff"));
		int sumData = 0 ;
		char[] tempChars = checkStr.toCharArray();
		for(int i=0;i<tempChars.length;i++){
			sumData = sumData + tempChars[i];
		}
		int checkValue = Integer.parseInt(dataArray[length-1].replace("0x", ""),16);
		Log.i(TAG, "sumData:"+sumData  + " checkValue:"+checkValue);
		if((byte)sumData != (byte)checkValue){
			Log.i(TAG, "checkData result is incorrect the sumData ");
			sendNoASK(dataArray[1]);
			return null;
		}
		sendASK(dataArray[1]);
		return dataArray ;
	}
	
	/**
	 * 发送验证正确回复命令
	 * @param data
	 */
	public void sendASK(String data){
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S", Utility.toHexString(Contants.COMMAND_ASK)+","+data);
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
	}
	
	/**
	 * 发送验证错误回复命令
	 * @param data
	 */
	public void sendNoASK(String data){
		String cmd = Contants.COMMAND_FORMAT.replaceFirst("%S",  Utility.toHexString(Contants.COMMAND_NASK)+","+data);
		cmd = cmd.replace("%S", Utility.getCheckCodeHexByString(cmd));
		sendCommand(cmd);
	}
	
}
