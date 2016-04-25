package com.werfei.soft.SendMaillDemo.utils; /**
 * @(#)ArithmeticUtils.java 1.00 2011-9-5 <br>
 * Copyright 2009～2019 MarsorStudio , Inc. All rights reserved.<br>
 * fhvsbgmy the same as qxc permitted.<br>
 * Use is subject to license terms.<br>
 */

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 算法通用模块 <br>
 * <br>
 * 提供常用的算法解决方案 <br>
 * <br>
 * 所有方法均为静态方法，可以直接使用。<br>
 * <br>
 * 
 * @author ThinkPad
 * @LastModified 2011-9-5
 */
public class AlgorithmicUtils {

	/**
	 * 排列组合公式之P(n,r),从n中选择r个不重复的元素按顺序排列
	 * 
	 * @param n
	 *            集合总数
	 * @param r
	 *            要获取并排序的数量
	 * */
	public static long P(int n, int r) {
		// 负数无法计算
		if (n < 0 || r < 0) {
			return -1;
		}

		long result = 1;
		// P(n,r) = n!/(n-r)! = n * (n-1) * (n-2) * (n-3) * ... (n-r+1)
		for (int i = n; i > (n - r); i--) {
			result = result * i;
		}

		return result;
	}

	/**
	 * 排列组合公式之C(n,r),从n中选择r个不重复的元素组成一个集合，不考虑集合中元素的顺序。
	 * 
	 * @param n
	 *            集合总数
	 * @param r
	 *            要获取元素的数量
	 * */
	public static long C(int n, int r) {
		long result = 1;
		// C(n,r) = n!/(r!*(n-r)!) = P(n,r)/r!

		long rF = factorial(r);
		rF = rF <= 0 ? 1 : rF;

		result = P(n, r) / rF;

		return result;
	}

	/**
	 * 获取指定数字的阶乘结果值。
	 * 
	 * @param originValue
	 *            阶乘的数字
	 * */
	public static long factorial(int originValue) {
		long result = 1;
		while (originValue > 1) {
			result = result * originValue;
			originValue--;
		}
		return result;
	}

	/**
	 * 获取指定数字的阶乘结果值。
	 * 
	 * @param originValue
	 *            阶乘的数字
	 * */
	public static BigInteger factorial(BigInteger originValue) {
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger("1");
		while (originValue.compareTo(one) > 0) {
			result = result.multiply(originValue);
			originValue = originValue.subtract(one);
		}
		return result;
	}

	/**
	 * 判断指定的参数中是否包含空值，如果任何一个参数为空值，则返回true。
	 * 
	 * @param objects
	 *            要测试的参数
	 * @return 是否含有空值
	 * */
	public static boolean hasEmpty(Object... objects) {
		if (objects == null || objects.length == 0) {
			return true;
		}
		for (Object obj : objects) {
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断指定的参数是否为空，包括空值，空字符串，空Collection，空Map，空数组，都会返回true
	 * 
	 * @param obj
	 *            要测试的参数
	 * @return 是否为空
	 * */
	public static boolean isEmpty(Object obj) {
		try {
			if (obj == null) {
				return true;
			}
			if (obj.toString().trim().length() == 0) {
				return true;
			}
			if (obj instanceof Collection<?>) {
				if (((Collection<?>) obj).size() == 0) {
					return true;
				}
			}
			if (obj instanceof Map<?, ?>) {
				if (((Map<?, ?>) obj).size() == 0) {
					return true;
				}
			}
			if (obj instanceof Object[] || obj.getClass().getName().startsWith("[")) {
				int length = Array.getLength(obj);
				if (length == 0) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 计算指定数组的平均值
	 * 
	 * @param numbers
	 *            数字数组
	 * @return 数组的平均值
	 * */
	public static <T extends Number> double getAverage(T... numbers) {
		if (AlgorithmicUtils.isEmpty(numbers)) {
			return -1;
		}

		double total = 0;
		for (T number : numbers) {
			total += number.doubleValue();
		}

		return total / numbers.length;
	}

	/**
	 * 从指定的数字数组中比较出最大的一个。
	 * 
	 * @param numbers
	 *            数字数组
	 * @return 最大的数字
	 * */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T getMax(T... numbers) {
		T max = (T) Integer.valueOf(0);

		for (T number : numbers) {
			if (number.doubleValue() > max.doubleValue()) {
				max = number;
			}
		}
		return max;
	}

	/**
	 * 从指定的数字数组中比较出最小的一个。
	 * 
	 * @param numbers
	 *            数字数组
	 * @return 最小的数字
	 * */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T getMin(T... numbers) {
		T min = (T) Integer.valueOf(Integer.MAX_VALUE);

		for (T number : numbers) {
			if (number.doubleValue() < min.doubleValue()) {
				min = number;
			}
		}
		return min;
	}

	/**
	 * 判断数组中是否存在指定的值,如果是一个List，判断是否存在指定的值，如果是一个Map，判断是否存在指定的Key,或者键值和该值相同的。
	 * 
	 * @param array
	 *            要查找的数组
	 * @param obj
	 *            要查看的值
	 * @return 是否存在
	 * */
	public static <T> boolean arrayContains(Object array, T obj) {
		if (array == null || obj == null) {
			return false;
		}
		if (array instanceof Object[]) {
			Object[] newObjs = (Object[]) array;
			for (Object o : newObjs) {
				if (o != null && o.equals(obj)) {
					return true;
				}
			}
		}
		if (array.getClass().getName().startsWith("[")) {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++) {
				if (Array.get(array, i) == null) {
					continue;
				}
				if (Array.get(array, i).toString().equals(obj.toString())) {
					return true;
				}
			}
		}

		if (array instanceof List<?>) {
			List<?> newObjs = (List<?>) array;
			for (Object o : newObjs) {
				if (o != null && o.equals(obj)) {
					return true;
				}
			}
		}
		if (array instanceof Map<?, ?>) {
			Map<?, ?> newObjs = (Map<?, ?>) array;
			for (Object o : newObjs.keySet()) {
				if (o != null && (o.equals(obj) || obj.equals(newObjs.get(o)))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 验证字符串是否可以转换为Integer类型
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isInteger(String param) {
		if (isEmpty(param)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher matcher = pattern.matcher(param);
		return matcher.find();
	}

	/**
	 * 验证字符串是否可以转换为Float类型
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isFloat(String param) {
		if (isEmpty(param)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[+|-]?\\d*\\.?\\d*$");
		Matcher matcher = pattern.matcher(param);
		return matcher.find();
	}

	/**
	 * TODO:: 在指定的数组中，取出n个元素来的所有排列组合。 要求
	 * n<= 数组的长度，并且n>0才有意义
	 * 
	 * @param art
	 *            指定的数组。
	 * @param n
	 *            要选出来元素的个数。
	 * */
	public static <T> List<T[]> combin(T[] art, int n) {
		ArrayList<T[]> lstResult = new ArrayList<T[]>();
		// 参数不合理。
		if (art == null || art.length == 0 || art.length < n || n <= 0) {
			return lstResult;
		}
		// 指定的数字与给定的数组长度一样，只取这一个好了。
		if (art.length == n) {
			lstResult.add(art);
			return lstResult;
		}

		// 如果选1个出来，那就遍历好了。
		if (n == 1) {
			for (int i = 0; i < art.length; i++) {
				ArrayList<T> lstTmp = new ArrayList<T>();
				lstTmp.add(art[i]);
				lstResult.add((T[]) lstTmp.toArray());
			}
			return lstResult;
		}

		// 去掉第一个。
		ArrayList<T> lst = new ArrayList<T>();
		for (int i = 1; i < art.length; i++) {
			lst.add(art[i]);
		}

		T[] subAry = (T[]) lst.toArray();
		lstResult.addAll(combin(subAry, n));

		// 拿到第一个
		T first = art[0];
		for (T[] t : combin(subAry, n - 1)) {
			ArrayList<T> tmpT = new ArrayList<T>();
			tmpT.add(first);
			for (int i = 0; i < t.length; i++) {
				tmpT.add(t[i]);
			}
			lstResult.add((T[]) tmpT.toArray());
		}
		return lstResult;
	}
}
