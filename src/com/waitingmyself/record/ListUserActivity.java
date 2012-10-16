package com.waitingmyself.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.waitingmyself.common.net.HttpConnectionCallback;
import com.waitingmyself.common.net.HttpConnectionUtil;
import com.waitingmyself.common.net.HttpConnectionUtil.HttpMethod;
import com.waitingmyself.record.common.ServiceUtil;

public class ListUserActivity extends BaseBackActivity {
	
	private static String TAG = ListUserActivity.class.getSimpleName();
	
	private ArrayAdapter<String> adapter = null;
	
	 SimpleAdapter detailAdapter = null;
	
	Button back = null;
	
	ListView listView = null;

	@Override
	protected String getId() {
		return ListUserActivity.class.getSimpleName();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.list_user);
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<String>(
				ListUserActivity.this, android.R.layout.simple_list_item_1);
		listView = (ListView) findViewById(R.id.user_result_id);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg1 instanceof TextView) {
					loadDetail(((TextView)arg1).getText().toString());					
				}
			}
			
		});
		back = (Button)findViewById(R.id.list_user_back_id);
		load();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listView.setAdapter(adapter);
//				load();
				v.setVisibility(View.GONE);
			}
		});
	}
	
	private void loadDetail(String user) {
		back.setVisibility(View.VISIBLE);
		findViewById(R.id.list_progeress_bar_id).setVisibility(View.VISIBLE);
		listView.setAdapter(new ArrayAdapter<String>(
				ListUserActivity.this, android.R.layout.simple_list_item_1));
		HttpConnectionUtil.asyncConnect(ServiceUtil.URL, createDetailParp(user),
				HttpMethod.GET, new HttpConnectionCallback() {

					@Override
					public void execute(String response) {
						Log.d(TAG, response);
						findViewById(R.id.list_progeress_bar_id).setVisibility(
								View.GONE);
						try {
							JSONArray result = new JSONArray(response);
							onResult(result);
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
							onError(e.getMessage());
						}

					}

					public void onResult(JSONArray data) throws JSONException {
						if (data != null && data.length() > 0) {
							JSONArray datas = data.getJSONObject(0).getJSONArray("datas");
							if(datas  != null && datas.length() > 0) {
								ArrayList<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
								for (int i = 0; i < datas.length(); i++) {
									Map<String, Object> obj = new HashMap<String, Object>();
									obj.put("weight", datas.getJSONObject(i).get("weight"));
									obj.put("time", datas.getJSONObject(i).get("time"));
									result.add(obj);
								}
								detailAdapter = new ListViewAdapter(
										ListUserActivity.this,result,
										R.layout.list_item,
										new String[] { "weight", "time" },
										new int[] { R.id.list_item_weight, R.id.list_item_time });
								listView.setAdapter(detailAdapter);
							}

						}
					}

					public void onError(String msg) {
						findViewById(R.id.list_progeress_bar_id).setVisibility(
								View.GONE);
						Toast.makeText(ListUserActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void execute(Bundle bundle) {

					}
				});

	}

	protected void load() {
		findViewById(R.id.list_progeress_bar_id).setVisibility(View.VISIBLE);
		adapter.clear();
		HttpConnectionUtil.asyncConnect(ServiceUtil.URL, createParp(),
				HttpMethod.GET, new HttpConnectionCallback() {

					@Override
					public void execute(String response) {
						Log.d(TAG, response);
						findViewById(R.id.list_progeress_bar_id).setVisibility(
								View.GONE);
						try {
							JSONArray result = new JSONArray(response);
							onResult(result);
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
							onError(e.getMessage());
						}

					}

					public void onResult(JSONArray data) throws JSONException {
						if (data != null && data.length() > 0) {
							for (int i = 0; i < data.length(); i++) {
								adapter.add(data.getJSONObject(i).getString(
										"_id"));
							}

						}
					}

					public void onError(String msg) {
						findViewById(R.id.list_progeress_bar_id).setVisibility(
								View.GONE);
						Toast.makeText(ListUserActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void execute(Bundle bundle) {

					}
				});

	}

	private Map<String, String> createParp() {
		JSONObject f = new JSONObject();
		try {
			f.put("_id", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> parp = new HashMap<String, String>();
		parp.put("apiKey", "4fb07c41e4b020a46b49e787");
		parp.put("f", f.toString());
		return parp;
	}
	
	private Map<String, String> createDetailParp(String user) {
		JSONObject f = new JSONObject();
		try {
			f.put("datas", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject q = new JSONObject();
		try {
			q.put("_id", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> parp = new HashMap<String, String>();
		parp.put("apiKey", "4fb07c41e4b020a46b49e787");
		parp.put("f", f.toString());
		parp.put("q", q.toString());
		return parp;
	}

}
