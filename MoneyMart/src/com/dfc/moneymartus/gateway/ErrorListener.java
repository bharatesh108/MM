package com.dfc.moneymartus.gateway;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.infra.EventTypes;

class ErrorListener implements Response.ErrorListener{

	private final DataObject mEventObj;
	private final ICommunicator mCallback;

	/**
	 * Constructor
	 * @param eventObj Data object
	 * @param callback Call back object
	 */
	public ErrorListener(DataObject eventObj, ICommunicator callback) {
		this.mEventObj = eventObj;
		this.mCallback = callback;
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Response.ErrorListener#onErrorResponse(com.android.volley.VolleyError)
	 */
	@Override
	  public void onErrorResponse(VolleyError error){
		  if (mEventObj.getEventType() == EventTypes.EVENT_LOGIN)
			{
				// eventObj.setJSONValues(response);
				mCallback.onResponseFailure(error);
				
			}

	  }
	}
