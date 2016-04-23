package com.ukynda.onlywoo;

import com.ukynda.onlywoo.R; 
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainFragment extends FragmentActivity {
	TabHost tabHost;
	LayoutInflater inflater;
	int tab_btn[] = new int[] { R.drawable.tab_home_btn,
			R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn,
			R.drawable.tab_more_btn,R.drawable.tab_more_btn };
	String tab_String[] = new String[] { "主题筛选", "款式定制", "钻石定制", "购物车","订单管理" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化组件
		InitiView();

		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				FragmentPage_home fragmentPage_home = (FragmentPage_home) fm.findFragmentByTag("Home");
				FragmentPage2 fragmentPage2 = (FragmentPage2) fm.findFragmentByTag("Page2");
				FragmentPage3 fragmentPage3 = (FragmentPage3) fm.findFragmentByTag("Page3");
				FragmentPage4 fragmentPage4 = (FragmentPage4) fm.findFragmentByTag("Page4");
				FragmentPage5 fragmentPage5 = (FragmentPage5) fm.findFragmentByTag("Page5");
				
				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction(); 
				if (fragmentPage_home != null)
					ft.detach(fragmentPage_home);
				if (fragmentPage2 != null)
					ft.detach(fragmentPage2);
				if (fragmentPage3 != null)
					ft.detach(fragmentPage3);
				if (fragmentPage4 != null)
					ft.detach(fragmentPage4);
				if (fragmentPage5 != null)
					ft.detach(fragmentPage5);
				
				// 当前选项卡
				if (tabId.equalsIgnoreCase("Home")) {
					if (fragmentPage_home == null) {
						ft.add(R.id.realtabcontent, new FragmentPage_home(),"Home");
					} else {
						ft.attach(fragmentPage_home);
					}
				} else if (tabId.equalsIgnoreCase("Page2")) {
					if (fragmentPage2 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage2(),"Page2");
					} else {
						ft.attach(fragmentPage2);
					}
				} else if (tabId.equalsIgnoreCase("Page3")) {
					if (fragmentPage3 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage3(),"Page3");
					} else {
						ft.attach(fragmentPage3);
					}
				} else if (tabId.equalsIgnoreCase("Page4")) {
					if (fragmentPage4 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage4(),"Page4");
					} else {
						ft.attach(fragmentPage4);
					}
				}else if (tabId.equalsIgnoreCase("Page5")) {
					if (fragmentPage5 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage5(),"Page5");
					} else {
						ft.attach(fragmentPage5);
					}
				}
				ft.commit();
			}
		};
		tabHost.setOnTabChangedListener(tabChangeListener);
		// 为Tab按钮设置图标、文字和内容
		TabHost.TabSpec tSpec_fragmentPage_home = tabHost.newTabSpec("Home");  
		tSpec_fragmentPage_home.setIndicator(getTabItemView(0));
		tSpec_fragmentPage_home.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage_home);

		TabHost.TabSpec tSpec_fragmentPage2 = tabHost.newTabSpec("Page2");
		tSpec_fragmentPage2.setIndicator(getTabItemView(1));
		tSpec_fragmentPage2.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage2);

		TabHost.TabSpec tSpec_fragmentPage3 = tabHost.newTabSpec("Page3");
		tSpec_fragmentPage3.setIndicator(getTabItemView(2));
		tSpec_fragmentPage3.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage3);

		TabHost.TabSpec tSpec_fragmentPage4 = tabHost.newTabSpec("Page4");
		tSpec_fragmentPage4.setIndicator(getTabItemView(3));
		tSpec_fragmentPage4.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage4);
		
		TabHost.TabSpec tSpec_fragmentPage5 = tabHost.newTabSpec("Page5");
		tSpec_fragmentPage5.setIndicator(getTabItemView(4));
		tSpec_fragmentPage5.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage5);

	}

	private void InitiView() {
		Log.v("tag", "OnCreate");
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		tabHost.setup();
	}

	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	private View getTabItemView(int i) {
		inflater = LayoutInflater.from(MainFragment.this);
		View view = inflater.inflate(R.layout.tab_items_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageview);
		imageView.setImageResource(tab_btn[i]);
		TextView textView = (TextView) view.findViewById(R.id.tab_textview);
		textView.setText(tab_String[i]);
		textView.setTextColor(R.color.red); 
		return view;
	}
}
