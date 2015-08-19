package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("InflateParams")
public class MomenCardConfActivity extends BaseActivity {

	@InjectView(R.id.btn_yes)
	Button mBtnYes;
	@InjectView(R.id.btn_no)
	Button mBtnNo;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_check_momentum_card);
		initUI();
		setUpActionBar();
	}
	
	/**
	 * this is used to initialize and set up the UI Components
	 */
	private void initUI() {
		ButterKnife.inject(this);
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
		mTitleTextView.setText(R.string.navigation_momentum_card);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * this is used to navigate to TermsConditions Screen
	 */
	@OnClick(R.id.btn_yes)
	public void yesBtnClick()
	{
		if (Util.isNetworkAvailable(this)) {
			Intent intent = new Intent(this, TermsConditonsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("Class", "MMMomenCardConfActivity");
			startActivity(intent);
			finish();
		} else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}
	
	/**
	 * this is used to navigate to NoMomentum Card Screen
	 */
	@OnClick(R.id.btn_no)
	public void noBtnClick()
	{
		if (Util.isNetworkAvailable(this)) {
			Intent intent = new Intent(this, NoMomentumCardActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}
	
	/**
	 * this is used to navigate to Login Screen
	 * @param v
	 */
	public void homeBtnClick(View v) {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		homeBtnClick(null);
	}
	
}
