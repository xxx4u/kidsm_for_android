package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

public class RegisterOrgItem extends BaseItem implements Parcelable {
	String org_srl;
	String org_address;
	String org_teacher_key;
	public ArrayList<RegisterClassItem> classList;
	View layout;
	boolean isVisible;
	
	public RegisterOrgItem(String org_srl, String name, String org_address, String org_teacher_key) {
		this.org_srl = org_srl;
		this.org_address = org_address;
		this.org_teacher_key = org_teacher_key;
		this.name = name;
		this.classList = new ArrayList<RegisterClassItem>();
		isVisible = true;
	}
	
	public RegisterOrgItem(String org_srl, String name, String org_address, String org_teacher_key, ArrayList<RegisterClassItem> classList) {
		this.org_srl = org_srl;
		this.org_address = org_address;
		this.org_teacher_key = org_teacher_key;
		this.name = name;
		this.classList = classList;
		isVisible = true;
	}
	
	public void setClassList(ArrayList<RegisterClassItem> classList) {
		this.classList = classList;
	}
	
	public void addClass(RegisterClassItem item) {
		classList.add(item);
	}

	// Parcelling part
    public RegisterOrgItem(Parcel in){
        org_srl = in.readString();
        layout = (View) in.readValue(View.class.getClassLoader());
		classList = new ArrayList<RegisterClassItem>();
		in.readTypedList(classList, RegisterClassItem.CREATOR);
        isVisible = true;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RegisterOrgItem createFromParcel(Parcel in) {
            return new RegisterOrgItem(in); 
        }

        public RegisterOrgItem[] newArray(int size) {
            return new RegisterOrgItem[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(org_srl);
		dest.writeValue(layout);
		dest.writeTypedList(classList);
	}
}
