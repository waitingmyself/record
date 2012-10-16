package com.waitingmyself.common.async;

import java.util.Map;
import java.util.WeakHashMap;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 异步多线程任务加载类
 * 
 * @author lixl
 * @version 2011-09-15
 */
public class AsyncLoader {

	private static String LAG = AsyncLoader.class.getName();

	/**
	 * 任务MAP<br/>
	 * key: 异步任务名<br/>
	 * value: 异步任务
	 */
	private Map<String, AsyncTask<Object, Object, Object>> taskMap;

	/**
	 * 添加异步任务到任务MAP中
	 * 
	 * @param taskName
	 *            异步任务名
	 * @param strUrl
	 *            访问的URL字符串
	 * @param params
	 *            参数对象
	 * @param asyncTaskExecute
	 *            任务工程接口
	 * @return
	 */
	public Object putTask(final String taskName, final String strUrl,
			final Object params, final IAsyncTaskExecute asyncTaskExecute) {
		AsyncTask<Object, Object, Object> asyncTask = getTaskMap().get(taskName);
		if(asyncTask != null) {
			asyncTask.cancel(true);
		}
		asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				return asyncTaskExecute.manifest(params);
			}

			@Override
			protected void onProgressUpdate(Object... values) {
				asyncTaskExecute.updating(values);
			}

			@Override
			protected void onPostExecute(Object result) {
				Log.d(LAG, "onPostExecute---" + taskName);
				getTaskMap().remove(taskName);
				if (!isCancelled()) {
					Log.d(LAG, "isCancelled---" + taskName);
					asyncTaskExecute.callback(result);
				}
			}

			@Override
			protected void onPreExecute() {
				asyncTaskExecute.presentation();
			}

			@Override
			protected void onCancelled() {
				Log.d(LAG, "onCancelled---" + taskName);
				asyncTaskExecute.cancelled();
			}
		};
		getTaskMap().put(taskName, asyncTask);
		// 执行任务
		asyncTask.execute(taskName, strUrl);

		return null;
	}

	/**
	 * 获得当前任务列表
	 * 
	 * @return
	 */
	public Map<String, AsyncTask<Object, Object, Object>> getTaskMap() {
		if (taskMap == null) {
			taskMap = new WeakHashMap<String, AsyncTask<Object, Object, Object>>();
		}
		return taskMap;
	}

	/**
	 * 清空任务列表
	 */
	public void cancelAllTask() {
		for (Map.Entry<String, AsyncTask<Object, Object, Object>> entry : getTaskMap()
				.entrySet()) {
			String asyncTaskName = entry.getKey();
			AsyncTask<Object, Object, Object> asyncTask = entry.getValue();
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
				Log.v(LAG, asyncTaskName + "---cancel---" + true);
			} else {
				Log.v(LAG, asyncTaskName + "---cancel---" + false);
			}
		}
		getTaskMap().clear();
	}

	/**
	 * 判断当前线程是否已添加到任务列表
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasTask(Object key) {
		return getTaskMap().containsKey(key);
	}

	/**
	 * 从任务列表中删除
	 * 
	 * @param key
	 */
	public void removeTask(Object key) {
		getTaskMap().remove(key);
	}
}