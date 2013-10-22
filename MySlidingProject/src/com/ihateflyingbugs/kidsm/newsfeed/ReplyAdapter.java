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
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 1;
	}

	public String makeTimeLog(String created) {
		long currentTime = System.currentTimeMillis()/1000;
		long timeGap = currentTime - Long.parseLong(created);
		
		if( timeGap > 60*60*24 )
			return ""+timeGap/(24*60*60)+"�� ��";
		if( timeGap > 60*60 )
			return ""+timeGap/3600+"�ð� ��";
		if( timeGap > 60 )
			return ""+(timeGap/60)+"�� ��";
		return "" + timeGap+"�� ��";
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
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
