package com.ihateflyingbugs.kidsm.mentory;

import com.ihateflyingbugs.kidsm.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SeeMentoryActivity extends Activity {
	WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seementory);
		
		webView = (WebView)findViewById(R.id.mentory_web);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.loadUrl(getIntent().getStringExtra("url"));
		webView.setWebViewClient(new WebViewClientClass());
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
            finish();
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }
     
    private class WebViewClientClass extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
}
