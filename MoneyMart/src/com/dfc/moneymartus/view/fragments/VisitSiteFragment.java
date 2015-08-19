package com.dfc.moneymartus.view.fragments;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BaseActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class VisitSiteFragment extends BaseFragment {

	@InjectView(R.id.wv_store_locator)
	WebView mWebView;
	private int mWebViewProgress = 100;

	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar() { 
		Util.setFragmentActionBarTitle((BaseActivity) getActivity(), getResources().getString(R.string.navigation_visit_site));
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		setUpActionBar();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		setRetainInstance(true);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.fragment_store_locator,
				container, false);
		initUI(mRootView);
	
		setWebSettings();
		if(Util.isNetworkAvailable(getActivity())){
		
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setUseWideViewPort(true);
			mWebView.loadUrl(Constants.VISITSITE_LINK);
		}else{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);
		}
		return mRootView;

	}

	private void setWebSettings() {
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == mWebViewProgress) {
					hideLoader();
				} else {
					showLoader(getResources().getString(R.string.pbar_text_loading));
				}
			}

		});

	}

	private void initUI(View view) {
		ButterKnife.inject(this, view);
	}

}
