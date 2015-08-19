package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CCServiceHandler;
import com.dfc.moneymartus.business.CoreServiceHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.dto.CCServicesDataObject;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.callback.ICCServiceView;
import com.dfc.moneymartus.view.callback.IServiceView;

@SuppressLint("InflateParams")
public class PrivacyPolicyActivty extends BaseActivity implements IServiceView, ICCServiceView {

	@InjectView(R.id.tv_privacy_policy)
	TextView mTvPrivatePolicy;

	//	private CustomerServicesDataObject coreServiceDataObj;
	//
	//	private CCServicesDataObject ccServiceDataObj;

	private String coreServicePrivatePolicy = "";

	private String ccServicePrivatePolicy = "";

	private boolean isCorePrivacyPolicyDataAvail;

	private boolean isCCPrivacyPolicyDataAvail;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_privacy_policy);

		initUI();
		setUpActionBar();
		checkCustServiceDataAvail();
//		checkCCServiceDataAvail();
		refreshScreenData();
	}

	private void checkCCServiceDataAvail() {
		if (Constants.CCServiceObj == null)
		{
			isCCPrivacyPolicyDataAvail = false;
			if(Util.isNetworkAvailable(this)) {
				processCCService();
			}
			else {
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}

		} else {
			//refershScreenData(Constants.CustServiceObj);
			ccServicePrivatePolicy = Constants.CCServiceObj.privacyPolicy;
			isCCPrivacyPolicyDataAvail = true;
		}		
	}

	private void checkCustServiceDataAvail() {
		if (Constants.mCustServiceObj == null)
		{
			isCorePrivacyPolicyDataAvail = false;
			if(Util.isNetworkAvailable(this)) {
				processCoreService();
			}
			else {
				showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}

		} else {
			//refershScreenData(Constants.CustServiceObj);
			coreServicePrivatePolicy = Constants.mCustServiceObj.privacyPolicy;
			isCorePrivacyPolicyDataAvail = true;
			checkCCServiceDataAvail();
		}		
	}

	/**
	 * Used to form request data body
	 */
	private void processService()
	{
		processCoreService();
		processCCService();
	}

	private void processCCService() {
		CCServiceHandler ccServiceHandler = new CCServiceHandler(this);
		ccServiceHandler.setReqEventType(EventTypes.EVENT_CC_SERVICE);
		processRequest(ccServiceHandler);		
	}

	private void processCoreService() {
		CoreServiceHandler serviceHandler = new CoreServiceHandler(this);
		serviceHandler.setReqEventType(EventTypes.EVENT_CORE_SERVICE);
		processRequest(serviceHandler);	
		checkCCServiceDataAvail();
	}

	/**
	 * Used to send request
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		dataObject.register(Method.GET);
	}

	/**
	 * Initialize ui components
	 */
	private void initUI() {
		ButterKnife.inject(this);
	}

	/**
	 * Used to set action bat title
	 */
	private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.navigation_menu_privacy_policy);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * Action bar button click event handling
	 * @param v View
	 */
	public void homeBtnClick(View v) {
		finish();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfcmoneymart.view.callback.IServiceView#onCoreServiceFailure(com.android.volley.VolleyError)
	 */
	@Override
	public void onCoreServiceFailure(VolleyError error, int eventType) {
		isCorePrivacyPolicyDataAvail = false;
		handleFailure(error, eventType);
		/*hideLoader();
		if (mDialog == null || !mDialog.isShowing()) {
			int statusCode = ErrorHandling.getStatusCode(error);
			String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
			if(statusCode == 401)
			{
				showAlertDialogUnAuthorised(errorMessage, this);
			} else {
				showAlertDialog(errorMessage, this);
			}
		}*/
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfcmoneymart.view.callback.IServiceView#onCoreServiceSuccess(com.dfcmoneymart.dto.CustomerServicesDataObject)
	 */
	@Override
	public void onCoreServiceSuccess(CustomerServicesDataObject dataObject) {
		//	hideLoader();
		coreServicePrivatePolicy = dataObject.privacyPolicy;
		isCorePrivacyPolicyDataAvail = true;
		refreshScreenData();
	}

	@Override
	public void onCCServiceFailure(VolleyError error, int eventType) {
		isCCPrivacyPolicyDataAvail = false;
		handleFailure(error, eventType);		
	}

	@Override
	public void onCCServiceSuccess(CCServicesDataObject dataObject) {
		ccServicePrivatePolicy = dataObject.privacyPolicy;
		isCCPrivacyPolicyDataAvail = true;
		refreshScreenData();
	}

	/**
	 * Used to refresh screen data
	 * @param dataObject
	 */
	private void refreshScreenData() {
		if (isCorePrivacyPolicyDataAvail && isCCPrivacyPolicyDataAvail) {
			hideLoader();
			mTvPrivatePolicy.setText(coreServicePrivatePolicy + "\n\n" + ccServicePrivatePolicy);
		}
	}

	private void handleFailure(VolleyError error, int eventType) {
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
}
