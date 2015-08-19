package com.dfc.moneymartus.view.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CCTermsDataObject;
import com.dfc.moneymartus.business.CoreTermsDataObject;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.TermCondUpdateUserHandler;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("InflateParams")
public class TermsConditonsActivity extends BaseActivity implements
IViewCallbackListner, OnCheckedChangeListener, View.OnClickListener {

	@InjectView(R.id.cb_accept)
	Switch mCbAccept;
	@InjectView(R.id.btn_continue)
	Button mBtnContinue;
	@InjectView(R.id.tv_terms_conditions_text)
	TextView mTermsConditions;

	private CCTermsDataObject mCCTermsDataObject;
	private CoreTermsDataObject mCoreTermsDataObject;

	//protected Dialog dialog;

	private TermCondUpdateUserHandler updateCustomerHandler;
	private String className;
	private boolean isCCTerms;
	private boolean isCoreTerms;


	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			className = extras.getString("Class");
		}

		//MMUtil.setActionBarTitle(this, getResources().getString(R.string.lbl_terms_conditions).toString());
		setContentView(R.layout.terms_conditions);
		setUpActionBar();
		mPreferences = Preferences.getInstance(this);
		mCCTermsDataObject = new CCTermsDataObject(this);
		mCoreTermsDataObject = new CoreTermsDataObject(this);
		initUI();
		formRequest();
	}

	/**
	 * this is used to form the request for CC Terms and Core Terms
	 */
	private void formRequest() {
		if(Util.isNetworkAvailable(this)) {
			mCCTermsDataObject.setReqEventType(EventTypes.EVENT_CC_TERMS);
			processRequest(mCCTermsDataObject, Method.GET);

			mCoreTermsDataObject.setReqEventType(EventTypes.EVENT_CORE_TERMS);
			processRequest(mCoreTermsDataObject, Method.GET);
		}
		else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			mCbAccept.setChecked(false);
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
		mTitleTextView.setText(R.string.lbl_terms_conditions);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to initialize and set up the action bar
	 */
	private void initUI() {
		ButterKnife.inject(this);
		ActionBar actionBar = this.getActionBar();
		actionBar.setIcon(R.drawable.back_btn);
		mCbAccept.setEnabled(false);
	}

	public void homeBtnClick(View v) {
		handleConfirmationDialog();
	}

	/**
	 * this is used to navigate to the Login Activity once terms and conditions evaluated or show the confirmaiton dialog with terms and conditions message
	 */
	private void handleConfirmationDialog() {
		if(mTermsConditions.getText().toString().isEmpty()){
			Intent intent = new Intent(TermsConditonsActivity.this,LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} else {

			showAlertDialog(this.getString(R.string.termsandcond_dialog_title), this.getString(R.string.terms_conditions_message),
					this, 0, getResources().getString(R.string.btn_ok), getResources().getString(R.string.btn_cancel), Constants.DialogId.TERAMS_AND_CONDITIONS);

		}
	}

	/**
	 * Shows a confirm dialog with "yes|ok" and "no|abort" options
	 * 
	 * @param message
	 * @param yesClicked
	 *            Callback that should be executed if "yes|ok" has been clicked
	 * @param noClicked
	 *            Callback that should be executed if "no|abort" has been
	 *            clicked
	 */
	public void showConfirmDialog(String message, OnClickListener yesClicked,
			OnClickListener noClicked) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(message)
		.setTitle(R.string.termsandcond_dialog_title)
		.setPositiveButton(android.R.string.cancel, yesClicked)
		.setNegativeButton(android.R.string.ok, noClicked)
		.setCancelable(false);
		AlertDialog alert = builder.create();
		alert.show();
	}


	/**
	 * this is used to show the dialog while request is processing
	 * @param dataObject
	 * @param reqMethod
	 */
	private void processRequest(DataObject dataObject, int reqMethod) {
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(reqMethod);
	}

	@OnClick(R.id.btn_continue)
	public void acceptTermsConditions() {
		if (Util.isNetworkAvailable(this)) {

			if ("LoginActivity".equals(className)) {
				updateCustomerHandler = new TermCondUpdateUserHandler(this);
				updateCustomerHandler
				.setReqEventType(EventTypes.EVENT_TERMCOND_UPDATE_USER);
				processRequest(updateCustomerHandler, Method.PUT);

			} else if ("MMMomenCardConfActivity".equals(className)) {
				Intent intent = new Intent(this,
						ChooseUserNamePasswordActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}

	@Override
	public void onViewSuccess() {
		//todo
	}

	@Override
	public void onViewSuccess(DataObject dataObject) {
		switch (dataObject.getEventType()) {
		case EventTypes.EVENT_CC_TERMS:
			String terms = null;
			CCTermsDataObject ccDataObject = (CCTermsDataObject) dataObject;

			if (ccDataObject.getTermsText() != null && !ccDataObject.getTermsText().isEmpty() ||
					mCoreTermsDataObject.getTermsText() != null && !mCoreTermsDataObject.getTermsText().isEmpty()) {
				// viewCallback.onViewSuccess(dataObject);
				terms = ccDataObject.getTermsText() +mCoreTermsDataObject.getTermsText();
				isCCTerms = true;
				refreshScreen(terms);

			} else {
				isCCTerms = true;
				refreshScreen(terms);
				//hideLoader();
			}

			break;
		case EventTypes.EVENT_CORE_TERMS:
			String coreterms = null;
			CoreTermsDataObject coreDataObject = (CoreTermsDataObject) dataObject;

			if (coreDataObject.getTermsText() != null && !coreDataObject.getTermsText().isEmpty()
					|| mCCTermsDataObject.getTermsText() != null && !mCCTermsDataObject.getTermsText().isEmpty()) {
				coreterms = mCCTermsDataObject.getTermsText() +coreDataObject.getTermsText();
				isCoreTerms = true;
				refreshScreen(coreterms);
			} else {
				isCoreTerms = true;
				refreshScreen(coreterms);
				//				hideLoader();
			}

			break;
		case EventTypes.EVENT_TERMCOND_UPDATE_USER:

			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}

	}

	/**
	 * this is used  to refresh the screen
	 * @param terms
	 */
	private void refreshScreen(String terms) {

		if (isCCTerms && isCoreTerms) {
			mTermsConditions.setText(terms);
			mCbAccept.setEnabled(true);
			mCbAccept.setOnCheckedChangeListener(TermsConditonsActivity.this);
			hideLoader();
		}

	}

	@Override
	public void onBackPressed() {
		if(mTermsConditions.getText().toString().isEmpty()) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} else {
			handleConfirmationDialog();
		}
	}


	@Override
	public void onViewFailure(VolleyError error, int eventType) {
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

	/*
	 * (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && !mTermsConditions.getText().toString().isEmpty()) {
			mBtnContinue.setEnabled(true);
			mBtnContinue.setClickable(true);
			mBtnContinue.setBackgroundResource(R.color.signin_header_bg);
		} else {
			mBtnContinue.setEnabled(false);
			mBtnContinue.setClickable(false);
			mBtnContinue.setBackgroundResource(R.color.personal_details_divider);
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btn_right){
			if(mDialogId != null && mDialogId.equals(Constants.DialogId.TERAMS_AND_CONDITIONS)) {
				Intent intent = new Intent(TermsConditonsActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				if ("MMMomenCardConfActivity".equals(className)){
					Intent intent = new Intent(this, MomenCardConfActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				} else if("LoginActivity".equals(className)){
					Intent intent = new Intent(this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				} 
			}

		} else if(view.getId() == R.id.btn_left){
			getDialog().dismiss();
		}
	}
}
