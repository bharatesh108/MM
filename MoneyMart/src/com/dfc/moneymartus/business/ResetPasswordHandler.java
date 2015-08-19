package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.ResetPasswordView;

public class ResetPasswordHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;
	private String mUserName;
	private String mNewPassword;
	final ResetPasswordView mViewCallback;
	private int mReqEventType;
	private String mAnswer;
	
	/**
	 * Used to get New Password
	 * @return NewPassword
	 */
	public String getNewPassword() {
		return mNewPassword;
	}

	/**
	 * Used to set the NewPassword
	 * @param newPassword
	 */
	public void setNewPassword(String newPassword) {
		this.mNewPassword = newPassword;
	}

	public ResetPasswordHandler(ResetPasswordView viewCallback){
		this.mViewCallback = viewCallback;
	}
	
	/**
	 * Used to get the Username
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
	
	/**
	 * Used to get the secret answer
	 * @return UserName
	 */
	public String getAnswer() {
		return mAnswer;
	}

	/**
	 * Used to set the secret answer
	 * @param userName
	 */
	public void setAnswer(String answer) {
		this.mAnswer = answer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_RESET_PASSWORD);//MMConstants.URL_RESET_PASSWORD;
		HashMap<String, String>  parameters = new HashMap<String, String>();
        parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
        parameters.put(Constants.CONTENT_TYPE, Constants.ACCEPT_JSON);
        parameters.put(Constants.LOGIN_REQUEST_HEADER_USERNAME, mUserName);
		requestParms.headers = parameters;
		/*String jsonRequest = Util.getJsonResource(R.raw.resetpasswordrequest, Moneymart.getContext());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.SECRET, mAnswer);
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.NEW_PASSWORD,mNewPassword);
		Log.d("MoneyMart","Req body"+jsonRequest);
		requestParms.body = jsonRequest;*/
		try {
			JSONObject json = new JSONObject();
			json.put("secret", mAnswer);
			json.put("newPassword", mNewPassword);
			requestParms.body = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return requestParms;	
	}

	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub	
	}
	
	/**
	 * Used to get EventType
	 * @return ReqEventType
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
		mViewCallback.onResetPasswordSuccess();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		mViewCallback.onResetPasswordSuccess();
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
		mViewCallback.onResetPasswordFailure(error, getEventType());
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		mViewCallback.onResetPasswordSuccess();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		mViewCallback.onResetPasswordSuccess();
	}

	/**
	 * Used to execute Request for New Password
	 * @param secret
	 * @param password
	 * @param userName 
	 */
	public void requestNewPassword(String userName, String secret, String password) {
		setUserName(userName);
		setNewPassword(password);
		setAnswer(secret);
		CommuncationHelper helper= new CommuncationHelper();
		helper.executePut(this, (ICommunicator) this);
	}

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
