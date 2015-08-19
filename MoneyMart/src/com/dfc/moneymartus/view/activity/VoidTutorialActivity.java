package com.dfc.moneymartus.view.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants;

public class VoidTutorialActivity extends BaseActivity implements OnClickListener{

	Button mBtnVoidImage;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.tutorial_void);
		mPreferences = Preferences.getInstance(mContext);
		initUI();
	}

	/**
	 * this is used to initialize the UI component
	 */
	private void initUI() {
		mBtnVoidImage = (Button)findViewById(R.id.btn_tutorial_void);
		mBtnVoidImage.setText(R.string.capture_check_void);
		mBtnVoidImage.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		//super.onClick(view);
		if (view == mBtnVoidImage) {
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
