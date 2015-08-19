package com.dfc.moneymartus.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.activity.ContactUsActivity;
import com.dfc.moneymartus.view.activity.HomeActivity;

public class LearnMoreFragment extends BaseFragment{
	
	@InjectView(R.id.tv_store_locator_link)
	TextView mTvStoreLocatorLink;
	
	private void setUpActionBar() {
		((HomeActivity)getActivity()).setActionBarTitle(getResources().getText(R.string.navigation_learn_more).toString());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setUpActionBar();
	}

	@Override
	public void onActivityCreated(final Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.fragment_learn_more, container, false);
		initUI(mRootView);
		return mRootView;

	}

	private void initUI(View view) {
		ButterKnife.inject(this, view);
		mTvStoreLocatorLink.setPaintFlags(mTvStoreLocatorLink.getPaintFlags());
		mTvStoreLocatorLink.setTextColor(Color.parseColor(getResources().getString(R.color.moneymart_login_forgot_password_text)));
		mTvStoreLocatorLink.setText(Constants.URL_FIND_A_STORE);
		mTvStoreLocatorLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(getActivity(), ContactUsActivity.class);
				intent.putExtra("Url", Constants.URL_FIND_A_STORE);
				intent.putExtra("title", "");
				startActivity(intent);
			}
		});
	}

}

