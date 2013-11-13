package com.ihateflyingbugs.kidsm.newsfeed;

import android.content.Context;

import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;

public class BusinfoNews extends News {
	String shuttle_srl;
	String shuttle_org_srl;
	String shuttle_name;
	String shuttle_route;
	String shuttle_location;
	
	public BusinfoNews(String identifier, String timeline_srl, String timeline_member_srl, String timeline_like, String timeline_created) {
		super(identifier, timeline_srl, timeline_member_srl, timeline_like, timeline_created);
		type = NEWSTYPE.BUSINFO;
	}

	public void setBusinfoNews(String shuttle_srl, String shuttle_org_srl,
			String shuttle_name, String shuttle_route, String shuttle_location) {
		this.shuttle_srl = shuttle_srl;
		this.shuttle_org_srl = shuttle_org_srl;
		this.shuttle_name = shuttle_name;
		this.shuttle_route = shuttle_route;
		this.shuttle_location = shuttle_location;
	}
}
