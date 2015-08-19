package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.IServiceView;
import com.google.gson.Gson;

public class CoreServiceHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 4133590743894970838L;
	private int mReqEventType;
	static final String TAG = "CoreServiceHandler";
	final IServiceView mViewCallback;

	public CoreServiceHandler(IServiceView viewCallback){
		Log.e("CustDetailDataObject",":"+"constructor call");
		this.mViewCallback = viewCallback;
	}

	@Override
	public void onResponseSuccess(String response) {
	/*	Gson gson = new Gson();
		CustomerServicesDataObject challenge = gson.fromJson(response, CustomerServicesDataObject.class);
		viewCallback.onCoreServiceSuccess(challenge);*/
	}

	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onCoreServiceFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		//TODO Nothing
	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		//TODO Nothing
	}

	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		//TODO Nothing

	}
	
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		//TODO Nothing
	}

	@Override
	public void setJSONValues(JSONObject values) {
		//TODO Nothing
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String values) {
		Gson gson = new Gson();
		CustomerServicesDataObject custService = gson.fromJson(values, CustomerServicesDataObject.class);
		Constants.setCustServiceObj(custService);
		mViewCallback.onCoreServiceSuccess(custService);
	}

	/**
	 * this is used to set the input parameter for CoreService request
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_CORE_SERVICE);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", Constants.ACCEPT_APP_JSON);
		requestParms.headers = parameters;
		return requestParms;	
	}

	@Override
	public JSONObject setJsonRequestParms() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		// TODO Auto-generated method stub
		return mReqEventType;
	}
	/**
	 * Used to get the ReqEventType
	 * @return the mEventType
	 */
	public int getReqEventType() {
		return mReqEventType;
	}

	/**
	 * Used to set the ReqEventType
	 * @param mEventType the ReqEventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
	}
}
