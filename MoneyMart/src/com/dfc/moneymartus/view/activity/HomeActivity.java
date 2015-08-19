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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.ChecksHandler;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.data.ModelData;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.fragments.AccountDetailsFragment;
import com.dfc.moneymartus.view.fragments.VisitSiteFragment;
import com.dfc.moneymartus.view.fragments.BaseFragment;
import com.dfc.moneymartus.view.fragments.CardDetailsFragment;
import com.dfc.moneymartus.view.fragments.CheckCashingFragment;
import com.dfc.moneymartus.view.fragments.ContactMoneymartFragment;
import com.dfc.moneymartus.view.fragments.FaqMoneyMartFragment;
import com.dfc.moneymartus.view.fragments.LoanFragment;
import com.dfc.moneymartus.view.fragments.MyChecksFragment;
import com.dfc.moneymartus.view.fragments.PersonalDetailsFragment;
import com.dfc.moneymartus.view.fragments.StoreLocatorFragment;

@SuppressLint("RtlHardcoded")
public class HomeActivity extends BaseActivity implements OnClickListener {//, CheckCashingDetailsFragment.Communicator{//implements ICommunicator{

	@InjectView(R.id.tv_cash_check)
	TextView mTvCheckCashing;
	@InjectView(R.id.tv_mychecks)
	TextView mTvMyChecks;
	@InjectView(R.id.tv_card_services)
	TextView mTvCardDetails;
	@InjectView(R.id.tv_personal_details)
	TextView mTvPersonalDetails;
	@InjectView(R.id.tv_apply)
	TextView mTvLoanApply;
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
	@InjectView(R.id.tl_navigation_drawer)
	TableLayout drawerMenu;
	@InjectView(R.id.tr_cash_check)
	TableRow mTrCheckCashing;
	@InjectView(R.id.tr_my_checks)
	TableRow mTrMyChecks;
	@InjectView(R.id.tr_card_services)
	TableRow mTrCardDetails;
	@InjectView(R.id.tr_personal_details)
	TableRow mTrPersonalDetails;
	@InjectView(R.id.tr_account_details)
	TableRow mTrAccountDetails;
	@InjectView(R.id.tr_contact_us)
	TableRow mTrContactUs;
	@InjectView(R.id.tr_apply)
	TableRow mTrLoanApply;
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
	ChecksHandler checkHandler;
	private MyChecksDataObject myCheckObjectForUpload;
	private final HashMap<String, MyChecksDataObject> myCheckUploadQueue = new HashMap<String, MyChecksDataObject>();
	private ArrayList<ChecksObject> myChecksListData = new ArrayList<ChecksObject>();
	private boolean isStateEnabled;

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
					Util.setFragmentActionBarTitle(HomeActivity.this, getResources().getString(R.string.navigation_menu_cash_a_check));
				} 
				
				if(fragment instanceof CheckCashingFragment){
					((CheckCashingFragment)fragment).removeFocus();
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

	public void retryCheckUpload(int position){
		((MyChecksFragment)fragment).performOnItemClick(position);
	}

	/**
	 * this is used to initialize the UI comoponent for Home Activity
	 */
	private void initHomeUI() {
		ButterKnife.inject(this);

		mTvCheckCashing.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash_acheck, 0, 0, 0);
		mTvMyChecks.setCompoundDrawablesWithIntrinsicBounds(R.drawable.my_checks, 0, 0, 0);
		mTvCardDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.funding_accounts, 0, 0, 0);
		mTvPersonalDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p_details, 0, 0, 0);
		mTvAccountDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_details, 0, 0, 0);
		mTvContactUs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_us, 0, 0, 0);
		mTvStoreLocator.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_locator, 0, 0, 0);
		mTvLogOut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logout, 0, 0, 0);
		mTvFAQ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.faq, 0, 0, 0);
		mTvLoanApply.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visit, 0, 0, 0);
		isStateEnabled  = ModelData.getInstance().isEnabled;
	//	System.out.println("MMM"+isStateEnabled);
		if(!isStateEnabled){
			mTvCheckCashing.setEnabled(false);
			mTvCheckCashing.setTextColor(Color.GRAY);
		}

	}

	public Fragment getFragment() {
		return fragment;
	}

	/**
	 * this is used to show the Fragment of CheckCashing on the click of check cash under the drawer menu
	 */
	@OnClick(R.id.tv_cash_check)
	public void fromDrawerMenuCheckCashingFragment(){
		Constants.setCheckCashingFragFromDrawer(true);
		goToCheckCashingFragment();
	}

	/**
	 * this is used to display the checkcashing fragment
	 */
	public void goToCheckCashingFragment(){
		mChecksDataObject = null;
		displayView(Constants.MENU_CHECK_CASHING_FRAGMENT);
	}

	/**
	 * this is used to display the mycheck fragment
	 */
	@OnClick(R.id.tv_mychecks)
	public void goToMyChecksFragment(){
		displayView(Constants.MENU_MY_CHECKS_FRAGMENT);
	}

	/**
	 * this is used to display the loan fragment screen
	 */
	@OnClick(R.id.tv_apply)
	public void goToLoansFragment(){
		displayView(Constants.MENU_LOAN_FRAGMENT);
	}


	/**
	 * this is used to display the funding accounts fragment
	 */
	@OnClick(R.id.tv_card_services)
	public void goToFundingAccountsFragment(){
		displayView(Constants.MENU_CARD_DETAILS_FRAGMENT);
		//	displayView(MMConstants.menu_funding_accounts_fragment);
	}

	/**
	 * this is used to display the personal details fragment screen
	 */
	@OnClick(R.id.tv_personal_details)
	public void goToPersonalDetailsFragment(){
		displayView(Constants.MENU_PERSONAL_DETAILS_FRAGMENT);
	}

	/**
	 * this is used to display the Account Details Fragment Screen
	 */
	@OnClick(R.id.tv_account_details)
	public void goToAccountDetailsFragment(){
		displayView(Constants.MENU_ACCOUNT_DETAILS_FRAGMENT);
	}

	/**
	 * this is used to display the Contact us Fragment Screen
	 */
	@OnClick(R.id.tv_contact_us)
	public void goToContactUsFragment(){
		displayView(Constants.MENU_CONTACT_US_FRAGMENT);
	}

	/**
	 * this is used to display the Store Locator Fragment Screen
	 */
	@OnClick(R.id.tv_store_locator)
	public void goToStoreLocatorFragment(){
		displayView(Constants.MENU_STORE_LOCATOR_FRAGMENT);
	}

	/**
	 * this is used to display the FAQ Fragment Screen
	 */
	@OnClick(R.id.tv_faq)
	public void goToFAQFragment(){
		displayView(Constants.MENU_FAQ_FRAGMENT);
	}

	/**
	 * this is used to show alert dialog 
	 */
	@OnClick(R.id.tv_logout)
	public void logout(){
		//mPreferences.clearAllPrefs();

		if(Moneymart.getInstance().myCheckUploadQueue.size() > 0){
			showAlertDialog("", getResources().getString(R.string.error_logout_upload_in_progress), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel),null);
		}
		else if(Moneymart.getInstance().failedChecksList.size() > 0){
			showAlertDialog("", getResources().getString(R.string.error_logout_failed_check_exists), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel),null);
		}
		else{
			showAlertDialog("", getResources().getString(R.string.lbl_logout_confirm_msg), this, 0, getResources().getString(R.string.lbl_menu_logout), getResources().getString(R.string.btn_cancel),null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.activity.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("isFirstLaunch", ":"+mPreferences.getBoolean(Constants.isFirstLaunch));
		//if (!mPreferences.getBoolean(MMConstants.isFirstLaunch))
		if(Constants.isOpenDrawer)
		{
			Constants.setOpenDrawer(false);
			mDrawerLayout.openDrawer(Gravity.LEFT);
			if (!mPreferences.getBoolean(Constants.isFirstLaunch))
				mPreferences.addOrUpdateBoolean(Constants.isFirstLaunch, true);
			Util.setFragmentActionBarTitle(HomeActivity.this, getResources().getString(R.string.navigation_menu_my_checks));
		}

		/*	if(!isIntialLaunch){
			mDrawerLayout.openDrawer(Gravity.LEFT);
			isIntialLaunch = true;
			MMUtil.setFragmentActionBarTitle(HomeActivity.this, "");
		}*/
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onConfigurationChanged(android.content.res.Configuration)
	 */
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

	/**
	 * this is used to get the data previously stored when check is uploading and 
	 * open the cashCheckFragment with passing this retrieved data
	 */
	public void cashCheckFragment() {
		Constants.setPrimary(true);
		if(mChecksDataObject == null){
			fragment = new CheckCashingFragment();
		}
		else
		{
			Bundle bundle = new Bundle();
			bundle.putString(Constants.CHECK_MAKER_NAME, mChecksDataObject.getmCheckMakerName());
			bundle.putString(Constants.CHECK_MADE_DATE, mChecksDataObject.getmCheckMadeDate());
			bundle.putString(Constants.CHECK_FRONT_IMAGE, mChecksDataObject.getmFrontCheckImage());
			bundle.putString(Constants.CHECK_BACK_IMAGE, mChecksDataObject.getmBackCheckImage());
			bundle.putDouble(Constants.CHECK_AMOUNT, mChecksDataObject.getmCheckAmount());
			fragment = new CheckCashingFragment();
			fragment.setArguments(bundle);
		}

	}

	public void myChecksFragment() {
		Constants.setPrimary(true);
		if(myCheckObjectForUpload == null){
			fragment = new MyChecksFragment();
		}
		else
		{
			Bundle bundle = new Bundle();
			bundle.putString(Constants.CHECK_FRONT_IMAGE, myCheckObjectForUpload.getmFrontCheckImage());
			bundle.putString(Constants.CHECK_BACK_IMAGE, myCheckObjectForUpload.getmBackCheckImage());
			bundle.putDouble(Constants.CHECK_AMOUNT, myCheckObjectForUpload.getmCheckAmount());
			bundle.putString(Constants.CHECK_MADE_DATE, myCheckObjectForUpload.getmCheckMadeDate());
			bundle.putString(Constants.CHECK_FUNDINGTO, myCheckObjectForUpload.getmFundingMechanismsId());
			fragment = new MyChecksFragment();
			fragment.setArguments(bundle);
		}


	}

	public void cardDetailFragment() {
		Constants.setPrimary(true);
		fragment = new CardDetailsFragment();
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
		//fragment = new LoanFragment();
		fragment = new VisitSiteFragment();
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position) {
		if (Util.isNetworkAvailable(this) || position == 4) {

			FragmentManager fragmentMgr = getFragmentManager();
			android.app.FragmentTransaction ft = fragmentMgr.beginTransaction();

			// update the main content by replacing fragments
			switch (position) {
			case Constants.MENU_CHECK_CASHING_FRAGMENT:
				if(isStateEnabled){
					cashCheckFragment();
					ft.replace(R.id.frame_container, fragment);
					setSelectedDrawerItem(mTrCheckCashing, mTvCheckCashing);
					setDrawerTitle(getResources().getString(R.string.navigation_menu_cash_a_check));
				}
				break;
			case Constants.MENU_MY_CHECKS_FRAGMENT:
				//if(isStateEnabled){
				myChecksFragment();
				ft.replace(R.id.frame_container, fragment);
				//	ft.addToBackStack("MY_FRAGMENT");//("MY_FRAGMENT");
				setSelectedDrawerItem(mTrMyChecks, mTvMyChecks);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_my_checks));
				//}
				break;
			case Constants.MENU_CARD_DETAILS_FRAGMENT:
				System.out.println("In card Details Fragment");
				cardDetailFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrCardDetails, mTvCardDetails);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_card_services));
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
			case Constants.MENU_LOAN_FRAGMENT:
				loanFragment();
				ft.replace(R.id.frame_container, fragment);
				setSelectedDrawerItem(mTrLoanApply, mTvLoanApply);
				setDrawerTitle(getResources().getString(R.string.navigation_apply));
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
		highlightedTableRow = mTrMyChecks;
		highlightedTextView = mTvMyChecks;
		displayView(Constants.MENU_MY_CHECKS_FRAGMENT);
		mTrMyChecks.setBackgroundColor(Color.parseColor(getResources().getString(R.color.list_background_pressed)));
		mTvMyChecks.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.my_checks_highlighted), null, null, null);
		mTvMyChecks.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
	}

	private void setSelectedDrawerItem(TableRow tr, TextView tv){
		tr.setBackgroundColor(Color.parseColor(getResources().getString(R.color.list_background_pressed)));
		tv.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		switch (tv.getId()) {
		case R.id.tv_cash_check:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cash_acheck_highlighted), null, null, null);
			break;
		case R.id.tv_mychecks:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.my_checks_highlighted), null, null, null);
			break;
		case R.id.tv_card_services:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.funding_accounts_highlighted), null, null, null);
			break;
		case R.id.tv_personal_details:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.p_details_highlighted), null, null, null);
			break;
		case R.id.tv_account_details:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.a_details_highlighted), null, null, null);
			break;
		case R.id.tv_contact_us:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.contact_us_highlighted), null, null, null);
			break;
		case R.id.tv_store_locator:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.map_locator_highlighted), null, null, null);
			break;
		case R.id.tv_faq:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.faq_highlighted), null, null, null);
			break;
		case R.id.tv_apply:
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.visit_highlighted), null, null, null);
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
			case R.id.tv_cash_check:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cash_acheck), null, null, null);
				break;
			case R.id.tv_mychecks:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.my_checks), null, null, null);
				break;
			case R.id.tv_card_services:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.funding_accounts), null, null,
						null);
				break;
			case R.id.tv_personal_details:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.p_details), null, null, null);
				break;
			case R.id.tv_account_details:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.a_details), null, null, null);
				break;
			case R.id.tv_contact_us:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.contact_us), null, null, null);
				break;
			case R.id.tv_store_locator:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.map_locator), null, null, null);
				break;
			case R.id.tv_faq:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.faq), null, null, null);
				break;
			case R.id.tv_apply:
				tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.visit), null, null, null);
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
			if (fragment instanceof MyChecksFragment) {
				//	 this.finish();
				logout();
			} else {
				FragmentManager fragmentMgr = getFragmentManager();
				android.app.FragmentTransaction ft = fragmentMgr.beginTransaction();	
				fragment = new MyChecksFragment();
				setSelectedDrawerItem(mTrMyChecks, mTvMyChecks);
				setDrawerTitle(getResources().getString(R.string.navigation_menu_my_checks));
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
		if(fragment instanceof CheckCashingFragment){
			mDrawerLayout.closeDrawers();
			fragment.onActivityResult(requestCode, resultCode, data);
		} else if (fragment instanceof AccountDetailsFragment) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		//super.onActivityResult(requestCode, resultCode, data);
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
