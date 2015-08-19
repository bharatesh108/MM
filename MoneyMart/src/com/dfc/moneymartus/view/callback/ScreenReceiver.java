package com.dfc.moneymartus.view.callback;

import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.view.activity.BaseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
	private Preferences mPreferences;
	private Context mContext;
	
    public ScreenReceiver(Context context) {
    	mContext = context;
    	mPreferences = Preferences.getInstance(context);

	}

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
         //   wasScreenOn = false;
        	mPreferences.addOrUpdateLong(PreferenceConstants.APP_BACKGROUND_TIME, System.currentTimeMillis());
            Log.e("Action", ":" + "ACTION_SCREEN_OFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
           // wasScreenOn = true;
            Log.e("Action", ":" + "ACTION_SCREEN_ON");
            
            //((BaseActivity)mContext).checkTimeDiff();
        } /*else if (intent.getAction().equals(Intent.ACTION_USER_BACKGROUND)) {
            // and do whatever you need to do here
       //     wasScreenOn = true;
            Log.e("Action", ":" + "ACTION_USER_BACKGROUND");
        } else if (intent.getAction().equals(Intent.ACTION_USER_FOREGROUND)) {
            // and do whatever you need to do here
         //   wasScreenOn = true;
            Log.e("Action", ":" + "ACTION_USER_FOREGROUND");
        }
        */
    }

}
