package com.dfc.moneymartus.dto;

import org.json.JSONObject;

public class FundingMechanismPostParserObject {
	public Object mID;
	public Object mType;
	public Object mBankName;
	public Object mAccountNumber;
	public Object mRoutingNumber;
	
	public FundingMechanismPostParserObject(JSONObject values) {
		if(values != null) {
			setID(values.opt("id"));
			setType(values.opt("type"));
			setBankName(values.opt("bankName"));
			setAccountNumber(values.opt("accountNumber"));
			setRoutingNumber(values.opt("routingNumber"));
		}
	}
	

	/**
	 * @return the mID
	 */
	public Object getID() {
		return mID;
	}

	/**
	 * @param mID the mID to set
	 */
	public void setID(Object mID) {
		this.mID = mID;
	}

	/**
	 * @return the mType
	 */
	public Object getType() {
		return mType;
	}

	/**
	 * @param mType the mType to set
	 */
	public void setType(Object mType) {
		this.mType = mType;
	}

	/**
	 * @return the mBankName
	 */
	public Object getBankName() {
		return mBankName;
	}

	/**
	 * @param mBankName the mBankName to set
	 */
	public void setBankName(Object mBankName) {
		this.mBankName = mBankName;
	}

	/**
	 * @return the mAccountNumber
	 */
	public Object getAccountNumber() {
		return mAccountNumber;
	}

	/**
	 * @param mAccountNumber the mAccountNumber to set
	 */
	public void setAccountNumber(Object mAccountNumber) {
		this.mAccountNumber = mAccountNumber;
	}

	/**
	 * @return the mRoutingNumber
	 */
	public Object getRoutingNumber() {
		return mRoutingNumber;
	}

	/**
	 * @param mRoutingNumber the mRoutingNumber to set
	 */
	public void setRoutingNumber(Object mRoutingNumber) {
		this.mRoutingNumber = mRoutingNumber;
	}


}
