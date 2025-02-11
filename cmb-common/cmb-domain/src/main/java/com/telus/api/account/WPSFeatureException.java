package com.telus.api.account;

import com.telus.api.TelusAPIException;

public class WPSFeatureException extends TelusAPIException {

	private static final int REASON_OFFSET = 700;
	public static final int ADD_TOOMANYFEATURES = REASON_OFFSET + 1;
	
	public static final String ADD_TOOMANYFEATURES_MESSAGE_EN = "Sorry, you are unable to add any more features to your account at this time.";
	public static final String ADD_TOOMANYFEATURES_MESSAGE_FR = "Nous sommes désolés. Il est impossible d’ajouter d’autres fonctions à votre compte pour le moment.";

	
	private final String englishMessage;
	private final String frenchMessage;
	
	public WPSFeatureException(Throwable e, int reason, String message, String messageFR){
	     super(e);
	     super.reason = reason;
	     this.englishMessage = message;
	     this.frenchMessage = messageFR;
	 }
	
	
	public String getEnglishMessage() {
		return englishMessage;
	}
	public String getFrenchMessage() {
		return frenchMessage;
	}
	public int getReason() {
		return super.reason;
	}
	
}
