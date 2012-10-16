package com.waitingmyself.common.async;

/**
 * 任务工程基本实现类
 * 
 * @author lixl
 * @version 2011-09-15
 */
public abstract class BaseAsyncTaskExecute implements IAsyncTaskExecute {

	/**
	 * 必需重写些方法<br/>
	 * 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。这里将主要负责执行那些很耗时的后台计算工作。可以调用
	 * publishProgress方法来更新实时的任务进度。该方法是抽象方法，子类必须实现。
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public abstract Object manifest(Object... params);

	@Override
	public void updating(Object... value) {
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void presentation() {
	}

	/**
	 * 必需重写些方法<br/>
	 * 在manifest 执行完成后，callback 方法将被UI thread调用，后台的计算结果将通过该方法传递到UI thread.
	 * 
	 * @param result
	 */
	@Override
	public abstract void callback(Object result);
}