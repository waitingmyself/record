package com.waitingmyself.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.waitingmyself.common.dao.bean.BaseDaoBean;
import com.waitingmyself.common.dao.constant.BaseDBConst;
import com.waitingmyself.common.util.BaseUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 操作数据库的基本类
 * 
 * @author lixl
 * @version 2011-09-08
 * @author 李旭光
 * @version 2012-05-03<br>
 *          把helper的操作数据库的方法转移到BaseDao中
 */
public abstract class BaseDao {

	private static final String TAG = BaseDao.class.getSimpleName();

	/** 数据库操作帮助类 */
	protected SQLiteOpenHelper dbHelper;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文环境
	 */
	public BaseDao(Context context) {
		initDBHelper(context);
	}

	/**
	 * 初始化操作DB的帮助类
	 */
	protected abstract void initDBHelper(Context context);

	protected SQLiteDatabase getWritableDatabase() {
		return dbHelper.getWritableDatabase();
	}

	protected SQLiteDatabase getReadableDatabase() {
		return dbHelper.getReadableDatabase();
	}

	/**
	 * 插入数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param prop
	 *            数据MAP
	 * @return long 数据记录主键
	 */
	public long save(String tableName, Map<String, Object> prop) {
		// 更新数据 并获取新数据的id
		ContentValues cv = new ContentValues();
		for (Iterator<String> iterator = prop.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			cv.put(key, BaseUtil.nullToSpace(prop.get(key)));
		}
		return save(tableName, cv);
	}

	/**
	 * 插入数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param bean
	 *            基本表数据对象
	 * @return long 数据记录主键
	 */
	public long save(String tableName, BaseDaoBean bean) {
		// 更新数据 并获取新数据的id
		ContentValues cv = new ContentValues();

		Field[] fields = bean.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			createContentValuesByFields(bean, field, cv);
		}

		long saveId = save(tableName, cv);
		bean._id = (int) saveId;

		return saveId;
	}

	/**
	 * 插入数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param cv
	 *            数据列表
	 * @return long 数据记录主键
	 */
	public long save(String tableName, ContentValues cv) {
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(tableName, null, cv);
		if (-1 == id) {
			Log.e(TAG, "insert SQL is error: tableName" + tableName
					+ "\nparams" + cv.toString());
		}
		closeDB(db);
		return id;
	}

	/**
	 * 更新数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param prop
	 *            数据MAP
	 * @return long 数据记录主键
	 */
	public long update(String tableName, Map<String, Object> prop) {
		// 更新数据 并获取新数据的id
		ContentValues cv = new ContentValues();
		for (Iterator<String> iterator = prop.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			cv.put(key, BaseUtil.nullToSpace(prop.get(key)));
		}
		return update(tableName, cv);
	}

	/**
	 * 更新数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param bean
	 *            基本表数据对象
	 * @return long 数据记录主键
	 */
	public long update(String tableName, BaseDaoBean bean) {
		// 更新数据 并获取新数据的id
		ContentValues cv = new ContentValues();

		Field[] fields = bean.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			createContentValuesByFields(bean, field, cv);
		}

		return update(tableName, cv);
	}

	/**
	 * 更新数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param cv
	 *            数据列表
	 * @return long 数据主键
	 */
	public long update(String tableName, ContentValues cv) {
		SQLiteDatabase db = getWritableDatabase();
		long id = db.update(tableName, cv, BaseDBConst.KEY_ID_WHERE,
				new String[] { cv.getAsString(BaseDBConst.KEY_ID) });
		closeDB(db);
		return id;
	}

	/**
	 * 删除数据库的表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param _id
	 *            表的主键
	 * @return int 删除记录数
	 */
	public int delete(String tableName, long _id) {
		SQLiteDatabase db = getWritableDatabase();
		int rs = db.delete(tableName, BaseDBConst.KEY_ID_WHERE,
				new String[] { String.valueOf(_id) });
		closeDB(db);
		return rs;
	}

	/**
	 * 清空表里的数据
	 * 
	 * @param tableName
	 *            表名
	 * @return int 删除记录数
	 */
	public int clearTabel(String tableName) {
		return delete(tableName, null, null);
	}

	/**
	 * 清空表里的数据
	 * 
	 * @param tableName
	 *            表名
	 * @return int 删除记录数
	 */
	public int delete(String tableName, String whereClause, String[] whereArgs) {

		SQLiteDatabase db = getWritableDatabase();
		int rs = db.delete(tableName, whereClause, whereArgs);
		closeDB(db);
		return rs;
	}

	/**
	 * 执行SQL
	 * 
	 * @param sql
	 *            执行的SQL语句
	 * @param prop
	 *            执行的SQL语句的对应参数列表
	 */
	public void execSQL(String sql, Map<String, Object> prop) {

		SQLiteDatabase db = getWritableDatabase();
		String execSql = createExecuteSQL(sql, prop);
		try {
			db.execSQL(execSql);
		} catch (SQLException e) {
			Log.e(TAG, "SQL is error: " + execSql);
		}
		closeDB(db);
	}

	/**
	 * 创建ContentValues对象通过Field对象
	 * 
	 * @param bean
	 *            数据结构
	 * @param field
	 *            Field对象
	 * @param cv
	 *            ContentValues对象
	 */
	private void createContentValuesByFields(BaseDaoBean bean, Field field,
			ContentValues cv) {
		String key = field.getName();
		Object keyValue = null;
		Class<?> clazz = field.getType();
		try {
			if (BaseDBConst.KEY_ID.equals(field.getName())) {
				return;
			}
			keyValue = field.get(bean);
			if (clazz != null && keyValue != null) {
				if (clazz.isPrimitive()) {
					if (Double.TYPE == clazz) {
						cv.put(key, field.getDouble(bean));
					} else if (Float.TYPE == clazz) {
						cv.put(key, field.getFloat(bean));
					} else if (Integer.TYPE == clazz) {
						cv.put(key, field.getInt(bean));
					} else if (Long.TYPE == clazz) {
						cv.put(key, field.getLong(bean));
					} else if (Short.TYPE == clazz) {
						cv.put(key, field.getShort(bean));
					}
				} else if (clazz == byte[].class) {
					cv.put(key, (byte[]) keyValue);
				} else if (Integer.class == clazz) {
					field.set(key, (Integer) keyValue);
				} else if (Long.class == clazz) {
					field.set(key, (Long) keyValue);
				} else if (Float.class == clazz) {
					field.set(key, (Float) keyValue);
				} else if (Double.class == clazz) {
					field.set(key, (Double) keyValue);
				} else if (Boolean.TYPE == clazz) {
					cv.put(key, (Boolean) keyValue);
				} else if (Byte.TYPE == clazz) {
					cv.put(key, (Byte) keyValue);
				} else if (String.class == clazz) {
					cv.put(key, (String) keyValue);
				}
			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG, bean.getClass().getName() + "【" + key + "】的值是:"
					+ keyValue);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询数据库表数据列表
	 * 
	 * @param query
	 *            查询SQL
	 * @param className
	 *            返回对象的类名（全名）
	 * @return List<?> 对象列表
	 */
	public <E> List<E> createSQLQuery(String query, Class<E> cls) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		List<E> rs = cursorToList(c, cls);
		closeCursor(c);
		closeDB(db);
		return rs;
	}

	/**
	 * 查询数据库表数据列表
	 * 
	 * @param query
	 *            查询SQL
	 * @param className
	 *            返回对象的类名（全名）
	 * @return List<?> 对象列表
	 */
	public <E> List<E> createSQLQuery(SQLiteDatabase db, String query,
			Class<E> cls) {
		Cursor c = db.rawQuery(query, null);
		List<E> rs = cursorToList(c, cls);
		closeCursor(c);
		return rs;
	}

	/**
	 * 查询
	 * 
	 * @param query
	 * @param className
	 * @param prop
	 * @return List
	 */
	public <E> List<E> createSQLQuery(String query, Class<E> cls,
			Map<String, Object> prop) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(createExecuteSQL(query, prop), null);
		List<E> rs = cursorToList(c, cls);
		closeCursor(c);
		closeDB(db);
		return rs;
	}

	/**
	 * count查询<br/>
	 * 也可以使用普通查询完成,获取List中Map对象的第一个值即可key为查询的字段,如count(*)
	 * 
	 * @param query
	 * @param prop
	 * @return long count
	 */
	public long createCountSQLQuery(String query, Map<String, Object> prop) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(createExecuteSQL(query, prop), null);
		c.moveToFirst();
		long count = c.getLong(0);
		closeCursor(c);
		closeDB(db);
		return count;
	}

	/**
	 * 查询
	 * 
	 * @param query
	 * @param className
	 * @param prop
	 * @param offset
	 * @param maxRows
	 * @return List
	 */
	public <E> List<E> createSQLQuery(String query, Class<E> cls,
			Map<String, Object> prop, int offset, int maxRows) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(
				limit(createExecuteSQL(query, prop), offset, maxRows), null);
		List<E> rs = cursorToList(c, cls);
		closeCursor(c);
		closeDB(db);
		return rs;
	}

	/**
	 * 将游标结果集转成指定类名的列表
	 * 
	 * @param cursor
	 *            游标
	 * @param className
	 *            类名（完全路径：包名+类名）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <E> List<E> cursorToList(Cursor cursor, Class<E> cls) {
		List<E> rs = new ArrayList<E>();
		if (!BaseUtil.isEmpty(cursor)) {
			int count = cursor.getCount();
			// 游标移动到第一条记录
			cursor.moveToFirst();
			int columnCount = cursor.getColumnCount();

			for (int i = 0; i < count; i++) {
				if (cls == null) {
					BaseDaoBean bean = new BaseDaoBean();
					for (int j = 0; j < columnCount; j++) {
						bean.put(cursor.getColumnName(j), cursor.getString(j));
					}
					rs.add((E) bean);
				} else {
					try {
						E obj = cls.newInstance();
						for (int j = 0; j < columnCount; j++) {
							setAttributeValue(obj, j, cursor);
						}
						rs.add(obj);
					} catch (Exception e) {
						Log.e(TAG, "数据转换失败");
						break;
					}
				}
				cursor.moveToNext();
			}
		}
		return rs;
	}

	/**
	 * 设置对象的指定属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setAttributeValue(Object obj, int index, Cursor cursor) {
		try {
			String fieldName = cursor.getColumnName(index);
			Field field = obj.getClass().getField(fieldName);
			Class<?> clazz = field.getType();
			if (clazz.isPrimitive()) {
				if (Short.TYPE == clazz) {
					field.setShort(obj, cursor.getShort(index));
				} else if (Integer.TYPE == clazz) {
					field.setInt(obj, cursor.getInt(index));
				} else if (Long.TYPE == clazz) {
					field.setLong(obj, cursor.getLong(index));
				} else if (Float.TYPE == clazz) {
					field.setFloat(obj, cursor.getFloat(index));
				} else if (Double.TYPE == clazz) {
					field.setDouble(obj, cursor.getDouble(index));
				}
			} else if (clazz.isArray()) {
				field.set(obj, cursor.getBlob(index));
			} else if (Integer.class == clazz) {
				field.set(obj, Integer.valueOf(cursor.getInt(index)));
			} else if (Long.class == clazz) {
				field.set(obj, Long.valueOf(cursor.getLong(index)));
			} else if (Float.class == clazz) {
				field.set(obj, Float.valueOf(cursor.getFloat(index)));
			} else if (Double.class == clazz) {
				field.set(obj, Double.valueOf(cursor.getDouble(index)));
			} else if (String.class == clazz) {
				field.set(obj, cursor.getString(index));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页
	 * 
	 * @param query
	 * @param offset
	 * @param maxRows
	 * @return String SQL
	 */
	protected static String limit(String query, int offset, int maxRows) {
		StringBuilder s = new StringBuilder();

		s.append(query);
		s.append(" LIMIT ");
		s.append(offset);
		s.append(BaseDBConst.COMMA);
		s.append(maxRows);
		s.append(BaseDBConst.SPACE);

		return s.toString();
	}

	/**
	 * 创建可运行SQL语句。 替换sql语句中的命名参数,如果命名参数没有找到,返回命名参数的名字.
	 * sql参数：指定为（：param）类型，以“:”开头+变量名。例:--> :param
	 * 
	 * @param sql
	 *            SQL语句（带参数）
	 * @param prop
	 *            参数MAP（参数名，参数值）
	 * @return String 可运行SQL语句
	 */
	protected String createExecuteSQL(String sql, Map<String, Object> prop) {
		/*
		 * 如果参数信息为空时，直接返回参数SQL
		 */
		if (BaseUtil.isEmpty(sql) || prop == null) {
			return sql;
		}
		// 获取sql中的所有变量参数
		List<String> var = BaseUtil.matcherList(BaseDBConst.SQL_VARIABLES, sql,
				1);
		if (var == null || var.size() == 0) {
			return sql;
		}
		StringBuilder s = new StringBuilder();
		// 循环所有参数
		for (Iterator<String> iterator = var.iterator(); iterator.hasNext();) {
			s.delete(0, s.length());
			String key = iterator.next();
			Object value = prop.get(key);
			/*
			 * 如果取得的值为null时，将参数变量替换为“?” 如果取得的值是数值类型时，将参数变量替换为“值”
			 * 如果取得的值是字符串类型时，将参数变量替换为“‘”+值+“’” 如果取得的值是集合类型时，将参数变量替换为集合转换in查询参数
			 * 取得的值是其他类型时，将参数变量替换为“‘”+值+“’”
			 */
			if (value == null) {
				// 参数不能为空
				sql = sql.replaceFirst(BaseDBConst.SQL_VARIABLES,
						BaseDBConst.QUESTION_MARK);
			} else if (value instanceof Number) {
				// 数值类型
				sql = sql.replaceFirst(BaseDBConst.SQL_VARIABLES,
						value.toString());
			} else if (value instanceof String) {
				// 字符串类型
				s.append(BaseDBConst.APOSTROPHE);
				s.append(value);
				s.append(BaseDBConst.APOSTROPHE);
				sql = sql.replaceFirst(BaseDBConst.SQL_VARIABLES, s.toString());
			} else if (value instanceof Collection) {
				// 集合 对应in查询
				sql = sql.replaceFirst(BaseDBConst.SQL_VARIABLES,
						collectionToInSQLVariables((Collection<?>) value));
			} else {
				// 其它情况作为普通String处理
				s.append(BaseDBConst.APOSTROPHE);
				s.append(value);
				s.append(BaseDBConst.APOSTROPHE);
				sql = sql.replaceFirst(BaseDBConst.SQL_VARIABLES, s.toString());
			}
		}
		return sql;
	}

	/**
	 * 转换in查询参数
	 * 
	 * @param collection
	 * @return String
	 */
	protected String collectionToInSQLVariables(Collection<?> collection) {
		if (collection == null) {
			return null;
		}
		StringBuilder s = new StringBuilder();
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object == null) {
				continue;
			}
			// 数字
			if (object instanceof Number) {
				s.append(object);
				s.append(BaseDBConst.COMMA);
				// 字符串
			} else if (object instanceof String) {
				s.append(BaseDBConst.APOSTROPHE);
				s.append(object);
				s.append(BaseDBConst.APOSTROPHE);
				s.append(BaseDBConst.COMMA);
				// 其它
			} else {
				s.append(BaseDBConst.APOSTROPHE);
				s.append(object);
				s.append(BaseDBConst.APOSTROPHE);
				s.append(BaseDBConst.COMMA);
			}
		} // end for
			// 不要最后一个','
		return s.substring(0, s.length() - 1);
	}

	/**
	 * 关闭游标
	 * 
	 * @param cursor
	 */
	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
			Log.d(TAG, "close cursor.");
		}
	}

	/**
	 * 关闭数据库
	 * 
	 * @param db
	 */
	protected void closeDB(SQLiteDatabase db) {
		if (db != null) {
			db.close();
			Log.d(TAG, "close db.");
		}
	}

}
