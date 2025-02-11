package com.telus.eas.account.info;

import com.telus.api.account.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PostpaidBusinessOfficialAccountInfo  extends PostpaidBusinessRegularAccountInfo implements PCSPostpaidBusinessOfficialAccount {
	 static final long serialVersionUID = 1L;


  public static PostpaidBusinessOfficialAccountInfo newPCSInstance0() {
    return new PostpaidBusinessOfficialAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_PCS_OFFICAL);
  }

  public static PostpaidBusinessRegularAccountInfo newPCSInstance() {
    return newPCSInstance0();
  }



  protected PostpaidBusinessOfficialAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidBusinessDealerAccountInfo:{\n");
    s.append(super.toString());
    s.append("}");

    return s.toString();
  }


}