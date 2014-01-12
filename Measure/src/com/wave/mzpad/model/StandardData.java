package com.wave.mzpad.model;

/**
 * 机车车辆限界、各级超限限界与建筑限界距离线路中心线所在垂直平面尺寸表
 * @author wave.li
 * @date   Dec 3, 2013
 */
public class StandardData extends AbstractObject {

	public static String COLUMN_TRACK_HIGH = "track_high";
	public static String COLUMN_TRAIN_LIMIT = "train_limit";
	public static String COLUMN_SECOND_LEVEL = "second_level";
	public static String COLUMN_BUILD_RIGHT = "build_right";
	public static String COLUMN_BUILD_DEVIOUS = "build_devious";

	private int trackHigh; // 自轨面起算的高度(mm)

	private int trainLimit; // 机车车辆限界

	private int secondLevel;// 二级超限限界

	private int buildRight; // 建筑限界*正线

	private int buildDevious; // 建筑限界*非正线

	public int getTrackHigh() {
		return trackHigh;
	}

	public void setTrackHigh(int trackHigh) {
		this.trackHigh = trackHigh;
	}

	public int getTrainLimit() {
		return trainLimit;
	}

	public void setTrainLimit(int trainLimit) {
		this.trainLimit = trainLimit;
	}

	public int getSecondLevel() {
		return secondLevel;
	}

	public void setSecondLevel(int secondLevel) {
		this.secondLevel = secondLevel;
	}

	public int getBuildRight() {
		return buildRight;
	}

	public void setBuildRight(int buildRight) {
		this.buildRight = buildRight;
	}

	public int getBuildDevious() {
		return buildDevious;
	}

	public void setBuildDevious(int buildDevious) {
		this.buildDevious = buildDevious;
	}

	
	
}
