package com.wave.mzpad.test;

import com.wave.mzpad.service.BusinessDataBase;

import jxl.WorkbookSettings;
import android.test.AndroidTestCase;

public class TestAndroid extends AndroidTestCase {

	public void testSelect(){
		System.out.println();
	}
	
	
	public void testWorkSheet(){
		WorkbookSettings workbook = new WorkbookSettings();
	}
	
	public void testMeasureParam(){
		BusinessDataBase businessDataBase = new BusinessDataBase(getContext());
	}
	
}
