package com.wave.mzpad.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.wave.mzpad.model.AbstractObject;
import com.wave.mzpad.model.CellElement;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.JxlExcelUtil.OperateExcel;

public class ServiceExportReport implements OperateExcel {

	private String TAG = "ServiceExportReport";
	
	/**
	 * 测量参数对象
	 */
	private MeasureParam abstractObject ;
	
	/**
	 * 输出结果对象集合
	 */
	
	private List<AbstractObject> listAbstractObject ;
	
	/**
	 * 导出Excel表格对象
	 */
	private JxlExcelUtil excelUtil ;
	
	//写入开始值Point(x,y) 和第某列最大值
	private int startX = 6,startY = 13 ,colMax = 16;
	
	private BusinessDataBase businessDataBase ;
	
	
	public ServiceExportReport(Context ctx ,AbstractObject abstractObject ,List<AbstractObject> listObject){
		businessDataBase = new BusinessDataBase(ctx);
		excelUtil = new JxlExcelUtil(ctx);
		excelUtil.setOperateExcel(this);
		this.abstractObject = (MeasureParam) abstractObject ;
		this.listAbstractObject = listObject ;
	}
	
	public void exportExcel(){
		new Thread(){
			public void run() {
				String prefix = "" ;
				if(abstractObject instanceof MeasureParam){
					prefix = ((MeasureParam)abstractObject).getLineName() + "-" + ((MeasureParam)abstractObject).getLineNumber() ;
				}
				String fileName = "测量结果-" + prefix + new DateFormat().format("yyyy-MM-dd-hhmmss", new Date()) + ".xls";
				String desFile = Environment.getExternalStorageDirectory() + File.separator + fileName ;
				try {
				  excelUtil.copyAndupdateExcel(null, desFile);
				} catch (IOException e) {
					Log.i(TAG, "exportExcel :Exception" + e.getMessage());
				}	
			};
		}.start();
	}
	
	@Override
	public void updateExcel(WritableWorkbook wwb) {
		try {
			int resultSize = listAbstractObject.size() ; 
				 for(int index = 0 ; index<resultSize ; index++){
					 if(index>31){
						 break;
					 }
						//测量点序号
					 if(index <colMax){//当结果15时， 根据报表中可以看出
						 int pointY = startY + index ;
						 int pointX = startX ;
						 insertColumnArray(wwb, index, pointY, pointX); 
					 }else{
						 int pointY = (startY-1) + (index - colMax);
						 int pointX = startX + 7 ;
						 insertColumnArray(wwb, index, pointY, pointX);
					 }
				 }
		} catch (IOException e) {
			Log.i(TAG, "updateExcel IOException :" + e.getMessage());
		} catch (RowsExceededException e) {
			Log.i(TAG, "updateExcel RowsExceededException :" + e.getMessage());
		} catch (WriteException e) {
			Log.i(TAG, "updateExcel WriteException :" + e.getMessage());
		}
	}

	/**
	 * 向Excel表中插入n行
	 * @param wwb Excel流程
	 * @param index:
	 * @param pointY
	 * @param pointX
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void insertColumnArray(WritableWorkbook wwb, int index, int pointY,
			int pointX) throws IOException, RowsExceededException,
			WriteException {
		MeasureResult measureResult;
		measureResult = (MeasureResult) listAbstractObject.get(index);
		String cellValue = index + "" ;
		insertExcelLabel(wwb, pointY, pointX, cellValue);
		//测量点
		cellValue = measureResult.getTravelDistance() +  "" ;
		insertExcelLabel(wwb, pointY, pointX + 1, cellValue);
		//外轨超高
		cellValue = abstractObject.getOuterrailHigh() +  "" ;
		insertExcelLabel(wwb, pointY, pointX + 3, cellValue);
		//距轨顶面断面测量高度
		cellValue = measureResult.getPlatformHigh() +  "" ;
		insertExcelLabel(wwb, pointY, pointX + 4, cellValue);
		//距轨中心线距离
		cellValue = measureResult.getPlatformDistance() +  "" ;
		insertExcelLabel(wwb, pointY, pointX + 5, cellValue);
		//超限值
		int result =  0 ;
		 try{
			 result = businessDataBase.calLimitValue(measureResult, abstractObject) ; 
		 }catch(Exception epx){
			 Log.i(TAG, "updateExcel 计算限制失败 epx"+ epx.getMessage());
		 }
		 cellValue = result+ "";
		 insertExcelLabel(wwb, pointY, startX + 6, cellValue);
	}

	/**
	 * 向Excel表中插入数据
	 * @param wwb：excel表中读取权限
	 * @param pointY：Excel表中Label对应的Y坐标
	 * @param pointX:Excel表中Label对应的X坐标
	 * @param cellValue:将要写入Excel表中的值
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void insertExcelLabel(WritableWorkbook wwb, int pointY, int pointX,
			String cellValue) throws IOException, RowsExceededException,
			WriteException {
		CellElement cell = new CellElement(pointX, pointY, cellValue);
		excelUtil.updateLabelCell(wwb, cell);
	}
}
