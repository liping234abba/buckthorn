package com.wave.mzpad.model;

/**
 * 测量参数
 * @author wave.li
 * @date   Dec 3, 2013
 *
 */
public class MeasureParam extends AbstractObject {

	public String COLUMN_LINE_NUMBER = "line_number"; // 线路编号 
	public String COLUMN_LINE_NAME = "line_name"; // 线路名称
	public String COLUMN_TRACK = "track"; // 是否正线 
	public String COLUMN_RADIUS = "radius"; // 半径(m) 
	public String COLUMN_OUTERRAIL_HIGH = "outerrail_high"; // 外轨超高(mm) 
	public String COLUMN_SAMPLE_INTERVAL = "sample_interval"; // 采样间隔(m) 
	public String COLUMN_INNER_SIDE = "inner_side"; // 是否内侧 
	public String COLUMN_MEASURE_STARTPOS = "measure_startpos"; // 测试起始位置
	
	public String COLUMN_STAND_NAME = "stand_name"; //站名、设备名称
	public String COLUMN_STAND_ID = "stand_id"; //设备编号
	public String COLUMN_STAND_AREA = "stand_area"; //换算面积
	public String COLUMN_STAND_DIRECTION = "stand_direction"; //面向车站
	public String COLUMN_STAND_ORIENTATION = "stand_orientation"; //设备方位
	public String COLUMN_BIGHT_DIRECTION = "bight_direction"; //曲线方向
	public String COLUMN_RAIL_HIGH = "rail_high"; //是否高铁 
	
	private String lineNumber = ""; // 线路编号
	private String lineName = ""; // 线路名称
	private String measureStartposition = ""; // 中心里程
	private int track = 0; // 是否正线 0:否；1：是
	private int innerSide = 0; // 是否内侧  0:否；1：是
	private int radius ; // 半径(m)
	private int outerrailHigh ; // 外轨超高(mm)
	private int sampleInterval ; // 采样间隔(m)
	
	private String standName = ""; //站名、设备名称
	private String standId = ""; //设备编号
	private String standArea = ""; //换算面积
	private String standDirection = ""; //面向车站
	private int standOrientation = 0; //设备方位:0:线路左侧，1：线路右侧
	private int bightDirection = 0; //曲线方向：0：左，1：右
	private int railHigh = 0; //是否高铁  1:是，0，否
	
	public String getStandName() {
		return standName;
	}
	public void setStandName(String standName) {
		this.standName = standName;
	}
	public String getStandId() {
		return standId;
	}
	public void setStandId(String standId) {
		this.standId = standId;
	}
	public String getStandArea() {
		return standArea;
	}
	public void setStandArea(String standArea) {
		this.standArea = standArea;
	}
	public String getStandDirection() {
		return standDirection;
	}
	public void setStandDirection(String standDirection) {
		this.standDirection = standDirection;
	}
	
	public int getStandOrientation() {
		return standOrientation;
	}
	public void setStandOrientation(int standOrientation) {
		this.standOrientation = standOrientation;
	}
	public int getBightDirection() {
		return bightDirection;
	}
	public void setBightDirection(int bightDirection) {
		this.bightDirection = bightDirection;
	}
	public int getRailHigh() {
		return railHigh;
	}
	public void setRailHigh(int railHigh) {
		this.railHigh = railHigh;
	}
	public int getTrack() {
		return track;
	}
	public void setTrack(int track) {
		this.track = track;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getOuterrailHigh() {
		return outerrailHigh;
	}
	public void setOuterrailHigh(int outerrailHigh) {
		this.outerrailHigh = outerrailHigh;
	}
	public int getSampleInterval() {
		return sampleInterval;
	}
	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}
	public int getInnerSide() {
		return innerSide;
	}
	public void setInnerSide(int innerSide) {
		this.innerSide = innerSide;
	}

	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getMeasureStartposition() {
		return measureStartposition;
	}
	public void setMeasureStartposition(String measureStartposition) {
		this.measureStartposition = measureStartposition;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false ;
		}
		MeasureParam mp = (MeasureParam)o ;
		if(mp.getLineNumber().equals(getLineNumber()) && mp.getLineName().equals(this.getLineName())){
			return true;
		}
		return false ;
	}
	@Override
	public String toString() {
		return "MeasureParam [lineNumber=" + lineNumber + ", lineName=" + lineName + ", track=" + track + ", radius=" + radius + ", outerrailHigh=" + outerrailHigh + ", sampleInterval=" + sampleInterval + ", innerSide=" + innerSide + ", measureStartposition=" + measureStartposition + "]";
	}
	
}
