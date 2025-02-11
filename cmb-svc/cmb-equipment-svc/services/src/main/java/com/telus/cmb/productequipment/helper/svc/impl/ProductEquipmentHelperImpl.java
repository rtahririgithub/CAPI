package com.telus.cmb.productequipment.helper.svc.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.reference.NetworkType;
import com.telus.api.util.VersionReader;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.dao.CardHelperDao;
import com.telus.cmb.productequipment.helper.dao.CreditAndPricingHelperDao;
import com.telus.cmb.productequipment.helper.dao.EquipmentHelperDao;
import com.telus.cmb.productequipment.helper.dao.ProductOfferingServiceDao;
import com.telus.cmb.productequipment.helper.dao.VoucherValidationServiceDao;
import com.telus.cmb.productequipment.helper.dao.impl.CreditAndPricingHelperDaoImpl;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelperTestPoint;
import com.telus.cmb.productequipment.utilities.AppConfiguration;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name = "ProductEquipmentHelper", mappedName = "ProductEquipmentHelper")
@Remote({ ProductEquipmentHelper.class, ProductEquipmentHelperTestPoint.class })
@RemoteHome(ProductEquipmentHelperHome.class)
@Interceptors({ SpringBeanAutowiringInterceptor.class, ProductEquipmentHelperSvcInvocationInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProductEquipmentHelperImpl implements ProductEquipmentHelper,
		ProductEquipmentHelperTestPoint {

	private static final Logger LOGGER = Logger
			.getLogger(ProductEquipmentHelperImpl.class);

	@Autowired
	private CreditAndPricingHelperDao creditAndPricingDao;

	@Autowired
	private EquipmentHelperDao equipmentDao;



	@Autowired
	private ProductOfferingServiceDao productOfferingDao;

	@Autowired
	private VoucherValidationServiceDao voucherValidationDao;

	@Autowired
	private CardHelperDao cardDao;

	@Autowired
	private DataSourceTestPointDao testPointDao;

	public static final int LOCATION_SHIPPED_TO = 2;

	public void setCardDao(CardHelperDao cardDao) {
		this.cardDao = cardDao;
	}

	public void setCreditAndPricingDao(
			CreditAndPricingHelperDao creditAndPricingDao) {
		this.creditAndPricingDao = creditAndPricingDao;
	}

	public void setEquipmentDao(EquipmentHelperDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}

	

	public ProductOfferingServiceDao getProductOfferingDao() {
		return productOfferingDao;
	}

	public void setProductOfferingDao(
			ProductOfferingServiceDao productOfferingDao) {
		this.productOfferingDao = productOfferingDao;
	}

	public VoucherValidationServiceDao getVoucherValidationDao() {
		return voucherValidationDao;
	}

	public void setVoucherValidationDao(
			VoucherValidationServiceDao voucherValidationDao) {
		this.voucherValidationDao = voucherValidationDao;
	}

	@Override
	public EquipmentInfo getEquipmentInfobySerialNo(String pSerialNo)
			throws ApplicationException {

		if (pSerialNo == null || pSerialNo.trim().isEmpty()) {
			throw new ApplicationException(
					SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKNOWN_SERIAL_NUMBER,
					EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_EN + "[" + pSerialNo +"]",
					"");
		}

		if (EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(pSerialNo) || EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(pSerialNo)) {
			return constructBusinessConnectDummyEquipmentInfo(pSerialNo);
		}
		
		EquipmentInfo clientEquipInfo = null;

		clientEquipInfo = creditAndPricingDao
				.getEquipmentInfobySerialNo(pSerialNo);
		clientEquipInfo.setContractTermCredits(creditAndPricingDao
				.retrieveContractTermCredits(
						clientEquipInfo.getProductGroupTypeID(),
						clientEquipInfo.getProductTypeID()));

		if (clientEquipInfo.isHSPA() && !clientEquipInfo.isUSIMCard()) {
			String[] imsi = equipmentDao.getIMSIsBySerialNumber(pSerialNo);
			if (imsi.length == 0) {
				clientEquipInfo.setPhoneNumber("0");
			} else {
				SubscriberInfo[] subscriberInfoArray = equipmentDao
						.retrieveHSPASubscriberIdsByIMSI(imsi[0]);
				if ((subscriberInfoArray != null)
						&& (subscriberInfoArray.length > 0)) {
					clientEquipInfo.setPhoneNumber(subscriberInfoArray[0]
							.getPhoneNumber());
					clientEquipInfo.setBanID(subscriberInfoArray[0].getBanId());
				}
			}
		} else if (clientEquipInfo.isUSIMCard() || !clientEquipInfo.isMule()) {
			clientEquipInfo = equipmentDao
					.getSubscriberByEquipment(clientEquipInfo);
		}

		return clientEquipInfo;

	}

	@Override
	public EquipmentInfo getEquipmentInfobySerialNumber(String pSerialNo)
			throws ApplicationException {
		if (EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(pSerialNo) || EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(pSerialNo)) {
			return constructBusinessConnectDummyEquipmentInfo(pSerialNo);
		}
		EquipmentInfo clientEquipInfo = null;
		clientEquipInfo = creditAndPricingDao
				.getEquipmentInfobySerialNo(pSerialNo);
		return clientEquipInfo;
	}

	@Override
	public String[] getESNByPseudoESN(String pSerialNo)
			throws ApplicationException {
		return creditAndPricingDao.getESNByPseudoESN(pSerialNo);
	}

	@Override
	public boolean isValidESNPrefix(String pSerialNo)
			throws ApplicationException {
		return equipmentDao.isValidESNPrefix(pSerialNo);
	}

	@Override
	public boolean isVirtualESN(String pSerialNo) throws ApplicationException {
		return equipmentDao.isVirtualESN(pSerialNo);
	}

	@Override
	public CardInfo getAirCardByCardNo(String fullCardNo, String phoneNumber,
			String equipmentSerialNo, String userId)
			throws ApplicationException {

		if (fullCardNo == null) {
			LOGGER.error("id=VAL10002; Card number is null.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.CARD_NUMBER_NULL,
					EquipmentErrorMessages.CARD_NUMBER_NULL_EN, "");
		} else if (fullCardNo.length() != 12 && fullCardNo.length() != 15) {
			LOGGER.error("id=VAL10002; Card number has to be 12 or 15 in length.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.INVALID_CARD_NUMBER_LENGTH,
					EquipmentErrorMessages.INVALID_CARD_NUMBER_LENGTH_EN, "");
		}

		if (userId == null) {
			LOGGER.error("id=EQU30009; Unknown user id. User is null.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_USER_ID,
					EquipmentErrorMessages.UNKNOWN_USER_ID_EN, "");
		}

		if (phoneNumber == null) {
			LOGGER.error("id=EQU30010; Unknown phone number. Phone number is null.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_PHONE_NUMBER,
					EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, "");
		}

			return getProductOfferingDao().getAirCardByCardNo(fullCardNo);
	}

	@Override
	public EquipmentInfo getAssociatedHandsetByUSIMID(String USIMID)
			throws ApplicationException {
		EquipmentInfo equipmentInfo = null;
		equipmentInfo = equipmentDao.getAssociatedHandsetByUSIMID(USIMID);

		if (equipmentInfo != null) {
			double[] contractTermCredits = creditAndPricingDao
					.retrieveContractTermCredits(
							equipmentInfo.getProductGroupTypeID(),
							equipmentInfo.getProductTypeID());
			equipmentInfo.setContractTermCredits(contractTermCredits);
		}
		return equipmentInfo;
	}

	@Override
	public double getBaseProductPrice(String serialNumber, String province,
			String npa) throws ApplicationException {
		return creditAndPricingDao.getBaseProductPrice(serialNumber, province,
				npa, null);
	}

	@Override
	public double getBaseProductPriceByProductCode(String productCode,
			String province, String npa) throws ApplicationException {

		double basePrice = 0.0;
		boolean useACME = AppConfiguration.isUseAcme();

		if (useACME) {
			basePrice = creditAndPricingDao
					.getBaseProductPriceByProductCodeFromACME(productCode,
							province, npa, null);
		} else {
			basePrice = creditAndPricingDao
					.getBaseProductPriceByProductCodeFromP3MS(productCode,
							province, null);
		}

		// Throw exception to zero dollar price.
		if (basePrice == 0.0) {
			LOGGER.error("No base product price available for product code: "
					+ productCode);
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					EquipmentErrorMessages.PRODUCT_PRICE_NOT_FOUND_EN + " "
							+ productCode, "");
		}
		return basePrice;
	}

	@Override
	public CardInfo getCardBySerialNo(String serialNo)
			throws ApplicationException {
		if (serialNo == null) {
			LOGGER.error("id=VAL10002; Serial number is null.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.SERIAL_NUMBER_NULL,
					EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, "");

		} else if (serialNo.length() != 11) {
			LOGGER.error("id=VAL10002; Serial number not 11 in length.");
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.INVALID_SERAIL_NUMBER_LENGTH,
					EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, "");
		}
			return getProductOfferingDao().getCardBySerialNo(serialNo);
	}

	@Override
	public ServiceInfo[] getCardServices(String serialNo, String techType,
			String billType) throws ApplicationException {

		// Need to first make sure that the string is not null and contains 11
		// chars..
		if (serialNo == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.SERIAL_NUMBER_NULL,
					EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, "");
		} else if (serialNo.length() != 11) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.INVALID_SERAIL_NUMBER_LENGTH,
					EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, "");
		}

		// Need to make sure there is a techType and a billing type
		if (techType == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.TECHNOGOTY_TYPE_NULL,
					EquipmentErrorMessages.TECHNOGOTY_TYPE_NULL_EN, "");
		}
		if (billType == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.BILLING_TYPE_NULL,
					EquipmentErrorMessages.BILLING_TYPE_NULL_EN, "");
		}
		return cardDao.getCardServices(serialNo, techType, billType);
	}

	@Override
	public CardInfo[] getCards(String phoneNumber, String cardType)
			throws ApplicationException {
		if (phoneNumber == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_PHONE_NUMBER,
					EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, "");
		}
		if (cardType == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.CARD_TYPE_NULL,
					EquipmentErrorMessages.CARD_TYPE_NULL_EN, "");
		}
		return cardDao.getCards(phoneNumber, cardType);
	}

	@Override
	public CardInfo[] getCards(String phoneNumber) throws ApplicationException {
		if (phoneNumber == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_PHONE_NUMBER,
					EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, "");
		}
		return cardDao.getCards(phoneNumber, null);
	}

	@Override
	public EquipmentInfo getEquipmentInfoByCapCode(String capCode,
			String encodingFormat) throws ApplicationException {

		EquipmentInfo clientEquipInfo = null;
		clientEquipInfo = equipmentDao.getEquipmentInfobyCapCode(capCode,
				encodingFormat);
		clientEquipInfo.setContractTermCredits(creditAndPricingDao
				.retrieveContractTermCredits(
						clientEquipInfo.getProductGroupTypeID(),
						clientEquipInfo.getProductTypeID()));
		clientEquipInfo = equipmentDao
				.getSubscriberByEquipment(clientEquipInfo);
		return clientEquipInfo;
	}

	@Override
	public EquipmentInfo getEquipmentInfobyPhoneNo(String pPhoneNo)
			throws ApplicationException {
		EquipmentInfo clientEquipInfo = null;
		clientEquipInfo = equipmentDao.getEquipmentInfobyPhoneNo(pPhoneNo);
		return clientEquipInfo;
	}

	@Override
	public EquipmentInfo getEquipmentInfobyProductCode(String productCode)
			throws ApplicationException {

		EquipmentInfo clientEquipInfo = null;
		clientEquipInfo = equipmentDao
				.getEquipmentInfobyProductCode(productCode);
		clientEquipInfo.setContractTermCredits(creditAndPricingDao
				.retrieveContractTermCredits(
						clientEquipInfo.getProductGroupTypeID(),
						clientEquipInfo.getProductTypeID()));
		return clientEquipInfo;

	}

	@Override
	public EquipmentInfo[] getEquipmentInfobySerialNo(String serialNo,
			boolean checkPseudoESN) throws ApplicationException {
		if (EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(serialNo) || EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(serialNo)) {
			return new EquipmentInfo[]  { constructBusinessConnectDummyEquipmentInfo(serialNo)};
		}
		EquipmentInfo[] equipmentInfos = null;
		equipmentInfos = creditAndPricingDao.getEquipmentInfobySerialNo(
				serialNo, checkPseudoESN);

		for (int idx = 0; idx < equipmentInfos.length; idx++) {
			EquipmentInfo equipmentInfo = equipmentInfos[idx];
			if ((!equipmentInfo.isMule()) && (!equipmentInfo.isPager())) {
				equipmentInfos[idx] = equipmentDao
						.getSubscriberByEquipment(equipmentInfo);
			}
		}

		return equipmentInfos;
	}

	@Override
	public String[] getEquipmentList(String pTechTypeClass, int n,
			boolean inUse, String startSerialNo) throws ApplicationException {

		String[] equipList0 = new String[0];
		ArrayList<String> equipList = new ArrayList<String>();
		String[] equipmentList = new String[0];
		String productType = (pTechTypeClass == null ? "C" : pTechTypeClass
				.equals("MIKE") ? "I" : "C");
		int j = 0, i = 0;
		String pStartSerialNo = (startSerialNo == null ? "10000000000"
				: startSerialNo.equals("") ? "10000000000" : startSerialNo);
		if (!inUse) {
			while ((equipList.size() < n) && (j < 100)) {
				j++;
				equipList0 = equipmentDao.getEquipmentList(pTechTypeClass,
						n * 20, pStartSerialNo);
				if (equipList0.length != 0) {
					pStartSerialNo = equipList0[equipList0.length - 1];
					for (i = equipList0.length - 1; (i >= 0)
							&& (equipList.size() < n); i--) {
						if (equipmentDao.isInUse(equipList0[i]) == false)
							equipList.add(equipList0[i]);
						else
							i = -1;
					}
				}
			}
			equipmentList = (String[]) equipList.toArray(new String[equipList
					.size()]);
		} else {
			equipmentList = equipmentDao.getKBEquipmentList(productType, n);
		}

		return equipmentList;
	}

	@Override
	public EquipmentModeInfo[] getEquipmentModes(String pProductCode)
			throws ApplicationException {
		return equipmentDao.getEquipmentModes(pProductCode);
	}

	@Override
	public long getIDENShippedToLocation(String serialNumber)
			throws ApplicationException {
		return equipmentDao.getIDENShippedToLocation(serialNumber,
				LOCATION_SHIPPED_TO);
	}

	@Override
	public String getIMEIBySIM(String pSimID) throws ApplicationException {
		return equipmentDao.getIMEIBySIM(pSimID);
	}

	@Override
	public EquipmentInfo getMuleBySIM(String pSimID)
			throws ApplicationException {
		EquipmentInfo equipInfo = null;
		equipInfo = equipmentDao.getMuleBySIM(pSimID);

		if (equipInfo != null) {
			equipInfo.setContractTermCredits(creditAndPricingDao
					.retrieveContractTermCredits(
							equipInfo.getProductGroupTypeID(),
							equipInfo.getProductTypeID()));
		}
		return equipInfo;
	}

	@Override
	public long getPCSShippedToLocation(String serialNumber)
			throws ApplicationException {
		return equipmentDao.getPCSShippedToLocation(serialNumber,
				LOCATION_SHIPPED_TO);
	}

	@Override
	public long getProductIdByProductCode(String productCode) throws ApplicationException {
		return equipmentDao.getProductIdByProductCode(productCode);
	}
	
	@Override
	public String[] getProductFeatures(String pProductCode)
			throws ApplicationException {
		return equipmentDao.getProductFeatures(pProductCode);
	}
	
	@Override
	public boolean isProductFeatureEnabled(String productCode, String productFeature) throws ApplicationException {
		for (String feature : getProductFeatures(productCode)) {
			if (StringUtils.equalsIgnoreCase(feature.trim(), productFeature)) {
				return true;
			}
		}
		return false;
	}	

	@Override
	public String getSIMByIMEI(String pImeiID) throws ApplicationException {
		try {
			return equipmentDao.getSIMByIMEI(pImeiID);
		} catch (ApplicationException ae) {
			if (ae.getSystemCode().equals(SystemCodes.CMB_PEH_DAO)
					&& ae.getErrorCode().equals(ErrorCodes.SIM_NOT_FOUND))
				return null;
			throw ae;
		}
	}

	@Override
	public long getShippedToLocation(String serialNumber)
			throws ApplicationException {
		return equipmentDao.getShippedToLocation(serialNumber,
				LOCATION_SHIPPED_TO);
	}

	@Override
	public WarrantyInfo getWarrantyInfo(String pSerialNo)
			throws ApplicationException {
		return equipmentDao.getWarrantyInfo(pSerialNo);
	}

	@Override
	public boolean isNewPrepaidHandset(String serialNo, String productCode)
			throws ApplicationException {
		return equipmentDao.isNewPrepaidHandset(serialNo, productCode);
	}

	@Override
	public CardInfo getCardByFullCardNo(String fullCardNo, String phoneNumber,
			String equipmentSerialNo, String userId)
			throws ApplicationException {

		CardInfo cardInfo = null;
		String serialNo = null;
		String cypherPIN = null;
		int failedPINAttempts = 0;

		// Need to first make sure that the string is not null and contains 15
		// chars..
		if (fullCardNo == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.CARD_NUMBER_NULL,
					EquipmentErrorMessages.CARD_NUMBER_NULL_EN, "");
		} else if (fullCardNo.length() != 15) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.INVALID_CARD_NUMBER_LENGTH,
					EquipmentErrorMessages.INVALID_CARD_NUMBER_LENGTH_EN, "");
		}

		// Also, need to make sure that the user is not null
		if (userId == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_USER_ID,
					EquipmentErrorMessages.UNKNOWN_USER_ID_EN, "");
		}

		// Also, need to make sure the phone number is not null
		if (phoneNumber == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.UNKONWN_PHONE_NUMBER,
					EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, "");
		}

		// the equipment serial number field is allowed to be null.
		// Therefore, no validation needed

		// Now we need to extract the serial number portion of the full card
		// number
		serialNo = fullCardNo.substring(0, 11);

		// invoke dao method to retrieve the cardInfo object if it exists by
		// passing the serial number.
		// There is no point in doing a card pin challenge if the serial number
		// does not exist.
		cardInfo = cardDao.getCardInfobySerialNo(serialNo);

		// now we need to check against the business rule here. Rule is
		// "Fail activation attempts when the card PIN is incorrect 3 times within a one hour period"

		failedPINAttempts = cardDao.checkPINAttemps(serialNo);

		if (failedPINAttempts >= 3) {
			throw new ApplicationException(SystemCodes.CMB_PEH_EJB,
					ErrorCodes.TOO_MANY_PIN_ATTEMPT_FAILURE,
					EquipmentErrorMessages.TOO_MANY_PIN_ATTEMPT_FAILURE_EN, "");
		}
		// do PIN challenge
		// This method is used by passing the 11 digit serial number (SN),
		// the 15 digit printed card number (clearPIN),
		// and the 16 digit encrypted PIN from the card_pin table in SEMS
		// (cipherPIN).
		// It returns 0 on valid PIN (success),
		// 1 on PIN mismatch (failure)
		// and -1 if the card serial number is unknown (failure).

		cypherPIN = cardDao.getCypherPIN(serialNo);

		
			cardInfo = getVoucherValidationDao().validateCardPIN(serialNo,
					fullCardNo, cypherPIN, userId, equipmentSerialNo,
					phoneNumber, cardInfo);

		return cardInfo;

	}

	public EquipmentInfo retrieveVirtualEquipment(String serialNumber,
			String techTypeClass) throws ApplicationException {
		EquipmentInfo equipmentInfo = null;
		equipmentInfo = equipmentDao.getVirtualEquipment(serialNumber,
				techTypeClass);
		return equipmentInfo;

	}

	@Override
	public EquipmentInfo retrievePagerEquipmentInfo(String serialNo)
			throws ApplicationException {
		EquipmentInfo equipmentInfo = null;
		equipmentInfo = equipmentDao.retrievePagerEquipmentInfo(serialNo);
		return equipmentInfo;
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */

	@Override
	public HashMap<String, ActivationCreditInfo[]> getActivationCreditsByProductCodes(
			String[] productCodes, String province, String npa,
			int contractTermMonths, Date activationDate,
			boolean fidoConversion, String[] productTypes,
			boolean isInitialActivation) throws ApplicationException {
		HashMap<String, ActivationCreditInfo[]> map = new HashMap<String, ActivationCreditInfo[]>();
		for (int i = 0; i < productCodes.length; i++) {
			ActivationCreditInfo[] creditInfos = getActivationCreditsByProductCode(
					productCodes[i], province, npa, contractTermMonths,
					activationDate, fidoConversion, productTypes[i],
					isInitialActivation);

			map.put(productCodes[i], creditInfos);
		}
		return map;
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */

	@Override
	public ActivationCreditInfo[] getActivationCreditsByProductCode(
			String productCode, String province, String npa,
			int contractTermMonths, Date activationDate,
			boolean fidoConversion, String productType,
			boolean isInitialActivation) throws ApplicationException {
		ActivationCreditInfo[] actCreditInfo = null;

		boolean useACME = AppConfiguration.isUseAcme();
		if (useACME) {
			actCreditInfo = creditAndPricingDao
					.getActivationCreditsByProductCodeFromACME(productCode,
							province, npa, contractTermMonths, "%",
							activationDate, productType, isInitialActivation);
		} else {
			actCreditInfo = creditAndPricingDao
					.getActivationCreditsByProductCodeFromP3MS(productCode,
							province, contractTermMonths, "%", activationDate,
							productType, isInitialActivation);
		}

		// getActivationCredits(serialNumber, province, npa,
		// contractTermMonths);
		// Filter the FIDO credits
		if (!fidoConversion) {
			actCreditInfo = filterActivationCredits(actCreditInfo,
					ActivationCreditInfo.CREDIT_TYPE_FIDO);
		}
		return actCreditInfo;
	}

	private ActivationCreditInfo[] filterActivationCredits(
			ActivationCreditInfo[] activationCreditInfo, String creditType) {
		// Filter out the unwanted credit type.
		ArrayList<ActivationCreditInfo> actCredits = new ArrayList<ActivationCreditInfo>();
		for (int i = 0; i < activationCreditInfo.length; i++) {
			if (!activationCreditInfo[i].getCreditType().equalsIgnoreCase(
					creditType)) {
				actCredits.add(activationCreditInfo[i]);
			}
		}
		return (ActivationCreditInfo[]) actCredits
				.toArray(new ActivationCreditInfo[actCredits.size()]);
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */
	@Override
	public ActivationCreditInfo[] getActivationCredits(String serialNumber,
			String province, String npa, String creditType)
			throws ApplicationException {

		return creditAndPricingDao.getActivationCredits(serialNumber, province,
				npa, CreditAndPricingHelperDaoImpl.CONTRACT_TERM_DONT_CARE,
				creditType, null);
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */

	@Override
	public ActivationCreditInfo[] getActivationCredits(String serialNumber,
			String province, String npa, int contractTermMonths,
			boolean fidoConversion) throws ApplicationException {

		ActivationCreditInfo[] actCreditInfo = null;
		actCreditInfo = creditAndPricingDao.getActivationCredits(serialNumber,
				province, npa, contractTermMonths, "%", null);
		// Filter the FIDO credits
		if (!fidoConversion) {
			actCreditInfo = filterActivationCredits(actCreditInfo,
					ActivationCreditInfo.CREDIT_TYPE_FIDO);
		}
		return actCreditInfo;
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */

	@Override
	public ActivationCreditInfo[] getActivationCredits(String serialNumber,
			String province, String npa, int contractTermMonths,
			String pricePlan, boolean fidoConversion)
			throws ApplicationException {
		ActivationCreditInfo[] actCreditInfo = null;
		ActivationCreditInfo[] tmpActCreditInfo = creditAndPricingDao
				.getActivationCredits(serialNumber, province, npa,
						contractTermMonths, "%", null);
		/*
		 * Filter out for price plan rules: If pricePlan within
		 */
		ArrayList<ActivationCreditInfo> actCredits = new ArrayList<ActivationCreditInfo>();
		for (int a = 0; a < tmpActCreditInfo.length; a++) {
			if (!tmpActCreditInfo[a].isPricePlanCredit()
					|| (tmpActCreditInfo[a].isPricePlanCredit() & getPriceplanFamilyCreditPlans()
							.contains(pricePlan.trim())))
				actCredits.add(tmpActCreditInfo[a]);
		}
		actCreditInfo = (ActivationCreditInfo[]) actCredits
				.toArray(new ActivationCreditInfo[actCredits.size()]);

		// Filter the FIDO credits
		if (!fidoConversion) {
			actCreditInfo = filterActivationCredits(actCreditInfo,
					ActivationCreditInfo.CREDIT_TYPE_FIDO);
		}
		return actCreditInfo;
	}

	/**
	 * @deprecated use P3MS EJB to retrieve the activation credit.
	 */
	@Override
	public ActivationCreditInfo[] getActivationCredits(String serialNumber,
			String province, String npa, int contractTermMonths,
			Date activationDate, boolean fidoConversion)
			throws ApplicationException {
		ActivationCreditInfo[] actCreditInfo = null;
		actCreditInfo = creditAndPricingDao.getActivationCredits(serialNumber,
				province, npa, contractTermMonths, "%", activationDate);
		// Filter the FIDO credits
		if (!fidoConversion) {
			actCreditInfo = filterActivationCredits(actCreditInfo,
					ActivationCreditInfo.CREDIT_TYPE_FIDO);
		}
		return actCreditInfo;
	}

	private List<String> getPriceplanFamilyCreditPlans()
			throws ApplicationException {
		String methodName = "PriceplanFamilyCreditPlans";
		List<String> creditablePricePlans = AppConfiguration
				.getPricePlanCreditFamilyPlans();

		LOGGER.debug("(" + getClass().getName() + "." + methodName
				+ ") Family Plan Credits, size: " + creditablePricePlans.size()
				+ " [" + creditablePricePlans.toString() + "]");

		return creditablePricePlans;
	}

	private EquipmentInfo constructBusinessConnectDummyEquipmentInfo(String pSerialNo) {
		if (EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(pSerialNo)) {
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setSerialNumber(EquipmentInfo.DUMMY_ESN_FOR_VOIP);
			equipmentInfo.setEquipmentType(EquipmentInfo.EQUIPMENT_TYPE_VOIP);
			equipmentInfo.setNetworkType(NetworkType.NETWORK_TYPE_HSPA);

			return equipmentInfo;
		}
		if (EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(pSerialNo)) {
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setSerialNumber(EquipmentInfo.DUMMY_ESN_FOR_HSIA);
			equipmentInfo.setEquipmentType(EquipmentInfo.EQUIPMENT_TYPE_HSIA);
			equipmentInfo.setNetworkType(NetworkType.NETWORK_TYPE_HSPA);
			return equipmentInfo;
		}
		return null;
	}
	
	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return testPointDao.testKnowbilityDataSource();
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public TestPointResultInfo getClientEquipmentPkgVersion() {
		return testPointDao.getClientEquipmentPkgVersion();
	}

	@Override
	public TestPointResultInfo testProductOfferingService() {
		return productOfferingDao.test();
	}

	@Override
	public TestPointResultInfo testVoucherValidationService() {
		return voucherValidationDao.test();
	}
}
	