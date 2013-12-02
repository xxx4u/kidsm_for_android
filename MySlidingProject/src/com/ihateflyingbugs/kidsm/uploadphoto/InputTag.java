package com.ihateflyingbugs.kidsm.uploadphoto;

import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class InputTag extends BaseItem{
	public boolean isTagged;
	public View convertView;
	public String member_srl;
	public String member_name;
	
	public InputTag(String member_srl, String member_name) {
		this.member_srl = member_srl;
		this.member_name = member_name;
		isTagged = false;
	}
	public InputTag(InputTag tag) {
		this.member_srl = tag.member_srl;
		this.member_name = tag.member_name;
		isTagged = false;
	}
}
