package com.wave.mzpad.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wave.mzpad.R;
import com.wave.mzpad.activity.MParamDetailsFragment;
import com.wave.mzpad.activity.UpdatePopWindow;
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
	
	private UpdatePopWindow updatePopWindow;
	
	private int position ;
	
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
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
        	   hv.edit = (Button)convertView.findViewById(R.id.edit);
        	   convertView.setTag(hv);
           }else{
        	   hv = (HodlerView) convertView.getTag();
           }
           hv.travelDistance.setText(Utility.getTravelDistance(mr.getTravelDistance()));
           hv.platformHigh.setText(mr.getPlatformHigh()+"");
           hv.platformDistance.setText(mr.getPlatformDistance()+"");
           hv.rainshedHigh.setText(mr.getRainshedHigh()+"");
           measureParam = MParamDetailsFragment.measureParam ;
           if(!Utility.isEmpty(measureParam)){
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
           hv.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showpopWindow(v,mr);
				position = arg0;
			}
		  });
		return convertView;
	}
	
    protected void showpopWindow(View v,final MeasureResult mr) {
    	  if(Utility.isEmpty(updatePopWindow)){
    		  updatePopWindow = new UpdatePopWindow(context,this);
    	  }
    	  updatePopWindow.ShowPopWindow(v,mr);
	}

	class HodlerView {
        TextView travelDistance;
        TextView platformHigh;
        TextView platformDistance;
        TextView rainshedHigh;
        TextView outlimited;//是否超限
        TextView outlimitValue;//超限值
        Button edit ;//编辑
    }
    
	public void updateData(MeasureResult mr){
		if(!Utility.isEmpty(mr)){
			Log.i("wave", "updateData");
			mrLists.add(0, mr);
			notifyDataSetChanged();
		}
	}
	
	public void updateMeasureResult(MeasureResult mr){
		if(!Utility.isEmpty(mr)){
			String sql = mr.COLUMN_ID + "=" + mr.getId() ;
			businessDataBase.getMeasureResultDao().updateMeasureResult(sql, mr);
			mrLists.set(position, mr);
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
