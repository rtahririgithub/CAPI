package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.equipment.Warranty;
import com.telus.api.reference.NetworkType;
import com.telus.cmb.common.ejb.AbstractLifecycleFacade;
import com.telus.cmb.common.ejb.LdapTestPoint;
import com.telus.cmb.common.eligibility.EsimDeviceSwapEligibilityUtil;
import com.telus.cmb.common.jms.JmsSupport;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.domain.SemsEquipmentInfo;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentActivationSupportSvcDao;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentInfoSvcDao;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao;
import com.telus.cmb.productequipment.lifecyclefacade.dao.ProductDeviceServiceDao;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint;
import com.telus.cmb.productequipment.utilities.AppConfiguration;
import com.telus.cmb.productequipment.utilities.EsimDeviceSwapValidationResult;
import com.telus.eas.equipment.info.CellularDigitalEquipmentUpgradeInfo;
import com.telus.eas.equipment.info.DeviceSwapValidateInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.framework.config.ConfigContext;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentactivationsupportservicerequestresponsetypes_v1.ReserveSimProfileResponse;
import com.telusmobility.nrt.NRTException;
import com.telusmobility.nrt.NrtEligibilityManagerEJBRemote;
import com.telusmobility.nrt.PromotionSummaryBean;
import com.telusmobility.p3ms.dvo.CallerDVO;
import com.telusmobility.p3ms.ejb.facade.ProductFacadeEJB;
import com.telusmobility.p3ms.exception.P3MSException;

@Stateless(name = "ProductEquipmentLifecycleFacade", mappedName = "ProductEquipmentLifecycleFacade")
@Interceptors({ ProductEquipmentLifecycleFacadeSvcInvocationInterceptor.class, SpringBeanAutowiringInterceptor.class })
@Remote({ ProductEquipmentLifecycleFacade.class, ProductEquipmentLifecycleFacadeTestPoint.class, LdapTestPoint.class })
@RemoteHome(ProductEquipmentLifecycleFacadeHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ProductEquipmentLifecycleFacadeImpl extends AbstractLifecycleFacade implements ProductEquipmentLifecycleFacade, ProductEquipmentLifecycleFacadeTestPoint {
	private static final String DEVICE_TYPE_TRACKER = "TRACKER";
	
	private static final Logger LOGGER = Logger.getLogger(ProductEquipmentLifecycleFacadeImpl.class);

	private ProductFacadeEJB p3msProductFacade;
	private NrtEligibilityManagerEJBRemote nrtEligibilityManager;

	private static final String p3msJNDIName = "p3ms/ejb/stateless/ProductFacadeEJB";
	private static final String nrtJNDIName = "nrt/ejb/stateless/NrtEligibilityManagerEJB";

	@EJB
	private ProductEquipmentHelper productEquipmentHelper;
	
	@Autowired
	private EquipmentInfoSvcDao equipmentInfoSvcDao;

	@Autowired
	private EquipmentLifecycleManagementSvcDao equipmentLifecycleManagementSvcDao;

	@Autowired
	private EquipmentActivationSupportSvcDao equipmentActivationSupportSvcDao;
	
	@Autowired
	private ProductDeviceServiceDao productDeviceServiceDao;

	@Autowired
	private JmsSupport queueSender;

	@Override
	public CellularDigitalEquipmentUpgradeInfo[] getCellularDigitalEquipmentUpgrades(EquipmentInfo pEquipmentInfo) throws ApplicationException {

		String methodName = "getCellularDigitalEquipmentUpgrades";
		CellularDigitalEquipmentUpgradeInfo[] cellularDigitalEquipmentUpgrades = new CellularDigitalEquipmentUpgradeInfo[0];

		if (!pEquipmentInfo.isCellularDigital()) {
			LOGGER.error("id=EQU30017; Upgrade information only available for Celular Digital equipment");
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_EQUIPMENT_TYPE, EquipmentErrorMessages.INVALID_EQUIPMENT_TYPE_EN, "");
		}

		// Call NRT ELigibility Manager EJB to get eligible promotions
		LOGGER.info("Calling getHandsetEligibilityForNrt() for serialNo=[" + pEquipmentInfo.getSerialNumber() + "]...");
		PromotionSummaryBean[] eligiblePromotions;
		try {
			eligiblePromotions = getNrtEligibilityManager().getHandsetEligibilityForNrt(pEquipmentInfo.getSerialNumber(), null);
			cellularDigitalEquipmentUpgrades = new CellularDigitalEquipmentUpgradeInfo[eligiblePromotions.length];
			for (int i = 0; i < eligiblePromotions.length; i++) {
				cellularDigitalEquipmentUpgrades[i] = new CellularDigitalEquipmentUpgradeInfo();
				cellularDigitalEquipmentUpgrades[i].setPromotionDescription(eligiblePromotions[i].getDescriptionEn());
				cellularDigitalEquipmentUpgrades[i].setPromotionDescriptionFrench(eligiblePromotions[i].getDescriptionFr());
				cellularDigitalEquipmentUpgrades[i].setPRLCode(eligiblePromotions[i].getPrlDescription());
				cellularDigitalEquipmentUpgrades[i].setBrowserVersion(eligiblePromotions[i].getReflashBrowserVersion());
				cellularDigitalEquipmentUpgrades[i].setFirmwareVersion(eligiblePromotions[i].getReflashSoftwareVersion());
				cellularDigitalEquipmentUpgrades[i].setStartDate(eligiblePromotions[i].getStartDate());
				cellularDigitalEquipmentUpgrades[i].setOtaspAvailable(eligiblePromotions[i].isOTASPAvailable());
			}
			
		} catch (NRTException ne) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, "NRT - " + methodName + " failed with NRTException (Message:" + ne.getMessage() + ")", "");
		} catch (ConnectException ce) {
			resetNrtEligibilityManager();
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.NRT_EJB_CONNECTION_EXCEPTION,
					ce.getMessage() == null ? "ConnectException occurred (" + ce.toString() + ") - see stack trace for details" : ce.getMessage(), "", ce);
		} catch (RemoteException re) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, re.getMessage() == null ? "RemoteException occurred(" + re.toString() + ") - see stack trace for details" : re.getMessage(), "",
					re);
		}

		return cellularDigitalEquipmentUpgrades;
	}

	private ProductFacadeEJB getP3MSProductFacade() {
		if (p3msProductFacade == null) {
			p3msProductFacade = EJBUtil.getStatelessProxy(ProductFacadeEJB.class, p3msJNDIName, AppConfiguration.getP3MSproductFacadeEjbUrl());
		}
		return p3msProductFacade;
	}

	private NrtEligibilityManagerEJBRemote getNrtEligibilityManager() {
		if (nrtEligibilityManager == null) {
			nrtEligibilityManager = EJBUtil.getStatelessProxy(NrtEligibilityManagerEJBRemote.class, nrtJNDIName, AppConfiguration.getNrtEligibilityManagerEJBUrl());
		}
		return nrtEligibilityManager;
	}

	private void resetNrtEligibilityManager() {
		nrtEligibilityManager = null;
	}

	@Override
	public void assignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.assignEquipmentToPhoneNumber(phoneNumber, serialNumber, associatedHandsetIMEI);
	}

	/**
	 * Returns false if one of the values in DPIneligibleVendors matches the
	 * manufacturer(vendor) ID
	 * 
	 * @deprecated
	 */
	@Override
	public boolean isDeviceProtectionEligible(EquipmentInfo equipmentInfo) {
		
		// As per Rich Media R4 design, 2010 May release, the following logic is removed in favor of new API isApple()
		/*
		 * String[] vendors = getDPIneligibleVendors(); for (int i = 0 ; i <
		 * vendors.length; i++) { if (vendors[i].equals(getVendorNo())) { return
		 * false; } } return true;
		 */

		boolean result = true;
		try {
			// Apple device is not eligible for Device protection
			if (isApple(equipmentInfo.getProductCode())) {
				result = false;
			}
		} catch (ApplicationException ae) {
			// log it
			LOGGER.debug("check isApple for sn(" + equipmentInfo.getSerialNumber() + ") failed with error below, the exceptin is ignored.");
			LOGGER.debug(ae);

			// in case we are not able to determine if this equipment is Apple,
			// then we check compare vendorNo with default Apple vendor no
			if ("10118119".equals(equipmentInfo.getVendorNo())) {
				result = false;
			}
		}
		
		return result;
	}

	/*
	 * @deprecated consumers should go to P3MS directly - will remove in July
	 * 2017
	 */
	@Override
	public boolean isApple(String productCode) throws ApplicationException {
		
		String methodName = "isApple";
		try {
			ProductFacadeEJB facadeejb = getP3MSProductFacade();
			return facadeejb.isApple(productCode, new CallerDVO("ClientAPI"));
		} catch (RemoteException re) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, re.getMessage() == null ? "RemoteException occurred(" + re.toString() + ") - see stack trace for details" : re.getMessage(), "", re);
		} catch (P3MSException pe) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, "P3MS - " + methodName + " failed with P3MSException (Message:" + pe.getMessage() + ")", "");
		}
	}

	@Override
	public void changePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.changePhoneNumber(serialNumber, newPhoneNumber);
	}

	@Override
	public void swapEquipmentForPhoneNumber(String phoneNumber, String oldUsimId, String oldAssociatedHandsetIMEI, String oldNetworkType, String newUsimId, String newAssociatedHandsetIMEI,
			String newNetworkType) throws ApplicationException {

		if (phoneNumber == null || phoneNumber.trim().equals("") || oldUsimId == null || oldUsimId.trim().equals("") || newUsimId == null || newUsimId.trim().equals("") || oldNetworkType == null
				|| oldNetworkType.trim().equals("") || newNetworkType == null || newNetworkType.trim().equals(""))
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid parameter !", "");

		if (!oldNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA) && newNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA)) {
			// cross-network swap to HSPA
			asyncAssignEquipmentToPhoneNumber(phoneNumber, newUsimId, newAssociatedHandsetIMEI);

		} else if (oldNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA) && newNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA)) {
			// HSPA - HSPA swap
			asyncSwapHSPAOnlyEquipmentForPhoneNumber(phoneNumber, oldUsimId, newUsimId, oldAssociatedHandsetIMEI, newAssociatedHandsetIMEI);

		} else if (oldNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA) && !newNetworkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA)) {
			// cross-network swap from HSPA
			asyncDisassociateEquipmentFromPhoneNumber(phoneNumber, oldUsimId);

		} else {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.UNSUPPORTED_NETWORK_SWAP, "Unsupported network swap !", "");
		}

	}

	@Override
	public void markEquipmentStolen(String usimId, String equipmentGroup) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.markEquipmentStolen(usimId, equipmentGroup);
	}

	@Override
	public void markEquipmentLost(String usimId, String equipmentGroup) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.markEquipmentLost(usimId, equipmentGroup);
	}

	@Override
	public void markEquipmentFound(String usimId, String equipmentGroup) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.markEquipmentFound(usimId, equipmentGroup);
	}

	@Override
	public void approveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.approveReservedEquipmentForPhoneNumber(phoneNumber, serialNumber, associatedHandsetIMEI);
	}

	@Override
	public void releaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.releaseReservedEquipmentForPhoneNumber(phoneNumber, serialNumber, associatedHandsetIMEI);
	}

	@Override
	public void disassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException {
		equipmentLifecycleManagementSvcDao.disassociateEquipmentFromPhoneNumber(phoneNumber, usimId);
	}

	@Override
	public Warranty getWarrantySummary(String serialNumber, String equipmentGroup) throws ApplicationException {

		if (serialNumber == null || serialNumber.trim().equals(""))
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid parameter !", "");

		WarrantyInfo warrantyInfo = new WarrantyInfo();
		if (equipmentGroup != null && equipmentGroup.toUpperCase().startsWith("ANA")) {
			warrantyInfo.setMessage("This handset requires a proof of purchase for warranty.");
		} else {
			warrantyInfo = equipmentInfoSvcDao.getWarrantySummary(serialNumber, equipmentGroup);
		}

		return warrantyInfo;
	}

	@Override
	public void swapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber, String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI)
			throws ApplicationException {
		equipmentLifecycleManagementSvcDao.swapHSPAOnlyEquipmentForPhoneNumber(phoneNumber, oldSerialNumber, newSerialNumber, oldAssociatedHandsetIMEI, newAssociatedHandsetIMEI);
	}

	@Override
	public UsimProfileInfo getProductUSIMProfile(long usimProductId) throws ApplicationException {
		return productDeviceServiceDao.getProductUsimProfile(usimProductId);
	}
	
	@Override
	public ProductInfo getProduct(String productCode) throws ApplicationException {
		return productDeviceServiceDao.getProduct(productCode);
	}

	@Override
	public ProductInfo getProduct(String productCode, boolean isEsim) throws ApplicationException {
		return productDeviceServiceDao.getProduct(productCode, isEsim);
	}
	
	@Override
	public TestPointResultInfo testEquipmentActivationSupportService() {
		return equipmentActivationSupportSvcDao.test();
	}
	
	@Override
	public TestPointResultInfo testEquipmentLifeCycleManagementService() {
		return equipmentLifecycleManagementSvcDao.test();
	}

	@Override
	public TestPointResultInfo testEquipmentInfoService_1_0() {
		return equipmentInfoSvcDao.test();
	}

	@Override
	public TestPointResultInfo testProductFacadeEJB() {
		
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		String productcode = "IACUP205901";
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("P3MSProductFacade Ejb");
		
		try {
			isApple("IACUP205901");
			resultInfo.setResultDetail("Invoked P3MSProductFacade to test isApple method for productcode :" + productcode);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}

		return resultInfo;
	}

	@Override
	public TestPointResultInfo testNrtEligibilityManagerEJB(String serialNumber) {
		
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("NRT ELigibility Manager EJB");
		
		try {
			getNrtEligibilityManager().getHandsetEligibilityForNrt(serialNumber, null);
			resultInfo.setResultDetail("Invoked NRT ELigibility Manager EJB to test getHandsetEligibilityForNrt for serialNumber:" + serialNumber);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			String exceptedError = "com.telusmobility.nrt.NRTException";
			if (t.getMessage() != null && t.getMessage().contains(exceptedError)) {
				resultInfo.setResultDetail("Invoked NRT ELigibility Manager EJB to test getHandsetEligibilityForNrt for serialNumber:" + serialNumber);
				resultInfo.setPass(true);
			} else {
				resultInfo.setPass(false);
				resultInfo.setExceptionDetail(t);
			}
		}

		return resultInfo;
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public void asyncAssignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setPhoneNumber(phoneNumber);
		semsEquipmentInfo.setSerialNumber(serialNumber);
		semsEquipmentInfo.setAssociatedHandsetIMEI(associatedHandsetIMEI);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_ASSIGN_EQUIPMENT_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}

	@Override
	public void asyncChangePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setSerialNumber(serialNumber);
		semsEquipmentInfo.setPhoneNumber(newPhoneNumber);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_CHANGE_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}

	@Override
	public void asyncApproveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setPhoneNumber(phoneNumber);
		semsEquipmentInfo.setSerialNumber(serialNumber);
		semsEquipmentInfo.setAssociatedHandsetIMEI(associatedHandsetIMEI);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_APPROVE_RESERVED_EQUIPMENT_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}

	@Override
	public void asyncReleaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setPhoneNumber(phoneNumber);
		semsEquipmentInfo.setSerialNumber(serialNumber);
		semsEquipmentInfo.setAssociatedHandsetIMEI(associatedHandsetIMEI);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_RELEASE_RESERVED_EQUIPMENT_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}

	@Override
	public void asyncDisassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setPhoneNumber(phoneNumber);
		semsEquipmentInfo.setUsimId(usimId);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_DISASSOCIATE_EQUIPMENT_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}

	@Override
	public void asyncSwapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber, String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI)
			throws ApplicationException {
		SemsEquipmentInfo semsEquipmentInfo = new SemsEquipmentInfo();
		semsEquipmentInfo.setPhoneNumber(phoneNumber);
		semsEquipmentInfo.setOldSerialNumber(oldSerialNumber);
		semsEquipmentInfo.setNewSerialNumber(newSerialNumber);
		semsEquipmentInfo.setOldAssociatedHandsetIMEI(oldAssociatedHandsetIMEI);
		semsEquipmentInfo.setNewAssociatedHandsetIMEI(newAssociatedHandsetIMEI);
		semsEquipmentInfo.setSemsMethodType(SemsEquipmentInfo.METHOD_TYPE_SWAP_HSPA_EQUIPMENT_PHONENUMBER);
		queueSender.send(semsEquipmentInfo, "msgSubTypeSEMS", null);
	}
	
	/**
	 * Validate the business rule for eSIM device swap
	 * @param currentUsim the current USIM
	 * @param newEsimDevice the new ESIM device
	 * @return An instance of DeviceSwapValidateInfo
	 */
	@Override
	public DeviceSwapValidateInfo validateEsimDeviceSwap(EquipmentInfo currentUsim, EquipmentInfo newEsimDevice) throws ApplicationException {
		DeviceSwapValidateInfo deviceSwapValidateInfo = new DeviceSwapValidateInfo();

		if(!AppConfiguration.isEsimSupportEnabled()) { // Check the enableEsimSupport flag in LDAP
			deviceSwapValidateInfo.setResultCd(EsimDeviceSwapValidationResult.UNSUPPORTED_ESIM_DEVICE.getResultCd());
			LOGGER.debug("ESIM device swapping is not supported since the flag isEsimSupportEnabled is set to false.");
			return deviceSwapValidateInfo;
		} else if (newEsimDevice.isStolen()) { // Check if the new equipment is lost or stolen in SEMS
			deviceSwapValidateInfo.setResultCd(EsimDeviceSwapValidationResult.LOST_STOLEN.getResultCd());
			LOGGER.debug("The new equipment is marked as lost or stolen in SEMS.");
			return deviceSwapValidateInfo;
		}
		
		// Check input parameters
		if (StringUtils.isBlank(newEsimDevice.getProductCode()) || StringUtils.isBlank(currentUsim.getProductCode())) {
			String errMsg = "Invalid input parameter(s): both current USIM and new ESIM device product codes are required";
			LOGGER.error(errMsg);
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, errMsg, StringUtils.EMPTY);
		}
		
		// Get product info of the new ESIM device by calling ProductDeviceService operation getProduct
		ProductInfo newEsimDeviceProductInfo = null;
		String newEsimDeviceSimProfileCode = null;
		String newDeviceType = null;
		
		try {
			newEsimDeviceProductInfo = getProduct(newEsimDevice.getProductCode(), true);
			newEsimDeviceSimProfileCode = newEsimDeviceProductInfo.getSimProfileCode();
			newDeviceType = newEsimDeviceProductInfo.getDeviceType();
			deviceSwapValidateInfo.setNewDeviceSimProfileCd(newEsimDeviceSimProfileCode);
			deviceSwapValidateInfo.setNewDeviceType(newDeviceType);
			deviceSwapValidateInfo.setNewSimType(newEsimDeviceProductInfo.getSupportedSimType());
		} catch (Exception e) {
			LOGGER.error("Failed to call ProductDeviceService operation getProduct: " + e);
		}
				
		// Check if the new ESIM device is a tracker
		if (StringUtils.isBlank(newDeviceType) || !DEVICE_TYPE_TRACKER.equalsIgnoreCase(newDeviceType) ? true : false) {
			deviceSwapValidateInfo.setResultCd(EsimDeviceSwapValidationResult.UNSUPPORTED_ESIM_DEVICE.getResultCd());
			LOGGER.debug(EsimDeviceSwapValidationResult.UNSUPPORTED_ESIM_DEVICE.getResultCd() + ". The new equipment is not a tracker device.");
			return deviceSwapValidateInfo;
		}
		
		// Get UsimProfileInfo by calling ProductDeviceService operation getProductUSIMProfile
		long currentUsimProductId = productEquipmentHelper.getProductIdByProductCode(currentUsim.getProductCode());
		UsimProfileInfo currentUsimProfileInfo = getProductUSIMProfile(currentUsimProductId);
		
		String currentUsimProfileCode = currentUsimProfileInfo.getProfileCode();
		
		// Check if current simProfileCd matches the new tracker's simProfileCd. Please note that Current USIM's simProfileCd may be null for some USIM.
		if (currentUsimProfileCode == null || !currentUsimProfileCode.equalsIgnoreCase(newEsimDeviceSimProfileCode)) {
			deviceSwapValidateInfo.setResultCd(EsimDeviceSwapValidationResult.SIM_PROFILE_MISMATCH.getResultCd());
			String simProfileCodeMessage = "[Current USIM's simProfileCd: " + currentUsimProfileCode;
			simProfileCodeMessage += "; New ESIM device's simProfileCd: " + newEsimDeviceSimProfileCode + "]";
			LOGGER.debug(EsimDeviceSwapValidationResult.SIM_PROFILE_MISMATCH.getResultCd() + simProfileCodeMessage);
			return deviceSwapValidateInfo;
		}
		
		deviceSwapValidateInfo.setCurrentSimProfileCd(currentUsimProfileCode);
		deviceSwapValidateInfo.setCurrentSimType(currentUsimProfileInfo.getSimType());
		deviceSwapValidateInfo.setResultCd(EsimDeviceSwapValidationResult.SUCCESS.getResultCd());
		return deviceSwapValidateInfo;
	}
	
	/**
	 * Reserve SIM profile (ICCID) for the new device
	 * @param imei the IMEI of the new ESIM device
	 * @param simProfileCd the simProfileCode of the new device
	 * @param embeddedIccId the embedded IccId of the new device
	 * @return an instance of EquipmentInfo with reserved ICCID
	 * @throws ApplicationException
	 */
	@Override
	public EquipmentInfo reserveSimProfile(String imei, String simProfileCd, String embeddedIccId) throws ApplicationException {
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		
		if (StringUtils.isBlank(imei) || StringUtils.isBlank(simProfileCd)) {
			String errorMsg = "ESIM device's IMEI or simProfileCd is missing, imei = [" + imei + "], simProfileCd = [" + simProfileCd + "]";
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, errorMsg, StringUtils.EMPTY);
		}
		
		ReserveSimProfileResponse res = equipmentActivationSupportSvcDao.reserveSimProfile(imei, simProfileCd, embeddedIccId);
		
		if (res == null || StringUtils.isBlank(res.getIccId())) {
			String errorMsg = "ICCID is not found in the response of EquipmentActivationSupportService operation reserveSimProfile, imei = [" + imei + "], simProfileCd = [" + simProfileCd + "]";
			LOGGER.error(errorMsg);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ESIM_ERROR_RESERVE_SIM_PROFILE, errorMsg, StringUtils.EMPTY);
		}
		
		equipmentInfo.setSerialNumber(res.getIccId());
		equipmentInfo.setProductClassCode(res.getProductClassCode());
		equipmentInfo.setProductCode(res.getProductCode());
		equipmentInfo.setProductGroupTypeCode(res.getProductGroupTypeCode());
		equipmentInfo.setProductTypeID(res.getProductTypeId());
		equipmentInfo.setTechType(res.getTechnologyTypeTxt());
		int[] brandIds = new int[1];
		brandIds[0] = res.getBrandId();
		equipmentInfo.setBrandIds(brandIds);
		return equipmentInfo;
	}
	
	private boolean validateDeviceSwapEligibility(String simType, String newDeviceType, String currentDeviceType) throws ApplicationException {
		return EsimDeviceSwapEligibilityUtil.validateEsimDeviceSwapEligibility(simType, newDeviceType, currentDeviceType);
	}
}