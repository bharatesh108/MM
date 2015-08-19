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
import com.dfc.moneymartus.infra.Constants;

public class BackTutorialActivity  extends BaseActivity implements OnClickListener{

	Button mBackTutorialImage;
	//CheckCashingFragment mCheckCashingFragment;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.tutorial_back);
		mPreferences = Preferences.getInstance(mContext);
		initUI();
	}

	/**
	 * this is used to initialize the component of Tutorial Activity
	 */
	private void initUI() {
		mBackTutorialImage = (Button)findViewById(R.id.btn_tutorial_back);
		/*if (mPreferences.getBoolean("isFirstLauch", true)) {
			mBackTutorialImage.setText(R.string.capture_check_back);
		} else {
			mBackTutorialImage.setText(R.string.ok);
		}*/

		mBackTutorialImage.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		if (view == mBackTutorialImage) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}


	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		mPreferences.addOrUpdateBoolean("isFirstLauch", false);
		Intent intent  = new Intent(this, FrontTutorialActivity.class);
		startActivity(intent);
		finish();
	}

}
