package com.waitingmyself.record.dao.model;

import com.waitingmyself.common.dao.bean.BaseDaoBean;

public class Weight extends BaseDaoBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4016207038558249628L;

	public Integer id;
	public int user;
	public long time;
	public double weight;
	public long createTime;
	public int updateFlag;
	public long updateTime;
	public int remoteFlag;
	public String memo;

}
