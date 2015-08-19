package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.CheckDataObject;

public interface IGetCheckCallback {
	
	/**
	 * Used to send call back to UI on failure of get checks API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onGetChecksFailure(VolleyError error, int eventType);
	
	/**
	 * Used to send call back to UI on success of get checks API call
	 * @param dataObject Object of class
	 */
	public void onGetChecksSuccess(CheckDataObject dataObject);
}
