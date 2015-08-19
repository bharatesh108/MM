package com.dfc.moneymartus.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.dto.TransactionDataObject.TransactionList;
import com.dfc.moneymartus.infra.Util;

public class CardDetailsArrayAdapter extends ArrayAdapter<TransactionList> 
{
	private final Context mContext;
	private final ArrayList<TransactionList> mTransactionsList = new ArrayList<TransactionList>() ;
	private final int[] colors = new int[2];  
	/**
	 * CardDetailsArrayAdapter constructor
	 * @param context
	 * @param resource
	 * @param values
	 */
	public CardDetailsArrayAdapter (Context context, int resource, ArrayList<TransactionList> values) {
		super(context, resource,values);
		this.mContext = context;
		mTransactionsList.clear();
		mTransactionsList.addAll(values);
		colors[0] = Color.WHITE;
		colors[1] = Color.parseColor(mContext.getResources().getString(R.color.moneymart_login_bg)) ;
	}

	/*
	 * Used to get transaction list number
	 * @param position List item position
	 */
	@Override
	public TransactionList getItem(int position) {
		return mTransactionsList.get(position);
	}

	/*
	 * Used to get total number of transaction
	 */
	@Override
	public int getCount() {
		return mTransactionsList.size();
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
			view = LayoutInflater.from(mContext).inflate(R.layout.transaction_row_details, parent,false);
			viewHolder.tvAmount = (TextView) view.findViewById(R.id.tv_amount_data);
			viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_transaction_date);
			viewHolder.tvDesc = (TextView) view.findViewById(R.id.tv_description);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		TransactionList transaction = getItem(position);
		String date = Util.getyyyymmddDate(transaction.date, true);
		viewHolder.tvDate.setText(date);
		viewHolder.tvDesc.setText(transaction.description);
		if(transaction.debit){
			viewHolder.tvAmount.setText("-$"+Util.getFormattedAmount(transaction.amount));
		}else{
			viewHolder.tvAmount.setText(" $"+Util.getFormattedAmount(transaction.amount));
		}
		/*if (position % 2 != 1) {
			view.setBackgroundColor(Color.WHITE);  
		} else {
			view.setBackgroundColor(Color.parseColor(mContext.getResources().getString(R.color.moneymart_login_bg)));
		}*/
		int colorPos = position % colors.length;  
		view.setBackgroundColor(colors[colorPos]);  
		return view;
	}

	/**
	 * Used hold the list row view data
	 */
	private static class ViewHolder
	{
		private	TextView tvAmount;
		private	TextView tvDesc;
		private	TextView tvDate;
	}




}