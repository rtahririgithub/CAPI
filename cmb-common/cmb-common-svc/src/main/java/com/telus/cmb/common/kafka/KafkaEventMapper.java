package com.telus.cmb.common.kafka;

import java.io.StringWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BasePrepaidAccountInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.evnthdl.publisher.domain.Characteristic;
import com.telus.evnthdl.publisher.domain.Event;
import com.telus.evnthdl.publisher.domain.Object;

public class KafkaEventMapper{
	
	private String sourceId;
	
	private String topic;
	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String createBillCycleUpdateEvent(KafkaEventType eventType, short billCycle, int billingAccountNumber) throws Exception{
		Event event = createEvent();
		Object object = new Object();

		object.setName(String.valueOf(eventType));
		
		List<Characteristic> characteristics = new ArrayList<Characteristic>();
		characteristics.add(createCharacteristic("billCycle", String.valueOf(billCycle)));
		characteristics.add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		
		object.setCharacteristic(characteristics);
		event.getObject().add(object);
		
		return eventMarshaller(event);
	}
	
	public String createAccountCreatedEvent(KafkaEventType eventType, AccountInfo source) throws Exception{
		Event event = createEvent();
		Object object = new Object();
		
		if(eventType==KafkaEventType.BILLING_ACCOUNT_UPDATED){
			object.setName(String.valueOf(KafkaEventType.BILLING_ACCOUNT_UPDATED.getAllUpdateEventTypes()));
		}
		else{
			object.setName(String.valueOf(eventType));
		}
		
		List<Characteristic> characteristics = new ArrayList<Characteristic>();
		
		if(eventType == KafkaEventType.BILLING_ACCOUNT_ALL){
			characteristics.add(createCharacteristic("status", "T"));
		}
		else
		{
			characteristics.add(createCharacteristic("status", String.valueOf(source.getStatus())));
		}
		
		characteristics.add(createCharacteristic("statusDate", String.valueOf(source.getStatusDate())));
		characteristics.add(createCharacteristic("billCycleCloseDay", String.valueOf(source.getBillCycleCloseDay())));
		characteristics.add(createCharacteristic("customerId", String.valueOf(source.getCustomerId())));
		characteristics.add(createCharacteristic("createDate", String.valueOf(source.getCreateDate())));
		characteristics.add(createCharacteristic("billingAccountNumber", String.valueOf(source.getBanId())));
		characteristics.add(createCharacteristic("billingMasterSourceId", "130"));
		characteristics.add(createCharacteristic("billCycle", String.valueOf(source.getBillCycle())));
		characteristics.add(createCharacteristic("nextBillCycle", String.valueOf(source.getNextBillCycle())));
		characteristics.add(createCharacteristic("accountType", String.valueOf(source.getAccountType())));
		characteristics.add(createCharacteristic("accountSubTypeCode", String.valueOf(source.getAccountSubType())));
		characteristics.add(createCharacteristic("startServiceDate", String.valueOf(source.getStartServiceDate())));
		characteristics.add(createCharacteristic("email", source.getEmail()));
		characteristics.add(createCharacteristic("language", source.getLanguage()));
		characteristics.add(createCharacteristic("brandId", String.valueOf(source.getBrandId())));
		characteristics.add(createCharacteristic("homePhone", source.getHomePhone()));
		characteristics.add(createCharacteristic("businessPhone", source.getBusinessPhone()));
		characteristics.add(createCharacteristic("businessPhoneExtension", source.getBusinessPhoneExtension()));
		characteristics.add(createCharacteristic("contactPhone", source.getContactPhone()));
		characteristics.add(createCharacteristic("contactPhoneExtension", source.getContactPhoneExtension()));
		characteristics.add(createCharacteristic("contactFax", source.getContactFax()));
		characteristics.add(createCharacteristic("otherPhoneType", source.getOtherPhoneType()));
		characteristics.add(createCharacteristic("otherPhone", source.getOtherPhone()));
		characteristics.add(createCharacteristic("otherPhoneExtension", source.getOtherPhoneExtension()));
		
		if(source.getContactName0()!=null){
			Object contactNameObj = new Object();
			contactNameObj.setSpecName("contactName");
			contactNameObj.getCharacteristic().add(createCharacteristic("title", source.getContactName0().getTitle()));
			contactNameObj.getCharacteristic().add(createCharacteristic("firstName", source.getContactName0().getFirstName()));
			contactNameObj.getCharacteristic().add(createCharacteristic("middleInitial", source.getContactName0().getMiddleInitial()));
			contactNameObj.getCharacteristic().add(createCharacteristic("lastName", source.getContactName0().getLastName()));
			contactNameObj.getCharacteristic().add(createCharacteristic("generation", source.getContactName0().getGeneration()));
			contactNameObj.getCharacteristic().add(createCharacteristic("additionalLine", source.getContactName0().getAdditionalLine()));
			contactNameObj.getCharacteristic().add(createCharacteristic("nameFormat", source.getContactName0().getNameFormat()));
			object.getObject().add(contactNameObj);
		}
		
		Object addressObj = new Object();
		addressObj.setSpecName("address");
		//business requirement to convert AddressInfo to EnterpriseAddress
		EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo();
		enterpriseAddressInfo.translateAddress(source.getAddress());
		
		addressObj.getCharacteristic().add(createCharacteristic("addressTypeCode", enterpriseAddressInfo.getAddressTypeCode()));
		
		if(enterpriseAddressInfo.getAdditionalAddressInformation()!= null && enterpriseAddressInfo.getAdditionalAddressInformation().size()>0){
			
			for(int i=0; i<enterpriseAddressInfo.getAdditionalAddressInformation().size(); i++){
				addressObj.getCharacteristic().add(createCharacteristic("additionalAddressInfo", enterpriseAddressInfo.getAdditionalAddressInformation().get(i).toString()));
			}
		}
		else{
			addressObj.getCharacteristic().add(createCharacteristic("additionalAddressInfo", new String("")));
		}
		
		addressObj.getCharacteristic().add(createCharacteristic("ruralRouteTypeCode", enterpriseAddressInfo.getRuralRouteTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("careOf", enterpriseAddressInfo.getCareOf()));
		addressObj.getCharacteristic().add(createCharacteristic("countryCode", enterpriseAddressInfo.getCountryCode()));
		addressObj.getCharacteristic().add(createCharacteristic("municipalityName", enterpriseAddressInfo.getMunicipalityName()));
		addressObj.getCharacteristic().add(createCharacteristic("postOfficeBoxNumber", enterpriseAddressInfo.getPostOfficeBoxNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("postalZipCode", enterpriseAddressInfo.getPostalZipCode()));
		addressObj.getCharacteristic().add(createCharacteristic("province", enterpriseAddressInfo.getProvinceStateCode()));
		addressObj.getCharacteristic().add(createCharacteristic("streetDirectionCode", enterpriseAddressInfo.getStreetDirectionCode()));
		addressObj.getCharacteristic().add(createCharacteristic("streetName", enterpriseAddressInfo.getStreetName()));
		addressObj.getCharacteristic().add(createCharacteristic("streetTypeCode", enterpriseAddressInfo.getStreetTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("unitNumber", enterpriseAddressInfo.getUnitNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("unitTypeCode", enterpriseAddressInfo.getUnitTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("civicNumber", enterpriseAddressInfo.getCivicNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("civicNumberSuffix", enterpriseAddressInfo.getCivicNumberSuffix()));
		addressObj.getCharacteristic().add(createCharacteristic("ruralRouteNumber", enterpriseAddressInfo.getRuralRouteNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("stationName", enterpriseAddressInfo.getStationName()));
		addressObj.getCharacteristic().add(createCharacteristic("stationQualifier", enterpriseAddressInfo.getStationQualifier()));
		addressObj.getCharacteristic().add(createCharacteristic("stationTypeCode", enterpriseAddressInfo.getStationTypeCode()));
		object.getObject().add(addressObj);
		
		ConsumerNameInfo billingNameSource = null;
		String billingAccountType = null;
		if (source instanceof PostpaidConsumerAccountInfo) {
			billingAccountType = "postpaid";
			billingNameSource = ((PostpaidConsumerAccountInfo) source).getName0();
		}else if (source instanceof BasePrepaidAccountInfo) {
			billingAccountType = "prepaid";
			billingNameSource = ((BasePrepaidAccountInfo) source).getName0();
		}else {
			billingNameSource = source.getContactName0();
		}
		
		if(billingNameSource!=null){
			Object billingNameObj = new Object();
			billingNameObj.setSpecType(billingAccountType);
			billingNameObj.setSpecName("billingName");
			billingNameObj.getCharacteristic().add(createCharacteristic("title", billingNameSource.getTitle()));
			billingNameObj.getCharacteristic().add(createCharacteristic("firstName", billingNameSource.getFirstName()));
			billingNameObj.getCharacteristic().add(createCharacteristic("middleInitial", billingNameSource.getMiddleInitial()));
			billingNameObj.getCharacteristic().add(createCharacteristic("lastName", billingNameSource.getLastName()));
			billingNameObj.getCharacteristic().add(createCharacteristic("generation", billingNameSource.getGeneration()));
			billingNameObj.getCharacteristic().add(createCharacteristic("additionalLine", billingNameSource.getAdditionalLine()));
			billingNameObj.getCharacteristic().add(createCharacteristic("nameFormat", billingNameSource.getNameFormat()));
			object.getObject().add(billingNameObj);
		}
		
		object.setCharacteristic(characteristics);
		event.getObject().add(object);
		
		return eventMarshaller(event);
	}
	
	public String createEmailUpdateEvent(KafkaEventType eventType, String email, int billingAccountNumber) throws Exception{
		Event event = createEvent();
		Object object = new Object();
		
		object.setName(String.valueOf(eventType));
		
		object.getCharacteristic().add(createCharacteristic("email", email));
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		
		event.getObject().add(object);
		
		return eventMarshaller(event);	
	}
	
	public String createAuthorizedContactsUpdateEvent(KafkaEventType eventType, ConsumerNameInfo[] authorizedContacts, int billingAccountNumber) throws Exception{
		Event event = createEvent();
		Object object = new Object();
		
		object.setName(String.valueOf(eventType));
		
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		
		int i = 0;
		for (ConsumerNameInfo contactSource : authorizedContacts){
			i++;
			Object contactName = new Object();
			contactName.setSpecName("authorizedContactName_" + i);
			contactName.getCharacteristic().add(createCharacteristic("title", contactSource.getTitle()));
			contactName.getCharacteristic().add(createCharacteristic("firstName", contactSource.getFirstName()));
			contactName.getCharacteristic().add(createCharacteristic("middleInitial", contactSource.getMiddleInitial()));
			contactName.getCharacteristic().add(createCharacteristic("lastName", contactSource.getLastName()));
			contactName.getCharacteristic().add(createCharacteristic("generation", contactSource.getGeneration()));
			contactName.getCharacteristic().add(createCharacteristic("additionalLine", contactSource.getAdditionalLine()));
			contactName.getCharacteristic().add(createCharacteristic("nameFormat", contactSource.getNameFormat()));
			
			object.getObject().add(contactName);
		}
		
		event.getObject().add(object);
		
		return eventMarshaller(event);	
		
	}
	
	public String createAccountCancelStatusUpdateEvent(KafkaEventType eventType, Date activityDate, String activityReasonCode, String depositReturnMethod,
			String waiveReason, String userMemoText, boolean isBrandPortActivity, boolean isPortActivity, int billingAccountNumber) throws Exception {
		
		Event event = createEvent();
		Object object = new Object();
		
		object.setName(String.valueOf(eventType));
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		object.getCharacteristic().add(createCharacteristic("activityDate", String.valueOf(activityDate)));
		object.getCharacteristic().add(createCharacteristic("activityReasonCode", activityReasonCode));
		object.getCharacteristic().add(createCharacteristic("depositReturnMethod", String.valueOf(depositReturnMethod)));
		object.getCharacteristic().add(createCharacteristic("waiveReason", waiveReason));
		object.getCharacteristic().add(createCharacteristic("userMemoText", userMemoText));
		object.getCharacteristic().add(createCharacteristic("isBrandPortActivity", String.valueOf(isBrandPortActivity)));
		object.getCharacteristic().add(createCharacteristic("isPortActivity", String.valueOf(isPortActivity)));
		
		event.getObject().add(object);
		
		return eventMarshaller(event);
	}
	
	//suspendAccount
	public String createAccountSuspendStatusUpdateEvent(KafkaEventType eventType, Date activityDate, String activityReasonCode, String userMemoText, String portOutInd, int billingAccountNumber) throws Exception {
		Event event = createEvent();
		Object object = new Object();
		object.setName(String.valueOf(eventType));
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		object.getCharacteristic().add(createCharacteristic("activityDate", String.valueOf(activityDate)));
		object.getCharacteristic().add(createCharacteristic("activityReasonCode", activityReasonCode));
		object.getCharacteristic().add(createCharacteristic("userMemoText", userMemoText));
		object.getCharacteristic().add(createCharacteristic("portOutInd", portOutInd));
		
		event.getObject().add(object);
		
		return eventMarshaller(event);
	}
	
	
	public String createBillingInfoUpdateEvent(KafkaEventType eventType, BillingPropertyInfo source, int billingAccountNumber) throws Exception{
		Event event = createEvent();
		Object object = new Object();
		
		if(source.getName()==null && source.getAddress()!=null){
			object.setName(String.valueOf(KafkaEventType.BILLING_ADDRESS));
		}
		else if(source.getName()!=null && source.getAddress()==null){
			object.setName(String.valueOf(KafkaEventType.BILLING_NAME));
		}
		else{
			object.setName(KafkaEventType.BILLING_INFO.getAllBillingInfoTypes());
		}
		
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		
		if(source.getName()!=null)
		{
			Object billingNameObj = new Object();
			billingNameObj.setSpecName("billingName");
			billingNameObj.getCharacteristic().add(createCharacteristic("title", source.getName().getTitle()));
			billingNameObj.getCharacteristic().add(createCharacteristic("firstName", source.getName().getFirstName()));
			billingNameObj.getCharacteristic().add(createCharacteristic("middleInitial", source.getName().getMiddleInitial()));
			billingNameObj.getCharacteristic().add(createCharacteristic("lastName", source.getName().getLastName()));
			billingNameObj.getCharacteristic().add(createCharacteristic("generation", source.getName().getGeneration()));
			billingNameObj.getCharacteristic().add(createCharacteristic("additionalLine", source.getName().getAdditionalLine()));
			billingNameObj.getCharacteristic().add(createCharacteristic("nameFormat", source.getName().getNameFormat()));
			object.getObject().add(billingNameObj);
		}

		Object addressObj = new Object();
		addressObj.setSpecName("address");
		//business requirement to convert AddressInfo to EnterpriseAddress
		EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo();
		enterpriseAddressInfo.translateAddress(source.getAddress());
		
		addressObj.getCharacteristic().add(createCharacteristic("addressTypeCode", enterpriseAddressInfo.getAddressTypeCode()));
		
		if(enterpriseAddressInfo.getAdditionalAddressInformation()!= null && enterpriseAddressInfo.getAdditionalAddressInformation().size()>0){
			
			for(int i=0; i<enterpriseAddressInfo.getAdditionalAddressInformation().size(); i++){
				addressObj.getCharacteristic().add(createCharacteristic("additionalAddressInfo", enterpriseAddressInfo.getAdditionalAddressInformation().get(i).toString()));
			}
		}
		
		addressObj.getCharacteristic().add(createCharacteristic("ruralRouteTypeCode", enterpriseAddressInfo.getRuralRouteTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("careOf", enterpriseAddressInfo.getCareOf()));
		addressObj.getCharacteristic().add(createCharacteristic("countryCode", enterpriseAddressInfo.getCountryCode()));
		addressObj.getCharacteristic().add(createCharacteristic("municipalityName", enterpriseAddressInfo.getMunicipalityName()));
		addressObj.getCharacteristic().add(createCharacteristic("postOfficeBoxNumber", enterpriseAddressInfo.getPostOfficeBoxNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("postalZipCode", enterpriseAddressInfo.getPostalZipCode()));
		addressObj.getCharacteristic().add(createCharacteristic("province", enterpriseAddressInfo.getProvinceStateCode()));
		addressObj.getCharacteristic().add(createCharacteristic("streetDirectionCode", enterpriseAddressInfo.getStreetDirectionCode()));
		addressObj.getCharacteristic().add(createCharacteristic("streetName", enterpriseAddressInfo.getStreetName()));
		addressObj.getCharacteristic().add(createCharacteristic("streetTypeCode", enterpriseAddressInfo.getStreetTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("unitNumber", enterpriseAddressInfo.getUnitNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("unitTypeCode", enterpriseAddressInfo.getUnitTypeCode()));
		addressObj.getCharacteristic().add(createCharacteristic("civicNumber", enterpriseAddressInfo.getCivicNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("civicNumberSuffix", enterpriseAddressInfo.getCivicNumberSuffix()));
		addressObj.getCharacteristic().add(createCharacteristic("ruralRouteNumber", enterpriseAddressInfo.getRuralRouteNumber()));
		addressObj.getCharacteristic().add(createCharacteristic("stationName", enterpriseAddressInfo.getStationName()));
		addressObj.getCharacteristic().add(createCharacteristic("stationQualifier", enterpriseAddressInfo.getStationQualifier()));
		addressObj.getCharacteristic().add(createCharacteristic("stationTypeCode", enterpriseAddressInfo.getStationTypeCode()));

		object.getObject().add(addressObj);
		
		event.getObject().add(object);
		
		return eventMarshaller(event);
	}
	
	public String createContactInfoUpdateEvent(KafkaEventType eventType, ContactPropertyInfo source, int billingAccountNumber) throws Exception{
		Event event = createEvent();
		Object object = new Object();
		
		if(source.getName()==null && source!=null){
			object.setName(String.valueOf(KafkaEventType.CONTACT_NUMBERS));
		}
		else{
			object.setName(KafkaEventType.CONTACT_INFO.getAllContactInfoTypes());
		}
		
		object.getCharacteristic().add(createCharacteristic("billingAccountNumber", String.valueOf(billingAccountNumber)));
		object.getCharacteristic().add(createCharacteristic("otherPhoneType", source.getOtherPhoneType()));
		object.getCharacteristic().add(createCharacteristic("otherPhone", source.getOtherPhoneNumber()));
		object.getCharacteristic().add(createCharacteristic("otherPhoneExtension", source.getOtherPhoneExtension()));
		object.getCharacteristic().add(createCharacteristic("homePhone", source.getHomePhoneNumber()));
		object.getCharacteristic().add(createCharacteristic("businessPhone", source.getBusinessPhoneNumber()));
		object.getCharacteristic().add(createCharacteristic("businessPhoneExtension", source.getBusinessPhoneExtension()));
		object.getCharacteristic().add(createCharacteristic("contactPhone", source.getContactPhoneNumber()));
		object.getCharacteristic().add(createCharacteristic("contactPhoneExtension", source.getContactPhoneExtension()));
		object.getCharacteristic().add(createCharacteristic("contactFax", source.getContactFax()));
		

		if(source.getName()!=null){
			Object contactNameObj = new Object();
			contactNameObj.setSpecName("contactName");
			contactNameObj.getCharacteristic().add(createCharacteristic("title", source.getName().getTitle()));
			contactNameObj.getCharacteristic().add(createCharacteristic("firstName", source.getName().getFirstName()));
			contactNameObj.getCharacteristic().add(createCharacteristic("middleInitial", source.getName().getMiddleInitial()));
			contactNameObj.getCharacteristic().add(createCharacteristic("lastName", source.getName().getLastName()));
			contactNameObj.getCharacteristic().add(createCharacteristic("generation", source.getName().getGeneration()));
			contactNameObj.getCharacteristic().add(createCharacteristic("additionalLine", source.getName().getAdditionalLine()));
			contactNameObj.getCharacteristic().add(createCharacteristic("nameFormat", source.getName().getNameFormat()));
			object.getObject().add(contactNameObj);
		}
		
		event.getObject().add(object);
		return eventMarshaller(event);
	}
	
	private Event createEvent() throws Exception {
		Event event = new Event();

		event.setSource(sourceId);
		event.setSpecName(topic);
		event.setTimestamp(getGregorianTimestamp());
		
		return event;
	}
	
	public Characteristic createCharacteristic(String name, String value){
		Characteristic characteristic = new Characteristic();
		characteristic.setName(name);
		characteristic.getValue().add(value);
		return characteristic;
	}
	
	private String eventMarshaller(Event event) throws Exception{
		Marshaller jaxbMarshaller = null;

		JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
		jaxbMarshaller = jaxbContext.createMarshaller();
			
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		QName qName = new QName("com.telus.evnthdl.publisher.domain", "Event");
		JAXBElement<Event> root = new JAXBElement<Event>(qName, Event.class, event);
		
		StringWriter outputWriter = new StringWriter();
		
		jaxbMarshaller.marshal(root, outputWriter);

		
		return outputWriter.toString();
	}
	
	private static XMLGregorianCalendar getGregorianTimestamp() throws Exception {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar currentTimestamp = null;
		
		currentTimestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

		return currentTimestamp;
	}
}
