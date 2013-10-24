package com.ihateflyingbugs.kidsm.friend;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.friend.FriendListItem.FriendListItemType;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendFragment extends NetworkFragment{
	public FriendListAdapter friendListAdapter;
	public ArrayList<FriendListItem> arItem;
	LayoutInflater inflater;
	View layout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_friend, container, false);
	
		auth_key = MainActivity.auth_key;
		
//		arItem.add(new FriendListItem(0, "���� ģ�� ��û", ""));
//		arItem.add(new FriendListItem(3, "������", "������"));
//		arItem.add(new FriendListItem(3, "������", "����"));
//		arItem.add(new FriendListItem(0, "�޴Թ�", ""));
//		arItem.add(new FriendListItem(1, "���̿�", "������"));
//		arItem.add(new FriendListItem(2, "����", "����"));
//		arItem.add(new FriendListItem(1, "�����", "���ڱ�"));
//		arItem.add(new FriendListItem(1, "�뿵��", "����"));
//		arItem.add(new FriendListItem(2, "�ٶ���", "�����"));
//		arItem.add(new FriendListItem(1, "������", "���տ�"));
		
		friendListAdapter = new FriendListAdapter(getActivity(), arItem);
		ListView MyList;
		MyList=(ListView)layout.findViewById(R.id.friend);
		MyList.setAdapter(friendListAdapter);
		
		return layout;
	}
	
	public void setFriendList(ArrayList<FriendListItem> list) {
		arItem = new ArrayList<FriendListItem>();
		arItem.addAll(list);
		if( friendListAdapter != null ) {
			friendListAdapter.arSrc = arItem;
			friendListAdapter.notifyDataSetChanged();
		}
	}
	
	public void OnButtonClick(final View v) {
		final int index = (Integer) v.getTag();
		//final RelativeLayout linear = (RelativeLayout)View.inflate(getActivity(), R.layout.profile, null);
		final FriendListItem friend = arItem.get(index);
		switch(friend.type) {
		case RECOMMENDED_FRIEND:
			new AlertDialog.Builder(getActivity())
			.setMessage(friend.getName()+"�Բ� ģ���� ��û�Ͻðڽ��ϱ�?" )
			.setNegativeButton("��û", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					FriendFragment.this.request_Member_setFriend(friend.friendInfo.origin_srl, friend.friendInfo.target_srl);
					FriendFragment.this.request_Service_notify_sendNotify(friend.friendInfo.origin_srl, friend.friendInfo.target_srl, "ģ�� ��û", SlidingMenuMaker.getProfile().getCurrentChildren().student_name+"�� �кθ�("+SlidingMenuMaker.getProfile().member_name+")���� ȸ���԰� ģ���� �ǰ� �;� �մϴ�.", "N");
				}
			})
			.setPositiveButton("���", null)
			.show();
			break;
		case CURRENT_FRIEND:
			String name = ((Button)v).getText().toString();
			new AlertDialog.Builder(getActivity())
			.setMessage(friend.getName()+"�԰� ģ���� �����ðڽ��ϱ�?")
			.setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					FriendFragment.this.request_Member_modFriendStatus(friend.friendInfo.friend_srl, "R");
				}
			})
			.setPositiveButton("���", null)
			.show();
			break;
		case WAITING_FRIEND:
			new AlertDialog.Builder(getActivity())
			.setMessage(friend.getName()+"�Կ��� ���� ģ����û�� ����Ͻðڽ��ϱ�?")
			.setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					FriendFragment.this.request_Member_modFriendStatus(friend.friendInfo.friend_srl, "R");
				}
			})
			.setPositiveButton("���", null)
			.show();
			break;
		case REQUESTED_FRIEND:
			switch(v.getId()) {
			case R.id.friend_button1:
				FriendFragment.this.request_Member_modFriendStatus(friend.friendInfo.friend_srl, "R");
				break;
			case R.id.friend_button2:
				FriendFragment.this.request_Member_modFriendStatus(friend.friendInfo.friend_srl, "A");
				break;
			}
			break;
		case CURRENT_STUDENT:
			new AlertDialog.Builder(getActivity())
			.setMessage(friend.getName()+"���� �ݿ��� �����Ͻðڽ��ϱ�?")
			.setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "R");
					FriendFragment.this.request_Member_delStudent(friend.getStudentsParentInfo().student_srl, friend.getStudentsParentInfo().student_member_srl);
				}
			})
			.setPositiveButton("���", null)
			.show();
			break;
		case GRANTED_TEACHER:
			switch(v.getId()) {
			case R.id.friend_button1:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "R");
				break;
			case R.id.friend_button2:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "A");
				break;
			}
			break;
		case UNGRANTED_TEACHER:
			switch(v.getId()) {
			case R.id.friend_button1:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "R");
				break;
			case R.id.friend_button2:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "A");
				break;
			}
			break;
		case REQUESTED_MESSAGE:
			switch(v.getId()) {
			case R.id.friend_button1:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "R");
				break;
			case R.id.friend_button2:
				//FriendFragment.this.request_Member_modFriendStaus(friend.friendInfo.friend_srl, "A");
				break;
			}
			break;
		}
	}
	
	public void OnNameClick(View v) {
		final RelativeLayout linear = (RelativeLayout)View.inflate(getActivity(), R.layout.profile, null);
		String name = (String) ((Button)v).getText();
		int idkey = 0;
		// �̸����θ� ���ؼ��� �ȵ�. �� �ڵ� ���� �� �� ���� �ʿ��� �κ�
		for(int i = 0; i < arItem.size(); i++) {
			if( arItem.get(i).getName().equals(name) )
				idkey = i;
		}
		TextView txt = (TextView) linear.findViewById(R.id.profile_childname);
		txt.setText(name);
		
		if( arItem.get(idkey).type == FriendListItemType.RECOMMENDED_FRIEND ) {
			new AlertDialog.Builder(getActivity())
			.setView(linear)
			.setNegativeButton("Ȯ��", null)
			.setPositiveButton("ģ�� �߰�", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new AlertDialog.Builder(getActivity())
					.setMessage("ģ���� ��û�Ͻðڽ��ϱ�?")
					.setNegativeButton("��û", null)
					.setPositiveButton("���", null)
					.show();
				}
			})
			.show();
		}
		else if( arItem.get(idkey).type == FriendListItemType.CURRENT_FRIEND ) {
			new AlertDialog.Builder(getActivity())
			.setView(linear)
			.setNegativeButton("Ȯ��", null)
			.setPositiveButton("ģ�� ���", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new AlertDialog.Builder(getActivity())
					.setMessage("ģ���� �����ðڽ��ϱ�?")
					.setNegativeButton("Ȯ��", null)
					.setPositiveButton("���", null)
					.show();
				}
			})
			.show();
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
				if(uri.equals("Member/setFriend")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					final String friend_srl = jsonObj.getString("friend_srl");
					String friend_origin_srl = jsonObj.getString("friend_origin_srl");
					String friend_target_srl = jsonObj.getString("friend_target_srl");
					final String friend_status = jsonObj.getString("friend_status");
					String friend_created = jsonObj.getString("friend_created");
					for(int i = 0; i < arItem.size(); i++) {
						if( arItem.get(i).friendInfo != null && 
							arItem.get(i).friendInfo.origin_srl.equals(friend_origin_srl) && 
							arItem.get(i).friendInfo.target_srl.equals(friend_target_srl)) {
							final int index = i;
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        FriendFragment.this.getActivity().runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
											arItem.get(index).friendInfo.friend_srl = friend_srl;
											arItem.get(index).friendInfo.status = friend_status.charAt(0);
											arItem.get(index).type = FriendListItemType.WAITING_FRIEND;
											friendListAdapter.notifyDataSetChanged();
							            }
							        });
							    }
							}).start();
						}
					}
				}
				else if(uri.equals("Member/modFriendStatus")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					final String friend_srl = jsonObj.getString("friend_srl");
					final String friend_origin_srl = jsonObj.getString("friend_origin_srl");
					final String friend_target_srl = jsonObj.getString("friend_target_srl");
					final String friend_status = jsonObj.getString("friend_status");
					for(int i = 0; i < arItem.size(); i++) {
						if( arItem.get(i).friendInfo != null && arItem.get(i).friendInfo.friend_srl.equals(friend_srl) ) {
							final int index = i;
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        FriendFragment.this.getActivity().runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
							            	if( friend_status.charAt(0) == 'R' ) {
												if(arItem.get(index).friendInfo.status == 'A') {
													arItem.get(index).friendInfo.status = friend_status.charAt(0);
													arItem.get(index).type = FriendListItemType.RECOMMENDED_FRIEND;
												}
												else if(arItem.get(index).friendInfo.status == 'S' && 
														arItem.get(index).friendInfo.origin_srl.equals(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl) == false) {
													arItem.remove(index);
												}
												else {
													arItem.get(index).friendInfo.status = friend_status.charAt(0);
													arItem.get(index).type = FriendListItemType.RECOMMENDED_FRIEND;
												}
											}
											else {
												arItem.get(index).friendInfo.status = friend_status.charAt(0);
												arItem.get(index).type = FriendListItemType.CURRENT_FRIEND;
											}
											friendListAdapter.notifyDataSetChanged();
							            }
							        });
							    }
							}).start();
						}
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
						JSONObject studentObj = jsonObj.getJSONObject("studnt");
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
					//FriendFragment.this.request_Member_delMember(member_srl, member_email, member_password)
				}
				else if(uri.equals("Member/delStudent")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String student_srl = jsonObj.getString("student_srl");
					String student_member_srl = jsonObj.getString("student_member_srl");
					String student_class_srl = jsonObj.getString("student_class_srl");
					String student_parent_srl = jsonObj.getString("student_parent_srl");
					String student_teacher_srl = jsonObj.getString("student_teacher_srl");
					String student_shuttle_srl = jsonObj.getString("student_shuttle_srl");
					String student_birthday = jsonObj.getString("student_birthday");
					String student_parent_key = jsonObj.getString("student_parent_key");
					//FriendFragment.this.request_Member_delMember(member_srl, member_email, member_password);
					for(int i = 0; i < arItem.size(); i++) {
						if( arItem.get(i).getStudentsParentInfo() != null && arItem.get(i).getStudentsParentInfo().student_member_srl.equals(student_member_srl) ) {
							final int index = i;
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        FriendFragment.this.getActivity().runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
							            	arItem.remove(index);
											friendListAdapter.notifyDataSetChanged();
							            }
							        });
							    }
							}).start();
						}
					}
				}
				else if(uri.equals("Member/delMember")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData); 
					String member_srl = jsonObj.getString("member_srl");
					for(int i = 0; i < arItem.size(); i++) {
						if( arItem.get(i).getStudentsParentInfo() != null && arItem.get(i).getStudentsParentInfo().student_member_srl.equals(member_srl) ) {
							final int index = i;
							new Thread(new Runnable() {
							    @Override
							    public void run() {    
							        FriendFragment.this.getActivity().runOnUiThread(new Runnable(){
							            @Override
							             public void run() {
							            	arItem.remove(index);
											friendListAdapter.notifyDataSetChanged();
							            }
							        });
							    }
							}).start();
						}
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
