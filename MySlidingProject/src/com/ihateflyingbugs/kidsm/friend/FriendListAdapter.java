package com.ihateflyingbugs.kidsm.friend;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.friend.FriendListItem.FriendListItemType;

public class FriendListAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<FriendListItem> arSrc;

	public FriendListAdapter(Context context, ArrayList<FriendListItem> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
	}

	public int getCount() {
		return arSrc.size();
	}

	public FriendListItem getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
//	public int getItemViewType(int position) {
//		return arSrc.get(position).type;
//	}
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 10;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (convertView == null) {
			int res = 0;
			switch (arSrc.get(position).type) {
			case NAMETAG:
				res = R.layout.friend_namecab;
				break;
			case RECOMMENDED_FRIEND:
			case CURRENT_FRIEND:
			case WAITING_FRIEND:
			case CURRENT_STUDENT:
				res = R.layout.friend_onebutton;
				break;
			case REQUESTED_FRIEND:
			case GRANTED_TEACHER:
			case UNGRANTED_TEACHER:
			case REQUESTED_MESSAGE:
				res = R.layout.friend_twobutton;
				break;
			case CURRENT_STUDENT_FOR_MANAGER:
				res = R.layout.friend_nobutton;
				break;
			}
			convertView = mInflater.inflate(res, parent, false);
		}
		
		int res = convertView.getId();
		switch (arSrc.get(position).type) {
		case NAMETAG:
			if(res != R.layout.friend_namecab) {
				res = R.layout.friend_namecab;
				convertView = mInflater.inflate(res, parent, false);
			}
			break;
		case RECOMMENDED_FRIEND:
		case CURRENT_FRIEND:
		case WAITING_FRIEND:
		case CURRENT_STUDENT:
			if(res != R.layout.friend_onebutton) {
				res = R.layout.friend_onebutton;
				convertView = mInflater.inflate(res, parent, false);
			}
			break;
		case REQUESTED_FRIEND:
		case GRANTED_TEACHER:
		case UNGRANTED_TEACHER:
		case REQUESTED_MESSAGE:
			if(res != R.layout.friend_twobutton) {
				res = R.layout.friend_twobutton;
				convertView = mInflater.inflate(res, parent, false);
			}
			break;
		case CURRENT_STUDENT_FOR_MANAGER:
			if(res != R.layout.friend_nobutton) {
				res = R.layout.friend_nobutton;
				convertView = mInflater.inflate(res, parent, false);
			}
			break;
		}
		// �׸� �並 �ʱ�ȭ�Ѵ�.
		TextView txt;
		Button btn1, btn2;
		switch (arSrc.get(position).type) {
		case NAMETAG:
			txt = (TextView)convertView.findViewById(R.id.friend_namecab);
			txt.setText(arSrc.get(position).getName());
			break;
		case RECOMMENDED_FRIEND:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			btn1 = (Button)convertView.findViewById(R.id.friend_button);
			btn1.setText(R.string.friend_friendcall);
			btn1.setTag(position);
			break;
		case CURRENT_FRIEND:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			btn1 = (Button)convertView.findViewById(R.id.friend_button);
			btn1.setText(R.string.friend_alreadyfriend);
			btn1.setTag(position);
			break;
		case WAITING_FRIEND:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			btn1 = (Button)convertView.findViewById(R.id.friend_button);
			btn1.setText(R.string.friend_requested);
			btn1.setTag(position);
			break;
		case REQUESTED_FRIEND:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			btn1 = (Button)convertView.findViewById(R.id.friend_button1);
			btn1.setTag(position);
			btn2 = (Button)convertView.findViewById(R.id.friend_button2);
			btn2.setTag(position);
			break;
		case CURRENT_STUDENT:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			btn1 = (Button)convertView.findViewById(R.id.friend_button);
			btn1.setText(R.string.friend_delete_student);
			btn1.setTag(position);
			break;
		case GRANTED_TEACHER:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" ������");
			btn1 = (Button)convertView.findViewById(R.id.friend_button1);
			btn1.setText(R.string.friend_delete_teacher);
			btn1.setTag(position);
			btn2 = (Button)convertView.findViewById(R.id.friend_button2);
			btn2.setText(R.string.friend_verified_teacher);
			btn2.setTag(position);
			break;
		case UNGRANTED_TEACHER:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" ������");
			btn1 = (Button)convertView.findViewById(R.id.friend_button1);
			btn1.setText(R.string.friend_deny);
			btn1.setTag(position);
			btn2 = (Button)convertView.findViewById(R.id.friend_button2);
			btn2.setText(R.string.friend_approve);
			btn2.setTag(position);
			break;
		case REQUESTED_MESSAGE:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName());
			btn1 = (Button)convertView.findViewById(R.id.friend_button1);
			btn1.setText(R.string.friend_deny);
			btn1.setTag(position);
			btn2 = (Button)convertView.findViewById(R.id.friend_button2);
			btn2.setText(R.string.friend_approve);
			btn2.setTag(position);
			break;
		case CURRENT_STUDENT_FOR_MANAGER:
			txt = (TextView)convertView.findViewById(R.id.friend_friendname);
			txt.setText(arSrc.get(position).getName()+" / �ڳ� : "+arSrc.get(position).getChildname());
			break;
		}

		return convertView;
	}
}
