package com.ihateflyingbugs.kidsm.mentory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.PhotoNews;
import com.ihateflyingbugs.kidsm.newsfeed.Reply;
import com.ihateflyingbugs.kidsm.newsfeed.ReplyActivity;
import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;

public class MentoryFragment extends NetworkFragment {
	public enum MentoryType {
		total,
		edu,
		infant,
		manage,
		healing,
		kidsmnews,
		scrap,
		recommend
	}
	LayoutInflater inflater;
	View layout;
	ViewFlipper viewFlipper;
	MentoryType mentoryType;
	ArrayList<MentoryArticle> articleList;
	ArrayList<MentoryCategory> categoryList;
	MentoryAdapter articleListAdapter;
	String temp;
	boolean isOrganizingScrapPage;
	int organizingScrapPageCounter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;

		if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' ) { 
			layout = inflater.inflate(R.layout.activity_mentory_parent, container, false);
		}
		else {
			//layout = inflater.inflate(R.layout.activity_mentory_parent, container, false);
			layout = inflater.inflate(R.layout.activity_mentory_notparent, container, false);
		}

		mentoryType = MentoryType.total;
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.mentory_page);

		categoryList = new ArrayList<MentoryCategory>();

		// 리스트 뷰 처리
		articleList = new ArrayList<MentoryArticle>(); // 멘토리 리스트 뷰와 연결된, 멘토리 리스트 목록

		articleListAdapter = new MentoryAdapter(getActivity(), R.layout.mentory_row, articleList);

		ListView listView = (ListView)layout.findViewById(R.id.mentory_article_listview);
		listView.setAdapter(articleListAdapter);
		listView = (ListView)layout.findViewById(R.id.mentory_edu_article_listview);
		listView.setAdapter(articleListAdapter);
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			listView = (ListView)layout.findViewById(R.id.mentory_infant_article_listview);
			listView.setAdapter(articleListAdapter);
			listView = (ListView)layout.findViewById(R.id.mentory_recommend_article_listview);
			listView.setAdapter(articleListAdapter);
			break;
		case 'T':
		case 'M':
			listView = (ListView)layout.findViewById(R.id.mentory_manage_article_listview);
			listView.setAdapter(articleListAdapter);
			break;
		}
		listView = (ListView)layout.findViewById(R.id.mentory_healing_article_listview);
		listView.setAdapter(articleListAdapter);
		listView = (ListView)layout.findViewById(R.id.mentory_kidsmnews_article_listview);
		listView.setAdapter(articleListAdapter);
		listView = (ListView)layout.findViewById(R.id.mentory_scrap_article_listview);
		listView.setAdapter(articleListAdapter);
		
		// 전체 목록 얻어오기 !!!! 임시 코드 !!!!
		requestMentoryInfo();

		RadioGroup rg = (RadioGroup) layout.findViewById(R.id.mentory_mode);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
				case R.id.mentory_total:	
					requestMentoryInfo();
					
					switch(mentoryType) {
					case total:
						break;
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(0);
						break;
					}
					mentoryType = MentoryType.total;
					break;

				case R.id.mentory_edu:
					articleList.clear();
					MentoryFragment.this.request_Mentor_getMentoringArticles("100", 1, 50, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);
					MentoryFragment.this.request_Mentor_getMentoringArticles("110", 1, 50, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);
					MentoryFragment.this.request_Mentor_getMentoringArticles("111", 1, 50, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);

					switch(mentoryType) {
					case total:
						setNextAnimation();
						viewFlipper.setDisplayedChild(1);
						break;
					case edu:
						break;
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(1);
						break;
					}
					mentoryType = MentoryType.edu;
					break;

				case R.id.mentory_manage:
					articleList.clear();
					MentoryFragment.this.request_Mentor_getMentoringArticles("300", 1, 100, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);

					switch(mentoryType) {
					case total:
					case edu:
						setNextAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					case manage:
					case infant:
						break;
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					}
					if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' ) 
						mentoryType = MentoryType.infant;
					else
						mentoryType = MentoryType.manage;
					break;

				case R.id.mentory_infant:
					articleList.clear();
					MentoryFragment.this.request_Mentor_getMentoringArticles("200", 1, 100, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);

					switch(mentoryType) {
					case total:
					case edu:
						setNextAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					case manage:
					case infant:
						break;
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					}
					if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' ) 
						mentoryType = MentoryType.infant;
					else
						mentoryType = MentoryType.manage;
					break;

				case R.id.mentory_healing:
					articleList.clear();
					MentoryFragment.this.request_Mentor_getMentoringArticles("400", 1, 100, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);

					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
						setNextAnimation();
						viewFlipper.setDisplayedChild(3);
						break;
					case healing:
						break;
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(3);
						break;
					}
					mentoryType = MentoryType.healing;
					break;

				case R.id.mentory_kidsmnews:
					articleList.clear();
					MentoryFragment.this.request_Mentor_getMentoringArticles("500", 1, 100, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);

					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
						setNextAnimation();
						viewFlipper.setDisplayedChild(4);
						break;
					case kidsmnews:
						break;
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(4);
						break;
					}
					mentoryType = MentoryType.kidsmnews;
					break;
				case R.id.mentory_scrap:
					articleList.clear();
					isOrganizingScrapPage = true;
					MentoryFragment.this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "M");

					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
						setNextAnimation();
						viewFlipper.setDisplayedChild(5);
						break;
					case scrap:
						break;
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(5);
						break;
					}
					mentoryType = MentoryType.scrap;
					break;
				case R.id.mentory_recommend:
					articleList.clear();
					request_Mentor_getMentoringRecommendArticles(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 100);
					
					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
						setNextAnimation();
						viewFlipper.setDisplayedChild(6);
						break;
					case recommend:
						break;
					}
					mentoryType = MentoryType.recommend;
					break;
				}
			}

		});
		return layout;
	}

	@Override
	public void onDetach() {
		viewFlipper.stopFlipping();
		this.unregisterForContextMenu(viewFlipper);
		super.onDetach();
	}
	
	private void requestMentoryInfo() {
		categoryList.clear();
		articleList.clear();
		isOrganizingScrapPage = false;
		MentoryFragment.this.request_Mentor_getMentoringCategory(SlidingMenuMaker.getProfile().member_type);
		MentoryFragment.this.request_Mentor_getMentoringArticles("0", 1, 200, SlidingMenuMaker.getProfile().member_type, SlidingMenuMaker.getProfile().member_srl);
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
		if( articleList.get(position).getMentoring_likeList().contains(member_srl) == false )
			this.request_Mentor_setMentoringArticleLikes(articleList.get(position).getMentoring_srl(), member_srl);
		else
			this.request_Mentor_delMentoringArticleLikes(articleList.get(position).getMentoring_srl(), member_srl);
	}
	
	public void OnReplyClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		Intent intent = new Intent(getActivity(), ReplyActivity.class);
		intent.putExtra("type", "M");
		intent.putExtra("mentoring_srl", articleList.get(position).getMentoring_srl());
		//intent.putExtra("mentoring_member_srl", articleList.get(position).getMentoring_mentor_srl())
		startActivity(intent);
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
		
		if( articleList.get(position).getMember_scrap_srl().isEmpty() )
			this.request_Scrap_setScrap(member_srl, "M", articleList.get(position).getMentoring_srl());
		else
			this.request_Scrap_delScrap(member_srl, articleList.get(position).getMember_scrap_srl(), articleList.get(position).getMentoring_srl());
	}
	
	public void OnSeeMentory(View v) {
//		Intent intent = new Intent(getActivity(), SeeMentoryActivity.class);
//		if(v.getTag().toString().equals("mentory_edu_notparent")) {
//			intent.putExtra("url", "file:///android_asset/10_edu_teacher.html");
//		}
//		else if(v.getTag().toString().equals("mentory_edu")) {
//			intent.putExtra("url", "file:///android_asset/10_edu.html");
//		}
//		else if(v.getTag().toString().equals("mentory_healing")) {
//			intent.putExtra("url", "file:///android_asset/10_healing.html");
//		}
//		else if(v.getTag().toString().equals("mentory_infant")) {
//			intent.putExtra("url", "file:///android_asset/10_infant_parent.html");
//		}
//		else if(v.getTag().toString().equals("mentory_manage_manager")) {
//			intent.putExtra("url", "file:///android_asset/10_manage_manager.html");
//		}
//		else if(v.getTag().toString().equals("mentory_manage_teacher")) {
//			intent.putExtra("url", "file:///android_asset/10_manage_teacher.html");
//		}
//		startActivity(intent);
		
		int position = Integer.parseInt(v.getTag().toString());
		Intent intent = new Intent(getActivity(), SeeMentoryActivity.class);
		//intent.putExtra("mentoring_srl", list.get(pos).getMentoring_srl());
		intent.putExtra("mentoring_text", articleList.get(position).getMentoring_text());
		intent.putExtra("mentoring_subject", articleList.get(position).getMentoring_subject());
		intent.putExtra("mentoring_srl", articleList.get(position).getMentoring_srl());
		//Toast.makeText(ctx, list.get(pos).getMentoring_text(), Toast.LENGTH_SHORT).show();
		startActivity(intent);
	}

	private void setPrevAnimation() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
	}

	private void setNextAnimation() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
	}

	private void goPrev() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
		viewFlipper.showPrevious();
	}

	private void goNext() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
		viewFlipper.showNext();
	}

	private void notifyDataSetChanged() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        getActivity().runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	articleListAdapter.notifyDataSetChanged();
		            }
		        });
		    }
		}).start();	
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() ) {
				Log.d("BusInfo", "return empty");
				return;
			}

			Log.d("MentoryFragment", response);
			Log.d("MentoryFragment", uri);

			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				Log.d("MentoryFragment", uri);
				if(uri.equals("Mentor/getMentoringArticles")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);

					for(int i = 0; i < dataArray.length(); i++) {
						String mentoring_srl = dataArray.getJSONObject(i).getString("mentoring_srl");
						String mentoring_category_srl = dataArray.getJSONObject(i).getString("mentoring_category_srl");
						String mentoring_type = dataArray.getJSONObject(i).getString("mentoring_type");
						String mentoring_subject = dataArray.getJSONObject(i).getString("mentoring_subject");
						String mentoring_text = dataArray.getJSONObject(i).getString("mentoring_text");
						String mentoring_created = dataArray.getJSONObject(i).getString("mentoring_created");
						String mentoring_updated = dataArray.getJSONObject(i).getString("mentoring_updated");
						String mentoring_mentor_srl = dataArray.getJSONObject(i).getString("mentoring_mentor_srl");
						String mentoring_like = dataArray.getJSONObject(i).getString("mentoring_like");
						String mentoring_share = dataArray.getJSONObject(i).getString("mentoring_share");
						articleList.add(new MentoryArticle(mentoring_srl, mentoring_category_srl, mentoring_type, mentoring_subject, 
								mentoring_text, mentoring_created, mentoring_updated, mentoring_mentor_srl, mentoring_like, mentoring_share));
						this.request_Mentor_getComments(mentoring_srl, 1, 100000);
						this.request_Scrap_getScrapCount(mentoring_srl, "M");
					}
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "M");
					Log.d("MentoryFragment", "array size : " + String.valueOf(articleList.size()));
//					Message msg = handler.obtainMessage();
//					Bundle b = new Bundle();
//					b.putInt("eventType", 0);
//					msg.setData(b);
//					handler.sendMessage(msg);
					notifyDataSetChanged();
				}
				else if(uri.equals("Mentor/getMentoringArticle")) {
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
					articleList.add(articleList.size(), new MentoryArticle(mentoring_srl, mentoring_category_srl, mentoring_type, mentoring_subject, 
							mentoring_text, mentoring_created, mentoring_updated, mentoring_mentor_srl, mentoring_like, mentoring_share));
					this.request_Mentor_getComments(mentoring_srl, 1, 100000);
					this.request_Scrap_getScrapCount(mentoring_srl, "M");
					if(--organizingScrapPageCounter == 0) {
						isOrganizingScrapPage = false;
						this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "M");
						notifyDataSetChanged();
					}
				}
				else if(uri.equals("Mentor/getMentoringRecommendArticles")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);

					for(int i = 0; i < dataArray.length(); i++) {
						String mentoring_srl = dataArray.getJSONObject(i).getString("mentoring_srl");
						String mentoring_category_srl = dataArray.getJSONObject(i).getString("mentoring_category_srl");
						String mentoring_type = dataArray.getJSONObject(i).getString("mentoring_type");
						String mentoring_subject = dataArray.getJSONObject(i).getString("mentoring_subject");
						String mentoring_text = dataArray.getJSONObject(i).getString("mentoring_text");
						String mentoring_created = dataArray.getJSONObject(i).getString("mentoring_created");
						String mentoring_updated = dataArray.getJSONObject(i).getString("mentoring_updated");
						String mentoring_mentor_srl = dataArray.getJSONObject(i).getString("mentoring_mentor_srl");
						String mentoring_like = dataArray.getJSONObject(i).getString("mentoring_like");
						String mentoring_share = dataArray.getJSONObject(i).getString("mentoring_share");
						articleList.add(new MentoryArticle(mentoring_srl, mentoring_category_srl, mentoring_type, mentoring_subject, 
								mentoring_text, mentoring_created, mentoring_updated, mentoring_mentor_srl, mentoring_like, mentoring_share));
						this.request_Mentor_getComments(mentoring_srl, 1, 100000);
						this.request_Scrap_getScrapCount(mentoring_srl, "M");
					}
					this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "M");
				}
				else if(uri.equals("Mentor/getMentoringCategory")) {
					temp = new String(response);

					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);

					for(int i = 0; i < dataArray.length(); i++) {
						String category_srl = dataArray.getJSONObject(i).getString("category_srl");
						String category_parent_srl = dataArray.getJSONObject(i).getString("category_parent_srl");
						String category_name = dataArray.getJSONObject(i).getString("category_name");
						String category_perm = dataArray.getJSONObject(i).getString("category_perm");

						categoryList.add(new MentoryCategory(category_srl, category_parent_srl, category_name, category_perm));

						Log.d("MentoryFragment", category_name+category_srl);
					}
				}
				else if(uri.equals("Mentor/setMentoringArticleLikes")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String mentoring_srl = jsonObj.getString("mentoring_srl");
					for(int i = 0; i < articleList.size(); i++) {
						if( articleList.get(i).getMentoring_srl().equals(mentoring_srl)) {
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
							articleList.get(i).getMentoring_likeList().add(member_srl);
							notifyDataSetChanged();
							break;
						}
					}
				}
				else if(uri.equals("Mentor/delMentoringArticleLikes") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String mentoring_srl = jsonObj.getString("mentoring_srl");
					for(int i = 0; i < articleList.size(); i++) {
						if( articleList.get(i).getMentoring_srl().equals(mentoring_srl)) {
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
							articleList.get(i).getMentoring_likeList().remove(member_srl);
							notifyDataSetChanged();
							break;
						}
					}
				}
				else if(uri.equals("Scrap/getScraps")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					organizingScrapPageCounter = dataArray.length();
					for(int i = 0; i < dataArray.length(); i++) {
						String scrap_srl = dataArray.getJSONObject(i).getString("scrap_srl");
						final String scrap_member_srl = dataArray.getJSONObject(i).getString("scrap_member_srl");
						String scrap_type = dataArray.getJSONObject(i).getString("scrap_type");
						String scrap_target_srl = dataArray.getJSONObject(i).getString("scrap_target_srl");
						String scrap_created = dataArray.getJSONObject(i).getString("scrap_created");
						if( isOrganizingScrapPage ) {
							this.request_Mentor_getMentoringArticle(scrap_target_srl);
						}
						else {
							for(int j = 0; j < articleList.size(); j++) {
								if( articleList.get(j).getMentoring_srl().equals(scrap_target_srl) ) {
									articleList.get(j).setMember_scrap_srl(scrap_srl);
									notifyDataSetChanged();
								}
							}
						}
					}
				}
				else if( uri.equals("Scrap/getScrapCount") ) {
					String type = jsonObj.getString("type");
					String target_srl = jsonObj.getString("target_srl");
					final String count = jsonObj.getString("count");
					for(int i = 0; i < articleList.size(); i++) {
						if( articleList.get(i).getMentoring_srl().equals(target_srl) ) {
							articleList.get(i).setScrapCount(Integer.parseInt(count));
							notifyDataSetChanged();
							break;
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
					for(int i = 0; i < articleList.size(); i++) {
						if( articleList.get(i).getMentoring_srl().equals(scrap_target_srl) ) {
							articleList.get(i).setMember_scrap_srl(scrap_srl);
							articleList.get(i).setScrapCount(articleList.get(i).getScrapCount()+1);
							notifyDataSetChanged();
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
					for(int i = 0; i < articleList.size(); i++) {
						if( articleList.get(i).getMentoring_srl().equals(scrap_target_srl) ) {
							articleList.get(i).setMember_scrap_srl("");
							articleList.get(i).setScrapCount(articleList.get(i).getScrapCount()-1);
							notifyDataSetChanged();
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
					
					if( replyList.size() != 0 ) {
						for(int i = 0; i < articleList.size(); i++) {
							if( articleList.get(i).getMentoring_srl().equals(mentoring_srl) ) {
								articleList.get(i).getMentoring_commentList().clear();
								articleList.get(i).getMentoring_commentList().addAll(replyList);
								notifyDataSetChanged();
								break;
							}
						}
					}
				}
			}
			else {
				Log.d("BusInfo", "return Fail");
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		}
	}

//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			Bundle b = msg.getData();
//			// eventType == 0 버스 리스트 업데이트
//			// eventType == 1 노선도 업데이트
//			int eventType = (Integer) b.get("eventType");
//			if (eventType == 0) {
//				//				for(int i=0; i<busList.size(); i++) {
//				//					addButton(busList.get(i).getShuttle_name(), i);
//				//					Log.d("BusInfo", busList.get(i).getShuttle_name());
//				//				}
//				//MentoryAdapter articleListAdapter = new MentoryAdapter(getActivity(), R.layout.mentory_row, articleList);
//
//				// 리스트뷰에 어댑터 연결
//				//ListView listView = (ListView)layout.findViewById(R.id.mentory_article_listview);
//				//listView.setAdapter(articleListAdapter);
//
//				articleListAdapter.notifyDataSetChanged();
//
//				//				listView.setOnItemClickListener(new OnItemClickListener() {
//				//					public void onItemClick(AdapterView<?> parent, View view,
//				//							int position, long id) {
//				//						Intent intent = new Intent(layout.getContext(), SeeMentoryActivity.class);
//				//						intent.putExtra("mentoring_text", articleList.get(position).getMentoring_text());
//				//						Toast.makeText(layout.getContext(), articleList.get(position).getMentoring_text(), Toast.LENGTH_SHORT).show();
//				//						startActivity(intent);
//				//					}
//				//				});
//			}
//		}
//	};

//	public class MentoryAdapter extends BaseAdapter{
//
//		ArrayList<MentoryArticle> list;
//		Context ctx;
//		int itemLayout;
//
//		public MentoryAdapter(Context ctx, int itemLayout, ArrayList<MentoryArticle> list){
//			this.ctx = ctx;
//			this.itemLayout = itemLayout;
//			this.list = list;
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public MentoryArticle getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			final int pos = position;
//
//			MentoryViewHolder holder;
//
//			//if(convertView==null){
//			if(true){
//				LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(itemLayout, parent, false);
//
//				holder = new MentoryViewHolder();
//
//				holder.textView = (TextView)convertView.findViewById(R.id.tv_mentory_article_title);
//
//				holder.textView.setText(list.get(pos).getMentoring_subject());
//
//				/*
//			int color = 0;
//			if(list.get(position).equals("Red")) color = Color.RED;
//			else if(list.get(position).equals("Green")) color = Color.GREEN;
//			else if(list.get(position).equals("Blue")) color = Color.BLUE;
//				 */
//
//				/* !!!!!!!!!!!!!!!!!!!!!!! */
//				/* !!! 카테고리 상수들 향후 서버에서 받아온 값 기준으로 수정 필요!!!! */
//				/* !!!!!!!!!!!!!!!!!!!!!!! */
//				ImageView categoryIcon = (ImageView)convertView.findViewById(R.id.imgbtn_mentory_category);
//				TextView tvMentoryCategory = (TextView)convertView.findViewById(R.id.tv_mentory_category);
//
//				//Log.d("MentoryFragment", list.get(pos).getMentoring_srl().substring(0,3));
//				if(list.get(pos).getMentoring_srl().length() > 3) {
//					if(list.get(pos).getMentoring_srl().substring(0,3).equals("100") || list.get(pos).getMentoring_srl().substring(0,3).equals("110") || list.get(pos).getMentoring_srl().substring(0,3).equals("111")) {
//						categoryIcon.setBackground(convertView.getResources().getDrawable(R.drawable.mento_icon_edu));
//						tvMentoryCategory.setText("교육멘토리");
//					}
//					else if(list.get(pos).getMentoring_srl().substring(0,3).equals("200")) {
//						categoryIcon.setBackground(convertView.getResources().getDrawable(R.drawable.mento_icon_nur));
//						tvMentoryCategory.setText("육아멘토리");
//					}
//					else if(list.get(pos).getMentoring_srl().substring(0,3).equals("300")) {
//						categoryIcon.setBackground(convertView.getResources().getDrawable(R.drawable.mento_icon_nur));
//						tvMentoryCategory.setText("운영멘토리");
//					}
//					else if(list.get(pos).getMentoring_srl().substring(0,3).equals("400")) {
//						categoryIcon.setBackground(convertView.getResources().getDrawable(R.drawable.mento_icon_healing));
//						tvMentoryCategory.setText("힐링멘토리");
//					}
//					else if(list.get(pos).getMentoring_srl().substring(0,3).equals("500")) {
//						categoryIcon.setBackground(convertView.getResources().getDrawable(R.drawable.mento_icon_edu));
//						tvMentoryCategory.setText("키즈엠소식");
//					}
//				}
//				else {
//					Log.d("MentoryFragment", list.get(pos).getMentoring_srl());
//				}
//
//				ImageView btn = (ImageView)convertView.findViewById(R.id.imagebtn_mentory_picture);
//				btn.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(ctx, SeeMentoryActivity.class);
//						//intent.putExtra("mentoring_srl", list.get(pos).getMentoring_srl());
//						intent.putExtra("mentoring_text", list.get(pos).getMentoring_text());
//						intent.putExtra("mentoring_subject", list.get(pos).getMentoring_subject());
//						//Toast.makeText(ctx, list.get(pos).getMentoring_text(), Toast.LENGTH_SHORT).show();
//						startActivity(intent);
//					}
//				});
//			}
//
//			//	else{
//			//		
//			//		//holder = (BusstopViewHolder)convertView.getTag();
//			//	}
//
//			return convertView;
//		}
//
//		class MentoryViewHolder {
//			public LinearLayout cellLayout;
//			public View colorBar;
//			public ImageView busstopIcon;
//			public TextView textView;
//			public Button nextButton;
//
//			public View blueTopLine;
//			public View blueBottomLine;
//			public View greyTopLine;
//			public View greyBottomLine;
//		}
//	}
}
