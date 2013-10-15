package com.ihateflyingbugs.kidsm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class NetworkActivityWithSliding extends SlidingActivity {
	public static HashMap<String, String> results;
	public String url;
	
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
		return query;
	}
	public String GET(final String uri, final List<NameValuePair> params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = "";
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url+uri+makeQuery(params));
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
		// override it plz.
	}
}
