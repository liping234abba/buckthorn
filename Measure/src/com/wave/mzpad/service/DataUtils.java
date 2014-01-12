package com.wave.mzpad.service;

import android.content.Context;
import android.content.SharedPreferences;

public class DataUtils {

	
	public static String app_filename = "mzpad";
	
	/**
	 * 第一次启动设置false
	 * @param paramContext
	 */
    public static void setfirst(Context paramContext) {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences(app_filename, 0).edit();
        localEditor.putBoolean("first", false);
        localEditor.commit();
    }

    /**
     * 获取是否为第一次
     * @param paramContext
     * @return
     */
    public static boolean isFirst(Context paramContext) {
        SharedPreferences local = paramContext.getSharedPreferences(app_filename, 0);
        return local.getBoolean("first", true);
    }
	
}
