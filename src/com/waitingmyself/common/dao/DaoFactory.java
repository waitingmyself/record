package com.waitingmyself.common.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

/**
 * 
 * dao工厂 保证dao的单例
 * 
 * @author 李旭光
 * @version 2012-5-3
 */
public class DaoFactory {

	private static Map<Class<? extends BaseDao>, BaseDao> daos;

	private static final String TAG = DaoFactory.class.getSimpleName();

	static {
		daos = new HashMap<Class<? extends BaseDao>, BaseDao>();
	}

	/**
	 * 取得一个dao实例
	 * 
	 * @param cls
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends BaseDao> E getDAO(Class<E> cls, Context context) {
		if (daos.containsKey(cls)) {
			return (E) daos.get(cls);
		}
		synchronized (daos) {
			if (daos.containsKey(cls)) {
				return (E) daos.get(cls);
			}
			try {
				BaseDao dao = cls.getConstructor(Context.class).newInstance(
						context);
				daos.put(cls, dao);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "IllegalArgumentException:" + e.getMessage());
			} catch (SecurityException e) {
				Log.e(TAG, "SecurityException:" + e.getMessage());
			} catch (InstantiationException e) {
				Log.e(TAG, "InstantiationException:" + e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(TAG, "IllegalAccessException:" + e.getMessage());
			} catch (InvocationTargetException e) {
				Log.e(TAG, "InvocationTargetException:" + e.getMessage());
			} catch (NoSuchMethodException e) {
				Log.e(TAG, "NoSuchMethodException:" + e.getMessage());
			}
		}
		if (daos.containsKey(cls)) {
			return (E) daos.get(cls);
		}
		return null;
	}

	public static void clear() {
		daos.clear();
	}

}
