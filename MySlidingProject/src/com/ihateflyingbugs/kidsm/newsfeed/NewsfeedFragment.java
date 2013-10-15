package com.ihateflyingbugs.kidsm.newsfeed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;
import com.ihateflyingbugs.kidsm.showimage.ShowImageActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

public class NewsfeedFragment extends NetworkFragment {
	LayoutInflater inflater;
	
	
	View layout;
	ArrayList<News> newsList;
	NewsAdapter newsAdapter;
	ListView newsListView;
	
	Map<Integer, News> newsMap;
	
	int timelineIndex;
	int Counter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null) {
			refreshTimeline();
			return layout;
		}
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_newsfeed, container, false);
		
		auth_key = MainActivity.auth_key;
		
		newsMap = new HashMap<Integer, News>();
		//newsList = new HashMap<String, News>();
		newsList = new ArrayList<News>();
		newsAdapter = new NewsAdapter(getActivity(), newsList);
		timelineIndex = 0;
		getTimeLineMessages();
        
		newsListView = (ListView)layout.findViewById(R.id.newsfeed);
		newsListView.setOnTouchListener( new OnTouchListener() {

			Rect m_rect;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				checkIsLocatedAtFooter();
				return false;
			}
			
			public void checkIsLocatedAtFooter()
			{
				if( m_rect == null )     /// 처음에는 Rect가 없을테니....
				{
					m_rect = new Rect( ) ;    /// new합니다.
					newsListView.getLocalVisibleRect( m_rect ) ;  /// 스크롤 영역 구합니다.(저는 0,480,0,696 이던가 했네요)
					//return ;       /// 그리고 걍 리턴합니다.
				}
				int oldBottom = m_rect.bottom;   /// 이전 bottom저장 이유는 맨아래인 상태에서 아래로 스크롤 했을떄 쌩까려구
				newsListView.getLocalVisibleRect( m_rect ) ;   /// 현재 스크롤뷰의 영역을 구합니다.
				/// 이때 스크롤 이동시켰으면 top와 bottom값이 이동한 만큼 변합니다.
				int height = newsListView.getMeasuredHeight( ) ;  /// 스크롤 뷰의 높이를 구합니다.
				View v = newsListView.getChildAt( 0 ) ;    /// 스크롤 뷰 안에 들어있는 내용물의 높이도 구합니다.
				if (oldBottom > 0 && height > 0)   /// 스크롤 뷰나 이전 bottom이 0 이상이어야만 처리
				{
					/// bottom값의 변화가 없으면 처리 안해요
					if (oldBottom != m_rect.bottom && m_rect.bottom == v.getMeasuredHeight( ) )
					{
						//addNewsFeed();
						getTimeLineMessages();
					}
				}
			}
			
		});
		newsListView.setDivider(null);
		newsListView.setDividerHeight(20);
		newsListView.setAdapter(newsAdapter);
		
		return layout;
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
		timelineIndex = 0;
		Counter = 0;
		getTimeLineMessages();
	}
	
	private void getTimeLineMessages() {
		newsMap.clear();
		newsMap.put(0, new BusinfoNews());
		newsMap.put(1, new MentoryNews());
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
//		final String key = (String)v.getTag();
//		final News news = newsList.get(key);
//		if(news.isLikeAlreadyDone()) {
//			news.setLikeAlreadyDone(false);
//			for(int i = 0; i < news.getLikeList().size(); i++) {
//				if( news.getLikeList().get(i).getName().equals(SlidingMenuMaker.getProfile().getMember_name()) )
//					news.deleteLikeMember(i);
//			}
//		}
//		else {
//			news.setLikeAlreadyDone(true);
//			news.addLikeMember(new LikeMember(SlidingMenuMaker.getProfile().getMember_name()));
//		}
//		news.updateLayout();
//		newsList.put(key, news);
	}
	
	public void OnReplyClick(View v) {
		Intent intent = new Intent(getActivity(), NewsfeedReplyActivity.class);
		intent.putExtra("key", (String)v.getTag());
		startActivity(intent);
	}
	
	public void OnScrapClick(View v) {
//		final String key = (String)v.getTag();
//		final News news = newsList.get(key);
//		//Button button = (Button)v;
//		if(news.isScrapAlreadyDone()) {
//			news.setScrapAlreadyDone(false);
//			news.setNumOfScrap(news.getNumOfScrap() - 1);
//			//button.setText(getResources().getString(R.string.news_scrap));
//		}
//		else {
//			Toast.makeText(getActivity(), getResources().getString(R.string.news_donescrap), Toast.LENGTH_SHORT).show();
//			news.setScrapAlreadyDone(true);
//			news.setNumOfScrap(news.getNumOfScrap() + 1);
//			//button.setText(getResources().getString(R.string.news_donescrap));
//		}
//		news.updateLayout();
//		newsList.put(key, news);
	}
	
	public void OnImageClick(View v) {
		Intent intent = new Intent(getActivity(), ShowImageActivity.class);
		intent.putExtra("url", v.getTag().toString());
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
			            	Set<Integer> keySet = newsMap.keySet();
							Iterator<Integer> iterator = keySet.iterator();
							while(iterator.hasNext()) {
								Integer key = iterator.next();
//								Date date = null;
//								date.setTime(Long.parseLong(key));
//								Calendar c = Calendar.getInstance();
//								c.setTime(date);
								newsList.add(newsMap.get(key));
							}
							
			            	newsAdapter.notifyDataSetChanged();
			            	
			            }
			        });
			    }
			}).start();
		}
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") == false )
				return;

			if( uri.equals("Timeline/getTimelineMessages") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				Counter = dataArray.length();
				for(int i = 0; i < dataArray.length(); i++ ) {
					JSONObject dataObj = dataArray.getJSONObject(i);
					final String timeline_type = dataObj.getString("timeline_type");
					final String timeline_srl = dataObj.getJSONObject("_id").getString("$oid");
					final String timeline_member_srl = dataObj.getString("timeline_member_srl");
					final String timeline_target_member_srl = dataObj.getString("timeline_target_member_srl");
					final String timeline_message = dataObj.getString("timeline_message");
					final String timeline_created = dataObj.getString("timeline_created");
					switch(timeline_type.charAt(0)) {
					case 'S':
						this.request_Calender_getCalender(timeline_message);
						break;
					case 'P':
						this.request_Album_getPhoto(timeline_message);
						break;
					case 'B':
						break;
					case 'M':
						break;
					}
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
				//newsList.add(new ScheduleNews(cal_srl, cal_org_srl, cal_class_srl, cal_member_srl, cal_type, cal_year, cal_month, cal_day, cal_time, cal_timestamp, cal_name, cal_created));
				newsMap.put(Integer.parseInt(cal_created), new ScheduleNews(cal_srl, cal_org_srl, cal_class_srl, cal_member_srl, cal_type, cal_year, cal_month, cal_day, cal_time, cal_timestamp, cal_name, cal_created));
				notifyDataSetChanged();
			}
			else if( uri.equals("Album/getPhoto")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String photo_srl = jsonObj.getString("photo_srl");
				String photo_member_srl = jsonObj.getString("photo_member_srl");
				String photo_album_srl = jsonObj.getString("photo_album_srl");
				String photo_tag = jsonObj.getString("photo_tag");
				String photo_path = jsonObj.getString("photo_path");
				String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
				String photo_like = jsonObj.getString("photo_like");
				String photo_private = jsonObj.getString("photo_private");
				String photo_created = jsonObj.getString("photo_created");
				String photo_updated = jsonObj.getString("photo_updated");
				//newsList.add(new PhotoNews(photo_srl, photo_member_srl, photo_album_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
				newsMap.put(Integer.parseInt(photo_created), new PhotoNews(photo_srl, photo_member_srl, photo_album_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
				this.request_Member_getMember(photo_member_srl);
			}
			else if( uri.equals("Member/getMember")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String member_srl = jsonObj.getString("member_srl");
				String member_name = jsonObj.getString("member_name");
//				for(int i = 0; i < newsList.size(); i++) {
//					if(newsList.get(i).type == NEWSTYPE.PHOTO) {
//						PhotoNews photoNews = (PhotoNews) newsList.get(i);
//						if(photoNews.photo_member_srl.equals(member_srl));
//							photoNews.photo_member_name = member_name;
//					}
//				}
				Set<Integer> keySet = newsMap.keySet();
				Iterator<Integer> iterator = keySet.iterator();
				while(iterator.hasNext()) {
					Integer key = iterator.next();
					if(newsMap.get(key).type == NEWSTYPE.PHOTO) {
						PhotoNews photoNews = (PhotoNews) newsMap.get(key);
						if(photoNews.photo_member_srl.equals(member_srl))
							photoNews.photo_member_name = member_name;
					}
				}
				
				notifyDataSetChanged();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
