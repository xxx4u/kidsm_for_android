package com.ihateflyingbugs.kidsm.login;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

public class RegisterOrgItem extends BaseItem implements Parcelable {
	private String org_srl;
	private String org_address;
	private String org_teacher_key;
	public ArrayList<RegisterClassItem> classList;
	View layout;
	private boolean isVisible;
	
	public RegisterOrgItem(String org_srl, String name, String org_address, String org_teacher_key) {
		this.setOrg_srl(org_srl);
		this.setOrg_address(org_address);
		this.setOrg_teacher_key(org_teacher_key);
		this.name = name;
		this.classList = new ArrayList<RegisterClassItem>();
		setVisible(true);
	}
	
	public RegisterOrgItem(String org_srl, String name, String org_address, String org_teacher_key, ArrayList<RegisterClassItem> classList) {
		this.setOrg_srl(org_srl);
		this.setOrg_address(org_address);
		this.setOrg_teacher_key(org_teacher_key);
		this.name = name;
		this.classList = classList;
		setVisible(true);
	}
	
	public void setClassList(ArrayList<RegisterClassItem> classList) {
		this.classList = classList;
	}
	
	public void addClass(RegisterClassItem item) {
		classList.add(item);
	}

	// Parcelling part
    public RegisterOrgItem(Parcel in){
        setOrg_srl(in.readString());
        layout = (View) in.readValue(View.class.getClassLoader());
		classList = new ArrayList<RegisterClassItem>();
		in.readTypedList(classList, RegisterClassItem.CREATOR);
        setVisible(true);
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
		dest.writeString(getOrg_srl());
		dest.writeValue(layout);
		dest.writeTypedList(classList);
	}

	public String getOrg_srl() {
		return org_srl;
	}

	public void setOrg_srl(String org_srl) {
		this.org_srl = org_srl;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getOrg_address() {
		return org_address;
	}

	public void setOrg_address(String org_address) {
		this.org_address = org_address;
	}

	public String getOrg_teacher_key() {
		return org_teacher_key;
	}

	public void setOrg_teacher_key(String org_teacher_key) {
		this.org_teacher_key = org_teacher_key;
	}
}
