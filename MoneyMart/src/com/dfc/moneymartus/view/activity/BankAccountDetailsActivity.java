package com.dfc.moneymartus.view.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.ExtrasConstants;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("InflateParams")
public class BankAccountDetailsActivity extends BaseActivity {

	private TextView mTvRoutingNumberVal;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_account_details);
		setUpActionBar();
		initUI();
	}

    /**
     * Used to initialise ui components
     */
	private void initUI() {
		TextView mTVAccountNumberVal = (TextView)findViewById(R.id.tv_account_number_val);
		mTvRoutingNumberVal = (TextView)findViewById(R.id.tv_routing_number_val);
		TextView mTVBankTypeVal = (TextView)findViewById(R.id.tv_account_number_type);
		TextView mTvContactUs = (TextView)findViewById(R.id.tv_contactus_link);
		TextView mTvPrivacyPolicy = (TextView)findViewById(R.id.text_privacy_policy);
		
		Bundle data = getIntent().getExtras();
		mTVAccountNumberVal.setText(data.getString(ExtrasConstants.AccountNumber));
		mTvRoutingNumberVal.setText(data.getString(ExtrasConstants.RoutingNumber));
		if(data.getString(ExtrasConstants.BankType).toLowerCase(Locale.US).contains("savings")){
			mTVBankTypeVal.setText(this.getString(R.string.savings_account_type_title));
		} else if(data.getString(ExtrasConstants.BankType).toLowerCase(Locale.US).contains("checking")) {
			mTVBankTypeVal.setText(this.getString(R.string.checking_account_type_title));
		} else {
			mTVBankTypeVal.setText(data.getString(ExtrasConstants.BankType));
		}
		
		mTvContactUs.setOnClickListener(this);
		mTvContactUs.setText(Html.fromHtml(getString(R.string.bank_account_contact_msg)));
		mTvPrivacyPolicy.setOnClickListener(this);
	}

	/**
	 * this is used to initialize and set up the action bar
	 */
	private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.bank_account_title);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}
	
	/**
	 * this is used to finish the activity on clicking of home button
	 * @param v view
	 */
	public void homeBtnClick(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public void onClick(View view) {
		Util.hideSoftKeyboard(this, mTvRoutingNumberVal);
		if (view.getId() == R.id.tv_contactus_link) {

			Intent intent = new Intent(mContext, SupportTroubleActivity.class);
			intent.putExtra("Title", mContext.getString(R.string.navigation_menu_contact_us));
			startActivity(intent);
			
		} else if (view.getId() == R.id.text_privacy_policy) {
			Intent intent = new Intent(this, PrivacyPolicyActivty.class);
			startActivity(intent);
		}
	}
}
