package com.blackswan.fake.activity;

import com.blackswan.fake.R;
import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class DiscussionPageActivity extends ActivityGroup
{
	private TabHost tabHost;
	private long exitTime = 0;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discussionpage);
        tabHost = (TabHost) findViewById(R.id.discussiontabHost);
		tabHost.setup(this.getLocalActivityManager());
		TabWidget tabWidget = tabHost.getTabWidget();
		TabSpec tab1 = tabHost.newTabSpec("alltopic");
        tab1.setIndicator(createContent("全部",R.drawable.toplabelleft));
        tab1.setContent(new Intent(this,AllDiscussionActivity.class));
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("minetopic");
        tab2.setIndicator(createContent("我的",R.drawable.toplabelright));
        tab2.setContent(new Intent(this,MineDiscussionActivity.class));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
      
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
			if (tabId.equals("alldtopic")) {
				tabHost.setCurrentTabByTag("全部");
			} else if (tabId.equals("minetopic")) {
				tabHost.setCurrentTabByTag("我的");
			}
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
		
	}
		
