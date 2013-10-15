package com.ihateflyingbugs.kidsm;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "413007677888";
    //���� api ������ �ּ� [https://code.google.com/apis/console/#project:�� ��ȣ]
   //#project: ������ ���ڰ� ���� PROJECT_ID ���� �ش��Ѵ�
   
    //public �⺻ �����ڸ� ������ ������ �Ѵ�.
    public GCMIntentService(){ this(PROJECT_ID); }
   
    public GCMIntentService(String project_id) { super(project_id); }
 
    /** Ǫ�÷� ���� �޽��� */
    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        StringBuffer bufferMessageAll = new StringBuffer();
        Bundle bundle = arg1.getExtras();
        Set<String> setKey = bundle.keySet();
        Iterator<String> iterKey = setKey.iterator();
        while (iterKey.hasNext()){
            String key = iterKey.next();
            String value = bundle.getString(key);
            Log.d("GCMIntentService", "onMessage. key = " + key + ", value = " + value);
            bufferMessageAll.append(key).append(" ").append(value).append("\n");
        }
        String messageAll = bufferMessageAll.toString();
        showMessage(arg0, messageAll, arg1);
    }

    public void showMessage(final Context context, final String message, final Intent intent){
        new Runnable() {
            @Override
            public void run() {
                String title = intent.getStringExtra("title");
                String data = intent.getStringExtra("msg");
                String ticker = intent.getStringExtra("ticker");
                
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);

				// �ش� ������ �����ϴ� �̺�Ʈ�� �ϰ���� �� �Ʒ� �ּ��� Ǯ���ּ���
				//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, 
				// new Intent(context, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
				
				Notification notification = new Notification();
				notification.icon = R.drawable.icon;
				notification.tickerText = ticker;
				notification.when = System.currentTimeMillis();
				notification.vibrate = new long[] { 500, 500, 100, 100 };
				notification.sound = Uri.parse("/system/media/audio/notifications/20_Cloud.ogg");
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(context, title, data, pendingIntent);
				
				notificationManager.notify(0, notification);
				
				Map<String, Object> map= new Hashtable<String, Object>();
                Message msg = new Message();
                msg.what = 0;
                map.put("title", title);
                map.put("data", data);
                map.put("context", context);
                msg.obj = map;
                handler.sendMessage(msg);
            }
        }.run();
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, Object> map = (Hashtable<String, Object>)msg.obj;
            String title = (String)map.get("title");
            String message = (String)map.get("data");
            Context context = (Context)map.get("context");
            
            View layout = ((LayoutInflater)context.getSystemService(
    				Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast, null, false);
            TextView txt = (TextView)layout.findViewById(R.id.toast_title);
            txt.setText(title);
            txt = (TextView)layout.findViewById(R.id.toast_message);
            txt.setText(message);
            Toast toast = new Toast(context);
        	toast.setGravity(Gravity.TOP, 0, 100);//�佺Ʈ ��ġ
        	toast.setDuration(Toast.LENGTH_LONG);//�佺Ʈ �������� �ð�
        	toast.setView(layout);//���� ������ ���̾ƿ��� �����ش�.
        	toast.show();//�佺Ʈ �����ֱ�
        }
    };

    /**���� �߻���*/
    @Override
    protected void onError(Context context, String errorId) {
        Log.d(tag, "on_error. errorId : "+errorId);
    }
 
    /**�ܸ����� GCM ���� ��� ���� �� ��� id�� �޴´�*/
    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d(tag, "onRegistered. regId : "+regId);
    }

    /**�ܸ����� GCM ���� ��� ������ �ϸ� ������ ��� id�� �޴´�*/
    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(tag, "onUnregistered. regId : "+regId);
    }
}