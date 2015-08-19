package com.dfc.moneymartus.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CheckByIdHandler;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;

@SuppressLint("InflateParams")
public class UploadingCheckDetailsActivity extends BaseActivity {

	private CheckByIdHandler checkByIDHandler;

	private TextView mTvChkAmt;
	private TextView mTvFunding;
	private TextView mTvDateSubmit;
	private TextView mTvDateComplete;

	private RelativeLayout mRelLayFrontChk;
	private RelativeLayout mRelLayBackChk;
	/*private Bundle bundle;
	private String frontImage;
	private String backImage;
	private double amount;
	private String dateSub;*/
	private IntentFilter mFilter;
	private CreateSubmitCheckReceiver mReceiver;
	private ProgressBar mProgressbar;

	@Override
	protected void onCreate(Bundle savedInstance) {

		super.onCreate(savedInstance);
		
		mFilter = new IntentFilter(CreateSubmitCheckReceiver.PROCESS_RESPONSE);
		mFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new CreateSubmitCheckReceiver();
		
		//bundle = getIntent().getExtras();
		setContentView(R.layout.layout_parent_capture_check);
		/*frontImage = bundle.getString("front_chk_img");
		backImage= bundle.getString("back_chk_img");
		amount= bundle.getDouble("amount");
		dateSub= bundle.getString("date_submited");*/

		setUpActionBar();
		initUI();
	}

	/**
	 * this is used to initialize the UI components
	 */
	private void initUI() {
		ImageView mIVTHeader = (ImageView)findViewById(R.id.iv_hearder_image);
		TextView mTvHaderTitle = (TextView)findViewById(R.id.tv_review_pending_custom_text);
		LinearLayout mLinLayTransactionInfo = (LinearLayout)findViewById(R.id.transaction_check_details_layout);
		TextView mTvChkMsg = (TextView)findViewById(R.id.tv_check_message_text);//gone
		Button mBtnAgree = (Button)findViewById(R.id.btn_action_required_submit);//
		Button mBTnCancel = (Button)findViewById(R.id.btn_action_required_cancel);//

		//Check details ui component
		mTvChkAmt =  (TextView)findViewById(R.id.tv_check_details_amount);
		mTvFunding =  (TextView)findViewById(R.id.tv_funding_to);
		mTvDateSubmit = (TextView)findViewById(R.id.tv_date_submitted);
		mTvDateComplete = (TextView)findViewById(R.id.tv_date_transaction_completed);

		//Capture check ui components
		mRelLayFrontChk = (RelativeLayout)findViewById(R.id.rl_front_check_capture);
		TextView mTvFrontChkImgBtn = (TextView)findViewById(R.id.tv_capture_check_front);
		TextView mTvCaptureFrontChk = (TextView)findViewById(R.id.tv_front_ok);

		mRelLayBackChk = (RelativeLayout)findViewById(R.id.rl_back_check_capture);
		TextView mTvBackChkImgBtn = (TextView)findViewById(R.id.tv_capture_check_back);
		TextView mTvCaptureBackChk = (TextView)findViewById(R.id.tv_back_ok);

		RelativeLayout mRelLayVoidChk = (RelativeLayout)findViewById(R.id.rl_void_check_capture);
		/*	mTvVoidChkImgBtn = (TextView)findViewById(R.id.tv_void_check_capture);
		mTvCaptureVoidChk = (TextView)findViewById(R.id.tv_void_check_details);*/

		//Transaction details ui component
		TextView mEtTransAmt = (TextView)findViewById(R.id.et_common_check_amount);
		TextView mEtTransChkMadeDate = (TextView)findViewById(R.id.et_common_check_made_date);
		Spinner mEtTransMomntCardNum = (Spinner)findViewById(R.id.details_spinner_momentum_card);
		mProgressbar = (ProgressBar)findViewById(R.id.upload_progress);

		mLinLayTransactionInfo.setVisibility(View.GONE);
		mProgressbar.setVisibility(View.VISIBLE);
		mIVTHeader.setVisibility(View.GONE);
		mTvHaderTitle.setVisibility(View.GONE);
		mBtnAgree.setVisibility(View.GONE);
		mBTnCancel.setVisibility(View.GONE);
		mEtTransAmt.setVisibility(View.GONE);
		mEtTransChkMadeDate.setVisibility(View.GONE);
		mEtTransMomntCardNum.setVisibility(View.GONE);
		mRelLayVoidChk.setVisibility(View.GONE);

		mTvFrontChkImgBtn.setVisibility(View.GONE);
		mTvCaptureFrontChk.setVisibility(View.GONE);
		mTvBackChkImgBtn.setVisibility(View.GONE);
		mTvCaptureBackChk.setVisibility(View.GONE);
		mTvChkMsg.setVisibility(View.GONE);
		refreshScreenData(checkByIDHandler);
		
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
		mTitleTextView.setText(R.string.navigation_check);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mFilter);
	}
	public class CreateSubmitCheckReceiver extends BroadcastReceiver{

		public static final String PROCESS_RESPONSE = "com.dfc.moneymart.action.PROCESS_RESPONSE";

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("BroadCast : Inside onReceive");
			mProgressbar.setVisibility(View.GONE);
		}

	}
	/**
	 * this is used to refresh the screen data
	 * @param checkByIDHandler
	 */
	private void refreshScreenData(CheckByIdHandler checkByIDHandler ) {
		ChecksObject checkHandlerDataObj = Constants.UPLOADING_STATE_CHECKBYID_OBJECT;
		mTvChkAmt.setText(getResources().getString(R.string.check_details_amount, "$"+checkHandlerDataObj.getAmount()));
		//mTvFunding.setText(getResources().getString(R.string.funding_to,checkByIDHandler.getCheckObj().getFundingMechanism()));
		mTvDateSubmit.setText(getResources().getString(R.string.date_submitted, checkHandlerDataObj.getDateMade()));
		//convert base 64 to img
		mRelLayFrontChk.setBackground(new BitmapDrawable(getResources(), getImage(checkHandlerDataObj.get_links().chequeImageFront.rel)));
		mRelLayBackChk.setBackground(new BitmapDrawable(getResources(), getImage(checkHandlerDataObj.get_links().chequeImageBack.rel)));
		mTvFunding.setText(getResources().getString(R.string.funding_to, "-"));
		mTvDateComplete.setText(getResources().getString(R.string.date_transaction_completed, "-"));
		showProgressBar();
	}
	
	/**
	 * this is used to show the progress bar while refreshing the screen data
	 */
	private void showProgressBar() {
		Resources res = mContext.getResources();
		Drawable drawable = res.getDrawable(R.drawable.upload_progressbar_drawable);
		mProgressbar.setProgress(25);   // Main Progress
		mProgressbar.setSecondaryProgress(50); // Secondary Progress
		mProgressbar.setMax(100); // Maximum Progress
		mProgressbar.setProgressDrawable(drawable);

		ObjectAnimator animation = ObjectAnimator.ofInt(mProgressbar, "progress", 0, 10, 20, 40, 80, 100);
		animation.setDuration(2000);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.start();
	}

	/**
	 * this is used to get the Bitmap Image
	 * @param image
	 * @return Bitmap
	 */
	private Bitmap getImage(String image) {
		byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	public void homeBtnClick(View v) {
		finish();
	}
}
