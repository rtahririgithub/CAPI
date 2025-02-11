package com.telus.eas.account.info;


import com.telus.api.account.*;

public class WesternPrepaidConsumerAccountInfo extends PrepaidConsumerAccountInfo implements PCSWesternPrepaidConsumerAccount  {

	static final long serialVersionUID = 1L;

  public static WesternPrepaidConsumerAccountInfo newPCSInstance0() {
    return new WesternPrepaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID);
  }

  public static PrepaidConsumerAccountInfo newPCSInstance() {
    return newPCSInstance0();
  }

  protected WesternPrepaidConsumerAccountInfo(char accountType, char accountSubType) {
        super(accountType, accountSubType);
  }

  public void copyFrom(AccountInfo o) {
    if(o instanceof WesternPrepaidConsumerAccountInfo) {
      copyFrom((WesternPrepaidConsumerAccountInfo)o);
    } else {
      super.copyFrom(o);
    }
  }

   public void copyFrom(WesternPrepaidConsumerAccountInfo o) {
    super.copyFrom(o);
  }




   public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("WesternPrepaidConsumerAccountInfo:{\n");
    s.append(super.toString());
    s.append("}");

    return s.toString();
  }

}




