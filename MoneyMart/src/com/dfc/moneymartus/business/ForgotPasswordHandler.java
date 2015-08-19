package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.Challenge;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.ForgotPasswordView;
import com.google.gson.Gson;

public class ForgotPasswordHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;
	private String mUserName;
	final ForgotPasswordView mViewCallback;
	private int mReqEventType;

	public ForgotPasswordHandler(ForgotPasswordView viewCallback) {
		this.mViewCallback = viewCallback;
	}
	
	/**
	 * Used to get the UserName
	 * @return UserName
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * Used to set the UserName
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_GET_SECURITY_QUESTION);//MMConstants.URL_GET_SECURITY_QUESTION;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put(Constants.LOGIN_REQUEST_HEADER_USERNAME, getUserName());//"ANGELA");
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
	//	parameters.put(MMConstants.LOGIN_REQUEST_HEADER_ACCEPT, MMConstants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		//TODO  Auto-generated method stub
	}
	
	/**
	 * Used to get the EventType
	 * @return EventType
	 */
	public int getReqEventType() {
		return mReqEventType; //MMEventTypes.EVENT_LOGIN;
	}
	
	/**
	 * Used to set the EventType
	 * @param reqEventType
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
		//return MMEventTypes.EVENT_LOGIN;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String response) {
		Log.e("Password Response : ",":"+response);
		Gson gson = new Gson();
		Challenge challenge = gson.fromJson(response, Challenge.class);
		mViewCallback.onGetPasswordSuccess(challenge);
	}

	
	@Override
	public void onResponseSuccess(String response) {
		//TODO  Auto-generated method stub
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
		mViewCallback.onGetPasswordFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		//TODO  Auto-generated method stub
	}
	
	/**
	 * Used to execute request for new Password
	 * @param username
	 */
	public void requestNewPassword(String username) {
		setUserName(username);
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
