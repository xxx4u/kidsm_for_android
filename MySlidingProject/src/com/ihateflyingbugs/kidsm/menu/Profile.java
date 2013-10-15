package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
	public String member_srl;
	public String member_name;
	public String member_type;
	public String member_org_srl;
	public String member_point;
	public String member_email;
	public String member_picture_uri;
	public String member_device_type;
	public String member_device_uuid;
	public String parent_srl;
	public String org_name;
	public String org_manager_member_srl;
	public String org_manager_name;
	public ArrayList<Children> childrenList;
	public Map<String, Children> childrens;
	public ArrayList<OrgClass> classList;
	public Map<String, OrgClass> classes;
	public Bitmap member_picture;
	public int selected_index;
	
	// Parcelling part
    public Profile(Parcel in){
        String[] data = new String[13];

        in.readStringArray(data);
        this.member_srl = data[0];
        this.member_name = data[1];
        this.member_type = data[2];
        this.member_org_srl = data[3];
        this.member_point = data[4];
        this.member_email = data[5];
        this.member_picture_uri = data[6];
        this.member_device_type = data[7];
        this.member_device_uuid = data[8];
        this.parent_srl = data[9];
        this.org_name = data[10];
        this.org_manager_member_srl = data[11];
        this.org_manager_name = data[12];
        switch(member_type.charAt(0)) {
        case 'P':
            childrenList = new ArrayList<Children>();
            in.readTypedList(childrenList, Children.CREATOR);
            childrens = new HashMap<String, Children>();
            for(int i = 0; i < childrenList.size(); i++)
            	childrens.put(childrenList.get(i).student_member_srl, childrenList.get(i));
        	break;
        case 'T':
        case 'M':
        	classList = new ArrayList<OrgClass>();
            in.readTypedList(classList, OrgClass.CREATOR);
            classes = new HashMap<String, OrgClass>();
            for(int i = 0; i < classList.size(); i++)
            	classes.put(classList.get(i).getClass_srl(), classList.get(i));
        	break;
        }
        this.selected_index = in.readInt();
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in); 
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {member_srl,
				member_name, member_type, member_org_srl, member_point, member_email, member_picture_uri, member_device_type, member_device_uuid
				, parent_srl, org_name, org_manager_member_srl, org_manager_name});
		switch(member_type.charAt(0)) {
        case 'P':
        	dest.writeTypedList(childrenList);
        	break;
        case 'T':
        case 'M':
    		dest.writeTypedList(classList);
        	break;
        }
		dest.writeInt(selected_index);
	}
	
	public Profile(	String member_srl, String member_name, String member_type, String member_org_srl, String member_point, 
			String member_email, String member_picture_uri, String member_device_type, String member_device_uuid, String parent_srl) {
		this.member_srl = member_srl;
		this.setMember_name(member_name);
		this.member_type = member_type;
		this.member_org_srl = member_org_srl;
		this.member_point = member_point;
		this.member_email = member_email;
		this.member_picture_uri = member_picture_uri;
		this.member_device_type = member_device_type;
		this.member_device_uuid = member_device_uuid;
		this.parent_srl = parent_srl;
		childrens = new HashMap<String, Children>();
		childrenList = new ArrayList<Children>();
		classes = new HashMap<String, OrgClass>();
		classList = new ArrayList<OrgClass>();
	}
	
	public Children getCurrentChildren() {
		return childrenList.get(selected_index);
	}
	
	public OrgClass getCurrentClass() {
		return classList.get(selected_index);
	}
	
	public Children getChildren(String student_member_srl) {
		return childrens.get(student_member_srl);
	}
	public void addChildren(String student_member_srl, Children child) {
		childrens.put(student_member_srl, child);
		childrenList.add(child);
	}

	public OrgClass getClass(String class_srl) {
		return classes.get(class_srl);
	}
	public void addClass(String class_srl, OrgClass orgClass) {
		classes.put(class_srl, orgClass);
		classList.add(orgClass);
	}

	
	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	
	public String getParent_srl() {
		return parent_srl;
	}

	public void setParent_srl(String parent_srl) {
		this.parent_srl = parent_srl;
	}
}
