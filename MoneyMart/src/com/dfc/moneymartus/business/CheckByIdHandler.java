package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;

public class CheckByIdHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 2032086896685119799L;
	final IViewCallbackListner mViewCallback;
	public String mReqestCheckID;
	private int mReqEventType;
	private ChecksObject mCheckObj;

	public CheckByIdHandler(IViewCallbackListner viewCallback){
		Log.e("CheckesHandler",":"+"constructor call");
		this.mViewCallback = viewCallback;
	}

	/**
	 * Used to get Request CheckId
	 * @return RequestCheckID
	 */
	public String getRequestCheckID() {
		return mReqestCheckID;
	}


	public void setRequestCheckID(String reqestCheckID) {
		this.mReqestCheckID = reqestCheckID;
	}


	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onViewFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseSuccess(JSONObject values, DataObject dataObject) {
	/*	if (values != null) {

			mCheckObj = new MMChecksObject();
			mCheckObj.setJSONValues(values);*/
		/*	JSONArray jsonArrayChecks = values.optJSONArray("cheques");
			//JSONObject jsonMessage = values.optJSONObject("message");
			this.setMessage(values.optString("message"));
			try {
				if (jsonArrayChecks != null) {
					mAlCheckObj = null;
					if (mAlCheckObj == null) {
						mAlCheckObj = new ArrayList<MMChecksObject>();
					}
					for (int j = 0; j < jsonArrayChecks.length(); j++) {
						JSONObject checksJSON = (JSONObject) jsonArrayChecks.get(j);
						MMChecksObject checksObj = new MMChecksObject();
						checksObj.setJSONValues(checksJSON);
						if (Integer.parseInt(checksObj.getStatusCode()) == 02)
						{
							this.mAlUarList.add(checksObj);
						}
						else {
							this.mAlCheckObj.add(checksObj);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		*/
		//}
		mViewCallback.onViewSuccess(dataObject);
	}

	/**
	 * Used to set the CheckObj
	 * @return the mCheckObj
	 */
	public ChecksObject getCheckObj() {
		return mCheckObj;
	}

	/**
	 * Used to set the CheckObj
	 * @param mCheckObj the mCheckObj to set
	 */
	public void setCheckObj(ChecksObject mCheckObj) {
		this.mCheckObj = mCheckObj;
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

	/**
	 * this is used to set the received json values into CheckObj
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		if (values != null) {

			mCheckObj = new ChecksObject();
			mCheckObj.setJSONValues(values);
		}
	}

	@Override
	public void setJSONValues(String values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * this method is used to set the input parameter for request
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL + Constants.URL_CHECK_BY_ID+ "/"+getRequestCheckID();
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put(Constants.CONTENT_TYPE,Constants.ACCEPT_JSON);
		requestParms.headers = parameters;
		
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	
	}

	/**
	 * returns the EventType
	 */
	@Override
	public int getEventType() {
		return mReqEventType;
	}

	public int getReqEventType() {
		return mReqEventType; //MMEventTypes.EVENT_LOGIN;
	}
	
	/**
	 * 
	 * @param reqEventType the reqEventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
		//return MMEventTypes.EVENT_LOGIN;
	}
}
