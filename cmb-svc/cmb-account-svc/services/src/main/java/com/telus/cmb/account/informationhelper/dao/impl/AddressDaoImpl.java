package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmb.account.informationhelper.dao.AddressDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;

public class AddressDaoImpl extends MultipleJdbcDaoTemplateSupport implements AddressDao {
	private String dateFormatSt = "MM/dd/yyyy";
	private SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);

	@Override
	public AddressInfo retrieveAlternateAddressByBan(int ban) {
		String queryStr = " select  adr_primary_ln, adr_city " +
		" ,adr_province, adr_postal " +
		" ,ad.adr_type, adr_secondary_ln " +
		" ,adr_country  ,adr_zip_geo_code " +
		" ,adr_state_code ,civic_no ,civic_no_suffix " +
		" , adr_st_direction, adr_street_name " +
		" ,adr_street_type ,adr_designator " +
		" ,adr_identifier, adr_box " +
		" ,unit_designator, unit_identifier " +
		" ,adr_area_nm ,adr_qualifier " +
		" ,adr_site , adr_compartment " +
		" from address_name_link anl " +
		" ,address_data   ad " +
		" where anl.customer_id= ? " +
		" and anl.expiration_date  is null " +
		" and  anl.link_type = 'T' " +
		" and  ad.address_id=anl.address_id " ;

		return super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{ban}, new ResultSetExtractor<AddressInfo>() {

			@Override
			public AddressInfo extractData(ResultSet result) throws SQLException,
			DataAccessException {
				AddressInfo addressInfo = new AddressInfo();
				while (result.next()){
					addressInfo = new AddressInfo();
					addressInfo.setPrimaryLine(result.getString(1));
					addressInfo.setCity(result.getString(2));
					addressInfo.setProvince(result.getString(3));
					addressInfo.setPostalCode(result.getString(4));
					addressInfo.setAddressType(result.getString(5));
					String tempAddressType = addressInfo.getAddressType();
					addressInfo.setSecondaryLine(result.getString(6));
					addressInfo.setCountry(result.getString(7));
					addressInfo.setZipGeoCode(result.getString(8));
					addressInfo.setForeignState(result.getString(9));
					addressInfo.setCivicNo(result.getString(10));
					addressInfo.setCivicNoSuffix(result.getString(11));
					addressInfo.setStreetDirection(result.getString(12));
					addressInfo.setStreetName(result.getString(13));
					addressInfo.setStreetType(result.getString(14));
					addressInfo.setRrDesignator(result.getString(15));
					addressInfo.setRrIdentifier(result.getString(16));
					addressInfo.setPoBox(result.getString(17));
					addressInfo.setUnitDesignator(result.getString(18));
					addressInfo.setUnitIdentifier(result.getString(19));
					addressInfo.setRrAreaNumber(result.getString(20));
					addressInfo.setRrQualifier(result.getString(21));
					addressInfo.setRrSite(result.getString(22));
					addressInfo.setRrCompartment(result.getString(23));
					addressInfo.setAddressType(tempAddressType);
				}
				return addressInfo;
			}
		});
	}

	@Override
	public AddressHistoryInfo[] retrieveAddressHistory(int pBan,
			final Date pFromDate, final Date pToDate) {

		String queryStr = " SELECT  distinct " +
		"             anl.effective_date, anl.expiration_date, " +
		"             ad.civic_no, ad.civic_no_suffix, ad.adr_street_name, " +
		"             ad.adr_st_direction, adr_street_type, " +
		"             ad.adr_primary_ln, ad.adr_city, ad.adr_province, " +
		"             ad.adr_country, ad.adr_postal " +
		" FROM  address_data ad, address_name_link anl " +
		" WHERE anl.ban = ?" + 
		" AND   anl.effective_date between to_date(?,'mm/dd/yyyy') and " +
		"                                       to_date(?,'mm/dd/yyyy') " +
		" AND   anl.link_type = 'B' " +
		" AND         anl.address_id = ad.address_id";

		String fromDate = dateFormat.format(pFromDate) ;
		String toDate = dateFormat.format(pToDate) ;
		
		
		List<AddressHistoryInfo> returnList = super.getKnowbilityJdbcTemplate().query(queryStr, new Object[] {pBan, fromDate, toDate}, new RowMapper<AddressHistoryInfo>() {

			@Override
			public AddressHistoryInfo mapRow(ResultSet result, int rowNum)
					throws SQLException {

				AddressHistoryInfo addressHistoryInfo = new AddressHistoryInfo();
				addressHistoryInfo.setEffectiveDate(result.getDate(1));
				addressHistoryInfo.setExpirationDate(result.getDate(2));
				AddressInfo addressInfo = new AddressInfo();
				addressInfo.setCivicNo(result.getString(3));
				addressInfo.setCivicNoSuffix(result.getString(4));
				addressInfo.setStreetName(result.getString(5));
				addressInfo.setStreetDirection(result.getString(6));
				addressInfo.setStreetType(result.getString(7));
				addressInfo.setPrimaryLine(result.getString(8));
				addressInfo.setCity(result.getString(9));
				addressInfo.setProvince(result.getString(10));
				addressInfo.setCountry(result.getString(11));
				addressInfo.setPostalCode(result.getString(12));
				addressHistoryInfo.setAddressInfo(addressInfo);
				
				return addressHistoryInfo;
			}
		});
		
		if (returnList != null) {
			return returnList.toArray(new AddressHistoryInfo[returnList.size()]);
		} else {
			return new AddressHistoryInfo[0];
		}		

/*
			@Override
			public AddressHistoryInfo[] doInPreparedStatement(
					PreparedStatement preparedStatement) throws SQLException,
					DataAccessException {

				String fromDate = dateFormat.format(pFromDate) ;
				String toDate = dateFormat.format(pToDate) ;
				Collection<AddressHistoryInfo> addressHistoryList = new ArrayList<AddressHistoryInfo>();		
				AddressHistoryInfo[] addressHistoryInfoArray;
				ResultSet result = null;

				preparedStatement.setString(1, fromDate);
				preparedStatement.setString(2, toDate);

				try {
					result = preparedStatement.executeQuery();

					while (result.next()) {
						AddressHistoryInfo addressHistoryInfo = new AddressHistoryInfo();
						addressHistoryInfo.setEffectiveDate(result.getDate(1));
						addressHistoryInfo.setExpirationDate(result.getDate(2));
						AddressInfo addressInfo = new AddressInfo();
						addressInfo.setCivicNo(result.getString(3));
						addressInfo.setCivicNoSuffix(result.getString(4));
						addressInfo.setStreetName(result.getString(5));
						addressInfo.setStreetDirection(result.getString(6));
						addressInfo.setStreetType(result.getString(7));
						addressInfo.setPrimaryLine(result.getString(8));
						addressInfo.setCity(result.getString(9));
						addressInfo.setProvince(result.getString(10));
						addressInfo.setCountry(result.getString(11));
						addressInfo.setPostalCode(result.getString(12));
						addressHistoryInfo.setAddressInfo(addressInfo);
						addressHistoryList.add(addressHistoryInfo);
					}
				} finally {
					if (result != null)
						result.close();
				}

				addressHistoryInfoArray = new AddressHistoryInfo[addressHistoryList.size()];
				addressHistoryInfoArray = (AddressHistoryInfo[])addressHistoryList.toArray(addressHistoryInfoArray);

				return addressHistoryInfoArray;
			}
		});
	*/
	}
	
}
