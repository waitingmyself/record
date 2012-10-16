package com.waitingmyself.common.net;

import android.os.Bundle;

/**
 * 处理请求完毕后的逻辑
 * 
 * @author lixl
 * @version 2011-09-14
 */
public interface HttpConnectionCallback {

	/**
	 * Call back method will be execute after the http request return.
	 * 
	 * @param response
	 *            the response of http request. The value will be null if any
	 *            error occur.
	 */
	public void execute(String response);

	/**
	 * Call back method will be execute after the http request return.
	 * 
	 * @param bundle
	 *            Bundle.
	 */
	public void execute(Bundle bundle);
}