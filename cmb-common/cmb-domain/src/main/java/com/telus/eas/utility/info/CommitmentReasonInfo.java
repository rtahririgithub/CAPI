package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import com.telus.api.reference.*;

public class CommitmentReasonInfo extends ReferenceInfo implements CommitmentReason{

	static final long serialVersionUID = 1L;

   public CommitmentReasonInfo(){}

  public CommitmentReasonInfo(String code, String description, String descriptionFrench){
     this.code = code;
     this.description = description;
     this.descriptionFrench = descriptionFrench;

  }

 
}