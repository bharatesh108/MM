package com.dfc.moneymartus.infra;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;

public final class ErrorHandling {

	/**
	 * ErrorHandling Constructor
	 */
	private ErrorHandling()
	{

	}

	/*
	 * get server status code
	 * @param ValleyError
	 */
	public static int getStatusCode(VolleyError error)
	{
		int errorStatusCode = 0;
		if (error instanceof ServerError) {
			if (error.networkResponse != null && error.networkResponse.data != null)
			{
				errorStatusCode = error.networkResponse.statusCode;
			}
		} else if (error instanceof AuthFailureError) {
			if (error.networkResponse != null && error.networkResponse.data != null)
			{
				errorStatusCode = error.networkResponse.statusCode;
			}
		} else if (error instanceof NoConnectionError) {
		/*	Error indicating that no connection could be established when performing a Volley request.*/
			if (error.getMessage().contains("No authentication challenges found")) {
				errorStatusCode = 401;
			} else {
				errorStatusCode = 10000;
			}
			
		} else if (error instanceof NetworkError) {
			/*Indicates that there was a network error when performing a Volley request*/
			if (error.networkResponse != null && error.networkResponse.data != null)
			{
				errorStatusCode = error.networkResponse.statusCode;
			}
		} else if (error instanceof ParseError) {
			if (error.networkResponse != null && error.networkResponse.data != null)
			{
				errorStatusCode = error.networkResponse.statusCode;
			}
		}  else if (error instanceof TimeoutError){
			errorStatusCode = 408;
		} 
		return errorStatusCode;
	}	

	/*
	 * get error message for respective error code
	 * @param context
	 * @param statusCode
	 * @param eventtype
	 */
	public static String getErrorMessage(Context context, int statusCode, int eventType) {
		String message;
		if(statusCode == 401){
			if (eventType == EventTypes.EVENT_LOGIN){
				message = context.getResources().getString(R.string.error_Login_api);
			} else if(eventType == EventTypes.EVENT_FORGOT_PASSWORD) {
				message = context.getResources().getString(R.string.error_server_forgot_password);
			}else{	
				message = context.getResources().getString(R.string.error_401);
			}

		} else if (statusCode == 403) {
			if (eventType == EventTypes.EVENT_NEW_PASSWORD){
				message = context.getResources().getString(R.string.error_403_put_resetpassword);
			} else {
				message = context.getResources().getString(R.string.error_generic);
			}
			/*Network error*/
		} else if (statusCode == 10000) {

			message = context.getResources().getString(R.string.error_network);

		} else if (statusCode == 400){
			message = context.getResources().getString(R.string.error_400);

		} else if (statusCode == 408){
			message = context.getResources().getString(R.string.error_408_request_timeout);

		} else {

			if (eventType == EventTypes.EVENT_LOGIN)
			{
				message = context.getResources().getString(R.string.error_Login_api);
			} else if(eventType == EventTypes.EVENT_FORGOT_PASSWORD) 
			{
				message = context.getResources().getString(R.string.error_server_forgot_password);
			} else if (eventType ==EventTypes.EVENT_NEW_PASSWORD) 
			{
				message = context.getResources().getString(R.string.error_reset_password_server);
			} else 
			{
				message = context.getResources().getString(R.string.error_generic);
			}
		}
		return message;
	}
}
