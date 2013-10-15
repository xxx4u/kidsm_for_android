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
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.showconfirmedlist_item, parent, false);
		}
		TextView txt = (TextView)convertView.findViewById(R.id.showconfimedlist_name);
		txt.setText(arSrc.get(position).member_name);
		
		return convertView;
	}
}
