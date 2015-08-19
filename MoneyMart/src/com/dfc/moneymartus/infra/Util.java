package com.dfc.moneymartus.infra;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.view.activity.LoginActivity;

@SuppressLint("SimpleDateFormat") 
public final class Util {

	static Map<String, String> sstates;

	private Util(){
		//private constructor to avoid instantiation of the class
	}

	/**
	 * Checks if network is available in the device or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(final Context context) {

		if (context == null) {
			return false;
		}
		final ConnectivityManager mConnManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnManager != null) {
			final NetworkInfo[] netInfo = mConnManager.getAllNetworkInfo();
			for (NetworkInfo mNetInfo : netInfo) {
				if ((mNetInfo.getTypeName().equalsIgnoreCase("WIFI") || mNetInfo
						.getTypeName().equalsIgnoreCase("MOBILE"))
						&& mNetInfo.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method used for hiding the soft keyboard.
	 */
	public static void hideSoftKeyboard(final Context context, final View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * This method is used to show the soft keyboard
	 */
	public static void showSoftKeyboard(final Context context) {

		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/*
	 * get us price format
	 * @param  pricevalue
	 */
	/*public static String getUSPriceFormat(final double priceValue) {
		final Locale locale = new Locale("en", "US");
		final NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
		return fmt.format(priceValue);
	}*/

	/*
	 * get three place details
	 * @param value
	 */
	/*public static BigDecimal getThreePlaceDecimals(double val) {
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bd;
	}*/

	/*
	 * get us price format
	 * @param pricevalue
	 */
	/*public static String getUSPriceFormat(final BigDecimal priceValue) {
		final DecimalFormat fmt = new DecimalFormat("$#,##0.000");
		return fmt.format(priceValue);
	}*/

	/*
	 * get double us price format
	 * @param price value
	 */
	public static String getdoubleUSPriceFormat(final double priceValue) {
		final DecimalFormat fmt = new DecimalFormat("#,##0.00");
		return fmt.format(priceValue);
	}

	/*
	 * get used amount in percentage
	 * @param limit
	 * @param remaining
	 */
	/*public static int getUsedAmtInPercentage(double limit, double remaining) {
		return (int) ((limit - remaining) / limit * 100);
	}*/

	/*
	 * distance from
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 */
	/*public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	 * Math.cos(Math.toRadians(lat1))
	 * Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));


		return (earthRadius * c);
	}*/

	/*public static Date getNotificationDate() {

		return new Date();
	}*/

	/*
	 * Method assumes endDate to be always greater than startDate
	 * 
	 * @param startDate
	 *            : Date of notification
	 * @param endDate
	 *            : Current Date
	 * @return
	 */
	/*public static long daysBetween(Date startDate, Date endDate) {
		Calendar sDate = getDatePart(startDate);
		Calendar eDate = getDatePart(endDate);

		long daysBetween = 0;
		while (sDate.before(eDate)) {
			sDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}*/

	/*public static Calendar getDatePart(Date date) {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0); // set millisecond in second

		return cal; // return the date part
	}*/

	/*public static void setActionBarTitle(Activity mContext, String title) {
		((TextView) mContext.getActionBar().getCustomView()
				.findViewById(R.id.tv_activity_title)).setText(title);
	}*/

	public static void setFragmentActionBarTitle(Activity mContext, String title) {
		((TextView) mContext.getActionBar().getCustomView()
				.findViewById(R.id.tv_screen_title)).setText(title);
	}

	// This method is for adding red asterik sign on textview
	public static SpannableStringBuilder markFieldRequired(String text) {
		String colored = "*";
		SpannableStringBuilder builder = new SpannableStringBuilder();

		builder.append(text);
		int start = builder.length();
		builder.append(colored);
		int end = builder.length();

		builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	public static String sanitizedReplace(String subject,
			final String placeholder,  String replacement) {
		String sub = subject;
		String replce = replacement;
		if(replce==null){
			replce = "";
		}
		try {
			sub = sub.replaceAll(placeholder, replce);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return sub;
	}


	public static String sanitizedReplace(String subject,
			final String placeholder,  Object replacement) {
		String sub = subject;
		try {
			if(replacement == null)
			{
				sub = sub.replaceAll(placeholder, null);

			}
			else
			{
				sub = sub.replaceAll(placeholder, replacement.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return sub;
	}

	@SuppressWarnings("deprecation")
	public static String getJsonResource(int resId, final Context context) {

		final InputStream is = context.getResources().openRawResource(resId);

		final java.io.DataInputStream din = new java.io.DataInputStream(is);
		StringBuffer sb = new StringBuffer();
		try {
			String line = null;
			while ((line = din.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static boolean isServerErrorCode500(VolleyError error) {
		boolean status;
		if (error.networkResponse == null)
		{
			status = false;
			return status;
		}
		else
		{

			NetworkResponse response = error.networkResponse;
			int statusCode = response.statusCode;
			if (statusCode == 500)
			{
				status = true;
			}
			else
			{
				status = false;
			}
			return status;
		}
	}

	public static boolean isServerErrorCode401(VolleyError error) {
		boolean status;
		NetworkResponse response = error.networkResponse;
		int statusCode = response.statusCode;
		if (statusCode == 500)
		{
			status = true;
		}
		else
		{
			status = false;
		}
		return status;
	}

	public static String getServerErrorMessage(VolleyError error) {
		String errorMsg = "";
		if(error.networkResponse!=null){
			NetworkResponse response = error.networkResponse;
			errorMsg = new String(response.data);
		}
		//  json =getMessage(json, "message");			
		return errorMsg;
	}

	//	public static String parseTimingsFromServerResponse(String openingHour, String closingHour) throws ParseException{
	//		DateFormat df1 = new SimpleDateFormat("'T'HH:mm:ss Z",Locale.US);
	//		Date parsedDate = df1.parse(openingHour);
	//		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
	//		calendar.setTime(parsedDate);   // assigns calendar to given date 
	//
	//		String OpeningHoursText = calendar.get(Calendar.HOUR)+"."+calendar.get(Calendar.MINUTE)+" "+ (Calendar.AM == 1 ? Constants.MMCALENDAR_PM : Constants.MMCALENDAR_AM);
	//
	//		parsedDate = df1.parse(closingHour);
	//		calendar = GregorianCalendar.getInstance();
	//		calendar.setTime(parsedDate); 
	//		OpeningHoursText = OpeningHoursText.concat(" - "+ calendar.get(Calendar.HOUR)+"."+calendar.get(Calendar.MINUTE)+" "+ (Calendar.AM == 1 ? Constants.MMCALENDAR_PM : Constants.MMCALENDAR_AM));
	//
	//		return OpeningHoursText;
	//	}

	public static Date parseDate(String dateDate)
	{
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss",Locale.US);
		Date parsedDate = null;
		try {
			parsedDate = df1.parse(dateDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsedDate;
	}


	public static void addMandatoryAsterisk(Context mContext, int stringId, TextView tv){
		String simple = mContext.getResources().getString(stringId, "*");
		SpannableStringBuilder builder = new SpannableStringBuilder();

		builder.append(simple);
		int start = simple.indexOf("*");
		int end = simple.indexOf("*") + 1;

		builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		tv.setText(builder);
	}

	public static String errorHandling(VolleyError error)
	{
		String msg = null;
		if (error instanceof ServerError) {
			msg = "Server error";
			if (error.networkResponse != null && error.networkResponse.data != null)
			{
				switch (error.networkResponse.statusCode) {
				case 400:
					msg = "400";
					msg = "Bad request.The request has an invalid header name.";
					break;
				case 401:
					msg = "User is not authenticated";
					break;
				case 500:
					msg = "Intenal server error";
					break;
				case 403:
					msg = error.getMessage();
					break;
				case 409:
					msg = error.getMessage();
					break;
				default :
					break;
				}
			}

		} else if (error instanceof TimeoutError){
			msg = "Timeout error";
		}else if (error instanceof NoConnectionError) {
			msg = "NoConnection Error";
		} else if (error instanceof AuthFailureError) {
			msg = "AuthFailure error";
		} else if (error instanceof NetworkError) {
			msg = "Timeout error";
		} else if (error instanceof ParseError) {
			msg = "Timeout error";
		} 
		return msg;
	}

	public static Map<String, String> getStateAbbrevation()
	{
		sstates = new HashMap<String, String>();
		sstates.put("Alabama","AL");
		sstates.put("Alaska","AK");
		sstates.put("Alberta","AB");
		sstates.put("American Samoa","AS");
		sstates.put("Arizona","AZ");
		sstates.put("Arkansas","AR");
		sstates.put("Armed Forces (AE)","AE");
		sstates.put("Armed Forces Americas","AA");
		sstates.put("Armed Forces Pacific","AP");
		sstates.put("British Columbia","BC");
		sstates.put("California","CA");
		sstates.put("Colorado","CO");
		sstates.put("Connecticut","CT");
		sstates.put("Delaware","DE");
		sstates.put("District Of Columbia","DC");
		sstates.put("Florida","FL");
		sstates.put("Georgia","GA");
		sstates.put("Guam","GU");
		sstates.put("Hawaii","HI");
		sstates.put("Idaho","ID");
		sstates.put("Illinois","IL");
		sstates.put("Indiana","IN");
		sstates.put("Iowa","IA");
		sstates.put("Kansas","KS");
		sstates.put("Kentucky","KY");
		sstates.put("Louisiana","LA");
		sstates.put("Maine","ME");
		sstates.put("Manitoba","MB");
		sstates.put("Maryland","MD");
		sstates.put("Massachusetts","MA");
		sstates.put("Michigan","MI");
		sstates.put("Minnesota","MN");
		sstates.put("Mississippi","MS");
		sstates.put("Missouri","MO");
		sstates.put("Montana","MT");
		sstates.put("Nebraska","NE");
		sstates.put("Nevada","NV");
		sstates.put("New Brunswick","NB");
		sstates.put("New Hampshire","NH");
		sstates.put("New Jersey","NJ");
		sstates.put("New Mexico","NM");
		sstates.put("New York","NY");
		sstates.put("Newfoundland","NF");
		sstates.put("North Carolina","NC");
		sstates.put("North Dakota","ND");
		sstates.put("Northwest Territories","NT");
		sstates.put("Nova Scotia","NS");
		sstates.put("Nunavut","NU");
		sstates.put("Ohio","OH");
		sstates.put("Oklahoma","OK");
		sstates.put("Ontario","ON");
		sstates.put("Oregon","OR");
		sstates.put("Pennsylvania","PA");
		sstates.put("Prince Edward Island","PE");
		sstates.put("Puerto Rico","PR");
		sstates.put("Quebec","PQ");
		sstates.put("Rhode Island","RI");
		sstates.put("Saskatchewan","SK");
		sstates.put("South Carolina","SC");
		sstates.put("South Dakota","SD");
		sstates.put("Tennessee","TN");
		sstates.put("Texas","TX");
		sstates.put("Utah","UT");
		sstates.put("Vermont","VT");
		sstates.put("Virgin Islands","VI");
		sstates.put("Virginia","VA");
		sstates.put("Washington","WA");
		sstates.put("West Virginia","WV");
		sstates.put("Wisconsin","WI");
		sstates.put("Wyoming","WY");
		sstates.put("Yukon Territory","YT");
		return sstates;
	}

	public static String getCheckStatus(String statusCode) {
		int statusCodeVal = Integer.parseInt(statusCode);
		String statusData = null;
		switch (statusCodeVal) {
		case Constants.CheckStatus.WAITING_FOR_REVIEW :
			//	Log.e(TAG  ,"getCheckStatus : CheckStatus 01"+ "Review Pending");
			statusData = "Review Pending";
			break;

		case Constants.CheckStatus.USER_ACTION_REQUIRED :
			//	Log.e(TAG ,"getCheckStatus : CheckStatus 02"+"Action Required");
			statusData = "Action Required";
			break;

		case Constants.CheckStatus.CASHED :
			//	Log.e(TAG ,"getCheckStatus : CheckStatus 03"+ "Cashed");
			statusData = "Cashed";
			break;

		case Constants.CheckStatus.REJECTED :
			//	Log.e(TAG ,"getCheckStatus : CheckStatus 04"+ "Rejected");
			statusData = "Rejected";
			break;
		default:
			break;

		}
		return statusData;
	}

	public static int pxTodpConversion(Context context, int pxVal)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxVal*scale + 0.5f);
	}

	//	private static class URLSpanNoUnderline extends URLSpan {
	//		public URLSpanNoUnderline(String url) {
	//			super(url);
	//		}
	//		@Override public void updateDrawState(TextPaint ds) {
	//			super.updateDrawState(ds);
	//			ds.setUnderlineText(false);
	//		}
	//	}


	public static String getyyyymmddDate(String dateMade) {
		SimpleDateFormat isoDateParser = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss", Locale.US);//("yyyy-MM-dd 'T'HH:mm:ss", Locale.US); HH:mm:ss ZZZZZ
		SimpleDateFormat isoStringParser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String formattedTime = dateMade.trim();
		Date parsedDate = null;
		try {
			if(dateMade.length()>10){
				parsedDate = isoDateParser.parse(dateMade);
				formattedTime = isoStringParser.format(parsedDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedTime;
	}

	public static String getyyyymmddDate(String dateMade, boolean isToBeDisplayed) {

		SimpleDateFormat isoDateParser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		SimpleDateFormat isoStringParser = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		String formattedTime = dateMade;

		Date parsedDate = null;
		try {
			parsedDate = isoDateParser.parse(dateMade);
			formattedTime = isoStringParser.format(parsedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedTime;
	}

	public static String getyyyymmddDateInUs(String dateMade) {
		SimpleDateFormat isoDateParser = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss ZZZZZ", Locale.US);//("yyyy-MM-dd 'T'HH:mm:ss", Locale.US);
		SimpleDateFormat isoStringParser = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		String formattedTime = dateMade;
		//		DateFormat dateDefault = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss 'Z'",Locale.US);
		Date parsedDate = null;
		try {
			if(dateMade.length()>10){
				parsedDate = isoDateParser.parse(dateMade);
				formattedTime = isoStringParser.format(parsedDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedTime;
	}

	public static String getVersionBuildNumber(LoginActivity mContext) {
		PackageManager manager = mContext.getPackageManager();
		PackageInfo info;
		String version = null;
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
			version = mContext.getString(R.string.momeymart_app_version, info.versionName);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;

	}

	public static String getFormattedAmount(double amount){
		double checkAmount = amount;
		String editTextAmount = Double.toString(Math.abs(checkAmount));
		int integerPlaces = editTextAmount.indexOf('.');
		int decimalPlaces = editTextAmount.length() - integerPlaces - 1;

		if(decimalPlaces == 1){
			editTextAmount+= 0;
		}

		return editTextAmount;
	}

	public static void clearUserDetailsPreferences(Preferences mPreferences){
		mPreferences. addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_FIRST_NAME, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_LAST_NAME, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_EMAIL_ADDRESS, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_DOB, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_SSN, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_HOUSE_NUMBER, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_STREET, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_CITY, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_STATE, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_ZIP_CODE, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_HOME_PHONE, "");
		mPreferences.addOrUpdateString(PreferenceConstants.PERSONAL_DETAILS_CELL_PHONE, "");
	}

	public static String getOpenCloseHrs(Context context, Object open, Object close) {
		String outputTime = null;
		if(open == null || close == null) {
			outputTime = "";
		} else {
			try {
				String time = open.toString();
				time = 	time.substring(1,6).trim();
				SimpleDateFormat  inputFormat = new SimpleDateFormat("HH:mm");
				DateFormat outputFormat = new SimpleDateFormat("hh:mm aa",
						Locale.US);
				Date date = inputFormat.parse(time);
				String output = outputFormat.format(date);
				String	closing = close.toString();
				closing = closing.substring(1,6).trim();
				Date date1 = inputFormat.parse(closing);
				String output1 = outputFormat.format(date1);
				String formattedTime = output +" - "+output1;
				outputTime = formattedTime.replace("AM", context.getText(R.string.opening_hours_am)).replace("PM",context.getText(R.string.opening_hours_pm));
			} catch (Exception e) {
				outputTime = "";
			}
		}
		return outputTime;
	}

	public static String getFormedNumber(String accountNumber) {
		String number = "";
		
		String asterisk = "****";
	    if(accountNumber.length() <= 8){
			int countOfAsterisk = accountNumber.length() - 4;
			String prefixAsterisk = "";
			prefixAsterisk  = asterisk.substring(0, countOfAsterisk);
			number = new StringBuilder(prefixAsterisk).append(accountNumber.substring(accountNumber.length()-4, accountNumber.length())).toString();
	    }
	    else{
	    	number = new StringBuilder(asterisk).append(accountNumber.substring(accountNumber.length()-4, accountNumber.length())).toString();
		}
		
		return number;
	}

	public static String getAccountType(Context context,String type) {
		if (type.contains("Bank")) {
			return context.getResources().getString(R.string.bank_account_title);
		} else {
			return context.getResources().getString(R.string.navigation_momentum_card_drawer);
		}
		
	}
}


