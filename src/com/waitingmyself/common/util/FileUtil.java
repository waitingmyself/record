package com.waitingmyself.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.util.Log;

/**
 * 文件帮助类
 * 
 * @author lixl
 * @version 2011-09-09
 */
public class FileUtil {

	private static String TAG = FileUtil.class.getName();

	/**
	 * 判断目录或文件是不否存在
	 * 
	 * @param path
	 *            目录或文件的路径
	 * @return boolean 目录或文件存在状态
	 */
	public static boolean notExists(String path) {
		return !exists(path);
	}

	/**
	 * 判断目录或文件是否存在
	 * 
	 * @param path
	 *            目录或文件的路径
	 * @return boolean 目录或文件存在状态
	 */
	public static boolean exists(String path) {
		return new File(path).exists();
	}

	/**
	 * 创建父目录
	 * 
	 * @param file
	 * @return boolean
	 */
	public static boolean mkParentsDir(File file) {
		if (file == null) {
			Log.i(TAG, "file is null!");
			return false;
		}
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (parent != null && parent.exists() == false) {
				return parent.mkdirs();
			}
		}
		return true;
	}

	/**
	 * 创建目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean mkdirs(File file) {
		if (file == null) {
			return false;
		}
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param srcFile
	 *            File 原文件
	 * @param destFile
	 *            File 复制后文件
	 */
	public static void copyFile(File srcFile, File destFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (srcFile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(srcFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(destFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
Log.v(TAG, "文件字节数:" + bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "复制单个文件操作出错");
			e.printStackTrace();
		}
	}
}