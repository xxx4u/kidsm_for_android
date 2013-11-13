package com.ihateflyingbugs.kidsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.ihateflyingbugs.kidsm.login.LoginActivity;
import com.ihateflyingbugs.kidsm.menu.Children;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.localytics.android.LocalyticsSession;

public class SplashActivity extends Activity {
	Profile profile;
	int numOfStudent;
	int studentCounter;
	ProgressBar progressbar;
	TextView state;
	
	
	static Sender sender;
	static com.google.android.gcm.server.Message message;
	
	private LocalyticsSession localyticsSession;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if("".equals(regId))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
		      GCMRegistrar.register(this, "413007677888");
		else
		      Log.d("==============", regId);
		

//		StrictMode.enableDefaults();
//    	sender = new Sender("AIzaSyCYccyf46AbrIbtGmVSPB9H8S-wbDsErJS");
//		message = new com.google.android.gcm.server.Message.Builder().addData("title", "welcome")
//				.addData("msg", "introduce me!").build();
//		
//		new Thread(new Runnable() {
//		    @Override
//		    public void run() {    
//		    	runOnUiThread(new Runnable(){
//		            @Override
//		             public void run() {
//		                handler.sendMessage(new Message());
//		            }
//		        });
//		    }
//		}).start();
        
		setContentView(R.layout.activity_splash);
		progressbar = (ProgressBar)findViewById(R.id.splash_progressbar);
		state = (TextView)findViewById(R.id.splash_state);
		initialize();
	
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
	
	private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	try {
    			String regId = "APA91bHkTdeKelQ4Knorq85zYCHdrPT2OVl4H6dE3GbRHXume0P-kGNlNFaxV8Xtm-p7ahfpQYHQDr-DJEbBTozXJkIh4vG-uujWmNL5S3tNiz4dvVDGmf56z_N5oDXNTmjWjxdDZoD_s9NmWXQwWk1FF-lHEU0vQw";
    			Result result = sender.send(message, regId, 5);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    };
    
	private void initialize()
	{
		state.setText("서버와의 데이터 동기화를 시작합니다");
//		progressbar.animate();
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("org_srl", "1"));
//		params.add(new BasicNameValuePair("class_srl", "0"));
//		params.add(new BasicNameValuePair("index", "1"));
//		params.add(new BasicNameValuePair("count", "5"));
//		GET("Organization/getOrgStudents", params);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("member_srl", "10"));
//		params.add(new BasicNameValuePair("org_srl", "1"));
//		params.add(new BasicNameValuePair("index", "1"));
//		params.add(new BasicNameValuePair("count", "5"));
//		GET("Member/getRecommendFriends", params);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("org_srl", "1"));
//		params.add(new BasicNameValuePair("class_srl", "1"));
//		params.add(new BasicNameValuePair("member_type", "T"));
//		params.add(new BasicNameValuePair("page", "1"));
//		params.add(new BasicNameValuePair("count", "5"));
//		GET("Member/getMembers", params);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("member_name", "마영명"));
//		params.add(new BasicNameValuePair("member_type", "S"));
//		params.add(new BasicNameValuePair("org_srl", "1"));
//		params.add(new BasicNameValuePair("member_email", "ert@gmail.com"));
//		params.add(new BasicNameValuePair("member_password", "ert"));
//		params.add(new BasicNameValuePair("member_device_type", "I"));
//		params.add(new BasicNameValuePair("member_device_uuid", "ert"));
//		POST("Member/addMember", params);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("member_srl", "7"));
//		params.add(new BasicNameValuePair("member_status", "A"));
//		PUT("Member/modMemberStatus", params);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("member_srl", "13"));
//		params.add(new BasicNameValuePair("member_email", "ebc@gmail.com"));
//		params.add(new BasicNameValuePair("member_password", "ebc"));
//		DELETE("Member/delMember", params);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", "12"));
		//GET("Member/getMember", params);
		
		studentCounter = 0;
		
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				startNewsfeedActivity();
			}
		};
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	
	private void startNewsfeedActivity()
	{
		startActivity(new Intent(this, LoginActivity.class));
		finish();
		overridePendingTransition(0, android.R.anim.fade_out);
	}
	
	void updateState(final String _state) {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	state.setText(_state);
		            	}
		        });
		    }
		}).start();
	}
	
}
