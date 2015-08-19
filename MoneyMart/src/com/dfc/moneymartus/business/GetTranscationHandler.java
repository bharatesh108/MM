package com.dfc.moneymartus.business;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.TransactionDataObject;
import com.dfc.moneymartus.gateway.CommuncationHelper;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.RequestInput;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.CardDetailView;
import com.google.gson.Gson;

public class GetTranscationHandler extends DataObject implements ICommunicator{

	private static final long serialVersionUID = 1L;

	CardDetailView mViewCallback;
	private String mUrl;
	private ArrayList<String> mCardNumber = new ArrayList<String>();
	
	private int mReqEventType;
	
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

	public GetTranscationHandler(CardDetailView viewCallback){
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
		parameters.put("Accept", Constants.ACCEPT_JSON);
		requestParms.headers = parameters;
		return requestParms;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.business.DataObject#setJSONValues(org.json.JSONObject)
	 */
	@Override
	public void setJSONValues(JSONObject values) {
		//TODO nothing
	}

	/**
	 * Used to get EventType
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
		Gson gson = new Gson();
		FundingMechanismDataObject fundingMechanismObject = gson.fromJson(response, FundingMechanismDataObject.class);
		mViewCallback.onGetFundingMechanismSuccess(fundingMechanismObject);
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
		mViewCallback.onGetFundingMechanismFailure(error, getEventType());
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
	 * Used to execut request for getting Funding Mechanism
	 */
	public void getfundingMechanisms() {
		CommuncationHelper helper= new CommuncationHelper();
		helper.executeGet(this, (ICommunicator) this);
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
		// TODO Auto-generated method stub
	}

	/**
	 * Used to set Transaction Json Values into TransactionDataObject
	 * @param response
	 */
	public void setTranscationJSONValues(String response) {
		System.out.println("Response is "+response);
		Gson gson = new Gson();
		TransactionDataObject transactionObject = gson.fromJson(response, TransactionDataObject.class);
		mViewCallback.onGetTrancationSuccess(transactionObject.transactions);
		
	}
}
