package com.wave.mzpad.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.wave.mzpad.R;
import com.wave.mzpad.adpter.MeasureResultAdapter;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;

public class UpdatePopWindow {

	private Context ctx;

	private PopupWindow updateWindow;

	private EditText et_pd,et_ph,et_limit;
	
	private MeasureResultAdapter mra ;
	
	private MeasureResult mr;
	
	private MeasureParam mp;
	
	private BusinessDataBase dataBase ;
	
	public UpdatePopWindow(Context ctx,MeasureResultAdapter mra,MeasureParam mp,BusinessDataBase dataBase) {
		this.ctx = ctx;
		this.mra = mra ;
		this.mp = mp ;
		this.dataBase = dataBase;
	}

	public void ShowPopWindow(View parent,MeasureResult tempMr) {
		if(Utility.isEmpty(tempMr)){
			return ;
		}
		this.mr = tempMr ;
		if (updateWindow == null) {
			View view = View.inflate(ctx, R.layout.edit_result, null);
			updateWindow = new PopupWindow(view, 500, 430);
			updateWindow.setFocusable(true);
			updateWindow.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.bg_popup_window));
			updateWindow.setOutsideTouchable(false);
			et_pd = (EditText)view.findViewById(R.id.et_platform_distance);
			et_ph = (EditText)view.findViewById(R.id.et_platform_high);
			et_limit = (EditText)view.findViewById(R.id.et_platform_limit);
			Button saveBtn = (Button)view.findViewById(R.id.save_data);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String pd = Utility.isEmpty(et_pd.getText().toString())?"0":et_pd.getText().toString(); 
					mr.setPlatformDistance(Integer.parseInt(pd));
					String ph = Utility.isEmpty(et_ph.getText().toString())?"0":et_ph.getText().toString() ;
					mr.setPlatformHigh(Integer.parseInt(ph));
				    int calLimitValue = Math.round(dataBase.calDiffResultValue(mr, mp));
				    mr.setLimitDefault(calLimitValue);
				    String limit = Utility.isEmpty(et_limit.getText().toString())?"0":et_limit.getText().toString();
				    int limitEditValue = Integer.parseInt(limit) ;
				    if(limitEditValue == mr.getLimitUpdate()){
				      mr.setLimitUpdate(calLimitValue);
				    }else{
				      mr.setLimitUpdate(limitEditValue);
				    }
					mra.updateMeasureResult(mr);
					updateWindow.dismiss();
				}
			});
		}else{
			et_pd = (EditText) updateWindow.getContentView().findViewById(R.id.et_platform_distance);
			et_ph = (EditText) updateWindow.getContentView().findViewById(R.id.et_platform_high);
			et_limit = (EditText)updateWindow.getContentView().findViewById(R.id.et_platform_limit);
		}
		et_pd.setText(tempMr.getPlatformDistance()+"");
		et_ph.setText(tempMr.getPlatformHigh()+"");
		et_limit.setText(tempMr.getLimitUpdate()+"");
		updateWindow.showAtLocation(parent, Gravity.CENTER, 0, 5);
	}
}