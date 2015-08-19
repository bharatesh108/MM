package com.dfc.moneymartus.dto;

public class CCServicesDataObject {

	public String serviceName;
	//@SerializedName("customerServices")
	public CustomerServices customerServices;
	public String privacyPolicy;

	public class CustomerServices {
		public String name;
		public String phone;
		public Object email;
		public String website;
		//	@SerializedName("address")
		public Addressdata address;
		public OpeningHours openingHours;

		public String getEmail() {
			if(email == null){
				this.email = "";
			}
			return email.toString();
		}

		public void setEmail(Object email) {
			this.email = email;
		}
	}

	public class Addressdata {
		public String houseNumber;
		public String street;
		public String city;
		public String county;
		public String state;
		public String postcodeZip;
		public String country;
		//@SerializedName("openingHours")

	}

	public class OpeningHours {

		//	@SerializedName("monday")		
		public Monday monday;
	}

	public class Monday {
		public Object open;
		public Object close;
	}

}

