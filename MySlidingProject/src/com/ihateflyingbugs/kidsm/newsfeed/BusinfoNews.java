package com.ihateflyingbugs.kidsm.newsfeed;

import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;

public class BusinfoNews extends News {
	public BusinfoNews(String timeline_srl, String timeline_like) {
		super(timeline_srl, timeline_like);
		type = NEWSTYPE.BUSINFO;
	}
}
