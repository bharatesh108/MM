package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.EventTypes;

public class DAOLogin extends DataObject implements ICommunicator{

	private static final long serialVersionUID = -8183268038828001324L;
	private String mUserName;
	private String mPassword;
	private String mSessionId;
	final IViewCallbackListner mViewCallback;
	private int mReqEventType;

	public DAOLogin(IViewCallbackListner viewCallback){
		this.mViewCallback = viewCallback;
	}
	
	/**
	 * used to get the UserName
	 * @return the mUserName
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * used to set the UserName
	 * @param userName the UserName to set
	 */
	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	/**
	 * used to get the Password
	 * @return the password
	 */
	public String getPassword() {
		return mPassword;
	}

	/**
	 * used to set the password
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.mPassword = password;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		
		RequestInput requestParms = new RequestInput();
		HashMap<String, String>  parameters = new HashMap<String, String>();
		/*
		 * Below array is used for testing purpose
		 */
		/*String[] username = {"MobileTestUser001","MobileTestUser002","MobileTestUser003","MobileTestUser004","MobileTestUser005",
				"MobileTestUser006","MobileTestUser007","MobileTestUser008", "MobileTestUser009","MobileTestUser010",
				"MobileTestUser011", "MobileTestUser012", "MobileTestUser013","SmitaTestUser", "MobileUser2",
				"MobileTestUser0071", "Moneymart11", "MobileTest100","TestUser07"};
		String[] psw = {"P@ssword15","P@ssword14","P@ssword15","P@ssword15","Password@15",
				"P@ssword18", "P@ssword19", "P@ssword20", "P@ssword21", "P@ssword20",
				"P@ssword23","P@ssword24", "P@ssword25","P@ssword12", "P@ssword15",
				"P@ssword19", "P@ssword55","Ajay@12345","TestUser07@" };
		String userName = username[18];
		String password = psw[18];

		parameters.put(Constants.LOGIN_REQUEST_HEADER_USERNAME, userName);
		parameters.put(Constants.LOGIN_REQUEST_HEADER_PASSWORD, password);*/

		parameters.put(Constants.LOGIN_REQUEST_HEADER_USERNAME,  getUserName());
		parameters.put(Constants.LOGIN_REQUEST_HEADER_PASSWORD, getPassword());

		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		parameters.put(Constants.LOGIN_REQUEST_HEADER_ACCEPT, Constants.ACCEPT_HAL_JSON_UTF);
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
	 * used to get the EventType
	 * @return the EventType
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
		Preferences mPreferences = Preferences.getInstance(Moneymart.getContext());
		mPreferences.addOrUpdateString(PreferenceConstants.AUTORIZATION_TOKEN, response);
		setSessionId(response);
	}

	/**
	 * Used to get the session id
	 * @return SessionId
	 */
	public String getSessionId() {
		return mSessionId;
	}

	/**
	 * Used to set the SessionId
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.mSessionId = sessionId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String)
	 */
	@Override
	public void onResponseSuccess(String response) {
		Constants.setAuthTOken(getSessionId());
		CustDetailDataObject custDetailsObj = new CustDetailDataObject(mViewCallback);
		custDetailsObj.setReqEventType(EventTypes.EVENT_LOGIN_PERSONAL_DETAILS);
		custDetailsObj.register(Method.GET);
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

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		// TODO Auto-generated method stub
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

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(int, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}
}
