package com.ihateflyingbugs.kidsm.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class OrgClassTeacher implements Parcelable {
	public String teacher_srl;
	public String teacher_name;
	public String teacher_member_srl;
	public String teacher_class_srl;
	public String teacher_shuttle_srl;
	
	public OrgClassTeacher(String teacher_srl, String teacher_name, String teacher_member_srl, String teacher_class_srl, String teacher_shuttle_srl) {
		this.teacher_srl = teacher_srl;
		this.teacher_name = teacher_name;
		this.teacher_member_srl = teacher_member_srl;
		this.teacher_class_srl = teacher_class_srl;
		this.teacher_shuttle_srl = teacher_shuttle_srl;
	}
	
	// Parcelling part
    public OrgClassTeacher(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.teacher_srl = data[0];
        this.teacher_name = data[1];
        this.teacher_member_srl = data[2];
        this.teacher_class_srl = data[3];
        this.teacher_shuttle_srl = data[4];
    }
    
    public static final Parcelable.Creator<OrgClassTeacher> CREATOR = new Parcelable.Creator<OrgClassTeacher>() {
        public OrgClassTeacher createFromParcel(Parcel in) {
            return new OrgClassTeacher(in); 
        }

        public OrgClassTeacher[] newArray(int size) {
            return new OrgClassTeacher[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {teacher_srl, teacher_name, teacher_member_srl, teacher_class_srl, teacher_shuttle_srl});
	}
	
}
