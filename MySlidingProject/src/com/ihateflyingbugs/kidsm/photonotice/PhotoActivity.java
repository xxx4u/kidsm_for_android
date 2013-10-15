package com.ihateflyingbugs.kidsm.photonotice;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PhotoActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		addPhotoNotices();
	}
	
	public void mOnClick(View v) {
		finish();
	}
	
	private void addPhotoNotices() {
		for(int i = 0; i < 10; i++ ) {
			RelativeLayout linear1 = (RelativeLayout)View.inflate(this, R.layout.photonotice_textonly, null);
			LinearLayout svw = (LinearLayout)findViewById(R.id.photonotice);
			svw.addView(linear1);
		}
	}
}
