package com.ihateflyingbugs.kidsm.uploadphoto;

import android.graphics.Bitmap;
import android.net.Uri;

import com.ihateflyingbugs.kidsm.BaseItem;

public class PhotoWithCheck extends BaseItem {
	Bitmap bitmap;
	String path;
	Uri uri;
	boolean isChecked;
	
	PhotoWithCheck(String path, Uri uri) {
		this.path = path;
		this.uri = uri;
		isChecked = false;
	}
}
