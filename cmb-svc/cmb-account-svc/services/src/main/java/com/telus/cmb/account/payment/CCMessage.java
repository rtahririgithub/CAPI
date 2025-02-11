package com.telus.cmb.account.payment;

public class CCMessage {
	private String englishMessage;
	private String frenchMessage;
	private String kbMemoMessage;
	
	public CCMessage() {
		
	}
	
	public CCMessage(String defaultMessage ) {
		englishMessage = defaultMessage;
		frenchMessage = defaultMessage;
		kbMemoMessage = defaultMessage;
	}
	
	public String getEnglishMessage() {
		return englishMessage;
	}
	public void setEnglishMessage(String englishMessage) {
		this.englishMessage = englishMessage;
	}
	public String getFrenchMessage() {
		return frenchMessage;
	}
	public void setFrenchMessage(String frenchMessage) {
		this.frenchMessage = frenchMessage;
	}
	public String getKbMemoMessage() {
		return kbMemoMessage;
	}
	public void setKbMemoMessage(String kbMemoMessage) {
		this.kbMemoMessage = kbMemoMessage;
	}
}
