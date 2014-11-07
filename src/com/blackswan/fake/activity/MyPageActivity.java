package com.blackswan.fake.activity;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.useractivity.LoginActivity;
import com.blackswan.fake.activity.useractivity.MyCollectActivity;
import com.blackswan.fake.activity.useractivity.PersonalInfoActivity;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.bean.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyPageActivity extends BaseActivity
{
	//用户信息
	private UserInfo userInfo = null;
	//我的头像
	private ImageView myHeadImageView;
	//我的昵称栏
	private LinearLayout myNmae;
	//我的预约
	private LinearLayout myAppraise;
	//我的收藏
	private LinearLayout myCollect;
	//预约详情
	private LinearLayout appraise;
	//设置
	private ImageView settingImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypage);
		initControl();
		setLisentener();
	}
	/**
	 * 初始化控件
	 */
	public void initControl()
	{
		myHeadImageView = (ImageView) findViewById(R.id.myHead);
		myNmae = (LinearLayout) findViewById(R.id.myName);
		settingImageView = (ImageView) findViewById(R.id.iv_setting);
		myAppraise = (LinearLayout) findViewById(R.id.myAppraise);
		myCollect = (LinearLayout) findViewById(R.id.myCollect);
		setAppraise((LinearLayout) findViewById(R.id.myAppraise));
		
	}
	/**
	 * 为各个控件设置监听
	 */
	public void setLisentener()
	{
		myHeadImageView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//弹出修改头像的界面
				
			}
		});
		myNmae.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//判断是否登入，未登入跳转登入界面
				if (userInfo==null)
				{
					Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
					startActivity(intent);
				}else {
					//跳转到我的详细信息页
					Intent intent = new Intent(MyPageActivity.this,PersonalInfoActivity.class);
					startActivity(intent);
				}
			}
		});
		settingImageView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//跳转至设置页面
				Intent intent = new Intent(MyPageActivity.this, SelectCityActivity.class);
				startActivity(intent);
				
			}
		});
		myAppraise.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//判断是否登入，未登入跳转登入界面
				if (userInfo==null)
				{
					Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
					startActivity(intent);
				}else {
					//若已登入，我的预约详细信息刷新显示
				}
				
			}
		});
		//收藏夹
		myCollect.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//判断是否登入，为登入跳转到登入页面
				if (userInfo==null)
				{
					Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
					startActivity(intent);
				}else {
					//若已登入，跳转至收藏页面
					Intent intent = new Intent(MyPageActivity.this,MyCollectActivity.class);
					startActivity(intent);
				}				
			}
		});
	}
	private long exitTime = 0;
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
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}
	public LinearLayout getAppraise() {
		return appraise;
	}
	public void setAppraise(LinearLayout appraise) {
		this.appraise = appraise;
	}
}
