package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihateflyingbugs.kidsm.BaseItem;
import com.ihateflyingbugs.kidsm.ImageMaker;
import com.ihateflyingbugs.kidsm.R;
import com.ihateflyingbugs.kidsm.menu.SlidingMenuMaker;

public class News extends BaseItem {
	public enum NEWSTYPE {
		SCHEDULE,
		PHOTO,
		BUSINFO,
		MENTORY
	}
	NEWSTYPE type;
}
