package com.wave.mzpad.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wave.mzpad.R;
import com.wave.mzpad.adpter.MeasureResultAdapter;
import com.wave.mzpad.common.Contants;
import com.wave.mzpad.common.Log;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;
import com.wave.mzpad.service.CommunicateServer;
import com.wave.mzpad.service.ServiceExportReport;

public class MParamDetailsFragment extends Fragment {

	//站名、设备名称,设备编号,换算面积,面向车站,
	private EditText stand_name,stand_id,stand_area,stand_direction;
		
	//设备方位,曲线方向
	private Spinner stand_orientation,bight_direction;
	
	//是否高铁
	private CheckBox rail_high;
	
	// 线路编号,线路名称,半径(m),外轨超高(mm),采样间隔(m),测试起始位置;
	private EditText line_number, line_name, radius, outerrail_high, sample_interval, measure_startposition;
	
	// 是否正线,是否内侧
	private CheckBox track, inner_side;

	// 开始,暂停,停止,保存数据
	private Button conn_start, conn_pause, conn_stop, save_data,immediately_measure,export_excel;

	public static MeasureParam measureParam; // 输入参数

	private String TAG = "MParamDetailsFragment";

	private BusinessDataBase businessDataBase;

	private Activity mActivity;
	
	private CommunicateServer server ;
	
	private TextView showMsg ;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Contants.REQUEST_SUCCESSED:
				Log.i(TAG, "REQUEST_SUCCESSED");
				sendMessage(obtainMessage(Contants.TOAST_MSG, "连接成功"));
				sendMessage(obtainMessage(Contants.SHOW_MSG, "连接成功！"));
				server.startIoManager();
				break;
			case Contants.REQUEST_FAILED:
				sendMessage(obtainMessage(Contants.TOAST_MSG, "连接失败，请检查设备!"));
				sendMessage(obtainMessage(Contants.SHOW_MSG, "连接失败！"));
				server.resetSerialDevice();
				break;
			case Contants.REQUEST_GETDATA:
				String[] data = (String[])msg.obj ;
				dealwithMeasureResult(data);
				break;
			case Contants.SHOW_MSG:
				showMsg.setText((String)msg.obj);
				break;	
			case Contants.TOAST_MSG:
				Toast.makeText(mActivity, (String)msg.obj+"", Toast.LENGTH_SHORT).show();
				break;	
			case Contants.SHOW_MSG_CHANGE:
				boolean show = (Boolean)msg.obj ;
				initImmediatelyStatus(show);
				break;	
			default:
				break;
			}
		};
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		businessDataBase = new BusinessDataBase(mActivity);
	}
	
    @Override
    public void onStart() {
    	super.onStart();
    	Log.i(TAG, " Details onStart");
    	server = CommunicateServer.getInstance(mActivity, mHandler);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public static MParamDetailsFragment newInstance(MeasureParam mp) {
		MParamDetailsFragment f = new MParamDetailsFragment();	
		measureParam = mp;
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", mp.getId());
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mr_detail, null);
		initWidget(view);// 初始化控件
		setTextValueByMeasureParam(measureParam);//初始化详细信息
		bindEvent(); // 绑定事件
		initMeasureResult();//初始化Listview
		return view;
	}

	private void bindEvent() {
		conn_start.setOnClickListener(clickListener);
		conn_pause.setOnClickListener(clickListener);
		conn_stop.setOnClickListener(clickListener);
		save_data.setOnClickListener(clickListener);
		immediately_measure.setOnClickListener(clickListener);
		export_excel.setOnClickListener(clickListener);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showDialog(arg2);
				return false;
			}
		});
	}

	private void initWidget(View view) {
		line_number = (EditText) view.findViewById(R.id.line_number);
		line_name = (EditText) view.findViewById(R.id.line_name);
		radius = (EditText) view.findViewById(R.id.radius);
		outerrail_high = (EditText) view.findViewById(R.id.outerrail_high);
		sample_interval = (EditText) view.findViewById(R.id.sample_interval);
		measure_startposition = (EditText) view.findViewById(R.id.measure_startposition);
		track = (CheckBox) view.findViewById(R.id.track);
		inner_side = (CheckBox) view.findViewById(R.id.inner_side);

		stand_name = (EditText) view.findViewById(R.id.stand_name);
		stand_id = (EditText) view.findViewById(R.id.stand_id);
		stand_area = (EditText) view.findViewById(R.id.stand_area);
		stand_direction = (EditText) view.findViewById(R.id.stand_direction);
		rail_high = (CheckBox) view.findViewById(R.id.rail_high);
		stand_orientation = (Spinner) view.findViewById(R.id.stand_orientation);
		bight_direction = (Spinner)view.findViewById(R.id.bight_direction);
		
		conn_start = (Button) view.findViewById(R.id.conn_start);
		conn_pause = (Button) view.findViewById(R.id.conn_pause);
		conn_stop = (Button) view.findViewById(R.id.conn_stop);
		save_data = (Button) view.findViewById(R.id.save_data);
		immediately_measure = (Button)view.findViewById(R.id.immediately_measure);
		export_excel = (Button)view.findViewById(R.id.export_excel);
		showMsg = (TextView)view.findViewById(R.id.show_msg);
		listView = (ListView) view.findViewById(android.R.id.list);
		initImmediatelyStatus(false);
	}

	private void setTextValueByMeasureParam(MeasureParam _mp) {
		if (Utility.isEmpty(_mp)) {
			return;
		}
		if(_mp.getId()>0){
			line_name.setText(_mp.getLineName());	
		}
		line_number.setText(_mp.getLineNumber());
		radius.setText(_mp.getRadius() + "");
		outerrail_high.setText(_mp.getOuterrailHigh() + "");
		sample_interval.setText(_mp.getSampleInterval() + "");
		measure_startposition.setText(_mp.getMeasureStartposition());
		track.setChecked(_mp.getTrack() > 0 ? true : false);
		inner_side.setChecked(_mp.getInnerSide() > 0 ? true : false);
		
		stand_name.setText(_mp.getStandName());
		stand_id.setText(_mp.getStandId());
		stand_area.setText(_mp.getStandArea());
		stand_direction.setText(_mp.getStandDirection());
		rail_high.setChecked(_mp.getRailHigh() > 0 ? true : false);
		stand_orientation.setSelection(_mp.getStandOrientation());
		bight_direction.setSelection(_mp.getBightDirection());
	}

	private void setMeasureParam() {
		Log.i(TAG, "linenumber:" + line_number.getText().toString() + "radius:" + track.isChecked() + "inner_side:" + inner_side.isChecked());
		if (Utility.isEmpty(measureParam)) {
			measureParam = new MeasureParam();
		}
		measureParam.setLineNumber(line_number.getText().toString());
		measureParam.setLineName(line_name.getText().toString());
		measureParam.setRadius(Integer.parseInt(radius.getText().toString()));
		measureParam.setOuterrailHigh(Integer.parseInt(outerrail_high.getText().toString()));
		measureParam.setSampleInterval(Integer.parseInt(sample_interval.getText().toString()));
		measureParam.setMeasureStartposition(measure_startposition.getText().toString());
		measureParam.setTrack(track.isChecked() == true ? 1 : 0);
		measureParam.setInnerSide(inner_side.isChecked() == true ? 1 : 0);
		
		measureParam.setStandArea(stand_area.getText().toString());
		measureParam.setStandDirection(stand_direction.getText().toString());
		measureParam.setStandId(stand_id.getText().toString());
		measureParam.setStandName(stand_name.getText().toString());
		measureParam.setRailHigh(rail_high.isChecked() == true?1:0);
		measureParam.setStandOrientation(stand_orientation.getSelectedItemPosition());
		measureParam.setBightDirection(bight_direction.getSelectedItemPosition());
	}

	private void saveMeasureParam() {
		if (!validateForm()) {
			return;
		}
		setMeasureParam();
		boolean isExist = measureParam.getId()>0?true:false ;
		if (isExist) {
			boolean result = false;
			result = businessDataBase.getMeasureParadmDao().updateMeasureParam(measureParam);
			if (result) {
				Log.i(TAG, "更新成功");
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "更新成功"));
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "更新失败"));
			}
		} else {
			MeasureParam result = businessDataBase.getMeasureParadmDao().insertMeasureParam(measureParam);
			if (!Utility.isEmpty(result)) {
				measureParam = result ;
				Log.i(TAG, "添加成功");
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "添加成功"));
				Intent intent = new Intent(MParamListFragment.UPDATE_LIST);
				mActivity.sendBroadcast(intent);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "添加失败"));
			}
		}
	}

	private boolean validateForm() {

		if (Utility.isEmpty(line_number.getText().toString())) {
			sendMessage(Contants.TOAST_MSG,"股道号不能为空！");
			return false;
		}

		if (Utility.isEmpty(line_name.getText().toString())) {
			sendMessage(Contants.TOAST_MSG,"线路名称不能为空！");
			return false;
		}
		return true;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.save_data :
					saveMeasureParam();
					break;
				case R.id.conn_start :
					if(!Utility.isEmpty(measureParam) && measureParam.getId()>0){
						server.sendStart();	
					}else{
						sendMessage(Contants.SHOW_MSG, "请保存当前数据，再开始!");
					}
					break;
				case R.id.conn_pause :
					server.sendPause();
					break;
				case R.id.conn_stop :
					server.sendStop();
					break;
				case R.id.immediately_measure :
					if(Utility.isFastDoubleClick(2000)){
						sendMessage(Contants.TOAST_MSG, "两秒以内不能重复点击立即测量！");
						return;
					}
					server.sendImmediately();
					break;
				case R.id.export_excel:
					exportExcel();
				default :
					break;
			}
		}
	};

	private ListView listView;

	private MeasureResultAdapter measureResultAdapter;
	
	private List<MeasureResult> results = new ArrayList<MeasureResult>();

	private void initMeasureResult() {
		results = businessDataBase.getMeasureResult(measureParam.getId()) ;
		measureResultAdapter = new MeasureResultAdapter(mActivity, results);
		listView.setAdapter(measureResultAdapter);
	}

	/**
	 * 处理前端返回数据
	 * @param data
	 */
	private void dealwithMeasureResult(String[] data){
		Log.i(TAG, "dealwithMeasureResult: data[]"+data);
		if(measureParam.getId()<0){
			sendMessage(Contants.SHOW_MSG, "请先保存当前记录！");
			return ;
		}
		if(measureParam.getRadius()<0){
			sendMessage(Contants.SHOW_MSG, "设置参数不合法，请检查半径值！");
			return ;
		}
		MeasureResult _result = new MeasureResult() ;
		_result.setParamId(measureParam.getId());
		//返回数据顺序：行进距离,、月台高度、月台距离、倾角、雨棚高度
		try{
			
			_result.setTravelDistance(Utility.getTravelDistance(Utility.strToInt(data[0])));
			_result.setPlatformHigh(Utility.strToInt(data[1]));
			_result.setPlatformDistance(Utility.strToInt(data[2]));
			_result.setDipAngle(Utility.strToInt(data[3]));
			_result.setRainshedHigh(Utility.strToInt(data[4]));
			int limitValue = Math.round(businessDataBase.calDiffResultValue(_result, measureParam)) ;
			_result.setLimitDefault(limitValue);
			_result.setLimitUpdate(limitValue);
			_result = businessDataBase.getMeasureResultDao().insertMeasureResult(_result);
			//转换失败保存listview中
			int measurePoint  =  (Utility.strToInt(data[0])/1000) ;
			sendMessage(Contants.SHOW_MSG, "测量点: "+measurePoint+" 接收成功");
			Log.i(TAG, "results size():"+ results.size() + ",_result:"+_result);
			measureResultAdapter.updateData(_result);
		}catch(Exception exp){
			Log.i(TAG, "整型转换出错  :data:"+ exp.getMessage());	
			sendMessage(Contants.TOAST_MSG, "整型转换出错!");
		}
		/*if(Utility.isEmpty(_result)){
			Log.i(TAG, "插入失败  :data:"+ data);
		}else{
		    Log.i(TAG, "results size():"+ results.size() + ",_result:"+_result);
		    measureResultAdapter.updateData(_result);
		}
		int measurePoint = 0;
		try{
		  measurePoint  =  (Utility.strToInt(data[0])/1000) ;
	    }catch(Exception exp){
		  Log.i(TAG, "整型转换出错  :data:"+ exp.getMessage());	
		  sendMessage(Contants.TOAST_MSG, "整型转换出错");
	    }
		sendMessage(Contants.SHOW_MSG, "测量点: "+measurePoint+" 接收成功");*/
	}
	
	private void exportExcel(){
		new Thread(){
			public void run() {
				if(Utility.isEmpty(measureParam) || Utility.isEmpty(measureParam.getLineName())){
					sendMessage(Contants.TOAST_MSG, "输入参数为空！");
					return;
				}
				List<MeasureResult> listResults = measureResultAdapter.getMrLists() ;
				if(Utility.isEmpty(listResults)){
					sendMessage(Contants.TOAST_MSG,"没有将要导出的数据！");
					return;
				}
				//导出Excel
				sendMessage(Contants.SHOW_MSG, "正在导出Excel数据……");
				Collections.reverse(listResults);//反序列
				ServiceExportReport exportReport = new ServiceExportReport(mActivity, measureParam,listResults);
				exportReport.exportExcel();	
				sendMessage(Contants.SHOW_MSG, "导出数据完成！");
			};
		}.start();
		
	}
	
	/**
	 * 向Handler里面发送数据
	 * @param what:发送种类
	 * @param text：发送内容
	 */
	public void sendMessage(int what,String text){
		mHandler.sendMessage(mHandler.obtainMessage(what, text));
	}
	
	@Override
	public void onPause() {
		if(!Utility.isEmpty(server) && server.getCurrStatus() == CommunicateServer.START){
			server.sendStop();
		}
		super.onPause();
	}
	
	private void initImmediatelyStatus(boolean isShow){
		immediately_measure.setEnabled(isShow);
	}
	
	protected void showDialog(final int mpIndex) {
		  AlertDialog.Builder builder = new Builder(mActivity);
		  builder.setMessage("确认要删除吗？");
		  builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			List<MeasureResult> mrList =  measureResultAdapter.getMrLists();
			businessDataBase.getMeasureResultDao().deleteMeasureResult(mrList.get(mpIndex));
			mrList.remove(mpIndex);
			measureResultAdapter.updateData(mrList);
			measureResultAdapter.notifyDataSetChanged();
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
	
}
