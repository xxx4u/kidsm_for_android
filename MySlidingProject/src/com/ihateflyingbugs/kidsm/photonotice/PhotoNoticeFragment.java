package com.ihateflyingbugs.kidsm.photonotice;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PhotoNoticeFragment extends NetworkFragment{
	LayoutInflater inflater;
	View layout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null) {
			return layout;
		}
		
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_photo, container, false);
		
		auth_key = MainActivity.auth_key;
		
		return layout;
	}
		
	private void addPhotoNotices() {
//		for(int i = 0; i < 10; i++ ) {
//			RelativeLayout linear1 = (RelativeLayout)View.inflate(this, R.layout.photonotice_textonly, null);
//			LinearLayout svw = (LinearLayout)findViewById(R.id.photonotice);
//			svw.addView(linear1);
//		}
	}
}
