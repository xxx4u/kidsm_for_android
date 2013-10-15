package com.ihateflyingbugs.kidsm.friend;

import android.os.Parcel;
import android.os.Parcelable;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.menu.Children;

public class FriendListItem extends BaseItem {
	public enum FriendListItemType {
		NAMETAG,
		RECOMMENDED_FRIEND,
		CURRENT_FRIEND,
		REQUESTED_FRIEND,
		WAITING_FRIEND,
		CURRENT_STUDENT,
		GRANTED_TEACHER,
		CURRENT_STUDENT_FOR_MANAGER,
		
		
		UNGRANTED_TEACHER,
		REQUESTED_MESSAGE
	}
	
	public class FriendInfo {
		public String friend_srl;
		public String origin_srl;
		public String target_srl;
		public char status;
		
		public FriendInfo(String friend_srl, String origin_srl, String target_srl, char status) {
			this.friend_srl = friend_srl;
			this.origin_srl = origin_srl;
			this.target_srl = target_srl;
			this.status = status;
		}
		
		public FriendInfo(FriendInfo info) {
			this.friend_srl = info.friend_srl;
			this.origin_srl = info.origin_srl;
			this.target_srl = info.target_srl;
			this.status = info.status;
		}
	}
	
	public class StudentsParentInfo {
		public String parent_srl;
		public String student_srl;
		public String student_member_srl;
		
		public StudentsParentInfo(String parent_srl, String student_srl, String student_member_srl) {
			this.parent_srl = parent_srl;
			this.student_srl = student_srl;
			this.student_member_srl = student_member_srl;
		}
		
		public StudentsParentInfo(StudentsParentInfo info) {
			this.parent_srl = info.parent_srl;
			this.student_srl = info.student_srl;
			this.student_member_srl = info.student_member_srl;
		}
	}
	
	public FriendListItem(FriendListItemType type, String name, String childname) {
		this.type = type;
		setName(name);
		this.setChildname(childname);
	}
	
	public FriendListItem(FriendListItem item) {
		this.type = item.type;
		setName(item.name);
		this.setChildname(item.childname);
	}
	
	FriendListItemType type;
	private String childname;
	FriendInfo friendInfo;
	private StudentsParentInfo studentsParentInfo;
	
//	// Parcelling part
//    public FriendListItem(Parcel in){
//        String[] data = new String[2];
//
//        in.readStringArray(data);
//        this.name = data[0];
//        this.setChildname(data[1]);
//        this.type = in.readInt();
//    }
//
//    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//        public FriendListItem createFromParcel(Parcel in) {
//            return new FriendListItem(in); 
//        }
//
//        public FriendListItem[] newArray(int size) {
//            return new FriendListItem[size];
//        }
//    };
//    
//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeStringArray(new String[] {this.name, this.getChildname()});
//		dest.writeInt(this.type);
//	}
	public String getChildname() {
		return childname;
	}
	public void setChildname(String childname) {
		this.childname = childname;
	}

	public FriendInfo getFriendInfo() {
		return friendInfo;
	}
	public void setFriendInfo(FriendInfo friendInfo) {
		this.friendInfo = friendInfo;
	}
	public StudentsParentInfo getStudentsParentInfo() {
		return studentsParentInfo;
	}
	public void setStudentsParentInfo(StudentsParentInfo studentsParentInfo) {
		this.studentsParentInfo = studentsParentInfo;
	}
}
