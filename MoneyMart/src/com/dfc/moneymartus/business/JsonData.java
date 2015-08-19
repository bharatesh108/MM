package com.dfc.moneymartus.business;

import org.json.JSONObject;

public class JsonData {
	
	private JSONObject mJsonRes;
	private boolean isSucess;

	/**
	 * Used to get the JsonResponse on success
	 * @return JsonResponse
	 */
	public JSONObject getJsonResSucess() {
		return mJsonRes;
	}

	/**
	 * Used to set the JsonResponse on Success
	 * @param jsonRes
	 */
	public void setJsonResSucess(JSONObject jsonRes) {
		this.mJsonRes = jsonRes;
	}

	/**
	 * Used to get the JsonResponse on failure
	 * @return JsonResponse
	 */
	public JSONObject getJsonResFailure() {
		return mJsonRes;
	}

	/**
	 * Used to set the JsonResponse on failure
	 * @param jsonRes
	 */
	public void setJsonResFailure(JSONObject jsonRes) {
		this.mJsonRes = jsonRes;
	}

	/**
	 * Used to verify the response is success
	 * @return isSucess
	 */
	public boolean isSucessRes() {
		return isSucess;
	}

	/**
	 * Used to set the Success Response
	 * @param isSucess
	 */
	public void setSucessRes(boolean isSucess) {
		this.isSucess = isSucess;
	}
}
