package com.blackswan.fake.activity;

import com.blackswan.fake.R;
//import com.blackswan.fake.activity.BaiduMapActivity;

import com.blackswan.fake.base.BaseApplication;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class HomePageActivity extends ActivityGroup implements OnClickListener
{
	private TabHost tabHost;
	private long exitTime = 0;
	
	View  cityView;
	TextView city;
	ImageView researchImageView;
	ImageView mapImageView;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_homepage);
		tabHost = (TabHost) findViewById(R.id.homepagetabHost);
		tabHost.setup(this.getLocalActivityManager());;
		TabWidget tabWidget = tabHost.getTabWidget();
		TabSpec tab1 = tabHost.newTabSpec("barbershop");
        tab1.setIndicator(createContent("发型师",R.drawable.toplabelleft));
        tab1.setContent(new Intent(this,BarberActivity.class));
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("barber");
        tab2.setIndicator(createContent("理发店",R.drawable.toplabelright));
        tab2.setContent(new Intent(this,BarberShopActivity.class));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
        initViews();
        initEvent();
	}
	//注入界面控件
	protected void initViews() {
		cityView =(View)findViewById(R.id.ll_city);
		city=(TextView) findViewById(R.id.tv_city);
		researchImageView = (ImageView)findViewById(R.id.homepageresearch);
		mapImageView = (ImageView) findViewById(R.id.homepagemap);
		
	}
	
	//为控件添加事件处理
	protected void initEvent() {
		cityView.setOnClickListener(this);
		BaseApplication application = ((BaseApplication) getApplication());
		String historycity=application.preferences.getString("city2", null);
        if (historycity==null) {
			city.setText(application.mCurrentcity);
		}else {
			city.setText(historycity);
		}
		researchImageView.setOnClickListener(this);
		mapImageView.setOnClickListener(this);
		// 设置tabHost切换时动态更改图标
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
						tabChanged(tabId);
					}

				});
	}
	
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
			if (tabId.equals("barbershop")) {
				tabHost.setCurrentTabByTag("理发店");
			} else if (tabId.equals("barber")) {
				tabHost.setCurrentTabByTag("理发师");
			}
		}
		
		@Override
		protected void onResume() {
			// 重新显示Activity时重置城市
			BaseApplication application = ((BaseApplication) getApplication());
			String historycity=application.preferences.getString("city2", null);
	        if (historycity==null) {
				city.setText(application.mCurrentcity);
			}else {
				city.setText(historycity);
			}
			super.onResume();
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				exit();
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}
		
		public void exit() {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出剃头",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
		
		}
		
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.homepageresearch:
				Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
				startActivity(intent);
				break;
			case R.id.homepagemap:
//				Intent intent2 = new Intent(HomePageActivity.this,BaiduMapActivity.class);
				break;
			case R.id.ll_city:
				Intent intent3 = new Intent(HomePageActivity.this,SelectCityActivity.class);
				startActivity(intent3);
			default:
				break;
			}
		}
	
}
