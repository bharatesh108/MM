package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;

public class RegistrationDataObject  extends DataObject {

	private static final long serialVersionUID = 511455008117825652L;
	private int mReqEventType;
	
	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setJSONValues(String values) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.URL_GET_REGISTRATIONS;
		//Log.d(CommuncationHelper.TAG,"Impl funding ........"+authtoken);
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", Constants.ACCEPT_HAL_JSON_UTF);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/**
	 * @return the mEventType
	 */
	public int getReqEventType() {
		return mReqEventType;
	}

	/**
	 * @param mEventType the mEventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#getEventType()
	 */
	@Override
	public int getEventType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	}
}
