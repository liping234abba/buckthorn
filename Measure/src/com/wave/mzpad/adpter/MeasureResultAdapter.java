package com.wave.mzpad.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import com.wave.mzpad.common.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wave.mzpad.R;
import com.wave.mzpad.activity.MParamDetailsFragment;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.model.StandardData;
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
		   MeasureResult mr = getItem(arg0);
           if (convertView == null) {
        	   hv = new HodlerView();
        	   convertView = mInflater.inflate(R.layout.measure_result_item, null);
        	   hv.travelDistance = (TextView) convertView.findViewById(R.id.travel_distance);
        	   hv.platformHigh = (TextView) convertView.findViewById(R.id.platform_high);
        	   hv.platformDistance = (TextView) convertView.findViewById(R.id.platform_distance);
        	   hv.dipAngle = (TextView) convertView.findViewById(R.id.dip_angle);
        	   hv.rainshedHigh = (TextView) convertView.findViewById(R.id.rainshed_high);
        	   hv.outlimited = (TextView) convertView.findViewById(R.id.outlimited);
        	   hv.outlimitValue = (TextView) convertView.findViewById(R.id.outlimit_value);
        	   convertView.setTag(hv);
           }else{
        	   hv = (HodlerView) convertView.getTag();
           }
           StandardData standardData = getStandardData(mr);
           hv.travelDistance.setText(mr.getTravelDistance()/1000+"");
           hv.platformHigh.setText(mr.getPlatformHigh()+"");
           hv.platformDistance.setText(mr.getPlatformDistance()+"");
           hv.dipAngle.setText(mr.getDipAngle()+"");
           hv.rainshedHigh.setText(mr.getRainshedHigh()+"");
           measureParam = MParamDetailsFragment.measureParam ;
           if(!Utility.isEmpty(measureParam) && measureParam.getRadius()>0 && !Utility.isEmpty(standardData)){
        	   Log.i(TAG, "MeasureResultAdapter:"+measureParam.toString());
        	   int[] result = businessDataBase.calWarningLevelLimited(standardData, mr, measureParam) ;
        	   hv.outlimitValue.setText(result[1]+"");
               if(result[0]>0){
            	   hv.outlimited.setText("(%S)".replace("%S", result[0]>1?"严重超限":"一般超限"));
            	   hv.outlimited.setTextSize(20);
            	   convertView.setBackgroundColor(Color.RED);
               }else{
            	   hv.outlimited.setText("否");
               }  
           }
		return convertView;
	}

    class HodlerView {
        TextView travelDistance;
        TextView platformHigh;
        TextView platformDistance;
        TextView dipAngle;
        TextView rainshedHigh;
        TextView outlimited;//是否超限
        TextView outlimitValue;//超限值
    }
    
	private StandardData getStandardData(MeasureResult measureResult) {
		int height = measureResult.getPlatformHigh();// 把月台高度与数据库中什么进行比较
		height = height / 10 * 10;
		if (height <= 150) {
			height = 150;
		} else if (height >= 1240) {
			height = 1240;
		}
		String sql = " where " + StandardData.COLUMN_TRACK_HIGH + "=" + height;
		return businessDataBase.getStandardDataDAO().getStandardData(sql).get(0);
	}
	
	public void updateData(MeasureResult mr){
		if(!Utility.isEmpty(mr)){
			Log.i("wave", "updateData");
			mrLists.add(0, mr);
			notifyDataSetChanged();
		}
	}

	public List<MeasureResult> getMrLists() {
		return mrLists;
	}
	
}
