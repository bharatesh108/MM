package com.dfc.moneymartus.business;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.RecaptureCheckView;

public class RecaptureCheckHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;

	final RecaptureCheckView mViewCallback;
	private String mUrl;
	private int mReqEventType;
	private String mStrImgBase64;

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

	public RecaptureCheckHandler(RecaptureCheckView viewCallback){
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
		parameters.put("Authorization",Constants.authToken);
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		requestParms.headers = parameters;
		return requestParms;	
	}

	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
	}

	/**
	 * Used to get the EventType 
	 * @return Even
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
		setImgBase64(response);
	//	viewCallback.onRecaptureSuccess(response);
		// byte[] decodedString = Base64.decode(response,Base64.DEFAULT);
		//Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 

	}

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

	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onRecaptureCheckFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		//viewCallback.onGetFaqSuccess();
	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		//viewCallback.onGetFaqSuccess();
	}

	/**
	 * Used to send API request to sever to download already uploaded check image
	 * @return EventType
	 */
	public int getImage() {
		//	CommuncationHelper helper= new CommuncationHelper();
		//helper.executeGet(this, (ICommunicator) this);
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(getUrl());
			httpget.addHeader("Authorization",Constants.authToken);
			httpget.addHeader(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
			HttpResponse response = client.execute(httpget);
			if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				setJSONValues(EntityUtils.toString(entity));
				 
				
			} 
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mReqEventType;
	}

	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	}

	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		mViewCallback.onRecaptureSuccess(getEventType(),getImgBase64());
	}

	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the mStrImgBase64
	 */
	public String getImgBase64() {
		return mStrImgBase64;
	}

	/**
	 * @param mStrImgBase64 the mStrImgBase64 to set
	 */
	public void setImgBase64(String mStrImgBase64) {
		this.mStrImgBase64 = mStrImgBase64;
	}
}
