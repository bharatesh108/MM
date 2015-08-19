package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.fragments.CheckCashingDetailsFragment;

@SuppressLint("InflateParams")
public class CheckCashingDetailsActivity extends BaseActivity implements OnClickListener {

	private int mActionRequiredStatus;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstance);
		//setContentView(R.layout.layout_parent_capture_check);
		setContentView(R.layout.activity_home);
		mActionRequiredStatus = getIntent().getExtras().getInt(Constants.mActionRequiredStatus);
		String mCheckId = getIntent().getExtras().getString("check_id");
		Bundle data = getIntent().getExtras();
		setUpActionBar();
		CheckCashingDetailsFragment chkCashDetailsFrag = new CheckCashingDetailsFragment();
		FragmentManager fm = getFragmentManager();  
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle1 = new Bundle();
		bundle1.putInt(Constants.mActionRequiredStatus, mActionRequiredStatus);
		bundle1.putString("check_id", mCheckId);
		bundle1.putString(Constants.CHECK_MADE_DATE, data.getString(Constants.CHECK_MADE_DATE));
		bundle1.putString(Constants.CHECK_MAKER_NAME, data.getString(Constants.CHECK_MAKER_NAME));
		bundle1.putString(Constants.CHECK_NUMBER, data.getString(Constants.CHECK_NUMBER));
		bundle1.putString(Constants.CHECK_AMOUNT,data.getString(Constants.CHECK_AMOUNT));
		bundle1.putString(Constants.CHECK_FUNDINGTO, data.getString(Constants.CHECK_FUNDINGTO));
		bundle1.putString(Constants.CHECK_FUNDINGTO_TYPE, data.getString(Constants.CHECK_FUNDINGTO_TYPE));
		bundle1.putString(Constants.CHECK_FEES_AGREED, data.getString(Constants.CHECK_FEES_AGREED));
		bundle1.putString(Constants.CHECK_DATE_SUBMIT, data.getString(Constants.CHECK_DATE_SUBMIT));
		bundle1.putString(Constants.CHECK_DATE_COMPLETE, data.getString(Constants.CHECK_DATE_COMPLETE));
		bundle1.putString(Constants.CHECK_FEES, data.getString(Constants.CHECK_FEES));
		bundle1.putString(Constants.CHECK_ID, data.getString(Constants.CHECK_ID));
		bundle1.putString(Constants.CHECK_AMOUNT_CURRENCY ,data.getString(Constants.CHECK_AMOUNT_CURRENCY));
		bundle1.putString(Constants.CHECK_FEES_CURRENCY ,data.getString(Constants.CHECK_FEES_CURRENCY));
		bundle1.putString(Constants.CHECK_FRONT_IMAGE_URL ,data.getString(Constants.CHECK_FRONT_IMAGE_URL));
		bundle1.putString(Constants.CHECK_BACK_IMAGE_URL ,data.getString(Constants.CHECK_BACK_IMAGE_URL));
		bundle1.putString(Constants.CHECK_VOID_IMAGE_URL ,data.getString(Constants.CHECK_VOID_IMAGE_URL));

		chkCashDetailsFrag.setArguments(bundle1);
		ft.replace(R.id.frame_container, chkCashDetailsFrag);
		ft.commit();

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
		mTitleTextView.setText(R.string.navigation_menu_cash_a_check_screen_title);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			
		case R.id.btn_right: 
			mDialog.dismiss();
			if(mDialogId!=null && mDialogId.equals(getResources().getString(R.string.error_confirm_void_chk))){
				mDialogId = null;
				finish();
			}
			break;
			
		case R.id.btn_left:
			mDialogId = null;
			mDialog.dismiss();
			break;	
			
		default:
			break;
		}
	};
	
	/**
	 *  this is used to finish the activity on the click of home button
	 * @param v
	 */
	public void homeBtnClick(View v) {
		if(mActionRequiredStatus == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT){
			showAlertDialog(getResources().getString(R.string.error_confirm_void_chk), getResources().getString(R.string.error_confirm_void_chk), getResources().getString(R.string.btn_ok), getResources().getString(R.string.btn_do_later),
					this);
		}
		else{
			finish();
		}
	}
}
