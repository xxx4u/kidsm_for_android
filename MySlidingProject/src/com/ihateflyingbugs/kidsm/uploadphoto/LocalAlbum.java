package com.ihateflyingbugs.kidsm.uploadphoto;

import android.graphics.Bitmap;

import com.ihateflyingbugs.kidsm.BaseItem;

public class LocalAlbum extends BaseItem {
	String bucketid;
	int numOfPhotos;
	String data;
	Bitmap bitmap;
	public LocalAlbum(String bucketid, String name, String data) {
		this.setName(name);
		this.bucketid = bucketid;
		this.data = data;
		numOfPhotos = 1;
	}
}