package com.dfc.moneymartus.business;

import java.io.Serializable;

import org.json.JSONObject;

import com.dfc.moneymartus.gateway.RequestInput;

public class ServiceObject extends MMDataObject implements Serializable{

	private static final long serialVersionUID = -7992660872103237282L;
	private String mService;
	private Boolean mTermsAgreed = null;
	private String mStatus;
	private String mExtra;
	
	/**
	 * set json values
	 * @param jsonobject values
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		   if (values != null) {
               this.mService       = values.optString("service");
               this.mTermsAgreed   = values.optBoolean("termsAgreed");
               this.mService       = values.optString("status");
           }	
	}

	/**
	 * set request params
	 */
	@Override
	public RequestInput setRequestParms() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * set event type
	 */
	@Override
	public int setEventType() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * check success response
	 */
	@Override
	public Boolean isSucessResponse(boolean isSucessRes) {
		return isSucessRes;
		
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return mService;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.mService = service;
	}

	/**
	 * @return the termsAgreed
	 */
	public Boolean getTermsAgreed() {
	//	mTermsAgreed = false;
		return mTermsAgreed;
	}

	/**
	 * @param termsAgreed the termsAgreed to set
	 */
	public void setTermsAgreed(Boolean termsAgreed) {
		this.mTermsAgreed = termsAgreed;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return mStatus;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.mStatus = status;
	}

	/**
	 * @return the extra
	 */
	public String getExtra() {
		return mExtra;
	}

	/**
	 * @param extra the extra to set
	 */
	public void setExtra(String extra) {
		this.mExtra = extra;
	}

}
