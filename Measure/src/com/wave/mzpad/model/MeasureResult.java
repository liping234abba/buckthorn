package com.wave.mzpad.model;

/**
 * 测量结果
 * @author wave.li
 * @date   Dec 3, 2013
 *
 */
public class MeasureResult extends AbstractObject {

	public  String COLUMN_TRAVEL_DISTANCE = "travel_distance";
	public  String COLUMN_PLATFORM_HIGH = "platform_high";
	public  String COLUMN_PLATFORM_DISTANCE = "platform_distance";
	public  String COLUMN_DIP_ANGLE = "dip_angle";
	public  String COLUMN_RAINSHED_HIGH = "rainshed_high";
	public  String COLUMN_PARAMID = "param_id";

	private int travelDistance = 0;// 行进距离
	private int platformHigh = 0; // 月台高度
	private int platformDistance = 0;//月台距离
	private int dipAngle = 0;//倾角
	private int rainshedHigh = 0;//雨棚高度
	private int paramId ;//外键
	
	public int getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(int travelDistance) {
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
		return "MeasureResult [travelDistance=" + travelDistance + ", platformHigh=" + platformHigh + ", platformDistance=" + platformDistance + ", dipAngle=" + dipAngle + ", rainshedHigh=" + rainshedHigh + ", paramId=" + paramId + ", id=" + id + "]";
	}
	
}
