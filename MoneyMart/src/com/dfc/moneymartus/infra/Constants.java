package com.dfc.moneymartus.infra;

import com.dfc.moneymartus.business.CustDetailDataObject;
import com.dfc.moneymartus.dto.CCServicesDataObject;
import com.dfc.moneymartus.dto.CheckDataObject;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.dto.CustomerServicesDataObject;


public class Constants {

	//App constants
	/** Request Headers **/
	public static final String LOGIN_REQUEST_HEADER_USERNAME = "DFC-Username";
	public static final String LOGIN_REQUEST_HEADER_PASSWORD = "DFC-Password";
	public static final String LOGIN_REQUEST_HEADER_API_KEY = "DFC-ApiKey";
	public static final String LOGIN_REQUEST_HEADER_ACCEPT = "Accept";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String EMAIL_SUBJECT = "MoneyMart mobile app inquiry";
	
	//The array has to be declared in one line required for project creation of moneymart ca
	private static final String[] ENABLED_STATES = {"AK","CA","HI","KS","LA","NM","PA","WA","AZ","FL","MO","OK","VA"};

	public static final String API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJHdWlkIjoiZGEwM2RkNzQtMDFjNS00NDI3LTk2YmEtNWI2MjI1ZDAwMDU3In0.Onj6UlRGkZy_NdMCdbACuA6V5ZOe7CCmX7tr3Q7P3Bs";
	public static final String ACCEPT_HAL_JSON_UTF = "application/hal+json; charset=utf-8";
	public static final String ACCEPT_APP_JSON = "application/json; charset=utf-8";
	public static final String ACCEPT_JSON = "application/json";
	//Required for Script -- Don't Delete
	//Accept-Language;
	
	//TEST base url
	//public static final String BASE_URL= "https://preview1.moneymart.ca/v2";
	
	//UAT base url
    public static  final String BASE_URL= "https://ustest.api.dfconline.com/v2";
	
	//Production base url
//	public static  final String BASE_URL= "https://us.api.dfconline.com/v2";
	
	public static final String URL_GET_SECURITY_QUESTION = "/core/passwordchallenge";
	//public static String URL_FORGOT_PASSWORD = BASE_URL + "/core/user/password";
	public static final String URL_RESET_PASSWORD = "/core/resetpassword";
	public static final String URL_GET_CUSTOMER = "/core/user";
	public static final String URL_GET_FUNDING_MECHANISM = BASE_URL + "/core/fundingmechanisms";
	public static final String URL_GET_REGISTRATIONS = BASE_URL + "/core/user/services"; 
	//public static String URL_GET_CHECKS_BY_STATUS = BASE_URL + "/cc/cheques/waitingforreview";
	
	//public static String CHECK_DOWNLOAD_BASE_URL = "https://preview1.moneymart.ca";
	//public static String FRONT_CHECK_DOWNLOAD_IMG = "/front";
//	public static String FRONT_CHECK_DOWNLOAD = BASE_URL + "/cc/cheques/{id}/front";
//	public static String BACK_CHECK_DOWNLOAD = BASE_URL + "/cc/cheques/{id}/back";
//	public static String VOIDED_CHECK_DOWNLOAD = BASE_URL + "/cc/cheques/{id}/voided";
	
	public static String authToken = "";
	public static boolean fetchListFromServer = false;
	//public static String BASE_URL= "https://preview1.moneymart.ca";
	public static final String CORE_TERMS_URL = "/core/terms/moneymartus";
	public static final String CC_TERMS_URL = "/cc/terms/moneymartus";
	public static final String UPDATE_PASSWORD = "/core/user/password";
	public static final String URL_CORE_SERVICE = "/core/moneymartus";
	public static final String URL_CC_SERVICE = "/cc/moneymartus";
	public static final String URL_CHECK_BY_ID = "/cc/cheques";
	
	
	public final static String MMCALENDAR_AM = "a.m.";
	public final static String MMCALENDAR_PM = "p.m.";
	public static final String URL_GET_CHECKS = "/cc/cheques";
	public static final String URL_FAQ_CORE= BASE_URL+"/core/faqs/moneymartus";
	public static final String URL_FAQ_CC= BASE_URL+ "/cc/faqs/moneymartus"; 
	public static final String URL_FAQ_MOMENTUM= BASE_URL + "/core/fundingmechanisms/faqs/moneymartus";
	public static final String URL_GET_SECURITY_QUESTIONS= BASE_URL + "/core/challenges";
	
	public static final String URL_FIND_A_STORE ="https://www.moneymart.com/StoreDetails/FindaStore";
	public static final String URL_LOAN_FORM ="https://www.moneymart.com/PaydayLoans/application-form";
	public static final String URL_GET_CHECK_LIST= BASE_URL + "/cc/cheques"; 
	public static final String URL_USER_LOGIN= BASE_URL + "/core/authtoken";
	public static final String URL_CARD_HOLDER_AGREEMENT ="http://www.mytitanium.ca/cardholder-agreement.aspx";
	public static final String URL_PPT_PRIVACY_POLICY ="http://www.mytitanium.ca/privacy-policy.aspx";
	
	
	public static final String URL_LOAN_TEST ="https://preview.moneymart.com/";
	public static final String URL_LOAN_UAT ="https://preview.moneymart.com/";
	public static final String URL_LOAN_PROD ="https://www.moneymart.com/PreApp/ApplicationForm";
	
	
	// MiSnap
	public static final String MISNAP_DIR = "MiSnap";
	public static final String MISNAP_FRONT_CHECK = "checkImage";
	public static final int MISNAP_FRONT_CHECK_RESULT_CODE = 101;
	public static final int MISNAP_BACK_CHECK_RESULT_CODE = 102;
	public static final int MISNAP_VOID_CHECK_FRONT_RESULT_CODE = 103;


	public static final int DATE_DIALOG_ID = 104;

	//Drawer Menu
	public static final int MENU_CHECK_CASHING_FRAGMENT = 0;
	public static final int MENU_MY_CHECKS_FRAGMENT = 1;
	public static final int MENU_CARD_DETAILS_FRAGMENT = 2;
	public static final int MENU_PERSONAL_DETAILS_FRAGMENT = 3;
	public static final int MENU_ACCOUNT_DETAILS_FRAGMENT = 4;
	public static final int MENU_CONTACT_US_FRAGMENT = 5;
	public static final int MENU_STORE_LOCATOR_FRAGMENT = 6;
	public static final int MENU_FAQ_FRAGMENT = 7;
	public static final int MENU_LOAN_FRAGMENT = 8;
	public static final int MENU_LEARN_MORE_FRAGMENTS = 9;

	//JSON Constants
	public static final String SECRET = "##secret##";
	public static final String NEW_PASSWORD = "##newpassword##";
	public static final String URL_CONTENT_TYPE= "application/json; charset=utf-8";
	public static CustDetailDataObject userData = null;
	public static boolean isPrimary;
	public static CustomerServicesDataObject mCustServiceObj = null;
	public static CCServicesDataObject CCServiceObj = null;
	public static final String isFirstLaunch = "IS-FIRST_LAUNCH";
	public static CheckDataObject myCheckServerData = null;

	//Check Detail
	public static final String CHECK_ID = "CHECK_ID";
	public static final String CHECK_NUMBER = "CHECK_NUMBER";
	public static final String CHECK_MAKER_NAME = "CHECK_MAKER_NAME";
	public static final String CHECK_MADE_DATE= "CHECK_MADE_DATE";
	public static final String CHECK_FRONT_IMAGE = "CHECK_FRONT_IMAGE";
	public static final String CHECK_BACK_IMAGE = "CHECK_BACK_IMAGE";
	public static final String CHECK_FEES= "CHECK_FEES";
	public static final String CHECK_FEES_AGREED= "CHECK_FEES_AGREED";
	public static final String CHECK_AMOUNT = "CHECK_AMOUNT";
	public static final String CHECK_FUNDINGTO = "CHECK_FUNDINGTO";
	public static final String CHECK_FUNDINGTO_TYPE = "CHECK_FUNDINGTO_TYPE";
	public static final String CHECK_DATE_SUBMIT = "CHECK_DATE_SUBMIT";
	public static final String CHECK_DATE_COMPLETE = "CHECK_DATE_COMPLETE";
	public static final String CHECK_AMOUNT_CURRENCY = "CHECK_AMOUNT_CURRENCY";
	public static final String CHECK_FEES_CURRENCY = "CHECK_FEES_CURRENCY";
	public static final String CHECK_FRONT_IMAGE_URL = "CHECK_FRONT_IMAGE_URL";
	public static final String CHECK_BACK_IMAGE_URL = "CHECK_BACK_IMAGE_URL";
	public static final String CHECK_VOID_IMAGE_URL = "CHECK_VOID_IMAGE_URL";
	
	//Update user name request params
		public static final String CUST_USERNAME = "##username##";
		public static final String FIRSTNAME = "##firstName##";
		public static final String SECONDNAME ="##secondName##";
		public static final String DATEOFBIRTH ="##dateOfBirth##";
		public static final String SSN = "##ssn##";
		public static final String HOUSENUMBER="##houseNumber##";
		public static final String STREET ="##street##";
		public static final String CITY ="##city##";
		public static final String COUNTY ="##county##";
		public static final String STATE ="##state##";
		public static final String POSTCODEZIP ="##postcodeZip##";
		public static final String PHONE ="##phone##";
		public static final String PHONE2 ="##phone2##";
		public static final String MOBILE ="##mobile##";
		public static final String EMAIL ="##email##";
		public static final String COUNTRY = "##country##";
		
		 
		public static final int CHECK_ACTION_ID_ONE = 1;
		public static final int CHECK_ACTION_ID_TWO = 2;
		public static final int CHECK_ACTION_ID_THREE = 3;
		public static final int CHECK_ACTION_ID_FOUR = 4;
		public static final int CHECK_ACTION_ID_FIVE = 5;
		public static final int Timeout = 240000;
		
		public static final String mActionRequiredStatus = "ACTION_REQUIRED_STATUS";
		
		public static ChecksObject UPLOADING_STATE_CHECKBYID_OBJECT;
		public static boolean isCheckCashingFragFromDrawer = false;
		public static boolean isOpenDrawer = false;
		public static final int BANK_ACCOUNT_ACTIVITY = 800;
        public static final int BANK_ACCOUNT_NUMBER_MIN_LENGTH = 9;
        public static final int BANK_ROUTING_NUMBER_MIN_LENGTH = 5;
		public static final String VISITSITE_LINK = "https://www.moneymart.com";

	public static class PreferenceConstants {
		public final static String PREFERENCE_FILE = "MoneyMart";
		public static final String AUTORIZATION_TOKEN = "AUTORIZATION_TOKEN";
		public static final String UPLOAD_QUEUE_ID = "UPLOAD_QUEUE_ID";
		public static final String IS_UPLOAD_SERVICE_RUNNING = "IS_UPLOAD_SERVICE_RUNNING";
		public static final String IS_SERVICE_IN_BACKGROUND = "IS_SERVICE_IN_BACKGROUND";
		public static final String IS_APP_IDLE_PERIOD_LAPSED = "IS_APP_IDLE_PERIOD_LAPSED";
		
		public static final String PERSONAL_DETAILS_FIRST_NAME = "PERSONAL_DETAILS_FIRST_NAME";
		public static final String PERSONAL_DETAILS_LAST_NAME = "PERSONAL_DETAILS_LAST_NAME";
		public static final String PERSONAL_DETAILS_EMAIL_ADDRESS = "PERSONAL_DETAILS_EMAIL_ADDRESS";
		public static final String PERSONAL_DETAILS_DOB = "PERSONAL_DETAILS_DOB";
		public static final String PERSONAL_DETAILS_POST_DOB = "PERSONAL_DETAILS_POST_DOB";
		public static final String PERSONAL_DETAILS_SSN = "PERSONAL_DETAILS_SSN";
		public static final String PERSONAL_DETAILS_HOUSE_NUMBER = "PERSONAL_DETAILS_HOUSE_NUMBER";
		public static final String PERSONAL_DETAILS_STREET = "PERSONAL_DETAILS_STREET";
		public static final String PERSONAL_DETAILS_CITY = "PERSONAL_DETAILS_CITY";
		public static final String PERSONAL_DETAILS_STATE = "PERSONAL_DETAILS_STATE";
		public static final String PERSONAL_DETAILS_ZIP_CODE = "PERSONAL_DETAILS_ZIP_CODE";
		public static final String PERSONAL_DETAILS_HOME_PHONE = "PERSONAL_DETAILS_HOME_PHONE";
		public static final String PERSONAL_DETAILS_CELL_PHONE = "PERSONAL_DETAILS_CELL_PHONE";
		
		public static final String APP_BACKGROUND_TIME = "APP_BACKGROUND_TIME";
		public static final String SHOW_VOID_TUTORIAL = "SHOW_VOID_TUTORIAL";
		public static final String SHOW_MASTER_CARD_AGREEMENT = "SHOW_MASTER_CARD_AGREEMENT";
		
	}

	public class CheckStatus {
		//01 to 04 : Status from server
		public static final int WAITING_FOR_REVIEW = 01;
		public static final int USER_ACTION_REQUIRED = 02;
		public static final int CASHED = 03;
		public static final int REJECTED = 04;
		//Below need to handled by app
		public static final int QUEUED = 05;
		public static final int UPLOADING = 06;
		public static final int UPLOADING_FAILED = 07;
	}
	
	
	public class MMActionRequired {
		public static final int RESUBMIT_CHEQUE_IMAGE_FRONT = 11;
		public static final int RESUBMIT_CHEQUE_IMAGE_BACK = 12;
		public static final int SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT = 13;
		public static final int RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT = 14;
		public static final int CONTACT_CUSTOMER_SERVICES = 15;
		public static final int AGREE_TO_TERMS = 16;
		public static final int AGREE_TO_FEES = 17;
		public static final int RESUBMIT_CHEQUE_IMAGE_FRONT_BACK = 18;
	}
	
	public class DialogId {
		public static final String FEE_DECLINE = "FEE_DECLINE";
		public static final String ADD_BANK_ACCOUNT_ERROR = "ADD_BANK_ACCOUNT_ERROR";
		public static final String TERAMS_AND_CONDITIONS = "TERAMS_AND_CONDITIONS";
		public static final String  NO_NETWORK = "NO_NETWORK";
	}
	
	private Constants(){
		// private constructor to make this as utility class
	}
	
	public static String[] getEnabledStates() {
		return ENABLED_STATES;
	}

	public static String getAuthTOken() {
		return authToken;
	}

	public static void setAuthTOken(String authTOken) {
		authToken = authTOken;
	}

	public static boolean isFetchListFromServer() {
		return fetchListFromServer;
	}

	public static void setFetchListFromServer(boolean fetchListFromServer) {
		Constants.fetchListFromServer = fetchListFromServer;
	}

	public static CustDetailDataObject getUserData() {
		return userData;
	}

	public static void setUserData(CustDetailDataObject userData) {
		Constants.userData = userData;
	}

	public static boolean isPrimary() {
		return isPrimary;
	}

	public static void setPrimary(boolean isPrimary) {
		Constants.isPrimary = isPrimary;
	}

	public static CustomerServicesDataObject getCustServiceObj() {
		return mCustServiceObj;
	}

	public static void setCustServiceObj(CustomerServicesDataObject custServiceObj) {
		mCustServiceObj = custServiceObj;
	}

	public static CCServicesDataObject getCCServiceObj() {
		return CCServiceObj;
	}

	public static void setCCServiceObj(CCServicesDataObject cCServiceObj) {
		CCServiceObj = cCServiceObj;
	}

	public static CheckDataObject getMyCheckServerData() {
		return myCheckServerData;
	}

	public static void setMyCheckServerData(CheckDataObject myCheckServerData) {
		Constants.myCheckServerData = myCheckServerData;
	}

	public static ChecksObject getUPLOADING_STATE_CHECKBYID_OBJECT() {
		return UPLOADING_STATE_CHECKBYID_OBJECT;
	}

	public static void setUPLOADING_STATE_CHECKBYID_OBJECT(
			ChecksObject uPLOADING_STATE_CHECKBYID_OBJECT) {
		UPLOADING_STATE_CHECKBYID_OBJECT = uPLOADING_STATE_CHECKBYID_OBJECT;
	}

	public static boolean isCheckCashingFragFromDrawer() {
		return isCheckCashingFragFromDrawer;
	}

	public static void setCheckCashingFragFromDrawer(
			boolean isCheckCashingFragFromDrawer) {
		Constants.isCheckCashingFragFromDrawer = isCheckCashingFragFromDrawer;
	}

	public static boolean isOpenDrawer() {
		return isOpenDrawer;
	}

	public static void setOpenDrawer(boolean isOpenDrawer) {
		Constants.isOpenDrawer = isOpenDrawer;
	}

}
