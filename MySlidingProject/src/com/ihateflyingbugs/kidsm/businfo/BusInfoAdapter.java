package com.ihateflyingbugs.kidsm.businfo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.R;

//public class BusInfoAdapter extends ArrayAdapter<BusStop> {
public class BusInfoAdapter extends BaseAdapter {

//	public BusInfoAdapter(Context context, int resource, BusStop[] objects) {
//		super(context, resource, objects);
//		// TODO Auto-generated constructor stub
//	}

	ArrayList<BusStop> list;
	Context ctx;
	int itemLayout;

	//public BusInfoAdapter(Context ctx, int itemLayout, BusStop[] list){
	public BusInfoAdapter(Context ctx, int itemLayout, ArrayList<BusStop> list){
		//super(ctx, itemLayout, list);
		this.ctx = ctx;
		this.itemLayout = itemLayout;
		this.list = list;
		//this.list = new ArrayList<BusStop>();
//		for (int i=0; i<list.length; i++) {
//			this.list.add(list[i]);
//		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BusStop getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		
		BusstopViewHolder holder;
		
		//if(convertView==null){
		if(true){
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(itemLayout, parent, false);
			
			holder = new BusstopViewHolder();

			holder.cellLayout = (LinearLayout)convertView.findViewById(R.id.ll_cell);
			holder.colorBar = (View)convertView.findViewById(R.id.view_busstop);
			holder.busstopIcon = (ImageView)convertView.findViewById(R.id.iv_busstop);
			holder.textView = (TextView)convertView.findViewById(R.id.tv_busstop_label);
			holder.nextButton = (Button)convertView.findViewById(R.id.button_next);

			holder.blueTopLine = (View)convertView.findViewById(R.id.view_line_blue_top);
			holder.blueBottomLine = (View)convertView.findViewById(R.id.view_line_blue_bottom);
			holder.greyTopLine = (View)convertView.findViewById(R.id.view_line_grey_top);
			holder.greyBottomLine = (View)convertView.findViewById(R.id.view_line_grey_bottom);

			holder.textView.setText(list.get(pos).getName());
			holder.nextButton.setVisibility(View.GONE);

			if (list.get(pos).isPassed() == true){
				holder.busstopIcon.setImageResource(R.drawable.bus_info_on);

			}
			else {
				holder.busstopIcon.setImageResource(R.drawable.bus_info_off);
			}

			if (list.get(pos).isCurrentLocation() == true) {
				holder.cellLayout.setBackgroundColor(Color.WHITE);
				holder.colorBar.setBackgroundColor(0xff48cce3);
				holder.nextButton.setVisibility(View.VISIBLE);
			}

			holder.blueTopLine.setVisibility(View.GONE);
			holder.blueBottomLine.setVisibility(View.GONE); 
			holder.greyTopLine.setVisibility(View.GONE);
			holder.greyBottomLine.setVisibility(View.GONE);	

			if (list.get(pos).isFinalElem() == true) {
				if(list.get(pos).isPassed() == true) {
					holder.blueTopLine.setVisibility(View.VISIBLE);
				}
				else {
					holder.greyTopLine.setVisibility(View.VISIBLE);
				}				
			}
			else if (pos == 0) {
				if(list.get(pos).isCurrentLocation() == true && list.get(pos).isPassed() == true) {
					holder.greyBottomLine.setVisibility(View.VISIBLE);
				}
				else if(list.get(pos).isCurrentLocation() == false && list.get(pos).isPassed() == true){
					holder.blueBottomLine.setVisibility(View.VISIBLE);
				}
				else {
					holder.greyBottomLine.setVisibility(View.VISIBLE);
				}
			}
			else {
				if(list.get(pos).isPassed() == true) {
					holder.blueTopLine.setVisibility(View.VISIBLE);
					holder.blueBottomLine.setVisibility(View.VISIBLE);
					if(list.get(pos).isCurrentLocation() == true){
						holder.blueBottomLine.setVisibility(View.GONE);
						holder.greyBottomLine.setVisibility(View.VISIBLE);
					}
				}
				else {
					holder.greyTopLine.setVisibility(View.VISIBLE);
					holder.greyBottomLine.setVisibility(View.VISIBLE);
				}
			}


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
//		else{
//			
//			//holder = (BusstopViewHolder)convertView.getTag();
//		}
		return convertView;
	}
	
	class BusstopViewHolder {
		public LinearLayout cellLayout;
		public View colorBar;
		public ImageView busstopIcon;
		public TextView textView;
		public Button nextButton;

		public View blueTopLine;
		public View blueBottomLine;
		public View greyTopLine;
		public View greyBottomLine;
	}
}
