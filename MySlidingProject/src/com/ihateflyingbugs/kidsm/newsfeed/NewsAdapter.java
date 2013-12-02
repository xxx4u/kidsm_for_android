package com.ihateflyingbugs.kidsm.newsfeed;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
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
import com.ihateflyingbugs.kidsm.Utils;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class NewsAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<News> arSrc;
	Context context;
	ImageLoader imageLoader;
	
	private static final Collator sCollator = Collator.getInstance();  //Collator 객체 생성
	

	//Adapter에 들어가는 List에 대한 자료형 으로 정렬 ( a, b의 순서를 바꾸면 역정렬 )
	public static class ItemInfoSort implements Comparator<News> {            
	        public final int compare(News a, News b) {
	            return sCollator.compare(a.timeline_created, b.timeline_created);  
	        }
	}
	
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
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 4;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
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
			res = R.layout.mentory_row;
			break;
		case RECOMMENDED_MENTORY:
			res = R.layout.news_recommended_mentory;
			break;
		}
		if (arSrc.get(position).layout == null) {
			arSrc.get(position).layout = mInflater.inflate(res, parent, false);
			//arSrc.get(position).layout = convertView;
			//convertView.setTag(res);
		}
//		else {
//			if( res != Integer.parseInt(convertView.getTag().toString()) ) {
//				convertView = mInflater.inflate(res, parent, false);
//			}
//		}
		
		// 항목 뷰를 초기화한다.
		TextView txt;
		Button btn1, btn2;
		ImageView image;
		switch (arSrc.get(position).type) {
		case PHOTO:
			PhotoNews photoNews = (PhotoNews) arSrc.get(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_profile_parentname);
			txt.setText(photoNews.photo_member_name);
			image = (ImageView)arSrc.get(position).layout.findViewById(R.id.news_photo_picture);
			//imageLoader.DisplayImage(context.getString(R.string.image_url)+photoNews.photo_path, image);
			//imageLoader.DisplayImage(context.getString(R.string.image_url)+photoNews.photo_thumbnail, image);
			String thumbnail640URL = (context.getString(R.string.image_url)+photoNews.photo_thumbnail);
			Log.d("NewsAdapter", thumbnail640URL);
			thumbnail640URL = thumbnail640URL.substring(0, thumbnail640URL.length() - 9);
			thumbnail640URL = thumbnail640URL + "640x640.png";
			imageLoader.DisplayImage(thumbnail640URL, image);
			Log.d("NewsAdapter", thumbnail640URL);
			image.setTag(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_timelog);
			txt.setText(Utils.makeTimeLog(photoNews.photo_created));

			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_numoflike);
			txt.setText(""+photoNews.likeMemberList.size());
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_numofreply);
			txt.setText(""+photoNews.commentList.size());
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_numofscrap);
			txt.setText(""+photoNews.scrapCount);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_message);
			txt.setText(photoNews.photo_message);
			
			arSrc.get(position).layout.findViewById(R.id.news_photo_like).setTag(position);
			arSrc.get(position).layout.findViewById(R.id.news_photo_reply).setTag(position);
			arSrc.get(position).layout.findViewById(R.id.news_photo_scrap).setTag(position);
			
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
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_liketext);
			final CheckBox cblike = (CheckBox) arSrc.get(position).layout.findViewById(R.id.news_photo_like_animation);
			if(arSrc.get(position).likeMemberList.contains(member_srl)) {
				txt.setText(R.string.news_likecancel);
				//AnimationDrawable frameAnimation = (AnimationDrawable) cb.getBackground();
	            // Start the animation (looped playback by default).
	            //frameAnimation.start();
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cblike.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_like);
				cblike.setChecked(false);
			}
			
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_photo_scraptext);
			final CheckBox cbscrap = (CheckBox) arSrc.get(position).layout.findViewById(R.id.news_photo_scrap_animation);
			if(arSrc.get(position).member_scrap_srl.isEmpty() == false) {
				txt.setText(R.string.news_donescrap);
				//AnimationDrawable frameAnimation = (AnimationDrawable) cb.getBackground();
	            // Start the animation (looped playback by default).
	            //frameAnimation.start();
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cbscrap.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_scrap);
				cbscrap.setChecked(false);
			}
			//cb = (CheckBox) arSrc.get(position).layout.findViewById(R.id.news_photo_scrap_animation);
			//cb.setChecked(arSrc.get(position).likeMemberList.contains(member_srl));
			if(photoNews.photo_member_picture_srl != null && photoNews.photo_member_picture_srl.startsWith("profile"))
				imageLoader.DisplayCroppedImage(context.getString(R.string.default_profile_url)+photoNews.photo_member_picture_srl, (ImageView)arSrc.get(position).layout.findViewById(R.id.news_photo_profile_picture));
			else
				imageLoader.DisplayCroppedImage(context.getString(R.string.profile_url)+photoNews.photo_member_picture_srl, (ImageView)arSrc.get(position).layout.findViewById(R.id.news_photo_profile_picture));
			
			break;
		case SCHEDULE:
			ScheduleNews scheduleNews = (ScheduleNews) arSrc.get(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_schedule_date);
			txt.setText(scheduleNews.cal_month+"월 "+scheduleNews.cal_day+"일");
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_schedule_message);
			txt.setText(scheduleNews.cal_name);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_schedule_timelog);
			txt.setText(Utils.makeTimeLog(scheduleNews.cal_created));
			break;
		case BUSINFO:
			BusinfoNews businfoNews = (BusinfoNews) arSrc.get(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_businfo_timelog);
			txt.setText(Utils.makeTimeLog(""+businfoNews.timeline_created));
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.news_businfo_message);
			txt.setText(businfoNews.shuttle_name+": 정거장을 지났습니다.");
//			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.friend_friendname);
//			txt.setText(arSrc.get(position).getName()+" / 자녀 : "+arSrc.get(position).getChildname());
//			btn1 = (Button)arSrc.get(position).layout.findViewById(R.id.friend_button);
//			btn1.setText(R.string.friend_alreadyfriend);
//			btn1.setTag(position);
			break;
		case MENTORY:
			MentoryNews mentoryNews = (MentoryNews) arSrc.get(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.mentory_timelog);
			txt.setText(Utils.makeTimeLog(mentoryNews.getMentoring_created()));
			txt = (TextView)mentoryNews.layout.findViewById(R.id.mentory_numoflike);
			txt.setText(""+mentoryNews.likeMemberList.size());
			txt = (TextView)mentoryNews.layout.findViewById(R.id.mentory_numofreply);
			txt.setText(""+mentoryNews.commentList.size());
			txt = (TextView)mentoryNews.layout.findViewById(R.id.mentory_numofscrap);
			txt.setText(""+mentoryNews.scrapCount);
			txt = (TextView)mentoryNews.layout.findViewById(R.id.tv_mentory_article_title);
			txt.setText(mentoryNews.getMentoring_subject());
			
			mentoryNews.layout.findViewById(R.id.mentory_like).setTag(position);
			mentoryNews.layout.findViewById(R.id.mentory_reply).setTag(position);
			mentoryNews.layout.findViewById(R.id.mentory_scrap).setTag(position);
			mentoryNews.layout.findViewById(R.id.imagebtn_mentory_picture).setTag(position);
			
			member_srl = "";
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				member_srl = SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl;
				break;
			case 'T':
			case 'M':
				member_srl = SlidingMenuMaker.getProfile().member_srl;
				break;
			}
			txt = (TextView)mentoryNews.layout.findViewById(R.id.mentory_liketext);
			final CheckBox cblike2 = (CheckBox) mentoryNews.layout.findViewById(R.id.mentory_like_animation);
			if(mentoryNews.likeMemberList.contains(member_srl)) {
				txt.setText(R.string.news_likecancel);
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cblike2.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_like);
				cblike2.setChecked(false);
			}
			
			txt = (TextView)mentoryNews.layout.findViewById(R.id.mentory_scraptext);
			final CheckBox cbscrap2 = (CheckBox) mentoryNews.layout.findViewById(R.id.mentory_scrap_animation);
			if(mentoryNews.member_scrap_srl.isEmpty() == false) {
				txt.setText(R.string.news_donescrap);
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cbscrap2.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_scrap);
				cbscrap2.setChecked(false);
			}
			
			if( mentoryNews.getMentoring_text() != null && mentoryNews.getMentoring_text().isEmpty() == false &&
					mentoryNews.getMentoring_text().contains("<img src=") ) {
				int startIndex = mentoryNews.getMentoring_text().indexOf("<img src=") + 10;
				String image_url = mentoryNews.getMentoring_text().substring(startIndex).split("\"")[0];
				imageLoader.DisplayImage(image_url, (ImageView)mentoryNews.layout.findViewById(R.id.imagebtn_mentory_picture));
			}
			/* !!!!!!!!!!!!!!!!!!!!!!! */ 
			/* !!! 카테고리 상수들 향후 서버에서 받아온 값 기준으로 수정 필요!!!! */
			/* !!!!!!!!!!!!!!!!!!!!!!! */
			ImageView categoryIcon = (ImageView)mentoryNews.layout.findViewById(R.id.imgbtn_mentory_category);
			TextView tvMentoryCategory = (TextView)mentoryNews.layout.findViewById(R.id.tv_mentory_category);

			//Log.d("MentoryFragment", arSrc.get(pos).getMentoring_category_srl().substring(0,3));
			if(mentoryNews.getMentoring_category_srl() != null && mentoryNews.getMentoring_category_srl().isEmpty() == false && 
					mentoryNews.getMentoring_category_srl().length() > 3) {
				if(mentoryNews.getMentoring_category_srl().substring(0,3).equals("100") || mentoryNews.getMentoring_category_srl().substring(0,3).equals("110") || mentoryNews.getMentoring_category_srl().substring(0,3).equals("111")) {
					categoryIcon.setBackground(mentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_edu));
					tvMentoryCategory.setText("교육멘토리");
				}
				else if(mentoryNews.getMentoring_category_srl().substring(0,3).equals("200")) {
					categoryIcon.setBackground(mentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_nur));
					tvMentoryCategory.setText("육아멘토리");
				}
				else if(mentoryNews.getMentoring_category_srl().substring(0,3).equals("300")) {
					categoryIcon.setBackground(mentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_nur));
					tvMentoryCategory.setText("운영멘토리");
				}
				else if(mentoryNews.getMentoring_category_srl().substring(0,3).equals("400")) {
					categoryIcon.setBackground(mentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_healing));
					tvMentoryCategory.setText("힐링멘토리");
				}
				else if(mentoryNews.getMentoring_category_srl().substring(0,3).equals("500")) {
					categoryIcon.setBackground(mentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_edu));
					tvMentoryCategory.setText("키즈엠소식");
				}
			}
			else {
				//Log.d("MentoryFragment", mentoryNews.getMentoring_category_srl());
			}
			break;
		case RECOMMENDED_MENTORY:
			RecommendedMentoryNews recommendedMentoryNews = (RecommendedMentoryNews) arSrc.get(position);
			txt = (TextView)arSrc.get(position).layout.findViewById(R.id.mentory_timelog);
			txt.setText(Utils.makeTimeLog(recommendedMentoryNews.getMentoring_created()));
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.mentory_numoflike);
			txt.setText(""+recommendedMentoryNews.likeMemberList.size());
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.mentory_numofreply);
			txt.setText(""+recommendedMentoryNews.commentList.size());
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.mentory_numofscrap);
			txt.setText(""+recommendedMentoryNews.scrapCount);
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.tv_mentory_article_title);
			txt.setText(recommendedMentoryNews.getMentoring_subject());
			
			recommendedMentoryNews.layout.findViewById(R.id.mentory_like).setTag(position);
			recommendedMentoryNews.layout.findViewById(R.id.mentory_reply).setTag(position);
			recommendedMentoryNews.layout.findViewById(R.id.mentory_scrap).setTag(position);
			recommendedMentoryNews.layout.findViewById(R.id.imagebtn_mentory_picture).setTag(position);
			
			member_srl = "";
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				member_srl = SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl;
				break;
			case 'T':
			case 'M':
				member_srl = SlidingMenuMaker.getProfile().member_srl;
				break;
			}
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.mentory_liketext);
			final CheckBox cblike3 = (CheckBox) recommendedMentoryNews.layout.findViewById(R.id.mentory_like_animation);
			if(recommendedMentoryNews.likeMemberList.contains(member_srl)) {
				txt.setText(R.string.news_likecancel);
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cblike3.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_like);
				cblike3.setChecked(false);
			}
			
			txt = (TextView)recommendedMentoryNews.layout.findViewById(R.id.mentory_scraptext);
			final CheckBox cbscrap3 = (CheckBox) recommendedMentoryNews.layout.findViewById(R.id.mentory_scrap_animation);
			if(recommendedMentoryNews.member_scrap_srl.isEmpty() == false) {
				txt.setText(R.string.news_donescrap);
				new Handler().postDelayed(new Runnable() { 
			        public void run() {
						cbscrap3.setChecked(true);
			        } 
			    }, 100);
			}
			else {
				txt.setText(R.string.news_scrap);
				cbscrap3.setChecked(false);
			}
			
			if( recommendedMentoryNews.getMentoring_text() != null && recommendedMentoryNews.getMentoring_text().isEmpty() == false &&
					recommendedMentoryNews.getMentoring_text().contains("<img src=") ) {
				int startIndex = recommendedMentoryNews.getMentoring_text().indexOf("<img src=") + 10;
				String image_url = recommendedMentoryNews.getMentoring_text().substring(startIndex).split("\"")[0];
				imageLoader.DisplayImage(image_url, (ImageView)recommendedMentoryNews.layout.findViewById(R.id.imagebtn_mentory_picture));
			}
			/* !!!!!!!!!!!!!!!!!!!!!!! */ 
			/* !!! 카테고리 상수들 향후 서버에서 받아온 값 기준으로 수정 필요!!!! */
			/* !!!!!!!!!!!!!!!!!!!!!!! */
			ImageView categoryIcon2 = (ImageView)recommendedMentoryNews.layout.findViewById(R.id.imgbtn_mentory_category);
			TextView tvMentoryCategory2 = (TextView)recommendedMentoryNews.layout.findViewById(R.id.tv_mentory_category);

			//Log.d("MentoryFragment", arSrc.get(pos).getMentoring_category_srl().substring(0,3));
			if(recommendedMentoryNews.getMentoring_category_srl() != null && recommendedMentoryNews.getMentoring_category_srl().isEmpty() == false && 
					recommendedMentoryNews.getMentoring_category_srl().length() > 3) {
				if(recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("100") || recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("110") || recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("111")) {
					categoryIcon2.setBackground(recommendedMentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_edu));
					tvMentoryCategory2.setText("교육멘토리");
				}
				else if(recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("200")) {
					categoryIcon2.setBackground(recommendedMentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_nur));
					tvMentoryCategory2.setText("육아멘토리");
				}
				else if(recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("300")) {
					categoryIcon2.setBackground(recommendedMentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_nur));
					tvMentoryCategory2.setText("운영멘토리");
				}
				else if(recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("400")) {
					categoryIcon2.setBackground(recommendedMentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_healing));
					tvMentoryCategory2.setText("힐링멘토리");
				}
				else if(recommendedMentoryNews.getMentoring_category_srl().substring(0,3).equals("500")) {
					categoryIcon2.setBackground(recommendedMentoryNews.layout.getResources().getDrawable(R.drawable.mento_icon_edu));
					tvMentoryCategory2.setText("키즈엠소식");
				}
			}
			else {
				//Log.d("MentoryFragment", mentoryNews.getMentoring_category_srl());
			}
			
			if(recommendedMentoryNews.getTimeline_member_name() != null && recommendedMentoryNews.getTimeline_member_name().isEmpty() == false ) {
				txt = (TextView)arSrc.get(position).layout.findViewById(R.id.recommended_member);
				txt.setText(recommendedMentoryNews.getTimeline_member_name());
			}
			break;
		}

		return arSrc.get(position).layout;
	}
}
