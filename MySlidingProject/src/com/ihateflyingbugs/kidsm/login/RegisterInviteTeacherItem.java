package com.ihateflyingbugs.kidsm.login;

import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class RegisterInviteTeacherItem extends BaseItem {
	int tag;
	int type;
	boolean isChecked;
	boolean isVisible;
	View layout;
	String number;
	
	public RegisterInviteTeacherItem(int tag, int type, String name, boolean isChecked, String number) {
		this.tag = tag;
		this.type = type;
		this.name = name;
		this.isChecked = isChecked;
		this.number = number;
		isVisible = true;
	}
}
