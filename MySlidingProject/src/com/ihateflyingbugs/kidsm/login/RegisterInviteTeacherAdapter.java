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
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
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
		// �׸� �並 �ʱ�ȭ�Ѵ�.
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
