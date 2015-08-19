package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.SignUpUserDetailDataObject;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;

public class SignupPersonalDetailHandler extends DataObject implements ICommunicator {

	private static final long serialVersionUID = -572753765299379870L;
	private int mReqEventType;
	final IViewCallbackListner mViewCallback;
	private SignUpUserDetailDataObject mUserDetailDataObject;
	static final String TAG = "MMSignupPersonalDetailHandler";

	/**
	 * SignupPersonalDetailHandler constructor
	 * @param viewCallback
	 */
	public SignupPersonalDetailHandler(IViewCallbackListner viewCallback) {
		this.mViewCallback = viewCallback;
	}

	/**
	 * set json value
	 * @param JsonObject value
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
	}

	/**
	 * set json values
	 * @param String values
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
		requestParms.url =  Constants.BASE_URL.concat(Constants.URL_GET_CUSTOMER);//MMConstants.URL_GET_CUSTOMER;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		parameters.put(Constants.LOGIN_REQUEST_HEADER_ACCEPT, Constants.ACCEPT_HAL_JSON_UTF);
		parameters.put(Constants.CONTENT_TYPE,  "application/json");
		requestParms.headers = parameters;

		try {
			JSONObject json = new JSONObject();
			json.put("username", mUserDetailDataObject.username);
			json.put("firstName", mUserDetailDataObject.firstName);
			json.put("secondName", mUserDetailDataObject.secondName);
			json.put("dateOfBirth", mUserDetailDataObject.dateOfBirth);
			json.put("ssn", mUserDetailDataObject.ssn);
			json.put("phone", mUserDetailDataObject.phone);
			json.put("phone2", mUserDetailDataObject.phone2);
			json.put("email", mUserDetailDataObject.email);
			json.put("termsAgreed", true);
			json.put("password", mUserDetailDataObject.password);
			json.put("challengeId", mUserDetailDataObject.challengeId);
			json.put("secret", mUserDetailDataObject.secret);
		
			JSONObject addressObj = new JSONObject();
			addressObj.put("county", null);
			addressObj.put("country", "USA");
			addressObj.put("houseNumber", mUserDetailDataObject.houseNumber);
			addressObj.put("street", mUserDetailDataObject.street);
			addressObj.put("city", mUserDetailDataObject.city);
			addressObj.put("state", mUserDetailDataObject.state);
			addressObj.put("postcodeZip", mUserDetailDataObject.postcodeZip);
			json.put("address", addressObj);
			
			JSONArray servicesArray = new JSONArray();
			JSONObject serviceObject = new JSONObject();
			serviceObject.put("service","cc");
			serviceObject.put("termsAgreed",true);
			serviceObject.put("status","enabled");
			serviceObject.put("extra",null);
			servicesArray.put(serviceObject);
			json.put("services", servicesArray);

			requestParms.body = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return requestParms;
	}

	/**
	 * set json request params
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	}

	/**
	 * get event type
	 */
	@Override
	public int getEventType() {
		return mReqEventType;
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

	/**
	 * @return the mUserDetailDataObject
	 */
	public SignUpUserDetailDataObject getUserDetailDataObject() {
		return mUserDetailDataObject;
	}

	/**
	 * @param mUserDetailDataObject the mUserDetailDataObject to set
	 */
	public void setUserDetailDataObject(SignUpUserDetailDataObject mUserDetailDataObject) {
		this.mUserDetailDataObject = mUserDetailDataObject;
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
	 * on failure response
	 * @param error
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onViewFailure(error, getEventType());	

	}

	/**
	 * on successful response
	 * @param jsonobject response
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
		mViewCallback.onViewSuccess(dataObject);

	}

	/**
	 * on successful response
	 * @param response data
	 * @param data object
	 */
	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub

	}

	/**
	 * on successful response
	 * @param response
	 * @param data object
	 */
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		mViewCallback.onViewSuccess(dataObject);

	}


}
