package com.ihateflyingbugs.kidsm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OverlayFragment extends NetworkFragment {
	LayoutInflater inflater;
	View layout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null) {
			return layout;
		}
		
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.overlay, container, false);
		
		auth_key = MainActivity.auth_key;
		
		return layout;
	}
}
