package com.waitingmyself.record;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.waitingmyself.common.async.AsyncLoader;
import com.waitingmyself.common.async.BaseAsyncTaskExecute;
import com.waitingmyself.record.R.color;
import com.waitingmyself.record.dao.AppDAO;

public class StatisticsActivity extends BaseBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.statistics);
		super.onCreate(savedInstanceState);
		final AppDAO dao = new AppDAO(StatisticsActivity.this);
		new AsyncLoader().putTask("statistcsData", null, null,
				new BaseAsyncTaskExecute() {

					@Override
					public Object manifest(Object... params) {
						return dao
								.createSQLQuery(
										"select weight,time from weight order by time desc",
										null);
					}

					@Override
					public void callback(Object result) {
						GraphicalView view = ChartFactory.getTimeChartView(
								StatisticsActivity.this,
								getDemoDataset(result), getDemoRenderer(),
								"yyyy/MM/dd");
						LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
						layout.addView(view, new LayoutParams(
								LayoutParams.FILL_PARENT,

								LayoutParams.FILL_PARENT));
						// Intent intent = ChartFactory.getTimeChartIntent(
						// StatisticsActivity.this,
						// getDemoDataset(result), getDemoRenderer(),
						// "yyyy/MM/dd");
						// startActivity(intent);
					}
				});
	}

	private XYMultipleSeriesDataset getDemoDataset(Object result) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("时间");
		@SuppressWarnings("unchecked")
		List<? extends Map<String, ?>> datas = (List<? extends Map<String, ?>>) result;
		for (Map<String, ?> map : datas) {
			series.add(new Date(Long.valueOf(map.get("time").toString())),
					Double.valueOf(map.get("weight").toString()));
		}
		dataset.addSeries(series);
		return dataset;
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(12);
		renderer.setChartTitleTextSize(16);
		renderer.setLabelsTextSize(12);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(3f);
		renderer.setBackgroundColor(Color.TRANSPARENT); // 设置背景色透明
		renderer.setXLabelsColor(Color.BLUE);
		renderer.setYLabelsColor(0, color.darkgreen);
		renderer.setShowGrid(true);
		// 上
		renderer.setMargins(new int[] { 0, 10, 0, 0 });
		renderer.setMarginsColor((Color.argb(0x00, 0x01, 0x01, 0x01)));
		renderer.setApplyBackgroundColor(true); // 使背景色生效
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.BLUE);
		r.setPointStyle(PointStyle.SQUARE);
		r.setFillBelowLine(true);
		r.setFillBelowLineColor(Color.TRANSPARENT);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		renderer.setAxesColor(Color.BLUE);
		renderer.setLabelsColor(Color.BLUE);
		return renderer;
	}
	
	@Override
	protected String getId() {
		return StatisticsActivity.class.getSimpleName();
	}

}
