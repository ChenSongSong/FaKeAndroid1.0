package com.blackswan.fake.activity;
import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseApplication;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class SelectCityActivity extends TabActivity{
	
	private TabHost tabHost;
	ImageView reback;
	Button citytext;
	BaseApplication application;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcity);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(this.getLocalActivityManager());
		TabWidget tabWidget = tabHost.getTabWidget();
		TabSpec tab1 = tabHost.newTabSpec("selectbyprovince");
        tab1.setIndicator(createContent("按省份",R.drawable.toplabelleft));
        tab1.setContent(new Intent(this,SelectCityByProvince.class));
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("selectbysort");
        tab2.setIndicator(createContent("按首字母",R.drawable.toplabelright));
        tab2.setContent(new Intent(this,SelectCityBySort.class));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
        
        initViews();
        initEvent();
        application = ((BaseApplication) getApplication());
        citytext.setText(application.mCurrentcity);
		
	}
	
	//注入界面控件
	protected void initViews(){
		reback = (ImageView) findViewById(R.id.select_reback);
		citytext = (Button) findViewById(R.id.selectcitytext);
	}
	
	//为控件添加事件
	protected void initEvent() {
		reback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
		citytext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseApplication application = ((BaseApplication) getApplication());
				String city0 = application.preferences.getString("city0", null);
				String city1 = application.preferences.getString("city1", null);
	            String city2 = application.preferences.getString("city2", null);
	            String currentcity = application.mCurrentcity;
	            if (currentcity.equals(city2)||currentcity.equals(city1)||currentcity.equals(city0)) {
	            	application.putString("city0", city0);
	            	application.putString("city1", city1);
	    			application.putString("city2", city2);
				}else {
					application.putString("city0",city1);
		        	application.putString("city1", city2);
					application.putString("city2", currentcity);
				}
				finish();
			}
		});
		// 设置tabHost切换时动态更改图标
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				tabChanged(tabId);
			}

		});		
	}
	@SuppressLint("InflateParams")
	private View createContent(String text, int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.toptabwidget, null,
				false);
		TextView tv_name = (TextView) view.findViewById(R.id.toptabwidgettext);
		tv_name.setText(text);
		tv_name.setBackgroundResource(resid);
		return view;
	}
		
		// 捕获tab变化事件
		private void tabChanged(String tabId) {
			// 当前选中项
			if (tabId.equals("selectbyprovince")) {
				tabHost.setCurrentTabByTag("按省份");
			} else if (tabId.equals("selectbysort")) {
				tabHost.setCurrentTabByTag("按首字母");
			}
		}

}
		
