package com.ihateflyingbugs.kidsm.point;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;
import com.ihateflyingbugs.kidsm.R.menu;
import com.ihateflyingbugs.kidsm.notice.NoticeActivity;
import com.ihateflyingbugs.kidsm.photonotice.PhotoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PointActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
//		case android.R.id.home:
//			toggle();
//			return true;
		case R.id.notice:
			intent = new Intent(this, NoticeActivity.class);
			startActivity(intent);
			return true;
		case R.id.photo:
			intent = new Intent(this, PhotoActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	public void mOnClick(View v) {
		finish();
	}
}
