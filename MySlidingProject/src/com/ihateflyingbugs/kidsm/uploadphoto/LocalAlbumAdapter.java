package com.ihateflyingbugs.kidsm.uploadphoto;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

public class LocalAlbumAdapter extends BaseAdapter {
	ArrayList<LocalAlbum> albumList;
	LayoutInflater inflater;

	public LocalAlbumAdapter(Context context, ArrayList<LocalAlbum> albumList) {
		inflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.albumList = albumList;
	}

	public int getCount() {
		return albumList.size();
	}

	public LocalAlbum getItem(int position) {
		return albumList.get(position);
	}


	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 최초 호출이면 항목 뷰를 생성한다. 
		// 타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.uploadphoto_album, parent, false);
		}
		
		// 항목 뷰를 초기화한다.
		TextView txt1 = (TextView)convertView.findViewById(R.id.album_name);
		txt1.setText(albumList.get(position).getName()+ "(" + albumList.get(position).numOfPhotos + ")");
		ImageView img = (ImageView)convertView.findViewById(R.id.album_thumbnail);
		img.setImageBitmap(albumList.get(position).bitmap);

		return convertView;
	}
}
