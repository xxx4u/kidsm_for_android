package com.ihateflyingbugs.kidsm.gallery;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.R;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class GalleryAdapter extends BaseAdapter {
 
	int type;
	ArrayList<Album> albumList;
    Context contxt;
    int sizeOfView;
    public ImageLoader imageLoader; 
    
    public GalleryAdapter(ArrayList<Album> albumList, Context context, int sizeOfView) {
        this.albumList = albumList;
        this.contxt=context;
        this.sizeOfView = sizeOfView;
        this.imageLoader = new ImageLoader(context, R.drawable.photo_album_default);
    }
 
    @Override
    public int getCount() {
 
        return albumList.size();
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
            switch(albumList.get(position).type) {
            case NEW:
            	gridView = inflater.inflate(R.layout.gallery_album_onebutton, null);
            	break;
            case NORMAL:
            	gridView = inflater.inflate(R.layout.gallery_album_withsettings, null);
            	View btn = gridView.findViewById(R.id.gallery_settings);
            	break;
            case SCRAP:
            	gridView = inflater.inflate(R.layout.gallery_album_withoutsettings, null);
            	break;
            }
            gridView.setLayoutParams(new AbsListView.LayoutParams(sizeOfView, sizeOfView));
        }
        
        // set value into textview
        int marginValue = 0;//sizeOfView/32;
        FrameLayout nameFrame = (FrameLayout) gridView.findViewById(R.id.gallery_albumnameframe);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizeOfView-2*marginValue, sizeOfView/4, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        params.setMargins(marginValue, 0, marginValue, marginValue/2);
        nameFrame.setLayoutParams(params);
        
        // TODO LIST : 새 사진첩 만들기랑 즐겨찾기 사진은 ...체커에 걸려서는 안됨.
        TextView textView = (TextView) gridView.findViewById(R.id.gallery_albumname);
        switch(albumList.get(position).type) {
        case NEW:
        	textView.setText(albumList.get(position).album_name);
        	break;
        case SCRAP:
        	textView.setText(albumList.get(position).album_name + " (" + albumList.get(position).photoList.size() + ")");
        	if(albumList.get(position).photoList.size() != 0) {
            	ImageView image = (ImageView) gridView.findViewById(R.id.gallery_albumimage);
            	//imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+albumList.get(position).photoList.get(0).photo_path, image);
//            	String[] tokens = albumList.get(position).photoList.get(0).photo_path.split("/");
//            	String uri = "";
//            	for( int i = 0; i < tokens.length; i++ ) {
//            		uri += tokens[i];
//            		if( i == 0 )
//            			uri += "/";
//            	}
            	imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+albumList.get(position).photoList.get(albumList.get(position).photoList.size()-1).photo_path, image);
            }
        	break;
        case NORMAL:

        	 
            if( albumList.get(position).album_type.equals("D") == false && albumList.get(position).needSetting )
            	gridView.findViewById(R.id.gallery_settings).setVisibility(View.VISIBLE);
            else
            	gridView.findViewById(R.id.gallery_settings).setVisibility(View.INVISIBLE);
            	
        	String albumName = albumList.get(position).album_name + " (" + albumList.get(position).photoList.size() + ")";
        	int maxLength = 20;
            if(albumName.length() <= maxLength)
            	textView.setText(albumName);
            else {
            	String suffix = "... (" + albumList.get(position).photoList.size() + ")";
            	String compressedName = albumName.substring(0, maxLength-1-suffix.length()) + suffix;
//            	String[] tokens = albumName.split(" ");
//            	for( int i = 0; i < tokens.length-1; i++ ) {
//            		if(compressedName.length() + tokens[i].length() + tokens[tokens.length-1].length() <= 18)
//            			compressedName += tokens[i] +" ";
//            		else {
//            			compressedName += "... " + tokens[tokens.length-1];
//            			break;
//            		}
//            	}
            	textView.setText(compressedName);
            }
            gridView.findViewById(R.id.gallery_albumframe).setTag(albumList.get(position).album_srl);
            gridView.findViewById(R.id.gallery_settings).setTag(albumList.get(position).album_srl);
            if(albumList.get(position).photoList.size() != 0) {
            	ImageView image = (ImageView) gridView.findViewById(R.id.gallery_albumimage);
            	imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+albumList.get(position).photoList.get(albumList.get(position).photoList.size()-1).photo_path, image);
//            	String[] tokens = albumList.get(position).photoList.get(albumList.get(position).photoList.size()-1).photo_path.split("/");
//            	String uri = "";
//            	for( int i = 0; i < tokens.length; i++ ) {
//            		uri += tokens[i];
//            		if( i == 0 )
//            			uri += "/";
//            	}
//            	imageLoader.DisplayImage(((Activity)contxt).getString(R.string.image_url)+uri, image);
            }
            break;
        }
        
        return gridView;
    }
 
    
}
