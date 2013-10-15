package com.ihateflyingbugs.kidsm.schedule;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.BaseItem;

public class CalendarEvent extends BaseItem {
	int type;
	String startDate;
	String endDate;
	String description;
	String cal_srl;
	String cal_org_srl;
	String cal_class_srl;
	String cal_member_srl;
	String cal_type;
	boolean isChecked;
	int numOfClassMember;
	ArrayList<CheckInfo> checkInfoList;
	
	public CalendarEvent(int type, String startDate, String endDate, String description, String cal_srl, String cal_org_srl, String cal_class_srl, String cal_member_srl, String cal_type) {
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.cal_srl = cal_srl;
		this.cal_org_srl = cal_org_srl;
		this.cal_class_srl = cal_class_srl;
		this.cal_member_srl = cal_member_srl;
		this.cal_type = cal_type;
		isChecked = false;
		numOfClassMember = 0;
	}
}
