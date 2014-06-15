package com.wave.mzpad.service;

import com.wave.mzpad.model.MeasureParam;
import com.wave.mzpad.model.MeasureResult;

public abstract class AbstractCalResult {

	/**
	 * 计算 普快内侧系数（W1 = （40500/R）+H*h/1500）(R:半径m ，H：月台高度，mm ，h：外轨超高mm)
	 */
	public abstract float calInnerSideCoeft(int radius,int platformHight,int outerrailHight);
	
	/**
	 * 计算普快外侧系数（W2 = 44000/R）
	 */
	public abstract float calOuterSideCoeft(int radius,int platformHight,int outerrailHight);
	
	/**
	 * 计算直线
	 */
	public abstract float calLineLimit(MeasureResult measureResult,MeasureParam measureParam);
		
	/**
	 *  内侧计算超限真实月台距离：前端月台距离+W1
	 */ 
	public abstract float calInnerSideLimit(MeasureResult measureResult,MeasureParam measureParam);
	
	/**
	 * 外侧计算超限真实月台距离：前端月台距离+W2
	 */
	public abstract float calOuterSideLimit(MeasureResult measureResult,MeasureParam measureParam);
	
}
