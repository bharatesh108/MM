package com.dfc.moneymartus.view.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.activity.ContactUsActivity;
import com.dfc.moneymartus.view.activity.HomeActivity;

public class LoanFragment extends BaseFragment{

	@InjectView(R.id.tv_payday_loans_custom_link)
	TextView mPayDayLoansLink;
	@InjectView(R.id.tv_payday_loans_custom_text)
	TextView mPayDayLoansText;
	@InjectView(R.id.tv_payday_loans_note)
	TextView mPayDayLoansNote;

	private String mLoanUrl;

	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar() {
		((HomeActivity)getActivity()).setActionBarTitle(getResources().getText(R.string.navigation_loan_title).toString());
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
		View mRootView = inflater.inflate(R.layout.fragment_payday_loans, container, false);
		mLoanUrl = getLoanURL();
		//mLoanUrl = Constants.URL_LOAN_FORM;
		initUI(mRootView);
		return mRootView;

	}

	private String getLoanURL() {
		//set test URL
		if(Constants.BASE_URL.contains("preview1")){
			mLoanUrl = Constants.URL_LOAN_TEST;
		} else if(Constants.BASE_URL.contains("catest")){
			//set UAT URL
			mLoanUrl = Constants.URL_LOAN_UAT;
		} else if(Constants.BASE_URL.contains("us.api.")){
			//set Production URL
			mLoanUrl = Constants.URL_LOAN_PROD;
		}
		return mLoanUrl;
		
	}

	private void initUI(View view) {
		ButterKnife.inject(this, view);
		/**
		 *  As string text contains multiple colors this is used to create spannable string.
		 */
		mPayDayLoansLink.setMovementMethod(LinkMovementMethod.getInstance());
		String strLeading = getXMLString(this, R.string.custom_link_pre);
		String strTrailing = getXMLString(this, R.string.custom_link_end);
		String strTrailingEnd = getXMLString(this,
				R.string.custom_link_end_text);
		mPayDayLoansLink.setMovementMethod(LinkMovementMethod.getInstance());
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		stringBuilder.append(strLeading);
		stringBuilder.append(" " + strTrailing);
		stringBuilder.append(" " + strTrailingEnd);

		stringBuilder.setSpan(
				new NonUnderlinedClickableSpan() {

					@Override
					public void onClick(View widget) {
							Intent intent  = new Intent(getActivity(), ContactUsActivity.class);
							intent.putExtra("Url", mLoanUrl);
							intent.putExtra("title", "");
							startActivity(intent);
					}
				}, strLeading.length(),
				strLeading.length() + strTrailing.length() + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		mPayDayLoansLink.setText(stringBuilder);
		mPayDayLoansLink.setMovementMethod(LinkMovementMethod.getInstance());

	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btn_right){
			mDialog.dismiss();
		}
	}
	
	/**
	 * Used to set text color of for the defined text
	 */
	public class NonUnderlinedClickableSpan extends ClickableSpan {

		@Override
		public void updateDrawState(TextPaint ds) {

			ds.setColor(getResources().getColor(R.color.hyper_link));
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View v) {
			//TODO Nothing
		}
	}

	/**
	 * Used convert id to string
	 * @param loanFragment Fragment object
	 * @param stringID String id
	 * @return String
	 */
	public static String getXMLString(LoanFragment loanFragment, int stringID) {
		Resources res = loanFragment.getResources();
		return res.getString(stringID);

	}

}
