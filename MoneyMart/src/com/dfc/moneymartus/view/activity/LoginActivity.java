package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CustDetailDataObject;
import com.dfc.moneymartus.business.DAOLogin;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("ClickableViewAccessibility")
public class LoginActivity extends BaseActivity implements IViewCallbackListner{//ICommunicator{

	@InjectView(R.id.et_login_username)
	EditText mETextUserName;
	@InjectView(R.id.et_login_password)
	EditText mETextPassword;
	@InjectView(R.id.btn_login)
	Button mBtnLogin;
	@InjectView(R.id.tv_forgot_password)
	TextView mTvForgotPassword;
	@InjectView(R.id.rl_login)
	RelativeLayout mRootView;
	@InjectView(R.id.btn_need_help)
	Button mBtnSupport;
	@InjectView(R.id.btn_create_new)
	Button mBtnNewAccount;
	@InjectView(R.id.btn_location)
	Button mBtnLocation;
	@InjectView(R.id.text_privacy_policy)
	TextView mTvPrivacyPolicy;
	@InjectView(R.id.tv_app_version)
	TextView mTvAppVersion;
	
	private Preferences mPreferences;


	private String mUserName;
	private String mPassword;

	NotificationCompat.Builder builder;
	int index = 0;
	private DAOLogin daoLogin;
	
	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_login);
		
		mPreferences = Preferences.getInstance(this);
		clearData();
		daoLogin = new DAOLogin(this);
		
		initLoginUI();

	}

	private void clearData() {
		if(mPreferences != null)
		mPreferences.delete(PreferenceConstants.AUTORIZATION_TOKEN);
		Constants.setAuthTOken("");
		Constants.setCustServiceObj(null);
		Constants.setMyCheckServerData(null);
		Constants.setCCServiceObj(null);
	}

	/**
	 * this is used to initialize and set up the UI Components
	 */
	private void initLoginUI() {
		ButterKnife.inject(this);
		//mETextUserName.setText(mEditTextData);
		//mETextUserName.setCursorVisible(true);
		//mETextPassword.setText(mEditTextPassword);
		/*if (mRootView.findViewById(focusId) != null) {
			mRootView.findViewById(focusId).requestFocus();
		}*/
	/*	mETextUserName.setText("MobileTestUser003");
		mETextPassword.setText("P@ssword15");*/
		if(mTvAppVersion!=null){
			   mTvAppVersion.setText(Util.getVersionBuildNumber(this));
			}
			
			if(mETextUserName!=null){
			    mETextUserName.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mETextUserName.requestFocus();
					mETextUserName.setFocusable(true);
					mETextUserName.setEnabled(true);
					mETextUserName.setCursorVisible(true);
					Util.showSoftKeyboard(LoginActivity.this);
					return false;
				}
			   });
			}
	}
	
	/**
	 * this is used to navigate to Contact us screen if network available
	 */
	@OnClick(R.id.btn_location)
	public void startStoreLocator(){
		if(Util.isNetworkAvailable(this)){
			Intent intent  = new Intent(LoginActivity.this, ContactUsActivity.class);
			intent.putExtra("Url", Constants.URL_FIND_A_STORE);
			intent.putExtra("title", getResources().getString(R.string.navigation_menu_store_locator));
			startActivity(intent);
		}else{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}
	
	/**
	 * this is used to navigate to forgot password screen if network available
	 */
	@OnClick(R.id.tv_forgot_password)
	public void navigateToForgotPasswordActivity(){
		Util.hideSoftKeyboard(LoginActivity.this, mETextUserName);
		if(Util.isNetworkAvailable(this))
		{
			startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
		} else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

/*	@OnTouch(R.id.et_login_username)
	public boolean usernameTouch()
	{
		return false;
	}*/
	
	@OnClick(R.id.et_login_username)
	public void usernameClick()
	{
		//mETextUserName.setEnabled(true);
		//mETextUserName.setCursorVisible(true);
		
	}

	@OnClick(R.id.et_login_password)
	public void passwordClick()
	{
		mETextPassword.setCursorVisible(true);
	}
	
	/**
	 * this is used to process the Login if username and password is valid 
	 */
	@OnClick(R.id.btn_login)
	public void doLogin() {
		Util.hideSoftKeyboard(LoginActivity.this, mETextUserName);
		if(Util.isNetworkAvailable(this))
		{
			mUserName = mETextUserName.getText().toString().trim();
			mPassword = mETextPassword.getText().toString().trim();
			if (isValid(mUserName, mPassword)) {
				//createLoginRequest("", "");
				processLogin();
			}
		} else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

	/**
	 * this is used to navigate to support Trouble screen 
	 */
	@OnClick(R.id.btn_need_help)
	public void supportInfo()
	{
		Util.hideSoftKeyboard(LoginActivity.this, mETextUserName);
		if(Util.isNetworkAvailable(this))
		{
			Intent intent = new Intent(this, SupportTroubleActivity.class);
			intent.putExtra("Title", mContext.getString(R.string.navigation_menu_having_trouble));
			startActivity(intent);
		}
		else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

	/**
	 * this is used to navigate to Momentum Card Confirmation Screen
	 */
	@OnClick(R.id.btn_create_new)
	public void createnewAccount()
	{
		Util.hideSoftKeyboard(LoginActivity.this, mETextUserName);
		Intent intent = new Intent(this, MomenCardConfActivity.class);
		startActivity(intent);
		this.finish();
		Util.clearUserDetailsPreferences(mPreferences);
	}
	
	/**
	 * this is used to navigate to privacy policy Screen
	 */
	@OnClick(R.id.text_privacy_policy)
	public void privatePolicyClick()
	{
		Util.hideSoftKeyboard(LoginActivity.this, mETextUserName);
		Intent intent = new Intent(this, PrivacyPolicyActivty.class);
		startActivity(intent);
	}
	
	/**
	 * this is used to validate the username and password
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean isValid(final String username, final String password) {
		boolean flag = true;
		if (username == null || username.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.enter_username), this);
			flag = false;
		} else if (password == null || password.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.enter_login_password), this);
			flag = false;
		} 
		return flag;
	}

	/**
	 * this is used to process the login request
	 */
	private void processLogin() {
		daoLogin.setUserName(mUserName);
		daoLogin.setPassword(mPassword);
		daoLogin.setReqEventType(EventTypes.EVENT_LOGIN);
		processRequest(daoLogin);
	}

	/**
	 * this is used to show the dialog with Logging in text while login request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_logging_in));
		dataObject.register(Method.GET);
	}

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
//		new DownloadWebPageTask().execute();
		hideLoader();
		if(dataObject.getEventType() ==  EventTypes.EVENT_LOGIN_PERSONAL_DETAILS) {
			CustDetailDataObject perDetailObj = (CustDetailDataObject) dataObject;
			Constants.setUserData(perDetailObj);
			if (!perDetailObj.getTermsAgreed() || perDetailObj.getServiceList() != null 
					&& perDetailObj.getServiceList().size() > 0 
					&& !perDetailObj.getServiceList().get(0).getTermsAgreed())
			{
				Constants.setUserData(perDetailObj);
				Intent intent = new Intent(this, TermsConditonsActivity.class);
				intent.putExtra("Class","LoginActivity");
				startActivity(intent);

			}
			else
			{
				Constants.setOpenDrawer(true);
				Intent intent = new Intent(this, HomeActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				this.finish();
			}
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
		String errorMessage = ErrorHandling.getErrorMessage(this, statusCode, eventType);
		showAlertDialog(errorMessage, this);
		/*MMUtil.getServerErrorMessage(error);
		if(MMUtil.isServerErrorCode500(error))
		{
			showAlertDialog(getResources().getString(R.string.error_server), this);
		}
		else {
			showAlertDialog(getResources().getString(R.string.error_Login_api), this);
		}*/
	}

	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			this.finish();
		}
//	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
//	    @Override
//	    protected String doInBackground(String... urls) {
//	    try{
//	    	postFile("https://preview1.moneymart.ca/v1/cc/cheques");
//
//	        } catch (Exception e) {
//	          e.printStackTrace();
//	        }
//	      return null;
//	    }
//
//	    @Override
//	    protected void onPostExecute(String result) {
//	      
//	    }
//	  }
}