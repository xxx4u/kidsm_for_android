package com.ihateflyingbugs.kidsm;


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
import com.ihateflyingbugs.kidsm.mentory.MentoryFragment;
import com.ihateflyingbugs.kidsm.menu.Children;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.newsfeed.NewsfeedFragment;
import com.ihateflyingbugs.kidsm.notice.NoticeActivity;
import com.ihateflyingbugs.kidsm.photonotice.PhotoActivity;
import com.ihateflyingbugs.kidsm.schedule.AddScheduleActivity;
import com.ihateflyingbugs.kidsm.schedule.ScheduleFragment;
import com.ihateflyingbugs.kidsm.settings.SettingsFragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends NetworkActivity {
	SlidingMenuMaker maker;
	static FragmentManager fragmentManager;
	static NewsfeedFragment newsfeed;
	static GalleryFragment gallery;
	static ScheduleFragment schedule;
	static BusInfoFragment businfo;
	static MentoryFragment mentory;
	static SettingsFragment settings;
	static FriendFragment friend;
	private static int currentFragment;
	private static MainActivity main;
	
	ArrayList<FriendListItem> friendList;
	
	int requestGetParentCounter;
	ArrayList<String> teacher_parent_srl_list;

	ArrayList<FriendListItem> requestList;
	ArrayList<FriendListItem> friendInClassList;
	ArrayList<FriendListItem> recommendedFriendList;
	
	int requestGetClassStudentCounter;
	ArrayList<FriendListItem> studentList;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = this;
		setContentView(R.layout.activity_main);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_drawer_btn_p);
		maker = new SlidingMenuMaker();
		Profile profile = getIntent().getParcelableExtra("profile");
		SlidingMenuMaker.setProfile(profile);
		maker.Initiate(this);
		//Toast.makeText(this, ""+profile.getCurrentClass().getNumOfStudentHavingParent(), Toast.LENGTH_SHORT).show();
		fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		newsfeed = new NewsfeedFragment();	// 0
		gallery = new GalleryFragment();	// 1
		schedule = new ScheduleFragment();	// 2
		businfo = new BusInfoFragment();	// 3
		mentory = new MentoryFragment();	// 4
		settings = new SettingsFragment();	// 5
		friend = new FriendFragment();		// 6
		transaction.add(R.id.main_fragment, newsfeed);
		setTitle(R.string.newsfeedactivity);
		transaction.commit();
		setCurrentFragment(0);
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
		return true;
	}

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
			intent = new Intent(this, NoticeActivity.class);
			startActivity(intent);
			return true;
		case R.id.photo:
			intent = new Intent(this, PhotoActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	public static void changeFragment(int id) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(currentFragment == 7)
			main.getActionBar().setIcon(R.drawable.general_actionbar_drawer_btn_p);
		switch(id) {
		case 0:
			transaction.replace(R.id.main_fragment, newsfeed);
			main.setTitle(R.string.newsfeedactivity);
			break;
		case 1:
			transaction.replace(R.id.main_fragment, gallery);
			main.setTitle(R.string.galleryactivity);
			break;
		case 2:
			transaction.replace(R.id.main_fragment, schedule);
			main.setTitle(R.string.scheduleactivity);
			break;
		case 3:
			transaction.replace(R.id.main_fragment, businfo);
			main.setTitle(R.string.busInfoactivity);
			break;
		case 4:
			transaction.replace(R.id.main_fragment, mentory);
			main.setTitle(R.string.mentoryactivity);
			break;
		case 6:
			transaction.replace(R.id.main_fragment, settings);
			main.setTitle(R.string.settingsactivity);
			break;
		case 7:
			transaction.replace(R.id.main_fragment, friend);
			main.setTitle(R.string.friendactivity);
			main.getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		}
		transaction.commit();
		currentFragment = id;
	}

	public static int getCurrentFragment() {
		return currentFragment;
	}

	public static void setCurrentFragment(int currentFragment) {
		MainActivity.currentFragment = currentFragment;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch(currentFragment) {
		case 1:
			gallery.dispatchTouchEvent(event);
			break;
		case 2:
			schedule.dispatchTouchEvent(event);
			break;
		
		}
		return super.dispatchTouchEvent(event);
	}
	
	public void OnLikeClick(View v) {
		newsfeed.OnLikeClick(v);
	}
	
	public void OnReplyClick(View v) {
		newsfeed.OnReplyClick(v);
	}
	
	public void OnScrapClick(View v) {
		newsfeed.OnScrapClick(v);
	}
	
	public void OnImageClick(View v) {
		newsfeed.OnImageClick(v);
	}
	
	public void OnUploadPhoto(View v) {
		gallery.OnUploadPhoto(v);
	}
	
	public void OnNewAlbum(View v) {
		gallery.OnNewAlbum(v);
	}
	
	public void OnSeeAllPhoto(View v) {
		gallery.OnSeeAllPhoto(v);
	}
	
	public void OnSeeAlbum(View v) {
		gallery.OnSeeAlbum(v);
	}
	
	public void OnSeeScrapAlbum(View v) {
		gallery.OnSeeScrapAlbum(v);
	}

	public void OnSettingClick(View v) {
		gallery.OnSettingClick(v);
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
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		if( keyCode == KeyEvent.KEYCODE_BACK) {
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
