package com.ihateflyingbugs.kidsm;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.businfo.BusInfoFragment;
import com.ihateflyingbugs.kidsm.friend.FriendFragment;
import com.ihateflyingbugs.kidsm.friend.FriendListItem;
import com.ihateflyingbugs.kidsm.friend.FriendListItem.FriendInfo;
import com.ihateflyingbugs.kidsm.friend.FriendListItem.FriendListItemType;
import com.ihateflyingbugs.kidsm.gallery.GalleryFragment;
import com.ihateflyingbugs.kidsm.login.LoginActivity;
import com.ihateflyingbugs.kidsm.login.RegisterInfoTakerActivity;
import com.ihateflyingbugs.kidsm.mentory.MentoryFragment;
import com.ihateflyingbugs.kidsm.menu.Children;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.NewsfeedFragment;
import com.ihateflyingbugs.kidsm.notice.NoticeFragment;
import com.ihateflyingbugs.kidsm.notice.NoticeLog;
import com.ihateflyingbugs.kidsm.notice.NoticeLog.NOTICE_TYPE;
import com.ihateflyingbugs.kidsm.notice.NoticeLogAdapter;
import com.ihateflyingbugs.kidsm.photonotice.PhotoNoticeFragment;
import com.ihateflyingbugs.kidsm.schedule.AddScheduleActivity;
import com.ihateflyingbugs.kidsm.schedule.ScheduleFragment;
import com.ihateflyingbugs.kidsm.settings.SettingsFragment;
import com.ihateflyingbugs.kidsm.uploadphoto.GetAlbumFromLocalActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.SimpleCamera;
import com.localytics.android.LocalyticsSession;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class MainActivity extends NetworkActivity {
	SlidingMenuMaker maker;
	static FragmentManager fragmentManager;
	static NewsfeedFragment newsfeed;
	private static GalleryFragment gallery;
	static ScheduleFragment schedule;
	static BusInfoFragment businfo;
	private static MentoryFragment mentory;
	static SettingsFragment settings;
	static FriendFragment friend;
	private static int currentFragmentIndex;
	private static MainActivity main;
	
	ArrayList<FriendListItem> friendList;
	
	int requestGetParentCounter;
	ArrayList<String> teacher_parent_srl_list;

	ArrayList<FriendListItem> requestList;
	ArrayList<FriendListItem> friendInClassList;
	ArrayList<FriendListItem> recommendedFriendList;
	
	int requestGetClassStudentCounter;
	ArrayList<FriendListItem> studentList;
	Menu menu;
	ViewFlipper noticeViewFlipper;

	SharedPreferences prefs;
	ArrayList<NoticeLog> noticeList;
	ArrayList<NoticeLog> photoNoticeList;
	NoticeLogAdapter noticeAdapter;
	NoticeLogAdapter photoNoticeAdapter;
	ListView noticeListView;
	ListView photoNoticeListView;
	static boolean[] isAdded;
	
	private static final int PICK_FROM_CAMERA = 5;
	private static final int PICK_FROM_ALBUM = 6;
	private static final int CROP_FROM_CAMERA = 7;
	private Uri profileImageCaptureUri;
	Bitmap profilePhoto;
	
	int requestLogDataCounter;
	
	int modifyMemberPictureTarget;
	
	private LocalyticsSession localyticsSession;
	
	public static MainActivity getMainActivity() {
		return main;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = this;
		setContentView(R.layout.activity_main);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_drawer_btnset);
		maker = new SlidingMenuMaker();
		//auth_key = getIntent().getStringExtra("auth_key");
		Profile profile = getIntent().getParcelableExtra("profile");
		SlidingMenuMaker.setProfile(profile);
		maker.Initiate(this);
		//Toast.makeText(this, ""+profile.getCurrentClass().getNumOfStudentHavingParent(), Toast.LENGTH_SHORT).show();
		fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		newsfeed = new NewsfeedFragment();	// 0
		setGallery(new GalleryFragment());	// 1
		schedule = new ScheduleFragment();	// 2
		businfo = new BusInfoFragment();	// 3
		setMentory(new MentoryFragment());	// 4
		settings = new SettingsFragment();	// 5
		friend = new FriendFragment();		// 6
		isAdded = new boolean[7];
		for( int i = 0; i < isAdded.length; i++)
			isAdded[i] = false;
		transaction.add(R.id.main_fragment, newsfeed);
		isAdded[0] = true;
		//		transaction.add(R.id.main_fragment, getGallery());
//		transaction.add(R.id.main_fragment, schedule);
//		transaction.add(R.id.main_fragment, businfo);
//		transaction.add(R.id.main_fragment, getMentory());
//		transaction.add(R.id.main_fragment, settings);
//		transaction.add(R.id.main_fragment, friend);
//		transaction.hide(getGallery());
//		transaction.hide(schedule);
//		transaction.hide(businfo);
//		transaction.hide(getMentory());
//		transaction.hide(settings);
//		transaction.hide(friend);
		setTitle(R.string.newsfeedactivity);
		transaction.commit();
		setCurrentFragmentIndex(0);
		
		noticeViewFlipper = (ViewFlipper)findViewById(R.id.main_flipper);
		
		noticeList = new ArrayList<NoticeLog>();
		photoNoticeList = new ArrayList<NoticeLog>();
		noticeAdapter = new NoticeLogAdapter(this, noticeList);
		photoNoticeAdapter = new NoticeLogAdapter(this, photoNoticeList);
		noticeListView = (ListView) findViewById(R.id.main_normal_notice);
		photoNoticeListView = (ListView) findViewById(R.id.main_photo_notice);
		noticeListView.setAdapter(noticeAdapter);
		photoNoticeListView.setAdapter(photoNoticeAdapter);
		refreshNoticeLogs();
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

	private void refreshNoticeLogs() {
		noticeList.clear();
		photoNoticeList.clear();
		int msgCount = 0;
		if( prefs == null ) {
        	prefs = getSharedPreferences("notice", MODE_PRIVATE);
        }
    	msgCount = Integer.parseInt(prefs.getString(""+maker.getProfile().member_srl+"_msgCount", "0"));
		if(msgCount == 0)
			return;

		requestLogDataCounter = msgCount;
		for(int i = msgCount-1; i >= 0; i--) {
			String title = prefs.getString(maker.getProfile().member_srl+"title"+i, "");
			String msg = prefs.getString(maker.getProfile().member_srl+"msg"+i, "");
			String from_srl = prefs.getString(maker.getProfile().member_srl+"from_srl"+i, "");
			String type = prefs.getString(maker.getProfile().member_srl+"type"+i, "");
			String ticker = prefs.getString(maker.getProfile().member_srl+"ticker"+i, "");
			
			if( type.isEmpty() == false ) {
				switch( type.charAt(0) ) {
				case 'N':
					noticeList.add(new NoticeLog(NOTICE_TYPE.NORMAL, title, msg, ticker, from_srl));
					this.request_Member_getMember(from_srl);
					break;
				case 'P':
					photoNoticeList.add(new NoticeLog(NOTICE_TYPE.PHOTO, title, msg, ticker, from_srl));
					this.request_Member_getMember(from_srl);
					break;
				}
			}
		}
		noticeAdapter.notifyDataSetChanged();
		photoNoticeAdapter.notifyDataSetChanged();
	}
	
	public void OnChildClick(View v) {
		maker.OnChildClick(v);
	}
	
	public void OnAddChild(View v) {
		maker.OnAddChild(v);
	}
	
	public void OnSeeChildren(View v) {
		maker.OnSeeChildren(v);
	}
	
	public void OnSeeFriend(View v) {
		maker.OnSeeFriend(v);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case android.R.id.home:
			if(maker.slidingMenu.isMenuShowing())
				maker.slidingMenu.showContent();
			else
				maker.slidingMenu.showMenu();
			return true;
		case R.id.notice:
			refreshNoticeLogs();
			switch( noticeViewFlipper.getDisplayedChild() ) {
			case 0:
				setNextAnimation();
				noticeViewFlipper.setDisplayedChild(1);
				break;
			case 2:
				setPrevAnimation();
				noticeViewFlipper.setDisplayedChild(1);
				break;
			}
			return true;
		case R.id.photo:
			refreshNoticeLogs();
			switch( noticeViewFlipper.getDisplayedChild() ) {
			case 0:
			case 1:
				setNextAnimation();
				noticeViewFlipper.setDisplayedChild(2);
				break;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			Intent intent;
			switch(requestCode) {
			case 0:
				String orgName = data.getStringExtra("org");
				String className = data.getStringExtra("class");
				String childName = data.getStringExtra("childname");
//				maker.OnAddChildDialog(orgName, className, childName);
				break;
			case 3:
				ArrayList<String> phoneList = data.getStringArrayListExtra("phoneList");
				String phoneListString = "";
				for(int i = 0; i < phoneList.size(); i++) {
					phoneListString += phoneList.get(i);
					if( i != phoneList.size()-1)
						phoneListString += ";";
				}
				intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+ phoneListString));
				intent.putExtra("sms_body", "[KIDSM]" + SlidingMenuMaker.getProfile().member_name +" 선생님께서 학부모님을 키즈엠 앱으로 초대하셨습니다. \n\n안드로이드 앱 다운로드 :\nhttps://db.tt/09P0dxrz");
				startActivity(intent);
				break;
			case CROP_FROM_CAMERA:
				final Bundle extras = data.getExtras();
				
				if(extras != null) {
					profilePhoto = extras.getParcelable("data");
					//((ImageView)findViewById(R.id.mainpicture)).setImageBitmap(ImageMaker.getCroppedBitmap(profilePhoto));
					switch( modifyMemberPictureTarget ) {
					case 0:
						this.request_Member_modMemberPicture(maker.getProfile().member_srl, profilePhoto);
						break;
					default:
						this.request_Member_modMemberPicture(maker.getProfile().getChildren(modifyMemberPictureTarget-1).student_member_srl, profilePhoto);
						break;
					}
				}
	
				// 임시 파일 삭제
				File f = new File(profileImageCaptureUri.getPath());
				if(f.exists()) {
					f.delete();
				}
				break;
			case PICK_FROM_ALBUM:
				profileImageCaptureUri = data.getData();
			case PICK_FROM_CAMERA:
				intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(profileImageCaptureUri, "image/*");
	
				intent.putExtra("outputX", 90);
				intent.putExtra("outputY", 90);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP_FROM_CAMERA);
				break;
			}
		}
	}
	
	public static void changeFragment(int id) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(currentFragmentIndex == 7) {
			main.getActionBar().setIcon(R.drawable.general_actionbar_drawer_btnset);
		}
		switch(currentFragmentIndex) {
		case 0:
			transaction.detach(newsfeed);
			//newsfeed = new NewsfeedFragment();
			break;
		case 1:
			transaction.detach(gallery);
			//setGallery(new GalleryFragment());	// 1
			break;
		case 2:
			transaction.detach(schedule);
			//schedule = new ScheduleFragment();	// 2
			break;
		case 3:
			transaction.detach(businfo);
			//businfo = new BusInfoFragment();	// 3
			break;
		case 4:
			transaction.hide(getMentory());
			//setMentory(new MentoryFragment());	// 4
			break;
		case 6:
			transaction.detach(settings);
			//settings = new SettingsFragment();	// 5
			break;
		case 7:
			transaction.detach(friend);
			//friend = new FriendFragment();		// 6
			break;
		}
		
		switch(id) {
		case 0:
			//newsfeed = new NewsfeedFragment();
			transaction.attach(newsfeed);
			//transaction.add(R.id.main_fragment, newsfeed);
			main.setTitle(R.string.newsfeedactivity);
			break;
		case 1:
			//setGallery(new GalleryFragment());
			if( isAdded[1] )
				transaction.attach(getGallery());
			else {
				transaction.add(R.id.main_fragment, getGallery());
				isAdded[1] = true;
			}
			//transaction.add(R.id.main_fragment, getGallery());
			main.setTitle(R.string.galleryactivity);
			break;
		case 2:
			//schedule = new ScheduleFragment();
			if( isAdded[2] )
				transaction.attach(schedule);
			else {
				transaction.add(R.id.main_fragment, schedule);
				isAdded[2] = true;
			}
			//transaction.add(R.id.main_fragment, schedule);
			main.setTitle(R.string.scheduleactivity);
			break;
		case 3:
			//businfo = new BusInfoFragment();
			if( isAdded[3] )
				transaction.attach(businfo);
			else {
				transaction.add(R.id.main_fragment, businfo);
				isAdded[3] = true;
			}
			//transaction.add(R.id.main_fragment, businfo);
			main.setTitle(R.string.busInfoactivity);
			break;
		case 4:
			//setMentory(new MentoryFragment());
			if( isAdded[4] )
				transaction.show(getMentory());
			else {
				transaction.add(R.id.main_fragment, getMentory());
				isAdded[4] = true;
			}
			//transaction.add(R.id.main_fragment, getMentory());
			main.setTitle(R.string.mentoryactivity);
			break;
		case 6:
			//settings = new SettingsFragment();
			if( isAdded[5] )
				transaction.attach(settings);
			else {
				transaction.add(R.id.main_fragment, settings);
				isAdded[5] = true;
			}
			//transaction.add(R.id.main_fragment, settings);
			main.setTitle(R.string.settingsactivity);
			break;
		case 7:
			//friend = new FriendFragment();
			if( isAdded[6] )
				transaction.attach(friend);
			else {
				transaction.add(R.id.main_fragment, friend);
				isAdded[6] = true;
			}
			//transaction.add(R.id.main_fragment, friend);
			main.setTitle(R.string.friendactivity);
			main.getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		}
		transaction.commit();
		currentFragmentIndex = id;
	}

	public static int getCurrentFragmentIndex() {
		return currentFragmentIndex;
	}

	public static void setCurrentFragmentIndex(int currentFragmentIndex) {
		MainActivity.currentFragmentIndex = currentFragmentIndex;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.dispatchTouchEvent(event);
			break;
		case 1:
			getGallery().dispatchTouchEvent(event);
			break;
		case 2:
			schedule.dispatchTouchEvent(event);
			break;
		
		}
		return super.dispatchTouchEvent(event);
	}
	
	public void OnLikeClick(View v) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.OnLikeClick(v);
			break;
		case 4:
			mentory.OnLikeClick(v);
			break;
		}
	}
	
	public void OnReplyClick(View v) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.OnReplyClick(v);
			break;
		case 4:
			mentory.OnReplyClick(v);
			break;
		}
	}
	
	public void OnScrapClick(View v) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.OnScrapClick(v);
			break;
		case 4:
			mentory.OnScrapClick(v);
			break;
		}
	}
	
	public void OnImageClick(View v) {
		newsfeed.OnImageClick(v);
	}
	
	public void OnUploadPhoto(View v) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.OnUploadPhoto(v);
			break;
		case 1:
			getGallery().OnUploadPhoto(v);
			break;
		}
	}
	
	public void OnNewAlbum(View v) {
		getGallery().OnNewAlbum(v);
	}
	
	public void OnSeeAllPhoto(View v) {
		getGallery().OnSeeAllPhoto(v);
	}
	
	public void OnSeeAlbum(View v) {
		getGallery().OnSeeAlbum(v);
	}
	
	public void OnSeeScrapAlbum(View v) {
		getGallery().OnSeeScrapAlbum(v);
	}

	public void OnSeeTaggedPhoto(View v) {
		getGallery().OnSeeTaggedPhoto(v);
	}
	
	public void OnSettingClick(View v) {
		getGallery().OnSettingClick(v);
	}
	
	public void OnMonthMode(View v) {
		schedule.OnMonthMode(v);
	}
	
	public void OnWeekMode(View v) {
		schedule.OnWeekMode(v);
	}

	public void OnPrevMonth(View v) {
		schedule.OnPrevMonth(v);
	}
	
	public void OnNextMonth(View v) {
		schedule.OnNextMonth(v);
	}
	
	public void OnShowConfirmedList(View v) {
		schedule.OnShowConfirmedList(v);
	}

	public void OnCheckSchedule(View v) {
		schedule.OnCheckSchedule(v);
	}
	
	public void OnUploadSchedule(View v) {
		schedule.OnUploadSchedule(v);
	}
	
	public void OnButtonClick(View v) {
		friend.OnButtonClick(v);
	}
	
	public void OnNameClick(View v) {
		friend.OnNameClick(v);
	}
	
	public void OnInviteFriend(View v) {
		friend.OnInviteFriend(v);
	}
	
	public void OnBusClick(View v) { 
		businfo.OnBusClick(v);
	}
	
	public void OnChangeBus(View v) {
		businfo.OnChangeBus(v);
	}
	
	public void OnSeeMentory(View v) {
		switch(currentFragmentIndex) {
		case 0:
			newsfeed.OnSeeMentory(v);
			break;
		case 4:
			mentory.OnSeeMentory(v);
			break;
		}
	}
	
	public void OnSetProfileImage(View v) {
		switch(v.getId()) {
		case R.id.profile_picture:
			this.modifyMemberPictureTarget = 0;
			break;
		case R.id.child_mainpicture:
			this.modifyMemberPictureTarget = Integer.parseInt(v.getTag().toString())+1;
			break;
		}
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.profile_image_mode, android.R.layout.simple_list_item_1);
		ListView listView = new ListView(this);
		listView.setAdapter(arrayAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final AlertDialog alert = new AlertDialog.Builder(this)
		.setTitle(getString(R.string.profile_select_photo))
		.setView(listView).create();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent;
				switch(position) {
				case 0:
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
					profileImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, profileImageCaptureUri);
					intent.putExtra("return-data", true);
					startActivityForResult(intent, PICK_FROM_CAMERA);
					break;
				case 1:
					intent = new Intent(Intent.ACTION_PICK);
					intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
					startActivityForResult(intent, PICK_FROM_ALBUM);
					break;
				case 2:
					break;
				case 3:
					break;
				}
				alert.dismiss();
			}
		});
		alert.show();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		if( keyCode == KeyEvent.KEYCODE_BACK) {
			switch( noticeViewFlipper.getDisplayedChild() ) {
			case 0:
				new AlertDialog.Builder(this)
				.setMessage(getString(R.string.app_name)+"을 종료하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton("취소", null)
				.show();
				break;
			case 1:
			case 2:
				setPrevAnimation();
				noticeViewFlipper.setDisplayedChild(0);
				break;
			}
        	return true;
        }
		else
			return super.onKeyDown(keyCode, event);
	}

	public void initiateRequestFriendStatus() {
		requestGetParentCounter = 0;
		teacher_parent_srl_list = new ArrayList<String>();
		if( friendList == null )
			friendList = new ArrayList<FriendListItem>();
		else
			friendList.clear();
		
		requestList = new ArrayList<FriendListItem>();
		friendInClassList = new ArrayList<FriendListItem>();
		recommendedFriendList = new ArrayList<FriendListItem>();
		
		requestGetClassStudentCounter = 0;
		studentList = new ArrayList<FriendListItem>();
	}
	
	public void requestFriendList() {
		initiateRequestFriendStatus();
		Profile profile = maker.getProfile();
		switch(profile.member_type.charAt(0)) {
		case 'P':
			if( profile.childrenList.size() > 1 )
				this.request_Member_getFriends(profile.getCurrentChildren().student_member_srl);
			else 
				Toast.makeText(this, "친구보기는 인증된 유치원생 부모님만 이용가능합니다.", Toast.LENGTH_SHORT).show();
			
			break;
		case 'T':
			for( int i = 0; i < profile.classList.size(); i++) {
				for( int j = 0; j < profile.classList.get(i).getTeacherList().size(); j++ ) {
					if( profile.member_srl.equals(profile.classList.get(i).getTeacherList().get(j).teacher_member_srl) ) {
						this.request_Class_getClassStudent(profile.member_org_srl, profile.classList.get(i).getClass_srl());
						return;
					}
				}
			}
			break;
		case 'M':
			if( profile.classList.size() != 0 ) {
				friendList.add(new FriendListItem(FriendListItemType.NAMETAG, "선생님", ""));
				for( int i = 0; i < profile.classList.size(); i++ ) {
					for(int j = 0; j < profile.classList.get(i).getTeacherList().size(); j++) {
						friendList.add(new FriendListItem(FriendListItemType.GRANTED_TEACHER, profile.classList.get(i).getTeacherList().get(j).teacher_name, ""));
					}
					this.request_Class_getClassStudent(profile.classList.get(i).getClass_org_srl(), profile.classList.get(i).getClass_srl());
				}
			}
			break;
		}
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
				if(uri.equals("Class/getClassStudent")) {
					boolean addClassTitle = false;
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
						switch(maker.getProfile().member_type.charAt(0)) {
						case 'T':
							if( addClassTitle == false ) {
								friendList.add(new FriendListItem(FriendListItemType.NAMETAG, maker.getProfile().classes.get(student_class_srl).getClass_name(), ""));
								addClassTitle = true;
							}
							FriendListItem student;
							if( student_parent_srl.equals("0") ) {
								student = new FriendListItem(FriendListItemType.CURRENT_STUDENT, "부모미가입", member_name);
								student.setStudentsParentInfo(student.new StudentsParentInfo(student_parent_srl, student_srl, student_member_srl));
								friendList.add(student);
							}
							else {
								student = new FriendListItem(FriendListItemType.CURRENT_STUDENT, student_parent_srl, member_name);
								student.setStudentsParentInfo(student.new StudentsParentInfo(student_parent_srl, student_srl, student_member_srl));
								friendList.add(student);
								teacher_parent_srl_list.add(student_parent_srl);
							}
							break;
						case 'M':
							if(student_parent_srl.equals("0"))
								friendList.add(new FriendListItem(FriendListItemType.CURRENT_STUDENT_FOR_MANAGER, "부모미가입", member_name));
							else {
								if(studentList.size() == 0) {
									friendList.add(new FriendListItem(FriendListItemType.NAMETAG, "학생 목록", ""));
								}
								friendList.add(new FriendListItem(FriendListItemType.CURRENT_STUDENT_FOR_MANAGER, student_parent_srl, member_name));
								studentList.add(new FriendListItem(FriendListItemType.CURRENT_STUDENT_FOR_MANAGER, student_parent_srl, member_name));
							}
							break;
						}
					}
					switch(maker.getProfile().member_type.charAt(0)) {
					case 'T':
						for( int i = 0; i < teacher_parent_srl_list.size(); i++ ) {
							this.request_Member_getParent(teacher_parent_srl_list.get(i));
						}
						break;
					case 'M':
						for( int i = 0; i < studentList.size(); i++ ) {
							this.request_Member_getParent(studentList.get(i).getName());
						}
						requestGetClassStudentCounter++;
						break;
					}
				}
				else if(uri.equals("Member/getParent")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_type = jsonObj.getString("member_type");
					String member_org_srl = jsonObj.getString("member_org_srl");
					String member_picture = jsonObj.getString("member_picture");
					JSONObject parentObj = jsonObj.getJSONObject("parent");
					String parent_srl = parentObj.getString("parent_srl");
					String parent_member_srl = parentObj.getString("parent_member_srl");
					String parent_viewer_key = parentObj.getString("parent_viewer_key");
					switch(maker.getProfile().member_type.charAt(0)) {
					case 'P':
						for( int i = 0; i < friendList.size(); i++ ) {
							if( parent_srl.equals(friendList.get(i).getName()) ) {
								friendList.get(i).setName(member_name);
								if( ++requestGetParentCounter == this.friendInClassList.size() + this.requestList.size() + this.recommendedFriendList.size()) {
									new Thread(new Runnable() {
									    @Override
									    public void run() {    
									        runOnUiThread(new Runnable(){
									            @Override
									             public void run() {
									            	maker.changeFragment(7);
													friend.setFriendList(friendList);
									            }
									        });
									    }
									}).start();
								}
								break;
							}
						}
						break;
					case 'T':
						for(int i = 0; i < friendList.size(); i++) {
							if( friendList.get(i).getName().equals(parent_srl) ) {
								friendList.get(i).setName(member_name);
								//TODO : 부모의 정보 저장하기
								break;
							}
						}
						if( ++requestGetParentCounter == teacher_parent_srl_list.size() ) {
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
							            	maker.changeFragment(7);
											friend.setFriendList(friendList);
							            }
							        });
							    }
							}).start();
						}
						break;
					case 'M':
						for( int i = 0; i < friendList.size(); i++ ) {
							if( parent_srl.equals(friendList.get(i).getName()) ) {
								friendList.get(i).setName(member_name);
								break;
							}
						}
						if( ++requestGetParentCounter == studentList.size() &&
								requestGetClassStudentCounter == maker.getProfile().classList.size()-1 ) {
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
							            	maker.changeFragment(7);
											friend.setFriendList(friendList);
							            }
							        });
							    }
							}).start();
						}
						break;
					}
				}
				else if(uri.equals("Member/getFriends")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String friend_srl = dataArray.getJSONObject(i).getString("friend_srl");
						String friend_origin_srl = dataArray.getJSONObject(i).getString("friend_origin_srl");
						String friend_target_srl = dataArray.getJSONObject(i).getString("friend_target_srl");
						String friend_status = dataArray.getJSONObject(i).getString("friend_status");
						String friend_created = dataArray.getJSONObject(i).getString("friend_created");
						FriendListItem friend;
						switch(friend_status.charAt(0)) {
						case 'S':
							if(friend_origin_srl.equals(maker.getProfile().getCurrentChildren().student_member_srl)) {
								friend = new FriendListItem(FriendListItemType.WAITING_FRIEND, friend_target_srl, friend_target_srl);
								friend.setFriendInfo(friend.new FriendInfo(friend_srl, friend_origin_srl, friend_target_srl, friend_status.charAt(0)));
								friendInClassList.add(friend);
							}
							else {
								friend = new FriendListItem(FriendListItemType.REQUESTED_FRIEND, friend_origin_srl, friend_origin_srl);
								friend.setFriendInfo(friend.new FriendInfo(friend_srl, friend_origin_srl, friend_target_srl, friend_status.charAt(0)));
								requestList.add(friend);
							}
							break;
						case 'W':
							break;
						case 'A':
							if(friend_origin_srl.equals(maker.getProfile().getCurrentChildren().student_member_srl))
								friend = new FriendListItem(FriendListItemType.CURRENT_FRIEND, friend_target_srl, friend_target_srl);
							else
								friend = new FriendListItem(FriendListItemType.CURRENT_FRIEND, friend_origin_srl, friend_origin_srl);
							friend.setFriendInfo(friend.new FriendInfo(friend_srl, friend_origin_srl, friend_target_srl, friend_status.charAt(0)));
							friendInClassList.add(friend);
							break;
						case 'R':
							break;
						}
					}
					this.request_Member_getRecommendFriends(maker.getProfile().getCurrentChildren().student_member_srl, maker.getProfile().getCurrentChildren().getStudent_org_srl(), 1, 100);
				}
				else if(uri.equals("Member/getRecommendFriends")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String member_srl = dataArray.getJSONObject(i).getString("member_srl");
						String member_name = dataArray.getJSONObject(i).getString("member_name");
						String member_nickname = dataArray.getJSONObject(i).getString("member_nickname");
						String member_type = dataArray.getJSONObject(i).getString("member_type");
						String member_org_srl = dataArray.getJSONObject(i).getString("member_org_srl");
						String member_point = dataArray.getJSONObject(i).getString("member_point");
						String member_email = dataArray.getJSONObject(i).getString("member_email");
						String member_picture = dataArray.getJSONObject(i).getString("member_picture");
						JSONObject studentObj = dataArray.getJSONObject(i).getJSONObject("student");
						String student_srl =studentObj.getString("student_srl");
						String student_member_srl =studentObj.getString("student_member_srl");
						String student_class_srl =studentObj.getString("student_class_srl");
						String student_parent_srl =studentObj.getString("student_parent_srl");
						String student_teacher_srl =studentObj.getString("student_teacher_srl");
						String student_shuttle_srl =studentObj.getString("student_shuttle_srl");
						String student_birthday =studentObj.getString("student_birthday");
						String student_parent_key =studentObj.getString("student_parent_key");
						
						if(student_class_srl.equals(SlidingMenuMaker.getProfile().getCurrentChildren().teacherList.get(0).teacher_class_srl) == false)
							continue;
						
						boolean isDataApproved = true;
						for(int j = 0; j < requestList.size(); j++) {
							if(requestList.get(j).getName().equals(member_srl)) {
								isDataApproved = false;
								break;
							}
						}
						for(int j = 0; j < friendInClassList.size(); j++) {
							if(friendInClassList.get(j).getName().equals(member_srl)) {
								isDataApproved = false;
								break;
							}
						}
						for(int j = 0; j < recommendedFriendList.size(); j++) {
							if(recommendedFriendList.get(j).getFriendInfo().target_srl.equals(member_srl)) {
								isDataApproved = false;
								break;
							}
						}
						
						if( isDataApproved ) {
							FriendListItem friend = new FriendListItem(FriendListItemType.RECOMMENDED_FRIEND, student_parent_srl, member_name);
							friend.setFriendInfo(friend.new FriendInfo("", maker.getProfile().getCurrentChildren().student_member_srl, member_srl, ' '));
							recommendedFriendList.add(friend);
						}
					}
					if( requestList.size() != 0 ) {
						friendList.add(new FriendListItem(FriendListItemType.NAMETAG, "받은 친구 신청", ""));
						friendList.addAll(requestList);
						for(int i = 0; i < requestList.size(); i++)
							this.request_Member_getMember(requestList.get(i).getName());
					}
					if( friendInClassList.size() != 0 ) {
						friendList.add(new FriendListItem(FriendListItemType.NAMETAG, maker.getProfile().getCurrentChildren().className, ""));
						friendList.addAll(friendInClassList);
						for(int i = 0; i < friendInClassList.size(); i++)
							this.request_Member_getMember(friendInClassList.get(i).getName());
					}
					if( recommendedFriendList.size() != 0 ) {
						friendList.add(new FriendListItem(FriendListItemType.NAMETAG, "같은 반 추천 친구", ""));
						for(int i = 0; i < recommendedFriendList.size(); i++) {
							if(recommendedFriendList.get(i).getName().equals("0")) {
								recommendedFriendList.get(i).setName("부모미가입");
								requestGetParentCounter++;
							}
							else {
								this.request_Member_getParent(recommendedFriendList.get(i).getName());
							}
						}
						friendList.addAll(recommendedFriendList);
					}
					if( friendList.size() == 0 ) {
						new Thread(new Runnable() {
						    @Override
						    public void run() {    
						        runOnUiThread(new Runnable(){
						            @Override
						             public void run() {
						            	maker.changeFragment(7);
										friend.setFriendList(friendList);
						            }
						        });
						    }
						}).start();
					}
				}
				else if(uri.equals("Member/getMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_type = jsonObj.getString("member_type");
					String member_org_srl = jsonObj.getString("member_org_srl");
					String member_point = jsonObj.getString("member_point");
					String member_email = jsonObj.getString("member_email");
					String member_picture = jsonObj.getString("member_picture");
					String student_srl = "";
					String student_member_srl = "";
					String student_class_srl = "";
					String student_parent_srl = "";
					String student_teacher_srl = "";
					String student_shuttle_srl = "";
					String student_birthday = "";
					String student_parent_key = "";
					String parent_srl = "";
					String parent_member_srl = "";
					String parent_viewer_key = "";
					switch(member_type.charAt(0)) {
					case 'P':
						JSONObject parentObj = jsonObj.getJSONObject("parent");
						parent_srl = parentObj.getString("parent_srl");
						parent_member_srl = parentObj.getString("parent_member_srl");
						parent_viewer_key = parentObj.getString("parent_viewer_key");
						break;
					case 'S':
						JSONObject studentObj = jsonObj.getJSONObject("student");
						student_srl = studentObj.getString("student_srl");
						student_member_srl = studentObj.getString("student_member_srl");
						student_class_srl = studentObj.getString("student_class_srl");
						student_parent_srl = studentObj.getString("student_parent_srl");
						student_teacher_srl = studentObj.getString("student_teacher_srl");
						student_shuttle_srl = studentObj.getString("student_shuttle_srl");
						student_birthday = studentObj.getString("student_birthday");
						student_parent_key = studentObj.getString("student_parent_key");
						break;
					case 'T':
						break;
					case 'M':
						break;
					}
					if(requestLogDataCounter > 0) {
						for(int i = 0; i < noticeList.size(); i++) {
							if( noticeList.get(i).getMember_srl().equals(member_srl) ) {
								noticeList.get(i).setMember_picture(member_picture);
							}
						}
						for(int i = 0; i < photoNoticeList.size(); i++) {
							if( photoNoticeList.get(i).getMember_srl().equals(member_srl) ) {
								photoNoticeList.get(i).setMember_picture(member_picture);
							}
						}
					}
					else {
						switch(maker.getProfile().member_type.charAt(0)) {
						case 'P':
							switch(member_type.charAt(0)) {
							case 'P':
								for(int i = 0; i < friendList.size(); i++) {
									if( member_srl.equals(friendList.get(i).getName()) ) {
										friendList.get(i).setChildname(member_name);
										if(student_parent_srl.equals("0")) {
											friendList.get(i).setName("부모미가입");
											if( ++requestGetParentCounter == this.friendInClassList.size() + this.requestList.size() + this.recommendedFriendList.size()) {
												new Thread(new Runnable() {
												    @Override
												    public void run() {    
												        runOnUiThread(new Runnable(){
												            @Override
												             public void run() {
												            	maker.changeFragment(7);
																friend.setFriendList(friendList);
												            }
												        });
												    }
												}).start();
											}
										}
										else {
											friendList.get(i).setName(parent_srl);
											this.request_Member_getParent(parent_srl);
										}
									}
								}
								break;
							case 'S':
								for(int i = 0; i < friendList.size(); i++) {
									if( member_srl.equals(friendList.get(i).getName()) ) {
										friendList.get(i).setChildname(member_name);
										if(student_parent_srl.equals("0")) {
											friendList.get(i).setName("부모미가입");
											if( ++requestGetParentCounter == this.friendInClassList.size() + this.requestList.size() + this.recommendedFriendList.size()) {
												new Thread(new Runnable() {
												    @Override
												    public void run() {    
												        runOnUiThread(new Runnable(){
												            @Override
												             public void run() {
												            	maker.changeFragment(7);
																friend.setFriendList(friendList);
												            }
												        });
												    }
												}).start();
											}
										}
										else {
											friendList.get(i).setName(student_parent_srl);
											this.request_Member_getParent(student_parent_srl);
										}
									}
								}
								break;
							case 'T':
								break;
							case 'M':
								break;
							}
							
							break;
						}
					}
				}
				else if(uri.equals("Member/modMemberPicture") ) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					final String member_picture = jsonObj.getString("member_picture");
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	switch(modifyMemberPictureTarget) {
					            	case 0:
										((ImageView)findViewById(R.id.mainpicture)).setImageBitmap(ImageMaker.getCroppedBitmap(profilePhoto));
					            		break;
					            	default:
					            		new Thread(new Runnable() {
										    @Override
										    public void run() {    
										        runOnUiThread(new Runnable(){
										            @Override
										             public void run() {
														SlidingMenuMaker.getProfile().getChildren(modifyMemberPictureTarget-1).student_picture = member_picture;
														SlidingMenuMaker.getProfile().getChildren(modifyMemberPictureTarget-1).layout = null;
														maker.menuChildrenAdapter.notifyDataSetChanged();
										            }
										        });
										    }
										}).start();
										break;
					            	}
					            }
					        });
					    }
					}).start();
				}
			}
			else {
			return;
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		}
	}

	public static GalleryFragment getGallery() {
		return gallery;
	}

	public static void setGallery(GalleryFragment gallery) {
		MainActivity.gallery = gallery;
	}
	
	private void setPrevAnimation() {
		noticeViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
		noticeViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
	}
	
	private void setNextAnimation() {
		noticeViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_left));
		noticeViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_left));
	}

	public static MentoryFragment getMentory() {
		return mentory;
	}

	public static void setMentory(MentoryFragment mentory) {
		MainActivity.mentory = mentory;
	}
}
