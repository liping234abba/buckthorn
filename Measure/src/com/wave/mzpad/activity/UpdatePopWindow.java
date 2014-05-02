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
import com.wave.mzpad.model.MeasureResult;

public class UpdatePopWindow {

	private Context ctx;

	private PopupWindow updateWindow;

	private EditText et_rh,et_pd,et_ph;
	
	private MeasureResultAdapter mra ;
	
	private MeasureResult mr;
	
	public UpdatePopWindow(Context ctx,MeasureResultAdapter mra) {
		this.ctx = ctx;
		this.mra = mra ;
	}

	public void ShowPopWindow(View parent,MeasureResult tempMr) {
		if(Utility.isEmpty(tempMr)){
			return ;
		}
		this.mr = tempMr ;
		if (updateWindow == null) {
			View view = View.inflate(ctx, R.layout.edit_result, null);
			updateWindow = new PopupWindow(view, 500, 400);
			updateWindow.setFocusable(true);
			updateWindow.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.bg_popup_window));
			updateWindow.setOutsideTouchable(true);
			et_rh = (EditText)view.findViewById(R.id.et_rainshed_high);
			et_pd = (EditText)view.findViewById(R.id.et_platform_distance);
			et_ph = (EditText)view.findViewById(R.id.et_platform_high);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateWindow.dismiss();
				}
			});
			Button saveBtn = (Button)view.findViewById(R.id.save_data);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mr.setRainshedHigh(Integer.parseInt(et_rh.getText().toString()));
					mr.setPlatformDistance(Integer.parseInt(et_pd.getText().toString()));
					mr.setPlatformHigh(Integer.parseInt(et_ph.getText().toString()));
					mra.updateMeasureResult(mr);
					updateWindow.dismiss();
				}
			});
		}else{
			et_rh = (EditText) updateWindow.getContentView().findViewById(R.id.et_rainshed_high);
			et_pd = (EditText) updateWindow.getContentView().findViewById(R.id.et_platform_distance);
			et_ph = (EditText) updateWindow.getContentView().findViewById(R.id.et_platform_high);
		}
		et_rh.setText(tempMr.getRainshedHigh()+"");
		et_pd.setText(tempMr.getPlatformDistance()+"");
		et_ph.setText(tempMr.getPlatformHigh()+"");
		updateWindow.showAtLocation(parent, Gravity.CENTER, 0, 5);
	}
}