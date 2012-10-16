package com.waitingmyself.common.activity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.waitingmyself.record.R;

public abstract class BaseActivity extends Activity {

	protected static Map<String, Activity> activityMap = new LinkedHashMap<String, Activity>();

	protected abstract String getId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("test", getId() + " onCreate");
		addActivity(this.getId(), this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		Log.d("test", getId() + " onStart");
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		Log.d("test", getId() + " onRestart");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		Log.d("test", getId() + " onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d("test", getId() + " onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		Log.d("test", getId() + " onDestroy");
		super.onDestroy();
	}

	protected void showLongText(CharSequence msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	protected void showShortText(CharSequence msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	 // 创建菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    } 
    
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case 0:
        	clickToExit();
        }
        return true;
    } 

	/**
	 * 再次单击back键退出.
	 * 
	 * @param keyCode
	 * @param event
	 * @return boolean
	 */
	protected boolean clickToExit() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.text_exit_title);
		builder.setPositiveButton(R.string.bt_confirm,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finishAll();
					}
				});
		builder.setNegativeButton(R.string.bt_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.show();
		return true;
	}

	/**
	 * 退出时，结束所有的Activity
	 */
	protected void finishAll() {
		for (Iterator<Activity> iterator = activityMap.values().iterator(); iterator
				.hasNext();) {
			Activity type = iterator.next();
			if (type == null || type.isFinishing()) {
				continue;
			}
			type.finish();
		}
		activityMap.clear();
		finish();
		System.exit(0);
	}

	/**
	 * 如果已经存在 旧的Activity会执行finish函数.
	 * 
	 * @param id
	 * @param activity
	 */
	protected void addActivity(String id, Activity activity) {
		if (activityMap.containsKey(id)) {
			Activity old = activityMap.remove(id);
			old.finish();
		}
		activityMap.put(id, activity);
	}

}
