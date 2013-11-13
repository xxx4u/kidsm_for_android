package com.ihateflyingbugs.kidsm.mentory;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.Utils;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class MentoryAdapter extends BaseAdapter{

	ArrayList<MentoryArticle> list;
	Context ctx;
	int itemLayout;
	ImageLoader imageLoader;
	
	public MentoryAdapter(Context ctx, int itemLayout, ArrayList<MentoryArticle> list){
		this.ctx = ctx;
		this.itemLayout = itemLayout;
		this.list = list;
		this.imageLoader = new ImageLoader(ctx, R.drawable.profile_default);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MentoryArticle getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convrtView, ViewGroup parent) {
		
		if(list.get(position).getLayout() == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list.get(position).setLayout(inflater.inflate(itemLayout, parent, false));
			
//			list.get(position).getLayout().findViewById(R.id.mentory_like).setTag(position);
//			list.get(position).getLayout().findViewById(R.id.mentory_reply).setTag(position);
//			list.get(position).getLayout().findViewById(R.id.mentory_scrap).setTag(position);
//			list.get(position).getLayout().findViewById(R.id.imagebtn_mentory_picture).setTag(position);
//			
//			ImageView btn = (ImageView)list.get(position).getLayout().findViewById(R.id.imagebtn_mentory_picture);
//			btn.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					MainActivity.getMentory().OnSeeMentory(v);
//				}
//			});
//			list.get(position).getLayout().findViewById(R.id.mentory_like).setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					MainActivity.getMentory().OnLikeClick(v);
//				}
//			});
//			list.get(position).getLayout().findViewById(R.id.mentory_reply).setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					MainActivity.getMentory().OnReplyClick(v);
//				}
//			});
//			list.get(position).getLayout().findViewById(R.id.mentory_scrap).setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					MainActivity.getMentory().OnScrapClick(v);
//				}
//			});
		}
		
		TextView txt;
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_timelog);
		txt.setText(Utils.makeTimeLog(list.get(position).getMentoring_created()));
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_numoflike);
		txt.setText(""+list.get(position).getMentoring_likeList().size());
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_numofreply);
		txt.setText(""+list.get(position).getMentoring_commentList().size());
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_numofscrap);
		txt.setText(""+list.get(position).getScrapCount());
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.tv_mentory_article_title);
		txt.setText(list.get(position).getMentoring_subject());
		
		list.get(position).getLayout().findViewById(R.id.mentory_like).setTag(position);
		list.get(position).getLayout().findViewById(R.id.mentory_reply).setTag(position);
		list.get(position).getLayout().findViewById(R.id.mentory_scrap).setTag(position);
		list.get(position).getLayout().findViewById(R.id.imagebtn_mentory_picture).setTag(position);
		
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
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_liketext);
		final CheckBox cblike = (CheckBox) list.get(position).getLayout().findViewById(R.id.mentory_like_animation);
		if(list.get(position).getMentoring_likeList().contains(member_srl)) {
			txt.setText(R.string.news_likecancel);
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
		
		txt = (TextView)list.get(position).getLayout().findViewById(R.id.mentory_scraptext);
		final CheckBox cbscrap = (CheckBox) list.get(position).getLayout().findViewById(R.id.mentory_scrap_animation);
		if(list.get(position).getMember_scrap_srl().isEmpty() == false) {
			txt.setText(R.string.news_donescrap);
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
		
		if( list.get(position).getMentoring_text().contains("<img src=") ) {
			int startIndex = list.get(position).getMentoring_text().indexOf("<img src=") + 10;
			String image_url = list.get(position).getMentoring_text().substring(startIndex).split("\"")[0];
			imageLoader.DisplayImage(image_url, (ImageView)list.get(position).getLayout().findViewById(R.id.imagebtn_mentory_picture));
		}
		/* !!!!!!!!!!!!!!!!!!!!!!! */ 
		/* !!! 카테고리 상수들 향후 서버에서 받아온 값 기준으로 수정 필요!!!! */
		/* !!!!!!!!!!!!!!!!!!!!!!! */
		ImageView categoryIcon = (ImageView)list.get(position).getLayout().findViewById(R.id.imgbtn_mentory_category);
		TextView tvMentoryCategory = (TextView)list.get(position).getLayout().findViewById(R.id.tv_mentory_category);

		//Log.d("MentoryFragment", list.get(pos).getMentoring_category_srl().substring(0,3));
		if(list.get(position).getMentoring_category_srl().length() > 3) {
			if(list.get(position).getMentoring_category_srl().substring(0,3).equals("100") || list.get(position).getMentoring_category_srl().substring(0,3).equals("110") || list.get(position).getMentoring_category_srl().substring(0,3).equals("111")) {
				categoryIcon.setBackground(list.get(position).getLayout().getResources().getDrawable(R.drawable.mento_icon_edu));
				tvMentoryCategory.setText("교육멘토리");
			}
			else if(list.get(position).getMentoring_category_srl().substring(0,3).equals("200")) {
				categoryIcon.setBackground(list.get(position).getLayout().getResources().getDrawable(R.drawable.mento_icon_nur));
				tvMentoryCategory.setText("육아멘토리");
			}
			else if(list.get(position).getMentoring_category_srl().substring(0,3).equals("300")) {
				categoryIcon.setBackground(list.get(position).getLayout().getResources().getDrawable(R.drawable.mento_icon_nur));
				tvMentoryCategory.setText("운영멘토리");
			}
			else if(list.get(position).getMentoring_category_srl().substring(0,3).equals("400")) {
				categoryIcon.setBackground(list.get(position).getLayout().getResources().getDrawable(R.drawable.mento_icon_healing));
				tvMentoryCategory.setText("힐링멘토리");
			}
			else if(list.get(position).getMentoring_category_srl().substring(0,3).equals("500")) {
				categoryIcon.setBackground(list.get(position).getLayout().getResources().getDrawable(R.drawable.mento_icon_edu));
				tvMentoryCategory.setText("키즈엠소식");
			}
		}
		else {
			Log.d("MentoryFragment", list.get(position).getMentoring_category_srl());
		}
		
//		final int pos = position;
//
//		MentoryViewHolder holder;
//
//		//if(convertView==null){
//		if(true){
//			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(itemLayout, parent, false);
//
//			holder = new MentoryViewHolder();
//
//			holder.textView = (TextView)convertView.findViewById(R.id.tv_mentory_article_title);
//
//			holder.textView.setText(list.get(pos).getMentoring_subject());
//
//			/*
//		int color = 0;
//		if(list.get(position).equals("Red")) color = Color.RED;
//		else if(list.get(position).equals("Green")) color = Color.GREEN;
//		else if(list.get(position).equals("Blue")) color = Color.BLUE;
//			 */
//
//
////			ImageButton btn = (ImageButton)convertView.findViewById(R.id.imagebtn_mentory_photo_picture);
////			btn.setOnClickListener(new View.OnClickListener() {
////				@Override
////				public void onClick(View v) {
////					Intent intent = new Intent(ctx, SeeMentoryActivity.class);
////					intent.putExtra("mentoring_text", list.get(pos).getMentoring_text());
////					//Toast.makeText(ctx, list.get(pos).getMentoring_text(), Toast.LENGTH_SHORT).show();
////					startActivity(intent);
////
////				}
////			});
//		}
//
//		//	else{
//		//		
//		//		//holder = (BusstopViewHolder)convertView.getTag();
//		//	}

		return list.get(position).getLayout();
	}

	class MentoryViewHolder {
		public LinearLayout cellLayout;
		public View colorBar;
		public ImageView busstopIcon;
		public TextView textView;
		public Button nextButton;

		public View blueTopLine;
		public View blueBottomLine;
		public View greyTopLine;
		public View greyBottomLine;
	}
}
