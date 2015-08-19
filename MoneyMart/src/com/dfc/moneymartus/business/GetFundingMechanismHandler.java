package com.dfc.moneymartus.business;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.data.ModelData;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.FundingMechanismPostParserObject;
import com.dfc.moneymartus.dto.FundingMechanismDataObject.FundingMechanisms;
import com.dfc.moneymartus.dto.FundingMechanismDataObject.FundingMechanismsObject;
import com.dfc.moneymartus.dto.FundingMechanismPostDataObject;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.view.callback.FundingMechanismView;
import com.google.gson.Gson;

public class GetFundingMechanismHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;

	final FundingMechanismView mViewCallback;
	private String mUrl;
	private ArrayList<String> mCardNumber = new ArrayList<String>();
	private ArrayList<FundingMechanismsObject> mCardList = new ArrayList<FundingMechanismsObject>();
	private int mReqEventType;
	private FundingMechanismsObject mfundingMechanismObject;

	public FundingMechanismDataObject fundingMechanismObject;

	private FundingMechanismPostDataObject fundPostDataObj;

	private FundingMechanismPostParserObject fundMechPostDataObj;
	
	private HashMap<String, String> mHMFundTypeAndId = new HashMap<String, String>();
	
	/**
	 * Used to get the Url
	 * @return Url
	 */
	public String getUrl() {
		return mUrl;
	}

	/**
	 * Get card number
	 * @return array of card numbers
	 */
	public ArrayList<String> getCardNumber() {
		return mCardNumber;
	}

	/**
	 * Set card number
	 * @param mCardNumber
	 */
	public void setCardNumber(ArrayList<String> mCardNumber) {
		this.mCardNumber = mCardNumber;
	}

	/**
	 * Used to set the Url 
	 * @param url
	 */
	public void setUrl(String url) {
		this.mUrl = url;
	}

	public GetFundingMechanismHandler(FundingMechanismView viewCallback){
		this.mViewCallback = viewCallback;
	}

	public FundingMechanismsObject getMfundingMechanismObject() {
		return mfundingMechanismObject;
	}

	public void setMfundingMechanismObject(
			FundingMechanismsObject mfundingMechanismObject) {
		this.mfundingMechanismObject = mfundingMechanismObject;
	}
	
	public HashMap<String, String> getmHMFundTypeAndId() {
		return mHMFundTypeAndId;
	}

	public void setmHMFundTypeAndId(HashMap<String, String> mHMFundTypeAndId) {
		this.mHMFundTypeAndId = mHMFundTypeAndId;
	}
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setRequestParms()
	 */
	@Override
	public RequestInput setRequestParms() {
		RequestInput requestParms = new RequestInput();
		if(getEventType() == EventTypes.FUNDING_MECHANISM_POST_V2) {

			try {
				requestParms.url = Constants.URL_GET_FUNDING_MECHANISM;
				HashMap<String, String>  parameters = new HashMap<String, String>();
				parameters.put("Authorization",Constants.authToken);
				parameters.put("DFC-ApiKey", Constants.API_KEY);
				parameters.put("Accept", Constants.ACCEPT_APP_JSON);
				requestParms.headers = parameters;

				JSONObject json = new JSONObject();
				json.put("type", fundPostDataObj.type);
				json.put("accountNumber", fundPostDataObj.accountNumber);
				json.put("routingNumber", fundPostDataObj.routingNumber);
				requestParms.body = json.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			requestParms.url = Constants.URL_GET_FUNDING_MECHANISM;
			HashMap<String, String>  parameters = new HashMap<String, String>();
			parameters.put("Authorization",Constants.authToken);
			parameters.put("DFC-ApiKey", Constants.API_KEY);
			parameters.put("Accept", Constants.ACCEPT_APP_JSON);
			requestParms.headers = parameters;

		}
		return requestParms;
	}

	/**
	 * Used to execute the request for get funding mechanism
	 */
	public void getfundingMechanisms() {
		CommuncationHelper helper= new CommuncationHelper();
		helper.executeGet(this, (ICommunicator) this);
	}

	@Override
	public void setJSONValues(JSONObject values) {

		try {
			if (values != null) {
				if (getEventType() == EventTypes.FUNDING_MECHANISM_V2) {

					JSONArray mServicesArray = values.optJSONArray("fundingMechanisms");
					FundingMechanismsObject serviceObj;
					//ServiceObject serviceObj;
					JSONObject serviceJSON;
					FundingMechanismDataObject fundMecObj = new FundingMechanismDataObject();
					if (mServicesArray != null) {
						for (int i = 0; i < mServicesArray.length(); i++) {
							serviceJSON =(JSONObject) mServicesArray.get(i);
							serviceObj = fundMecObj.new FundingMechanismsObject();
							serviceObj.setJSONValues(serviceJSON);
							this.mCardList.add(serviceObj);
							mHMFundTypeAndId.put(serviceObj.getType().toString(), serviceObj.getID().toString());
						}
					}
				} else if (getEventType() == EventTypes.FUNDING_MECHANISM_POST_V2) {
					setFundMechPostDataObj(new  FundingMechanismPostParserObject(values));
//						setID(values.opt("id"));
//						setType(values.opt("type"));
//						setBankName(values.opt("bankName"));
//						setAccountNumber(values.opt("accountNumber"));
//						setRoutingNumber(values.opt("routingNumber"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}



	}

	/**
	 * Used to get the EventType
	 * @return EventType
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
		try {
			Gson gson = new Gson();
			fundingMechanismObject = gson.fromJson(response, FundingMechanismDataObject.class);
			FundingMechanisms [] fundingArray = fundingMechanismObject.fundingMechanisms;
			if (fundingArray != null && fundingArray.length > 0){
				for(int i = 0; i < fundingArray.length; i++){
					mCardNumber.add("****" + fundingArray[i].cardNumberEnding);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseFailure(com.android.volley.VolleyError)
	 */
	@Override
	public void onResponseFailure(VolleyError error) {
		mViewCallback.onGetFundingMechanismFailure(error, getEventType());
	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		//viewCallback.onGetFaqSuccess();

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicator#onResponseSuccess(org.json.JSONObject, com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		//viewCallback.onGetFaqSuccess();
		if (mReqEventType == EventTypes.FUNDING_MECHANISM_DROPDOWN){

			mViewCallback.onGetFundingMechanismSuccess(mCardNumber);

		} else if (mReqEventType == EventTypes.FUNDING_MECHANISM_V2 ||
				mReqEventType == EventTypes.FUNDING_MECHANISM_POST_V2){

			mViewCallback.onGetFundingMechanismSuccess((GetFundingMechanismHandler)dataObject);

		} else {
			mViewCallback.onGetFundingMechanismSuccess(fundingMechanismObject, mCardNumber);
		}
	}

	@Override
	public JSONObject setJsonRequestParms() {
		return null;
	}

	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		if(mReqEventType == EventTypes.FUNDING_MECHANISM_POST_V2){
			mViewCallback.onGetFundingMechanismSuccess((GetFundingMechanismHandler)dataObject);
		}
	}

	/**
	 * @return the mCardList
	 */
	public ArrayList<FundingMechanismsObject> getCardList() {
		return mCardList;
	}

	/**
	 * @param mCardList the mCardList to set
	 */
	public void setCardList(ArrayList<FundingMechanismsObject> mCardList) {
		this.mCardList = mCardList;
	}

	/**
	 * @return the fundPostDataObj
	 */
	public FundingMechanismPostDataObject getFundingMechanismPostDataObject() {
		return fundPostDataObj;
	}

	/**
	 * @param fundPostDataObj the fundPostDataObj to set
	 */
	public void setFundingMechanismPostDataObject(FundingMechanismPostDataObject fundPostDataObj) {
		this.fundPostDataObj = fundPostDataObj;
	}

	/**
	 * @return the fundMechPostDataObj
	 */
	public FundingMechanismPostParserObject getFundMechPostDataObj() {
		return fundMechPostDataObj;
	}

	/**
	 * @param fundMechPostDataObj the fundMechPostDataObj to set
	 */
	public void setFundMechPostDataObj(FundingMechanismPostParserObject fundMechPostDataObj) {
		this.fundMechPostDataObj = fundMechPostDataObj;
	}
}
