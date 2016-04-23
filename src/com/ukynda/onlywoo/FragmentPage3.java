package com.ukynda.onlywoo;





import com.ukynda.onlywoo.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPage3 extends Fragment {
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.fragmentpage3, null);		
	}	
}
