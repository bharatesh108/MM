package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

@SuppressLint("InflateParams")
public class SignUpNoMomentumCard extends BaseActivity implements IServiceView, android.view.View.OnClickListener {
	
	private final static String TAG = "MMSignUpNoMomentumCard";

	@InjectView(R.id.tv_having_trouble_phone_number)
	TextView mTextViewPhNumber;
	@InjectView(R.id.tv_having_trouble_time)
	TextView mTextViewTime;
	@InjectView(R.id.tv_having_trouble_day_french)
	TextView mTextViewWeekFrench;
	@InjectView(R.id.tv_having_trouble_day)
	TextView mTextViewTimeWeek;
	@InjectView(R.id.tv_having_trouble_mail)
	TextView mTextViewEmail;
	@InjectView(R.id.tv_having_trouble_weburl)
	TextView mTextViewUrl;
	@InjectView(R.id.ok_btn)
	Button mBtnOk;
	@InjectView(R.id.store_locator_btn)
	Button mBtnStoreLocator;
	
	private String phoneStr;
	private String emailStr;
	private String webUrl;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_do_not_posses_momentum_card);
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
		mTitleTextView.setText(R.string.navigation_account_no_momntcard);
		mTitleTextView.setPadding(Util.pxTodpConversion(this,20), 0, 0, 0);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		ButterKnife.inject(this);
		mTextViewPhNumber.setOnClickListener(this);
		mTextViewUrl.setOnClickListener(this);
		mTextViewEmail.setOnClickListener(this);
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
	 * this is used to show the dialog while request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}

	/**
	 * this is used to  handle the error when core service request is fail
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
		Log.e(TAG,"onCoreServiceSuccess : "+dataObject.serviceName);
		Log.e(TAG,"onCoreServiceSuccess : "+dataObject.customerServices.email);
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

		//String openingHour = dataObject.customerServices.openingHours.monday.open;
		//Log.e(TAG,"onCoreServiceSuccess : "+ openingHour);
		//String closingHour = dataObject.customerServices.openingHours.monday.close;
		//Log.e(TAG,"onCoreServiceSuccess : "+ closingHour);
		/*try {
			String OpeningHoursText = MMUtil.parseTimingsFromServerResponse(openingHour, closingHour);
//			mTextViewTime.setText(OpeningHoursText);
			mTextViewTime.setText(getResources().getString(R.string.time_pst, OpeningHoursText));
			mTextViewTimeWeek.setText(mContext.getResources().getString(R.string.day_format));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		//String time;
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
	
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
		//calling the implicit intent of phone call
		else if (view.equals(mTextViewPhNumber)) {
			if(phoneStr!=null&&phoneStr.length()>0){
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData( Uri.parse("tel:" +phoneStr));
				mContext.startActivity(callIntent);
			}
		}
		//calling implicit intent of sending a mail
		else if (view.equals(mTextViewEmail)) {
			if(emailStr!=null&&emailStr.length()>0){
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", emailStr, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		}
		//starting a  ContactUs Activity with passing the Url and title as content
		else if (view.equals(mTextViewUrl)) {
			Intent browserIntent = new Intent(this,ContactUsActivity.class);
			browserIntent.putExtra("Url", webUrl);
			browserIntent.putExtra("title", getResources().getString(R.string.navigation_menu_contact_us));

			startActivity(browserIntent);
		}
	}
	
	/**
	 * this is used to navigate to sign up Locator Screen on the click of Store Button
	 */
	@OnClick(R.id.store_locator_btn)
	public void storeBtnClick()
	{
		//Navigate to store locator
		Intent intent = new Intent(this, SignUpLocatorActivity.class);
		startActivity(intent);
	}
	
	/**
	 * this is used to navigate to Login Activity on the click of OK Button
	 */
	@OnClick(R.id.ok_btn)
	public void okBtnClick()
	{
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	
	 @Override
	 public void onBackPressed() {
		 Intent intent = new Intent(this, LoginActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		 startActivity(intent);
		 finish();
	 }
}