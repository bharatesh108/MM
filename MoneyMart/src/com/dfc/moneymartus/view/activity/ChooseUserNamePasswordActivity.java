package com.dfc.moneymartus.view.activity;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DAOSecurityQuestions;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.SecurityQuestionDataObject;
import com.dfc.moneymartus.dto.SecurityQuestionDataObject.Questions;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.infra.Validator;
import com.dfc.moneymartus.view.callback.GetSecurityQuestionsView;

@SuppressLint("InflateParams")
public class ChooseUserNamePasswordActivity extends BaseActivity implements IViewCallbackListner, GetSecurityQuestionsView{

	@InjectView(R.id.tv_choose_username_mandatory)
	TextView mTvChooseUsernameLabel;
	@InjectView(R.id.tv_choose_username_text)
	TextView mTvChooseUsernameText;
	@InjectView(R.id.choose_specify_pwd_text)
	TextView mTvChoosePasswordText;
	@InjectView(R.id.confirm_pwd_message)
	TextView mTvConfirmPasswordMsg;
	@InjectView(R.id.tv_answer_text)
	TextView mTvAnswerText;
	@InjectView(R.id.tv_security_question_message)
	TextView mTvSecurityQuestionMessage;
	@InjectView(R.id.et_username)
	EditText mEtUserName;
	@InjectView(R.id.et_password)
	EditText mEtPassword;
	@InjectView(R.id.et_confirm_password)
	EditText mEtConfirmPassword;
	@InjectView(R.id.et_select_question)
	Spinner mEtSecurityQuestions;
	@InjectView(R.id.et_answer)
	EditText mEtAnswer;
	@InjectView(R.id.btn_next)
	Button mBtnNext;
	@InjectView(R.id.text_privacy_policy)
	TextView mTvPrivacyPolicy;
	
	private DAOSecurityQuestions daoSecurityQuestion;
	private final ArrayList<String> questionsList = new ArrayList<String>();
	private final ArrayList<String> questionsListId = new ArrayList<String>();
	

	@Override
	protected void onCreate(final Bundle savedInstance) {
		Log.e("terms", ":" + "onCreate");
		super.onCreate(savedInstance);
		daoSecurityQuestion = new DAOSecurityQuestions(this);
//		MMUtil.setActionBarTitle(this,
//				getResources().getString(R.string.lbl_terms_conditions)
//						.toString());
		setContentView(R.layout.activity_choose_username);
		setUpActionBar();
		mPreferences = Preferences.getInstance(this);
		initUI();

	}

	/**
	 * this is used to initialize and set up the action bar
	 */
	private void setUpActionBar() {
	/*	ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		View mainCustomActionBar = this.getLayoutInflater().inflate(
				R.layout.view_actionbarview_new, null);

		actionBar.setCustomView(mainCustomActionBar,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		TextView textViewTitle = (TextView)actionBar.getCustomView().findViewById(R.id.actionbar_title);
		textViewTitle.setText(R.string.navigation_choose_username_password);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);*/
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.navigation_choose_username_password);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to initialize the UI component
	 */
	private void initUI() {
		Log.e("terms", ":" + "initUI");
		ButterKnife.inject(this);
		ActionBar actionBar = this.getActionBar();
		actionBar.setIcon(R.drawable.back_btn);
		
		Util.addMandatoryAsterisk(mContext, R.string.choose_username_mandatory, mTvChooseUsernameLabel);
		Util.addMandatoryAsterisk(mContext, R.string.choose_username_text, mTvChooseUsernameText);
		Util.addMandatoryAsterisk(mContext, R.string.choose_specify_pwd_text, mTvChoosePasswordText);
		Util.addMandatoryAsterisk(mContext, R.string.confirm_pwd_message, mTvConfirmPasswordMsg);
		Util.addMandatoryAsterisk(mContext, R.string.answer_text, mTvAnswerText);
		Util.addMandatoryAsterisk(mContext, R.string.security_question_choose, mTvSecurityQuestionMessage);
		
	//	setNextFocus(etUserName);
	//	setNextFocus(etConfirmPassword);
		/*
		etUserName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!etUserName.hasFocus())  
					isValidUserName();
				else
					hasEdiTextFocus = false;
			}
		});
		
		etConfirmPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasEdiTextFocus)  
					isValidPassword();
			}
		});
		
		etPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)  
					hasEdiTextFocus = true;
			}
		});
		*/
	/*	etSecurityQuestions.setOnItemSelectedListener(new OnItemSelectedListener() {

	        public void onItemSelected(AdapterView<?> arg0, View arg1,
	                int position, long arg3) {
	            if (position == 0) {
	                txtSpinner1.setHint("Select Country");
	            } else {
	                txtSpinner1.setText(CountryList.get(position));
	            }

	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub
	            txtSpinner1.setHint("Select Country");
	        }

	    });*/
		
		prepareRequest();
	}

	/**
	 * this is used to validate the password
	 * @return flag 
	 */
	private boolean isValidPassword() {
		boolean flag = true;
		String newPasswordStr = mEtPassword.getText().toString();
		String confirmPasswordStr = mEtConfirmPassword.getText().toString();
	//	PasswordValidator passwordValidater = new PasswordValidator();
		
		if (newPasswordStr == null || newPasswordStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.mandatory_filed_error), this);
			flag = false;
		}
		else if (confirmPasswordStr == null || confirmPasswordStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.mandatory_filed_error), this);
			flag = false;
		}
		else if(!Validator.validate(Validator.PASSWORD_PATTREN, newPasswordStr)){
		//else if (!passwordValidater.validate(newPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}

		else if (!Validator.validate(Validator.PASSWORD_PATTREN, confirmPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password), this);
			flag = false;
		}

		else if (!newPasswordStr.equals(confirmPasswordStr)) {
			showAlertDialog(getResources().getString(R.string.error_reset_password_match), this);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * this is used to verify the answer provided on security question
	 * @return flag
	 */
	private boolean isValidAnswer() {
		boolean flag = true;
		String chStr = mEtAnswer.getText().toString().trim();
		
		if (chStr == null || chStr.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.error_no_answer), this);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * this is used to validate the username
	 * @return flag 
	 */
	private boolean isValidUserName() {
		boolean flag = true;
		String userName = mEtUserName.getText().toString();
//		UsernameValidator usernameValidater = new UsernameValidator();
//		Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
		
		if (userName == null || userName.isEmpty()) {
			showAlertDialog(getResources().getString(R.string.mandatory_filed_error), this);
			flag = false;
		}
		
	///	else if (!usernameValidater.validate(userName) /*|| special.matcher(userName).find()*/) {
		/*else if(!MMValidator.validate(MMValidator.USERNMAE_PATTERN, userName)) {
			showAlertDialog(getResources().getString(R.string.error_username_updated_msg), this);
			flag = false;
		}*/
			else if(userName.length()<8||userName.length()>20){
			showAlertDialog(getResources().getString(R.string.error_username_updated_msg), this);
			flag = false;
			}
		return flag;
	}

	/**
	 * this is used to submit the user details
	 */
	@OnClick(R.id.btn_next)
	public void submitUserDetails(){
		if(mEtSecurityQuestions.getAdapter() == null || mEtSecurityQuestions.getAdapter().getCount() == 0){
			showAlertDialog(getResources().getString(R.string.choose_security_question), ChooseUserNamePasswordActivity.this);
		}
		else if (isValidUserName() && isValidPassword() && isValidAnswer())
		{
					Intent intent = new Intent(this, UpdatePersonalDetailsActivity.class);
					intent.putExtra("user_name", mEtUserName.getText().toString());
					intent.putExtra("password", mEtConfirmPassword.getText().toString());
					intent.putExtra("challenge_id", questionsListId.get(mEtSecurityQuestions.getSelectedItemPosition()));
					intent.putExtra("answer", mEtAnswer.getText().toString());
					startActivity(intent);
		}
	}
	
	/**
	 * this is used to prepare the request for security question
	 */
	private void prepareRequest() {
		daoSecurityQuestion.setReqEventType(EventTypes.EVENT_SECURITY_QUESTIONS);
		processRequest(daoSecurityQuestion);
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
	 * this is used to finish the activity on the click of home button
	 * @param v
	 */
	public void homeBtnClick(View v) {
		//finish();
		handleNavigation();
	}
	
/*	public void setNextFocus(final EditText et){
		et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
	        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {

	            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
	            	switch (et.getId()) {
					case R.id.et_username:
						if(isValidUserName())
							etPassword.requestFocus();
						else
							etUserName.requestFocus();
						break;
					case R.id.et_confirm_password:
						if(!isValidPassword()){
							etPassword.setText("");
							etConfirmPassword.setText("");
							hasEdiTextFocus = true;
						}
						else
							etAnswer.requestFocus();
						break;
					case R.id.et_answer:
						if(!isValidAnswer())
							etAnswer.requestFocus();
						break;

					default:
						break;
					}
	                
	                return true;
	            } else {
	                return false;
	            }
	        }
	    });
	}*/

	@Override
	public void onViewSuccess() {
		// TODO Auto-generated method stub
		
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

	@Override
	public void onViewSuccess(DataObject dataObject) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.GetSecurityQuestionsView#onGetQuestionsFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetQuestionsFailure(VolleyError error, int eventType) {
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
	 * @see com.dfc.moneymartus.view.callback.GetSecurityQuestionsView#onGetQuestionsSuccess(com.dfc.moneymartus.dto.SecurityQuestionDataObject.Questions[])
	 */
	@Override
	public void onGetQuestionsSuccess(SecurityQuestionDataObject.Questions... questionsArray) {
		hideLoader();
		for(Questions q : questionsArray){
			questionsList.add(q.challenge);
			questionsListId .add(q.challengeId);
		}
	
	/*	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_data,questionsList);
		etSecurityQuestions.setAdapter(adapter); */
		
		final ArrayAdapter<String> spinnerArrayAdapter=new ArrayAdapter<String>(this, R.layout.spinner_data, android.R.id.text1, questionsList)
				  {
					
				@Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
					 final View v=super.getDropDownView(position,convertView,parent);
					    v.post(new Runnable()
					    {
					    @Override
					    public void run()
					      {
					      ((TextView)v.findViewById(android.R.id.text1)).setSingleLine(false);
					      }
					    });
					    return v;
					}
				  };
				  spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				  mEtSecurityQuestions.setAdapter(spinnerArrayAdapter);
	}
	
	/**
	 * this is used to start the privacypolicyActivity
	 */
	@OnClick(R.id.text_privacy_policy)
	public void privatePolicyClick()
	{
		Intent intent = new Intent(this, PrivacyPolicyActivty.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		handleNavigation();
	}
	
	private void handleNavigation() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
		
	}
	
	@Override
	protected void onResume() {
		Util.hideSoftKeyboard(this, mEtAnswer);
		super.onResume();
	}

	@Override
	protected void onPause() {
		Util.hideSoftKeyboard(this, mEtAnswer);
		super.onPause();
	}
}
