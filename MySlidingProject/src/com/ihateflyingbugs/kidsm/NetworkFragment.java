package com.ihateflyingbugs.kidsm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class NetworkFragment extends Fragment {
	public static HashMap<String, String> results;
	public String url;
	static public String auth_key = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		results = new HashMap<String, String>();
		url = getResources().getString(R.string.url);
	}
	
	public String makeQuery(List<NameValuePair> params) {
		String query = "";
		for(int i = 0; i < params.size(); i++) {
    		if( i == 0 )
    			query += '?';
    		query += params.get(i).getName() + "=" + params.get(i).getValue();
    		if( i != params.size()-1 )
    			query += '&';
    	}
		
		query = query.replace(" ", "%20");
		return query;
	}
	
	public String GET_POSTAL_CODE(final String query) {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = "";
					HttpClient client = new DefaultHttpClient();
					String encodedQuery = URLEncoder.encode(query, "EUC-KR");
					HttpGet get = new HttpGet("http://biz.epost.go.kr/KpostPortal/openapied?regkey=a0140b22407d27fe91377838423839&target=post&query="+encodedQuery);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					if( entity != null ) {
						//Log.e("RESPONSE", EntityUtils.toString(entity));
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "EUC-KR"));
						while(true) {
							String line = br.readLine();
							if( line == null ) break;
							result += line;
						}
						br.close();
						results.put("POSTAL_CODE", result);
						response("POSTAL_CODE", result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		return "";
	}
	public String GET(final String uri, final List<NameValuePair> params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = "";
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url+uri+makeQuery(params));
					
					if(auth_key.isEmpty() == false)
						get.addHeader("Authorization", auth_key);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					if( entity != null ) {
						//Log.e("RESPONSE", EntityUtils.toString(entity));
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
						while(true) {
							String line = br.readLine();
							if( line == null ) break;
							result += line;
						}
						br.close();
						results.put(uri, result);
						response(uri, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		return "";
	}
	
	public String POST(final String uri, final List<NameValuePair> params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = "";
					DefaultHttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(url+uri);
					UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
					if(auth_key.isEmpty() == false)
						post.addHeader("Authorization", auth_key);
					post.addHeader("Content-Type", "application/x-www-form-urlencoded");
					post.setEntity(postEntity);
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();
					if(entity != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
						while(true) {
							String line = br.readLine();
							if( line == null ) break;
							result += line;
						}
						br.close();
						results.put(uri, result);
						response(uri, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return "";
	}
	
	public String PUT(final String uri, final List<NameValuePair> params) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			    try {	
					String result = "";
					HttpClient client = new DefaultHttpClient();
				    HttpPut put = new HttpPut(url+uri);
					UrlEncodedFormEntity putEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
					if(auth_key.isEmpty() == false)
						put.addHeader("Authorization", auth_key);
					put.addHeader("Content-Type", "application/x-www-form-urlencoded");
					put.setEntity(putEntity);
					HttpResponse response = client.execute(put);
				    HttpEntity entity = response.getEntity();
					if(entity != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
						while(true) {
							String line = br.readLine();
							if( line == null ) break;
							result += line;
						}
						br.close();
						results.put(uri, result);
						response(uri, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return "";
	}
	
	public String DELETE(final String uri, final List<NameValuePair> params) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			    try {	
					String result = "";
					HttpClient client = new DefaultHttpClient();
				    HttpDelete delete = new HttpDelete(url+uri+makeQuery(params));
				    if(auth_key.isEmpty() == false)
				    	delete.addHeader("Authorization", auth_key);
					HttpResponse response = client.execute(delete);
				    HttpEntity entity = response.getEntity();
					if(entity != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
						while(true) {
							String line = br.readLine();
							if( line == null ) break;
							result += line;
						}
						br.close();
						results.put(uri, result);
						response(uri, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return "";
	}
	
	public void response(String uri, String response) {
		return;
	}
	
	public void request_Album_getAlbum(String album_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		GET("Album/getAlbum", params);
	}
	
	public void request_Album_getAlbums(String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		GET("Album/getAlbums", params);
	}
	
	public void request_Album_getPhoto(String photo_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		GET("Album/getPhoto", params);
	}
	
	public void request_Album_getPhotos(String album_srl, String member_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Album/getPhotos", params);
	}
	
	public void request_Album_getMemberPhotos(String member_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Album/getMemberPhotos", params);
	}
	
	public void request_Album_getMemberTaggedPhotos(String member_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Album/getMemberTaggedPhotos", params);
	}
	
	public void request_Album_setAlbum(String album_name, String member_srl, String album_type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_name", album_name));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("album_type", album_type));
		POST("Album/setAlbum", params);
	}
	
	public void request_Album_modAlbum(String album_srl, String album_name, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("album_name", album_name));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		PUT("Album/modAlbum", params);
	}
	
	public void request_Album_delAlbum(String album_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Album/delAlbum", params);
	}
	
	public void request_Album_setPhotoLike(String photo_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		POST("Album/setPhotoLike", params);
	}
	
	public void request_Album_delPhotoLike(String photo_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Album/delPhotoLike", params);
	}
	
	public void request_Album_setPhoto(String album_srl, String member_srl, String photo_tag, String photo_private, Bitmap photo) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("photo_tag", photo_tag));
		params.add(new BasicNameValuePair("photo_private", photo_private));
		//params.add(new BasicNameValuePair("photo", photo));
		//POST("Album/setPhoto", params);
	}
	
	public void request_Album_delPhoto(String photo_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Album/delPhoto", params);
	}
	
	public void request_Album_modPhotoTag(String photo_srl, String photo_tag) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		params.add(new BasicNameValuePair("photo_tag", photo_tag));
		PUT("Album/modPhotoTag", params);
	}
	
	public void request_Album_setPhotoAlbum(String album_srl, String photo_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		POST("Album/setPhotoAlbum", params);
	}
	
	public void request_Album_setPhotoTimeline(String photo_srl, String timeline_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		POST("Album/setPhotoTimeline", params);
	}
	
	public void request_Album_delPhotoAlbum(String album_srl, String photo_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("album_srl", album_srl));
		params.add(new BasicNameValuePair("photo_srl", photo_srl));
		DELETE("Album/delPhotoAlbum", params);
	}
	
	public void request_Calender_setCalender(String cal_name, String org_srl, String member_srl, String class_srl, String cal_year, String cal_month, String cal_day, String cal_time, String cal_timestamp, String cal_type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cal_name", cal_name));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("cal_year", cal_year));
		params.add(new BasicNameValuePair("cal_month", cal_month));
		params.add(new BasicNameValuePair("cal_day", cal_day));
		params.add(new BasicNameValuePair("cal_time", cal_time));
		params.add(new BasicNameValuePair("cal_timestamp", cal_timestamp));
		params.add(new BasicNameValuePair("cal_type", cal_type));
		POST("Calender/setCalender", params);
	}
	
	public void request_Calender_delCalender(String cal_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cal_srl", cal_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Calender/delCalender", params);
	}
	
	public void request_Calender_getCalender(String cal_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cal_srl", cal_srl));
		GET("Calender/getCalender", params);
	}
	
	public void request_Calender_getCalenders(String org_srl, String year, String month) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("year", year));
		params.add(new BasicNameValuePair("month", month));
		GET("Calender/getCalenders", params);
	}
	
	public void request_Calender_getCalenders(String org_srl, String class_srl, String year, String month) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("year", year));
		params.add(new BasicNameValuePair("month", month));
		GET("Calender/getCalenders", params);
	}
	
	public void request_Calender_getCalenders(String org_srl, String class_srl, String member_srl, String year, String month) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("year", year));
		params.add(new BasicNameValuePair("month", month));
		GET("Calender/getCalenders", params);
	}
	
	public void request_Calender_checkCalender(String cal_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cal_srl", cal_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		POST("Calender/checkCalender", params);
	}
	
	public void request_Calender_getCheckCalender(String cal_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cal_srl", cal_srl));
		GET("Calender/getCheckCalender", params);
	}
	
	public void request_Class_setClass(String org_srl, String class_name) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_name", class_name));
		POST("Class/setClass", params);
	}
	
	public void request_Class_modClass(String class_srl, String org_srl, String class_name, String class_status) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_name", class_name));
		params.add(new BasicNameValuePair("class_status", class_status));
		PUT("Class/modClass", params);
	}
	
	public void request_Class_getClass(String org_srl, String class_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		GET("Class/getClass", params);
	}
	
	public void request_Class_getClasses(String org_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Class/getClasses", params);
	}
	
	public void request_Class_getClassTeacher(String org_srl, String class_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		GET("Class/getClassTeacher", params);
	}
	
	public void request_Class_getClassStudent(String org_srl, String class_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		GET("Class/getClassStudent", params);
	}
	
	public void request_Member_login(String member_email, String member_password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_email", member_email));
		params.add(new BasicNameValuePair("member_password", member_password));
		POST("Member/login", params);
	}
	
	public void request_Member_checkEmail(String member_email) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_email", member_email));
		POST("Member/checkEmail", params);
	}
	
	public void request_Member_addMember(String member_name, String member_nickname, String member_type, String org_srl, String member_email, String member_password, String member_device_type, String member_device_uuid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_name", member_name));
		params.add(new BasicNameValuePair("member_nickname", member_nickname));
		params.add(new BasicNameValuePair("member_type", member_type));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("member_email", member_email));
		params.add(new BasicNameValuePair("member_password", member_password));
		params.add(new BasicNameValuePair("member_device_type", member_device_type));
		params.add(new BasicNameValuePair("member_device_uuid", member_device_uuid));
		POST("Member/addMember", params);
	}
	
	public void request_Member_modMember(String member_srl, String member_name, String member_nickname, String member_org_srl, String member_email, String member_password, String member_device_type, String member_device_uuid, String member_enabled) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("member_name", member_name));
		params.add(new BasicNameValuePair("member_nickname", member_nickname));
		params.add(new BasicNameValuePair("member_org_srl", member_org_srl));
		params.add(new BasicNameValuePair("member_email", member_email));
		params.add(new BasicNameValuePair("member_password", member_password));
		params.add(new BasicNameValuePair("member_device_type", member_device_type));
		params.add(new BasicNameValuePair("member_device_uuid", member_device_uuid));
		params.add(new BasicNameValuePair("member_enabled", member_enabled));
		PUT("Member/modMember", params);
	}
	
	public void request_Member_delMember(String member_srl, String member_email, String member_password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("member_email", member_email));
		params.add(new BasicNameValuePair("member_password", member_password));
		DELETE("Member/delMember", params);
	}
	
	public void request_Member_getMember(String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		GET("Member/getMember", params);
	}
	
	public void request_Member_getMembers(String org_srl, int page, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("page", ""+page));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Member/getMembers", params);
	}
	
	public void request_Member_getMembers(String org_srl, String class_srl, int page, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("page", ""+page));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Member/getMembers", params);
	}
	
	public void request_Member_getMembers(String org_srl, String class_srl, String member_type, int page, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("member_type", member_type));
		params.add(new BasicNameValuePair("page", ""+page));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Member/getMembers", params);
	}
	
	public void request_Member_setTeacher(String member_srl, String org_srl, String org_teacher_auth_key, String class_srl, String shuttle_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("org_teacher_auth_key", org_teacher_auth_key));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		POST("Member/setTeacher", params);
	}
	
	public void request_Member_modTeacher(String member_srl, String teacher_srl, String class_srl, String shuttle_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("teacher_srl", member_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		PUT("Member/modTeacher", params);
	}
	
	public void request_Member_modStudent(String student_srl, String member_srl, String class_srl, String parent_srl, String teacher_srl, String shuttle_srl, String birthday) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("student_srl", student_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("parent_srl", parent_srl));
		params.add(new BasicNameValuePair("teacher_srl", teacher_srl));
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		params.add(new BasicNameValuePair("birthday", birthday));
		PUT("Member/modStudent", params);
	}
	
	
	public void request_Member_setStudent(String member_srl, String class_srl, String parent_srl, String teacher_srl, String shuttle_srl, String birthday) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("parent_srl", parent_srl));
		params.add(new BasicNameValuePair("teacher_srl", teacher_srl));
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		params.add(new BasicNameValuePair("birthday", birthday));
		POST("Member/setStudent", params);
	}
	
	public void request_Member_delStudent(String student_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("student_srl", student_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Member/delStudent", params);
	}
	
	public void request_Member_getParent(String parent_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("parent_srl", parent_srl));
		GET("Member/getParent", params);
	}
	
	public void request_Member_setParent(String member_srl, String org_srl, String student_parent_auth_key) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("student_parent_auth_key", student_parent_auth_key));
		POST("Member/setParent", params);
	}
	
	public void request_Member_setViewer(String member_srl, String org_srl, String parent_viewer_auth_key) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("parent_viewer_auth_key", parent_viewer_auth_key));
		POST("Member/setViewer", params);
	}
	
	public void request_Member_modMemberStatus(String member_srl, String member_status) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("member_status", member_status));
		PUT("Member/modMemberStatus", params);
	}
	
	public void request_Member_getParentStudents(String parent_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("parent_srl", parent_srl));
		GET("Member/getParentStudents", params);
	}
	
	public void request_Member_getFriends(String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		GET("Member/getFriends", params);
	}
	
	public void request_Member_setFriend(String origin_member_srl, String target_member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("origin_member_srl", origin_member_srl));
		params.add(new BasicNameValuePair("target_member_srl", target_member_srl));
		POST("Member/setFriend", params);
	}
	
	public void request_Member_getRecommendFriends(String member_srl, String org_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Member/getRecommendFriends", params);
	}
	
	public void request_Member_modFriendStatus(String friend_srl, String friend_status) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("friend_srl", friend_srl));
		params.add(new BasicNameValuePair("friend_status", friend_status));
		PUT("Member/modFriendStatus", params);
	}
	
	public void request_Mentor_setMentoringArticle(String category_srl, String mentoring_subject, String mentoring_text, String mentor_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("category_srl", category_srl));
		params.add(new BasicNameValuePair("mentoring_subject", mentoring_subject));
		params.add(new BasicNameValuePair("mentoring_text", mentoring_text));
		params.add(new BasicNameValuePair("mentor_srl", mentor_srl));
		POST("Mentor/setMentoringArticle", params);
	}
	
	public void request_Mentor_modMentoringArticle(String mentoring_srl, String category_srl, String mentoring_subject, String mentoring_text, String mentor_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("category_srl", category_srl));
		params.add(new BasicNameValuePair("mentoring_subject", mentoring_subject));
		params.add(new BasicNameValuePair("mentoring_text", mentoring_text));
		params.add(new BasicNameValuePair("mentor_srl", mentor_srl));
		PUT("Mentor/modMentoringArticle", params);
	}
	
	public void request_Mentor_delMentoringArticle(String mentoring_srl, String mentor_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("mentor_srl", mentor_srl));
		DELETE("Mentor/delMentoringArticle", params);
	}
	
	public void request_Mentor_getMentoringArticle(String mentoring_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		GET("Mentor/getMentoringArticle", params);
	}
	
	public void request_Mentor_getMentoringArticles(String mentoring_category, String order_by, String order_type, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_category", mentoring_category));
		params.add(new BasicNameValuePair("order_by", order_by));
		params.add(new BasicNameValuePair("order_type", order_type));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Mentor/getMentoringArticles", params);
	}
	
	public void request_Mentor_setMentoringArticleLikes(String mentoring_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		POST("Mentor/setMentoringArticleLikes", params);
	}
	
	public void request_Mentor_getMentor(String mentor_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentor_srl", mentor_srl));
		GET("Mentor/getMentor", params);
	}
	
	public void request_Mentor_setComment(String mentoring_srl, String member_srl, String comment_text) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("comment_text", comment_text));
		POST("Mentor/setComment", params);
	}
	
	public void request_Mentor_modComment(String mentoring_srl, String comment_srl, String member_srl, String comment_text) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("comment_srl", comment_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("comment_text", comment_text));
		PUT("Mentor/modComment", params);
	}
	
	public void request_Mentor_delComment(String comment_srl, String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment_srl", comment_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		DELETE("Mentor/delComment", params);
	}
	
	public void request_Mentor_getComments(String mentoring_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mentoring_srl", mentoring_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Mentor/getComments", params);
	}
	
	public void request_Mentor_getMentoringCategory(String member_type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_type", member_type));
		GET("Mentor/getMentoringCategory", params);
	}
	
	public void request_Organization_setOrganization(String member_srl, String org_name, String org_phone, String org_address) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_name", org_name));
		params.add(new BasicNameValuePair("org_phone", org_phone));
		params.add(new BasicNameValuePair("org_address", org_address));
		POST("Organization/setOrganization", params);
	}
	
	public void request_Organization_modOrganization(String member_srl, String org_srl, String org_name, String org_phone, String org_address, String org_status) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("org_name", org_name));
		params.add(new BasicNameValuePair("org_phone", org_phone));
		params.add(new BasicNameValuePair("org_address", org_address));
		params.add(new BasicNameValuePair("org_status", org_status));
		PUT("Organization/modOrganization", params);
	}
	
	public void request_Organization_getOrganization(String org_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		GET("Organization/getOrganization", params);
	}
	
	public void request_Organization_getOrganizations(int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Organization/getOrganizations", params);
	}
	
	public void request_Organization_getOrgTeachers(String org_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		GET("Organization/getOrgTeachers", params);
	}
	
	public void request_Organization_getOrgStudents(String org_srl, String class_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Organization/getOrgStudents", params);
	}
	
	public void request_Organization_getOrgParents(String org_srl, String class_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		params.add(new BasicNameValuePair("class_srl", class_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Organization/getOrgParents", params);
	}
	
	public void request_Point_getPoint(String member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		GET("Point/getPoint", params);
	}
	
	public void request_Point_setPoint(String member_srl, String point) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("point", point));
		POST("Point/setPoint", params);
	}
	
	public void request_Point_increasePoint(String member_srl, String point_amount) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("point_amount", point_amount));
		POST("Point/increasePoint", params);
	}
	
	public void request_Point_decreasePoint(String member_srl, String point_amount) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("point_amount", point_amount));
		POST("Point/decreasePoint", params);
	}
	
	public void request_Scrap_getScrap(String member_srl, String scrap_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("scrap_srl", scrap_srl));
		GET("Scrap/getScrap", params);
	}
	
	public void request_Scrap_getScraps(String member_srl, int index, int count, String scrap_type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		params.add(new BasicNameValuePair("scrap_type", scrap_type));
		GET("Scrap/getScraps", params);
	}
	
	public void request_Scrap_setScrap(String member_srl, String scrap_type, String scrap_target_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("scrap_type", scrap_type));
		params.add(new BasicNameValuePair("scrap_target_srl", scrap_target_srl));
		POST("Scrap/setScrap", params);
	}
	
	public void request_Scrap_delScrap(String member_srl, String scrap_srl, String scrap_target_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("scrap_srl", scrap_srl));
		params.add(new BasicNameValuePair("scrap_target_srl", scrap_target_srl));
		DELETE("Scrap/delScrap", params);
	}
	
	public void request_ServiceInfo_checkServerStatus() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		GET("ServiceInfo/checkServerStatus", params);
	}
	
	public void request_Service_notify_sendNotify(String member_srl, String target_member_srl, String notify_title, String notify_message, String notify_type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("target_member_srl", target_member_srl));
		params.add(new BasicNameValuePair("notify_title", notify_title));
		params.add(new BasicNameValuePair("notify_message", notify_message));
		params.add(new BasicNameValuePair("notify_type", notify_type));
		GET("Service/notify/sendNotify", params);
	}
	
	public void request_Shuttlebus_getShuttlebus(String shuttle_srl, String org_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		params.add(new BasicNameValuePair("org_srl", org_srl));
		GET("Shuttlebus/getShuttlebus", params);
	}
	
	public void request_Shuttlebus_getShuttlebuses(String org_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_srl", org_srl));
		GET("Shuttlebus/getShuttlebuses", params);
	}
	
	public void request_Shuttlebus_setNextBusStopSequence(String shuttle_srl, String teacher_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		params.add(new BasicNameValuePair("teacher_srl", teacher_srl));
		POST("Shuttlebus/setNextBusStopSequence", params);
	}
	
	public void request_Shuttlebus_getNextBusStopSequence(String shuttle_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("shuttle_srl", shuttle_srl));
		GET("Shuttlebus/getNextBusStopSequence", params);
	}
	
	public void request_Timeline_getTimelineMessages(String member_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Timeline/getTimelineMessages", params);
	}
	
	public void request_Timeline_getTimelineMessage(String member_srl, String timeline_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		GET("Timeline/getTimelineMessage", params);
	}
	
	public void request_Timeline_setTimelineMessage(String member_srl, String timeline_type, String timeline_target_srl, String timeline_target_member_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_title", ""));
		params.add(new BasicNameValuePair("timeline_type", timeline_type));
		params.add(new BasicNameValuePair("timeline_message", ""));
		params.add(new BasicNameValuePair("timeline_target_srl", timeline_target_srl));
		params.add(new BasicNameValuePair("timeline_target_member_srl", timeline_target_member_srl));
		POST("Timeline/setTimelineMessage", params);
	}
	
	public void request_Timeline_delTimelineMessage(String member_srl, String timeline_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		DELETE("Timeline/delTimelineMessage", params);
	}
	
	public void request_Timeline_setTimelineComment(String timeline_srl, String member_srl, String tcomment_message) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("tcomment_message", tcomment_message));
		POST("Timeline/setTimelineComment", params);
	}
	
	public void request_Timeline_getTimelineComments(String timeline_srl, int index, int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		params.add(new BasicNameValuePair("index", ""+index));
		params.add(new BasicNameValuePair("count", ""+count));
		GET("Timeline/getTimelineComments", params);
	}
	
	public void request_Timeline_delTimelineComment(String member_srl, String timeline_srl, String tcomment_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		params.add(new BasicNameValuePair("tcomment_srl", tcomment_srl));
		DELETE("Timeline/delTimelineComment", params);
	}
	
	public void request_Timeline_setLike(String member_srl, String timeline_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		POST("Timeline/setLike", params);
	}
	
	public void request_Timeline_delLike(String member_srl, String timeline_srl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("member_srl", member_srl));
		params.add(new BasicNameValuePair("timeline_srl", timeline_srl));
		DELETE("Timeline/delLike", params);
	}
}
