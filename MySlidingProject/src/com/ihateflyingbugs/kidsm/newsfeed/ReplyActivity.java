package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;
import com.ihateflyingbugs.kidsm.schedule.ScheduleFragment;
import com.localytics.android.LocalyticsSession;

public class ReplyActivity extends NetworkActivity{
	String timeline_srl;
	String timeline_member_srl;
	String mentoring_srl;
	String mentoring_member_srl;
	News news;
	ViewFlipper viewFlipper;
	ArrayList<Reply> replyList;
	ReplyAdapter replyAdapter;
	ListView replyListView;
	int replyCounter;
	boolean isRequestCommentAlarm;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeedreply);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		replyList = new ArrayList<Reply>();
		replyAdapter = new ReplyAdapter(this, replyList);
		replyListView = (ListView)findViewById(R.id.replylayout);
		replyListView.setAdapter(replyAdapter);
		replyListView.setDivider(null);
		replyListView.setDividerHeight(0);
		isRequestCommentAlarm = false;
		
		switch(getIntent().getStringExtra("type").charAt(0)) {
		case 'T':
			timeline_srl = getIntent().getStringExtra("timeline_srl");
			timeline_member_srl = getIntent().getStringExtra("timeline_member_srl");
			this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
			break;
		case 'M':
			mentoring_srl = getIntent().getStringExtra("mentoring_srl");
			mentoring_member_srl = getIntent().getStringExtra("mentoring_member_srl");
			this.request_Mentor_getComments(mentoring_srl, 1, 100000);
			break;
		}
		replyCounter = 0;
		viewFlipper = (ViewFlipper)findViewById(R.id.replyflipper);
		this.localyticsSession = new LocalyticsSession(this.getApplicationContext());  // Context used to access device resources
		this.localyticsSession.open();                // open the session
		this.localyticsSession.upload();      // upload any data
	}
	
	public void onResume() {
	    super.onResume();
	    this.localyticsSession.open();
	}
	
	public void onPause() {
	    this.localyticsSession.close();
	    this.localyticsSession.upload();
	    super.onPause();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( findViewById(R.id.replytext).getWindowToken(), 0 );
			finish();
			return true;
		}
		return false;
	}
	
	public void mOnClick(View v) {
		finish();
	}
	
	public void OnPostClick(View v) {
		EditText txt = (EditText)findViewById(R.id.replytext);
		switch(getIntent().getStringExtra("type").charAt(0)) {
		case 'T':
			this.request_Timeline_setTimelineComment(timeline_srl, SlidingMenuMaker.getProfile().member_srl, txt.getText().toString());
			break;
		case 'M':
			this.request_Mentor_setComment(mentoring_srl, SlidingMenuMaker.getProfile().member_srl, txt.getText().toString());
			break;
		}
	}
	
	public void OnFlip(View v) {
//		switch(v.getId()) {
//		case R.id.newsreply_likeinfo:
//			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_left));
//			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_left));
//			viewFlipper.showNext();
//			break;
//		case R.id.newsreply_likelisttitle:
//			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
//			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
//			viewFlipper.showPrevious();
//			break;
//		}
	}
	
	private void notifyDataSetChanged() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        ReplyActivity.this.runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	replyAdapter.notifyDataSetChanged();
		            	replyListView.setSelection(replyList.size()-1);
		            }
		        });
		    }
		}).start();
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") == false )
				return;

			if( uri.equals("Timeline/getTimelineComments") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				String timeline_srl = "";
				replyList.clear();
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
				
				if( replyList.size() != 0 ) {
					replyCounter = replyList.size();
					for(int i = 0; i < replyList.size(); i++) {
						this.request_Member_getMember(replyList.get(i).tcomment_member_srl);
					}
				}
				else {
					notifyDataSetChanged();
				}
			}
			else if( uri.equals("Timeline/setTimelineComment") ) {
				replyCounter = 0;
				isRequestCommentAlarm = true;
				this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
				this.request_Member_getMember(timeline_member_srl);
				
				
				new Thread(new Runnable() {
				    @Override
				    public void run() {    
				        ReplyActivity.this.runOnUiThread(new Runnable(){
				            @Override
				             public void run() {
				            	EditText txt = (EditText)findViewById(R.id.replytext);
								txt.setText("");
				            }
				        });
				    }
				}).start();
			}
			else if( uri.equals("Member/getMember") ) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String member_srl = jsonObj.getString("member_srl");
				String member_name = jsonObj.getString("member_name");
				String member_type = jsonObj.getString("member_type");
				String member_picture = jsonObj.getString("member_picture");
				if( isRequestCommentAlarm == false ) {
					for(int i = 0; i < replyList.size(); i++) {
						if(replyList.get(i).tcomment_member_srl.equals(member_srl)) {
							switch(member_type.charAt(0)) {
							case 'T':
								replyList.get(i).setCommenterName(member_name+" 선생님");
								break;
							case 'M':
								replyList.get(i).setCommenterName(member_name+" 원장선생님");
								break;
							case 'P':
								replyList.get(i).setCommenterName(member_name);
								break;
							}
							replyList.get(i).setTcomment_member_picture_uri(member_picture);
						}
					}
					if( --replyCounter == 0 ) {
						notifyDataSetChanged();
					}
				}
				else {
					isRequestCommentAlarm = false;
					switch(member_type.charAt(0)) {
					case 'P':
					case 'T':
					case 'M':
						switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
						case 'P':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 학부모님이 사진에 댓글을 남겼습니다.", "P");
							break;
						case 'T':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 선생님이 사진에 댓글을 남겼습니다.", "P");
							break;
						case 'M':
							this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 원장선생님이 사진에 댓글을 남겼습니다.", "P");
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
				switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
				case 'P':
					this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 학부모님이 사진에 댓글을 남겼습니다.", "P");
					break;
				case 'T':
					this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 선생님이 사진에 댓글을 남겼습니다.", "P");
					break;
				case 'M':
					this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 댓글 알림", SlidingMenuMaker.getProfile().member_name+" 원장선생님이 사진에 댓글을 남겼습니다.", "P");
					break;
				}
			}
			
			else if( uri.equals("Mentor/getComments") ) {
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				replyList.clear();
				for(int i = 0; i < dataArray.length(); i++ ) {
					JSONObject dataObj = dataArray.getJSONObject(i);
					String comment_srl = dataObj.getString("comment_srl");
					String comment_mentoring_srl = dataObj.getString("comment_mentoring_srl");
					String comment_member_srl = dataObj.getString("comment_member_srl");
					String comment_text = dataObj.getString("comment_text");
					String comment_created = dataObj.getString("comment_created");
					String comment_updated = dataObj.getString("comment_updated");
					replyList.add(new Reply(comment_srl, comment_member_srl, comment_mentoring_srl, comment_text, comment_created));
				}
				
				if( replyList.size() != 0 ) {
					replyCounter = replyList.size();
					for(int i = 0; i < replyList.size(); i++) {
						this.request_Member_getMember(replyList.get(i).tcomment_member_srl);
					}
				}
				else {
					notifyDataSetChanged();
				}
			}
			else if( uri.equals("Mentor/setComment") ) {
				replyCounter = 0;
				//isRequestCommentAlarm = true;
				this.request_Mentor_getComments(mentoring_srl, 1, 100000);
				//this.request_Member_getMember(mentoring_member_srl);
				
				
				new Thread(new Runnable() {
				    @Override
				    public void run() {    
				        ReplyActivity.this.runOnUiThread(new Runnable(){
				            @Override
				             public void run() {
				            	EditText txt = (EditText)findViewById(R.id.replytext);
								txt.setText("");
				            }
				        });
				    }
				}).start();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
