package com.dfc.moneymartus.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dfc.moneymartus.R;

public class FaqArrayAdapter extends ArrayAdapter<String> 
{
    private final Context mContext;
	private final String[] mQuestionList ;
	
	/**
	 * FaqArrayAdapter constructor
	 * @param context Activity context
	 * @param resource List row layout
	 * @param values String array holds list of questions
	 */
	public FaqArrayAdapter (Context context, int resource, String... values) {
    	 super(context, resource,values);
    	 this.mContext = context;
    	 this.mQuestionList=values;
	}
     
	/**
	 * Used to get item position
	 * @param position List item position
	 */
	@Override
	public String getItem(int position) {
		return mQuestionList[position];
	}
	
	/**
	 * Used to get total number of question
	 */
	@Override
	public int getCount() {
		return mQuestionList.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
     @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	 ViewHolder viewHolder;
 		View view =convertView;
 		if (view == null) {
 			viewHolder = new ViewHolder();
 			view = LayoutInflater.from(mContext).inflate(R.layout.faq_list_item, parent,false);
 			viewHolder.tvName = (TextView) view.findViewById(R.id.text1);
 			view.setTag(viewHolder);
 		}else {
 			viewHolder = (ViewHolder) view.getTag();
 		}
 		String question = getItem(position);
 		viewHolder.tvName.setText(question);
		return view;
 		

 	}

     /**
      * Used to hold view data
      */
     private static class ViewHolder
     {
 		private	TextView tvName;
     }


     
     
}