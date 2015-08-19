package com.dfc.moneymartus.business;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;

public class DownloadImageHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;

	private String mCheckId;
	private String mImageType;
	private String mBase64;
	final IViewCallbackListner mViewCallback;
	private int mReqEventType;

	public DownloadImageHandler(IViewCallbackListner viewCallback){
		this.mViewCallback = viewCallback;
	}
	
	
	public DownloadImageHandler(String checkId, String imageType,
			IViewCallbackListner viewCallback) {
		super();
		this.mCheckId = checkId;
		this.mImageType = imageType;
		this.mViewCallback = viewCallback;
	}

    /**
     * Used to get the CheckId 
     * @return CheckId
     */
	public String getCheckId() {
		return mCheckId;
	}

	/**
	 * Used to set the CheckId
	 * @param checkId Check id
	 */
	public void setCheckId(String checkId) {
		this.mCheckId = checkId;
	}

	/**
	 * Used to get the ImageType
	 * @return Image type
	 */
	public String getImageType() {
		return mImageType;
	}

	/**
	 * Used to set the ImageType 
	 * @param imageType image type
	 */
	public void setImageType(String imageType) {
		this.mImageType = imageType;
	}

	/**
	 * Used to get the Base64
	 * @return Base64
	 */
	public String getBase64() {
		return mBase64;
	}

	/**
	 * Used to set the Base64
	 * @param base64 Base64 string of image
	 */
	public void setBase64(String base64) {
		this.mBase64 = base64;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL + Constants.URL_GET_CHECKS + File.separator + "349" + File.separator + mImageType;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		parameters.put("Authorization", Constants.authToken);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Used to get the EventType
	 * @return Event
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
		this.mBase64 = response;
		mViewCallback.onViewSuccess(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		this.mBase64 = response;
		mViewCallback.onViewSuccess(this);
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
		mViewCallback.onViewFailure(error, getEventType());
		
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
