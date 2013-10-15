package com.ihateflyingbugs.kidsm.gallery;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.menu.Children;
import com.ihateflyingbugs.kidsm.menu.OrgClassTeacher;

public class Album extends BaseItem implements Parcelable {
	public enum ALBUMTYPE {
		NEW,
		NORMAL,
		SCRAP,
		ALL,
		TAGGED, 
		
		MODIFIED,
		DELETED
	}

	public ALBUMTYPE type;
	public String album_srl;
	public String album_member_srl;
	public String album_name;
	public String album_type;
	public String album_created;
	public String album_updated;
	public String album_count;
	public ArrayList<Photo> photoList;
	public boolean needSetting;
	
	public Album(ALBUMTYPE type, String album_srl, String album_member_srl, String album_name, String album_type, String album_created, String album_updated, String album_count){
		//this.setIdentifier(identifier);
		this.type = type;
		this.album_srl = album_srl;
		this.album_member_srl = album_member_srl;
		this.album_name = album_name;
		this.album_type = album_type;
		this.album_created = album_created;
		this.album_updated = album_updated;
		this.album_count = album_count;
		this.photoList = new ArrayList<Photo>();
		needSetting = true;
	}
	
	// Parcelling part
    public Album(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);
        this.type = ALBUMTYPE.valueOf(data[0]);
        this.album_srl = data[1];
        this.album_member_srl = data[2];
        this.album_name = data[3];
        this.album_type = data[4];
        this.album_created = data[5];
        this.album_updated = data[6];
        this.album_count = data[7];
        
        photoList = new ArrayList<Photo>();
        in.readTypedList(photoList, Photo.CREATOR);

		needSetting = true;
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel in) {
            return new Album(in); 
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {type.name(), album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count});
		dest.writeTypedList(photoList);
	}
	
	public void getPhoto(String photo_srl) {
		for(int i = 0; i < photoList.size(); i++) {
			
		}
	}
	
	public String getTitle() {
		return album_name + " (" + photoList.size() + ")";
	}
}
