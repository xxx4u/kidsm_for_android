package com.ihateflyingbugs.kidsm;

import com.localytics.android.LocalyticsSession;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

public class DoctypeErrorActivity extends Activity {
	WebView webView;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctypeerror);
		webView = (WebView)findViewById(R.id.error_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		String error = getIntent().getStringExtra("error");
		String uri = getIntent().getStringExtra("uri");
		String params = getIntent().getStringExtra("params");
		webView.loadDataWithBaseURL(null, error, "text/html", "utf-8", null);
		webView.setWebViewClient(new WebViewClientClass());
		
		TextView textView = (TextView)findViewById(R.id.error_text);
		textView.setText("uri:"+uri+"\n"+"params:"+params+"\n백 버튼을 누르면 화면으로 돌아갑니다.");
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
	
    public class WebViewClientClass extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
}
