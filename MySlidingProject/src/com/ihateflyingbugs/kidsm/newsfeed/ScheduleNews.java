package com.ihateflyingbugs.kidsm.newsfeed;

public class ScheduleNews extends News {
	
	String cal_srl;
	String cal_org_srl;
	String cal_class_srl;
	String cal_member_srl;
	String cal_type;
	String cal_year;
	String cal_month;
	String cal_day;
	String cal_time;
	String cal_timestamp;
	String cal_name;
	String cal_created;
	
	public ScheduleNews(String cal_srl, String cal_org_srl, String cal_class_srl, String cal_member_srl, String cal_type, String cal_year, String cal_month, String cal_day, String cal_time, String cal_timestamp, String cal_name, String cal_created) {
		type = NEWSTYPE.SCHEDULE;
		this.cal_srl = cal_srl;
		this.cal_org_srl = cal_org_srl;
		this.cal_class_srl = cal_class_srl;
		this.cal_member_srl = cal_member_srl;
		this.cal_type = cal_type;
		this.cal_year = cal_year;
		this.cal_month = cal_month;
		this.cal_day = cal_day;
		this.cal_time = cal_time;
		this.cal_timestamp = cal_timestamp;
		this.cal_name = cal_name;
		this.cal_created = cal_created;
	}
}
