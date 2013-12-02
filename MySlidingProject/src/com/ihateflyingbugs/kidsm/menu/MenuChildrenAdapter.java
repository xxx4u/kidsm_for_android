package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuChildrenAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<Children> arSrc;
	Context context;
	ImageLoader imageLoader;
	
	public MenuChildrenAdapter(Context context, ArrayList<Children> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
		imageLoader = new ImageLoader(context, R.drawable.profile_default);
	}

	public int getCount() {
		return arSrc.size();
	}

	public Children getItem(int position) {
		return arSrc.get(position);
	}


	public long getItemId(int position) {
		return position;
	}
	
	// getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
	// 이 메서드에서 개수를 제대로 조사해 주지 않으면 다운된다.
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (arSrc.get(position).layout == null) {
			int res = 0;
			if(position != getCount()-1)
				res = R.layout.profile_child;
			else
				res = R.layout.profile_addchild;
			arSrc.get(position).layout = mInflater.inflate(res, parent, false);
			
			// 항목 뷰를 초기화한다.
			if(position != getCount()-1) {
				arSrc.get(position).layout.setTag(position);
				TextView txt = (TextView)arSrc.get(position).layout.findViewById(R.id.child_parentname);
				txt.setText(arSrc.get(position).getStudent_parent_name());
				txt = (TextView)arSrc.get(position).layout.findViewById(R.id.child_childname);
				txt.setText(arSrc.get(position).getStudent_name());
				txt = (TextView)arSrc.get(position).layout.findViewById(R.id.child_organization);
				txt.setText(arSrc.get(position).getOrganizationName());
				txt = (TextView)arSrc.get(position).layout.findViewById(R.id.child_class);
				txt.setText(arSrc.get(position).getClassName());
				RelativeLayout relative = (RelativeLayout)arSrc.get(position).layout.findViewById(R.id.child_spacelayout);
				ImageView img = (ImageView)relative.findViewById(R.id.child_mainpicture);
				img.setTag(position);
				if(arSrc.get(position).student_picture != null && arSrc.get(position).student_picture.startsWith("profile"))
					imageLoader.DisplayCroppedImage(context.getString(R.string.default_profile_url)+arSrc.get(position).student_picture, img);
				else
					imageLoader.DisplayCroppedImage(context.getString(R.string.profile_url)+arSrc.get(position).student_picture, img);
				
			}
			else {
				TextView txt = (TextView)arSrc.get(position).layout.findViewById(R.id.profile_add_message);
				txt.setText(arSrc.get(position).student_name);
			}
			
		}
		
		
		
		return arSrc.get(position).layout;
	}
}
