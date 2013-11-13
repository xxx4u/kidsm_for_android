package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class RegisterClassAdapter  extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<RegisterClassItem> arSrc;
	Context context;
	
	public RegisterClassAdapter(Context context, ArrayList<RegisterClassItem> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		arSrc = arItem;
	}

	public int getCount() {
			return arSrc.size();
	}

	public RegisterClassItem getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	public int getItemViewType(int position) {
		return 0;
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
			convertView = mInflater.inflate(R.layout.register_info_text, parent, false);
			arSrc.get(position).layout = convertView; 
		}

		TextView txt = (TextView) convertView.findViewById(R.id.register_info_text);
		txt.setText(arSrc.get(position).getName());
		
		return convertView;
	}
}
