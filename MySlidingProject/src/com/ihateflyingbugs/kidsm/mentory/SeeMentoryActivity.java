package com.ihateflyingbugs.kidsm.mentory;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.localytics.android.LocalyticsSession;

public class SeeMentoryActivity extends NetworkActivity {
	WebView webView;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seementory);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		webView = (WebView)findViewById(R.id.mentory_web);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		String content = getIntent().getStringExtra("mentoring_text");
		String subject = getIntent().getStringExtra("mentoring_subject");
		Log.d("SeeMentory", content);
		
		setTitle(subject);
		
		webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
		//webView.loadUrl(getIntent().getStringExtra("mentoring_srl"));
		webView.setWebViewClient(new WebViewClientClass());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
            finish();
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	if( SlidingMenuMaker.getProfile().member_type.equals("P") == false )
    		getMenuInflater().inflate(R.menu.recommend, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case android.R.id.home:
	        finish();
			return true;
		case R.id.recommend:
			String mentoring_srl = getIntent().getStringExtra("mentoring_srl");
			if( mentoring_srl != null && mentoring_srl.isEmpty() == false )
				this.request_Mentor_broadMentoringArticle(SlidingMenuMaker.getProfile().member_srl, mentoring_srl);
			return true;
		}
		return false;
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			if(response.startsWith("<!DOCTYPE html>")) {
				return;
			}
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Timeline/broadTimelineMessage")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									Toast.makeText(SeeMentoryActivity.this, "추천이 완료되었습니다", Toast.LENGTH_SHORT).show();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Mentor/broadMentoringArticle")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									Toast.makeText(SeeMentoryActivity.this, "추천이 완료되었습니다", Toast.LENGTH_SHORT).show();
					            }
					        });
					    }
					}).start();
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
    
   public class WebViewClientClass extends WebViewClient { 
       @Override
       public boolean shouldOverrideUrlLoading(WebView view, String url) { 
           view.loadUrl(url); 
           return true; 
       } 
   }
}
