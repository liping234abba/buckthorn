package com.yaokan.devices;
/*
 * 此文件为JNI接口
 * */
public class wavir {

	static wavir instance;

	// cmd 为命令码值: 相邻两次码之间的发送，需要间隔半秒钟(500ms以上)
	private native int transmitIR(String cmd);

	static {
		// load library
		System.loadLibrary("iryaokanzte");
	}

	// Test command
	public void IRTest(String _cmd) {
		transmitIR(_cmd);
	}

	public static wavir getInstance() {
		if (instance == null) {
			instance = new wavir();
		}
		return instance;
	}
}