package com.waitingmyself.common.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.waitingmyself.common.constant.Const;

/**
 * Android帮助类
 * 
 * @author lixl
 * @version 2011-09-09
 */
public class AndroidUtil {

	private static String TAG = AndroidUtil.class.getName();

	/**
	 * 判断是否有SD卡<br/>
	 * 如果手机插入了SD卡，且应用程序具有访问SD的权限，返回true，否则返回false
	 * 
	 * @return boolean SD卡的状态
	 */
	public static boolean hasSDCard() {
		boolean hasSDCard = false;
		/*
		 * 如果手机插入了SD卡，且应用程序具有访问SD的权限，返回true，否则返回false
		 */
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			hasSDCard = true;
		}
		return hasSDCard;
	}

	/**
	 * 取得SD卡目录
	 * 
	 * @return File SD卡目录
	 */
	public static File getSDStorageDirectory() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 判断是否有网络连接功能
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNet(Context context) {
		boolean hasNet = false;
		// 取得ConnectivityManager对象（添加<uses-permission
		// android:name="android.permission.ACCESS_NETWORK_STATE" />权限）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		/*
		 * // mobile 3G Data Network State mobileState =
		 * connectivityManager.getNetworkInfo(
		 * ConnectivityManager.TYPE_MOBILE).getState(); // wifi State wifiState
		 * = connectivityManager.getNetworkInfo(
		 * ConnectivityManager.TYPE_WIFI).getState();
		 */
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			hasNet = true;
		}
		return hasNet;
	}

	/**
	 * 获取本地IP函数
	 * 
	 * @return String 本地IP地址
	 */
	public static String getLocalIPAddress() {
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
					.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf
						.getInetAddresses(); enumIPAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIPAddr.nextElement();
					// 如果不是回环地址
					if (!inetAddress.isLoopbackAddress()) {
						// 直接返回本地IP地址
						ip = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			Log.e("Error", e.toString());
		}
		return ip;
	}

	/**
	 * 取得网络类型
	 * 
	 * @param context
	 *            上下文件对象
	 * @return int 网络类型：没有网络（-1）；未知网络（0）；WIFI网络（1）；手机网络（2）
	 */
	public static int getNetType(Context context) {
		int intRes = -1;
		// 获取系统的连接服务对象
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取网络连接的信息
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {

		} else {
			// 如果是WIFI网络
			if (ConnectivityManager.TYPE_WIFI == ni.getType()) {
				intRes = 1;
				// 如果是手机网络
			} else if (ConnectivityManager.TYPE_MOBILE == ni.getType()) {
				intRes = 2;
				// 未知网络
			} else {
				intRes = 0;
			}
		}

		return intRes;
	}

	/**
	 * 获取设备状态
	 * 
	 * @param context
	 * @return TelephonyManager
	 */
	public static TelephonyManager getTelephonyManager(Context context) {
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取本机号码<br />
	 * 注意! 不是所有环境都能取得到号码
	 * 
	 * @param context
	 * @return String
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean hasConnecting(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = getTelephonyManager(context);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}

		int netType = info.getType();
		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (ConnectivityManager.TYPE_MOBILE == netType && TelephonyManager.NETWORK_TYPE_UMTS == netSubtype
				&& !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	/**
	 * 取得SDCARD的可以空间大小（byte）
	 * 
	 * @return long SDCARD的可以空间大小
	 */
	public static long getSDCardFreeSize() {
		long nSdFreeSize = 0;
		if (hasSDCard()) {
			// 取得SDCARD文件路径
			File pathFile = getSDStorageDirectory();
			StatFs statFs = new StatFs(pathFile.getPath());

			// 取得SdCard上每个Block的SIZE
			int nBlockSize = statFs.getBlockSize();

			// 取得可供程序使用的Block数量
			int nAvailaBlock = statFs.getAvailableBlocks();

			// 计算SDCard剩余大小MB
			nSdFreeSize = nAvailaBlock * nBlockSize;

			if (nSdFreeSize < 0) {
				nSdFreeSize = 0 - nSdFreeSize;
			}

			// // 取得SDCard上的BLOCK总数
			// int nTotalBlocks = statFs.getBlockCount();
			// // 计算SDCard总容量大小MB
			// long nSdTotalSize = nTotalBlocks * nBlockSize / 1024;
			// // // 取得剩下的所有Block的数量（包括预留的一般程序无法使用的块）
			// int nFreeBlock = statFs.getFreeBlocks();
			// // 取得可供程序使用的Block数量
			// int nAvailaBlock = statFs.getAvailableBlocks();
		}
		Log.i(TAG, "SDCARD FREE SIZE IS: " + nSdFreeSize + "(byte).");
		return nSdFreeSize;
	}

	/**
	 * 取得应用程序共享对象
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(Const.PREFERENCES_NAME, Activity.MODE_PRIVATE);
	}

	/**
	 * 取得编辑应用程序共享对象
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
		return getSharedPreferences(context).edit();
	}

	/**
	 * WiFi 是否可用
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();

		// ConnectivityManager connectivity = (ConnectivityManager)
		// context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// if (connectivity != null) {
		// NetworkInfo[] info = connectivity.getAllNetworkInfo();
		// if (info != null) {
		// for (int i = 0; i < info.length; i++) {
		// if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
		// return true;
		// }
		// }
		// }
		// }
		// return false;
	}

	/**
	 * 隐藏软键盘
	 * @param activity Activity对象
	 */
	public static void hiddenSoftInput(Activity activity) {
		View v = activity.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}