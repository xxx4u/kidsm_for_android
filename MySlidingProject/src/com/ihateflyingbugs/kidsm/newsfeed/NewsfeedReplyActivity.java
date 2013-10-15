package com.ihateflyingbugs.kidsm.newsfeed;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.R;

public class NewsfeedReplyActivity extends Activity{
	String key;
	News news;
	ViewFlipper viewFlipper;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeedreply);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		key = getIntent().getStringExtra("key");
		//news = NewsfeedFragment.getNews(key);
		updateReply();
		updateLikeList();
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
}