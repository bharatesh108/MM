package com.dfc.moneymartus.business;

import org.json.JSONObject;

import com.dfc.moneymartus.gateway.RequestInput;

public abstract class MMDataObject {
	/**
	 * Used to set JSON values
	 * @param values : JSON object to set
	 * @return 
	 */
	public abstract void setJSONValues(JSONObject values);
	
	/**
	 * Set request parameters
	 * @return request input data
	 */
	public abstract RequestInput setRequestParms();
	
	public abstract int setEventType();
	public abstract Boolean isSucessResponse(boolean isSucessRes);
}
