package com.yaokan.sdk.service;

import com.yaokan.sdk.utils.HttpUtil;

import android.content.Context;

public class YanKanService implements IYaokan {

    private String url_prefix = "http://city.sun-cam.com.cn/zte/m.php?"  ;
	
    
    private HttpUtil httpUtil ;
    
	public YanKanService(Context ctx){
		httpUtil = new HttpUtil(ctx);
	}
	 
	public String getTypes() {
		String url_s = "c=t" ;
		return getResult(url_s);
	}

	public String getBrands(int t, int pn, int ps) {
		String url_s = "c=f&t="+t+"&pn="+pn+"&ps="+ps ;
		return getResult(url_s);
	}

	public String getPairCodes(String n, int t) {
		String url_s = "c=l&n="+n+"&t="+t ;
		return getResult(url_s);
	}

	public String getCodes(String r) {
		String url_s = "c=d&r="+r ;
		return getResult(url_s);
	}

	public String getPairCodesByAid(int a) {
		String url_s = "c=p&a="+a ;
		return getResult(url_s);
	}

	public String getResult(String url_sufx) {
		return httpUtil.getMethod(url_prefix + url_sufx);
	}
}
