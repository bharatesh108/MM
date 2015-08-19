package com.dfc.moneymartus.gateway;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.business.ChecksHandler;
import com.dfc.moneymartus.business.DataObject;


public class CommuncationHelper implements ICommunicationHelper {

	public static final String TAG = "MoneyMart";

	/**
	 * Used to send API to get retrieve check data
	 * @param event Request header
	 * @param callback Call back object
	 * @param handler Object of class where data will be sent
	 */
	public void executeGetChecks(final RequestInput event,
			final ICommunicator callback,final ChecksHandler handler) {
		final StringRequest strReq = new StringRequest(Method.GET, event.url,
				new Response.Listener<String>() {

			@Override
			public void onResponse(final String response) {
				if (callback != null) {
				    JSONObject jsonObj;
					try {
						if(response.charAt(0)=='{'){
						jsonObj = new JSONObject(response);
						handler.setJSONValues(jsonObj);
						callback.onResponseSuccess(jsonObj, handler);
					}else{
						callback.onResponseSuccess(new JSONObject(), null);
					}
					} catch (JSONException e) {
						Log.e(TAG,"on response"+e.getMessage());
					}
					
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				if (callback != null) {
					callback.onResponseFailure(error);
				}
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}
			
			@Override
			public String getBodyContentType() {
				return "application/json";
			}


		};

		// Adding request to request queue
		Moneymart.getInstance().addToRequestQueue(strReq);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicationHelper#executeGetWithoutHeader(com.dfc.moneymartus.gateway.RequestInput, com.dfc.moneymartus.gateway.ICommunicator)
	 */
	@Override
	public void executeGetWithoutHeader(final RequestInput event,
			final ICommunicator callback) {
		final StringRequest strReq = new StringRequest(Method.GET, event.url,
				new Response.Listener<String>() {

			@Override
			public void onResponse(final String response) {
				if (callback != null) {
					callback.onResponseSuccess(response);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				if (callback != null) {
					callback.onResponseFailure(error);
				}
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}
			
			@Override
			public String getBodyContentType() {
				return "application/json";
			}

		};

		// Adding request to request queue
		Moneymart.getInstance().addToRequestQueue(strReq);
	}


	/*
	 * (non-Javadoc)
	 * @see com.dfcmoneymart.gateway.ICommunicationHelper#executeGetLogin(com.dfcmoneymart.business.DataObject, com.dfcmoneymart.gateway.ICommunicator)
	 */
	@Override
	public void executeGetLogin(final DataObject eventObj, final ICommunicator callback)
	{
		final RequestInput event = eventObj.setRequestParms();

		final StringRequest request = new StringRequest(Method.GET, event.url,  
				new ResponseListener(eventObj, callback), new ErrorListener(eventObj, callback))
		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}

			@Override
			protected Response<String> parseNetworkResponse(
					final NetworkResponse networkResponse) {
				String sessionId = networkResponse.headers.get("DFC-AuthToken");
				com.android.volley.Response<String> result = com.android.volley.Response
						.success(sessionId, HttpHeaderParser
								.parseCacheHeaders(networkResponse));
				eventObj.setJSONValues(sessionId);
				return result;
			}
		};
		Moneymart.getInstance().addToRequestQueue(request);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfcmoneymart.gateway.ICommunicationHelper#executeGet(com.dfcmoneymart.business.DataObject, com.dfcmoneymart.gateway.ICommunicator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void executeGet(final DataObject eventObj,final ICommunicator callback) {
		final RequestInput event = eventObj.setRequestParms();
		final JsonObjectRequest request = new JsonObjectRequest(Method.GET, event.url,null,  
				new JsonResponseListener(eventObj, callback), new JsonErrorListener(eventObj, callback))
		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}


			@Override
			public String getBodyContentType() {
				return "application/json";
			}

		};
		Moneymart.getInstance().addToRequestQueue(request);

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfcmoneymart.gateway.ICommunicationHelper#executeGet(com.dfcmoneymart.business.DataObject, com.dfcmoneymart.gateway.ICommunicator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void executePutRequest(final DataObject eventObj,final ICommunicator callback) {

	   final ServerStatusRequestObject req = new ServerStatusRequestObject(eventObj,callback, new JsonErrorListener(eventObj, callback));

		/*	JsonObjectRequest request = new JsonObjectRequest(Method.PUT, event.url,eventObj.setJsonRequestParms(),  
				new JsonResponseListener(eventObj, callback), new JsonErrorListener())
		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}
		};*/
		Moneymart.getInstance().addToRequestQueue(req);

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicationHelper#executePut(com.dfc.moneymartus.business.DataObject, com.dfc.moneymartus.gateway.ICommunicator)
	 */
	@Override
	public void executePut(final DataObject eventObj, final ICommunicator callback) {
		final RequestInput event = eventObj.setRequestParms();
		final StringRequest strReq = new StringRequest(Method.PUT, event.url,
				new Response.Listener<String>() {

			@Override
			public void onResponse(final String response) {
				callback.onResponseSuccess(response);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				callback.onResponseFailure(error);
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}

			/*	@Override
			public byte[] getBody() throws AuthFailureError {
				Log.d(TAG, "Resposne body ........"+event.body);
				return event.body.getBytes(Charset.forName("UTF-8"));
			}
			 */


			@Override
			public String getBodyContentType() {
				return "application/json";
			}

			@Override
			public byte[] getBody() {
				return event.body.getBytes();
				//return event.body.getBytes(Charset.forName("UTF-8"));
			}
		};

		// Adding request to request queue
		Moneymart.getInstance().addToRequestQueue(strReq);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicationHelper#executePostRequest(com.dfc.moneymartus.business.DataObject, com.dfc.moneymartus.gateway.ICommunicator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void executePostRequest(final DataObject eventObj,final ICommunicator callback) {

		final RequestInput event = eventObj.setRequestParms();
		//	ServerStatusRequestObject req = new ServerStatusRequestObject(eventObj,callback);

		final JsonObjectRequest request = new JsonObjectRequest(Method.POST, event.url,eventObj.setJsonRequestParms(),  
				new JsonResponseListener(eventObj, callback), new JsonErrorListener(eventObj, callback))
		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}

			@Override
			public String getBodyContentType() {
				return "application/json";
			}

			@Override
			public byte[] getBody() {
				return event.body.getBytes();
				//return event.body.getBytes(Charset.forName("UTF-8"));
			}

			@Override
			protected Response<JSONObject> parseNetworkResponse(
					final NetworkResponse networkResponse) {
				String sessionId = networkResponse.headers.get("DFC-AuthToken");
				Response<JSONObject> result = null;
				eventObj.setJSONValues(sessionId);
				return result;
			}
		};
		Moneymart.getInstance().addToRequestQueue(request);

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicationHelper#executeGetImage(com.dfc.moneymartus.business.DataObject, com.dfc.moneymartus.gateway.ICommunicator)
	 */
	@Override
	public void executeGetImage(final DataObject eventObj,final ICommunicator callback) {
		final RequestInput event = eventObj.setRequestParms();

		final StringRequest request = new StringRequest(Method.GET, event.url,
				new Response.Listener<String>() {

			@Override
			public void onResponse(final String response) {
				callback.onResponseSuccess(response);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				callback.onResponseFailure(error);
			}
		});
		Moneymart.getInstance().addToRequestQueue(request);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.ICommunicationHelper#executeStringPostRequest(com.dfc.moneymartus.business.DataObject, com.dfc.moneymartus.gateway.ICommunicator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void executeStringPostRequest(final DataObject eventObj,
			final ICommunicator callback) {
		final RequestInput event = eventObj.setRequestParms();
		//	ServerStatusRequestObject req = new ServerStatusRequestObject(eventObj,callback);
		final StringRequest request = new StringRequest(Method.POST, event.url, new JsonResponseListener(eventObj, callback), new JsonErrorListener(eventObj, callback))

		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return event.headers;
			}

			@Override
			public String getBodyContentType() {
				return "application/json";
			}

			@Override
			public byte[] getBody() {
				return event.body.getBytes();
				//return event.body.getBytes(Charset.forName("UTF-8"));
			}
		};
		Moneymart.getInstance().addToRequestQueue(request);

	}


}