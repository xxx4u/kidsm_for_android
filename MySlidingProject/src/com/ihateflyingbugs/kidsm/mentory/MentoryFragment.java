package com.ihateflyingbugs.kidsm.mentory;

import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

public class MentoryFragment extends NetworkFragment {
	public enum MentoryType {
		edu,
		infant,
		healing,
		scrap
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
		layout = inflater.inflate(R.layout.activity_mentory_parent, container, false);
		
		mentoryType = MentoryType.edu;
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.mentory_page);
		LinearLayout eduPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_edu);
		eduPage.addView(inflater.inflate(R.layout.mentory_edu, container, false));
		eduPage.addView(inflater.inflate(R.layout.mentory_edu, container, false));
		LinearLayout infantPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_infant);
		infantPage.addView(inflater.inflate(R.layout.mentory_infant, container, false));
		
		LinearLayout healingPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_healing);
		healingPage.addView(inflater.inflate(R.layout.mentory_healing, container, false));
		
		LinearLayout scrapPage = (LinearLayout) viewFlipper.findViewById(R.id.mentory_page_scrap);
		
		RadioGroup rg = (RadioGroup) layout.findViewById(R.id.mentory_mode);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
				case R.id.mentory_edu:
					switch(mentoryType) {
					case edu:
						break;
					case infant:
						goPrev();
						break;
					case healing:
						goPrev();
						goPrev();
						break;
					case scrap:
						goPrev();
						goPrev();
						goPrev();
						break;
					}
					mentoryType = MentoryType.edu;
					break;
				case R.id.mentory_infant:
					switch(mentoryType) {
					case edu:
						goNext();
						break;
					case infant:
						break;
					case healing:
						goPrev();
						break;
					case scrap:
						goPrev();
						goPrev();
						break;
					}
					mentoryType = MentoryType.infant;
					break;
				case R.id.mentory_healing:
					switch(mentoryType) {
					case edu:
						goNext();
						goNext();
						break;
					case infant:
						goNext();
						break;
					case healing:
						break;
					case scrap:
						goPrev();
						break;
					}
					mentoryType = MentoryType.healing;
					break;
				case R.id.mentory_scrap:
					switch(mentoryType) {
					case edu:
						goNext();
						goNext();
						goNext();
						break;
					case infant:
						goNext();
						goNext();
						break;
					case healing:
						goNext();
						break;
					case scrap:
						break;
					}
					mentoryType = MentoryType.scrap;
					break;
				}
			}
			
		});
		return layout;
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
