package com.dfc.moneymartus.business;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.data.ModelData;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;

public class CustDetailDataObject extends DataObject implements ICommunicator {

	private static final long serialVersionUID = 1412358454529246313L;
	/*private String mUserName;
	private String mFirstName;
	private String mSecondName;
	private String mDOB;
	private String mSSN;
	private Object mPhone;
	private Object mPhone2;
	private Object mMobile;
	private String mEmail;
	private String mPassword;
	private Boolean mTermsAgreed;
	private String mHouseNumber;
	private String mStreet;
	private String mCity;
	private String mCounty;
	private String mState;
	private String mPostalCode;
	private String mCountry = "USA";
	private int mReqEventType;
*/
	private Object mUserName;
	private Object mFirstName;
	private Object mSecondName;
	private Object mDOB;
	private Object mSSN;
	private Object mPhone;
	private Object mPhone2;
	private Object mMobile;
	private Object mEmail;
	private Object mPassword;
	private Boolean mTermsAgreed;
	private Object mHouseNumber;
	private Object mStreet;
	private Object mCity;
	private Object mCounty;
	private Object mState;
	private Object mPostalCode;
	private Object mCountry = "USA";
	private int mReqEventType;

	private ArrayList<ServiceObject> mServiceList = new ArrayList<ServiceObject>();
	final IViewCallbackListner viewCallback;
	@SuppressWarnings("unused")
	private boolean isStateEnabled;

	public CustDetailDataObject(IViewCallbackListner viewCallback){
		this.viewCallback = viewCallback;
	}

	/**
	 * this is used to set json values
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		try {
			if (values != null) {
				mUserName = values.get("username");
				mFirstName = values.get("firstName");
				mSecondName = values.get("secondName");
				mDOB = values.get("dateOfBirth");
				mSSN = values.get("ssn");
				mPhone = values.get("phone");
				mPhone2 = values.get("phone2");
				mMobile = values.get("mobile");
				mEmail = values.get("email");
				mPassword = values.get("password");
				mTermsAgreed = values.optBoolean("termsAgreed");

				JSONObject addressJSON = values.getJSONObject("address");
				mHouseNumber = addressJSON.get("houseNumber");
				mStreet = addressJSON.get("street");
				mCity = addressJSON.get("city");
				mCounty = addressJSON.get("county");
				mState = addressJSON.get("state");
				
				mPostalCode = addressJSON.get("postcodeZip");
				//	 mCountry = addressJSON.getString("country");
				
				String states [] = Constants.getEnabledStates();
				System.out.println("mstate  is "+mState);
				ModelData.getInstance().isEnabled=false;
				for (String stateEnabled : states) {
					System.out.println("state is "+stateEnabled);
					if(stateEnabled.equalsIgnoreCase(mState.toString())){
						isStateEnabled = true;
						ModelData.getInstance().isEnabled=true;
						break;
						
					}
					
				}

				JSONObject embeddedJSONObj = values.getJSONObject("_embedded");
				JSONArray mServicesArray = embeddedJSONObj.optJSONArray("services");
				ServiceObject serviceObj;
				JSONObject serviceJSON;
				if (mServicesArray != null) {
					for (int i = 0; i < mServicesArray.length(); i++) {
						serviceJSON =(JSONObject) mServicesArray.get(i);
						serviceObj = new ServiceObject();
						serviceObj.setJSONValues(serviceJSON);
						this.mServiceList.add(serviceObj);
					}
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	
	@Override
	public void setJSONValues(String values) {
		//TODO Nothing
	}

	/**
	 * this is used to set input parameter for get customer request
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		requestParms.url = Constants.BASE_URL.concat(Constants.URL_GET_CUSTOMER);//MMConstants.URL_GET_CUSTOMER;
		HashMap<String, String>  parameters = new HashMap<String, String>();
		parameters.put("Authorization",Constants.authToken);
		parameters.put("DFC-ApiKey", Constants.API_KEY);
		parameters.put("Accept", "application/hal+json; charset=utf-8");
		requestParms.headers = parameters;
		return requestParms;	
	}

	/**
	 * Used to get EventType
	 * returns the mReqEventType
	 */
	@Override
	public int getEventType() {
		return mReqEventType;// MMEventTypes.EVENT_PERSONAL_DETAILS;
	}

	/**
	 * Used to get the UserName
	 * @return the mUserName
	 */
	public Object getUserName() {
		return mUserName;
	}

	/**
	 * Used to set Username
	 * @param mUserName the mUserName to set
	 */
	public void setUserName(Object mUserName) {
		this.mUserName = mUserName;
	}

	/**
	 * 
	 * @return the mFirstName
	 */
	public Object getFirstName() {
		return mFirstName;
	}

	/**
	 * Used to set the FirstName
	 * @param mFirstName the mFirstName to set
	 */
	public void setFirstName(Object mFirstName) {
		this.mFirstName = mFirstName;
	}

	/**
	 * Used to get the SecondName
	 * @return the mSecondName
	 */
	public Object getSecondName() {
		return mSecondName;
	}

	/**
	 * Used to set the SecondName
	 * @param mSecondName the mSecondName to set
	 */
	public void setSecondName(Object mSecondName) {
		this.mSecondName = mSecondName;
	}

	/**
	 * Used to get the DOB
	 * @return the mDOB
	 */
	public Object getDOB() {
		return mDOB;
	}

	/**
	 * Used to set the DOB
	 * @param mDOB the mDOB to set
	 */
	public void setDOB(Object mDOB) {
		this.mDOB = mDOB;
	}

	/**
	 * Used to get the SSN
	 * @return the mSSN
	 */
	public Object getSSN() {
		return mSSN;
	}

	/**
	 * Used to set the SSN
	 * @param mSSN the mSSN to set
	 */
	public void setSSN(Object mSSN) {
		this.mSSN = mSSN;
	}

	/**
	 * Used to get the Phone
	 * @return the mPhone
	 */
	public Object getPhone() {
		return mPhone;
	}

	/**
	 * @param mPhone the mPhone to set
	 */
	public void setPhone(Object mPhone) {
		this.mPhone = mPhone;
	}

	/**
	 * Used to get the Phone2
	 * @return the mPhone2
	 */
	public Object getPhone2() {
		return mPhone2;
	}

	/**
	 * Used to set the Phone2
	 * @param mPhone2 the mPhone2 to set
	 */
	public void setPhone2(Object mPhone2) {
		this.mPhone2 = mPhone2;
	}

	/**
	 * Used to get the Mobile
	 * @return the mMobile
	 */
	public Object getMobile() {
		return mMobile;
	}

	/**
	 * @param mMobile the mMobile to set
	 */
	public void setMobile(Object mMobile) {
		this.mMobile = mMobile;
	}

	/**
	 * Used to get the Email
	 * @return the mEmail
	 */
	public Object getEmail() {
		return mEmail;
	}

	/**
	 * Used to set the Email
	 * @param mEmail the mEmail to set
	 */
	public void setEmail(Object mEmail) {
		this.mEmail = mEmail;
	}

	/**
	 * Used to set the Password
	 * @return the mPassword
	 */
	public Object getPassword() {
		return mPassword;
	}

	/**
	 * Used to set the Password
	 * @param mPassword the mPassword to set
	 */
	public void setPassword(Object mPassword) {
		this.mPassword = mPassword;
	}

	/**
	 * Used to get the TermsAgreed
	 * @return the mTermsAgreed
	 */
	public Boolean getTermsAgreed() {
		//mTermsAgreed = false;
		return mTermsAgreed;
	}

	/**
	 * Used to set the TermsAgreed
	 * @param mTermsAgreed the mTermsAgreed to set
	 */
	public void setTermsAgreed(Boolean mTermsAgreed) {
		this.mTermsAgreed = mTermsAgreed;
	}

	/**
	 * Used to get the HouseNumber
	 * @return the mHouseNumber
	 */
	public Object getHouseNumber() {
		return mHouseNumber;
	}

	/**
	 * @param mHouseNumber the mHouseNumber to set
	 */
	public void setHouseNumber(Object mHouseNumber) {
		this.mHouseNumber = mHouseNumber;
	}

	/**
	 * Used to get the Street
	 * @return the mStreet
	 */
	public Object getStreet() {
		return mStreet;
	}

	/**
	 * Used to set the Street
	 * @param mStreet the mStreet to set
	 */
	public void setStreet(Object mStreet) {
		this.mStreet = mStreet;
	}

	/**
	 * Used to get the City
	 * @return the mCity
	 */
	public Object getCity() {
		return mCity;
	}

	/**
	 * Used to set the City
	 * @param mCity the mCity to set
	 */
	public void setCity(Object mCity) {
		this.mCity = mCity;
	}

	/**
	 * Used to get the County
	 * @return the mCounty
	 */
	public Object getCounty() {
		return mCounty;
	}

	/**
	 * Used to set the County
	 * @param mCounty the mCounty to set
	 */
	public void setCounty(Object mCounty) {
		this.mCounty = mCounty;
	}

	/**
	 * Used to get the State
	 * @return the mState
	 */
	public Object getState() {
		return mState;
	}

	/**
	 * Used to set the State
	 * @param mState the mState to set
	 */
	public void setState(Object mState) {
		this.mState = mState;
	}

	/**
	 * Used to get the PostalCode
	 * @return the mPostalCode
	 */
	public Object getPostalCode() {
		return mPostalCode;
	}

	/**
	 * Used to set the PostalCode
	 * @param mPostalCode the mPostalCode to set
	 */
	public void setPostalCode(Object mPostalCode) {
		this.mPostalCode = mPostalCode;
	}

	/**
	 * Used to get the Country
	 * @return the mCountry
	 */
	public Object getCountry() {
		return mCountry;
	}

	/**
	 * Used to set the Country
	 * @param mCountry the mCountry to set
	 */
	public void setmCountry(Object mCountry) {
		this.mCountry = mCountry;
	}

	/**
	 * Used to get the ServiceList
	 * @return the mServiceList
	 */
	public ArrayList<ServiceObject> getServiceList() {
		return mServiceList;
	}

	/**
	 * Used to set the ServiceList
	 * @param mServiceList the mServiceList to set
	 */
	public void setServiceList(ArrayList<ServiceObject> mServiceList) {
		this.mServiceList = mServiceList;
	}

	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseFailure(VolleyError error) {
		viewCallback.onViewFailure(error, getEventType());	
	}

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
		switch (dataObject.getEventType()) {
		case EventTypes.EVENT_LOGIN_PERSONAL_DETAILS:
			if (getTermsAgreed() || (getServiceList()!= null && !getServiceList().isEmpty() && ((ServiceObject)getServiceList().get(0)).getTermsAgreed()))
			{
				viewCallback.onViewSuccess(dataObject);
			}
			else
			{
				viewCallback.onViewSuccess(dataObject);
			}
			break;

		case EventTypes.EVENT_PERSONAL_DETAILS:
			viewCallback.onViewSuccess(dataObject);
			break;

		default:
			break;
		}
	}

	/**
	 * Used to get the EventType
	 * @return the mEventType
	 */
	public int getReqEventType() {
		return mReqEventType;
	}

	/**
	 * Used to set the EventType
	 * @param mEventType the mEventType to set
	 */
	public void setReqEventType(int reqEventType) {
		this.mReqEventType = reqEventType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJsonRequestParms()
	 */
	@Override
	public JSONObject setJsonRequestParms() {
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

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(java.lang.String, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub

	}
}