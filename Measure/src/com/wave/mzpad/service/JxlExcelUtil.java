package com.wave.mzpad.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Environment;

import com.wave.mzpad.common.Log;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.CellElement;

public class JxlExcelUtil {

	private String TAG = "JxlExcelUtil";

	private Context ctx;

	
	public JxlExcelUtil() {
		
	}
	
	public JxlExcelUtil(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Excel操作对象接口
	 * 
	 */
	public interface OperateExcel {
		public void updateExcel(WritableSheet ws);
	}

	/**
	 * 更新Excel接口对象
	 */
	private OperateExcel operateExcel;

	/**
	 * 
	 * @param updateExcel
	 */
	public void setOperateExcel(OperateExcel operateExcel) {
		this.operateExcel = operateExcel;
	}

	/**
	 * 创建或打开Excel文件 filePath Excel文件路径
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
			Log.i(TAG, "createExcel:" + e.getMessage());
		}
	}

	/**
	 * 把 模版文件拷贝在修改
	 * 
	 * @param srcfilePath
	 *            :原文件路径
	 * @param desfilePath
	 *            ：目标文件路径
	 * @throws IOException
	 */
	public void copyAndupdateExcel(String srcfilePath,String fileName, String desfilePath)
			throws IOException {
		if (Utility.isEmpty(desfilePath)) {
			Log.i(TAG, "目标文件名称不能为空!");
			return;
		}
		// 获取源文件流
		InputStream inputStream = null;
		if (Utility.isEmpty(srcfilePath)) {
			Log.i(TAG, "源文件名称为空!");
			inputStream = ctx.getAssets().open(fileName);
		} else {
			inputStream = new FileInputStream(new File(srcfilePath));
		}
		
		/*拷贝Excel文件
		FileOutputStream fileOutputStream = new FileOutputStream(desfilePath);
		byte[] buffer = new byte[1024];
		int length = 0 ;
		while(  (length = inputStream.read(buffer) ) != -1){
			fileOutputStream.write(buffer,0,length);
		}
		fileOutputStream.flush();
		fileOutputStream.close();
		inputStream.close();*/
		try {
			Workbook rwb = Workbook.getWorkbook(inputStream);
			WritableWorkbook wwb = Workbook.createWorkbook(new File(desfilePath), rwb);// copy
			operateExcel.updateExcel(wwb.getSheet(0)); //修改Excel文件内容
			wwb.write();
			wwb.close();
			rwb.close();
		} catch (Exception e) {
			Log.i(TAG, "copyAndupdateExcel:" + e.getMessage());
		}
	}

	/**
	 * 更新 Excel元素对象
	 * 
	 * @param wwb
	 * @throws IOException
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public void updateLabelCell(WritableSheet ws, CellElement cell)
			throws IOException, RowsExceededException, WriteException {
		WritableCell wc = ws.getWritableCell(cell.x, cell.y); // 判断单元格的类型,做出相应的转换
		Log.i(TAG, "wc : cell.x"+cell.x + " cell.y:"+cell.y + "type:"+ wc.getType());
		if(wc.getType() == CellType.EMPTY){
		   WritableCellFormat wcfF = new WritableCellFormat();
		   wcfF.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
		   wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		   wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		   wc = new Label(cell.x, cell.y, cell.value); 
		   wc.setCellFormat(wcfF);
		}else if(wc.getType() == CellType.LABEL){
		  ((Label)wc).setString(cell.value);
		}
		ws.addCell(wc);
	}

	/**
	 * 关闭 WritableWorkbook 对象
	 * 
	 * @param wwb
	 * @throws IOException
	 * @throws WriteException
	 */
	public void colseWritableWorkbook(WritableWorkbook wwb)
			throws WriteException, IOException {
		if (!Utility.isEmpty(wwb)) {
			wwb.close();
		}
	}
}
