package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;

@SuppressLint("InflateParams")
public class SignUpLocatorActivity extends BaseActivity{

	@InjectView(R.id.wv_store_locator)
	WebView mWebView;
	private int mWebViewProgress = 100;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.fragment_store_locator);
		initUI();
		Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_contact_us),
				getString(R.string.event_contactus_visit_storelocator));
		setUpActionBar();
		launchWebView();
	}

	/**
	 * this is used to launch the webview
	 */
	private void launchWebView() {
		setWebSettings();
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.loadUrl(Constants.URL_FIND_A_STORE);
	}

	private void setWebSettings() {
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == mWebViewProgress) {
					hideLoader();
				} else {
					showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
				}
			}
		});
	}

	/**
	 * this is used to initialize the UI components
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
		mTitleTextView.setText(R.string.navigation_menu_store_locator);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}
	
	public void homeBtnClick(View v) {
		finish();
	}
	
}
