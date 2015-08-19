package com.dfc.moneymartus.view.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.GetTranscationHandler;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.dto.FundingMechanismDataObject.FundingMechanisms;
import com.dfc.moneymartus.dto.FundingMechanismDataObject.Transaction;
import com.dfc.moneymartus.dto.TransactionDataObject.TransactionList;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.ContactUsActivity;
import com.dfc.moneymartus.view.adapter.CardDetailsArrayAdapter;
import com.dfc.moneymartus.view.callback.CardDetailView;

public class CardDetailsFragment extends BaseFragment implements CardDetailView {
	
	@InjectView(R.id.lv_transaction_details)
	ListView mLvTransactionDetails;
	@InjectView(R.id.tv_balance)
	TextView mTvBalance;
	@InjectView(R.id.tv_master_card_holder_agreement)
	TextView mTvMasterCardAgreement;
	@InjectView(R.id.tv_ptc_privacy_policy)
	TextView mTvPTCPrivacy;
	private Activity mContext;
	private GetTranscationHandler mTranscationHandler;
	final ArrayList<String> mCardNumber = new ArrayList<String>();
	private Transaction mTransaction;
	private String mDate;
	private CardDetailsArrayAdapter mCardsAdapter;
	private String mAmount;
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
		/**
		 * App will send Get funding mechanism API call	
		 */
		if(Util.isNetworkAvailable(mContext))
		{
			Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_card_services),
					getString(R.string.event_card_details));
			showLoader();
			mTranscationHandler = new GetTranscationHandler(this);
			mTranscationHandler.setReqEventType(EventTypes.FUNDING_MECHANISM_TRANSACTION);
			mTranscationHandler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
			mTranscationHandler.getfundingMechanisms();
		} else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}
	
	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar()
	{
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_momentum_card_drawer));
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
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.fragment_card_services, container, false);
		initUI(mRootView);
		return mRootView;

	}

	/**
	 * Used to initialize UI components 
	 * @param view used to create UI components 
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);
		mCardsAdapter = new CardDetailsArrayAdapter(getActivity(), R.layout.transaction_row_details,new  ArrayList<TransactionList>());
		mLvTransactionDetails.setAdapter(mCardsAdapter);
		mTvBalance.setText(getString(R.string.balance_details, ""));
		
	}

	@OnClick(R.id.tv_master_card_holder_agreement)
	public void showCardHolderAgreementURL(){
		 Intent browserIntent = new Intent(mContext, ContactUsActivity.class);
		 browserIntent.putExtra("Url", Constants.URL_CARD_HOLDER_AGREEMENT);
		 browserIntent.putExtra("title", getResources().getString(R.string.navigation_momentum_card_drawer));
		 startActivity(browserIntent);
	}
	
	@OnClick(R.id.tv_ptc_privacy_policy)
	public void showPplPrivacyPolicyURL(){
		 Intent browserIntent = new Intent(mContext, ContactUsActivity.class);
		 browserIntent.putExtra("Url", Constants.URL_PPT_PRIVACY_POLICY);
		 browserIntent.putExtra("title", getResources().getString(R.string.navigation_momentum_card_drawer));
		 startActivity(browserIntent);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.CardDetailView#onGetFundingMechanismFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(mContext, errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.CardDetailView#onGetFundingMechanismSuccess(com.dfc.moneymartus.dto.FundingMechanismDataObject)
	 */
	@Override
	public void onGetFundingMechanismSuccess(
			FundingMechanismDataObject fundingMechanismObject) {
		FundingMechanisms [] fundingArray = fundingMechanismObject.fundingMechanisms;
		if (fundingArray != null && fundingArray.length > 0){
			for(int i = 0; i < fundingArray.length; i++){
				 mTransaction =  fundingArray[i]._links.transactions;
				 mDate =  fundingArray[i].validFromDate;
				 mAmount = fundingArray[i].balance;
//				 mCardNumber.add("Momentum Card ****" + fundingArray[i].cardNumberEnding);
				 mCardNumber.add("Momentum Card");
			}
			if(mDate!=null && !mDate.equals("null") && !mDate.equals("")){
				mDate  = Util.getyyyymmddDate(mDate, true);
			}
			mTvBalance.setText(getString(R.string.balance_details, "$"+(Util.getdoubleUSPriceFormat(Double.valueOf(mAmount)))));
		
		}
		hideLoader();
		if(mTransaction!=null){
			Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_card_services),
					getString(R.string.event_card_transactions));
			showLoader();
			//String link = Constants.BASE_URL.replace("/v2", "")+mTransaction.href;
			String link = Constants.BASE_URL.substring(0 ,Constants.BASE_URL.length() - 3)+mTransaction.href;
			mTranscationHandler.setReqEventType(EventTypes.FUNDING_MECHANISM_TRANSACTION_HISTORY);
			mTranscationHandler.setUrl(link);
			mTranscationHandler.getfundingMechanisms();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.CardDetailView#onGetTrancationSuccess(com.dfc.moneymartus.dto.TransactionDataObject.TransactionList[])
	 */
	@Override
	public void onGetTrancationSuccess(TransactionList... transactionList) {
		if (transactionList != null && transactionList.length > 0){
			ArrayList<TransactionList> adapterTransactionsList  = new ArrayList<TransactionList>();
		    adapterTransactionsList.addAll(Arrays.asList(transactionList));
			mCardsAdapter = new CardDetailsArrayAdapter(getActivity(), R.layout.transaction_row_details, adapterTransactionsList);
			mLvTransactionDetails.setAdapter(mCardsAdapter);
			mCardsAdapter.notifyDataSetChanged();
		}
		hideLoader();
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.CardDetailView#onGetTrancationFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetTrancationFailure(VolleyError error, int eventType) {
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(mContext, errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
	}
}
