package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

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

class MenuChildrenAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<Children> arSrc;
	Context context;
	
	public MenuChildrenAdapter(Context context, ArrayList<Children> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
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
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (convertView == null) {
			int res = 0;
			if(position != getCount()-1)
				res = R.layout.profile_child;
			else
				res = R.layout.profile_addchild;
			convertView = mInflater.inflate(res, parent, false);
			
			// 항목 뷰를 초기화한다.
			if(position != getCount()-1) {
				convertView.setTag(position);
				TextView txt = (TextView)convertView.findViewById(R.id.child_parentname);
				txt.setText(arSrc.get(position).getStudent_parent_name());
				txt = (TextView)convertView.findViewById(R.id.child_childname);
				txt.setText(arSrc.get(position).getStudent_name());
				txt = (TextView)convertView.findViewById(R.id.child_organization);
				txt.setText(arSrc.get(position).getOrganizationName());
				txt = (TextView)convertView.findViewById(R.id.child_class);
				txt.setText(arSrc.get(position).getClassName());
				RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.child_spacelayout);
				ImageView img = (ImageView)relative.findViewById(R.id.child_mainpicture);
				if(position == 0)
					img.setImageBitmap(ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.son1)));
				else
					img.setImageBitmap(ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.son2)));
				
			}
			else {
				TextView txt = (TextView)convertView.findViewById(R.id.profile_add_message);
				txt.setText(arSrc.get(position).student_name);
			}
		}
		
		
		return convertView;
	}
}
