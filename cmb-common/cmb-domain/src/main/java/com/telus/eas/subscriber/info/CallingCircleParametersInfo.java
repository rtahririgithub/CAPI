package com.telus.eas.subscriber.info;

import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.CallingCirclePhoneList;
import com.telus.eas.framework.info.Info;

public class CallingCircleParametersInfo extends Info implements
		CallingCircleParameters {

	private static final long serialVersionUID = 1L;

	private CallingCirclePhoneListInfo callingCircleCurrentPhoneNumberList;
	private CallingCirclePhoneListInfo callingCircleFuturePhoneNumberList;


	public CallingCirclePhoneList getCallingCircleCurrentPhoneNumberList() {
		return callingCircleCurrentPhoneNumberList;
	}

	public void setCallingCircleCurrentPhoneNumberList(
			CallingCirclePhoneListInfo callingCircleCurrentPhoneNumberList) {
		this.callingCircleCurrentPhoneNumberList = callingCircleCurrentPhoneNumberList;
	}

	public CallingCirclePhoneList getCallingCircleFuturePhoneNumberList() {
		return callingCircleFuturePhoneNumberList;
	}

	public void setCallingCircleFuturePhoneNumberList(
			CallingCirclePhoneListInfo callingCircleFuturePhoneNumberList) {
		this.callingCircleFuturePhoneNumberList = callingCircleFuturePhoneNumberList;
	}



}
