package com.dfc.moneymartus.business;

import java.io.Serializable;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;

public abstract class DataObject implements Serializable {

	private static final long serialVersionUID = -2451741210752165842L;

	/**
	 * Used to parse API response. Response data is in JSON object format
	 * @param values : JSON object to set
	 */
	public abstract void setJSONValues(JSONObject values);
	
	/**
	 * Used to parse API response. Response data is in string format
	 * @param values
	 */
	public abstract void setJSONValues(String values);
	
	/**
	 * Set API request parameters
	 * @return request input data
	 */
	public abstract RequestInput setRequestParms();
	
	/**
	 * Used to set API request format as JSON object
	 * @return JSON object
	 */
	public abstract JSONObject setJsonRequestParms();
	
	/**
	 * Used to get event type
	 * @return event type
	 */
	public abstract int getEventType();
	
	/**
	 * Used to set API request method i.e. GET/POST/PUT
	 * @param reqMethod Request method
	 */
	public void register(int reqMethod)
	{
		CommuncationHelper helper= new CommuncationHelper();

		switch (reqMethod)
		{
		case Method.GET:
			if (this instanceof DAOLogin) {
				helper.executeGetLogin(this, (ICommunicator) this);
			}
			else if(this instanceof DownloadImageHandler){
				helper.executeGetImage(this, (ICommunicator) this);
			}
			else{
				helper.executeGet(this, (ICommunicator) this);
			}
			break;

		case Method.PUT:
			helper.executePutRequest(this, (ICommunicator) this);
			break;

		case Method.POST:
			if(this instanceof SignupPersonalDetailHandler || this instanceof GetFundingMechanismHandler){
				helper.executeStringPostRequest(this, (ICommunicator) this);
			} else {
				helper.executePostRequest(this, (ICommunicator) this);
			}
			break;
		default:
			break;
		}
	}

}
