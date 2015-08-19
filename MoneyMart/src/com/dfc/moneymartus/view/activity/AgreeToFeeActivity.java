package com.dfc.moneymartus.view.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CheckByIdHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.FundingMechanismView;

@SuppressLint("InflateParams")
public class AgreeToFeeActivity extends BaseActivity implements IViewCallbackListner, View.OnClickListener, FundingMechanismView {

	@InjectView(R.id.tv_review_pending_custom_text)//visible
	TextView mTvHaderTitle;
	@InjectView(R.id.check_details_layout)//visible
	LinearLayout mLinLiayCheckDetails;//check_details_lay
	@InjectView(R.id.btn_action_required_submit)//
	Button mBtnAgree;
	//Check detail
	@InjectView(R.id.tv_check_details_amount)
	TextView mTvChkAmt;
	@InjectView(R.id.tv_funding_to)
	TextView mTvFunding;
	@InjectView(R.id.tv_date_submitted)
	TextView mTvDateSubmit;
	@InjectView(R.id.tv_date_transaction_completed)
	TextView mTvDateComplete;

	private CheckByIdHandler mChkByIdHandler;
	private final static String TAG = "MMAgreeToFeeActivity";
	//private MMCheckByIdHandler checkByIDHandler;
	//private MMCheckByIdHandler declineCheckByIDHandler; 
	private Preferences mPreferences;
	private String mFundingToValue;
	private String mDateSubmited;
	private String mDateCompleted;
	private String mCheckAmount;
	private String mFundingMechanismID;

	@Override
	protected void onCreate(Bundle savedInstance) {

		super.onCreate(savedInstance);
		mChkByIdHandler = new CheckByIdHandler(this);
		mChkByIdHandler.setRequestCheckID(getIntent().getExtras().getString("check_id"));
		setContentView(R.layout.activity_agree_to_fees);
		mPreferences = Preferences.getInstance(this);
		setUpActionBar();
		initUI();

		if(Util.isNetworkAvailable(this)) {

			processCheckByIDService();
		}
		else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}

	}

	/**
	 * this is used to process request for check by id service
	 */
	private void processCheckByIDService()
	{
		mChkByIdHandler.setReqEventType(EventTypes.EVENT_CHECK_BY_ID);
		processRequest(mChkByIdHandler);
	}

	/**
	 * this is used to show the dialog while  request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}
	
	/**
	 * this is used to process putrequest for check by id
	 * @param dataObject
	 */
	public void processPUTRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		String putURL = Constants.BASE_URL + Constants.URL_CHECK_BY_ID+ "/"+ mChkByIdHandler.getCheckObj().getChequeId();
		new SubmitDeclineFeeTask(putURL, mChkByIdHandler.getCheckObj(), prepareJson().toString()).execute();
	}

	/**
	 * initialize the UI components of Agree to free
	 */
	private void initUI() {
		ButterKnife.inject(this);
		mTvHaderTitle.setText(AgreeToFeeActivity.this.getResources().getString(R.string.fees_text, "", "", ""));
		mBtnAgree.setText(R.string.agree_btn);
		
		mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
				"", "", "" ));
		mTvFunding.setText(getResources().getString(R.string.funding_to, "-"));	
		mTvChkAmt.setText(getResources().getString(R.string.check_details_amount, "-"));
		mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, "-"));
		mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed, "-"));
	}

	/*
	 * this is used to initialize and set up the UI of action bar
	 */
	@SuppressLint("InflateParams")
	private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.navigation_agree_to_fees);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to  store checkbyid values into intent
	 */
	@OnClick(R.id.btn_action_required_submit)
	public void submitBtnClick()
	{
		Intent i = getIntent();
		i.putExtra(Constants.mActionRequiredStatus, Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT);
		//	Bundle bundle = new Bundle();
		i.putExtra(Constants.CHECK_ID, mChkByIdHandler.getRequestCheckID());
		i.putExtra(Constants.CHECK_MAKER_NAME, mChkByIdHandler.getCheckObj().getChequeMaker());
		i.putExtra(Constants.CHECK_MADE_DATE, mChkByIdHandler.getCheckObj().getDateMade());
		i.putExtra(Constants.CHECK_NUMBER, String.valueOf(mChkByIdHandler.getCheckObj().getChequeNumber()));
		i.putExtra(Constants.CHECK_AMOUNT, mCheckAmount);//mChkByIdHandler.getCheckObj().getAmount()+"");
		i.putExtra(Constants.CHECK_FUNDINGTO, mFundingMechanismID);
		i.putExtra(Constants.CHECK_FUNDINGTO_TYPE, mFundingToValue);//mChkByIdHandler.getCheckObj().getFundingMechanism());
		i.putExtra(Constants.CHECK_DATE_SUBMIT, mDateSubmited);//mChkByIdHandler.getCheckObj().getDateSubmitted());
		i.putExtra(Constants.CHECK_DATE_COMPLETE, mDateCompleted);//mChkByIdHandler.getCheckObj().getDateCompleted());
		i.putExtra(Constants.CHECK_FEES, mChkByIdHandler.getCheckObj().getFees()+"");
		i.putExtra(Constants.CHECK_AMOUNT_CURRENCY ,mChkByIdHandler.getCheckObj().getAmountCurrency());
		i.putExtra(Constants.CHECK_FEES_CURRENCY ,mChkByIdHandler.getCheckObj().getFeesCurrency());
		i.putExtra(Constants.CHECK_FRONT_IMAGE_URL ,mChkByIdHandler.getCheckObj().getFrontImageUrl());
		i.putExtra(Constants.CHECK_BACK_IMAGE_URL ,mChkByIdHandler.getCheckObj().getBackImageUrl());
		i.putExtra(Constants.CHECK_VOID_IMAGE_URL ,mChkByIdHandler.getCheckObj().getVoidImageUrl());
		
		
		setResult(RESULT_OK, i);
		finish();


	}

	/**
	 * this is used to show the alert dialog with the specified text on the click of home button
	 * @param v
	 */
	public void homeBtnClick(View v) {
		showAlertDialog("", getResources().getString(R.string.error_agree_fees), this, 0, getResources().getString(R.string.btn_fees_info), getResources().getString(R.string.btn_decline),Constants.DialogId.FEE_DECLINE);
	}

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		showAlertDialog("", getResources().getString(R.string.error_agree_fees), this, 0, getResources().getString(R.string.btn_fees_info), getResources().getString(R.string.btn_decline),Constants.DialogId.FEE_DECLINE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess()
	 */
	@Override
	public void onViewSuccess() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onViewFailure(VolleyError error, int eventType) {
		Log.e(TAG ,":"+ "onGetChecksFailure");
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
		/*int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
*/
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess(com.dfc.moneymartus.business.DataObject)
	 */
	@Override 
	public void onViewSuccess(DataObject dataObject) {
		//hideLoader();
		switch (dataObject.getEventType()) {
		case EventTypes.EVENT_CHECK_BY_ID:

			mChkByIdHandler = (CheckByIdHandler) dataObject;
			getFundingMechanism();
			refreshScreenData(mChkByIdHandler);
			break;
		
		case EventTypes.EVENT_DECLINE_FEE_PUT:

			System.out.println("Go to my checks and refresh list.");
			break;
		default:
			break;
		}
	}

	/**
	 * this is used to get the Funding Mechanism
	 */
	public void getFundingMechanism() {

		GetFundingMechanismHandler handler = new GetFundingMechanismHandler(this);
		handler.setReqEventType(EventTypes.FUNDING_MECHANISM_V2);
		handler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
		handler.getfundingMechanisms();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_right:
			getDialog().dismiss();
			// send request to cc/checks/checkID with fees agreed false
			if(mDialogId!=null && mDialogId.equals(getResources().getString(R.string.lbl_alert))){
				mDialogId = null;
				refreshListFromServer();
			}
			else{
				if (mDialogId == Constants.DialogId.FEE_DECLINE)
					processPUTRequest(mChkByIdHandler);
			}
			break;

		case R.id.btn_left:
			getDialog().dismiss();
			break;
		default :
			break;
		}
	}
	
	/**
	 * this is used to refresh the screen
	 * @param checkByIDHandler
	 */
	private void refreshScreenData(CheckByIdHandler checkByIDHandler ) {
		mChkByIdHandler = checkByIDHandler;
		mFundingMechanismID = mChkByIdHandler.getCheckObj().getFundingMechanism();
		String feeAmount = getFormattedAmount(checkByIDHandler.getCheckObj().getFees());
		String checkAmount = getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(checkByIDHandler.getCheckObj().getAmount() - checkByIDHandler.getCheckObj().getFees())));
		mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
				feeAmount, checkAmount, "" ));
				//checkByIDHandler.getCheckObj().getAmount(), "****1234"));
		mCheckAmount = String.valueOf(checkByIDHandler.getCheckObj().getAmount());
		mTvChkAmt.setText(getResources().getString(R.string.check_details_amount, "$"+checkAmount));
	//	mTvFunding.setText(getResources().getString(R.string.funding_to,mChkByIdHandler.getCheckObj().getFundingMechanism()));
	//	mTvDateSubmit.setText(getResources().getString(R.string.date_submitted,mChkByIdHandler.getCheckObj().getDateSubmitted()));
	//	mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed,mChkByIdHandler.getCheckObj().getDateCompleted()));
		
		if(mChkByIdHandler.getCheckObj().getDateSubmitted() == null || mChkByIdHandler.getCheckObj().getDateSubmitted().equalsIgnoreCase("null")) {
			mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, "-"));
			mDateSubmited = "-";
		} else {
			mDateSubmited = Util.getyyyymmddDate(mChkByIdHandler.getCheckObj().getDateSubmitted()); 
			mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, Util.getyyyymmddDate(mChkByIdHandler.getCheckObj().getDateSubmitted(), true)));
		}

		if(mChkByIdHandler.getCheckObj().getDateCompleted() == null || mChkByIdHandler.getCheckObj().getDateCompleted().equalsIgnoreCase("null")) {
			mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed, "-"));
			mDateCompleted = "-";
		} else {
			mDateCompleted = Util.getyyyymmddDate(mChkByIdHandler.getCheckObj().getDateCompleted());
			mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed,Util.getyyyymmddDate(mChkByIdHandler.getCheckObj().getDateCompleted(), true)));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {
		mTvFunding.setText(getResources().getString(R.string.funding_to, "-"));
		String feeAmount = getFormattedAmount(mChkByIdHandler.getCheckObj().getFees());
		String checkAmount = getFormattedAmount(mChkByIdHandler.getCheckObj().getAmount() - mChkByIdHandler.getCheckObj().getFees());
		mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
				feeAmount, checkAmount, "-"));
		mFundingToValue = "-";
		
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
		hideLoader();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismSuccess(com.dfc.moneymartus.dto.FundingMechanismDataObject, java.util.ArrayList)
	 */
	@Override
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject, ArrayList<String> mCardNumber) {
		if (fundingMechanismObject.fundingMechanisms == null) {
			mFundingToValue = "-";
			mTvFunding.setText(getResources().getString(R.string.funding_to,"-"));
			String feeAmount = getFormattedAmount(mChkByIdHandler.getCheckObj().getFees());
			String checkAmount = getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(mChkByIdHandler.getCheckObj().getAmount() - mChkByIdHandler.getCheckObj().getFees())));
			mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
					feeAmount, checkAmount, "-"));
		} else {
//			mTvFunding.setText(getResources().getString(R.string.funding_to,"Momentum Card ****"+fundingMechanismObject.fundingMechanisms[0].cardNumberEnding));
			mTvFunding.setText(getResources().getString(R.string.funding_to,"Momentum Card"));
			String feeAmount = getFormattedAmount(mChkByIdHandler.getCheckObj().getFees());
			String checkAmount = getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(mChkByIdHandler.getCheckObj().getAmount() - mChkByIdHandler.getCheckObj().getFees())));
			mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
					feeAmount, checkAmount, ""));
			mFundingToValue = fundingMechanismObject.fundingMechanisms[0].cardNumberEnding;
		}
		hideLoader();
	}
	
	/**
	 * this is used to submit the declined fee
	 * @param postUrl
	 * @param mCheckObject
	 * @param jsonPayload
	 * @return
	 * @throws Exception
	 */
	private int submitDeclineFee(String postUrl, ChecksObject mCheckObject, String jsonPayload) throws Exception{

		DefaultHttpClient mHttpClient = null;
		String boundary = "--DFC-WEBAPI-MULTIPART-BOUNDARY";
		String headerBoundary = "DFC-WEBAPI-MULTIPART-BOUNDARY";

		HttpResponse response = null;

		mHttpClient = new DefaultHttpClient();
		String jsonStr = jsonPayload; 
		System.out.println("JSON VALUE  " + jsonStr);


		String checkBoundary = boundary;
		String str2 = "Content-Disposition: form-date; name=\"cheque\"";
		String str3 = "Content-Type: application/json; charset=utf-8";

		String StrTotal = checkBoundary + "\r\n" + str2 + "\r\n" + str3 +"\r\n" + jsonStr + "\r\n" + checkBoundary + "--" ;

		HttpPut put = new HttpPut(postUrl);


		put.addHeader("Authorization", mPreferences.getString(PreferenceConstants.AUTORIZATION_TOKEN));
		put.addHeader(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		put.addHeader("Accept", "application/hal+json");
		put.addHeader("Content-Type","multipart/form-data");
		put.addHeader("Boundary", headerBoundary);

		StringEntity se = new StringEntity(StrTotal);
		se.setContentEncoding("UTF-8");
		put.setEntity(se);
		response = mHttpClient.execute(put);

		/* Checking response */
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("Http Execute finish " + statusCode + response);

//		As this method returns only status code the following code is not required		
//		HttpEntity entity = response.getEntity();
//
//		StringBuilder sb = new StringBuilder();
//		try {
//			BufferedReader reader = 
//					new BufferedReader(new InputStreamReader(entity.getContent()));
//			String line = null;
//
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//		}
//		catch (IOException e) { e.printStackTrace(); }
//		catch (Exception e) { e.printStackTrace(); }
//
//
//		System.out.println("finalResult " + sb.toString());

		return statusCode;

	}
	/**
	 * this is used to get the prepared json
	 * @return jsonObject
	 */
	public JSONObject prepareJson(){
		JSONObject jo = new JSONObject();
		ChecksObject mcheckObj = mChkByIdHandler.getCheckObj();
		try {
			jo.put("chequeId", mcheckObj.getChequeId());
			jo.put("chequeMaker", mcheckObj.getChequeMaker());
			jo.put("chequeNumber", mcheckObj.getChequeNumber());
			jo.put("amount", mcheckObj.getAmount());
			jo.put("amountCurrency", "USD");
			jo.put("dateMade", mcheckObj.getDateMade().substring(0, mcheckObj.getDateMade().indexOf(" ")));
			jo.put("fees", mcheckObj.getFees());
			jo.put("feesCurrency", "USD");
			jo.put("fundingMechanism", 1);
			jo.put("feesAgreed", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	private class SubmitDeclineFeeTask extends AsyncTask<String, Void, Integer> {

		private final String requestURL;
		private final String jsonData;
		private final ChecksObject mCheckObject;

		private int statusCode = 0;

		public SubmitDeclineFeeTask(String requestURL, ChecksObject mCheckObject,
				String jsonData){
			super();
			this.requestURL = requestURL;
			this.jsonData = jsonData;
			this.mCheckObject = mCheckObject;
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			showLoader();
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Integer doInBackground(String... urls) {
			try{
				statusCode = submitDeclineFee(requestURL, mCheckObject, jsonData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return statusCode;
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(statusCode == 200 || statusCode == 201 || statusCode == 202){
				refreshListFromServer();
			}
			else{
				if(statusCode == 401){
					showAlertDialogUnAuthorised(getResources().getString(R.string.error_401), AgreeToFeeActivity.this);
				} else if (statusCode == 403) {
					showAlertDialog(getResources().getString(R.string.error_generic), getResources().getString(R.string.lbl_alert), AgreeToFeeActivity.this);
				} else if (statusCode == 10000) {
					showAlertDialog(getResources().getString(R.string.error_network), getResources().getString(R.string.lbl_alert), AgreeToFeeActivity.this);
				} else if (statusCode == 400){
					showAlertDialog(getResources().getString(R.string.error_400), getResources().getString(R.string.lbl_alert), AgreeToFeeActivity.this);
				} else if (statusCode == 408){
					showAlertDialog(getResources().getString(R.string.error_408_request_timeout), getResources().getString(R.string.lbl_alert), AgreeToFeeActivity.this);
				} else {
					showAlertDialog(getResources().getString(R.string.error_generic), getResources().getString(R.string.lbl_alert), AgreeToFeeActivity.this);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismSuccess(java.util.ArrayList)
	 */
	@Override
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber) {
		hideLoader();
	}
	
	/**
	 * this is used to get the amount in string data Type format from double 
	 * @param amount
	 * @return
	 */
	private String getFormattedAmount(double amount){
		double checkAmount = amount;
		String editTextAmount = Double.toString(Math.abs(checkAmount));
		int integerPlaces = editTextAmount.indexOf('.');
		int decimalPlaces = editTextAmount.length() - integerPlaces - 1;
		
		if(decimalPlaces == 1){
			editTextAmount+= 0;
		}
		
		return editTextAmount;
	}
	
	/**
	 * this is used to refresh the list
	 */
	private void refreshListFromServer() {
		hideLoader();
		setResult(Activity.RESULT_OK);
		finish();
	}

	@Override
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler fundingMechanismObject) {
		if (fundingMechanismObject.getCardList() != null && fundingMechanismObject.getCardList().size() > 0) {
			for(int i =0; i < fundingMechanismObject.getCardList().size(); i++) {
				if (fundingMechanismObject.getCardList().get(i).getID().toString().equalsIgnoreCase(mFundingMechanismID)) {
					String accountType = Util.getAccountType(this, fundingMechanismObject.getCardList().get(i).getType().toString());
					mTvFunding.setText(getResources().getString(R.string.funding_to,
							accountType));
					
					String feeAmount = getFormattedAmount(mChkByIdHandler.getCheckObj().getFees());
					String checkAmount = getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(mChkByIdHandler.getCheckObj().getAmount() - mChkByIdHandler.getCheckObj().getFees())));
					mTvHaderTitle.setText(getResources().getString(R.string.fees_text, 
							feeAmount, checkAmount, accountType));
					mFundingToValue = accountType;
				//	mTvFunding.setText(getResources().getString(R.string.funding_to,fundingMechanismObject.getCardList().get(i).getType().toString()));
				}
			}
		}
		hideLoader();
	}

}
