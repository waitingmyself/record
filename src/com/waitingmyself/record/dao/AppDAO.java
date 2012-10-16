package com.waitingmyself.record.dao;

import java.util.Map;

import android.content.Context;

import com.waitingmyself.common.dao.BaseDao;
import com.waitingmyself.record.common.AppUtil;
import com.waitingmyself.record.common.IConfig;
import com.waitingmyself.record.dao.helper.AppDbHelper;
import com.waitingmyself.record.dao.model.User;

public class AppDAO extends BaseDao {

	public AppDAO(Context context) {
		super(context);
	}

	@Override
	protected void initDBHelper(Context context) {
		super.dbHelper = AppDbHelper.getInstance(context,
				AppUtil.getAppSysDBLongName(), IConfig.DB_VERSION);
	}

	public void addWeight(Map<String, Object> db) {
		save("weight", db);
	}
	
	public void addUser(User user) {
		save("user", user);
	}

}
