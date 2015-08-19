package com.dfc.moneymartus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.view.callback.ScreenReceiver;


public class Moneymart extends Application {

	private static Context sContext;
	public static Activity sCurrentActivity = null;
	public static final String TAG = Moneymart.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private static  Moneymart sInstance;
	static AppAnalytics sMoneyMartAnalytics;
	private Activity mActivity;

	public  ArrayList<ChecksObject> myChecksList= new ArrayList<ChecksObject>() ;
	public LinkedHashMap<String, MyChecksDataObject> myCheckUploadQueue = new LinkedHashMap<String, MyChecksDataObject>();
	public TreeMap<String, ChecksObject> failedChecksList= new TreeMap<String, ChecksObject>(Collections.reverseOrder()) ;


	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		setContext(this);
		createAppAnalyticsInstance();
		setsInstance(this);

	}

	/*
	 * set activity instance
	 */
	public void setInstanceToFinish(final Activity activity) {
		this.mActivity = activity;
	}

	/*
	 * get activity instance
	 */
	public Activity getInstanceToFinish() {
		return mActivity;
	}

	/*
	 * Used to set MoneyMart instance
	 */
	private void setsInstance(final Moneymart instance) {
		sInstance = instance;
	}

	/*
	 * Used to set MoneyMart context
	 */
	private void setContext(final Moneymart currentObj) {
		sContext = currentObj;
	}

	/*
	 * Used to get current activity instance
	 */
	public static Activity getCurrentActivity() {
		return sCurrentActivity;
	}

	/*
	 * Used to get MoneyMart instance
	 */
	public static  Moneymart getInstance() {
		synchronized (sInstance) {
			return sInstance;
		}

	}

	/**
	 * Used to get app context
	 * @return Application context
	 */
	public static Context getContext() {
		return sContext;
	}

	/*
	 * create AppAnalytics instance
	 */
	private void createAppAnalyticsInstance() {
		sMoneyMartAnalytics = AppAnalytics.getAppAnalytics(getContext(), R.xml.app_tracker);
		//		moneyMartAnalytics.enableAdvertising();
		sMoneyMartAnalytics.enableTestExecution(false);
	}

	/*
	 * Used to get AppAnalytics instance
	 */
	public static AppAnalytics getAppAnalytics() {
		return sMoneyMartAnalytics;
	}

	/*
	 * Used to get new request queue
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/*
	 * Used to add new request to queue
	 */
	public <T> void addToRequestQueue(Request<T> req, final String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	/*
	 * Used to add new request to queue
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		req.setRetryPolicy(new DefaultRetryPolicy(Constants.Timeout,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getRequestQueue().add(req);
	}

	/*
	 * Used to cancel pending request
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * Used to get resource string value
	 * 
	 * @param resId
	 *              String resource id
	 * @return String resource value
	 */
	public static String getResString(final int resId) {
		return getContext().getResources().getString(resId);
	}

	public static Activity getsCurrentActivity() {
		return sCurrentActivity;
	}

	public static void setsCurrentActivity(Activity sCurrentActivity) {
		Moneymart.sCurrentActivity = sCurrentActivity;
	}

}
