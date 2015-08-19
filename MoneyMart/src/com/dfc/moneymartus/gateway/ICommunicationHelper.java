package com.dfc.moneymartus.gateway;

import com.dfc.moneymartus.business.DataObject;


public interface ICommunicationHelper {
	/**
	 * Used to  is not having execute request which is not having header and request method is GET. API response will be in string format. 
	 * @param event Request input parameters
	 * @param callback Event callback
	 */
	public void executeGetWithoutHeader(final RequestInput event, ICommunicator callback);
	
	/**
	 * Used to execute login request and request method is GET. API response will be in string format. 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	public void executeGetLogin(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute get image request and request method is GET 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	public void executeGetImage(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute request and request method is GET. API response will be in json object. 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	void executeGet(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute request and request method is PUT. API response will be in string format. 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	public void executePut(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute request and request method is PUT.API response return object. 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	void executePutRequest(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute request and request method is POST.API response return json object. 
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	void executePostRequest(DataObject eventObj, ICommunicator callback);
	
	/**
	 * Used to execute request and request method is POST.API response return string.  
	 * @param eventObj Object of data class
	 * @param callback Event callback
	 */
	void executeStringPostRequest(DataObject eventObj, ICommunicator callback);
	

}
