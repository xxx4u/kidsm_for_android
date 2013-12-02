package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class RegisterChildAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<RegisterChildItem> arSrc;
	Context context;
	
	public RegisterChildAdapter(Context context, ArrayList<RegisterChildItem> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		arSrc = arItem;
	}

	public int getCount() {
			return arSrc.size();
	}

	public RegisterChildItem getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	public int getItemViewType(int position) {
		return 0;
	}
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.register_info_namechecker, parent, false);
			arSrc.get(position).layout = convertView; 
		}

//		if( arSrc.get(position).parent_srl.equals("0") == false )
//			convertView.setVisibility(View.GONE);
//		else
//			convertView.setVisibility(View.VISIBLE);

		TextView txt = (TextView) convertView.findViewById(R.id.register_info_namechecker_name);
		
		if( arSrc.get(position).getParent_srl().equals("0") ) {
			txt.setText(arSrc.get(position).getName());
			txt.setTextColor(Color.BLACK);
		}
		else {
			txt.setText(arSrc.get(position).getName() + "(�θ�����)");
			txt.setTextColor(Color.LTGRAY);
		}
		
		convertView.setTag(position);
		CheckBox cb = (CheckBox)convertView.findViewById(R.id.register_info_namechecker_checker);
		cb.setTag(position);
		cb.setChecked(arSrc.get(position).isChecked());
		
		return convertView;
	}
}
