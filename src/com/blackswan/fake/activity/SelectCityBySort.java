package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.City;
import com.blackswan.fake.util.DBManager;
import com.blackswan.fake.view.CityListView;
import com.blackswan.fake.view.LetterListView;
import com.blackswan.fake.view.LetterListView.OnTouchingLetterChangedListener;

public class SelectCityBySort extends BaseActivity
{
	private BaseAdapter adapter;

    private CityListView mCityLit;

    private TextView overlay;

    private LetterListView letterListView;

    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置

    private String[] sections;// 存放存在的汉语拼音首字母

    private Handler handler;
    
    private SQLiteDatabase database;
    
    private OverlayThread overlayThread;

    private ArrayList<City> mCityNames;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcitybyalph);

        mCityLit = (CityListView) findViewById(R.id.city_list);
        mCityLit.setmHeaderViewVisible(true);
        mCityLit.setPinnedHeaderView(this, R.layout.title,
                R.id.contactitem_catalog);
        letterListView = (LetterListView) findViewById(R.id.cityLetterListView);
        DBManager dbManager = new DBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatabase();
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
                + DBManager.DB_NAME, null);
        mCityNames = getCityNamesBySort();
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        setAdapter(mCityNames);
        mCityLit.setOnItemClickListener(new CityListOnItemClick());

    }

    /**
     * 从数据库获取城市数据
     * 
     * @return
     */
    private ArrayList<City> getCityNamesBySort()
    {
        ArrayList<City> names = new ArrayList<City>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM cities ORDER BY NameSort", null);
        for (int i = 0; i < cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            City cityModel = new City();
            cityModel.setName(cursor.getString(cursor
                    .getColumnIndex("CityName")));
            cityModel.setProvince(cursor.getString(cursor
            		.getColumnIndex("Province")));
            cityModel.setNameSort(cursor.getString(cursor
                    .getColumnIndex("NameSort")));
            cityModel.setProvinceSort(cursor.getString(cursor.
            		getColumnIndex("ProvinceSort")));
            names.add(cityModel);
        }
        return names;
    }
   
    /**
     * 城市列表点击事件
     * 
     * @author 
     * 
     */
    class CityListOnItemClick implements OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                long arg3)
        {
            City cityModel = (City) mCityLit.getAdapter()
                    .getItem(pos);
            BaseApplication application = ((BaseApplication) getApplication());
            String city0 = application.preferences.getString("city0", null);
            String city1 = application.preferences.getString("city1", null);
            String city2 = application.preferences.getString("city2", null);
            String city3 = application.preferences.getString("city3", null);
            if (cityModel.getName().equals(city2)||cityModel.getName().equals(city1)||cityModel.getName().equals(city0)) {
            	application.putString("city3", cityModel.getName());
			}
            if (city3.equals(city0)||city3.equals(city1)||city3.equals(city2)) {
            	application.putString("city0",city0);
	        	application.putString("city1", city1);
				application.putString("city2", city2);
				application.putString("city3", cityModel.getName());
			}else {
				application.putString("city0",city1);
	        	application.putString("city1", city2);
				application.putString("city2", city3);
				application.putString("city3", cityModel.getName());
			}
            finish();
        }

    }

    /**
     * 为ListView设置适配器
     * 
     * @param list
     */
    private void setAdapter(List<City> list)
    {
        if (list != null)
        {
            adapter = new ListAdapter(this, list);
            mCityLit.setAdapter(adapter);
        }

    }

    /**
     * ListViewAdapter
     * 
     * @author 
     * 
     */
    private class ListAdapter extends BaseAdapter
    {
        private LayoutInflater inflater;

        private List<City> list;

        public ListAdapter(Context context, List<City> list)
        {

            this.inflater = LayoutInflater.from(context);
            this.list = list;
            BaseApplication application = ((BaseApplication) getApplication());
            City lastcity2 = new City();
            lastcity2.setLastCity(application.preferences.getString("city2", null));
            list.add(0,lastcity2);
            City lastcity1 = new City();
            lastcity1.setLastCity(application.preferences.getString("city1", null));
            list.add(1,lastcity1);
            City lastcity0 = new City();
            lastcity0.setLastCity(application.preferences.getString("city0", null));
            list.add(2,lastcity0);
            City cityModel = new City();
            cityModel.setHotCity("深圳");
            list.add(3, cityModel);
            City cityMode2 = new City();
            cityMode2.setHotCity("北京");
            list.add(4, cityMode2);
            City cityMode3 = new City();
            cityMode3.setHotCity("武汉");
            list.add(5, cityMode3);
            alphaIndexer = new HashMap<String, Integer>();   
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
                // 当前汉语拼音首字母
                // getAlpha(list.get(i));
                String currentStr = list.get(i).getNameSort();
                // 上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? list.get(i - 1)
                        .getNameSort() : " ";
                if (!previewStr.equals(currentStr))
                {  	
                		String name = list.get(i).getNameSort();
                        alphaIndexer.put(name, i);
                        sections[i] = name;
                }
            }
            System.out.println("alphaIndexer  =" + alphaIndexer.toString());
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @SuppressLint("InflateParams")
		@Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if (convertView == null) 
            {
                convertView = inflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.alpha = (TextView) convertView
                        .findViewById(R.id.contactitem_catalog);
                holder.name = (TextView) convertView
                        .findViewById(R.id.contactitem_nick);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getName());
            String currentStr = list.get(position).getNameSort();
            String previewStr = (position - 1) >= 0 ? list.get(position - 1)
                    .getNameSort() : " ";
            if (!previewStr.equals(currentStr))
            {
                holder.alpha.setVisibility(View.VISIBLE);
                holder.alpha.setText(currentStr);
            }
            else
            {
                holder.alpha.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder
        {
            TextView alpha;

            TextView name;
        }

    }

    // 初始化汉语拼音首字母弹出提示框
    @SuppressLint("InflateParams")
	private void initOverlay()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class LetterListViewListener implements
            OnTouchingLetterChangedListener
    {

        @Override
        public void onTouchingLetterChanged(final String s)
        {
            if (alphaIndexer.get(s) != null)
            {
                int position = alphaIndexer.get(s);
                 mCityLit.setSelection(position);
                 overlay.setText(sections[position]);
                 overlay.setVisibility(View.VISIBLE);
                 handler.removeCallbacks(overlayThread);
                 // 延迟一秒后执行，让overlay为不可见
                 handler.postDelayed(overlayThread, 1500);
                if (android.os.Build.VERSION.SDK_INT >= 11)
                {
                    mCityLit.smoothScrollToPositionFromTop(position, 0, 300);
                }
                else if (android.os.Build.VERSION.SDK_INT >= 8)
                {
                    int firstVisible = mCityLit.getFirstVisiblePosition();
                    int lastVisible = mCityLit.getLastVisiblePosition();
                    if (position < firstVisible)
                        mCityLit.smoothScrollToPosition(position);
                    else
                        mCityLit.smoothScrollToPosition(position + lastVisible
                                - firstVisible - 2);
                }
                else
                {
                    mCityLit.setSelectionFromTop(position, 0);
                }

            }
        }

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable
    {

        @Override
        public void run()
        {
            overlay.setVisibility(View.GONE);
        }

    }

	@Override
	protected void initViews() {
		
	}

	@Override
	protected void initEvents() {
		
	}

}
