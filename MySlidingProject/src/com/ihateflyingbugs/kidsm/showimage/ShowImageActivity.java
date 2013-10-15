package com.ihateflyingbugs.kidsm.showimage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.LikeMember;
import com.ihateflyingbugs.kidsm.newsfeed.News;
import com.ihateflyingbugs.kidsm.newsfeed.NewsfeedFragment;
import com.ihateflyingbugs.kidsm.newsfeed.NewsfeedReplyActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

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

public class ShowImageActivity extends Activity{
	
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
	Toast toast;
	String key;
	//News news;
	LinearLayout linear;
	ImageLoader imageLoader;
	//Toast testToast = Toast.makeText(this, "사진 확대 서비스 업데이트 예정입니다^^ 10월 13일", Toast.LENGTH_LONG);
	
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
		
		imageLoader.DisplayImage(getIntent().getStringExtra("url"), view);
		
		image = new CustomImage(view);
		minScale = mRatio = Math.min(screenWidth/image.width, screenHeight/image.height);
		//view.setScaleX(mRatio);
		//view.setScaleY(mRatio);
	}
	
	void initiateLayout() {
		View btn = linear.findViewById(R.id.image_like);
		btn.setTag(key);
		btn = linear.findViewById(R.id.image_reply);
		btn.setTag(key);
		btn = linear.findViewById(R.id.image_scrap);
		btn.setTag(key);
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
	}
	
	public void OnReplyClick(View v) {
		Intent intent = new Intent(this, NewsfeedReplyActivity.class);
		intent.putExtra("key", (String)v.getTag());
		startActivity(intent);
	}
	
	public void OnScrapClick(View v) {
//		if(news.isScrapAlreadyDone()) {
//			news.setScrapAlreadyDone(false);
//			news.setNumOfScrap(news.getNumOfScrap() - 1);
//		}
//		else {
//			Toast.makeText(getApplicationContext(), getResources().getString(R.string.news_donescrap), Toast.LENGTH_SHORT).show();
//			news.setScrapAlreadyDone(true);
//			news.setNumOfScrap(news.getNumOfScrap() + 1);
//		}
//		news.updateLayout();
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
}