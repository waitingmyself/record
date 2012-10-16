package com.waitingmyself.record;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.waitingmyself.common.async.AsyncLoader;
import com.waitingmyself.common.async.BaseAsyncTaskExecute;
import com.waitingmyself.common.util.TimeUtil;
import com.waitingmyself.record.dao.AppDAO;
import com.waitingmyself.record.dao.model.Weight;

public class EditActivity extends BaseBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.edit);
		super.onCreate(savedInstanceState);
		final AppDAO dao = new AppDAO(EditActivity.this);
		final AsyncLoader asyncLoader = new AsyncLoader();
		final Calendar now = Calendar.getInstance();
		final Button dateText = (Button) findViewById(R.id.date_text_id);
		final EditText text = (EditText) findViewById(R.id.weight_text_id);
		final Button saveBtn = (Button) findViewById(R.id.save_btn);

		dateText.setText(TimeUtil.getNowTime("yyyy/MM/dd"));
		dateText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(EditActivity.this,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								dateText.setText(year + "/" + (monthOfYear + 1)
										+ "/" + dayOfMonth);
							}
						}, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Weight weight = new Weight();
				String weightText = text.getText().toString();
				boolean flag = true;
				try {
					weight.weight = Double.valueOf(weightText) * 1.00;
				} catch (Exception e) {
					showShortText("数值输入不合法");
					flag = false;
				}
				if (!flag) {
					return;
				}
				saveBtn.setEnabled(false);
				weight.time = TimeUtil
						.stringToLong(dateText.getText().toString(),
								"yyyy/MM/dd");
				weight.createTime = TimeUtil.dateTolong();
				asyncLoader.putTask("saveData" + weight.createTime, null, null,
						new BaseAsyncTaskExecute() {

							@Override
							public Object manifest(Object... params) {
								return dao.save("weight", weight);
							}

							@Override
							public void callback(Object result) {
								saveBtn.setEnabled(true);
								if(Long.valueOf(result.toString()) > -1) {
									text.setText("");
									startActivity(new Intent(EditActivity.this,
											MainActivity.class));									
								} else {
									showLongText("保存失败");
								}
							}
						});
			}
		});

	}

	@Override
	protected String getId() {
		return EditActivity.class.getSimpleName();
	}

}
