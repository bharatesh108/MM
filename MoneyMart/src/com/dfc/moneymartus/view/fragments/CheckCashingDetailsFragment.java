package com.dfc.moneymartus.view.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.GetFundingMechanismHandler;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.business.RecaptureCheckHandler;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.FundingMechanismDataObject;
import com.dfc.moneymartus.gateway.DownloadCheckImage;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.OnBackPressedListener;
import com.dfc.moneymartus.infra.USCurrencyFormatter;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BackTutorialActivity;
import com.dfc.moneymartus.view.activity.BackTutorialActivityInfoScrn;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.FrontTutorialActivity;
import com.dfc.moneymartus.view.activity.FrontTutorialActivityInfoScrn;
import com.dfc.moneymartus.view.activity.HomeActivity;
import com.dfc.moneymartus.view.activity.TutorialOverlayActivity;
import com.dfc.moneymartus.view.activity.VoidTutorialActivity;
import com.dfc.moneymartus.view.activity.VoidTutorialActivityInfoScrn;
import com.dfc.moneymartus.view.callback.FundingMechanismView;
import com.dfc.moneymartus.view.callback.RecaptureCheckView;
import com.miteksystems.misnap.MiSnap;
import com.miteksystems.misnap.MiSnapAPI;

@SuppressLint("HandlerLeak")
public class CheckCashingDetailsFragment extends BaseFragment implements OnClickListener, FundingMechanismView, RecaptureCheckView, OnBackPressedListener{

	private static final int FRONT_CHECK_IMG_MISNAP = 0;
	private static final int BACK_CHECK_IMG_MISNAP = 1;
	@InjectView(R.id.tv_capture_check_front)
	TextView mBtnCaptureFront;
	@InjectView(R.id.tv_capture_check_back)
	TextView mBtnCaptureBack;
	@InjectView(R.id.et_check_amount)
	EditText mEtCheckAmount;
	@InjectView(R.id.et_check_made_date)
	EditText mEtCheckMadeDate; 
	@InjectView(R.id.tv_momentum_card_text)
	TextView mTvMomentumCard;
	@InjectView(R.id.tv_confirm_check_amount)
	TextView mTvConfirmCheckAmount;
	@InjectView(R.id.iv_front_check_tutorial_image)
	ImageView mIvFrontCheckImage;
	@InjectView(R.id.iv_back_check_tutorial_image)
	ImageView mIvBackCheckImage;
	@InjectView(R.id.iv_confirm_check_tutorial_image)
	ImageView mIvConfirmCheckAmountImage;
	@InjectView(R.id.spinner_momentum_card)
	Spinner mSpinnerMomentumCard;
	@InjectView(R.id.btn_submit)
	Button mBtnSubmit;
	@InjectView(R.id.relative_front_check_capture)
	RelativeLayout mRLfrontCheck;
	@InjectView(R.id.relative_back_check_capture)
	RelativeLayout mRLbackCheck;

	private View mRootView;
	private Activity mContext;
	private Preferences mPreferences;

	private String mFrontCheck = "";
	private String mBackCheck  = "";
	private String mVoidFrontCheck;

	private DatePickerDialog mDatePicker;
	private String mSubmitISODate;
	String forSercerFormat = "yyyy-MM-dd'T'HH:mm:ss";
	SimpleDateFormat sdfISOFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//	SimpleDateFormat sdfserver = new SimpleDateFormat(forSercerFormat, Locale.US);
	SimpleDateFormat sdfEditTextDisplayFormatDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

	private Calendar mCalendar;
//	String mDate = "";
	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;

	Handler mHandler;
	boolean mStarted = false;
	File file;

	private Bundle mBundle;
	private String mCheckMakerName;
	private String mCheckNumber;
	private String mCheckMadeDate;
	private String mCheckSubmittedDate;
	private String mCheckCompletedDate;
	private String mCheckAmount;
	private String mCheckId;
	private String mFeesAgreed;

	private boolean isTutorialCall;
	private int mActionRequiredStatus = 0;
	private ImageView mIVTHeader;
	private TextView mTvHaderTitle;
	private LinearLayout mLinLayCapture;
	private LinearLayout mLinLayTransactionInfo;
	private LinearLayout mLinLiayCheckDetails;
	private TextView mTvChkMsg;
	private Button mBtnAgree;
	private Button mBTnCancel;
	private TextView mTvChkAmt;
	private TextView mTvFunding;
	private TextView mTvDateSubmit;
	private TextView mTvDateComplete;
	private RelativeLayout mRelLayFrontChk;
	private TextView mTvCaptureFrontChk;
	private TextView mTvFrontChkImgBtn;
	private RelativeLayout mRelLayBackChk;
	private TextView mTvCaptureBackChk;
	private TextView mTvBackChkImgBtn;
	private RelativeLayout mRelLayVoidChk;
	private TextView mTvCaptureVoidChk;
	private TextView mTvVoidChkImgBtn;
	private EditText mEtTransAmt;
	private TextView mEtTransChkMadeDate;
	private Spinner mEtTransMomntCardNum;
	private String mChkAmtCurrency;
	private String mChkFees;
	private String mChkFeesCurrency;

	Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message message) {

			switch (message.what) {

			case FRONT_CHECK_IMG_MISNAP:
				handleFrontCheckImgSnap(message);
				break;

			case BACK_CHECK_IMG_MISNAP:
				handleBackCheckImgSnap(message);
				break;
			default:
				break;
			}
		}
	};
	private ImageView mIvVoidCheckImage;
	private ImageView mIvFrontCheckTutImage;
	private ImageView mIvBackCheckTutImage;
	private String mFundingMechanismID;
	private String mFundingMechanismType = "";
	private ArrayList<String> mFundingMechanismIDList = new ArrayList<String>();


	/**
	 * If first launch then after successful capture of front check image app will navigate to back tutorial screen 
	 * else it will show captured check image in the cash a check screen
	 */
	private void handleFrontCheckImgSnap(Message message) {
		byte[] imageData = (byte[]) message.obj;

		if(mRLfrontCheck == null){
			mTvFrontChkImgBtn.setVisibility(View.GONE);
			mTvCaptureFrontChk.setVisibility(View.GONE);
			mRelLayFrontChk.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length)));
		}
		else {
			mBtnCaptureFront.setVisibility(View.INVISIBLE);
			mRLfrontCheck.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length)));

		}
		if(isTutorialCall  && mPreferences.getBoolean("isFirstLauch", true)){
			isTutorialCall = true;
			Intent intent  = new Intent(getActivity(), BackTutorialActivity.class);
			getActivity().startActivityForResult(intent, 891);
			//mPreferences.addOrUpdateBoolean("isFirstLauch", false);
		}
	}


	/**
	 * If first launch then after successful capture of back check image app will navigate to amount information tutorial screen 
	 * else it will show captured check image in the cash a check screen
	 */
	private void handleBackCheckImgSnap(Message message) {
		byte[] backImageData = (byte[]) message.obj;
		if(mRLbackCheck == null) {
			mTvBackChkImgBtn.setVisibility(View.GONE);
			mTvCaptureBackChk.setVisibility(View.GONE);
			mRelLayBackChk.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(backImageData, 0, backImageData.length)));

		}
		else {
			mBtnCaptureBack.setVisibility(View.INVISIBLE);					
			mRLbackCheck.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(backImageData, 0, backImageData.length)));
		}
		if(isTutorialCall){
			if (mPreferences.getBoolean("isFirstLauch", true)) {
				isTutorialCall = true;
				Intent intent  = new Intent(getActivity(), TutorialOverlayActivity.class);
				getActivity().startActivityForResult(intent, 892);
				mPreferences.addOrUpdateBoolean("isFirstLauch", false);
			}
			isTutorialCall = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
		mPreferences = Preferences.getInstance(mContext);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		boolean hasCheckId = false;
		Bundle checkBundle = getArguments();
		if(checkBundle!=null  && checkBundle.containsKey("check_id")){
			hasCheckId = true;
		}
		
		mRootView = inflater.inflate(R.layout.fragment_cash_check, container, false);
		initUI(mRootView);
		
		if (mPreferences.getBoolean("isFirstLauch", true) && !hasCheckId) {
			// Do first run stuff here then set 'firstrun' as false
			// using the following line to edit/commit prefs
			isTutorialCall = true;
			Intent intent  = new Intent(getActivity(), FrontTutorialActivity.class);
			startActivityForResult(intent, 890);

		}
		else
		{
			SimpleDateFormat isodate = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss", Locale.US);
			mBundle = getArguments();
			if (mBundle != null) {
				mCheckMakerName = mBundle.getString(Constants.CHECK_MAKER_NAME);
				mCheckNumber =  mBundle.getString(Constants.CHECK_NUMBER);
				mCheckAmount = mBundle.getString(Constants.CHECK_AMOUNT);
				mFrontCheck = mBundle.getString(Constants.CHECK_FRONT_IMAGE);
				mBackCheck = mBundle.getString(Constants.CHECK_BACK_IMAGE);
				mActionRequiredStatus = mBundle.getInt(Constants.mActionRequiredStatus);
				mCheckId = mBundle.getString(Constants.CHECK_ID);
				mChkAmtCurrency = mBundle.getString(Constants.CHECK_AMOUNT_CURRENCY);
				mChkFees = mBundle.getString(Constants.CHECK_FEES);
				mFeesAgreed = mBundle.getString(Constants.CHECK_FEES_AGREED);
				mChkFeesCurrency = mBundle.getString(Constants.CHECK_FEES_CURRENCY);
				mCheckSubmittedDate = mBundle.getString(Constants.CHECK_DATE_SUBMIT);
				mCheckCompletedDate = mBundle.getString(Constants.CHECK_DATE_COMPLETE);
				mFundingMechanismID = mBundle.getString(Constants.CHECK_FUNDINGTO);
				if(mBundle.getString(Constants.CHECK_FUNDINGTO_TYPE)!=null){
					mFundingMechanismType = mBundle.getString(Constants.CHECK_FUNDINGTO_TYPE);
				}
				try{
					Date mCheckDate = isodate.parse(mBundle.getString(Constants.CHECK_MADE_DATE));
					mCheckMadeDate = sdfISOFormatDate.format(mCheckDate);
				}catch(Exception e){
					Log.e("Check Cashing Fragment","CheckDate"+e.getMessage());
				}

			}
			mRootView = checkActionStatus(container, inflater);
		}
		((BaseActivity)mContext).setOnBackPressedListener(this); 

		return mRootView;

	}

	/**
	 * Based on check action status app will screen UI data
	 * @param container contain other views
	 * @param inflater Instantiates a layout 
	 * @return View used to create UI components 
	 */
	private View checkActionStatus(ViewGroup container, LayoutInflater inflater) {
		switch (mActionRequiredStatus) {

		case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT:
		case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK:
		case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK:
			mRootView = inflater.inflate(R.layout.layout_parent_capture_check, container, false);
			initCommonUI(mRootView);
			initUIResubFrontChk(mActionRequiredStatus);
			if(Util.isNetworkAvailable(mContext)) {

				refreshCaptureScreenData();
			}
			else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
			break;

		case Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
			mRootView = inflater.inflate(R.layout.layout_parent_capture_check, container, false);
			initCommonUI(mRootView);
			initUISubVoidChk(mActionRequiredStatus);
			if(!mPreferences.getBoolean(PreferenceConstants.SHOW_VOID_TUTORIAL)){
				Intent intent  = new Intent(getActivity(), VoidTutorialActivity.class);
				startActivityForResult(intent, 892);
			}
			break;

		case Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
			mRootView = inflater.inflate(R.layout.layout_parent_capture_check, container, false);
			initCommonUI(mRootView);
			initUISubVoidChk(mActionRequiredStatus);
			if(Util.isNetworkAvailable(mContext)) {

				refreshCaptureScreenData();
			}
			else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}
			break;
		case Constants.CheckStatus.WAITING_FOR_REVIEW:
			mRootView = inflater.inflate(R.layout.layout_parent_capture_check, container, false);
			initCommonUI(mRootView);
			initUIResubFrontChk(mActionRequiredStatus);

			if(Util.isNetworkAvailable(mContext)) {

				refreshCaptureScreenData();
			}
			else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);

			}
			break;

		default:
			mRootView = inflater.inflate(R.layout.fragment_cash_check, container, false);
			initUI(mRootView);
			break;
		}
		return mRootView;
	}

	public void processRequest(DataObject dataObject)
	{
		dataObject.register(Method.GET);
	}

	/**
	 * Used to initialized UI components
	 * @param view  Used to create UI components
	 */
	private void initCommonUI(View view)
	{
		mIVTHeader = (ImageView)view.findViewById(R.id.iv_hearder_image);
		mTvHaderTitle = (TextView)view.findViewById(R.id.tv_review_pending_custom_text);
		mLinLayCapture = (LinearLayout)view.findViewById(R.id.check_capture_layout);
		mLinLayTransactionInfo = (LinearLayout)view.findViewById(R.id.transaction_check_details_layout);
		mLinLiayCheckDetails = (LinearLayout)view.findViewById(R.id.check_details_layout);
		mTvChkMsg = (TextView)view.findViewById(R.id.tv_check_message_text);//gone
		mBtnAgree = (Button)view.findViewById(R.id.btn_action_required_submit);//
		mBTnCancel = (Button)view.findViewById(R.id.btn_action_required_cancel);//

		//Check details ui component
		mTvChkAmt =  (TextView)view.findViewById(R.id.tv_check_details_amount);
		mTvFunding =  (TextView)view.findViewById(R.id.tv_funding_to);
		mTvDateSubmit = (TextView)view.findViewById(R.id.tv_date_submitted);
		mTvDateComplete = (TextView)view.findViewById(R.id.tv_date_transaction_completed);

		//Capture check ui components
		mRelLayFrontChk = (RelativeLayout)view.findViewById(R.id.rl_front_check_capture);
		mTvFrontChkImgBtn = (TextView)view.findViewById(R.id.tv_capture_check_front);
		mTvCaptureFrontChk = (TextView)view.findViewById(R.id.tv_front_ok);

		mRelLayBackChk = (RelativeLayout)view.findViewById(R.id.rl_back_check_capture);
		mTvBackChkImgBtn = (TextView)view.findViewById(R.id.tv_capture_check_back);
		mTvCaptureBackChk = (TextView)view.findViewById(R.id.tv_back_ok);

		mRelLayVoidChk = (RelativeLayout)view.findViewById(R.id.rl_void_check_capture);
		mTvVoidChkImgBtn = (TextView)view.findViewById(R.id.tv_void_check_capture);
		mTvCaptureVoidChk = (TextView)view.findViewById(R.id.tv_void_check_details);
		mIvVoidCheckImage = (ImageView)view.findViewById(R.id.iv_void_check_tutorial_image);
		mIvFrontCheckTutImage = (ImageView)view.findViewById(R.id.iv_front_check_tutorial_image);
		mIvBackCheckTutImage = (ImageView)view.findViewById(R.id.iv_back_check_tutorial_image);
		//Transaction details ui component
		mEtTransAmt = (EditText) view.findViewById(R.id.et_common_check_amount);

		mEtTransChkMadeDate = (TextView)view.findViewById(R.id.et_common_check_made_date);
		mEtTransMomntCardNum = (Spinner)view.findViewById(R.id.details_spinner_momentum_card);
		mEtTransMomntCardNum.setClickable(false);
		mEtTransMomntCardNum.setEnabled(false);
		
		final USCurrencyFormatter currencyFrormater = new USCurrencyFormatter(new WeakReference<EditText>(
				mEtTransAmt));
		mEtTransAmt.addTextChangedListener(currencyFrormater);

		mRelLayFrontChk.setOnClickListener(this);
		mRelLayBackChk.setOnClickListener(this);
		mRelLayVoidChk.setOnClickListener(this);
		mIvVoidCheckImage.setOnClickListener(this);
		mIvFrontCheckTutImage.setOnClickListener(this);
		mIvBackCheckTutImage.setOnClickListener(this);
		mBtnAgree.setOnClickListener(this);
		mBTnCancel.setOnClickListener(this);



		/*mTvHaderTitle.setText(mContext.getResources().getString(R.string.check_details_amount, mBundle.getString(Constants.CHECK_FEES), 
				"-"));*/
		mTvChkAmt.setText(mContext.getResources().getString(R.string.check_details_amount,  "-"));
		mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"-"));
		mTvDateSubmit.setText(mContext.getResources().getString(R.string.date_submitted, "-"));
		mTvDateComplete.setText(mContext.getResources().getString(R.string.date_transaction_completed, "-"));
		mEtTransAmt.setEnabled(false);
	}

	/**
	 * Refresh UI if user action required is resubmit or submit void check front
	 * @param mActionRequiredStatus2 User action required status
	 */
	private void initUISubVoidChk(int mActionRequiredStatus2) {
		mIVTHeader.setVisibility(View.GONE);
		mTvHaderTitle.setVisibility(View.GONE);
		mLinLayCapture.setVisibility(View.VISIBLE);//img_capture_lay
		mLinLayTransactionInfo.setVisibility(View.GONE);//transaction_info_lay
		mLinLiayCheckDetails.setVisibility(View.VISIBLE);//transaction_info_lay
		mTvChkMsg.setVisibility(View.GONE);
		mBtnAgree.setText(R.string.btn_cash_check);
		mBTnCancel.setVisibility(View.GONE);

		mRelLayVoidChk.setVisibility(View.VISIBLE);

		if (mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT){
			mTvVoidChkImgBtn.setText(R.string.recapture_void_check_front);
			mTvVoidChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
			mTvVoidChkImgBtn.setTag(R.drawable.photo_camera);
			mIvVoidCheckImage.setVisibility(View.VISIBLE);
			mTvHaderTitle.setVisibility(View.VISIBLE);
			/*
			 * mCheckMakerName = mBundle.getString(Constants.CHECK_MAKER_NAME);
				mCheckNumber =  mBundle.getString(Constants.CHECK_NUMBER);
				mCheckAmount = mBundle.getString(Constants.CHECK_AMOUNT);
				mFrontCheck = mBundle.getString(Constants.CHECK_FRONT_IMAGE);
				mBackCheck = mBundle.getString(Constants.CHECK_BACK_IMAGE);
				mActionRequiredStatus = mBundle.getInt(Constants.mActionRequiredStatus);
				mCheckId = mBundle.getString(Constants.CHECK_ID);
				mChkAmtCurrency = mBundle.getString(Constants.CHECK_AMOUNT_CURRENCY);
				mChkFees = mBundle.getString(Constants.CHECK_FEES);
				mChkFeesCurrency = mBundle.getString(Constants.CHECK_FEES_CURRENCY);
				mCheckSubmittedDate = mBundle.getString(Constants.CHECK_DATE_SUBMIT);
				mCheckCompletedDate = mBundle.getString(Constants.CHECK_DATE_COMPLETE);
			 */
			String checkAmount = "";
			String feeAmount = Util.getFormattedAmount(Double.valueOf(mChkFees));
			if(mFeesAgreed.equals("true")){
				checkAmount = Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(Double.valueOf(mCheckAmount) - Double.valueOf(mChkFees))));
			}
			else{
				checkAmount = Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(mCheckAmount)));
			}
			mTvHaderTitle.setText(mContext.getResources().getString(R.string.fees_text, 
					feeAmount, checkAmount, ": " + mFundingMechanismType));

		}
		else if(mActionRequiredStatus == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT)
		{
			mTvVoidChkImgBtn.setText(R.string.void_check_capture);
			mTvVoidChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
			mTvVoidChkImgBtn.setTag(R.drawable.photo_camera);
			mIvVoidCheckImage.setVisibility(View.VISIBLE);
			mTvHaderTitle.setVisibility(View.VISIBLE);
			String feeAmount = Util.getFormattedAmount(Double.valueOf(mChkFees));
			String checkAmount = Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(Double.valueOf(mCheckAmount) - Double.valueOf(mChkFees))));
			mTvHaderTitle.setText(mContext.getResources().getString(R.string.fees_text, 
					feeAmount, checkAmount, ": " + mFundingMechanismType));
		} else {
			mTvVoidChkImgBtn.setText(R.string.void_check_capture);
		}

		if(mActionRequiredStatus == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT)
		{
			refreshScreenData();
		}
		mTvVoidChkImgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(((TextView)v).getTag().equals(R.drawable.photo_camera)){
					captureVoidCheckFront();
				}
			}
		});
	}

	/**
	 * Used to form json request and send API request of void check submit
	 * @param postUrl API request url
	 * @param checkString Base64 string of check image
	 * @param jsonPayload Request payload data string
	 * @param relString API request Content rel value
	 * @param mFrontCheck Front check image Base64 string
	 * @param mBackCheck Back check image Base64 string
	 * @param relFrontCheck API request Content front check image rel value
	 * @param relBackCheck API request Content back check image rel value
	 * @return API response status code
	 * @throws Exception exception
	 */
	private int submitVoidedCheck(String postUrl, String checkString, String jsonPayload, String relString, String mFrontCheck,
			String mBackCheck, String relFrontCheck, String relBackCheck) throws Exception{

		DefaultHttpClient mHttpClient = null;
		String boundary = "DFC-WEBAPI-MULTIPART-BOUNDARY";

		HttpResponse response = null;

		mHttpClient = new DefaultHttpClient();
		String jsonStr = jsonPayload; 
		String mRequestPayload = "";
		if (checkString != null) {
			String checkBoundary = "--" + boundary;
			String str2 = "Content-Disposition: form-date; name=\"cheque\"";
			String str3 = "Content-Type: application/json; charset=utf-8";
			// String str4 =
			// "Content-Disposition: form-date; name=\""+mVoidedCheckId+"\" rel=\"imageChequeVoided\"";
			String str4 = "Content-Disposition: form-date; name=\"" + mCheckId + "\" rel=\"" + relString + "\"";
			String str5 = "Content-Type: image/jpeg";
			String str8 = "--" + boundary + "--";

			mRequestPayload = checkBoundary + "\r\n" + str2 + "\r\n" + str3	+ "\r\n" + jsonStr + "\r\n" + checkBoundary + "\r\n" + str4
					+ "\r\n" + str5 + "\r\n" + "Content-Transfer-Encoding: base64" + "\r\n"	+ checkString + str8;
		}

		else if(mFrontCheck!=null && mBackCheck!=null){
			String str = "--"+boundary;
			String str2 = "Content-Disposition: form-date; name=\"cheque\"";
			String str3 = "Content-Type: application/json; charset=utf-8";
			String str4 = "Content-Disposition: form-date; name=\"" + mCheckId + "\" rel=\"" + relFrontCheck + "\"";
			String str5 = "Content-Type: image/jpeg";
			String str6 = "Content-Disposition: form-date; name=\"" + mCheckId + "\" rel=\"" + relBackCheck + "\"";
			String str7 = "Content-Type: image/jpeg";
			String str8 = "--" + boundary + "--";

			mRequestPayload = str + "\r\n" + str2 + "\r\n" + str3 +"\r\n" + jsonStr + "\r\n" + str + "\r\n"
					+ str4 + "\r\n" + str5 + "\r\n" + "Content-Transfer-Encoding: base64" + "\r\n"+ mFrontCheck + str +"\r\n" + str6 + "\r\n" + str7 + "\r\n" + "Content-Transfer-Encoding: base64" +"\r\n" + mBackCheck + str8;
		}

		System.out.print("Multipart request string is "+mRequestPayload);

		HttpPut put = new HttpPut(postUrl);


		put.addHeader("Authorization", mPreferences.getString(PreferenceConstants.AUTORIZATION_TOKEN));
		put.addHeader(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		put.addHeader("Accept", "application/hal+json");
		put.addHeader("Content-Type","multipart/form-data");
		put.addHeader("Boundary", boundary);

		StringEntity se = new StringEntity(mRequestPayload);
		se.setContentEncoding("UTF-8");
		put.setEntity(se);
		response = mHttpClient.execute(put);

		/* Checking response */
		int statusCode = response.getStatusLine().getStatusCode();

//		HttpEntity entity = response.getEntity();
//		
//		StringBuilder sb = new StringBuilder();
//		try {
//			BufferedReader reader = 
//					new BufferedReader(new InputStreamReader(entity.getContent()));
//			String line = null;
//
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//		}
//		catch (IOException e) { e.printStackTrace(); }
//		catch (Exception e) { e.printStackTrace(); }

		return statusCode;

	}
	/**
	 * Used to refresh screen if UAR is submit void check image front. 
	 * App will show check amount, date and funding mechanism value retrieved from server
	 */
	private void refreshScreenData() {
		/*mTvHaderTitle.setText(mContext.getResources().getString(R.string.check_details_amount, mBundle.getString(Constants.CHECK_FEES), 
				mBundle.getString(Constants.CHECK_AMOUNT)));*/
		
		if(mFeesAgreed.equals("true")){
			setActualCreditAmount();
		}
		else{
			mTvChkAmt.setText(mContext.getResources().getString(R.string.check_details_amount, "$"+Util.getFormattedAmount(Double.valueOf(mCheckAmount))));
		}
		/**
		 * Required for future once API will send valid card number
		 */
		//		mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"Momentum Card ****"+bundle.getString(Constants.CHECK_FUNDINGTO)));
		mTvFunding.setText(mContext.getResources().getString(R.string.funding_to, mFundingMechanismType));//"Momentum Card"));
//		mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"Momentum Card"));
		mTvDateSubmit.setText(mContext.getResources().getString(R.string.date_submitted, Util.getyyyymmddDate(mBundle.getString(Constants.CHECK_DATE_SUBMIT), true)));
		mTvDateComplete.setText(mContext.getResources().getString(R.string.date_transaction_completed, Util.getyyyymmddDate(mBundle.getString(Constants.CHECK_DATE_COMPLETE), true)));
	}

	/**
	 * Update screen UI for UAR. Here app will hide check details data and enable cash check button
	 * @param mActionRequiredStatus2 User action required status
	 */
	private void initUIResubFrontChk(int mActionRequiredStatus2) {
		mIVTHeader.setVisibility(View.GONE);
		mTvHaderTitle.setVisibility(View.VISIBLE);
		mLinLayCapture.setVisibility(View.VISIBLE);//img_capture_lay

		mLinLiayCheckDetails.setVisibility(View.GONE);//transaction_info_lay
		mTvChkMsg.setVisibility(View.GONE);
		mBtnAgree.setText(R.string.btn_cash_check);
		mBTnCancel.setVisibility(View.VISIBLE);
		mRelLayVoidChk.setVisibility(View.GONE);
		mIvVoidCheckImage.setVisibility(View.GONE);

		mTvHaderTitle.setText(R.string.action_required_custom_text);
		if (mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT) {
			createUIResubmitFrontImage();

		} else if (mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK) {
			createUIResubmitBackImage();

		} else if (mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK) {
			createUIResubmitFrontBackImage();

		} else if (mActionRequiredStatus == Constants.CheckStatus.WAITING_FOR_REVIEW) {
			createUiWaitingForReview();
		}

		mBtnAgree.setOnClickListener(this);
	}

	/**
	 * Used to update UI of check block if UAR is recapture image front and back 
	 */
	private void createUIResubmitFrontBackImage() {
		mLinLayTransactionInfo.setVisibility(View.VISIBLE);

		//set red img to mTvFrontChkImgBtn
		mTvFrontChkImgBtn.setText(R.string.recapture_check_front);
		mTvFrontChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
		mIvFrontCheckTutImage.setVisibility(View.VISIBLE);
		mTvCaptureFrontChk.setVisibility(View.GONE);

		//set red img to mTvBackChkImgBtn
		mTvBackChkImgBtn.setText(R.string.recapture_check_back);
		mTvBackChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
		mTvCaptureBackChk.setVisibility(View.GONE);
		mIvBackCheckTutImage.setVisibility(View.VISIBLE);

		mBtnAgree.setVisibility(View.VISIBLE);
		mBTnCancel.setVisibility(View.VISIBLE);

		mBtnAgree.setText(R.string.submit);
		mBTnCancel.setText(R.string.cancel);
	}

	/**
	 * Used to update UI of check block if UAR is recapture image front
	 */
	private void createUIResubmitFrontImage() {
		mLinLayTransactionInfo.setVisibility(View.VISIBLE);//transaction_info_lay
		//set red img to mTvFrontChkImgBtn
		mTvFrontChkImgBtn.setText(R.string.recapture_check_front);
		mTvFrontChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
		mIvFrontCheckTutImage.setVisibility(View.VISIBLE);
		mTvCaptureFrontChk.setVisibility(View.GONE);

		mTvBackChkImgBtn.setText(R.string.download_text);
		//set green img to mTvBackChkImgBtn
		mTvBackChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_check, 0, 0);
		mIvBackCheckTutImage.setVisibility(View.INVISIBLE);
		mTvCaptureBackChk.setText(R.string.back_ok);

		mBtnAgree.setVisibility(View.VISIBLE);
		mBTnCancel.setVisibility(View.VISIBLE);

		mBtnAgree.setText(R.string.submit);
		mBTnCancel.setText(R.string.cancel);
		/*mTvHaderTitle.setText(mContext.getResources().getString(R.string.check_details_amount, mBundle.getString(Constants.CHECK_FEES), 
				mBundle.getString(Constants.CHECK_AMOUNT)));*/
		mTvHaderTitle.setText(R.string.action_required_custom_text);

	}

	/**
	 * Used to update UI of check block if UAR is recapture image back
	 */
	private void createUIResubmitBackImage() {
		mLinLayTransactionInfo.setVisibility(View.VISIBLE);//transaction_info_lay
		//set red img to mTvBackChkImgBtn
		mTvBackChkImgBtn.setText(R.string.recapture_check_back);
		mTvBackChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photo_camera, 0, 0);
		mIvBackCheckTutImage.setVisibility(View.VISIBLE);
		mTvCaptureBackChk.setVisibility(View.GONE);

		mTvFrontChkImgBtn.setText(R.string.download_text);
		mTvFrontChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_check, 0, 0);
		//set green img to mTvFrontChkImgBtn
		mIvFrontCheckTutImage.setVisibility(View.INVISIBLE);
		mTvCaptureFrontChk.setText(R.string.front_ok);

		mBtnAgree.setVisibility(View.VISIBLE);
		mBTnCancel.setVisibility(View.VISIBLE);

		mBtnAgree.setText(R.string.submit);
		mBTnCancel.setText(R.string.cancel);		
		mTvHaderTitle.setText(R.string.action_required_custom_text);
		mEtTransAmt.setEnabled(false);
	}

	/**
	 * Used to update UI of check block if UAR is waiting for review
	 */
	private void createUiWaitingForReview() {
		mLinLayTransactionInfo.setVisibility(View.GONE);//transaction_info_lay
		mTvBackChkImgBtn.setText(R.string.download_text);
		//set green img to mTvBackChkImgBtn
		//	mTvBackChkImgBtn.setText(R.string.back_ok);

		mTvFrontChkImgBtn.setText(R.string.download_text);
		//set green img to mTvFrontChkImgBtn
		//	mTvFrontChkImgBtn.setText(R.string.back_ok);

		mTvFrontChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_check, 0, 0);
		mTvBackChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_check, 0, 0);
		mIvFrontCheckTutImage.setVisibility(View.GONE);
		mIvBackCheckTutImage.setVisibility(View.GONE);
		mIvVoidCheckImage.setVisibility(View.GONE);

		if(mBundle!=null && mBundle.getString(Constants.CHECK_VOID_IMAGE_URL)!=null && !mBundle.getString(Constants.CHECK_VOID_IMAGE_URL).equals("")
				&& !mBundle.getString(Constants.CHECK_VOID_IMAGE_URL).equals("null")){
			mRelLayVoidChk.setVisibility(View.VISIBLE);
			mTvVoidChkImgBtn.setText(R.string.download_text);
			mTvVoidChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_check, 0, 0);
			mTvCaptureVoidChk.setText(R.string.void_ok);
			mTvCaptureVoidChk.setTypeface(null, Typeface.BOLD);
		}

		mLinLiayCheckDetails.setVisibility(View.VISIBLE);
		mBtnAgree.setVisibility(View.GONE);
		mBTnCancel.setVisibility(View.GONE);		
		mTvHaderTitle.setText(R.string.review_pending_custom_text);
	}

	@OnClick(R.id.tv_confirm_check_amount)
	public void confirmCheckAmount(){
		Intent intent1  = new Intent(getActivity(), TutorialOverlayActivity.class);
		startActivity(intent1);
	}

	@OnClick(R.id.iv_back_check_tutorial_image)
	public void backTutorialImage(){
		Intent intent1  = new Intent(getActivity(), BackTutorialActivityInfoScrn.class);//BackTutorialActivity.class);
		startActivity(intent1);
	}


	@OnClick(R.id.iv_front_check_tutorial_image)
	public void frontTutorialImage(){
		Intent intent1  = new Intent(getActivity(), FrontTutorialActivityInfoScrn.class);//FrontTutorialActivity.class);
		startActivity(intent1);
	}

	@OnClick(R.id.iv_confirm_check_tutorial_image)
	public void checkConfirmAmountTutorialImage(){
		Intent intent1  = new Intent(getActivity(), TutorialOverlayActivity.class);
		startActivity(intent1);
	}


	@OnClick(R.id.relative_front_check_capture)
	public void captureFrontImageUpload(){
		recaptureCheckFront();
	}

	@OnClick(R.id.relative_back_check_capture)
	public void captureBackImageUpload(){
		recaptureCheckBack();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_front_check_capture:
			clickEventFrontCheckCapture();
			break;

		case R.id.rl_back_check_capture:
			clickEventBackCheckCapture();
			break;

		case R.id.btn_action_required_submit:
			clickEventActionRequiredSubmitBtn();
			break;

		case R.id.btn_right: 
			mDialog.dismiss();
			if(mDialogId!=null && mDialogId.equals(mContext.getResources().getString(R.string.lbl_alert))){
				mDialogId = null;
				refreshListFromServer();
			}
			else if(mDialogId!=null && mDialogId.equals(mContext.getResources().getString(R.string.error_confirm_void_chk))){
				mDialogId = null;
				getActivity().finish();
			}
			break;

		case R.id.btn_action_required_cancel:
			getActivity().finish();
			break;

		case R.id.rl_void_check_capture:
			clickEventVoidCheckCapture();
			break;
		case R.id.iv_void_check_tutorial_image:
			Intent i = new Intent(getActivity().getBaseContext(), VoidTutorialActivityInfoScrn.class);
			startActivity(i);
			break;
		case R.id.iv_back_check_tutorial_image:
			Intent intent  = new Intent(getActivity(), BackTutorialActivityInfoScrn.class);//BackTutorialActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_front_check_tutorial_image:
			Intent frontIntent  = new Intent(getActivity(), FrontTutorialActivityInfoScrn.class);//FrontTutorialActivity.class);
			startActivity(frontIntent);
			break;
		case R.id.btn_left:
			mDialogId = null;
			mDialog.dismiss();
			break;	

		default:
			break;
		}
	};


	private void clickEventFrontCheckCapture() {
		if(mTvFrontChkImgBtn != null && mTvFrontChkImgBtn.getText().toString().equals(mContext.getResources().getString(R.string.download_text))) {
			if(Util.isNetworkAvailable(mContext)) {
				downloadAvailableImage(mBundle.getString(Constants.CHECK_FRONT_IMAGE_URL), EventTypes.EVENT_DOWNLOAD_FRONT_IMAGE);	
			} else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
			}

		} else {
			if(Util.isNetworkAvailable(mContext)) {
				recaptureCheckFront();
			} else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);
			}
		}
	}


	private void clickEventBackCheckCapture() {
		if(mTvBackChkImgBtn != null && mTvBackChkImgBtn.getText().toString().equals(mContext.getResources().getString(R.string.download_text))) {
			if(Util.isNetworkAvailable(mContext)) {
				downloadAvailableImage(mBundle.getString(Constants.CHECK_BACK_IMAGE_URL), EventTypes.EVENT_DOWNLOAD_BACK_IMAGE);
			} else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);	
			}
		} else {
			if(Util.isNetworkAvailable(mContext)) {
				recaptureCheckBack();
			} else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);
			}
		}
	}

	private void clickEventVoidCheckCapture() {
		if(mTvVoidChkImgBtn != null && mTvVoidChkImgBtn.getText().toString().equals(mContext.getResources().getString(R.string.download_text))) {
			if(Util.isNetworkAvailable(mContext)) {
				downloadAvailableImage(mBundle.getString(Constants.CHECK_VOID_IMAGE_URL), EventTypes.EVENT_DOWNLOAD_VOID_IMAGE);
			} else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);	
			}
		} else {
			if(Util.isNetworkAvailable(mContext)) {
				captureVoidCheckFront();
			}else {
				showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);
			}
		}
	}

	private void clickEventActionRequiredSubmitBtn() {
		if (Util.isNetworkAvailable(mContext)){
			if(mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT) {

				if(mFrontCheck == null || mFrontCheck.equals("")|| mFrontCheck.length() == 0) {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_front_chk), this);
				} else if(mEtTransAmt.getText().toString() != null && mEtTransAmt.getText().toString().length() > 0 
						&& Double.parseDouble(mEtTransAmt.getText().toString().substring(1)) > 0
						&& mEtTransChkMadeDate.getText().toString() != null && mEtTransChkMadeDate.getText().toString().length() > 0 
						&& mEtTransMomntCardNum != null && mEtTransMomntCardNum.getCount() > 0) {

					submitCheckImages(mFrontCheck, "imageChequeFront");
				} else {

					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_chk_data), this);
				}

			} else if(mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK) {

				if(mBackCheck == null || mBackCheck.equals("")|| mBackCheck.length() == 0) {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_back_chk), this);
				} else if(mEtTransAmt.getText().toString() != null && mEtTransAmt.getText().toString().length() > 0 
						&& Double.parseDouble(mEtTransAmt.getText().toString().substring(1)) > 0
						&& mEtTransChkMadeDate.getText().toString() != null && mEtTransChkMadeDate.getText().toString().length() > 0 
						&& mEtTransMomntCardNum != null && mEtTransMomntCardNum.getCount() > 0) {

					submitCheckImages(mBackCheck, "imageChequeBack");
				} else {

					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_chk_data), this);
				}

			} else if(mActionRequiredStatus ==  Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT) {

				if(mVoidFrontCheck != null && mVoidFrontCheck.toString().length() > 0) {
					submitCheckImages(mVoidFrontCheck, "imageChequeVoided");
				} else {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_void_chk), this);
				}
			} else if(mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT) {

				if(mVoidFrontCheck != null && mVoidFrontCheck.toString().length() > 0) {
					submitCheckImages(mVoidFrontCheck, "imageChequeVoided");
				} else {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_void_chk), this);
				}
			} else if(mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK){

				if(mFrontCheck == null || mFrontCheck.toString().length() <= 0
						|| mBackCheck == null || mBackCheck.toString().length() <= 0) {

					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_front_back_chk), this);

				} else if(mEtTransAmt.getText().toString() != null && mEtTransAmt.getText().toString().length() > 0 
						&& Double.parseDouble(mEtTransAmt.getText().toString().substring(1)) > 0
						&& mEtTransChkMadeDate.getText().toString() != null && mEtTransChkMadeDate.getText().toString().length() > 0 
						&& mEtTransMomntCardNum != null && mEtTransMomntCardNum.getCount() > 0) {

					submitCheckImages(mFrontCheck, mBackCheck, "imageChequeFront", "imageChequeBack");
				} else {

					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_chk_data), this);
				}
			}
		} else {
			showAlertDialog(mContext, mContext.getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}
	}


	/**
	 * Used to submit check image to server
	 * @param check Base64 string of check image
	 * @param relString rel param value of content 
	 */
	private void submitCheckImages(String check, String relString){
		String url = Constants.BASE_URL + Constants.URL_GET_CHECKS + File.separator + mCheckId;
		try {
			new SubmitVoidedCheckImage(url, prepareJson().toString(), check, relString).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to submit check image to server
	 * @param frontCheck Base64 string of front check image
	 * @param backCheck Base64 string of back check image
	 * @param relStringFront rel param value front image of content 
	 * @param relStringBack rel param value back image of content 
	 */
	private void submitCheckImages(String frontCheck, String backCheck, String relStringFront, String relStringBack){
		String url = Constants.BASE_URL + Constants.URL_GET_CHECKS + File.separator + mCheckId;
		try {
			new SubmitVoidedCheckImage(url, prepareJson().toString(), frontCheck, backCheck, relStringFront, relStringBack).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recaptureCheckFront(){
		if(!mStarted) {
			if(mHandler==null)
				mHandler = new Handler();
			mHandler.removeCallbacks(mCheckCapture);
			mHandler.post(mCheckCapture);
		}
	}

	public void recaptureCheckBack(){
		if(!mStarted) {
			if(mHandler==null)
				mHandler = new Handler();
			mHandler.removeCallbacks(mDL);
			mHandler.post(mDL);
		}
	}

	public void captureVoidCheckFront(){
		if(!mStarted) {
			if(mHandler==null)
				mHandler = new Handler();
			mHandler.removeCallbacks(mVoidCheckFront);
			mHandler.post(mVoidCheckFront);
		}
	}

	Runnable mCheckCapture = new Runnable() {
		public void run() {
			mStarted = true;
			JSONObject jjs = null;
			try {
				jjs = new JSONObject();
				//jjs.put(MiSnapAPI.Name, "CheckFront");	// set job file - CheckFront,CheckBack,PDF417,CREDIT_CARD etc
				jjs.put(MiSnapAPI.ButtonFirstTimeTutorialContinue, mContext.getResources().getString(R.string.btn_continue));
				jjs.put(MiSnapAPI.Name, "ACH");
				jjs.put(MiSnapAPI.CaptureMode, "2");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jjs!=null){
				Intent i = new Intent(getActivity().getBaseContext(), MiSnap.class);
				i.putExtra(MiSnapAPI.JOB_SETTINGS, jjs.toString());
				startActivityForResult(i, Constants.MISNAP_FRONT_CHECK_RESULT_CODE);
			}
		}
	};

	Runnable mDL = new Runnable() {
		public void run() {
			mStarted = true;
			JSONObject jjs = null;
			try {
				jjs = new JSONObject();
				jjs.put(MiSnapAPI.Name, "DRIVER_LICENSE");	// set job file - CheckFront,CheckBack,PDF417,CREDIT_CARD etc
				jjs.put(MiSnapAPI.CaptureMode, "2");
				jjs.put(MiSnapAPI.CameraViewfinderMinFill, "400");	// For DLs
				jjs.put(MiSnapAPI.CameraSharpness, "350");
				jjs.put(MiSnapAPI.CameraMaxTimeouts, "1");
				jjs.put(MiSnapAPI.ShortDescription, "Mobile Deposit Check Back");
				jjs.put(MiSnapAPI.UnnecessaryScreenTouchLimit, "4");
				jjs.put(MiSnapAPI.RESULT_IMAGE_QUALITY, "50");
				jjs.put(MiSnapAPI.CameraMaxTimeouts, "0");
				jjs.put(MiSnapAPI.LightingVideo, "3");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(jjs!=null){
				Intent i = new Intent(getActivity().getBaseContext(), MiSnap.class);
				i.putExtra(MiSnapAPI.JOB_SETTINGS, jjs.toString());
				startActivityForResult(i, Constants.MISNAP_BACK_CHECK_RESULT_CODE);
			}
		}
	};

	Runnable mVoidCheckFront = new Runnable() {
		public void run() {
			mStarted = true;
			JSONObject jjs = null;
			try {
				jjs = new JSONObject();
				//jjs.put(MiSnapAPI.Name, "CheckFront");	// set job file - CheckFront,CheckBack,PDF417,CREDIT_CARD etc
				jjs.put(MiSnapAPI.ButtonFirstTimeTutorialContinue, mContext.getResources().getString(R.string.btn_continue));
				jjs.put(MiSnapAPI.Name, "ACH");
				jjs.put(MiSnapAPI.CaptureMode, "2");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(jjs!=null){
				Intent i = new Intent(getActivity().getBaseContext(), MiSnap.class);
				i.putExtra(MiSnapAPI.JOB_SETTINGS, jjs.toString());
				startActivityForResult(i, Constants.MISNAP_VOID_CHECK_FRONT_RESULT_CODE);
			}
		}
	};

	@OnClick(R.id.btn_submit)
	public void createCheck(){

		if(mFrontCheck==null || mFrontCheck.equals("") || mBackCheck==null || mBackCheck.equals("")){
			showAlertDialog(mContext, mContext.getResources().getString(R.string.capture_all_images_msg), this);
		}
		else if(mEtCheckAmount.getText()!=null && !mEtCheckAmount.getText().toString().isEmpty() && mSubmitISODate!=null && !mSubmitISODate.isEmpty()){

			MyChecksDataObject dataObj = new MyChecksDataObject();
			dataObj.setmFrontCheckImage(mFrontCheck);
			dataObj.setmBackCheckImage(mBackCheck);
			dataObj.setmCheckAmount(Double.valueOf(mEtCheckAmount.getText().toString().substring(1).replaceAll(",", "")));
			dataObj.setmCheckMadeDate(mSubmitISODate);
			((HomeActivity)getActivity()).setMyCheckObjectForUpload(dataObj);
			Constants.setFetchListFromServer(false);
			((HomeActivity)getActivity()).goToMyChecksFragment();
			//		new SubmitCapturedCheckImage().execute(); 
		}else{
			showAlertDialog(mContext, mContext.getResources().getString(R.string.fill_all_fields_msg), this);
		}
	}

	/**
	 * Used to prepare json request body
	 * @return json object
	 */
	public JSONObject prepareJson(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("chequeId", mCheckId);
			//			jo.put("chequeMaker", mCheckMakerName);
			jo.put("chequeMaker", mCheckMakerName);//"ABC");
			jo.put("chequeNumber", mCheckNumber);
			String amount = "0.0";
			if (mCheckAmount.toString().contains("$")){
				amount = mCheckAmount.toString().substring(1);
				if (amount.contains(",")){
					amount = amount.replaceAll(",", "");
				}
			} else {
				amount = mCheckAmount ;
			}
			jo.put("amount", Util.getFormattedAmount(Double.valueOf(amount)));//Double.valueOf(mCheckAmount.substring(1).replaceAll(",", "")));
			jo.put("amountCurrency", mChkAmtCurrency);//"USD");
			if(mCheckMadeDate.trim().length() > 10){
				jo.put("dateMade", Util.getyyyymmddDate(mCheckMadeDate));//mCheckMadeDate);
			} else {
				jo.put("dateMade", mCheckMadeDate);
			}

			jo.put("fees", mChkFees);//30.00);
			jo.put("feesCurrency", mChkFeesCurrency);//"USD");
			jo.put("fundingMechanism", mFundingMechanismID);
			jo.put("feesAgreed", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

	/**
	 *  Asyn task to submit check image to server
	 *
	 */
	private class SubmitVoidedCheckImage extends AsyncTask<String, Void, Integer> {

		private final String requestURL;
		private final String jsonData;
		private String mBase64VoidImage = null;

		private String mFrontReCaptureImage = null;
		private String mBackReCaptureImage = null;

		private int statusCode = 0;
		private String relString;

		private String relFrontString;
		private String relBackString;

		public SubmitVoidedCheckImage(String requestURL, String jsonData,
				String mBase64VoidImage, String relString) {
			super();
			this.requestURL = requestURL;
			this.jsonData = jsonData;
			this.mBase64VoidImage = mBase64VoidImage;
			this.relString = relString;
		}

		public SubmitVoidedCheckImage(String requestURL, String jsonData,
				String mFrontReCaptureImage, String mBackReCaptureImage, String relFrontString, String relBackString) {
			super();
			this.requestURL = requestURL;
			this.jsonData = jsonData;
			this.mFrontReCaptureImage = mFrontReCaptureImage;
			this.mBackReCaptureImage = mBackReCaptureImage;
			this.relFrontString = relFrontString;
			this.relBackString = relBackString;
		}

		@Override
		protected void onPreExecute() {
			showLoader();
		}

		@Override
		protected Integer doInBackground(String... urls) {
			try{
				statusCode = submitVoidedCheck(requestURL, mBase64VoidImage, jsonData, relString, mFrontReCaptureImage, mBackReCaptureImage, relFrontString, relBackString);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statusCode;
		}

		@Override
		protected void onPostExecute(Integer result) {

			if(statusCode == 200 || statusCode == 201){
				refreshListFromServer();
			}
			else{
				hideLoader();
				if(statusCode == 401){
					showAlertDialogUnAuthorised(mContext, mContext.getResources().getString(R.string.error_401), CheckCashingDetailsFragment.this);
				} else if (statusCode == 403) {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_generic), mContext.getResources().getString(R.string.lbl_alert), CheckCashingDetailsFragment.this);
				} else if (statusCode == 10000) {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_network), mContext.getResources().getString(R.string.lbl_alert), CheckCashingDetailsFragment.this);
				} else if (statusCode == 400){
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_400), mContext.getResources().getString(R.string.lbl_alert), CheckCashingDetailsFragment.this);
				} else if (statusCode == 408){
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_408_request_timeout), mContext.getResources().getString(R.string.lbl_alert), CheckCashingDetailsFragment.this);
				} else {
					showAlertDialog(mContext, mContext.getResources().getString(R.string.error_generic), mContext.getResources().getString(R.string.lbl_alert), CheckCashingDetailsFragment.this);
				}
			}
		}
	}

	/**
	 * Used to initialize ui components
	 * @param view  used to create UI components 
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);

		mEtCheckMadeDate.setInputType(InputType.TYPE_NULL);
		mCalendar = Calendar.getInstance();
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mTvConfirmCheckAmount.setGravity(Gravity.END);
		mEtCheckMadeDate.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_UP) {
					showDatePickerDialog(Constants.DATE_DIALOG_ID).show();
				}
				return false;
			}
		});


		final USCurrencyFormatter addLineNumberFormatter = new USCurrencyFormatter(new WeakReference<EditText>(
				mEtCheckAmount));
		mEtCheckAmount.addTextChangedListener(addLineNumberFormatter);

		mHandler = new Handler();

		if(mBundle!=null){

			byte[] imageDataBytes = Base64.decode(mFrontCheck, Base64.DEFAULT);
			ByteArrayOutputStream baos = generateImages(imageDataBytes);
			byte[] imageData = baos.toByteArray();
			Bitmap decodedByte = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			mRLfrontCheck.setBackground(new BitmapDrawable(mContext.getResources(), decodedByte));

			imageDataBytes = Base64.decode(mBackCheck, Base64.DEFAULT);
			baos = generateImages(imageDataBytes);
			imageData = baos.toByteArray();
			decodedByte = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			mRLbackCheck.setBackground(new BitmapDrawable(mContext.getResources(), decodedByte));
			mEtCheckAmount.setText(mCheckAmount.substring(1));
			mEtCheckMadeDate.setText(mCheckMadeDate);

		}
	}

	/**
	 * Captured image into byte format temporarily to file 
	 * @param candidateJPEG Image in byte format
	 */
	private void saveJPEGImage(byte[] candidateJPEG) {
		file = getOutputMediaFile();
		saveToFile(candidateJPEG, file);
	}

	/**
	 * Used to get stored image so that app will convert it in base64 once ready to interact with server
	 * @return image file
	 */
	public File getOutputMediaFile(){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = getActivity().getDir(Constants.MISNAP_DIR, Context.MODE_PRIVATE); //Creating an internal dir;
		File fileWithinMyDir = new File(mediaStorageDir, Constants.MISNAP_FRONT_CHECK); //Getting a file within the dir.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
			return null;
		}

		return fileWithinMyDir;
	}

	/**
	 * Used to save image in file 
	 * @param bytes  Image data in bytes
	 * @param file File to write data
	 * @return status of file is saved or not
	 */
	public boolean saveToFile(byte[] bytes, File file){
		boolean saved = false;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
			saved = true;
		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}
		return saved;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mStarted = false;
		if(mHandler != null) {
			mHandler.removeCallbacks(mVoidCheckFront);
		}

		if((requestCode == Constants.MISNAP_BACK_CHECK_RESULT_CODE 
				|| requestCode == Constants.MISNAP_FRONT_CHECK_RESULT_CODE
				|| requestCode == Constants.MISNAP_VOID_CHECK_FRONT_RESULT_CODE) && resultCode == Activity.RESULT_OK && data != null){
			Bundle extras = data.getExtras();
			String miSnapResultCode = extras.getString(MiSnapAPI.RESULT_CODE);

			if (MiSnapAPI.RESULT_SUCCESS_VIDEO.equals(miSnapResultCode)
					|| MiSnapAPI.RESULT_SUCCESS_STILL.equals(miSnapResultCode)) {

				// Image returned successfully
				byte[] image = data.getByteArrayExtra(MiSnapAPI.RESULT_PICTURE_DATA);
				ByteArrayOutputStream baos = generateImages(image);
				byte[] imageData = baos.toByteArray();
				if(requestCode == Constants.MISNAP_FRONT_CHECK_RESULT_CODE){
					Message msg = new Message();
					msg.what = FRONT_CHECK_IMG_MISNAP;
					msg.obj = imageData;
					uiHandler.sendMessage(msg);
					
					mTvFrontChkImgBtn.setVisibility(View.GONE);
					mTvCaptureFrontChk.setVisibility(View.GONE);
					mRelLayFrontChk.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length)));
					

				} else if(requestCode == Constants.MISNAP_BACK_CHECK_RESULT_CODE){
					Message msg = new Message();
					msg.what = BACK_CHECK_IMG_MISNAP;
					msg.obj = imageData;
					uiHandler.sendMessage(msg);
					
					mTvBackChkImgBtn.setVisibility(View.GONE);
					mTvCaptureBackChk.setVisibility(View.GONE);
					mRelLayBackChk.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length)));
					
				} else if(requestCode == Constants.MISNAP_VOID_CHECK_FRONT_RESULT_CODE){
					mTvVoidChkImgBtn.setVisibility(View.GONE);
					mTvCaptureVoidChk.setVisibility(View.GONE);
					mTvVoidChkImgBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					mRelLayVoidChk.setBackground(new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length)));
					mPreferences.addOrUpdateBoolean(PreferenceConstants.SHOW_VOID_TUTORIAL, true);

				}
				//encrypt the byte array
				if(image != null) {
					String base64 = Base64.encodeToString(image, Base64.DEFAULT);
					if(requestCode == Constants.MISNAP_FRONT_CHECK_RESULT_CODE){
						mFrontCheck = base64;
					}
					else if(requestCode == Constants.MISNAP_BACK_CHECK_RESULT_CODE){
						mBackCheck = base64;
					}
					else if(requestCode == Constants.MISNAP_VOID_CHECK_FRONT_RESULT_CODE){
						mVoidFrontCheck = base64;
					}
				}
			}
		}
		/*else if((requestCode == Constants.MISNAP_BACK_CHECK_RESULT_CODE || requestCode == Constants.MISNAP_FRONT_CHECK_RESULT_CODE) && resultCode == Activity.RESULT_OK && data == null) {
			// Image canceled, stop
			//TODO Nothing
		}*/
		else if (requestCode == 890) {
			// Capture Front Check Image
			recaptureCheckFront();
		}
		else if (requestCode == 891) {
			// Capture Back Check Image
			recaptureCheckBack();
		}
		else if (requestCode == 892) {
			// Capture Void Check Image
			captureVoidCheckFront();
		}
	}

	/**
	 * Used to generate image 
	 * @param image image data
	 * @return Byte array
	 */
	private ByteArrayOutputStream generateImages(byte[] image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		if(image != null) {
			saveJPEGImage(image);
			final int THUMBNAIL_HEIGHT = 200;
			final int THUMBNAIL_WIDTH = 400;

			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false);

				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return baos;
	}

	/**
	 * Used to show date picker when user select date
	 * @param id Ui id
	 * @return Date picker dialog
	 */
	private Dialog showDatePickerDialog(int id) {
		if (id == Constants.DATE_DIALOG_ID) {

			mDatePicker = new DatePickerDialog(getActivity(), datePicker, mYear,
					mMonth, mDay) {
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					//TODO Nothing
				}
			};
			return mDatePicker;
		}
		return null;
	}

	/**
	 * Date picker click event listener
	 */
	private final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDateLabel();
		}

	};

	/**
	 * Convert date selected using date picker into MM/dd/yyyy format
	 */
	private void updateDateLabel() {
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//		mDate = sdfserver.format(mCalendar.getTime());
		mSubmitISODate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(mCalendar.getTime());
		mEtCheckMadeDate.setText(sdfEditTextDisplayFormatDate.format(mCalendar.getTime()));
	}

	/**
	 * Used to refresh screen data based on check status or UAR
	 */
	private void refreshCaptureScreenData() {
		getFundingMechanism();
		if(mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK
				|| mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT
				|| mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK) {

			double checkAmount = Double.valueOf(mCheckAmount);
			String editTextAmount = Double.toString(Math.abs(checkAmount));
			int integerPlaces = editTextAmount.indexOf('.');
			int decimalPlaces = editTextAmount.length() - integerPlaces - 1;

			if(decimalPlaces == 1){
				editTextAmount+= 0;
				mEtTransAmt.setText("$" +editTextAmount);
			}
			else{
				mEtTransAmt.setText("$" +checkAmount);
			}
			mEtTransChkMadeDate.setText(Util.getyyyymmddDate(mCheckMadeDate, true));

		} else if(mActionRequiredStatus == Constants.CheckStatus.WAITING_FOR_REVIEW
				|| mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT
				|| mActionRequiredStatus == Constants.CheckStatus.REJECTED
				|| mActionRequiredStatus == Constants.CheckStatus.CASHED) {
			
			if(mFeesAgreed.equals("true")){
				setActualCreditAmount();
			}
			else{
				mTvChkAmt.setText(mContext.getResources().getString(R.string.check_details_amount, "$"+Util.getFormattedAmount(Double.valueOf(mCheckAmount))));
			}
			if(mCheckSubmittedDate == null || mCheckSubmittedDate.equalsIgnoreCase("null")) {
				mTvDateSubmit.setText(mContext.getResources().getString(R.string.date_submitted, "-"));
			} else {
				mTvDateSubmit.setText(mContext.getResources().getString(R.string.date_submitted, Util.getyyyymmddDate(mCheckSubmittedDate, true)));
			}
			if(mCheckCompletedDate == null || mCheckCompletedDate.equalsIgnoreCase("null")) {
				mTvDateComplete.setText(mContext.getResources().getString(R.string.date_transaction_completed, "-"));
			} else {
				mTvDateComplete.setText(mContext.getResources().getString(R.string.date_transaction_completed,Util.getyyyymmddDate(mCheckCompletedDate, true)));
			}

		}
	}

	/**
	 * Dialog button click event
	 * @param v used to create UI components
	 */
	public void homeBtnClick(View v) {
		backButtonClick();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetFundingMechanismFailure(VolleyError error, int eventType) {
		try {
			hideLoader();
			int statusCode = ErrorHandling.getStatusCode(error);
			String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
			if(statusCode == 401)
			{
				showAlertDialogUnAuthorised(mContext, errorMessage, this);
			} else {

				if (mBtnAgree.getVisibility()== View.VISIBLE)
				{
					mBtnAgree.setEnabled(false);
					mBtnAgree.setClickable(false);
					mBtnAgree.setBackgroundResource(R.color.personal_details_divider);
					//mEtTransMomntCardNum.setText("-");
				} else if (mTvFunding.getVisibility() == View.VISIBLE)
				{
					mTvFunding.setText(mContext.getResources().getString(R.string.funding_to, "-"));
				}
				showAlertDialog(mContext, errorMessage, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to show card drop down list on successful response of funding mechanism for local checks
	 * @param mCardNumber Card number list
	 */
	@SuppressWarnings("unused")
	private void refreshMomntCardData(ArrayList<String> mCardNumber) {
		ArrayList<String> tempCardNumber = new ArrayList<String>();
		if(mCardNumber!=null&&!mCardNumber.isEmpty()){
			for (String card : mCardNumber) {
				/**
				 * Required in future once app retrieve valid card number from API  
				 */
				//				tempCardNumber.add("Momentum Card "+card);
				tempCardNumber.add("Momentum Card");
			}
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
		(mContext, android.R.layout.simple_spinner_item,tempCardNumber);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerMomentumCard.setAdapter(dataAdapter);

		hideLoader();
	}

	/**
	 * Used to send get funding mechanism request to server 
	 */
	public void getFundingMechanism() {
		showLoader();
		GetFundingMechanismHandler handler = new GetFundingMechanismHandler(this);
		handler.setReqEventType(EventTypes.FUNDING_MECHANISM_V2);//(MMEventTypes.FUNDING_MECHANISM);
		handler.setUrl(Constants.URL_GET_FUNDING_MECHANISM);
		handler.getfundingMechanisms();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismSuccess(com.dfc.moneymartus.dto.FundingMechanismDataObject, java.util.ArrayList)
	 */
	@Override
	public void onGetFundingMechanismSuccess(FundingMechanismDataObject fundingMechanismObject, ArrayList<String> mCardNumber) {
		if (fundingMechanismObject.fundingMechanisms == null) {
			mTvFunding.setText(mContext.getResources().getString(R.string.funding_to, "-"));
			hideLoader();
		} else {
			refreshDetailsFindingMechSpinner(mCardNumber);
			/**
			 * Required in future once app retrieve valid card number from API  
			 */
			//mEtTransMomntCardNum.setText(mContext.getResources().getString(R.string.funding_to,"****" + fundingMechanismObject.fundingMechanisms[0].cardNumberEnding));
			//			mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"Momentum Card ****"+fundingMechanismObject.fundingMechanisms[0].cardNumberEnding));
//			mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"Momentum Card"));
			hideLoader();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FundingMechanismView#onGetFundingMechanismSuccess(java.util.ArrayList)
	 */
	@Override
	public void onGetFundingMechanismSuccess(ArrayList<String> mCardNumber) {

		if (mActionRequiredStatus == 0 ){

			refreshMomntCardData(mCardNumber);

		} else if(mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT 
				||mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK
				||mActionRequiredStatus == Constants.CheckStatus.QUEUED
				||mActionRequiredStatus == Constants.CheckStatus.UPLOADING_FAILED
				|| mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK
				|| mActionRequiredStatus == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT)
		{

			refreshDetailsFindingMechSpinner(mCardNumber);
			hideLoader();
		}

	}

	/**
	 * Used to show card drop down list on successful response of funding mechanism for UAR checks
	 * @param mCardNumber Card number
	 */
	@SuppressWarnings("unused")
	private void refreshDetailsFindingMechSpinner(ArrayList<String> mCardNumber) {
		ArrayList<String> tempCardNumber = new ArrayList<String>();
		if(mCardNumber!=null&&!mCardNumber.isEmpty()){
			for (String card : mCardNumber) {
				//		tempCardNumber.add("Momentum Card "+card);
				tempCardNumber.add("Momentum Card");
			}
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
		(mContext, android.R.layout.simple_spinner_item,tempCardNumber);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mEtTransMomntCardNum.setAdapter(dataAdapter);
	}

	/**
	 * Used to send API request to sever to download already uploaded check image
	 * @param imageUrl Image url
	 * @param event Request event id
	 */
	public void downloadAvailableImage(String imageUrl, int event){

		RecaptureCheckHandler handler = new RecaptureCheckHandler(this);
		handler.setReqEventType(event);//(MMEventTypes.BASE64_IMAGE_DOWLOAD);
		handler.setUrl(Constants.BASE_URL.substring(0 ,Constants.BASE_URL.length() - 3)+imageUrl);
		showLoader();
		new DownloadCheckImage(handler).execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.RecaptureCheckView#onRecaptureCheckFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onRecaptureCheckFailure(VolleyError error, int eventType) {
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(mContext, errorMessage, this);
		} else {
			showAlertDialog(mContext, errorMessage, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.RecaptureCheckView#onRecaptureSuccess(java.lang.String)
	 */
	@Override
	public void onRecaptureSuccess(String response) {
		//TODO Nothing
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.RecaptureCheckView#onRecaptureSuccess(int, java.lang.String)
	 */
	@Override
	public void onRecaptureSuccess(int responseData, String response) {
		if (response == null) {
			
			if (Util.isNetworkAvailable(mContext)){
				showAlertDialog(mContext, mContext.getResources().getString(R.string.error_generic), this);
			} else {
				showAlertDialog(mContext, mContext.getResources().getString(R.string.error_network), this);
			}
			
		} else {

			byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
			//Show front image received from server
			if(responseData == EventTypes.EVENT_DOWNLOAD_FRONT_IMAGE) {
				mRelLayFrontChk.setBackground(new BitmapDrawable(mContext.getResources(), decodedByte));
				mTvFrontChkImgBtn.setVisibility(View.GONE);
				mTvCaptureFrontChk.setVisibility(View.GONE);
				mRelLayFrontChk.setClickable(false);
			}
			//Show back image received from server
			else if(responseData == EventTypes.EVENT_DOWNLOAD_BACK_IMAGE){
				mRelLayBackChk.setBackground(new BitmapDrawable(mContext.getResources(), decodedByte));
				mTvBackChkImgBtn.setVisibility(View.GONE);
				mTvCaptureBackChk.setVisibility(View.GONE);
				mRelLayBackChk.setClickable(false);
			}
			//Show void image received from server
			else if(responseData == EventTypes.EVENT_DOWNLOAD_VOID_IMAGE){
				mRelLayVoidChk.setBackground(new BitmapDrawable(mContext.getResources(), decodedByte));
				mTvVoidChkImgBtn.setVisibility(View.GONE);
				mTvCaptureVoidChk.setVisibility(View.GONE);
				mRelLayVoidChk.setClickable(false);
			}
		}
		hideLoader();
	}

	/**
	 * Used to calculate actual credit amount. Here app will deduct fees amount from actual credited amount
	 */
	private void setActualCreditAmount(){
		double checkAmount = Double.valueOf(mCheckAmount);
		double checkFees = Double.valueOf(mChkFees);
		mTvChkAmt.setText(mContext.getResources().getString(R.string.check_details_amount, "$"+Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(checkAmount - checkFees)))));
	}

	private void refreshListFromServer() {
		hideLoader();
		Constants.setMyCheckServerData(null);
		Intent i = new Intent(getActivity(), HomeActivity.class);
		startActivity(i);
		getActivity().finish();
	}


	@Override
	public void backButtonClick() {
		if(mActionRequiredStatus == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT){
			showAlertDialog(mContext.getResources().getString(R.string.error_confirm_void_chk), mContext.getResources().getString(R.string.error_confirm_void_chk), mContext.getResources().getString(R.string.btn_ok), mContext.getResources().getString(R.string.btn_do_later),
					this);
		}
		else{
			getActivity().finish();
		}
	}


	@Override
	public void onGetFundingMechanismSuccess(GetFundingMechanismHandler fundingMechanismObject) {
		if(mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT ||
				mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK ||
				mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK ||
						mActionRequiredStatus ==  Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT){
			
			String mfundType = "";
			ArrayList<String> mFundingMechanismTypes = new ArrayList<String>();
			if (fundingMechanismObject.getCardList() != null
					&& fundingMechanismObject.getCardList().size() > 0) {
				for (int i = 0; i < fundingMechanismObject.getCardList().size(); i++) {
					String fundingMechanismType = fundingMechanismObject
							.getCardList().get(i).getType().toString();
					mfundType = Util.getAccountType(mContext,
							fundingMechanismObject.getCardList().get(i)
									.getType().toString());
					
					if( mFundingMechanismID.equals(fundingMechanismObject.getCardList().get(i)
							.getID().toString())){
					
					if (fundingMechanismType.toLowerCase(Locale.US).contains(
							"card")) {
						mFundingMechanismTypes.add(0, mfundType);
						mFundingMechanismIDList.add(0, fundingMechanismObject
								.getCardList().get(i).getID().toString());
						break;
					} else {
						mFundingMechanismTypes.add(mfundType);
						mFundingMechanismIDList.add(fundingMechanismObject
								.getCardList().get(i).getID().toString());
						break;
					}
					}
				}

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						mFundingMechanismTypes);

				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mEtTransMomntCardNum.setAdapter(dataAdapter);
				
				if(mFundingMechanismTypes != null && mFundingMechanismTypes.size() > 0) {
					mFundingMechanismType = Util.getAccountType(mContext, mFundingMechanismTypes.get(mEtTransMomntCardNum.getSelectedItemPosition()));
				}
				
//				mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,"Momentum Card"));
				
				if(mActionRequiredStatus == Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT){
					String feeAmount = Util.getFormattedAmount(Double.valueOf(mChkFees));
					String checkAmount = "";
					if(mFeesAgreed.equals("true")){
						checkAmount = Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(Double.valueOf(mCheckAmount) - Double.valueOf(mChkFees))));
					}
					else{
						checkAmount = Util.getFormattedAmount(Double.valueOf(new DecimalFormat("0.00").format(mCheckAmount)));
					}
					mTvHaderTitle.setText(mContext.getResources().getString(R.string.fees_text, 
							feeAmount, checkAmount, ": " + mFundingMechanismType));
					mTvFunding.setText(mContext.getResources().getString(R.string.funding_to, mFundingMechanismType));
				}
			}
		}
		else {
			if (fundingMechanismObject.getCardList() != null && fundingMechanismObject.getCardList().size() > 0) {
				for(int i =0; i < fundingMechanismObject.getCardList().size(); i++) {
					if (fundingMechanismObject.getCardList().get(i).getID().toString().equalsIgnoreCase(mFundingMechanismID)) {
						mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,
								Util.getAccountType(getActivity(), fundingMechanismObject.getCardList().get(i).getType().toString())));
						//mTvFunding.setText(mContext.getResources().getString(R.string.funding_to,fundingMechanismObject.getCardList().get(i).getType().toString()));
					}
				}
			}
		}
		hideLoader();
	}

}
