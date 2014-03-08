/* 
 * 文件名：NSLog.java
 * 版权：Copyright 2009-2010 companyName MediaNet. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.wave.mzpad.common;


/**
 */
public class Log {

    /**
     * true 代表开发所用
     */
    private static boolean mLogGrade = false;

    public static void e(String tag, String msg, Exception e) {
        if (mLogGrade) {
            android.util.Log.e(tag, ""+msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mLogGrade) {
            android.util.Log.e(tag, ""+msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mLogGrade) {
            android.util.Log.i(tag, ""+msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mLogGrade) {
            android.util.Log.w(tag, ""+msg);
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (mLogGrade) {
            android.util.Log.w(tag, ""+msg);
        }
    }

    public static void w(String tag, String msg, Throwable cause) {
        if (mLogGrade) {
            android.util.Log.w(tag, ""+msg);
        }
    }

    public static void v(String tag, String msg) {
        if (mLogGrade) {
            android.util.Log.v(tag, ""+msg);
        }
    }

    public static void d(String tag, String msg) {
        if (mLogGrade) {
            android.util.Log.d(tag, ""+msg);
        }
    }
}
