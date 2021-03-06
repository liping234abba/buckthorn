package com.wave.mzpad.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wave.mzpad.R;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;
import com.wave.mzpad.service.ServiceExportReport;

public class ReportActivity extends Activity implements View.OnClickListener{

	private EditText dir_path ;//导出文件夹路径
	
	private Button export ; //导出ExcelButton
	
	private int PICK_REQUEST_CODE = 100; //选择文件

	private String TAG = "ReportActivity";
	
	private ServiceExportReport serviceReport ;
	
	private BusinessDataBase businessDataBase;
	
	private int paramIndex ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		paramIndex = 1;
		businessDataBase = new BusinessDataBase(getApplicationContext());
		setContentView(R.layout.activity_report);
		dir_path = (EditText)findViewById(R.id.dir_path);
		export = (Button)findViewById(R.id.export);
		dir_path.setOnClickListener(this);
		export.setOnClickListener(this);
	}
	
	/**
	 * 选择文件夹路径
	 */
	private void selectDirPath(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		Uri startDir = Uri.fromFile(new File("sdcard"));
		intent.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.directory");
		intent.putExtra("explorer_title", "选择文件夹");
		intent.putExtra("browser_filter_extension_whitelist", "*"); 
		intent.putExtra("browser_title_background_color", "440000AA");
		intent.putExtra("browser_title_foreground_color", "FFFFFFFF");
		intent.putExtra("browser_list_background_color", "00000066");
		intent.putExtra("browser_list_fontscale", "100%");
		intent.putExtra("browser_list_layout", "2");
		startActivityForResult(intent, PICK_REQUEST_CODE);
	}
	
	/**
	 * 转出Excel
	 */
	private void exportExcel() {
		new Thread(){
			public void run() {
				String sql = " where id=" + paramIndex;
				MeasureParam measureParam  = businessDataBase.getMeasureParadmDao().getMeasureParam(sql).get(0) ;
				if(Utility.isEmpty(measureParam)){
					Log.i(TAG, "measureParam is null ");
					return;
				}
				Log.i(TAG, "measureParam:" + measureParam.toString());
				List<MeasureResult> listMeasureResult = new ArrayList<MeasureResult>();
				listMeasureResult = businessDataBase.getMeasureResult(measureParam.getId()) ;
				Log.i(TAG, "measureResult:" + listMeasureResult.size()) ;
				if(Utility.isEmpty(listMeasureResult)){
					Log.i(TAG, "measureResult:" + listMeasureResult.size()) ;	
					return;
				}
				serviceReport = new ServiceExportReport(ReportActivity.this, measureParam, listMeasureResult);
				serviceReport.exportExcel();
			};
		}.start();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dir_path:
			selectDirPath();
			break;
		case R.id.export:
			exportExcel();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PICK_REQUEST_CODE){
			if(Utility.isEmpty(data)){
				Log.i(TAG, "onActivityResult: data 为空!");
				return;
			}
			Uri selectFile = data.getData();
			if(Utility.isEmpty(selectFile)){
				Log.i(TAG, "onActivityResult: selectFile 为空!");
				return;
			}
			String tempStr = selectFile.toString() ;
			if(Utility.isEmpty(tempStr)){
				Log.i(TAG, "onActivityResult: tempStr 为空 !");
				return;
			}
			tempStr = tempStr.replace("file://", "");
			dir_path.setText(tempStr);
		}
	}

	/**
	 * 输入参数
	 */
	private MeasureParam measureParam ; 
	
	/**
	 * 读取报表数据
	 * @return
	 */
	private List<MeasureResult> getReportData(){
		String paramSql =  "" ;
	    measureParam  =	businessDataBase.getMeasureParadmDao().getMeasureParam(paramSql).get(0) ;
	    if(Utility.isEmpty(measureParam)){
	    	return new ArrayList<MeasureResult>();
	    }
	    return businessDataBase.getMeasureResult(measureParam.getId()) ;
	}
	
}
