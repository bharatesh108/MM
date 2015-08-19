package com.dfc.moneymartus.view.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
public class SignUpAccountAlreadyExists extends BaseActivity implements IServiceView {

	/*@InjectView(R.id.tv_having_trouble_customtext)
	TextView mTextViewTitle;
	 */@InjectView(R.id.tv_having_trouble_phone_number)
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
	 @InjectView(R.id.signup_btn)
	 Button mBtnSignup;
	 @InjectView(R.id.forgot_username_psw_btn)
	 Button mBtnForgotUsePsw;

	 private String phoneStr;
	 private String emailStr;
	 private String webUrl;

	 @Override
	 protected void onCreate(Bundle savedInstance) {
		 super.onCreate(savedInstance);
		 setContentView(R.layout.activity_signup_account_already_exits);
		
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
	  * this is used to process the request for Core Service
	  */
	 private void processCoreService()
	 {
		 CoreServiceHandler serviceHandler = new CoreServiceHandler(this);
		 serviceHandler.setReqEventType(EventTypes.EVENT_CORE_SERVICE);
		 processRequest(serviceHandler);	
	 }

	 /**
	  * this is used to show the dialog while Core Service Request is processing
	  * @param dataObject
	  */
	 public void processRequest(DataObject dataObject)
	 {
		 showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		 dataObject.register(Method.GET);
	 }

	 /**
	  * this is used to initialize the UI components.
	  */
	 private void initUI() {
		 ButterKnife.inject(this);
		 Util.hideSoftKeyboard(this, mTextViewUrl);
		 //		mTextViewTitle.setText(R.string.text_signup_existing_account_trouble);
		 mTextViewPhNumber.setOnClickListener(this);
		 mTextViewEmail.setOnClickListener(this);
		 mTextViewUrl.setOnClickListener(this);
		 mBtnForgotUsePsw.setOnClickListener(this);
		 mBtnSignup.setOnClickListener(this);
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
		 ImageView mBackBtn = (ImageView) mCustomView.findViewById(R.id.homeBtn);
		 mBackBtn.setVisibility(View.GONE);
		 mTitleTextView.setText(R.string.navigation_account_exists);
		 mTitleTextView.setPadding(Util.pxTodpConversion(this,20), 0, 0, 0);
		 mActionBar.setCustomView(mCustomView);
		 mActionBar.setDisplayShowCustomEnabled(true);

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
	  * this is used to refresh the screen
	  * @param dataObject
	  */
	 private void refershScreenData(CustomerServicesDataObject dataObject) {
		 this.phoneStr    =dataObject.customerServices.phone;
		 this.emailStr    =dataObject.customerServices.getEmail();
		 this.webUrl    =dataObject.customerServices.website;
		 mTextViewPhNumber.setText(this.phoneStr);

		// String openingHour = dataObject.customerServices.openingHours.monday.open;
		 //String closingHour = dataObject.customerServices.openingHours.monday.close;
		/* try {
			 String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
//			 mTextViewTime.setText(OpeningHoursText);
			 mTextViewTime.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			 mTextViewTimeWeek.setText(mContext.getResources().getString(R.string.day_format));
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }*/

		// String time;
			try {
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
		 mTextViewEmail.setText(emailStr.toLowerCase());
		 mTextViewUrl.setText(dataObject.customerServices.website);
		/* String time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
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

	 @Override
	 public void onClick(final View view) {
		 if (view.getId() == R.id.btn_right) {
			 mDialog.dismiss();
		 }
		 //Calling the implicit intent for Phone Call
		 else if (view.getId() == R.id.tv_having_trouble_phone_number) {
			 if(phoneStr!=null&&phoneStr.length()>0){
				 Intent callIntent = new Intent(Intent.ACTION_DIAL);
				 callIntent.setData( Uri.parse("tel:" +phoneStr));
				 mContext.startActivity(callIntent);
			 }
		 }
		 //Calling the implicit intent for sending mail
		 else if (view.getId() == R.id.tv_having_trouble_mail) {
			 if(emailStr!=null&&emailStr.length()>0){
				 Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", emailStr, null));
				 emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				 startActivity(Intent.createChooser(emailIntent, "Send email..."));
			 }
		 } 
		 // starting the ContactUs Activity with passing the url and title.
		 else if (view.getId() == R.id.tv_having_trouble_weburl) {
			 Intent browserIntent = new Intent(this,ContactUsActivity.class);
			 browserIntent.putExtra("Url", webUrl);
			 browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));

			 startActivity(browserIntent);
		 }
		 // navigating to Login Activity Screen when sign up button is clicked
		 else if(view.getId() == R.id.signup_btn)
		 {
			 Intent intent = new Intent(this, LoginActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(intent);
			 finish();

		 }
		 // navigating to forgotpassword  Screen
		 else if(view.getId() == R.id.forgot_username_psw_btn)
		 {
			 if(Util.isNetworkAvailable(this))
			 {
				 startActivity(new Intent(this, ForgotPasswordActivity.class));
				 this.finish();
			 } else
			 {
				 showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			 }

		 }
	 }

	 @Override
	 public void onBackPressed() {
		 Intent intent = new Intent(this, LoginActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		 startActivity(intent);
		 finish();
	 }
}
