package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.blackswan.fake.R;
import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseActivity;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class BarberShopActivity extends BaseActivity
{
	private TextView text1;
	private TextView text2;
	private TextView text3;
	
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	
	private ArrayList<HashMap<String,Object>> itemList;
	
	private LinearLayout linLayout;
	private String title[]= {"环翠区","高新技术开发区","经济开发区","文登区"};
	//private ListView lv_barbershop;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barbershop);
		initPopupWindow();
//		lv_barbershop = (ListView) findViewById(R.id.lv_barbershop);
//		BarbershopListAdapter adapter = new BarbershopListAdapter();
//		lv_barbershop.setAdapter(adapter);
	}
	private void initPopupWindow() {
		itemList = new ArrayList<HashMap<String,Object>>();
		text1 = (TextView) findViewById(R.id.barbershop_text1);
		text2 = (TextView) findViewById(R.id.barbershop_text2);
		text3 = (TextView) findViewById(R.id.barbershop_text3);
		linLayout = (LinearLayout) findViewById(R.id.barbershop);
		
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			}
		});
		
		text2.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//将理发店铺列表按评价从高到低排列
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				}
			});
		
		text3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将理发店铺列表按距离从近到远排序
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			}
		});
	}
	@SuppressLint("InflateParams")
	private void showPopupWindow(int width, int height) {
		
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", title[i]);
			items.put("count", i*5+4);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopActivity.this, itemList);
		rootList.setAdapter(cla);
		
		flChild = (FrameLayout) layout.findViewById(R.id.child_lay);
		childList = (ListView) layout.findViewById(R.id.childcategory);
		childList.setAdapter(cla);
		flChild.setVisibility(View.INVISIBLE);
		
		mPopWin = new PopupWindow(layout, width * 9 / 10, height / 2, true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(text1, 5, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flChild.setVisibility(View.VISIBLE);
				childList
						.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								layout.setVisibility(View.GONE);
							}
					});
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
}
