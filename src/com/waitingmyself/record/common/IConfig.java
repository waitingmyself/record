package com.waitingmyself.record.common;

/**
 * 系统配置
 * 
 * @author 李旭光
 * @version 2012-5-4
 */
public interface IConfig {

	/** 数据库版本 */
	public static int DB_VERSION = 1;

	/** 数据库名称 */
	public static String DB_NAME = "record.db";

	/** 数据库目录名称 */
	public static String DB_DIR = "databases";

	/** 项目名称 */
	public static String APP_NAME = "record";

	public static interface GAME {
		/** 加密常量 */
		public static String ENCRYPT_CONSTANT = "android";
		
//		public static String SERVER_URL = "http://192.168.8.14:8080/gmatch/";
		public static String SERVER_URL = "http://gmatch.sciemr.com/";

		public static String SERVER_URL_LIST = SERVER_URL + "game_data.do";
		
		public static String SERVER_URL_LOAD = SERVER_URL + "client/load.do";

		public static String SERVER_URL_SAVE = SERVER_URL + "client/save.do";

	}

}
