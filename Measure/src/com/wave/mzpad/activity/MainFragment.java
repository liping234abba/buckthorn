package com.wave.mzpad.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wave.mzpad.R;
import com.wave.mzpad.service.BusinessDataBase;
import com.wave.mzpad.service.DataUtils;

public class MainFragment extends FragmentActivity {

	private BusinessDataBase businessDataBase ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
		businessDataBase = new BusinessDataBase(this);
		//第一次启动初始化数据库
		if(DataUtils.isFirst(this)){
			businessDataBase.initData();
			DataUtils.setfirst(this);
		}
	}

}
