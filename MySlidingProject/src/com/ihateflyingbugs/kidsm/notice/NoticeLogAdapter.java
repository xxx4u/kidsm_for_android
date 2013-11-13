package com.ihateflyingbugs.kidsm.notice;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.newsfeed.Reply;

public class NoticeLogAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<NoticeLog> arSrc;
	Context context;
	ImageLoader imageLoader;
	
	public NoticeLogAdapter(Context context, ArrayList<NoticeLog> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
		imageLoader = new ImageLoader(context, R.drawable.photo_in_album_default);
	}

	public int getCount() {
		return arSrc.size();
	}

	public NoticeLog getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
//	public int getItemViewType(int position) {
//		return arSrc.get(position).type;
//	}
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 2;
	}

	public String makeTimeLog(String created) {
		long currentTime = System.currentTimeMillis()/1000;
		long timeGap = currentTime - Long.parseLong(created);
		
		if( timeGap > 60*60*24 )
			return ""+timeGap/(24*60*60)+"일 전";
		if( timeGap > 60*60 )
			return ""+timeGap/3600+"시간 전";
		if( timeGap > 60 )
			return ""+(timeGap/60)+"분 전";
		return "" + timeGap+"초 전";
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (convertView == null) {
			switch(arSrc.get(position).type) {
			case NORMAL:
				convertView = mInflater.inflate(R.layout.notice_textonly, parent, false);
				break;
			case PHOTO:
				convertView = mInflater.inflate(R.layout.photonotice_textonly, parent, false);
				break;
			}
		}

		TextView txt;
		switch(arSrc.get(position).type) {
		case NORMAL:
			txt = (TextView)convertView.findViewById(R.id.notice_message);
			txt.setText(arSrc.get(position).message);
			txt = (TextView)convertView.findViewById(R.id.notice_timelog);
			txt.setText(makeTimeLog(arSrc.get(position).ticker));
			break;
		case PHOTO:
			txt = (TextView)convertView.findViewById(R.id.photonotice_message);
			txt.setText(arSrc.get(position).message);
			txt = (TextView)convertView.findViewById(R.id.photonotice_timelog);
			txt.setText(makeTimeLog(arSrc.get(position).ticker));
			break;
		}
		
		return convertView;
	}
}
