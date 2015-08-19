package com.dfc.moneymartus.dto;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONObject;

import com.dfc.moneymartus.business.MMDataObject;
import com.dfc.moneymartus.gateway.RequestInput;

public class FundingMechanismDataObject {

	//V1 parser
	public FundingMechanisms[] fundingMechanisms;

	public class FundingMechanisms {

		public String id;
		public String type;
		public String prn;
		public String cardNumberEnding;
		public String balance;
		public String currency;
		public String statusCode;
		public String statusDescription;
		public String validFromDate;
		public Links _links;
	}

	public class Links {
		public Transaction transactions;

	}
	public class Transaction {
		public String href;

	}

	//V2 parser
	public class FundingMechanismsObject extends MMDataObject implements Serializable {
		
		public static final long serialVersionUID = 9047493129715907921L;
		public Object mID;
		public Object mType;
		public Object mBankName;
		public Object mAccountNumber;
		public Object mRoutingNumber;
		public Object mPrn;
		public Object mCardNumberEnding;
		public Object mBalance;
		public Object mCurrency;
		public Object mStatusCode;
		public Object mValidFromDate;
		public Object mStatusDescription;
		public HashMap<String, String> mHMFundTypeAndId = new HashMap<String, String>();

		@Override
		public void setJSONValues(JSONObject values) {
			if (values != null) {
				setID(values.opt("id"));
				setType(values.opt("type"));
				setBankName(values.opt("bankName"));
				setAccountNumber(values.opt("accountNumber"));
				setRoutingNumber(values.opt("routingNumber"));
				setPrn(values.opt("prn"));
				setCardNumberEnding(values.opt("cardNumberEnding"));
				setBalance(values.opt("balance"));
				setCurrency(values.opt("currency"));
				setStatusCode(values.opt("statusCode"));
				setStatusDescription(values.opt("statusDescription"));
				setValidFromDate(values.opt("validFromDate"));
				mHMFundTypeAndId.put(getType().toString(), getID().toString());
			}
		}

		@Override
		public RequestInput setRequestParms() {
			return null;
		}

		@Override
		public int setEventType() {
			return 0;
		}

		@Override
		public Boolean isSucessResponse(boolean isSucessRes) {
			return null;
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
			return mType.toString().substring(0, 1).toUpperCase() + mType.toString().substring(1, mType.toString().length());
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

		/**
		 * @return the mPrn
		 */
		public Object getPrn() {
			return mPrn;
		}

		/**
		 * @param mPrn the mPrn to set
		 */
		public void setPrn(Object mPrn) {
			this.mPrn = mPrn;
		}

		/**
		 * @return the mCardNumberEnding
		 */
		public Object getCardNumberEnding() {
			return mCardNumberEnding;
		}

		/**
		 * @param mCardNumberEnding the mCardNumberEnding to set
		 */
		public void setCardNumberEnding(Object mCardNumberEnding) {
			this.mCardNumberEnding = mCardNumberEnding;
		}

		/**
		 * @return the mBalance
		 */
		public Object getBalance() {
			return mBalance;
		}

		/**
		 * @param mBalance the mBalance to set
		 */
		public void setBalance(Object mBalance) {
			this.mBalance = mBalance;
		}

		/**
		 * @return the mCurrency
		 */
		public Object getCurrency() {
			return mCurrency;
		}

		/**
		 * @param mCurrency the mCurrency to set
		 */
		public void setCurrency(Object mCurrency) {
			this.mCurrency = mCurrency;
		}

		/**
		 * @return the mStatusCode
		 */
		public Object getStatusCode() {
			return mStatusCode;
		}

		/**
		 * @param mStatusCode the mStatusCode to set
		 */
		public void setStatusCode(Object mStatusCode) {
			this.mStatusCode = mStatusCode;
		}

		/**
		 * @return the mValidFromDate
		 */
		public Object getValidFromDate() {
			return mValidFromDate;
		}

		/**
		 * @param mValidFromDate the mValidFromDate to set
		 */
		public void setValidFromDate(Object mValidFromDate) {
			this.mValidFromDate = mValidFromDate;
		}

		/**
		 * @return the mStatusDescription
		 */
		public Object getStatusDescription() {
			return mStatusDescription;
		}

		/**
		 * @param mStatusDescription the mStatusDescription to set
		 */
		public void setStatusDescription(Object mStatusDescription) {
			this.mStatusDescription = mStatusDescription;
		}
		
		public HashMap<String, String> getFundTypeAndId() {
			return mHMFundTypeAndId;
		}

		public void setFundTypeAndId(HashMap<String, String> mHMFundTypeAndId) {
			this.mHMFundTypeAndId = mHMFundTypeAndId;
		}

	}

}
