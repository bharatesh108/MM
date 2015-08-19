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
public class UpdateUsernameActivity extends BaseActivity implements IServiceView, android.view.View.OnClickListener {

	@InjectView(R.id.tv_phone_number)
	TextView mTextViewPhoneNumber;
	@InjectView(R.id.tv_update_username_time)
	TextView mTextViewHoursOfOperation;
	@InjectView(R.id.tv_update_username_day)
	TextView mTextViewHoursOfOperationWeek;
	@InjectView(R.id.tv_having_trouble_day_french)
	TextView mTextViewWeekFrench;
	@InjectView(R.id.tv_mail)
	TextView mTextViewMail;
	@InjectView(R.id.tv_weburl)
	TextView mTextViewWebUrl;
	private String mEmailStr;
	private String mWebUrl;
	private String mPhoneStr;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		//MMUtil.setActionBarTitle(this, getResources().getString(R.string.navigation_menu_update_username).toString());
		setContentView(R.layout.update_username);
		setUpActionBar();
		initUI();
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
	 * this is used to display the dialog while request is processing
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
		/*mTextViewPhoneNumber.setText("1-877-819-5857");
		mTextViewMail.setText("customercare@moneymart.com");
		mTextViewWebUrl.setText("www.moneymart.com");
		mTextViewHoursOfOperation.setText("7.00 a.m. - 8:00 p.m.");*/
		mTextViewPhoneNumber.setOnClickListener(this);
		mTextViewMail.setOnClickListener(this);
		mTextViewWebUrl.setOnClickListener(this);
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
		mTitleTextView.setText(R.string.navigation_menu_update_username);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	public void homeBtnClick(View v) {
		finish();
	}


	/**
	 * this is used to handle the error when Core Service Request fail
	 */
	@Override
	public void onCoreServiceFailure(VolleyError error, int eventType) {
		hideLoader();
		if (mDialog == null || !mDialog.isShowing()) {
			int statusCode = ErrorHandling.getStatusCode(error);
			String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
			if(statusCode == 401)
			{
				showAlertDialogUnAuthorised(errorMessage, this);
			} else {
				showAlertDialog(errorMessage, this);
			}
		}
	}


	/**
	 * 
	 */
	@Override
	public void onCoreServiceSuccess(CustomerServicesDataObject dataObject) {
		hideLoader();
		refershScreenData(dataObject);

	}

	/**
	 * this is used to refresh the screen data
	 * @param dataObject
	 */
	private void refershScreenData(CustomerServicesDataObject dataObject) {
		mEmailStr = dataObject.customerServices.getEmail();
		mWebUrl = dataObject.customerServices.website;
		mPhoneStr = dataObject.customerServices.phone;

		mTextViewPhoneNumber.setText(dataObject.customerServices.phone);
		mTextViewMail.setText(dataObject.customerServices.getEmail().toLowerCase());
		mTextViewWebUrl.setText(dataObject.customerServices.website);
		//String openingHour = dataObject.customerServices.openingHours.monday.open;
		//		String closingHour = dataObject.customerServices.openingHours.monday.close;

		/*try {
			String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
//			mTextViewHoursOfOperation.setText(OpeningHoursText);
			mTextViewHoursOfOperation.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			mTextViewHoursOfOperationWeek.setText(mContext.getResources().getString(R.string.day_format));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

		//		String time;
		try {
			//time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
			String outputTime = Util.getOpenCloseHrs(this, dataObject.customerServices.openingHours.monday.open, dataObject.customerServices.openingHours.monday.close);
			/*time = dataObject.customerServices.openingHours.monday.open;
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
				mTextViewHoursOfOperationWeek.setText("");
				mTextViewWeekFrench.setVisibility(View.VISIBLE);
				mTextViewWeekFrench.setText(mContext.getResources().getString(R.string.day_format));
			}
			else{
				mTextViewHoursOfOperationWeek.setText(mContext.getResources().getString(R.string.day_format));
			}
			
			mTextViewHoursOfOperation.setText(getResources().getString(R.string.time_pst, outputTime));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		} else if (view.equals(mTextViewPhoneNumber)) {
			//else if (view.getId() == R.id.tv_forgot_username_phone_number) {
			if(mPhoneStr!=null&&mPhoneStr.length()>0){
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData( Uri.parse("tel:" +mPhoneStr));
				mContext.startActivity(callIntent);
			}
		} else if (view.equals(mTextViewMail)) {
			//else if (view.getId() == R.id.tv_forgot_username_mail) {
			if(mEmailStr!=null&&mEmailStr.length()>0){
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", mEmailStr, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		}  else if (view.equals(mTextViewWebUrl)) { 
			//else if (view.getId() == R.id.tv_forgot_username_weburl) {
			Intent browserIntent = new Intent(this,ContactUsActivity.class);
			browserIntent.putExtra("Url", mWebUrl);
			browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));
			startActivity(browserIntent);
		}

	}

}
