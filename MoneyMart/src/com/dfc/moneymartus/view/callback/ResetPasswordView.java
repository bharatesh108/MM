package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;


public interface ResetPasswordView {
	/**
	 * Used to send call back to UI on success of reset password API call
	 */
	public void onResetPasswordSuccess();
	
	/**
	 * Used to send call back to UI on failure of reset password API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onResetPasswordFailure(VolleyError error, int eventType);
	

}
