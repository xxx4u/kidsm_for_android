package com.ihateflyingbugs.kidsm.uploadphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ihateflyingbugs.kidsm.R;
import com.localytics.android.LocalyticsSession;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SimpleCamera extends Activity {
	MediaPlayer mPlayer;
	MyCameraSurface mSurface;
	Button mShutter;
	private LocalyticsSession localyticsSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_camera);
		
		mSurface = (MyCameraSurface)findViewById(R.id.preview);

		// ���� ��Ŀ�� ����
//		findViewById(R.id.focus).setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				mShutter.setEnabled(false);
//				mSurface.mCamera.autoFocus(mAutoFocus);
//			}
//		}); 

		// ���� �Կ�
		mShutter = (Button)findViewById(R.id.simple_camera_shutter);
		mShutter.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				mSurface.mCamera.takePicture(null, null, mPicture);
			}
		}); 
		
		mPlayer = MediaPlayer.create(this, R.raw.shuffle);
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

	// ��Ŀ�� �����ϸ� �Կ� �㰡
	AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mShutter.setEnabled(success);
		}
	};

	// ���� ����.
	PictureCallback mPicture = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
			String path = sd + "/cameratest.jpg";
			
			File file = new File(path);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				Toast.makeText(SimpleCamera.this, "���� ���� �� ���� �߻� : " + 
						e.getMessage(), 0).show();
				return;
			}
			
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.parse("file://" + path);
			intent.setData(uri);
			sendBroadcast(intent);


			AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.shutter);
			mPlayer.reset();
			mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			try {
				mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				mPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mPlayer.start();

			Toast.makeText(SimpleCamera.this, "���� ���� �Ϸ� : " + path, 0).show();
			mSurface.mCamera.startPreview();
			
			intent = new Intent();
			intent.putExtra("uri", uri.toString() );
			setResult(Activity.RESULT_OK, intent);
			
			finish();
		}
	};
	
	public void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}

// �̸����� ǥ�� Ŭ����
class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;

	public MyCameraSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	// ǥ�� ������ ī�޶� �����ϰ� �̸����� ����
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}

    // ǥ�� �ı��� ī�޶� �ı��Ѵ�.
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	// ǥ���� ũ�Ⱑ ������ �� ������ �̸����� ũ�⸦ ���� �����Ѵ�.
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		Camera.Parameters params = mCamera.getParameters();
		List<Size> arSize = params.getSupportedPreviewSizes();
		if (arSize == null) {
			params.setPreviewSize(width, height);
		} else {
			int diff = 10000;
			Size opti = null;
			for (Size s : arSize) {
				if (Math.abs(s.height - height) < diff) {
					diff = Math.abs(s.height - height);
					opti = s;
					
				}
			}
			params.setPreviewSize(opti.width, opti.height);
		}
		mCamera.setParameters(params);
		mCamera.startPreview();
	}
}
