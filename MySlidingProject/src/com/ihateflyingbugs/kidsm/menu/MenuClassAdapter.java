package com.ihateflyingbugs.kidsm.menu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;

class MenuClassAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<OrgClass> arSrc;
	Context context;
	
	public MenuClassAdapter(Context context, ArrayList<OrgClass> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
	}

	public int getCount() {
		return arSrc.size();
	}

	public OrgClass getItem(int position) {
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
				res = R.layout.profile_class;
			else
				res = R.layout.profile_addchild;
			convertView = mInflater.inflate(res, parent, false);
			
			// 항목 뷰를 초기화한다.
			if(position != getCount()-1) {
				convertView.setTag(position);
				TextView txt = (TextView)convertView.findViewById(R.id.class_orgname);
				txt.setText(SlidingMenuMaker.getProfile().org_name);
				txt = (TextView)convertView.findViewById(R.id.class_classname);
				txt.setText(arSrc.get(position).getClass_name());
				txt = (TextView)convertView.findViewById(R.id.class_teachername);
				String teacherName = "";
				for( int i = 0; i < arSrc.get(position).getTeacherList().size(); i++ ) {
					teacherName += arSrc.get(position).getTeacherList().get(i).teacher_name;
					if(i != arSrc.get(position).getTeacherList().size()-1 )
						teacherName += ", ";
				}
				txt.setText(teacherName);
//				RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.child_spacelayout);
//				ImageView img = (ImageView)relative.findViewById(R.id.child_mainpicture);
//				if(position == 0)
//					img.setImageBitmap(ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.son1)));
//				else
//					img.setImageBitmap(ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.son2)));
				
			}
			else {
				TextView txt = (TextView)convertView.findViewById(R.id.profile_add_message);
				txt.setText(arSrc.get(position).getClass_name());
			}
		}
		
		
		return convertView;
	}
}
