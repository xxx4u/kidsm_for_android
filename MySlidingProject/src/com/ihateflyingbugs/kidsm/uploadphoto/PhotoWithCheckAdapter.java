package com.ihateflyingbugs.kidsm.uploadphoto;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoWithCheckAdapter extends BaseAdapter {
	ArrayList<PhotoWithCheck> photoList;
    Context contxt;
    
    public PhotoWithCheckAdapter(ArrayList<PhotoWithCheck> photoList, Context context) {
        this.photoList = photoList;
        this.contxt=context;
    }
 
    @Override
    public int getCount() {
 
        return photoList.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        // create a new LayoutInflater
        LayoutInflater inflater = (LayoutInflater) contxt
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View gridView;
        gridView = null;
        convertView = null;// avoids recycling of grid view
        if (convertView == null) {
 
            gridView = new View(contxt);
            // inflating grid view item
            gridView = inflater.inflate(R.layout.uploadphoto_photo_thumbnail, null);
            
            ImageView img = (ImageView)gridView.findViewById(R.id.uploadphoto_photo);
            img.setImageBitmap(photoList.get(position).bitmap);
            CheckBox cb = (CheckBox)gridView.findViewById(R.id.uploadphoto_check);
            cb.setTag(photoList.get(position).uri);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					photoList.get(position).isChecked = isChecked;
				}
            	
            });
        }
 
        return gridView;
    }
 
}
