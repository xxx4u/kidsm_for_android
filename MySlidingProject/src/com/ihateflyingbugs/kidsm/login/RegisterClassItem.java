package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class RegisterClassItem extends BaseItem implements Parcelable {
	String class_srl;
	ArrayList<RegisterChildItem> childList;
	View layout;
	
	public RegisterClassItem(String class_srl, String name) {
		this.class_srl = class_srl;
		this.childList = new ArrayList<RegisterChildItem>();
		this.name = name;
	}
	
	public void addChild(RegisterChildItem item) {
		childList.add(item);
	}
	
    // Parcelling part
    public RegisterClassItem(Parcel in){
    	class_srl = in.readString();
    	name = in.readString();
    	childList = new ArrayList<RegisterChildItem>();
    	in.readTypedList(childList, RegisterChildItem.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RegisterClassItem createFromParcel(Parcel in) {
            return new RegisterClassItem(in); 
        }

        public RegisterClassItem[] newArray(int size) {
            return new RegisterClassItem[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(class_srl);
		dest.writeString(name);
		dest.writeTypedList(childList);	
	}
}
