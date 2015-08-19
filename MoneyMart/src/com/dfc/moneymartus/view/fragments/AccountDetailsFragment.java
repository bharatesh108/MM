package com.dfc.moneymartus.view.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.ExtrasConstants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.AddBankAccountActivity;
import com.dfc.moneymartus.view.activity.BankAccountDetailsActivity;
import com.dfc.moneymartus.view.activity.HomeActivity;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.ChangePswActivity;
import com.dfc.moneymartus.view.activity.UpdateUsernameActivity;
import com.dfc.moneymartus.view.callback.FundingMechanismView;

public class AccountDetailsFragment extends BaseFragment implements FundingMechanismView {


	@InjectView(R.id.ll_update_password_label)
	LinearLayout mLinearLayoutUpdatePassword;
	@InjectView(R.id.ll_update_username_label)
	LinearLayout mLinearLayoutUpdateUsername;
	@InjectView(R.id.ll_add_bank_details)
	LinearLayout mLinearAddBankAccount;
	@InjectView(R.id.tv_add_bank_account)
	TextView mTvAccountNumber;

	private Activity mContext;
	private String mAccountNumber;
	private String mRoutingNumber;
	private String mAccountNumberVal = null;
	private String mRoutingNumberVal = null;
	private String mBankType;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
		setUpActionBar();
		if (!Constants.BASE_URL.contains("catest")) {
			if(Util.isNetworkAvailable(mContext)) {				
				processFundingMechanism();
			}
			else {
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
		}
	}

	/**
	 * this is used to process the core service request
	 */
	private void processFundingMechanism()
	{
		GetFundingMechanismHandler serviceHandler = new GetFundingMechanismHandler(this);
		serviceHandler.setReqEventType(EventTypes.FUNDING_MECHANISM_V2);
		serviceHandler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
		processRequest(serviceHandler);	

	}

	/**
	 * this is used to display the dialog while request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}
	/*
	 * Used to set action bar title
	 */
	private void setUpActionBar(){
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_menu_account_details));
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		setUpActionBar();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		setRetainInstance(true);

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {

		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.fragment_account_details, container, false);
		initUI(mRootView);
		return mRootView;
	}

	/**
	 * USed to initialize UI comeponents
	 * @param view UI view
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);
		Util.hideSoftKeyboard(mContext, view);

		//mLinearLayoutUpdatePassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.right_arrow), null);
		//mLinearLayoutUpdateUsername.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.right_arrow), null);
	}

	/**
	 * On Click of update password button app will navigate to Change Password screen 
	 */
	@OnClick(R.id.ll_update_password_label)
	public void updatePassword() {
		Log.e("updatePassword",":"+"onclick");
		Intent intent = new Intent(mContext, ChangePswActivity.class);
		((HomeActivity) getActivity()).startActivity(intent);	
		//((HomeActivity)getActivity()).goToAccountDetailsFragment();
	}

	/**
	 * On Click of update user name app will navigate to update user name screen
	 */
	@OnClick(R.id.ll_update_username_label)
	public void updateUserName() {
		Intent intent = new Intent(mContext, UpdateUsernameActivity.class);
		((HomeActivity) getActivity()).startActivity(intent);	
	}

	/**
	 * On Click of add back account button application will navigate to Add Bank Account screen
	 */
	@OnClick(R.id.ll_add_bank_details)
	public void addBankAccount() {
		//		Intent intent = new Intent(mContext, AddBankAccountActivity.class);
		//		startActivityForResult(intent, Constants.BANK_ACCOUNT_ACTIVITY);
		if(mTvAccountNumber.getText().toString().contains(getResources().getString(R.string.add_bank_account_title))){

			//Launch add bank account number screen
			Intent intent = new Intent(mContext, AddBankAccountActivity.class);
			startActivityForResult(intent, Constants.BANK_ACCOUNT_ACTIVITY);
		} else {
			//Launch account details screen
			Intent intent = new Intent(mContext, BankAccountDetailsActivity.class);
			intent.putExtra(ExtrasConstants.AccountNumber, getAccountNumber());
			intent.putExtra(ExtrasConstants.RoutingNumber, getRoutingNumber());
			intent.putExtra(ExtrasConstants.BankType, getBankType());
			startActivity(intent);		
		}

	}

	@Override
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject, ArrayList<String> mCardNumber) {
		//onGetFundingMechanismSuccess
	}

	@Override
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber) {
		//onGetFundingMechanismSuccess
	}

	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {

		try {
			hideLoader();
			int statusCode = ErrorHandling.getStatusCode(error);
			String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
			if(statusCode == 401)
			{
				showAlertDialogUnAuthorised(mContext, errorMessage, this);
			} else {
				showAlertDialog(mContext, errorMessage, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler fundingMechanismObject) {
		hideLoader();
		if (fundingMechanismObject.getCardList() != null && fundingMechanismObject.getCardList().size() > 0) {
			for(int i =0; i < fundingMechanismObject.getCardList().size(); i++) {
				if (fundingMechanismObject.getCardList().get(i).mType.toString().contains("Bank"))
				{
					//BankAccountNumber
					String accountNumber = Util.getFormedNumber(fundingMechanismObject.getCardList().get(i).getAccountNumber().toString());
					setAccountNumber(accountNumber);
					String routingNumber = fundingMechanismObject.getCardList().get(i).getRoutingNumber().toString();
					setRoutingNumber(routingNumber);
					String bankType = fundingMechanismObject.getCardList().get(i).getType().toString();
					setBankType(bankType);
					refreshScreenData();
				}
			}
		}
	}

	private void refreshScreenData() {
	//	mTvAccountNumber.setText(getResources().getString(R.string.bank_account_number_title, getAccountNumber()));
		mTvAccountNumber.setText(Html.fromHtml(this.getResources().getString(R.string.bank_account_number_title, getAccountNumber())));
	}

	/**
	 * @return the mAccountNumber
	 */
	public String getAccountNumber() {
		return mAccountNumber;
	}

	/**
	 * @param mAccountNumber the mAccountNumber to set
	 */
	public void setAccountNumber(String mAccountNumber) {
		this.mAccountNumber = mAccountNumber;
	}

	/**
	 * @return the mRoutingNumber
	 */
	public String getRoutingNumber() {
		return mRoutingNumber;
	}

	/**
	 * @param mRoutingNumber the mRoutingNumber to set
	 */
	public void setRoutingNumber(String mRoutingNumber) {
		this.mRoutingNumber = mRoutingNumber;
	}
	

	private void setBankType(String bankType) {
		this.mBankType = bankType;
	}
	
	private String getBankType() {
		return mBankType;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.BANK_ACCOUNT_ACTIVITY && data != null) {
			if(data.hasExtra(ExtrasConstants.AccountNumber) && data.hasExtra(ExtrasConstants.RoutingNumber)
					&& data.hasExtra(ExtrasConstants.BankType)){
				mAccountNumberVal = data.getStringExtra(ExtrasConstants.AccountNumber);
				mRoutingNumberVal = data.getStringExtra(ExtrasConstants.RoutingNumber);
				mBankType = data.getStringExtra(ExtrasConstants.BankType);
				String accountNumber = Util.getFormedNumber(mAccountNumberVal);
				setAccountNumber(accountNumber);
				setRoutingNumber(mRoutingNumberVal);
				refreshScreenData();
			}
		}
	}
}

