package com.ihateflyingbugs.kidsm.schedule;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class CalendarWeekAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<CalendarWeek> arSrc;
	Context context;
	
	public CalendarWeekAdapter(Context context, ArrayList<CalendarWeek> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		arSrc = arItem;
		
	}

	public int getCount() {
		return arSrc.size();
	}

	public CalendarWeek getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
//	public int getItemViewType(int position) {
//		return arSrc.get(position).type;
//	}
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 5;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		
		int res = 0;
		switch (arSrc.get(position).type) {
		case BLANK:
			res = R.layout.blank;
			break;
		case DATE:
			res = R.layout.schedule_date;
			break;
		case SCHEDULE_FOR_PARENT:
			res = R.layout.schedule_schedule;
			break;
		case SCHEDULE_FOR_TEACHER:
			res = R.layout.schedule_schedule_for_teacher;
			break;
		case BIRTHDAY:
			res = R.layout.schedule_birthday;
			break;
		}
		
		if (convertView == null) {
			convertView = mInflater.inflate(res, parent, false);
			arSrc.get(position).layout = convertView; 
		}
		else {
			if( res != convertView.getId()) {
				convertView = mInflater.inflate(res, parent, false);
				arSrc.get(position).layout = convertView; 
			}
		}

		TextView txt;
		// �׸� �並 �ʱ�ȭ�Ѵ�.
		switch (arSrc.get(position).type) {
		case BLANK:
			break;
		case DATE:
			txt = (TextView)convertView.findViewById(R.id.schedule_date_date);
			txt.setText(arSrc.get(position).date);
			break;
		case SCHEDULE_FOR_PARENT:
			txt = (TextView)convertView.findViewById(R.id.schedule_schedule_message);
			txt.setText(arSrc.get(position).events.get(0).description);
			CheckBox cb = (CheckBox)convertView.findViewById(R.id.schedule_schedule_check);
			cb.setTag(arSrc.get(position).events.get(0).cal_srl);
			cb.setChecked(arSrc.get(position).events.get(0).isChecked);
			break;
		case SCHEDULE_FOR_TEACHER:
			txt = (TextView)convertView.findViewById(R.id.schedule_schedule_message);
			txt.setText(arSrc.get(position).events.get(0).description);
			txt = (TextView)convertView.findViewById(R.id.schedule_schedule_show_confirmed_list);
			txt.setTag(arSrc.get(position).events.get(0).cal_srl);
			txt = (TextView)convertView.findViewById(R.id.schedule_schedule_confirmed_list);
			txt.setText("(" + arSrc.get(position).events.get(0).checkInfoList.size() + "/" + arSrc.get(position).events.get(0).numOfClassMember + ")");
			break;
		case BIRTHDAY:
			txt = (TextView)convertView.findViewById(R.id.schedule_birthday_namelist);
			String birthdayList = "";
			for(int i = 0; i < arSrc.get(position).events.size(); i++) {
				if( i != 0 )
					birthdayList += ", ";
				birthdayList += arSrc.get(position).events.get(i).description;
			}
			txt.setText(birthdayList);
			break;
		}
		
		return convertView;
	}
}
