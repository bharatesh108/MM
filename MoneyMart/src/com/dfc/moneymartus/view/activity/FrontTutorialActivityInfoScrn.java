package com.dfc.moneymartus.view.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;

public class FrontTutorialActivityInfoScrn extends BaseActivity implements OnClickListener{

	Button mBtnFrontImage;
	//CheckCashingFragment mCheckCashingFragment;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.tutorial_front);
		mPreferences = Preferences.getInstance(mContext);
		initUI();
	}

	/**
	 * this is used to initialize the UI component
	 */
	private void initUI() {
		mBtnFrontImage = (Button)findViewById(R.id.btn_tutorial_front);
		mBtnFrontImage.setText(R.string.ok);
		mBtnFrontImage.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		if (view ==mBtnFrontImage) {
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
