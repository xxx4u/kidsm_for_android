package com.ihateflyingbugs.kidsm.businfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.NetworkFragment;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class BusInfoFragment extends NetworkFragment  {
	LayoutInflater inflater;
	View layout;
	LinearLayout busSelectLayout;
	RelativeLayout busstopLayout;
	ImageView teacherImageView;
	ViewFlipper viewFlipper;
	BusInfoAdapter adapter;
	ListView busListView;
	ArrayList<Bus> busList;
	ArrayList<BusStop> busStoplist;
	int selectedBus = 0;

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			// eventType == 0 버스 리스트 업데이트
			// eventType == 1 노선도 업데이트
			int eventType = (Integer) b.get("eventType");
			if (eventType == 0) {
				//				for(int i=0; i<busList.size(); i++) {
				//					addButton(busList.get(i).getShuttle_name(), i);
				//					Log.d("BusInfo", busList.get(i).getShuttle_name());
				//				}
				BusListAdapter busListAdapter = new BusListAdapter(getActivity(), R.layout.businfo_buslist_row, busList);

				// 리스트뷰에 어댑터 연결
				ListView listView = (ListView)layout.findViewById(R.id.bus_list);
				listView.setAdapter(busListAdapter);				

			}
			else if (eventType == 1) {

				final int shuttleNum = (Integer) b.get("shuttleNum"); // array에 저장된 순서, srl 값 아님 
				Log.d("BusInfo", "event Type 1");

				Button nextButton = (Button)layout.findViewById(R.id.button_next_2);
				//nextButton.setVisibility(View.GONE);
				nextButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Message msg = handler.obtainMessage();
						Bundle b = new Bundle();
						b.putInt("eventType", 2);
						b.putInt("suttle_srl", Integer.valueOf(busList.get(shuttleNum).getSuttle_srl()));
						b.putInt("shuttle_org_srl", Integer.valueOf(busList.get(shuttleNum).getShuttle_org_srl()));
						msg.setData(b);
						handler.sendMessage(msg);				
					}
				});

				String[] values = busList.get(shuttleNum).getShuttle_route().split(",");
				//ArrayList<String> list = new ArrayList<String>();
				busStoplist = new ArrayList<BusStop>();
				for (int i=0; i < values.length; i++){
					boolean isPassed = false;
					boolean currentLocation = false;
					boolean isFinalElem = false;
					if (i <= Integer.valueOf(busList.get(shuttleNum).getShuttle_location())) {
						isPassed = true;
					}
					if (i == Integer.valueOf(busList.get(shuttleNum).getShuttle_location())) {
						currentLocation = true;
					}
					if (i+1 == values.length) {
						isFinalElem = true;
					}
					busStoplist.add(new BusStop(values[i], isPassed, currentLocation, isFinalElem ));
					Log.d("BusInfo", "Shuttle Route : " + values[i]);
				}

				// 어댑터에 데이터 포함
				adapter = new BusInfoAdapter(getActivity(), R.layout.businfo_busstop_row, busStoplist);

				// 리스트뷰에 어댑터 연결
				busListView = (ListView)layout.findViewById(R.id.list);
				busListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				busListView.invalidateViews();

			}
			else if (eventType == 2) {
				Log.d("BusInfo", "event 2 occur");
				int suttle_srl = (Integer) b.get("suttle_srl");
				int suttle_org_srl = (Integer) b.get("shuttle_org_srl");

				Log.d("BusInfo", String.valueOf(suttle_srl));
				Log.d("BusInfo", String.valueOf(suttle_org_srl));
				BusInfoFragment.this.request_Shuttlebus_setNextBusStopSequence(String.valueOf(suttle_srl), String.valueOf(suttle_org_srl));
			}
			else if (eventType == 3) {

				busStoplist = new ArrayList<BusStop>();

				String[] values = busList.get(selectedBus).getShuttle_route().split(",");

				for (int i=0; i < values.length; i++){
					boolean isPassed = false;
					boolean currentLocation = false;
					boolean isFinalElem = false;
					if (i <= Integer.valueOf(busList.get(selectedBus).getShuttle_location())) {
						isPassed = true;
					}
					if (i == Integer.valueOf(busList.get(selectedBus).getShuttle_location())) {
						currentLocation = true;
					}
					if (i+1 == values.length) {
						isFinalElem = true;
					}
					busStoplist.add(new BusStop(values[i], isPassed, currentLocation, isFinalElem ));
					Log.d("BusInfo", "Shuttle Route : " + values[i]);
				}

				adapter.notifyDataSetChanged();
				busListView.invalidateViews();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout != null)
			return layout;
		this.inflater = inflater;
		layout = inflater.inflate(R.layout.activity_businfo, container, false);
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.businfo_page);
		busSelectLayout = (LinearLayout) layout.findViewById(R.id.busselect_linearlayout);
		teacherImageView = (ImageView) layout.findViewById(R.id.iv_busstop_teacher);
		busListView = (ListView)layout.findViewById(R.id.list);

		Bitmap teacherImage = ImageMaker.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.teacher));
		teacherImageView.setImageBitmap(teacherImage);

		busList = new ArrayList<Bus>();

		//Log.d("BusInfo", SlidingMenuMaker.getProfile().member_org_srl);

		BusInfoFragment.this.request_Shuttlebus_getShuttlebuses(SlidingMenuMaker.getProfile().member_org_srl, 1, 100);
		//BusInfoFragment.this.request_Shuttlebus_setShuttlebus("0", "1호차", "세종초등학교,군자 삼거리,군자역,면목시장");
		//BusInfoFragment.this.request_Shuttlebus_getShuttlebuses("1",1,100);

		return layout;
	}

	private void goPrev() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_right));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_right));
		viewFlipper.showPrevious();
	}

	private void goNext() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewin_left));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewout_left));
		viewFlipper.showNext();
	}

	private void addButton(String buttonLabel, final int id)  {
		Button btn = new Button(getActivity());
		btn.setText(buttonLabel);
		btn.setBackgroundResource(R.drawable.bus_select_btnset);
		btn.setTextColor(Color.WHITE);
		//LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) busSelectLayout.getLayoutParams();
		//param.leftMargin=30;
		//param.rightMargin=30;
		//param.topMargin=30;
		//btn.setLayoutParams(param);

		busSelectLayout.addView(btn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((TextView)layout.findViewById(R.id.businfo_busname)).setText(busList.get(id).getShuttle_name());
				Message msg = handler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("eventType", 1);
				b.putInt("shuttleNum", id);
				msg.setData(b);
				handler.sendMessage(msg);
				goNext();   
			}
		});
	}

	public void OnBusClick(View v) {
		goNext();
		int id = v.getId();
		switch(id) {
		//		case R.id.businfo_bus1:
		//			((TextView)layout.findViewById(R.id.businfo_busname)).setText("1호차");
		//			break;
		}
	}

	public void OnChangeBus(View v) {
		goPrev();
	}

	@Override
	public void response(String uri, String response) {
		try {
			if( response.isEmpty() ) {
				Log.d("BusInfo", "return empty");
				return;
			}
			Log.d("BusInfo", response);
			Log.d("BusInfo", uri);

			if(uri.equals("Shuttlebus/setNextBusStopSequence")) {
				//BusInfoFragment.this.request_Shuttlebus_getShuttlebuses(SlidingMenuMaker.getProfile().member_org_srl, 1, 100);
				BusInfoFragment.this.request_Shuttlebus_getShuttlebus(busList.get(selectedBus).getSuttle_srl(), SlidingMenuMaker.getProfile().member_org_srl);

				//((TextView)layout.findViewById(R.id.businfo_busname)).setText(busList.get(selectedBus).getShuttle_name());
				Message msg = handler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("eventType", 3);
				b.putInt("shuttleNum", selectedBus);
				msg.setData(b);
				handler.sendMessage(msg);
			}

			JSONObject jsonObj = new JSONObject(response);
			String result = jsonObj.getString("result");
			if( result.equals("OK") ) {
				Log.d("BusInfo", uri);
				if(uri.equals("Shuttlebus/getShuttlebuses")) {
					String nativeData = jsonObj.getString("data");
					JSONArray dataArray = new JSONArray(nativeData);
					// TEMP!!!!!
					busList.clear();
					for(int i = 0; i < dataArray.length(); i++) {
						String suttle_srl = dataArray.getJSONObject(i).getString("shuttle_srl");
						String shuttle_org_srl = dataArray.getJSONObject(i).getString("shuttle_org_srl");
						String shuttle_name = dataArray.getJSONObject(i).getString("shuttle_name");
						String shuttle_route = dataArray.getJSONObject(i).getString("shuttle_route");
						String shuttle_location = dataArray.getJSONObject(i).getString("sutttle_location");
						busList.add(new Bus(suttle_srl, shuttle_org_srl, shuttle_name, shuttle_route, shuttle_location));
					}
					Log.d("BusInfo", "array size : " + String.valueOf(busList.size()));
					Message msg = handler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("eventType", 0);
					msg.setData(b);
					handler.sendMessage(msg);
				}

				if(uri.equals("Shuttlebus/getShuttlebus")) {
					String nativeData = jsonObj.getString("data");
					//JSONArray dataArray = new JSONArray(nativeData);
					JSONObject json = new JSONObject(nativeData);

					String suttle_srl = json.getString("shuttle_srl");
					String shuttle_org_srl = json.getString("shuttle_org_srl");
					String shuttle_name = json.getString("shuttle_name");
					String shuttle_route = json.getString("shuttle_route");
					String shuttle_location = json.getString("sutttle_location");
					busList.set(selectedBus, new Bus(suttle_srl, shuttle_org_srl, shuttle_name, shuttle_route, shuttle_location));
					
					Log.d("BusInfo", "array size : " + String.valueOf(busList.size()));
					Message msg = handler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("eventType",1);
					b.putInt("shuttleNum", selectedBus);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			}
			else {
				Log.d("BusInfo", "return Fail");
			}
		}
		catch(JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String s = errors.toString();
			System.out.println(s);
		}
	}

	public class BusListAdapter extends BaseAdapter {

		ArrayList<Bus>list;
		Context ctx;
		int itemLayout;

		BusListAdapter(Context ctx, int itemLayout, ArrayList<Bus> list){
			this.ctx = ctx;
			this.itemLayout = itemLayout;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Bus getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			if(convertView==null){
				LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(itemLayout, parent, false);

				Button busButton = (Button)convertView.findViewById(R.id.button_buslist);
				busButton.setText(list.get(pos).getShuttle_name());
				busButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						selectedBus = pos;

						Toast.makeText(ctx, list.get(pos).getShuttle_name(), Toast.LENGTH_SHORT).show();
						((TextView)layout.findViewById(R.id.businfo_busname)).setText(list.get(pos).getShuttle_name());
						Message msg = handler.obtainMessage();
						Bundle b = new Bundle();
						b.putInt("eventType", 1);
						b.putInt("shuttleNum", pos);
						msg.setData(b);
						handler.sendMessage(msg);
						goNext();   
					}
				});
				/*
				int color = 0;
				if(list.get(position).equals("Red")) color = Color.RED;
				else if(list.get(position).equals("Green")) color = Color.GREEN;
				else if(list.get(position).equals("Blue")) color = Color.BLUE;
				 */

				/*
				Button btn = (Button)convertView.findViewById(R.id.btn);
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(ctx, list.get(pos), Toast.LENGTH_SHORT).show();
					}
				});
				 */
			}
			return convertView;
		}
	}
}
