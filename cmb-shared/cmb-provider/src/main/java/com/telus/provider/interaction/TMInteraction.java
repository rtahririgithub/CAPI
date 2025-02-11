/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.interaction;

import java.util.Date;

import com.telus.api.TelusAPIException;

import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
  * Provider Interaction implementation.  This provides clients with an implementation of Interaction
  * that is capable of making remote calls.
  *
  */
public class TMInteraction extends BaseProvider implements Interaction {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Interaction delegate = null;

  /**
    * Default empty constructor
    */
//  public TMInteraction() {
//  }

  /**
    * Creates a Interaction provider that deglates remote calls to configurationManagerEJB and local
    * calls to delegate.
    *
    * @param delegate -- The delegate used for local calls.
    * @param configurationManagerEJB -- The delegate for remote calls.
    */
  public TMInteraction(TMProvider provider, Interaction delegate) {
	super(provider);
    this.delegate = delegate;
  }

  /**
   *
   * Returns the details of this interaction.
   *
   * <P>The returned objects will be specific types of InteractionDetail (i.e. PricePlanChange,
   * BillPayment, etc.).  Use the getType() method to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @see #getType
   *
   * @associates <{InteractionDetail}>
   * @link aggregation
   * @supplierCardinality 0..*
   *
   */
  public InteractionDetail[] getDetails() throws TelusAPIException {
    InteractionDetail[] details = null;
    try {
        details = provider.getConfigurationManagerNew().getInteractionDetails(getId());
    }
    catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
    }

    if(details == null)
      return new InteractionDetail[0];
    else
      return details;
  }

  /**
   * Returns one of the InteractionManager.TYPE_xxx constants.
   *
   * @see InteractionManager
   */
  public String getType() {
    return delegate.getType();
  }

  /**
    * Returns the id (primary key) of the interaction.
    *
    * @return long -- The id.
    */
  public long getId() {
    return delegate.getId();
  }

  /**
    * Returns the date the interaction takes place.
    *
    * @return Date
    */
  public Date getDatetime() {
    return delegate.getDatetime();
  }

  /**
   * Returns the subscriber associated with this interaction or <CODE>null</CODE>.
   *
   */
  public String getSubscriberId() {
    return delegate.getSubscriberId();
  }

  /**
    * Returns the id of the account the interaction affected.
    *
    * @return int
    */
  public int getBan() {
    return delegate.getBan();
  }

  /**
    * Returns the id of the application that initiated the interaction.
    *
    * @return String
    */
  public String getApplicationId() {
    return delegate.getApplicationId();
  }

  /**
    * Returns the id of the user that initiated the interaction.
    *
    * @return Integer
    */
  public Integer getOperatorId() {
    return delegate.getOperatorId();
  }

  /**
    * Returns the dealer code or <code>null</code> if there is no dealer associated with this interaction
    *
    * @return String
    */
  public String getDealerCode() {
    return delegate.getDealerCode();
  }

  /**
    * Returns the sales rep code or <code>null</code> if there is no dealer associated with this interaction
    *
    * @return String
    */
  public String getSalesrepCode() {
    return delegate.getSalesrepCode();
  }
}