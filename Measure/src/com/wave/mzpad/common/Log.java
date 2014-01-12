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
 * 〈一句话功能简述〉 〈功能详细描述〉
 * @author qinyulun
 * @version HDMNV100R001, 2013-4-27
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Log {

    /**
     * true 代表开发所用
     */
    private static boolean mLogGrade = true;

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
