package com.ukynda.onlywoo.data;

import java.util.ArrayList;
import com.ukynda.onlywoo.R; 
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemImageAdatper  extends BaseAdapter {  
    private ArrayList<String> mNameList = new ArrayList<String>();  
    private ArrayList<Integer> mDrawableList = new ArrayList<Integer>();  
    private LayoutInflater mInflater;   
    RelativeLayout.LayoutParams params;  
    
    public ItemImageAdatper(Context context) { 
    	 
    	mInflater = LayoutInflater.from(context);
    	
    	for (int i = 0; i < 100; i++) {
    		mNameList.add("²úÆ·"+String.valueOf(i));
    		mDrawableList.add(R.drawable.itemimage);
		}
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);  
          
    }  
  
    public int getCount() {  
        return mNameList.size();  
    }  
  
    public Object getItem(int position) {  
        return mNameList.get(position);  
    }  
  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent)
    {  
        ItemViewTag viewTag;  
          
        if (convertView == null)  
        {  
            convertView = mInflater.inflate(R.layout.gridview_item, null);  
              
            // construct an item tag   
            viewTag = new ItemViewTag(
            		(ImageView) convertView.findViewById(R.id.itemImage), 
            		(TextView) convertView.findViewById(R.id.itemName));  
            convertView.setTag(viewTag);  
        } else  
        {  
            viewTag = (ItemViewTag) convertView.getTag();  
        }  
          
        // set name   
        viewTag.mName.setText(mNameList.get(position));  
          
        // set icon   
        viewTag.mIcon.setBackgroundResource(mDrawableList.get(position));  
        viewTag.mIcon.setLayoutParams(params);  
        return convertView;  
    }
    
    class ItemViewTag  
    {  
        protected ImageView mIcon;  
        protected TextView mName; 
        
        public ItemViewTag(ImageView icon, TextView name)  
        {  
            this.mName = name;  
            this.mIcon = icon;  
        }  
    }  

}
