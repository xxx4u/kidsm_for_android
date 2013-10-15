package com.ihateflyingbugs.kidsm.uploadphoto;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.gallery.Album;
import com.ihateflyingbugs.kidsm.gallery.AlbumActivity;
import com.ihateflyingbugs.kidsm.gallery.GalleryFragment;
import com.ihateflyingbugs.kidsm.gallery.Photo;
import com.ihateflyingbugs.kidsm.gallery.Album.ALBUMTYPE;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.showimage.ShowUploadImageListActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

class UploadPhoto {
	String filepath;
	ArrayList<InputTag> tagList;
	InputTagAdapter adapter;
	ListView listView;
	String tagValue;
	
	UploadPhoto(String filepath) {
		this.filepath = filepath;
	}
}

public class UploadPhotoActivity extends NetworkActivity {
	public enum TAGMODE {
		SEE_ALL_FRIEND,
		SEE_TAGGED_FRIEND,
		SEE_PRIVATE
	}
	ImageView image;
	//ArrayList<String> filePathList;
	ArrayList<UploadPhoto> photoList;
	int photoIndex;
	ViewFlipper viewFlipper;
	float prevTouchX;
	boolean haveFlipChance;
	boolean isFling;
	boolean isTagging;
	int photoMode;
	TAGMODE tagMode;
	int sendCounter;
	ArrayList<Album> albumList;
	ArrayList<Album> newAlbumList;
	AlbumNameAdapter albumListAdapter;
	Spinner spinner;
	String current_album_srl;
	//String tagValue;
	ArrayList<Photo> uploadedPhotoList;
	ArrayList<InputTag> tagMemberList;
	int requestGetClassStudentCounter;
	int requestGetMemberCounter;
	ImageLoader imageLoader;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadphoto);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_function_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		
		sendCounter = 0;
		photoList = new ArrayList<UploadPhoto>();
		viewFlipper = (ViewFlipper)findViewById(R.id.uploadphoto_photolist);
		RelativeLayout linear;
		
		photoMode = getIntent().getIntExtra("mode", -1);
		tagMode = TAGMODE.SEE_ALL_FRIEND;
		
		uploadedPhotoList = new ArrayList<Photo>();
		
		tagMemberList = new ArrayList<InputTag>();
		requestTagData();
		
		imageLoader = new ImageLoader(this, R.drawable.photo_in_album_default);
		
		UploadPhoto photo;
        switch(photoMode) {
        case 0:
        	linear = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.uploadphoto_photo, null);
        	
        	photo = new UploadPhoto(getIntent().getStringExtra("filePath"));
        	photo.tagValue = "";
        	photo.tagList = new ArrayList<InputTag>();
        	photo.tagList.add(new InputTag("", "전체보기"));
        	photo.adapter = new InputTagAdapter(this, photo.tagList);
        	photo.listView = (ListView) linear.findViewById(R.id.uploadphoto_taglist);
        	photo.listView.setAdapter(photo.adapter);
        	photo.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
    				onUpdate(Integer.parseInt(((CheckBox)v.findViewById(R.id.tag_check)).getTag().toString()), !((CheckBox)v.findViewById(R.id.tag_check)).isChecked());
    			}
    			
    		});
        	photoList.add(photo);
        	
        	
	        image = (ImageView)linear.findViewById(R.id.uploadphoto_selected);
	        //imageLoader.DisplayImage(photoList.get(0).filepath, image);
	        BitmapFactory.Options opt = new BitmapFactory.Options();
	        imageLoader.getBitmap(photoList.get(0).filepath);
        	image.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0).filepath, opt));
	        linear.setTag(photoList.get(0).filepath);
	        viewFlipper.addView(linear);
        	break;
        case 1:
			try {
				ArrayList<String> filePathList = getIntent().getStringArrayListExtra("filePath");
				for(int i = 0; i < filePathList.size(); i++) {
					linear = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.uploadphoto_photo, null);
					
					photo = new UploadPhoto(filePathList.get(i));
		        	photo.tagValue = "";
		        	photo.tagList = new ArrayList<InputTag>();
		        	photo.tagList.add(new InputTag("", "전체보기"));
		        	photo.adapter = new InputTagAdapter(this, photo.tagList);
		        	photo.listView = (ListView) linear.findViewById(R.id.uploadphoto_taglist);
		        	photo.listView.setAdapter(photo.adapter);
		        	photo.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    			@Override
		    			public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
		    				onUpdate(Integer.parseInt(((CheckBox)v.findViewById(R.id.tag_check)).getTag().toString()), !((CheckBox)v.findViewById(R.id.tag_check)).isChecked());
		    			}
		    			
		    		});
					photoList.add(photo);
					Bitmap bm = readBitmap(Uri.parse(photoList.get(i).filepath));
			        image = (ImageView)linear.findViewById(R.id.uploadphoto_selected);
			        image.setImageBitmap(bm);
			        //bm.recycle();
			        linear.setTag(photoList.get(i).filepath);
			        viewFlipper.addView(linear);
				}
				
			} catch(Exception e) {;}
        	break;
        }
        newAlbumList = new ArrayList<Album>();
        albumList = getIntent().getParcelableArrayListExtra("albumList");
        albumList.add(new Album(ALBUMTYPE.NEW, "", "", getResources().getString(R.string.make_new_album), "", "", "", ""));
		albumListAdapter = new AlbumNameAdapter(this, albumList);
		current_album_srl = albumList.get(0).album_srl;
		spinner = (Spinner)findViewById(R.id.uploadphoto_selectalbum);
		spinner.setAdapter(albumListAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, final int position, long id) {
				String album_srl = v.getTag().toString();
				current_album_srl = album_srl;
				if( album_srl.isEmpty() ) {
					final EditText txt = new EditText(UploadPhotoActivity.this);
					
					new AlertDialog.Builder(UploadPhotoActivity.this)
					.setView(txt)
					.setMessage("새 사진첩 이름을 입력하세요")
					.setNegativeButton("취소", null)
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							request_Album_setAlbum(txt.getText().toString(), SlidingMenuMaker.getProfile().member_srl, "N");
						}
					})
					.show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		RadioGroup tagmode = (RadioGroup) findViewById(R.id.uploadphoto_tagmode);
		final RadioButton btn1 = (RadioButton) tagmode.findViewById(R.id.uploadphoto_tagmode_all);
		final RadioButton btn2 = (RadioButton) tagmode.findViewById(R.id.uploadphoto_tagmode_tagged);
		final RadioButton btn3 = (RadioButton) tagmode.findViewById(R.id.uploadphoto_tagmode_private);
		tagmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.BLACK);
				btn3.setTextColor(Color.BLACK);
				RadioButton btn = (RadioButton) group.findViewById(checkedId);
				btn.setTextColor(Color.parseColor("#63C3B1"));
				
				View layout = findViewById(R.id.uploadphoto_tagstatepage);
				switch(checkedId) {
				case R.id.uploadphoto_tagmode_all:
					tagMode = TAGMODE.SEE_ALL_FRIEND;
					layout.setVisibility(View.INVISIBLE);
					OnFinishTagging(null);
					break;
				case R.id.uploadphoto_tagmode_tagged:
					tagMode = TAGMODE.SEE_TAGGED_FRIEND;
					layout.setVisibility(View.VISIBLE);
					break;
				case R.id.uploadphoto_tagmode_private:
					tagMode = TAGMODE.SEE_PRIVATE;
					layout.setVisibility(View.INVISIBLE);
					OnFinishTagging(null);
					break;
				}
			}
			
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			switch(requestCode) {
			case 1:
				//fileIndex = data.getIntExtra("index", 0);
				break;
			}
		}
	}
	
	private void setTagMemberData() { 
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	for(int i = 0; i < photoList.size(); i++) {
		            		photoList.get(i).tagValue = "";
		        			photoList.get(i).tagList.clear();
		        			photoList.get(i).tagList.add(new InputTag("", "전체보기"));
		        			for(int j = 0 ; j < tagMemberList.size(); j++) {
		        				photoList.get(i).tagList.add(new InputTag(tagMemberList.get(j)));
		        			}
		        			photoList.get(i).adapter.notifyDataSetChanged();
		        		}
		            }
		        });
		    }
		}).start();
	}
	
	private void requestTagData() {
		Profile profile = SlidingMenuMaker.getProfile();
		tagMemberList.clear();
		requestGetClassStudentCounter = 0;
		requestGetMemberCounter = 0;
		
		switch(profile.member_type.charAt(0)) {
		case 'P':
			tagMemberList.add(new InputTag(profile.org_manager_member_srl, profile.org_manager_name + " 원장선생님"));
			for(int i = 0; i < profile.getCurrentChildren().teacherList.size(); i++)
				tagMemberList.add(new InputTag(profile.getCurrentChildren().teacherList.get(i).teacher_member_srl, profile.getCurrentChildren().teacherList.get(i).teacher_name + " 선생님"));
			this.request_Member_getFriends(profile.getCurrentChildren().student_member_srl);
			break;
		case 'T':
			tagMemberList.add(new InputTag(profile.org_manager_member_srl, profile.org_manager_name + " 원장선생님"));
			this.request_Class_getClassStudent(profile.member_org_srl, profile.getCurrentClass().getClass_srl());
			break;
		case 'M':
			for(int i = 0; i < profile.classList.size()-1; i++) {
				for(int j = 0; j < profile.classList.get(i).getTeacherList().size(); j++)
					tagMemberList.add(new InputTag(profile.classList.get(i).getTeacherList().get(j).teacher_member_srl, profile.classList.get(i).getTeacherList().get(j).teacher_name + " 선생님"));
				this.request_Class_getClassStudent(SlidingMenuMaker.getProfile().member_org_srl, SlidingMenuMaker.getProfile().classList.get(i).getClass_srl());
			}
			break;
		}
	}
	
	private void showTaggedList() {
		ArrayList<InputTag> tagList = photoList.get(photoIndex).tagList;
		
		TextView txt = (TextView)findViewById(R.id.uploadphoto_taggedname);
		String taggedName = "";
		//photoList.get(photoIndex).tagValue = "";
		ArrayList<String> taggedNameList = new ArrayList<String>();
		//ArrayList<String> taggedSrlList = new ArrayList<String>();
		//if(tagList != null) {
		for(int i = 1; i < tagList.size(); i++) {
			if( tagList.get(i).isTagged ) {
				taggedNameList.add(tagList.get(i).member_name);
				//taggedSrlList.add(tagList.get(i).member_srl);
			}
		}
		for(int i = 0; i < taggedNameList.size(); i++) {
			taggedName += taggedNameList.get(i);
			//photoList.get(photoIndex).tagValue += taggedSrlList.get(i);
			if( i != taggedNameList.size()-1 ) {
				taggedName += ", ";
			}
			//photoList.get(photoIndex).tagValue += ",";
		}
		//}
		updateTagValue(photoIndex);
		
		ImageView img = (ImageView)findViewById(R.id.uploadphoto_tagicon);
		if( taggedName.isEmpty() )
			img.setImageResource(R.drawable.photo_edit_tag_off);
		else 
			img.setImageResource(R.drawable.photo_edit_tag_on);
		txt.setText(taggedName);
	}
	
	private void updateTagValue(int index) {
		photoList.get(index).tagValue = "";
		for(int i = 1; i < photoList.get(index).tagList.size(); i++) {
			if( photoList.get(index).tagList.get(i).isTagged ) {
				photoList.get(index).tagValue += photoList.get(index).tagList.get(i).member_srl + ",";
			}
		}
	}
	
	private String makeAllMemberIncludedTag() {
		String tagValue = "";
		for(int i = 0; i < tagMemberList.size(); i++) {
			tagValue += tagMemberList.get(i).member_srl + ",";
		}
		return tagValue;
	}
	
	public void OnButtonCheck(View v) {
		CheckBox cb = (CheckBox)v;
		boolean isChecked = cb.isChecked();
		int position = (Integer) cb.getTag();
		onUpdate(position, isChecked);
	}
	
	public void onUpdate(final int position, boolean checker) {
		if(position == 0) {
			for(int i = 0; i < photoList.get(photoIndex).tagList.size(); i++) {
				photoList.get(photoIndex).tagList.get(i).isTagged = checker;
			}
		}
		else {
			photoList.get(photoIndex).tagList.get(position).isTagged = checker;
			if(checker == false)
				photoList.get(photoIndex).tagList.get(0).isTagged = checker;
			else {
				boolean isAllChecked = true;
				for(int i = 1; i < photoList.get(photoIndex).tagList.size(); i++) {
					if( photoList.get(photoIndex).tagList.get(i).isTagged == false ) {
						isAllChecked = false;
						break;
					}
				}
				if(isAllChecked)
					photoList.get(photoIndex).tagList.get(0).isTagged = checker;
			}
		}
		photoList.get(photoIndex).adapter.notifyDataSetChanged();
		showTaggedList();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(isTagging) { 
		    boolean ret = super.dispatchTouchEvent(event);
		    return ret;
		}
		
		if( event.getAction() == MotionEvent.ACTION_DOWN ) {
			prevTouchX = event.getX();
			if( photoList.size() > 1 )
				haveFlipChance = true;
			isFling = false;
		}
		else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int width = displaymetrics.widthPixels;
			float gap = Math.abs(event.getX() - prevTouchX);
			if( haveFlipChance && gap > width/10 ) {
				if( event.getX() - prevTouchX < 0 ) {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewin_left));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewout_left));
					viewFlipper.showNext();
					photoIndex = (photoIndex+1)%photoList.size();
					showTaggedList();
					haveFlipChance = false;
				}
				else {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewin_right));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewout_right));
					viewFlipper.showPrevious();
					if(photoIndex==0)
						photoIndex= photoList.size();
					photoIndex = (photoIndex-1)%photoList.size();
					showTaggedList();
					haveFlipChance = false;
				}
				//prevTouchX = event.getX();
				Toast.makeText(this, ""+photoIndex, Toast.LENGTH_SHORT).show();
				isFling = true;
			}
		}
		else if( event.getAction() == MotionEvent.ACTION_UP ) {
			haveFlipChance = false;
		}
	    boolean ret = super.dispatchTouchEvent(event);
	    return ret;
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uploadphoto_register, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.uploadphoto_register:
//			intent = new Intent(this, AlbumActivity.class);
//			startActivity(intent);
			for(int i = 0; i < photoList.size(); i++) {
				Bitmap bitmap;
				if( photoMode == 0 ) {
					BitmapFactory.Options opt = new BitmapFactory.Options();
					opt.inSampleSize = 2;
					bitmap = BitmapFactory.decodeFile(photoList.get(i).filepath, opt);
				}
				else {
					bitmap = readBitmap(Uri.parse(photoList.get(i).filepath));
				}
				switch(tagMode) {
				case SEE_ALL_FRIEND:
					this.request_Album_setPhoto(current_album_srl, SlidingMenuMaker.getProfile().member_srl, makeAllMemberIncludedTag(), "N", bitmap);
					break;
				case SEE_TAGGED_FRIEND:
					this.request_Album_setPhoto(current_album_srl, SlidingMenuMaker.getProfile().member_srl, photoList.get(i).tagValue, "N", bitmap);
					break;
				case SEE_PRIVATE:
					this.request_Album_setPhoto(current_album_srl, SlidingMenuMaker.getProfile().member_srl, "", "Y", bitmap);
					break;
				}
			}
			return true;
		}
		return false;
	}

	void applyAll() {
		for(int i = 0; i < photoList.size(); i++) {
			if(i != photoIndex) {
				for(int j = 0; j < photoList.get(i).tagList.size(); j++)
					photoList.get(i).tagList.get(j).isTagged = photoList.get(photoIndex).tagList.get(j).isTagged;
			}
			photoList.get(i).adapter.notifyDataSetChanged();
			updateTagValue(i);
		}
	}
	
	public void OnApplyAllClick(final View v) {
		if(((CheckBox)v).isChecked())
			applyAll();
	}
	
	public void OnSelectImage(final View v) {
		if(isFling == false && tagMode == TAGMODE.SEE_TAGGED_FRIEND) {
//			Intent intent = new Intent(this, ShowUploadImageListActivity.class);
//			intent.putExtra("mode", photoMode);
//			intent.putExtra("index", photoIndex);
//			intent.putExtra("filepath", photoList.get(photoIndex).filepath);
//			startActivityForResult(intent, 1);
			viewFlipper.getChildAt(photoIndex).findViewById(R.id.uploadphoto_tagpage).setVisibility(View.VISIBLE);
        	viewFlipper.getChildAt(photoIndex).findViewById(R.id.uploadphoto_delete).setVisibility(View.INVISIBLE);
        	isTagging = true;
		}
	}
	
	public void OnFinishTagging(View v) {
		viewFlipper.getChildAt(photoIndex).findViewById(R.id.uploadphoto_tagpage).setVisibility(View.INVISIBLE);
    	viewFlipper.getChildAt(photoIndex).findViewById(R.id.uploadphoto_delete).setVisibility(View.VISIBLE);
		isTagging = false;
		
		CheckBox cb = (CheckBox)findViewById(R.id.uploadphoto_tagapplyall);
		if(cb.isChecked())
			applyAll();
		showTaggedList();
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            }
		        });
		    }
		}).start();
	}
	
	public void OnDeleteImage(final View v) {
		new AlertDialog.Builder(this)
		.setMessage("사진을 업로드 목록에서 지우시겠습니까?")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(photoList.size() == 0) {
					return;
				}
				photoList.remove(photoIndex);
				if(photoList.size() == 0) {
					//((ImageView)((View)v.getParent()).findViewById(R.id.uploadphoto_selected)).setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
					new AlertDialog.Builder(UploadPhotoActivity.this)
					.setMessage("사진을 1장 이상 선택해주세요")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					})
					.show();
				}
				else {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewin_right));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(UploadPhotoActivity.this, R.anim.viewout_right));
					viewFlipper.showPrevious();
					viewFlipper.removeViewAt(photoIndex);
					if(photoIndex == 0)
						photoIndex = photoList.size();
					photoIndex = (photoIndex-1)%photoList.size();
					showTaggedList();
				}
				try {
					//image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePathList.get(fileIndex))));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		})
		.setNegativeButton("취소", null)
		.show();
			
	}
	
	public Bitmap readBitmap(Uri selectedImage) { 
		Bitmap bm = null; 
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		//options.inSampleSize = 2; 
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
	
	public ArrayList<String> makeTagListForTimeline(String photo_tag) {
		Profile profile = SlidingMenuMaker.getProfile();
		ArrayList<String> tagList = new ArrayList<String>();
		
		//tagList.add(profile.member_srl);
		switch(tagMode) {
		case SEE_ALL_FRIEND:
			for(int i = 0; i < tagMemberList.size(); i++)
				tagList.add(tagMemberList.get(i).member_srl);
			break;
		case SEE_TAGGED_FRIEND:
			String[] photo_tagList = photo_tag.split(",");
			for(int i = 0; i < photo_tagList.length; i++)
				tagList.add(photo_tagList[i]);
			break;
		case SEE_PRIVATE:
			break;
		}
		
		return tagList;
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Album/setPhoto")) {
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
					uploadedPhotoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
					
					String targetList = "";
					ArrayList<String> tagList = this.makeTagListForTimeline(photo_tag);
					for(int i = 0; i < tagList.size(); i++)
						targetList += tagList.get(i) + ",";
					this.request_Timeline_setTimelineMessage(SlidingMenuMaker.getProfile().member_srl, "P", photo_srl, targetList);
				}
				else if(uri.equals("Timeline/setTimelineMessage")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++ ) {
						JSONObject dataObj = dataArray.getJSONObject(i);
						String timeline_type = dataObj.getString("timeline_type");
						String timeline_srl = dataObj.getJSONObject("_id").getString("$oid");
						String timeline_member_srl = dataObj.getString("timeline_member_srl");
						String timeline_target_member_srl = dataObj.getString("timeline_target_member_srl");
						String timeline_message = dataObj.getString("timeline_message");
						String timeline_created = dataObj.getString("timeline_created");
						for(int j = 0; j < uploadedPhotoList.size(); j++) {
							if(uploadedPhotoList.get(j).photo_srl.equals(timeline_message)) {
								uploadedPhotoList.get(j).photo_timeline_srl = timeline_srl;
								break;
							}
						}
						this.request_Album_setPhotoTimeline(timeline_message, timeline_srl);
						break;
					}
					//}
				}
				else if(uri.equals("Album/setPhotoTimeline")) {
					if( ++sendCounter == photoList.size() ) {
						Intent data = new Intent();
						data.putExtra("uploadedPhotoList", uploadedPhotoList);
						data.putExtra("album_srl", uploadedPhotoList.get(0).photo_album_srl);
						data.putExtra("albumList", newAlbumList);
						setResult(Activity.RESULT_OK, data);
						finish();
					}
				}
				else if(uri.equals("Album/setAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String album_srl = jsonObj.getString("album_srl");
					String album_member_srl = jsonObj.getString("album_member_srl");
					final String album_name = jsonObj.getString("album_name");
					String album_type = jsonObj.getString("album_type");
					String album_created = jsonObj.getString("album_created");
					String album_updated = jsonObj.getString("album_updated");
					String album_count = jsonObj.getString("album_count");
					current_album_srl = album_srl;
					albumList.add(albumList.size()-1, new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
					newAlbumList.add(new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									albumListAdapter.notifyDataSetChanged();
									spinner.setSelection(albumList.size()-2);
									albumListAdapter.notifyDataSetChanged();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Member/getFriends")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					ArrayList<String> getMemberList = new ArrayList<String>();
					for(int i = 0; i < dataArray.length(); i++) {
						String friend_srl = dataArray.getJSONObject(i).getString("friend_srl");
						String friend_origin_srl = dataArray.getJSONObject(i).getString("friend_origin_srl");
						String friend_target_srl = dataArray.getJSONObject(i).getString("friend_target_srl");
						String friend_status = dataArray.getJSONObject(i).getString("friend_status");
						String friend_created = dataArray.getJSONObject(i).getString("friend_created");
						if(friend_status.equals("A")) {
							if(friend_origin_srl.equals(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl))
								getMemberList.add(friend_target_srl);
							else
								getMemberList.add(friend_origin_srl);
							requestGetMemberCounter++;
						}
					}
					for(int i = 0; i < getMemberList.size(); i++)
						this.request_Member_getMember(getMemberList.get(i));
				}
				else if(uri.equals("Class/getClassStudent")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String member_srl = dataArray.getJSONObject(i).getString("member_srl");
						String member_name = dataArray.getJSONObject(i).getString("member_name");
						String member_type = dataArray.getJSONObject(i).getString("member_type");
						String member_org_srl = dataArray.getJSONObject(i).getString("member_org_srl");
						String member_picture = dataArray.getJSONObject(i).getString("member_picture");
						JSONObject studentObj = dataArray.getJSONObject(i).getJSONObject("student");
						String student_srl = studentObj.getString("student_srl");
						String student_member_srl = studentObj.getString("student_member_srl");
						String student_class_srl = studentObj.getString("student_class_srl");
						String student_parent_srl = studentObj.getString("student_parent_srl");
						String student_teacher_srl = studentObj.getString("student_teacher_srl");
						String student_shuttle_srl = studentObj.getString("student_shuttle_srl");
						String student_birthday = studentObj.getString("student_birthday");
						String student_parent_key = studentObj.getString("student_parent_key");
						tagMemberList.add(new InputTag(member_srl, member_name));
					}
					switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
					case 'T':
						setTagMemberData();
						break;
					case 'M':
						if( ++requestGetClassStudentCounter == SlidingMenuMaker.getProfile().classList.size()-1 ) {
							setTagMemberData();
						}
						break;
					}
				}
				else if(uri.equals("Member/getMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					tagMemberList.add(new InputTag(member_srl, member_name));
					if(--requestGetMemberCounter == 0) {
						setTagMemberData();
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
