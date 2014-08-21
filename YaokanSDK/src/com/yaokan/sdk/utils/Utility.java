package com.yaokan.sdk.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.view.View;

/**
 * Common utility functions which used in systems.
 * 
 * @version $version 1.0
 */
public class Utility {

	private Utility() {
		
	}

	/**
	 * get the current calendar
	 * 
	 * @return Calendar
	 */
	public static Calendar getCurrentCalendarTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}

	/**
	 * isEmpty Test to see whether input string is empty.
	 * 
	 * @param str
	 *          a String
	 * @return True if it is empty; false if it is not.
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0 || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim()));
	}

	/**
	 * isEmpty Test to see whether input string buffer is empty.
	 * 
	 * @param str
	 *          A StringBuffer
	 * @return True if it is empty; false if it is not.
	 */
	public static boolean isEmpty(StringBuffer stringBuffer) {
		return (stringBuffer == null || stringBuffer.length() == 0 || stringBuffer.toString().trim().equals(""));
	}

	/**
	 * isEmpty Test to see whether input string is empty.
	 * 
	 * @param str
	 *          A String
	 * @return True if it is empty; false if it is not.
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * isEmpty Test to see whether input is empty.
	 * 
	 * @param StringArray
	 *          A array
	 * @return True if it is empty; false if it is not.
	 */
	public static boolean isEmpty(String[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * isEmpty Test to see whether input is representing a NULL value.
	 * 
	 * @param val
	 *          An Object
	 * @return True if it represents NULL; false if it is not.
	 */
	public static boolean isEmpty(Object val) {
		return (val == null);
	}

	/**
	 * isEmpty Test to see whether input is empty.
	 * 
	 * @param list
	 *          A List
	 * @return True if it is empty; false if it is not.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(java.util.List list) {
		return (list == null || list.isEmpty() || list.size() == 0);
	}

	/**
	 * isEmpty Test to see whether input is empty.
	 * 
	 * @param vector
	 *          A Vector
	 * @return True if it is empty; false if it is not.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(java.util.Vector vector) {
		return (vector == null || vector.size() == 0);
	}

	/**
	 * getStringArray
	 * 
	 * @param ary
	 *          The object array.
	 * @return The list contains all the object array elements.
	 */
	@SuppressWarnings("rawtypes")
	public static String[] getStringArray(List list) {
		if (list == null) {
			return (null);
		}
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			try {
				result[i] = (String) list.get(i);
			} catch (ClassCastException ce) {
				result[i] = ((Integer) list.get(i)).toString();
			}
		}
		return (result);
	}


	/**
	 * remove the repeat element in the list
	 * 
	 * @param list
	 * @return newList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeDuplicate(List list) {
		if (Utility.isEmpty(list)) {
			return null;
		}
		HashSet h = new HashSet(list);
		List newList = new ArrayList();
		newList.addAll(h);
		return newList;
	}

	/**
	 * InputStream is changed to the String
	 * 
	 * @param is
	 *          an InputStream
	 * @return String
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		if (null == is) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	/**
	 * get the key set from the Map
	 * 
	 * @param map
	 *          is Map Collection
	 * @return collection
	 */
	@SuppressWarnings("rawtypes")
	public static Set keySetFromMap(Map map) {
		return map.keySet();
	}

	/**
	 * get the value set from the Map Collection
	 * 
	 * @param map
	 *          is Map collection
	 * @return collection
	 */
	@SuppressWarnings("rawtypes")
	public static Set valueSetFromMap(Map map) {
		return map.entrySet();
	}

	/**
	 * show view
	 * 
	 * @param view
	 */
	public static void showView(View view) {
		if (!Utility.isEmpty(view)) {
			if (View.VISIBLE != view.getVisibility()) {
				view.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * hide view
	 * 
	 * @param view
	 */
	public static void hideView(View view) {
		if (!Utility.isEmpty(view)) {
			if (View.INVISIBLE != view.getVisibility()) {
				view.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Gone view
	 * 
	 * @param view
	 */
	public static void goneView(View view) {
		if (!Utility.isEmpty(view)) {
			if (View.GONE != view.getVisibility()) {
				view.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 全角转换半角
	 * 
	 * @param input
	 * @return
	 */
	public static String toSemiangle(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375) {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 半角角转换全角
	 * 
	 * @param input
	 * @return
	 */
	public static String toDBC(String input) {
		if (isEmpty(input)) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] > 32 && c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 在整数前面添加相同的字符以补全位数
	 * 
	 * @param src
	 *          : 整数
	 * @param num
	 *          ：要补全的位数
	 * @return String
	 */
	public static String addZeroInNum(int src, int num) {
		String tempString = Integer.toString(src);
		return addCharInString(tempString, num, '0');
	}

	/**
	 * 在字符串前面添加相同的字符以补全位数
	 * 
	 * @param src
	 *          : 原字符串
	 * @param num
	 *          ：要补全的位数
	 * @param tempChar
	 *          :用于补位的字符
	 * @return String
	 */
	public static String addCharInString(String src, int num, char tempChar) {
		String tempStr = src;
		int length = src.length();
		if (length >= num) {
		} else {
			for (int i = 0; i < num - length; i++) {
				tempStr = tempChar + tempStr;
			}
		}
		return tempStr;
	}


	/*
	 * 字符串转为整数 str 源字符串, default 默认值
	 */
	public static int CInt(String str, int iDefault) {
		int iR = iDefault;
		try {
			iR = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			iR = iDefault;
		}
		return iR;
	}

	public static byte CByte(String str, byte iDefault) {
		byte iR = iDefault;
		try {
			iR = Byte.parseByte(str);
		} catch (NumberFormatException e) {
			iR = iDefault;
		}
		return iR;
	}

	public static byte[] intToByteArray(int[] cmd) {
		byte[] result = new byte[cmd.length];
		for (int i = 0; i < cmd.length; i++)
			result[i] = (byte) cmd[i];
		return result;
	}

	public static int[] byteToIntArray(byte[] cmd) {
		int[] result = new int[cmd.length];
		for (int i = 0; i < cmd.length; i++)
			result[i] = (int) cmd[i];
		return result;
	}

	public static int[] String2Int(String _s) {
		String s[] = _s.split(",");
		int iLen = s.length;
		int aCMD[] = new int[iLen];
		for (int i = 0; i < s.length; i++) {
			aCMD[i] = CInt(s[i], 0);
		}
		return aCMD;
	}

	public static byte[] str2Byte(String _s) {
		String s[] = _s.split(",");
		int iLen = s.length;
		byte aCMD[] = new byte[iLen];
		for (int i = 0; i < s.length; i++) {
			aCMD[i] = CByte(s[i], (byte) 0);
		}
		return aCMD;
	}

	// 删除前后的 [ 与 ]
	public static String trimSytax(String _s) {
		String s = _s;
		String c = "";
		if(isEmpty(_s)){
			return c ;
		}
		c = s.substring(0, 1);
		if (c.equals("["))
			s = s.substring(1);
		c = s.substring(s.length() - 1, s.length());
		if (c.equals("]"))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	public static String int2Str(int _a[]) {
		String s = "";
		for (int i = 0; i < _a.length; i++) {
			if (s.equals(""))
				s = _a[i] + "";
			else
				s = s + "," + _a[i];
		}
		return s;
	}

	public static String byte2Str(byte _a[]) {
		String s = "";
		for (int i = 0; i < _a.length; i++) {
			if (s.equals(""))
				s = _a[i] + "";
			else
				s = s + "," + _a[i];
		}
		return s;
	}

	//获取android 版本信息
	public static int getAndroidOSVersion() {
		int osVersion;
		try {
			osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			osVersion = 0;
		}

		return osVersion;
	}
	
	//获取IMEI
	public static String getIMEI(Context ctx){
		IMEI imei = new IMEI() ;
		return imei.getIMEI(ctx);
	}
	
}
