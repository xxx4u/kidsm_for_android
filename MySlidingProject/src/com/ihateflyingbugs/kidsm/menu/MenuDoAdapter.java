package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class MenuDoButton extends BaseItem {
	int Type;
	Drawable iconImage;
	Drawable selectedImage;
	View layout;
	boolean measured;
	
	public MenuDoButton(String name, Drawable iconImage, Drawable selectedImage) {
		if(name == null) {
			Type = 0;
		}
		else {
			setName(name);
			this.iconImage = iconImage;
			this.selectedImage = selectedImage;
			Type = 1;
		}
	}
}

class MenuDoAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<MenuDoButton> arSrc;
	Context context;
	
	public MenuDoAdapter(Context context, ArrayList<MenuDoButton> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		arSrc = arItem;
		
	}

	public int getCount() {
		return arSrc.size();
	}

	public MenuDoButton getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	public int getItemViewType(int position) {
		return arSrc.get(position).Type;
	}
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (convertView == null) {
			int res = 0;
			switch (arSrc.get(position).Type) {
			case 0:
				res = R.layout.linear_for_space;
				convertView = mInflater.inflate(res, parent, false);
				
				final View cv = convertView;
				final View pv = parent;
				arSrc.get(0).layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

					@Override
					public void onGlobalLayout() {
						if(arSrc.get(0).measured == false) {
							DisplayMetrics displaymetrics = new DisplayMetrics();
							((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
							int height = displaymetrics.heightPixels;
							int[] above = new int[2];
							arSrc.get(0).layout.getLocationOnScreen(above);
							height -= above[1];
							height -= (arSrc.size()-1)*arSrc.get(0).layout.getMeasuredHeight();
							((LinearLayout)cv).getLayoutParams().height = height - 15;
							pv.getLayoutParams().height = height + (arSrc.size()-1)*arSrc.get(0).layout.getMeasuredHeight();
							arSrc.get(0).measured = true;
						}
					}
					
				});
				break;
			case 1:
				res = R.layout.menu_dobutton;
				convertView = mInflater.inflate(res, parent, false);
				break;
			}
			arSrc.get(position).layout = convertView; 
			
			if(position == 0) {
				
				if (Build.VERSION.SDK_INT >= 16) 
					arSrc.get(position).layout.setBackground(arSrc.get(position).selectedImage);
				else 
					arSrc.get(position).layout.setBackgroundDrawable(arSrc.get(position).selectedImage);

			}
			// 항목 뷰를 초기화한다.
			switch (arSrc.get(position).Type) {
			case 1:
				TextView txt1 = (TextView)convertView.findViewById(R.id.menu_dobutton_text);
				txt1.setText(arSrc.get(position).getName());
				ImageView icon = (ImageView)convertView.findViewById(R.id.menu_dobutton_image);
				icon.setImageDrawable(arSrc.get(position).iconImage);
				break;
			}
		}

		return convertView;
	}
}