package com.waitingmyself.common.async;

/**
 * 任务工程接口
 * 
 * @author lixl
 * @version 2011-09-15
 */
public interface IAsyncTaskExecute {

	/**
	 * 必需重写些方法<br/>
	 * 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。这里将主要负责执行那些很耗时的后台计算工作。可以调用
	 * publishProgress方法来更新实时的任务进度。该方法是抽象方法，子类必须实现。
	 * 
	 * @param params
	 * @return
	 */
	public abstract Object manifest(Object... params);

	/**
	 * 在publishProgress方法被调用后，UI thread将调用这个方法从而在界面上展示任务的进展情况，例如通过一个进度条进行展示。
	 * 
	 * @param value
	 */
	public void updating(Object... value);

	/**
	 * 任务取消时，产生的对应操作
	 */
	public void cancelled();

	/**
	 * 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。
	 */
	public void presentation();

	/**
	 * 必需重写些方法<br/>
	 * 在manifest 执行完成后，callback 方法将被UI thread调用，后台的计算结果将通过该方法传递到UI thread.
	 * 
	 * @param result
	 */
	public abstract void callback(Object result);
}