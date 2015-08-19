package com.dfc.moneymartus.gateway;

import java.util.HashMap;

import com.dfc.moneymartus.infra.Constants;

public class RequestInput {
	public String url = Constants.URL_USER_LOGIN;
	public HashMap<String, String>  headers = new HashMap<String, String>();
	public String body;
	

}
