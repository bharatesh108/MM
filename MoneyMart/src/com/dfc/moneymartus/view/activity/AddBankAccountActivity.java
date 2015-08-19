package com.dfc.moneymartus.view.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.FundingMechanismPostDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.ExtrasConstants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.FundingMechanismView;

@SuppressLint("InflateParams")
public class AddBankAccountActivity extends BaseActivity implements FundingMechanismView {

	private EditText mEtAccNumber;
	private EditText mEtConfAccNumber;
	private EditText mEtRoutingNumber;
	private EditText mEtConfRoutingNumber;
	private Spinner mAccountTypeSpinner;
	private GetFundingMechanismHandler mGetFundingMechanismHandler;
	private boolean isFromSignUp = false;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_add_bank_account);//fragment_add_account);
		Bundle data = getIntent().getExtras();
		if(data != null) {
			String fromSignUp = getIntent().getExtras().getString(ExtrasConstants.SIGNUPTOBANK);
            /*This will be used to check app is navigated to this screen from signup or login.
            App navigation is based on this flag.
             */
			if(fromSignUp != null && !fromSignUp.isEmpty()) {
				isFromSignUp  = true;
			}

		}
		setUpActionBar();
		initUI();
	}

    /**
     * Used to initialize UI components
     */
	private void initUI() {
		mEtAccNumber = (EditText)findViewById(R.id.et_account_number);
		mEtConfAccNumber = (EditText)findViewById(R.id.et_confirm_account_number);
		mEtRoutingNumber = (EditText)findViewById(R.id.et_routing_number);
		mEtConfRoutingNumber = (EditText)findViewById(R.id.et_conf_routing_number);
		mAccountTypeSpinner = (Spinner) findViewById(R.id.spinner_account_type);
		TextView mTvPrivacyPolicy = (TextView)findViewById(R.id.text_privacy_policy);
		Button mBtnAddAccount = (Button)findViewById(R.id.btn_add_account);
		mTvPrivacyPolicy.setOnClickListener(this);
		mBtnAddAccount.setOnClickListener(this);
	}

	/*
	 * this is used to initialize and set up the UI of action bar
	 */
	private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.navigation_add_account);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.text_privacy_policy) {
			Intent intent = new Intent(this, PrivacyPolicyActivty.class);
			startActivity(intent);
		} else if (v.getId() == R.id.btn_add_account) {
			validateData();
		} else if(v.getId() == R.id.btn_right){
			getDialog().dismiss();
			if (mGetFundingMechanismHandler!=null && mGetFundingMechanismHandler.getEventType() == EventTypes.FUNDING_MECHANISM_POST_V2) {
			    /*After adding successfully bank account app will navigate to my checks screen if app landed to this screen signup
                or it will navigate to account details screen
                */
					if(isFromSignUp) {
					navigateToMyChecksSCreen();
				} else {
					finishBankActivity();
				}
			}
			else if(mDialogId.equals(Constants.DialogId.ADD_BANK_ACCOUNT_ERROR)){
				//navigate to my checks
				if(isFromSignUp) {
					navigateToMyChecksSCreen();
				} else {
                    //navigate to account details screen
					Intent i = getIntent();
					setResult(Constants.BANK_ACCOUNT_ACTIVITY, i);
					AddBankAccountActivity.this.finish();
				}
			}
		} else if(v.getId() == R.id.btn_left){
			getDialog().dismiss();
		}
	}

    /**
     * Used to finish current activity and
     * pass funding mechanism id to account details screen to update bank account number in ui
     */
	private void finishBankActivity() {
		Intent i = getIntent();
		i.putExtra(ExtrasConstants.AccountNumber, mGetFundingMechanismHandler.getFundMechPostDataObj().getAccountNumber().toString());
		i.putExtra(ExtrasConstants.RoutingNumber, mGetFundingMechanismHandler.getFundMechPostDataObj().getRoutingNumber().toString());
		i.putExtra(ExtrasConstants.BankType, mGetFundingMechanismHandler.getFundMechPostDataObj().getType().toString());
		setResult(Constants.BANK_ACCOUNT_ACTIVITY, i);
		AddBankAccountActivity.this.finish();
		
	}

	private void navigateToMyChecksSCreen() {
		mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, false);
		Constants.setOpenDrawer(true);
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.finish();		
	}

    /**
     * Used to validate bank account number and show error message if data is wrong
     */
	private void validateData() {
		if(validateEmptyData(mEtAccNumber) && validateEmptyData(mEtConfAccNumber) && validateEmptyData(mEtRoutingNumber) 
				&& validateEmptyData(mEtConfRoutingNumber)) {
			if(checkAccountNumberLength(mEtAccNumber) && isValidData(mEtAccNumber,mEtConfAccNumber, R.string.error_account_number_match) 
					&& checkRoutingNumberLength(mEtRoutingNumber) && isValidData(mEtRoutingNumber, mEtConfRoutingNumber,R.string.error_routing_number_match)) {
				setAccountData();
			}
		} else{
			showAlertDialog(getResources().getString(R.string.fill_all_fields_msg), this);
		}
	}

    /**
     * Used to validate bank account number and confirm account number, routing number and confirm routing number
     * are same
     * @param numbStr bank or routing number
     * @param confNumberStr confirm bank or confirm routing number
     * @param messageID message to show if not matches
     * @return is number valid or not
     */
	private boolean isValidData(EditText numbStr,EditText confNumberStr, int messageID) {
		boolean flag = true;
		//String accNumbStr = mEtAccNumber.getText().toString();
		//String confAccNumberStr = mEtConfAccNumber.getText().toString();
		//	PasswordValidator passwordValidater = new PasswordValidator();

		if (!numbStr.getText().toString().equals(confNumberStr.getText().toString())) {
			showAlertDialog(getResources().getString(messageID), this);
			flag = false;
		}
		return flag;
	}

    /**
     * Used to validate routing number. Routing number should be 9 digits in length
     * @param routingNumb routing number
     * @return is routing number is valid or not
     */
	private boolean checkRoutingNumberLength(EditText routingNumb) {
		boolean flag = true;
		if (routingNumb.getText().toString().length() < Constants.BANK_ACCOUNT_NUMBER_MIN_LENGTH ){
			routingNumb.requestFocus();
			flag = false;
		}
		if(!flag){
			showAlertDialog(getResources().getString(R.string.error_routing_number_limit), this);
		}
		return flag;
	}

    /**
     * Used to validate bank account number. Account number should not be less than 5 digits in length
     * @param accountNumb Account number
     * @return Account number is valid oor not
     */
	private boolean checkAccountNumberLength(EditText accountNumb) {
		boolean flag = true;
		if (accountNumb.getText().toString().length() < Constants.BANK_ROUTING_NUMBER_MIN_LENGTH ){
			accountNumb.requestFocus();
			flag = false;
		}
		if(!flag){
			showAlertDialog(getResources().getString(R.string.error_account_number_limit), this);
		}
		return flag;
	}

    /**
     * Used to form funding mechanism POST request
     */
	private void setAccountData() {
		GetFundingMechanismHandler fundingMechanismHandler = new GetFundingMechanismHandler(this);
		fundingMechanismHandler.setReqEventType(EventTypes.FUNDING_MECHANISM_POST_V2);
		fundingMechanismHandler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
		FundingMechanismPostDataObject fundDataObj = new FundingMechanismPostDataObject();
		fundDataObj.type = mAccountTypeSpinner.getSelectedItem().toString();
		fundDataObj.accountNumber = mEtAccNumber.getText().toString();
		fundDataObj.routingNumber = mEtRoutingNumber.getText().toString();
		fundingMechanismHandler.setFundingMechanismPostDataObject(fundDataObj);
		precessAddAccount(fundingMechanismHandler);	
	}

	/**
	 * Change text color if edit text is empty and show error message
	 * @param et Edit text to validate
	 * @return True id empty else false
	 */
	private Boolean validateEmptyData(EditText et) {
		boolean flagStatus = true;
		if (et.getText().toString().isEmpty()) {
			et.requestFocus();
			flagStatus = false;
		}
		return flagStatus;
	}

    /**
     * Used to verify network availability and send request to server
     * @param fundingMechanismHandler data object class
     */
	private void precessAddAccount(GetFundingMechanismHandler fundingMechanismHandler) {
		if(Util.isNetworkAvailable(this)) {
			fundingMechanismHandler.setReqEventType(EventTypes.FUNDING_MECHANISM_POST_V2);
			processRequest(fundingMechanismHandler);
		}
		else {

			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);
		}
	}

	private void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));
		dataObject.register(Method.POST);
	}

	@Override
	public void onGetFundingMechanismSuccess(
			FundingMechanismDataObject fundingMechanismObject,
			ArrayList<String> mCardNumber) {
		// TODO Nothing

	}

	@Override
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber) {
		// TODO Nothing

	}

	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {
		mGetFundingMechanismHandler = null;
		hideLoader();
		showAlertDialog("", getResources().getString(R.string.add_bank_account_error_msg), this, 0, getResources().getString(R.string.btn_yes), getResources().getString(R.string.btn_later),Constants.DialogId.ADD_BANK_ACCOUNT_ERROR);
	}

	@Override
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler dataObject) {
		mGetFundingMechanismHandler = dataObject;
		hideLoader();
		showAlertDialog(getResources().getString(R.string.add_bank_account_success_msg), this);
	}

	@Override
	public void onBackPressed() {
		handleNavigation();
	}

	/**
	 * this is used to finish the activity on clicking of home button
	 * @param v view
	 */
	public void homeBtnClick(View v) {
		handleNavigation();
		//		finish();
	}

    /**
     * Used to handle navigation based on is from signup flag
     */
	private void handleNavigation() {
		if(isFromSignUp) {
			mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, false);
			Constants.setOpenDrawer(true);
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			this.finish();	
		} else {
			finish();
		}
	}
}
