package com.wave.mzpad.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.wave.mzpad.common.Contants;
import com.wave.mzpad.common.Utility;
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
	private int startX = 0,startY = 7 ,colMax = 20;
	
	private BusinessDataBase businessDataBase ;
	
	
	public ServiceExportReport(Context ctx ,AbstractObject abstractObject ,List<?> listObject){
		businessDataBase = new BusinessDataBase(ctx);
		excelUtil = new JxlExcelUtil(ctx);
		excelUtil.setOperateExcel(this);
		this.abstractObject = (MeasureParam) abstractObject ;
		this.listAbstractObject = (List<AbstractObject>) listObject ;
	}
	
	public void exportExcel(){
		String prefix = abstractObject.getLineName() + "-" + abstractObject.getLineNumber() ;
		String fileName = new DateFormat().format("yyyy-MM-dd", new Date()) +"("+ prefix + ").xls" ; // new DateFormat().format("yyyy-MM-dd-hhmmss", new Date()) + ".xls";
		String desFile = Environment.getExternalStorageDirectory() + File.separator + Contants.EXPORT_EXCEL_FILEPATH ;
		File desFileDir = new File(desFile);
		if(!desFileDir.exists()){
			desFileDir.mkdirs();
		}
		desFile = desFile + File.separator + fileName ;
//		String srcfileName = abstractObject.getRadius()>0?"template_curviline.xls":"template_stratline.xls";
		String srcfileName = "template.xls";
		try {
		  excelUtil.copyAndupdateExcel(null,srcfileName, desFile);
		} catch (IOException e) {
			Log.i(TAG, "exportExcel :Exception" + e.getMessage());
		}	
	}
	
	@Override
	public void updateExcel(WritableSheet ws) {
		try {
			insertMeasureParam(ws);
			int resultSize = listAbstractObject.size() ; 
			 for(int index = 0 ; index<resultSize ; index++){
				 if(index>2*colMax){
					 break;
				 }
					//测量点序号
				 if(index <colMax){//当结果15时， 根据报表中可以看出
					 int pointY = startY + index ;
					 int pointX = startX ;
					 insertColumnArray(ws, index, pointY, pointX); 
				 }else{
					 int pointY = (startY-1) + (index - colMax);
					 int pointX = startX + 5 ;
					 insertColumnArray(ws, index, pointY, pointX);
				 }
			 }
		} catch (IOException e) {
			Log.e(TAG, "updateExcel IOException :" + e.getMessage());
		} catch (RowsExceededException e) {
			Log.e(TAG, "updateExcel RowsExceededException :" + e.getMessage());
		} catch (WriteException e) {
			Log.e(TAG, "updateExcel WriteException :" + e.getMessage());
		}
	}

	/**
	 * 插入表头数据
	 * @param ws
	 */
	private void insertMeasureParam(WritableSheet ws) {
		// TODO Auto-generated method stub
		if(Utility.isEmpty(abstractObject)){
			Log.e(TAG, "insertMeasureParam object is null ");
			return;
		}
		try {
			String cellValue = "";//填入值
			int pointX = 0,pointY = 0 ;//需填入的Cell坐标
			//站名、设备名称
			cellValue = abstractObject.getStandName();
			pointY = 2 ;pointX = 2;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			ws.mergeCells(2, 2, 5, 2);
			//设备编号
			cellValue = abstractObject.getStandId();
			pointY = 7 ;pointX = 2;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//换算面积
			cellValue = abstractObject.getStandArea();
			pointY = 10 ;pointX = 2;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			ws.mergeCells(10, 2, 12, 2);
			//线路名称
			cellValue = abstractObject.getLineName();
			pointY = 0 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//中心里程
			cellValue = abstractObject.getMeasureStartposition();
			pointY = 1 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//站内股道号
			cellValue = abstractObject.getLineNumber();
			pointY = 2 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//测量时面向车站
			cellValue = abstractObject.getStandDirection();
			pointY = 3 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//设备方位
			cellValue = abstractObject.getStandOrientation()>0?"线路右侧":"线路左侧";
			pointY = 4 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			ws.mergeCells(4, 4, 5, 4);
			//曲线半径
			cellValue = abstractObject.getRadius()+"";
			pointY = 6 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//外轨超高
			cellValue = abstractObject.getOuterrailHigh()+"";
			pointY = 7 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//曲线方向
			cellValue = abstractObject.getBightDirection()>0?"右侧":"左侧";
			pointY = 8 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			ws.mergeCells(8, 4, 9, 4);
			//曲线内或外侧
			cellValue = abstractObject.getInnerSide()>0?"是":"否";
			pointY = 10 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			//是否正线
			cellValue = abstractObject.getTrack()>0?"是":"否";
			pointY = 11 ;pointX = 4;
			insertExcelLabel(ws, pointY, pointX, cellValue);
			ws.mergeCells(11, 4, 12, 4);
		} catch (RowsExceededException e) {
			Log.e(TAG, "insertMeasureParam :"+ e.getMessage());
		} catch (WriteException e) {
			Log.e(TAG, "insertMeasureParam :"+ e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "insertMeasureParam :"+ e.getMessage());
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
		//曲线内侧加宽量:只有曲线才需要填写
	/*	if(abstractObject.getRadius()>0){
			cellValue = businessDataBase.getCurveWidenValue(abstractObject, measureResult)+  "" ;
			insertExcelLabel(ws, pointY, pointX + 2, cellValue);
		}*/
		
		/*//外轨超高
		cellValue = abstractObject.getOuterrailHigh() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 3, cellValue);*/
		//距轨顶面断面测量高度
		cellValue = measureResult.getPlatformHigh() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 2, cellValue);
		//距轨中心线距
		cellValue = measureResult.getPlatformDistance() +  "" ;
		insertExcelLabel(ws, pointY, pointX + 3, cellValue);
		//超限值
		int result =  0 ;
		try{
			result = businessDataBase.calWarningLevelLimited(measureResult, (MeasureParam)abstractObject)[1] ; 
		}catch(Exception epx){
			Log.e(TAG, "updateExcel 计算限制失败 epx"+ epx.getMessage());
		}
		 cellValue = result+ "";
		 insertExcelLabel(ws, pointY, pointX + 4, cellValue);
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
