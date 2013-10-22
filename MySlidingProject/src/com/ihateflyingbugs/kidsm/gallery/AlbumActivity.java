package com.ihateflyingbugs.kidsm.gallery;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.ExpandableHeightGridView;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.WrappingSlidingDrawer;
import com.ihateflyingbugs.kidsm.gallery.Album.ALBUMTYPE;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.showimage.ShowImageActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity;

public class AlbumActivity extends NetworkActivity {
	ExpandableHeightGridView gridView;
	AlbumAdapter adapter;
	int albumMode;
	String prevTitle;
	int selectedPhotoCounter;
	WrappingSlidingDrawer drawer;
	Menu menu;
	
	ExpandableHeightGridView albumGridView;
	GalleryAdapter albumAdapter;
	Album selectedAlbum;
	ArrayList<Album> albumList;
	ArrayList<Album> newAlbumList;
	ViewFlipper viewFlipper;
	int sizeOfView;
	ArrayList<Photo> photoList;
	int requestCounter;
	Album modifiedData;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		
		selectedAlbum = getIntent().getParcelableExtra("album");
		newAlbumList = new ArrayList<Album>();
		albumList = getIntent().getParcelableArrayListExtra("albumList");
		int indexForRemove = -1;
		for(int i = 0; i < albumList.size(); i++) {
			albumList.get(i).needSetting = false;
			if(selectedAlbum.album_srl.equals(albumList.get(i).album_srl))
				indexForRemove = i;
		}
		if( indexForRemove != -1)
			albumList.remove(indexForRemove);
		setTitle(selectedAlbum.getTitle());
		
		photoList = new ArrayList<Photo>();
		for(int i = 0; i < selectedAlbum.photoList.size(); i++) {
			photoList.add(selectedAlbum.photoList.get(selectedAlbum.photoList.size()-1-i));
		}
		
		gridView = (ExpandableHeightGridView) findViewById(R.id.gallery_photolist);
        adapter = new AlbumAdapter(photoList, this, 3);
        gridView.setExpanded(true);
        gridView.setAdapter(adapter);
        
        drawer = (WrappingSlidingDrawer)findViewById(R.id.album_drawer);
        drawer.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
        	
        });
        
        albumMode = 0;
        selectedPhotoCounter = 0;
        
		albumGridView = (ExpandableHeightGridView) findViewById(R.id.movephoto_albumlist);
		
		int numOfColumn = 3;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		sizeOfView = (displaymetrics.widthPixels - 10*(numOfColumn+1))/numOfColumn;
		
		albumAdapter = new GalleryAdapter(albumList, this, sizeOfView);
		albumGridView.setExpanded(true);
		albumGridView.setAdapter(albumAdapter);
		viewFlipper = (ViewFlipper)findViewById(R.id.album_flipper);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if( selectedAlbum.type == ALBUMTYPE.NORMAL) {
			getMenuInflater().inflate(R.menu.photo_multiselect, menu);
			this.menu = menu;
		}
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		if( albumMode == 2 && keyCode == KeyEvent.KEYCODE_BACK) {
			getActionBar().setTitle(R.string.select_photo);
			MenuItem item = menu.findItem(R.id.select_or_cancel);
	        item.setTitle(R.string.cancel);
	        getActionBar().setDisplayShowHomeEnabled(false);
			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
			viewFlipper.showPrevious();
			albumMode = 1;
        	return true;
        }
		else
			return super.onKeyDown(keyCode, event);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case android.R.id.home:
			switch( albumMode ) {
			case 2:
				getActionBar().setTitle(R.string.select_photo);
				menu.findItem(R.id.select_or_cancel).setTitle(R.string.cancel);
		        getActionBar().setDisplayShowHomeEnabled(false);
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
				viewFlipper.showPrevious();
				albumMode = 1;
				break;
			default:
				finish();
				break;
			}
			return true;
		case R.id.select_or_cancel:
				switch(albumMode) {
				case 2:
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
					viewFlipper.showPrevious();
					albumMode = 0;
				case 0:
					prevTitle = getActionBar().getTitle().toString();
					getActionBar().setTitle(R.string.select_photo);
			        item.setTitle(R.string.cancel);
			        getActionBar().setDisplayShowHomeEnabled(false);
			        for(int i = 0; i < adapter.getCount(); i++) {
			        	gridView.findViewWithTag(i).setVisibility(View.INVISIBLE); 
			        	gridView.findViewWithTag(-i-1).setVisibility(View.VISIBLE);
			        }
					break;
				case 1:
					getActionBar().setTitle(prevTitle);
			        item.setTitle(R.string.select);
			        getActionBar().setDisplayShowHomeEnabled(true);
			        for(int i = 0; i < adapter.getCount(); i++) {
			        	gridView.findViewWithTag(i).setVisibility(View.VISIBLE);
			        	gridView.findViewWithTag(-i-1).setVisibility(View.INVISIBLE);
			        }
			        break;
				}
				albumMode = (albumMode+1)%2;
			return true;
		}
		return false;
	}

	public void OnSelectImage(View v) {
		switch(albumMode) {
		case 0:
			Intent intent = new Intent(this, ShowImageActivity.class);
			intent.putExtra("url", v.getTag().toString());
			startActivity(intent);
			break;
		case 1:
			CheckBox cb = (CheckBox)v;
			int photoIndex = -((Integer)cb.getTag() + 1);
			if(photoList.get(photoIndex).isSelected == false && cb.isChecked()) 
				selectedPhotoCounter++;
			else if( photoList.get(photoIndex).isSelected == true && cb.isChecked() == false )
				selectedPhotoCounter--;
			photoList.get(photoIndex).isSelected = cb.isChecked();
			
			if( drawer.isOpened() == false && selectedPhotoCounter > 0 )
				drawer.animateOpen();
			else if( drawer.isOpened() == true && selectedPhotoCounter == 0 )
				drawer.animateClose();
			break;
		}
	}
	
	public void OnMovePhoto(View v) {
		getActionBar().setTitle(R.string.select_album);
		MenuItem item = menu.findItem(R.id.select_or_cancel);
        item.setTitle(R.string.close);
        getActionBar().setDisplayShowHomeEnabled(true);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_left));
		viewFlipper.showNext();
		albumMode = 2;
	}
	
	public void OnDeletePhoto(View v) {
		new AlertDialog.Builder(this)
		.setMessage("선택한 사진을 삭제하겠습니까?")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				requestCounter = 0;
				modifiedData = new Album(ALBUMTYPE.DELETED, selectedAlbum.album_srl, "", "", "", "", "", "");
				ArrayList<String> photo_srl_list = new ArrayList<String>();
				for(int i = 0; i < photoList.size(); i++) {
					if( photoList.get(i).isSelected ) {
						photo_srl_list.add(photoList.get(i).photo_srl);
						requestCounter++;
					}
				}
				for(int i = 0; i < photo_srl_list.size(); i++) {
					request_Album_delPhotoAlbum(selectedAlbum.album_srl, photo_srl_list.get(i));
				}
			}
		})
		.setNegativeButton("취소", null)
		.show();
	}
	
	public void OnNewAlbum(View v) {
		final EditText txt = new EditText(this);
		
		new AlertDialog.Builder(this)
		.setView(txt)
		.setMessage("새 사진첩 이름을 입력하세요")
		.setNegativeButton("취소", null)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				albumList.add(new Album(1, txt.getText().toString()));
//		        albumAdapter = new GalleryAdapter(albumList, AlbumActivity.this, sizeOfView);
//		        albumGridView.setExpanded(true);
//		        albumGridView.setAdapter(albumAdapter);
				
				request_Album_setAlbum(txt.getText().toString(), SlidingMenuMaker.getProfile().member_srl, "N");
//				getActionBar().setTitle(txt.getText().toString());
//				menu.findItem(R.id.select_or_cancel).setTitle(R.string.select);
//		        getActionBar().setDisplayShowHomeEnabled(true);
//		        for(int i = 0; i < adapter.getCount(); i++) {
//		        	gridView.findViewWithTag(i).setVisibility(View.VISIBLE);
//		        	gridView.findViewWithTag(-i-1).setVisibility(View.INVISIBLE);
//		        	((CheckBox)gridView.findViewWithTag(-i-1)).setChecked(false);
//		        	photoList.get(i).isSelected = false;
//		        	selectedPhotoCounter = 0;
//		        }
//				drawer.animateClose();
//				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(AlbumActivity.this, R.anim.viewin_right));
//				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(AlbumActivity.this, R.anim.viewout_right));
//				viewFlipper.showPrevious();
//				albumMode = 0;
			}
		})
		.show();
	}
	
	public void OnSeeAlbum(View v) {
//		getActionBar().setTitle(((Button)v).getText());
//		menu.findItem(R.id.select_or_cancel).setTitle(R.string.select);
//        getActionBar().setDisplayShowHomeEnabled(true);
//        for(int i = 0; i < adapter.getCount(); i++) {
//        	gridView.findViewWithTag(i).setVisibility(View.VISIBLE);
//        	gridView.findViewWithTag(-i-1).setVisibility(View.INVISIBLE);
//        	((CheckBox)gridView.findViewWithTag(-i-1)).setChecked(false);
//        	photoList.get(i).isSelected = false;
//        	selectedPhotoCounter = 0;
//        }
//		drawer.animateClose();
//		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
//		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
//		viewFlipper.showPrevious();
//		albumMode = 0;
		requestCounter = 0;
		modifiedData = new Album(ALBUMTYPE.MODIFIED, v.getTag().toString(), "", "", "", "", "", "");
		ArrayList<String> photo_srl_list = new ArrayList<String>();
		for(int i = 0; i < photoList.size(); i++) {
			if( photoList.get(i).isSelected ) {
				photo_srl_list.add(photoList.get(i).photo_srl);
				requestCounter++;
			}
		}
		for(int i = 0; i < photo_srl_list.size(); i++) {
			request_Album_setPhotoAlbum(v.getTag().toString(), photo_srl_list.get(i));
		}
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Album/setAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String album_srl = jsonObj.getString("album_srl");
					String album_member_srl = jsonObj.getString("album_member_srl");
					final String album_name = jsonObj.getString("album_name");
					String album_type = jsonObj.getString("album_type");
					String album_created = jsonObj.getString("album_created");
					String album_updated = jsonObj.getString("album_updated");
					String album_count = jsonObj.getString("album_count");
					albumList.add(new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
					newAlbumList.add(new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	albumAdapter.notifyDataSetChanged();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Album/setPhotoAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
					String photo_member_srl = jsonObj.getString("photo_member_srl");
					String photo_album_srl = jsonObj.getString("photo_album_srl");
					String photo_tag = jsonObj.getString("photo_tag");
					String photo_path = jsonObj.getString("photo_path");
					String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
					String photo_like = jsonObj.getString("photo_like");
					String photo_private = jsonObj.getString("photo_private");
					String photo_created = jsonObj.getString("photo_created");
					String photo_updated = jsonObj.getString("photo_updated");
					this.request_Album_delPhotoAlbum(selectedAlbum.album_srl, photo_srl);
				}
				else if(uri.equals("Album/delPhotoAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
					String photo_member_srl = jsonObj.getString("photo_member_srl");
					String photo_album_srl = jsonObj.getString("photo_album_srl");
					String photo_timeline_srl = jsonObj.getString("photo_timeline_srl");
					String photo_tag = jsonObj.getString("photo_tag");
					String photo_path = jsonObj.getString("photo_path");
					String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
					String photo_like = jsonObj.getString("photo_like");
					String photo_private = jsonObj.getString("photo_private");
					String photo_created = jsonObj.getString("photo_created");
					String photo_updated = jsonObj.getString("photo_updated");
					modifiedData.photoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
					if(--requestCounter == 0) {
						Intent intent = new Intent();
						if(modifiedData.type == ALBUMTYPE.MODIFIED)
							intent.putExtra("modified_album_srl", selectedAlbum.album_srl);
						intent.putExtra("modifiedData", modifiedData);
						intent.putExtra("newAlbumList", newAlbumList);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
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
}
