package com.dfc.moneymartus.view.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.SignupPersonalDetailHandler;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.dto.SignUpUserDetailDataObject;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.infra.Validator;
import com.dfc.moneymartus.view.callback.IServiceView;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class UpdatePersonalDetailsActivityFrench extends BaseActivity implements IServiceView, IViewCallbackListner{

	@InjectView(R.id.tv_user_details_text)
	TextView mTvUserDetails;
	@InjectView(R.id.tv_user_first_name_message)
	TextView mTvFirstName;
	@InjectView(R.id.user_last_name_message)
	TextView mTvLastName;
	@InjectView(R.id.tv_user_dob_message)
	TextView mTvDOB;
	@InjectView(R.id.tv_user_house_number_message)
	TextView mTvHouseNumber;
	@InjectView(R.id.tv_user_street_message)
	TextView mTvStreet;
	@InjectView(R.id.tv_user_city_message)
	TextView mTvCity;
	@InjectView(R.id.tv_user_state_message)
	TextView mTvState;
	@InjectView(R.id.tv_user_zip_postal_message)
	TextView mTvZipCode;
	@InjectView(R.id.tv_user_country_message)
	TextView mTvCountry;
	@InjectView(R.id.tv_user_home_phone_message)
	TextView mTvHomePhone;
	@InjectView(R.id.tv_email_id_message)
	TextView mTvEmailId;

	@InjectView(R.id.et_user_first_name_message)
	EditText mEtUserFirstName;
	@InjectView(R.id.et_user_last_name)
	EditText mEtUserLastName;
	@InjectView(R.id.et_user_dob)
	EditText mEtDOB;
	@InjectView(R.id.et_user_ssn)
	EditText mEtSSN;
	@InjectView(R.id.et_user_house_number)
	EditText mEtHouseNumber;
	@InjectView(R.id.et_user_street)
	EditText mEtUserStreet;
	@InjectView(R.id.et_user_city_hint)
	EditText mEtCity;
	@InjectView(R.id.et_user_state_hint)
	Spinner mEtState;
	@InjectView(R.id.et_user_country_hint)
	EditText mEtCountry;
	@InjectView(R.id.et_user_zip_postal)
	EditText mEtPostalCode;
	@InjectView(R.id.et_user_home_phone)
	EditText mEtHomePhone;
	@InjectView(R.id.et_user_cell_phone_message)
	EditText mEtCellPhone;
	@InjectView(R.id.btn_next)
	Button mBtnNext;
	@InjectView(R.id.et_email_id)
	EditText mEtEmailId;
	@InjectView(R.id.text_privacy_policy)
	TextView mTvPrivacyPolicy;
	
	@InjectView(R.id.firstname_divider)
	View mFirstnameDivider;
	@InjectView(R.id.lastname_divider)
	View mLastnameDivider;
	@InjectView(R.id.ssn_divider)
	View mSsnDivider;
	@InjectView(R.id.email_divider)
	View mEmailDivider;
	@InjectView(R.id.postalcode_divider)
	View mPostalcodeDivider;
	@InjectView(R.id.homephone_divider)
	View mHomephoneDivider;
	@InjectView(R.id.housenumber_divider)
	View mHousenumberDivider;

	private Preferences mPreferences;
	private SignUpUserDetailDataObject mUserDetailDataObject;
	private SignupPersonalDetailHandler mSignupPersonalDetailHandler;
	private DatePickerDialog mDatePicker;
	String mForSercerFormat = "yyyy-MM-dd";//"yyyy-MM-dd'T'HH:mm:ss";
	SimpleDateFormat mSdfText = new SimpleDateFormat("MM/dd/yyyy",Locale.US);//("yyyy-MM-dd", Locale.US);
	SimpleDateFormat mSdfserver = new SimpleDateFormat(mForSercerFormat, Locale.US);
	private DatePickerDialog.OnDateSetListener datePicker; 

	private Calendar mCalendar;
	private Calendar mCalendarInitDate;
	String mDate = "";
	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;
	private boolean isDefaultDateIntialized = false;

	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_signup_personal_details);

		setUpActionBar();
		mPreferences = Preferences.getInstance(this);
		mSignupPersonalDetailHandler = new SignupPersonalDetailHandler(this);
		mUserDetailDataObject = new SignUpUserDetailDataObject(); 

		mUserDetailDataObject.password = getIntent().getExtras().getString("password");
		mUserDetailDataObject.challengeId = getIntent().getExtras().getString("challenge_id");
		mUserDetailDataObject.secret = getIntent().getExtras().getString("answer");
		mUserDetailDataObject.username = getIntent().getExtras().getString("user_name");
		initUI();

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
		mTitleTextView.setText(R.string.navigation_menu_personal_details);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		ButterKnife.inject(this);
		ActionBar actionBar = this.getActionBar();
		actionBar.setIcon(R.drawable.back_btn);
		mEtCountry.setEnabled(false);
		mEtDOB.setInputType(InputType.TYPE_NULL);
		mCalendar = Calendar.getInstance();
		mCalendarInitDate = Calendar.getInstance();
		mCalendarInitDate.add(Calendar.YEAR, -18);
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		//mEtCheckAmount.setText("$0.00");
		//	EtDOB.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.calendar), null);
		mEtDOB.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				datePicker = new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
						mCalendar.set(Calendar.YEAR, year);
						mCalendar.set(Calendar.MONTH, monthOfYear);
						mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						isDefaultDateIntialized = true;
						updateDateLabel();
					}

				};
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					//if(event.getRawX() >= (EtDOB.getRight() - EtDOB.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
					showDatePickerDialog(Constants.DATE_DIALOG_ID).show();
					//return true;
					//s}
				}
				return false;
			}
		});

		mEtCountry.setText("CAN");
		Util.addMandatoryAsterisk(mContext, R.string.user_details_text, mTvUserDetails);
		Util.addMandatoryAsterisk(mContext, R.string.user_first_name_message, mTvFirstName);
		Util.addMandatoryAsterisk(mContext, R.string.user_last_name_message, mTvLastName);
		Util.addMandatoryAsterisk(mContext, R.string.user_dob_message, mTvDOB);
	/*	Util.addMandatoryAsterisk(mContext, R.string.user_house_number_message, mTvHouseNumber);
		Util.addMandatoryAsterisk(mContext, R.string.user_street_message, mTvStreet);
		Util.addMandatoryAsterisk(mContext, R.string.user_city_message, mTvCity);
		Util.addMandatoryAsterisk(mContext, R.string.user_state_message, mTvState);
		Util.addMandatoryAsterisk(mContext, R.string.user_zip_postal_message, mTvZipCode);
		Util.addMandatoryAsterisk(mContext, R.string.user_country_message, mTvCountry);*/
		//Util.addMandatoryAsterisk(mContext, R.string.user_home_phone_message, mTvHomePhone);
		Util.addMandatoryAsterisk(mContext, R.string.email_id, mTvEmailId);
		
		retainEnteredUserDetails();	
	}

	@OnClick(R.id.btn_next)
	public void submitBtnClicked()
	{	
		validateEmptyEditText();

	}

	/**
	 * this is used to validate user entered details
	 */
	private void validateEmptyEditText() {
		
		if(validateEmptyData(mEtUserFirstName) && validateEmptyData(mEtUserLastName) && validateEmptyData(mEtEmailId) 
				 && validateEmptyData(mEtDOB)) {
		
	/*	if(validateEmptyData(mEtUserFirstName) && validateEmptyData(mEtUserLastName) && validateEmptyData(mEtEmailId) && validateEmptyData(mEtHouseNumber)
				&& validateEmptyData(mEtPostalCode) && validateEmptyData(mEtHomePhone) && validateEmptyData(mEtDOB) && validateEmptyData(mEtSSN)
				&& validateEmptyData(mEtCity) && validateEmptyData(mEtUserStreet)) {*/
			 if(!validateData(Validator.EMAIL_PATTERN, mEtEmailId,mEmailDivider)) {
				
				showAlertDialog(getResources().getString(R.string.email_error), this);
				
			} else if (!mEtSSN.getText().toString().isEmpty() && !validateData(Validator.SSN_PATTERN, mEtSSN,mSsnDivider)) {

				showAlertDialog(getResources().getString(R.string.signup_error_ssn), this);
				
			}else if(!mEtPostalCode.getText().toString().isEmpty() && !validateData(Validator.ZIPCODE_PATTERN, mEtPostalCode, mPostalcodeDivider)) {
				
				showAlertDialog(getResources().getString(R.string.signup_error_postalcode), this);
				
			} else if (!mEtHomePhone.getText().toString().isEmpty() && !validateData(Validator.PHONENUMBER_PATTERN, mEtHomePhone,mHomephoneDivider)) {

				showAlertDialog(getResources().getString(R.string.signup_error_phone), this);
				
			} else if (!mEtCellPhone.getText().toString().isEmpty() && !validateData(Validator.PHONENUMBER_PATTERN, mEtCellPhone,mHomephoneDivider)) {

				showAlertDialog(getResources().getString(R.string.signup_error_phone), this);
				
			}
			/*else if(!validateConatctNumberData(EtHomePhone)){//(EtHomePhone.length() < 10) {
			
				showAlertDialog(getResources().getString(R.string.phone_number_error), this);
				//validateConatctNumberData(EtHomePhone);
		
			} */else {
				setPersonaleDetailsData();
				precessPersonalDetails();
			}

		} else  {
			showAlertDialog(getResources().getString(R.string.mandatory_filed_error), this);
		}

	}

	//never used
	/*private boolean validateConatctNumberData(EditText et) {
		boolean flagStatus = true;
		if(et.getText().toString().trim().length() < 10) {
			setTextViewColor(et, getResources().getColor(R.color.color_action_bar));
			et.requestFocus();
			flagStatus = false;
		} else {
			setTextViewColor(et, getResources().getColor(R.color.personal_details_data));
		}
		return flagStatus;
	}
*/
	/**
	 * this is used to set the users personal details data
	 */
	private void setPersonaleDetailsData() {
		mUserDetailDataObject.firstName = mEtUserFirstName.getText().toString();
		mUserDetailDataObject.secondName = mEtUserLastName.getText().toString();
		mUserDetailDataObject.dateOfBirth = mDate;//EtDOB.getText().toString();
		mUserDetailDataObject.ssn = mEtSSN.getText().toString();
		mUserDetailDataObject.street = mEtUserStreet.getText().toString();
		mUserDetailDataObject.city = mEtCity.getText().toString();
		mUserDetailDataObject.country= "CAN";
		mUserDetailDataObject.state = Util.getStateAbbrevation().get(mEtState.getSelectedItem().toString());//EtState.getSelectedItem().toString();
		mUserDetailDataObject.postcodeZip = mEtPostalCode.getText().toString();
		mUserDetailDataObject.county = mEtCountry.getText().toString();
		mUserDetailDataObject.houseNumber = mEtHouseNumber.getText().toString();

		mUserDetailDataObject.phone = mEtHomePhone.getText().toString();
		mUserDetailDataObject.phone2 = null;
		mUserDetailDataObject.mobile = mEtCellPhone.getText().toString();

		mUserDetailDataObject.email = mEtEmailId.getText().toString();
		mUserDetailDataObject.termsAgreed = true;
		mSignupPersonalDetailHandler.setUserDetailDataObject(mUserDetailDataObject);

		/*	String nowAsISO = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);
			Date parsedDate = df.parse("1990�08�03");
			nowAsISO = df.format(parsedDate);
			Log.e("nowAsISO ==== ",":" + nowAsISO);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mUserDetailDataObject.dateOfBirth = nowAsISO;*/
	}


	/**
	 * this is used to validate data
	 * @param pattern
	 * @param et
	 * @param divider
	 * @return
	 */
	private boolean validateData(String pattern, EditText et, View divider) {
		boolean flagStatus = true;
		if (Validator.validate(pattern, et.getText().toString().trim())) {
			setTextViewColor(et, getResources().getColor(R.color.black_color));
			divider.setBackgroundColor(getResources().getColor(R.color.personal_details_data));
		} else {
			setTextViewColor(et, getResources().getColor(R.color.color_action_bar));
			divider.setBackgroundColor(getResources().getColor(R.color.color_action_bar));
			et.requestFocus();
			flagStatus = false;
		}
		return flagStatus;
	}

	/**
	 * Change text color if edit text is empty and show error message
	 * @param et Edit text to validate
	 * @return True id empty else false
	 */
	private Boolean validateEmptyData(EditText et) {
		boolean flagStatus = true;
		if (et.getText().toString().isEmpty()) {
			//	setTextViewColor(et, getResources().getColor(R.color.color_action_bar));
			et.requestFocus();
			flagStatus = false;
		}// else
		//	setTextViewColor(et, getResources().getColor(R.color.personal_details_data));
		return flagStatus;
	}
	//showAlertDialog(getResources().getString(R.string.mandatory_filed_error), this);

	/*
	 * Set text view color
	 */
	private void setTextViewColor(EditText editText, int textColor) {
		editText.setTextColor(textColor);
	}

	/**
	 * this is used to process the request for personal details
	 */
	private void precessPersonalDetails() {
		if(Util.isNetworkAvailable(this)) {
			mSignupPersonalDetailHandler.setReqEventType(EventTypes.EVENT_SIGNUP_PERSONALDETAILS);
			processRequest(mSignupPersonalDetailHandler);
		}
		else {
			
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

	/**
	 * this is used to show the dialog while request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.POST);
	}

	@Override
	public void onBackPressed() {
		saveEnteredUserDetails();
		finish();
	}

	public void homeBtnClick(View v) {
		saveEnteredUserDetails();
		finish();
	}

	/**
	 * this is used to handle the error when request for core service fail
	 */
	@Override
	public void onCoreServiceFailure(VolleyError error, int eventType) {
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

	@Override
	public void onCoreServiceSuccess(CustomerServicesDataObject dataObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewSuccess() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.instacheques.gateway.IViewCallbackListner#onViewFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onViewFailure(VolleyError error, int eventType) {
		hideLoader();
		Util.errorHandling(error);
		//showAlertDialog(MMUtil.errorHandling(error), this);

		int iErrorCode = ErrorHandling.getStatusCode(error);
		signUpErrorHandling(iErrorCode);
	}

	/**
	 * this is used to handle the error
	 * @param iErrorCode
	 */
	private void signUpErrorHandling(int iErrorCode) {
		/*
		 * DFC Web api document 0.2.2 page number :55
		 */
		if(iErrorCode == 401)
		{
			//	showAlertDialog(("401 : "), this);
			Intent i = new Intent(this, SignupInvalidUserNameActivity.class);
			startActivity(i);
			finish();

		} else if(iErrorCode == 403)
		{
			//	showAlertDialog(("403 : Customer Doesnt exist"), this);
			Intent i = new Intent(this, SignUpNoMomentumCard.class);
			startActivity(i);
			finish();

		} else if (iErrorCode == 409 )
		{
			//showAlertDialog(("409 : Account already exists. Please log in with account credentials."), this);
			Intent i = new Intent(this, SignUpAccountAlreadyExists.class);
			startActivity(i);
			finish();
			
		} else if (iErrorCode == 201 )
		{
			//showAlertDialog(("409 : Account already exists. Please log in with account credentials."), this);
			Intent intent = new Intent(this, AccountCreatedActivity.class);
			intent.putExtra("user_name", mUserDetailDataObject.username);
			intent.putExtra("password", mUserDetailDataObject.password);
			startActivity(intent);
			finish();
		} else {
			if (mDialog == null || !mDialog.isShowing()) {
				String errorMessage = ErrorHandling.getErrorMessage(mContext, iErrorCode, EventTypes.EVENT_SIGNUP_PERSONALDETAILS);
				showAlertDialog(errorMessage, this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.instacheques.gateway.IViewCallbackListner#onViewSuccess(com.dfc.instacheques.business.DataObject)
	 */
	@Override
	public void onViewSuccess(DataObject dataObject) {
		if (dataObject.getEventType() == EventTypes.EVENT_SIGNUP_PERSONALDETAILS)
		{
			Intent intent = new Intent(this, AccountCreatedActivity.class);
			intent.putExtra("user_name", mUserDetailDataObject.username);
			intent.putExtra("password", mUserDetailDataObject.password);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * this is used to display the datepickerdialog
	 * @param id
	 * @return dialog
	 */
	private Dialog showDatePickerDialog(int id) {
		if (id == Constants.DATE_DIALOG_ID) {

			mDatePicker = new DatePickerDialog(this, datePicker, mYear,
					mMonth, mDay) {
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					//onDateChanged
				}
			};
			mDatePicker.getDatePicker().setMaxDate(mCalendarInitDate.getTimeInMillis());
			if (mEtDOB.getText().toString() != null && !mEtDOB.getText().toString().equals("")) {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
					Date date = formatter.parse(mEtDOB.getText().toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					mDatePicker.getDatePicker().updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(!isDefaultDateIntialized){
				mDatePicker.getDatePicker().updateDate(1990, Calendar.JANUARY, 1);
			}
			mDatePicker.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
			return mDatePicker;
		}
		return null;
	}

	/*
	 * String forSercerFormat = "yyyy-MM-dd'T'HH:mm:ss";
	SimpleDateFormat sdfText = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	SimpleDateFormat sdfserver = new SimpleDateFormat(forSercerFormat, Locale.US);
	 */
	private void updateDateLabel() {
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mDate = mSdfserver.format(mCalendar.getTime());
		mEtDOB.setText(mSdfText.format(mCalendar.getTime()));//mDate);//
		/*	
		String strdate = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

		if (mCalendar != null) {
		strdate = sdf.format(mCalendar.getTime());
		 EtDOB.setText(strdate);
		}*/
	}

	/**
	 * this is used to navigate to privacy policy Activity 
	 */
	@OnClick(R.id.text_privacy_policy)
	public void privatePolicyClick()
	{
		Intent intent = new Intent(this, PrivacyPolicyActivty.class);
		startActivity(intent);
	}
	
	/**
	 * this is used to save the entered user details in preferences
	 */
	private void saveEnteredUserDetails() {

		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_FIRST_NAME, mEtUserFirstName.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_LAST_NAME, mEtUserLastName.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_EMAIL_ADDRESS, mEtEmailId.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_DOB, mEtDOB.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_POST_DOB, mDate);
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_SSN, mEtSSN.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_HOUSE_NUMBER, mEtHouseNumber.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_STREET, mEtUserStreet.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_CITY, mEtCity.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_STATE, mEtState.getSelectedItem().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_ZIP_CODE, mEtPostalCode.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_HOME_PHONE, mEtHomePhone.getText().toString());
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_CELL_PHONE, mEtCellPhone.getText().toString());
	
	}
	/**
	 * this is used to get the entered user details
	 */
	private void retainEnteredUserDetails() {
		mEtUserFirstName.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_FIRST_NAME));
		mEtUserLastName.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_LAST_NAME));
		mEtEmailId.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_EMAIL_ADDRESS));
		mEtDOB.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_DOB));
		mDate = mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_POST_DOB);
		mEtSSN.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_SSN));
		mEtHouseNumber.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_HOUSE_NUMBER));
		mEtUserStreet.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_STREET));
		mEtCity.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_CITY));
		
		String compareValue= mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_STATE);
        int index = 0;
        while(index < mEtState.getAdapter().getCount()){
        //	if (!compareValue.equals(null)) {
        	if (compareValue != null) {
    	        String spinnerState = (String) mEtState.getItemAtPosition(index);
    	        if(compareValue.equals(spinnerState)){
    	        	mEtState.setSelection(index);
    	        	break;
    	        }
    	    }
        	index++;
        }
		
		mEtPostalCode.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_ZIP_CODE));
		mEtHomePhone.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_HOME_PHONE));
		mEtCellPhone.setText(mPreferences.getString(PreferenceConstants.PERSONAL_DETAILS_CELL_PHONE));
		
	}
}