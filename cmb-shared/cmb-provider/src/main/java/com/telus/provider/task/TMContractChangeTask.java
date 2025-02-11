/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.task;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.reference.*;
import com.telus.api.task.*;
import com.telus.provider.account.*;
import java.util.*;

public class TMContractChangeTask extends TMTask implements ContractChangeTask {

  private final List servicesToAdd = new ArrayList();
  private final List servicesToDelete = new ArrayList();
  private final TMSubscriber subscriber;
  private final TMContract contract;
  private final String dealerCode;
  private final String salesRepCode;


  public TMContractChangeTask(TMSubscriber subscriber, String dealerCode, String salesRepCode) throws TelusAPIException {
    this.subscriber = subscriber;
    this.contract = subscriber.getContract0();
    this.contract.setCascadeShareableServiceChanges(false);
    this.dealerCode = dealerCode;
    this.salesRepCode = salesRepCode;
  }

  public Subscriber getSubscriber() {
    return subscriber;
  }

  public Contract getContract() {
    return contract;
  }

  public Service[] getServicesToAdd() {
    return (Service[])servicesToAdd.toArray(new Service[servicesToAdd.size()]);
  }

  public Service[] getServicesToDelete() {
    return (Service[])servicesToDelete.toArray(new Service[servicesToDelete.size()]);
  }

  public void addService(Service service) {
    servicesToAdd.add(service);
  }

  public void removeService(Service service) {
    servicesToDelete.add(service);
  }

  public boolean hasWork() {
    return servicesToAdd.size() > 0 || servicesToDelete.size() > 0;
  }

  protected void runImpl() throws Throwable {
    for (int i = servicesToAdd.size()-1; i >= 0; i--) {
      Service service = (Service)servicesToAdd.get(i);
      if (!contract.containsService(service.getCode()) && contract.canModifyShareableService(service.getCode())) {
        contract.addService(service);
      } else {
        servicesToAdd.remove(i);  // exclude services already on contract
      }
    }

    for (int i = servicesToDelete.size()-1; i >= 0; i--) {
      Service service = (Service)servicesToDelete.get(i);
      if (contract.containsService(service.getCode()) && contract.canModifyShareableService(service.getCode())) {
        contract.removeService(service.getCode());
      } else {
        servicesToDelete.remove(i);  // exclude services not on contract
      }
    }

    if (contract.isModified()) {
      contract.save(dealerCode, salesRepCode);
    }
  }

}



