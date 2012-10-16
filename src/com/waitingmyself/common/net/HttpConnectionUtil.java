package com.waitingmyself.common.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * HTTP链接帮助类
 * 
 * @author lixl
 * @version 2011-10-12
 */
public class HttpConnectionUtil {

	private final static String TAG = HttpConnectionUtil.class.getName();

	public static final String DOWNLOAD_FILE = "downloadFile";

	private static final int BUFFER = 10240;
	/** 符号字符串：? */
	private static final String SYMBOL_QUESTION_MARK = "?";
	/** 符号字符串：& */
	private static final String SYMBOL_AND = "&";
	/** 符号字符串：= */
	private static final String SYMBOL_EQUAL = "=";
	/** 响应结果内容 */
	private static final String RESPONSE_CONTENT = "responseContent";

	private static CookieStore cookieStore;

	/** HTTP方法：GET| POST */
	public static enum HttpMethod {
		GET, POST, PUT
	}

	/**
	 * POST方法异步连接处理（带参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void postAsync(final String url,
			final Map<String, String> params,
			final HttpConnectionCallback callback) {
		asyncConnect(url, params, HttpMethod.POST, callback);
	}

	/**
	 * POST方法异步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void postAsync(final String url,
			final HttpConnectionCallback callback) {
		asyncConnect(url, HttpMethod.POST, callback);
	}

	/**
	 * POST方法同步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void postAync(final String url,
			final HttpConnectionCallback callback) {
		syncConnect(url, null, HttpMethod.POST, callback);
	}

	/**
	 * POST方法同步连接处理（有参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void postAync(final String url,
			final Map<String, String> params,
			final HttpConnectionCallback callback) {
		syncConnect(url, params, HttpMethod.POST, callback);
	}

	/**
	 * GET方法异步连接处理（带参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void getAsync(final String url,
			final Map<String, String> params,
			final HttpConnectionCallback callback) {
		asyncConnect(url, params, HttpMethod.GET, callback);
	}

	/**
	 * GET方法异步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void getAsync(final String url,
			final HttpConnectionCallback callback) {
		asyncConnect(url, HttpMethod.GET, callback);
	}

	/**
	 * GET方法同步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void getAync(final String url,
			final HttpConnectionCallback callback) {
		syncConnect(url, null, HttpMethod.GET, callback);
	}

	/**
	 * GET方法同步连接处理（有参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void getAync(final String url,
			final Map<String, String> params,
			final HttpConnectionCallback callback) {
		syncConnect(url, params, HttpMethod.GET, callback);
	}

	/**
	 * 异步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param method
	 *            处理方式（GET|POST）
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void asyncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		asyncConnect(url, null, method, callback);
	}

	/**
	 * 异步连接处理（带参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param method
	 *            处理方式（GET|POST）
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void asyncConnect(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {

		/* ========== Handler执行Runnable时开启新线程 START ================= */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				Log.d(TAG, "handleMessage　线程ID: "
						+ Thread.currentThread().getId());
				if (bundle != null) {
					callback.execute(bundle.getString(RESPONSE_CONTENT));
				}
			}
		};

		new Thread() {
			@Override
			public void run() {
				String strResponse = httpConnectString(httpConnect(url, params,
						method));
				Bundle data = new Bundle();
				data.putString(RESPONSE_CONTENT, strResponse);
				Message msg = new Message();
				Log.d(TAG, "Runnable　线程ID: " + Thread.currentThread().getId());
				msg.setData(data);
				handler.sendMessage(msg);
			}
		}.start();

		/* ========== Handler执行Runnable时开启新线程 END ================= */
	}

	/**
	 * 异步连接处理（带参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param method
	 *            处理方式（GET|POST）
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void asyncConnectHttpEntity(final String url,
			final HttpMethod method, final File file,
			final HttpConnectionCallback callback) {

		/* ========== Handler执行Runnable时开启新线程 START ================= */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				if (bundle != null) {
					callback.execute(bundle);
				}
			}
		};

		new Thread() {
			@Override
			public void run() {
				BufferedInputStream in = null;
				BufferedOutputStream out = null;
				try {
					HttpEntity httpEntity = httpConnect(url, null, method);
					if (httpEntity != null) {
						InputStream is = httpEntity.getContent();
						in = new BufferedInputStream(is, BUFFER);
						if (!file.exists()) {
							File f = file.getParentFile();
							if (f != null) {

							}
						}
						out = new BufferedOutputStream(new FileOutputStream(
								file, false), BUFFER);

						byte buf[] = new byte[BUFFER];
						int size = -1;
						while ((size = in.read(buf)) != -1) {
							out.write(buf, 0, size);
						}
						out.flush();
					}

					Bundle data = new Bundle();
					data.putSerializable(DOWNLOAD_FILE, (Serializable) file);
					Message msg = new Message();
					msg.setData(data);
					handler.sendMessage(msg);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

		/* ========== Handler执行Runnable时开启新线程 END ================= */
	}

	/**
	 * 同步连接处理（无参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param method
	 *            处理方式（GET|POST）
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void syncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		syncConnect(url, null, method, callback);
	}

	/**
	 * 同步连接处理（带参数MAP）
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param method
	 *            处理方式（GET|POST）
	 * @param callback
	 *            处理结果的回调类
	 */
	public static void syncConnect(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		callback.execute(httpConnectString(httpConnect(url, params, method)));
	}

	public static String httpConnectString(HttpEntity httpEntity) {

		// 定义访问结果字符串
		String strResponseResult = null;
		// 定义BufferedReader对象
		BufferedReader reader = null;

		if (httpEntity != null) {
			try {
				// 取得返回结果对象
				reader = new BufferedReader(new InputStreamReader(
						httpEntity.getContent()));
				StringBuilder sbResponseResult = new StringBuilder();
				for (String lineInfo = reader.readLine(); lineInfo != null; lineInfo = reader
						.readLine()) {
					sbResponseResult.append(lineInfo);
				}
				strResponseResult = sbResponseResult.toString();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return strResponseResult;
	}

	/**
	 * HTTP连接处理
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param method
	 *            处理方式（GET|POST）
	 * @return
	 */
	public static HttpEntity httpConnect(final String url,
			final Map<String, String> params, final HttpMethod method) {
		Log.d(TAG, "httpConnect　线程ID: " + Thread.currentThread().getId());
		// 定义访问结果对象
		HttpEntity httpEntity = null;
		try {
			// 创建HttpClient对象
			DefaultHttpClient client = new DefaultHttpClient();
			if (cookieStore != null) {
				client.setCookieStore(cookieStore);
			}
			// 取得HttpUriRequest对象
			HttpUriRequest request = getUriRequest(url, params, method);
			// 执行Http连接处理，并取得返回HttpResponse对象
			HttpResponse response = client.execute(request);
			/*
			 * 如果请求成功，取得返回结果
			 */
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				httpEntity = response.getEntity();
			} else {
				Log.d(TAG, "httpConnect()；URL：" + url + "\n");
			}
			cookieStore = client.getCookieStore();
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return httpEntity;
	}

	/**
	 * 取得HttpUriRequest对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @param method
	 *            处理方式（GET|POST）
	 * @return HttpUriRequest HttpUriRequest对象
	 */
	private static HttpUriRequest getUriRequest(String url,
			Map<String, String> params, HttpMethod method) {
		/*
		 * 处理方式为POST类型，取得HttpUriRequest对象
		 */
		if (HttpMethod.GET.equals(method)) {
			return createGetRequestUri(url, params);
		} else {
			return createPostOrPutRequestUri(url, params, method);
		}
	}

	/**
	 * 创建Post的HttpUriRequest对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @return HttpUriRequest HttpUriRequest对象
	 */
	@SuppressWarnings("deprecation")
	private static HttpUriRequest createPostOrPutRequestUri(String url,
			Map<String, String> params, HttpMethod method) {
		// 参数列表
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		/*
		 * URL参数MAP对象不为null时，将其转换为参数列表对象
		 */
		if (params != null && params.size() > 0) {
			for (String name : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(name, params
						.get(name)));
			}
		} else {
			Log.i(TAG, "param map is null!");
		}
		try {
			HttpEntityEnclosingRequestBase request;
			if (HttpMethod.POST == method) {
				request = new HttpPost(url);
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));
			} else {
				request = new HttpPut(url);
				BasicHttpEntity entry = new BasicHttpEntity();
				entry.setContentType("application/json");
				entry.setContent(new StringBufferInputStream(params.get("data")));
				((HttpPut) request).setEntity(entry);
			}
			return request;
		} catch (UnsupportedEncodingException e) {
			// Should not come here, ignore me.
			Log.e(TAG, e.getMessage(), e);
			throw new java.lang.RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 创建get的HttpUriRequest对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            URL参数MAP
	 * @return HttpUriRequest HttpUriRequest对象
	 */
	private static HttpUriRequest createGetRequestUri(String url,
			Map<String, String> params) {
		// get方式，URL字符串
		StringBuilder sbUrl = new StringBuilder(url);
		/*
		 * URL参数MAP对象不为null时，将其转换为参数字符串
		 */
		if (params != null && params.size() > 0) {
			/*
			 * 如果URL字符串中不包含“？”则在URL字符串后添加“？”
			 */
			if (url.indexOf(SYMBOL_QUESTION_MARK) < 0) {
				sbUrl.append(SYMBOL_QUESTION_MARK);
			}

			/*
			 * 根据参数MAP，组装参数字符串
			 */
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sbUrl.append(entry.getKey()).append(SYMBOL_EQUAL)
						.append(URLEncoder.encode(entry.getValue()))
						.append(SYMBOL_AND);
			}
			// 删除“参数字符串”的最后字符，多余的“&”字符
			int length = sbUrl.length();
			sbUrl.delete(length - 1, length);
		} else {
			Log.i(TAG, "param map is null!");
		}
		return new HttpGet(sbUrl.toString());
	}

	public static void downSaveFile(final String url, final String savePath,
			final HttpConnectionCallback callback) {
		asyncConnectHttpEntity(url, HttpMethod.GET, new File(savePath),
				callback);
	}
}