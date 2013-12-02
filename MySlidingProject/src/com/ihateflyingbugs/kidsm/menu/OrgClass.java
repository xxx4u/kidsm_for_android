package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class OrgClass extends BaseItem implements Parcelable {
	private String class_srl;
	private String class_org_srl;
	private String class_name;
	private ArrayList<OrgClassTeacher> teacherList;
	private int numOfStudent;
	private int numOfStudentHavingParent;
	View layout;
	
	public OrgClass(String class_srl, String class_org_srl, String class_name) {
		this.setClass_srl(class_srl);
		this.setClass_org_srl(class_org_srl);
		this.setClass_name(class_name);
		setTeacherList(new ArrayList<OrgClassTeacher>());
		setNumOfStudent(0);
		setNumOfStudentHavingParent(0);
	}
	
	public void addTeacher(OrgClassTeacher teacher) {
		getTeacherList().add(teacher);
	}
	
	// Parcelling part
    public OrgClass(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        this.setClass_srl(data[0]);
        this.setClass_org_srl(data[1]);
        this.setClass_name(data[2]);
		setNumOfStudent(Integer.parseInt(data[3]));
		setNumOfStudentHavingParent(Integer.parseInt(data[4]));
		
		teacherList = new ArrayList<OrgClassTeacher>();
		in.readTypedList(teacherList, OrgClassTeacher.CREATOR);
    }
    
    public static final Parcelable.Creator<OrgClass> CREATOR = new Parcelable.Creator<OrgClass>() {
        public OrgClass createFromParcel(Parcel in) {
            return new OrgClass(in); 
        }

        public OrgClass[] newArray(int size) {
            return new OrgClass[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {getClass_srl(), getClass_org_srl(), getClass_name(), ""+getNumOfStudent(), ""+getNumOfStudentHavingParent()});
		dest.writeTypedList(teacherList);
	}

	public ArrayList<OrgClassTeacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(ArrayList<OrgClassTeacher> teacherList) {
		this.teacherList = teacherList;
	}

	public String getClass_srl() {
		return class_srl;
	}

	public void setClass_srl(String class_srl) {
		this.class_srl = class_srl;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getClass_org_srl() {
		return class_org_srl;
	}

	public void setClass_org_srl(String class_org_srl) {
		this.class_org_srl = class_org_srl;
	}

	public int getNumOfStudent() {
		return numOfStudent;
	}

	public void setNumOfStudent(int numOfStudent) {
		this.numOfStudent = numOfStudent;
	}

	public int getNumOfStudentHavingParent() {
		return numOfStudentHavingParent;
	}

	public void setNumOfStudentHavingParent(int numOfStudentHavingParent) {
		this.numOfStudentHavingParent = numOfStudentHavingParent;
	}
}
