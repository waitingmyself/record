package com.waitingmyself.record.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

	private AppDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static AppDbHelper getInstance(Context context, String name,
			int version) {
		return new AppDbHelper(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// String sql = "CREATE TABLE IF NOT EXISTS  user (" + "name  TEXT(64),"
		// + "loginName  TEXT(64)," + "passwd  TEXT(64)" + ");";
		String sql = "CREATE TABLE IF NOT EXISTS weight ("
				+ "id  INTEGER PRIMARY KEY AUTOINCREMENT," + "user  INTEGER,"
				+ "time  bigint(30)," + "weight REAL(11,2),"
				+ "createTime  bigint(30)," + "updateFlag  INTEGER,"
				+ "updateTime bigint(30)," + "remoteFlag  INTEGER,"
				+ "memo  TEXT(256)" + ");";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
