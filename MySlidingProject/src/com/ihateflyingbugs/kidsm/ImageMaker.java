package com.ihateflyingbugs.kidsm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;

public class ImageMaker {
	public static int getId(String resourceName, Class<?> c) {
	    try {
	        Field idField = c.getDeclaredField(resourceName);
	        return idField.getInt(idField);
	    } catch (Exception e) {
	        throw new RuntimeException("No resource ID found for: "
	                + resourceName + " / " + c, e);
	    }
	}
	
	public static Bitmap readBitmap(Context context, Uri selectedImage) {
		Bitmap bm = null; 
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inSampleSize = 5; 
		AssetFileDescriptor fileDescriptor =null; 
		try { 
			fileDescriptor = context.getContentResolver().openAssetFileDescriptor(selectedImage,"r"); 
		} 
		catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} 
		finally{ 
			try { 
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options); 
				fileDescriptor.close(); 
			} 
			catch (IOException e) { 
				e.printStackTrace(); 
			} 
		} 
		return bm; 
	} 
	
	public static Bitmap getCroppedBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    return output;
	}
}
