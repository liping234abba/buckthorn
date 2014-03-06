package com.wave.mzpad.common;

public class Contants {

	//命令种类  开始（1）和停止（2）, 暂停（3），采样间隔（4）， 前端上传数据（5），答（6）
	public static enum Command{START,STOP,PAUSE,SAMPLE_INTERVAL,UPLOAD_DATA,ASK} ;
	
	public final static int REQUEST_TIMEOUT = 5000; // 请求 检查超时时间
	
	public final static int REPEAT_TIME = 3; //连续发送次数
	
	public final static int REQUEST_FAILED= 0 ;//请求失败
	
	public final static int REQUEST_SUCCESSED = 1 ;//请求成功
	
	public final static int REQUEST_GETDATA = 4; //发送请求返回数据
	
	public final static int SHOW_MSG = 5; //显示消息
	
	public final static int TOAST_MSG = 6; //Toast显示消息
	
	public final static int PAGE_MAXSIZE = 20 ;//一页显示条数
	
	public final static byte COMMAND_START = 0x01 ;//开始测量命令
	
	public final static byte COMMAND_PAUSE =0x02 ;//暂停测量命令
	
	public final static byte COMMAND_STOP =0x03 ;//停止测量命令
	
	public final static byte COMMAND_SAMPLE_INTERVAL =0x04 ;//采样间隔命令
	
	public final static byte COMMAND_UPLOAD_DATA =0x05 ;//前端上传数据命令
	
	public final static byte COMMAND_ASK =0x06 ;//回答命令
	
	public final static byte COMMAND_IMMEDIATELY =0x07 ;//立即测量命令
	
	public final static byte COMMAND_HEARTBEAT =0x08 ;//心跳测试命令
	
	public final static byte COMMAND_NASK =0x09 ;//校验失败回答NAK
	
    public static String COMMAND_FORMAT = "0x55,%S,0xff,%S,0xaa"; //命令格式
    
    public static String EXPORT_EXCEL_FILEPATH = "wave" ;
	
}
