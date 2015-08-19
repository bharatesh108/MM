package com.dfc.moneymartus.view.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CoreServiceHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.IServiceView;

@SuppressLint({ "InflateParams", "DefaultLocale" })
public class SupportTroubleActivity extends BaseActivity implements IServiceView, android.view.View.OnClickListener { //IViewCallbackListner {

	@InjectView(R.id.tv_having_trouble_customtext)
	TextView mTextViewTitle;
	@InjectView(R.id.tv_having_trouble_phone_number)
	TextView mTextViewPhNumber;
	@InjectView(R.id.tv_having_trouble_time)
	TextView mTextViewTime;
	@InjectView(R.id.tv_having_trouble_day)
	TextView mTextViewTimeWeek;
	@InjectView(R.id.tv_having_trouble_day_french)
	TextView mTextViewWeekFrench;
	@InjectView(R.id.tv_having_trouble_mail)
	TextView mTextViewEmail;
	@InjectView(R.id.tv_having_trouble_weburl)
	TextView mTextViewUrl;

	private String phoneStr;
	private String emailStr;
	private String webUrl;


	private String actionBarTitle;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		//MMUtil.setActionBarTitle(this, getResources().getString(R.string.navigation_menu_update_username).toString());
		setContentView(R.layout.having_trouble);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			actionBarTitle = extras.getString("Title");
		}
		initUI();
		setUpActionBar();
		if (Constants.mCustServiceObj == null)
		{
			if(Util.isNetworkAvailable(this)) {
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
	 * this is used to process the core service request
	 */
	private void processCoreService()
	{
		CoreServiceHandler serviceHandler = new CoreServiceHandler(this);
		serviceHandler.setReqEventType(EventTypes.EVENT_CORE_SERVICE);
		processRequest(serviceHandler);	
	}

	/**
	 * this is used to display the dialog while processing the request
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		ButterKnife.inject(this);
		mTextViewTitle.setText(R.string.having_trouble_custom_text);
		mTextViewPhNumber.setOnClickListener(this);
		mTextViewEmail.setOnClickListener(this);
		mTextViewUrl.setOnClickListener(this);
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
		mTitleTextView.setText(actionBarTitle);//(R.string.navigation_menu_having_trouble);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		/*ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		View mainCustomActionBar = this.getLayoutInflater().inflate(
				R.layout.view_actionbarview_new, null);

		actionBar.setCustomView(mainCustomActionBar,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
		TextView textViewTitle = (TextView)actionBar.getCustomView().findViewById(R.id.actionbar_title);
		textViewTitle.setText(R.string.navigation_menu_having_trouble);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);*/
	}

	public void homeBtnClick(View v) {
		finish();
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
			showAlertDialogUnAuthorised(errorMessage, this);
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
	 * 
	 * @param dataObject
	 */
	private void refershScreenData(CustomerServicesDataObject dataObject) {
		this.phoneStr    =dataObject.customerServices.phone;
		this.emailStr    =dataObject.customerServices.getEmail();
		this.webUrl    =dataObject.customerServices.website;
		mTextViewPhNumber.setText(this.phoneStr);//(phoneStr);
		//mTextViewPhNumber.setSpan(MMUtil.URLSpanNoUnderline)
		//(new URLSpanNoUnderline(PhoneNumberUtils.formatNumber(this.phoneStr)), 0, UrlText.length() , 0);

		/*String openingHour = dataObject.customerServices.openingHours.monday.open;
		Log.e(TAG,"onCoreServiceSuccess : "+ openingHour);
		String closingHour = dataObject.customerServices.openingHours.monday.close;
		Log.e(TAG,"onCoreServiceSuccess : "+ closingHour);
		try {
			String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
			//mTextViewTime.setText(OpeningHoursText + " PST");
			mTextViewTime.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			mTextViewTimeWeek.setText(mContext.getResources().getString(R.string.day_format));
		} catch (ParseException e) {
			e.printStackTrace();
		}
*/
		mTextViewEmail.setText(emailStr.toLowerCase());
		mTextViewUrl.setText(dataObject.customerServices.website);
		System.out.println("Opening hours "+dataObject.customerServices.openingHours.monday.open);
		//String time;
		try {
			//time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
			String outputTime = Util.getOpenCloseHrs(this, dataObject.customerServices.openingHours.monday.open, dataObject.customerServices.openingHours.monday.close);
		/*	time = dataObject.customerServices.openingHours.monday.open;
			time = 	time.substring(1,6).trim();
			SimpleDateFormat  inputFormat = new SimpleDateFormat("HH:mm");
			DateFormat outputFormat = new SimpleDateFormat("hh:mm aa",
                    Locale.US);
			Date date = inputFormat.parse(time);
			String output = outputFormat.format(date);
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

		/*DateFormat df1 = new SimpleDateFormat("'T'HH:mm:ss",Locale.US);//("'T'HH:mm:ss.SSSZ");
		try {
			Date result1 = df1.parse(time);
			Log.e(TAG,"onCoreServiceSuccess : "+ result1);
			// time.setText(result1.toString()+"");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
		//calling implicit intent of phone call
		else if (view.getId() == R.id.tv_having_trouble_phone_number) {
			if(phoneStr!=null&&phoneStr.length()>0){
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData( Uri.parse("tel:" +phoneStr));
				mContext.startActivity(callIntent);
			}
		}
		//calling implicit intent of sending the mail
		else if (view.getId() == R.id.tv_having_trouble_mail) {
			if(emailStr!=null&&emailStr.length()>0){
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", emailStr, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		}
		//navigating to ContactUs Activity with passing Url and Title data
		else if (view.getId() == R.id.tv_having_trouble_weburl) {
			Intent browserIntent = new Intent(this,ContactUsActivity.class);
			browserIntent.putExtra("Url", webUrl);
			browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));

			startActivity(browserIntent);
		}


	}


}
