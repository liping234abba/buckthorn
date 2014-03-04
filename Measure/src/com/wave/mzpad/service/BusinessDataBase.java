package com.wave.mzpad.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.wave.mzpad.db.MeasureParamDAO;
import com.wave.mzpad.db.MeasureResultDAO;
import com.wave.mzpad.db.SQLiteHelper;
import com.wave.mzpad.db.StandardDataDAO;
import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.model.StandardData;

public class BusinessDataBase {

	private Context ctx ;
	
	private MeasureResultDAO measureResultDao ;
	
	private MeasureParamDAO measureParadmDao ;
	
	private StandardDataDAO standardDataDAO ;
	
	public BusinessDataBase(Context _ctx){
		this.ctx =_ctx ;
		measureResultDao = new MeasureResultDAO(ctx);
		measureParadmDao = new MeasureParamDAO(ctx);
		standardDataDAO = new StandardDataDAO(ctx);
	}
	
	public MeasureParamDAO getMeasureParadmDao() {
		return measureParadmDao;
	}
	
	public MeasureResultDAO getMeasureResultDao() {
		return measureResultDao;
	}
	
	public StandardDataDAO getStandardDataDAO() {
		return standardDataDAO;
	}
	
	/**
	 * 根据参数Id获取采集数据
	 */
	public List<MeasureResult> getMeasureResult(int paramId){
		String sql = " where param_id=" + paramId ;
		return measureResultDao.getMeasureResult(sql);
	}
	
	
	/**
	 * 计算内侧系数（W1 = （40500/R）+H*h/1500）(R:半径m ，H：月台高度，mm ，h：外轨超高mm)
	 */
	public int calInnerSideCoeft(int radius,int platformHight,int outerrailHight){
		return 40500/radius+platformHight*outerrailHight/1500 ;
	}
	
	/**
	 * 计算外侧系数（W2 = 44000/R）
	 */
	public int calOuterSideCoeft(int radius){
		return 44000/radius ;
	}
	
	/**
	 *  内侧计算超限：月台距离+W1 与限界比较
	 */
	public int calInnerSideLimit(MeasureResult measureResult,MeasureParam measureParam){
		return measureResult.getPlatformDistance() + calInnerSideCoeft(measureParam.getRadius(), measureResult.getPlatformHigh(), measureParam.getOuterrailHigh()) ;
	}
	
	/**
	 * 外侧计算超限：月台距离+W2 与限界比较
	 */
	public int calOuterSideLimit(MeasureResult measureResult,MeasureParam measureParam){
		return measureResult.getPlatformDistance() + calOuterSideCoeft(measureParam.getRadius()) ;
	}
	
	/**
     * 初始化数据
     * @param pDataBase
     */
    public void initData(){
    	//创建表
    	SQLiteHelper.getInstance(ctx);
    	//初始化数据
    	AssetManager assetManager = ctx.getAssets();
		InputStream is=null;
		BufferedReader br=null;
		try {
			is=assetManager.open("standard_data.txt");
			br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String str=null;
			br.readLine();
			while((str=br.readLine())!=null){
				String[] ss=str.split(",");
				StandardData  standardData = new StandardData();
				standardData.setTrackHigh(Integer.parseInt(ss[0]));
				standardData.setTrainLimit(Integer.parseInt(ss[1]));
				standardData.setSecondLevel(Integer.parseInt(ss[2]));
				standardData.setBuildRight(Integer.parseInt(ss[3]));
				standardData.setBuildDevious(Integer.parseInt(ss[4]));
				standardDataDAO.insertStandardData(standardData);
			}
		} catch (IOException e) {
				e.printStackTrace();
		}finally{
				
		}
    }

	/**
	 *  计算超限值
	 */
	public int calLimitValue(MeasureResult measureResult,MeasureParam measureParam){
		if(measureParam.getInnerSide()>0){
			return calInnerSideLimit(measureResult, measureParam);
		}else{
			return calOuterSideLimit(measureResult, measureParam);
		}
	}
	
	/**
	 *  计算超限是否超限
	 */
	public int[] calWarningLevelLimited(StandardData sd, MeasureResult measureResult,MeasureParam measureParam){
		int[] result = new int[2];//[0]:是否超限，[1]侵限值
		int isLimited = -1 ;
		int limitValue = 0 ;
		int diffResult = 0;
		int secondLimit = sd.getSecondLevel() ;// 在二级超限到建筑界限之间属于一般超限，小于二级超限属于严重超限
		//计算是否正线对应的超限差别
		if(measureParam.getInnerSide()>0){
			limitValue = calInnerSideLimit(measureResult, measureParam) ;
			if(measureParam.getTrack()>0){
				diffResult = limitValue -  sd.getBuildRight() ;
			}else{
				diffResult = limitValue - sd.getBuildDevious();
			}
		}else{
			limitValue = calOuterSideLimit(measureResult, measureParam) ;
			if(measureParam.getTrack()>0){
				diffResult = limitValue -  sd.getBuildRight() ;
			}else{
				diffResult = limitValue -  sd.getBuildDevious();
			}
			
		}
		//根据不同差别计算级别
		if(diffResult<0){
			if(limitValue-secondLimit<0){
				isLimited = 2 ;
			}else{
				isLimited = 1 ;
			}
		}else{
			isLimited = -1 ;
		}	
		result[0] = isLimited ;
		result[1] = diffResult ;
		return result ;
	}
	

}
