package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class ReplyAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<Reply> arSrc;
	Context context;
	ImageLoader imageLoader;
	
	public ReplyAdapter(Context context, ArrayList<Reply> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
		imageLoader = new ImageLoader(context, R.drawable.photo_in_album_default);
	}

	public int getCount() {
		return arSrc.size();
	}

	public Reply getItem(int position) {
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
		return 1;
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
			convertView = mInflater.inflate(R.layout.newsfeed_reply, parent, false);
		}

		TextView txt = (TextView)convertView.findViewById(R.id.reply_name);
		txt.setText(arSrc.get(position).tcomment_member_srl);
		txt = (TextView)convertView.findViewById(R.id.reply_secondtext);
		txt.setText(arSrc.get(position).tcomment_message);
		txt = (TextView)convertView.findViewById(R.id.reply_timelog);
		txt.setText(makeTimeLog(arSrc.get(position).tcomment_created));
		
		return convertView;
	}
}
