package com.dfc.moneymartus.infra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {

	private final Pattern mPattern;

	  private static final String USERNMAE_PATTERN = 
		 "^((?=.*[a-z])(?=.*[A-Z])[0-9]*).{8,20}$";

	  public UsernameValidator(){
		  mPattern = Pattern.compile(USERNMAE_PATTERN);
	  }

	  /**
	   * Validate password with regular expression
	   * @param password password for validation
	   * @return true valid password, false invalid password
	   */
	  public boolean validate(final String password){

		  Matcher mMatcher = mPattern.matcher(password);
		  return mMatcher.matches();

	  }
}
