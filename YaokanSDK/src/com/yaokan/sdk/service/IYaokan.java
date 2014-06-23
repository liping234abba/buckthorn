package com.yaokan.sdk.service;

public interface IYaokan {

	//从服务端获取数据
	public String getResult(String url);
	
	//获取遥控设备类型列表
	public String  getTypes();
	   
	//获取遥控设备品牌列表 (只有电视机顶盒与电视机有效) pn分页页码(第几页，默认第1页), ps分页条数(每页记录数,默认20条)
	public String  getBrands(int t,int pn, int ps);
			
	//根据品牌、设备类型获取遥控码列表（部分数据-匹配用）
	public String  getPairCodes(String n,int t);
	
	//根据遥控器ID获取遥控器码库（完整数据）
	public String  getCodes(String r);

	// 根据省份ID获取机顶盒遥控码列表（部分数据-匹配用）
    public String getPairCodesByAid(int a);
 

	
}
