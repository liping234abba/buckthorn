package com.wave.mzpad.adpter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wave.mzpad.R;
import com.wave.mzpad.activity.MParamDetailsFragment;
import com.wave.mzpad.activity.MainFragment;
import com.wave.mzpad.common.Log;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;

public class MeasureResultAdapter extends BaseAdapter {
	
	private Context context ;
	
	private List<MeasureResult>  mrLists = new ArrayList<MeasureResult>();
	
	private LayoutInflater mInflater;
	
	private BusinessDataBase businessDataBase ;
	
	private MeasureParam measureParam;

	private String TAG="MeasureResultAdapter";
	
	public MeasureResultAdapter(Context ctx,List<MeasureResult> results){
		this.context = ctx ;
		this.mrLists = results ;
		this.mInflater = LayoutInflater.from(context);
		businessDataBase = new BusinessDataBase(ctx);
	}

	@Override
	public int getCount() {
		return mrLists.size();
	}

	@Override
	public MeasureResult getItem(int arg0) {
		return mrLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return mrLists.get(arg0).getId();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		   HodlerView hv = null;
		   final MeasureResult mr = getItem(arg0);
           if (convertView == null) {
        	   hv = new HodlerView();
        	   convertView = mInflater.inflate(R.layout.measure_result_item, null);
        	   hv.travelDistance = (TextView) convertView.findViewById(R.id.travel_distance);
        	   hv.platformHigh = (TextView) convertView.findViewById(R.id.platform_high);
        	   hv.platformDistance = (TextView) convertView.findViewById(R.id.platform_distance);
        	   hv.rainshedHigh = (TextView) convertView.findViewById(R.id.rainshed_high);
        	   hv.outlimited = (TextView) convertView.findViewById(R.id.outlimited);
        	   hv.outlimitValue = (TextView) convertView.findViewById(R.id.outlimit_value);
        	   convertView.setTag(hv);
           }else{
        	   hv = (HodlerView) convertView.getTag();
           }
           hv.travelDistance.setText("第"+mr.getTravelDistance()/1000+"米");
           hv.platformHigh.setText(mr.getPlatformHigh()+"");
           hv.platformDistance.setText(mr.getPlatformDistance()+"");
           hv.rainshedHigh.setText(mr.getRainshedHigh()+"");
           measureParam = MParamDetailsFragment.measureParam ;
           if(!Utility.isEmpty(measureParam)){
        	   Log.i(TAG, "MeasureResultAdapter:"+measureParam.toString());
        	   int[] result = businessDataBase.calWarningLevelLimited(mr, measureParam) ;
        	   hv.outlimitValue.setText(result[1]+"");
               if(result[0]>0){
            	   hv.outlimited.setText("(%S)".replace("%S", result[0]>1?"严重超限":"一般超限"));
            	   hv.outlimited.setTextSize(20);
            	   convertView.setBackgroundColor(result[0]>1?Color.RED:Color.YELLOW);  
               }else{
            	   hv.outlimited.setText("否");
            	   convertView.setBackgroundColor(Color.WHITE);
               }  
           }
           hv.platformDistance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUpdateDialog(mr);
			}
		  });
		return convertView;
	}
	
    protected void showUpdateDialog(final MeasureResult mr) {
    	  AlertDialog.Builder builder = new Builder(context);
		  builder.setMessage("确认要修改吗？");
//		  builder.setView(view);
		  builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });
		  builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });
		  builder.create().show();
	}

	class HodlerView {
        TextView travelDistance;
        TextView platformHigh;
        TextView platformDistance;
        TextView rainshedHigh;
        TextView outlimited;//是否超限
        TextView outlimitValue;//超限值
    }
    
	public void updateData(MeasureResult mr){
		if(!Utility.isEmpty(mr)){
			Log.i("wave", "updateData");
			mrLists.add(0, mr);
			notifyDataSetChanged();
		}
	}
	
	public void updateData(List<MeasureResult> mrList){
		if(!Utility.isEmpty(mrList)){
			Log.i("wave", "updateData");
			this.mrLists = mrList;
			notifyDataSetChanged();
		}
	}


	public List<MeasureResult> getMrLists() {
		return mrLists;
	}
	
}
