package com.dfc.moneymartus.gateway;

import org.json.JSONException;
import org.json.JSONObject;


import org.json.JSONStringer;

import com.android.volley.Response.Listener;
import com.dfc.moneymartus.business.CCServiceHandler;
import com.dfc.moneymartus.business.CCTermsDataObject;
import com.dfc.moneymartus.business.ChecksHandler;
import com.dfc.moneymartus.business.CoreServiceHandler;
import com.dfc.moneymartus.business.CoreTermsDataObject;
import com.dfc.moneymartus.business.CustDetailDataObject;
import com.dfc.moneymartus.business.DAOSecurityQuestions;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.FaqHandler;
import com.dfc.moneymartus.business.ForgotPasswordHandler;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.business.GetTranscationHandler;
import com.dfc.moneymartus.business.CheckByIdHandler;
import com.dfc.moneymartus.business.SignupPersonalDetailHandler;
import com.dfc.moneymartus.business.RegistrationDataObject;
import com.dfc.moneymartus.business.TermCondUpdateUserHandler;
import com.dfc.moneymartus.business.UpdatePswDataObject;
import com.dfc.moneymartus.infra.EventTypes;

@SuppressWarnings("rawtypes")
public class JsonResponseListener implements Listener {

	private final DataObject mEventObj;
	private final ICommunicator mCallback;
	private static final String TAG = "JsonResponseListener";

	public JsonResponseListener(DataObject eventObj, ICommunicator callback) {
		this.mEventObj = eventObj;
		this.mCallback = callback;
	}

	/*@Override
	public void onResponse(JSONObject response) {
		switch (eventObj.getEventType()) {
		case MMEventTypes.EVENT_REGISTRATION:
			 RegistrationDataObject regObj = (RegistrationDataObject) eventObj;
			 regObj.setJSONValues(response);
			callback.onResponseSuccess(response);
			break;

		case MMEventTypes.EVENT_PERSONAL_DETAILS:
		case MMEventTypes.EVENT_LOGIN_PERSONAL_DETAILS:
			Log.e("personal detail response", "success ........" + response);
			CustDetailDataObject perDetailObj = (CustDetailDataObject) eventObj;
			perDetailObj.setJSONValues(response);
			callback.onResponseSuccess(response, perDetailObj);
			break;

		case MMEventTypes.EVENT_CORE_TERMS:
			CoreTermsDataObject coreTermObj = (CoreTermsDataObject)eventObj;
			coreTermObj.setJSONValues(response);
			callback.onResponseSuccess(response, coreTermObj);
			break;
		case MMEventTypes.EVENT_CC_TERMS:
			CCTermsDataObject ccTermObj = (CCTermsDataObject)eventObj;
			ccTermObj.setJSONValues(response);
			callback.onResponseSuccess(response, ccTermObj);
			break;
		case MMEventTypes.FORGOT_PASSWORD:
			ForgotPasswordHandler forgotPassHandler = (ForgotPasswordHandler)eventObj;
			forgotPassHandler.setJSONValues(response.toString());
			callback.onResponseSuccess(response, forgotPassHandler);
			break;
		case MMEventTypes.UPDATE_PASSWORD:
			UpdatePswDataObject updatePswDao = (UpdatePswDataObject)eventObj;
			Log.e("UPDATE_PASSWORD response", "success ........" + response);
			break;
		default:
			break;
		}
	}*/

	@Override
	public void onResponse(Object responseData) {

		if(responseData instanceof JSONObject){
			JSONObject response  = (JSONObject) responseData;
			switch (mEventObj.getEventType()) {
			case EventTypes.EVENT_REGISTRATION:
				RegistrationDataObject regObj = (RegistrationDataObject) mEventObj;
				regObj.setJSONValues(response);
				mCallback.onResponseSuccess(response);
				break;

			case EventTypes.EVENT_PERSONAL_DETAILS:
			case EventTypes.EVENT_LOGIN_PERSONAL_DETAILS:
				CustDetailDataObject perDetailObj = (CustDetailDataObject) mEventObj;
				perDetailObj.setJSONValues(response);
				mCallback.onResponseSuccess(response, perDetailObj);
				break;

			case EventTypes.EVENT_CORE_TERMS:
				CoreTermsDataObject coreTermObj = (CoreTermsDataObject)mEventObj;
				coreTermObj.setJSONValues(response);
				mCallback.onResponseSuccess(response, coreTermObj);
				break;
			case EventTypes.EVENT_CC_TERMS:
				CCTermsDataObject ccTermObj = (CCTermsDataObject)mEventObj;
				ccTermObj.setJSONValues(response);
				mCallback.onResponseSuccess(response, ccTermObj);
				break;
			case EventTypes.EVENT_FORGOT_PASSWORD:
				ForgotPasswordHandler forgotPassHandler = (ForgotPasswordHandler)mEventObj;
				forgotPassHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, forgotPassHandler);
				break;
			case EventTypes.EVENT_TERMCOND_UPDATE_USER:
				TermCondUpdateUserHandler userHandler = (TermCondUpdateUserHandler)mEventObj;
				userHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, userHandler);
				break;
			case EventTypes.EVENT_CORE_SERVICE:
				CoreServiceHandler serviceHandler = (CoreServiceHandler)mEventObj;
				serviceHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response.toString());//, serviceHandler);
				break;
			case EventTypes.EVENT_GET_CHECKS:
				ChecksHandler checkHandler = (ChecksHandler)mEventObj; 
				checkHandler.setJSONValues(response);
				mCallback.onResponseSuccess(response, checkHandler);
				break;
			case EventTypes.FAQ:
			case EventTypes.FAQ_GENERAL:
			case EventTypes.FAQ_CC:
			case EventTypes.FAQ_MC:
				FaqHandler faqPassHandler = (FaqHandler)mEventObj;
				faqPassHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, faqPassHandler);
				break;
			case EventTypes.EVENT_SECURITY_QUESTIONS:
				DAOSecurityQuestions SecurityQuestionsHandler = (DAOSecurityQuestions)mEventObj;
				SecurityQuestionsHandler.setJSONValues(response.toString());
				break;
			case EventTypes.EVENT_SIGNUP_PERSONALDETAILS:
				SignupPersonalDetailHandler perDelObj = (SignupPersonalDetailHandler)mEventObj;
				perDelObj.setJSONValues(response);
				mCallback.onResponseSuccess(response, perDelObj);
				break;
			case EventTypes.EVENT_CHECK_BY_ID:
				CheckByIdHandler chkByIdHandler = (CheckByIdHandler)mEventObj;
				chkByIdHandler.setJSONValues(response);
				mCallback.onResponseSuccess(response, chkByIdHandler);
				break;
			case EventTypes.FUNDING_MECHANISM:
			case EventTypes.FUNDING_MECHANISM_DROPDOWN:
				GetFundingMechanismHandler fundMechHandler = (GetFundingMechanismHandler)mEventObj;
				fundMechHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, fundMechHandler);
				break;
			case EventTypes.FUNDING_MECHANISM_V2:
			case EventTypes.FUNDING_MECHANISM_POST_V2:
				GetFundingMechanismHandler fundMechHandlerV2 = (GetFundingMechanismHandler)mEventObj;
				fundMechHandlerV2.setJSONValues(response);
				mCallback.onResponseSuccess(response, fundMechHandlerV2);
				break;
			case EventTypes.FUNDING_MECHANISM_TRANSACTION:
				GetTranscationHandler transactionHandler = (GetTranscationHandler)mEventObj;
				transactionHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, transactionHandler);
				break;
			case EventTypes.FUNDING_MECHANISM_TRANSACTION_HISTORY:
				GetTranscationHandler transactionHistory = (GetTranscationHandler)mEventObj;
				transactionHistory.setTranscationJSONValues(response.toString());
				break;
			case EventTypes.EVENT_CC_SERVICE:
				CCServiceHandler ccServiceHandler = (CCServiceHandler)mEventObj;
				ccServiceHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response.toString());//, serviceHandler);
				break;
			default:
				break;
			}
		}
		else if(responseData instanceof Integer)
		{
			int response  = (Integer) responseData;
			switch (mEventObj.getEventType()) {
			case EventTypes.EVENT_UPDATE_PASSWORD:
				UpdatePswDataObject updatePswDao = (UpdatePswDataObject)mEventObj;
				//	updatePswDao.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, updatePswDao);
				break;

			case EventTypes.EVENT_TERMCOND_UPDATE_USER:
				TermCondUpdateUserHandler userHandler = (TermCondUpdateUserHandler)mEventObj;
				//userHandler.setJSONValues(response.toString());
				mCallback.onResponseSuccess(response, userHandler);
				break;
			default:
				break;
			}
		}
		else if (responseData instanceof String)
		{
			String response  = (String) responseData;
			switch (mEventObj.getEventType()) {
			case EventTypes.EVENT_SIGNUP_PERSONALDETAILS:
				SignupPersonalDetailHandler perDelObj = (SignupPersonalDetailHandler)mEventObj;
				perDelObj.setJSONValues(response);
				mCallback.onResponseSuccess(response, perDelObj);
				break;
			case EventTypes.FUNDING_MECHANISM_POST_V2:
				GetFundingMechanismHandler fundMechHandlerV2 = (GetFundingMechanismHandler)mEventObj;
			//	JSONObject.
				try {
					fundMechHandlerV2.setJSONValues(new JSONObject(response));
					mCallback.onResponseSuccess(response, fundMechHandlerV2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			default:
				break;
			}
		}
	}
}
