package com.yaokan.sdk;

import java.util.List;

import com.yaokan.sdk.aidl.IRemoteService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class YaokanSDKService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("YaokanSDKService onBind()");
		return new ServerBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("YaokanSDKService onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		System.out.println("YaokanSDKService onCreate()");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		System.out.println("YaokanSDKService onDestroy()");
		super.onDestroy();
	}
	
	private  class ServerBinder extends IRemoteService.Stub {
		@Override
		public void callEPG(String areaId, String provider)
				throws RemoteException {
		}
	}
	
}
