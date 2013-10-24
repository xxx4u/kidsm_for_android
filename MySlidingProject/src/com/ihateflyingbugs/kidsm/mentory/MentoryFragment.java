package com.ihateflyingbugs.kidsm.mentory;

import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

public class MentoryFragment extends NetworkFragment {
	public enum MentoryType {
		total,
		edu,
		infant,
		manage,
		healing,
		kidsmnews,
		scrap,
		recommend
	}
	LayoutInflater inflater;
	View layout;
	ViewFlipper viewFlipper;
	MentoryType mentoryType;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		
		if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' ) 
			layout = inflater.inflate(R.layout.activity_mentory_parent, container, false);
		else
			layout = inflater.inflate(R.layout.activity_mentory_notparent, container, false);
		
		mentoryType = MentoryType.total;
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.mentory_page);
		
		LinearLayout totalPage1 = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_total1);
		LinearLayout totalPage2 = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_total2);
		LinearLayout totalPage3 = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_total3);
		
		LinearLayout eduPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_edu);
		if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' )  {
			eduPage.addView(inflater.inflate(R.layout.mentory_edu, container, false));
			totalPage1.addView(inflater.inflate(R.layout.mentory_edu, container, false));
		}
		else {
			eduPage.addView(inflater.inflate(R.layout.mentory_edu_notparent, container, false));
			totalPage1.addView(inflater.inflate(R.layout.mentory_edu_notparent, container, false));
		}
		if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' )  {
			LinearLayout infantPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_infant);
			infantPage.addView(inflater.inflate(R.layout.mentory_infant, container, false));
			totalPage2.addView(inflater.inflate(R.layout.mentory_infant, container, false));
		}
		else {
			LinearLayout managePage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_manage);
			if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'T' ) {
				managePage.addView(inflater.inflate(R.layout.mentory_manage_teacher, container, false));
				totalPage2.addView(inflater.inflate(R.layout.mentory_manage_teacher, container, false));
			}
			else {
				managePage.addView(inflater.inflate(R.layout.mentory_manage_manager, container, false));
				totalPage2.addView(inflater.inflate(R.layout.mentory_manage_manager, container, false));
			}
		}
		LinearLayout healingPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_healing);
		healingPage.addView(inflater.inflate(R.layout.mentory_healing, container, false));
		totalPage3.addView(inflater.inflate(R.layout.mentory_healing, container, false));
		
		LinearLayout kidsmnewsPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_kidsmnews);
		kidsmnewsPage.addView(inflater.inflate(R.layout.mentory_manage_manager, container, false));
		LinearLayout scrapPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_scrap);
		scrapPage.addView(inflater.inflate(R.layout.mentory_infant, container, false));
		if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' )  {
			LinearLayout recommendPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_recommend);
			recommendPage.addView(inflater.inflate(R.layout.mentory_healing, container, false));
		}
		
		RadioGroup rg = (RadioGroup) layout.findViewById(R.id.mentory_mode);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
				case R.id.mentory_total:
					switch(mentoryType) {
					case total:
						break;
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(0);
						break;
					}
					mentoryType = MentoryType.total;
					break;
				case R.id.mentory_edu:
					switch(mentoryType) {
					case total:
						setNextAnimation();
						viewFlipper.setDisplayedChild(1);
						break;
					case edu:
						break;
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(1);
						break;
					}
					mentoryType = MentoryType.edu;
					break;
				case R.id.mentory_manage:
				case R.id.mentory_infant:
					switch(mentoryType) {
					case total:
					case edu:
						setNextAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					case manage:
					case infant:
						break;
					case healing:
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(2);
						break;
					}
					if( SlidingMenuMaker.getProfile().member_type.charAt(0) == 'P' ) 
						mentoryType = MentoryType.infant;
					else
						mentoryType = MentoryType.manage;
					break;
				case R.id.mentory_healing:
					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
						setNextAnimation();
						viewFlipper.setDisplayedChild(3);
						break;
					case healing:
						break;
					case kidsmnews:
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(3);
						break;
					}
					mentoryType = MentoryType.healing;
					break;
				case R.id.mentory_kidsmnews:
					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
						setNextAnimation();
						viewFlipper.setDisplayedChild(4);
						break;
					case kidsmnews:
						break;
					case scrap:
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(4);
						break;
					}
					mentoryType = MentoryType.kidsmnews;
					break;
				case R.id.mentory_scrap:
					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
						setNextAnimation();
						viewFlipper.setDisplayedChild(5);
						break;
					case scrap:
						break;
					case recommend:
						setPrevAnimation();
						viewFlipper.setDisplayedChild(5);
						break;
					}
					mentoryType = MentoryType.scrap;
					break;
				case R.id.mentory_recommend:
					switch(mentoryType) {
					case total:
					case edu:
					case manage:
					case infant:
					case healing:
					case kidsmnews:
					case scrap:
						setNextAnimation();
						viewFlipper.setDisplayedChild(6);
						break;
					case recommend:
						break;
					}
					mentoryType = MentoryType.recommend;
					break;
				}
			}
			
		});
		return layout;
	}
	
	public void OnSeeMentory(View v) {
		Intent intent = new Intent(getActivity(), SeeMentoryActivity.class);
		if(v.getTag().toString().equals("mentory_edu_notparent")) {
			intent.putExtra("url", "file:///android_asset/10_edu.html");
		}
		else if(v.getTag().toString().equals("mentory_edu")) {
			intent.putExtra("url", "file:///android_asset/10_edu.html");
		}
		else if(v.getTag().toString().equals("mentory_healing")) {
			intent.putExtra("url", "file:///android_asset/10_healing.html");
		}
		else if(v.getTag().toString().equals("mentory_infant")) {
			intent.putExtra("url", "file:///android_asset/10_infant_parent.html");
		}
		else if(v.getTag().toString().equals("mentory_manage_manager")) {
			intent.putExtra("url", "file:///android_asset/10_manage_manager.html");
		}
		else if(v.getTag().toString().equals("mentory_manage_teacher")) {
			intent.putExtra("url", "file:///android_asset/10_manage_teacher.html");
		}
		startActivity(intent);
	}
	
	private void setPrevAnimation() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
	}
	
	private void setNextAnimation() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
	}
	
	private void goPrev() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
		viewFlipper.showPrevious();
	}
	
	private void goNext() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
		viewFlipper.showNext();
	}
}
