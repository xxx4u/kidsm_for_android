package com.ihateflyingbugs.kidsm.newsfeed;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.WrappingSlidingDrawer;
import com.ihateflyingbugs.kidsm.mentory.MentoryArticle;
import com.ihateflyingbugs.kidsm.mentory.SeeMentoryActivity;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;
import com.ihateflyingbugs.kidsm.showimage.ShowImageActivity;

public class NewsfeedFragment extends NetworkFragment {
	LayoutInflater inflater;
	
	
	View layout;
	ArrayList<News> newsList;
	NewsAdapter newsAdapter;
	ListView newsListView;
	
	ArrayList<News> newNewsList;
	
	int timelineIndex;
	int Counter;
	int requestGetParentForProfileImageCounter;
	boolean currentlyRequestNews;
	
	WrappingSlidingDrawer drawer;
	float prevTouchY;
	String filePath;
	boolean isFirstScrollDone;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null) {
			refreshTimeline();
			return layout;
		}
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_newsfeed, container, false);
		
		auth_key = MainActivity.auth_key;
		
		currentlyRequestNews = false;
		newNewsList = new ArrayList<News>();
		//newsList = new HashMap<String, News>();
		newsList = new ArrayList<News>();
		newsAdapter = new NewsAdapter(getActivity(), newsList);
		timelineIndex = 0;
		isFirstScrollDone = false;
		getTimeLineMessages();
        
		newsListView = (ListView)layout.findViewById(R.id.newsfeed);

		newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			            // Make your calculation stuff here. You have all your
			            // needed info from the parameters of this function.

			            // Sample calculation to determine if the last 
			            // item is fully visible.
	            final int lastItem = firstVisibleItem + visibleItemCount;
	            if(lastItem == totalItemCount) {
	                // Last item is fully visible.
	            	if( isFirstScrollDone )
	            		getTimeLineMessages();
	            }
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
		});
		newsListView.setDivider(null);
		newsListView.setDividerHeight(20);
		newsListView.setAdapter(newsAdapter);
		
		filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/attachimage.jpg";
        drawer = (WrappingSlidingDrawer)layout.findViewById(R.id.newsfeed_drawer);
        drawer.animateOpen();
		
		return layout;
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		if( event.getAction() == MotionEvent.ACTION_DOWN ) {
			prevTouchY = event.getY();
		}
		else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			float gap = Math.abs(event.getY() - prevTouchY);
			if( gap > height/20 ) {
				if( event.getY() - prevTouchY < 0 ) {
					if( drawer.isOpened() == false ) {
						drawer.animateOpen();
					}
				}
				else {
					if( drawer.isOpened() == true ) {
						drawer.animateClose();
					}
				}
				prevTouchY = event.getY();
			}
		}
		else if( event.getAction() == MotionEvent.ACTION_UP ) {
		}
	    return false;
	}
	
	public void OnUploadPhoto(View v) {
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gallery_uploadmode, android.R.layout.simple_list_item_1);
		ListView listView = new ListView(getActivity());
		listView.setAdapter(arrayAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final AlertDialog alert = new AlertDialog.Builder(getActivity())
		.setTitle(getString(R.string.upload_photo))
		.setView(listView).create();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent;
				switch(position) {
				case 0:
					MainActivity.changeFragment(1);
					MainActivity.getMainActivity().getGallery().setRequestFromNewsfeed(1);
					break;
				case 1:
					MainActivity.changeFragment(1);
					MainActivity.getMainActivity().getGallery().setRequestFromNewsfeed(2);
					break;
				}
				alert.dismiss();
			}
		});
		alert.show();
	}
	
//	public static News getNews(String key) {
//		//deprecate
//		return newsList.get(key);
//	}
	
//	public static void setNews(String key, News news) {
//		newsList.put(key, news);
//	}
	
	private void refreshTimeline() {
		newsList.clear();
		newNewsList.clear();
		timelineIndex = 0;
		Counter = 0;
		requestGetParentForProfileImageCounter = 0;
		getTimeLineMessages();
	}
	
	private void getTimeLineMessages() {
		if( currentlyRequestNews )
			return;
		currentlyRequestNews = true;
		
		newNewsList.clear();
		//newsMap.put(makeIdentifier("B", "0"), new BusinfoNews("0", "", ""));
		//newsMap.put(makeIdentifier("M", "0"), new MentoryNews("1", "", ""));
		
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			if( SlidingMenuMaker.getProfile().childrenList.size() > 1 )
   				this.request_Timeline_getTimelineMessages(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, ++timelineIndex, 10);
			else
				Toast.makeText(getActivity(), "키즈엠 소속 유치원 소속회원만 소식을 받아볼 수 있습니다.", Toast.LENGTH_SHORT).show();
			break;
		case 'T':
		case 'M':
			this.request_Timeline_getTimelineMessages(SlidingMenuMaker.getProfile().member_srl, ++timelineIndex, 10);
			break;
		}
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("member_srl", "4"));
//		params.add(new BasicNameValuePair("index", "1"));
//		params.add(new BasicNameValuePair("count", "5"));
//		GET("Timeline/getTimelineMessages", params);
		
//		addNewsFeed_TextWithPhoto("1", 1, 1, "1", "가족사진 찍었어요~", "IMAGE");
//		addNewsFeed_TextWithPhoto("1", 1, 1, "1", "가족사진 찍었어요~", "IMAGE");
//		addNewsFeed_TextWithPhoto("1", 1, 1, "1", "가족사진 찍었어요~", "IMAGE");
	}
	
	private void addNewsFeed_TextOnly(String srl, int member_srl, int target_srl, String created, String message) {
//		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.news_textonly, null);
//		ArrayList<Reply> replyList = new ArrayList<Reply>();
//		replyList.add(new Reply("쥐며느리", "image", "이 정도는 기본이죠."));
//		ArrayList<LikeMember> likeList = new ArrayList<LikeMember>();
//		likeList.add(new LikeMember("민머리"));
//		likeList.add(new LikeMember("나리엄마"));
//		News news = new News(srl, member_srl, target_srl, created, message, false, false, 2, linear, replyList, likeList);
//		//newsList.put(srl, news);
//		
//		LinearLayout svw = (LinearLayout)layout.findViewById(R.id.newsfeed);
//		svw.addView(linear);
	}
	
	private void addNewsfeed(News news) {
//		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.news_textwithphoto, null);
//		ArrayList<Reply> replyList = new ArrayList<Reply>();
//		replyList.add(new Reply("쥐며느리", "image", "부러워라 ㅜㅜㅜ"));
//		ArrayList<LikeMember> likeList = new ArrayList<LikeMember>();
//		likeList.add(new LikeMember("민머리"));
//		likeList.add(new LikeMember("나리엄마"));
//		News news = new News(""+newsList.size()+""+newsList.size(), member_srl, target_srl, created, "IMAGE", message, false, false, 2, linear, replyList, likeList);
//		newsList.put(""+newsList.size()+""+newsList.size(), news);
		//newsList.put(srl, news);
		
//		LinearLayout svw = (LinearLayout)layout.findViewById(R.id.newsfeed);
//		svw.addView(linear);
	}
	
	public void OnLikeClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
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
		switch( newsList.get(position).type ) {
		case PHOTO:
			PhotoNews photoNews = (PhotoNews) newsList.get(position);
			if( photoNews.likeMemberList.contains(member_srl) == false )
				this.request_Album_setPhotoLike(photoNews.photo_srl, member_srl);
			else
				this.request_Album_delPhotoLike(photoNews.photo_srl, member_srl);
			break;
		case MENTORY:
			MentoryNews mentoryNews = (MentoryNews) newsList.get(position);
			if( mentoryNews.likeMemberList.contains(member_srl) == false )
				this.request_Mentor_setMentoringArticleLikes(mentoryNews.getMentoring_srl(), member_srl);
			else
				this.request_Mentor_delMentoringArticleLikes(mentoryNews.getMentoring_srl(), member_srl);
			break;
		}
	}
	
	public void OnReplyClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		Intent intent = new Intent(getActivity(), ReplyActivity.class);
		switch( newsList.get(position).type ) {
		case PHOTO:
			intent.putExtra("type", "T");
			intent.putExtra("timeline_srl", newsList.get(position).timeline_srl);
			intent.putExtra("timeline_member_srl", newsList.get(position).timeline_member_srl);
			startActivity(intent);
			break;
		case MENTORY:
			MentoryNews mentoryNews = (MentoryNews) newsList.get(position);
			intent = new Intent(getActivity(), ReplyActivity.class);
			intent.putExtra("type", "M");
			intent.putExtra("mentoring_srl", mentoryNews.getMentoring_srl());
			startActivity(intent);
			break;
		}
	}
	
	public void OnScrapClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
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
		switch( newsList.get(position).type ) {
		case PHOTO:
			PhotoNews photoNews = (PhotoNews) newsList.get(position);
			if( photoNews.member_scrap_srl.isEmpty() )
				this.request_Scrap_setScrap(member_srl, "P", photoNews.photo_srl);
			else
				this.request_Scrap_delScrap(member_srl, newsList.get(position).member_scrap_srl, photoNews.photo_srl);
			break;
		case MENTORY:
			MentoryNews mentoryNews = (MentoryNews) newsList.get(position);
			if( mentoryNews.member_scrap_srl.isEmpty() )
				this.request_Scrap_setScrap(member_srl, "M", mentoryNews.getMentoring_srl());
			else
				this.request_Scrap_delScrap(member_srl, mentoryNews.member_scrap_srl, mentoryNews.getMentoring_srl());
			break;
		}
	}
	
	public void OnImageClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		PhotoNews photoNews = (PhotoNews) newsList.get(position);
		Intent intent = new Intent(getActivity(), ShowImageActivity.class);
		intent.putExtra("photo_url", photoNews.photo_path);
		intent.putExtra("photo_srl", photoNews.photo_srl);
		startActivity(intent);
	}
	
	public void OnSeeMentory(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		MentoryNews mentoryNews = (MentoryNews) newsList.get(position);
		Intent intent = new Intent(getActivity(), SeeMentoryActivity.class);
		intent.putExtra("mentoring_text", mentoryNews.getMentoring_text());
		intent.putExtra("mentoring_subject", mentoryNews.getMentoring_subject());
		intent.putExtra("mentoring_srl", mentoryNews.getMentoring_srl());
		startActivity(intent);
	}
	
	private void notifyDataSetChanged() {
		if( --Counter == 0 ) {
			new Thread(new Runnable() {
			    @Override
			    public void run() {    
		        	NewsfeedFragment.this.getActivity().runOnUiThread(new Runnable(){
			            @Override
			             public void run() {
			            	newsList.addAll(newNewsList);
			            	newNewsList.clear();
							if( isFirstScrollDone == false )
								isFirstScrollDone = true;
			            	newsAdapter.notifyDataSetChanged();
			            	currentlyRequestNews = false;
			            }
			        });
			    }
			}).start();
		}
	}
	
	
	
	public void refreshList() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        NewsfeedFragment.this.getActivity().runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	newsAdapter.notifyDataSetChanged();
		            }
		        });
		    }
		}).start();
	}
	
	private String makeIdentifier(String type, String srl) {
		return type+srl;
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			Log.d("NewsfeedFragment", uri);
			Log.d("NewsfeedFragment", response);
			
			if(response.startsWith("<!DOCTYPE html>")) {
				if(uri.equals("Calender/getCalender")) {
					notifyDataSetChanged();
				}
				else if(uri.equals("Album/getPhoto")) {
					notifyDataSetChanged();
				}
				else if(uri.equals("Mentor/getMentoringArticle")) {
					notifyDataSetChanged();
				}
				else if(uri.equals("Shuttlebus/getShuttlebus")) {
					notifyDataSetChanged();
				}
				return;
			}
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") == false ) {
				if( uri.equals("Calender/getCalender")) {
					notifyDataSetChanged();
				}
				return;
			}

			if( uri.equals("Timeline/getTimelineMessages") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				Counter = dataArray.length();
				if( Counter == 0 ) {
					Counter++;
					notifyDataSetChanged();
				}
				for(int i = 0; i < dataArray.length(); i++ ) {
					JSONObject dataObj = dataArray.getJSONObject(i);
					final String timeline_type = dataObj.getString("timeline_type");
					final String timeline_srl = dataObj.getJSONObject("_id").getString("$oid");
					final String timeline_member_srl = dataObj.getString("timeline_member_srl");
					final String timeline_target_member_srl = dataObj.getString("timeline_target_member_srl");
					final String timeline_target_srl = dataObj.getString("timeline_target_srl");
					final String timeline_like = dataObj.getString("timeline_like");
					final String timeline_comment = dataObj.getString("timeline_comment");
					final String timeline_created = dataObj.getString("timeline_created");
					switch(timeline_type.charAt(0)) {
					case 'S':
						boolean isScheduleAlreadyIn = false;
						for(int j = 0; j < newNewsList.size(); j++) {
							if( newNewsList.get(j).identifier != null && newNewsList.get(j).identifier.equals(makeIdentifier(""+timeline_type.charAt(0), timeline_target_srl))) {
								isScheduleAlreadyIn = true;
								break;
							}
						}
						if(isScheduleAlreadyIn) {
							notifyDataSetChanged();
						}
						else {
							newNewsList.add(new ScheduleNews(makeIdentifier(timeline_type, timeline_target_srl), timeline_srl, timeline_member_srl, timeline_like, timeline_created));
							this.request_Calender_getCalender(timeline_target_srl);
						}
						break;
					case 'P':
						boolean isPhotoAlreadyIn = false;
						for(int j = 0; j < newNewsList.size(); j++) {
							if( newNewsList.get(j).identifier != null && newNewsList.get(j).identifier.equals(makeIdentifier(""+timeline_type.charAt(0), timeline_target_srl))) {
								isPhotoAlreadyIn = true;
								break;
							}
						}
						if(isPhotoAlreadyIn) {
							notifyDataSetChanged();
						}
						else {
							newNewsList.add(new PhotoNews(makeIdentifier(timeline_type, timeline_target_srl), timeline_srl, timeline_member_srl, timeline_like, timeline_created));
							this.request_Album_getPhoto(timeline_target_srl);
							this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
							this.request_Scrap_getScrapCount(timeline_target_srl, "P");
						}
						break;
					case 'B':
						newNewsList.add(new BusinfoNews(makeIdentifier(timeline_type, timeline_target_srl), timeline_srl, timeline_member_srl, timeline_like, timeline_created));
						String org_srl = "";
						switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
						case 'P':
							org_srl = SlidingMenuMaker.getProfile().getCurrentChildren().student_org_srl;
							break;
						case 'T':
						case 'M':
							org_srl = SlidingMenuMaker.getProfile().member_org_srl;
							break;
						}
						this.request_Shuttlebus_getShuttlebus(timeline_target_srl, org_srl);
						break;
					case 'M':
//						switch(timeline_type.charAt(1)) {
//						case 'P':
//							break;
//						case 'T':
//							break;
//						case 'M':
//							break;
//						}
						boolean isMentoryAlreadyIn = false;
						for(int j = 0; j < newNewsList.size(); j++) {
							if( newNewsList.get(j).identifier != null && newNewsList.get(j).identifier.equals(makeIdentifier(""+timeline_type.charAt(0), timeline_target_srl.split("/")[1]))) {
								isMentoryAlreadyIn = true;
								break;
							}
						}
						if(isMentoryAlreadyIn) {
							notifyDataSetChanged();
						}
						else {
							MentoryNews mentoryNews = new MentoryNews(makeIdentifier(""+timeline_type.charAt(0), timeline_target_srl.split("/")[1]), timeline_srl, timeline_member_srl, timeline_like, timeline_created);
							mentoryNews.setTimeline_type(timeline_type);
							newNewsList.add(mentoryNews);
							this.request_Mentor_getMentoringArticle(timeline_target_srl.split("/")[1]);
						}
						break;
					case 'D':
						switch(timeline_type.charAt(1)) {
						case 'P':
							newNewsList.add(new PhotoNews(makeIdentifier("P", timeline_target_srl), timeline_srl, timeline_member_srl, timeline_like, timeline_created));
							this.request_Album_getPhoto(timeline_target_srl);
							this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
							this.request_Scrap_getScrapCount(timeline_target_srl, "P");
							break;
						case 'M':
							isMentoryAlreadyIn = false;
							for(int j = 0; j < newNewsList.size(); j++) {
								if( newNewsList.get(j).identifier != null && newNewsList.get(j).identifier.equals(makeIdentifier("D"+timeline_type.charAt(1), timeline_target_srl.split("/")[1]))) {
									isMentoryAlreadyIn = true;
									break;
								}
							}
							if(isMentoryAlreadyIn) {
								notifyDataSetChanged();
							}
							else {
								RecommendedMentoryNews recommendedMentoryNews = new RecommendedMentoryNews(makeIdentifier("D"+timeline_type.charAt(1), timeline_target_srl.split("/")[1]), timeline_srl, timeline_member_srl, timeline_like, timeline_created);
								recommendedMentoryNews.setTimeline_type(timeline_type);
								newNewsList.add(recommendedMentoryNews);
								this.request_Mentor_getMentoringArticle(timeline_target_srl.split("/")[1]);
							}
							break;
						}
						break;
					case 'X':
						notifyDataSetChanged();
						break;
					}
				}
				switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
				case 'P':
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 10000, "P");
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 10000, "M");
					break;
				case 'T':
				case 'M':
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "P");
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "M");
					break;
				}
			}
			else if( uri.equals("Calender/getCalender")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String cal_srl = jsonObj.getString("cal_srl");
				String cal_org_srl = jsonObj.getString("cal_org_srl");
				String cal_class_srl = jsonObj.getString("cal_class_srl");
				String cal_member_srl = jsonObj.getString("cal_member_srl");
				String cal_type = jsonObj.getString("cal_type");
				String cal_year = jsonObj.getString("cal_year");
				String cal_month = jsonObj.getString("cal_month");
				String cal_day = jsonObj.getString("cal_day");
				String cal_time = jsonObj.getString("cal_time");
				String cal_timestamp = jsonObj.getString("cal_timestamp");
				String cal_name = jsonObj.getString("cal_name");
				String cal_created = jsonObj.getString("cal_created");
				for(int i = 0; i < newNewsList.size(); i++) {
					if( newNewsList.get(i).identifier != null && newNewsList.get(i).identifier.equals(makeIdentifier("S", cal_srl))) {
						((ScheduleNews)newNewsList.get(i)).setScheduleNews(cal_srl, cal_org_srl, cal_class_srl, cal_member_srl, cal_type, cal_year, cal_month, cal_day, cal_time, cal_timestamp, cal_name, cal_created); 
						break;
					}
				}
				notifyDataSetChanged();
			}
			else if( uri.equals("Album/getPhoto")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String photo_srl = jsonObj.getString("photo_srl");
				String photo_member_srl = jsonObj.getString("photo_member_srl");
				String photo_album_srl = jsonObj.getString("photo_album_srl");
				String photo_message = URLDecoder.decode(jsonObj.getString("photo_message"), HTTP.UTF_8);
				String photo_tag = jsonObj.getString("photo_tag");
				String photo_path = jsonObj.getString("photo_path");
				String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
				String photo_like = jsonObj.getString("photo_like");
				String photo_private = jsonObj.getString("photo_private");
				String photo_created = jsonObj.getString("photo_created");
				String photo_updated = jsonObj.getString("photo_updated");
				//newsList.add(new PhotoNews(photo_srl, photo_member_srl, photo_album_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
				//newsMap.put(Integer.parseInt(photo_created), new PhotoNews(photo_srl, photo_member_srl, photo_album_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
				for(int i = 0; i < newNewsList.size(); i++) {
					if( newNewsList.get(i).identifier != null && newNewsList.get(i).identifier.equals(makeIdentifier("P", photo_srl))) {
						((PhotoNews)newNewsList.get(i)).setPhotoNews(photo_srl, photo_member_srl, photo_album_srl, photo_message, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated); 
						this.request_Member_getMember(photo_member_srl);
						break;
					}
				}
				
			}
			if(uri.equals("Mentor/getMentoringArticle")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String mentoring_srl = jsonObj.getString("mentoring_srl");
				String mentoring_category_srl = jsonObj.getString("mentoring_category_srl");
				String mentoring_type = jsonObj.getString("mentoring_type");
				String mentoring_subject = jsonObj.getString("mentoring_subject");
				String mentoring_text = jsonObj.getString("mentoring_text");
				String mentoring_created = jsonObj.getString("mentoring_created");
				String mentoring_updated = jsonObj.getString("mentoring_updated");
				String mentoring_mentor_srl = jsonObj.getString("mentoring_mentor_srl");
				String mentoring_like = jsonObj.getString("mentoring_like");
				String mentoring_share = jsonObj.getString("mentoring_share");
				
				for(int i = 0; i < newNewsList.size(); i++) {
					if( newNewsList.get(i).identifier != null ) {
						if( newNewsList.get(i).identifier.equals(makeIdentifier("M", mentoring_srl))) {
							((MentoryNews)newNewsList.get(i)).setMentoryNews(mentoring_srl, mentoring_category_srl, mentoring_type, mentoring_subject, 
									mentoring_text, mentoring_created, mentoring_updated, mentoring_mentor_srl, mentoring_like, mentoring_share); 
	
							NewsfeedFragment.this.request_Mentor_getComments(mentoring_srl, 1, 100000);
							NewsfeedFragment.this.request_Scrap_getScrapCount(mentoring_srl, "M");
							notifyDataSetChanged();
							break;
						}
						else if( newNewsList.get(i).identifier.equals(makeIdentifier("DM", mentoring_srl))) {
							((MentoryNews)newNewsList.get(i)).setMentoryNews(mentoring_srl, mentoring_category_srl, mentoring_type, mentoring_subject, 
									mentoring_text, mentoring_created, mentoring_updated, mentoring_mentor_srl, mentoring_like, mentoring_share); 
	
							NewsfeedFragment.this.request_Mentor_getComments(mentoring_srl, 1, 100000);
							NewsfeedFragment.this.request_Scrap_getScrapCount(mentoring_srl, "M");
							NewsfeedFragment.this.request_Member_getMember(newNewsList.get(i).timeline_member_srl);
							break;
						}
					}
				}
				//notifyDataSetChanged();
			}
			else if(uri.equals("Shuttlebus/getShuttlebus")) {
				String nativeData = jsonObj.getString("data");
				JSONObject json = new JSONObject(nativeData);

				String shuttle_srl = json.getString("shuttle_srl");
				String shuttle_org_srl = json.getString("shuttle_org_srl");
				String shuttle_name = json.getString("shuttle_name");
				String shuttle_route = json.getString("shuttle_route");
				String shuttle_location = json.getString("sutttle_location");
				
				for(int i = 0; i < newNewsList.size(); i++) {
					if( newNewsList.get(i).identifier != null && newNewsList.get(i).identifier.equals(makeIdentifier("B", shuttle_srl))) {
						((BusinfoNews)newNewsList.get(i)).setBusinfoNews(shuttle_srl, shuttle_org_srl, shuttle_name, shuttle_route, 
								shuttle_location); 
					}
				}
				notifyDataSetChanged();
			}
			else if( uri.equals("Member/getMember")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String member_srl = jsonObj.getString("member_srl");
				String member_name = jsonObj.getString("member_name");
				String member_type = jsonObj.getString("member_type");
				String member_picture = jsonObj.getString("member_picture");
				
//				for(int i = 0; i < newsList.size(); i++) {
//					if(newsList.get(i).type == NEWSTYPE.PHOTO) {
//						PhotoNews photoNews = (PhotoNews) newsList.get(i);
//						if(photoNews.photo_member_srl.equals(member_srl));
//							photoNews.photo_member_name = member_name;
//					}
//				}
				if(Counter > 0) {
					for(int i = 0; i < newNewsList.size(); i++) {
						if(newNewsList.get(i).type == NEWSTYPE.PHOTO) {
							PhotoNews photoNews = (PhotoNews) newNewsList.get(i);
							if(photoNews.photo_member_srl.equals(member_srl)) {
								photoNews.photo_member_name = member_name;
								if( member_type.charAt(0) == 'S' ) {
									photoNews.photo_member_name += " 학부모";
									JSONObject studentObj = jsonObj.getJSONObject("student");
									String student_parent_srl = studentObj.getString("student_parent_srl");
									requestGetParentForProfileImageCounter++;
									photoNews.photo_member_picture_srl = "P"+student_parent_srl;
									this.request_Member_getParent(student_parent_srl);
								}
								else
									photoNews.photo_member_picture_srl = member_picture;
							}
						}
						else if(newNewsList.get(i).type == NEWSTYPE.RECOMMENDED_MENTORY) {
							RecommendedMentoryNews recommendedMentoryNews = (RecommendedMentoryNews) newNewsList.get(i);
							if(recommendedMentoryNews.timeline_member_srl.equals(member_srl)) {
								if( member_type.charAt(0) == 'T' ) {
									recommendedMentoryNews.setTimeline_member_name(member_name+" 선생님");
								}
								else if( member_type.charAt(0) == 'M' ) {
									recommendedMentoryNews.setTimeline_member_name(member_name+" 원장 선생님");
								}
								//notifyDataSetChanged();
							}
						}
					}
					notifyDataSetChanged();
				}
				else {
					switch(member_type.charAt(0)) {
					case 'S':
						JSONObject studentObj = jsonObj.getJSONObject("student");
						String student_parent_srl = studentObj.getString("student_parent_srl");
						this.request_Member_getParent(student_parent_srl);
						break;
					case 'T':
					case 'M':
						switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
						case 'P':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 학부모님이 사진을 좋아합니다.", "P");
							break;
						case 'T':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 선생님이 사진을 좋아합니다.", "P");
							break;
						case 'M':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 원장선생님이 사진을 좋아합니다.", "P");
							break;
						}
						break;
					}
				}
			}
			else if( uri.equals("Member/getParent") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String member_srl = jsonObj.getString("member_srl");
				final String member_picture = jsonObj.getString("member_picture");
				JSONObject parentObj = jsonObj.getJSONObject("parent");
				String parent_srl = parentObj.getString("parent_srl");
				if( requestGetParentForProfileImageCounter > 0 ) {
					for(int i = 0; i < newNewsList.size(); i++) {
						if( newNewsList.get(i).type == NEWSTYPE.PHOTO ) {
							final PhotoNews photoNews = (PhotoNews) newNewsList.get(i);
							if( photoNews.photo_member_picture_srl != null && photoNews.photo_member_picture_srl.equals("P"+parent_srl) ) {
								new Thread(new Runnable() {
								    @Override
								    public void run() {    
								        NewsfeedFragment.this.getActivity().runOnUiThread(new Runnable(){
								            @Override
								             public void run() {
												photoNews.photo_member_picture_srl = member_picture;
												requestGetParentForProfileImageCounter--;
												newsAdapter.notifyDataSetChanged();
								            }
								        });
								    }
								}).start();
							}
						}
					}
				}
				else {
					switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
					case 'P':
						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 학부모님이 사진을 좋아합니다.", "P");
						break;
					case 'T':
						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 선생님이 사진을 좋아합니다.", "P");
						break;
					case 'M':
						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 좋아요 알림", SlidingMenuMaker.getProfile().member_name+" 원장선생님이 사진을 좋아합니다.", "P");
						break;
					}
				}
			}
			else if( uri.equals("Album/setPhotoLike") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String photo_srl = jsonObj.getString("photo_srl");
				//String photo_like = jsonObj.getString("photo_like");
				for(int i = 0; i < newsList.size(); i++) {
					if(newsList.get(i).type == NEWSTYPE.PHOTO ) {
						PhotoNews photo = (PhotoNews)newsList.get(i);
						if( photo.photo_srl.equals(photo_srl)) {
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
							photo.likeMemberList.add(member_srl);
							refreshList();
							
							this.request_Member_getMember(photo.photo_member_srl);
							break;
						}
					}
				}
			}
			else if( uri.equals("Album/delPhotoLike") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String photo_srl = jsonObj.getString("photo_srl");
				for(int i = 0; i < newsList.size(); i++) {
					if(newsList.get(i).type == NEWSTYPE.PHOTO ) {
						PhotoNews photo = (PhotoNews)newsList.get(i);
						if( photo.photo_srl.equals(photo_srl)) {
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
							photo.likeMemberList.remove(member_srl);
							refreshList();
							break;
						}
					}
				}
			}
			else if( uri.equals("Timeline/getTimelineComments") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				String timeline_srl = "";
				ArrayList<Reply> replyList = new ArrayList<Reply>();
				for(int i = 0; i < dataArray.length(); i++ ) {
					JSONObject dataObj = dataArray.getJSONObject(i);
					String tcomment_srl = dataObj.getJSONObject("_id").getString("$oid");
					String tcomment_member_srl = dataObj.getString("tcomment_member_srl");
					String tcomment_timeline_srl = dataObj.getString("tcomment_timeline_srl");
					String tcomment_message = dataObj.getString("tcomment_message");
					String tcomment_created = dataObj.getString("tcomment_created");
					
					timeline_srl = tcomment_timeline_srl;
					replyList.add(new Reply(tcomment_srl, tcomment_member_srl, tcomment_timeline_srl, tcomment_message, tcomment_created));
				}
				if( timeline_srl.isEmpty() == false ) {
					if( Counter > 0 ) {
						for(int i = 0; i < newNewsList.size(); i++) {
							if(newNewsList.get(i).timeline_srl.equals(timeline_srl)) {
								newNewsList.get(i).commentList.addAll(replyList);
								refreshList();
							}
						}
					}
					else {
						for(int j = 0; j < newsList.size(); j++) {
							if( newsList.get(j).timeline_srl.equals(timeline_srl) ) {
								newsList.get(j).commentList.addAll(replyList);
								refreshList();
								break;
							}
						}
					}
				}
			}
			else if(uri.equals("Scrap/getScraps")) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				for(int i = 0; i < dataArray.length(); i++) {
					String scrap_srl = dataArray.getJSONObject(i).getString("scrap_srl");
					final String scrap_member_srl = dataArray.getJSONObject(i).getString("scrap_member_srl");
					String scrap_type = dataArray.getJSONObject(i).getString("scrap_type");
					String scrap_target_srl = dataArray.getJSONObject(i).getString("scrap_target_srl");
					String scrap_created = dataArray.getJSONObject(i).getString("scrap_created");
					if( Counter > 0 ) {
						for(int j = 0; j < newNewsList.size(); j++) {
							if( newNewsList.get(j).identifier.equals(this.makeIdentifier(scrap_type, scrap_target_srl)) ) {
								newNewsList.get(j).member_scrap_srl = scrap_srl;
								refreshList();
								break;
							}
						}
					}
					else {
						for(int j = 0; j < newsList.size(); j++) {
							if( newsList.get(j).identifier.equals(this.makeIdentifier(scrap_type, scrap_target_srl)) ) {
								newsList.get(j).member_scrap_srl = scrap_srl;
								refreshList();
								break;
							}
						}
					}
				}
			}
			else if( uri.equals("Scrap/getScrapCount") ) {
				String type = jsonObj.getString("type");
				String target_srl = jsonObj.getString("target_srl");
				final String count = jsonObj.getString("count");
				if( Counter > 0 ) { 
					for(int i = 0; i < newNewsList.size(); i++) {
						if( newNewsList.get(i).identifier.equals(this.makeIdentifier(type, target_srl)) ) {
							newNewsList.get(i).scrapCount = Integer.parseInt(count);
							refreshList();
							break;
						}
					}
					for(int i = 0; i < newsList.size(); i++) {
						if( newsList.get(i).identifier.equals(this.makeIdentifier(type, target_srl)) ) {
							newsList.get(i).scrapCount = Integer.parseInt(count);
							refreshList();
							break;
						}
					}
				}
				else {
					for(int i = 0; i < newsList.size(); i++) {
						if( newsList.get(i).identifier.equals(this.makeIdentifier(type, target_srl)) ) {
							newsList.get(i).scrapCount = Integer.parseInt(count);
							refreshList();
							break;
						}
					}
					for(int i = 0; i < newNewsList.size(); i++) {
						if( newNewsList.get(i).identifier.equals(this.makeIdentifier(type, target_srl)) ) {
							newNewsList.get(i).scrapCount = Integer.parseInt(count);
							refreshList();
							break;
						}
					}
				}
			}
			else if( uri.equals("Scrap/setScrap") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String scrap_srl = jsonObj.getString("scrap_srl");
				String scrap_member_srl = jsonObj.getString("scrap_member_srl");
				String scrap_type = jsonObj.getString("scrap_type");
				String scrap_target_srl = jsonObj.getString("scrap_target_srl");
				String scrap_created = jsonObj.getString("scrap_created");
				for(int i = 0; i < newsList.size(); i++) {
					if( newsList.get(i).identifier.equals(this.makeIdentifier(scrap_type, scrap_target_srl)) ) {
						newsList.get(i).member_scrap_srl = scrap_srl;
						newsList.get(i).scrapCount++;
						refreshList();
						break;
					}
				}
			}
			else if( uri.equals("Scrap/delScrap") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String scrap_srl = jsonObj.getString("scrap_srl");
				String scrap_member_srl = jsonObj.getString("scrap_member_srl");
				String scrap_type = jsonObj.getString("scrap_type");
				String scrap_target_srl = jsonObj.getString("scrap_target_srl");
				String scrap_created = jsonObj.getString("scrap_created");
				for(int i = 0; i < newsList.size(); i++) {
					if( newsList.get(i).identifier.equals(this.makeIdentifier(scrap_type, scrap_target_srl)) ) {
						newsList.get(i).member_scrap_srl = "";
						newsList.get(i).scrapCount--;
						refreshList();
						break;
					}
				}
			}
			else if( uri.equals("Mentor/getComments") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				ArrayList<Reply> replyList = new ArrayList<Reply>();
				String mentoring_srl = "";
				for(int i = 0; i < dataArray.length(); i++ ) {
					JSONObject dataObj = dataArray.getJSONObject(i);
					String comment_srl = dataObj.getString("comment_srl");
					String comment_mentoring_srl = dataObj.getString("comment_mentoring_srl");
					String comment_member_srl = dataObj.getString("comment_member_srl");
					String comment_text = dataObj.getString("comment_text");
					String comment_created = dataObj.getString("comment_created");
					String comment_updated = dataObj.getString("comment_updated");
					replyList.add(new Reply(comment_srl, comment_member_srl, comment_mentoring_srl, comment_text, comment_created));
					mentoring_srl = comment_mentoring_srl;
				}
				if( Counter > 0 ) { 
					for(int i = 0; i < newNewsList.size(); i++) {
						if( newNewsList.get(i).identifier.equals(this.makeIdentifier("M", mentoring_srl)) ) {
							newNewsList.get(i).commentList.addAll(replyList);
							refreshList();
							break;
						}
					}
					for(int i = 0; i < newsList.size(); i++) {
						if( newsList.get(i).identifier.equals(this.makeIdentifier("M", mentoring_srl)) ) {
							newsList.get(i).commentList.addAll(replyList);
							refreshList();
							break;
						}
					}
				}
				else {
					for(int i = 0; i < newsList.size(); i++) {
						if( newsList.get(i).identifier.equals(this.makeIdentifier("M", mentoring_srl)) ) {
							newsList.get(i).commentList.addAll(replyList);
							refreshList();
							break;
						}
					}
					for(int i = 0; i < newNewsList.size(); i++) {
						if( newNewsList.get(i).identifier.equals(this.makeIdentifier("M", mentoring_srl)) ) {
							newNewsList.get(i).commentList.addAll(replyList);
							refreshList();
							break;
						}
					}
				}
			}
			else if(uri.equals("Mentor/setMentoringArticleLikes")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String mentoring_srl = jsonObj.getString("mentoring_srl");
				for(int i = 0; i < newsList.size(); i++) {
					if( newsList.get(i).type == NEWSTYPE.MENTORY ) {
						MentoryNews mentoryNews = (MentoryNews)newsList.get(i);
						if( mentoryNews.getMentoring_srl().equals(mentoring_srl)) {
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
							newsList.get(i).likeMemberList.add(member_srl);
							refreshList();
							break;
						}
					}
				}
			}
			else if(uri.equals("Mentor/delMentoringArticleLikes") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String mentoring_srl = jsonObj.getString("mentoring_srl");
				for(int i = 0; i < newsList.size(); i++) {
					if( newsList.get(i).type == NEWSTYPE.MENTORY ) {
						MentoryNews mentoryNews = (MentoryNews)newsList.get(i);
						if( mentoryNews.getMentoring_srl().equals(mentoring_srl)) {
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
							newsList.get(i).likeMemberList.remove(member_srl);
							refreshList();
							break;
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
