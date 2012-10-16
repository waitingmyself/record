package com.waitingmyself.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waitingmyself.common.constant.Const;

/**
 * 基本帮助类
 * 
 * @author lixl
 * @version 2011-09-08
 */
public class BaseUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            指定的字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 对象是否为 null 或 空串
	 * 
	 * @param obj
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof Map) {
			return ((Map) obj).size() == 0;
		} else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		} else {
			return Const.STR_EMPTY.equals(obj.toString());
		}
	}

	/**
	 * null转为String值为""
	 * 
	 * @param obj
	 * @return String
	 */
	public static String nullToSpace(Object obj) {
		if (obj == null) {
			return Const.STR_EMPTY;
		}
		return obj.toString();
	}

	/**
	 * 通过完整className获取对象.
	 * 
	 * @param className
	 * @return Object
	 */
	public static Object getInstance(String className) {
		if (isEmpty(className)) {
			return null;
		}
		// 注册类对象.
		Object regClass = null;
		try {
			regClass = Class.forName(className).newInstance();
		} catch (Exception e) {
			return null;
		}
		return regClass;
	}

	/**
	 * 取得对象的指定属性的值
	 * 
	 * @param obj
	 *            对象
	 * @param attributeName
	 *            对象属性
	 * @return Object 对象值
	 */
	public static Object getAttributeValue(Object obj, String attributeName) {

		// 创建返回值对象
		Object attributeValue = null;
		if (isEmpty(obj)) {
			return attributeValue;
		}

		Method[] methods = obj.getClass().getMethods();
		String methodName = attributeToMethodName(attributeName, Const.STR_GET);
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (methodName.equals(method.getName())) {
				Object[] oarray = new Object[0];
				try {
					attributeValue = method.invoke(obj, oarray);
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		// 返回对象
		return attributeValue;
	}

	/**
	 * 获得符合表达式的字符串内容
	 * 
	 * @param regx
	 * @param content
	 * @param groupIndex
	 * @return List<String>
	 */
	public static List<String> matcherList(String regx, String content,
			int groupIndex) {
		List<String> ls = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			ls.add(matcher.group(groupIndex));
		}
		return ls;
	}

	// /**
	// * 设置对象的指定属性值
	// *
	// * @param obj
	// * @param fieldName
	// * @param value
	// */
	// public static void setAttributeValue(Object obj, String fieldName,
	// Object value) {
	// try {
	// Field field = obj.getClass().getField(fieldName);
	// if (value == null) {
	// field.set(obj, null);
	// } else if (value instanceof Boolean) {
	// field.setBoolean(obj, (Boolean) value);
	// } else if (value instanceof Byte) {
	// field.setByte(obj, (Byte) value);
	// } else if (value instanceof Double) {
	// field.setDouble(obj, (Double) value);
	// } else if (value instanceof Float) {
	// field.setFloat(obj, (Float) value);
	// } else if (value instanceof Integer) {
	// field.setInt(obj, (Integer) value);
	// } else if (value instanceof Long) {
	// field.setLong(obj, (Long) value);
	// } else if (value instanceof Short) {
	// field.setShort(obj, (Short) value);
	// } else {
	// field.set(obj, value);
	// }
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchFieldException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 属性名变方法名
	 * 
	 * @param attributeName
	 *            属性名
	 * @param methodActionName
	 *            方法动作类型
	 * @return 变化后的方法名
	 */
	public static String attributeToMethodName(String attributeName,
			String methodActionName) {

		// 方法名
		StringBuffer methodName = new StringBuffer();
		/*
		 * 属性名不为空时，变化处理开始
		 */
		if (!isEmpty(attributeName)) {
			// 将动作类型添加到方法名中
			methodName.append(methodActionName);
			// 将属性名的首字母变成大成字母添加到方法名中
			methodName.append(attributeName.substring(0, 1).toUpperCase());
			// 将属性名的首字以外的字符添加到方法名中
			methodName
					.append(attributeName.substring(1, attributeName.length()));
		}
		return methodName.toString();
	}

	/**
	 * URL编码转换为字符串.
	 * 
	 * @param code
	 * @param charSet
	 * @return string
	 */
	public static String urlEncoderToString(String code, String charSet) {
		try {
			return URLDecoder.decode(code, charSet);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static Double roundDouble(double val, int precision) {
		Double ret = null;
		try {
			double factor = Math.pow(10, precision);
			ret = Math.floor(val * factor + 0.5) / factor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}