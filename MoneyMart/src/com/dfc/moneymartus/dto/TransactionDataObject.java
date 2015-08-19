package com.dfc.moneymartus.dto;

public class TransactionDataObject {

	public TransactionList[] transactions;

	public class TransactionList {

		public String date;
		public double amount;
		public String amountCurrency;
		public String description;
		public boolean debit;
		
	}
	
}
