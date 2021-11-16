package com.seculayer.util;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;

public class StringUtil {

	public static String get(Object obj) {
		String strRtn = "";

		if (obj != null) {
			if (obj instanceof String) {
				strRtn = ((String) obj).trim();
			} else {
				strRtn = obj.toString();
			}
		}

		return strRtn;
	}

	public static String get(Object obj, String str) {
		String s = get(obj);
		if (isEmpty(s)) {
			return str;
		}
		return s;
	}

	public static int getInt(Object obj) {
		int dRtn = 0;
		if (isNotEmpty(get(obj)))
			dRtn = Integer.parseInt(trunc(get(obj)));

		return dRtn;
	}

	public static long getLong(Object obj) {
		long dRtn = 0;
		if (isNotEmpty(get(obj)))
			dRtn = Long.parseLong(trunc(get(obj)));

		return dRtn;
	}

	public static double getDouble(Object obj) {
		double dRtn = 0;

		if (isNotEmpty(get(obj)))
			dRtn = Double.parseDouble(get(obj));

		return dRtn;
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isBlank(str);
	}

	public static boolean isNotEmpty(String str) {
		return StringUtils.isNotBlank(str);
	}

	public static boolean isAlpha(String str) {
		return StringUtils.isAlpha(str);
	}

	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}

	public static boolean contains(String str1, String str2) {
		return StringUtils.contains(str1, str2);
	}

	public static boolean isNumber(String str) {
		return StringUtils.isNumeric(str);
	}

	public static String trunc(String val) {
		if (isEmpty(val)) return "0";
		int prdIdx = val.indexOf('.');
		if (prdIdx != -1)
			return val.substring(0, prdIdx);
		return val;
	}

	public static boolean startsWith(String str, String prefix) {
		return StringUtils.startsWith(str, prefix);
	}

	public static boolean endWith(String str, String suffix) {
		return StringUtils.endsWith(str, suffix);
	}

	public static String replace(String text, String searchString, String replacement) {
		return StringUtils.replace(text, searchString, replacement);
	}

	public static String replaceAll(String text, String regex, String replacement) {
		return get(text).replaceAll(regex, replacement);
	}

	public static String join(Collection<?> collection, String separator) {
		return StringUtils.join(collection, separator);
	}

	public static String[] split(String str) {
		return split(str, ",");
	}

	public static String[] split(String str, String separator) {
		return StringUtils.split(str, separator);
	}

    public static boolean getBoolean(Object gpu_use) {
        String valueString = get(gpu_use);
        if ("true".equals(valueString))
            return true;
        else if ("false".equals(valueString))
            return false;
        else return false;
    }
}
