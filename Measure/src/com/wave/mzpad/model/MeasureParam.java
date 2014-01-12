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
	
	private int lineNumber = 0; // 线路编号
	private String lineName = ""; // 线路名称
	private int track = 0; // 是否正线
	private int radius = 0; // 半径(m)
	private int outerrailHigh = 0; // 外轨超高(mm)
	private int sampleInterval = 0; // 采样间隔(m)
	private int innerSide = 0; // 是否内侧
	private int measureStartposition = 0; // 测试起始位置
	
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
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
	public int getMeasureStartposition() {
		return measureStartposition;
	}
	public void setMeasureStartposition(int measureStartposition) {
		this.measureStartposition = measureStartposition;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false ;
		}
		MeasureParam mp = (MeasureParam)o ;
		if(mp.getLineNumber() == this.getLineNumber() && mp.getLineName().equals(this.getLineName())){
			return true;
		}
		return false ;
	}
	@Override
	public String toString() {
		return "MeasureParam [lineNumber=" + lineNumber + ", lineName=" + lineName + ", track=" + track + ", radius=" + radius + ", outerrailHigh=" + outerrailHigh + ", sampleInterval=" + sampleInterval + ", innerSide=" + innerSide + ", measureStartposition=" + measureStartposition + "]";
	}
	
}
