package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.cmb.account.informationhelper.dao.ContactDao;
import com.telus.cmb.account.utilities.AccountLifecycleUtilities;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

public class ContactDaoImpl extends MultipleJdbcDaoTemplateSupport implements ContactDao {

	@Override
	public BillingPropertyInfo retrieveBillingInformation(int billingAccountNumber) {
		String sql ="select  ba.account_type, ba.account_sub_type, nd.first_name, nd.last_business_name, nd.middle_initial, " +
		"nd.name_title, nd.additional_title, nd.name_format, nd.name_suffix, ad.adr_primary_ln, ad.adr_city, ad.adr_province, " +
		"ad.adr_postal, ad.adr_secondary_ln, ad.adr_country, ad.adr_zip_geo_code, ad.adr_state_code, ad.civic_no, ad.civic_no_suffix, " +
		"ad.adr_st_direction, ad.adr_street_name, ad.adr_street_type, ad.adr_designator, ad.adr_identifier, ad.adr_box, " +
		"ad.unit_designator, ad.unit_identifier, ad.adr_area_nm, ad.adr_qualifier, ad.adr_site, ad.adr_compartment,ad.adr_attention, " +
		"ad.adr_delivery_tp, ad.adr_group, ad.adr_type, c.verified_date from billing_account ba,  customer c,  address_name_link anl,  " +
		"address_data ad,  name_data nd " +
		"WHERE " +
		"ba.ban = ?   AND c.customer_id = ba.customer_id    AND anl.customer_id = c.customer_id   " +
		"AND (   TRUNC (anl.expiration_date) >   TRUNC (sysdate)   OR anl.expiration_date IS NULL    )   " +
		"AND anl.link_type = 'B'   AND ad.address_id = anl.address_id   AND nd.name_id = anl.name_id";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {billingAccountNumber}, new ResultSetExtractor<BillingPropertyInfo>() {

			@Override
			public BillingPropertyInfo extractData(ResultSet result) throws SQLException, DataAccessException {
				BillingPropertyInfo billingPropertyInfo = null;
				if (result.next()) {
					char accountType;
					char accountSubType;

					accountType = result.getString(1).charAt(0);
					accountSubType = result.getString(2).charAt(0);
					billingPropertyInfo = new BillingPropertyInfo();
					Hashtable<String,Boolean> accountTypeIdentifiers = AccountLifecycleUtilities.getAccountTypeIdentifiers(accountType, accountSubType);

					String legalBusinessName = "";
					if(accountTypeIdentifiers.get("postpaidBusinessRegular") || accountTypeIdentifiers.get("postpaidLikeBusinessRegular") || 
							accountTypeIdentifiers.get("IDENCorporateVPN") || accountTypeIdentifiers.get("IDENCorporate") || 
							accountTypeIdentifiers.get("postpaidBusinessDealer")	|| accountTypeIdentifiers.get("postpaidCorporateRegional") || 
							accountTypeIdentifiers.get("postpaidBusinessOffical") ||accountTypeIdentifiers.get("postpaidCorporateAutotel") || 
							accountTypeIdentifiers.get("postpaidCorporateRegular"))
						legalBusinessName=result.getString(4).toUpperCase();

					ConsumerNameInfo consumerName = null;
					if(!(accountTypeIdentifiers.get("postpaidBusinessRegular") || accountTypeIdentifiers.get("postpaidLikeBusinessRegular") || accountTypeIdentifiers.get("IDENCorporateVPN") || 
							accountTypeIdentifiers.get("IDENCorporate") || accountTypeIdentifiers.get("postpaidBusinessDealer")	|| accountTypeIdentifiers.get("postpaidCorporateRegional") || accountTypeIdentifiers.get("postpaidBusinessOffical") || 
							accountTypeIdentifiers.get("postpaidCorporateAutotel") || accountTypeIdentifiers.get("postpaidCorporateRegular"))){
						consumerName=new ConsumerNameInfo();
						consumerName.setFirstName(result.getString(3));
						consumerName.setLastName(result.getString(4));
						consumerName.setMiddleInitial(result.getString(5));
						consumerName.setTitle(result.getString(6));
						consumerName.setAdditionalLine(result.getString(7));
						consumerName.setNameFormat(result.getString(8));
						consumerName.setGeneration(result.getString(9));
					}

					AddressInfo address = new AddressInfo();
					address.setPrimaryLine(result.getString(10));
					address.setCity(result.getString(11));
					address.setProvince(result.getString(12));
					address.setPostalCode(result.getString(13));
					address.setSecondaryLine(result.getString(14));
					address.setCountry(result.getString(15));
					address.setZipGeoCode(result.getString(16));
					address.setStreetNumber(result.getString(17));
					address.setCivicNo(result.getString(18));
					address.setCivicNoSuffix(result.getString(19));
					address.setStreetDirection(result.getString(20));
					address.setStreetName(result.getString(21));
					address.setStreetType(result.getString(22));
					address.setRrDesignator(result.getString(23));
					address.setRrIdentifier(result.getString(24));
					address.setRrBox(result.getString(25));
					address.setUnitDesignator(result.getString(26));
					address.setUnitIdentifier(result.getString(27));
					address.setRrAreaNumber(result.getString(28));
					address.setRrQualifier(result.getString(29));
					address.setRrSite(result.getString(30));
					address.setRrCompartment(result.getString(31));
					address.setAttention(result.getString(32));
					address.setRrDeliveryType(result.getString(33));
					address.setRrGroup(result.getString(34));
					address.setAddressType(result.getString(35));		

					billingPropertyInfo.setFullName(result.getString(3)+" "+result.getString(4));
					java.sql.Date date=result.getDate(36);
					if(date != null)
						billingPropertyInfo.setVerifiedDate(new Date(date.getTime()));
					billingPropertyInfo.setLegalBusinessName(legalBusinessName);
					billingPropertyInfo.setName(consumerName);
					billingPropertyInfo.setAddress(address);

				}

				return billingPropertyInfo;
			}

		});
	}

	@Override
	public ContactPropertyInfo retrieveContactInformation(int billingAccountNumber) {
		String sql = "SELECT nd.first_name, nd.last_business_name, nd.middle_initial, nd.name_title, nd.additional_title, nd.name_suffix, " +
		"c.contact_telno, c.contact_tn_extno, c.contact_faxno, c.work_telno, c.work_tn_extno, c.home_telno, c.other_telno, " +
		"c.other_extno, c.other_tn_type     FROM customer c, address_name_link anl, name_data nd    " +
		"WHERE " +
		"anl.ban = ?   AND anl.customer_id = c.customer_id   AND anl.name_id=nd.name_id AND (   TRUNC (anl.expiration_date) > TRUNC (sysdate)      " +
		"OR anl.expiration_date IS NULL     )     AND anl.link_type = 'C'     AND nd.name_id = anl.name_id";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {billingAccountNumber}, new ResultSetExtractor<ContactPropertyInfo>() {

			@Override
			public ContactPropertyInfo extractData(ResultSet result) throws SQLException, DataAccessException {
				ContactPropertyInfo contactPropertyInfo = null;
				if (result.next()) {
					contactPropertyInfo = new ContactPropertyInfo();

					ConsumerNameInfo consumerName=new ConsumerNameInfo();
					consumerName.setFirstName(result.getString(1));
					consumerName.setLastName(result.getString(2));
					consumerName.setMiddleInitial(result.getString(3));
					consumerName.setTitle(result.getString(4));
					consumerName.setAdditionalLine(result.getString(5));
					consumerName.setGeneration(result.getString(6));

					contactPropertyInfo.setName(consumerName);
					contactPropertyInfo.setHomePhoneNumber(result.getString(12));
					contactPropertyInfo.setBusinessPhoneNumber(result.getString(10));
					contactPropertyInfo.setBusinessPhoneExtension(result.getString(11));
					contactPropertyInfo.setContactPhoneNumber(result.getString(7));
					contactPropertyInfo.setContactPhoneExtension(result.getString(8));
					contactPropertyInfo.setContactFax(result.getString(9));
					contactPropertyInfo.setOtherPhoneType(result.getString(15));
					contactPropertyInfo.setOtherPhoneNumber(result.getString(13));
					contactPropertyInfo.setOtherPhoneExtension(result.getString(14));
				}
				return contactPropertyInfo;
			}

		});
	}


}
