package com.ihateflyingbugs.kidsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class SplashActivity extends NetworkActivity {
	Profile profile;
	int numOfStudent;
	int studentCounter;
	ProgressBar progressbar;
	TextView state;
	
	
	static Sender sender;
	static com.google.android.gcm.server.Message message;
	
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
	@Override
	public void response(String uri, String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") == false ) {
				new AlertDialog.Builder(this)
				.setMessage("서버로부터 데이터를 받아오는 데 실패했습니다 : " + result)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.show();
				return;
			}
			
			if(uri.equals("Member/getMember")) {
				String nativeData = jsonObj.getString("data");
				jsonObj = new JSONObject(nativeData);
				String member_srl = jsonObj.getString("member_srl");
				String member_name = jsonObj.getString("member_name");
				String member_type = jsonObj.getString("member_type");
				String member_org_srl = jsonObj.getString("member_org_srl");
				String member_point = jsonObj.getString("member_point");
				String member_email = jsonObj.getString("member_email");
				String member_picture = jsonObj.getString("member_picture");
				String member_device_type = "";//jsonObj.getString("member_device_type");
				String member_device_uuid = "";//jsonObj.getString("member_device_uuid");
				if(member_type.equals("T")){
					
				}
				else if(member_type.equals("S")) {
					updateState("자식 정보를 받아옵니다2");
					jsonObj = jsonObj.getJSONObject("student");
					String student_srl = jsonObj.getString("student_srl");
					String student_member_srl = jsonObj.getString("student_member_srl");
					String student_class_srl = jsonObj.getString("student_class_srl");
					String student_parent_srl = jsonObj.getString("student_parent_srl");
					String student_teacher_srl = jsonObj.getString("student_teacher_srl");
					String student_shuttle_srl = jsonObj.getString("student_shuttle_srl");
					String student_birthday = jsonObj.getString("student_birthday");
					String student_parent_key = jsonObj.getString("student_parent_key");
//					profile.addChildren(Integer.parseInt(student_member_srl), new Children(student_srl, student_member_srl, member_name, member_picture, profile.getMember_name(), member_org_srl, 
//							student_class_srl, student_parent_srl, student_teacher_srl, student_shuttle_srl, student_birthday, student_parent_key));
//					
//					if( ++studentCounter == numOfStudent ) {
//						profile.addChildren(-1, new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));
//						SlidingMenuMaker.setProfile(profile);
//						startNewsfeedActivity();
//					}
					
					//List<NameValuePair> params = new ArrayList<NameValuePair>();
					//params.add(new BasicNameValuePair("org_srl", member_org_srl));
					//GET("/Organization/getOrganization", params);
				}
				else if(member_type.equals("P")) {
					updateState("부모 정보를 받아옵니다");
					jsonObj = jsonObj.getJSONObject("parent");
					String parent_srl = jsonObj.getString("parent_srl");
					//profile = new Profile(member_srl, member_name, member_type, member_org_srl, member_point, 
					//		member_email, member_picture, member_device_type, member_device_uuid, parent_srl);
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("parent_srl", parent_srl));
					GET("Member/getParentStudents", params);
				}
				else if(member_type.equals("V")) {
					
				}
			}
			else if(uri.equals("Member/getParentStudents")) {
				updateState("자식 정보를 받아옵니다1");
				String nativeData = jsonObj.getString("data");
				JSONArray dataArray = new JSONArray(nativeData);
				numOfStudent = dataArray.length();
				for(int i = 0; i < dataArray.length(); i++) {
//					String student_srl = dataArray.getJSONObject(i).getString("student_srl");
					String student_member_srl = dataArray.getJSONObject(i).getString("student_member_srl");
//					String student_class_srl = dataArray.getJSONObject(i).getString("student_class_srl");
//					String student_parent_srl = dataArray.getJSONObject(i).getString("student_parent_srl");
//					String student_teacher_srl = dataArray.getJSONObject(i).getString("student_teacher_srl");
//					String student_shuttle_srl = dataArray.getJSONObject(i).getString("student_shuttle_srl");
//					String student_birthday = dataArray.getJSONObject(i).getString("student_birthday");
//					String student_parent_key = dataArray.getJSONObject(i).getString("student_parent_key");
//					profile.addChildren(Integer.parseInt(student_member_srl), new Children(student_srl, student_member_srl, student_class_srl, 
//							student_parent_srl, student_teacher_srl, student_shuttle_srl, student_birthday, student_parent_key));
				
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("member_srl", student_member_srl));
					GET("Member/getMember", params);
				
				}
			}
			else if(uri.equals("Organization/getOrganization")) {
				
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		
//		if( uri.equals("Member/getRecommendFriends") ) {
//		}
	}
}
