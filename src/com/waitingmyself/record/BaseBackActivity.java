package com.waitingmyself.record;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.waitingmyself.common.activity.BaseActivity;

public abstract class BaseBackActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button btn = (Button) findViewById(R.id.back_btn);
		final BaseBackActivity base = this;
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				base.finish();
//				startActivity(new Intent(BaseBackActivity.this,
//						MainActivity.class));
				
			}
		});
	}

}
