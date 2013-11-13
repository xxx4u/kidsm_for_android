package com.ihateflyingbugs.kidsm.newsfeed;

import com.ihateflyingbugs.kidsm.BaseItem;

public class Reply extends BaseItem {
	String tcomment_srl;
	String tcomment_member_srl;
	String tcomment_timeline_srl;
	String tcomment_message;
	String tcomment_created;
	String tcomment_name;
	private String tcomment_member_picture_uri;
	
	public Reply(String tcomment_srl, String tcomment_member_srl, String tcomment_timeline_srl, String tcomment_message, String tcomment_created) {
		this.tcomment_srl = tcomment_srl;
		this.tcomment_member_srl = tcomment_member_srl;
		this.tcomment_timeline_srl = tcomment_timeline_srl;
		this.tcomment_message = tcomment_message;
		this.tcomment_created = tcomment_created;
	}
	
	public void setCommenterName(String tcomment_name) {
		this.tcomment_name = tcomment_name;
	}
	
	public String getCommenterName() {
		return tcomment_name;
	}

	public String getTcomment_member_picture_uri() {
		return tcomment_member_picture_uri;
	}

	public void setTcomment_member_picture_uri(
			String tcomment_member_picture_uri) {
		this.tcomment_member_picture_uri = tcomment_member_picture_uri;
	}
}
