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
	
	// getView�� �����ϴ� ���� ������ �����Ѵ�. �׻� ���� �並 �����ϸ� 1�� �����Ѵ�.
	// �� �޼��忡�� ������ ����� ������ ���� ������ �ٿ�ȴ�.
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (arSrc.get(position).layout == null) {
			int res = 0;
			if(position != getCount()-1)
				res = R.layout.profile_child;
			else
				res = R.layout.profile_addchild;
			arSrc.get(position).layout = mInflater.inflate(res, parent, false);
			
			// �׸� �並 �ʱ�ȭ�Ѵ�.
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
