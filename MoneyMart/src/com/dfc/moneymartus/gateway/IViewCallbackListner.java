package com.dfc.moneymartus.gateway;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.business.DataObject;

public interface IViewCallbackListner {
	
	/**
	 * Used to send call back to UI on successful response of API call
	 */
	public void onViewSuccess();
	
	/**
	 * Used to send call back to UI on failure of API call
	 * @param error Error message
	 * @param eventType Event type of API
	 */
	public void onViewFailure(VolleyError error, int eventType);
	
	/**
	 * Used to send call back along with server data to UI on successful response of API call
	 * @param dataObject Object of DTO class
	 */
	public void onViewSuccess(DataObject dataObject);
}
