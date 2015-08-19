package com.dfc.moneymartus.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.dfc.moneymartus.infra.Constants;

public class Preferences {

	// Preferences keys
	public static final boolean DEFAULT_BOOLEAN = false;
	public static final String DEFAULT_STRING = null;
	public static final float DEFAULT_FLOAT = 0.0f;
	public static final long DEFAULT_LONG = 0l;
	public static final int DEFAULT_INT = 0;

	
	public static final String DEFAULT_KEY = "0";

	private final SharedPreferences mSharedPreferences;
	private final SharedPreferences.Editor mSharedPreferencesEditor;

	private static Preferences appPreferences;

	/**
	 * 
	 * @param context
	 *            A Context object to allow access to the default
	 *            mSharedPreferences object.
	 */
	Preferences(final Context context) {
		mSharedPreferences = context.getSharedPreferences(Constants.PreferenceConstants.PREFERENCE_FILE, Context.MODE_PRIVATE);
		mSharedPreferencesEditor = mSharedPreferences.edit();
	}

	/**
	 * Singleton instance of ApexPreferences
	 * 
	 * @param context
	 *            A Context object to allow access to the default
	 *            mSharedPreferences object.
	 * @return ApexPreferences object
	 */
	public static Preferences getInstance(final Context context) {
		//synchronized (Preferences.class) {
			if (appPreferences == null) {

				appPreferences = new Preferences(context);
			}
			return appPreferences;
//		}
		
	}

	/**
	 * clear all the stored value
	 */
	public void clearAllPrefs() {
		mSharedPreferencesEditor.clear();
		mSharedPreferencesEditor.commit();
	}

	// Getters methods for SharedPreference.

	/**
	 * Gets boolean value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return boolean true or false
	 * 
	 */

	public boolean getBoolean(final String key) {
		return mSharedPreferences.getBoolean(key, DEFAULT_BOOLEAN);
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * Gets float value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return float value
	 * 
	 */

	public float getFloat(final String key) {
		return mSharedPreferences.getFloat(key, DEFAULT_FLOAT);
	}

	/**
	 * Gets int value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return int value
	 * 
	 */

	public int getInt(final String key) {
		return mSharedPreferences.getInt(key, DEFAULT_INT);
	}

	/**
	 * Gets int value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @param defaultvalue
	 *            if return value is null, then this default value will be
	 *            returned
	 * @return int value
	 */
	public int getInt(final String key, final int defaultvalue) {
		return mSharedPreferences.getInt(key, defaultvalue);
	}

	/**
	 * Gets long value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return long value
	 * 
	 */
	public long getLong(final String key) {
		return mSharedPreferences.getLong(key, DEFAULT_LONG);
	}

	/**
	 * Gets String value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return string value
	 * 
	 */

	public String getString(final String key) {
		return mSharedPreferences.getString(key, DEFAULT_STRING);
	}

	/**
	 * Gets string value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @param defaultvalue
	 *            if return value is null, then this default value will be
	 *            returned
	 * @return string value
	 */

	public String getString(final String key, final String defaultvalue) {
		return mSharedPreferences.getString(key, defaultvalue);
	}

	/**
	 * Gets string value from SharedPreference
	 * 
	 * @param key
	 *            to get the value from the preference file
	 * @return string value
	 * 
	 */

	public String getKeyString(final String key) {
		return mSharedPreferences.getString(key, DEFAULT_KEY);
	}

	// Setters methods for SharedPreference.
	/**
	 * Sets boolean value into shared preference
	 * 
	 * @param key
	 *            to set the value into preference file
	 * @param value
	 *            value to insert
	 * 
	 */

	public void addOrUpdateBoolean(final String key, final boolean value) {
	//	synchronized (this) {
			mSharedPreferencesEditor.putBoolean(key, value);
			mSharedPreferencesEditor.commit();
		//}
	}

	/**
	 * Sets float value into shared preference
	 * 
	 * @param key
	 *            to set the value into preference file
	 * @param value
	 *            value to insert
	 * 
	 */

	public void addOrUpdateFloat(final String key, final float value) {
		synchronized (this) {
			mSharedPreferencesEditor.putFloat(key, value);
			mSharedPreferencesEditor.commit();
		}
	}

	/**
	 * Sets integer value into shared preference
	 * 
	 * @param key
	 *            to set the value into preference file
	 * @param value
	 *            value to insert
	 * 
	 */

	public void addOrUpdateInt(final String key, final int value) {
		synchronized (this) {
			mSharedPreferencesEditor.putInt(key, value);
			mSharedPreferencesEditor.commit();
		}
	}

	/**
	 * Sets long value into shared preference
	 * 
	 * @param key
	 *            to set the value into preference file
	 * @param value
	 *            value to insert
	 * 
	 */

	public void addOrUpdateLong(final String key, final long value) {
		synchronized (this) {
			mSharedPreferencesEditor.putLong(key, value);
			mSharedPreferencesEditor.commit();
		}
	}

	/**
	 * Sets string value into shared preference
	 * 
	 * @param key
	 *            to set the value into preference file
	 * @param value
	 *            value to insert
	 * 
	 */

	public void addOrUpdateString(final String key, final String value) {
		synchronized (this) {
			mSharedPreferencesEditor.putString(key, value);
			mSharedPreferencesEditor.commit();
		}
	}

	/**
	 * Deletes the preference values into shared preference
	 * 
	 * @param key
	 *            to delete value
	 * 
	 */

	public void delete(final String key) {
		synchronized (this) {
			mSharedPreferencesEditor.remove(key);
			mSharedPreferencesEditor.commit();
		}
	}
}
