package com.ihateflyingbugs.kidsm.gallery;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AlbumAdapter extends BaseAdapter {
	ArrayList<Photo> photoList;
    Context contxt;
    int numOfColumn;
    public ImageLoader imageLoader; 
    
    public AlbumAdapter(ArrayList<Photo> photoList, Context context, int numOfColumn) {
        this.photoList = photoList;
        this.contxt=context;
        this.numOfColumn = numOfColumn;
        this.imageLoader = new ImageLoader(context, R.drawable.photo_in_album_default);
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
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // create a new LayoutInflater
        LayoutInflater inflater = (LayoutInflater) contxt
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View gridView;
        gridView = null;
        convertView = null;// avoids recycling of grid view
        if (convertView == null) {
 
            gridView = new View(contxt);
            // inflating grid view item
            gridView = inflater.inflate(R.layout.gallery_photo, null);
            
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) contxt).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int size = (displaymetrics.widthPixels - 6*(numOfColumn+1))/numOfColumn;
            gridView.setLayoutParams(new GridView.LayoutParams(size, size));
            CheckBox cb = (CheckBox) gridView.findViewById(R.id.gallery_check);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked)
						buttonView.setBackground(contxt.getResources().getDrawable(R.drawable.photo_in_album_select));
					else
						buttonView.setBackgroundColor(Color.TRANSPARENT);
				}
            });
        }
		
        Button btn = (Button) gridView.findViewById(R.id.gallery_photo_scrap);
        //deprecate
        btn.setTag(position);
        
        CheckBox cb = (CheckBox) gridView.findViewById(R.id.gallery_check);
        //deprecate
        cb.setTag(-position-1);
        
        if(photoList.size() != 0) {
        	ImageView image = (ImageView) gridView.findViewById(R.id.gallery_photo);
        	imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+photoList.get(position).photo_path, image);
        	image.setTag(((Activity)contxt).getString(R.string.image_url)+photoList.get(position).photo_path);
//        	String[] tokens = photoList.get(position).photo_path.split("/");
//        	String uri = "";
//        	for( int i = 0; i < tokens.length; i++ ) {
//        		uri += tokens[i];
//        		if( i == 0 )
//        			uri += "/";
//        	}
//        	imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+uri, image);
        }
        return gridView;
    }
 
}