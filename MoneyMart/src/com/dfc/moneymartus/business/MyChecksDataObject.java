package com.dfc.moneymartus.business;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.gateway.ICommunicator;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.gateway.RequestInput;

public class MyChecksDataObject extends DataObject  implements ICommunicator {

	private static final long serialVersionUID = 1412358454529246313L;
	private String mCheckMakerName;
	private String mCheckMadeDate;
	private String mFrontCheckImage;
	private String mBackCheckImage;
	private String mCheckStatus;
	private Double mCheckAmount;
	private String mFundingMechanismsId;

	private ArrayList<Object> mServiceList = new ArrayList<Object>();
	private IViewCallbackListner viewCallback;

	public MyChecksDataObject() {
		super();
	}

	
	public MyChecksDataObject(String mCheckMakerName, String mCheckMadeDate,
			String mFrontCheckImage, String mBackCheckImage,
			String mCheckStatus, Double mCheckAmount) {
		super();
		this.mCheckMakerName = mCheckMakerName;
		this.mCheckMadeDate = mCheckMadeDate;
		this.mFrontCheckImage = mFrontCheckImage;
		this.mBackCheckImage = mBackCheckImage;
		this.mCheckStatus = mCheckStatus;
		this.mCheckAmount = mCheckAmount;
	}


	public MyChecksDataObject(IViewCallbackListner viewCallback){
		this.viewCallback = viewCallback;
	}
	
	public String getmFundingMechanismsId() {
		return mFundingMechanismsId;
	}


	public void setmFundingMechanismsId(String mFundingMechanismsId) {
		this.mFundingMechanismsId = mFundingMechanismsId;
	}
	
	@Override
	public void setJSONValues(JSONObject values) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setJSONValues(String values) {
		//TODO Nothing
	}

	@Override
	public RequestInput setRequestParms() {
		return null;
	}

	@Override
	public int getEventType() {
		return 0;// MMEventTypes.EVENT_PERSONAL_DETAILS;
	}

	/**
	 * Used to get the ServiceList
	 * @return the mServiceList
	 */
	public ArrayList<Object> getServiceList() {
		return mServiceList;
	}

	/**
	 * Used to set the ServiceList
	 * @param mServiceList the mServiceList to set
	 */
	public void setServiceList(ArrayList<Object> mServiceList) {
		this.mServiceList = mServiceList;
	}

	/**
	 * Used to get the CheckMakerName
	 * @return the mCheckMakerName
	 */
	public String getmCheckMakerName() {
		return mCheckMakerName;
	}

	/**
	 * Used to set the CheckMakerName
	 * @param mCheckMakerName the mCheckMakerName to set
	 */
	public void setmCheckMakerName(String mCheckMakerName) {
		this.mCheckMakerName = mCheckMakerName;
	}

	/**
	 * Used to get the CheckMadeDate
	 * @return the mCheckMadeDate
	 */
	public String getmCheckMadeDate() {
		return mCheckMadeDate;
	}

	/**
	 * Used to set the CheckMadeDate
	 * @param mCheckMadeDate the mCheckMadeDate to set
	 */
	public void setmCheckMadeDate(String mCheckMadeDate) {
		this.mCheckMadeDate = mCheckMadeDate;
	}

	/**
	 * Used to get the FrontCheckImage
	 * @return the mFrontCheckImage
	 */
	public String getmFrontCheckImage() {
		return mFrontCheckImage;
	}

	/**
	 * Used to set the FronCheckImage
	 * @param mFrontCheckImage the mFrontCheckImage to set
	 */
	public void setmFrontCheckImage(String mFrontCheckImage) {
		this.mFrontCheckImage = mFrontCheckImage;
	}

	/**
	 * Used to get the BackCheckImage
	 * @return the mBackCheckImage
	 */
	public String getmBackCheckImage() {
		return mBackCheckImage;
	}

	/**
	 * Used to set the BackCheckImage
	 * @param mBackCheckImage the mBackCheckImage to set
	 */
	public void setmBackCheckImage(String mBackCheckImage) {
		this.mBackCheckImage = mBackCheckImage;
	}

	/**
	 * Used to get the CheckStatus
	 * @return the mCheckStatus
	 */
	public String getmCheckStatus() {
		return mCheckStatus;
	}

	/**
	 * Used to set the CheckStatus
	 * @param mCheckStatus the mCheckStatus to set
	 */
	public void setmCheckStatus(String mCheckStatus) {
		this.mCheckStatus = mCheckStatus;
	}

	/**
	 * Used to get the CheckAmount
	 * @return the mCheckAmount
	 */
	public Double getmCheckAmount() {
		return mCheckAmount;
	}

	/**
	 * Used to set the CheckAmount
	 * @param mCheckAmount the mCheckAmount to set
	 */
	public void setmCheckAmount(Double mCheckAmount) {
		this.mCheckAmount = mCheckAmount;
	}

	/**
	 * Used to get ServiceList
	 * @return the mServiceList
	 */
	public ArrayList<Object> getmServiceList() {
		return mServiceList;
	}

	/**
	 * @param mServiceList the mServiceList to set
	 */
	public void setmServiceList(ArrayList<Object> mServiceList) {
		this.mServiceList = mServiceList;
	}

	/**
	 * Used to getViewCallback
	 * @return the viewCallback
	 */
	public IViewCallbackListner getViewCallback() {
		return viewCallback;
	}

	/**
	 * Used to set the ViewCallback
	 * @param viewCallback the viewCallback to set
	 */
	public void setViewCallback(IViewCallbackListner viewCallback) {
		this.viewCallback = viewCallback;
	}

	/**
	 * Used to get the Serialversionuid
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void onResponseSuccess(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseFailure(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseSuccess(JSONObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseSuccess(JSONObject response, DataObject dataObject) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the mEventType
	 */
	public int getReqEventType() {
		return 0;
	}

	/**
	 * @param mEventType the mEventType to set
	 */
/*	public void setReqEventType(int reqEventType) {
		
	}*/


	@Override
	public void onResponseSuccess(int responseData, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public JSONObject setJsonRequestParms() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onResponseSuccess(String response, DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}
}

