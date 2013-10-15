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
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (convertView == null) {
			int res = 0;
			if(position != getCount()-1)
				res = R.layout.profile_class;
			else
				res = R.layout.profile_addchild;
			convertView = mInflater.inflate(res, parent, false);
			
			// �׸� �並 �ʱ�ȭ�Ѵ�.
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
