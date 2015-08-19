package com.dfc.moneymartus.view.callback;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.dto.SecurityQuestionDataObject.Questions;

public interface GetSecurityQuestionsView {

	/**
	 * Used to send call back to UI on failure of get security API call
	 * @param error Error message
	 * @param eventType Event type of API call
	 */
	public void onGetQuestionsFailure(VolleyError error, int eventType);
	/**
	 * Used to send call back to UI on success of get security API call
	 * @param questionsArray Array of security questions
	 */
	public void onGetQuestionsSuccess(Questions... questionsArray);
}
