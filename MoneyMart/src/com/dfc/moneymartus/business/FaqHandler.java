package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.FaqDataObject;
import com.dfc.moneymartus.dto.FaqDataObject.Faq;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.FaqView;
import com.google.gson.Gson;

public class FaqHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;
	
	final FaqView mViewCallback;
	private String mUrl;
	private int mReqEventType;
	
	/**
	 * Used to get the Url
	 * @return Url
	 */
	public String getUrl() {
		return mUrl;
	}

	/**
	 * Used to set the Url
	 * @param url
	 */
	public void setUrl(String url) {
		this.mUrl = url;
	}

	public FaqHandler(FaqView viewCallback){
		this.mViewCallback = viewCallback;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = mUrl;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		//TODO Auto-generated method stub
	}

	/**
	 * Used to get the EventType
	 * @return EventType
	 */
	public int getReqEventType() {
		return mReqEventType; 
	}
	
	/**
	 * Used to set the EventType
	 * @param reqEventType
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String response) {
		Gson gson = new Gson();
	    FaqDataObject faqObject = gson.fromJson(response, FaqDataObject.class);
	    Faq [] faqArray = faqObject.faqs;
	    mViewCallback.onGetFaqSuccess(faqArray);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		//viewCallback.onResetPasswordSuccess();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return mReqEventType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseFailure(com.android.volley.VolleyError)
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onGetFaqFailure(error, getEventType());
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		//viewCallback.onGetFaqSuccess();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		//viewCallback.onGetFaqSuccess();
	}

	/**
	 * Used to execute a Faq request
	 */
	public void requestFaq() {
		CommuncationHelper helper= new CommuncationHelper();
		helper.executeGet(this, (ICommunicator) this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
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
