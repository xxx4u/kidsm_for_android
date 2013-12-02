package com.ihateflyingbugs.kidsm.login;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.uploadphoto.InputTag;

public class RegisterChildItem extends BaseItem implements Parcelable {
	private String member_srl;
	private String student_srl;
	String org_srl;
	private String class_srl;
	private String parent_srl;
	private String teacher_srl;
	private String shuttle_srl;
	private String birthday;
	private boolean isChecked;
	View layout;
	
	public RegisterChildItem(String member_srl, String student_srl, String name, String org_srl, String class_srl, String parent_srl, String teacher_srl, String shuttle_srl, String birthday) {
		this.setMember_srl(member_srl);
		this.setStudent_srl(student_srl);
		this.name = name;
		this.org_srl = org_srl;
		this.setClass_srl(class_srl);
		this.setParent_srl(parent_srl);
		this.setTeacher_srl(teacher_srl);
		this.setShuttle_srl(shuttle_srl);
		this.setBirthday(birthday);
	}
	
    // Parcelling part
    public RegisterChildItem(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.setBirthday(data[1]);
        setChecked(false);
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
                this.getBirthday()});
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getParent_srl() {
		return parent_srl;
	}

	public void setParent_srl(String parent_srl) {
		this.parent_srl = parent_srl;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getStudent_srl() {
		return student_srl;
	}

	public void setStudent_srl(String student_srl) {
		this.student_srl = student_srl;
	}

	public String getMember_srl() {
		return member_srl;
	}

	public void setMember_srl(String member_srl) {
		this.member_srl = member_srl;
	}

	public String getShuttle_srl() {
		return shuttle_srl;
	}

	public void setShuttle_srl(String shuttle_srl) {
		this.shuttle_srl = shuttle_srl;
	}

	public String getTeacher_srl() {
		return teacher_srl;
	}

	public void setTeacher_srl(String teacher_srl) {
		this.teacher_srl = teacher_srl;
	}

	public String getClass_srl() {
		return class_srl;
	}

	public void setClass_srl(String class_srl) {
		this.class_srl = class_srl;
	}
}
