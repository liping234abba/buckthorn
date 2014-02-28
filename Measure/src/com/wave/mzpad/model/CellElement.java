package com.wave.mzpad.model;

/**
 * Excel Cell 对象
 */
public class CellElement {

	/**
	 * Cell 对应的X 坐标
	 */
	public int x ;
	
	/**
	 * Cell 对应的Y坐标
	 */
	public int y ;
	
	/**
	 * Cell 对应的值
	 */
	public String value;
	
	public CellElement(int x,int y,String value){
		this.x = x ;
		this.y = y ;
		this.value = value ;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
