package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.FaqDataObject.Faq;


public interface FaqView {
	/**
	 *  Used to send call back to UI on success of faq API call
	 * @param faqArray Array of faq's
	 */
	public void onGetFaqSuccess(Faq... faqArray);
	
	/**
	 *  Used to send call back to UI on failure of get checks API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onGetFaqFailure(VolleyError error, int eventType);
}
