package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CoreServiceHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.ForgotPasswordHandler;
import com.dfc.moneymartus.dto.Challenge;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.ForgotPasswordView;
import com.dfc.moneymartus.view.callback.IServiceView;

@SuppressLint({ "InflateParams", "DefaultLocale" })
public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordView, IServiceView, OnClickListener{

	@InjectView(R.id.et_username)
	EditText mETextUserName;
	@InjectView(R.id.tv_forgot_username_phone_number)
	TextView mTvDialPhoneNumber;
	@InjectView(R.id.tv_forgot_username_mail)
	TextView mTvUserMail;
	@InjectView(R.id.tv_forgot_username_weburl)
	TextView mTvWebURL;
	@InjectView(R.id.btn_set_forgot_password_submit)
	Button mBtnSubmit;
	@InjectView(R.id.tv_forgot_username_time)
	TextView mtime;
	@InjectView(R.id.tv_forgot_username_day)
	TextView mWeek;
	@InjectView(R.id.tv_having_trouble_day_french)
	TextView mTextViewWeekFrench;

	@SuppressWarnings("unused")
	private static final String TAG = "MMForgotPasswordActivity";
	private String phoneStr;
	private String emailStr;
	private String webUrl;


	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_forgot_password);
		setUpActionBar();
		initLoginUI();
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

		Moneymart.getInstance().setInstanceToFinish(this);
		Util.hideSoftKeyboard(ForgotPasswordActivity.this, mETextUserName);
	}


	/**
	 * this is used to process the request for core service
	 */
	private void processCoreService()
	{
		CoreServiceHandler serviceHandler = new CoreServiceHandler(this);
		serviceHandler.setReqEventType(EventTypes.EVENT_CORE_SERVICE);
		processRequest(serviceHandler);	
	}

	/**
	 * this is used to show the dialog while core service request is processing 
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
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
		mTitleTextView.setText(R.string.navigation_menu_enter_username);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to process the request for new password
	 * @param username
	 */
	private void processPasswordDetails(String username) {
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		ForgotPasswordHandler  passwordHandler = new ForgotPasswordHandler(this);
		passwordHandler.setReqEventType(EventTypes.EVENT_FORGOT_PASSWORD);
		passwordHandler.requestNewPassword(username);

	}

	/**
	 * this is used to initialize the UI component 
	 */
	private void initLoginUI() {
		ButterKnife.inject(this);
		mTvDialPhoneNumber.setOnClickListener(this);
		mTvWebURL.setOnClickListener(this);
		mTvUserMail.setOnClickListener(this);

	}

	/**
	 * Making the cursor visible on the click of EditText for Username
	 */
	@OnClick(R.id.et_username)
	public void userNameClick()
	{
		mETextUserName.setCursorVisible(true);
	}
	
	/**
	 * this is used to set the new password
	 */
	@OnClick(R.id.btn_set_forgot_password_submit)
	public void setNewPassword() {
		Util.hideSoftKeyboard(ForgotPasswordActivity.this, mETextUserName);
		String mUserName = mETextUserName.getText().toString().trim();
		if (isValid(mUserName)) {
			if(Util.isNetworkAvailable(this)) {
				processPasswordDetails(mUserName);
			} else
			{
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
		}
	}

	/**
	 * this is used to validate the username
	 * @param username
	 * @return
	 */
	private boolean isValid(final String username) {
		boolean flag = true;
		if (username == null || username.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.enter_username), this);
			flag = false;
		}
		return flag;
	}


	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.ForgotPasswordView#onGetPasswordFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetPasswordFailure(VolleyError error, int eventType) {
		hideLoader();
		/*showAlertDialog(getResources().getString(R.string.error_server_forgot_password), this);*/
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
	 * @see com.dfc.moneymartus.view.callback.ForgotPasswordView#onGetPasswordSuccess(com.dfc.moneymartus.dto.Challenge)
	 */
	@Override
	public void onGetPasswordSuccess(Challenge dataObject) {
		Log.d("MoneyMart","response "+dataObject.challenge);
		hideLoader();
		Intent intent= new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
		intent.putExtra("challenge", dataObject.challenge);
		intent.putExtra("UserName",  mETextUserName.getText().toString().trim());
		startActivity(intent);
	}

	public void homeBtnClick(View v) {
		finishActivity();
	}
	
	@Override
	public void onBackPressed() {
		finishActivity();
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
		phoneStr = dataObject.customerServices.phone;
		mTvDialPhoneNumber.setText(this.phoneStr);

	//	String openingHour = dataObject.customerServices.openingHours.monday.open;
		//String closingHour = dataObject.customerServices.openingHours.monday.close;
		/*try {
			String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
			//mtime.setText(OpeningHoursText);
			mtime.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			mWeek.setText(mContext.getResources().getString(R.string.day_format));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		//String time;
		try {
			//time = dataObject.customerServices.openingHours.monday.open.substring(0, (dataObject.customerServices.openingHours.monday.open.indexOf("-")-1));
			if(dataObject.customerServices.openingHours.monday.open != null && dataObject.customerServices.openingHours.monday.close != null){
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
				mWeek.setText("");
				mTextViewWeekFrench.setVisibility(View.VISIBLE);
				mTextViewWeekFrench.setText(mContext.getResources().getString(R.string.day_format));
				}
			else{
				mWeek.setText(mContext.getResources().getString(R.string.day_format));
				}
				
			mtime.setText(getResources().getString(R.string.time_pst, outputTime));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		emailStr = dataObject.customerServices.getEmail();
		mTvUserMail.setText(emailStr.toLowerCase());
		webUrl = dataObject.customerServices.website;
		mTvWebURL.setText(webUrl);
	}



	
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
		else if (view.equals(mTvDialPhoneNumber)) {
			if(phoneStr!=null&&phoneStr.length()>0){
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
			       	callIntent.setData( Uri.parse("tel:" +phoneStr));
				mContext.startActivity(callIntent);
			}
		}
		else if (view.equals(mTvUserMail)) {
			if(emailStr!=null&&emailStr.length()>0){
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", emailStr, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		}

		else if (view.equals(mTvWebURL)) {
			Intent browserIntent = new Intent(this,ContactUsActivity.class);
			browserIntent.putExtra("Url", webUrl);
			browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));
			startActivity(browserIntent);
		}


	}
	
	public void finishActivity(){
		Util.hideSoftKeyboard(this, mETextUserName);
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}

