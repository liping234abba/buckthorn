package com.wave.mzpad.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.wave.mzpad.common.Log;
import com.wave.mzpad.db.SQLiteHelper;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;

public class TestAndroid extends AndroidTestCase {
	BusinessDataBase businessDataBase ;
	
	private String TAG = "TestAndroid" ;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		businessDataBase = new BusinessDataBase(getContext());
//		businessDataBase.initData();
	}

	public void testSelect(){
		businessDataBase.initData();
		System.out.println();
	}
	
	public void testMeasureParam(){
		SQLiteHelper.getInstance(getContext());
		
		MeasureParam measureParam = new MeasureParam() ;
		measureParam.setOuterrailHigh(45);
		measureParam.setRadius(1000);
		measureParam.setInnerSide(1);
		measureParam.setTrack(1);
		measureParam.setMeasureStartposition(20+"");
		measureParam.setSampleInterval(2);
		measureParam.setLineNumber(1+"");
		measureParam.setLineName("怀化");
		businessDataBase.getMeasureParadmDao().insertMeasureParam(measureParam);
	}
	
	public void testMeasureResult(){
		MeasureResult measureResult = new MeasureResult() ;
		for(int i = 0 ; i < 20 ; i++){
			measureResult.setDipAngle(0);
			measureResult.setParamId(1);
			measureResult.setTravelDistance(i*1000);
			measureResult.setPlatformDistance(1155);
			measureResult.setPlatformHigh(1798);
			measureResult.setRainshedHigh(30);
			measureResult.setTravelDistance(100);
			businessDataBase.getMeasureResultDao().insertMeasureResult(measureResult);
		}
	}
	
	public void testMeasureParamResult(){
		MeasureParam measureParam  = businessDataBase.getMeasureParadmDao().getMeasureParam("").get(0) ;
		Log.i(TAG, "measureParam:" + measureParam.toString());
		List<MeasureResult> listMeasureResult = new ArrayList<MeasureResult>();
		if(measureParam != null){
			listMeasureResult = businessDataBase.getMeasureResult(measureParam.getId()) ;
			Log.i(TAG, "measureResult:" + listMeasureResult.size()) ;
		}
	}
	
	public void testcurvlReportData(){
		MeasureParam measureParam = new MeasureParam() ;
		measureParam.setOuterrailHigh(45);
		measureParam.setRadius(1000);
		measureParam.setInnerSide(1);
		measureParam.setTrack(1);
		measureParam.setMeasureStartposition(20+"");
		measureParam.setSampleInterval(2);
		measureParam.setLineNumber(1+"");
		measureParam.setLineName("怀化");
		
		MeasureResult measureResult = new MeasureResult();
		measureResult.setDipAngle(0);
		measureResult.setParamId(1);
		measureResult.setPlatformDistance(1155);
		measureResult.setPlatformHigh(1798);
		measureResult.setRainshedHigh(30);
		measureResult.setTravelDistance(100);
		int[] result = businessDataBase.calWarningLevelLimited(measureResult, measureParam);
		Log.i(TAG, "result :0"+ result[0] + " 1:"+result[1]);
	}
	
}
