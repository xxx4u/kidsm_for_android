package com.ihateflyingbugs.kidsm.gallery;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihateflyingbugs.kidsm.ExpandableHeightGridView;
import com.ihateflyingbugs.kidsm.ImageLoader;
import com.ihateflyingbugs.kidsm.MainActivity;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.WrappingSlidingDrawer;
import com.ihateflyingbugs.kidsm.gallery.Album.ALBUMTYPE;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;
import com.ihateflyingbugs.kidsm.schedule.ScheduleFragment;
import com.ihateflyingbugs.kidsm.uploadphoto.GetAlbumFromLocalActivity;
import com.ihateflyingbugs.kidsm.uploadphoto.SimpleCamera;
import com.ihateflyingbugs.kidsm.uploadphoto.UploadPhotoActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GalleryFragment extends NetworkFragment{
	ExpandableHeightGridView gridView;
	GalleryAdapter adapter;
	//Album defaultAlbum;
	Album allPhotoAlbum;
	Album taggedPhotoAlbum;
	ArrayList<Album> albumList;
	float prevTouchX;
	float prevTouchY;
	WrappingSlidingDrawer drawer;
	boolean isDrawerShouldOpen;
	String filePath;
	LayoutInflater inflater;
	View layout;
	int sizeOfView;
	
	int getPhotoCounter;
	int numOfScrappedPhoto;
	int getPhotosCounter;
	boolean isGetMemberPhotosResponded;
	boolean isGetMemberTaggedPhotosResponded;
	int requestModeFromNewsfeed;
	
    public ImageLoader imageLoader; 
	
    public GalleryFragment() {
    	requestModeFromNewsfeed = 0;
    }
    
    public void setRequestFromNewsfeed(int requestModeFromNewsfeed) {
    	this.requestModeFromNewsfeed = requestModeFromNewsfeed;
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null) {
			requestAlbumData();
			return layout;
		}
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_gallery, container, false);
		
		auth_key = MainActivity.auth_key;
		
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/attachimage.jpg";
		albumList = new ArrayList<Album>();
//		albumList.add(new Album(0, getResources().getString(R.string.make_new_album)));
//		albumList.add(new Album(1, "2013 제주도 여행 2015 제부도 만행"));
//		albumList.add(new Album(1, "with friends"));
//		albumList.add(new Album(2, getResources().getString(R.string.scrap_album)));
        gridView = (ExpandableHeightGridView) layout.findViewById(R.id.gallery_albumlist);
        
        int numOfColumn = 3;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		sizeOfView = (displaymetrics.widthPixels - 10*(numOfColumn+1))/numOfColumn;
        
		FrameLayout frame = (FrameLayout) layout.findViewById(R.id.gallery_seeallphoto_frame);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displaymetrics.widthPixels-20, sizeOfView);
		params.setMargins(10, 10, 10, 0);
		frame.setLayoutParams(params);
		frame = (FrameLayout) layout.findViewById(R.id.gallery_seetaggedphoto_frame);
		params = new LinearLayout.LayoutParams(displaymetrics.widthPixels-20, sizeOfView);
		params.setMargins(10, 10, 10, 0);
		frame.setLayoutParams(params);
        adapter = new GalleryAdapter(albumList, getActivity(), sizeOfView);
        gridView.setExpanded(true);
        gridView.setAdapter(adapter);
        drawer = (WrappingSlidingDrawer)layout.findViewById(R.id.gallery_drawer);
        drawer.animateOpen();
        
        imageLoader = new ImageLoader(getActivity(), R.drawable.photo_in_album_default);
        
        requestAlbumData();
        
		return layout;
	}
	
	public void OnUploadPhoto(View v) {
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gallery_uploadmode, android.R.layout.simple_list_item_1);
		ListView listView = new ListView(getActivity());
		listView.setAdapter(arrayAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final AlertDialog alert = new AlertDialog.Builder(getActivity())
		.setTitle(getString(R.string.upload_photo))
		.setView(listView).create();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent;
				switch(position) {
				case 0:
					intent = new Intent(getActivity(), SimpleCamera.class);
					//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
					startActivityForResult(intent, 0);
					break;
				case 1:
					intent = new Intent(getActivity(), GetAlbumFromLocalActivity.class);
					startActivityForResult(intent, 1);
					break;
				}
				alert.dismiss();
			}
		});
		alert.show();
	}
	
	private ArrayList<Album> makeAlbumListForSpinner() {
		ArrayList<Album> array = new ArrayList<Album>();
		for(int i = 1; i < albumList.size()-1; i++)
			array.add(albumList.get(i));
		return array;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == Activity.RESULT_OK ) {
			Intent intent;
			switch(requestCode) {
			case 0:
				intent = new Intent(getActivity(), UploadPhotoActivity.class);
				intent.putExtra("mode", 0);
				intent.putExtra("filePath", data.getStringExtra("uri"));
				intent.putExtra("albumList", makeAlbumListForSpinner());
				startActivityForResult(intent, 2);
				break;
			case 1:
				intent = new Intent(getActivity(), UploadPhotoActivity.class);
				intent.putExtra("mode", data.getIntExtra("mode", -1));
				intent.putExtra("filePath", data.getStringArrayListExtra("filePath"));
				intent.putExtra("albumList", makeAlbumListForSpinner());
				startActivityForResult(intent, 2);
				break;
			case 2:
				String album_srl = data.getStringExtra("album_srl").split(",")[0];
				ArrayList<Album> newAlbumList = data.getParcelableArrayListExtra("albumList");
				ArrayList<Photo> uploadedPhotoList = data.getParcelableArrayListExtra("uploadedPhotoList");
				for( int i = 0; i < newAlbumList.size(); i++ ) {
					albumList.add(albumList.size()-1, newAlbumList.get(i));
				}
				adapter.notifyDataSetChanged();
				for( int i = 1; i < albumList.size()-1; i++ ) {
					if( album_srl.equals(albumList.get(i).album_srl) ) {
						albumList.get(i).photoList.addAll(uploadedPhotoList);
						allPhotoAlbum.photoList.addAll(uploadedPhotoList);
						adapter.notifyDataSetChanged();
						View v = new View(getActivity());
						v.setTag(album_srl);
						//OnSeeAlbum(v);
						break;
					}
				}
				refreshDataSet();
				MainActivity.changeFragment(0);
				break;
			case 3:
				Album modifiedData = data.getParcelableExtra("modifiedData");
				ArrayList<Album> newAlbums = data.getParcelableArrayListExtra("newAlbumList");
				for( int i = 0; i < newAlbums.size(); i++ ) {
					albumList.add(albumList.size()-1, newAlbums.get(i));
				}
				adapter.notifyDataSetChanged();
				if(modifiedData.type == ALBUMTYPE.MODIFIED) {
					String modified_album_srl = data.getStringExtra("modified_album_srl");
					for( int i = 1; i < albumList.size()-1; i++ ) {
						if( modified_album_srl.equals(albumList.get(i).album_srl) ) {
							for(int j = 0; j < modifiedData.photoList.size(); j++) {
								for(int k = 0; k < albumList.get(i).photoList.size(); k++) {
									if(modifiedData.photoList.get(j).photo_srl.equals(albumList.get(i).photoList.get(k).photo_srl)) {
										albumList.get(i).photoList.remove(k);
										break;
									}
								}
								for( int k = 0; k < allPhotoAlbum.photoList.size(); k++ ) {
									if(modifiedData.photoList.get(j).photo_srl.equals(allPhotoAlbum.photoList.get(k).photo_srl)) {
										allPhotoAlbum.photoList.remove(k);
										break;
									}
								}
							}
							break;
						}
					}
					
					for( int i = 0; i < albumList.size()-1; i++ ) {
						if(albumList.get(i).album_srl.equals(modifiedData.album_srl)) {
							albumList.get(i).photoList.addAll(modifiedData.photoList);
							break;
						}
					}
				}
				else {
					for( int i = 1; i < albumList.size()-1; i++ ) {
						if( modifiedData.album_srl.equals(albumList.get(i).album_srl) ) {
							for(int j = 0; j < modifiedData.photoList.size(); j++) {
								for(int k = 0; k < albumList.get(i).photoList.size(); k++) {
									if(modifiedData.photoList.get(j).photo_srl.equals(albumList.get(i).photoList.get(k).photo_srl)) {
										albumList.get(i).photoList.remove(k);
										break;
									}
								}
								for( int k = 0; k < allPhotoAlbum.photoList.size(); k++ ) {
									if(modifiedData.photoList.get(j).photo_srl.equals(allPhotoAlbum.photoList.get(k).photo_srl)) {
										allPhotoAlbum.photoList.remove(k);
										break;
									}
								}
							}
							break;
						}
					}
				}
				refreshDataSet();
				break;
			}
		}
	}
	
	public void OnNewAlbum(View v) {
		final EditText txt = new EditText(getActivity());
		
		new AlertDialog.Builder(getActivity())
		.setView(txt)
		.setMessage("새 사진첩 이름을 입력하세요")
		.setNegativeButton("취소", null)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				albumList.add(new Album(1, txt.getText().toString()));
				GalleryFragment.this.request_Album_setAlbum(txt.getText().toString(), SlidingMenuMaker.getProfile().member_srl, "N");
			}
		})
		.show();
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
//		if( event.getAction() == MotionEvent.ACTION_DOWN ) {
//			prevTouchY = event.getY();
//		}
//		else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//			int height = displaymetrics.heightPixels;
//			float gap = Math.abs(event.getY() - prevTouchY);
//			if( gap > height/10 ) {
//				if( event.getY() - prevTouchY < 0 ) {
//					if( drawer.isOpened() == false ) {
//						drawer.animateOpen();
//					}
//				}
//				else {
//					if( drawer.isOpened() == true ) {
//						drawer.animateClose();
//					}
//				}
//				prevTouchY = event.getY();
//			}
//		}
//		else if( event.getAction() == MotionEvent.ACTION_UP ) {
//		}
	    return false;
	}
	
	public void OnSeeAllPhoto(View v) {
		Intent intent = new Intent(getActivity(), AlbumActivity.class);
		intent.putExtra("album", allPhotoAlbum);
		intent.putExtra("albumList", makeAlbumListForSpinner());
		startActivity(intent);
	}
	
	public void OnSeeAlbum(View v) {
		String album_srl = v.getTag().toString();
		for( int i = 1; i < albumList.size()-1; i++ ) {
			if( album_srl.equals(albumList.get(i).album_srl) ) {
				Intent intent = new Intent(getActivity(), AlbumActivity.class);
				intent.putExtra("album", albumList.get(i));
				ArrayList<Album> albumListForMove = new ArrayList<Album>();
				albumListForMove.add(albumList.get(0));
				albumListForMove.addAll(makeAlbumListForSpinner());
				intent.putExtra("albumList", albumListForMove);
				startActivityForResult(intent, 3);
				break;
			}
		}
	}
	
	public void OnSeeScrapAlbum(View v) {
		Intent intent = new Intent(getActivity(), AlbumActivity.class);
		intent.putExtra("album", albumList.get(albumList.size()-1));
		intent.putExtra("albumList", makeAlbumListForSpinner());
		startActivity(intent);
	}
	
	public void OnSeeTaggedPhoto(View v) {
		Intent intent = new Intent(getActivity(), AlbumActivity.class);
		intent.putExtra("album", taggedPhotoAlbum);
		intent.putExtra("albumList", makeAlbumListForSpinner());
		startActivity(intent);
	}
	
	public void OnSettingClick(View v) {
		final String album_srl = v.getTag().toString();
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gallery_albumsettingmode, android.R.layout.simple_list_item_1);
		ListView listView = new ListView(getActivity());
		listView.setAdapter(arrayAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final AlertDialog alert = new AlertDialog.Builder(getActivity())
		.setTitle(getString(R.string.gallery_albumsetting))
		.setView(listView).create();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent;
				switch(position) {
				case 0:
					Album tempAlbum = null;
					for( int i = 1; i < albumList.size()-1; i++ ) {
						if( album_srl.equals(albumList.get(i).album_srl) ) {
							tempAlbum = albumList.get(i);
							break;
						}
					}
					final EditText txt = new EditText(getActivity());
					final Album album = tempAlbum;
					txt.setText(album.album_name);
					new AlertDialog.Builder(getActivity())
					.setView(txt)
					.setMessage("수정할 사진첩 이름을 입력하세요")
					.setNegativeButton("취소", null)
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							GalleryFragment.this.request_Album_modAlbum(album_srl, txt.getText().toString(), SlidingMenuMaker.getProfile().member_srl);
						}
					})
					.show();
					break;
				case 1:
					new AlertDialog.Builder(getActivity())
					.setMessage("앨범을 삭제하면 안에 있는 사진도 같이 삭제됩니다.\n또한 한번 삭제된 앨범은 복구가 불가능합니다.\n정말로 삭제하시겠습니까?")
					.setNegativeButton("취소", null)
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							albumList.add(new Album(""+albumList.size(), 1, txt.getText().toString(), true));
//					        adapter = new GalleryAdapter(albumList, getActivity(), sizeOfView);
//					        gridView.setExpanded(true);
//					        gridView.setAdapter(adapter);
							GalleryFragment.this.request_Album_delAlbum(album_srl, SlidingMenuMaker.getProfile().member_srl);
						}
					})
					.show();
					break;
				}
				alert.dismiss();
			}
		});
		alert.show();
	}
	
	private void requestAlbumData() {
		albumList.clear();
		
		getPhotoCounter = 0;
		numOfScrappedPhoto = 0;
		getPhotosCounter = 0;
		isGetMemberPhotosResponded = false;
		isGetMemberTaggedPhotosResponded = false;
		
		GalleryFragment.this.request_Album_getAlbums(SlidingMenuMaker.getProfile().member_srl);
	}
	
	private boolean isAllResponsesArrived() {
		if( getPhotosCounter == albumList.size()-2 &&
			getPhotoCounter == numOfScrappedPhoto &&
			isGetMemberPhotosResponded && isGetMemberTaggedPhotosResponded ) {
			

			Intent intent;
			switch(requestModeFromNewsfeed) {
			case 1:
				intent = new Intent(getActivity(), SimpleCamera.class);
				startActivityForResult(intent, 0);
				requestModeFromNewsfeed = 0;
				break;
			case 2:
				intent = new Intent(getActivity(), GetAlbumFromLocalActivity.class);
				startActivityForResult(intent, 1);
				requestModeFromNewsfeed = 0;
				break;
			}
			
			return true;
		}
		return false;
	}
	
	private void refreshDataSet() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        GalleryFragment.this.getActivity().runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	adapter.notifyDataSetChanged();
		            	((TextView)layout.findViewById(R.id.gallery_seeallphoto_name)).setText(getString(R.string.gallery_seeallphoto) + " (" + allPhotoAlbum.photoList.size() + ")");
		            	ImageView image;
		            	if( allPhotoAlbum.photoList.size() != 0) {
			            	image = (ImageView) layout.findViewById(R.id.gallery_seeallphoto_image);
			            	imageLoader.DisplayImage(getString(R.string.image_url)+allPhotoAlbum.photoList.get(allPhotoAlbum.photoList.size()-1).photo_path, image);
		            	}
		            	if( taggedPhotoAlbum.photoList.size() != 0 ) {
		            		image = (ImageView) layout.findViewById(R.id.gallery_seetaggedphoto_image);
		            		imageLoader.DisplayImage(getString(R.string.image_url)+taggedPhotoAlbum.photoList.get(taggedPhotoAlbum.photoList.size()-1).photo_path, image);
		            	}
		            }
		        });
		    }
		}).start();
	}
	
	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() )
				return;
			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				if(uri.equals("Album/getAlbums")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					albumList.add(new Album(ALBUMTYPE.NEW, "", "", getResources().getString(R.string.make_new_album), "", "", "", ""));
					for(int i = 0; i < dataArray.length(); i++) {
						String album_srl = dataArray.getJSONObject(i).getString("album_srl");
						String album_member_srl = dataArray.getJSONObject(i).getString("album_member_srl");
						String album_name = dataArray.getJSONObject(i).getString("album_name");
						String album_type = dataArray.getJSONObject(i).getString("album_type");
						String album_created = dataArray.getJSONObject(i).getString("album_created");
						String album_updated = dataArray.getJSONObject(i).getString("album_updated");
						String album_count = dataArray.getJSONObject(i).getString("album_count");
						albumList.add(new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
						GalleryFragment.this.request_Album_getPhotos(album_srl, SlidingMenuMaker.getProfile().member_srl, 1, 10000);
					}
					albumList.add(new Album(ALBUMTYPE.SCRAP, "", "", getResources().getString(R.string.scrap_album), "", "", "", ""));
					allPhotoAlbum = new Album(ALBUMTYPE.ALL, "", "", getResources().getString(R.string.gallery_seeallphoto), "", "", "", "");
					taggedPhotoAlbum = new Album(ALBUMTYPE.TAGGED, "", "", getResources().getString(R.string.tagged_album), "", "", "", "");
			
					switch(SlidingMenuMaker.getProfile().member_type.charAt(0)) {
					case 'P':
						GalleryFragment.this.request_Album_getMemberPhotos(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 10000);
						GalleryFragment.this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 10000, "P");
						GalleryFragment.this.request_Album_getMemberTaggedPhotos(SlidingMenuMaker.getProfile().getCurrentChildren().student_member_srl, 1, 10000);
						break;
					case 'T':
					case 'M':
						GalleryFragment.this.request_Album_getMemberPhotos(SlidingMenuMaker.getProfile().member_srl, 1, 10000);
						GalleryFragment.this.request_Scrap_getScraps(SlidingMenuMaker.getProfile().member_srl, 1, 10000, "P");
						GalleryFragment.this.request_Album_getMemberTaggedPhotos(SlidingMenuMaker.getProfile().member_srl, 1, 10000);
						break;
					}
					refreshDataSet();
				}
				else if(uri.equals("Album/setAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String album_srl = jsonObj.getString("album_srl");
					String album_member_srl = jsonObj.getString("album_member_srl");
					String album_name = jsonObj.getString("album_name");
					String album_type = jsonObj.getString("album_type");
					String album_created = jsonObj.getString("album_created");
					String album_updated = jsonObj.getString("album_updated");
					String album_count = jsonObj.getString("album_count");
					albumList.add(albumList.size()-1, new Album(ALBUMTYPE.NORMAL, album_srl, album_member_srl, album_name, album_type, album_created, album_updated, album_count));
					refreshDataSet();
				}
				else if(uri.equals("Album/modAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String album_srl = jsonObj.getString("album_srl");
					String album_member_srl = jsonObj.getString("album_member_srl");
					String album_name = jsonObj.getString("album_name");
					String album_type = jsonObj.getString("album_type");
					String album_created = jsonObj.getString("album_created");
					String album_updated = jsonObj.getString("album_updated");
					String album_count = jsonObj.getString("album_count");
					for( int i = 1; i < albumList.size()-1; i++ ) {
						if( album_srl.equals(albumList.get(i).album_srl) ) {
							albumList.get(i).album_name = album_name;
							refreshDataSet();
							break;
						}
					}
				}
				else if(uri.equals("Album/delAlbum")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String album_srl = jsonObj.getString("album_srl");
					String album_member_srl = jsonObj.getString("album_member_srl");
					String album_name = jsonObj.getString("album_name");
					String album_type = jsonObj.getString("album_type");
					String album_created = jsonObj.getString("album_created");
					String album_updated = jsonObj.getString("album_updated");
					String album_count = jsonObj.getString("album_count");
					for( int i = 1; i < albumList.size()-1; i++ ) {
						if( album_srl.equals(albumList.get(i).album_srl) ) {
							albumList.remove(i);
							refreshDataSet();
							break;
						}
					}
				}
				else if(uri.equals("Album/getMemberPhotos")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					allPhotoAlbum.photoList.clear();
					for(int i = 0; i < dataArray.length(); i++) {
						String photo_srl = dataArray.getJSONObject(i).getString("photo_srl");
						String photo_member_srl = dataArray.getJSONObject(i).getString("photo_member_srl");
						String photo_album_srl = dataArray.getJSONObject(i).getString("photo_album_srl");
						String photo_timeline_srl = dataArray.getJSONObject(i).getString("photo_timeline_srl");
						String photo_tag = dataArray.getJSONObject(i).getString("photo_tag");
						String photo_path = dataArray.getJSONObject(i).getString("photo_path");
						String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
						String photo_like = dataArray.getJSONObject(i).getString("photo_like");
						String photo_private = dataArray.getJSONObject(i).getString("photo_private");
						String photo_created = dataArray.getJSONObject(i).getString("photo_created");
						String photo_updated = dataArray.getJSONObject(i).getString("photo_updated");
						if( photo_album_srl.isEmpty() == false)
							allPhotoAlbum.photoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));						
					}
					isGetMemberPhotosResponded = true;
					if( isAllResponsesArrived() )
						refreshDataSet();
				}
				else if(uri.equals("Album/getMemberTaggedPhotos")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					taggedPhotoAlbum.photoList.clear();
					for(int i = 0; i < dataArray.length(); i++) {
						String photo_srl = dataArray.getJSONObject(i).getString("photo_srl");
						String photo_member_srl = dataArray.getJSONObject(i).getString("photo_member_srl");
						String photo_album_srl = dataArray.getJSONObject(i).getString("photo_album_srl");
						String photo_timeline_srl = dataArray.getJSONObject(i).getString("photo_timeline_srl");
						String photo_tag = dataArray.getJSONObject(i).getString("photo_tag");
						String photo_path = dataArray.getJSONObject(i).getString("photo_path");
						String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
						String photo_like = dataArray.getJSONObject(i).getString("photo_like");
						String photo_private = dataArray.getJSONObject(i).getString("photo_private");
						String photo_created = dataArray.getJSONObject(i).getString("photo_created");
						String photo_updated = dataArray.getJSONObject(i).getString("photo_updated");
						taggedPhotoAlbum.photoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
					}
					isGetMemberTaggedPhotosResponded = true;
					if( isAllResponsesArrived() )
						refreshDataSet();
				}
				else if(uri.equals("Album/getPhotos")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					String album_srl = "";
					ArrayList<Photo> photoList = new ArrayList<Photo>();
					for(int i = 0; i < dataArray.length(); i++) {
						String photo_srl = dataArray.getJSONObject(i).getString("photo_srl");
						String photo_member_srl = dataArray.getJSONObject(i).getString("photo_member_srl");
						String photo_album_srl = dataArray.getJSONObject(i).getString("photo_album_srl");
						String photo_timeline_srl = dataArray.getJSONObject(i).getString("photo_timeline_srl");
						String photo_tag = dataArray.getJSONObject(i).getString("photo_tag");
						String photo_path = dataArray.getJSONObject(i).getString("photo_path");
						String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
						String photo_like = dataArray.getJSONObject(i).getString("photo_like");
						String photo_private = dataArray.getJSONObject(i).getString("photo_private");
						String photo_created = dataArray.getJSONObject(i).getString("photo_created");
						String photo_updated = dataArray.getJSONObject(i).getString("photo_updated");
						if(photo_album_srl.contains(","))
							album_srl = photo_album_srl.split(",")[0];
						else 
							album_srl = photo_album_srl;
						photoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
					}
					if(photoList.size() != 0 ) {
						int index = -1;
						for(int i = 1; i < albumList.size()-1; i++) {
							if(album_srl.equals(albumList.get(i).album_srl)) {
								albumList.get(i).photoList.clear();
								albumList.get(i).photoList.addAll(photoList);
								break;
							}
						}
					}
					getPhotosCounter++;
					if( isAllResponsesArrived() )
						refreshDataSet();
				}
				else if(uri.equals("Scrap/getScraps")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					for(int i = 0; i < dataArray.length(); i++) {
						String scrap_srl = dataArray.getJSONObject(i).getString("scrap_srl");
						String scrap_member_srl = dataArray.getJSONObject(i).getString("scrap_member_srl");
						String scrap_type = dataArray.getJSONObject(i).getString("scrap_type");
						String scrap_target_srl = dataArray.getJSONObject(i).getString("scrap_target_srl");
						String scrap_created = dataArray.getJSONObject(i).getString("scrap_created");
						GalleryFragment.this.request_Album_getPhoto(scrap_target_srl);
					}
					numOfScrappedPhoto = dataArray.length();
					if( numOfScrappedPhoto == 0 && isAllResponsesArrived() )
						refreshDataSet();
				}
				else if(uri.equals("Album/getPhoto")) {
					String nativeData = jsonObj.getString("data");
					jsonObj = new JSONObject(nativeData);
					String photo_srl = jsonObj.getString("photo_srl");
					String photo_member_srl = jsonObj.getString("photo_member_srl");
					String photo_album_srl = jsonObj.getString("photo_album_srl");
					String photo_timeline_srl = jsonObj.getString("photo_timeline_srl");
					String photo_tag = jsonObj.getString("photo_tag");
					String photo_path = jsonObj.getString("photo_path");
					String photo_thumbnail = photo_path.substring(0, photo_path.length()-4 ) + "_96x96.png";
					String photo_like = jsonObj.getString("photo_like");
					String photo_private = jsonObj.getString("photo_private");
					String photo_created = jsonObj.getString("photo_created");
					String photo_updated = jsonObj.getString("photo_updated");
					albumList.get(albumList.size()-1).photoList.add(new Photo(photo_srl, photo_member_srl, photo_album_srl, photo_timeline_srl, photo_tag, photo_path, photo_thumbnail, photo_like, photo_private, photo_created, photo_updated));
					getPhotoCounter++;
					if( isAllResponsesArrived() )
						refreshDataSet();
				}
			}
			else {
				
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		}
	}
}
