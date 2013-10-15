package com.ihateflyingbugs.kidsm.notice;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NoticeActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		addNotices();
	}
	
	public void mOnClick(View v) {
		finish();
	}
	
	private void addNotices() {
		for(int i = 0; i < 10; i++ ) {
			RelativeLayout linear1 = (RelativeLayout)View.inflate(this, R.layout.notice_textonly, null);
			LinearLayout svw = (LinearLayout)findViewById(R.id.notice);
			svw.addView(linear1);
		}
	}
}
