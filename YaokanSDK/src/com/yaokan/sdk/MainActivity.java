package com.yaokan.sdk;

import com.yaokan.sdk.service.YanKanService;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button test ;
	
	private YanKanService kanRemote ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		test = (Button) findViewById(R.id.test) ;
		kanRemote = new YanKanService(this);
		test.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread(){
					public void run() {
						String json = kanRemote.getPairCodesByAid(291);
						Log.i("wave", "json" +json);
					};
				}.start();
			}
		});
	}
	
}
