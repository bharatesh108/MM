package com.dfc.moneymartus.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.view.activity.LoginActivity;

public class BaseFragment extends Fragment implements OnClickListener {
	private ProgressDialog mProgressDialog;
	protected Dialog mDialog;
	protected String mDialogId = null;
	private Activity mActivity;
	private Preferences mPreferences;

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mActivity = getActivity();
		mPreferences = Preferences.getInstance(mActivity);
		/* getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); */

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * This is used to show a progress dialog with default loading text
	 **/
	public void showLoader() {

		if (mActivity != null) {
			mActivity.runOnUiThread(new RunShowLoader(mActivity.getResources()
					.getString(R.string.pbar_text_loading)));

		}
	}

	/**
	 * This is used to show a progress dialog with the passed msg text
	 * 
	 * @param msg Text to show along with progress dialog
	 */
	public void showLoader(final String msg) {
		if (mActivity != null) {
			mActivity.runOnUiThread(new RunShowLoader(msg));
		}
	}

	/**
	 * Implementing Runnable for runOnUiThread(), This will show a progress dialog
	 */
	class RunShowLoader implements Runnable {
		private String strMsg = "";

		public RunShowLoader(final String strMsg) {
			if(strMsg != null) {
				this.strMsg = strMsg;
			}
		}

		@Override
		public void run() {
			try {
				if (mProgressDialog == null || !mProgressDialog.isShowing()) {
					if (mActivity != null && !mActivity.isFinishing()) {
						mProgressDialog = ProgressDialog.show(mActivity, "", strMsg);
					} else {
						
					}
				} else {
					mProgressDialog.setMessage(strMsg);
				}
				mProgressDialog.setCancelable(false);
			} catch (Exception exception) {
				mProgressDialog = null;
			}
		}
	}

	/**
	 * This is used to hide the running progress dialog
	 */
	public void hideLoader() {
		if (mActivity != null) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						if (mProgressDialog != null && mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
						mProgressDialog = null;
					} catch (Exception exception) {
						exception.getMessage();
					}
				}
			});
		}
	}

	/**
	 * Makes a standard toast that just contains a text view.
	 * 
	 * @param strMsg The message to show. Can be formatted text.
	 */
	public void showToast(final String strMsg) {
		if (mActivity != null) {
			Toast.makeText(mActivity, strMsg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * This method displays the alert dialog with default positive button.
	 * 
	 * @param message Message to display
	 * @param onClickListener Listener for positive button i.e. OK or YES
	 */
	/*	public void showAlertDialog(final String message, final DialogInterface.OnClickListener onClickListener) {
		if (mActivity != null) {
			hideLoader();
			final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(getString(R.string.lbl_alert));
			builder.setMessage(message);
			builder.setPositiveButton(getString(R.string.btn_ok), onClickListener);
			if (!mActivity.isFinishing()) {
				builder.show();
			}
		}
	}*/

	/**
	 * This method displays the alert dialog with default positive button.
	 * 
	 * @param message Message to display
	 * @param onClickListener Listener for positive button i.e. OK or YES
	 */
	public void showAlertDialog(final int title, final String message, final OnClickListener onClickListener,
			final int icon) {
		hideLoader();
		if (mActivity != null) {
			mDialog = new Dialog(mActivity);
			mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.alert_dialog_ok);
			mDialog.setCancelable(false);

			final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
			final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
			final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
			if (icon != 0) {
				tvDialogTitle.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
			}
			tvDialogTitle.setText(title);
			tvDialogMessage.setText(message);

			btnOk.setOnClickListener(onClickListener);

			mDialog.show();
		}
	}

	/**
	 * This method displays the alert dialog
	 * @param title Dialog box title
	 * @param Message to display
	 * @param onClickListener Dialog button click event listener
	 * @param icon Image to display
	 * @param left Display button 
	 * @param right Display button 
	 */
	public void showAlertDialog(final int title, final String message, final OnClickListener onClickListener,
			final int icon, final String left, final String right) {
		hideLoader();
		if (mActivity != null) {
			mDialog = new Dialog(mActivity);
			mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.alert_dialog_ok);
			mDialog.setCancelable(false);

			final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
			final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
			final Button btnRight = (Button) mDialog.findViewById(R.id.btn_right);
			final Button btnLeft = (Button) mDialog.findViewById(R.id.btn_left);
			btnLeft.setVisibility(View.VISIBLE);
			btnRight.setText(right);
			btnLeft.setText(left);
			if (icon != 0) {
				tvDialogTitle.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
			}
			tvDialogTitle.setText(title);
			tvDialogMessage.setText(message);

			btnRight.setOnClickListener(onClickListener);
			btnLeft.setOnClickListener(onClickListener);

			mDialog.show();
		}
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
		mDialog = new Dialog(mActivity);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);

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
	
	public void showAlertDialog(final String dialogId, final String title, final String message, final String btnPositive, final String btnNegative,
			final OnClickListener onClickListener) {
		this.mDialogId = dialogId;
		mDialog = new Dialog(mActivity);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);
		final TextView tvDialogMessage = (TextView) mDialog
				.findViewById(R.id.tv_display_msg);
		tvDialogMessage.setLineSpacing(2, 1);
		final TextView tvTitleMessage = (TextView) mDialog
				.findViewById(R.id.tv_title_msg);
		tvTitleMessage.setText(title);
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

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right || view.getId() == R.id.btn_left) {
			mDialog.dismiss();
		}
	}

	/**
	 * This error alert dialog box is used to show if server response is unauthorized and on click of dialog button
	 * app will clear all stored data and navigate to login screen
	 * @param message Message to display
	 * @param onClickListener Button click event listener
	 */
	public void showAlertDialogUnAuthorised(Context context, final String message, final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(context);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

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
				
				Intent intent = new Intent(mActivity, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		});

		mDialog.show();
	}

	/**
	 * Used to show alert dialog with single button
	 * @param message Message to display
	 * @param onClickListener Button click event listener
	 */
	public void showAlertDialog(final String message, final OnClickListener onClickListener) {
		hideLoader();
		//if (mActivity != null) {
		mDialog = new Dialog(mActivity);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(onClickListener);

		mDialog.show();
	}

	/**
	 * Used to show alert based on dialog id
	 * @param message Message to display
	 * @param dialogId Dialog id
	 * @param onClickListener Button click event listener
	 */
	public void showAlertDialog(final String message, String dialogId, final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(mActivity);
		this.mDialogId = dialogId;
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(onClickListener);
		mDialog.show();
	}
	
	/**
	 * Used to show alert based on dialog id
	 * @param context Activity 
	 * @param message Message to display
	 * @param dialogId Dialog id
	 * @param onClickListener Button click event listener
	 */
	public void showAlertDialog(Context context, final String message, String dialogId, final OnClickListener onClickListener) {
		hideLoader();
		mDialog = new Dialog(context);
		this.mDialogId = dialogId;
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(onClickListener);
		mDialog.show();
	}
	

	/**
	 * Used to show alert dialog with single button
	 * @param message Message to display
	 * @param onClickListener Button click event listener
	 */
	public void showAlertDialog(Context context, final String message, final OnClickListener onClickListener) {
		hideLoader();
		//if (mActivity != null) {
		mDialog = new Dialog(context);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_dialog_ok);
		mDialog.setCancelable(false);

		final TextView tvDialogTitle = (TextView) mDialog.findViewById(R.id.tv_title_msg);
		final TextView tvDialogMessage = (TextView) mDialog.findViewById(R.id.tv_display_msg);
		final Button btnOk = (Button) mDialog.findViewById(R.id.btn_right);
		tvDialogTitle.setVisibility(View.GONE);
		tvDialogMessage.setText(message);

		btnOk.setOnClickListener(onClickListener);

		mDialog.show();
	}

}
