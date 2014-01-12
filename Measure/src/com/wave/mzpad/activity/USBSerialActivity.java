package com.wave.mzpad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class USBSerialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent2 = new Intent(this,MainFragment.class);
		startActivity(intent2);
		Intent intent = new Intent();
		intent.setAction(MParamDetailsFragment.ACTION_CUSTOMIZE_INSERT);
		sendBroadcast(intent);
	}
}
