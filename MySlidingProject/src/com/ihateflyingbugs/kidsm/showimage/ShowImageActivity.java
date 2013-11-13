package com.ihateflyingbugs.kidsm.showimage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.gallery.Photo;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.LikeMember;
import com.ihateflyingbugs.kidsm.newsfeed.News;
import com.ihateflyingbugs.kidsm.newsfeed.NewsfeedFragment;
import com.ihateflyingbugs.kidsm.newsfeed.PhotoNews;
import com.ihateflyingbugs.kidsm.newsfeed.Reply;
import com.ihateflyingbugs.kidsm.newsfeed.ReplyActivity;
import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;
import com.localytics.android.LocalyticsSession;

class CustomImage {
	ImageView view;
	float width;
	float height;
	float baseWidth;
	float baseHeight;
	
	CustomImage(ImageView view) {
		this.view = view;
		baseWidth = this.width = view.getDrawable().getIntrinsicWidth();
		baseHeight = this.height = view.getDrawable().getIntrinsicHeight();
		//this.view.setBackgroundColor(Color.WHITE);
	}
	
	void setScale(float ratio) {
		width = baseWidth*ratio;
		height = baseHeight*ratio;
	}
}

public class ShowImageActivity extends NetworkActivity{
	
	final static float STEP = 200;
	float mRatio = 1.0f;
	int mBaseDist;
	float mBaseRatio;
	CustomImage image;
	int screenWidth;
	int screenHeight;
	float minScale;
	float maxScale;
	float prevPositionX;
	float prevPositionY;
	//News news;
	LinearLayout linear;
	ImageLoader imageLoader;
	//Toast testToast = Toast.makeText(this, "사진 확대 서비스 업데이트 예정입니다^^ 10월 13일", Toast.LENGTH_LONG);
	Photo photo;
	ArrayList<String> likeList;
	ArrayList<Reply> commentList;
	String member_scrap_srl;
	int scrapCount;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		
		imageLoader = new ImageLoader(this, R.drawable.photo_in_album_default);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(
			    Context.LAYOUT_INFLATER_SERVICE);
		linear = (LinearLayout)inflater.inflate(R.layout.showimage_overlay, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
			    LinearLayout.LayoutParams.FILL_PARENT,
			    LinearLayout.LayoutParams.FILL_PARENT);
		addContentView(linear, paramlinear);
		
		//key = getIntent().getStringExtra("key");
		//news = NewsfeedFragment.getNews(key);
		initiateLayout();
				
		Display newDisplay = getWindowManager().getDefaultDisplay(); 
		screenWidth = newDisplay.getWidth();
		screenHeight = newDisplay.getHeight();
		
		ImageView view = (ImageView)findViewById(R.id.image);
		
		imageLoader.DisplayImage(getString(R.string.image_url)+getIntent().getStringExtra("photo_url"), view);
		likeList = new ArrayList<String>();
		commentList = new ArrayList<Reply>();
		requestPhotoInfo();
		
		image = new CustomImage(view);
		minScale = mRatio = Math.min(screenWidth/image.width, screenHeight/image.height);
		//view.setScaleX(mRatio);
		//view.setScaleY(mRatio);
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
	
	private void requestPhotoInfo() {
		likeList.clear();
		commentList.clear();
		member_scrap_srl = "";
		scrapCount = 0;
		this.request_Album_getPhoto(getIntent().getStringExtra("photo_srl"));
	}
	
	void initiateLayout() {
//		View btn = linear.findViewById(R.id.image_like);
//		btn.setTag(key);
//		btn = linear.findViewById(R.id.image_reply);
//		btn.setTag(key);
//		btn = linear.findViewById(R.id.image_scrap);
//		btn.setTag(key);
		//news.setReplyLinear(linear);
		//news.updateLayout();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			switch(requestCode) {
			case 1:
				//news.updateLayout();
				//NewsfeedActivity.setNews(key, news);
				finish();
				break;
			}
		}
	}
	
	public void OnLikeClick(View v) {
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
		if( likeList.contains(member_srl) == false )
			this.request_Album_setPhotoLike(photo.photo_srl, member_srl);
		else
			this.request_Album_delPhotoLike(photo.photo_srl, member_srl);
	}
	
	public void OnReplyClick(View v) {
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
		this.request_Timeline_getTimelineMessage(member_srl, photo.photo_timeline_srl);
	}
	
	public void OnScrapClick(View v) {
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
		if( member_scrap_srl.isEmpty() )
			this.request_Scrap_setScrap(member_srl, "P", photo.photo_srl);
		else
			this.request_Scrap_delScrap(member_srl, member_scrap_srl, photo.photo_srl);
	}
	public void OnModify(View v) {
//		Intent intent = new Intent(this, ModifyImageActivity.class);
//		intent.putExtra("filepath", "filepath");
//		startActivityForResult(intent, 1);
		//Toast.makeText(this, "업데이트 예정입니다^^ 10월 13일", Toast.LENGTH_LONG).show();
	}
	
	public void OnClose(View v) {
//		news.updateLayout();
//		NewsfeedFragment.setNews(key, news);
		finish();
	}
	public void mOnClick(View v) {
		finish();
	}

	
	public boolean onTouchEvent(MotionEvent event) {
//		float leftWall = -screenWidth/2;
//		float rightWall = screenWidth/2;
//		float topWall = -screenHeight/2;
//		float bottomWall = screenHeight/2;
//		float width = image.view.getDrawable().getIntrinsicWidth()*mRatio;
//		float height = image.view.getDrawable().getIntrinsicHeight()*mRatio;
//		float translationX = image.view.getTranslationX();
//		float translationY = image.view.getTranslationY();
//		float gapX = event.getX() - prevPositionX;
//		float gapY = event.getY() - prevPositionY;
//		if( event.getPointerCount() == 1 ) {
//			int action = event.getAction();
//			int pureaction = action & MotionEvent.ACTION_MASK;
//			if( pureaction == MotionEvent.ACTION_DOWN) {
//				prevPositionX = event.getX();
//				prevPositionY = event.getY();
//			}
//			if (pureaction == MotionEvent.ACTION_MOVE) {
//				if( translationX + gapX < width/2+leftWall && translationX + gapX > -width/2+rightWall )
//					image.view.setTranslationX(translationX + gapX);
//				if( translationY + gapY < height/2+topWall && translationY + gapY > -height/2+bottomWall )
//					image.view.setTranslationY(translationY + gapY);
//				prevPositionX = event.getX();
//				prevPositionY = event.getY();
//			}
//		}
//		else if (event.getPointerCount() == 2) {
//			int action = event.getAction();
//			int pureaction = action & MotionEvent.ACTION_MASK;
//			if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
//				mBaseDist = getDistance(event);
//				mBaseRatio = mRatio;
//			} else {
//				float delta = (getDistance(event) - mBaseDist) / STEP;
//				float multi = (float)Math.pow(2, delta);
//				mRatio = Math.min(2*minScale, Math.max(minScale, mBaseRatio * multi));
//				width = image.view.getDrawable().getIntrinsicWidth()*mRatio;
//				height = image.view.getDrawable().getIntrinsicHeight()*mRatio;
//				if(screenWidth/image.width <= screenHeight/image.height) {
//					if( translationX > width/2+leftWall )
//						image.view.setTranslationX(leftWall+width/2);
//					if( translationX < -width/2+rightWall )
//						image.view.setTranslationX(rightWall-width/2);
//					if( height >= screenHeight ) {
//						if( translationY > height/2+topWall )
//							image.view.setTranslationY(topWall+height/2);
//						if( translationY < -height/2+bottomWall )
//							image.view.setTranslationY(bottomWall-height/2);
//					}
//				}
//				if(screenWidth/image.width >= screenHeight/image.height){
//					if( translationY > height/2+topWall )
//						image.view.setTranslationY(topWall+height/2);
//					if( translationY < -height/2+bottomWall )
//						image.view.setTranslationY(bottomWall-height/2);
//					if( width >= screenWidth ) {
//						if( translationX > width/2+leftWall )
//							image.view.setTranslationX(leftWall+width/2);
//						if( translationX < -width/2+rightWall )
//							image.view.setTranslationX(rightWall-width/2);
//					}
//				}
//				image.view.setScaleX(mRatio);
//				image.view.setScaleY(mRatio);
//			}
//		}
		
		return true; 
	}

	int getDistance(MotionEvent event) {
		int dx = (int)(event.getX(0) - event.getX(1));
		int dy = (int)(event.getY(0) - event.getY(1));
		return (int)(Math.sqrt(dx * dx + dy * dy));
	}
	
	private void updateLayout() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		        		TextView txt;
		        		txt = (TextView)linear.findViewById(R.id.image_numoflike);
		        		txt.setText(""+likeList.size());
		        		txt = (TextView)linear.findViewById(R.id.image_numofreply);
		        		txt.setText(""+commentList.size());
		        		txt = (TextView)linear.findViewById(R.id.image_numofscrap);
		        		txt.setText(""+scrapCount);
		        		
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
		    			txt = (TextView)linear.findViewById(R.id.image_liketext);
		    			final CheckBox cblike = (CheckBox) linear.findViewById(R.id.image_like_animation);
		    			if(likeList.contains(member_srl)) {
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
		    			
		    			txt = (TextView)linear.findViewById(R.id.image_scraptext);
		    			final CheckBox cbscrap = (CheckBox) linear.findViewById(R.id.image_scrap_animation);
		    			if(member_scrap_srl.isEmpty() == false) {
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
		            }
		        });
		    }
		}).start();
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			if(response.startsWith("<!DOCTYPE html>")) {
				return;
			}
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Album/getPhoto")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
					String photo_member_srl = jsonObj.getString("photo_member_srl");
					String photo_album_srl = jsonObj.getString("photo_album_srl");
					String photo_timeline_srl = jsonObj.getString("photo_timeline_srl");
					String photo_tag = jsonObj.getString("photo_tag");
					String photo_path = jsonObj.getString("photo_path");
					String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
					String photo_like = jsonObj.getString("photo_like");
					String photo_private = jsonObj.getString("photo_private");
					String photo_created = jsonObj.getString("photo_created");
					String photo_updated = jsonObj.getString("photo_updated");
					photo = new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated);
					if( photo_like.isEmpty() == false ) {
						String[] likeMemberData = photo_like.split(",");
						for(int i = 0; i < likeMemberData.length; i++) {
							if(likeMemberData[i] != null && likeMemberData[i].isEmpty() == false)
								likeList.add(likeMemberData[i]);
						}
					}
					
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
					updateLayout();
					this.request_Timeline_getTimelineComments(photo_timeline_srl, 1, 100000);
					this.request_Scrap_getScraps(member_srl, 1, 10000, "P");
					this.request_Scrap_getScrapCount(photo_srl, "P");
				}
				else if( uri.equals("Timeline/getTimelineComments") ) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++ ) {
						JSONObject dataObj = dataArray.getJSONObject(i);
						String tcomment_srl = dataObj.getJSONObject("_id").getString("$oid");
						String tcomment_member_srl = dataObj.getString("tcomment_member_srl");
						String tcomment_timeline_srl = dataObj.getString("tcomment_timeline_srl");
						String tcomment_message = dataObj.getString("tcomment_message");
						String tcomment_created = dataObj.getString("tcomment_created");
						commentList.add(new Reply(tcomment_srl, tcomment_member_srl, tcomment_timeline_srl, tcomment_message, tcomment_created));
						updateLayout();
					}
				}
				else if(uri.equals("Scrap/getScraps")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String scrap_srl = dataArray.getJSONObject(i).getString("scrap_srl");
						String scrap_member_srl = dataArray.getJSONObject(i).getString("scrap_member_srl");
						String scrap_type = dataArray.getJSONObject(i).getString("scrap_type");
						String scrap_target_srl = dataArray.getJSONObject(i).getString("scrap_target_srl");
						String scrap_created = dataArray.getJSONObject(i).getString("scrap_created");
						if( scrap_type.equals("P") && photo.photo_srl.equals(scrap_target_srl) ) {
							member_scrap_srl = scrap_srl;
							updateLayout();
						}
					}
				}
				else if( uri.equals("Scrap/getScrapCount") ) {
					String type = jsonObj.getString("type");
					String target_srl = jsonObj.getString("target_srl");
					String count = jsonObj.getString("count");
					scrapCount = Integer.parseInt(count);
					updateLayout();
				}
				else if( uri.equals("Timeline/getTimelineMessage") ) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
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
						
						new Thread(new Runnable() {
						    @Override
						    public void run() {    
						        runOnUiThread(new Runnable(){
						            @Override
						             public void run() {
						            	Intent intent = new Intent(ShowImageActivity.this, ReplyActivity.class);
										intent.putExtra("type", "T");
										intent.putExtra("timeline_srl", timeline_srl);
										intent.putExtra("timeline_member_srl", timeline_member_srl);
										startActivity(intent);
						            }
						        });
						    }
						}).start();
						break;
					}
				}
				else if( uri.equals("Album/setPhotoLike") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
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
					likeList.add(member_srl);
					updateLayout();
				}
				else if( uri.equals("Album/delPhotoLike") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
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
					likeList.remove(member_srl);
					updateLayout();
				}
				else if( uri.equals("Scrap/setScrap") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String scrap_srl = jsonObj.getString("scrap_srl");
					String scrap_member_srl = jsonObj.getString("scrap_member_srl");
					String scrap_type = jsonObj.getString("scrap_type");
					String scrap_target_srl = jsonObj.getString("scrap_target_srl");
					String scrap_created = jsonObj.getString("scrap_created");
					member_scrap_srl = scrap_srl;
					scrapCount++;
					updateLayout();
				}
				else if( uri.equals("Scrap/delScrap") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String scrap_srl = jsonObj.getString("scrap_srl");
					String scrap_member_srl = jsonObj.getString("scrap_member_srl");
					String scrap_type = jsonObj.getString("scrap_type");
					String scrap_target_srl = jsonObj.getString("scrap_target_srl");
					String scrap_created = jsonObj.getString("scrap_created");
					member_scrap_srl = "";
					scrapCount--;
					updateLayout();
				}
			}
			else {
				
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		}
	}
}
