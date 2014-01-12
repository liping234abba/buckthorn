package com.wave.mzpad.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wave.mzpad.R;
import com.wave.mzpad.model.MeasureParam;

public class ParamListAdapter extends BaseAdapter {
	private Context context ;
	private List<MeasureParam> listMP = new ArrayList<MeasureParam>();
	public ParamListAdapter(Context ctx,List<MeasureParam> listMP){
		context = ctx ;
		this.listMP = listMP;
	}
	
	  @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  HodlerView mHodlerView = null;
	      MeasureParam _mparam = listMP.get(position);
	      if(convertView == null){
	    	  mHodlerView = new HodlerView();
	    	  convertView = View.inflate(context, R.layout.fragment_mr_listitem, null);
	    	  mHodlerView.showName = (TextView) convertView.findViewById(R.id.show_name);
	    	  convertView.setTag(mHodlerView);
	      }else{
	    	  mHodlerView = (HodlerView) convertView.getTag();
	      }
	      String showContent = _mparam.getLineName();
	      if(_mparam.getId()>=0){
	    	  showContent = showContent +"-"+_mparam.getLineNumber() ;
	      }
	      mHodlerView.showName.setText(showContent); 
		return convertView;
	}



	class HodlerView {
	        TextView showName;
	    }

	@Override
	public int getCount() {
		return listMP.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listMP.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return listMP.get(position).getId();
	}
	
}
