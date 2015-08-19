package com.dfc.moneymartus.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GetCheckDataObject {

	public ArrayList<ChecksCls> cheques;

	public class ChecksCls {// implements Comparable<ChecksCls>{
		public String chequeId;
		public String statusCode;
		public String statusDescription;
		public String dateSubmitted;
		public String dateCompleted;
		public int transactionId;
		public String chequeMaker;
		public int chequeNumber;
		public int amount;
		public String amountCurrency;
		public String dateMade;
		public int fees;
		public String feesCurrency;
		public String feesAgreed;
		public String fundingMechanism;
		public ArrayList<ActionRequiredCls> actionsRequired;
		public LinksCls _links;
	}

	public class ActionRequiredCls {
		public String action;
	}

	public class LinksCls {
		public SelfCls self;
		public CheckImageBackCls chequeImageBack;
		public CheckImageFrontCls chequeImageFront;
		public ChequeImageFrontVoidedCls chequeImageFrontVoided;
	}

	/**
	 * get check data objects
	 */
	public GetCheckDataObject() {
		super();
	}

	public class SelfCls {
		public String href;
	}

	public class CheckImageFrontCls {
		@SerializedName("content-type")
		public String content_type;
		public String rel;
		public String href;
	}

	public class CheckImageBackCls {
		@SerializedName("content-type")
		public String content_type;
		public String rel;
		public String href;
	}

	public class ChequeImageFrontVoidedCls {
		@SerializedName("content-type")
		public String content_type;
		public String rel;
		public String href;
	}
}
