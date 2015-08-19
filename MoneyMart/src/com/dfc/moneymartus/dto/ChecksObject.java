package com.dfc.moneymartus.dto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dfc.moneymartus.dto.GetCheckDataObject.LinksCls;

public class ChecksObject {
	private String mChequeId;
	private String mStatusCode;
	private String mStatusDescription;
	private String mDateSubmitted;
	private String mDateCompleted;
	private int mTransactionId;
	private String mChequeMaker;
	private int mChequeNumber;
	private double mAmount;
	private String mAmountCurrency;
	private String mDateMade;
	private double mFees;
	private String mFeesCurrency;
	private String mFeesAgreed;
	private String mFundingMechanism;
//	private ArrayList<ActionRequiredCls> actionsRequired;
	private LinksCls _links;
	private int mQueuedActionId;
	private boolean isProgressUpdateRequired = true;
	private ArrayList<ActionObj> mActionRequired = new ArrayList<ActionObj>();
	private String mFrontImageUrl;
	private String mBackImageUrl;
	private String mVoidImageUrl;
	
	/**
	 * Used to set json data value
	 * @param values Object of JSON
	 */
	public void setJSONValues(JSONObject values) {
		
		/*Gson gson = new Gson();
		declineFeeObject = gson.fromJson(values.toString(), MMChecksObject.class);*/
		
		this.setChequeId(values.optString("chequeId"));
		this.setStatusCode(values.optString("statusCode"));
		this.setStatusDescription(values.optString("statusDescription"));
		this.setDateSubmitted(values.optString("dateSubmitted"));
		this.setDateCompleted(values.optString("dateCompleted"));
		this.setTransactionId(values.optInt("transactionId"));
		this.setChequeMaker(values.optString("chequeMaker"));
		this.setChequeNumber(values.optInt("chequeNumber"));
		this.setAmount(values.optDouble("amount"));
		this.setAmountCurrency(values.optString("amountCurrency"));
		this.setDateMade(values.optString("dateMade"));
		this.setFees(values.optDouble("fees"));
		this.setFeesCurrency(values.optString("feesCurrency"));
		this.setFeesAgreed(values.optString("feesAgreed"));
		this.setFundingMechanism(values.optString("fundingMechanism"));

		JSONArray jsonActionReq = values.optJSONArray("actionsRequired");
		ActionObj actionObj;
		JSONObject actionReqJSON;
		try {
			if (mActionRequired != null && jsonActionReq != null) {
				for (int j = 0; j < jsonActionReq.length(); j++) {
					actionReqJSON = (JSONObject) jsonActionReq.get(j);
					actionObj = new ActionObj();
					actionObj.setJSONValues(actionReqJSON);
					this.mActionRequired.add(actionObj);
				}
			}
		} catch (JSONException e) {
			Log.e("JSONException", e.toString());
		}
		
		try {
			JSONObject linksJSONObj = values.getJSONObject("_links");
			JSONObject frontImgJSONObj = linksJSONObj.getJSONObject("chequeImageFront");
			setFrontImageUrl(frontImgJSONObj.getString("href"));
			
			JSONObject backImgJSONObj = linksJSONObj.getJSONObject("chequeImageBack");
			setBackImageUrl(backImgJSONObj.getString("href"));
			
			JSONObject voidImgJSONObj = linksJSONObj.getJSONObject("chequeImageVoided");
			setVoidImageUrl(voidImgJSONObj.getString("href"));
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to get check id
	 * @return chequeId
	 */
	public String getChequeId() {
		return mChequeId;
	}

	/**
	 * Used to set check id
	 * @param chequeId check id
	 */
	public void setChequeId(String chequeId) {
		this.mChequeId = chequeId;
	}

	/**
	 * Used to set check status code
	 * @return check statusCode
	 */
	public String getStatusCode() {
		return mStatusCode;
	}

	/**
	 * Used to set status code
	 * @param statusCode check status code
	 */
	public void setStatusCode(String statusCode) {
		this.mStatusCode = statusCode;
	}

	/**
	 * Used to get check status description
	 * @return check status description
	 */
	public String getStatusDescription() {
		return mStatusDescription;
	}

	/**
	 * Used to set check status description
	 * @param statusDescription Check status description
	 */
	public void setStatusDescription(String statusDescription) {
		this.mStatusDescription = statusDescription;
	}

	/**
	 * Used to get check submitted date
	 * @return check submitted date
	 */
	public String getDateSubmitted() {
		return mDateSubmitted;
	}

	/**
	 * Used to set date submitted date
	 * @param dateSubmitted check submitted date
	 */
	public void setDateSubmitted(String dateSubmitted) {
		this.mDateSubmitted = dateSubmitted;
	}

	/**
	 * Used to get check date completed
	 * @return Check completed date
	 */
	public String getDateCompleted() {
		return mDateCompleted;
	}

	/**
	 * Used to set check completed date
	 * @param dateCompleted the dateCompleted to set
	 */
	public void setDateCompleted(String dateCompleted) {
		this.mDateCompleted = dateCompleted;
	}

	/**
	 * Used to set transaction id
	 * @return transaction id
	 */
	public int getTransactionId() {
		return mTransactionId;
	}

	/** 
	 * Used to set transaction id
	 * @param transactionId  id of transaction
	 */
	public void setTransactionId(int transactionId) {
		this.mTransactionId = transactionId;
	}

	/**
	 * Used to get check maker name
	 * @return check maker name
	 */
	public String getChequeMaker() {
		return mChequeMaker;
	}

	/**
	 * Used to set check maker name
	 * @param chequeMaker maker name
	 */
	public void setChequeMaker(String chequeMaker) {
		this.mChequeMaker = chequeMaker;
	}

	/**
	 * Used to get check number
	 * @return check number
	 */
	public int getChequeNumber() {
		return mChequeNumber;
	}

	/**
	 * Used to set check number
	 * @param chequeNumber check number
	 */
	public void setChequeNumber(int chequeNumber) {
		this.mChequeNumber = chequeNumber;
	}

	/**
	 * Used to get check amount
	 * @return check amount
	 */
	public double getAmount() {
		return mAmount;
	}

	/**
	 * Used to set check amount
	 * @param amount check amount
	 */
	public void setAmount(double amount) {
		this.mAmount = amount;
	}

	/** 
	 * Used to get check amount currency type
	 * @return check amount currency type
	 */
	public String getAmountCurrency() {
		return mAmountCurrency;
	}

	/**
	 * Used to set check amount currency type
	 * @param amountCurrency check amount currency type
	 */
	public void setAmountCurrency(String amountCurrency) {
		this.mAmountCurrency = amountCurrency;
	}

	/**
	 * Used to get check made date
	 * @return the dateMade check made date
	 */
	public String getDateMade() {
		return mDateMade;
	}

	/** 
	 * Used to set check made date
	 * @param dateMade check made date
	 */
	public void setDateMade(String dateMade) {
		this.mDateMade = dateMade;
	}

	/**
	 * Used to get check incash fees amount
	 * @return check incash fees amount
	 */
	public double getFees() {
		return mFees;
	}

	/**
	 * Used tp set check incash fees amount
	 * @param fees check incash fees amount
	 */
	public void setFees(double fees) {
		this.mFees = fees;
	}

	/**
	 * Used to get check incash fees amount currency type
	 * @return check incash fees amount currency type
	 */
	public String getFeesCurrency() {
		return mFeesCurrency;
	}

	/**
	 * Used to set check incash fees amount currency type
	 * @param feesCurrency check incash fees amount currency type
	 */
	public void setFeesCurrency(String feesCurrency) {
		this.mFeesCurrency = feesCurrency;
	}

	/**
	 * Used to get funding mechanism
	 * @return funding mechanism
	 */
	public String getFundingMechanism() {
		return mFundingMechanism;
	}

	/**
	 * Used to set  funding mechanism
	 * @param fundingMechanism  funding mechanism
	 */
	public void setFundingMechanism(String fundingMechanism) {
		this.mFundingMechanism = fundingMechanism;
	}

	/**
	 * Used to get fees agreed status
	 * @return fees agreed status
	 */
	public String getFeesAgreed() {
		return mFeesAgreed;
	}

	/**
	 * Used to set fees agreed status
	 * @param feesAgreed fees agreed status
	 */
	public void setFeesAgreed(String feesAgreed) {
		this.mFeesAgreed = feesAgreed;
	}

	/** 
	 * Used to get links
	 * @return the links
	 */
	public LinksCls get_links() {
		return _links;
	}

	/**
	 * Used to get UAR check status
	 * @return UAR check status
	 */
	public ArrayList<ActionObj> getActionRequired() {
		return mActionRequired;
	}

	/**
	 * Used to set UAR check status
	 * @param mActionRequired array of required check status 
	 */
	public void setActionRequired(ArrayList<ActionObj> mActionRequired) {
		this.mActionRequired = mActionRequired;
	}

	/**
	 * Used to get links
	 * param _links links
	 */
	public void set_links(LinksCls _links) {
		this._links = _links;
	}

	/**
	 * Used to get progress of uploading check status
	 * @return check update status
	 */
	public boolean isProgressUpdateRequired() {
		return isProgressUpdateRequired;
	}

	/**
	 * Used to set update flag for progress update
	 * @param isProgressUpdateRequired status of update progress 
	 */
	public void setProgressUpdateRequired(boolean isProgressUpdateRequired) {
		this.isProgressUpdateRequired = isProgressUpdateRequired;
	}

	/**
	 * Used to get action id
	 * @return queue id
	 */
	public int getQueuedActionId() {
		return mQueuedActionId;
	}

	/**
	 * set action id
	 * @param queuedActionId Queue id
	 */
	public void setQueuedActionId(int queuedActionId) {
		this.mQueuedActionId = queuedActionId;
	}
	
	/**
	 * Used to front check image url
	 * @return the frontImageUrl front check image url
	 */
	public String getFrontImageUrl() {
		return mFrontImageUrl;
	}

	/**
	 * Used to set front check image url
	 * @param frontImageUrl front check image url
	 */
	public void setFrontImageUrl(String frontImageUrl) {
		this.mFrontImageUrl = frontImageUrl;
	}

	/**
	 * Used to get back check image url
	 * @return the backImageUrl back check image url
	 */
	public String getBackImageUrl() {
		return mBackImageUrl;
	}

	/**
	 * Used to set back check image url
	 * @param backImageUrl back check image url
	 */
	public void setBackImageUrl(String backImageUrl) {
		this.mBackImageUrl = backImageUrl;
	}

	/**
	 * Used to get void check image url
	 * @return the voidImageUrl void check image url
	 */
	public String getVoidImageUrl() {
		return mVoidImageUrl;
	}

	/**
	 * Used to set void check image url
	 * @param voidImageUrl void check image url
	 */
	public void setVoidImageUrl(String voidImageUrl) {
		this.mVoidImageUrl = voidImageUrl;
	}

	public class ActionObj {

		private String mStrAction;

		/**
		 * Used to parse action
		 * @param values JSON object
		 */
		public void setJSONValues(JSONObject values) {
			this.setAction(values.optString("action"));

		}

		/**
		 * Used to set check action
		 * @param mStrAction check action
		 */
		private void setAction(String mStrAction) {
			this.mStrAction = mStrAction;

		}
		
		/**
		 * Used to get check action
		 * @return check action
		 */
		public String getAction() {

			return mStrAction;
		}
	}
}
