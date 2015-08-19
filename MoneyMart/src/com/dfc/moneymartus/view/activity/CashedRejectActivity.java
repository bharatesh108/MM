package com.dfc.moneymartus.view.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.FundingMechanismView;

@SuppressLint("InflateParams")
public class CashedRejectActivity extends BaseActivity implements FundingMechanismView {

	@InjectView(R.id.iv_hearder_image)//gone
	ImageView mIvTHeader;
	@InjectView(R.id.tv_review_pending_custom_text)//visible
	TextView mTvHaderTitle;
	@InjectView(R.id.check_capture_layout)//gone
	LinearLayout mLinLayCapture;//img_capture_lay
	@InjectView(R.id.transaction_check_details_layout)//gone
	LinearLayout mLinLayTransactionInfo;//transaction_info_lay
	@InjectView(R.id.check_details_layout)//visible
	LinearLayout mLinLiayCheckDetails;//check_details_lay
	@InjectView(R.id.tv_check_message_text)//gone
	TextView mTvChkMsg;
	@InjectView(R.id.btn_action_required_submit)//
	Button mBtnAgree;
	@InjectView(R.id.btn_action_required_cancel)//gone
	Button mBTnCancel;
	//Check detail
	@InjectView(R.id.tv_check_details_amount)
	TextView mTvChkAmt;
	@InjectView(R.id.tv_funding_to)
	TextView mTvFunding;
	@InjectView(R.id.tv_date_submitted)
	TextView mTvDateSubmit;
	@InjectView(R.id.tv_date_transaction_completed)
	TextView mTvDateComplete;

	private int mActionStatus;

	private String mCheckSubmittedDate;
	private String mCheckCompletedDate;
	private String mCheckAmount;
	private String mCheckFee;
	private String mFundingMechanismID;
	private String mFeesAgreed;
	
	@Override
	protected void onCreate(Bundle savedInstance) {

		super.onCreate(savedInstance);
		setContentView(R.layout.layout_parent_capture_check);
		setUpActionBar();
		initUI();
		refreshScreenData();

	}

	/**
	 * this is used to initialize the component of UI component
	 */
	private void initUI() {
		ButterKnife.inject(this);
		Bundle data = getIntent().getExtras();
		mCheckAmount = data.getString(Constants.CHECK_AMOUNT);
		mCheckSubmittedDate = data.getString(Constants.CHECK_DATE_SUBMIT);
		mCheckCompletedDate = data.getString(Constants.CHECK_DATE_COMPLETE);
		mCheckFee = data.getString(Constants.CHECK_FEES);
		mFeesAgreed = data.getString(Constants.CHECK_FEES_AGREED);
		mActionStatus = data.getInt("action_status");
		mFundingMechanismID = data.getString(Constants.CHECK_FUNDINGTO);
		
		mIvTHeader.setVisibility(View.VISIBLE);
		mTvHaderTitle.setText(getResources().getString(R.string.cashed_chk_title,"-"));
		mTvChkMsg.setVisibility(View.GONE);
		if(mActionStatus == Constants.CheckStatus.REJECTED)
		{
			mTvHaderTitle.setText(R.string.rejected_chk_title);
			
//			mTvChkMsg.setVisibility(View.GONE);

			mIvTHeader.setBackgroundResource(R.drawable.reject_status);
		} else {
			mTvHaderTitle.setText(getResources().getString(R.string.cashed_chk_title, "MM/DD/YYYY"));
			mTvChkMsg.setVisibility(View.VISIBLE);
			mTvChkMsg.setText(R.string.check_message_text);
			mIvTHeader.setBackgroundResource(R.drawable.cashed_status);
		}
		mLinLayCapture.setVisibility(View.GONE);//img_capture_lay
		mLinLayTransactionInfo.setVisibility(View.GONE);//transaction_info_lay
		//	mLinLiayCheckDetails;//check_details_lay

		mBtnAgree.setVisibility(View.GONE);
		mBTnCancel.setVisibility(View.GONE);
		mTvChkAmt.setText(getResources().getString(R.string.check_details_amount, "-"));
		mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed, "-"));
		mTvFunding.setText(getResources().getString(R.string.funding_to,"-"));
		mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, "-"));
		
	
	}

	/**
	 * this is used to initialize and set up the action bar
	 */
	@SuppressLint("InflateParams") private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.navigation_check);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	public void homeBtnClick(View v) {
		finish();
	}

	/**
	 * this is used to refresh the screen
	 */
	private void refreshScreenData() {
		getFundingMechanism();
		if(mActionStatus == Constants.CheckStatus.CASHED) {
			if(mCheckCompletedDate==null || mCheckCompletedDate.equalsIgnoreCase("null") || (mCheckCompletedDate.trim() != null && mCheckCompletedDate.isEmpty())) {
				mTvHaderTitle.setText(getResources().getString(R.string.cashed_chk_title,"-"));
			} else {
				mTvHaderTitle.setText(getResources().getString(R.string.cashed_chk_title, Util.getyyyymmddDate(mCheckCompletedDate, true)));
			}
		}
		//		mTvHaderTitle.setText(getResources().getString(R.string.check_details_amount, checkByIDHandler.getCheckObj().getFees(),
		//				checkByIDHandler.getCheckObj().getAmount() + ""));
		
		if(mFeesAgreed.equals("true")){
			double checkAmount = Double.valueOf(mCheckAmount);
			double checkFees = Double.valueOf(mCheckFee);
			mTvChkAmt.setText(getResources().getString(R.string.check_details_amount, "$"+Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(checkAmount - checkFees)))));
		}
		else{
			mTvChkAmt.setText(mContext.getResources().getString(R.string.check_details_amount, "$"+Util.getFormattedAmount(Double.valueOf(mCheckAmount))));
		}
		
	//mTvFunding.setText(getResources().getString(R.string.funding_to,checkByIDHandler.getCheckObj().getFundingMechanism()));
		if(mCheckSubmittedDate == null || mCheckSubmittedDate.equalsIgnoreCase("null") || (mCheckSubmittedDate.trim() != null && mCheckSubmittedDate.isEmpty())) {
			mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, "-"));
		} else {
			mTvDateSubmit.setText(getResources().getString(R.string.date_submitted,Util.getyyyymmddDate(mCheckSubmittedDate, true)));
		}

		if(mCheckCompletedDate == null || mCheckCompletedDate.equalsIgnoreCase("null") || (mCheckCompletedDate.trim() != null && mCheckCompletedDate.isEmpty())) {
			mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed, "-"));
		} else {
			mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed,Util.getyyyymmddDate(mCheckCompletedDate, true)));
		}
	}

	/**
	 * this is used to get the funding mechanism
	 */
	public void getFundingMechanism() {
		showLoader();
		GetFundingMechanismHandler handler = new GetFundingMechanismHandler(this);
		handler.setReqEventType(EventTypes.FUNDING_MECHANISM_V2);
		handler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
		handler.getfundingMechanisms();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {
		mTvFunding.setText(getResources().getString(R.string.funding_to, "-"));
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismSuccess(com.dfc.moneymartus.dto.FundingMechanismDataObject, java.util.ArrayList)
	 */
	@Override
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject, ArrayList<String> mCardNumber) {
		if (fundingMechanismObject.fundingMechanisms == null) {
			mTvFunding.setText(getResources().getString(R.string.funding_to,"-"));
		} else {
//			mTvFunding.setText(getResources().getString(R.string.funding_to,"Momentum Card ****"+fundingMechanismObject.fundingMechanisms[0].cardNumberEnding));
			mTvFunding.setText(getResources().getString(R.string.funding_to,"Momentum Card"));
		}

		hideLoader();

	}

	@Override
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler fundingMechanismObject) {
		if (fundingMechanismObject.getCardList() != null && fundingMechanismObject.getCardList().size() > 0) {
			for(int i =0; i < fundingMechanismObject.getCardList().size(); i++) {
				if (fundingMechanismObject.getCardList().get(i).getID().toString().equalsIgnoreCase(mFundingMechanismID)) {
					String accountType = Util.getAccountType(this, fundingMechanismObject.getCardList().get(i).getType().toString());
					mTvFunding.setText(getResources().getString(R.string.funding_to,
							accountType));
				//	mTvFunding.setText(getResources().getString(R.string.funding_to,fundingMechanismObject.getCardList().get(i).getType().toString()));
					if(accountType.equalsIgnoreCase(this.getResources().getString(R.string.bank_account_title))) {
						mTvChkMsg.setVisibility(View.GONE);
					} else {
						mTvChkMsg.setVisibility(View.VISIBLE);
					}
				}
			}
		}
		hideLoader();
	}
}