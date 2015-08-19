package com.dfc.moneymartus.gateway;

import com.android.volley.Response;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.infra.EventTypes;

class ResponseListener implements Response.Listener<String>{

	private final DataObject mEventObj;
	private final ICommunicator mCallback;

	/**
	 * Constructor
	 * @param eventObj Object of data class
	 * @param callback Callback object
	 */
	public ResponseListener(DataObject eventObj, ICommunicator callback) {
		this.mEventObj = eventObj;
		this.mCallback = callback;
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Response.Listener#onResponse(java.lang.Object)
	 */
	@Override
	public void onResponse(String response) {
		if (mEventObj.getEventType() == EventTypes.EVENT_LOGIN)
		{
			mCallback.onResponseSuccess(response);
			
		}

	}
}
