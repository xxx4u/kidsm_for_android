package com.ihateflyingbugs.kidsm.schedule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.localytics.android.LocalyticsSession;

public class ShowConfirmedListActivity extends NetworkActivity {
	
	ArrayList<ConfirmedMember> memberList;
	ConfirmedMemberAdapter memberListAdapter;
	int memberListCounter;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showconfirmedlist);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));

		memberListCounter = 0;
		memberList = getIntent().getParcelableArrayListExtra("memberList");
		for(int i = 0; i < memberList.size(); i++)
			this.request_Member_getMember(memberList.get(i).member_name);
		setTitle("확인한 학부모("+memberList.size()+"/"+SlidingMenuMaker.getProfile().getCurrentClass().getNumOfStudentHavingParent()+")");
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
		getMenuInflater().inflate(R.menu.close, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case R.id.close:
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Member/getMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_picture = jsonObj.getString("member_picture");
					for(int i = 0; i < memberList.size(); i++) {
						if(member_srl.equals(memberList.get(i).member_name)) {
							memberList.get(i).member_name = member_name;
							memberList.get(i).member_picture = member_picture;
							if( ++memberListCounter == memberList.size() ) {
								new Thread(new Runnable() {
								    @Override
								    public void run() {    
								        runOnUiThread(new Runnable(){
								            @Override
								             public void run() {
								            	memberListAdapter = new ConfirmedMemberAdapter(ShowConfirmedListActivity.this, memberList);
												ListView list = (ListView) findViewById(R.id.showconfirmedlist_list);
												list.setAdapter(memberListAdapter);
								            }
								        });
								    }
								}).start();
							}
							break;
						}
					}
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
