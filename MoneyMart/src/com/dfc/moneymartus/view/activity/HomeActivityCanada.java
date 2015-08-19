package com.dfc.moneymartus.view.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.data.ModelData;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.fragments.AccountDetailsFragment;
import com.dfc.moneymartus.view.fragments.BaseFragment;
import com.dfc.moneymartus.view.fragments.CardDetailsFragment;
import com.dfc.moneymartus.view.fragments.ContactMoneymartFragment;
import com.dfc.moneymartus.view.fragments.FaqMoneyMartFragment;
import com.dfc.moneymartus.view.fragments.LearnMoreFragment;
import com.dfc.moneymartus.view.fragments.LoanFragment;
import com.dfc.moneymartus.view.fragments.PersonalDetailsFragment;
import com.dfc.moneymartus.view.fragments.StoreLocatorFragment;

public class HomeActivityCanada extends BaseActivity implements OnClickListener {//, CheckCashingDetailsFragment.Communicator{//implements ICommunicator{

	@InjectView(R.id.tv_apply)
	TextView mTvApply;
	@InjectView(R.id.tv_card_services)
	TextView mTvCardServices;
	@InjectView(R.id.tv_learn_more)
	TextView mTvLearnMore;
	@InjectView(R.id.tv_personal_details)
	TextView mTvPersonalDetails;
	@InjectView(R.id.tv_account_details)
	TextView mTvAccountDetails;
	@InjectView(R.id.tv_contact_us)
	TextView mTvContactUs;
	@InjectView(R.id.tv_store_locator)
	TextView mTvStoreLocator;
	@InjectView(R.id.tv_faq)
	TextView mTvFAQ;
	@InjectView(R.id.tv_logout)
	TextView mTvLogOut;

	@InjectView(R.id.tr_apply)
	TableRow mTrApply;
	@InjectView(R.id.tr_card_services)
	TableRow mTrCardServices;
	@InjectView(R.id.tr_learn_more)
	TableRow mTrLearnMore;
	@InjectView(R.id.tr_personal_details)
	TableRow mTrPersonalDetails;
	@InjectView(R.id.tr_account_details)
	TableRow mTrAccountDetails;
	@InjectView(R.id.tr_contact_us)
	TableRow mTrContactUs;
	@InjectView(R.id.tr_store_locator)
	TableRow mTrStoreLocator;
	@InjectView(R.id.tr_faq)
	TableRow mTrFAQ;
	@InjectView(R.id.tr_logout)
	TableRow mTrLogOut;


	private Fragment fragment;


	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;


	// used to store app title
	private CharSequence mTitle;

	private String navMenuTitles[]; 

	//Current Drawer Menu Selection
	private TableRow highlightedTableRow;
	private TextView highlightedTextView;
	private Preferences mPreferences;

	private MyChecksDataObject mChecksDataObject;
	private String drawertitle = "";
	private MyChecksDataObject myCheckObjectForUpload;
	private final HashMap<String, MyChecksDataObject> myCheckUploadQueue = new HashMap<String, MyChecksDataObject>();
	private ArrayList<ChecksObject> myChecksListData = new ArrayList<ChecksObject>();

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_home);

		//CheckCashingDetailsFragment otherFragment = new CheckCashingDetailsFragment();
		//otherFragment.setCommunicator(this);

		initHomeUI();
		mTitle = getTitle();
		mPreferences = Preferences.getInstance(mContext);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle(R.string.navigation_menu_cash_a_check);
		getActionBar().setDisplayShowTitleEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.menu_btn_phn, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
				) {
			public void onDrawerClosed(View view) {
				//				getActionBar().setTitle(mTitle);
				//	MMUtil.setFragmentActionBarTitle(HomeActivity.this, mTitle.toString());
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				//				getActionBar().setTitle("");
				if(getDrawerTitle().isEmpty()) {
					Util.setFragmentActionBarTitle(HomeActivityCanada.this, getResources().getString(R.string.navigation_menu_cash_a_check));
				} 
				//				else
				//				{
				//	MMUtil.setFragmentActionBarTitle(HomeActivity.this, getDrawerTitle());
				//				}
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstance == null) {
			// on first time display view for first nav item
			setFocus();
		}
	}


	public MyChecksDataObject getMyCheckObjectForUpload() {
		return myCheckObjectForUpload;
	}

	public void setMyCheckObjectForUpload(MyChecksDataObject myCheckObjectForUpload) {
		this.myCheckObjectForUpload = myCheckObjectForUpload;
	}

	public HashMap<String, MyChecksDataObject> getMyCheckUploadQueue() {
		return myCheckUploadQueue;
	}

	public ArrayList<ChecksObject> getMyChecksListData() {
		return myChecksListData;
	}

	public void setMyChecksListData(ArrayList<ChecksObject> myChecksListData) {
		this.myChecksListData = myChecksListData;
	}

	private void initHomeUI() {
		ButterKnife.inject(this);

		mTvApply.setCompoundDrawablesWithIntrinsicBounds(R.drawable.apply_icon, 0, 0, 0);
		mTvCardServices.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_details, 0, 0, 0);
		mTvLearnMore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.learn_more, 0, 0, 0);
		mTvPersonalDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p_details, 0, 0, 0);
		mTvAccountDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_details, 0, 0, 0);
		mTvContactUs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_us, 0, 0, 0);
		mTvStoreLocator.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_locator, 0, 0, 0);
		mTvLogOut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logout, 0, 0, 0);
		mTvFAQ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.faq, 0, 0, 0);
		boolean isStateEnabled  = ModelData.getInstance().isEnabled;
		System.out.println("MMM"+isStateEnabled);
		/*if(!isStateEnabled){
//			mTvApply.setEnabled(false);
//			mTvApply.setTextColor(Color.GRAY);
		}*/

	}

	public Fragment getFragment() {
		return fragment;
	}

	@OnClick(R.id.tv_apply)
	public void goToLoansFragment(){
		displayView(Constants.MENU_LOAN_FRAGMENT);
	}

	@OnClick(R.id.tv_card_services)
	public void goToFundingAccountsFragment(){
		displayView(Constants.MENU_CARD_DETAILS_FRAGMENT);
	}

	@OnClick(R.id.tv_learn_more)
	public void goToLearnMoreFragment(){
		displayView(Constants.MENU_LEARN_MORE_FRAGMENTS);
	}

	@OnClick(R.id.tv_personal_details)
	public void goToPersonalDetailsFragment(){
		displayView(Constants.MENU_PERSONAL_DETAILS_FRAGMENT);
	}

	@OnClick(R.id.tv_account_details)
	public void goToAccountDetailsFragment(){
		displayView(Constants.MENU_ACCOUNT_DETAILS_FRAGMENT);
	}

	@OnClick(R.id.tv_contact_us)
	public void goToContactUsFragment(){
		displayView(Constants.MENU_CONTACT_US_FRAGMENT);
	}

	@OnClick(R.id.tv_store_locator)
	public void goToStoreLocatorFragment(){
		displayView(Constants.MENU_STORE_LOCATOR_FRAGMENT);
	}

	@OnClick(R.id.tv_faq)
	public void goToFAQFragment(){
		displayView(Constants.MENU_FAQ_FRAGMENT);
	}

	@OnClick(R.id.tv_logout)
	public void logout(){
		//mPreferences.clearAllPrefs();

		if(Moneymart.getInstance().myCheckUploadQueue.size() > 0){
			showAlertDialog("", getResources().getString(R.string.error_logout_upload_in_progress), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel), null);
		}
		else if(Moneymart.getInstance().failedChecksList.size() > 0){
			showAlertDialog("", getResources().getString(R.string.error_logout_failed_check_exists), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel), null);
		}
		else{
			showAlertDialog("", getResources().getString(R.string.lbl_logout_confirm_msg), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel), null);
		}
	}

	@SuppressLint("RtlHardcoded")
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("isFirstLaunch", ":"+mPreferences.getBoolean(Constants.isFirstLaunch));
		if (Constants.isOpenDrawer)
		{
			Constants.setOpenDrawer(false);
			mDrawerLayout.openDrawer(Gravity.LEFT);
			if(!mPreferences.getBoolean(Constants.isFirstLaunch))
				mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, true);
			Util.setFragmentActionBarTitle(HomeActivityCanada.this, getResources().getString(R.string.navigation_menu_my_checks));
		}

		/*	if(!isIntialLaunch){
			mDrawerLayout.openDrawer(Gravity.LEFT);
			isIntialLaunch = true;
			MMUtil.setFragmentActionBarTitle(HomeActivity.this, "");
		}*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
//		switch (item.getItemId()) {
//		default:
			return super.onOptionsItemSelected(item);
//		}
	}


	@Override
	public void setTitle(CharSequence title) {
		Log.e("title",":"+title);
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/*
	 * home fragment
	 */
	public void homeFragment() {
		//todo
	}

	public void cardDetailFragment() {
		Constants.setPrimary(true);
		fragment = new CardDetailsFragment();
	}

	public void learnMoreFragment() {
		Constants.setPrimary(true);
		fragment = new LearnMoreFragment();
	}

	public void personalDetailFragment() {
		Constants.setPrimary(true);
		fragment = new PersonalDetailsFragment();
		//	startActivity(new Intent(this, RegistrationActivity.class));
	}

	public void accountDetailFragment() {
		Constants.setPrimary(true);
		fragment = new AccountDetailsFragment();
	}

	public void contactUsFragment() {
		Constants.setPrimary(true);
		fragment = new ContactMoneymartFragment();
	}

	public void storeLocatorFragment() {
		Constants.setPrimary(true);
		fragment = new StoreLocatorFragment();
	}

	public void faqFragment() {
		Constants.setPrimary(true);
		fragment = new FaqMoneyMartFragment();
	}


	public void loanFragment() {
		Constants.setPrimary(true);
		fragment = new LoanFragment();
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position) {
		/*if (!Util.isNetworkAvailable(this) && position != 4) {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
		else
		{*/
		if(Util.isNetworkAvailable(this) || position == 4) {
			FragmentManager fragmentMgr = getFragmentManager();
			android.app.FragmentTransaction ft = fragmentMgr.beginTransaction();

			// update the main content by replacing fragments
			switch (position) {
			case Constants.MENU_LOAN_FRAGMENT:
				loanFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrApply, mTvApply);
				setDrawerTitle(getResources().getString(R.string.navigation_apply));
				break;
			case Constants.MENU_CARD_DETAILS_FRAGMENT:
				System.out.println("In card Details Fragment");
				cardDetailFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrCardServices, mTvCardServices);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_card_services));
				break;
			case Constants.MENU_LEARN_MORE_FRAGMENTS:
				System.out.println("In Learn More Fragment");
				learnMoreFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrLearnMore, mTvLearnMore);
				setDrawerTitle(getResources().getString(R.string.navigation_learn_more));
				break;	
			case Constants.MENU_PERSONAL_DETAILS_FRAGMENT:
				personalDetailFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrPersonalDetails, mTvPersonalDetails);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_personal_details));
				break;
			case Constants.MENU_ACCOUNT_DETAILS_FRAGMENT:
				accountDetailFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrAccountDetails, mTvAccountDetails);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_account_details));
				break;
			case Constants.MENU_CONTACT_US_FRAGMENT:
				contactUsFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrContactUs, mTvContactUs);
				setTitle(getResources().getString(R.string.navigation_menu_contact_us));
				break;
			case Constants.MENU_STORE_LOCATOR_FRAGMENT:
				storeLocatorFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrStoreLocator, mTvStoreLocator);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_store_locator));
				break;
			case Constants.MENU_FAQ_FRAGMENT:
				faqFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrFAQ, mTvFAQ);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_faq));
				break;

			default:
				break;
			}

			if (fragment != null) {
				ft.addToBackStack(null);
				ft.commit();
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawers();
			}
		} else {
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);
		}
	}

	public void replaceTabFragment(BaseFragment fragment){
		FragmentManager fragmentMgr = getFragmentManager();
		android.app.FragmentTransaction ft = fragmentMgr.beginTransaction();
		ft.replace(R.id.frame_container, fragment);
		ft.commit();
	}

	private void setDrawerTitle(String drawerTitle) {
		this.drawertitle = drawerTitle;
	}

	private String getDrawerTitle() {
		return drawertitle;
	}

	private void setFocus() {
		highlightedTableRow = mTrStoreLocator;
		highlightedTextView = mTvStoreLocator;
		displayView(Constants.MENU_STORE_LOCATOR_FRAGMENT);
		mTrStoreLocator.setBackgroundColor(Color.parseColor(getResources().getString(R.color.list_background_pressed)));
		mTvStoreLocator.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_locator_highlighted, 0, 0, 0);
		mTvStoreLocator.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
	}

	private void setSelectedDrawerItem(TableRow tr, TextView tv){
		tr.setBackgroundColor(Color.parseColor(getResources().getString(R.color.list_background_pressed)));
		tv.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		switch (tv.getId()) {
		case R.id.tv_apply:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.apply_icon_highlighted, 0, 0, 0);
			break;	
		case R.id.tv_card_services:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_details_highlighted, 0, 0, 0);
			break;
		case R.id.tv_learn_more:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.learn_more_higlighted, 0, 0, 0);
			break;	
		case R.id.tv_personal_details:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p_details_highlighted, 0, 0, 0);
			break;
		case R.id.tv_account_details:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_details_highlighted, 0, 0, 0);
			break;
		case R.id.tv_contact_us:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_us_highlighted, 0, 0, 0);
			break;
		case R.id.tv_store_locator:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_locator_highlighted, 0, 0, 0);
			break;
		case R.id.tv_faq:
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.faq_highlighted, 0, 0, 0);
			break;

		default:
			break;
		}

		if(tr!=null && !tr.equals(highlightedTableRow) && tv!=null && !tv.equals(highlightedTextView)){
			resetSelectedDrawerItem(highlightedTableRow, highlightedTextView);
			highlightedTableRow = tr;
			highlightedTextView = tv;
		}
	}

	private void resetSelectedDrawerItem(TableRow tr, TextView tv){
		if(tr!=null && tv!=null){
			tr.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
			tv.setTextColor(Color.parseColor(getResources().getString(R.color.list_text_color)));
			switch (tv.getId()) {
			case R.id.tv_apply:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.apply_icon, 0, 0, 0);
				break;	
			case R.id.tv_card_services:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_details, 0, 0, 0);
				break;
			case R.id.tv_learn_more:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.learn_more, 0, 0, 0);
				break;	
			case R.id.tv_personal_details:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p_details, 0, 0, 0);
				break;
			case R.id.tv_account_details:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_details, 0, 0, 0);
				break;
			case R.id.tv_contact_us:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_us, 0, 0, 0);
				break;
			case R.id.tv_store_locator:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_locator, 0, 0, 0);
				break;
			case R.id.tv_faq:
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.faq, 0, 0, 0);
				break;

			default:
				break;
			}
		}
	}

	public void setCaptureCheckObject(MyChecksDataObject obj){
		mChecksDataObject = obj;
	}

	public MyChecksDataObject getCaptureCheckObject(){
		return mChecksDataObject;
	}

	@Override
	public void onBackPressed() {

		if (getFragmentManager().getBackStackEntryCount() == 0) {
			//this.finish();
			logout();
		} else {
			//getFragmentManager().popBackStack();
			if (fragment instanceof StoreLocatorFragment) {
				//	 this.finish();
				logout();
			} else {
				FragmentManager fragmentMgr = getFragmentManager();
				android.app.FragmentTransaction ft = fragmentMgr.beginTransaction();	
				fragment = new StoreLocatorFragment();
				ft.replace(R.id.frame_container, fragment);
				ft.addToBackStack(null);
				//	ft.addToBackStack("MY_FRAGMENT");
				ft.commit();
			}

		}
	}

	public void setActionBarTitle(String txt){
		((TextView)getActionBar().getCustomView().findViewById(R.id.tv_screen_title)).setText(txt);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(final View view) {
		switch (view.getId()) {
		case R.id.btn_right:
			mDialog.dismiss();
			break;

		case R.id.btn_left:
			mPreferences.delete(PreferenceConstants.AUTORIZATION_TOKEN);
			Constants.setAuthTOken("");
			Constants.setCustServiceObj(null);
			Constants.setMyCheckServerData(null);
			Constants.setCCServiceObj(null);

			Moneymart.getInstance().myChecksList = new ArrayList<ChecksObject>();
			Moneymart.getInstance().myCheckUploadQueue = new LinkedHashMap<String, MyChecksDataObject>();
			Moneymart.getInstance().failedChecksList = new TreeMap<String, ChecksObject>();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	/*	@Override
	public void getDataFromCheckCashingDetailsFragment() {
		// TODO Auto-generated method stub

	}*/
}
