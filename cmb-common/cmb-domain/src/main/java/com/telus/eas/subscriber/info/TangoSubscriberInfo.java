package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.account.TangoSubscriber;

public class TangoSubscriberInfo extends SubscriberInfo implements TangoSubscriber {

	  static final long serialVersionUID = 1L;

	  private Date nextPhoneNumberChangeDate;
	  private String nextPhoneNumber;

	  public void setNextPhoneNumberChangeDate(Date nextPhoneNumberChangeDate) {
	    this.nextPhoneNumberChangeDate = nextPhoneNumberChangeDate;
	  }

	  public Date getNextPhoneNumberChangeDate() {
	    return nextPhoneNumberChangeDate;
	  }

	  public void setNextPhoneNumber(String nextPhoneNumber) {
	    this.nextPhoneNumber = nextPhoneNumber;
	  }

	  public String getNextPhoneNumber() {
	    return nextPhoneNumber;
	  }
	
	public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("TangoSubscriberInfo:[\n");
    sb.append(super.toString()).append("]\n");
    sb.append("]");

    return sb.toString();
  }
}
