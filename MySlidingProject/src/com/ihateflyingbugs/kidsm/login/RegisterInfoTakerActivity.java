package com.ihateflyingbugs.kidsm.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity;

public class RegisterInfoTakerActivity extends NetworkActivity {
	
	ViewFlipper flipper;
	Menu menu;
	int type;
	int currentPageNumber;
	ArrayList<RegisterInviteTeacherItem> inviteTeacherList;
	ArrayList<RegisterInviteTeacherItem> inviteTeacherList_pool;
	RegisterInviteTeacherAdapter inviteTeacherAdapter;
	ArrayList<RegisterAddressItem> addressList;
	RegisterAddressAdapter addressAdapter;
	ArrayList<RegisterOrgItem> orgList;
	RegisterOrgAdapter orgAdapter;
	int indexOfOrg, indexOfClass, indexOfChild;
	int classDataLoadRequestCounter, classDataLoadResponseCounter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_infotaker);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		flipper = (ViewFlipper)findViewById(R.id.register_infotaker_flipper);
		
		type = getIntent().getIntExtra("type", -1);
		switch(type) {
		case 0:
			setTitle(getString(R.string.register_find_org));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_org, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_class, null));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_child, null));
			break;
		case 1:
			setTitle(getString(R.string.register_find_orglocation));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_orglocation, null));
			break;
		case 2:
			setTitle(getString(R.string.register_invite_teacher));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_invite_teacher, null));
			break;
		case 3:
			setTitle(getString(R.string.register_invite_parent));
			flipper.addView(LayoutInflater.from(this).inflate(R.layout.register_infotaker_invite_teacher, null));
			break;
		}
		setListResources();
		currentPageNumber = 0;
	}
	
	private void setListResources() {
		ListView listView;
		EditText editText;
		switch(type) {
		case 0:
			editText = (EditText)flipper.findViewById(R.id.register_infotaker_org_search);
			editText.addTextChangedListener(textWatcher);
			listView = (ListView) flipper.findViewById(R.id.register_infotaker_org_list);
//			ArrayList<RegisterChildItem> childList = new ArrayList<RegisterChildItem>();
//			childList.add(new RegisterChildItem("강민경", "901226"));
//			childList.add(new RegisterChildItem("강민지", "941226"));
//			childList.add(new RegisterChildItem("강민혀", "931026"));
//			ArrayList<RegisterClassItem> classList = new ArrayList<RegisterClassItem>();
//			RegisterClassItem tempClass = new RegisterClassItem("햇님반");
//			tempClass.setChildList(childList);
//			classList.add(tempClass);
//			childList = new ArrayList<RegisterChildItem>();
//			childList.add(new RegisterChildItem("강민경", "901226"));
//			childList.add(new RegisterChildItem("강민지", "941226"));
//			childList.add(new RegisterChildItem("강민혀", "931026"));
//			childList.add(new RegisterChildItem("강민경", "901226"));
//			childList.add(new RegisterChildItem("강민지", "941226"));
//			childList.add(new RegisterChildItem("강민혀", "931026"));
//			tempClass = new RegisterClassItem("별님반");
//			tempClass.setChildList(childList);
//			classList.add(tempClass);
			orgList = new ArrayList<RegisterOrgItem>();
//			RegisterOrgItem tempOrg = new RegisterOrgItem("버그스1 유치원");
//			tempOrg.setClassList(classList);
//			orgList_pool.add(tempOrg);
//			tempOrg = new RegisterOrgItem("버그스3 유치원");
//			tempOrg.setClassList(classList);
//			orgList_pool.add(tempOrg);
//			tempOrg = new RegisterOrgItem("버그스3 유치원");
//			tempOrg.setClassList(classList);
//			orgList_pool.add(tempOrg);
			refreshListData();
			orgAdapter = new RegisterOrgAdapter(this, orgList);
			listView.setAdapter(orgAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					indexOfOrg = position;
					ListView classListView = (ListView) flipper.findViewById(R.id.register_infotaker_class_list);
					final ArrayList<RegisterClassItem> selectedClassList = orgList.get(position).classList;
					RegisterClassAdapter classAdapter = new RegisterClassAdapter(RegisterInfoTakerActivity.this, selectedClassList);
					classListView.setAdapter(classAdapter);
					classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							indexOfClass = position;
							ListView childListView = (ListView) flipper.findViewById(R.id.register_infotaker_child_list);
							final ArrayList<RegisterChildItem> selectedChildList = selectedClassList.get(position).childList;
							RegisterChildAdapter childAdapter = new RegisterChildAdapter(RegisterInfoTakerActivity.this, selectedChildList);
							childListView.setAdapter(childAdapter);
							childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									indexOfChild = position;
									MenuItem item = menu.findItem(R.id.register_onebutton);
									CheckBox cb = (CheckBox)view.findViewById(R.id.register_info_namechecker_checker);
									int index = Integer.parseInt(view.getTag().toString());
									if( selectedChildList.get(index).isChecked ) {
										selectedChildList.get(index).isChecked = false;
									}
									else {
										for(int i = 0; i < selectedChildList.size(); i++) {
											if( selectedChildList.get(i).layout != null ) {
												selectedChildList.get(i).isChecked = false;
											}
										}
										selectedChildList.get(index).isChecked = true;
										item.setVisible(true);
									}
								}
							});
							goNextPage();
						}
					});
					goNextPage();
					
				}
			});
			break;
		case 1:
			editText = (EditText)flipper.findViewById(R.id.register_infotaker_orglocation_search);
			editText.setOnKeyListener(new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) {
						flipper.findViewById(R.id.register_infotaker_orglocation_loading).setVisibility(View.VISIBLE);
	            		flipper.findViewById(R.id.register_infotaker_orglocation_not_found).setVisibility(View.INVISIBLE);
						GET_POSTAL_CODE(((EditText)v).getText().toString());
					}
					return false;
				}
			});
			listView = (ListView) flipper.findViewById(R.id.register_infotaker_orglocation_list);
			addressList = new ArrayList<RegisterAddressItem>();
			addressAdapter = new RegisterAddressAdapter(this, addressList);
			listView.setAdapter(addressAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent result = new Intent();
					result.putExtra("address", ((TextView)view.findViewById(R.id.register_info_text)).getText().toString());
					setResult(Activity.RESULT_OK, result);
					finish();
				}
			});
			break;
		case 2:
		case 3:
			editText = (EditText)flipper.findViewById(R.id.register_infotaker_invite_teacher_search);
			editText.addTextChangedListener(textWatcher);
			listView = (ListView) flipper.findViewById(R.id.register_infotaker_invite_teacher_list);
			inviteTeacherList = new ArrayList<RegisterInviteTeacherItem>();
			inviteTeacherList_pool = new ArrayList<RegisterInviteTeacherItem>();
			setInviteTeacherList();
			refreshListData();
			inviteTeacherAdapter = new RegisterInviteTeacherAdapter(this, inviteTeacherList);
			listView.setAdapter(inviteTeacherAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					CheckBox cb = (CheckBox)view.findViewById(R.id.register_info_namechecker_checker);
					cb.setChecked(!cb.isChecked());
					int tag = Integer.parseInt(cb.getTag().toString());
					inviteTeacherList_pool.get(tag).isChecked = cb.isChecked();
				}
			});
			break;
		}
	}
	
	private void refreshListData() {
		switch(type) {
		case 0:
			orgList.clear();
			for(RegisterOrgItem item : LoginActivity.orgList_pool) {
				if(item.isVisible) {
					orgList.add(new RegisterOrgItem(""+item.org_srl, item.getName(), item.org_address, item.org_teacher_key, item.classList));
				}
			}
			break;
		case 1:
			break;
		case 2:
		case 3:
			inviteTeacherList.clear();
			for(RegisterInviteTeacherItem item : inviteTeacherList_pool) {
				if(item.isVisible)
					inviteTeacherList.add(new RegisterInviteTeacherItem(item.tag, item.type, item.getName(), item.isChecked, item.number));
			}
			break;
		}
	}
	
	public void OnNameCheckerClick(View v) {
		CheckBox cb = (CheckBox)v;
		switch(type) {
		case 0:
			ArrayList<RegisterChildItem> childList = orgList.get(indexOfOrg).classList.get(indexOfClass).childList;
			MenuItem item = menu.findItem(R.id.register_onebutton);
			if( cb.isChecked() ) {
				for(int i = 0; i < childList.size(); i++) {
					cb = (CheckBox) childList.get(i).layout.findViewById(R.id.register_info_namechecker_checker);
					cb.setChecked(false);
				}
				cb= (CheckBox)v;
				cb.setChecked(true);
				item.setVisible(true);
				for(int i = 0; i < childList.size(); i++) {
					cb = (CheckBox) childList.get(i).layout.findViewById(R.id.register_info_namechecker_checker);
					if( cb.isChecked() ) {
						indexOfChild = i;
					}
				}
			}
			else {
				item.setVisible(false);
			}
			break;
		case 2:
		case 3:
			cb = (CheckBox)v;
			int tag = Integer.parseInt(cb.getTag().toString());
			inviteTeacherList_pool.get(tag).isChecked = cb.isChecked();
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
			switch(type) {
			case 0:
				edit = (EditText)flipper.findViewById(R.id.register_infotaker_org_search);
				if(edit.isFocused()) {
					for(int i = 0; i < LoginActivity.orgList_pool.size(); i++) {
						if(LoginActivity.orgList_pool.get(i).getName().contains(edit.getText()))
							LoginActivity.orgList_pool.get(i).isVisible = true;
						else
							LoginActivity.orgList_pool.get(i).isVisible = false;
					}
					refreshListData();
					orgAdapter.notifyDataSetChanged();
				}
				break;
			case 1:
				break;
			case 2:
			case 3:
				edit = (EditText)flipper.findViewById(R.id.register_infotaker_invite_teacher_search);
				if(edit.isFocused()) {
					for(int i = 0; i < inviteTeacherList_pool.size(); i++) {
						if(inviteTeacherList_pool.get(i).type == 0)
							continue;
						if(inviteTeacherList_pool.get(i).getName().contains(edit.getText()))
							inviteTeacherList_pool.get(i).isVisible = true;
						else
							inviteTeacherList_pool.get(i).isVisible = false;
					}
					
					int nametagIndex = 0;
					int numOfName = 0;
					for(int i = 0; i < inviteTeacherList_pool.size(); i++) {
						if(inviteTeacherList_pool.get(i).type == 0) {
							if(i != 0 && numOfName == 0)
								inviteTeacherList_pool.get(nametagIndex).isVisible = false;
							else
								inviteTeacherList_pool.get(nametagIndex).isVisible = true;
							nametagIndex = i;
							numOfName = 0;
						}
						else {
							if(inviteTeacherList_pool.get(i).isVisible)
								numOfName++;
						}
					}
					if(numOfName == 0)
						inviteTeacherList_pool.get(nametagIndex).isVisible = false;
					else
						inviteTeacherList_pool.get(nametagIndex).isVisible = true;
					refreshListData();
					inviteTeacherAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
		
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent result = new Intent();
		switch(item.getItemId()) {
		case android.R.id.home:
			if(currentPageNumber == 0)
				finish();
			else
				goPrevPage();
			return true;
		case R.id.register_onebutton:
			switch(type) {
			case 0:
				result.putExtra("formNumber", getIntent().getIntExtra("formNumber", -1));
				result.putExtra("org", orgList.get(indexOfOrg).getName());
				result.putExtra("class", orgList.get(indexOfOrg).classList.get(indexOfClass).getName());
				result.putExtra("childname", orgList.get(indexOfOrg).classList.get(indexOfClass).childList.get(indexOfChild).getName());
				result.putExtra("birthday", orgList.get(indexOfOrg).classList.get(indexOfClass).childList.get(indexOfChild).birthday);
				
				for(int i = 0; i < LoginActivity.orgList_pool.size(); i++) {
					if( orgList.get(indexOfOrg).org_srl.equals(LoginActivity.orgList_pool.get(i).org_srl)) {
						result.putExtra("index_org", i);
						break;
					}
				}
				result.putExtra("index_class", indexOfClass);
				result.putExtra("index_child", indexOfChild);
				break;
			case 2:
			case 3:
				EditText editText = (EditText)flipper.findViewById(R.id.register_infotaker_invite_teacher_search);
				if(editText.isFocused()) {
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				}
				ArrayList<String> phoneList = new ArrayList<String>();
				for(int i = 0; i < inviteTeacherList_pool.size(); i++) {
					if( inviteTeacherList_pool.get(i).isChecked ) 
						phoneList.add(inviteTeacherList_pool.get(i).number);
				}
				result.putExtra("phoneList", phoneList);
				break;
			}
			setResult(Activity.RESULT_OK, result);
			finish();
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
		switch(type) {
		case 0:
			item.setTitle(R.string.finish);
			item.setVisible(false);
			break;
		case 1:
			item.setVisible(false);
			break;
		case 2:
		case 3:
			item.setTitle(R.string.invite);
			break;
		}
		
		return true;
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
	
	public void response(String uri, String response) {
		// override it plz.
		if(uri.equals("POSTAL_CODE")) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(response)));
				doc.getDocumentElement().normalize();
				final NodeList nodeList = doc.getElementsByTagName("item");
				addressList.clear();
				for(int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element headLineElement = (Element)node;
						
						NodeList addressElement = headLineElement.getElementsByTagName("address");
                        Element subItem = (Element) addressElement.item(0);
                        NodeList subElement1 = subItem.getChildNodes();
                        
                        NodeList codeElement = headLineElement.getElementsByTagName("postcd");
                        subItem = (Element) codeElement.item(0);
                        NodeList subElement2 = subItem.getChildNodes();
                        
                        String address = subElement1.item(0).getNodeValue();
                        String postcd = subElement2.item(0).getNodeValue();
                        postcd = "["+postcd.substring(0, 3)+"-"+postcd.substring(3)+"]";
    					addressList.add(new RegisterAddressItem(address));
					}
				}
				new Thread(new Runnable() {
				    @Override
				    public void run() {    
				        runOnUiThread(new Runnable(){
				            @Override
				            public void run() {
				            	if(nodeList.getLength() == 0)
				            		flipper.findViewById(R.id.register_infotaker_orglocation_not_found).setVisibility(View.VISIBLE);
			            		flipper.findViewById(R.id.register_infotaker_orglocation_loading).setVisibility(View.INVISIBLE);
								addressAdapter.notifyDataSetChanged();
				            }
				        });
				    }
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
		
			try {
				if( response.isEmpty() )
					return;
				JSONObject jsonObj = new JSONObject(response);
				String result = jsonObj.getString("result");
				if( result.equals("OK") ) {
					
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
	
	private void setInviteTeacherList() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
				ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

		String[] selectionArgs = null;

		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		Cursor contactCursor = getContentResolver().query(uri, projection, null,
				selectionArgs, sortOrder);

		char currentNameTag = ' ';
		if (contactCursor.moveToFirst()) {
			do {
//				String phonenumber = contactCursor.getString(1).replaceAll("-",
//						"");
//				if (phonenumber.length() == 10) {
//					phonenumber = phonenumber.substring(0, 3) + "-"
//							+ phonenumber.substring(3, 6) + "-"
//							+ phonenumber.substring(6);
//				} else if (phonenumber.length() > 8) {
//					phonenumber = phonenumber.substring(0, 3) + "-"
//							+ phonenumber.substring(3, 7) + "-"
//							+ phonenumber.substring(7);
//				}

				// acontact = new Contact();
				//acontact.setPhotoid(contactCursor.getLong(0));
				//acontact.setPhonenum(phonenumber);
				//acontact.setName(contactCursor.getString(2));
				//inviteTeacherList_pool.add(new RegisterInviteTeacherItem(0, "ㄱ"));
				if( currentNameTag != checkNameTag(contactCursor.getString(2)) ) {
					currentNameTag = checkNameTag(contactCursor.getString(2));
					inviteTeacherList_pool.add(new RegisterInviteTeacherItem(inviteTeacherList_pool.size(), 0, ""+currentNameTag, false, contactCursor.getString(1)));
				}
				inviteTeacherList_pool.add(new RegisterInviteTeacherItem(inviteTeacherList_pool.size(), 1, contactCursor.getString(2), false, contactCursor.getString(1)));
			} while (contactCursor.moveToNext());
		}
	}
	
	private char checkNameTag(String name) {
		// typo스트링의 글자수 만큼 list에 담아둡니다.
		//for (int i = 0; i < name.length(); i++) {
//		String source = "" + name.charAt(0);
//		char result;
//		if( source.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*"))
		char source = name.charAt(0);
		char result = source;
		char comVal = (char) (source-0xAC00);
		if (comVal >= 0 && comVal <= 11172){
			char uniVal = (char)comVal;
			char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
			if(cho!=4519)
				result = cho;
		} 
		else {
			if((source >= 'a' && source <= 'z') || (source >= 'A' && source <= 'Z')) {
				result = source;
				if(source >= 'a' && source <= 'z')
					result += 'A'-'a';
			}
			else {
				String s = ""+source;
				if(Character.isDigit(source) || Character.isSpace(source) || !s.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*") )
					result = '#';
				else
					result = source;
			}
				
			
		}
		return result;
	}
	//}
}
