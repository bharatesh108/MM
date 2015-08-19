package com.dfc.moneymartus.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.dfc.moneymartus.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		/**
		 * another thread to navigate to Login Activity while displaying the splash screen
		 */
		new Thread() {

			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// Handle Exception
				} finally {
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			};

		}.start();
	}

}

