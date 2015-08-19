package com.dfc.moneymartus.view.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;

@SuppressLint("InflateParams")
public class EditBankAccountActivity extends BaseActivity {

	@InjectView(R.id.et_edit_account_number)
	EditText mAccountNumber;
	@InjectView(R.id.et_edit_routing_number)
	EditText mRoutingNumber;
	@InjectView(R.id.btn_save_account)
	Button mBtnSave;
	
	private MenuItem mMenuEdit;
	private MenuItem mMenuDelete;

	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_edit_bank_account);
		setUpActionBar();
		initLoginUI();
		setUpActionBar();

		Moneymart.getInstance().setInstanceToFinish(this);
	}

	/**
	 * this is used to initialize and set up the actionbar
	 */
	private void setUpActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new,
				null);
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(R.string.bank_account_title);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	/**
	 * this is used to initialize the UI component of Login
	 */
	private void initLoginUI() {
		ButterKnife.inject(this);
        enableEditText(mAccountNumber, false);
        enableEditText(mRoutingNumber, false);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mMenuEdit = menu.findItem(R.id.action_edit);
	    mMenuDelete = menu.findItem(R.id.action_delete);
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_account_bank_menu, menu);
	    return true;
	  } 
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_edit:
	    	changeMenuButtonVisibility(false);
	    	enableEditText(mAccountNumber, true);
	    	enableEditText(mRoutingNumber, true);
	    	mBtnSave.setVisibility(View.VISIBLE);
	      break;
	    case R.id.action_delete:
	    	showAlertDialog(getResources().getString(R.string.remove_account_success_msg), null);
	      break;
	    default:
	      break;
	    }

	    return true;
	  } 

	@OnClick(R.id.btn_save_account)
	public void saveBankAccount() {
		//todo
	}

	/**
	 * this is used to change the visibility of a menu button when required 
	 * @param reqVisibility
	 */
	public void changeMenuButtonVisibility(boolean reqVisibility){
		if(mMenuEdit!=null && mMenuDelete!=null){
			mMenuEdit.setVisible(reqVisibility);
			mMenuDelete.setVisible(reqVisibility);
		}
	}
	
	/**
	 * this is used to finish the activity on the click of a button
	 * @param v
	 */
	public void homeBtnClick(View v) {
		finish();
	}
	
	/**
	 * this method is used to enable the EditText for editable
	 * @param et
	 * @param editable
	 */
	public void enableEditText(EditText et, boolean editable){
		if(editable)
			et.setFocusableInTouchMode(editable);
		else
			et.setFocusable(editable);
	}
}
