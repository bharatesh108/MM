package com.dfc.moneymartus.view.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants;

public class FrontTutorialActivity extends BaseActivity implements OnClickListener{

	Button mBtnFrontImage;
	//CheckCashingFragment mCheckCashingFragment;

	@Override
	protected void onCreate(Bundle savedInstance) {
		Log.e("FrontTutorialActivity",":"+"oncreate");
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
		/*	if (mPreferences.getBoolean("isFirstLauch", true)) {
			mBtnFrontImage.setText(R.string.capture_check_back);
		}else {
			mBtnFrontImage.setText(R.string.ok);
		}*/
		mBtnFrontImage.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		if(view == mBtnFrontImage) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		mPreferences.addOrUpdateBoolean("isFirstLauch", false);
		setResult(Activity.RESULT_OK);
		finish();
	}

}
