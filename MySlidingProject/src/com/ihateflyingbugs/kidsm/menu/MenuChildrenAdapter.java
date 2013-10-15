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
				res = R.layout.profile_child;
			else
				res = R.layout.profile_addchild;
			convertView = mInflater.inflate(res, parent, false);
			
			// �׸� �並 �ʱ�ȭ�Ѵ�.
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
