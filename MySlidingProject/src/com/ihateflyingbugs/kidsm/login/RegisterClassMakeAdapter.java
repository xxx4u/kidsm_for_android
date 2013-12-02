package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegisterClassMakeAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	Context context;
	ArrayList<String> list;
	
	public RegisterClassMakeAdapter(Context context, ArrayList<String> list) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.register_class_item, parent, false);
		}

		TextView txt = (TextView) convertView.findViewById(R.id.register_class_item_text);
		txt.setText(list.get(position));
		
		return convertView;	}

}
