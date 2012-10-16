package com.waitingmyself.record;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.waitingmyself.common.activity.BaseActivity;

public class DailogActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dailog);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getId() {
		return DailogActivity.class.getSimpleName();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	} 

}
