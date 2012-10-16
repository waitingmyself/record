package com.waitingmyself.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class RandomUtil {

	public static String[] randoms(String[] strs) {
		if (strs == null || strs.length == 0) {
			return strs;
		}
		int length = strs.length;
		int stLength = strs.length;
		String[] result = new String[length];
		Random random = new Random();
		for (int i = 0; i < stLength; i++) {
			int k = random.nextInt(length);
			result[i] = strs[k];
			strs = (String[]) remove((Object) strs, k);
			length = strs.length;
		}
		return result;
	}

	private static Object remove(Object array, int index) {
		int length = 0;
		if (array != null) {
			length = Array.getLength(array);
		}
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index
					+ ", Length: " + length);
		}

		Object result = Array.newInstance(array.getClass().getComponentType(),
				length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index
					- 1);
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(randoms(new String[] { "1", "2",
				"3", "4", "5", "6" })));
	}

}
