package com.ihateflyingbugs.kidsm.login;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

public class RegisterChildItem extends BaseItem implements Parcelable {
	String member_srl;
	String student_srl;
	String org_srl;
	String class_srl;
	String parent_srl;
	String teacher_srl;
	String shuttle_srl;
	String birthday;
	boolean isChecked;
	View layout;
	
	public RegisterChildItem(String member_srl, String student_srl, String name, String org_srl, String class_srl, String parent_srl, String teacher_srl, String shuttle_srl, String birthday) {
		this.member_srl = member_srl;
		this.student_srl = student_srl;
		this.name = name;
		this.org_srl = org_srl;
		this.class_srl = class_srl;
		this.parent_srl = parent_srl;
		this.teacher_srl = teacher_srl;
		this.shuttle_srl = shuttle_srl;
		this.birthday = birthday;
	}
	
    // Parcelling part
    public RegisterChildItem(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.birthday = data[1];
        isChecked = false;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RegisterChildItem createFromParcel(Parcel in) {
            return new RegisterChildItem(in); 
        }

        public RegisterChildItem[] newArray(int size) {
            return new RegisterChildItem[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {this.name,
                this.birthday});
	}
}
