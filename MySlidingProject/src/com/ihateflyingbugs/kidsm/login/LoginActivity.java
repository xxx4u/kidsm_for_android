package com.ihateflyingbugs.kidsm.login;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gcm.GCMRegistrar;
import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkActivity;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.Children;
import com.ihateflyingbugs.kidsm.menu.OrgClass;
import com.ihateflyingbugs.kidsm.menu.OrgClassTeacher;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity;
import com.localytics.android.LocalyticsSession;

public class LoginActivity extends NetworkActivity {

	ViewFlipper flipper;
	Dialog dialog;
	int currentRegisterMode;
	int currentPageNumber;
	SharedPreferences prefs;
	int currentChildFormCount;
	static ArrayList<RegisterOrgItem> orgList_pool;
	ArrayList<RegisterChildItem> registerChildList;
	int setStudentInfoCounter;
	String registerNewClassTeacher_member_srl;
	Profile profile;
	int parent_addchild_org_counter, parent_addchild_class_counter, parent_addchild_teacher_counter;
	int manager_addclass_org_counter, manager_addclass_teacher_counter;
	boolean isAddChildCompleted;
	boolean isAddClassCompleted;
	boolean isRequestClassList;
	boolean isLoginRequested;
	int teacherGetClassStudentCounter;
	String register_parent_member_srl;
	private LocalyticsSession localyticsSession;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		flipper = (ViewFlipper)findViewById(R.id.register_flipper);
		prefs = getSharedPreferences("login", MODE_PRIVATE);
		orgList_pool = new ArrayList<RegisterOrgItem>();
		registerChildList = new ArrayList<RegisterChildItem>();
		registerChildList.add(null);
		setStudentInfoCounter = 0;
		parent_addchild_org_counter = 0;
		parent_addchild_class_counter = 0;
		parent_addchild_teacher_counter = 0;
		isAddChildCompleted = false;
		isAddClassCompleted = false;
		isRequestClassList = false;
		isLoginRequested = false;
		teacherGetClassStudentCounter = 0;
		
		String member_email = prefs.getString("member_email", "");
		if(member_email.isEmpty() == false) {
			EditText email = (EditText) findViewById(R.id.login_email);
			EditText passwd = (EditText) findViewById(R.id.login_password);
			String member_password = prefs.getString("member_password", "");
			email.setText(member_email);
			passwd.setText(member_password);
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("member_email", email));
//			params.add(new BasicNameValuePair("member_password", passwd));
//			POST("Member/login", params);
		}
		
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
	
	public void OnLoginClick(View v) {
		if( CheckLoginForm() ) {
			CheckBox cb = (CheckBox) findViewById(R.id.login_automatically);
			EditText email = (EditText) findViewById(R.id.login_email);
			EditText passwd = (EditText) findViewById(R.id.login_password);
			if( cb.isChecked() ) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("member_email", email.getText().toString());
				editor.putString("member_password", passwd.getText().toString());
				editor.commit();
			}
			else {
				SharedPreferences.Editor editor = prefs.edit();
				editor.clear();
				editor.commit();
			}

			if( isLoginRequested ) {
				showDialogMessage("로그인 진행중입니다");
			}
			else {
				request_Member_login(email.getText().toString(), passwd.getText().toString());
				isLoginRequested = true;
			}
		}
	}
	
	public boolean CheckLoginForm() {
		EditText txt;
		String status = "", data;
		txt = (EditText) findViewById(R.id.login_email);
		data = txt.getText().toString();
		if(data.isEmpty())
			status = "아이디를 입력해 주세요.";
		else if(checkEmailLevel(data) == false)
			status = "올바른 메일 형식이 아닙니다.";
		txt = (EditText) findViewById(R.id.login_password);
		data = txt.getText().toString();
		if(data.isEmpty())
			status = "비밀번호를 입력해주세요.";
		
		if( status.isEmpty() )
			return true;
		else {
			showDialogMessage(status);
			return false;
		}
	}
	
	public void OnForgotPasswordClick(View v) {
			
	}

	public void OnMemberRegisterClick(View v) {
		currentPageNumber = 0;
		EditText editText = (EditText)findViewById(R.id.login_email);
		if(editText.isFocused())
			setKeyboardDown(editText);
		else
			editText = (EditText)findViewById(R.id.login_password);
		if(editText.isFocused())
			setKeyboardDown(editText);
		goNextPage();
	}
	
	public void OnSelectRegisterMode(View v) {
		if( flipper.getChildCount() > 3 )
			flipper.removeViews(3, flipper.getChildCount()-3);
		int tag = Integer.parseInt(v.getTag().toString());
		if(tag == 2)
			return;
		currentRegisterMode = tag;
		switch(currentRegisterMode){
		case 1:
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_parent, null));
			currentChildFormCount = 0;
			break;
		case 2:
			goPrevPage();
			break;
		case 3:
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_teacher, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_teacher_selectclass, null));
			break;
		case 4:
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_orgmaster, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_finish_orgmaster, null));
			break;
		}
		goNextPage();
//		Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				EditText editText = (EditText)findViewById(R.id.register_email);
//				editText.requestFocus();
//				setKeyboardUp(editText);
//			}
//		};
//		handler.sendEmptyMessageDelayed(0, 100);
	}
	
	public void OnDefaultInfoFormed(View v) {
		String formResult = checkDefaultForm();
		if(formResult.equals("OK") == false) {
			showDialogMessage(formResult);
			return;
		}
		EditText editText = (EditText)findViewById(R.id.register_email);
		if(editText.isFocused())
			setKeyboardDown(editText);
		else
			editText = (EditText)findViewById(R.id.register_password);
		if(editText.isFocused())
			setKeyboardDown(editText);
		else
			editText = (EditText)findViewById(R.id.register_confirmpassword);
		if(editText.isFocused())
			setKeyboardDown(editText);
		
		switch(currentRegisterMode) {
		case 1:
			editText = (EditText) flipper.findViewById(R.id.register_parent_org);
			editText.setKeyListener(null);
			editText = (EditText) flipper.findViewById(R.id.register_parent_class);
			editText.setKeyListener(null);
//			editText = (EditText) flipper.findViewById(R.id.register_parent_childname);
//			editText.setKeyListener(null);
			
			request_Organization_getOrganizations(1, 1000);
			break;
		case 2:
			break;
		case 3:
			ArrayList<String> classLevelList = new ArrayList<String>();
			Collections.addAll(classLevelList, getResources().getStringArray(R.array.register_teacher_classlevel));
			RegisterClassMakeAdapter classLevelAdapter = new RegisterClassMakeAdapter(this, classLevelList);
			Spinner spinner = (Spinner)findViewById(R.id.register_teacher_classlevel);
			spinner.setAdapter(classLevelAdapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, final int position, long id) {
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			request_Organization_getOrganizations(1, 1000);
			break;
		case 4:
			editText = (EditText) flipper.findViewById(R.id.register_orgmaster_orglocation);
			editText.setKeyListener(null);
			break;
		}
		editText = (EditText)findViewById(R.id.register_email);
		request_Member_checkEmail(editText.getText().toString());
	}
	
	public void OnSpecificInfoFormed(View v) {
		String formResult = checkSpecificForm();
		if(formResult.equals("OK") == false) {
			showDialogMessage(formResult);
			return;
		}
		
		EditText editText;
		String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
		String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
		switch(currentRegisterMode){
		case 1:
			String parent_name = ((EditText)findViewById(R.id.register_parent_name)).getText().toString();
			String parent_nickname = ((EditText)findViewById(R.id.register_parent_nickname)).getText().toString();
			String parent_phone = ((EditText)findViewById(R.id.register_parent_phone)).getText().toString();
			
			//this.request_Member_addMember(parent_name, parent_nickname, "P", "1", email, password, "I", "nope");
			editText = (EditText)findViewById(R.id.register_parent_name);
			if(editText.isFocused())
				setKeyboardDown(editText);
			else
				editText = (EditText)findViewById(R.id.register_parent_nickname);
			if(editText.isFocused())
				setKeyboardDown(editText);
			else
				editText = (EditText)findViewById(R.id.register_parent_phone);
			if(editText.isFocused())
				setKeyboardDown(editText);
			// TODO
			//가입요청중입니다
			this.request_Member_addMember(parent_name, parent_nickname, "P", "0", email, password, "A", GCMRegistrar.getRegistrationId(this));
			break;
		case 2:
			break;
		case 3:
			String teacher_name = ((EditText)findViewById(R.id.register_teacher_name)).getText().toString();
			String teacher_phone = ((EditText)findViewById(R.id.register_teacher_phone)).getText().toString();
			String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
			boolean findOrg = false;
			for(int i = 0; i < orgList_pool.size(); i++) {
				if(teacher_orgcode.equals(orgList_pool.get(i).org_teacher_key)) {
					dialog = new Dialog(this, R.style.TransparentDialog);
					View layout = LayoutInflater.from(this).inflate(R.layout.register_confirm_teacher_dialog, null);
					TextView txt = (TextView) layout.findViewById(R.id.register_confirm_teacher_name);
					txt.setText(teacher_name);
					txt = (TextView) layout.findViewById(R.id.register_confirm_teacher_orgname);
					txt.setText(orgList_pool.get(i).getName()+"("+orgList_pool.get(i).org_address+")");
					dialog.setContentView(layout);
					dialog.show();
					findOrg = true;
					break;
				}
			}
			if( findOrg == false ) {
				showDialogMessage("기관 코드가 올바르지 않습니다.");
			}
			break;
		case 4:
			String orgmaster_name = ((EditText)findViewById(R.id.register_orgmaster_name)).getText().toString();
			String orgmaster_phone = ((EditText)findViewById(R.id.register_orgmaster_phone)).getText().toString();
			String orgmaster_org = ((EditText)findViewById(R.id.register_orgmaster_org)).getText().toString();
			String orgmaster_orglocation = ((EditText)findViewById(R.id.register_orgmaster_orglocation)).getText().toString();
			this.request_Member_addMember(orgmaster_name, orgmaster_name, "M", "1", email, password, "A", GCMRegistrar.getRegistrationId(this));
			editText = (EditText)findViewById(R.id.register_orgmaster_name);
			if(editText.isFocused())
				setKeyboardDown(editText);
			else
				editText = (EditText)findViewById(R.id.register_orgmaster_phone);
			if(editText.isFocused())
				setKeyboardDown(editText);
			else
				editText = (EditText)findViewById(R.id.register_orgmaster_org);
			if(editText.isFocused())
				setKeyboardDown(editText);
			else
				editText = (EditText)findViewById(R.id.register_orgmaster_org);
			if(editText.isFocused())
				setKeyboardDown(editText);
			break;
		}
	}
	
	public void OnAddChildForm(View v) {
		currentChildFormCount++;
		registerChildList.add(null);
		LinearLayout layout = (LinearLayout) flipper.findViewById(R.id.register_parent_form);
		LinearLayout childLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.register_parent_addchild, null);
		View childform = childLayout.findViewById(R.id.register_parent_org);
		childform.setTag("register_parent_org_"+currentChildFormCount);
		childform = childLayout.findViewById(R.id.register_parent_class);
		childform.setTag("register_parent_class_"+currentChildFormCount);
		childform = childLayout.findViewById(R.id.register_parent_childname);
		childform.setTag("register_parent_childname_"+currentChildFormCount);
		childform = childLayout.findViewById(R.id.register_parent_birthday);
		childform.setTag("register_parent_birthday_"+currentChildFormCount);
		layout.addView(childLayout);
	}
	
	private boolean checkEmailLevel(String email) {
		if( email.contains(" ") || email.contains("@") == false )
			return false;
		String[] parts = email.split("@");
		if( parts.length != 2 )
			return false;
		if( parts[1].isEmpty() || parts[1].contains(".") == false )
			return false;
		return true;
	}
	
	private boolean checkPasswordLevel(String password) {
		int digitCounter = 0;
		int alphaCounter = 0;
		for( int i = 0; i < password.length(); i++ ) {
			char c = password.charAt(i);
			if( Character.isDigit(c) )
				digitCounter++;
			else if( Character.isLetter(c) )
				alphaCounter++;
		}
		if( digitCounter == 0 || alphaCounter == 0 )
			return false;
		return true;
	}
	
	private boolean hasDigit(String string) {
		for( int i = 0; i < string.length(); i++ )
			if( Character.isDigit(string.charAt(i)) )
				return true;
		return false;
	}
	
	private boolean checkPhoneLevel(String phone) {
		for( int i = 0; i < phone.length(); i++ )
			if( Character.isDigit(phone.charAt(i)) == false )
				return false;
		return true;
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
	private String checkDefaultForm() {
		String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
		String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
		String confirmpassword = ((EditText)findViewById(R.id.register_confirmpassword)).getText().toString();
		if(email.isEmpty())
			return "이메일을 입력해주세요.";
		if( checkEmailLevel(email) == false )
			return "올바른 메일 형식이 아닙니다.";
		if( password.isEmpty() )
			return "비밀번호를 입력해주세요.";
		if( confirmpassword.isEmpty() )
			return "비밀번호를 입력해주세요.";
		if( password.length() < 8 )
			return "비밀번호는 8자 이상 설정해주세요.";
		if( checkPasswordLevel(password) == false )
			return "비밀번호는 영문과 숫자를 혼합해서 설정해주세요.";
		else if( password.equals(confirmpassword) == false )
			return "비밀번호가 일치하지 않습니다.";
		return "OK";
	}

	private String checkSpecificForm() {
		switch(currentRegisterMode) {
		case 1:
			String parent_name = ((EditText)findViewById(R.id.register_parent_name)).getText().toString();
			String parent_nickname = ((EditText)findViewById(R.id.register_parent_nickname)).getText().toString();
			String parent_phone = ((EditText)findViewById(R.id.register_parent_phone)).getText().toString();
			if( parent_name.isEmpty() )
				return "이름을 입력해주세요.";
			if( hasDigit(parent_name) )
				return "이름에는 숫자가 들어갈 수 없습니다.";
			if( parent_nickname.isEmpty() )
				return "닉네임을 입력해주세요.";
			if( parent_phone.isEmpty() )
				return "휴대폰 번호를 입력해주세요.";
			if( checkPhoneLevel(parent_phone) == false )
				return "휴대폰 번호는 숫자만 입력가능합니다.";
			LinearLayout layout = (LinearLayout)findViewById(R.id.register_parent_form);
			for(int i = 0; i < currentChildFormCount+1; i++) {
				String parent_org = ((EditText)layout.findViewWithTag("register_parent_org_"+i)).getText().toString();
				String parent_class = ((EditText)layout.findViewWithTag("register_parent_class_"+i)).getText().toString();
				String parent_childname = ((EditText)layout.findViewWithTag("register_parent_childname_"+i)).getText().toString();
				String birthday = ((EditText)layout.findViewWithTag("register_parent_birthday_"+i)).getText().toString();
				String preCounter = "";
				if( currentChildFormCount != 0 )
					preCounter = ""+(i+1)+"번째 ";
//				if(currentChildFormCount != 0 && parent_org.isEmpty())
//					return preCounter+"자녀의 기관을 선택해주세요.";
				if(parent_childname.isEmpty())
					return preCounter+"자녀의 이름을 입력해주세요.";
				if( hasDigit(parent_childname) )
					return "이름에는 숫자가 들어갈 수 없습니다.";
				if(birthday.isEmpty())
					return preCounter+"자녀의 생일을 입력해주세요.";
				if(checkIsDate(birthday) == false)
					return preCounter+"자녀의 생일이 올바르지 않습니다.\n2008년 12월 24일 생일 경우 081224";
			}
			break;
		case 2:
			break;
		case 3:
			String teacher_name = ((EditText)findViewById(R.id.register_teacher_name)).getText().toString();
			String teacher_phone = ((EditText)findViewById(R.id.register_teacher_phone)).getText().toString();
			String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
			if( teacher_name.isEmpty() )
				return "이름을 입력해주세요.";
			if( hasDigit(teacher_name) )
				return "이름에는 숫자가 들어갈 수 없습니다.";
			if( teacher_phone.isEmpty() )
				return "휴대폰 번호를 입력해주세요.";
			if( checkPhoneLevel(teacher_phone) == false )
				return "휴대폰 번호는 숫자만 입력가능합니다.";
			if( teacher_orgcode.isEmpty() )
				return "기관 코드를 입력해주세요.";
			break;
		case 4:
			String orgmaster_name = ((EditText)findViewById(R.id.register_orgmaster_name)).getText().toString();
			String orgmaster_phone = ((EditText)findViewById(R.id.register_orgmaster_phone)).getText().toString();
			String orgmaster_org = ((EditText)findViewById(R.id.register_orgmaster_org)).getText().toString();
			String orgmaster_orglocation = ((EditText)findViewById(R.id.register_orgmaster_orglocation)).getText().toString();
			if( orgmaster_name.isEmpty() )
				return "이름을 입력해주세요.";
			if( hasDigit(orgmaster_name) )
				return "이름에는 숫자가 들어갈 수 없습니다.";
			if( orgmaster_phone.isEmpty() )
				return "휴대폰 번호를 입력해주세요.";
			if( checkPhoneLevel(orgmaster_phone) == false )
				return "휴대폰 번호는 숫자만 입력가능합니다.";
			if( orgmaster_org.isEmpty() )
				return "기관 이름을 입력해주세요.";
			if( orgmaster_orglocation.isEmpty() )
				return "기관 위치를 입력해주세요.";
			break;
		}
		return "OK";
	}
	
	public void OnConfirmDialog(View v) {
		dialog.cancel();
	}
	
	public void OnConfirmTeacherResponse(View v) {
		dialog.cancel();
		switch(v.getId()) {
		case R.id.confirm_teacher_ok:
			ArrayList<String> classList = new ArrayList<String>();
			String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
			for(int i = 0; i < orgList_pool.size(); i++) {
				if(teacher_orgcode.equals(orgList_pool.get(i).org_teacher_key)) {
					for(int j = 0; j < orgList_pool.get(i).classList.size(); j++) {
						classList.add(orgList_pool.get(i).classList.get(j).getName());
					}
					break;
				}
			}
			RegisterClassMakeAdapter classAdapter = new RegisterClassMakeAdapter(this, classList);
			Spinner spinner = (Spinner)findViewById(R.id.register_teacher_classlist);
			spinner.setAdapter(classAdapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, final int position, long id) {
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			goNextPage();
			break;
		case R.id.confirm_teacher_cancel:
			break;
		}
	}

	public void OnFinishSelectClass(View v) {
		String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
		for(int i = 0; i < orgList_pool.size(); i++) {
			if(teacher_orgcode.equals(orgList_pool.get(i).org_teacher_key)) {
				String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
				String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
				String teacher_name = ((EditText)findViewById(R.id.register_teacher_name)).getText().toString();
				this.request_Member_addMember(teacher_name, teacher_name, "T", orgList_pool.get(i).org_srl, email, password, "A", GCMRegistrar.getRegistrationId(this));
				break;
			}
		}
	}
	
	public void OnRequestNewClassComplete(View v) {
		dialog.cancel();
		goNextPage();
	}
	
	public void OnClickForAddInfo(View v) {
		Intent intent = new Intent(this, RegisterInfoTakerActivity.class);
		switch(v.getId()) {
		case R.id.register_parent_org:
			intent.putExtra("type", 0);
			intent.putExtra("title", getString(R.string.register_find_org));
			intent.putExtra("formNumber", Integer.parseInt(v.getTag().toString().substring(v.getTag().toString().length()-1)));
			startActivityForResult(intent, 0);
			break;
		//case R.id.register_parent_class:
		//	break;
		//case R.id.register_parent_childname:
		//	break;
		//case R.id.register_parent_birthday:
		//	break;
		case R.id.register_orgmaster_orglocation:
			intent.putExtra("type", 1);
			intent.putExtra("title", getString(R.string.register_find_orglocation));
			startActivityForResult(intent, 1);
			break;
		}
		
	}
	
	public void OnInviteTeacherClick(View v) {
		Intent intent = new Intent(this, RegisterInfoTakerActivity.class);
		intent.putExtra("type", 2);
		intent.putExtra("title", getString(R.string.register_invite_teacher));
		startActivityForResult(intent, 2);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			TextView text;
			switch(requestCode) {
			case 0:
				int formNumber = data.getIntExtra("formNumber", -1);
				if(formNumber == -1)
					break;
				text = (TextView) flipper.findViewWithTag("register_parent_org_"+formNumber);
				text.setText(data.getStringExtra("org"));
				text = (TextView) flipper.findViewWithTag("register_parent_class_"+formNumber);
				text.setText(data.getStringExtra("class"));
				text = (TextView) flipper.findViewWithTag("register_parent_childname_"+formNumber);
				text.setText(data.getStringExtra("childname"));
				int index_org = data.getIntExtra("index_org", -1);
				int index_class = data.getIntExtra("index_class", -1);
				int index_child = data.getIntExtra("index_child", -1);
				registerChildList.set(formNumber, orgList_pool.get(index_org).classList.get(index_class).childList.get(index_child));
					
//				text = (TextView) flipper.findViewById(R.id.register_parent_birthday);
//				text.setText(data.getStringExtra("birthday"));
				break;
			case 1:
				//String address = data.getStringExtra("address");
				text = (TextView) flipper.findViewById(R.id.register_orgmaster_orglocation);
				text.setText(data.getStringExtra("address"));
				break;
			case 2:
				ArrayList phoneList = data.getStringArrayListExtra("phoneList");
				String phoneListString = "";
				for(int i = 0; i < phoneList.size(); i++) {
					phoneListString += phoneList.get(i);
					if( i != phoneList.size()-1)
						phoneListString += ";";
				}
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+ phoneListString));
				intent.putExtra("sms_body", "[KIDSM]" + ((EditText)findViewById(R.id.register_orgmaster_name)).getText().toString() +" 원장님께서 선생님을 " + ((EditText)findViewById(R.id.register_orgmaster_org)).getText().toString()
						+"으로 초대하셨습니다. \n\n안드로이드 앱 다운로드 :\nhttps://play.google.com/store/apps/details?id=com.ihateflyingbugs.kidsm\n기관 초대코드 : " + ((TextView)findViewById(R.id.register_finish_orgcode)).getText().toString());
				startActivity(intent);
				this.showDialogMessage("초대를 완료했습니다!");
				goNextPage();
				break;
			case 3:
				ArrayList phoneList2 = data.getStringArrayListExtra("phoneList");
				String phoneListString2 = "";
				for(int i = 0; i < phoneList2.size(); i++) {
					phoneListString2 += phoneList2.get(i);
					if( i != phoneList2.size()-1)
						phoneListString2 += ";";
				}
				Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+ phoneListString2));
				intent2.putExtra("sms_body", "[KIDSM]" + ((EditText)findViewById(R.id.register_teacher_name)).getText().toString() +" 선생님께서 학부모님을 키즈엠 앱으로 초대하셨습니다. \n\n안드로이드 앱 다운로드 :\nhttps://db.tt/09P0dxrz\n아이폰 앱 다운로드 :\nhttps://db.tt/09P0dxrz");
				startActivity(intent2);
				this.showDialogMessage("초대를 완료했습니다!");
				goNextPage();
				break;
			}
		}
	}
	
	private void setKeyboardUp(EditText editText) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, 0);
	}
	
	private void setKeyboardDown(EditText editText) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		if( keyCode == KeyEvent.KEYCODE_BACK) {
			if( currentPageNumber != 0 )
				goPrevPage();
			else
				return super.onKeyDown(keyCode, event);
        	return true;
        }
		else
			return super.onKeyDown(keyCode, event);
	}
	
	private void showDialogMessage(String message) {
		View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_checker_dialog, null);
		TextView textView = (TextView)v.findViewById(R.id.login_checker_message);
		textView.setText(message);
		dialog = new Dialog(LoginActivity.this, R.style.TransparentDialog);
		dialog.setContentView(v);
		dialog.show();
	}
	
	// you just activated my trap card..... plz fix it...
	void tempGoMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("profile", profile);
		intent.putExtra("auth_key", auth_key);
		startActivity(intent);
		finish();
		overridePendingTransition(0, android.R.anim.fade_out);
	}
	
	void goMainActivity() {
		this.request_Member_getMember(profile.org_manager_member_srl);
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Member/login")) {
					String nativeData = jsonObj.getString("data");
					auth_key = jsonObj.getString("key");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_type = jsonObj.getString("member_type");
					String member_org_srl = jsonObj.getString("member_org_srl");
					String member_point = jsonObj.getString("member_point");
					String member_email = jsonObj.getString("member_email");
					String member_picture = jsonObj.getString("member_picture");
					String member_device_type = jsonObj.getString("member_device_type");
					String member_device_uuid = jsonObj.getString("member_device_uuid");
					if(GCMRegistrar.getRegistrationId(this).equals(member_device_uuid) == false) {
						this.request_Member_modMember(member_srl, member_name, member_name, member_org_srl, member_email, ((EditText)findViewById(R.id.register_password)).getText().toString(), member_device_type, GCMRegistrar.getRegistrationId(this), "A");
					}
					profile = new Profile(member_srl, member_name, member_type, member_org_srl, member_point, 
							member_email, member_picture, member_device_type, member_device_uuid, "");
					if( member_type.charAt(0) == 'P' ) {
						JSONObject parentObj = jsonObj.getJSONObject("parent");
						String parent_srl = parentObj.getString("parent_srl");
						this.request_Member_getParentStudents(parent_srl);
					}
					else if( member_type.charAt(0) == 'T' ) {
						isRequestClassList = true;
						this.request_Class_getClasses(member_org_srl, 1, 100);
					}
					else if( member_type.charAt(0) == 'M' ) {
						isRequestClassList = true;
						this.request_Class_getClasses(member_org_srl, 1, 100);
					}
				}
				else if(uri.equals("Member/checkEmail")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									goNextPage();
									if(currentRegisterMode == 1)
										LoginActivity.this.showDialogMessage("자신의 자녀가 기관 원생이 아닐경우 자녀명/생년월일만 입력후 다음단계로 넘어가세요.");
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Organization/getOrganizations")) {
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
					if(dataArray.length() == 0) {
						this.request_Organization_getOrganization(profile.member_org_srl);
					}
					else {
						for(int i = 0; i < dataArray.length(); i++) {
							String class_srl = dataArray.getJSONObject(i).getString("class_srl");
							String class_org_srl = dataArray.getJSONObject(i).getString("class_org_srl");
							String class_name = dataArray.getJSONObject(i).getString("class_name");
							if( isRequestClassList ) {
								profile.addClass(class_srl, new OrgClass(class_srl, class_org_srl, class_name));
								this.request_Class_getClassStudent(class_org_srl, class_srl);
								this.request_Class_getClassTeacher(class_org_srl, class_srl);
								this.request_Organization_getOrganization(class_org_srl);
							}
							else {
								for(int j = 0; j < orgList_pool.size(); j++) {
									if( orgList_pool.get(j).org_srl.equals(class_org_srl) ) {
										orgList_pool.get(j).addClass(new RegisterClassItem(class_srl, class_name));
										break;
									}
								}
								request_Class_getClassStudent(class_org_srl, class_srl);
							}
						}
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
							if( orgList_pool.get(j).org_srl.equals(member_org_srl) ) {
								for(int k = 0; k < orgList_pool.get(j).classList.size(); k++) {
									if( orgList_pool.get(j).classList.get(k).class_srl.equals(student_class_srl)) {
										orgList_pool.get(j).classList.get(k).addChild(new RegisterChildItem(member_srl, student_srl, member_name, member_org_srl, 
												student_class_srl, student_parent_srl, student_teacher_srl, student_shuttle_srl, student_birthday));
										break;
									}
								}
								break;
							}
						}
					
					}
					if( isRequestClassList ) {
						if( class_srl.isEmpty() == false ) {
							profile.classes.get(class_srl).setNumOfStudent(dataArray.length());
							profile.classes.get(class_srl).setNumOfStudentHavingParent(numOfStudentHavingParent);
						}
						teacherGetClassStudentCounter++;
						if( (profile.classList.size() == 0 || (profile.classList.size() == manager_addclass_org_counter && profile.classList.size() == manager_addclass_teacher_counter && profile.classList.size() == teacherGetClassStudentCounter) ) &&
							isAddClassCompleted == false ) {
							profile.addClass("ADD_CLASS", new OrgClass("", "", "새 학급 등록하기"));
							goMainActivity();
							isAddClassCompleted = true;
						}
					}
				}
				else if(uri.equals("Organization/getOrgTeachers")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String member_srl = dataArray.getJSONObject(i).getString("member_srl");
						String member_name = dataArray.getJSONObject(i).getString("member_name");
						String member_type = dataArray.getJSONObject(i).getString("member_type");
						String member_org_srl = dataArray.getJSONObject(i).getString("member_org_srl");
						String member_point = dataArray.getJSONObject(i).getString("member_point");
						String member_email = dataArray.getJSONObject(i).getString("member_email");
						//String member_picture = dataArray.getJSONObject(i).getString("member_picture");
						JSONObject teacherObj = dataArray.getJSONObject(i).getJSONObject("teacher");
						String teacher_srl = teacherObj.getString("teacher_srl");
						String teacher_member_srl = teacherObj.getString("teacher_member_srl");
						String teacher_class_srl = teacherObj.getString("teacher_class_srl");
						String teacher_shuttle_srl = teacherObj.getString("teacher_shuttle_srl");
					}
				}
				else if(uri.equals("Member/getParentStudents")) {
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
						profile.addChildren(member_srl, new Children(student_srl, member_srl, member_name, member_picture, profile.member_name,
								member_org_srl, student_class_srl, student_parent_srl, student_teacher_srl, 
								student_shuttle_srl, student_birthday, student_parent_key));
						this.request_Organization_getOrganization(member_org_srl);
						this.request_Class_getClass(member_org_srl, student_class_srl);
						this.request_Class_getClassTeacher(member_org_srl, student_class_srl);
					}
					if( dataArray.length() == 0 ) {
						profile.addChildren("ADD_CHILD", new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));
						tempGoMainActivity();
					}
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
					String org_status = jsonObj.getString("org_status");
					if( isRequestClassList ) {
						profile.org_name = org_name;
						profile.org_manager_member_srl = org_manager_member_srl;
						manager_addclass_org_counter++;
						if( (profile.classList.size() == 0 || (profile.classList.size() == manager_addclass_org_counter && profile.classList.size() == manager_addclass_teacher_counter && profile.classList.size() == teacherGetClassStudentCounter) ) &&
							isAddClassCompleted == false ) {
							profile.addClass("ADD_CLASS", new OrgClass("", "", "새 학급 등록하기"));
							goMainActivity();
							isAddClassCompleted = true;
						}
					}
					else {
						profile.org_name = org_name;
						profile.org_manager_member_srl = org_manager_member_srl;
						profile.childrenList.get(parent_addchild_org_counter++).setOrganizationName(org_name);
						if( profile.childrenList.size() == parent_addchild_org_counter &&
							profile.childrenList.size() == parent_addchild_class_counter &&
							profile.childrenList.size() == parent_addchild_teacher_counter && 
							isAddChildCompleted == false ) {
							profile.addChildren("ADD_CHILD", new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));
							goMainActivity();
							isAddChildCompleted = true;
						}
					}
				}
				else if(uri.equals("Class/getClass")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String class_srl = jsonObj.getString("class_srl");
					String class_org_srl = jsonObj.getString("class_org_srl");
					String class_name = jsonObj.getString("class_name");
					profile.childrenList.get(parent_addchild_class_counter++).setClassName(class_name);
					if( profile.childrenList.size() == parent_addchild_org_counter &&
						profile.childrenList.size() == parent_addchild_class_counter &&
						profile.childrenList.size() == parent_addchild_teacher_counter && 
						isAddChildCompleted == false ) {
						profile.addChildren("ADD_CHILD", new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));
						goMainActivity();
						isAddChildCompleted = true;
					}
				}
				else if(uri.equals("Class/getClassTeacher")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					String class_srl = "";
					for(int i = 0; i < dataArray.length(); i++) {
						String member_srl = dataArray.getJSONObject(i).getString("member_srl");
						String member_name = dataArray.getJSONObject(i).getString("member_name");
						String member_type = dataArray.getJSONObject(i).getString("member_type");
						String member_org_srl = dataArray.getJSONObject(i).getString("member_org_srl");
						String member_point = dataArray.getJSONObject(i).getString("member_point");
						String member_email = dataArray.getJSONObject(i).getString("member_email");
						String member_picture = dataArray.getJSONObject(i).getString("member_picture");
						JSONObject teacherObj = dataArray.getJSONObject(i).getJSONObject("teacher");
						String teacher_srl = teacherObj.getString("teacher_srl");
						String teacher_member_srl = teacherObj.getString("teacher_member_srl");
						String teacher_class_srl = teacherObj.getString("teacher_class_srl");
						String teacher_shuttle_srl = teacherObj.getString("teacher_shuttle_srl");
						class_srl = teacher_class_srl;
						if( isRequestClassList ) {
							if( class_srl.isEmpty() == false ) {
								if(profile.member_type.charAt(0) == 'T' && profile.member_srl.equals(teacher_member_srl)) {
									for(int j = 0; j < profile.classList.size(); j++) {
										if(profile.classList.get(j).getClass_srl().equals(teacher_class_srl)) {
											profile.selected_index = j;
											break;
										}
									}
								}
								profile.classes.get(class_srl).addTeacher(new OrgClassTeacher(teacher_srl, member_name, teacher_member_srl, teacher_class_srl, teacher_shuttle_srl));
								
							}
						}
						else {
							profile.childrenList.get(parent_addchild_teacher_counter++).addTeacher(new OrgClassTeacher(teacher_srl, member_name, teacher_member_srl, teacher_class_srl, teacher_shuttle_srl));
							if( profile.childrenList.size() == parent_addchild_org_counter &&
								profile.childrenList.size() == parent_addchild_class_counter &&
								profile.childrenList.size() == parent_addchild_teacher_counter && 
								isAddChildCompleted == false ) {
								profile.addChildren("ADD_CHILD", new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));
								goMainActivity();
								isAddChildCompleted = true;
							}
						}
					}
					if( isRequestClassList ) {
						manager_addclass_teacher_counter++;
						if( (profile.classList.size() == 0 || (profile.classList.size() == manager_addclass_org_counter && profile.classList.size() == manager_addclass_teacher_counter && profile.classList.size() == teacherGetClassStudentCounter) ) &&
							isAddClassCompleted == false ) {
							profile.addClass("ADD_CLASS", new OrgClass("", "", "새 학급 등록하기"));
							goMainActivity();
							isAddClassCompleted = true;
						}
					}
				}
				else if(uri.equals("Member/addMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_srl = jsonObj.getString("member_srl");
					String member_name = jsonObj.getString("member_name");
					String member_type = jsonObj.getString("member_type");
					String member_org_srl = jsonObj.getString("member_org_srl");
					String member_point = jsonObj.getString("member_point");
					String member_email = jsonObj.getString("member_email");
					String member_picture = jsonObj.getString("member_picture");
					String member_device_type = "";//jsonObj.getString("member_device_type");
					String member_device_uuid = "";//jsonObj.getString("member_device_uuid");
					switch(member_type.charAt(0)) {
					case 'S':
						//this.request_Member_setStudent(member_srl, "1", register_parent_member_srl, "1", "1", "0");
						setStudentInfoCounter++;
						if(setStudentInfoCounter == registerChildList.size()) {
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
											showDialogMessage("회원가입을 축하합니다!");
											goNextPage();
							            }
							        });
							    }
							}).start();
						}
						break;
					case 'P':
						register_parent_member_srl = member_srl;
						this.request_Member_setParent(member_srl, member_org_srl, "WHAT");
						break;
					case 'T':
						String classname = ((EditText)findViewById(R.id.register_teacher_newclass)).getText().toString();
						String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
						for(int i = 0; i < orgList_pool.size(); i++) {
							if(teacher_orgcode.equals(orgList_pool.get(i).org_teacher_key)) {
								//this.request_Member_setTeacher(member_srl, member_org_srl, teacher_orgcode, class_srl, shuttle_srl)
								if(classname.isEmpty()) {
									Spinner spinner = (Spinner)findViewById(R.id.register_teacher_classlist);
									int j = spinner.getSelectedItemPosition();
									this.request_Member_setTeacher(member_srl, member_org_srl, teacher_orgcode, orgList_pool.get(i).classList.get(j).class_srl, "0");
								}
								else {
									registerNewClassTeacher_member_srl = member_srl;
									Spinner spinner = (Spinner)findViewById(R.id.register_teacher_classlevel);
									switch(spinner.getSelectedItemPosition()) {
									case 0:
										this.request_Class_setClass(""+orgList_pool.get(i).org_srl, classname, "7");
										break;
									case 1:
										this.request_Class_setClass(""+orgList_pool.get(i).org_srl, classname, "6");
										break;
									case 2:
										this.request_Class_setClass(""+orgList_pool.get(i).org_srl, classname, "5");
										break;
									case 3:
										this.request_Class_setClass(""+orgList_pool.get(i).org_srl, classname, "4");
										break;
									}
								}
								break;
							}
						}
						break;
					case 'M':
						String orgmaster_name = ((EditText)findViewById(R.id.register_orgmaster_name)).getText().toString();
						String orgmaster_phone = ((EditText)findViewById(R.id.register_orgmaster_phone)).getText().toString();
						String orgmaster_org = ((EditText)findViewById(R.id.register_orgmaster_org)).getText().toString();
						String orgmaster_orglocation = ((EditText)findViewById(R.id.register_orgmaster_orglocation)).getText().toString();
						this.request_Organization_setOrganization(member_srl, orgmaster_org, orgmaster_phone, orgmaster_orglocation);
						break;
					}
				}
				else if(uri.equals("Member/modMember")) {
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
				else if(uri.equals("Class/setClass")) {
					String teacher_orgcode = ((EditText)findViewById(R.id.register_teacher_orgcode)).getText().toString();
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String class_srl = jsonObj.getString("class_srl");
					String class_org_srl = jsonObj.getString("class_org_srl");
					String class_name = jsonObj.getString("class_name");
					String class_status = jsonObj.getString("class_status");
					this.request_Member_setTeacher(registerNewClassTeacher_member_srl, class_org_srl, teacher_orgcode, class_srl, "0");
				}
				else if(uri.equals("Member/setStudent")) {
					setStudentInfoCounter++;
					if(setStudentInfoCounter == registerChildList.size()) {
						new Thread(new Runnable() {
						    @Override
						    public void run() {    
						        runOnUiThread(new Runnable(){
						            @Override
						             public void run() {
										showDialogMessage("회원가입을 축하합니다!");
										goNextPage();
						            }
						        });
						    }
						}).start();
					}
				}
				else if(uri.equals("Member/setTeacher")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
//					            	Intent intent = new Intent(LoginActivity.this, RegisterInfoTakerActivity.class);
//					        		intent.putExtra("type", 3);
//					        		startActivityForResult(intent, 3);
									showDialogMessage("회원가입을 축하합니다!");
									goNextPage();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Member/setParent")) {
					String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
					String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
					
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String parent_srl = jsonObj.getString("parent_srl");
					
					LinearLayout layout = (LinearLayout)findViewById(R.id.register_parent_form);
					for(int i = 0; i < currentChildFormCount+1; i++) {
						String parent_org = ((EditText)layout.findViewWithTag("register_parent_org_"+i)).getText().toString();
						String parent_class = ((EditText)layout.findViewWithTag("register_parent_class_"+i)).getText().toString();
						String parent_childname = ((EditText)layout.findViewWithTag("register_parent_childname_"+i)).getText().toString();
						String birthday = ((EditText)layout.findViewWithTag("register_parent_birthday_"+i)).getText().toString();
						if(parent_org.isEmpty()) {
							this.request_Member_addMember(parent_childname, parent_childname, "S", "1", parent_childname, parent_childname, "N", "");
						}
						else {
							this.request_Member_modStudent(registerChildList.get(i).student_srl, registerChildList.get(i).member_srl, registerChildList.get(i).class_srl, parent_srl, 
								registerChildList.get(i).teacher_srl, registerChildList.get(i).shuttle_srl, "20"+birthday);
						}
					}
				}
				else if(uri.equals("Member/modStudent")) {
					setStudentInfoCounter++;
					if(setStudentInfoCounter == registerChildList.size()) {
						new Thread(new Runnable() {
						    @Override
						    public void run() {    
						        runOnUiThread(new Runnable(){
						            @Override
						             public void run() {
										showDialogMessage("회원가입을 축하합니다!");
										goNextPage();
						            }
						        });
						    }
						}).start();
					}
				}
				else if(uri.equals("Organization/setOrganization")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String org_srl = jsonObj.getString("org_srl");
					final String org_name = jsonObj.getString("org_name");
					String org_manager_member_srl = jsonObj.getString("org_manager_member_srl");
					String org_phone = jsonObj.getString("org_phone");
					final String org_address = jsonObj.getString("org_address");
					final String org_teacher_key = jsonObj.getString("org_teacher_key");
					String org_created = jsonObj.getString("org_created");
					String org_updated = jsonObj.getString("org_updated");
					String org_status = jsonObj.getString("org_status");
					String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
					String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
					String orgmaster_name = ((EditText)findViewById(R.id.register_orgmaster_name)).getText().toString();
					this.request_Member_modMember(org_manager_member_srl, orgmaster_name, orgmaster_name, org_srl, email, password, "A", GCMRegistrar.getRegistrationId(this), "N");
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	TextView txt = (TextView)findViewById(R.id.register_finish_orgname);
					            	txt.setText(org_name+"( "+org_address+")");
					            	txt = (TextView)findViewById(R.id.register_finish_orgcode);
					            	txt.setText(org_teacher_key);
					            	showDialogMessage("회원가입 및 기관등록이 완료됐습니다!");
									goNextPage();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Member/getMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String member_name = jsonObj.getString("member_name");
					profile.org_manager_name = member_name;
					this.tempGoMainActivity();
				}
			}
			else {
				if(uri.equals("Member/login")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									showDialogMessage("아이디 또는 비밀번호가 틀렸습니다.");
									isLoginRequested = false;
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Member/checkEmail")) {
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
									showDialogMessage("현재 가입된 이메일입니다. 다른 이메일을 사용해주세요.");
					            }
					        });
					    }
					}).start();
				}
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
