package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;

public class CoreTermsDataObject  extends DataObject  implements ICommunicator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -465535896607285577L;
	private String mServiceName;
	private String mTermsText;
	private int mReqEventType;
	final IViewCallbackListner mViewCallback;
//	private CCTermsDataObject mCCTermsDataObject;
	private boolean isTermsAvail;
	
	public CoreTermsDataObject(IViewCallbackListner viewCallback) {//, CCTermsDataObject mCCTermsDataObject){
		this.mViewCallback = viewCallback;
		//this.mCCTermsDataObject = mCCTermsDataObject;
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
				 JSONObject addressJSON = values.getJSONObject("coreTerms");
				 setServiceName(addressJSON.getString("serviceName"));
				 setTermsText(addressJSON.getString("termsText"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setJSONValues(String values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * this is used to create input parameters for CoreTerms Services
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.CORE_TERMS_URL);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", Constants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return EventTypes.EVENT_CORE_TERMS;
	}

	/**
	 * Used to get the ServiceName
	 * @return the mServiceName
	 */
	public String getServiceName() {
		return mServiceName;
	}

	/**
	 * Used to set the ServiceName
	 * @param mServiceName the mServiceName to set
	 */
	public void setServiceName(String mServiceName) {
		this.mServiceName = mServiceName;
	}

	/**
	 * Used to get the TermsText
	 * @return the mTermsText
	 */
	public String getTermsText() {
		return mTermsText;
	}

	/**
	 * Used to set the Terms Text
	 * @param mTermsText the mTermsText to set
	 */
	public void setTermsText(String mTermsText) {
		this.mTermsText = mTermsText;
	}

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
//		Log.e("CoreTermsDataObject failure",":"+ error.networkResponse.statusCode);
		mViewCallback.onViewFailure(error, getEventType());		
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		if (dataObject.getEventType() == EventTypes.EVENT_CORE_TERMS) {
			
			mViewCallback.onViewSuccess(dataObject);/*
			if (getTermsText() != null && !getTermsText().isEmpty()) {
				if (mCCTermsDataObject.getTermsText() != null
						&& !mCCTermsDataObject.getTermsText().isEmpty()) {
					//viewCallback.onViewSuccess(dataObject);
					setTermsAvail(true);
				
				}
			}
		*/}
		
	}


	/**
	 * Used to get the EventType
	 * @return the eventType
	 */
	public int getReqEventType() {
		return mReqEventType;
	}

	/**
	 * Used to set the EventType
	 * @param eventType the eventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
	}

	/**
	 * Used to verify terms data is available
	 * @return TermsAvail
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

	@Override
	public JSONObject setJsonRequestParms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}
}
