package com.ihateflyingbugs.kidsm.schedule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.WrappingSlidingDrawer;
import com.ihateflyingbugs.kidsm.menu.Profile;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.schedule.CalendarWeek.CalendarWeekType;
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

public class ScheduleFragment extends NetworkFragment {
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter1;// adapter instance
	//public CalendarAdapter adapter2;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	//ArrayList<String> event;
	LinearLayout rLayout;
	ArrayList<String> date;
	ArrayList<String> desc;
	
	LayoutInflater inflater;
	View layout;
	static boolean isMeasured;
	ViewFlipper calendarFlipper;
	int scheduleMode;
	String selectedGridDate;
	ArrayList<CalendarEvent> eventList;
	TextView numOfBirth;
	Dialog singlePopDateDialog;
	float prevTouchX;
	float prevTouchY;
	boolean isFling;
	WrappingSlidingDrawer drawer;
	boolean canUploadPhoto;
	int monthMemory;

	boolean isRequestForConfirmedList;
	boolean isRequestForShowDialog;
	boolean isRequestForShowWeekPage;
	
	ArrayList<CalendarWeek> weekData;
	CalendarWeekAdapter calendarWeekAdapter;
	ListView weekList;
	ArrayList<CalendarWeek> dayData;
	CalendarWeekAdapter calendarDayAdapter;
	ListView dayList;
	
	ArrayList<String> cal_srlList;
	int getCheckCounter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_schedule, container, false);
		
		auth_key = MainActivity.auth_key;
		
		Locale.setDefault(Locale.US);

		isRequestForConfirmedList = false;
		isRequestForShowDialog = false;
		isRequestForShowWeekPage = false;
		
		cal_srlList = new ArrayList<String>();
		
		weekData = new ArrayList<CalendarWeek>();
		calendarWeekAdapter = new CalendarWeekAdapter(getActivity(), weekData);
		dayData = new ArrayList<CalendarWeek>();
		calendarDayAdapter = new CalendarWeekAdapter(getActivity(), dayData);
		
		weekList = (ListView)layout.findViewById(R.id.schedule_weekpage);
		weekList.setDivider(null);
		weekList.setDividerHeight(0);
		weekList.setAdapter(calendarWeekAdapter);
		
		LinearLayout dateDialog = (LinearLayout) inflater.inflate(R.layout.schedule_datedialog, null);
		dayList = (ListView) dateDialog.findViewById(R.id.schedule_eventlist_day);
		dayList.setDivider(null);
		dayList.setDividerHeight(0);
		dayList.setAdapter(calendarDayAdapter);
		singlePopDateDialog = new Dialog(getActivity(), R.style.TransparentDialog);
		singlePopDateDialog.setContentView(dateDialog);
		
		Button close = (Button)dateDialog.findViewById(R.id.schedule_eventlist_close);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				singlePopDateDialog.cancel();
			}
		});
		
		
		
		
		//rLayout = (LinearLayout) findViewById(R.id.text);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		monthMemory = month.get(Calendar.MONTH);
		
		items = new ArrayList<String>();
		
		selectedGridDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		final ViewFlipper viewFlipper = (ViewFlipper)layout.findViewById(R.id.schedule_flipper);
		RadioGroup rg = (RadioGroup) layout.findViewById(R.id.schedule_mode);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
				case R.id.schedule_monthmode:
					layout.findViewById(R.id.schedule_prev).setVisibility(View.VISIBLE);
					layout.findViewById(R.id.schedule_next).setVisibility(View.VISIBLE);
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
					viewFlipper.showPrevious();
					scheduleMode = 0;
					break;
				case R.id.schedule_weekmode:
					layout.findViewById(R.id.schedule_prev).setVisibility(View.INVISIBLE);
					layout.findViewById(R.id.schedule_next).setVisibility(View.INVISIBLE);
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
					viewFlipper.showNext();
					scheduleMode = 1;
					break;
				}
				refreshCalendar();
			}
			
		});

		LinearLayout monthPage = (LinearLayout)layout.findViewById(R.id.schedule_monthpage);
		monthPage.addView((LinearLayout) inflater.inflate(R.layout.schedule_month, null));
		
		RelativeLayout monthCalendarInfo = (RelativeLayout)monthPage.findViewById(R.id.schedule_month_calendarinfo);
		numOfBirth = (TextView)monthCalendarInfo.findViewById(R.id.schedule_birthday_namelist);
		calendarFlipper = (ViewFlipper)monthCalendarInfo.findViewById(R.id.schedule_calendar_flipper);
		final GridView gridview1 = (GridView) calendarFlipper.findViewById(R.id.schedule_calendar1);
		final GridView gridview2 = (GridView) calendarFlipper.findViewById(R.id.schedule_calendar2);
		eventList = new ArrayList<CalendarEvent>();
//		eventList.add(new CalendarEvent(0, "2013-08-26", "2013-08-26", "카이스트 면접 월 12시 40분 전산학동 E3-1 1층 1441호실 수험번호 3020404"));
//		eventList.add(new CalendarEvent(0, "2013-08-28", "2013-08-28", "카이스트 면접 월 12시 40분 전산학동 E3-1 1층 1441호실 수험번호 3020404"));
//		eventList.add(new CalendarEvent(1, "2013-08-28", "2013-08-28", "김민효"));
//		eventList.add(new CalendarEvent(1, "2013-08-28", "2013-08-28", "김민희"));
//		eventList.add(new CalendarEvent(1, "2013-08-29", "2013-08-29", "정재승"));
//		eventList.add(new CalendarEvent(1, "2013-09-29", "2013-09-29", "정재승"));
		requestEvent();
		isMeasured = false;
		gridview1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

			@Override
			public void onGlobalLayout() {
				if(isMeasured == false) {
					
					adapter1 = new CalendarAdapter(getActivity(), month, eventList);
					gridview1.setAdapter(adapter1);
					//adapter2 = new CalendarAdapter(getActivity(), month, gridview1.getMeasuredHeight()/5);
					gridview2.setAdapter(adapter1);
					isMeasured = true;
				}
			}
		});

		handler = new Handler();
//		handler.post(calendarUpdater);

		TextView title = (TextView) layout.findViewById(R.id.schedule_info);
		title.setText(android.text.format.DateFormat.format("yyyy" + getString(R.string.year) +" MMMM", month));

//		Button previous = (Button) layout.findViewById(R.id.schedule_prev);
//		previous.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				switch(scheduleMode) {
//				case 0:
//					setPreviousMonth();
//					break;
//				case 1:
//					setPreviousWeek();
//					break;
//				}
//				refreshCalendar();
//			}
//		});
//
//		Button next = (Button) layout.findViewById(R.id.schedule_next);
//		next.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				switch(scheduleMode) {
//				case 0:
//					setNextMonth();
//					break;
//				case 1:
//					setNextWeek();
//					break;
//				}
//				refreshCalendar();
//			}
//		});

		gridview1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				OnCalendarItemClick(parent, v, position);
			}
		});
		
		
		gridview2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				OnCalendarItemClick(parent, v, position);
			}
		});

		scheduleMode = 0;
		refreshBirthdayCounter();
		
		drawer = (WrappingSlidingDrawer)layout.findViewById(R.id.schedule_drawer);
		drawer.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
				case 'P':
					return true;
				case 'T':
				case 'M':
					return false;
				}
				return true;
			}
			
		});
		switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
		case 'P':
			canUploadPhoto = false;
			break;
		case 'T':
		case 'M':
			canUploadPhoto = true;
			break;
		}

		return layout;
	}

	private void setPreviousWeek() {
		try {
			Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(selectedDate);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String prevDate = selectedGridDate;
			selectedGridDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			adapter1.currentDateString = selectedGridDate;
			if( prevDate.substring(0, 7).equals(selectedGridDate.substring(0, 7)) == false )
				setPreviousMonth();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void setNextWeek() {
		try {
			Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(selectedDate);
			cal.add(Calendar.DAY_OF_MONTH, 7);
			String prevDate = selectedGridDate;
			selectedGridDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			adapter1.currentDateString = selectedGridDate;
			if( prevDate.substring(0, 7).equals(selectedGridDate.substring(0, 7)) == false )
				setNextMonth();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void OnCalendarItemClick(AdapterView<?> parent, View v, int position) {
		desc = new ArrayList<String>();
		date = new ArrayList<String>();
		((CalendarAdapter) parent.getAdapter()).setSelected(v);
		selectedGridDate = CalendarAdapter.dayString
				.get(position);
		((CalendarAdapter) parent.getAdapter()).currentDateString = selectedGridDate;
		
		String[] separatedTime = selectedGridDate.split("-");
		String gridvalueString = separatedTime[2].replaceFirst("^0*",
				"");// taking last part of date. ie; 2 from 2012-12-02.
		int gridvalue = Integer.parseInt(gridvalueString);
		// navigate to next or previous month on clicking offdays.
		if ((gridvalue > 10) && (position < 8)) {
			setPreviousMonth();
			refreshCalendar();
		} else if ((gridvalue < 7) && (position > 28)) {
			setNextMonth();
			refreshCalendar();
		}
		((CalendarAdapter) parent.getAdapter()).setSelected(v);

		cal_srlList.clear();
		ArrayList<CalendarEvent> eventListOnThisDay = new ArrayList<CalendarEvent>();
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).startDate.equals(selectedGridDate)) {
				if(eventList.get(i).cal_type.equals("N"))
					cal_srlList.add(eventList.get(i).cal_srl);
				eventListOnThisDay.add(eventList.get(i));
			}
		}
		
		if( eventListOnThisDay.size() != 0 ) {
			showDayEventList(selectedGridDate, eventListOnThisDay);
		}

		desc = null;
	}
	
	private void showDayEventList(String dateString, ArrayList<CalendarEvent> eventListOnThisDay) {
		if( singlePopDateDialog != null && singlePopDateDialog.isShowing() )
			return;
		
		getCheckCounter = 0;
		for(int i = 0; i < eventList.size(); i++) {
			if( eventList.get(i).cal_type.equals("N") ) {
				eventList.get(i).checkInfoList.clear();
				eventList.get(i).isChecked = false;
			}
		}
		for(int i = 0; i < cal_srlList.size(); i++)
			ScheduleFragment.this.request_Calender_getCheckCalender(cal_srlList.get(i));
		
		dayData.clear();
		addViewToList(dateString, eventListOnThisDay, DATETYPE.DAY);
		calendarDayAdapter.notifyDataSetChanged();
		isRequestForShowDialog = true;
		
//		new Thread(new Runnable() {
//		    @Override
//		    public void run() {    
//		        ScheduleFragment.this.getActivity().runOnUiThread(new Runnable(){
//		            @Override
//		             public void run() {
		        		
		        		
		        		
//		            }
//		        });
//		    }
//		}).start();
	}
	
	private void showWeekEventList(String dateString) {
		try {
			Date startDay = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDay);
			// Set the calendar to monday of the current week
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			weekData.clear();
			cal_srlList.clear();
			for (int i = 0; i < 7; i++) {
				dateString = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				ArrayList<CalendarEvent> eventListOnThisDay = new ArrayList<CalendarEvent>();
				for (int j = 0; j < eventList.size(); j++) {
					if (eventList.get(j).startDate.equals(dateString)) {
						if(eventList.get(j).cal_type.equals("N"))
							cal_srlList.add(eventList.get(j).cal_srl);
						eventListOnThisDay.add(eventList.get(j));
					}
				}
				if( eventListOnThisDay.size() != 0 ) {
					if( weekData.size() != 0 )
						weekData.add(new CalendarWeek(CalendarWeekType.BLANK, ""));
					addViewToList(dateString, eventListOnThisDay, DATETYPE.WEEK);
				}
			    cal.add(Calendar.DATE, 1);
			}
			
			getCheckCounter = 0;
			for(int i = 0; i < eventList.size(); i++) {
				if( eventList.get(i).cal_type.equals("N") ) {
					eventList.get(i).checkInfoList.clear();
					eventList.get(i).isChecked = false;
				}
			}
			for(int i = 0; i < cal_srlList.size(); i++)
				ScheduleFragment.this.request_Calender_getCheckCalender(cal_srlList.get(i));
			
			calendarWeekAdapter.notifyDataSetChanged();
			isRequestForShowWeekPage = true;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public enum DATETYPE {
		DAY,
		WEEK
	}
	private void addViewToList(String dateString, ArrayList<CalendarEvent> eventListOnThisDay, DATETYPE type){
		Date dateInfo;
		try {
			dateInfo = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			dateString = new SimpleDateFormat("MMMM dd일 EE", Locale.KOREA).format(dateInfo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch(type) {
		case DAY:
			dayData.add(new CalendarWeek(CalendarWeekType.DATE, dateString));
			break;
		case WEEK:
			weekData.add(new CalendarWeek(CalendarWeekType.DATE, dateString));
			break;
		}
		ArrayList<CalendarEvent> BirthdayList = new ArrayList<CalendarEvent>();;
		for(int i = 0; i < eventListOnThisDay.size(); i++) {
			CalendarEvent event = eventListOnThisDay.get(i);
			CalendarWeek item;
			switch(event.type) {
			case 0:
				switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
				case 'P':
					item = new CalendarWeek(CalendarWeekType.SCHEDULE_FOR_PARENT, dateString);
					item.events.add(event);
					switch(type) {
					case DAY:
						dayData.add(item);
						break;
					case WEEK:
						weekData.add(item);
						break;
					}
					break;
				case 'T':
					item = new CalendarWeek(CalendarWeekType.SCHEDULE_FOR_TEACHER, dateString);
					item.events.add(event);
					switch(type) {
					case DAY:
						dayData.add(item);
						break;
					case WEEK:
						weekData.add(item);
						break;
					}
					break;
				case 'M':
					break;
				}
				break;
			case 1:
				BirthdayList.add(event);
				break;
			}
		}
		if(BirthdayList.isEmpty() == false) {
			CalendarWeek item = new CalendarWeek(CalendarWeekType.BIRTHDAY, dateString);
			item.events.addAll(BirthdayList);
			switch(type) {
			case DAY:
				dayData.add(item);
				break;
			case WEEK:
				weekData.add(item);
				break;
			}
		}
	}
	
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}
		calendarFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		calendarFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
		calendarFlipper.showNext();
	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		calendarFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		calendarFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
		calendarFlipper.showPrevious();
	}

	protected void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	protected void refreshBirthdayCounter() {
		int birthdayCounter = 0;
		for(int i = 0; i < eventList.size(); i++) {
			String monthData = new SimpleDateFormat("yyyy-MM").format(month.getTime());
			if(monthData.equals(eventList.get(i).startDate.substring(0, 7)) && eventList.get(i).type == 1)
				birthdayCounter++;
		}
		numOfBirth.setText(""+birthdayCounter+"명");
	}
	
	private void requestEvent() {
		eventList.clear();
		Profile profile = SlidingMenuMaker.getProfile();
		String org_srl = "";
		String class_srl = "";
		String year = ""+this.month.get(Calendar.YEAR);
		String prevmonth = ""+(this.month.get(Calendar.MONTH));
		String month = ""+(this.month.get(Calendar.MONTH)+1);
		String nextmonth = ""+(this.month.get(Calendar.MONTH)+2);
		switch(profile.member_type.charAt(0)) {
		case 'P':
			if( profile.childrenList.size() <= 1 ) 
				return;
			org_srl = profile.getCurrentChildren().student_org_srl;
			class_srl = profile.getCurrentChildren().student_class_srl;
			break;
		case 'T':
			if( profile.classList.size() <= 1 ) 
				return;
			org_srl = profile.member_org_srl;
			class_srl = profile.getCurrentClass().getClass_srl();
			break;
		case 'M':
			if( profile.classList.size() <= 1 ) 
				return;
			org_srl = profile.member_org_srl;
			class_srl = profile.getCurrentClass().getClass_srl();
			break;
		}
		ScheduleFragment.this.request_Calender_getCalenders(org_srl, class_srl, year, prevmonth);
		ScheduleFragment.this.request_Calender_getCalenders(org_srl, class_srl, year, month);
		ScheduleFragment.this.request_Calender_getCalenders(org_srl, class_srl, year, nextmonth);
		ScheduleFragment.this.request_Class_getClassStudent(org_srl, class_srl);
	}
	
	public void refreshCalendar() {
		TextView title = (TextView) layout.findViewById(R.id.schedule_info);

		adapter1.refreshDays();
		adapter1.notifyDataSetChanged();		
		//adapter2.refreshDays();
		//adapter2.notifyDataSetChanged();
//		handler.post(calendarUpdater); // generate some calendar items

		if( monthMemory != month.get(Calendar.MONTH) ) {
			monthMemory = month.get(Calendar.MONTH);
			requestEvent();
		}
		
		
		
		showWeekEventList(selectedGridDate);
		if( scheduleMode == 0 )
			title.setText(android.text.format.DateFormat.format("yyyy" + getString(R.string.year) +" MMMM", month));
		else
		{
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate));
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				Date sundayDate = cal.getTime();
				String sunday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				Date saterdayDate = cal.getTime();
				String saturday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				if( sunday.substring(0, 7).equals(saturday.substring(0, 7)) )
					title.setText(getWeekName(selectedGridDate));
				else {

					Date selectedMonth = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate.substring(0, 8) + "01");
					Date sundayMonth = new SimpleDateFormat("yyyy-MM-dd").parse(sunday.substring(0, 8) + "01");
					Date saterdayMonth = new SimpleDateFormat("yyyy-MM-dd").parse(saturday.substring(0, 8) + "01");
					String first = "";
					String second = "";
					if( sunday.substring(0, 7).equals(selectedGridDate.substring(0, 7)) ) {
						first = getWeekName(sunday);
						setNextMonth();
						adapter1.refreshDays();
						adapter1.notifyDataSetChanged();
						second = getWeekName(saturday);
						if(sundayMonth.equals(selectedMonth)) {
							setPreviousMonth();
							adapter1.refreshDays();
							adapter1.notifyDataSetChanged();
						}
					}
					else {
						second = getWeekName(saturday);
						setPreviousMonth();
						adapter1.refreshDays();
						adapter1.notifyDataSetChanged();
						first = getWeekName(sunday);
						if(saterdayMonth.equals(selectedMonth)) {
							setNextMonth();
							adapter1.refreshDays();
							adapter1.notifyDataSetChanged();
						}
					}
						
					title.setText(first + "\n" + second);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private String getWeekName(String dateString) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		String dateFormatted = new SimpleDateFormat("yyyy" + getString(R.string.year) +" MMMM ", Locale.KOREA).format(date);
		
		for(int i = 0; i < CalendarAdapter.dayString.size(); i++) {
			if( CalendarAdapter.dayString.get(i).equals(dateString) ) {
				int dayOfWeekInMonth = i/7+1;
				switch(dayOfWeekInMonth) {
				case 1:
					dateFormatted += getString(R.string.first_week);
					break;
				case 2:
					dateFormatted += getString(R.string.second_week);
					break;
				case 3:
					dateFormatted += getString(R.string.third_week);
					break;
				case 4:
					dateFormatted += getString(R.string.fourth_week);
					break;
				case 5:
					dateFormatted += getString(R.string.fifth_week);
					break;
				case 6:
					dateFormatted += getString(R.string.sixth_week);
					break;
				}
				break;
			}
		}
		
		return dateFormatted;
	}
//	public Runnable calendarUpdater = new Runnable() {
//
//		@Override
//		public void run() {
//			items.clear();
//
//			// Print dates of the current week
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//			String itemvalue = "";
//			ArrayList<String> event = CalendarUtility.readCalendarEvent(getActivity());
//			//Log.d("=====Event====", event.toString());
//			Log.d("=====Date ARRAY====", CalendarUtility.startDates.toString());
//
//			for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
//				itemvalue = df.format(itemmonth.getTime());
//				itemmonth.add(GregorianCalendar.DATE, 1);
//				items.add(CalendarUtility.startDates.get(i).toString());
//			}
//			adapter1.setItems(items);
//			adapter1.notifyDataSetChanged();
//			//adapter2.setItems(items);
//			//adapter2.notifyDataSetChanged();
//		}
//	};

	public void OnMonthMode(View v) {
		
	}
	
	public void OnWeekMode(View v) {
		
	}

	public void OnPrevMonth(View v) {
		setPreviousMonth();
		refreshCalendar();
	}
	
	public void OnNextMonth(View v) {
		setNextMonth();
		refreshCalendar();
	}
	
	private void showConfirmedList(String cal_srl) {
		Intent intent = new Intent(getActivity(), ShowConfirmedListActivity.class);
		ArrayList<ConfirmedMember> memberList = new ArrayList<ConfirmedMember>();
		for(int i = 0; i < eventList.size(); i++) {
			if(eventList.get(i).cal_type.charAt(0) == 'N' && eventList.get(i).cal_srl.equals(cal_srl)) {
				for(int j = 0; j < eventList.get(i).checkInfoList.size(); j++) {
					memberList.add(new ConfirmedMember(eventList.get(i).checkInfoList.get(j).check_member_srl, ""));
				}
				break;
			}
		}
		intent.putExtra("memberList", memberList);
		startActivity(intent);
	}
	
	public void OnShowConfirmedList(View v) {
		String cal_srl = v.getTag().toString();
		for(int i = 0; i < eventList.size(); i++) {
			if( eventList.get(i).cal_type.equals("N") ) {
				eventList.get(i).checkInfoList.clear();
				eventList.get(i).isChecked = false;
			}
		}
		ScheduleFragment.this.request_Calender_getCheckCalender(cal_srl);
		isRequestForConfirmedList = true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == Activity.RESULT_OK ) {
			Intent intent;
			switch(requestCode) {
			case 0:
				requestEvent();
				break;
			}
		}
	}
	
	public void OnCheckSchedule(View v) {
		for(int i = 0; i < eventList.size(); i++) {
			if(eventList.get(i).cal_type.charAt(0) == 'N' && eventList.get(i).cal_srl.equals(v.getTag().toString())) {
				final int index = i;
				final CheckBox cb = (CheckBox)v;
				cb.setChecked(eventList.get(index).isChecked);
//				cb.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						cb.setChecked(eventList.get(index).isChecked);
//					}
//				});
				if( eventList.get(i).isChecked == false ) 
					ScheduleFragment.this.request_Calender_checkCalender(eventList.get(i).cal_srl, SlidingMenuMaker.getProfile().member_srl);
				break;
			}
		}
	}
	
	public void OnUploadSchedule(View v) {
		Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		if( event.getAction() == MotionEvent.ACTION_DOWN ) {
			prevTouchX = event.getX();
			prevTouchY = event.getY();
			isFling = false;
		}
		else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int width = displaymetrics.widthPixels;
			int height = displaymetrics.heightPixels;
			float gapX = Math.abs(event.getX() - prevTouchX);
			float gapY = Math.abs(event.getY() - prevTouchY);
			
			if( canUploadPhoto && gapY > height/10 ) {
				if( event.getY() - prevTouchY < 0 ) {
					if( drawer.isOpened() == false ) {
						drawer.animateOpen();
					}
				}
				else {
					if( drawer.isOpened() == true ) {
						drawer.animateClose();
					}
				}
				prevTouchY = event.getY();
			}
			if( isFling == false && gapX > width/5 ) {
				if( event.getX() - prevTouchX < 0 ) {
					switch(scheduleMode) {
					case 0:
						setNextMonth();
						break;
					case 1:
						setNextWeek();
						break;
					}
					
				}
				else {
					//prev
					switch(scheduleMode) {
					case 0:
						setPreviousMonth();
						break;
					case 1:
						setPreviousWeek();
						break;
					}
				}
				refreshCalendar();
				//prevTouchX = event.getX();
				isFling = true;
			}
		}
		else if( event.getAction() == MotionEvent.ACTION_UP ) {
		}
	    return true;
	};
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Calender/getCalenders")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String cal_srl = dataArray.getJSONObject(i).getString("cal_srl");
						String cal_org_srl = dataArray.getJSONObject(i).getString("cal_org_srl");
						String cal_class_srl = dataArray.getJSONObject(i).getString("cal_class_srl");
						String cal_member_srl = dataArray.getJSONObject(i).getString("cal_member_srl");
						String cal_type = dataArray.getJSONObject(i).getString("cal_type");
						String cal_year = dataArray.getJSONObject(i).getString("cal_year");
						String cal_month = dataArray.getJSONObject(i).getString("cal_month");
						String cal_day = dataArray.getJSONObject(i).getString("cal_day");
						String cal_time = dataArray.getJSONObject(i).getString("cal_time");
						String cal_timestamp = dataArray.getJSONObject(i).getString("cal_timestamp");
						String cal_name = dataArray.getJSONObject(i).getString("cal_name");
						String cal_created = dataArray.getJSONObject(i).getString("cal_created");
						
						String class_srl = "";
						switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
						case 'P':
							class_srl = SlidingMenuMaker.getProfile().getCurrentChildren().teacherList.get(0).teacher_class_srl;
							break;
						case 'T':
						case 'M':
							class_srl = SlidingMenuMaker.getProfile().getCurrentClass().getClass_srl();
							break;
						}
						if(SlidingMenuMaker.getProfile().member_type.charAt(0) != 'M' && cal_class_srl.equals(class_srl) == false)
							continue;
						
						if(cal_month.length() == 1)
							cal_month = "0"+cal_month;
						if(cal_day.length() == 1)
							cal_day = "0"+cal_day;
						String date = cal_year + "-" + cal_month + "-" + cal_day;
						eventList.add(new CalendarEvent(0, date, date, cal_name, cal_srl, cal_org_srl, cal_class_srl, cal_member_srl, cal_type));
						if(cal_type.charAt(0) == 'N') {
							eventList.get(eventList.size()-1).checkInfoList = new ArrayList<CheckInfo>();
							ScheduleFragment.this.request_Calender_getCheckCalender(cal_srl);
						}
					}
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        ScheduleFragment.this.getActivity().runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	adapter1.notifyDataSetChanged();
					            }
					        });
					    }
					}).start();
				}
				else if(uri.equals("Calender/getCheckCalender")) {
					ArrayList<CheckInfo> checkInfoList = new ArrayList<CheckInfo>();
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					String cal_srl = "";
					for(int i = 0; i < dataArray.length(); i++) {
						String check_srl = dataArray.getJSONObject(i).getJSONObject("_id").getString("$oid");
						String check_cal_srl = dataArray.getJSONObject(i).getString("check_cal_srl");
						String check_member_srl = dataArray.getJSONObject(i).getString("check_member_srl");
						String check_created = dataArray.getJSONObject(i).getString("check_created");
						checkInfoList.add(new CheckInfo(check_srl, check_cal_srl, check_member_srl));
						cal_srl = check_cal_srl;
					}
					if(cal_srl.isEmpty()) {
						for(int i = 0; i < eventList.size(); i++ ) {
							if( eventList.get(i).cal_type.charAt(0) == 'N' ) {
								switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
								case 'P':
									break;
								case 'T':
								case 'M':
									eventList.get(i).numOfClassMember = SlidingMenuMaker.getProfile().getCurrentClass().getNumOfStudentHavingParent();
									break;
								}
							}
						}
					}
					else {
						for(int i = 0; i < eventList.size(); i++ ) {
							if( eventList.get(i).cal_type.charAt(0) == 'N' && cal_srl.equals(eventList.get(i).cal_srl) ) {
								eventList.get(i).checkInfoList.clear();
								eventList.get(i).checkInfoList.addAll(checkInfoList);
								switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
								case 'P':
									for(int j = 0; j < checkInfoList.size(); j++) {
										if(SlidingMenuMaker.getProfile().member_srl.equals(checkInfoList.get(j).check_member_srl)) {
											eventList.get(i).isChecked = true;
											break;
										}
									}
									break;
								case 'T':
								case 'M':
									eventList.get(i).numOfClassMember = SlidingMenuMaker.getProfile().getCurrentClass().getNumOfStudentHavingParent();
									break;
								}
								break;
							}
						}
					}
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        ScheduleFragment.this.getActivity().runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	adapter1.notifyDataSetChanged();
					    			if( isRequestForShowDialog && ++getCheckCounter == cal_srlList.size()) {
					    				String data = "";
					    				for( int i = 0; i < dayData.size(); i++) {
					    					if( dayData.get(i).type != CalendarWeekType.SCHEDULE_FOR_TEACHER)
					    						continue;
					    					for( int j = 0; j < eventList.size(); j++) {
						    					if( dayData.get(i).events.get(0).cal_srl.equals(eventList.get(j).cal_srl) ) {
						    						dayData.get(i).events.get(0).checkInfoList = eventList.get(j).checkInfoList;
						    						data += "" + dayData.get(i).events.get(0).checkInfoList.size() + ", ";
						    						break;
						    					}
					    					}
					    				}
						    			calendarDayAdapter.notifyDataSetChanged();
						    			//dayList.setAdapter(calendarDayAdapter);
					    				isRequestForShowDialog = false;
					    				singlePopDateDialog.show();
					    			}
					    			else if( isRequestForShowWeekPage && ++getCheckCounter == cal_srlList.size()) {
					    				for( int i = 0; i < weekData.size(); i++) {
					    					if( weekData.get(i).type != CalendarWeekType.SCHEDULE_FOR_TEACHER)
					    						continue;
					    					for( int j = 0; j < eventList.size(); j++) {
						    					if( weekData.get(i).events.get(0).cal_srl.equals(eventList.get(j).cal_srl) ) {
						    						weekData.get(i).events.get(0).checkInfoList = eventList.get(j).checkInfoList;
						    						break;
						    					}
					    					}
					    				}
					    				calendarWeekAdapter.notifyDataSetChanged();
					    				isRequestForShowWeekPage = false;
						    			//weekList.setAdapter(calendarWeekAdapter);
					    			}
				    			}
					        });
					    }
					}).start();
					if( isRequestForConfirmedList ) {
						isRequestForConfirmedList = false;
						showConfirmedList(cal_srl);
					}
				}
				else if(uri.equals("Calender/checkCalender")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String check_srl = dataArray.getJSONObject(i).getJSONObject("_id").getString("$oid");
						String check_cal_srl = dataArray.getJSONObject(i).getString("check_cal_srl");
						String check_member_srl = dataArray.getJSONObject(i).getString("check_member_srl");
						String check_created = dataArray.getJSONObject(i).getString("check_created");
						for(int j = 0; j < eventList.size(); j++) {
							if(eventList.get(j).cal_type.charAt(0) == 'N' && eventList.get(j).cal_srl.equals(check_cal_srl)) {
								final int index = j;
								new Thread(new Runnable() {
								    @Override
								    public void run() {    
								        ScheduleFragment.this.getActivity().runOnUiThread(new Runnable(){
								            @Override
								             public void run() {
								            	eventList.get(index).isChecked = true;
								            	adapter1.notifyDataSetChanged();
								            	calendarDayAdapter.notifyDataSetChanged();
								            	calendarWeekAdapter.notifyDataSetChanged();
								            }
								        });
								    }
								}).start();
								break;
							}
						}
					}
					
				}
				else if(uri.equals("Class/getClassStudent")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String member_srl = dataArray.getJSONObject(i).getString("member_srl");
						String member_name = dataArray.getJSONObject(i).getString("member_name");
						String member_type = dataArray.getJSONObject(i).getString("member_type");
						String member_org_srl = dataArray.getJSONObject(i).getString("member_org_srl");
						String member_picture = dataArray.getJSONObject(i).getString("member_picture");
						JSONObject studentObj = dataArray.getJSONObject(i).getJSONObject("student");
						String student_srl = studentObj.getString("student_srl");
						String student_member_srl = studentObj.getString("student_member_srl");
						String student_class_srl = studentObj.getString("student_class_srl");
						String student_parent_srl = studentObj.getString("student_parent_srl");
						String student_teacher_srl = studentObj.getString("student_teacher_srl");
						String student_shuttle_srl = studentObj.getString("student_shuttle_srl");
						String student_birthday = studentObj.getString("student_birthday");
						String student_parent_key = studentObj.getString("student_parent_key");
						if(student_birthday.equals("0"))
							continue;
						Date date = new SimpleDateFormat("yyyyMMdd").parse(student_birthday);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						if( calendar.get(Calendar.MONTH) == this.month.get(Calendar.MONTH)) {
							String dateString = "" + Calendar.getInstance().get(Calendar.YEAR) + new SimpleDateFormat("-MM-dd").format(date);
							eventList.add(new CalendarEvent(1, dateString, dateString, member_name, "", member_org_srl, student_class_srl, member_srl, "B"));
						}
					}
					new Thread(new Runnable() {
					    @Override
					    public void run() {    
					        ScheduleFragment.this.getActivity().runOnUiThread(new Runnable(){
					            @Override
					             public void run() {
					            	adapter1.notifyDataSetChanged();
					            	refreshBirthdayCounter();
					            }
					        });
					    }
					}).start();
				}
			}
			else {
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
