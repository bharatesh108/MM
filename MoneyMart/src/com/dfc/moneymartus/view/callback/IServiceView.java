package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;

public interface IServiceView {
	
	/**
	 * Used to send call back to UI on failure of service API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onCoreServiceFailure(VolleyError error, int eventType);
	/**
	 * Used to send call back to UI on success of customer service API call
	 * @param dataObject Object of class
	 */
	public void onCoreServiceSuccess(CustomerServicesDataObject dataObject);

}
