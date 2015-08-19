package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;

public class UpdatePswDataObject extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 7719143641475574337L;
	final IViewCallbackListner mViewCallback;
	private String mPassword;
	private int mReqEventType;
	private JSONObject jsonData;
	
	/**
	 * Update Password Data Object Constructor
	 * @param viewCallback
	 */
	public UpdatePswDataObject(IViewCallbackListner viewCallback) {
		this.mViewCallback = viewCallback;
	}
	
	/**
	 * on successful response
	 * @param response
	 */
	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub
	}

	/**
	 * response failur
	 * @param error
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
//		Log.e("failure",":"+ error.networkResponse.statusCode);
		mViewCallback.onViewFailure(error, getEventType());			
	}

	/**
	 * on successful response
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * on successful response
	 * @param response
	 * @param dataobject
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * set json data
	 * @param JsonObject
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * set json value
	 * @param String
	 */
	@Override
	public void setJSONValues(String values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * set request params
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.UPDATE_PASSWORD);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put(Constants.CONTENT_TYPE, Constants.ACCEPT_JSON);//MMConstants.ACCEPT_HAL_JSON_UTF);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/**
	 * @return the jsonData
	 */
	public JSONObject getJsonReqData() {
		return jsonData;
	}

	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonReqData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", getPassword());
		JSONObject data = new JSONObject(params);
		this.jsonData = data;
	}
	@Override
	public int getEventType() {
		return mReqEventType; //MMEventTypes.UPDATE_PASSWORD;
	}

	/**
	 * @return the mPassword
	 */
	public String getPassword() {
		//return "P@ssword12";
		return mPassword;
	}

	/**
	 * @param mPassword the mPassword to set
	 */
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	/**
	 * @return the mReqEventType
	 */
	public int geReqEventType() {
		return mReqEventType;
	}

	/**
	 * @param mReqEventType the mReqEventType to set
	 */
	public void setReqEventType(int mReqEventType) {
		this.mReqEventType = mReqEventType;
	}

	/**
	 * set json request param
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", getPassword());
		/*JSONObject data = new JSONObject(params);
		return data;*/
		return new JSONObject(params);
	}

	/**
	 * on successfull response
	 * @param response data
	 * @param data object
	 */
	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		if (dataObject.getEventType() == EventTypes.EVENT_UPDATE_PASSWORD) {
			Log.e("EVENT_UPDATE_PASSWORD",":"+"navigate to home screen");
			mViewCallback.onViewSuccess(dataObject);
		}
	}

	/**
	 * on successfull response
	 */
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

}
