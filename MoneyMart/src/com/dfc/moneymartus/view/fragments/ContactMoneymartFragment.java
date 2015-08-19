package com.dfc.moneymartus.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CoreServiceHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.ContactUsActivity;
import com.dfc.moneymartus.view.activity.HomeActivity;
import com.dfc.moneymartus.view.activity.SignUpLocatorActivity;
import com.dfc.moneymartus.view.callback.IServiceView;

public class ContactMoneymartFragment extends BaseFragment implements IServiceView {
	
	//private String TAG = "MMContactUsFragment";
	@InjectView(R.id.tv_contact_moneymart_customtext)
	TextView mTextViewTitle;
	@InjectView(R.id.tv_contact_moneymart_phone_number)
	TextView mTextViewPhNumber;
	@InjectView(R.id.tv_contact_moneymart_time)
	TextView mTextViewTime;
	@InjectView(R.id.tv_contact_moneymart_day)
	TextView mTextViewTimeWeek;
	@InjectView(R.id.tv_having_trouble_day_french)
	TextView mTextViewWeekFrench;
	@InjectView(R.id.tv_contact_moneymart_mail)
	TextView mTextViewEmail;
	@InjectView(R.id.tv_contact_moneymart_weburl)
	TextView mTextViewUrl;
	@InjectView(R.id.btn_contact_moneymart_store_locator)
	Button mBtnUrl;
	

	private String phoneStr;
	private String emailStr;
	private String webUrl;
	private Activity mContext;
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
	}
	
	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar() {
		((HomeActivity)getActivity()).setActionBarTitle(getResources().getText(R.string.navigation_menu_contact_moneymart).toString());
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_menu_contact_moneymart));
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
		if (Constants.mCustServiceObj == null)
		{
			if(Util.isNetworkAvailable(mContext)) {
				processCoreService();
		}
		else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
		} else {
			refershScreenData(Constants.mCustServiceObj);
		}
	}
	
	/**
	 * Used to send core service API request to sever
	 */
	private void processCoreService()
	{
		CoreServiceHandler serviceHandler = new CoreServiceHandler(this);
		serviceHandler.setReqEventType(EventTypes.EVENT_CORE_SERVICE);
		processRequest(serviceHandler);	
	}
	
	/**
	 * Used to set request method i.e.GET/POST/PUT 
	 * @param dataObject Obejct of data class
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
		View mRootView = inflater.inflate(R.layout.fragment_contact_moneymart, container, false);
		initUI(mRootView);
		return mRootView;
	}
	
	/**
	 * Used to initialize UI components
	 * @param view used to create UI components 
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);
		mTextViewTitle.setText(getResources().getString(R.string.contact_title, "/"));
		mTextViewTitle.setTypeface(null, Typeface.BOLD); 
		mTextViewPhNumber.setOnClickListener(this);
		mTextViewEmail.setOnClickListener(this);
		mTextViewUrl.setOnClickListener(this);
		mBtnUrl.setOnClickListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.IServiceView#onCoreServiceFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onCoreServiceFailure(VolleyError error, int eventType) {
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
	 * @see com.dfc.moneymartus.view.callback.IServiceView#onCoreServiceSuccess(com.dfc.moneymartus.dto.CustomerServicesDataObject)
	 */
	@Override
	public void onCoreServiceSuccess(CustomerServicesDataObject dataObject) {
		hideLoader();
		refershScreenData(dataObject);
	}

	/**
	 * Used to refresh screen data on successful response of API call
	 * @param dataObject Object of class
	 */
	private void refershScreenData(CustomerServicesDataObject dataObject) {
		this.phoneStr    =dataObject.customerServices.phone;
		this.emailStr    =dataObject.customerServices.getEmail();
		this.webUrl    =dataObject.customerServices.website;
		mTextViewPhNumber.setText(this.phoneStr);
		
//		String openingHour = dataObject.customerServices.openingHours.monday.open;
//		String closingHour = dataObject.customerServices.openingHours.monday.close;
		/*try {
			String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
//			mTextViewTime.setText(OpeningHoursText);
			mTextViewTime.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			mTextViewTimeWeek.setText(mContext.getResources().getString(R.string.day_format));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		mTextViewEmail.setText(emailStr.toLowerCase());
		mTextViewUrl.setText(dataObject.customerServices.website);
		
//		String time;
		try {
			//time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
			String outputTime = Util.getOpenCloseHrs(mContext, dataObject.customerServices.openingHours.monday.open, dataObject.customerServices.openingHours.monday.close);
		/*	time = dataObject.customerServices.openingHours.monday.open;
			time = 	time.substring(1,6).trim();
			SimpleDateFormat  inputFormat = new SimpleDateFormat("HH:mm");
			DateFormat outputFormat = new SimpleDateFormat("hh:mm aa",
                    Locale.US);
			Date date = inputFormat.parse(time);
			String output = outputFormat.format(date);
		//String closing = dataObject.customerServices.openingHours.monday.close.substring(0, (dataObject.customerServices.openingHours.monday.close.indexOf("-")-1));
			//closing = closing.substring(1,6);
			String	closing = dataObject.customerServices.openingHours.monday.close;
			closing = closing.substring(1,6).trim();
			Date date1 = inputFormat.parse(closing);
			String output1 = outputFormat.format(date1);
			String formattedTime = output +" - "+output1;
			String outputTime = formattedTime.replace("AM", "am").replace("PM","pm");*/
			
			if(outputTime.contains("Avant-midi") || outputTime.contains("Après-midi")){
				mTextViewTimeWeek.setText("");
				mTextViewWeekFrench.setVisibility(View.VISIBLE);
				mTextViewWeekFrench.setText(mContext.getResources().getString(R.string.day_format));
			}
			else{
				mTextViewTimeWeek.setText(mContext.getResources().getString(R.string.day_format));
			}
			
			mTextViewTime.setText(getResources().getString(R.string.time_pst, outputTime));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * Required if time format changed in future
		 */
		/*String time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
		Log.e(TAG,"onCoreServiceSuccess : "+ time);

		DateFormat df1 = new SimpleDateFormat("'T'HH:mm:ss.SSSZ",Locale.US);//("'T'HH:mm:ss.SSSZ");
		try {
			Date result1 = df1.parse(time);
			Log.e(TAG,"onCoreServiceSuccess : "+ result1);
			// time.setText(result1.toString()+"");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
		/**
		 * on click of phone number app will navigate to default dialer screen
		 */
		else if (view.getId() == R.id.tv_contact_moneymart_phone_number) {
			if(phoneStr!=null && phoneStr.length()>0){
				Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_contact_us),
							getString(R.string.event_contactus_quickdial_cusomer_care));	
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
		        callIntent.setData( Uri.parse("tel:" +phoneStr));
		        mContext.startActivity(callIntent);
			}
		}
		/**
		 * On click of email app will show list mail clients on selection of mail client it will show mail format screen.
		 * If it's gmail it will show pre populated mail subject 
		 */
		else if (view.getId() == R.id.tv_contact_moneymart_mail) {
			if(emailStr!=null && emailStr.length()>0){
				Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_contact_us),
						getString(R.string.event_contactus_email_customer_care));
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", emailStr, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		}
		/**
		 * On click of web url app will navigate to web page within app
		 */
		else if (view.getId() == R.id.tv_contact_moneymart_weburl) {
			Intent browserIntent = new Intent(mContext, ContactUsActivity.class);
			browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));
			browserIntent.putExtra("Url", webUrl);
			startActivity(browserIntent);
		}
		/**
		 * On click of store locator app will navigate to store locator web page within app
		 */
		else if (view.getId() == R.id.btn_contact_moneymart_store_locator) {
			if (Util.isNetworkAvailable(mContext)){
				Intent intent = new Intent(mContext, SignUpLocatorActivity.class);
				startActivity(intent);
			}else {
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);
			}
		}
		
		
	}
}
