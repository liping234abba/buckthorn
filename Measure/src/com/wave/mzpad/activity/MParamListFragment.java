package com.wave.mzpad.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.wave.mzpad.R;
import com.wave.mzpad.adpter.ParamListAdapter;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.service.BusinessDataBase;

public class MParamListFragment extends ListFragment implements OnItemLongClickListener {

	int mCurCheckPosition = 0;
	int mShownCheckPosition = -1;
	private ParamListAdapter paramListAapter ;
	private List<MeasureParam> mpLists = new ArrayList<MeasureParam>();
	
	private BusinessDataBase businessDataBase ;
	
	private Context context ;
	
	public static String UPDATE_LIST="com.wave.mzpad.updatelist" ;
	
	private UpdateListBroadcastReceiver updateListBroadcastReceiver = new UpdateListBroadcastReceiver();
	
	private String TAG = "MParamListFragment";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity() ;
		businessDataBase = new BusinessDataBase(context);
		context.registerReceiver(updateListBroadcastReceiver, new IntentFilter(UPDATE_LIST));
		initMeasureParamList();
		paramListAapter = new ParamListAdapter(context,mpLists);
		setListAdapter(paramListAapter);
		getListView().setOnItemLongClickListener(this);
		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
			mShownCheckPosition = savedInstanceState.getInt("shownChoice", -1);
		}
		showDetails(mCurCheckPosition);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_list, container,false);
		return view ;
	}
	@Override
	public void onDestroy() {
		context.unregisterReceiver(updateListBroadcastReceiver);
		super.onDestroy();
	}
	

	private void initMeasureParamList() {
		mpLists.clear();
		mpLists.add(getAddMeasureParam());
		List<MeasureParam> measureParams = businessDataBase.getMeasureParadmDao().getMeasureParam("");
		if(!Utility.isEmpty(measureParams)){
			mpLists.addAll(measureParams);
		}
	}
	
	private MeasureParam getAddMeasureParam(){
		MeasureParam measureParam = new MeasureParam();
		measureParam.setId(-1);
		measureParam.setInnerSide(0);
		measureParam.setLineName("新建");
		measureParam.setLineNumber(1);
		measureParam.setMeasureStartposition(1);
		measureParam.setOuterrailHigh(1);
		measureParam.setRadius(1);
		measureParam.setSampleInterval(1);
		measureParam.setTrack(0);
		return measureParam ;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
		outState.putInt("shownChoice", mShownCheckPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position);
	}
	
	

	/**
	  *显示listview item 详情
	  */
	void showDetails(int index) {
		mCurCheckPosition = index;
		if (mShownCheckPosition != mCurCheckPosition) {
			@SuppressWarnings("unchecked")
			MeasureParam measureParam = (MeasureParam) getListAdapter().getItem(index);
			MParamDetailsFragment df = MParamDetailsFragment.newInstance(measureParam);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.paramdetails, df);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
			mShownCheckPosition = index;
		}
	}



	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.i(TAG, "onItemLongClick + id" + arg2) ;
		if(arg2>0){
			showDialog(arg2);
		}
		return false;
	}
	
	protected void showDialog(final int mpIndex) {
		  AlertDialog.Builder builder = new Builder(context);
		  builder.setMessage("确认要删除吗？");
		  builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			businessDataBase.getMeasureParadmDao().deleteMeasureParam(mpLists.get(mpIndex));
			mpLists.remove(mpIndex);
			paramListAapter.notifyDataSetChanged();
		    dialog.dismiss();
		    showDetails(mpIndex-1);
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
	
   class UpdateListBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction() ;
			if(UPDATE_LIST.equals(action)){
				initMeasureParamList();
				paramListAapter.notifyDataSetChanged();
			}
		}
		
	}
}
