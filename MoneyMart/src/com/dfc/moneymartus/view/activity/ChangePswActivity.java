package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.UpdatePswDataObject;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.infra.Validator;

@SuppressLint("InflateParams")
public class ChangePswActivity extends BaseActivity implements IViewCallbackListner, OnClickListener {

	@InjectView(R.id.et_new_password)
	EditText mETextNewPassword;
	@InjectView(R.id.et_confirm_password)
	EditText mETextConfirmPassword;
	@InjectView(R.id.btn_submit)
	Button mBtnSubmit;
	@InjectView(R.id.tv_new_password_header_text)
	TextView mPswScrnTitle;
	@InjectView(R.id.tv_new_password)
	TextView mNewPswTitle;
	@InjectView(R.id.tv_confirm_password)
	TextView mConfPswTitle;
	private UpdatePswDataObject mUpdatePswDao;
	private String mNewPsw;
	private static final String TAG = "MMChangePswFragment";
	private boolean isSucess;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		//MMUtil.setActionBarTitle(this, getResources().getString(R.string.navigation_menu_change_psw).toString());
		setContentView(R.layout.change_password);
		mUpdatePswDao = new UpdatePswDataObject(this);
		setUpActionBar();
		initUI();
	}

	/**
	 * this is used to initialize the UI component
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void initUI() {
		ButterKnife.inject(this);
		Util.markFieldRequired(getResources().getString(R.string.confirm_hint_password));
		Util.markFieldRequired(getResources().getString(R.string.new_password_text));
		mPswScrnTitle.setText(Html.fromHtml(this.getResources().getString(R.string.new_password_header_text)));
		mNewPswTitle.setText(Html.fromHtml(this.getResources().getString(R.string.new_password)));
		mConfPswTitle.setText(Html.fromHtml(this.getResources().getString(R.string.confirm_password)));

		mETextNewPassword.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mETextNewPassword.requestFocus();
				mETextNewPassword.setFocusable(true);
				mETextNewPassword.setEnabled(true);
				mETextNewPassword.setCursorVisible(true);
				Util.showSoftKeyboard(ChangePswActivity.this);
				return false;
			}
		});
	}

	/**
	 * this is used to update the password
	 */
	@OnClick(R.id.btn_submit)
	public void updatePsw() {
		Log.e(TAG, ":" +"updatePsw");
		Util.hideSoftKeyboard(this, mETextConfirmPassword);

		/*	============
		 * mETextNewPassword.setText("P@ssword12");
		mETextConfirmPassword.setText("P@ssword12");
		===========*/

		mNewPsw = mETextNewPassword.getText().toString().trim();
		String mConfPsw = mETextConfirmPassword.getText().toString().trim();

		/*
		 * Testing with static string 
		 */
		//	validatePsw(mNewPsw, mConfPsw);

		/*if (!mNewPsw.isEmpty() && !mConfPsw.isEmpty() && mNewPsw.equals(mConfPsw)) {
			if(!MMUtil.isNetworkAvailable(this)) {

				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
			else {
				precessUpdatePsw();
			}
		}*/

		if (validatePsw(mNewPsw, mConfPsw) && mNewPsw.equals(mConfPsw)) {
			if(Util.isNetworkAvailable(this)) {
				precessUpdatePsw();
			}
			else {
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
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
		mTitleTextView.setText(R.string.navigation_menu_change_psw);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 *  this is used to validate the password 
	 * @param mNewPswStr
	 * @param mConfPswStr
	 * @return flag 
	 */
	private boolean validatePsw(String mNewPswStr, String mConfPswStr) {
		boolean flag = true;
		if (mNewPswStr == null || mNewPswStr.isEmpty()) {
			showAlertDialog(R.string.lbl_alert, getResources().getString(R.string.error_psw_empty), this, 0);
			flag = false;
		}
		else if (mConfPswStr == null || mConfPswStr.isEmpty()) {
			showAlertDialog(R.string.lbl_alert, getResources().getString(R.string.error_confirm_psw_empty), this, 0);
			flag = false;
		}
		else if (!Validator.validate(Validator.PASSWORD_PATTREN, mNewPswStr)) {
		//else if (!passwordValidater.validate(mNewPswStr)) {
			showAlertDialog(R.string.lbl_alert, getResources().getString(R.string.error_reset_password), this, 0);
			flag = false;
		}
		else if (!Validator.validate(Validator.PASSWORD_PATTREN, mConfPswStr)) {
//		else if (!passwordValidater.validate(mConfPswStr)) {
			showAlertDialog(R.string.lbl_alert, getResources().getString(R.string.error_reset_confirm_password), this, 0);
			flag = false;
		}

		else if (!mNewPswStr.equals(mConfPswStr)) {
			showAlertDialog(R.string.lbl_alert, getResources().getString(R.string.error_password_match), this, 0);
			flag = false;
		}
		return flag;
	}


	public void homeBtnClick(View v) {
		finish();
	}

	/**
	 *  this is used to process the request for updating the password
	 */
	private void precessUpdatePsw() {
		mUpdatePswDao.setPassword(mNewPsw);
		mUpdatePswDao.setReqEventType(EventTypes.EVENT_UPDATE_PASSWORD);
		processRequest(mUpdatePswDao);
	}

	/**
	 * this is used to show dialog while requesting is processing
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.PUT);
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
		hideLoader();
		isSucess = true;
		//showAlertDialog("Your passwrod has been sucessfully changed.", onClickListener);
		showAlertDialog(getResources().getString(R.string.change_password_success_msg), this);
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
			mDialog.dismiss();
			if(isSucess){
				finish();
			}
		}
	}
}
