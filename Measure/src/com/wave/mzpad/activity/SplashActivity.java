package com.wave.mzpad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wave.mzpad.R;
import com.wave.mzpad.service.BusinessDataBase;
import com.wave.mzpad.service.DataUtils;

public class SplashActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 100;
	
	private Handler mHandler = new Handler();
	
	private BusinessDataBase businessDataBase ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		businessDataBase = new BusinessDataBase(this);
		//第一次启动初始化数据库
		if(DataUtils.isFirst(this)){
			businessDataBase.initData();
			DataUtils.setfirst(this);
		}
		mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGHT);
	}
	
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			Intent intent = new Intent(SplashActivity.this,MainFragment.class);
			startActivity(intent);
			finish();
		}
	};
	
}
