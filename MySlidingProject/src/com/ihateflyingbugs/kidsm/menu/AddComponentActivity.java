package com.ihateflyingbugs.kidsm.menu;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.login.RegisterChildItem;
import com.ihateflyingbugs.kidsm.login.RegisterClassItem;
import com.ihateflyingbugs.kidsm.login.RegisterClassMakeAdapter;
import com.ihateflyingbugs.kidsm.login.RegisterOrgItem;
import com.localytics.android.LocalyticsSession;

public class AddComponentActivity extends NetworkActivity {
	LayoutInflater inflater;
	View layout;
	String org_srl = "";
	String student_srl = "";
	String member_srl = "";
	String class_srl = "";
	String parent_srl = "";
	String teacher_srl = "";
	String shuttle_srl = "";
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				setContentView(R.layout.activity_addchild);
				break;
			case 'M':
				setContentView(R.layout.activity_addclass);
				ArrayList<String> classLevelList = new ArrayList<String>();
				Collections.addAll(classLevelList, getResources().getStringArray(R.array.register_teacher_classlevel));
				RegisterClassMakeAdapter classLevelAdapter = new RegisterClassMakeAdapter(this, classLevelList);
				Spinner spinner = (Spinner)findViewById(R.id.addclass_classlevel);
				spinner.setAdapter(classLevelAdapter);
				setTitle(R.string.profile_addclass);
				break;
		}
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_function_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.register_onebutton:
			String formResult = checkForm();
			if(formResult.equals("OK") == false) {
				showDialogMessage(formResult);
				return true;
			}
			switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
			case 'P':
				String orgName = ((EditText)findViewById(R.id.addchild_org)).getText().toString();
				String className = ((EditText)findViewById(R.id.addchild_class)).getText().toString();
				String childName = ((EditText)findViewById(R.id.addchild_childname)).getText().toString();
				String birthday = ((EditText)findViewById(R.id.addchild_birthday)).getText().toString();
				this.request_Member_modStudent(student_srl, member_srl, class_srl, parent_srl, teacher_srl, shuttle_srl, "20"+birthday);
				break;
			case 'M':
				String classname = ((EditText)findViewById(R.id.addclass_newclass)).getText().toString();
				Spinner spinner = (Spinner)findViewById(R.id.addclass_classlevel);
				switch(spinner.getSelectedItemPosition()) {
				case 0:
					this.request_Class_setClass(SlidingMenuMaker.getProfile().member_org_srl, classname, "7");
					break;
				case 1:
					this.request_Class_setClass(SlidingMenuMaker.getProfile().member_org_srl, classname, "6");
					break;
				case 2:
					this.request_Class_setClass(SlidingMenuMaker.getProfile().member_org_srl, classname, "5");
					break;
				case 3:
					this.request_Class_setClass(SlidingMenuMaker.getProfile().member_org_srl, classname, "4");
					break;
				}
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
		return true;
	}	
	
	private void showDialogMessage(String formResult) {
		new AlertDialog.Builder(this)
		.setMessage(formResult)
		.setPositiveButton("확인", null)
		.show();
	}
	
	private boolean checkIsDate(String date) {
		if(date.length() != 6)
			return false;
		try {	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			dateFormat.setLenient(false);
			dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private String checkForm() {
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			if( ((EditText)findViewById(R.id.addchild_org)).getText().toString().isEmpty() )
				return "유치원을 선택해주세요.";
			else if( ((EditText)findViewById(R.id.addchild_birthday)).getText().toString().isEmpty() )
				return "자녀의 생일을 입력해주세요.";
			else if( checkIsDate(((EditText)findViewById(R.id.addchild_birthday)).getText().toString()) == false )
				return "자녀의 생일이 올바르지 않습니다.\n2008년 12월 24일 생일 경우 081224";
			break;
		case 'M':
			if( ((EditText)findViewById(R.id.addclass_newclass)).getText().toString().isEmpty() )
				return "반 이름을 입력해주세요.";
			break;
		}
		return "OK";
	}
	
	public void OnClickForAddInfo(View v) {
		Intent intent = new Intent(this, AddChildInfoTakerActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			switch(requestCode) {
			case 0:
				((EditText)findViewById(R.id.addchild_org)).setText(data.getStringExtra("org"));
				((EditText)findViewById(R.id.addchild_class)).setText(data.getStringExtra("class"));
				((EditText)findViewById(R.id.addchild_childname)).setText(data.getStringExtra("childname"));
				org_srl = data.getStringExtra("org_srl");
				student_srl = data.getStringExtra("student_srl");
				member_srl = data.getStringExtra("member_srl");
				class_srl = data.getStringExtra("class_srl");
				parent_srl = SlidingMenuMaker.getProfile().parent_srl;
				teacher_srl = data.getStringExtra("teacher_srl");
				shuttle_srl = data.getStringExtra("shuttle_srl");
				break;
			}
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
				if(uri.equals("Member/modStudent")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("student_member_srl");
					this.request_Member_getMember(member_srl);
				}
				else if(uri.equals("Member/getMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_type = jsonObj.getString("member_type");
					String member_org_srl = jsonObj.getString("member_org_srl");
					String member_picture = jsonObj.getString("member_picture");
					JSONObject studentObj = jsonObj.getJSONObject("student");
					String student_srl = studentObj.getString("student_srl");
					String student_member_srl = studentObj.getString("student_member_srl");
					String student_class_srl = studentObj.getString("student_class_srl");
					String student_parent_srl = studentObj.getString("student_parent_srl");
					String student_teacher_srl = studentObj.getString("student_teacher_srl");
					String student_shuttle_srl = studentObj.getString("student_shuttle_srl");
					String student_birthday = studentObj.getString("student_birthday");
					String student_parent_key = studentObj.getString("student_parent_key");
					Children children = new Children(student_srl, member_srl, member_name, member_picture, SlidingMenuMaker.getProfile().member_name,
							member_org_srl, student_class_srl, student_parent_srl, student_teacher_srl, 
							student_shuttle_srl, student_birthday, student_parent_key);
					children.organizationName = ((EditText)findViewById(R.id.addchild_org)).getText().toString();
					children.className = ((EditText)findViewById(R.id.addchild_class)).getText().toString();
	            	SlidingMenuMaker.getProfile().addChildrenByRequest(member_srl, children);
	            	
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									Toast.makeText(AddComponentActivity.this, "자녀가 추가되었습니다!", Toast.LENGTH_SHORT).show();
									finish();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Class/setClass")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String class_srl = jsonObj.getString("class_srl");
					String class_org_srl = jsonObj.getString("class_org_srl");
					final String class_name = jsonObj.getString("class_name");
					String class_status = jsonObj.getString("class_status");
	            	SlidingMenuMaker.getProfile().addClassByRequest(class_srl, new OrgClass(class_srl, class_org_srl, class_name));
					
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									Toast.makeText(AddComponentActivity.this, "새로운 반이 생성되었습니다.\n"+class_name, Toast.LENGTH_SHORT).show();
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
