package com.wave.mzpad.common;

public class Utility {

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

	public static String dumpHexString(byte[] array) {
		return dumpHexString(array, 0, array.length);
	}

	public static String dumpHexString(byte[] array, int offset, int length) {
		String tempSting = new String(array, offset, length);
		/*
		 * byte[] line = new byte[16]; int lineIndex = 0; StringBuilder result =
		 * new StringBuilder(); result.append("\n0x");
		 * result.append(toHexString(offset)); for (int i = offset; i < offset +
		 * length; i++) { if (lineIndex == 16) { result.append(" ");
		 * 
		 * for (int j = 0; j < 16; j++) { if (line[j] > ' ' && line[j] < '~') {
		 * result.append(new String(line, j, 1)); } else { result.append("."); }
		 * }
		 * 
		 * result.append("\n0x"); result.append(toHexString(i)); lineIndex = 0;
		 * }
		 * 
		 * byte b = array[i]; result.append(" "); result.append(HEX_DIGITS[(b
		 * >>> 4) & 0x0F]); result.append(HEX_DIGITS[b & 0x0F]);
		 * 
		 * line[lineIndex++] = b; }
		 * 
		 * if (lineIndex != 16) { int count = (16 - lineIndex) * 3; count++; for
		 * (int i = 0; i < count; i++) { result.append(" "); }
		 * 
		 * for (int i = 0; i < lineIndex; i++) { if (line[i] > ' ' && line[i] <
		 * '~') { result.append(new String(line, i, 1)); } else {
		 * result.append("."); } } } result.toString();
		 */
		return tempSting;
	}

	private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String toHexString(byte b) {
		return toHexString(toByteArray(b));
	}

	public static String toHexString(byte[] array) {
		return toHexString(array, 0, array.length);
	}

	public static String toHexString(byte[] array, int offset, int length) {
		char[] buf = new char[length * 2];
		int bufIndex = 0;
		for (int i = offset; i < offset + length; i++) {
			byte b = array[i];
			buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
			buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
		}

		return "0x" + new String(buf);
	}

	public static String toHexString(int i) {
		return toHexString(toByteArray(i));
	}

	public static String toHexString(short i) {
		return toHexString(toByteArray(i));
	}

	public static byte[] toByteArray(byte b) {
		byte[] array = new byte[1];
		array[0] = b;
		return array;
	}

	public static byte[] toByteArray(int i) {
		byte[] array = new byte[4];

		array[3] = (byte) (i & 0xFF);
		array[2] = (byte) ((i >> 8) & 0xFF);
		array[1] = (byte) ((i >> 16) & 0xFF);
		array[0] = (byte) ((i >> 24) & 0xFF);

		return array;
	}

	public static byte[] toByteArray(short i) {
		byte[] array = new byte[2];

		array[1] = (byte) (i & 0xFF);
		array[0] = (byte) ((i >> 8) & 0xFF);

		return array;
	}

	private static int toByte(char c) {
		if (c >= '0' && c <= '9')
			return (c - '0');
		if (c >= 'A' && c <= 'F')
			return (c - 'A' + 10);
		if (c >= 'a' && c <= 'f')
			return (c - 'a' + 10);

		throw new RuntimeException("Invalid hex char '" + c + "'");
	}

	public static byte[] hexStringToByteArray(String hexString) {
		int length = hexString.length();
		byte[] buffer = new byte[length / 2];

		for (int i = 0; i < length; i += 2) {
			buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
		}

		return buffer;
	}

	/**
	 * 字符串转整型
	 * @param str
	 * @return
	 */
	public static int strToInt(String str) {
		int result = 0;
		if (!isEmpty(str)) {
			result = Integer.parseInt(str);
		}
		return result;
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
	
	public static String getCheckCodeHexByString(String cmd){
		String checkSum = cmd.substring(0, cmd.indexOf("0xff"));
		char[] charArray = checkSum.toCharArray();
		int sum = 0;
		for(int i =0 ;i < charArray.length;i++){
			sum = sum+charArray[i];
		}
		return Utility.toHexString((byte)sum);
	}
	
}
