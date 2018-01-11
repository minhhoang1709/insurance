package com.ninelives.insurance.provider.payment.midtrans.ref;

//capture, settlement, pending, cancel, expired) + (denied?
public class MidtransTransactionStatus {
	
	public static final String pending="pending";
	public static final String settlement="settlement";
	public static final String capture="capture";
	public static final String expire="expire";
	public static final String cancel="cancel";
	public static final String deny="deny";
	
}
