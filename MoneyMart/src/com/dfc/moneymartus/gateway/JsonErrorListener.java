package com.dfc.moneymartus.gateway;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.business.DataObject;

public class JsonErrorListener implements ErrorListener {

	private final ICommunicator mCallback;
	
	/**
	 * Constructor
	 * @param eventObj Object od data class
	 * @param callback Callback object
	 */
	public JsonErrorListener(DataObject eventObj, ICommunicator callback) {
		this.mCallback = callback;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Response.ErrorListener#onErrorResponse(com.android.volley.VolleyError)
	 */
	@Override
	public void onErrorResponse(VolleyError error) {
		mCallback.onResponseFailure(error);
	}
	
}
