/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import com.telus.api.reference.*;
import com.telus.eas.utility.info.*;

public class TMShareablePricePlan extends TMPricePlan implements ShareablePricePlan {

  private final PricePlanInfo delegate;

  public TMShareablePricePlan(TMReferenceDataManager referenceDataManager, PricePlanInfo delegate) {
    super(referenceDataManager, delegate);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int getMaximumSubscriberCount() {
    return delegate.getMaximumSubscriberCount();
  }

  public String getSecondarySubscriberService() {
    return delegate.getSecondarySubscriberService();
  }


}




