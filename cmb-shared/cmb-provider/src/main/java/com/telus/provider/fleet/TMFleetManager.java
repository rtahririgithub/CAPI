/*

 * $Id$

 * %E% %W%

 * Copyright (c) Clearnet Inc. All Rights Reserved.

 */



package com.telus.provider.fleet;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidNetworkException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.FleetManager;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;






public class TMFleetManager extends BaseProvider implements FleetManager {

 private final Map fleetById = new HashMap(300);

  public TMFleetManager(TMProvider provider) {

    super(provider);

  }



  //--------------------------------------------------------------------

  //  Decorative Methods

  //--------------------------------------------------------------------



  //--------------------------------------------------------------------

  //  Service Methods

  //--------------------------------------------------------------------

  private Fleet[] decorate(Fleet[] fleets) throws TelusAPIException, UnknownBANException {
    ArrayList list = new ArrayList();
    for(int i=0; i<fleets.length; i++) {
      list.add(new TMFleet(provider, (FleetInfo)fleets[i]));
    }
   return (Fleet[]) list.toArray(new Fleet[list.size()]);
  }

  private Fleet decorate(Fleet fleet) throws TelusAPIException, UnknownBANException {
      return new TMFleet(provider, (FleetInfo)fleet);
  }


  public Fleet[] getPublicFleets(int networkId) throws InvalidNetworkException, TelusAPIException {
    try{
      FleetInfo[] fleetInfos = provider.getReferenceDataHelperEJB().retrieveFleetsByFleetType(Fleet.TYPE_PUBLIC);
      Fleet[] fleets = decorate(fleetInfos);

      //---------------------------------------------
      // filter fleets by networkId.
      //---------------------------------------------
      List list = new ArrayList(fleets.length);
      for(int i=0; i<fleets.length; i++) {
        if(fleets[i].getNetworkId() == networkId) {
          list.add(fleets[i]);
        }
      }

      return (Fleet[])list.toArray(new Fleet[list.size()]);
     }catch (TelusException e) {
       if ("APP10013".equals(e.id)) {
        throw new InvalidNetworkException("Fleet not found", e,  networkId);
       }
       throw new TelusAPIException(e);
     }catch (Throwable e) {
      throw new TelusAPIException(e);
     }
  }

  public Fleet[] getFleetsByBan(int banId) throws UnknownBANException, TelusAPIException {
	  try{
		  FleetInfo[] fleets = provider.getAccountInformationHelper().retrieveFleetsByBan(banId);
		  return decorate(fleets);
	  }catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
	  return null;
  }

  public Fleet getFleetByPhoneNumber(String phoneNumber) throws UnknownSubscriberException, TelusAPIException {
    try{
      Subscriber s = provider.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);

      if(!s.isIDEN()) {
        throw new UnknownSubscriberException("phoneNumber=[" + phoneNumber + "] is not IDEN");
      }

      IDENSubscriber subscriber = (IDENSubscriber)s;
      return subscriber.getFleet();
    }catch(TelusAPIException e) {
      throw e;
    }catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }



  public Fleet getFleetById(int urbanID, int fleetId) throws UnknownObjectException, TelusAPIException {
    FleetIdentityInfo fleetIdentity = new FleetIdentityInfo(urbanID, fleetId);
     try{
           String  obj = "" + urbanID + fleetId;
           TMFleet fleet = (TMFleet)fleetById.get(obj);
           if(fleet == null){
             fleet = (TMFleet)decorate(provider.getReferenceDataHelperEJB().
                              retrieveFleetByFleetIdentity(fleetIdentity));
             fleetById.put(obj, fleet);
           }
         return fleet;
      }catch (TelusException e) {
       if ("VAL10013".equals(e.id)) {
        throw new UnknownObjectException("Fleet not found:  urbanID=[" + urbanID + "]; fleetId=[" + fleetId + "]", e);
       }
       throw new TelusAPIException(e);
      }catch(Throwable e) {
      throw new TelusAPIException(e);
     }

  }



}









