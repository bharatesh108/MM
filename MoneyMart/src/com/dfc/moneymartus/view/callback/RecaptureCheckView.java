package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;

public interface RecaptureCheckView {
	/**
	 * Used to send call back to UI on failure of recaptured download check image API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onRecaptureCheckFailure(VolleyError error, int eventType);

	/**
	 *Used to send call back to UI on success of recaptured download check image API call
	 * @param base64 Image data in base64 format
	 */
	public void onRecaptureSuccess(String base64);
	
	/**
	 * Used to send call back to UI on success of download check image API call
	 * @param responseData response status code
	 * @param base64Img Image data in base64 format
	 */
	public void onRecaptureSuccess(int responseData, String base64Img);
}
