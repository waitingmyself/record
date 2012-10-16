package com.waitingmyself.common.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.waitingmyself.common.model.IJSONKey;

public abstract class JSONCallback<E> implements HttpConnectionCallback, IJSONKey {

	private String TAG = JSONCallback.class.getSimpleName();

	@SuppressWarnings("unchecked")
	@Override
	public void execute(String response) {
		try {
			JSONObject json = new JSONObject(response);
			boolean flag = onResult(json);
			if(flag) {
				return;
			}
			boolean success = json.getBoolean(SUCCESS);
			if (success) {
				onSuccess((E) json.get(DATA));
			} else {
				onError(json.getString(MSG));
			}
		} catch (JSONException e) {
			Log.e(TAG, "json create error");
			onFailure(response);
		}
	}

	@Override
	public void execute(Bundle bundle) {
	}

	public boolean onResult(JSONObject data) {
		return false;
	}
	
	public void onSuccess(E data) {
		
	}

	public void onError(String msg) {

	}

	public void onFailure(String response) {

	}

}
