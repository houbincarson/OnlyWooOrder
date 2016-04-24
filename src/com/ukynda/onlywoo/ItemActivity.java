package com.ukynda.onlywoo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		Bundle bundle = getIntent().getExtras();
		String dataString = bundle.getString("data");
		TextView tView = (TextView) this.findViewById(R.id.textview1);
		tView.setText(dataString);
	}
}
