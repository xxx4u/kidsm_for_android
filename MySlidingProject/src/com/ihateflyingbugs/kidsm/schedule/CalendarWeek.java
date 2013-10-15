package com.ihateflyingbugs.kidsm.schedule;

import java.util.ArrayList;

import android.view.View;

import com.ihateflyingbugs.kidsm.BaseItem;

public class CalendarWeek extends BaseItem {
	public enum CalendarWeekType {
		BLANK,
		DATE,
		SCHEDULE_FOR_PARENT,
		SCHEDULE_FOR_TEACHER,
		BIRTHDAY
	}
	public CalendarWeekType type;
	public ArrayList<CalendarEvent> events;
	public String date;
	View layout;
	
	public CalendarWeek(CalendarWeekType type, String date) {
		this.type = type;
		this.date = date;
		this.events = new ArrayList<CalendarEvent>();
	}
}
