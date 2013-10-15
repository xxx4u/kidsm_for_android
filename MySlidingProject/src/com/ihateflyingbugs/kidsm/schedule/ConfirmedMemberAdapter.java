package com.ihateflyingbugs.kidsm.schedule;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class ConfirmedMemberAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<ConfirmedMember> arSrc;
	Context context;
	
	public ConfirmedMemberAdapter(Context context, ArrayList<ConfirmedMember> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
	}
	
	public int getCount() {
		return arSrc.size();
	}

	public ConfirmedMember getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.showconfirmedlist_item, parent, false);
		}
		TextView txt = (TextView)convertView.findViewById(R.id.showconfimedlist_name);
		txt.setText(arSrc.get(position).member_name);
		
		return convertView;
	}
}
