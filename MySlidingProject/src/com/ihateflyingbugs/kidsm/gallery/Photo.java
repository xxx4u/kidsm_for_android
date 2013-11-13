package com.ihateflyingbugs.kidsm.gallery;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class Photo extends BaseItem implements Parcelable {
	public String photo_srl;
	public String photo_member_srl;
	public String photo_album_srl;
	public String photo_timeline_srl;
	public String photo_tag;
	public String photo_path;
	public String photo_thumbnail;
	public String photo_like;
	public String photo_private;
	public String photo_created;
	public String photo_updated;
	boolean isSelected;
	View layout;
	
	public Photo(String photo_srl, String photo_member_srl, String photo_album_srl, String photo_timeline_srl, String photo_tag, String photo_path
				, String photo_thumbnail, String photo_like, String photo_private, String photo_created, String photo_updated) {
		this.photo_srl = photo_srl;
		this.photo_member_srl = photo_member_srl;
		this.photo_album_srl = photo_album_srl;
		this.photo_timeline_srl = photo_timeline_srl;
		this.photo_tag = photo_tag;
		this.photo_path = photo_path;
		this.photo_thumbnail = photo_thumbnail;
		this.photo_like = photo_like;
		this.photo_private = photo_private;
		this.photo_created = photo_created;
		this.photo_updated = photo_updated;
		isSelected = false;
	}
	
	// Parcelling part
    public Photo(Parcel in){
        String[] data = new String[11];

        in.readStringArray(data);
		this.photo_srl = data[0];
		this.photo_member_srl = data[1];
		this.photo_album_srl = data[2];
		this.photo_timeline_srl = data[3];
		this.photo_tag = data[4];
		this.photo_path = data[5];
		this.photo_thumbnail = data[6];
		this.photo_like = data[7];
		this.photo_private = data[8];
		this.photo_created = data[9];
		this.photo_updated = data[10];
        isSelected = false;
    }
    
    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in); 
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated});
	}
}
