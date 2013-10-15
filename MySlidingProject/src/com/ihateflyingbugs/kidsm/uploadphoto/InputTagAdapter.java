package com.ihateflyingbugs.kidsm.uploadphoto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;

public class InputTagAdapter extends BaseAdapter {
	ArrayList<InputTag> tagList;
	LayoutInflater inflater;
	Context context;
	public InputTagAdapter(Context context, ArrayList<InputTag> tagList) {
		inflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.tagList = tagList;
		this.context = context;
	}

	public int getCount() {
		return tagList.size();
	}

	public InputTag getItem(int position) {
		return tagList.get(position);
	}


	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		for(int i = 0; i < tagList.size(); i++) {
			View convertView = tagList.get(i).convertView;
			getView(i, convertView, null);
		}
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		// ���� ȣ���̸� �׸� �並 �����Ѵ�. 
		// Ÿ�Ժ��� �並 �ٸ��� �������� �� ������ ���̰� �޶� �������.
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.uploadphoto_tag, parent, false);
			tagList.get(position).convertView = convertView;
		}
		
//		
//		// �׸� �並 �ʱ�ȭ�Ѵ�.
//		TextView name = (TextView)convertView.findViewById(R.id.tag_name);
//		name.setText(tagList.get(position).getName());
//		TextView nickname = (TextView)convertView.findViewById(R.id.tag_nickname);
//		if(tagList.get(position).nickname.isEmpty() == false ) {
//			nickname.setText(tagList.get(position).nickname);
//		}
//		else {
//			nickname.setVisibility(View.INVISIBLE);
//			TextView bar = (TextView)convertView.findViewById(R.id.tag_bar);
//			bar.setVisibility(View.INVISIBLE);
//		}
//		if(tagList.get(position).profileImageUri.isEmpty() == false ) {
//			ImageView img = (ImageView)convertView.findViewById(R.id.tag_profileimage);
//			Bitmap bmp = ImageMaker.getCroppedBitmap(ImageMaker.readBitmap(context, Uri.parse(tagList.get(position).profileImageUri)));
//			img.setImageBitmap(bmp);
//		}
		
		TextView name = (TextView)convertView.findViewById(R.id.tag_name);
		name.setText(tagList.get(position).member_name);
		CheckBox cb = (CheckBox)convertView.findViewById(R.id.tag_check);
		cb.setChecked(tagList.get(position).isTagged);
		cb.setTag(position);
		
		return convertView;
	}
}
