package com.dfc.moneymartus.view.adapter;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dfc.moneymartus.R;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.HomeActivity;

public class MyChecksAdapter extends BaseAdapter implements OnClickListener{

	private ViewHolder mHolder;
	private Context mContext;
	private ArrayList<ChecksObject> mListData= new ArrayList<ChecksObject>();
	private ChecksObject mCurrentListObject;
	private Dialog mDialog;
	
	/*
	 * Constructor
	 */
	public MyChecksAdapter() {
		super();
	}

	/*
	 * Constructor
	 * @param Context Activity context
	 * @param ArrayList List data array
	 */
	public MyChecksAdapter(Context mContext, ArrayList<ChecksObject> listData) {
		this.mContext = mContext;
		//inflater = LayoutInflater.from(mContext);
		this.mListData = listData;
	}

	/**
	 * Used to get total count
	 */
	@Override
	public int getCount() {
		if (mListData == null || mListData.isEmpty()) {
			return 0;
		} else {
			return mListData.size();
		}
	}

	/**
	 * Used to get item at position
	 * @param position Item position
	 */
	@Override
	public ChecksObject getItem(int position) {
		return mListData.get(position);
	}

	/**
	 * Used to get item id
	 * @param position List item position
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * Used to get item
	 * @return ArrayList List data in array
	 */
	public ArrayList<ChecksObject> getItems(){
		return this.mListData;
	}

	/**
	 * Used to populate data
	 * @param listData List data
	 */
	public void setItems(ArrayList<ChecksObject> listData){
		this.mListData = listData;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		mHolder = new ViewHolder();
		View convertView = rowView;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.my_checks_row_details, parent, false);
//			convertView.setBackgroundColor(Color.parseColor(mContext.getResources().getString(R.color.moneymart_login_bg)));
			mHolder.mCheckStatusColor = (View) convertView.findViewById(R.id.v_mycheck_status);
			mHolder.mTvCheckTypeImage = (ImageView) convertView.findViewById(R.id.iv_mycheck_icon_image);
			mHolder.mTvCheckMakerName = (TextView) convertView.findViewById(R.id.tv_check_name);
			mHolder.mTvCheckAmount = (TextView) convertView.findViewById(R.id.tv_check_amount);
			mHolder.mTvCheckDate = (TextView) convertView.findViewById(R.id.tv_check_date);
			mHolder.mStatusImageOne = (ImageView) convertView.findViewById(R.id.iv_uploading_image_one);
			mHolder.mStatusImageTwo = (ImageView) convertView.findViewById(R.id.iv_uploading_image_two);
			mHolder.mStatusImageThree = (ImageView) convertView.findViewById(R.id.iv_uploading_image_three);
			mHolder.mTvCheckStatus = (TextView) convertView.findViewById(R.id.tv_check_status);
			mHolder.mUploadProgress = (ProgressBar) convertView.findViewById(R.id.upload_progress);
			mHolder.flUploadFailed = (Button) convertView.findViewById(R.id.btn_upload_failed);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.flUploadFailed.setTag(position);
		/*MyChecksDataObject obj = new MMChecksObject();
		obj = listData.get(0);

		holder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.queued_icon));
		holder.mTvCheckMakerName.setText(obj.getmCheckMakerName());
		holder.mTvCheckAmount.setText(obj.getmCheckAmount());
		holder.mTvCheckDate.setText(obj.getmCheckMadeDate());
		holder.mTvCheckStatus.setText(obj.getmCheckStatus());*/
		mHolder.mUploadProgress.setVisibility(View.GONE);
		mHolder.flUploadFailed.setVisibility(View.GONE);

		mHolder.flUploadFailed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//((HomeActivity)mContext).retryCheckUpload(Integer.parseInt(v.getTag().toString()));
				// Re-try check upload
				if (Util.isNetworkAvailable(mContext)) {
					((HomeActivity)mContext).retryCheckUpload(Integer.parseInt(v.getTag().toString()));
				} else {
					showAlertDialog(mContext.getResources().getText(R.string.error_network).toString(), null);
				}
			}
		});

		setViewVisibility(new ImageView[]{mHolder.mStatusImageOne, mHolder.mStatusImageTwo, mHolder.mStatusImageThree},  true);

		mCurrentListObject = mListData.get(position);

		mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.queued));
		if (mCurrentListObject.getChequeMaker() != null && mCurrentListObject.getChequeMaker() != "null")
			mHolder.mTvCheckMakerName.setText(mCurrentListObject.getChequeMaker());
		mHolder.mTvCheckAmount.setText("$ "+Util.getdoubleUSPriceFormat(mCurrentListObject.getAmount()));

		if(!getCheckStatus(mCurrentListObject.getStatusCode()).equals("Queued") && !getCheckStatus(mCurrentListObject.getStatusCode()).equals("Uploading"))
			mHolder.mTvCheckDate.setText(Util.getyyyymmddDate(mCurrentListObject.getDateMade(), true));//(obj.getDateMade());
		//		else
		//			holder.mTvCheckDate.setText(currentListObject.getDateMade());//(obj.getDateMade());
		prepareMyChecksRowIndicators(mHolder.mCheckStatusColor, mHolder.mTvCheckTypeImage, mHolder.mStatusImageOne, mHolder.mStatusImageTwo, mHolder.mStatusImageThree, mHolder.mTvCheckStatus, mCurrentListObject.getStatusCode());
		String data = getCheckStatus(mCurrentListObject.getStatusCode());
		mHolder.mTvCheckStatus.setText(data);

		return convertView;
	}
	/*
	private String getyyyymmddDate(String dateMade) {
		SimpleDateFormat isoDateParser = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss ZZZZZ", Locale.US);//("yyyy-MM-dd 'T'HH:mm:ss", Locale.US);
		SimpleDateFormat isoStringParser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String formattedTime = "";
//		DateFormat dateDefault = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss 'Z'",Locale.US);
		Date parsedDate = null;
		try {
			parsedDate = isoDateParser.parse(dateMade);
			formattedTime = isoStringParser.format(parsedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedTime;
	}
	 */
	
	/**
	 * Used to get check status
	 * @param statusCode UAR status code
	 */
	private String getCheckStatus(String statusCode) {
		int statusCodeVal = Integer.parseInt(statusCode);
		String statusData = null;
		switch (statusCodeVal) {
		case Constants.CheckStatus.WAITING_FOR_REVIEW :
			statusData = mContext.getResources().getString(R.string.review_pending_status_msg);
			break;

		case Constants.CheckStatus.USER_ACTION_REQUIRED :
			statusData = mContext.getResources().getString(R.string.action_required_status_msg);
			break;

		case Constants.CheckStatus.CASHED :
			statusData = mContext.getResources().getString(R.string.cashed_status_msg);
			break;

		case Constants.CheckStatus.REJECTED :
			statusData = mContext.getResources().getString(R.string.rejected_status_msg);
			break;

		case Constants.CheckStatus.QUEUED :
			statusData = mContext.getResources().getString(R.string.queued_status_msg);
			break;

		case Constants.CheckStatus.UPLOADING :
			statusData = mContext.getResources().getString(R.string.uploading_status_msg);
			break;

		case Constants.CheckStatus.UPLOADING_FAILED :	
			statusData = mContext.getResources().getString(R.string.uploading_failed_status_msg);
			break;
		default:
			break;
		}
		return statusData;
	}

	/**
	 * Used to set check list indicator based on check status also change the text color
	 * @param colorView Left side bar indicator color
	 * @param ivCheckIcon Check icon
	 * @param indOne First indicator image
	 * @param indTwo Second indicator image
	 * @param indThree Third indicator image
	 * @param tv Text view
	 * @param statusCode Check status code
	 */
	private void prepareMyChecksRowIndicators(View colorView, ImageView ivCheckIcon, ImageView indOne, ImageView indTwo, ImageView indThree, TextView tv, String statusCode) {
		int statusCodeVal = Integer.parseInt(statusCode);
		switch (statusCodeVal) {
		case Constants.CheckStatus.QUEUED :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.queued_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.queued_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.queued));
			setViewVisibility(new ImageView[]{indOne, indTwo, indThree},  false);
			break;

		case Constants.CheckStatus.UPLOADING :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.uploading_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.uploading_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.uploading));
			mHolder.mUploadProgress.setVisibility(View.VISIBLE);
			Resources res = mContext.getResources();
			Drawable drawable = res.getDrawable(R.drawable.upload_progressbar_drawable);
			mHolder.mUploadProgress.setProgress(25);   // Main Progress
			mHolder.mUploadProgress.setSecondaryProgress(50); // Secondary Progress
			mHolder.mUploadProgress.setMax(100); // Maximum Progress
			mHolder.mUploadProgress.setProgressDrawable(drawable);
			if(!mCurrentListObject.isProgressUpdateRequired())
				mHolder.mUploadProgress.setVisibility(View.INVISIBLE);

			ObjectAnimator animation = ObjectAnimator.ofInt(mHolder.mUploadProgress, "progress", 0, 10, 20, 40, 80, 100);
			animation.setDuration(2000);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setInterpolator(new DecelerateInterpolator());
			animation.start();

			mHolder.mTvCheckDate.setText("");
			setImageView(new StatusImage[]{new StatusImage(indOne, false), new StatusImage(indTwo, false), new StatusImage(indThree, false)});

			break;

		case Constants.CheckStatus.UPLOADING_FAILED :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.uploading_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.uploading_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.uploading_failed));
			setViewVisibility(new ImageView[]{indOne, indTwo, indThree},  false);
			mHolder.flUploadFailed.setVisibility(View.VISIBLE);
			mHolder.mTvCheckDate.setText("");

			break;

		case Constants.CheckStatus.USER_ACTION_REQUIRED :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.action_required_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.action_required_font_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.action_required));

			int actionStatus = getCheckActionRequired();
			switch (actionStatus) {
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT:
				setImageView(new StatusImage[]{new StatusImage(indOne, false), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
				break;
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK:
				setImageView(new StatusImage[]{new StatusImage(indOne, false), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
				break;
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK:
				setImageView(new StatusImage[]{new StatusImage(indOne, false), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
				break;
			case Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
				setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
				break;
			case Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
				mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.action_required_two));
				setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
				break;
			default:
				break;
			}
			break;

		case Constants.CheckStatus.WAITING_FOR_REVIEW :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.review_pending_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.review_pending_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.review_pending));
			
			if(mCurrentListObject.getVoidImageUrl()!=null && !mCurrentListObject.getVoidImageUrl().equals("") && !mCurrentListObject.getVoidImageUrl().equals("null")){
				setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, true), new StatusImage(indThree, false)});
			}
			else{
				setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, false), new StatusImage(indThree, false)});
			}
			break;

		case Constants.CheckStatus.CASHED :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.cashed_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.cashed_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.cashed));
			setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, true), new StatusImage(indThree, true)});
			break;

		case Constants.CheckStatus.REJECTED :
			colorView.setBackgroundColor(mContext.getResources().getColor(R.color.rejected_status_color));
			tv.setTextColor(mContext.getResources().getColor(R.color.rejected_status_color));
			mHolder.mTvCheckTypeImage.setBackground(mContext.getResources().getDrawable(R.drawable.rejected));
			setImageView(new StatusImage[]{new StatusImage(indOne, true), new StatusImage(indTwo, true), new StatusImage(indThree, true)});
			break;
		default:
			break;
		}
	}

	/**
	 * This will be used hold row view data
	 */
	class ViewHolder{
		View mCheckStatusColor;
		ImageView mTvCheckTypeImage;
		TextView mTvCheckMakerName;
		TextView mTvCheckAmount;
		TextView mTvCheckDate;
		ImageView mStatusImageOne;
		ImageView mStatusImageTwo;
		ImageView mStatusImageThree;
		TextView mTvCheckStatus;
		ProgressBar mUploadProgress;
		Button flUploadFailed;
	}

	/**
	 * Used to set image view
	 * @param views image array
	 */
	private void setImageView(StatusImage... views) {
		for(int i=0;i< views.length; i++){
			StatusImage v = views[i];
			if(v.isChecked())
				v.getIv().setImageDrawable(mContext.getResources().getDrawable(R.drawable.tick));
			else
				v.getIv().setImageDrawable(mContext.getResources().getDrawable(R.drawable.untick));
		}
	}

	/**
	 * Used to set view visibility
	 * @param ImageViews[] image array
	 * @param boolean status of view visibility
	 */
	private void setViewVisibility(ImageView[] views, boolean isVisible) {
		for(int k=0;k < views.length; k++){
			ImageView v = views[k];
			if(isVisible)
				v.setVisibility(View.VISIBLE);
			else
				v.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Used to set image indicator
	 */
	private class StatusImage {
		private final ImageView iv;
		private final boolean isChecked;

		public StatusImage(ImageView iv,boolean isChecked){
			this.iv = iv;
			this.isChecked = isChecked;
		}

		public ImageView getIv() {
			return iv;
		}

		public boolean isChecked() {
			return isChecked;
		}

	}

	/*
	 * Used to check action required
	 */
	private int getCheckActionRequired() {
		int pos = 0;
		if (mCurrentListObject.getActionRequired().size() == 1) {
			if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image front")){
				pos = 11;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image back")){
				pos = 12;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Submit voided cheque image front")){
				pos = 13;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit voided cheque image front")){
				pos = 14;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Contact customer services")){
				pos = 15;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Agree to terms")){
				pos = 16;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Agree to fees")){
				pos = 17;
			}
		} else if (mCurrentListObject.getActionRequired().size() == 2){
			if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image front") 
					&& mCurrentListObject.getActionRequired().get(1).getAction().equalsIgnoreCase("Resubmit cheque image back") ){
				pos = 18;
			} else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Submit voided cheque image front")){
				pos = 13;
			}else if(mCurrentListObject.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit voided cheque image front")) {
				pos = 13;	
			}
		}
		return pos;		
	}

	/**
	 * Used to show alert dialog
	 * @param message Dialog message
	 * @param onClickListener Dialog button click event listener
	 */
	public void showAlertDialog(final String message, final OnClickListener onClickListener) {
		mDialog = new Dialog(mContext);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View view) {
		/*
		 *  Dialog button click call back
		 */
		if (view.getId() == R.id.btn_right || view.getId() == R.id.btn_left) {
			mDialog.dismiss();
		}
	}
}
