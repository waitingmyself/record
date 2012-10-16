package com.waitingmyself.record;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.waitingmyself.common.util.TimeUtil;

/**
 * 自定义Adapter
 * 
 * @author kingyee
 * 
 */
public class ListViewAdapter extends SimpleAdapter {

	public ListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView text = (TextView) view.findViewById(R.id.list_item_time);
		text.setText(TimeUtil.longToString(
				Long.valueOf(text.getText().toString()), "yyyy/MM/dd"));
		return view;
	}
}