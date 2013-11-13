package com.ihateflyingbugs.kidsm.point;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;
import com.ihateflyingbugs.kidsm.R.menu;
import com.localytics.android.LocalyticsSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PointActivity extends Activity{
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		}
		return false;
	}
	
	public void mOnClick(View v) {
		finish();
	}
}
