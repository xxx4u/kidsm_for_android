package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.ContextThemeWrapper;
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
import com.ihateflyingbugs.kidsm.friend.FriendListItem;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class NewsAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<News> arSrc;
	Context context;
	ImageLoader imageLoader;
	
	public NewsAdapter(Context context, ArrayList<News> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
		imageLoader = new ImageLoader(context, R.drawable.photo_in_album_default);
	}

	public int getCount() {
		return arSrc.size();
	}

	public News getItem(int position) {
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
		return 4;
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
		int res = 0;
		switch (arSrc.get(position).type) {
		case PHOTO:
			res = R.layout.news_photo;
			break;
		case SCHEDULE:
			res = R.layout.news_schedule;
			break;
		case BUSINFO:
			res = R.layout.news_businfo;
			break;
		case MENTORY:
			res = R.layout.news_mentory;
			break;
		}
		if (convertView == null) {
			convertView = mInflater.inflate(res, parent, false);
		}
		else {
			if( res != convertView.getId() ) {
				convertView = mInflater.inflate(res, parent, false);
			}
		}
		
		// �׸� �並 �ʱ�ȭ�Ѵ�.
		TextView txt;
		Button btn1, btn2;
		ImageView image;
		switch (arSrc.get(position).type) {
		case PHOTO:
			PhotoNews photoNews = (PhotoNews) arSrc.get(position);
			txt = (TextView)convertView.findViewById(R.id.news_photo_profile_parentname);
			txt.setText(photoNews.photo_member_name);
			image = (ImageView)convertView.findViewById(R.id.news_photo_picture);
			imageLoader.DisplayImage(context.getString(R.string.image_url)+photoNews.photo_path, image);
			image.setTag(context.getString(R.string.image_url)+photoNews.photo_path);
			txt = (TextView)convertView.findViewById(R.id.news_photo_timelog);
			txt.setText(makeTimeLog(photoNews.photo_created));

			txt = (TextView)convertView.findViewById(R.id.news_photo_numoflike);
			txt.setText(""+photoNews.likeMemberList.size());
			txt = (TextView)convertView.findViewById(R.id.news_photo_numofreply);
			txt.setText(""+photoNews.commentList.size());
			
			convertView.findViewById(R.id.news_photo_like).setTag(position);
			convertView.findViewById(R.id.news_photo_reply).setTag(position);
			convertView.findViewById(R.id.news_photo_scrap).setTag(position);
			
			String member_srl = "";
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				member_srl = SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl;
				break;
			case 'T':
			case 'M':
				member_srl = SlidingMenuMaker.getProfile().member_srl;
				break;
			}
			txt = (TextView)convertView.findViewById(R.id.news_photo_liketext);
			final CheckBox cb = (CheckBox) convertView.findViewById(R.id.news_photo_like_animation);
			if(arSrc.get(position).likeMemberList.contains(member_srl)) {
				txt.setText(R.string.news_likecancel);
				//AnimationDrawable frameAnimation = (AnimationDrawable) cb.getBackground();
	            // Start the animation (looped playback by default).
	            //frameAnimation.start();
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cb.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_like);
				cb.setChecked(false);
			}
			//cb = (CheckBox) convertView.findViewById(R.id.news_photo_scrap_animation);
			//cb.setChecked(arSrc.get(position).likeMemberList.contains(member_srl));
			
			break;
		case SCHEDULE:
			ScheduleNews scheduleNews = (ScheduleNews) arSrc.get(position);
			txt = (TextView)convertView.findViewById(R.id.news_schedule_date);
			txt.setText(scheduleNews.cal_month+"�� "+scheduleNews.cal_day+"��");
			txt = (TextView)convertView.findViewById(R.id.news_schedule_message);
			txt.setText(scheduleNews.cal_name);
			txt = (TextView)convertView.findViewById(R.id.news_schedule_timelog);
			txt.setText(makeTimeLog(scheduleNews.cal_created));
			break;
		case BUSINFO:
//			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
//			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
//			btn1 = (Button)convertView.findViewById(R.id.friend_button);
//			btn1.setText(R.string.friend_alreadyfriend);
//			btn1.setTag(position);
			break;
		case MENTORY:
//			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
//			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
//			btn1 = (Button)convertView.findViewById(R.id.friend_button);
//			btn1.setText(R.string.friend_requested);
//			btn1.setTag(position);
			break;
		}

		return convertView;
	}
}
