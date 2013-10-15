package com.ihateflyingbugs.kidsm.schedule;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.ihateflyingbugs.kidsm.BaseItem;

public class ConfirmedMember extends BaseItem implements Parcelable {
	public String member_name;
	public String member_picture;
	
	public ConfirmedMember(String member_name, String member_picture) {
		this.member_name = member_name;
		this.member_picture = member_picture;
	}
	
	// Parcelling part
    public ConfirmedMember(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.member_name = data[0];
        this.member_picture = data[1];
    }

    public static final Parcelable.Creator<ConfirmedMember> CREATOR = new Parcelable.Creator<ConfirmedMember>() {
        public ConfirmedMember createFromParcel(Parcel in) {
            return new ConfirmedMember(in); 
        }

        public ConfirmedMember[] newArray(int size) {
            return new ConfirmedMember[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {member_name, member_picture});
	}
}
