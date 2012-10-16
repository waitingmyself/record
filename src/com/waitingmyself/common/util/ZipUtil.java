package com.waitingmyself.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

import com.waitingmyself.common.constant.Const;

/**
 * zip gzip压缩/解压缩到String支持
 * 
 * @author lixl
 * @version 2011-09-09
 */
public class ZipUtil {

	private static String TAG = ZipUtil.class.getName();

	private static final int BUFF_SIZE = 10240000;

	/**
	 * 解压缩文件
	 * 
	 * @param inputStream
	 * @param unzipPath
	 * @throws Exception
	 */
	public static void unzip(InputStream inputStream, String unzipPath) {
		FileUtil.mkdirs(new File(unzipPath));

		ZipInputStream zipInputStream = null;
		FileOutputStream fileOutputStream = null;
		ZipEntry zipEntry = null;
		try {
			zipInputStream = new ZipInputStream(inputStream);
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				File zipFile = new File(unzipPath + Const.ROOT_PATH
						+ zipEntry.getName());
				// 目录
				if (zipEntry.isDirectory()) {
					zipFile.mkdirs();
				} else {
					// 文件
					FileUtil.mkParentsDir(zipFile);
					int n;
					fileOutputStream = new FileOutputStream(zipFile);
					byte[] buf = new byte[BUFF_SIZE];
					while ((n = zipInputStream.read(buf, 0, BUFF_SIZE)) > -1) {
						fileOutputStream.write(buf, 0, n);
					}
				}
				zipInputStream.closeEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.v(TAG, "FileNotFoundException!");
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "IOException!");
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if (zipInputStream != null) {
					zipInputStream.close();
				}
			} catch (IOException e) {
				Log.v(TAG, "file close is error!");
				e.printStackTrace();
			}
		}
	}
}