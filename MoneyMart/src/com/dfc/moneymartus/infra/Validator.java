package com.dfc.moneymartus.infra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
	private static Pattern sPattern;
	private static Matcher sMatcher;

//	public static final String FIRSTNAME_PATTERN = "^[a-zA-Z0-9]+$";//"^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}";"^[a-zA-Z0-9]*$"
//	public static final String SECONDNAME_PATTERN = "^[a-zA-Z0-9]+$";
//	public static final String FIRSTNAME_PATTERN = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){1})(?=(?:.*\\d){1})(?!(?:.*[~?_!@#$%^&*-]){1}).{8,20}$";
//	public static final String SECONDNAME_PATTERN = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){1})(?=(?:.*\\d){1})(?!(?:.*[~?_!@#$%^&*-]){1}).{8,20}$";
	public static final String FIRSTNAME_PATTERN = "^([a-zA-Z0-9]+)(?!(?:.*[~?_!@#$%^&*-]){1}).{1,30}$";
	public static final String SECONDNAME_PATTERN = "^([a-zA-Z0-9]+)(?!(?:.*[~?_!@#$%^&*-]){1}).{1,30}$";
	public static final String HOUSENUMBER_PATTERN = "^[a-zA-Z0-9]+$";
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//	public static final String PASSWORD_PATTREN = "((?=.*\\d)(?=.*[A-Z])(?=.*[_!@#$%^&*-]).{8,20})";
	public static final String PASSWORD_PATTREN = "^(?=(?:.*[A-Z]){1})(?=(?:.*\\d){1})(?=(?:.*[~?_!@#$%^&*-]){1}).{8,20}$";
	public static final String PHONENUMBER_PATTERN = "^[0-9]{10,10}$";
	public static final String ZIPCODE_PATTERN = "^[0-9]{5,5}$";
	public static final String SSN_PATTERN = "^[0-9]{9,9}$";
	public static final String USERNMAE_PATTERN = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){1})(?=(?:.*\\d){1})(?!(?:.*[~?_!@#$%^&*-]){1}).{8,20}$"; 

	private Validator(){
		//pattern = Pattern.compile(USERNMAE_PATTERN);
	}

	/**
	 *  Validate data with regular expression
	 * @param patternStr pattern to verify
	 * @param validationStr string to verification using pattern
	 * @return true valid data, false invalid data
	 */
	public static boolean validate(final String patternStr, String validationStr){
		sPattern = Pattern.compile(patternStr);
		sMatcher = sPattern.matcher(validationStr);
		return sMatcher.matches();

	}

}
