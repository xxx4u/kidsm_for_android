package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.login.RegisterChildItem;

public class Children extends BaseItem implements Parcelable {
	public String student_srl;
	public String student_member_srl;
	public String student_name;
	public String student_picture;
	public String student_parent_name;
	public String student_org_srl;
	public String student_class_srl;
	public String student_parent_srl;
	public String student_teacher_srl;
	public String student_shuttle_srl;
	public String student_birthday;
	public String student_parent_key;
	
	public String organizationName;
	public String className;
	public ArrayList<OrgClassTeacher> teacherList;
	
	// Parcelling part
    public Children(Parcel in){
        String[] data = new String[14];

        in.readStringArray(data);
        this.student_srl = data[0];
        this.student_member_srl = data[1];
        this.student_name = data[2];
        this.student_picture = data[3];
        this.student_parent_name = data[4];
        this.setStudent_org_srl(data[5]);
        this.student_class_srl = data[6];
        this.student_parent_srl = data[7];
        this.student_teacher_srl = data[8];
        this.student_shuttle_srl = data[9];
        this.student_birthday = data[10];
        this.student_parent_key = data[11];
        this.organizationName = data[12];
        this.className = data[13];
        
        teacherList = new ArrayList<OrgClassTeacher>();
        in.readTypedList(teacherList, OrgClassTeacher.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Children createFromParcel(Parcel in) {
            return new Children(in); 
        }

        public Children[] newArray(int size) {
            return new Children[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {student_srl,
				student_member_srl, student_name, student_picture, student_parent_name, getStudent_org_srl(), student_class_srl, student_parent_srl, student_teacher_srl
				, student_shuttle_srl, student_birthday, student_parent_key, organizationName, className});
		dest.writeTypedList(teacherList);
	}
	
	public Children(String student_srl, String student_member_srl, String student_name, String student_picture, String student_parent_name,
			String student_org_srl, String student_class_srl, String student_parent_srl, String student_teacher_srl, 
			String student_shuttle_srl, String student_birthday, String student_parent_key) {
		this.student_srl = student_srl;
		this.student_member_srl = student_member_srl;
		this.setStudent_name(student_name);
		this.student_picture = student_picture;
		this.setStudent_parent_name(student_parent_name);
		this.setStudent_org_srl(student_org_srl);
		this.student_class_srl = student_class_srl;
		this.student_parent_srl = student_parent_srl;
		this.student_teacher_srl = student_teacher_srl;
		this.student_shuttle_srl = student_shuttle_srl;
		this.student_birthday = student_birthday;
		this.student_parent_key = student_parent_key;

		this.organizationName = "";
		this.className = "";
		teacherList = new ArrayList<OrgClassTeacher>();
	}

	public String getStudent_parent_name() {
		return student_parent_name;
	}

	public void setStudent_parent_name(String student_parent_name) {
		this.student_parent_name = student_parent_name;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public void addTeacher(OrgClassTeacher teacher) {
		teacherList.add(teacher);
	}
	public String getStudent_org_srl() {
		return student_org_srl;
	}
	public void setStudent_org_srl(String student_org_srl) {
		this.student_org_srl = student_org_srl;
	}
}
