package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.Challenge;


public interface ForgotPasswordView {
	/**
	 * Used to send call back to UI on success of forgot password API call
	 * @param dataObject object of class
	 */
	public void onGetPasswordSuccess(Challenge dataObject);
	
	/**
	 * Used to send call back to UI on failure of get checks API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onGetPasswordFailure(VolleyError error, int eventType);

}
