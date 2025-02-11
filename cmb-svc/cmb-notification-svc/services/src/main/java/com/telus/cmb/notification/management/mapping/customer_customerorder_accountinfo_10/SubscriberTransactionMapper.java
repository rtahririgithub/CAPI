package com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_10;

import java.util.Date;
import java.util.List;

import com.telus.cmb.common.confirmationnotification.BillingAccount;
import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;
import com.telus.cmb.common.confirmationnotification.FavoriteNumber;
import com.telus.cmb.common.confirmationnotification.Service;
import com.telus.cmb.common.confirmationnotification.ServiceCancellation;
import com.telus.cmb.common.confirmationnotification.ServiceChange;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.notification.management.mapping.SubscriberTransactionEmailTemplateMapper;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.AccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.CancellationInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.CustomerAccount;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.FavoriteNumberInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.ServiceAgreementInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.ServiceInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.wirelessserviceinfo_v1.TransferOwnershipInfo;

public class SubscriberTransactionMapper extends AbstractSchemaMapper<CustomerAccount, ConfirmationNotification> implements SubscriberTransactionEmailTemplateMapper{

	public SubscriberTransactionMapper() {
		super(CustomerAccount.class, ConfirmationNotification.class);
	}

	@Override
	protected CustomerAccount performSchemaMapping(ConfirmationNotification source, CustomerAccount target) {
		
		target.setAccountInfo( mapToAccountInfo(source.getBillingAccount()));
		target.setTransactionDate(source.getTransactionDate());
		if ( source.getSubscriber()!=null) {
			target.getAccountInfo().setSubscriberPhoneNumber(source.getSubscriber().getSubscriberPhoneNumber());
		}
		
		if (source.getServiceCancellation()!=null ) {
			target.setCancellationInfo(mapToCancellationInfo( source.getServiceCancellation() ));
		}
		else if ( source.getTransferOwnership()!=null  ) {
			TransferOwnershipInfo town = new TransferOwnershipInfo();
			town.setEffectiveDate(source.getTransactionDate());
			town.getSubscriberPhoneNumber().add( target.getAccountInfo().getSubscriberPhoneNumber() );
			target.setTransferOwnershipInfo( town);
		}
		else if ( source.getServiceChange()!=null ) {
			target.setServiceAgreementInfo( mapToServiceAgreementment( source.getServiceChange() ) ) ;
		}
		
		return super.performSchemaMapping(source, target);
	}
	

	private ServiceAgreementInfo mapToServiceAgreementment(	ServiceChange source) {

		ServiceAgreementInfo target = new ServiceAgreementInfo();
		
		target.setTotalProrationCharge(source.getProratedCharge());
		
		//added services
		List<Service> addedServices = source.getAddedServices();
		
		Date earliestNewServiceEffectiveDate = null;
		
		for( Service service: addedServices ) {
			ServiceInfo serviceInfo = mapToServiceInfo(service);
			serviceInfo.setTransactionType("ADD");
			target.getServiceInfo().add( serviceInfo );

			if (earliestNewServiceEffectiveDate==null) {
				earliestNewServiceEffectiveDate = serviceInfo.getEffectiveDate(); 
			} else  if (serviceInfo.getEffectiveDate().before(earliestNewServiceEffectiveDate) ) {
				earliestNewServiceEffectiveDate = serviceInfo.getEffectiveDate();
			}
		}
		target.setFavoriteNumberInd(source.isContainNewCallingCircleService());
		
		//removed services
		List<Service> removedServices = source.getRemovedServices();
		for( Service service: removedServices ) {
			ServiceInfo serviceInfo = mapToServiceInfo(service);
			serviceInfo.setTransactionType("REMOVE");
			target.getServiceInfo().add( serviceInfo );
		}
		
		//favorite number list and expiration date
		List<FavoriteNumber> favoriteNumbers = source.getFavoriteNumber();
		if ( favoriteNumbers.size()>0 ) {

			Date earliestMyFaveDate = null;			
			
			FavoriteNumberInfo fnInfo = new FavoriteNumberInfo();
			for( FavoriteNumber favoriteNumber : favoriteNumbers ) {
				//add up myFave list from all SOCs
				fnInfo.getPhoneNumber().addAll( favoriteNumber.getPhoneNumberList() );
				
				//but use the earliest myFave effective date
				if (earliestMyFaveDate==null) {
					earliestMyFaveDate = favoriteNumber.getEffectiveDate();
				} 
				else if (favoriteNumber.getEffectiveDate().before(earliestMyFaveDate) ) {
					earliestMyFaveDate = favoriteNumber.getEffectiveDate();
				}
			}
			fnInfo.setEffectiveDate( earliestMyFaveDate );

			target.setFavoriteNumberInfo(fnInfo);
		}
		
		//always send the earliest effective date regardless if proratedCharge>0 or not
		if ( earliestNewServiceEffectiveDate!=null ) {
			target.setEarliestEffectiveDate(earliestNewServiceEffectiveDate);
		}
		
		if ( source.getProratedCharge()!=null) {
			target.setTotalProrationCharge( source.getProratedCharge() );
		}
		
		return target;
	}

	private ServiceInfo mapToServiceInfo(Service service) {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setEffectiveDate(service.getEffectiveDate());
		serviceInfo.setExpiryDate(service.getExpirationDate());
		serviceInfo.setServiceCost(service.getPrice() );
		serviceInfo.setServiceDescriptionEnglish(service.getEnglishDescription());
		serviceInfo.setServiceDescriptionFrench(service.getFrenchDescription());
		if (service.getProratedCharge()!=null ) serviceInfo.setProrationCharge( service.getProratedCharge());
		return serviceInfo;
	}

	private CancellationInfo mapToCancellationInfo(ServiceCancellation source) {
		CancellationInfo target = new CancellationInfo();
		target.setEffectiveDate(source.getEffectiveDate());
		target.getSubscriberPhoneNumber().addAll(source.getPhoneNumberList());
		return target;
	}

	private AccountInfo mapToAccountInfo(BillingAccount source ) {
		AccountInfo target = new AccountInfo();
		target.setBillingAccountNumber(source.getAccountNumber());
		target.setBillCycleCloseDate(source.getCurrentCycleCloseDate());
		
		return target;
	}
}
