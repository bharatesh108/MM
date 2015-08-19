package com.dfc.moneymartus.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dfc.moneymartus.infra.Util;

public class CheckDataObject {

	private ArrayList<ChecksObject> mAlCheckObj;
	public ArrayList<ChecksObject> alPendingList = new ArrayList<ChecksObject>();
	public ArrayList<ChecksObject> alCashingList = new ArrayList<ChecksObject>();
	public ArrayList<ChecksObject> alRejectedList = new ArrayList<ChecksObject>();
	public ArrayList<ChecksObject> alUarList = new ArrayList<ChecksObject>();

	private String mMessage;

	/*
	 * Used to parse json data response
	 */
	public void parseData(JSONObject values) {
		if (values != null) {

			JSONArray jsonArrayChecks = values.optJSONArray("cheques");
			//JSONObject jsonMessage = values.optJSONObject("message");
			this.setMessage(values.optString("message"));
			try {
				if (jsonArrayChecks != null) {
					
					mAlCheckObj = new ArrayList<ChecksObject>();
					
					for (int j = 0; j < jsonArrayChecks.length(); j++) {
						JSONObject checksJSON = (JSONObject) jsonArrayChecks.get(j);
						ChecksObject checksObj = new ChecksObject();
						checksObj.setJSONValues(checksJSON);
						if (Integer.parseInt(checksObj.getStatusCode()) == 02)
						{
							this.alUarList.add(checksObj);
						}
						else {
							this.mAlCheckObj.add(checksObj);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Comparator for sorting the list by data made
	 */
	public Comparator<ChecksObject> dateComparator = new Comparator<ChecksObject>() {

		@SuppressWarnings("unused")
		public int compare(ChecksObject s1, ChecksObject s2) {
			//descending order
			int val = 0;
			Date date1 = Util.parseDate(s1.getDateMade());
			Date date2 = Util.parseDate(s2.getDateMade());
			if (date1 == null || date2 == null) {
				return 0;
			}
			else
			{
				return date2.compareTo(date1);
			}
		}};
		
		/**
		 * Used to get sorted check list
		 * @return
		 */
		public ArrayList<ChecksObject> getAlCheckObj() {
			return mAlCheckObj;
		}

		/**
		 * Used to set sorted check list
		 * @param mAlCheckObj
		 */
		public void setAlCheckObj(ArrayList<ChecksObject> mAlCheckObj) {
			this.mAlCheckObj = mAlCheckObj;
		}

		/**
		 * Used to get message
		 * @return message
		 */
		public String getMessage() {
			return mMessage;
		}

		/**
		 * Used to set message
		 * @param message Message data
		 */
		public void setMessage(String message) {
			this.mMessage = message;
		}

		/**
		 * Used to get pending checks list
		 * @return List of pending checks
		 */
		public ArrayList<ChecksObject> getPendingList() {
			return alPendingList;
		}

		/**
		 * Used to set pending check list in array
		 * @param mArrListPendingList array of pending checks
		 */
		public void setPendingList(ArrayList<ChecksObject> mArrListPendingList) {
			this.alPendingList = mArrListPendingList;
		}

		/**
		 * Used to get cashed check list
		 * @return List of cashed check
		 */
		public ArrayList<ChecksObject> getCashingList() {
			return alCashingList;
		}

		/**
		 * Used to set list of cashed list
		 * @param mArrListCashingList array of cashed checks
		 */
		public void setCashingList(ArrayList<ChecksObject> mArrListCashingList) {
			this.alCashingList = mArrListCashingList;
		}

		/**
		 * Used to get list of rejected checks
		 * @return the mArrListRejectedList array of rejected checks
		 */
		public ArrayList<ChecksObject> getRejectedList() {
			return alRejectedList;
		}

		/**
		 * Used to set rejected check list
		 * @param mArrListRejectedList Array of rejected checks
		 */
		public void setRejectedList(ArrayList<ChecksObject> mArrListRejectedList) {
			this.alRejectedList = mArrListRejectedList;
		}

		/**
		 * Used to get user action required check list
		 * @return List of UAR checks
		 */
		public ArrayList<ChecksObject> getUarList() {
			return alUarList;
		}

		/**
		 * Used to set UAR checks list 
		 * @param mAlUarList Array of UAR checks
		 */
		public void setUarList(ArrayList<ChecksObject> mAlUarList) {
			this.alUarList = mAlUarList;
		}
}
