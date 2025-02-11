package com.telus.eas.utility.info;

/**
 * Title:        TELUS Mobility Client Authentication Enhancement
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      TELUS Mobility
 * @author       Ken Duffy
 * @version 1.0
 */

import java.util.Date;
import com.telus.eas.framework.info.Info;
import com.telus.api.reference.SubscriptionRoleType;

public class SubscriptionRoleTypeInfo extends Info implements SubscriptionRoleType
{
	static final long serialVersionUID = 1L;

  public String getCode(){ return code; }

  public void setCode(String code){ this.code = code; }

  public String getDescription(){ return description; }

  public void setDescription(String description){ this.description = description; }

  public String getDescriptionFrench() { return descriptionFrench; }

  public void setDescriptionFrench(String descriptionFrench){ this.descriptionFrench = descriptionFrench; }

  public Date getLoadDate(){ return loadDate; }

  public void setLoadDate(Date loadDate){ this.loadDate = loadDate; }

  public Date getUpdateDate(){ return updateDate; }

  public void setUpdateDate(Date updateDate){ this.updateDate = updateDate; }

  public String getUserLastModify(){ return userLastModify; }

  public void setUserLastModify(String userLastModify){ this.userLastModify = userLastModify; }

  public int getRank(){ return rank; }

  public void setRank(int rank){ this.rank = rank; }

  public String toString()
    {
      StringBuffer s = new StringBuffer(128);
      s.append("SubscriptionRoleInfo:[\n");
      s.append("    subscriptionRoleCode=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    loadDate=[").append(loadDate).append("]\n");
      s.append("    updateDate=[").append(updateDate).append("]\n");
      s.append("    userLastModify=[").append(userLastModify).append(
          "]\n");
      s.append("    rank=[").append(rank).append("]\n");
      s.append("]");
      return s.toString();
    }

  private String code;
  private String description;
  private String descriptionFrench;
  private Date loadDate;
  private Date updateDate;
  private String userLastModify;
  private int rank;
}
