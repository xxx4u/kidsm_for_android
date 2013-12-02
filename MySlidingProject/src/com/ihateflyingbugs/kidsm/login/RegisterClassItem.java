package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class RegisterClassItem extends BaseItem implements Parcelable {
	private String class_srl;
	private ArrayList<RegisterChildItem> childList;
	View layout;
	
	public RegisterClassItem(String class_srl, String name) {
		this.setClass_srl(class_srl);
		this.setChildList(new ArrayList<RegisterChildItem>());
		this.name = name;
	}
	
	public void addChild(RegisterChildItem item) {
		getChildList().add(item);
	}
	
    // Parcelling part
    public RegisterClassItem(Parcel in){
    	setClass_srl(in.readString());
    	name = in.readString();
    	setChildList(new ArrayList<RegisterChildItem>());
    	in.readTypedList(getChildList(), RegisterChildItem.CREATOR);
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
		dest.writeString(getClass_srl());
		dest.writeString(name);
		dest.writeTypedList(getChildList());	
	}

	public String getClass_srl() {
		return class_srl;
	}

	public void setClass_srl(String class_srl) {
		this.class_srl = class_srl;
	}

	public ArrayList<RegisterChildItem> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<RegisterChildItem> childList) {
		this.childList = childList;
	}
}
