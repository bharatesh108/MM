package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
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
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CustDetailDataObject;
import com.dfc.moneymartus.business.DAOLogin;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.ExtrasConstants;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("InflateParams")
public class AccountCreatedActivity extends BaseActivity implements IViewCallbackListner{

	@InjectView(R.id.btn_yes)
	Button mBtnYes;
	@InjectView(R.id.btn_no)
	Button mBtnNo;
	private String mPassword;
	private String mUsername;
	private Preferences mPreferences;
	private boolean mIsYesButtonClicked;


	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_account_created);
		mPassword = getIntent().getExtras().getString("password");
		mUsername = getIntent().getExtras().getString("user_name");
		setUpActionBar();
		initUI();
		Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_user_registration),
				getString(R.string.event_user_registration));
	}

	/*
	 * this is used to initialize and set up the UI of action bar
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
		mTitleTextView.setText(R.string.navigation_account_created);
		mTitleTextView.setPadding(Util.pxTodpConversion(this,20), 0, 0, 0);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * initialize the UI components of Account Creation
	 */
	private void initUI() {
		ButterKnife.inject(this);
	}

	@OnClick(R.id.btn_yes)
	public void yesBtnClick()
	{
		//	showAlertDialog("Feature coming soon", this);
		if(Util.isNetworkAvailable(this))
		{
			processLogin();
			mIsYesButtonClicked  = true;
		}else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}
	@OnClick(R.id.btn_no)
	public void laterBtnClick()
	{
		/*Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();*/
		if(Util.isNetworkAvailable(this))
		{
			processLogin();
			mIsYesButtonClicked  = false;
		}else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

	/**
	 * this is used to Initiate the login process with given input username and password
	 */
	private void processLogin() {
		mPreferences = Preferences.getInstance(this);
		DAOLogin daoLogin = new DAOLogin(this);

		daoLogin.setUserName(mUsername);
		daoLogin.setPassword(mPassword);
		daoLogin.setReqEventType(EventTypes.EVENT_LOGIN);
		processRequest(daoLogin);
	}

	/**
	 * this is used to show the dialog while  request is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
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
				if(mIsYesButtonClicked){
					mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, false);
					Constants.setOpenDrawer(false);
					Intent intent = new Intent(this, AddBankAccountActivity.class);
					intent.putExtra(ExtrasConstants.SIGNUPTOBANK, ExtrasConstants.SIGNUPTOBANK);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					this.finish();
				} else {
					mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, false);
					Constants.setOpenDrawer(true);
					Intent intent = new Intent(this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					this.finish();	
				}
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
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			if (mDialogMessage.contains(this.getString(R.string.error_network))) {
				mDialog.dismiss();
			} else {
				mDialog.dismiss();
				Intent intent = new Intent(this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}
