
package com.wave.mzpad.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.wave.mzpad.R;
import com.wave.mzpad.service.CommunicateServer;

public class MainFragment extends FragmentActivity {

	private String TAG = MainFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		CommunicateServer.getInstance(this, new Handler()).stopIoManager();
		System.exit(0);
	}
}
