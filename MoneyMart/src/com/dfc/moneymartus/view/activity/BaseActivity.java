package com.dfc.moneymartus.view.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.OnBackPressedListener;
import com.dfc.moneymartus.view.callback.ScreenReceiver;

@SuppressLint("HandlerLeak")
public abstract class BaseActivity extends FragmentActivity implements
OnClickListener {

	private ProgressDialog mProgressDialog;
	protected Dialog mDialog;
	protected String mDialogId = null;
	protected String mDialogMessage;
	//	View mViewStub;
	public Preferences mPreferences;
	static Context mContext;
	protected OnBackPressedListener onBackPressedListener;

	public static final long DISCONNECT_TIMEOUT = 300000; // 5 min = 5 * 60 *
	// 1000 ms
	private IntentFilter filter;
	private ScreenReceiver mReceiver;
	private long storedTime;


	@Override
	protected void onCreate(final Bundle savedInstance) {
		Log.e("BaseActivity",":"+"oncreate");
		super.onCreate(savedInstance);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setmContext(this);
		mPreferences = Preferences.getInstance(this);
		mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());

	/*	filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction( Intent.ACTION_USER_FOREGROUND );
		filter.addAction( Intent.ACTION_USER_BACKGROUND);
		mReceiver = new ScreenReceiver(this);
		registerReceiver(mReceiver, filter);*/

	}

	/*public static void setmContext(Context mContext) {
		BaseActivity.mContext = mContext;
	}

	@Override
	public void onUserInteraction() {
		Log.e("onUserInteraction",":" + "onUserInteraction");
		mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());
		super.onUserInteraction();
	}


	@Override
	protected void onUserLeaveHint() {
		Log.e("onUserLeaveHint",":" + "onUserLeaveHint");
		mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());
		super.onUserLeaveHint();
	}


	@Override
	protected void onResume() {
		Log.e("onResume",":" + "onResume");
		checkTimeDiff();
		super.onResume();
	}


	public void checkTimeDiff() {
		storedTime = mPreferences.getLong(PreferenceConstants.APP_BACKGROUND_TIME);
		if (storedTime == 0) {
			Log.e("checkTimeDiff",":" + "== 0");
			mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());
		} else {
			long timeDIfference = System.currentTimeMillis() - storedTime;
			if(System.currentTimeMillis() < storedTime) {
				Log.e("checkTimeDiff",":" + "current is less than stored");
				clearReferences();
				clearAppData();
			}
			if(timeDIfference >= 60000) {
				Log.e("checkTimeDiff",":" + "current is >= 60000");
				clearReferences();
				clearAppData();
			} else {
				mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());
			}
		}
	}


	public void clearAppData() {
		if(Constants.authToken!=null&&Constants.authToken.length()>0){
			Log.e("clearAppData",":" + "clearAppData");
			mPreferences.delete(PreferenceConstants.AUTORIZATION_TOKEN);
			Constants.setAuthTOken("");
			Constants.setCustServiceObj(null);
			Constants.setMyCheckServerData(null);
			Constants.CCServiceObj = null;

			Moneymart.getInstance().myChecksList = new ArrayList<ChecksObject>();
			Moneymart.getInstance().myCheckUploadQueue = new LinkedHashMap<String, MyChecksDataObject>();
			Moneymart.getInstance().failedChecksList = new TreeMap<String, ChecksObject>();
			Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}

	}*/

	private final Handler disconnectHandler = new Handler() {
		public void handleMessage(Message msg) {
			//ToDo

		}
	};

	private final Runnable disconnectCallback = new Runnable() {
		@Override
		public void run() {
			if(Constants.authToken!=null&&Constants.authToken.length()>0){
				mPreferences.delete(PreferenceConstants.AUTORIZATION_TOKEN);
				Constants.setAuthTOken("");
				Constants.setCustServiceObj(null);
				Constants.setMyCheckServerData(null);

				Moneymart.getInstance().myChecksList = new ArrayList<ChecksObject>();
				Moneymart.getInstance().myCheckUploadQueue = new LinkedHashMap<String, MyChecksDataObject>();
				Moneymart.getInstance().failedChecksList = new TreeMap<String, ChecksObject>();
				Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		}
	};


	public static void setmContext(Context mContext) {
		BaseActivity.mContext = mContext;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Moneymart.setsCurrentActivity(this);
		resetDisconnectTimer();
	}

	public void resetDisconnectTimer() {
		disconnectHandler.removeCallbacks(disconnectCallback);
		disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
	}

	public void stopDisconnectTimer() {
		disconnectHandler.removeCallbacks(disconnectCallback);
	}

	@Override
	public void onUserInteraction() {
		resetDisconnectTimer();
	}

	@Override
	public void onStop() {
		super.onStop();
		stopDisconnectTimer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		clearReferences();
	}

	protected void onDestroy() {
		super.onDestroy();
		clearReferences();
	}
	 
	/**
	 * 
	 * @return: Dialog Instance to dismiss on button click
	 */
	public Dialog getDialog() {
		return mDialog;
	}

	/**
	 * This is used to show a progress dialog with default loading text
	 **/
	public void showLoader() {
		runOnUiThread(new RunShowLoader(this.getResources()
				.getString(R.string.pbar_text_loading)));
	}

	/**
	 * This is used to show a progress dialog with the passed msg text
	 * 
	 * @param msg
	 *            Text to show along with progress dialog
	 */
	public void showLoader(final String msg) {
		runOnUiThread(new RunShowLoader(msg));
	}

	/**
	 * Implementing Runnable for runOnUiThread(), This will show a progress
	 * dialog
	 */
	class RunShowLoader implements Runnable {
		private final String strMsg;

		public RunShowLoader(final String strMsg) {
			this.strMsg = strMsg;
		}

		@Override
		public void run() {
			try {
				if (mProgressDialog == null || !mProgressDialog.isShowing()) {
					if (!isFinishing()) {
						mProgressDialog = ProgressDialog.show(
								BaseActivity.this, "", strMsg);
					}
				} else {
					mProgressDialog.setMessage(strMsg);
				}
				mProgressDialog.setCancelable(false);
			} catch (Exception e) {
				mProgressDialog = null;
			}
		}
	}

	/**
	 * This is used to hide the running progress dialog
	 */
	public void hideLoader() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (mProgressDialog != null && mProgressDialog.isShowing()) {
						mProgressDialog.dismiss();
					}
					mProgressDialog = null;
				} catch (Exception e) {
					mProgressDialog = null;
				}
			}
		});
	}

	/**
	 * Makes a standard toast that just contains a text view.
	 * 
	 * @param strMsg
	 *            The message to show. Can be formatted text.
	 */
	public void showToast(final String strMsg) {
		Toast.makeText(BaseActivity.this, strMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method displays the alert dialog with default positive
	 * button.Finishes the activity once ok is pressed
	 * 
	 * @param message
	 *            Message to display
	 * @param onClickListener
	 *            Listener for positive button i.e. OK or YES
	 */
	public void showAlertDialogAndFinishActivity(final String message,
			final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(BaseActivity.this);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogMessage.setText(message);
		if (onClickListener == null) {
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View view) {
					mDialog.dismiss();
					mDialog.cancel();
					mDialog = null;
					finish();

				}
			});

		} else {
			btnOk.setOnClickListener(onClickListener);
		}

		mDialog.setCancelable(false);
		mDialog.show();
	}

	/**
	 * This method displays the alert dialog with default positive button.
	 * 
	 * @param message
	 *            Message to display
	 * @param onClickListener
	 *            Listener for positive button i.e. OK or YES
	 */
	public void showAlertDialog(final String message,
			final OnClickListener onClickListener) {
		Log.e("show alert dialog",":"+"MMBaseActivity : " +this.getLocalClassName());
		hideLoader();
		mDialog = new Dialog(BaseActivity.this);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;
		// ImageView imgViewTitle =
		// (ImageView)dialog.findViewById(R.id.iv_title_image);
		TextView tvTitle = (TextView)mDialog.findViewById(R.id.tv_title_msg);
		tvTitle.setVisibility(View.GONE);
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogMessage.setText(message);
		if (onClickListener == null) {
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View view) {
					mDialog.dismiss();
					mDialog.cancel();
					mDialog = null;

				}
			});

		} else {
			btnOk.setOnClickListener(onClickListener);
		}

		mDialog.setCancelable(false);
		mDialog.show();
	}

	/**
	 * This method displays the alert dialog with default positive button.
	 * 
	 * @param message
	 *            Message to display
	 * @param onClickListener
	 *            Listener for positive button i.e. OK or YES
	 */
	public void showAlertDialog(final int title, final String message,
			final OnClickListener onClickListener, final int icon) {
		hideLoader();
		mDialog = new Dialog(BaseActivity.this);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;
		// ImageView imgViewTitle =
		// (ImageView)dialog.findViewById(R.id.iv_title_image);
		final TextView tvDialogTitle = (TextView) mDialog
				.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		if (icon != 0) {
			tvDialogTitle
			.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
		}
		tvDialogTitle.setText(title);
		tvDialogMessage.setText(message);
		if (onClickListener == null) {
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View view) {
					mDialog.dismiss();
					mDialog.cancel();
					mDialog = null;
				}
			});

		} else {
			btnOk.setOnClickListener(onClickListener);
		}

		mDialog.setCancelable(false);
		mDialog.show();
	}

	public void showAlertDialog(final String title, final String message,
			final OnClickListener onClickListener, final int icon,
			final String left, final String right, final String dialogID) {
		hideLoader();
		mDialog = new Dialog(BaseActivity.this);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogId  = dialogID;
		mDialogMessage = message;
		final TextView tvDialogTitle = (TextView) mDialog
				.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		final Button btnRight = (Button) mDialog.findViewById(R.id.btn_right);
		final Button btnLeft = (Button) mDialog.findViewById(R.id.btn_left);
		btnLeft.setVisibility(View.VISIBLE);
		btnRight.setText(right);
		btnLeft.setText(left);
		if (icon != 0) {
			tvDialogTitle
			.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
		}
		tvDialogTitle.setText(title);
		tvDialogMessage.setText(message);

		btnRight.setOnClickListener(onClickListener);
		btnLeft.setOnClickListener(onClickListener);

		mDialog.show();
	}

	/**
	 * This method displays the alert dialog with specified buttons.
	 * 
	 * @param message
	 *            Message to display
	 * @param btnPositive
	 *            The text to display in the positive button
	 * @param btnNegative
	 *            The text to display in the negative button
	 * @param onClickListener
	 *            Listener for buttons (positive or negative)
	 */
	public void showAlertDialog(final String message, final String title,
			final String btnPositive, final String btnNegative,
			final DialogInterface.OnClickListener onClickListener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(btnPositive, onClickListener);
		builder.setNegativeButton(btnNegative, onClickListener);

		builder.show();
	}

	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
			mDialog.cancel();
			mDialog = null;
		}
	}

	private void clearReferences() {
		Activity currActivity = Moneymart.getCurrentActivity();
		if (currActivity != null && currActivity.equals(this))
			Moneymart.sCurrentActivity = null;
	}

	public void showAlertDialogUnAuthorised(final String message, final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(this);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View v) {

				if(mPreferences != null)
					mPreferences.delete(PreferenceConstants.AUTORIZATION_TOKEN);
				Constants.setAuthTOken("");
				Constants.setCustServiceObj(null);
				Constants.setMyCheckServerData(null);
				Constants.setCCServiceObj(null);
				Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				BaseActivity.this.startActivity(intent);
				BaseActivity.this.finish();
			}
		});

		mDialog.show();
	}

	public void showAlertDialog(final String message, String dialogId, final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(this);
		this.mDialogId = dialogId;
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;
		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(onClickListener);
		mDialog.show();
	}

	/**
	 * This method displays the alert dialog with specified buttons.
	 * 
	 * @param message Message to display
	 * @param btnPositive The text to display in the positive button
	 * @param btnNegative The text to display in the negative button
	 * @param onClickListener Listener for buttons (positive or negative)
	 */
	public void showAlertDialog(final String dialogId, final String message, final String btnPositive, final String btnNegative,
			final OnClickListener onClickListener) {
		this.mDialogId = dialogId;
		mDialog = new Dialog(this);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		mDialogMessage = message;
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		final Button btnRight = (Button) mDialog.findViewById(R.id.btn_right);
		final Button btnLeft = (Button) mDialog.findViewById(R.id.btn_left);
		btnLeft.setVisibility(View.VISIBLE);
		btnRight.setText(btnNegative);
		btnLeft.setText(btnPositive);
		tvDialogMessage.setText(message);

		btnRight.setOnClickListener(onClickListener);
		btnLeft.setOnClickListener(onClickListener);

		mDialog.show();
	}

	public void showErrorDialog(VolleyError error, int eventType)
	{
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
	}

	public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
		this.onBackPressedListener = onBackPressedListener;
	}

	@Override
	public void onBackPressed() {
		if (onBackPressedListener == null){
			super.onBackPressed();
		} else {
			onBackPressedListener.backButtonClick();
		}
	}
}
