package com.ihateflyingbugs.kidsm.schedule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.friend.FriendFragment;
import com.ihateflyingbugs.kidsm.login.RegisterOrgItem;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class AddScheduleActivity extends NetworkActivity {
	int year, month, day;
	ArrayList<String> tagList;
	int getClassStudentCounter;
	int setTimelineMessageCounter;
	String cal_srl;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addschedule);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_function_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		((EditText)findViewById(R.id.addschedule_date)).setText(""+year+"년 "+(month+1)+"월 "+day+"일");
		
		tagList = new ArrayList<String>();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if(((EditText)findViewById(R.id.addschedule_message)).isFocused()) {
				InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
				imm.hideSoftInputFromWindow( findViewById(R.id.addschedule_message).getWindowToken(), 0 );
			}
			finish();
		case R.id.finish:
			Profile profile = SlidingMenuMaker.getProfile();
			String message = ((EditText)findViewById(R.id.addschedule_message)).getText().toString();
			this.request_Calender_setCalender(message, profile.getCurrentClass().getClass_org_srl(), profile.member_srl, profile.getCurrentClass().getClass_srl(), ""+year, ""+(month+1), ""+day, "0:00", "0", "N");
			return true;
		}
		return false;
	}
	
	public void OnSelectDate(View v) {
		final EditText date = (EditText)v;
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int iYear, int iMonth, int iDay) {
				year = iYear;
				month = iMonth;
				day = iDay;
				date.setText(""+year+"년 "+(month+1)+"월 "+day+"일");
			}
		}, year, month, day)
		.show();
	}
	
	private void makeTagList() { 
		getClassStudentCounter = 0;
		setTimelineMessageCounter = 0;
		Profile profile = SlidingMenuMaker.getProfile();
		tagList.clear();
		//tagList.add(profile.member_srl);
		switch(profile.member_type.charAt(0)) {
		case 'T':
			this.request_Organization_getOrganization(profile.member_org_srl);
			break;
		case 'M':
			for(int i = 0; i < profile.classList.size()-1; i++)
				for(int j = 0; j < profile.classList.get(i).getTeacherList().size(); j++)
					tagList.add(profile.classList.get(i).getTeacherList().get(j).teacher_member_srl);
			for(int i = 0; i < profile.classList.size()-1; i++) 
				this.request_Class_getClassStudent(profile.member_org_srl, profile.classList.get(i).getClass_srl());
			break;
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
				if(uri.equals("Calender/setCalender")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String cal_srl = jsonObj.getString("cal_srl");
					String cal_org_srl = jsonObj.getString("cal_org_srl");
					String cal_class_srl = jsonObj.getString("cal_class_srl");
					String cal_member_srl = jsonObj.getString("cal_member_srl");
					String cal_type = jsonObj.getString("cal_type");
					String cal_year = jsonObj.getString("cal_year");
					String cal_month = jsonObj.getString("cal_month");
					String cal_day = jsonObj.getString("cal_day");
					String cal_time = jsonObj.getString("cal_time");
					String cal_timestamp = jsonObj.getString("cal_timestamp");
					String cal_name = jsonObj.getString("cal_name");
					String cal_created = jsonObj.getString("cal_created");
						
					makeTagList();
					this.cal_srl = cal_srl;
					
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
						tagList.add(member_srl);
					}
					
					String targetList = "";
					Profile profile = SlidingMenuMaker.getProfile();
					switch(profile.member_type.charAt(0)) {
					case 'T':
						for(int i = 0; i < tagList.size(); i++)
							targetList += tagList.get(i) + ",";
						this.request_Timeline_setTimelineMessage(SlidingMenuMaker.getProfile().member_srl, "S", cal_srl, targetList);
//						for(int i = 0 ; i < tagList.size(); i++)
//							this.request_Timeline_setTimelineMessage(SlidingMenuMaker.getProfile().member_srl, "S", cal_srl, tagList.get(i));
						break;
					case 'M':
						if( ++getClassStudentCounter == profile.classList.size()-1 ) {
							for(int i = 0 ; i < tagList.size(); i++)
								targetList += tagList.get(i) + ",";
							this.request_Timeline_setTimelineMessage(SlidingMenuMaker.getProfile().member_srl, "S", cal_srl, targetList);
//							for(int i = 0 ; i < tagList.size(); i++)
//								this.request_Timeline_setTimelineMessage(SlidingMenuMaker.getProfile().member_srl, "S", cal_srl, tagList.get(i));
						}
						break;
					}
				}
				else if(uri.equals("Timeline/setTimelineMessage")) {
					//if(++setTimelineMessageCounter == tagList.size()) {
						setResult(Activity.RESULT_OK, new Intent());
						finish();
					//}
				}
				else if(uri.equals("Organization/getOrganization")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String org_srl = jsonObj.getString("org_srl");
					String org_name = jsonObj.getString("org_name");
					String org_manager_member_srl = jsonObj.getString("org_manager_member_srl");
					String org_phone = jsonObj.getString("org_phone");
					String org_address = jsonObj.getString("org_address");
					String org_teacher_key = jsonObj.getString("org_teacher_key");
					String org_created = jsonObj.getString("org_created");
					String org_updated = jsonObj.getString("org_updated");
					tagList.add(org_manager_member_srl);
					this.request_Class_getClassStudent(SlidingMenuMaker.getProfile().member_org_srl, SlidingMenuMaker.getProfile().getCurrentClass().getClass_srl());
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
