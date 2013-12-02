package com.ihateflyingbugs.kidsm.menu;

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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.login.LoginActivity;
import com.ihateflyingbugs.kidsm.login.RegisterAddressAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterAddressItem;
import com.ihateflyingbugs.kidsm.login.RegisterChildAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterChildItem;
import com.ihateflyingbugs.kidsm.login.RegisterClassAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterClassItem;
import com.ihateflyingbugs.kidsm.login.RegisterInfoTakerActivity;
import com.ihateflyingbugs.kidsm.login.RegisterInviteTeacherAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterInviteTeacherItem;
import com.ihateflyingbugs.kidsm.login.RegisterOrgAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterOrgItem;
import com.localytics.android.LocalyticsSession;

public class AddChildInfoTakerActivity extends NetworkActivity {
	ViewFlipper flipper;
	Menu menu;
	ArrayList<RegisterOrgItem> orgList_pool;
	int currentPageNumber;
	ArrayList<RegisterOrgItem> orgList;
	RegisterOrgAdapter orgAdapter;
	int indexOfOrg, indexOfClass, indexOfChild;
	RegisterChildAdapter currentChildAdapter;
	
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_infotaker);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		flipper = (ViewFlipper)findViewById(R.id.register_infotaker_flipper);
		orgList_pool = new ArrayList<RegisterOrgItem>();
		this.localyticsSession = new LocalyticsSession(this.getApplicationContext());  // Context used to access device resources
		this.localyticsSession.open();                // open the session
		this.localyticsSession.upload();      // upload any data
		
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			setTitle(getString(R.string.register_find_org));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_org, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_class, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_child, null));
			this.request_Organization_getOrganizations(1, 1000);
			break;
		case 'M':
			break;
		}

		setListResources();
		currentPageNumber = 0;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case android.R.id.home:
			if(currentPageNumber == 0)
				finish();
			else
				goPrevPage();
			return true;
		case R.id.register_onebutton:
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				Intent result = new Intent();
				result.putExtra("org", orgList.get(indexOfOrg).getName());
				result.putExtra("class", orgList.get(indexOfOrg).classList.get(indexOfClass).getName());
				result.putExtra("childname", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getName());
				result.putExtra("birthday", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getBirthday());
				

				result.putExtra("org_srl", orgList.get(indexOfOrg).getOrg_srl());
				result.putExtra("student_srl", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getStudent_srl());
				result.putExtra("member_srl", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getMember_srl());
				result.putExtra("class_srl", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getClass_srl());
				result.putExtra("teacher_srl", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getTeacher_srl());
				result.putExtra("shuttle_srl", orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList().get(indexOfChild).getShuttle_srl());
				setResult(Activity.RESULT_OK, result);
				finish();
				break;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_onebutton, menu);
		this.menu = menu;
		
		MenuItem item = menu.findItem(R.id.register_onebutton);
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			item.setTitle(R.string.finish);
			item.setVisible(false);
			break;
		}
		
		return true;
	}	
	
	private void setListResources() {
		ListView listView;
		EditText editText;
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			editText = (EditText)flipper.findViewById(R.id.register_infotaker_org_search);
			editText.addTextChangedListener(textWatcher);
			listView = (ListView) flipper.findViewById(R.id.register_infotaker_org_list);
			orgList = new ArrayList<RegisterOrgItem>();
			refreshListData();
			orgAdapter = new RegisterOrgAdapter(this, orgList);
			listView.setAdapter(orgAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					indexOfOrg = position;
					ListView classListView = (ListView) flipper.findViewById(R.id.register_infotaker_class_list);
					final ArrayList<RegisterClassItem> selectedClassList = orgList.get(position).classList;
					RegisterClassAdapter classAdapter = new RegisterClassAdapter(AddChildInfoTakerActivity.this, selectedClassList);
					classListView.setAdapter(classAdapter);
					classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							indexOfClass = position;
							ListView childListView = (ListView) flipper.findViewById(R.id.register_infotaker_child_list);
							final ArrayList<RegisterChildItem> selectedChildList = selectedClassList.get(position).getChildList();
							final RegisterChildAdapter childAdapter = new RegisterChildAdapter(AddChildInfoTakerActivity.this, selectedChildList);
							currentChildAdapter = childAdapter;
							childListView.setAdapter(childAdapter);
							childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									indexOfChild = Integer.parseInt(view.getTag().toString());
									MenuItem item = menu.findItem(R.id.register_onebutton);
									//CheckBox cb = (CheckBox)view.findViewById(R.id.register_info_namechecker_checker);
									int index = Integer.parseInt(view.getTag().toString());
									if( selectedChildList.get(index).isChecked() ) {
										selectedChildList.get(index).setChecked(false);
										item.setVisible(false);
									}
									else {
										int count = selectedChildList.size();
										for(int i = 0; i < selectedChildList.size(); i++) {
											//if( selectedChildList.get(i).layout != null ) {
												selectedChildList.get(i).setChecked(false);
											//}
										}
										if( selectedChildList.get(position).getParent_srl().equals("0")) {
											selectedChildList.get(index).setChecked(true);
											item.setVisible(true);
										}
										else {
											item.setVisible(false);
										}
									}
									childAdapter.notifyDataSetChanged();
								}
							});
							goNextPage();
						}
					});
					goNextPage();
					
				}
			});
			break;
		}
	}
	
	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			EditText edit;
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				edit = (EditText)flipper.findViewById(R.id.register_infotaker_org_search);
				if(edit.isFocused()) {
					for(int i = 0; i < orgList_pool.size(); i++) {
						if(orgList_pool.get(i).getName().contains(edit.getText()))
							orgList_pool.get(i).setVisible(true);
						else
							orgList_pool.get(i).setVisible(false);
					}
					refreshListData();
					orgAdapter.notifyDataSetChanged();
				}
				break;
			
			}
		}
		
	};
	
	private void refreshListData() {
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			orgList.clear();
			for(RegisterOrgItem item : orgList_pool) {
				if(item.isVisible()) {
					orgList.add(new RegisterOrgItem(""+item.getOrg_srl(), item.getName(), item.getOrg_address(), item.getOrg_teacher_key(), item.classList));
				}
			}
			break;
		}
	}
	
	public void OnNameCheckerClick(View v) {
		CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(v.getTag().toString());
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			ArrayList<RegisterChildItem> childList = orgList.get(indexOfOrg).classList.get(indexOfClass).getChildList();
			MenuItem item = menu.findItem(R.id.register_onebutton);
			if( cb.isChecked() ) {
				for(int i = 0; i < childList.size(); i++) {
					childList.get(i).setChecked(false);
				}
				if( childList.get(position).getParent_srl().equals("0")) {
					childList.get(position).setChecked(true);
					item.setVisible(true);
					indexOfChild = position;
				}
				else {
					item.setVisible(false);
				}
			}
			else {
				item.setVisible(false);
				childList.get(position).setChecked(false);
			}
			currentChildAdapter.notifyDataSetChanged();
			break;
		}
	}
	
	private void goPrevPage() {
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
		flipper.showPrevious();
		currentPageNumber--;
	}
	
	private void goNextPage() {
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_left));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_left));
		flipper.showNext();
		currentPageNumber++;
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
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Organization/getOrganizations")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String org_srl = dataArray.getJSONObject(i).getString("org_srl");
						String org_name = dataArray.getJSONObject(i).getString("org_name");
						String org_phone = dataArray.getJSONObject(i).getString("org_phone");
						String org_address = dataArray.getJSONObject(i).getString("org_address");
						String org_teacher_key = dataArray.getJSONObject(i).getString("org_teacher_key");
						String org_created = dataArray.getJSONObject(i).getString("org_created");
						String org_updated = dataArray.getJSONObject(i).getString("org_updated");
						orgList_pool.add(new RegisterOrgItem(org_srl, org_name, org_address, org_teacher_key));
						request_Class_getClasses(org_srl, 1, 100);
					}
				}
				else if(uri.equals("Class/getClasses")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String class_srl = dataArray.getJSONObject(i).getString("class_srl");
						String class_org_srl = dataArray.getJSONObject(i).getString("class_org_srl");
						String class_name = dataArray.getJSONObject(i).getString("class_name");
						for(int j = 0; j < orgList_pool.size(); j++) {
							if( orgList_pool.get(j).getOrg_srl().equals(class_org_srl) ) {
								orgList_pool.get(j).addClass(new RegisterClassItem(class_srl, class_name));
								break;
							}
						}
						request_Class_getClassStudent(class_org_srl, class_srl);
					}
				}
				else if(uri.equals("Class/getClassStudent")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					String class_srl = "";
					int numOfStudentHavingParent = 0;
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
						class_srl = student_class_srl;
						if(student_parent_srl.equals("0") == false)
							numOfStudentHavingParent++;
						
						for(int j = 0; j < orgList_pool.size(); j++) {
							if( orgList_pool.get(j).getOrg_srl().equals(member_org_srl) ) {
								for(int k = 0; k < orgList_pool.get(j).classList.size(); k++) {
									if( orgList_pool.get(j).classList.get(k).getClass_srl().equals(student_class_srl)) {
										orgList_pool.get(j).classList.get(k).addChild(new RegisterChildItem(member_srl, student_srl, member_name, member_org_srl, 
												student_class_srl, student_parent_srl, student_teacher_srl, student_shuttle_srl, student_birthday));
										break;
									}
								}
								break;
							}
						}
					
						new Thread(new Runnable() {
						    @Override
						    public void run() {    
						        runOnUiThread(new Runnable(){
						            @Override
						             public void run() {
										refreshListData();
										orgAdapter.notifyDataSetChanged();
						            }
						        });
						    }
						}).start();
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
