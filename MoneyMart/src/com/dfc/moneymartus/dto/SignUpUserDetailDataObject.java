package com.dfc.moneymartus.dto;

public class SignUpUserDetailDataObject {

	public String username;//mUserName;
	public String firstName;//mFirstName;
	public String secondName;//mSecondName;
	public String dateOfBirth;//mDOB;
	public String ssn;//mSSN;
	public String houseNumber = "";//mHouseNumber;
	public String street = "";//mStreet;
	public String city ="";//mCity;
	public String county = "";//mCounty;
	public String state = "";//mState;
	public String postcodeZip ="";//mPostalCode;
	public String country = "";
	public String phone="";//mPhone;
	public String phone2 = "";//mPhone2;
	public String mobile = "";//mMobile;
	public String email = "";//mEmail;
	public String password = "";//mPassword;
	public String challengeId = "";
	public String secret = "";
	public Boolean termsAgreed;//mTermsAgreed;
	
//	@SerializedName("address")
		public Addressdata address;
		public class Addressdata {
			public String houseNumber = "";//mHouseNumber;
			public String street = "";//mStreet;
			public String city ="";//mCity;
			public String county = "";//mCounty;
			public String state = "";//mState;
			public String postcodeZip ="";//mPostalCode;
			public String country = "";
		}
}
/*{
	"username": "##username##",
	"firstName":"##firstName##",
	"secondName":"##secondName##",
	"dateOfBirth":"##dateOfBirth##",
	"ssn":"##ssn##",
	"address":
	{
		"houseNumber":"##houseNumber##",
		"street":"##street##",
		"city":"##city##",
		"county":null,
		"state":"##state##",
		"postcodeZip":"##postcodeZip##",
		"country":"##country##"
	},
	"phone":"##phone##",
	"phone2":null,
	"mobile":"##mobile##",
	"email":"##email##",
	"password":"##password##",
	"challengeId":"##challengeId##",
	"secret":"##secret##",
	"termsAgreed":true,
	"services":[{
		"service":"cc",
		"termsAgreed":true,
		"status":"enabled",
		"extra":null
	}]
}
 */ 
