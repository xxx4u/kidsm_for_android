package com.ihateflyingbugs.kidsm.uploadphoto;

import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class InputTag extends BaseItem{
	boolean isTagged;
	View convertView;
	
	String member_srl;
	String member_name;
	InputTag(String member_srl, String member_name) {
		this.member_srl = member_srl;
		this.member_name = member_name;
		isTagged = false;
	}
	InputTag(InputTag tag) {
		this.member_srl = tag.member_srl;
		this.member_name = tag.member_name;
		isTagged = false;
	}
}
