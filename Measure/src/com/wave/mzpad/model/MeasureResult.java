package com.wave.mzpad.model;

/**
 * 测量结果
 * @author wave.li
 * @date   Dec 3, 2013
 *
 */
public class MeasureResult extends AbstractObject implements Comparable {

	public  String COLUMN_TRAVEL_DISTANCE = "travel_distance";
	public  String COLUMN_PLATFORM_HIGH = "platform_high";
	public  String COLUMN_PLATFORM_DISTANCE = "platform_distance";
	public  String COLUMN_DIP_ANGLE = "dip_angle";
	public  String COLUMN_RAINSHED_HIGH = "rainshed_high";
	public  String COLUMN_PARAMID = "param_id";
	
	public  String COLUMN_LIMIT_DEFAULT = "limit_default";
	public  String COLUMN_LIMIT_UPDATE = "limit_update";
	

	private String travelDistance = "";// 行进距离
	private int platformHigh = 0; // 月台高度
	private int platformDistance = 0;//月台距离
	private int dipAngle = 0;//倾角
	private int rainshedHigh = 0;//雨棚高度
	private int paramId ;//外键
	
	private int limitDefault = 0 ,limitUpdate = 0 ; //原始值，修改值
	
	public String getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(String travelDistance) {
		this.travelDistance = travelDistance;
	}
	public int getPlatformHigh() {
		return platformHigh;
	}
	public void setPlatformHigh(int platformHigh) {
		this.platformHigh = platformHigh;
	}
	public int getPlatformDistance() {
		return platformDistance;
	}
	public void setPlatformDistance(int platformDistance) {
		this.platformDistance = platformDistance;
	}
	public int getDipAngle() {
		return dipAngle;
	}
	public void setDipAngle(int dipAngle) {
		this.dipAngle = dipAngle;
	}
	public int getRainshedHigh() {
		return rainshedHigh;
	}
	public void setRainshedHigh(int rainshedHigh) {
		this.rainshedHigh = rainshedHigh;
	}
	public int getParamId() {
		return paramId;
	}
	public void setParamId(int paramId) {
		this.paramId = paramId;
	}
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false ;
		}
		MeasureResult mr = (MeasureResult)o;
		if(mr.getId()== this.getId() && mr.getParamId() == this.getParamId()){
			return true ;
		}
		return false ;
	}
	
	@Override
	public String toString() {
		return "MeasureResult [travelDistance=" + travelDistance + ", platformHigh=" + platformHigh + ", platformDistance=" + platformDistance + ", dipAngle=" + dipAngle + ", rainshedHigh=" + rainshedHigh + ", paramId=" + paramId + ", limitDefault=" + limitDefault + ", limitUpdate=" + limitUpdate + "]";
	}
	@Override
	public int compareTo(Object another) {
		MeasureResult measureResult = (MeasureResult)another ;
		if(this.getId()>measureResult.getId()){
			return 1;
		}else if(this.getId()<measureResult.getId()){
			return -1;
		}
		return 0;
	}
	
	public int getLimitDefault() {
		return limitDefault;
	}
	public void setLimitDefault(int limitDefault) {
		this.limitDefault = limitDefault;
	}
	public int getLimitUpdate() {
		return limitUpdate;
	}
	public void setLimitUpdate(int limitUpdate) {
		this.limitUpdate = limitUpdate;
	}
	
}
