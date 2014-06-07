package com.wave.mzpad.service;

import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;

public class GTCalResult extends AbstractCalResult {

	/**
	 * 计算高铁内侧加宽量W=H×h/1500(H：月台高度，mm ，h：外轨超高mm)
	 */
	public float calInnerSideCoeft(int radius,int platformHight,int outerrailHight){
		return  40500/(float)radius+ (float)platformHight*(float)outerrailHight/1500 ;
	}
	
	/**
	 * 计算普快外侧系数（W2 = 44000/R）
	 */
	public float calOuterSideCoeft(int radius){
		return 44000/(float)radius ;
	}
	
	
	
	
	/**
	 *  内侧计算超限真实月台距离：前端月台距离+W1
	 */ 
	public float calInnerSideLimit(MeasureResult measureResult,MeasureParam measureParam){
		return measureResult.getPlatformDistance() - calInnerSideCoeft(measureParam.getRadius(), measureResult.getPlatformHigh(), measureParam.getOuterrailHigh()) ;
	}
	
	/**
	 * 外侧计算超限真实月台距离：前端月台距离+W2
	 */
	public float calOuterSideLimit(MeasureResult measureResult,MeasureParam measureParam){
		return measureResult.getPlatformDistance() - calOuterSideCoeft(measureParam.getRadius()) ;
	}
	
}
