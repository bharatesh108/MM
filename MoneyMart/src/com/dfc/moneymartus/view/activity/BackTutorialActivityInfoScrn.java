package com.dfc.moneymartus.view.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;

public class BackTutorialActivityInfoScrn  extends BaseActivity implements OnClickListener{

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
	 * this is used to initialize the component of Tutorial info
	 */
	private void initUI() {
		mBackTutorialImage = (Button)findViewById(R.id.btn_tutorial_back);
		mBackTutorialImage.setText(R.string.ok);

		mBackTutorialImage.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		if(view == mBackTutorialImage) {
			finish();
		}
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

