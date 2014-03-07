package com.wave.mzpad.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.wave.mzpad.common.Contants;
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
	private int startX = 6,startY = 13 ,colMax = 15;
	
	private BusinessDataBase businessDataBase ;
	
	
	public ServiceExportReport(Context ctx ,AbstractObject abstractObject ,List<?> listObject){
		businessDataBase = new BusinessDataBase(ctx);
		excelUtil = new JxlExcelUtil(ctx);
		excelUtil.setOperateExcel(this);
		this.abstractObject = (MeasureParam) abstractObject ;
		this.listAbstractObject = (List<AbstractObject>) listObject ;
	}
	
	public void exportExcel(){
		String prefix = "" ;
		if(abstractObject instanceof MeasureParam){
			prefix = ((MeasureParam)abstractObject).getLineName() + "-" + ((MeasureParam)abstractObject).getLineNumber() ;
		}
		String fileName = new DateFormat().format("yyyy-MM-dd", new Date()) +"("+ prefix + ").xls" ; // new DateFormat().format("yyyy-MM-dd-hhmmss", new Date()) + ".xls";
		String desFile = Environment.getExternalStorageDirectory() + File.separator + Contants.EXPORT_EXCEL_FILEPATH ;
		File desFileDir = new File(desFile);
		if(!desFileDir.exists()){
			desFileDir.mkdirs();
		}
		desFile = desFile + File.separator + fileName ;
		try {
		  excelUtil.copyAndupdateExcel(null, desFile);
		} catch (IOException e) {
			Log.i(TAG, "exportExcel :Exception" + e.getMessage());
		}	
	}
	
	@Override
	public void updateExcel(WritableSheet ws) {
		try {
			int resultSize = listAbstractObject.size() ; 
				 for(int index = 0 ; index<resultSize ; index++){
					 if(index>30){
						 break;
					 }
						//测量点序号
					 if(index <colMax){//当结果15时， 根据报表中可以看出
						 int pointY = startY + index ;
						 int pointX = startX ;
						 insertColumnArray(ws, index, pointY, pointX); 
					 }else{
						 int pointY = (startY-1) + (index - colMax);
						 int pointX = startX + 7 ;
						 insertColumnArray(ws, index, pointY, pointX);
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
	 * @param ws Excel流程
	 * @param index:
	 * @param pointY
	 * @param pointX
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void insertColumnArray(WritableSheet ws, int index, int pointY,
			int pointX) throws IOException, RowsExceededException,
			WriteException {
		MeasureResult measureResult;
		measureResult = (MeasureResult) listAbstractObject.get(index);
		//测量序列号
		String cellValue = index + "" ;
		insertExcelLabel(ws, pointY, pointX, cellValue);
		//测量点
		cellValue = measureResult.getTravelDistance() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 1, cellValue);
		//外轨超高
		cellValue = abstractObject.getOuterrailHigh() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 3, cellValue);
		//距轨顶面断面测量高度
		cellValue = measureResult.getPlatformHigh() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 4, cellValue);
		//距轨中心线距
		cellValue = measureResult.getPlatformDistance() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 5, cellValue);
		//超限值
		int result =  0 ;
		try{
			result = businessDataBase.calWarningLevelLimited(measureResult, (MeasureParam)abstractObject)[1] ; 
		}catch(Exception epx){
			Log.i(TAG, "updateExcel 计算限制失败 epx"+ epx.getMessage());
		}
		 cellValue = result+ "";
		 insertExcelLabel(ws, pointY, pointX + 6, cellValue);
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
	private void insertExcelLabel(WritableSheet ws, int pointY, int pointX,
			String cellValue) throws IOException, RowsExceededException,
			WriteException {
		CellElement cell = new CellElement(pointX, pointY, cellValue);
		excelUtil.updateLabelCell(ws, cell);
	}
}
