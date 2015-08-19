package com.dfc.moneymartus.business;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.UserDetailDataObject;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.google.gson.Gson;

public class TermCondUpdateUserHandler extends DataObject implements ICommunicator {

	private static final long serialVersionUID = -8988016406906114808L;
	final IViewCallbackListner mViewCallback;
	private int mReqEventType;
	static final String TAG = "TermCondUpdateUserHandler";

	/**
	 * TermsAndCondUpdateUserhandler constructor
	 * @param viewCallback
	 */
	public TermCondUpdateUserHandler(IViewCallbackListner viewCallback) {
		// Log.e("CustDetailDataObject",":"+"constructor call");
		this.mViewCallback = viewCallback;
	}

	/**
	 * on successful response
	 * @param response
	 */
	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub
		mViewCallback.onViewSuccess();
	}

	/**
	 * on failure response
	 * @param error
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		//Log.e("UpdateCustometHandler failure",":"+ error.networkResponse.statusCode);
		mViewCallback.onViewFailure(error, getEventType());		
	}

	/**
	 * on successful response
	 * @param jsonobject response
	 */
	@Override
	public void onResponseSuccess(JSONObject response) {
		//ToDo
		
	}

	/**
	 * on successful response
	 * @param jsonobject response
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
		 mViewCallback.onViewSuccess(dataObject);
	}

	/**
	 * set json values
	 * @param jsonvalues
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		//ToDo
		// 	Gson gson = new Gson();
		//private UserDetailDataObject userDetailDatObject = new UserDetailDataObject();
	}

	/**
	 * set json values
	 * @param String values
	 */
	@Override
	public void setJSONValues(String values) {
		Gson gson = new Gson();
		//UserDetailDataObject challenge = gson.fromJson(values, UserDetailDataObject.class);
		 gson.fromJson(values, UserDetailDataObject.class);
	}

	/**
	 * set request params
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url =  Constants.BASE_URL.concat(Constants.URL_GET_CUSTOMER);//MMConstants.URL_GET_CUSTOMER;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
        parameters.put(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
        parameters.put(Constants.LOGIN_REQUEST_HEADER_ACCEPT, Constants.ACCEPT_HAL_JSON_UTF);
        parameters.put(Constants.CONTENT_TYPE, Constants.ACCEPT_JSON);//"multipart/form-data");
		requestParms.headers = parameters;
		
		try {
			JSONObject json = new JSONObject();
			json.put("username", Constants.userData.getUserName());
			json.put("firstName", Constants.userData.getFirstName());
			json.put("secondName", Constants.userData.getSecondName());
			json.put("dateOfBirth", Constants.userData.getDOB());
			json.put("ssn", Constants.userData.getSSN());
			
			json.put("phone", Constants.userData.getPhone());
			json.put("phone2", Constants.userData.getPhone2());
			json.put("mobile", Constants.userData.getMobile());
			json.put("email", Constants.userData.getEmail());
			json.put("password", null);
			json.put("termsAgreed", true);
			
			JSONObject addressObj = new JSONObject();
			addressObj.put("houseNumber", Constants.userData.getHouseNumber());
			addressObj.put("street", Constants.userData.getStreet());
			addressObj.put("city", Constants.userData.getCity());
			addressObj.put("county", null);
			addressObj.put("country", Constants.userData.getCountry());
			addressObj.put("state", Constants.userData.getState());
			addressObj.put("postcodeZip", Constants.userData.getPostalCode());
			json.put("address", addressObj);
			
			requestParms.body = json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	/*	String jsonRequest = Util.getJsonResource(R.raw.updatepassword_request, Moneymart.getContext());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.PHONE,Constants.userData.getPhone());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.CUST_USERNAME,Constants.userData.getUserName());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.FIRSTNAME,Constants.userData.getFirstName());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.SECONDNAME, Constants.userData.getSecondName());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.DATEOFBIRTH,Constants.userData.getDOB());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.SSN,Constants.userData.getSSN());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.HOUSENUMBER,Constants.userData.getHouseNumber());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.STREET,Constants.userData.getStreet());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.CITY,Constants.userData.getCity());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.COUNTRY,Constants.userData.getCountry());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.STATE,Constants.userData.getState());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.POSTCODEZIP,Constants.userData.getPostalCode());
//		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.PHONE,Constants.userData.getPhone());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.PHONE2,Constants.userData.getPhone2());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.MOBILE,Constants.userData.getMobile());
		jsonRequest = Util.sanitizedReplace(jsonRequest, Constants.EMAIL,Constants.userData.getEmail());
		Log.d("MoneyMart","Req body"+jsonRequest);
		requestParms.body = jsonRequest;*/
		
		return requestParms;	
	}

	/**
	 * set json request params
	 */
	@Override
	public JSONObject setJsonRequestParms() {
		// TODO Auto-generated method stub
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
	 * on successfult response
	 * @param response
	 * @param dataobject
	 */
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

}
