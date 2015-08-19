package com.dfc.moneymartus.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CustDetailDataObject;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.SupportTroubleActivity;

public class PersonalDetailsFragment extends BaseFragment implements IViewCallbackListner {//IFragmentListener{

	@InjectView(R.id.tv_firstname)
	TextView mTvFirstName;
	@InjectView(R.id.tv_secondname)
	TextView mTvSecondName;
	@InjectView(R.id.tv_housenumber)
	TextView mTvHouseNo;
	@InjectView(R.id.tv_street)
	TextView mTVStreet;
	@InjectView(R.id.tv_city)
	TextView mTvCity;
	@InjectView(R.id.tv_county)
	TextView mTvCounty;
	@InjectView(R.id.tv_state)
	TextView mTvState;
	@InjectView(R.id.tv_zippostalcode)
	TextView mTvZipCode;
	@InjectView(R.id.tv_country)
	TextView mTvCountry;
	@InjectView(R.id.tv_phone)
	TextView mTvPhone;
	@InjectView(R.id.tv_mobile)
	TextView mMobile;

	@InjectView(R.id.firstname_lay)
	LinearLayout mFirstLay;
	@InjectView(R.id.secondname_lay)
	LinearLayout mSecondLay;
	@InjectView(R.id.housenumber_lay)
	LinearLayout mHouseLay;
	@InjectView(R.id.street_lay)
	LinearLayout mStreetlay;
	@InjectView(R.id.city_lay)
	LinearLayout mCityLay;
	@InjectView(R.id.county_lay)
	LinearLayout mCountyLay;
	@InjectView(R.id.state_lay)
	LinearLayout mStateLay;
	@InjectView(R.id.postalcode_lay)
	LinearLayout mPostcodeLay;
	@InjectView(R.id.country_lay)
	LinearLayout mCountryLay;
	@InjectView(R.id.phone_lay)
	LinearLayout mPhoneLay;
	@InjectView(R.id.mobile_lay)
	LinearLayout mMobileLay;
	@InjectView(R.id.tv_conatctus_link)
	TextView mTvConatctUsLink;

	private Activity mContext;
	private CustDetailDataObject personalDetailDao;

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
		Preferences.getInstance(mContext);
		personalDetailDao = new CustDetailDataObject(this);
	}

	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar()
	{
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_menu_personal_details));
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
		precessPersonalDetails();
	}

	/**
	 * Set event type for to get personal details API call
	 */
	private void precessPersonalDetails() {
		personalDetailDao.setReqEventType(EventTypes.EVENT_PERSONAL_DETAILS);
		processRequest(personalDetailDao);
	}

	/**
	 * Used to send API call to server
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.fragment_personal_details, container, false);
		initUI(mRootView);
		return mRootView;

	}

	/**
	 * Used to initialize UI components
	 * @param view
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);
		mTvConatctUsLink.setOnClickListener(this);
		mTvConatctUsLink.setText(Html.fromHtml(getString(R.string.personal_details_contact_us)));
	}

	/**
	 * Used to refresh screen data on success of API call 
	 */
	private void refreshScreenData() {
		if (personalDetailDao.getFirstName() == null || personalDetailDao.getFirstName().toString().isEmpty() ||  
				personalDetailDao.getFirstName().toString().equalsIgnoreCase("null")){
			mFirstLay.setVisibility(View.GONE);
		} else {
			mTvFirstName.setText(personalDetailDao.getFirstName().toString());
		}

		if (personalDetailDao.getSecondName() == null || personalDetailDao.getSecondName().toString().isEmpty() ||  
				personalDetailDao.getSecondName().toString().equalsIgnoreCase("null")){
			mSecondLay.setVisibility(View.GONE);
		} else {
			mTvSecondName.setText(personalDetailDao.getSecondName().toString());
		}

		if (personalDetailDao.getHouseNumber() == null || personalDetailDao.getHouseNumber().toString().isEmpty() ||  
				personalDetailDao.getHouseNumber().toString().equalsIgnoreCase("null")){
			mHouseLay.setVisibility(View.GONE);
		} else {
			mTvHouseNo.setText(personalDetailDao.getHouseNumber().toString());
		}

		if (personalDetailDao.getCity() == null || personalDetailDao.getCity().toString().isEmpty() ||  
				personalDetailDao.getCity().toString().equalsIgnoreCase("null")){
			mCityLay.setVisibility(View.GONE);
		} else {
			mTvCity.setText(personalDetailDao.getCity().toString());
		}

		if (personalDetailDao.getStreet() == null || personalDetailDao.getStreet().toString().isEmpty() || 
				personalDetailDao.getStreet().toString().equalsIgnoreCase("null")){
			mStreetlay.setVisibility(View.GONE);
		} else {
			mTVStreet.setText(personalDetailDao.getStreet().toString());
		}

		if (personalDetailDao.getCounty() == null || personalDetailDao.getCounty().toString().isEmpty() 
				|| personalDetailDao.getCounty().toString().equalsIgnoreCase("null")) {
			mCountyLay.setVisibility(View.GONE);
		} else {
			mTvCounty.setText(personalDetailDao.getCounty().toString());
		}

		if (personalDetailDao.getPostalCode() == null || personalDetailDao.getPostalCode().toString().isEmpty() ||  
				personalDetailDao.getPostalCode().toString().equalsIgnoreCase("null")){
			mPostcodeLay.setVisibility(View.GONE);
		} else {
			mTvZipCode.setText(personalDetailDao.getPostalCode().toString());
		}

		if (personalDetailDao.getCountry() == null || personalDetailDao.getCountry().toString().isEmpty() ||  
				personalDetailDao.getCountry().toString().equalsIgnoreCase("null")){
			mCountryLay.setVisibility(View.GONE);
		} else {
			Log.e("personalDetail Country",":"+ personalDetailDao.getCountry());
			mTvCountry.setText(personalDetailDao.getCountry().toString());
		}

		if (personalDetailDao.getMobile() == null || personalDetailDao.getMobile().toString().isEmpty() ||  
				personalDetailDao.getMobile().toString().equalsIgnoreCase("null")){
			mMobile.setVisibility(View.GONE);
		} else {
			//mMobile.setText(personalDetailDao.getMobile());
			mMobile.setText(personalDetailDao.getMobile().toString());
		}

		if (personalDetailDao.getState() == null || personalDetailDao.getState().toString().isEmpty() || 
				personalDetailDao.getState().toString().equalsIgnoreCase("null")){
			mStateLay.setVisibility(View.GONE);
		} else {
			mTvState.setText(personalDetailDao.getState().toString());
		}

		if (personalDetailDao.getPhone() == null || personalDetailDao.getPhone().toString().isEmpty() ||  
				personalDetailDao.getPhone().toString().equalsIgnoreCase("null")){
			mPhoneLay.setVisibility(View.GONE);
		} else {
			//mTvPhone.setText(personalDetailDao.getPhone());
			mTvPhone.setText(personalDetailDao.getPhone().toString());
		}
		
		if (personalDetailDao.getMobile() == null || personalDetailDao.getMobile().toString().isEmpty() ||  
				personalDetailDao.getMobile().toString().equalsIgnoreCase("null")){
			mMobileLay.setVisibility(View.GONE);
		} else {
			//mTvPhone.setText(personalDetailDao.getPhone());
			mMobile.setText(personalDetailDao.getMobile().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess()
	 */
	@Override
	public void onViewSuccess() {
		hideLoader();

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess(com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onViewSuccess(DataObject dataObject) {
		hideLoader();
		if (dataObject.getEventType() == EventTypes.EVENT_PERSONAL_DETAILS)
		{
			refreshScreenData();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onViewFailure(VolleyError error, int eventType) {
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

	/**
	 * Used to navigate to previous screen from the stack on click of device back button
	 */
	public void onBackPressed()
	{
		FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
		fm.popBackStack();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tv_conatctus_link) {
			if(Util.isNetworkAvailable(mContext))
			{
				Intent intent = new Intent(mContext, SupportTroubleActivity.class);
				intent.putExtra("Title", mContext.getString(R.string.navigation_menu_contact_us));
				startActivity(intent);
			}
			else
			{
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
		} else if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
	}
}
