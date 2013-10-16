package com.ihateflyingbugs.kidsm.businfo;

import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class BusInfoFragment extends NetworkFragment {
	LayoutInflater inflater;
	View layout;
	ViewFlipper viewFlipper;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_businfo, container, false);
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.businfo_page);
		
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
	
	public void OnBusClick(View v) {
		goNext();
		int id = v.getId();
		switch(id) {
		case R.id.businfo_bus1:
			((TextView)layout.findViewById(R.id.businfo_busname)).setText("1호차");
			break;
		case R.id.businfo_bus2:
			((TextView)layout.findViewById(R.id.businfo_busname)).setText("2호차");
			break;
		case R.id.businfo_bus3:
			((TextView)layout.findViewById(R.id.businfo_busname)).setText("3호차");
			break;
		case R.id.businfo_bus4:
			((TextView)layout.findViewById(R.id.businfo_busname)).setText("4호차");
			break;
		}
	}
	
	public void OnChangeBus(View v) {
		goPrev();
	}
}
