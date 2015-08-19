package com.dfc.moneymartus.infra;

import java.lang.ref.WeakReference;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class USCurrencyFormatter implements TextWatcher {

	private boolean isFormatting;

	private int mLastStartLocation;
	private String mBeforeText;
	private final WeakReference<EditText> mWeakEditText;
	private int mLastCursorIndex;
	private String mLastBeforeText;

	public USCurrencyFormatter(WeakReference<EditText> weakEditText) {
		this.mWeakEditText = weakEditText;
	}

	/**
	 * listener for before text change
	 * @param charsequence
	 * @param start
	 * @param count
	 * @param after
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		mLastStartLocation = start;
		mBeforeText = s.toString();
	}

	/**
	 * listener for on text change
	 * @param charsequence
	 * @param start
	 * @param count
	 * @param after
	 */
	@Override
	public void onTextChanged(CharSequence string, int start, int before, int count) {
		Log.i("Currency", "====>= " + string);
	}

	/**
	 * listener for after text change
	 * @param charsequence
	 * @param start
	 * @param count
	 * @param after
	 */
	@Override
	public void afterTextChanged(Editable s) {
		String formattedValue = "";
		int cursorIdexToSet = 0;
		if (!isFormatting) {
			mLastCursorIndex = mLastStartLocation;
			mLastBeforeText = mBeforeText;
			isFormatting = true;
			formattedValue = formatUsNumber(s);
			try{
			if (formattedValue != null) {
				mWeakEditText.get().setText(formattedValue);
				mWeakEditText.get().setTextKeepState(formattedValue.toString());
			}
			
			if(mWeakEditText.get().getText().toString().length() == 5){
				cursorIdexToSet = mBeforeText.toString().length();
			}
			else if(mLastBeforeText.toString().length() > mWeakEditText.get().getText().toString().length() &&
					mLastBeforeText.toString().length() - mWeakEditText.get().getText().toString().length() == 1){
				if(mLastBeforeText.toString().indexOf(",") == mLastCursorIndex){
					mLastCursorIndex--;
				}
				cursorIdexToSet = mLastCursorIndex;
			}
			else if(mLastBeforeText.toString().length() - mWeakEditText.get().getText().toString().length() > 1){
				cursorIdexToSet = mWeakEditText.get().getText().length();
			}
			else{
				if((mLastCursorIndex + 1) <= mWeakEditText.get().getText().length()){
					if(mWeakEditText.get().getText().toString().length() - mLastBeforeText.toString().length() > 1){
						mLastCursorIndex += mWeakEditText.get().getText().toString().length() - mLastBeforeText.toString().length();
					}
					else{
						mLastCursorIndex++;
					}
					cursorIdexToSet = mLastCursorIndex;
				}
				else{
					cursorIdexToSet = mWeakEditText.get().getText().length();
				}
			}
			
			}catch(IndexOutOfBoundsException e){
				isFormatting = false;
			}
			isFormatting = false;
		}
		System.out.println("Lenght : "+mWeakEditText.get().getText().toString().length());
		System.out.println("mLastBeforeText : "+mLastBeforeText.toString().length());
		System.out.println("lastCursorIndex : "+mLastCursorIndex);
		
		Selection.setSelection(mWeakEditText.get().getText(), cursorIdexToSet);
	}

	/*
	 * format us number
	 * @param Editable text
	 */
	private String formatUsNumber(Editable text) {
		StringBuilder cashAmountBuilder = null;
		String USCurrencyFormat = text.toString();
//		if (!text.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) { 
			String userInput = "" + text.toString().replaceAll("[^\\d]", "");
			cashAmountBuilder = new StringBuilder(userInput);

			while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
				cashAmountBuilder.deleteCharAt(0);
			}
			while (cashAmountBuilder.length() < 3) {
				cashAmountBuilder.insert(0, '0');
			}
			cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');
			USCurrencyFormat = cashAmountBuilder.toString();
			USCurrencyFormat = Util.getdoubleUSPriceFormat(Double.parseDouble(USCurrencyFormat));

//		}
		if("0.00".equals(USCurrencyFormat)){
			return "";
		}
		if(!USCurrencyFormat.contains("$"))
			return "$"+USCurrencyFormat;
		return USCurrencyFormat;
	}
}
