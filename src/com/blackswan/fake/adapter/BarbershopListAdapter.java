package com.blackswan.fake.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.blackswan.fake.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BarbershopListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<HashMap<String,Object>> data;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DataList dl = new DataList();
//		convertView = LayoutInflater.from(context).inflate(R.layout.listitem_barber, null);
//		dl.iv_name = (ImageView) convertView.findViewById(R.id.iv_name);
//		//dl.iv_shangjia_image = (ImageView) convertView.findViewById(R.id.iv_shangjia_image);
//		dl.tv_shangjia_name = (TextView) convertView.findViewById(R.id.tv_shangjia_name);
//		dl.tv_youhuijia = (TextView) convertView.findViewById(R.id.tv_youhuijia);
//		dl.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
//		dl.tv_qi = (TextView) convertView.findViewById(R.id.tv_qi);
//		dl.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
//		dl.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
//		dl.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
//		
		return convertView;
	}
	
	private class DataList{
		public TextView tv_shangjia_name,//
						tv_youhuijia,//
						tv_price,//
						tv_qi,//
						tv_category,//
						tv_address,//
						tv_distance;//
		
		public ImageView iv_shangjia_image,//
						 iv_name;//
		
	}

}
