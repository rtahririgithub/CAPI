package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AddressDao;
import com.telus.eas.account.info.AddressInfo;

public class AddressDaoImpl extends MultipleJdbcDaoTemplateSupport implements AddressDao {

	@Override
	public AddressInfo retrieveSubscriberAddress(int ban, String subscriber) {
		String sql= "SELECT ADR_PRIMARY_LN, "
            + "       ADR_CITY, "
            + "       ADR_PROVINCE, "
            + "       ADR_POSTAL, "
            + "       ADR_SECONDARY_LN, "
            + "       ADR_COUNTRY, "
            + "       ADR_ZIP_GEO_CODE, "
            + "       ADR_STATE_CODE, "
            + "       CIVIC_NO, "
            + "       CIVIC_NO_SUFFIX, "
            + "       ADR_ST_DIRECTION, "
            + "       ADR_STREET_NAME, "
            + "       ADR_STREET_TYPE, "
            + "       ADR_DESIGNATOR, "
            + "       ADR_IDENTIFIER, "
            + "       ADR_BOX, "
            + "       UNIT_DESIGNATOR, "
            + "       UNIT_IDENTIFIER, "
            + "       ADR_AREA_NM, "
            + "       ADR_QUALIFIER, "
            + "       ADR_SITE, "
            + "       ADR_COMPARTMENT, "
            + "       ADR_ATTENTION, "
            + "       ADR_DELIVERY_TP, "
            + "       ADR_GROUP, "
            + "       ADR_TYPE "
            + "  FROM address_data "
            + " WHERE address_id IN (SELECT address_id "
            + "                        FROM address_name_link "
            + "                       WHERE ban = ? "
            + "                         AND subscriber_no = ? "
            + "                         AND link_type = 'U' "
            + "                         AND NVL(expiration_date, TO_DATE('4700/12/31', 'YYYY/MM/DD')) >= SYSDATE)";
		return super.getKnowbilityJdbcTemplate().query(sql,new Object[]{ban,subscriber}, new ResultSetExtractor<AddressInfo>(){

			@Override
			public AddressInfo extractData(ResultSet result) throws SQLException,
					DataAccessException {
				AddressInfo addressInfo = null;
				if(result.next()){
	                addressInfo = new AddressInfo();

	                addressInfo.setPrimaryLine(result.getString(1));
	                addressInfo.setCity(result.getString(2));
	                addressInfo.setProvince(result.getString(3));
	                addressInfo.setPostalCode(result.getString(4));
	                addressInfo.setSecondaryLine(result.getString(5));
	                addressInfo.setCountry(result.getString(6));
	                addressInfo.setZipGeoCode(result.getString(7));
	                addressInfo.setForeignState(result.getString(8));
	                addressInfo.setCivicNo(result.getString(9));
	                addressInfo.setCivicNoSuffix(result.getString(10));
	                addressInfo.setStreetDirection(result.getString(11));
	                addressInfo.setStreetName(result.getString(12));
	                addressInfo.setStreetType(result.getString(13));
	                addressInfo.setRrDesignator(result.getString(14));
	                addressInfo.setRrIdentifier(result.getString(15));
	                addressInfo.setPoBox(result.getString(16));
	                addressInfo.setUnitDesignator(result.getString(17));
	                addressInfo.setUnitIdentifier(result.getString(18));
	                addressInfo.setRrAreaNumber(result.getString(19));
	                addressInfo.setRuralQualifier(result.getString(20));
	                addressInfo.setRuralSite(result.getString(21));
	                addressInfo.setRrCompartment(result.getString(22));
	                addressInfo.setAttention(result.getString(23));
	                addressInfo.setRrDeliveryType(result.getString(24));
	                addressInfo.setRrGroup(result.getString(25));
	                addressInfo.setAddressType(result.getString(26));

				}
				return addressInfo;
			}
			
		});
	}

}
