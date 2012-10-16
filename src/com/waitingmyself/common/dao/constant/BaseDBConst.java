package com.waitingmyself.common.dao.constant;

/**
 * 数据库常量类
 * 
 * @author lixl
 * @version 2011-09-08
 */
public class BaseDBConst {
	/** 表的主键（由于Android的某些组件应用时，可能会用到这个KEY，意见在创建表时，统一添加此字段为为主键，long类型） */
	public static final String KEY_ID = "_id";
	/** 表内数据操作时的主键条件 */
	public static final String KEY_ID_WHERE = " where _id=?";
	/** SQL命名参数变量(以：开头，后面跟变量名) */
	public final static String SQL_VARIABLES = "\\(:(.*?)\\)";
	/** ? */
	public final static String QUESTION_MARK = "?";
	/** ' */
	public final static String APOSTROPHE = "'";
	/** comma '.' */
	public final static String COMMA = ",";
	/** ' ' */
	public final static String SPACE = " ";
}