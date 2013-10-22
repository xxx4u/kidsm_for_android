package com.ihateflyingbugs.kidsm.newsfeed;

import com.ihateflyingbugs.kidsm.BaseItem;

public class Reply extends BaseItem {
	String tcomment_srl;
	String tcomment_member_srl;
	String tcomment_timeline_srl;
	String tcomment_message;
	String tcomment_created;
	
	Reply(String tcomment_srl, String tcomment_member_srl, String tcomment_timeline_srl, String tcomment_message, String tcomment_created) {
		this.tcomment_srl = tcomment_srl;
		this.tcomment_member_srl = tcomment_member_srl;
		this.tcomment_timeline_srl = tcomment_timeline_srl;
		this.tcomment_message = tcomment_message;
		this.tcomment_created = tcomment_created;
	}
}
