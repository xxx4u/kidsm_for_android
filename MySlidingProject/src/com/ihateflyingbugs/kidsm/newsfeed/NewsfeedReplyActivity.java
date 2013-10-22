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
import android.view.Menu;
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

public class NewsfeedReplyActivity extends NetworkActivity{
	String timeline_srl;
	News news;
	ViewFlipper viewFlipper;
	ArrayList<Reply> replyList;
	ReplyAdapter replyAdapter;
	ListView replyListView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeedreply);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		timeline_srl = getIntent().getStringExtra("timeline_srl");
		replyList = new ArrayList<Reply>();
		replyAdapter = new ReplyAdapter(this, replyList);
		replyListView = (ListView)findViewById(R.id.replylayout);
		replyListView.setAdapter(replyAdapter);
		replyListView.setDivider(null);
		replyListView.setDividerHeight(0);
		
		this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
		//news = NewsfeedFragment.getNews(key);
		//updateReply();
		//updateLikeList();
		viewFlipper = (ViewFlipper)findViewById(R.id.replyflipper);
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
		//news.updateLayout();
		//NewsfeedFragment.setNews(key, news);
		finish();
	}
	
	public void OnPostClick(View v) {
//		LinearLayout linear1 = (LinearLayout)View.inflate(this, R.layout.newsfeed_reply, null);
//		LinearLayout svw = (LinearLayout)findViewById(R.id.replylayout);
//		TextView txt = (TextView)findViewById(R.id.replytext);
//		Reply reply = new Reply(getResources().getString(R.string.profile_parentname), "image", txt.getText().toString());
//		txt.setText("");
//		txt = (TextView)linear1.findViewById(R.id.reply_name);
//		txt.setText(reply.getName());
//		txt = (TextView)linear1.findViewById(R.id.reply_secondtext);
//		txt.setText(reply.getMessage());
//		news.getReplyList().add(reply);
//		svw.addView(linear1);
//		news.updateLayout();
		//NewsfeedFragment.setNews(key, news);
		EditText txt = (EditText)findViewById(R.id.replytext);
		this.request_Timeline_setTimelineComment(timeline_srl, SlidingMenuMaker.getProfile().member_srl, txt.getText().toString());
	}
	
	private void updateReply() {
//		for(int i = 0; i < news.getReplyList().size(); i++ ) {
//			LinearLayout linear1 = (LinearLayout)View.inflate(this, R.layout.newsfeed_reply, null);
//			LinearLayout svw = (LinearLayout)findViewById(R.id.replylayout);
//			TextView txt =  (TextView)linear1.findViewById(R.id.reply_name);
//			txt.setText(news.getReplyList().get(i).getName());
//			txt =  (TextView)linear1.findViewById(R.id.reply_secondtext);
//			txt.setText(news.getReplyList().get(i).getMessage());
//			svw.addView(linear1);
//		}
	}
	
	private void updateLikeList() {
//		for(int i = 0; i < news.getLikeList().size(); i++ ) {
//			RelativeLayout linear1 = (RelativeLayout)View.inflate(this, R.layout.newsfeed_likemember, null);
//			LinearLayout svw = (LinearLayout)findViewById(R.id.newsreply_likelist);
//			TextView txt =  (TextView)linear1.findViewById(R.id.like_name);
//			txt.setText(news.getLikeList().get(i).getName());
//			svw.addView(linear1);
//		}
//		Button btn = (Button)findViewById(R.id.newsreply_likeinfo);
//		btn.setText(news.getLikeThisNewsString());
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
		        NewsfeedReplyActivity.this.runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	replyAdapter.notifyDataSetChanged();
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
				notifyDataSetChanged();
			}
			else if( uri.equals("Timeline/setTimelineComment") ) {
				this.request_Timeline_getTimelineComments(timeline_srl, 1, 100000);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}