package com.ihateflyingbugs.kidsm.newsfeed;

import com.ihateflyingbugs.kidsm.BaseItem;

public class Reply extends BaseItem {
	String profileImage;
	String message;
	
	Reply(String name, String profileImage, String message) {
		this.setName(name);
		setProfileImage(profileImage);
		setMessage(message);
	}

	public String getProfileImage() {
		return profileImage;
	}
	
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
