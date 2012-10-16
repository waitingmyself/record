package com.waitingmyself.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.waitingmyself.common.activity.BaseActivity;
import com.waitingmyself.common.async.AsyncLoader;
import com.waitingmyself.common.async.BaseAsyncTaskExecute;
import com.waitingmyself.common.net.HttpConnectionCallback;
import com.waitingmyself.common.net.HttpConnectionUtil;
import com.waitingmyself.common.net.HttpConnectionUtil.HttpMethod;
import com.waitingmyself.record.common.ServiceUtil;
import com.waitingmyself.record.dao.AppDAO;

public class MainActivity extends BaseActivity {

	private boolean uploadFlag = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final GridView gridview = (GridView) findViewById(R.id.GridView);
		SimpleAdapter saMenuItem = new SimpleAdapter(this, getData(),
				R.layout.meunitem, new String[] { "ItemImage", "ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });

		// 添加Item到网格中
		gridview.setAdapter(saMenuItem);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				switch (arg2) {
				case 0:
					intent = new Intent(MainActivity.this, EditActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MainActivity.this, ListActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MainActivity.this,
							StatisticsActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(MainActivity.this,
							ListUserActivity.class);
					startActivity(intent);
					break;
				case 4:
					if (uploadFlag) {
						return;
					}
					upload();
					break;
				case 5:
					intent = new Intent(MainActivity.this,
							DailogActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	public ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> memuList = new ArrayList<HashMap<String, Object>>();
		for (int i = 1; i <= 6; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			switch (i) {
			case 1:
				map.put("ItemImage", R.drawable.add);
				map.put("ItemText", "添加记录");
				break;
			case 2:
				map.put("ItemImage", R.drawable.list);
				map.put("ItemText", "我的数据");
				break;
			case 3:
				map.put("ItemImage", R.drawable.stats);
				map.put("ItemText", "统计分析");
				break;
			case 4:
				map.put("ItemImage", R.drawable.world);
				map.put("ItemText", "网友共享");
				break;
			case 5:
				map.put("ItemImage", R.drawable.upload);
				map.put("ItemText", "我要共享");
				break;
			case 6:
				map.put("ItemImage", R.drawable.help);
				map.put("ItemText", "帮助");
				break;
			default:
				break;
			}
			memuList.add(map);
		}
		return memuList;
	}

	@Override
	protected String getId() {
		return MainActivity.class.getSimpleName();
	}

	protected void upload() {
		uploadFlag = true;
		findViewById(R.id.list_progeress_bar_id).setVisibility(View.VISIBLE);
		findViewById(R.id.GridView).setVisibility(View.GONE);
		
		final AsyncLoader asyncLoader = new AsyncLoader();
		final AppDAO dao = new AppDAO(MainActivity.this);
		asyncLoader.putTask("listUploadData", null, null,
				new BaseAsyncTaskExecute() {

					@Override
					public Object manifest(Object... params) {
						return dao
								.createSQLQuery(
										"select weight,time from weight order by time desc",
										null);
					}

					@SuppressWarnings("unchecked")
					@Override
					public void callback(Object result) {
						HttpConnectionUtil.asyncConnect(getUrl(),
								createParp((List<Map<String, Object>>) result),
								HttpMethod.PUT, new HttpConnectionCallback() {

									@Override
									public void execute(String response) {
										uploadFlag = false;
										findViewById(R.id.list_progeress_bar_id).setVisibility(View.GONE);
										findViewById(R.id.GridView).setVisibility(View.VISIBLE);
										try {
											JSONObject result = new JSONObject(
													response);
											onResult(result);
										} catch (Exception e) {
											onError(e.getMessage());
										}

									}

									public void onResult(JSONObject data)
											throws JSONException {
									}

									public void onError(String msg) {
										Toast.makeText(MainActivity.this, msg == null?"":msg,
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void execute(Bundle bundle) {

									}
								});
					}
				});
	}

	private String getUrl() {
		return ServiceUtil.URL + "/" + getDeviceId()
				+ "?apiKey=4fb07c41e4b020a46b49e787";
	}

	public String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) MainActivity.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();

	}

	private Map<String, String> createParp(List<Map<String, Object>> result) {
		Map<String, String> parp = new HashMap<String, String>();
		JSONObject data = new JSONObject();
		JSONArray datas = new JSONArray();
		try {
			int i = 0;
			if (result != null && result.size() > 0) {
				for (Map<String, Object> map : result) {
					if(i == 400) {
						
						break;
					}
					datas.put(new JSONObject().put("time", map.get("time"))
							.put("weight", map.get("weight")));
					i++;
				}
			}
			data.put("$set", new JSONObject().put("datas", datas));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		parp.put("data", data.toString());
		return parp;
	}

}
