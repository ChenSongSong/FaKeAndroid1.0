package com.blackswan.fake.activity;

import com.blackswan.fake.R;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup
{
	private TabHost tabhost;
	private long exitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabhost = (TabHost) findViewById(R.id.maintabhost);
		tabhost.setup(this.getLocalActivityManager());
		TabSpec tab1 = tabhost.newTabSpec("homepage");
        tab1.setIndicator(createContent("首页",R.drawable.homepage_tab));
        tab1.setContent(new Intent(this,HomePageActivity.class));
        tabhost.addTab(tab1);
        
        TabSpec tab2 = tabhost.newTabSpec("hairdo");
        tab2.setIndicator(createContent("发现",R.drawable.hairdopage_tab));
        tab2.setContent(new Intent(this,HairDoActivity.class));
        tabhost.addTab(tab2);
        
        TabSpec tab3 = tabhost.newTabSpec("discussionpage");
        tab3.setIndicator(createContent("讨论",R.drawable.discussionpage_tab));
        tab3.setContent(new Intent(this,DiscussionPageActivity.class));
        tabhost.addTab(tab3);
        
        TabSpec tab4 = tabhost.newTabSpec("mypage");
        tab4.setIndicator(createContent("我的",R.drawable.mypage_tab));
        tab4.setContent(new Intent(this,MyPageActivity.class));
        tabhost.addTab(tab4);
        
        tabhost.setCurrentTab(0);
        tabhost.setBackgroundColor(Color.argb(200, 237, 237, 237));
	}
	
	//设置单个按钮样式和内容
	private View createContent(String text,int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabwidget, null,
				false);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		ImageView iv_icon = (ImageView) view.findViewById(R.id.img_name);
		tv_name.setText(text);
		iv_icon.setBackgroundResource(resid);
		return view;
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
