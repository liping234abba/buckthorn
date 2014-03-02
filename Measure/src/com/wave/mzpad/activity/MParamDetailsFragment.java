package com.wave.mzpad.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import com.wave.mzpad.common.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wave.mzpad.R;
import com.wave.mzpad.adpter.MeasureResultAdapter;
import com.wave.mzpad.common.Contants;
import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.service.BusinessDataBase;
import com.wave.mzpad.service.CommunicateServer;

public class MParamDetailsFragment extends Fragment {

	// 线路编号,线路名称,半径(m),外轨超高(mm),采样间隔(m),测试起始位置;
	private EditText line_number, line_name, radius, outerrail_high, sample_interval, measure_startposition;

	// 是否正线,是否内侧
	private CheckBox track, inner_side;

	// 开始,暂停,停止,保存数据
	private Button conn_start, conn_pause, conn_stop, save_data,immediately_measure;

	public static MeasureParam measureParam; // 输入参数

	private String TAG = "MParamDetailsFragment";

	private BusinessDataBase businessDataBase;

	private Activity mActivity;
	
	private CommunicateServer server ;
	
	private  final String EVENT_USB_INSERT = "intent_action_usb_inserted" ;
	
	private  final String ACTION_USB_PERMISSION = "com.android.hardware.USB_PERMISSION";
	
	public final static String ACTION_CUSTOMIZE_INSERT = "com.android.hardware.CUSTOMIZE";
	
	private USBSerialBroadcastReceiver broadcastReceiver = null ; 
	
	private TextView showMsg ;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Contants.REQUEST_SUCCESSED:
				sendMessage(obtainMessage(Contants.TOAST_MSG, "连接成功"));
				sendMessage(obtainMessage(Contants.SHOW_MSG, "连接成功！"));
				break;
			case Contants.REQUEST_FAILED:
				sendMessage(obtainMessage(Contants.TOAST_MSG, "连接失败，请检查设备!"));
				sendMessage(obtainMessage(Contants.SHOW_MSG, "连接失败！"));
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
		server = CommunicateServer.getInstance(mActivity, mHandler);
		//注册USB设备监听广播
        broadcastReceiver = new USBSerialBroadcastReceiver();
        IntentFilter filter = new IntentFilter() ;
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(EVENT_USB_INSERT);
        filter.addAction(ACTION_CUSTOMIZE_INSERT);
        mActivity.registerReceiver(broadcastReceiver, filter); 
	}

	@Override
	public void onDestroy() {
		mActivity.unregisterReceiver(broadcastReceiver);
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

		conn_start = (Button) view.findViewById(R.id.conn_start);
		conn_pause = (Button) view.findViewById(R.id.conn_pause);
		conn_stop = (Button) view.findViewById(R.id.conn_stop);
		save_data = (Button) view.findViewById(R.id.save_data);
		immediately_measure = (Button)view.findViewById(R.id.immediately_measure);
		showMsg = (TextView)view.findViewById(R.id.show_msg);
		listView = (ListView) view.findViewById(android.R.id.list);
	}

	private void setTextValueByMeasureParam(MeasureParam _mp) {
		if (Utility.isEmpty(_mp)) {
			return;
		}
		Log.i(TAG, "linenumber:" + line_number.getText().toString() + "radius:" + track.isChecked() + "inner_side:" + inner_side.isChecked());
		line_number.setText(_mp.getLineNumber() + "");
		if( _mp.getId()>0){
			line_name.setText(_mp.getLineName() + "");
		}
		radius.setText(_mp.getRadius() + "");
		outerrail_high.setText(_mp.getOuterrailHigh() + "");
		sample_interval.setText(_mp.getSampleInterval() + "");
		measure_startposition.setText(_mp.getMeasureStartposition() + "");
		track.setChecked(_mp.getTrack() > 0 ? true : false);
		inner_side.setChecked(_mp.getInnerSide() > 0 ? true : false);
	}

	private void setMeasureParam() {
		Log.i(TAG, "linenumber:" + line_number.getText().toString() + "radius:" + track.isChecked() + "inner_side:" + inner_side.isChecked());
		if (Utility.isEmpty(measureParam)) {
			measureParam = new MeasureParam();
		}
		measureParam.setLineNumber(Integer.parseInt(line_number.getText().toString()));
		measureParam.setLineName(line_name.getText().toString());
		measureParam.setRadius(Integer.parseInt(radius.getText().toString()));
		measureParam.setOuterrailHigh(Integer.parseInt(outerrail_high.getText().toString()));
		measureParam.setSampleInterval(Integer.parseInt(sample_interval.getText().toString()));
		measureParam.setMeasureStartposition(Integer.parseInt(measure_startposition.getText().toString()));
		measureParam.setTrack(track.isChecked() == true ? 1 : 0);
		measureParam.setInnerSide(inner_side.isChecked() == true ? 1 : 0);
	}

	private void saveMeasureParam() {
		if (!validateForm()) {
			return;
		}
		setMeasureParam();
		boolean isExist = businessDataBase.getMeasureParadmDao().isExistObject(measureParam);
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
				Log.i(TAG, "插入成功");
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "插入成功"));
				Intent intent = new Intent(MParamListFragment.UPDATE_LIST);
				mActivity.sendBroadcast(intent);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "插入失败"));
			}
		}
	}

	private boolean validateForm() {

		if (Utility.isEmpty(line_number.getText().toString())) {
			Toast.makeText(mActivity, "编号不能为空！", 0).show();
			return false;
		}

		if (Utility.isEmpty(line_name.getText().toString())) {
			Toast.makeText(mActivity, "线路名称不能为空！", 0).show();
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
					server.firstStart = 0 ;
					break;
				case R.id.conn_start :
					if(!Utility.isEmpty(measureParam) && measureParam.getId()>0){
						server.sendStart();	
					}else{
					  mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "请保存当前数据，再开始!"));
					}
					break;
				case R.id.conn_pause :
					server.sendPause();
					break;
				case R.id.conn_stop :
					server.sendStop();
					break;
				case R.id.immediately_measure :
					server.sendImmediately();
					break;
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
			mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "请先保存当前记录！"));
			return ;
		}
		if(measureParam.getRadius()<=0){
			mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "设置参数不合法，请检查半径值！"));
			return ;
		}
		MeasureResult _result = new MeasureResult() ;
		_result.setParamId(measureParam.getId());
		//返回数据顺序：行进距离,、月台高度、月台距离、倾角、雨棚高度
		try{
			_result.setTravelDistance(Utility.strToInt(data[0]));
			_result.setPlatformHigh(Utility.strToInt(data[1]));
			_result.setPlatformDistance(Utility.strToInt(data[2]));
			_result.setDipAngle(Utility.strToInt(data[3]));
			_result.setRainshedHigh(Utility.strToInt(data[4]));
			_result = businessDataBase.getMeasureResultDao().insertMeasureResult(_result);
		}catch(Exception exp){
			Log.i(TAG, "整型转换出错  :data:"+ exp.getMessage());	
			mHandler.sendMessage(mHandler.obtainMessage(Contants.TOAST_MSG, "整型转换出错"));
		}
		if(Utility.isEmpty(_result)){
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
		  mHandler.sendMessage(mHandler.obtainMessage(Contants.TOAST_MSG, "整型转换出错"));
	    }
		mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "测量点: "+measurePoint+" 接收成功"));
	}
	
	/**
	 * 
	 */
	public class USBSerialBroadcastReceiver extends BroadcastReceiver {
		private String TAG="USBBroadCastReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e(TAG, "BroadcastReceiver action:" + action);
			//授予权限成功后
			/*if (ACTION_USB_PERMISSION.equals(action)) {
				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED,false)) {
					mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "前端设备连上！"));
					server.initSerialDevice(mActivity,mHandler);
				} else{
					server.resetSerialDevice();
				}
				return;
			}*/
			//USB设备插入
			if(ACTION_CUSTOMIZE_INSERT.equals(action)){
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "前端设备连上！"));
				server.initSerialDevice(mActivity,mHandler);
				return;
			}
			//USB设备拔出
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				Log.e(TAG, "前端设备拔出！");
				mHandler.sendMessage(mHandler.obtainMessage(Contants.SHOW_MSG, "前端设备拔出！"));
				server.resetSerialDevice();
				return;
			}
		}
	}
	
}
