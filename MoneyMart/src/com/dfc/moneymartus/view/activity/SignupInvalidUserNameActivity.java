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

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Util;

@SuppressLint("InflateParams")
public class SignupInvalidUserNameActivity extends BaseActivity {
	
	@InjectView(R.id.btn_ok)
	Button mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_invalid_user);
		setUpActionBar();
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
		ImageView mBackBtn = (ImageView) mCustomView.findViewById(R.id.homeBtn);
		mBackBtn.setVisibility(View.GONE);
		mTitleTextView.setText(R.string.navigation_account_invalid_username);
		mTitleTextView.setPadding(Util.pxTodpConversion(this,20), 0, 0, 0);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		ButterKnife.inject(this);
	}
	
	/**
	 * this is used to navigate to Login Screen on the click of OK Button
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