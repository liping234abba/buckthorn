package com.example.lifemyth;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MainActivity extends Activity {
	
	private Button check_update ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		check_update = (Button)findViewById(R.id.check_update);
		UmengUpdateAgent.update(this);
		check_update.setOnClickListener(listener);
	}
	
	
	private View.OnClickListener listener = new View.OnClickListener() {
		public void onClick(View v) {
			// ������������ʱ�Զ�����Ƿ���Ҫ���£� ���������д������Activity ��onCreate()�����
			com.umeng.common.Log.LOG = true;
			
			UmengUpdateAgent.setUpdateOnlyWifi(false); // Ŀǰ����Ĭ����Wi-Fi��������²Ž����Զ����ѡ�����Ҫ���������绷���½��и����Զ����ѣ�������Ӹ��д���
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(updateListener);
			UmengUpdateAgent.setDownloadListener(new UmengDownloadListener(){

			    @Override
			    public void OnDownloadStart() {
			        Toast.makeText(getApplicationContext(), "download start" , Toast.LENGTH_SHORT).show();
			    }

			    @Override
			    public void OnDownloadUpdate(int progress) {
			        Toast.makeText(getApplicationContext(), "download progress : " + progress + "%" , Toast.LENGTH_SHORT).show();
			    }

			    @Override
			    public void OnDownloadEnd(int result, String file) {
			        //Toast.makeText(getApplicationContext(), "download result : " + result , Toast.LENGTH_SHORT).show();
			        Toast.makeText(getApplicationContext(), "download file path : " + file , Toast.LENGTH_SHORT).show();
			    }           
			});
			UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			    @Override
			    public void onClick(int status) {
			        switch (status) {
			        case UpdateStatus.Update:
			            Toast.makeText(getApplicationContext(), "User chooses update." , Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.Ignore:
			            Toast.makeText(getApplicationContext(), "User chooses ignore." , Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.NotNow:
			            Toast.makeText(getApplicationContext(), "User chooses cancel." , Toast.LENGTH_SHORT).show();
			            break;
			        }
			        return ;
			    }
			});
			UmengUpdateAgent.forceUpdate(getApplicationContext());
		}
	};

	
	UmengUpdateListener updateListener = new UmengUpdateListener() {
		@Override
		public void onUpdateReturned(int updateStatus,
				UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
				UmengUpdateAgent.showUpdateDialog(getApplicationContext(), updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(getApplicationContext(), "û�и���", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2: // none wifi
				Toast.makeText(getApplicationContext(), "û��wifi���ӣ� ֻ��wifi�¸���", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3: // time out
				Toast.makeText(getApplicationContext(), "��ʱ", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4: // is updating
				/*Toast.makeText(getApplicationContext(), "�������ظ���...", Toast.LENGTH_SHORT)
						.show();*/
				break;
			}

		}
	};
	
	
}
