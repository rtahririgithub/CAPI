
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ServiceExclusionGroupsInfo extends Info implements ServiceExclusionGroups {

 static final long serialVersionUID = 1L; 


protected String[] exclusionGroups ;
private String code = "";
private String description = "";
private String descriptionFrench = "" ;



/**
 * @return Returns the exclusionGroups.
 */
public String[] getExclusionGroups() {
	return exclusionGroups;
}
/**
 * @param exclusionGroups The exclusionGroups to set.
 */
public void setExclusionGroups(String[] exclusionGroups) {
	this.exclusionGroups = exclusionGroups;
}

public String toString()
{
    StringBuffer s = new StringBuffer(1024);

    s.append("ExclusionGroups:[\n");
    s.append("    code=[").append(code).append("]\n");

    if(exclusionGroups == null)
    {
        s.append("    exclusionGroups=[null]\n");
    }
    else if(exclusionGroups.length== 0)
    {
        s.append("    aexclusionGroups={}\n");
    }
    else
    {

        for (int i=0; i<exclusionGroups.length; i++) {
          s.append("   exclusionGroups[").append(i).append("]=[").append(exclusionGroups[i]).append("]\n");
        }
  s.append("]");
    }
  return s.toString();

}
/**
 * @return Returns the code.
 */
public String getCode() {
	return code;
}
/**
 * @param code The code to set.
 */
public void setCode(String code) {
	this.code = code;
}
/**
 * @return Returns the description.
 */
public String getDescription() {
	return description;
}
/**
 * @return Returns the descriptionFrench.
 */
public String getDescriptionFrench() {
	return descriptionFrench;
}
}
