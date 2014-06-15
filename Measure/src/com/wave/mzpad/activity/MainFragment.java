
package com.wave.mzpad.activity;

import java.util.Iterator;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.wave.mzpad.R;
import com.wave.mzpad.common.Utility;

public class MainFragment extends FragmentActivity {

	public static BluetoothDevice blueDeviceAddr = null ;
	
	private String TAG = MainFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
		Set<BluetoothDevice> setBTD = BluetoothAdapter.getDefaultAdapter().getBondedDevices() ;
		if(!Utility.isEmpty(setBTD)){
			Iterator<BluetoothDevice> iterator = setBTD.iterator() ;
			if(!Utility.isEmpty(iterator)){
				try {
					blueDeviceAddr = iterator.next() ;
				} catch (Exception e) {
					Log.i(TAG, "exception exp:"+e.getMessage()) ;
				}
			}
		}else{
			Log.i(TAG, "blueDeviceAddr is null ") ;
		}
		
	}
}
