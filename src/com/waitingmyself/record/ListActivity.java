package com.waitingmyself.record;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.waitingmyself.common.async.AsyncLoader;
import com.waitingmyself.common.async.BaseAsyncTaskExecute;
import com.waitingmyself.record.dao.AppDAO;

public class ListActivity extends BaseBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.list);
		super.onCreate(savedInstanceState);
		final ListView list = (ListView) findViewById(R.id.list_result_id);
		final AsyncLoader asyncLoader = new AsyncLoader();
		final AppDAO dao = new AppDAO(ListActivity.this);
		asyncLoader.putTask("listData", null, null, new BaseAsyncTaskExecute() {

			@Override
			public Object manifest(Object... params) {
				return dao.createSQLQuery(
						"select weight,time from weight order by time desc",
						null);
			}

			@Override
			public void callback(Object result) {
				@SuppressWarnings("unchecked")
				SimpleAdapter adapter = new ListViewAdapter(
						ListActivity.this,
						(List<? extends Map<String, ?>>) result,
						R.layout.list_item,
						new String[] { "weight", "time" },
						new int[] { R.id.list_item_weight, R.id.list_item_time });
				list.setAdapter(adapter);
			}
		});
	}

	@Override
	protected String getId() {
		return ListActivity.class.getSimpleName();
	}



}
