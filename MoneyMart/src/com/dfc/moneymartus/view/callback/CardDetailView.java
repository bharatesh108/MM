package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.TransactionDataObject.TransactionList;


public interface CardDetailView {
	
	/**
	 * Used to send error message on failure of get funding mechanism API call
	 * @param error Error message
	 * @param eventType API call event type
	 */
	public void onGetFundingMechanismFailure(VolleyError error, int eventType);
	
	/**
	 * Used to send server data to parser on successful response of get funding mechanism API call
	 * @param fundingMechanismObject
	 */
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject);
	
	/**
	 * Used to send server data to parser on successful response of transaction history 
	 * @param transactionList List of transactions
	 */
	public void onGetTrancationSuccess(TransactionList... transactionList );
	
	/**
	 * Used to send error message on failure of transaction history API call
	 * @param error Error message
	 * @param eventType API call event type
	 */ 
	public void onGetTrancationFailure(VolleyError error, int eventType);

}
