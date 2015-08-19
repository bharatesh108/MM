package com.dfc.moneymartus.gateway;

import android.os.AsyncTask;
import android.util.Log;

import com.dfc.moneymartus.business.RecaptureCheckHandler;

public class DownloadCheckImage 
extends AsyncTask<String, Void, Integer> {

	private final RecaptureCheckHandler mDownImgHandler;

	/**
	 * Constructor
	 * @param handler Object of data class
	 */
	public DownloadCheckImage(final RecaptureCheckHandler handler) {
		super();
		mDownImgHandler = handler;
	}

	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		Log.d("Download Check image", "onPreExecute");
	}

	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Integer doInBackground(String... urls) {
		try{
			//statusCode = submitVoidedCheck(requestURL, jsonData, mBase64VoidImage);
			mDownImgHandler.getImage();

		} catch (Exception e) {
			e.getMessage();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Integer result) {
		super.onPostExecute(result);
		mDownImgHandler.onResponseSuccess(result, mDownImgHandler);
	}

}
