package com.blackswan.fake.activity;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.service.UpdateService;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

@SuppressLint("WorldReadableFiles")
public class WelcomeActivity extends BaseActivity
{
	private UpdateService updateService;
	 /** Called when the activity is firstcreated. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        final View view = View.inflate(this, R.layout.acvtivity_welcome, null);
			setContentView(view);
			// 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
    		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
    		aa.setDuration(2000);
    		view.startAnimation(aa);
    		//给动画添加监听方法
    		aa.setAnimationListener(new AnimationListener() {
    			@Override
    			public void onAnimationEnd(Animation arg0) {
    				redirectTo();
    			}

    			@Override
    			public void onAnimationRepeat(Animation animation) {
    			}

    			@Override
    			public void onAnimationStart(Animation animation) {
    				 setUpdateService(new UpdateService(WelcomeActivity.this));
    			}

    		});
	   }
	   
	   /**
		 * 跳转到主界面的方法
		 */
		private void redirectTo() {
			 //读取SharedPreferences中需要的数据
			BaseApplication application = (BaseApplication) getApplication();
	        int count = application.preferences.getInt("count", 0);
	        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
	        if (count == 0) {
	        	
	            Intent intent = new Intent();
	            intent.setClass(getApplicationContext(),IntroductionActivity.class);
	            startActivity(intent);
	            finish();
	        }else {
	        	Intent intent = new Intent(this,MainActivity.class);
				startActivity(intent);
				finish();
			}
	     
	        //存入数据
			application.putInt("count", ++count);
		}

	public UpdateService getUpdateService() {
		return updateService;
	}

	public void setUpdateService(UpdateService updateService) {
		this.updateService = updateService;
	}

	@Override
	protected void initViews() {
		
	}

	@Override
	protected void initEvents() {
		
	}
      
}
	
	