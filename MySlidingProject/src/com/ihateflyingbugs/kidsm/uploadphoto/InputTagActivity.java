package com.ihateflyingbugs.kidsm.uploadphoto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.ihateflyingbugs.kidsm.R;

public class InputTagActivity extends Activity {
	ArrayList<InputTag> tagList;
	ArrayList<InputTag> taggedList;
	InputTagAdapter adapter;
	ListView listView;
	boolean checkListenerOnlyOnce;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadphoto_taglist);
        getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		
//		if( getIntent().getParcelableArrayListExtra("tagList") == null ) {
//			taggedList = new ArrayList<InputTag>();
//		}
//		else {
//			taggedList = getIntent().getParcelableArrayListExtra("tagList");
//			for(int i = 0; i < tagList.size(); i++) {
//				InputTag tag = tagList.get(i);
//				for(int j = 0; j < taggedList.size(); j++){
//					if(taggedList.get(j).getName().equals(tag.getName())) {
//						tag.isTagged = true;
//						tagList.set(i, tag);
//					}
//				}
//			}
//			if(taggedList.size() == tagList.size() - 1 ) {
//				InputTag tag = tagList.get(0);
//				tag.isTagged = true;
//				tagList.set(0, tag);
//			}
//		}
		adapter = new InputTagAdapter(this, tagList);
		adapter.notifyDataSetChanged();
		showTaggedList();
		listView = (ListView)findViewById(R.id.uploadphoto_taglist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
				onUpdate(position, !((CheckBox)v.findViewById(R.id.tag_check)).isChecked());
			}
			
		});
	}
	
	public void OnButtonCheck(View v) {
		CheckBox cb = (CheckBox)v;
		boolean isChecked = cb.isChecked();
		int position = (Integer) cb.getTag();
		onUpdate(position, isChecked);
	}
	
	public void onUpdate(final int position, boolean checker) {
		if(position == 0) {
			for(int i = 0; i < adapter.getCount(); i++) {
				InputTag inputTag = adapter.tagList.get(i);
				inputTag.isTagged = checker;
				adapter.tagList.set(i, inputTag);
				if(checker && i > 0 ) {
					boolean isExist = false;
					for(int j = 0; j < taggedList.size(); j++) {
						//deprecate
				        if(taggedList.get(j).getName().equals(tagList.get(i).getName()))
				        	isExist = true;
					}
					if(isExist == false)
					    taggedList.add(tagList.get(i));
			    }
			    else {
				    if( i == 0 ) {
					    taggedList.clear();
					    taggedList = new ArrayList<InputTag>();
				    }
			    }
			}
		}
		else {
			InputTag inputTag = adapter.tagList.get(position);
			inputTag.isTagged = checker;
			adapter.tagList.set(position, inputTag);
			if(checker) {
				taggedList.add(tagList.get(position));
			}
			else {
				for(int i = 0; i < taggedList.size(); i++) {
					//deprecate
					if(taggedList.get(i).getName().equals(tagList.get(position).getName())) {
						taggedList.remove(i);
						break;
					}
				}
			}
		}
		adapter.notifyDataSetChanged();
		showTaggedList();
	}
		
	void showTaggedList() {
		EditText et = (EditText)findViewById(R.id.uploadphoto_taggedlist);
		String taggedName = "";
		for(int i = 0; i < taggedList.size(); i++) {
			taggedName += taggedList.get(i).getName();
			if( i != taggedList.size()-1 )
				taggedName += ", ";
		} 
		et.setText(taggedName);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uploadphoto_tagregister, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent data;
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.uploadphoto_tag_register:
			data = new Intent();
			data.putParcelableArrayListExtra("tagList", (ArrayList<? extends Parcelable>) taggedList);
			setResult(Activity.RESULT_OK, data);
			finish();
			return true;
		}
		return false;
	}
}
