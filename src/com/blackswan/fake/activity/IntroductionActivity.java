package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;
import com.blackswan.fake.R;
import com.blackswan.fake.adapter.ViewPagerAdapter;
import com.blackswan.fake.base.BaseActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

@SuppressLint("InflateParams")
public class IntroductionActivity extends BaseActivity implements OnPageChangeListener
{
	private ViewPagerAdapter adapter;
	private ViewPager pager;
	private List<View> views;//视图数据
	private int[] imageVeiwResourceId = { R.drawable.cartoon13,
			R.drawable.cartoon25, R.drawable.cartoon32 };//显示图片的数据
	private ImageView[] point;//底部小圆点
	private int currentId = 0;//当前ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction);
		initView();
		
		setPoint();//第一次设置小圆点位置
	}

	/**初始化view
	 * 
	 */
	private void initView() {
		pager = (ViewPager) this.findViewById(R.id.vp);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		views = new ArrayList<View>();
		
		for (int i = 0; i < imageVeiwResourceId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageVeiwResourceId[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			views.add(imageView);
		}
		
		View view = inflater.inflate(R.layout.into, null);
		views.add(view);
		adapter = new ViewPagerAdapter(views, IntroductionActivity.this);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
	}


	/**设置小圆点
	 * 
	 */
	private void setPoint() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.viewpager_ll);
		point = new ImageView[ll.getChildCount()];
		for (int i = 0; i < ll.getChildCount(); i++) {
			if (currentId == i) {
				point[i] = (ImageView) ll.getChildAt(i);
				point[i].setImageResource(R.drawable.guide_dot_white);
			} else {
				point[i] = (ImageView) ll.getChildAt(i);
				point[i].setImageResource(R.drawable.guide_dot_black);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 滑动状态监听方法

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 滑动是的监听方法
	}

	@Override
	public void onPageSelected(int arg0) {
		//TODO 选择页面的监听方法
		currentId = arg0;
		setPoint();

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}
}