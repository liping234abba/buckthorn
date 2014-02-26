package com.wave.mzpad.service;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import android.os.Environment;

import com.wave.mzpad.common.Log;
import com.wave.mzpad.common.Utility;

public class JxlExcelUtil {

	private String TAG = "JxlExcelUtil";

	public JxlExcelUtil() {

	}

   /**
	 *  创建或打开Excel文件
	 * filePath Excel文件路径
	 */
	public void createExcel(String filePath, String sheetName) {
		if (Utility.isEmpty(filePath)) {
			filePath = Environment.getExternalStorageDirectory()
					+ File.separator + "test.xlsx";
		}
		try {
			
			WritableWorkbook book = Workbook.createWorkbook(new File(filePath));
			if (Utility.isEmpty(sheetName)) {
				sheetName = "第一页";
			}
			book.createSheet(sheetName, 0);
			book.write();
			book.close();
		} catch (Exception e) {
			Log.i(TAG , "createExcel:"+e.getMessage());
		}
	}

	/**
	 *  把 模版文件拷贝在修改
	 * @param srcfilePath :原文件路径
	 * @param desfilePath：目标文件路径
	 */
	public void updateExcel(String srcfilePath, String desfilePath) {
		if(Utility.isEmpty(srcfilePath) || Utility.isEmpty(desfilePath)){
			Log.i(TAG , "源文件名称或者目标文件名称不能为空!");
			return ; 
		}
		try {
			Workbook rwb = Workbook.getWorkbook(new File(srcfilePath));
			WritableWorkbook wwb = Workbook.createWorkbook(
					new File(desfilePath), rwb);// copy
		  /*WritableSheet ws = wwb.getSheet(0);
			WritableCell wc = ws.getWritableCell(0, 0);
			// 判断单元格的类型,做出相应的转换
			Label label = (Label) wc;
			label.setString("The value has been modified");
			wwb.write();
			wwb.close();*/
			operateExcel.updateExcel(wwb); //修改Excel文件内容
			rwb.close();
		} catch (Exception e) {
			Log.i(TAG, "Update Excel:"+ e.getMessage());
		}
	}

	/**
	 * 更新Excel接口对象
	 */
	private OperateExcel operateExcel ;
	
	/**
	 * 
	 * @param updateExcel
	 */
	public void setOperateExcel(OperateExcel operateExcel) {
		this.operateExcel = operateExcel;
	}

	interface OperateExcel{
		public void updateExcel(WritableWorkbook wwb);
	}
	
}
