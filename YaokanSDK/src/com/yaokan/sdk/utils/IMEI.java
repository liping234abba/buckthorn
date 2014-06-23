package com.yaokan.sdk.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

public class IMEI {

	private String sID = null;
	
	private final String INSTALLATION = "INSTALLATION";

	public String getIMEI(Context ctx){
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService("phone"); 
		String imei = tm.getDeviceId();
		if( imei == null){
			imei = getId(ctx);
		}
		return imei ;
	}
	
	public  String getId(Context context) {
		if (sID == null) {
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists())
					writeInstallationFile(installation);
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sID;
	}

	private String readInstallationFile(File installation) throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private void writeInstallationFile(File installation) throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		id = id.replace("-", "");
		out.write(id.getBytes());
		out.close();
	}

}
