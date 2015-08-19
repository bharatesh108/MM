package com.dfc.moneymartus.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;

public class VoidTutorialActivityInfoScrn  extends BaseActivity implements OnClickListener{

	Button mVoidTutorialImage;
	//CheckCashingFragment mCheckCashingFragment;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.tutorial_void);
		mPreferences = Preferences.getInstance(mContext);
		initUI();
	}
	
	/**
	 * this is used to initialize the component of Tutorial info
	 */
	private void initUI() {
		mVoidTutorialImage = (Button)findViewById(R.id.btn_tutorial_void);
		mVoidTutorialImage.setText(R.string.ok);
			
		mVoidTutorialImage.setOnClickListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		if(view == mVoidTutorialImage)
			finish();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

}

