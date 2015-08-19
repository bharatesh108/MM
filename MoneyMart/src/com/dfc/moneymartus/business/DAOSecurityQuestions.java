package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.SecurityQuestionDataObject;
import com.dfc.moneymartus.dto.SecurityQuestionDataObject.Questions;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.GetSecurityQuestionsView;
import com.google.gson.Gson;

public class DAOSecurityQuestions extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1665111800225598156L;
	final GetSecurityQuestionsView mViewCallback;
	private int mReqEventType;
	
	public DAOSecurityQuestions(GetSecurityQuestionsView viewCallback){
		this.mViewCallback = viewCallback;
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
		mViewCallback.onGetQuestionsFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setJSONValues(JSONObject response) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String values) {
		Gson gson = new Gson();
	    SecurityQuestionDataObject faqObject = gson.fromJson(values, SecurityQuestionDataObject.class);
	    Questions [] faqArray = faqObject.challenges;
	    mViewCallback.onGetQuestionsSuccess(faqArray);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.URL_GET_SECURITY_QUESTIONS;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		parameters.put(Constants.LOGIN_REQUEST_HEADER_ACCEPT, Constants.ACCEPT_APP_JSON);
		parameters.put(Constants.CONTENT_TYPE, Constants.ACCEPT_APP_JSON);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/**
	 * Used to get the EventType
	 * @return Event Type
	 */
	public int getReqEventType() {
		return mReqEventType;
	}
	
	/**
	 * Used to set the EventType
	 * @param reqEventType value of event type
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
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
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return mReqEventType;
	}

}
