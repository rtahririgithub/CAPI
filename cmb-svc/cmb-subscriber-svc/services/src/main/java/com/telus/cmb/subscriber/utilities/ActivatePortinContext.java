package com.telus.cmb.subscriber.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.cmb.subscriber.bo.ActivatePortinBo;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.utilities.activation.ActivationPortInUtilities;
import com.telus.cmb.subscriber.utilities.activation.ActivationUtilities;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.PricePlanInfo;

public class ActivatePortinContext extends BaseChangeContext<ActivationChangeInfo> {
	
	private static final Log logger = LogFactory.getLog(ActivatePortinContext.class);
	
	private ActivatePortinBo activatePortinBo;
	private ServiceRequestHeaderInfo requestHeader;
	private PortRequestInfo portRequest;
	private String portProcessCode = PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT; // Set this as the default port process type
	private boolean portedIn;
	private String portRequestId;
	private Exception softException;
	private boolean koodoPre2Post;
	private boolean isExistingUSIMPortIn;


	public ActivatePortinContext(ActivationChangeInfo changeInfo, PortRequestInfo portRequest, ServiceRequestHeaderInfo requestHeader, AuditInfo auditInfo) throws ApplicationException {
		super(changeInfo);
		this.portRequest = portRequest;
		this.requestHeader = requestHeader;
		setAuditInfo(auditInfo);
	}
	
	public Exception getSoftException() {
		return softException;
	}

	public void setSoftException(Exception softException) {
		this.softException = softException;
	}

	public PortRequestInfo getPortRequest() {
		return portRequest;
	}

	public void setPortRequest(PortRequestInfo portRequest) {
		this.portRequest = portRequest;
	}

	public String getPortRequestId() {
		return portRequestId;
	}

	public void setPortRequestId(String portRequestId) {
		this.portRequestId = portRequestId;
	}

	public ServiceRequestHeaderInfo getRequestHeader() {
		return requestHeader;
	}

	public String getPortProcessCode() {
		return portProcessCode;
	}

	public void setPortProcessCode(String portProcessCode) {
		this.portProcessCode = portProcessCode;
	}

	public boolean isPortedIn() {
		return portedIn;
	}

	public void setPortedIn(boolean portedIn) {
		this.portedIn = portedIn;
	}
	
	public boolean isKoodoPre2Post() {
		return koodoPre2Post;
	}

	public void setKoodoPre2Post(boolean koodoPre2Post) {
		this.koodoPre2Post = koodoPre2Post;
	}
	
	public boolean isExistingUSIMPortIn() {
		return isExistingUSIMPortIn;
	}

	public void setExistingUSIMPortIn(boolean isExistingUSIMPortIn) {
		this.isExistingUSIMPortIn = isExistingUSIMPortIn;
	}
	
	public SubscriberInfo getSubscriberInfo() {
		return this.getChangeInfo().getCurrentSubscriberInfo();
	}
	
	public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
		getChangeInfo().setCurrentSubscriberInfo(subscriberInfo);
		currentSubscriber = new SubscriberBo(subscriberInfo, this);
	}
	
	public void setPreviousSubscriberInfo(SubscriberInfo previousSubscriberInfo) {
		getChangeInfo().setPreviousSubscriberInfo(previousSubscriberInfo);
		previousSubscriber = new SubscriberBo(previousSubscriberInfo, this);
	}
	
	public EquipmentInfo getEquipmentInfo() {
		return this.getChangeInfo().getCurrentEquipmentInfo();
	}
	
	public AccountInfo getAccountInfo() {
		return this.getChangeInfo().getCurrentAccountInfo();
	}
	
	public SubscriberContractInfo getContractInfo() {
		return this.getChangeInfo().getCurrentContractInfo();
	}
	
	public void refreshSubscriberInfo() throws ApplicationException {
		SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriber(getChangeInfo().getSubscriberId());
		if(subscriberInfo != null) {
			setSubscriberInfo(subscriberInfo);
		}
	}

	@Override
	public void initialize() throws ApplicationException {

		activatePortinBo = new ActivatePortinBo(this);
		
		// Retrieve and enrich the equipmentInfo - this needs to be done first, as there are dependencies below
		EquipmentInfo equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getPrimaryEquipmentSerialNumber());
		equipmentInfo.setAssociatedHandsetIMEI(getChangeInfo().getAssociatedHandsetSerialNumber());
		getChangeInfo().setCurrentEquipmentInfo(equipmentInfo);
		
		// Enrich the subscriberContractInfo and set the port-in and port process attributes
		try {
			PricePlanInfo pricePlanInfo = getRefDataFacade().getPricePlan(
					"", 
					getChangeInfo().getCurrentContractInfo().getPricePlanCode(),
		    		ContractUtilities.translateEquipmentType(equipmentInfo, getProductEquipmentHelper()),
		    		getChangeInfo().getCurrentSubscriberInfo().getMarketProvince(),
		    		Character.toString(getCurrentAccount().getDelegate().getAccountType()),
		    		Character.toString(getCurrentAccount().getDelegate().getAccountSubType()),
		    		getCurrentAccount().getDelegate().getBrandId());
			getChangeInfo().getCurrentContractInfo().setPricePlanInfo(pricePlanInfo);
			setPortProcessType();
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}

		// Finally, enrich the subscriberInfo with additional contract and equipment info
		getChangeInfo().getCurrentSubscriberInfo().setPricePlan(getChangeInfo().getCurrentContractInfo().getPricePlanCode());
		getChangeInfo().getCurrentSubscriberInfo().setSerialNumber(getChangeInfo().getCurrentEquipmentInfo().getSerialNumber());
		getChangeInfo().getCurrentSubscriberInfo().setEquipmentType(getChangeInfo().getCurrentEquipmentInfo().getEquipmentType());
		getChangeInfo().getCurrentSubscriberInfo().setEquipment(equipmentInfo);
		
		// The super method will handle the current ContractBo, EquipmentBo and SubscriberBo initialization
		super.initialize();
		
		// Update all current account references
		getPortRequest().setAccount(getCurrentAccount().getDelegate());
		getChangeInfo().setCurrentAccountInfo(getCurrentAccount().getDelegate());
		
		// Oct 2019 - Allowing the customer to use existing USIM for MVNE->Koodo post and Telus -> Koodo post port in activations.
		// later phases will enable this shared usim functionality for MVNE->Telus and Koodo -> Telus port in activations, possibly for Feb 2020
		// we will configure the supporting shared sim incoming & outgoing brands in ldap that would allow us to control the shared usin portIn flows/project rollback easily
		
		// Validate the equipment and brand combination for activation
		PortInEligibilityInfo eligibilityInfo = (PortInEligibilityInfo) getChangeInfo().getPortInEligibility();
		if (!StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT,getPortProcessCode())) {
			if (ActivationPortInUtilities.isValidSharedUSIMPortInRequest(eligibilityInfo)) {
				// Validate existing USIM for MVNE -> Koodo post/telus port in's
				if (StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_MVNE_PORT,getPortProcessCode()) && ActivationPortInUtilities.isExistingMVNEUSIM(eligibilityInfo.getOutgoingBrandId(),equipmentInfo)) {
					setExistingUSIMPortIn(true);
					ActivationPortInUtilities.validateExistingMVNEUSIM(getCurrentEquipment().getDelegate());
					// Validate existing USIM for interBrand port in's ( Telus <-> Koodo post )
				} else if (StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_BRAND_PORT,getPortProcessCode()) && ActivationPortInUtilities.isExistingInterBrandUSIM(eligibilityInfo.getOutgoingBrandId(),equipmentInfo)) {
					setExistingUSIMPortIn(true);
					ActivationPortInUtilities.validateExistingInterBrandUSIM(getCurrentEquipment().getDelegate());
				} else{
					logger.debug(" [ " + equipmentInfo.getSerialNumber()+ " ]  is not valid shared USIM");
				}
			} else{
				logger.debug("unsupported brandId for shared usim portIn activation , incoming brandId = [ "+eligibilityInfo.getIncomingBrandId()+" ], outgoing brandId = [ "+eligibilityInfo.getOutgoingBrandId()+" ]");
			}
		}
		
		logger.debug("isExistingUSIMPortIn =  [ " + isExistingUSIMPortIn +" ] ");

		if(!isExistingUSIMPortIn()){
			// Execute validation for all other BAU flows where customer uses new USIM or shared USIM not supported.
			ActivationUtilities.activationValidate(getCurrentSubscriber().getBrandId(), getCurrentEquipment().getDelegate());
		}
	}
	
	
	
	public ActivationChangeInfo activatePortin(boolean validateOnly) throws ApplicationException {
		
		logger.debug("activatePortin(validateOnly=" + validateOnly + ")");
		try {
			if (StringUtils.isBlank(getCurrentSubscriber().getPhoneNumber())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.NO_PHONE_NUMBER_RESERVED, "No phone number has been reserved.");
			}	
			if (getChangeInfo().isDealerHasDeposit() && StringUtils.isBlank(getCurrentSubscriber().getDealerCode())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Invalid dealer code; dealerHasDeposit == true, but dealerCode is not set.", "");
			}
			
			EquipmentInfo primaryEquipment =  getChangeInfo().getCurrentEquipmentInfo();
			if (!(EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(primaryEquipment.getSerialNumber()) || EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(primaryEquipment.getSerialNumber()))) {
				if(!primaryEquipment.isUSIMCard()){
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Invalid equipment. Primary equipment is not USIM + [ "+getChangeInfo().getCurrentEquipmentInfo().getSerialNumber() +" ]", "");
				}
			}
			
			if (!validateOnly) {
				activatePortinBo.activatePortin();
			}
			
			return getChangeInfo();
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	private void setPortProcessType() throws TelusAPIException, ApplicationException {

		// Determine port process type
		BrandInfo[] brands = getRefDataFacade().getBrands();
		PortInEligibility portInEligibility = getChangeInfo().getPortInEligibility();
		if (portInEligibility != null) {
			String portProcessCode = portInEligibility.isPortInFromMVNE() ? PortInEligibilityInfo.PORT_PROCESS_INTER_MVNE_PORT
					: (ReferenceDataManager.Helper.validateBrandId(portInEligibility.getIncomingBrandId(), brands)
							&& ReferenceDataManager.Helper.validateBrandId(portInEligibility.getOutgoingBrandId(), brands)) ? PortInEligibilityInfo.PORT_PROCESS_INTER_BRAND_PORT
									: PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT;
			setPortProcessCode(portProcessCode);
			setPortedIn(true);
		}
	}
	
}