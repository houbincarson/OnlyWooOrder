package com.ukynda.onlywoo;

import com.ukynda.onlywoo.R;
import com.ukynda.onlywoo.data.ItemImageAdatper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.GridView;

public class FragmentPage_home extends Fragment {
	View mview;
	GridView gridView;
	Context mContext;
	@Override
	public void onAttach(Context context) {
		System.out.println("AAAAAAAAAA____onAttach");
		mContext = context;
		super.onAttach(context); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("AAAAAAAAAA____onCreateView"); 
		mview=(View)inflater.inflate(R.layout.fragmentpage_home,container,false);
		System.out.println(mview.toString()); 
		initiView();
		return mview;
	}

	private void initiView() { 
		gridView = (GridView) mview.findViewById(R.id.gridView1); 
		gridView.setAdapter(new ItemImageAdatper(mContext));
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		System.out.println("AAAAAAAAAA____onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		System.out.println("AAAAAAAAAA____onStart");
		super.onStart();
	}
	@Override
	public void onResume() {
		 System.out.println("AAAAAAAAAA____onResume");
		super.onResume();
	}
	@Override
	public void onPause() {
		System.out.println("AAAAAAAAAA____onPause");
		super.onPause();
	}
	@Override
	public void onStop() {
		System.out.println("AAAAAAAAAA____onStop");
		super.onStop();
	}
	@Override
	public void onDestroyView() {
		System.out.println("AAAAAAAAAA____onDestroyView");
		super.onDestroyView();
	}
	@Override
	public void onDestroy() {
		System.out.println("AAAAAAAAAA____onDestroy");
		super.onDestroy();
	}
	@Override
	public void onDetach() {
		System.out.println("AAAAAAAAAA____onDetach");
		super.onDetach();
	}
}
