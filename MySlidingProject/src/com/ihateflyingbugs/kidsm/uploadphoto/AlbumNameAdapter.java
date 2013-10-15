package com.ihateflyingbugs.kidsm.uploadphoto;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.gallery.Album;
import com.ihateflyingbugs.kidsm.menu.Children;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumNameAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<Album> arSrc;
	Context context;
	
	public AlbumNameAdapter(Context context, ArrayList<Album> arItem) {
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
		this.context = context;
	}

	public int getCount() {
		return arSrc.size();
	}

	public Album getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.uploadphoto_spinner_item, parent, false);
		}
		TextView txt = (TextView)convertView.findViewById(R.id.albumlistname);
		txt.setText(arSrc.get(position).album_name);
		convertView.setTag(arSrc.get(position).album_srl);
		
		return convertView;
	}
}
