package com.dfc.moneymartus.view.callback;

import java.util.ArrayList;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.FundingMechanismDataObject.FundingMechanisms;


public interface FundingMechanismView {
	/**
	 * Used to send call back to UI on success of get funding mechanism API call
	 * @param fundingMechanismObject Class object
	 * @param mCardNumber array of card number
	 */
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject, ArrayList<String> mCardNumber);
	
	/**
	 * Used to send call back to UI on success of get funding mechanism API call
	 * @param mCardNumber array of card number
	 */
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber);
	/**
	 * Used to send call back to UI on failure of get funding mechanism API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onGetFundingMechanismFailure(VolleyError error, int eventType);
	
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler dataObject);

}
