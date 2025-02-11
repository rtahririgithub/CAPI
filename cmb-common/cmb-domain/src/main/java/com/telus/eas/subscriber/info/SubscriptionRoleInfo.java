package com.telus.eas.subscriber.info;

/**
 * Title:        TELUS Mobility Client Authentication Enhancement
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      TELUS Mobility
 * @author       Ken Duffy
 * @version 1.0
 */

import com.telus.eas.framework.info.Info;
import com.telus.api.account.SubscriptionRole;

public class SubscriptionRoleInfo extends Info implements SubscriptionRole
{

  static final long serialVersionUID = 1L;

  public String getCode(){ return code; }

  public void setCode(String code){ this.code = code; }

  public String getDealerCode(){ return dealerCode; }

  public void setDealerCode(String dealerCode){ this.dealerCode = dealerCode; }

  public String getSalesRepCode(){ return salesRepCode; }

  public void setSalesRepCode(String salesRepCode){ this.salesRepCode = salesRepCode; }

  public String getCsrId(){ return csrId; }

  public void setCsrId(String csrId){ this.csrId = csrId; }

  public String toString()
    {
      StringBuffer s = new StringBuffer(128);
      s.append("SubscriptionRoleInfo:[\n");
      s.append("    subscriptionRoleCode=[").append(code).append("]\n");
      s.append("    dealerCode=[").append(dealerCode).append("]\n");
      s.append("    salesRepCode=[").append(salesRepCode).append("]\n");
      s.append("    csrId=[").append(csrId).append("]\n");
      s.append("    isModified=[").append(modified).append("[\n");
      s.append("]");
      return s.toString();
    }

  public boolean isModified(){ return modified; }

  public void setModified(boolean modified){ this.modified = modified; }

  private String code;
  private String dealerCode;
  private String salesRepCode;
  private String csrId;
  private boolean modified;
}
