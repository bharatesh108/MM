package com.dfc.moneymartus.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.view.fragments.MyChecksFragment.CreateSubmitCheckReceiver;

public class CreateSubmitRequestService  extends IntentService{

	public static final String JSON_BODY = "";
	public static final String FRONT_CHECK_BASE64 = "FRONT_CHECK_BASE64";
	public static final String BACK_CHECK_BASE64 = "BACK_CHECK_BASE64";
	public static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
	public static final String UPLOAD_QUEUE_ID = "UPLOAD_QUEUE_ID";
	private String mUploadQueueId;
	private String mCreateCheckResponse;
	private Preferences mPreferences;
	private int mStatusCode = 500;
	LinkedHashMap.Entry<String, MyChecksDataObject> mEntry= null;

	/*
	 * Constructor
	 */
	public CreateSubmitRequestService() {
		super("CreateSubmitRequestService");
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle b = intent.getExtras();

		mPreferences = Preferences.getInstance(this);
		String requestURL = Constants.BASE_URL + Constants.URL_GET_CHECKS;

		String jsonPayload = b.getString(JSON_BODY);
		mUploadQueueId = b.getString(UPLOAD_QUEUE_ID);

		String frontCheckBase64 = Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(mUploadQueueId)).getmFrontCheckImage();
		String backCheckBase64 = Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(mUploadQueueId)).getmBackCheckImage();

		mPreferences.addOrUpdateBoolean(PreferenceConstants.IS_UPLOAD_SERVICE_RUNNING, true);
		try {
			mStatusCode =postFile(requestURL, frontCheckBase64, backCheckBase64, jsonPayload);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadListData(String.valueOf(mStatusCode), Integer.parseInt(mUploadQueueId));

	}

	/**
	 * Used to load list data having status uploading
	 * @param statusCode Status code of check
	 * @param uploadedItemQueueId Queue ID
	 */
	private void loadListData(String statusCode,int uploadedItemQueueId) {
		int uploadItemPositionInList = findUploadItemInList();
		Moneymart.getInstance().myCheckUploadQueue.remove(String.valueOf(uploadedItemQueueId));

		//if(!statusCode.equals("201") && !statusCode.equals("200")){
		if (!"201".equals(statusCode) && !"200".equals(statusCode)) {
			if (!Moneymart.getInstance().myChecksList.isEmpty()) {
				Moneymart.getInstance().myChecksList.get(uploadItemPositionInList).setProgressUpdateRequired(false);
				Moneymart.getInstance().myChecksList.get(uploadItemPositionInList).setStatusCode(String.valueOf(Constants.CheckStatus.UPLOADING_FAILED));
			}
		} else if (!Moneymart.getInstance().myChecksList.isEmpty()) {

			Moneymart.getInstance().myChecksList.get(uploadItemPositionInList).setProgressUpdateRequired(false);
			try {
				JSONObject values = new JSONObject(mCreateCheckResponse);
				Moneymart.getInstance().myChecksList.get(uploadItemPositionInList).setChequeId(values.optString("chequeId"));
				Moneymart.getInstance().myChecksList.get(uploadItemPositionInList).setStatusCode(String.valueOf(Constants.CheckStatus.WAITING_FOR_REVIEW));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		Moneymart.getInstance().failedChecksList = new TreeMap<String, ChecksObject>();
		populateTheFailedChecksList();
		notifyReceiver();

		if(Moneymart.getInstance().myCheckUploadQueue.size() > 0){
			mEntry= Moneymart.getInstance().myCheckUploadQueue.entrySet().iterator().next();

			String key= mEntry.getKey();
			addNextCheckToUpload(Integer.parseInt(key), findNextItemForUploadInList(key));
		}
	}

	/**
	 * Used to upload check from the queue
	 * @param queueId queue id of check
	 * @param position Position of check
	 */
	public void addNextCheckToUpload(int queueId, int position){
		Moneymart.getInstance().myChecksList.get(position).setStatusDescription("Uploading");
		Moneymart.getInstance().myChecksList.get(position).setStatusCode(String.valueOf(Constants.CheckStatus.UPLOADING));
		Moneymart.getInstance().myChecksList.get(position).setProgressUpdateRequired(true);
		submitCapturedCheckImage(queueId);
	}

	/**
	 * Used to get the checks having status is uplaoding 
	 * @return position of uploading check
	 */
	public int findUploadItemInList(){
		int uploadCheckPosition = 0;
		Iterator<ChecksObject> checkIterator  = Moneymart.getInstance().myChecksList.iterator();
		while(checkIterator.hasNext()){
			ChecksObject checkObj = checkIterator.next();
			if(Integer.parseInt(checkObj.getStatusCode()) == Constants.CheckStatus.UPLOADING){
				break;
			}
			uploadCheckPosition++;
		}
		return uploadCheckPosition;
	}

	/**
	 * Used to change check status to uploading failed if upload fails
	 */
	public void populateTheFailedChecksList(){
		Iterator<ChecksObject> checkIterator  = Moneymart.getInstance().myChecksList.iterator();
		while(checkIterator.hasNext()){
			ChecksObject checkObj = checkIterator.next();
			if(Integer.parseInt(checkObj.getStatusCode()) == Constants.CheckStatus.UPLOADING_FAILED){
				Moneymart.getInstance().failedChecksList.put(String.valueOf(checkObj.getQueuedActionId()), checkObj);
			}
		}
	}

	/**
	 * Used to fetch check from queue and upload it to server
	 * @param queueId Queue id
	 * @return check position
	 */
	public int findNextItemForUploadInList(String queueId){
		int positionInList = 0;
		Iterator<ChecksObject> checkIterator = Moneymart.getInstance().myChecksList.iterator();
		while(checkIterator.hasNext()){
			ChecksObject checkObj = checkIterator.next();
			if(checkObj.getQueuedActionId() == Integer.parseInt(queueId))
				break;
			positionInList++;
		}
		return positionInList;
	}

	/**
	 * Used to invoke broadcast for uploading checks 
	 */
	private void notifyReceiver() {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(CreateSubmitCheckReceiver.PROCESS_RESPONSE);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(RESPONSE_MESSAGE, String.valueOf(mStatusCode));
		broadcastIntent.putExtra(UPLOAD_QUEUE_ID, Integer.parseInt(mUploadQueueId));
		sendBroadcast(broadcastIntent);
	}

	/**
	 * Used to upload check, it will form request and send request to server
	 * @param postUrl API url
	 * @param mFrontCheck Front check image in base64 string format
	 * @param mBackCheck Back check image in base64 string format
	 * @param jsonPayload request payload data
	 * @return response status code
	 * @throws Exception
	 */
	public int postFile(String postUrl, String mFrontCheck, String mBackCheck, String jsonPayload) throws Exception{

		DefaultHttpClient mHttpClient = null;
		String boundary = "DFC-WEBAPI-MULTIPART-BOUNDARY";

		HttpResponse response = null;
		HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.Timeout);
        HttpConnectionParams.setSoTimeout(httpParameters, Constants.Timeout);
		mHttpClient = new DefaultHttpClient(httpParameters);
		String jsonStr = jsonPayload; 

		mPreferences.addOrUpdateString(PreferenceConstants.UPLOAD_QUEUE_ID, mUploadQueueId);
		
		String str = "--"+boundary;
		String str2 = "Content-Disposition: form-date; name=\"cheque\"";
		String str3 = "Content-Type: application/json; charset=utf-8";
		String str4 = "Content-Disposition: form-date; name=\"cheque\" rel=\"imageChequeFront\"";
		String str5 = "Content-Type: image/jpeg";
		String str6 = "Content-Disposition: form-date; name=\"cheque\" rel=\"imageChequeBack\"";
		String str7 = "Content-Type: image/jpeg";
		String str8 = "--" + boundary + "--";

		String StrTotal = str + "\r\n" + str2 + "\r\n" + str3 +"\r\n" + jsonStr + "\r\n" + str + "\r\n"
				+ str4 + "\r\n" + str5 + "\r\n" + "Content-Transfer-Encoding: base64" + "\r\n"+ mFrontCheck + str +"\r\n" + str6 + "\r\n" + str7 + "\r\n" + "Content-Transfer-Encoding: base64" +"\r\n" + mBackCheck + str8;


		HttpPost post = new HttpPost(postUrl);


		post.addHeader("Authorization", mPreferences.getString(PreferenceConstants.AUTORIZATION_TOKEN));
		post.addHeader(Constants.LOGIN_REQUEST_HEADER_API_KEY, Constants.API_KEY);
		post.addHeader("Accept", Constants.ACCEPT_APP_JSON);
		post.addHeader("Content-Type","multipart/form-data");
		post.addHeader("Boundary", boundary);

		StringEntity se = new StringEntity(StrTotal);
		se.setContentEncoding("UTF-8");
		post.setEntity(se);
		response = mHttpClient.execute(post);

		/* Checking response */
		int statusCode = response.getStatusLine().getStatusCode();

		/*if(statusCode != 200 || statusCode!=201){
			// Update status of item in list data
		}*/


		HttpEntity entity = response.getEntity();

		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = 
					new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		catch (IOException e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }


		mCreateCheckResponse = sb.toString();

		return statusCode;
	}

	public void submitCapturedCheckImage(int position) {
		try{
			Intent msgIntent = new Intent(this, CreateSubmitRequestService.class);
			Bundle b = new Bundle();
			b.putString(CreateSubmitRequestService.JSON_BODY, prepareJson(position).toString());
			b.putString(CreateSubmitRequestService.FRONT_CHECK_BASE64, "");
			b.putString(CreateSubmitRequestService.BACK_CHECK_BASE64, "");
			b.putString(CreateSubmitRequestService.UPLOAD_QUEUE_ID, String.valueOf(position));

			msgIntent.putExtras(b);
			startService(msgIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject prepareJson(int position){
		JSONObject jo = new JSONObject();
		try {
			jo.put("chequeId", null);
			jo.put("chequeMaker", null);
			jo.put("chequeNumber", null);
			jo.put("amount", Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmCheckAmount());
			jo.put("amountCurrency", "USD");
			jo.put("dateMade", Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmCheckMadeDate());
			jo.put("fees", null);
			jo.put("feesCurrency", null);
			jo.put("fundingMechanism",  Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmFundingMechanismsId());
			jo.put("feesAgreed", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

}
