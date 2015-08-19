package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;

@SuppressLint("InflateParams")
public class ContactUsActivity extends BaseActivity {


	@InjectView(R.id.wv_store_locator)
	WebView mWebView;
	private String mTitle;
	private int mWebViewProgress = 100;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.fragment_store_locator);
		String url = getIntent().getExtras().getString("Url");
		mTitle = getIntent().getExtras().getString("title");
		
		initUI();
		if(url.equals(Constants.URL_LOAN_FORM)){
			Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_loan_services),
					getString(R.string.event_apply_laons));
		}
		else{
			Moneymart.getAppAnalytics().trackEvent(getString(R.string.category_contact_us),
					getString(R.string.event_contactus_visit_dfc_website));
		}
		setUpActionBar();
		setWebSettings();
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new WebViewClient());
		
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
	 * this is used to initialize the UI component
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
		mTitleTextView.setText(mTitle);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * this is used to finish the activity on the click of home button
	 * @param v
	 */
	public void homeBtnClick(View v) {
		finish();
	}
}
