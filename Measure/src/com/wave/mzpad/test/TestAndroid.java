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
	}

	public void testSelect(){
		businessDataBase.initData();
		System.out.println();
	}
	
	public void testMeasureParam(){
		SQLiteHelper.getInstance(getContext());
		
		MeasureParam measureParam = new MeasureParam() ;
		measureParam.setInnerSide(0);
		measureParam.setLineName("线路名称");
		measureParam.setLineNumber(1);
		measureParam.setMeasureStartposition(20);
		measureParam.setRadius(500);
		measureParam.setSampleInterval(20);
		measureParam.setTrack(0);
		measureParam.setOuterrailHigh(20);
		businessDataBase.getMeasureParadmDao().insertMeasureParam(measureParam);
	}
	
	public void testMeasureResult(){
		MeasureResult measureResult = new MeasureResult() ;
		for(int i = 0 ; i < 20 ; i++){
			measureResult.setParamId(2);
			measureResult.setPlatformDistance(1745);
			measureResult.setPlatformHigh(310);
			measureResult.setRainshedHigh(30);
			measureResult.setTravelDistance(100);
			measureResult.setDipAngle(0);
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
		measureParam.setOuterrailHigh(75);
		measureParam.setRadius(1020);
		measureParam.setInnerSide(0);
		measureParam.setTrack(0);
		measureParam.setMeasureStartposition(20);
		measureParam.setSampleInterval(2);
		measureParam.setLineNumber(1);
		measureParam.setLineName("怀化");
		
		MeasureResult measureResult = new MeasureResult();
		measureResult.setDipAngle(0);
		measureResult.setParamId(1);
		measureResult.setPlatformDistance(1745);
		measureResult.setPlatformHigh(310);
		measureResult.setRainshedHigh(30);
		measureResult.setTravelDistance(100);
		int[] result = businessDataBase.calWarningLevelLimited(measureResult, measureParam);
		Log.i(TAG, "result :0"+ result[0] + " 1:"+result[1]);
	}
	
}
