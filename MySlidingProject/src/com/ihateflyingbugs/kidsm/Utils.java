package com.ihateflyingbugs.kidsm;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

	public static String makeTimeLog(String created) {
		if(created == null)
			return "";
		if(created.isEmpty())
			return "";
		
		long currentTime = System.currentTimeMillis()/1000;
		long timeGap = currentTime - Long.parseLong(created);
		
		if( timeGap > 60*60*24 )
			return ""+timeGap/(24*60*60)+"일 전";
		if( timeGap > 60*60 )
			return ""+timeGap/3600+"시간 전";
		if( timeGap > 60 )
			return ""+(timeGap/60)+"분 전";
		return "" + timeGap+"초 전";
	}
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
