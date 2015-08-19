package com.dfc.moneymartus.dto;

public class MyChecksListDataObject {
	private String mStrChequeId;
	private String mStrStatusCode;
	private String mStrStatusDescription;
	private String mStrDdateSubmitted;
	private String mStrDateCompleted;
	private int mIntTransactionId;
	private String mStrChequeMaker;
	private int mIntChequeNumber;
	private int mIntAmount;
	private String mStrAmountCurrency;
	private String mStrDateMade;
	
	
	/**
	 * Used to get check id
	 * @return check id
	 */
	public String getChequeId() {
		return mStrChequeId;
	}
	/**
	 * Used to set check id
	 * @param mStrChequeId check id
	 */
	public void setChequeId(String mStrChequeId) {
		this.mStrChequeId = mStrChequeId;
	}
	/**
	 * Used to get check status
	 * @return check status
	 */
	public String getStatusCode() {
		return mStrStatusCode;
	}
	/**
	 * Used to set check status
	 * @param mStrStatusCode check status
	 */
	public void setStatusCode(String mStrStatusCode) {
		this.mStrStatusCode = mStrStatusCode;
	}
	/**
	 * Used to get check status description
	 * @return check status description
	 */
	public String getStatusDescription() {
		return mStrStatusDescription;
	}
	/**
	 * Used to set check status description
	 * @param mStrStatusDescription check status description
	 */
	public void setStatusDescription(String mStrStatusDescription) {
		this.mStrStatusDescription = mStrStatusDescription;
	}
	/** 
	 * Used to get check submitted date
	 * @return check submitted date
	 */
	public String getDdateSubmitted() {
		return mStrDdateSubmitted;
	}
	/**
	 * Used to set check submitted date
	 * @param mStrDdateSubmitted check submitted date
	 */
	public void setDdateSubmitted(String mStrDdateSubmitted) {
		this.mStrDdateSubmitted = mStrDdateSubmitted;
	}
	/**
	 * USed to get check date completed 
	 * @return check date completed 
	 */
	public String getDateCompleted() {
		return mStrDateCompleted;
	}
	/**
	 * Used to set check date completed 
	 * @param mStrDateCompleted check date completed 
	 */
	public void setDateCompleted(String mStrDateCompleted) {
		this.mStrDateCompleted = mStrDateCompleted;
	}
	/**
	 * USed to get transaction id
	 * @return transaction id
	 */
	public int getTransactionId() {
		return mIntTransactionId;
	}
	/**
	 * Used to set transaction id
	 * @param mIntTransactionId transaction id
	 */
	public void setTransactionId(int mIntTransactionId) {
		this.mIntTransactionId = mIntTransactionId;
	}
	/**
	 * Used to get  check maker name
	 * @return  check maker name
	 */
	public String getChequeMaker() {
		return mStrChequeMaker;
	}
	/**
	 * Used to set check maker name
	 * @param mStrChequeMaker check maker name
	 */
	public void setChequeMaker(String mStrChequeMaker) {
		this.mStrChequeMaker = mStrChequeMaker;
	}
	/**
	 * Used to get check number
	 * @return check number
	 */
	public int getChequeNumber() {
		return mIntChequeNumber;
	}
	/**
	 * Used to set check number
	 * @param mIntChequeNumber check number
	 */
	public void setChequeNumber(int mIntChequeNumber) {
		this.mIntChequeNumber = mIntChequeNumber;
	}
	/**
	 * Used to get get check amount
	 * @return get check amount
	 */
	public int getAmount() {
		return mIntAmount;
	}
	/**
	 * Used to get check amount
	 * @param mIntAmount get check amount
	 */
	public void setAmount(int mIntAmount) {
		this.mIntAmount = mIntAmount;
	}
	/**
	 * Used to get amount currency type
	 * @return  amount currency type
	 */
	public String getAmountCurrency() {
		return mStrAmountCurrency;
	}
	/**
	 * Used to set amount currency type
	 * @param mStrAmountCurrency amount currency type
	 */
	public void setAmountCurrency(String mStrAmountCurrency) {
		this.mStrAmountCurrency = mStrAmountCurrency;
	}
	/**
	 * Used to get check made date
	 * @return check made date
	 */
	public String getDateMade() {
		return mStrDateMade;
	}
	/**
	 * Used to set check made date
	 * @param mStrDateMade check made date
	 */
	public void setDateMade(String mStrDateMade) {
		this.mStrDateMade = mStrDateMade;
	}
}
