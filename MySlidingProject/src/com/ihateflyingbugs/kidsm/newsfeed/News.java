package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class News extends BaseItem {
	public enum NEWSTYPE {
		SCHEDULE,
		PHOTO,
		BUSINFO,
		MENTORY
	}
	
	NEWSTYPE type;
	String identifier;
	String timeline_srl;
	String timeline_member_srl;
	int timeline_created;
	ArrayList<String> likeMemberList;
	ArrayList<Reply> commentList;
	String member_scrap_srl;
	int scrapCount;
	View layout;
	
	News(String identifier, String timeline_srl, String timeline_member_srl, String timeline_like, String timeline_created) {
		this.identifier = identifier;
		this.timeline_srl = timeline_srl;
		this.timeline_member_srl = timeline_member_srl;
		this.timeline_created = Integer.parseInt(timeline_created);
		
		likeMemberList = new ArrayList<String>();
		if( timeline_like.isEmpty() == false ) {
			String[] likeMemberData = timeline_like.split(",");
			for(int i = 0; i < likeMemberData.length; i++) {
				if(likeMemberData[i] != null && likeMemberData[i].isEmpty() == false)
					likeMemberList.add(likeMemberData[i]);
			}
		}
		commentList = new ArrayList<Reply>();
		member_scrap_srl = "";
		scrapCount = 0;
	}
}
