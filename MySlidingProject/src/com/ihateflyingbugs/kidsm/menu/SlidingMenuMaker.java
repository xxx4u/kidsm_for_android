package com.ihateflyingbugs.kidsm.menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.OverlayFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.WrappingSlidingDrawer;
import com.ihateflyingbugs.kidsm.notice.NoticeFragment;
import com.ihateflyingbugs.kidsm.photonotice.PhotoNoticeFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;



public class SlidingMenuMaker {
	private static Profile profile;
	public SlidingMenu slidingMenu;
	WrappingSlidingDrawer profileDrawer;
	MainActivity activity;
	int doListClickFocusState;
	ArrayList<MenuDoButton> doListItem;
	ArrayList<MenuDoButton> doListItem2;
	View dolistlayout;
	ImageLoader imageLoader;
	public MenuChildrenAdapter menuChildrenAdapter;
	public MenuClassAdapter menuClassAdapter;
	
	public static Profile getProfile() {
		return profile;
	}
	
	public static void setProfile(Profile _profile) {
		profile = _profile;
	}

	public void Initiate(MainActivity activity) {
		this.activity = activity;
		slidingMenu = new SlidingMenu(activity);
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		
		switch(profile.member_type.charAt(0)) {
		case 'P':
			slidingMenu.setMenu(R.layout.activity_menu);
			break;
		case 'T':
			slidingMenu.setMenu(R.layout.activity_menu_teacher);
			break;
		case 'M':
			slidingMenu.setMenu(R.layout.activity_menu_manager);
			break;
		}
		
		Display newDisplay = activity.getWindowManager().getDefaultDisplay(); 
		@SuppressWarnings("deprecation")
		int width = newDisplay.getWidth();
		
		slidingMenu.setBehindOffset((int) (width*0.15));
		dolistlayout = activity.findViewById(R.id.profile_dolistlayout);
		
		doListClickFocusState = 1;
		doListItem = new ArrayList<MenuDoButton>();
		String[] dolist = activity.getResources().getStringArray(R.array.drawer_dolist);
		String[] dolist_IconImage = activity.getResources().getStringArray(R.array.drawer_dolist_icon);
		String[] dolist_SelectedImage = activity.getResources().getStringArray(R.array.drawer_dolist_selected);
		for( int i = 0; i < dolist.length; i++ ) {
			Drawable iconImage = activity.getResources().getDrawable(ImageMaker.getId(dolist_IconImage[i], R.drawable.class));
			Drawable selectedImage = activity.getResources().getDrawable(ImageMaker.getId(dolist_SelectedImage[i], R.drawable.class));
			if( i == dolist.length-1 )
				doListItem.add(new MenuDoButton(null, null, null));
			doListItem.add(new MenuDoButton(dolist[i], iconImage, selectedImage));
		}
		
		MenuDoAdapter menuDoAdapter = new MenuDoAdapter(activity, doListItem);
		final ListView list = (ListView)activity.findViewById(R.id.profile_dolist1);
		list.setAdapter(menuDoAdapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if( position == doListItem.size()-2 ) return;
				
				if(doListClickFocusState == 1) {
					for(int i = 0; i < doListItem.size(); i++) {
						if( i != position ) {
							doListItem.get(i).layout.setBackgroundColor(Color.TRANSPARENT);
						}
					}
				}
				doListItem.get(position).layout.setBackground(doListItem.get(position).selectedImage);
				doListClickFocusState = 1;
				changeFragment(id);
			}
		});
		list.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if( profileDrawer.isOpened() )
					return true;
				return false;
			}
			
		});

//		profile = new Profile("", "김미정", "P", "1", "1", 
//				"1", ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mom)), "1", "1", "1");
//		profile.addChildren(1, new Children("1", "1", "성자현", "1", "김미정", "1", 
//				"1", "1", "1", "1", "1", "1"));
//		profile.addChildren(2, new Children("2", "2", "성명현", "1", "김미정", "1", 
//				"1", "1", "1", "1", "1", "1"));
//		profile.addChildren(-1, new Children("", "", "자녀 추가하기", "", "", "", "", "", "", "", "", ""));

		imageLoader = new ImageLoader(activity, R.drawable.profile_default);
		ImageView profile_image = (ImageView)activity.findViewById(R.id.mainpicture);
		//profile_image.setImageBitmap(ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mom)));
		if(profile.member_picture_uri.startsWith("profile"))
			imageLoader.DisplayCroppedImage(activity.getString(R.string.default_profile_url)+profile.member_picture_uri, profile_image);
		else
			imageLoader.DisplayCroppedImage(activity.getString(R.string.profile_url)+profile.member_picture_uri, profile_image);
		
		TextView txt;
		switch(profile.member_type.charAt(0)) {
		case 'P':
			txt = (TextView)activity.findViewById(R.id.profile_parentname);
			txt.setText(profile.getMember_name());
			txt = (TextView)activity.findViewById(R.id.profile_childname);
			txt.setText(profile.childrenList.get(0).getStudent_name());
			txt = (TextView)activity.findViewById(R.id.profile_organization);
			txt.setText(profile.childrenList.get(0).getOrganizationName());
			txt = (TextView)activity.findViewById(R.id.profile_class);
			txt.setText(profile.childrenList.get(0).getClassName());
			menuChildrenAdapter = new MenuChildrenAdapter(activity, profile.childrenList);
			ListView list3 = (ListView)activity.findViewById(R.id.profile_childlist);
			list3.setAdapter(menuChildrenAdapter);
			break;
		case 'T':
			txt = (TextView)activity.findViewById(R.id.profile_teachername);
			txt.setText(profile.getMember_name());
			txt = (TextView)activity.findViewById(R.id.profile_organization);
			txt.setText(profile.org_name);
			break;
		case 'M':
			txt = (TextView)activity.findViewById(R.id.profile_managername);
			txt.setText(profile.getMember_name());
			txt = (TextView)activity.findViewById(R.id.profile_organization);
			txt.setText(profile.org_name);
			menuClassAdapter = new MenuClassAdapter(activity, profile.classList);
			ListView list4 = (ListView)activity.findViewById(R.id.profile_childlist);
			list4.setAdapter(menuClassAdapter);
			break;
		}

        profileDrawer = (WrappingSlidingDrawer)activity.findViewById(R.id.profile_drawer);
        profileDrawer.bringToFront();
		
	}
	
	public void changeFragment(long id) {
		if(MainActivity.getCurrentFragmentIndex() != Integer.parseInt(""+id))
			MainActivity.changeFragment(Integer.parseInt(""+id));
		slidingMenu.showContent();
	}
	
	public void OnSeeChildren(View v) {
		if( profileDrawer.isOpened() == false ) {
			switch(profile.member_type.charAt(0)) {
			case 'P':
				menuChildrenAdapter.notifyDataSetChanged();
				break;
			case 'M':
				menuClassAdapter.notifyDataSetChanged();
				break;
			}
			((Button)v).setBackgroundResource(R.drawable.drawer_btnset2);
			dolistlayout.setAlpha(0.2f);
			profileDrawer.animateOpen();
			((Button)v).setText(R.string.close);
		}
		else {
			dolistlayout.setAlpha(1.0f);
			profileDrawer.animateClose();
			((Button)v).setBackgroundResource(R.drawable.drawer_btnset);
			
			switch(profile.member_type.charAt(0)) {
			case 'P':
				((Button)v).setText(R.string.profile_seechildren);
				break;
			case 'M':
				((Button)v).setText(R.string.profile_seeclass);
				break;
			}
		}
	}
	
	public void OnSeeFriend(View v) {
		activity.requestFriendList();
		for(int i = 0; i < doListItem.size(); i++)
			doListItem.get(i).layout.setBackgroundColor(Color.TRANSPARENT);
		doListClickFocusState = 0;
		dolistlayout.setAlpha(1.0f);
	}
	
	public void OnChildClick(View v) {
		int position = (Integer) v.getTag();
		TextView txt;
		switch(profile.member_type.charAt(0)) {
		case 'P':
			txt= (TextView)activity.findViewById(R.id.profile_parentname);
			txt.setText(profile.childrenList.get(position).getStudent_parent_name());
			txt = (TextView)activity.findViewById(R.id.profile_childname);
			txt.setText(profile.childrenList.get(position).getStudent_name());
			txt = (TextView)activity.findViewById(R.id.profile_organization);
			txt.setText(profile.childrenList.get(position).getOrganizationName());
			txt = (TextView)activity.findViewById(R.id.profile_class);
			txt.setText(profile.childrenList.get(position).getClassName());
			((Button)activity.findViewById(R.id.profile_seechildren)).setText(R.string.profile_seechildren);
			break;
		case 'M':
			((Button)activity.findViewById(R.id.profile_seechildren)).setText(R.string.profile_seeclass);
			break;
		}
		profile.selected_index = position;

		dolistlayout.setAlpha(1.0f);
		profileDrawer.animateClose();
	}
	
	private boolean checkIsDate(String date) {
		if(date.length() != 6)
			return false;
		try {	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			dateFormat.setLenient(false);
			dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void OnAddChild(View v) {
//		final View view = ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.register_parent_addchild, null);
//		((TextView)view.findViewById(R.id.register_parent_org)).setText(orgName);
//		((TextView)view.findViewById(R.id.register_parent_class)).setText(className);
//		((TextView)view.findViewById(R.id.register_parent_childname)).setText(childName);
//		new AlertDialog.Builder(activity)
//		.setTitle("자녀 추가하기")
//		.setView(view)
//		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				if( ((TextView)view.findViewById(R.id.register_parent_birthday)).getText().toString().isEmpty() ) {
//					new AlertDialog.Builder(activity)
//					.setMessage("생일을 입력해주세요.")
//					.setPositiveButton("확인", null)
//					.show();
//				}
//				else if(checkIsDate(((TextView)view.findViewById(R.id.register_parent_birthday)).getText().toString()) == false) {
//					new AlertDialog.Builder(activity)
//					.setMessage("생일이 올바르지 않습니다.")
//					.setPositiveButton("확인", null)
//					.show();
//				}
//				else {
//					
//				}
//			}
//		})
//		.
		
		
		switch(profile.member_type.charAt(0)) {
		case 'P':
			((Button)activity.findViewById(R.id.profile_seechildren)).setText(R.string.profile_seechildren);
			break;
		case 'M':
			((Button)activity.findViewById(R.id.profile_seechildren)).setText(R.string.profile_seeclass);
			break;
		}
		dolistlayout.setAlpha(1.0f);
		profileDrawer.animateClose();
		
		Intent intent = new Intent(activity, AddComponentActivity.class);
		activity.startActivity(intent);
	}
}
