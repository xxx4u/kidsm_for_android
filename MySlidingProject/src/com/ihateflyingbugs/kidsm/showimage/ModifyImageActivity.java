package com.ihateflyingbugs.kidsm.showimage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.gallery.AlbumActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTagActivity;
import com.localytics.android.LocalyticsSession;

public class ModifyImageActivity extends Activity {
	ImageView image;
	String filepath;
	ArrayList<InputTag> tagList;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_modifyimage);
//        getActionBar().setHomeButtonEnabled(true);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		tagList = new ArrayList<InputTag>();
//		
//		try {
//			filepath = getIntent().getStringExtra("filepath");
//	        image = (ImageView)findViewById(R.id.modifyphoto_selected);
//	        //image.setImageBitmap(readBitmap(Uri.parse(filepath)));
//			
//		} catch(Exception e) {;}
//
//		final ArrayList<String> albumList = new ArrayList<String>();
//		albumList.add("전체보기");
//		albumList.add("즐겨찾기");
//		albumList.add("새 앨범 만들기");
//		final ArrayAdapter<String> albumListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumList);
//		
//		final Spinner spinner = (Spinner)findViewById(R.id.modifyphoto_selectalbum);
//		spinner.setAdapter(albumListAdapter);
//		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View v, final int position, long id) {
//				if( position == spinner.getAdapter().getCount()-1 ) {
//					final EditText txt = new EditText(ModifyImageActivity.this);
//					
//					new AlertDialog.Builder(ModifyImageActivity.this)
//					.setView(txt)
//					.setMessage("새 사진첩 이름을 입력하세요")
//					.setNegativeButton("취소", null)
//					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							albumList.add(position, txt.getText().toString());
//							albumListAdapter.notifyDataSetChanged();
//							spinner.setSelection(position);
//						}
//					})
//					.show();
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			switch(requestCode) {
			case 1:
				//fileIndex = data.getIntExtra("index", 0);
				break;
			case 2:
				// tagList = data.getParcelableArrayListExtra("tagList");
				showTaggedList();
				break;
			}
		}
	}
	
	void showTaggedList() {
//		EditText et = (EditText)findViewById(R.id.modifyphoto_tag);
//		String taggedName = "";
//		if(tagList != null) {
//			for(int i = 0; i < tagList.size(); i++) {
//				taggedName += tagList.get(i).getName();
//				if( i != tagList.size()-1 )
//					taggedName += ", ";
//			}
//		}
//		et.setText(taggedName);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modifyphoto_register, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.modifyphoto_register:
			intent = new Intent(this, AlbumActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	public void OnTagClick(final View v) {
		Intent intent = new Intent(this, InputTagActivity.class);
		intent.putParcelableArrayListExtra("tagList", (ArrayList<? extends Parcelable>) tagList);
		startActivityForResult(intent, 2);
	}
	
	public void OnDeletePhoto(final View v) {
		new AlertDialog.Builder(this)
		.setMessage("사진을 삭제합니다")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent data = new Intent();
				data.putExtra("result", 1);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
			
		})
		.setNegativeButton("취소", null)
		.show();
	}
	
	public Bitmap readBitmap(Uri selectedImage) { 
		Bitmap bm = null; 
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inSampleSize = 5; 
		AssetFileDescriptor fileDescriptor =null; 
		try { 
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r"); 
		} 
		catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} 
		finally{ 
			try { 
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options); 
				fileDescriptor.close(); 
			} 
			catch (IOException e) { 
				e.printStackTrace(); 
			} 
		} 
		return bm; 
	} 
}
