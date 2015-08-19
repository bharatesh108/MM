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

@SuppressLint("InflateParams")
public class NoMomentumCardActivity extends BaseActivity{

	@InjectView(R.id.store_locator_btn)
	Button mBtnStoreLocator;
	@InjectView(R.id.ok_btn)
	Button mBtnSupport;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_no_momentumcard);
		initUI();
		setUpActionBar();
	}
	
	/**
	 * this is used to initialize the UI Components of NoMomentumCard Screen
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
		mTitleTextView.setText("");
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}
	
	/**
	 * this is used to navigate to SignUpLocator Screen on the click of Store button click
	 */
	@OnClick(R.id.store_locator_btn)
	public void storeBtnClick()
	{
		//Navigate to store locator
		Intent intent = new Intent(this, SignUpLocatorActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.ok_btn)
	public void okBtnClick()
	{
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	
	public void homeBtnClick(View v) {
		okBtnClick();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		okBtnClick();
	}
	
}
