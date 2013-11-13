package com.ihateflyingbugs.kidsm.showimage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.R;
import com.localytics.android.LocalyticsSession;

public class ShowUploadImageListActivity extends Activity{
	
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
	int fileIndex;
	ArrayList<String> filePathList;
	RelativeLayout linear;
	private LocalyticsSession localyticsSession;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		
		
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(
			    Context.LAYOUT_INFLATER_SERVICE);
		linear = (RelativeLayout)inflater.inflate(R.layout.showuploadimage_overlay, null);
		RelativeLayout.LayoutParams paramlinear = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		addContentView(linear, paramlinear);
		
		fileIndex = getIntent().getIntExtra("index", 0);
		//filePathList = getIntent().getStringArrayListExtra("filePathList");
		
		Display newDisplay = getWindowManager().getDefaultDisplay(); 
		screenWidth = newDisplay.getWidth();
		screenHeight = newDisplay.getHeight();
		
		ImageView view = (ImageView)findViewById(R.id.image);
		int mode = getIntent().getIntExtra("mode", -1);
		switch(mode) {
		case 0:
			view.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("filepath")));
			break;
		case 1:
			view.setImageBitmap(readBitmap(Uri.parse(getIntent().getStringExtra("filepath"))));
			break;
		}
		image = new CustomImage(view);
		minScale = mRatio = Math.min(screenWidth/image.width, screenHeight/image.height);
		view.setScaleX(mRatio);
		view.setScaleY(mRatio);
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

	public void OnClose(View v) {
		
		finish();
	}

	public boolean onTouchEvent(MotionEvent event) {
		float leftWall = -screenWidth/2;
		float rightWall = screenWidth/2;
		float topWall = -screenHeight/2;
		float bottomWall = screenHeight/2;
		float width = image.view.getDrawable().getIntrinsicWidth()*mRatio;
		float height = image.view.getDrawable().getIntrinsicHeight()*mRatio;
		float translationX = image.view.getTranslationX();
		float translationY = image.view.getTranslationY();
		float gapX = event.getX() - prevPositionX;
		float gapY = event.getY() - prevPositionY;
		if( event.getPointerCount() == 1 ) {
			int action = event.getAction();
			int pureaction = action & MotionEvent.ACTION_MASK;
			if( pureaction == MotionEvent.ACTION_DOWN) {
				prevPositionX = event.getX();
				prevPositionY = event.getY();
			}
			if (pureaction == MotionEvent.ACTION_MOVE) {
				if( translationX + gapX < width/2+leftWall && translationX + gapX > -width/2+rightWall )
					image.view.setTranslationX(translationX + gapX);
				if( translationY + gapY < height/2+topWall && translationY + gapY > -height/2+bottomWall )
					image.view.setTranslationY(translationY + gapY);
				prevPositionX = event.getX();
				prevPositionY = event.getY();
			}
		}
		else if (event.getPointerCount() == 2) {
			int action = event.getAction();
			int pureaction = action & MotionEvent.ACTION_MASK;
			if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
				mBaseDist = getDistance(event);
				mBaseRatio = mRatio;
			} else {
				float delta = (getDistance(event) - mBaseDist) / STEP;
				float multi = (float)Math.pow(2, delta);
				mRatio = Math.min(2*minScale, Math.max(minScale, mBaseRatio * multi));
				width = image.view.getDrawable().getIntrinsicWidth()*mRatio;
				height = image.view.getDrawable().getIntrinsicHeight()*mRatio;
				if(screenWidth/image.width <= screenHeight/image.height) {
					if( translationX > width/2+leftWall )
						image.view.setTranslationX(leftWall+width/2);
					if( translationX < -width/2+rightWall )
						image.view.setTranslationX(rightWall-width/2);
					if( height >= screenHeight ) {
						if( translationY > height/2+topWall )
							image.view.setTranslationY(topWall+height/2);
						if( translationY < -height/2+bottomWall )
							image.view.setTranslationY(bottomWall-height/2);
					}
				}
				if(screenWidth/image.width >= screenHeight/image.height){
					if( translationY > height/2+topWall )
						image.view.setTranslationY(topWall+height/2);
					if( translationY < -height/2+bottomWall )
						image.view.setTranslationY(bottomWall-height/2);
					if( width >= screenWidth ) {
						if( translationX > width/2+leftWall )
							image.view.setTranslationX(leftWall+width/2);
						if( translationX < -width/2+rightWall )
							image.view.setTranslationX(rightWall-width/2);
					}
				}
				image.view.setScaleX(mRatio);
				image.view.setScaleY(mRatio);
			}
		}

		return true; 
	}

	int getDistance(MotionEvent event) {
		int dx = (int)(event.getX(0) - event.getX(1));
		int dy = (int)(event.getY(0) - event.getY(1));
		return (int)(Math.sqrt(dx * dx + dy * dy));
	}
	
	public Bitmap readBitmap(Uri selectedImage) { 
		Bitmap bm = null; 
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inSampleSize = 5; 
		AssetFileDescriptor fileDescriptor =null; 
		try { 
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r"); 
		} 
		catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} 
		finally{ 
			try { 
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options); 
				fileDescriptor.close(); 
			} 
			catch (IOException e) { 
				e.printStackTrace(); 
			} 
		} 
		return bm; 
	} 
}
