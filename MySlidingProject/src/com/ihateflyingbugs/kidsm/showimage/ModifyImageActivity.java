package com.ihateflyingbugs.kidsm.showimage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.gallery.Album;
import com.ihateflyingbugs.kidsm.gallery.AlbumActivity;
import com.ihateflyingbugs.kidsm.gallery.Photo;
import com.ihateflyingbugs.kidsm.gallery.Album.ALBUMTYPE;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTagActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTagAdapter;
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity.TAGMODE;
import com.localytics.android.LocalyticsSession;

public class ModifyImageActivity extends NetworkActivity {
	ImageLoader imageLoader;
	
	int requestGetClassStudentCounter;
	int requestGetMemberCounter;
	ArrayList<InputTag> tagMemberList;
	String tagValue;
	TAGMODE tagMode;
	InputTagAdapter tagAdapter;
	ListView listView;
	
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyimage);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_function_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);

		tagMode = TAGMODE.SEE_TAGGED_FRIEND;
		tagValue = "";
		tagMemberList = new ArrayList<InputTag>();
		tagAdapter = new InputTagAdapter(this, tagMemberList);
    	listView = (ListView) findViewById(R.id.modifyphoto_taglist);
    	listView.setAdapter(tagAdapter);
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
				onUpdate(Integer.parseInt(((CheckBox)v.findViewById(R.id.tag_check)).getTag().toString()), !((CheckBox)v.findViewById(R.id.tag_check)).isChecked());
			}
			
		});
    	listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				((ScrollView)findViewById(R.id.modifyphoto_scroll)).requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
   
		requestTagData();
		
		
		RadioGroup tagmode = (RadioGroup) findViewById(R.id.modifyphoto_tagmode);
		final RadioButton btn1 = (RadioButton) tagmode.findViewById(R.id.modifyphoto_tagmode_all);
		final RadioButton btn2 = (RadioButton) tagmode.findViewById(R.id.modifyphoto_tagmode_tagged);
		final RadioButton btn3 = (RadioButton) tagmode.findViewById(R.id.modifyphoto_tagmode_private);
		tagmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.BLACK);
				btn3.setTextColor(Color.BLACK);
				RadioButton btn = (RadioButton) group.findViewById(checkedId);
				btn.setTextColor(Color.parseColor("#63C3B1"));
				
				View layout = findViewById(R.id.modifyphoto_tagstatepage);
				switch(checkedId) {
				case R.id.modifyphoto_tagmode_all:
					tagMode = TAGMODE.SEE_ALL_FRIEND;
					layout.setVisibility(View.GONE);
					findViewById(R.id.modifyphoto_tagpage).setVisibility(View.INVISIBLE);
					OnFinishTagging(null);
					break;
				case R.id.modifyphoto_tagmode_tagged:
					tagMode = TAGMODE.SEE_TAGGED_FRIEND;
					layout.setVisibility(View.VISIBLE);
					findViewById(R.id.modifyphoto_tagpage).setVisibility(View.VISIBLE);
					break;
				case R.id.modifyphoto_tagmode_private:
					tagMode = TAGMODE.SEE_PRIVATE;
					layout.setVisibility(View.GONE);
					findViewById(R.id.modifyphoto_tagpage).setVisibility(View.INVISIBLE);
					OnFinishTagging(null);
					break;
				}
			}
			
		});
		
		imageLoader = new ImageLoader(this, R.drawable.photo_in_album_default);
		imageLoader.DisplayImage(getIntent().getStringExtra("photo_url"), (ImageView)findViewById(R.id.modifyphoto_selected));
		
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
				break;
			case 2:
				break;
			}
		}
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
			//String message = ((EditText)findViewById(R.id.modifyphoto_message)).getText().toString();
			
			switch(tagMode) {
			case SEE_ALL_FRIEND:
				this.request_Album_modPhotoTag(getIntent().getStringExtra("photo_srl"), makeAllMemberIncludedTag());
				break;
			case SEE_TAGGED_FRIEND:
				this.request_Album_modPhotoTag(getIntent().getStringExtra("photo_srl"), tagValue);
				break;
			case SEE_PRIVATE:
				this.request_Album_modPhotoTag(getIntent().getStringExtra("photo_srl"), "");
				break;
			}
			return true;
		}
		return false;
	}
	
	private void requestTagData() {
		Profile profile = SlidingMenuMaker.getProfile();
		tagMemberList.clear();
		tagMemberList.add(new InputTag("", "전체보기"));
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
	
	public void OnDeletePhoto(final View v) {
		new AlertDialog.Builder(this)
		.setMessage("정말로 사진을 삭제하시겠습니까?")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String member_srl = "";
				switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
				case 'P':
					member_srl = SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl;
					break;
				case 'T':
				case 'M':
					member_srl = SlidingMenuMaker.getProfile().member_srl;
					break;
				}
				request_Album_delPhoto(getIntent().getStringExtra("photo_srl"), member_srl);
			}
			
		})
		.setNegativeButton("취소", null)
		.show();
	}
	
	private void updateTagValue() {
		tagValue = "";
		for(int i = 1; i < tagMemberList.size(); i++) {
			if( tagMemberList.get(i).isTagged ) {
				tagValue += tagMemberList.get(i).member_srl + ",";
			}
		}
	}
	
	private String makeAllMemberIncludedTag() {
		String tagValue = "";
		for(int i = 1; i < tagMemberList.size(); i++) {
			tagValue += tagMemberList.get(i).member_srl + ",";
		}
		return tagValue;
	}
	
	private void showTaggedList() {
		ArrayList<InputTag> tagList = tagMemberList;
		
		TextView txt = (TextView)findViewById(R.id.modifyphoto_taggedname);
		String taggedName = "";
		ArrayList<String> taggedNameList = new ArrayList<String>();
		for(int i = 1; i < tagList.size(); i++) {
			if( tagList.get(i).isTagged ) {
				taggedNameList.add(tagList.get(i).member_name);
			}
		}
		for(int i = 0; i < taggedNameList.size(); i++) {
			taggedName += taggedNameList.get(i);
			if( i != taggedNameList.size()-1 ) {
				taggedName += ", ";
			}
		}
		updateTagValue();
		
		ImageView img = (ImageView)findViewById(R.id.modifyphoto_tagicon);
		if( taggedName.isEmpty() )
			img.setImageResource(R.drawable.photo_edit_tag_off);
		else 
			img.setImageResource(R.drawable.photo_edit_tag_on);
		txt.setText(taggedName);
	}
	
	public void OnButtonCheck(View v) {
		CheckBox cb = (CheckBox)v;
		boolean isChecked = cb.isChecked();
		int position = (Integer) cb.getTag();
		onUpdate(position, isChecked);
	}
	
	public void onUpdate(final int position, boolean checker) {
		if(position == 0) {
			for(int i = 0; i < tagMemberList.size(); i++) {
				tagMemberList.get(i).isTagged = checker;
			}
		}
		else {
			tagMemberList.get(position).isTagged = checker;
			if(checker == false)
				tagMemberList.get(0).isTagged = checker;
			else {
				boolean isAllChecked = true;
				for(int i = 1; i < tagMemberList.size(); i++) {
					if( tagMemberList.get(i).isTagged == false ) {
						isAllChecked = false;
						break;
					}
				}
				if(isAllChecked)
					tagMemberList.get(0).isTagged = checker;
			}
		}
		tagAdapter.notifyDataSetChanged();
		showTaggedList();
	}
	
	public void OnSelectImage(final View v) {
		if( tagMode == TAGMODE.SEE_TAGGED_FRIEND) {
			findViewById(R.id.modifyphoto_tagpage).setVisibility(View.VISIBLE);
		}
	}
	
	public void OnFinishTagging(View v) {
		findViewById(R.id.modifyphoto_tagpage).setVisibility(View.INVISIBLE);
	}
	
	private void setTagMemberData() { 
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	tagAdapter.notifyDataSetChanged();
		            }
		        });
		    }
		}).start();
		
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
//				if(uri.equals("Album/setPhoto")) {
//					String nativeData = jsonObj.getString("data");
//					jsonObj = new JSONObject(nativeData);
//					String photo_srl = jsonObj.getString("photo_srl");
//					String photo_member_srl = jsonObj.getString("photo_member_srl");
//					String photo_album_srl = jsonObj.getString("photo_album_srl");
//					String photo_timeline_srl = jsonObj.getString("photo_timeline_srl");
//					String photo_tag = jsonObj.getString("photo_tag");
//					String photo_path = jsonObj.getString("photo_path");
//					String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
//					String photo_like = jsonObj.getString("photo_like");
//					String photo_private = jsonObj.getString("photo_private");
//					String photo_created = jsonObj.getString("photo_created");
//					String photo_updated = jsonObj.getString("photo_updated");
//					uploadedPhotoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
//					
//					String targetList = "";
//					isRequestAlarmingMember = true;
//					ArrayList<String> tagList = this.makeTagListForTimeline(photo_tag);
//					alarmingCounter = tagList.size();
//					for(int i = 0; i < tagList.size(); i++) {
//						targetList += tagList.get(i) + ",";
//						this.request_Member_getMember(tagList.get(i));
//					}
//					this.request_Timeline_setTimelineMessage(photo_member_srl, "P", photo_srl, targetList);
//				}
//				else if(uri.equals("Timeline/setTimelineMessage")) {
//					String nativeData = jsonObj.getString("data");
//					JSONArray dataArray = new JSONArray(nativeData);
//					for(int i = 0; i < dataArray.length(); i++ ) {
//						JSONObject dataObj = dataArray.getJSONObject(i);
//						String timeline_type = dataObj.getString("timeline_type");
//						String timeline_srl = dataObj.getJSONObject("_id").getString("$oid");
//						String timeline_member_srl = dataObj.getString("timeline_member_srl");
//						String timeline_target_member_srl = dataObj.getString("timeline_target_member_srl");
//						String timeline_target_srl = dataObj.getString("timeline_target_srl");
//						String timeline_created = dataObj.getString("timeline_created");
//						for(int j = 0; j < uploadedPhotoList.size(); j++) {
//							if(uploadedPhotoList.get(j).photo_srl.equals(timeline_target_srl)) {
//								uploadedPhotoList.get(j).photo_timeline_srl = timeline_srl;
//								break;
//							}
//						}
//						this.request_Album_setPhotoTimeline(timeline_target_srl, timeline_srl);
//						break;
//					}
//					//}
//				}
//				else if(uri.equals("Album/setPhotoTimeline")) {
//					new Thread(new Runnable() {
//					    @Override
//					    public void run() {    
//					        runOnUiThread(new Runnable(){
//					            @Override
//					             public void run() {
//									sendCounter++;
//									isFinished();
//					            }
//					        });
//					    }
//					}).start();
//				}
//				else if(uri.equals("Album/setAlbum")) {
//					String nativeData = jsonObj.getString("data");
//					jsonObj = new JSONObject(nativeData);
//					String album_srl = jsonObj.getString("album_srl");
//					String album_member_srl = jsonObj.getString("album_member_srl");
//					final String album_name = jsonObj.getString("album_name");
//					String album_type = jsonObj.getString("album_type");
//					String album_created = jsonObj.getString("album_created");
//					String album_updated = jsonObj.getString("album_updated");
//					String album_count = jsonObj.getString("album_count");
//					current_album_srl = album_srl;
//					albumList.add(albumList.size()-1, new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
//					newAlbumList.add(new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
//					new Thread(new Runnable() {
//					    @Override
//					    public void run() {    
//					        runOnUiThread(new Runnable(){
//					            @Override
//					             public void run() {
//									albumListAdapter.notifyDataSetChanged();
//									spinner.setSelection(albumList.size()-2);
//									albumListAdapter.notifyDataSetChanged();
//					            }
//					        });
//					    }
//					}).start();
//				}
				if(uri.equals("Member/getFriends")) {
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
					String member_type = jsonObj.getString("member_type");
					tagMemberList.add(new InputTag(member_srl, member_name));
					if(--requestGetMemberCounter == 0) {
						setTagMemberData();
					}
				}
				else if(uri.equals("Album/delPhoto")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_member_srl = jsonObj.getString("photo_member_srl");
					String photo_timeline_srl = jsonObj.getString("photo_timeline_srl");
					this.request_Timeline_delTimelineMessage(photo_member_srl, photo_timeline_srl);
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					        		new AlertDialog.Builder(ModifyImageActivity.this)
					        		.setMessage("사진이 삭제되었습니다.")
					        		.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					        			@Override
					        			public void onClick(DialogInterface dialog, int which) {
					        			}
					        			
					        		})
					        		.show();
					        		finish();
					            }
					        });
					    }
					}).start();
				}
//				else if(uri.equals("Member/getParent")) {
//					String nativeData = jsonObj.getString("data");
//					jsonObj = new JSONObject(nativeData);
//					String member_srl = jsonObj.getString("member_srl");
//					switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
//					case 'P':
//						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 태그 알림", SlidingMenuMaker.getProfile().member_name+"학부모님이 회원님이 태그된 사진을 업로드했습니다.", "P");
//						break;
//					case 'T':
//						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 태그 알림", SlidingMenuMaker.getProfile().member_name+"선생님이 회원님이 태그된 사진을 업로드했습니다.", "P");
//						break;
//					case 'M':
//						this.request_Service_notify_sendNotify(SlidingMenuMaker.getProfile().member_srl, member_srl, "사진 태그 알림", SlidingMenuMaker.getProfile().member_name+"원장선생님이 회원님이 태그된 사진을 업로드했습니다.", "P");
//						break;
//					}
//					new Thread(new Runnable() {
//					    @Override
//					    public void run() {    
//					        runOnUiThread(new Runnable(){
//					            @Override
//					             public void run() {
//									alarmingCounter--;
//									isFinished();
//					            }
//					        });
//					    }
//					}).start();
//				}
				else if(uri.equals("Album/modPhotoTag")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									Toast.makeText(ModifyImageActivity.this, "태그가 수정되었습니다.", Toast.LENGTH_SHORT).show();
									finish();
					            }
					        });
					    }
					}).start();
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
