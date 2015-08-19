package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.CheckDataObject;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.IGetCheckCallback;

public class ChecksHandler extends DataObject implements ICommunicator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4999757445790671067L;
	private int mReqEventType;
	final IGetCheckCallback mViewCallback;
	private CheckDataObject mChkDao;

	public ChecksHandler(IGetCheckCallback viewCallback){
		Log.e("CheckesHandler",":"+"constructor call");
		this.mViewCallback = viewCallback;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		 mChkDao = new CheckDataObject();
		mChkDao.parseData(values);
		
	}

	/**
	 * Used to get the ChkDao
	 * @return Object of class 
	 */
	public CheckDataObject getChkDao() {
		return mChkDao;
	}

	/**
	 * Used to set the ChkDao
	 * @param chkDao object check data class
	 */
	public void setChkDao(CheckDataObject chkDao) {
		this.mChkDao = chkDao;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(java.lang.String)
	 */
	@Override
	public void setJSONValues(String values) {
	
	/*	Gson gson = new Gson();
		GetCheckDataObject getChecks = gson.fromJson(values, GetCheckDataObject.class);
		viewCallback.onGetChecksSuccess(getChecks);*/
	}

	/**
	 * this is used to set the input parameter for request and also return the requestParams
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_GET_CHECKS);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", Constants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * this is used to form the request used for getting checks
	 */
	public void requestChecks() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_GET_CHECKS);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", Constants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		CommuncationHelper helper= new CommuncationHelper();
		helper.executeGetChecks(requestParms, (ICommunicator) this,this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return mReqEventType;
	}
	
	public int getReqEventType() {
		return mReqEventType; //MMEventTypes.EVENT_LOGIN;
	}
	
	/**
	 * Used to set the EventType
	 * @param reqEventType the reqEventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
		//return MMEventTypes.EVENT_LOGIN;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		Log.e("ChecksHandler"," : "+"onResponseSuccess(String)");
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseFailure(com.android.volley.VolleyError)
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onGetChecksFailure(error, getEventType());
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject)
	 */
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
		Log.d("MoneyMart", "Resposne success on response ........" + response);
		mViewCallback.onGetChecksSuccess(mChkDao);
		
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
