package com.waitingmyself.record.common;

import java.io.File;

import com.waitingmyself.common.constant.Const;
import com.waitingmyself.common.util.AndroidUtil;

/**
 * 应用系统的帮助类
 * 
 * @author lixl
 * @version 2011-09-22
 */
public class AppUtil implements IConfig {

	/**
	 * 获取当前应用在SD卡中的存储路径
	 * 
	 * @return String
	 */
	public static String getAppSDHomePath() {
		File file = new File(AndroidUtil.getSDStorageDirectory().getPath()
				+ Const.ROOT_PATH + APP_NAME);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath();
	}

	/**
	 * 取得升级文件存储路径
	 * 
	 * @return
	 */
	public static String getUpgradeFilePath() {
		return getAppSDHomePath() + Const.ROOT_PATH
				+ Const.FOLDER_NM_SAVE_UPGRADE_;
	}

	/**
	 * 获取应用系统数据库完整路径名称
	 * 
	 * @return
	 */
	public static String getAppSysDBLongName() {
		String appDBPaht = getAppSDHomePath() + Const.ROOT_PATH + DB_DIR;
		File file = new File(appDBPaht);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath() + Const.ROOT_PATH + DB_NAME;
	}
}