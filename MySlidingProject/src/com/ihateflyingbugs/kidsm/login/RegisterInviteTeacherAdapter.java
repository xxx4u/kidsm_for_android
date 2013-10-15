package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class RegisterInviteTeacherAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<RegisterInviteTeacherItem> arSrc;
	Context context;
	
	public RegisterInviteTeacherAdapter(Context context, ArrayList<RegisterInviteTeacherItem> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		arSrc = arItem;
	}

	public int getCount() {
			return arSrc.size();
	}

	public RegisterInviteTeacherItem getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	public int getItemViewType(int position) {
		return arSrc.get(position).type;
	}
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		CheckBox cb;
		if (convertView == null) {
			switch (arSrc.get(position).type) {
			case 0:
				convertView = mInflater.inflate(R.layout.register_info_nametag, parent, false);
				break;
			case 1:
				convertView = mInflater.inflate(R.layout.register_info_namechecker, parent, false);
				break;
			}
			arSrc.get(position).layout = convertView; 

		}
		
		TextView txt;
		// 항목 뷰를 초기화한다.
		switch (arSrc.get(position).type) {
		case 0:
			txt = (TextView)convertView.findViewById(R.id.register_info_nametag);
			txt.setText(arSrc.get(position).getName());
			break;
		case 1:
			convertView.setPadding(20, 0, 0, 0);
			txt = (TextView)convertView.findViewById(R.id.register_info_namechecker_name);
			txt.setText(arSrc.get(position).getName());
			cb = (CheckBox)convertView.findViewById(R.id.register_info_namechecker_checker);
			cb.setTag(arSrc.get(position).tag);
			cb.setChecked(arSrc.get(position).isChecked);

			break;
		}

		return convertView;
	}
}
