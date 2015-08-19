package com.dfc.moneymartus.gateway;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dfc.moneymartus.business.DataObject;

@SuppressWarnings("rawtypes")
public class ServerStatusRequestObject extends Request {

	private String mBody;
	private final HashMap mCustomHeaders;
	private final JsonResponseListener mJsonResponseListener;
	private final JsonErrorListener jsonErrorListener;

	/**
	 * Constructor
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 * @param jsonErrorListener API error listener
	 */
	public ServerStatusRequestObject(DataObject eventObj,
			ICommunicator callback, JsonErrorListener jsonErrorListener) {

		super(Method.PUT, eventObj.setRequestParms().url, jsonErrorListener);
		this.jsonErrorListener = jsonErrorListener;
		mCustomHeaders = eventObj.setRequestParms().headers;
		if (eventObj.setJsonRequestParms() == null) {
			mBody =  eventObj.setRequestParms().body;
		}
		else {
			mBody =  eventObj.setJsonRequestParms().toString();//body;
		}
		mJsonResponseListener = new JsonResponseListener(eventObj, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.NetworkResponse)
	 */
	@Override
	protected Response parseNetworkResponse(NetworkResponse response) {
		return Response.success(response.statusCode, HttpHeaderParser.parseCacheHeaders(response));
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#getHeaders()
	 */
	@Override
	public Map getHeaders() throws AuthFailureError {
		if (mCustomHeaders != null) {
			return mCustomHeaders;
		}
		return super.getHeaders();
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#getBody()
	 */
	@Override
	public byte[] getBody() throws AuthFailureError {
		return mBody.getBytes();
	}    

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#getBodyContentType()
	 */
	@Override
    public String getBodyContentType() {
        return "application/json";
    }

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#deliverResponse(java.lang.Object)
	 */
	@Override
	protected void deliverResponse(Object response) {
		//mJsonResponseListener.onResponse((Integer)response);
		mJsonResponseListener.onResponse(response);
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.volley.Request#deliverError(com.android.volley.VolleyError)
	 */
	@Override
	public void deliverError(VolleyError error) {

		this.jsonErrorListener.onErrorResponse(error);
	}
}
