package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;

public class CCTermsDataObject extends DataObject implements ICommunicator{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1746183475409553823L;
	private String mServiceName;
	private String mTermsText;
	private int mReqEventType;
	final IViewCallbackListner mViewCallback;
//	private CoreTermsDataObject mCoreTermsDataObject;
	private boolean isTermsAvail;
	
	public CCTermsDataObject(IViewCallbackListner viewCallback)/*, CoreTermsDataObject mCoreTermsDataObject)*/{
		this.mViewCallback = viewCallback;
	//	this.mCoreTermsDataObject = mCoreTermsDataObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		try {
			if(values != null)
			{
				 JSONObject addressJSON = values.getJSONObject("ccTerms");
				 setServiceName(addressJSON.getString("serviceName"));
				 setTermsText(addressJSON.getString("termsText"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String values) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.CC_TERMS_URL);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		//parameters.put("Accept", MMConstants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return mReqEventType;//MMEventTypes.EVENT_CC_TERMS;
	}

	/**
	 * Used to get service name
	 * @return service name
	 */
	public String getServiceName() {
		return mServiceName;
	}

	/**
	 * Used to set service name
	 * @param mServiceName service name
	 */
	public void setServiceName(String mServiceName) {
		this.mServiceName = mServiceName;
	}

	/**
	 * Used to get terms text
	 * @return terms data
	 */
	public String getTermsText() {
		return mTermsText;
	}

	/**
	 * Used to set terms text
	 * @param mTermsText terms text
	 */
	public void setTermsText(String mTermsText) {
		this.mTermsText = mTermsText;
	}

	/**
	 * Used to get event type
	 * @return event type
	 */
	public int getreqEventType() {
		return mReqEventType;
	}

	/**
	 * Used to set event type
	 * @param mEventType event type
	 */
	public void setReqEventType(int ReqEventType) {
		this.mReqEventType = ReqEventType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseFailure(com.android.volley.VolleyError)
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onViewFailure(error, getEventType());
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		//TODO Nothing
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		mViewCallback.onViewSuccess(dataObject);
/*
		if (dataObject.getEventType() == MMEventTypes.EVENT_CC_TERMS) {
			if (getTermsText() != null && !getTermsText().isEmpty()) {
				if (mCoreTermsDataObject.getTermsText() != null
						&& !mCoreTermsDataObject.getTermsText().isEmpty()) {
					//viewCallback.onViewSuccess(dataObject);
					setTermsAvail(true);
				
				}
			}
		}*/
		
	}

	/**
	 * Used to verify terms data is available
	 * @return status of terms data
	 */
	public boolean isTermsAvail() {
		return isTermsAvail;
	}

	/**
	 * Used to set status of terms data availability
	 * @param isTermsAvail status of terms data
	 */
	public void setTermsAvail(boolean isTermsAvail) {
		this.isTermsAvail = isTermsAvail;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(int, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}
}