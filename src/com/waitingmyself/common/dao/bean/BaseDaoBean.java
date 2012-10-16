package com.waitingmyself.common.dao.bean;

import java.util.HashMap;

/**
 * 数据库表结构基本类
 * 
 * @author lixl
 * @version 2011-09-07
 */
public class BaseDaoBean extends HashMap<Object, Object> {

	private static final long serialVersionUID = 1L;

	/** 数据库表的主键（integer类型） */
	public int _id;
}