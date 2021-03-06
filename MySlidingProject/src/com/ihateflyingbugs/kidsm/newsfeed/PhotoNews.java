package com.ihateflyingbugs.kidsm.newsfeed;

import android.content.Context;

public class PhotoNews extends News{
	String photo_srl;
	String photo_member_srl;
	String photo_album_srl;
	String photo_message;
	String photo_tag;
	String photo_path;
	String photo_thumbnail;
	String photo_like;
	String photo_private;
	String photo_created;
	String photo_updated;
	String photo_member_name;
	String photo_member_picture_srl;
	
	public PhotoNews(String identifier, String timeline_srl, String timeline_member_srl, String timeline_like, String timeline_created) {
		super(identifier, timeline_srl, timeline_member_srl, timeline_like, timeline_created);
	}
	
	public void setPhotoNews(String photo_srl, String photo_member_srl, String photo_album_srl, String photo_message, String photo_tag, String photo_path, String photo_thumbnail, String photo_like, String photo_private, String photo_created, String photo_updated) {
		type = NEWSTYPE.PHOTO;
		this.photo_srl = photo_srl;
		this.photo_member_srl = photo_member_srl;
		this.photo_album_srl = photo_album_srl;
		this.photo_message = photo_message;
		this.photo_tag = photo_tag;
		this.photo_path = photo_path;
		this.photo_thumbnail = photo_thumbnail;
		this.photo_like = photo_like;
		this.photo_private = photo_private;
		this.photo_created = photo_created;
		this.photo_updated = photo_updated;
		
		this.likeMemberList.clear();
		if( photo_like.isEmpty() == false ) {
			String[] likeMemberData = photo_like.split(",");
			for(int i = 0; i < likeMemberData.length; i++) {
				if(likeMemberData[i] != null && likeMemberData[i].isEmpty() == false)
					likeMemberList.add(likeMemberData[i]);
			}
		}
	}
}
