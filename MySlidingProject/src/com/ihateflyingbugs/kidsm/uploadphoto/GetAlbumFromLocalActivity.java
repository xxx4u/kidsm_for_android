package com.ihateflyingbugs.kidsm.uploadphoto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.ExpandableHeightGridView;
import com.ihateflyingbugs.kidsm.R;
import com.localytics.android.LocalyticsSession;

public class GetAlbumFromLocalActivity extends Activity {
	Map<String, LocalAlbum> albumInfo;
	ViewFlipper viewFlipper;
	boolean isAlbumShowing;
	ExpandableHeightGridView gridView;
	PhotoWithCheckAdapter adapter;
	ArrayList<PhotoWithCheck> photoList;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getalbumfromlocal);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.general_actionbar_function_bg));
		getActionBar().setIcon(R.drawable.general_actionbar_back_btnset);
		albumInfo = new HashMap<String, LocalAlbum>();
		viewFlipper = (ViewFlipper)findViewById(R.id.uploadphoto_flipper);
		isAlbumShowing = true;
		
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	getAlbumInfo();
		            }
		        });
		    }
		}).start();
		

		isAlbumShowing = true;
		this.localyticsSession = new LocalyticsSession(this.getApplicationContext());  // Context used to access device resources
		this.localyticsSession.open();                // open the session
		this.localyticsSession.upload();      // upload any data
	}
	
	public void onResume() {
	    super.onResume();
	    this.localyticsSession.open();
	}
	
	public void onPause() {
	    this.localyticsSession.close();
	    this.localyticsSession.upload();
	    super.onPause();
	}
	
	void flipView() {
		if( isAlbumShowing ) {
			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(GetAlbumFromLocalActivity.this, R.anim.viewin_left));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(GetAlbumFromLocalActivity.this, R.anim.viewout_left));
			viewFlipper.showNext();
		}
		else {
			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.viewin_right));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.viewout_right));
			viewFlipper.showPrevious();
		}
		isAlbumShowing = !isAlbumShowing;
		invalidateOptionsMenu();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		if( isAlbumShowing == false && keyCode == KeyEvent.KEYCODE_BACK) {
			setTitle(getString(R.string.getalbumfromlocalactivity));
			flipView();
        	return true;
        }
		else
			return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		if( isAlbumShowing ) {
		}
		else {
			getMenuInflater().inflate(R.menu.uploadphoto_register, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if( isAlbumShowing ) {
				finish();
			}
			else {
				setTitle(getString(R.string.getalbumfromlocalactivity));
				flipView();
			}	
			return true;
		case R.id.uploadphoto_register:
			ArrayList<String> filePaths = new ArrayList<String>();
			for(int i = 0; i < photoList.size(); i++) {
				if( photoList.get(i).isChecked )
					filePaths.add(photoList.get(i).uri.toString());
			}
			if( filePaths.size() != 0 ) {
				Intent data = new Intent();
				data.putExtra("mode", 1);
				data.putExtra("filePath", filePaths);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
			else {
				new AlertDialog.Builder(this)
				.setMessage("사진을 1개 이상 선택하세요")
				.setPositiveButton("확인", null)
				.show();
			}
			return true;
		}
		return false;
	}
	
	void setPhotosInAlbum(String albumId) {
		photoList = new ArrayList<PhotoWithCheck>();
		gridView = (ExpandableHeightGridView) findViewById(R.id.uploadphoto_photolist);
		
		String[] projection = new String[]{
	            MediaStore.Images.Media.BUCKET_ID,
	            MediaStore.Images.Media._ID, 
	            MediaStore.Images.Media.DATA, 
	    };
		
		// Get the base URI for the People table in the Contacts content provider.
	    Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	    // Make the query.\
		Cursor cur = getContentResolver().query(images,
	            projection, // Which columns to return
	            MediaStore.Images.Media.BUCKET_ID + " = " + albumId,         // Which rows to return (all rows)
	            null,       // Selection arguments (none)
	            ""          // Ordering
	            );
		
		if (cur.moveToFirst()) {
	        String data;
	        String bucketid;
	        long id;
	        
	        int bucketIdColumn = cur.getColumnIndex(
	            MediaStore.Images.Media.BUCKET_ID);

	        int idColumn = cur.getColumnIndex(
		            MediaStore.Images.Media._ID);
	        
	        int dataColumn = cur.getColumnIndex(
	            MediaStore.Images.Media.DATA);

	        do {
	            // Get the field values
	        	bucketid = cur.getString(bucketIdColumn);
	            data = cur.getString(dataColumn);
	            id = cur.getLong(idColumn);
	            
	            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
	            PhotoWithCheck photoWithCheck = new PhotoWithCheck(data, uri);
	            photoWithCheck.bitmap = getPreview(data);
	            photoList.add(photoWithCheck);
	            
	        } while (cur.moveToNext());
	    }
        adapter = new PhotoWithCheckAdapter(photoList, this);
        //gridView.setExpanded(true);
        gridView.setAdapter(adapter);
	}
	
	private void getAlbumInfo() {
		String[] projection = new String[]{
	            MediaStore.Images.Media._ID,
	            MediaStore.Images.Media.BUCKET_ID, 
	            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
	            MediaStore.Images.Thumbnails.DATA
	    };

	    // Get the base URI for the People table in the Contacts content provider.
	    Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	    // Make the query.\
		Cursor cur = getContentResolver().query(images,
	            projection, // Which columns to return
	            "",         // Which rows to return (all rows)
	            null,       // Selection arguments (none)
	            ""          // Ordering
	            );

	    Log.i("ListingImages"," query count="+cur.getCount());

	    if (cur.moveToFirst()) {
	        String bucket;
	        String data;
	        String bucketid;
	        
	        int bucketIdColumn = cur.getColumnIndex(
	            MediaStore.Images.Media.BUCKET_ID);
	        
	        int bucketColumn = cur.getColumnIndex(
	            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

	        int dataColumn = cur.getColumnIndex(
	            MediaStore.Images.Thumbnails.DATA);

	        do {
	            // Get the field values
	        	bucketid = cur.getString(bucketIdColumn);
	            bucket = cur.getString(bucketColumn);
	            data = cur.getString(dataColumn);

	            LocalAlbum album = new LocalAlbum(bucketid, bucket, data);
	            if( albumInfo.containsKey(bucketid) ) {
	            	int numOfPhotos = albumInfo.get(bucketid).numOfPhotos;
	            	album.numOfPhotos = numOfPhotos + 1;
	            }
            	albumInfo.put(bucketid, album);
	            // Do something with the values.
	            
	        } while (cur.moveToNext());
	    }
	    
	    
	    final ArrayList<LocalAlbum> albumArray = new ArrayList<LocalAlbum>();
	    new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	Set<String> keySet = albumInfo.keySet();
		        	    Iterator<String> iterator = keySet.iterator();
		            	while(iterator.hasNext()) {
		        	    	try {
		        	    	String key = iterator.next();
		        	    	LocalAlbum album = albumInfo.get(key);
		        	    	album.bitmap = getPreview(album.data);
		        	    	albumArray.add(album);
		        	    	Log.i("ListingImages", " bucket is " + album.getName() + " and number of photos are " + album.numOfPhotos);
		        	    	}
		        	    	catch(Exception e) {
		        	    		e.printStackTrace();
		        	    	}
		        	    }
		            }
		        });
		    }
		}).start();
	    
	    LocalAlbumAdapter adapter = new LocalAlbumAdapter(this, albumArray);
	    ListView listView = (ListView)findViewById(R.id.uploadphoto_albumlist);
	    listView.setAdapter(adapter);
	    listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View v, final int position, long id) {
				
				
				new Thread(new Runnable() {
				    @Override
				    public void run() {    
				        runOnUiThread(new Runnable(){
				            @Override
				             public void run() {
				            	setPhotosInAlbum(albumArray.get(position).bucketid);
								setTitle(((TextView)v.findViewById(R.id.album_name)).getText().toString());
								flipView();
				            }
				        });
				    }
				}).start();
			}
		});
	}
	
	Bitmap getPreview(String uri) {
	    File image = new File(uri);

	    BitmapFactory.Options bounds = new BitmapFactory.Options();
	    bounds.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(image.getPath(), bounds);
	    if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
	        return null;

	    int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
	            : bounds.outWidth;

	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inSampleSize = originalSize / 80;
	    Bitmap srcBmp = BitmapFactory.decodeFile(image.getPath(), opts);
	    Bitmap dstBmp;
	    if (srcBmp.getWidth() >= srcBmp.getHeight()){
    	  dstBmp = Bitmap.createBitmap(
    	     srcBmp, 
    	     srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
    	     0,
    	     srcBmp.getHeight(), 
    	     srcBmp.getHeight()
    	     );

    	}else{

    	  dstBmp = Bitmap.createBitmap(
    	     srcBmp,
    	     0, 
    	     srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
    	     srcBmp.getWidth(),
    	     srcBmp.getWidth() 
    	     );
    	}
	    return dstBmp;
	}
}
