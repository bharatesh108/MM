package com.dfc.moneymartus;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

public final class AppAnalytics {

	private static AppAnalytics sAppAnalytic;

	private final GoogleAnalytics mGoogleAnalytics;
	private final Tracker mAppTracker;

	/**
	 * AppNalytice Constructor
	 * 
	 * @param context
	 * @param configResId
	 */
	private AppAnalytics(final Context context, final int configResId) {
		mGoogleAnalytics = GoogleAnalytics.getInstance(context);
		mGoogleAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		// Remove comment for below code line to enable auto Activity tracking
		// googleAnalytics.enableAutoActivityReports((Application) context);
		mAppTracker = mGoogleAnalytics.newTracker(configResId);
	}

	/**
	 * Creates single instance of Google Analytics object for the application.
	 * Create an instance of this in the project's Application class and use it
	 * through out the project for better performance and usage.
	 * 
	 * @param context
	 *            Application context
	 * @param configResId
	 *            Configuration XML file id.
	 * @return Singleton instance of MoneyMart Analytics object
	 */
	public static AppAnalytics getAppAnalytics(final Context context,
			final int configResId) {
		synchronized (AppAnalytics.class) {
			if (sAppAnalytic == null) {
				sAppAnalytic = new AppAnalytics(context, configResId);
			}
			return sAppAnalytic;
		}
	}

	public void enableAdvertising() {
		mAppTracker.enableAdvertisingIdCollection(true);
	}

	public void disableAdvertising() {
		mAppTracker.enableAdvertisingIdCollection(false);
	}

	/**
	 * This method will switch on or off the sending of tracking reports to
	 * Google Analytics. By default it is set to false, which means all the hits
	 * are dispatched.
	 * 
	 * @param runForTesting
	 *            True to run the app for debug / testing, where Application
	 *            will not send any reports to Google Analytics. Else false.
	 */
	public void enableTestExecution(final boolean runForTesting) {
		// When dry run is set, hits will not be dispatched, but will still be
		// logged as though they were dispatched.
		mGoogleAnalytics.setDryRun(runForTesting);
	}

	/**
	 * Used to send the screen that user is viewing
	 * 
	 * @param screenName
	 *            Screen that the user is viewing
	 */
	public void trackScreen(final String screenName) {
		synchronized (AppAnalytics.class) {
			mAppTracker.setScreenName(screenName);
			// Build and send screen.
			mAppTracker.send(new HitBuilders.AppViewBuilder().build());
		}
	}

	/**
	 * Used to send an event to google analytics
	 * 
	 * @param category
	 *            The event category. For ex: Video / Audio
	 * @param action
	 *            The event action. For ex: Play / Pause / Resume
	 */
	public void trackEvent(final String category, final String action) {
		synchronized (AppAnalytics.class) {
			System.out.println("Analytics : " + category + " " + action);
			mAppTracker.send(new HitBuilders.EventBuilder()
					.setCategory(category).setAction(action).build());
		}

	}

	/**
	 * Method used to send an event to google analytics
	 * 
	 * @param category
	 *            The event category. For ex: Video / Audio
	 * @param action
	 *            The event action. For ex: Play / Pause / Resume
	 * @param label
	 *            The event label. For ex: "The Wizard of Oz" /
	 *            "Gone With the Wind" (Name of the Video or audio file)
	 */
	public void trackEvent(final String category, final String action, final String label) {
		synchronized (AppAnalytics.class) {
			mAppTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
		}
	}

	/**
	 * Used to send an event to google analytics
	 * 
	 * @param category
	 *            The event category. For ex: Video / Audio
	 * @param action
	 *            The event action. For ex: Play / Pause / Resume
	 * @param label
	 *            The event label. For ex: "The Wizard of Oz" /
	 *            "Gone With the Wind" (Name of the Video or audio file)
	 * @param value
	 *            The event value. For ex: 4 (No of times that the video was
	 *            played)
	 */
	public void trackEvent(final String category, final String action, final String label, final long value) {
		synchronized (AppAnalytics.class) {
			mAppTracker.send(new HitBuilders.EventBuilder().setCategory(category)
					.setAction(action).setLabel(label).setValue(value).build());
		}
	}

	/**
	 * Used to send an exception
	 * 
	 * @param exceptionDetails
	 *            Complete exception message like class name : method name :
	 *            cause of exception
	 * @param isFatal
	 *            true if fatal exception
	 */
	public void trackException(final String exceptionDetails, final boolean isFatal) {
		synchronized (AppAnalytics.class) {
			// Build and send exception.
			mAppTracker.send(new HitBuilders.ExceptionBuilder().setDescription(exceptionDetails).setFatal(isFatal).build());
		}
	}

}

