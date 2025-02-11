package com.telus.cmb.productequipment.helper.dao.impl;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.equipment.Equipment;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.dao.CreditAndPricingHelperDao;
import com.telus.cmb.productequipment.utilities.AppConfiguration;
import com.telus.eas.equipment.info.CiPromotionInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;

public class CreditAndPricingHelperDaoImpl implements CreditAndPricingHelperDao {

	private static final Logger LOGGER = Logger.getLogger(CreditAndPricingHelperDaoImpl.class);

	public static final int CONTRACT_TERM_DONT_CARE = -1;
	private static final int ORACLE_REF_CURSOR = -10;

	private JdbcTemplate knowbilityJdbcTemplate;
	private JdbcTemplate distJdbcTemplate;

	public JdbcTemplate getKnowbilityJdbcTemplate() {
		return knowbilityJdbcTemplate;
	}

	public void setKnowbilityJdbcTemplate(JdbcTemplate knowbilityJdbcTemplate) {
		this.knowbilityJdbcTemplate = knowbilityJdbcTemplate;
	}

	public JdbcTemplate getDistJdbcTemplate() {
		return distJdbcTemplate;
	}

	public void setDistJdbcTemplate(JdbcTemplate distJdbcTemplate) {
		this.distJdbcTemplate = distJdbcTemplate;
	}

	@Override
	public double[] retrieveContractTermCredits(long productGroupTypeID, long productTypeID) throws ApplicationException {
		
		String queryStr = " select ctpt.credit_amount, ct.duration, ct.unit_type_cd "
				+ " from contract_term_product_type ctpt , " + " contract_term ct  "
				+ " where ctpt.product_gp_type_id= ? " + " and   ctpt.product_type_id= ? "
				+ " and   ct.contract_term_id=ctpt.contract_term_id   " + " order by ct.duration ";
		
		return getDistJdbcTemplate().query(queryStr, new Object[] { productGroupTypeID, productTypeID }, new ResultSetExtractor<double[]>() {
			
			@Override
			public double[] extractData(ResultSet result) throws SQLException, DataAccessException {
				
				double[] contractTermCredits = new double[4];
				contractTermCredits[0] = 0.00;
				contractTermCredits[1] = 0.00;
				contractTermCredits[2] = 0.00;
				contractTermCredits[3] = 0.00;

				while (result.next()) {
					if (result.getInt(2) == 1) {
						contractTermCredits[0] = result.getDouble(1);
					} else if (result.getInt(2) == 12) {
						contractTermCredits[1] = result.getDouble(1);	
					}
					if (result.getInt(2) == 24) {
						contractTermCredits[2] = result.getDouble(1);
					}
					if (result.getInt(2) == 36) {
						contractTermCredits[3] = result.getDouble(1);
					}
				}
				
				return contractTermCredits;	
			}	
		});
	}

	@Override
	public double getBaseProductPrice(String serialNumber, String province, String npa, Date activationDate) throws ApplicationException {

		double basePrice = 0.0;
		String input = "\n serialNumber :- " + serialNumber + "\n province :- " + province + "\n npa :- " + npa
				+ "\n activationDate :- " + activationDate;

		LOGGER.info(input);

		EquipmentInfo einfo = getEquipmentInfobySerialNo(serialNumber);

		LOGGER.info("einfo.getProductCode() :- " + einfo.getProductCode());
		boolean useACME = AppConfiguration.isUseAcme();

		if (useACME) {
			basePrice = getBaseProductPriceByProductCodeFromACME(einfo.getProductCode(), province, npa, activationDate);
		} else {
			basePrice = getBaseProductPriceByProductCodeFromP3MS(einfo.getProductCode(), province, activationDate);
		}
		
		return basePrice;
	}

	/**
	 * Retrieves all pieces of equipment corresponding to the provided real or
	 * pseudo serial number.
	 * 
	 * @param serialNo - any of ESN, pseudo ESN or MEID in decimal format
	 * @param checkPseudoESN - include pseudo ESN flag
	 * @return an array of EquipmentInfo objects
	 * 
	 * @throws ApplicationException
	 */
	@Override
	public EquipmentInfo[] getEquipmentInfobySerialNo(final String serialNo, final boolean checkPseudoESN) throws ApplicationException {

		List<String> sourceSerialNumbers = new ArrayList<String>();
		List<EquipmentInfo> equipmentInfos = new ArrayList<EquipmentInfo>();

		sourceSerialNumbers.add(serialNo);

		if (checkPseudoESN) {
			String[] serialNumbers = getESNByPseudoESN(serialNo);
			sourceSerialNumbers.addAll(Arrays.asList(serialNumbers));
		}

		Iterator<String> iter = sourceSerialNumbers.iterator();
		while (iter.hasNext()) {
			String serialNumber = (String) iter.next();
			EquipmentInfo equipmentInfo = getEquipmentInfobySerialNo(serialNumber);
			if (equipmentInfo != null) {
				equipmentInfos.add(equipmentInfo);
			}
		}
		
		return (EquipmentInfo[]) equipmentInfos.toArray(new EquipmentInfo[equipmentInfos.size()]);
	}

	@Override
	public String[] getESNByPseudoESN(final String pSerialNo) throws ApplicationException {
		
		String call = "{? = call client_equipment.getallesnbypseudoesn(?) }";

		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String[]>() {

			@Override
			public String[] doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				String[] serialNumbers = null;
				callable.registerOutParameter(1, Types.ARRAY, "ESN_TBL_T");
				callable.setString(2, pSerialNo);
				callable.execute();

				serialNumbers = (String[]) callable.getArray(1).getArray();

				return serialNumbers;
			}
		});
	}

	@Override
	public EquipmentInfo getEquipmentInfobySerialNo(final String serialNumber) throws ApplicationException {
		
		if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(serialNumber)) {
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.UNKNOWN_SERIAL_NUMBER, EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_EN + serialNumber, "");
		}

		String call = "{call Client_Equipment.GetEquipmentInfo(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?,?,?, " 
				+ "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?)}";
		
		try {
			return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {

				@Override
				public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

					callable.setString(1, serialNumber);
					callable.registerOutParameter(2, Types.VARCHAR);
					callable.registerOutParameter(3, Types.VARCHAR);
					callable.registerOutParameter(4, Types.VARCHAR);
					callable.registerOutParameter(5, Types.VARCHAR);
					callable.registerOutParameter(6, Types.VARCHAR);
					callable.registerOutParameter(7, Types.VARCHAR);
					callable.registerOutParameter(8, Types.NUMERIC);
					callable.registerOutParameter(9, Types.NUMERIC);
					callable.registerOutParameter(10, Types.NUMERIC);
					callable.registerOutParameter(11, Types.VARCHAR);
					callable.registerOutParameter(12, Types.NUMERIC);
					callable.registerOutParameter(13, Types.VARCHAR);
					callable.registerOutParameter(14, Types.VARCHAR);
					callable.registerOutParameter(15, Types.NUMERIC);
					callable.registerOutParameter(16, Types.VARCHAR);
					callable.registerOutParameter(17, Types.NUMERIC);
					callable.registerOutParameter(18, Types.VARCHAR);
					callable.registerOutParameter(19, Types.VARCHAR);
					callable.registerOutParameter(20, Types.NUMERIC);
					callable.registerOutParameter(21, Types.VARCHAR);
					callable.registerOutParameter(22, Types.VARCHAR);
					callable.registerOutParameter(23, Types.VARCHAR);
					callable.registerOutParameter(24, Types.VARCHAR);
					callable.registerOutParameter(25, Types.VARCHAR);
					callable.registerOutParameter(26, Types.VARCHAR);
					callable.registerOutParameter(27, Types.VARCHAR);
					callable.registerOutParameter(28, Types.VARCHAR);
					callable.registerOutParameter(29, Types.VARCHAR);
					callable.registerOutParameter(30, Types.VARCHAR);
					callable.registerOutParameter(31, Types.NUMERIC);
					callable.registerOutParameter(32, Types.VARCHAR);
					callable.registerOutParameter(33, Types.VARCHAR);
					callable.registerOutParameter(34, Types.NUMERIC);
					callable.registerOutParameter(35, Types.VARCHAR);
					callable.registerOutParameter(36, Types.VARCHAR); // product type
					callable.registerOutParameter(37, Types.VARCHAR); // equipment type
					callable.registerOutParameter(38, Types.VARCHAR); // equipment type class
					callable.registerOutParameter(39, Types.VARCHAR);
					callable.registerOutParameter(40, Types.NUMERIC);
					/*
					 * commented for pager
					 * callable.registerOutParameter(41,Types.VARCHAR);
					 * callable.registerOutParameter(42,Types.VARCHAR);
					 * callable.registerOutParameter(43,Types.NUMERIC);
					 */
					// replace 41, 42 and 43 by the following:
					callable.registerOutParameter(41, Types.VARCHAR); // pager - cap code
					callable.registerOutParameter(42, Types.VARCHAR); // pager - coverage region list
					callable.registerOutParameter(43, Types.VARCHAR); // pager - encoding format code
					callable.registerOutParameter(44, Types.VARCHAR); // pager - ownership code
					callable.registerOutParameter(45, Types.VARCHAR); // pager - prepaid
					callable.registerOutParameter(46, Types.VARCHAR); // pager - frequency cd
					callable.registerOutParameter(47, Types.VARCHAR); // firmware feature code list
					callable.registerOutParameter(48, Types.VARCHAR); // browser_protocol
					callable.registerOutParameter(49, Types.NUMERIC);
					callable.registerOutParameter(50, Types.DATE); // equipment status date
					callable.registerOutParameter(51, Types.VARCHAR); // puk code
					callable.registerOutParameter(52, Types.NUMERIC); // product_category_id
					callable.registerOutParameter(53, Types.NUMERIC); // virtual
					callable.registerOutParameter(54, Types.VARCHAR); // rim pin
					callable.registerOutParameter(55, Types.ARRAY, "BRAND_ARRAY"); // brand indicator
					// added for USIM
					callable.registerOutParameter(56, Types.VARCHAR); // assohandsetimei_for_usim
					callable.registerOutParameter(57, Types.VARCHAR); // local_imsi
					callable.registerOutParameter(58, Types.VARCHAR); // remote_imsi
					callable.registerOutParameter(59, Types.NUMERIC); // assignable
					callable.registerOutParameter(60, Types.NUMERIC); // previously_activated
					callable.registerOutParameter(61, Types.VARCHAR); // equipment_group
					callable.registerOutParameter(62, Types.VARCHAR); // last_event_type
					callable.registerOutParameter(63, Types.VARCHAR); // network type
					callable.registerOutParameter(64, Types.VARCHAR); // pre_post_paid_flag
					callable.execute();

					EquipmentInfo equipmentInfo = new EquipmentInfo();
					long[] productPromoTypeList = new long[0];
					String[] coverageRegionList = new String[0];
					String[] firmwareFeatureList = new String[0];
					
					equipmentInfo.setSerialNumber(callable.getString(2));
					equipmentInfo.setTechType(callable.getString(3));
					equipmentInfo.setProductCode(callable.getString(4));
					equipmentInfo.setProductStatusCode(callable.getString(5));
					equipmentInfo.setVendorName(callable.getString(6));
					equipmentInfo.setVendorNo(callable.getString(7));
					equipmentInfo.setEquipmentStatusTypeID(callable.getLong(8));
					equipmentInfo.setEquipmentStatusID(callable.getLong(9));
					equipmentInfo.setStolen(callable.getInt(10) == 1);
					equipmentInfo.setSublock1(callable.getString(11));
					equipmentInfo.setProductGroupTypeID(callable.getLong(12));
					equipmentInfo.setProductGroupTypeCode(callable.getString(13));
					equipmentInfo.setProductGroupTypeDescription(callable.getString(14));
					equipmentInfo.setProductTypeID(callable.getLong(15));
					equipmentInfo.setProductTypeDescription(callable.getString(16));
					equipmentInfo.setProductClassID(callable.getLong(17));
					equipmentInfo.setProductClassCode(callable.getString(18));
					equipmentInfo.setProductClassDescription(callable.getString(19));
					equipmentInfo.setProviderOwnerID(callable.getLong(20));
					equipmentInfo.setLastMuleIMEI(callable.getString(21));
					equipmentInfo.setProductName(callable.getString(22));
					equipmentInfo.setProductNameFrench(callable.getString(23));
					equipmentInfo.setBrowserVersion(callable.getString(24));
					equipmentInfo.setFirmwareVersion(callable.getString(25));
					equipmentInfo.setPRLCode(callable.getString(26));
					equipmentInfo.setPRLDescription(callable.getString(27));
					equipmentInfo.setProductGroupTypeDescriptionFrench(callable.getString(29) == null ? callable.getString(14) 
							: callable.getString(29).equals("") ? callable.getString(14) : callable.getString(29));
					equipmentInfo.setProductTypeDescriptionFrench(callable.getString(28) == null ? callable.getString(16) 
							: callable.getString(28).equals("") ? callable.getString(16) : callable.getString(28));
					// equipInfo.setSubscriberNumber(callable.getString(30));
					// equipInfo.setBanID(callable.getInt(31));
					if (!(callable.getString(32) == null)) {
						int i = callable.getString(32).length() / 8;
						productPromoTypeList = new long[i];
						for (int j = 0; j < i; j++) {
							productPromoTypeList[j] = Long.parseLong(callable.getString(32).substring(j * 8, j * 8 + 8));
						}
					}
					equipmentInfo.setProductPromoTypeList(productPromoTypeList);
					equipmentInfo.setInitialActivation("Y".equals(callable.getString(33)));
					equipmentInfo.setModeCode(callable.getLong(34));
					equipmentInfo.setModeDescription(callable.getString(35));
					equipmentInfo.setProductType(callable.getString(36) == null ? "" : callable.getString(36));
					equipmentInfo.setEquipmentType(callable.getString(37) == null ? "" : callable.getString(37));
					equipmentInfo.setModelType(callable.getString(37) == null ? "" : callable.getString(37));
					equipmentInfo.setEquipmentTypeClass(callable.getString(38) == null ? "" : callable.getString(38));
					equipmentInfo.setLegacy(equipmentInfo.isSIMCard() ? false : equipmentInfo.isMule() ? false : callable.getString(39) == null ? true 
							: "N".equals(callable.getString(39)));
					// pager info
					if (equipmentInfo.isPager()) {
						equipmentInfo.setCapCode(callable.getString(41));
						if (!(callable.getString(42) == null)) {
							StringTokenizer strToken = new StringTokenizer(callable.getString(42), "|");
							coverageRegionList = new String[strToken.countTokens()];
							int i = 0;
							while (strToken.hasMoreTokens()) {
								coverageRegionList[i] = strToken.nextToken();
								i++;
							}
						}
						equipmentInfo.setCoverageRegionCodes(coverageRegionList);
						equipmentInfo.setEncodingFormat(callable.getString(43));
						equipmentInfo.setPossession(callable.getString(44));
						equipmentInfo.setPrepaid("Y".equals(callable.getString(45)));
						equipmentInfo.setFrequencyCode(callable.getString(46));
					}					
					// firmwareVersionFeatureCodes
					if (!(callable.getString(47) == null)) {
						StringTokenizer strToken = new StringTokenizer(callable.getString(47), "|");
						firmwareFeatureList = new String[strToken.countTokens()];
						int i = 0;
						while (strToken.hasMoreTokens()) {
							firmwareFeatureList[i] = strToken.nextToken();
							i++;
						}
					}
					equipmentInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);
					// browserProtocol
					equipmentInfo.setBrowserProtocol(callable.getString(48));
					equipmentInfo.setUnscanned(callable.getInt(49) == 1 ? true : false);
					equipmentInfo.setEquipmentStatusDate(callable.getDate(50));
					equipmentInfo.setPUKCode(callable.getString(51) == null ? "" : callable.getString(51));
					equipmentInfo.setProductCategoryId(callable.getLong(52));
					equipmentInfo.setRIMPin(callable.getString(54));
					// added for USIM
					equipmentInfo.setAssociatedHandsetIMEI(callable.getString(56));
					ProfileInfo profile = new ProfileInfo();
					profile.setLocalIMSI(callable.getString(57));
					profile.setRemoteIMSI(callable.getString(58));
					equipmentInfo.setProfile(profile);
					equipmentInfo.setAvailableUSIM(callable.getInt(59) == 1);
					if (equipmentInfo.isMule()) {
						equipmentInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_DIGITAL);
					}
					equipmentInfo.setVirtual(callable.getInt(53) == 1);
					equipmentInfo.setBrandIds(getBrandArrays(callable.getArray(55)));
					equipmentInfo.setPreviouslyActivated(callable.getInt(60) == 1);
					equipmentInfo.setEquipmentGroup(callable.getString(61));
					equipmentInfo.setLastAssociatedHandsetEventType(callable.getString(62));
					equipmentInfo.setNetworkType(callable.getString(63) == null ? "" : callable.getString(63));
					equipmentInfo.setProductPrePostpaidFlag(callable.getString(64));

					return equipmentInfo;
				}
			});

		} catch (UncategorizedSQLException usqle) {
			SQLException sqle = usqle.getSQLException();
			if (sqle != null) {
				if (sqle.getErrorCode() == 20310) {
					throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.UNKNOWN_SERIAL_NUMBER, EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_EN + serialNumber, "", usqle);
				} else if (sqle.getErrorCode() == 20323) {
					throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.IMSI_NOT_FOUND, EquipmentErrorMessages.IMSI_NOT_FOUND_EN, "", usqle);
				} else if (sqle.getErrorCode() == 20315) {
					throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.MULTIPLE_ASSOCIATED_HANDSETS, EquipmentErrorMessages.MULTIPLE_ASSOCIATED_HANDSETS_EN + serialNumber, "", usqle);
				}
			}
			throw usqle;
		}
	}

	private int[] getBrandArrays(Array brandArray) throws SQLException {
		
		BigDecimal[] brandIds = null;
		if (brandArray != null) {
			brandIds = (BigDecimal[]) brandArray.getArray();
		}
		if (brandIds == null) {
			brandIds = new BigDecimal[0];
		}
		int[] brands = new int[brandIds.length];
		for (int brandCount = 0; brandCount < brandIds.length; brandCount++) {
			brands[brandCount] = brandIds[brandCount].intValue();
		}
		
		return brands;
	}

	@Override
	public double getBaseProductPriceByProductCodeFromACME(final String productCode, String province, final String npa,	Date activationDate) throws ApplicationException {

		String input = "\n productCode :- " + productCode + "\n province :- " + province + "\n npa :- " + npa
				+ "\n activationDate :- " + activationDate;

		LOGGER.info(input);

		if (activationDate == null) {
			activationDate = new java.util.Date();
		}
		final java.sql.Timestamp actDate = new Timestamp(activationDate.getTime());

		final String pvnce;
		if ("PQ".equals(province)) {
			pvnce = "QC";
		} else {
			pvnce = province;
		}

		String call = "{call Client_Equipment.getBaseProductPrice(?,?,?,?,?) }";

		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<Double>() {

			@Override
			public Double doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

				double basePrice = 0.0;
				ResultSet result = null;
				try {

					callable.setString(1, productCode);
					callable.setString(2, pvnce);
					callable.setString(3, npa);
					callable.setTimestamp(4, actDate);
					callable.registerOutParameter(5, ORACLE_REF_CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(5);
					if (result.next())
						basePrice = result.getDouble(2);
				} finally {
					if (result != null) {
						result.close();
					}
				}

				return basePrice;
			}
		});
	}

	@Override
	public double getBaseProductPriceByProductCodeFromP3MS(final String productCode, String province,
			Date activationDate) throws ApplicationException {

		String query = "SELECT CIP.CATALOGUE_ITEM_PRICE_ID, CIP.PRICE, CIPM.MODIFIER_TYPE_CD, CIPM.PRICE_MODIFIER_ID, "
				+ "       CIP.EFFECTIVE_DT, CIP.EXPIRY_DT "
				+ "FROM CI_PRICE_MODIFIER CIPM, CATALOGUE_ITEM_PRICE CIP, CATALOGUE_ITEM CI, PRODUCT P "
				+ "WHERE P.PRODUCT_ID = CI.SUB_TYPE_ID AND " + "      CI.CATALOGUE_ITEM_ID = CIP.CATALOGUE_ITEM_ID AND "
				+ "      CIP.CATALOGUE_ITEM_PRICE_ID = CIPM.CATALOGUE_ITEM_PRICE_ID (+) AND "
				+ "      P.PRODUCT_CD = ? AND " + "      CIP.PRICE_CLASS_ID = 1 AND "
				+ "      CIP.EFFECTIVE_DT <= ? AND " + "      CIP.EXPIRY_DT > ? AND " + "      NOT EXISTS "
				+ "      ( SELECT CATALOGUE_ITEM_PRICE_ID FROM CI_PRICE_MODIFIER CIPM2 "
				+ "        WHERE CIPM2.CATALOGUE_ITEM_PRICE_ID = CIPM.CATALOGUE_ITEM_PRICE_ID AND "
				+ "              CIPM2.MODIFIER_TYPE_CD <> 'REGION' ) " + "ORDER BY CIP.EFFECTIVE_DT DESC ";

		String input = "\n productCode :- " + productCode + "\n province :- " + province + "\n activationDate :- "
				+ activationDate;

		LOGGER.debug(input);

		if (activationDate == null) {
			activationDate = new java.util.Date();
		}

		final java.sql.Timestamp actDate = new Timestamp(activationDate.getTime());
		final String pvnce;

		if ("PQ".equals(province)) {
			pvnce = "QC";
		} else {
			pvnce = province;
		}

		return getDistJdbcTemplate().execute(query, new PreparedStatementCallback<Double>() {

			@Override
			public Double doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				
				ResultSet result = null;
				double basePrice = 0.0;

				try {
					pstmt.setString(1, productCode);
					pstmt.setTimestamp(2, actDate);
					pstmt.setTimestamp(3, actDate);

					result = pstmt.executeQuery();

					/**
					 * NPA will be ignored by this query.
					 */
					while (result.next()) {
						double dbPrice = result.getDouble("PRICE");
						String modifierTypeCd = result.getString("MODIFIER_TYPE_CD");
						String prieModifierId = result.getString("PRICE_MODIFIER_ID");

						if (modifierTypeCd == null) {
							basePrice = dbPrice;
						} else {
							if (modifierTypeCd.equalsIgnoreCase("REGION")) {
								long regionId = Long.parseLong(prieModifierId);
								if (isProvinceMatch(regionId, pvnce)) {
									basePrice = dbPrice;
									return basePrice;
								}
							}
						}
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return basePrice;
			}
		});
	}

	/**
	 * Return true if the province is matching the region
	 * 
	 * @return true if the province is matching the region
	 */
	private boolean isProvinceMatch(final long regionId, final String province) {

		String query = "SELECT LOCALITY_CODE, REGION_TYPE_CD, REGION_DES FROM TM_REGION " + "WHERE REGION_ID = ?";

		return getDistJdbcTemplate().execute(query, new PreparedStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {

				ResultSet result = null;

				try {
					pstmt.setLong(1, regionId);
					result = pstmt.executeQuery();

					while (result.next()) {
						String localityCode = result.getString("LOCALITY_CODE");
						String regionTypeCd = result.getString("REGION_TYPE_CD");
						String regionDes = result.getString("REGION_DES");

						if (regionTypeCd != null) {
							if (regionTypeCd.equalsIgnoreCase("REGION_GRP")) {
								StringTokenizer tokens = new StringTokenizer(regionDes, "|");
								while (tokens.hasMoreTokens()) {
									String token = tokens.nextToken();
									if (province.equalsIgnoreCase(token)) {
										return true;
									}
								}
							} else if (regionTypeCd.equalsIgnoreCase("PROVINCE")) {
								if (province.equalsIgnoreCase(localityCode)) {
									return true;
								}
							}
						}
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return false;
			}
		});

	}

	@Override
	public ActivationCreditInfo[] getDefaultActivationCredits(final String productType, String province, final String npa, final int contractTermMonths, 
			final String creditType, Date activationDate) throws ApplicationException {

		final Date atDate;
		final String pvnce;

		if (activationDate == null) {
			atDate = new java.util.Date();
		} else {
			atDate = activationDate;
		}

		if ("PQ".equals(province)) {
			pvnce = "QC";
		} else {
			pvnce = province;
		}

		StringBuffer buff = new StringBuffer(1024);

		/**
		 * credit by npa has higher priority and ignore the second one. If they
		 * all the same but just with different effective date then write errors
		 * note the sorting sequence, as it compares to the previous row.
		 */

		buff.append("select /*+ RULE */ 'ANPA' s_credit_reason, cd.credit_type_cd s_credit_type, ");
		buff.append("nvl(cd.contract_term_id, 0) s_credit_term_id, ");
		buff.append("cd.credit_master_id, ");
		buff.append("cd.credit_detail_id, ");
		buff.append("cd.effective_dt s_eff_date, ");
		buff.append("cd.expiry_dt, ");
		buff.append("ct.credit_type_des, ");
		buff.append("ct.credit_type_des_french, ");
		buff.append("ca.credit_amount,  ");
		buff.append("ca.bar_code ");
		buff.append("from ");
		buff.append("credit_type ct, ");
		buff.append("credit_amount ca, ");
		buff.append("credit_detail cd, ");
		buff.append("credit_region_area_code crac, ");
		buff.append("credit_master cm ");
		buff.append("where ");
		buff.append("cm.product_gp_type_id  = (select product_gp_type_id from product_group_type where  product_gp_type_cd = ? )  ");
		buff.append("and cm.region_group_id = crac.region_group_id ");
		buff.append("and cm.effective_dt <= ? ");
		buff.append("and (cm.expiry_dt is null or cm.expiry_dt > ? ) ");
		buff.append("and crac.npa_code = to_number( ? )  ");
		buff.append("and cd.credit_master_id = cm.credit_master_id  ");
		buff.append("and cd.effective_dt <= ? ");
		buff.append("and (cd.expiry_dt is null or cd.expiry_dt > ? )  ");
		buff.append("and cd.credit_type_cd like ?  ");
		if (contractTermMonths != CONTRACT_TERM_DONT_CARE) {
			buff.append(" and cd.contract_term_id = (select ccc.contract_term_id from contract_term ccc where ccc.duration = ").append(contractTermMonths);
			buff.append("    and ccc.unit_type_cd = 'MTH' ) ");
		}
		buff.append("and ca.credit_type_cd = cd.credit_type_cd ");
		buff.append("and ca.credit_amount_id = cd.credit_amount_id  ");
		buff.append("and ct.credit_type_cd = ca.credit_type_cd  ");
		buff.append("union ");
		buff.append("select /*+ RULE */ 'ZPRO' s_credit_reason, cd.credit_type_cd s_credit_type, ");
		buff.append("nvl(cd.contract_term_id, 0) s_credit_term_id, ");
		buff.append("cd.credit_master_id, ");
		buff.append("cd.credit_detail_id, ");
		buff.append("cd.effective_dt s_eff_date, ");
		buff.append("cd.expiry_dt, ");
		buff.append("ct.credit_type_des, ");
		buff.append("ct.credit_type_des_french, ");
		buff.append("ca.credit_amount, ");
		buff.append("ca.bar_code  ");
		buff.append("from ");
		buff.append("credit_type ct, ");
		buff.append("credit_amount ca, ");
		buff.append("credit_detail cd, ");
		buff.append("credit_region_province crp, ");
		buff.append("credit_master cm  ");
		buff.append("where  ");
		buff.append("cm.product_gp_type_id  = (select product_gp_type_id from product_group_type where  product_gp_type_cd = ? )  ");
		buff.append("and cm.region_group_id = crp.region_group_id  ");
		buff.append("and cm.effective_dt <= ? ");
		buff.append("and (cm.expiry_dt is null or cm.expiry_dt > ? ) ");
		buff.append("and crp.province_cd = ?  ");
		buff.append("and cd.credit_master_id = cm.credit_master_id  ");
		buff.append("and cd.effective_dt <= ?  ");
		buff.append("and (cd.expiry_dt is null or cd.expiry_dt > ? )  ");
		buff.append("and cd.credit_type_cd like ?  ");
		if (contractTermMonths != CONTRACT_TERM_DONT_CARE) {
			buff.append(" and cd.contract_term_id = (select ccc.contract_term_id from contract_term ccc where ccc.duration = ").append(contractTermMonths);
			buff.append("    and ccc.unit_type_cd = 'MTH' ) ");
		}
		buff.append("and ca.credit_type_cd = cd.credit_type_cd  ");
		buff.append("and ca.credit_amount_id = cd.credit_amount_id   ");
		buff.append("and ct.credit_type_cd = ca.credit_type_cd  ");
		buff.append("order by   s_credit_type asc ,  s_credit_term_id asc , s_credit_reason asc, s_eff_date desc ");

		ActivationCreditInfo[] actCreditInfoArray = getDistJdbcTemplate().execute(buff.toString(), new PreparedStatementCallback<ActivationCreditInfo[]>() {

			@Override
			public ActivationCreditInfo[] doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {

				ResultSet result = null;
				ActivationCreditInfo[] actCreditInfoArray = null;
				ActivationCreditInfo actCreditInfo = null;
				Collection<ActivationCreditInfo> actCreditInfoList = new ArrayList<ActivationCreditInfo>();
				String DEFAULT_ACTIVATION_CREDIT = "DACR";

				String l_creditReasonSave = " ";
				String l_creditTypeSave = " ";
				int l_contractTermSave = -2;

				String l_creditReason = "";
				String l_creditType = "";
				int l_contractTerm = -1;
				
				try {
					java.sql.Timestamp actDate = new Timestamp(atDate.getTime());

					String input = "\n productType :- " + productType + "\n province :- " + pvnce + "\n npa :- " + npa + "\n contractTermMonths :- " + contractTermMonths + "\n creditType :- "
							+ creditType + "\n activationDate :- " + actDate;

					LOGGER.info("INPUT: " + input);

					statement.setString(1, DEFAULT_ACTIVATION_CREDIT);
					statement.setTimestamp(2, actDate);
					statement.setTimestamp(3, actDate);
					statement.setString(4, npa);
					statement.setTimestamp(5, actDate);
					statement.setTimestamp(6, actDate);
					statement.setString(7, creditType);

					statement.setString(8, DEFAULT_ACTIVATION_CREDIT);
					statement.setTimestamp(9, actDate);
					statement.setTimestamp(10, actDate);

					statement.setString(11, pvnce);
					statement.setTimestamp(12, actDate);
					statement.setTimestamp(13, actDate);
					statement.setString(14, creditType);

					result = statement.executeQuery();

					while (result.next()) {

						l_creditReason = result.getString(1);
						l_creditType = result.getString(2);
						l_contractTerm = result.getInt(3);

						if (l_creditReason.equals(l_creditReasonSave) && l_creditType.equals(l_creditTypeSave) && l_contractTerm == l_contractTermSave) {

							// just different in effective date, take the latest one and throw it away
							String bodytext = "Input parameters :- " + input + "\n\nERROR DUPLICATE DATA IN CREDIT TABLES same prov/npa, credit type, contract term " + "\n Row credit_master_id : "
									+ result.getLong(4) + " , credit_detail_id : " + result.getLong(5) + " \n credit reason : by " + l_creditReason.substring(1) + " , credit type : " + l_creditType
									+ " , contract term : " + l_contractTerm;

							LOGGER.info(bodytext);

							continue;
						}

						if (!l_creditReason.equals(l_creditReasonSave) && l_creditType.equals(l_creditTypeSave) && l_contractTerm == l_contractTermSave) {
							LOGGER.info("IGNORE diff prov/npa, same credit type, contract term " + " , Row credit_master_id : " + result.getLong(4) + " , credit_detail_id : " + result.getLong(5)
									+ " , credit reason : " + l_creditReason.substring(1) + " , creedit type : " + l_creditType + " , contract term : " + l_contractTerm);

							continue;
						}

						l_creditReasonSave = l_creditReason;
						l_creditTypeSave = l_creditType;
						l_contractTermSave = l_contractTerm;

						actCreditInfo = new ActivationCreditInfo(ActivationCreditInfo.TAX_OPTION_NO_TAX);

						actCreditInfo.setCode(l_creditType);
						actCreditInfo.setCreditType(l_creditType);

						actCreditInfo.setContractTerm(l_contractTerm);

						actCreditInfo.setEffectiveDate(result.getDate(6));
						actCreditInfo.setExpiryDate(result.getDate(7));

						actCreditInfo.setDescription(result.getString(8));
						actCreditInfo.setText(result.getString(8)); // assume
																	// english

						actCreditInfo.setDescriptionFrench(result.getString(9));

						actCreditInfo.setAmount(result.getDouble(10));
						actCreditInfo.setBarCode(result.getString(11));

						actCreditInfo.setProductType(productType);

						actCreditInfoList.add(actCreditInfo);
					}

					actCreditInfoArray = (ActivationCreditInfo[]) actCreditInfoList.toArray(new ActivationCreditInfo[0]);

					actCreditInfoList = null;

				} finally {
					if (result != null) {
						result.close();
					}
				}
				
				return actCreditInfoArray;
			}
		});

		if (actCreditInfoArray == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO, EquipmentErrorMessages.DEFAULT_ACTIVATION_CREDIT_NULL_EN, "");
		}

		return actCreditInfoArray;
	}

	@Override
	public ActivationCreditInfo[] getActivationCredits(String serialNumber, String province, String npa, int contractTermMonths, String creditType, Date activationDate) throws ApplicationException {
		
		String methodName = "getActivationCredits";
		String className = getClass().getName();

		String input = "\n serialNumber :- " + serialNumber + "\n province :- " + province + "\n npa :- " + npa
				+ "\n contractTermMonths :- " + contractTermMonths + "\n creditType :- " + creditType
				+ "\n activationDate :- " + activationDate;

		LOGGER.debug("(" + className + "." + methodName + ") " + input);

		EquipmentInfo einfo = getEquipmentInfobySerialNo(serialNumber);
		boolean isInitialActivation = false;

		String productType = einfo.getProductType();

		// do our own way due to family grouping just get product code
		String productCode = einfo.getProductCode();

		LOGGER.debug("(" + className + "." + methodName + ") " + "einfo.getProductCode() :- " + productCode
				+ " einfo.isInitialActivation() :- " + einfo.isInitialActivation());

		isInitialActivation = einfo.isInitialActivation();
		boolean useACME = AppConfiguration.isUseAcme();
		if (useACME) {
			return getActivationCreditsByProductCodeFromACME(productCode, province, npa, contractTermMonths, creditType, activationDate, productType, isInitialActivation);
		} else {
			return getActivationCreditsByProductCodeFromP3MS(productCode, province, contractTermMonths, creditType, activationDate, productType, isInitialActivation);
		}
	}

	@Override
	public ActivationCreditInfo[] getActivationCreditsByProductCodeFromACME(final String productCode, String province, final String npa, final int contractTermMonths, 
			final String creditType, Date activationDate, final String productType, final boolean isInitialActivation) throws ApplicationException {

		final Date atDate;
		final String pvnce;

		if (activationDate == null) {
			atDate = new java.util.Date();
		} else {
			atDate = activationDate;
		}

		if ("PQ".equals(province)) {
			pvnce = "QC";
		} else {
			pvnce = province;
		}

		StringBuffer buff = new StringBuffer(1024);

		/**
		 * credit by npa has higher priority and ignore the second one. If they
		 * all the same but just with different effective date then write errors
		 * note the sorting sequence, as it compares to the previous row.
		 */

		buff.append("select /*+ RULE */ 'ANPA' s_credit_reason, cd.credit_type_cd s_credit_type, ");
		buff.append("nvl(cd.contract_term_id, 0) s_credit_term_id, ");
		buff.append("cd.credit_master_id, ");
		buff.append("cd.credit_detail_id, ");
		buff.append("cd.effective_dt s_eff_date, ");
		buff.append("cd.expiry_dt, ");
		buff.append("ct.credit_type_des, ");
		buff.append("ct.credit_type_des_french, ");
		buff.append("ca.credit_amount,  ");
		buff.append("ca.bar_code ");
		buff.append("from ");
		buff.append("credit_type ct, ");
		buff.append("credit_amount ca, ");
		buff.append("credit_detail cd, ");
		buff.append("credit_region_area_code crac, ");
		buff.append("credit_master cm, ");
		buff.append("product_type pt, ");
		buff.append("product_family_group pfg, ");
		buff.append("product pro ");
		buff.append("where ");
		buff.append("pro.product_cd = ? ");
		buff.append("and pfg.product_id  = pro.product_id ");
		buff.append("and pt.product_type_id = pfg.product_type_id ");
		buff.append("and pt.product_gp_type_id = pfg.product_gp_type_id ");
		buff.append("and cm.product_gp_type_id = pt.product_gp_type_id ");
		buff.append("and cm.product_type_id = pt.product_type_id ");
		buff.append("and cm.region_group_id = crac.region_group_id ");
		buff.append("and cm.effective_dt <= ? ");
		buff.append("and (cm.expiry_dt is null or cm.expiry_dt > ? ) ");
		buff.append("and crac.npa_code = to_number( ? )  ");
		buff.append("and cd.credit_master_id = cm.credit_master_id  ");
		buff.append("and cd.effective_dt <= ? ");
		buff.append("and (cd.expiry_dt is null or cd.expiry_dt > ? ) ");
		buff.append("and cd.credit_type_cd like ?  ");
		if (contractTermMonths != CONTRACT_TERM_DONT_CARE) {
			buff.append(" and cd.contract_term_id = (select ccc.contract_term_id from contract_term ccc where ccc.duration = ").append(contractTermMonths);
			buff.append("    and ccc.unit_type_cd = 'MTH' ) ");
		}
		buff.append("and ca.credit_type_cd = cd.credit_type_cd ");
		buff.append("and ca.credit_amount_id = cd.credit_amount_id  ");
		buff.append("and ct.credit_type_cd = ca.credit_type_cd  ");
		buff.append("union ");
		buff.append("select /*+ RULE */ 'ZPRO' s_credit_reason, cd.credit_type_cd s_credit_type, ");
		buff.append("nvl(cd.contract_term_id, 0) s_credit_term_id, ");
		buff.append("cd.credit_master_id, ");
		buff.append("cd.credit_detail_id, ");
		buff.append("cd.effective_dt s_eff_date, ");
		buff.append("cd.expiry_dt, ");
		buff.append("ct.credit_type_des, ");
		buff.append("ct.credit_type_des_french, ");
		buff.append("ca.credit_amount, ");
		buff.append("ca.bar_code  ");
		buff.append("from ");
		buff.append("credit_type ct, ");
		buff.append("credit_amount ca, ");
		buff.append("credit_detail cd, ");
		buff.append("credit_region_province crp, ");
		buff.append("credit_master cm, ");
		buff.append("product_type pt, ");
		buff.append("product_family_group pfg, ");
		buff.append("product pro ");
		buff.append("where ");
		buff.append("pro.product_cd = ? ");
		buff.append("and pfg.product_id  = pro.product_id ");
		buff.append("and pt.product_type_id = pfg.product_type_id ");
		buff.append("and pt.product_gp_type_id = pfg.product_gp_type_id ");
		buff.append("and cm.product_gp_type_id = pt.product_gp_type_id ");
		buff.append("and cm.product_type_id = pt.product_type_id ");
		buff.append("and cm.region_group_id = crp.region_group_id  ");
		buff.append("and cm.effective_dt <= ? ");
		buff.append("and (cm.expiry_dt is null or cm.expiry_dt > ? ) ");
		buff.append("and crp.province_cd = ?  ");
		buff.append("and cd.credit_master_id = cm.credit_master_id  ");
		buff.append("and cd.effective_dt <= ?  ");
		buff.append("and (cd.expiry_dt is null or cd.expiry_dt > ? )  ");
		buff.append("and cd.credit_type_cd like ?  ");
		if (contractTermMonths != CONTRACT_TERM_DONT_CARE) {
			buff.append(" and cd.contract_term_id = (select ccc.contract_term_id from contract_term ccc where ccc.duration = ").append(contractTermMonths);
			buff.append("    and ccc.unit_type_cd = 'MTH' ) ");
		}
		buff.append("and ca.credit_type_cd = cd.credit_type_cd  ");
		buff.append("and ca.credit_amount_id = cd.credit_amount_id   ");
		buff.append("and ct.credit_type_cd = ca.credit_type_cd  ");
		buff.append("order by   s_credit_type asc ,  s_credit_term_id asc , s_credit_reason asc, s_eff_date desc ");

		ActivationCreditInfo[] actCreditInfoArray = getDistJdbcTemplate().execute(buff.toString(), new PreparedStatementCallback<ActivationCreditInfo[]>() {
			
			@Override
			public ActivationCreditInfo[] doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {

				ResultSet result = null;
				ActivationCreditInfo[] actCreditInfoArray;
				ActivationCreditInfo actCreditInfo = null;
				Collection<ActivationCreditInfo> actCreditInfoList = new ArrayList<ActivationCreditInfo>();
				String l_creditReasonSave = " ";
				String l_creditTypeSave = " ";
				int l_contractTermSave = -2;

				String l_creditReason = "";
				String l_creditType = "";
				int l_contractTerm = -1;

				String input = "\n productCode :- " + productCode + "\n province :- " + pvnce + "\n npa :- " + npa + "\n contractTermMonths :- " + contractTermMonths + "\n creditType :- " + creditType
						+ "\n activationDate :- " + atDate;

				LOGGER.info("INPUT: " + input);

				try {
					java.sql.Timestamp actDate = new Timestamp(atDate.getTime());

					statement.setString(1, productCode);
					statement.setTimestamp(2, actDate);
					statement.setTimestamp(3, actDate);
					statement.setString(4, npa);
					statement.setTimestamp(5, actDate);
					statement.setTimestamp(6, actDate);
					statement.setString(7, creditType);
					statement.setString(8, productCode);
					statement.setTimestamp(9, actDate);
					statement.setTimestamp(10, actDate);
					statement.setString(11, pvnce);
					statement.setTimestamp(12, actDate);
					statement.setTimestamp(13, actDate);
					statement.setString(14, creditType);

					result = statement.executeQuery();

					while (result.next()) {
						l_creditReason = result.getString(1);
						l_creditType = result.getString(2);
						l_contractTerm = result.getInt(3);

						if (isInitialActivation == false) {
							// Do not allow Activation/Promotion/Priceplan credits for old handset activation
							if (ActivationCreditInfo.CREDIT_TYPE_NEW_ACTIVATION.equals(l_creditType) || ActivationCreditInfo.CREDIT_TYPE_PROMOTION.equals(l_creditType)
									|| ActivationCreditInfo.CREDIT_TYPE_PRICE_PLAN.equals(l_creditType)) {
								LOGGER.info("Not Initial Activation, no credit for " + l_creditType);
								continue;
							}
						}

						if (l_creditReason.equals(l_creditReasonSave) && l_creditType.equals(l_creditTypeSave) && l_contractTerm == l_contractTermSave) {
							// just different in effective date, take the latest one and throw it away
							String bodytext = "Input parameters :- " + input + "\n\nERROR DUPLICATE DATA IN CREDIT TABLES same prov/npa, credit type, contract term " + "\n Row credit_master_id : "
									+ result.getLong(4) + " , credit_detail_id : " + result.getLong(5) + " \n credit reason : by " + l_creditReason.substring(1) + " , credit type : " + l_creditType
									+ " , contract term : " + l_contractTerm;
							LOGGER.warn(bodytext);
							continue;
						}

						if (!l_creditReason.equals(l_creditReasonSave) && l_creditType.equals(l_creditTypeSave) && l_contractTerm == l_contractTermSave) {
							LOGGER.info("IGNORE diff prov/npa, same credit type, contract term " + " , Row credit_master_id : " + result.getLong(4) + " , credit_detail_id : " + result.getLong(5)
									+ " , credit reason : " + l_creditReason.substring(1) + " , creedit type : " + l_creditType + " , contract term : " + l_contractTerm);
							continue;
						}

						l_creditReasonSave = l_creditReason;
						l_creditTypeSave = l_creditType;
						l_contractTermSave = l_contractTerm;

						actCreditInfo = new ActivationCreditInfo(ActivationCreditInfo.TAX_OPTION_NO_TAX);

						actCreditInfo.setCode(l_creditType);
						actCreditInfo.setCreditType(l_creditType);

						actCreditInfo.setContractTerm(l_contractTerm);

						actCreditInfo.setEffectiveDate(result.getDate(6));
						actCreditInfo.setExpiryDate(result.getDate(7));

						actCreditInfo.setDescription(result.getString(8));
						actCreditInfo.setText(result.getString(8)); // assume
																	// english

						actCreditInfo.setDescriptionFrench(result.getString(9));

						actCreditInfo.setAmount(result.getDouble(10));
						actCreditInfo.setBarCode(result.getString(11));

						actCreditInfo.setProductType(productType);

						actCreditInfoList.add(actCreditInfo);
					}

					actCreditInfoArray = (ActivationCreditInfo[]) actCreditInfoList.toArray(new ActivationCreditInfo[0]);

					actCreditInfoList = null;

				} finally {
					if (result != null) {
						result.close();
					}
				}
				
				return actCreditInfoArray;
			}
		});

		if (actCreditInfoArray == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO, "getActivationCredit 1 cannot return null", "");
		}

		if (actCreditInfoArray.length == 0) {
			actCreditInfoArray = getDefaultActivationCredits(productType, province, npa, contractTermMonths, creditType, activationDate);
		}

		if (actCreditInfoArray == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO, "getActivationCredit 2 cannot return null", "");
		}

		return actCreditInfoArray;
	}

	@Override
	public ActivationCreditInfo[] getActivationCreditsByProductCodeFromP3MS(final String productCode, String province, final int contractTermMonths, final String creditType,
			Date activationDate, final String productType, final boolean isInitialActivation) throws ApplicationException {

		final Date atDate;
		final String pvnce;
		final String methodName = "getActivationCreditsByProductCode";
		final String className = getClass().getName();

		final String input = "\n productCode :- " + productCode + "\n province :- " + province
				+ "\n contractTermMonths :- " + contractTermMonths + "\n creditType :- " + creditType
				+ "\n activationDate :- " + activationDate;
		LOGGER.debug("(" + className + "." + methodName + ") " + input);

		if (activationDate == null) {
			atDate = new java.util.Date();
		} else {
			atDate = activationDate;
		}

		if ("PQ".equals(province)) {
			pvnce = "QC";
		} else {
			pvnce = province;
		}

		String call = "{call CREDIT_CONDITION_PKG.get_credit_conditions(?, ?, ?, ?)}";

		ActivationCreditInfo[] actCreditInfoArray = getDistJdbcTemplate().execute(call, new CallableStatementCallback<ActivationCreditInfo[]>() {

			@Override
			public ActivationCreditInfo[] doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

				ResultSet result = null;
				ActivationCreditInfo[] actCreditInfoArray = null;

				try {
					HashMap<Long, CiPromotionInfo> ciPromoMap = getCiPromoMapInListByProductCdFromP3MS(productCode, atDate);

					Collection<ActivationCreditInfo> actCreditInfoList = new ArrayList<ActivationCreditInfo>();

					String l_creditTypeSave = " ";
					int l_contractTermSave = -2;

					String l_creditType = "";
					int l_contractTerm = -1;

					if (ciPromoMap.isEmpty()) {
						return new ActivationCreditInfo[0];
					}

					Long[] ciPromoIds = (Long[]) ciPromoMap.keySet().toArray(new Long[0]);

					boolean excludeExpired = false;

					callable.setTimestamp(1, atDate == null ? null : new Timestamp(atDate.getTime()));
					callable.setString(2, excludeExpired == false ? "N" : "Y");
					ArrayDescriptor memoIdArrayDesc = ArrayDescriptor.createDescriptor("DISTADM.CHILDIDARRAYTYPE", callable.getConnection());
					ARRAY childIdArray = new ARRAY(memoIdArrayDesc, callable.getConnection(), ciPromoIds);
					callable.setArray(3, childIdArray);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.execute();

					result = (ResultSet) callable.getObject(4);

					HashMap<Long, ActivationCreditInfo> ciPromoIdCreditMap = new HashMap<Long, ActivationCreditInfo>();
					ArrayList<Long> restrictedCiPromoList = new ArrayList<Long>();

					while (result.next()) {
						
						long dbCatalogueItemPromotionId = result.getLong(1);
						String dbModifierTypeCd = result.getString(2);

						ActivationCreditInfo tmpActCreditInfo = (ActivationCreditInfo) ciPromoIdCreditMap.get(new Long(dbCatalogueItemPromotionId));
						if (tmpActCreditInfo == null) {
							tmpActCreditInfo = new ActivationCreditInfo(ActivationCreditInfo.TAX_OPTION_NO_TAX);
							ciPromoIdCreditMap.put(new Long(dbCatalogueItemPromotionId), tmpActCreditInfo);
						}

						if (dbModifierTypeCd.equalsIgnoreCase("BEHAVIOUR")) {
							
							double dbCreditAmt = result.getDouble(4);
							String dbCreditType = StringUtil.emptyFromNull(result.getString(5)).toUpperCase();
							String dbBarCode = result.getString(6);

							CiPromotionInfo ciPromo = (CiPromotionInfo) ciPromoMap.get(new Long(dbCatalogueItemPromotionId));

							String dbCreditTypeEnglishDesc = ciPromo.getPromotionTypeDes();
							String dbCreditTypeFrenchDesc = ciPromo.getPromotionTypeDesFrench();
							Timestamp effectiveTs = ciPromo.getEffectiveTs();
							Timestamp expirationTs = ciPromo.getExpirationTs();

							tmpActCreditInfo.setCode(dbCreditType);
							tmpActCreditInfo.setAmount(dbCreditAmt);
							tmpActCreditInfo.setCreditType(dbCreditType);
							tmpActCreditInfo.setBarCode(dbBarCode);
							tmpActCreditInfo.setDescription(dbCreditTypeEnglishDesc);
							tmpActCreditInfo.setText(dbCreditTypeEnglishDesc);
							tmpActCreditInfo.setDescriptionFrench(dbCreditTypeFrenchDesc);

							if (effectiveTs != null) {
								tmpActCreditInfo.setEffectiveDate(new java.util.Date(effectiveTs.getTime()));
							}

							if (expirationTs != null) {
								tmpActCreditInfo.setExpiryDate(new java.util.Date(expirationTs.getTime()));
							}

						} else if (dbModifierTypeCd.equalsIgnoreCase("CONTR_TERM")) {
							int dbContractTermId = result.getInt(3);
							tmpActCreditInfo.setContractTerm(dbContractTermId);
						} else if (dbModifierTypeCd.equalsIgnoreCase("REGION")) {
							String localityCode = result.getString(4);
							String regionDes = result.getString(5);
							String regionTypeCd = result.getString(6);

							if (regionTypeCd.equalsIgnoreCase("PROVINCE")) {
								if (!localityCode.equalsIgnoreCase(pvnce)) {
									restrictedCiPromoList.add(new Long(dbCatalogueItemPromotionId));
								}
							} else if (regionTypeCd.equalsIgnoreCase("REGION_GRP")) {
								if (regionDes.indexOf(pvnce) < 0) {
									restrictedCiPromoList.add(new Long(dbCatalogueItemPromotionId));
								}
							}
						} else if (dbModifierTypeCd.equalsIgnoreCase("CUSTACTTYP")) {
							restrictedCiPromoList.add(new Long(dbCatalogueItemPromotionId));
						}
					}

					Object[] keys = ciPromoIdCreditMap.keySet().toArray();
					for (int i = 0; i < keys.length; i++) {
						
						if (restrictedCiPromoList.contains(keys[i])) {
							continue;
						}

						ActivationCreditInfo tmpActCreditInfo = (ActivationCreditInfo) ciPromoIdCreditMap.get(keys[i]);

						l_creditType = tmpActCreditInfo.getCreditType();
						l_contractTerm = tmpActCreditInfo.getContractTerm();

						if (isInitialActivation == false) {
							// Do not allow Activation/Promotion/Priceplan credits for old handset activation
							if (ActivationCreditInfo.CREDIT_TYPE_NEW_ACTIVATION.equals(l_creditType) || ActivationCreditInfo.CREDIT_TYPE_PROMOTION.equals(l_creditType)
									|| ActivationCreditInfo.CREDIT_TYPE_PRICE_PLAN.equals(l_creditType)) {
								LOGGER.debug("(" + className + "." + methodName + ") " + "Not Initial Activation, no credit for " + l_creditType);
								continue;
							}
						}

						if (l_creditType.equals(l_creditTypeSave) && l_contractTerm == l_contractTermSave) {
							// just different in effective date, take the latest one and throw it away
							String bodytext = "Input parameters :- " + input + "\n\nERROR DUPLICATE DATA IN CREDIT TABLES same prov, credit type, contract term " + "\n Product Code : by "
									+ productCode + " , credit type : " + l_creditType + " , contract term : " + l_contractTerm + "\n DB:- " + callable.getConnection().getMetaData().getURL();
							LOGGER.info(className + " -- " + methodName + " -- " + bodytext);
							continue;
						}

						l_creditTypeSave = l_creditType;
						l_contractTermSave = l_contractTerm;

						tmpActCreditInfo.setProductType(productType);

						boolean bAdd = false;
						if (contractTermMonths == -1 || contractTermMonths == l_contractTerm) {
							bAdd = true;
							// TODO what is the purpose of this conditional statement?!?!?
							if (creditType.equalsIgnoreCase("%") || creditType.equalsIgnoreCase(l_creditType)) {
								bAdd = true;
							}
						}

						if (bAdd) {
							actCreditInfoList.add(tmpActCreditInfo);
						}
					}
					actCreditInfoArray = (ActivationCreditInfo[]) actCreditInfoList.toArray(new ActivationCreditInfo[0]);
					actCreditInfoList = null;

				} finally {
					if (result != null) {
						result.close();
					}
				}
				
				return actCreditInfoArray;
			}
		});

		if (actCreditInfoArray == null) {
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO, "getActivationCredit 1 cannot return null", "");
		}

		return actCreditInfoArray;
	}

	private HashMap<Long, CiPromotionInfo> getCiPromoMapInListByProductCdFromP3MS(final String productCode,	Date activationDate) {
		
		final Date atDate;
		String methodName = "getCiPromoMapInListByProductCdFromP3MS";
		String className = getClass().getName();

		String input = "\n v.2 productCode :- " + productCode + "\n activationDate :- " + activationDate;

		LOGGER.debug("(" + className + "." + methodName + ") " + input);
		if (activationDate == null) {
			atDate = new Date();
		} else {
			atDate = activationDate;
		}
		String query = "SELECT /*+ INDEX(P I_PRODUCT_01 CIP I02_CATALOGUE_ITEM_PROMOTION)*/ "
				+ "  DISTINCT CIP.CATALOGUE_ITEM_ID, P.PROGRAM_ID, P.PROMOTION_ID, CIP.CATALOGUE_ITEM_PROMOTION_ID, "
				+ "  P.EFFECTIVE_DT, P.EXPIRATION_DT,P.USER_LAST_MODIFY, PT.DES, PT.PROMOTION_TYP_FR_DESC_TXT "
				+ "FROM PRODUCT PRO,CI_RELATIONSHIP CIR, CATALOGUE_ITEM CIC, "
				+ "     CATALOGUE_ITEM_PROMOTION CIP, PROMOTION P, PROMOTION_TYPE PT " + "WHERE PRO.PRODUCT_CD= :b1 "
				+ "AND PRO.PRODUCT_ID=CIC.SUB_TYPE_ID " + "AND CIC.CATALOGUE_ITEM_ID=CIR.CATALOGUE_CHILD_ITEM_ID "
				+ "AND CIR.RELATIONSHIP_TYPE_CD ='ACE_GROUP' "
				+ "AND CIR.CATALOGUE_PARENT_ITEM_ID = CIP.CATALOGUE_ITEM_ID " + "AND CIP.PROMOTION_ID = P.PROMOTION_ID "
				+ "AND P.PROMOTION_TYPE_CD = PT.PROMOTION_TYPE_CD " + "AND P.EFFECTIVE_DT <= ? AND P.EXPIRATION_DT > ? "
				+ "ORDER BY P.EFFECTIVE_DT DESC ";
		
		return getDistJdbcTemplate().execute(query, new PreparedStatementCallback<HashMap<Long, CiPromotionInfo>>() {
		
			@Override
			public HashMap<Long, CiPromotionInfo> doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {
				HashMap<Long, CiPromotionInfo> ciPromoMap = new HashMap<Long, CiPromotionInfo>();
				ResultSet result = null;
				try {
					statement.setString(1, productCode);
					statement.setTimestamp(2, new Timestamp(atDate.getTime()));
					statement.setTimestamp(3, new Timestamp(atDate.getTime()));

					result = statement.executeQuery();

					while (result.next()) {
						// long catalogueItemGroupId = result.getLong("CATALOGUE_ITEM_ID");
						// long programId = result.getLong("PROGRAM_ID");
						long promotionId = result.getLong("PROMOTION_ID");
						long catalogueItemPromotionId = result.getLong("CATALOGUE_ITEM_PROMOTION_ID");
						Timestamp effectiveTs = result.getTimestamp("EFFECTIVE_DT");
						Timestamp expirationt = result.getTimestamp("EXPIRATION_DT");
						String des = result.getString("DES");
						String desFr = result.getString("promotion_typ_fr_desc_txt");

						CiPromotionInfo info = new CiPromotionInfo();
						info.setCatalogueItemPromotionId(new Long(catalogueItemPromotionId));
						info.setPromotionId(new Long(promotionId));
						info.setEffectiveTs(effectiveTs);
						info.setExpirationTs(expirationt);
						info.setPromotionTypeDes(des);
						info.setPromotionTypeDesFrench(desFr);

						ciPromoMap.put(new Long(catalogueItemPromotionId), info);
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return ciPromoMap;
			}
		});
	}

}