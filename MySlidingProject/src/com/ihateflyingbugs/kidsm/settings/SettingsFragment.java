package com.ihateflyingbugs.kidsm.settings;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.R.id;
import com.ihateflyingbugs.kidsm.R.layout;
import com.ihateflyingbugs.kidsm.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsFragment extends NetworkFragment {
	LayoutInflater inflater;
	View layout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_settings, container, false);
		
		String[] settingModeArray = getActivity().getResources().getStringArray(R.array.setting_mode);
		ArrayList<String> settingModeList = new ArrayList<String>();
		for(int i = 0; i < settingModeArray.length; i++)
			settingModeList.add(settingModeArray[i]);
		ArrayAdapter<String> settingModeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, settingModeList);
		ListView settingList = (ListView) layout.findViewById(R.id.settings_list);
		settingList.setAdapter(settingModeAdapter);
		settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kidsmentory1@gmail.com"});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");
				emailIntent.setType("message/rfc822");
				startActivity(Intent.createChooser(emailIntent, "이메일 어플리케이션 선택"));
			}
		});
		
		return layout;
	}
}
