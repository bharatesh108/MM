package com.dfc.moneymartus.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants;

public class TutorialOverlayActivity extends BaseActivity implements OnClickListener{

	Button mBtnOk;
	//	CheckCashingFragment mCheckCashingFragment;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.final_overlay);
		mPreferences = Preferences.getInstance(mContext);
		initUI();
	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		mBtnOk = (Button)findViewById(R.id.btn_final_overlay);
		mBtnOk.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		if (view == mBtnOk) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}


	@Override
	public void onBackPressed() {
		mPreferences.addOrUpdateBoolean("isFirstLauch", false);
		finish();
	}



}

