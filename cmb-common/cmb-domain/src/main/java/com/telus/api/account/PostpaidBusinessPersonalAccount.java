/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

/**
 * <CODE>PostpaidBusinessPersonalAccount</CODE>
 *
 */
public interface PostpaidBusinessPersonalAccount extends PostpaidConsumerAccount {
  String getLegalBusinessName();

  /**
   * This method has been modified to use the ConsumerName interface accessible through the parent interface.
   * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
   * 
   * @see PostpaidConsumerAccount#getName 
   */
  void setLegalBusinessName(String legalBusinessName);

  /**
   * This method has been modified to use the ConsumerName interface accessible through the parent interface.
   * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
   * 
   * @see PostpaidConsumerAccount#getName 
   */
  String getOperatingAs();

  /**
   * This method has been modified to use the ConsumerName interface accessible through the parent interface.
   * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
   * 
   * @see PostpaidConsumerAccount#getName 
   */
  void setOperatingAs(String operatingAs);
}



