package com.ukynda.onlywoo;

import android.os.Bundle; 
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentPage5 extends ListFragment {
	
	 String android_versions[] = new String[]{
	            "Jelly Bean",
	            "IceCream Sandwich",
	            "HoneyComb",
	            "Ginger Bread",
	            "Froyo",
	            "Jelly Bean",
	            "IceCream Sandwich",
	            "HoneyComb",
	            "Ginger Bread",
	            "Froyo",
	            "Jelly Bean",
	            "IceCream Sandwich",
	            "HoneyComb",
	            "Ginger Bread",
	            "Froyo"
	    };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, android_versions);
		setListAdapter(adapter);		
		return super.onCreateView(inflater, container, savedInstanceState);	
		//return inflater.inflate(R.layout.fragmentpage4, null);
	}	
	@Override
    public void onStart() {
            super.onStart();
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
