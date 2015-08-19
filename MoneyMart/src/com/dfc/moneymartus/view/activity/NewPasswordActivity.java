package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.ResetPasswordHandler;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Validator;
import com.dfc.moneymartus.view.callback.ResetPasswordView;

@SuppressLint("InflateParams")
public class NewPasswordActivity extends BaseActivity implements ResetPasswordView {

	@InjectView(R.id.tv_security_question)
	TextView mTvSecurityQuestion;
	@InjectView(R.id.et_answer)
	EditText mEtAnswer;
	@InjectView(R.id.et_new_password)
	EditText mEtNewPassword;
	@InjectView(R.id.et_confirm_password)
	EditText mEtConfirmPassword;
	@InjectView(R.id.btn_set_new_password_submit)
	Button mBtnSubmit;
	@InjectView(R.id.tv_new_password)
	TextView mNewPswTitle;
	@InjectView(R.id.tv_confirm_password)
	TextView mConfPswTitle;
	@InjectView(R.id.tv_new_password_header_text)
	TextView mPswScrnTitle;

	private ResetPasswordHandler resetHandler;

	NotificationCompat.Builder builder;
	private boolean isSucess;
	private int statusCode;
	private String mStrUserName;

	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_set_new_password);
		setUpActionBar();
		initUI();
		String challengeStr = getIntent().getExtras().getString("challenge");
		mStrUserName = getIntent().getExtras().getString("UserName");
		String first = challengeStr;
		String next = "<font color='#FF4338'> *</font>";
		mTvSecurityQuestion.setText(Html.fromHtml(first + next));
		//		mTvSecurityQuestion.setText(challengeStr);
		resetHandler = new ResetPasswordHandler(this);
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
		mTitleTextView.setText(R.string.navigation_menu_set_new_password);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to initialize UI Components of Password Screen
	 */
	private void initUI() {
		ButterKnife.inject(this);
		mPswScrnTitle.setText(Html.fromHtml(this.getResources().getString(R.string.new_password_title_text)));
		mNewPswTitle.setText(Html.fromHtml(this.getResources().getString(R.string.new_password)));
		mConfPswTitle.setText(Html.fromHtml(this.getResources().getString(R.string.confirm_password)));
	}

	/**
	 * this is used to reset the password
	 */
	@OnClick(R.id.btn_set_new_password_submit)
	public void resetPassword() {
		if(isValid()){
			showLoader();
			String newPasswordStr = mEtNewPassword.getText().toString();
			String chStr = mEtAnswer.getText().toString();
			resetHandler.setReqEventType(EventTypes.EVENT_NEW_PASSWORD);
			resetHandler.requestNewPassword(mStrUserName, chStr, newPasswordStr);
		}
	}

	/**
	 * this is used to validate the new password ,confirmation for the password and security answer
	 * @return flag
	 */
	private boolean isValid() {
		boolean flag = true;
		String newPasswordStr = mEtNewPassword.getText().toString();
		String confirmPasswordStr = mEtConfirmPassword.getText().toString();
		String chStr = mEtAnswer.getText().toString();
		//PasswordValidator passwordValidater = new PasswordValidator();

		if (chStr == null || chStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.error_no_answer), this);
			flag = false;
		}
		else if (newPasswordStr == null || newPasswordStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}
		else if (confirmPasswordStr == null || confirmPasswordStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}
		else if (!newPasswordStr.equals(confirmPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password_match), this);
			flag = false;
		}
		else if (!Validator.validate(Validator.PASSWORD_PATTREN, newPasswordStr)) {
//		else if (!passwordValidater.validate(newPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}
		else if (!Validator.validate(Validator.PASSWORD_PATTREN, confirmPasswordStr)) {
//		else if (!passwordValidater.validate(confirmPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}


		return flag;
	}


	/**
	 * this is used to handle the error when reset password is fail
	 */
	@Override
	public void onResetPasswordFailure(VolleyError error, int eventType) {
		hideLoader();
	//	showAlertDialog(getResources().getString(R.string.error_reset_password_server), null);
		
		 statusCode = ErrorHandling.getStatusCode(error);
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
	 * @see com.dfc.moneymartus.view.callback.ResetPasswordView#onResetPasswordSuccess()
	 */
	@Override
	public void onResetPasswordSuccess() {
		hideLoader();
		isSucess  = true;
		showAlertDialog(getResources().getString(R.string.reset_password_success), this);
		Moneymart.getInstance().getInstanceToFinish().finish();
	}

	public void homeBtnClick(View v) {
		finish();
	}


	/**
	 * this is used to navigate to login Screen
	 */
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
			if(isSucess){
				Intent intent = new Intent(this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
			else{
				if(statusCode==401){
				this.finish();
				statusCode=0;
			    }
		    }
	    }
	}
}

