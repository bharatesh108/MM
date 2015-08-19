package com.dfc.moneymartus.gateway;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.business.DataObject;

public interface ICommunicator {

	/**
	 * Used to send call back to UI on success of API call 
	 * @param response API response retrieved from server
	 */
	public void onResponseSuccess(String response);

	/**
	 * Used to send call back to UI on failure of service API call
	 * @param error Error message
	 */
	public void onResponseFailure(VolleyError error);
	//public RequestInput setRequestParms();

	/**
	 * Used to send call back to UI on success of API call 
	 * @param response API response retrieved from server
	 */
	public void onResponseSuccess(JSONObject response);

	/**
	 * Used to send call back to UI on success of API call 
	 * @param response API response retrieved from server
	 * @param dataObject Object of data class
	 */
	public void onResponseSuccess(JSONObject response,DataObject dataObject);

	/**
	 * Used to send call back to UI on success of API call 
	 * @param responseData  API response retrieved from server
	 * @param dataObject Object of data class
	 */
	public void onResponseSuccess(int responseData,DataObject dataObject);
	public void onResponseSuccess(String response, DataObject dataObject);

}