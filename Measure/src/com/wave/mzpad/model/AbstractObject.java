package com.wave.mzpad.model;

/**
 * 对象超类
 * @author wave.li
 * @date   Dec 7, 2013
 *
 */
public class AbstractObject {

	public String COLUMN_ID = "id";
	
	protected int id ;//主键
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
