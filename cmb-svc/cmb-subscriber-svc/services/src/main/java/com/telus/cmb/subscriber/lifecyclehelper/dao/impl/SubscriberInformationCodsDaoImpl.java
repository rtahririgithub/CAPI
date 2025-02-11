package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberInformationCodsDao;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;

public class SubscriberInformationCodsDaoImpl extends MultipleJdbcDaoTemplateSupport implements SubscriberInformationCodsDao {

	private static final int CONTACT_MECHANISM_TYPE_TELEPHONE = 4;
	private final Logger logger = Logger.getLogger(SubscriberInformationCodsDaoImpl.class);
	public PhoneDirectoryEntry[] getPhoneDirectory(long subscriptionID) throws DataAccessException {
		logger.debug("Retrieve Phone Directory Entries for Subscriber : [" + subscriptionID + "]");

		String sql = "select csc.effective_start_ts, csc.contact_nickname_txt, " +
					 "       sca.contact_address_txt " +
				     "  from client_social_contact csc, social_contact_address sca" +
				     " where sca.client_social_contact_id = csc.client_social_contact_id" +
				     "   and sca.subscription_id = " + subscriptionID + " " +
				     "   and sca.effective_stop_ts > sysdate " +
				     "   and sca.contact_mechanism_type_id = " + CONTACT_MECHANISM_TYPE_TELEPHONE;
		
		return getCodsJdbcTemplate().query(sql, new RowMapper<PhoneDirectoryEntry>() {
			
			@Override
			public PhoneDirectoryEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PhoneDirectoryEntry pdeBo = new PhoneDirectoryEntry();

				pdeBo.setEffectiveDate(rs.getTimestamp(1));
				pdeBo.setNickName(rs.getString(2));
				pdeBo.setPhoneNumber(rs.getString(3));
				
				return pdeBo;
			}
		}).toArray(new PhoneDirectoryEntry[0]);
	}

	public void updatePhoneDirectory(final long subscriptionID, final PhoneDirectoryEntry[] entries) throws DataAccessException {
		logger.debug("Update Phone Directory Entries for Subscriber : [" + subscriptionID + "]");

		PhoneDirectoryEntry[] currentPD = getPhoneDirectory(subscriptionID);
		
		for(int i=0; i<entries.length; i++) {
			for(int j=0; j<currentPD.length; j++) {
				if(entries[i].getPhoneNumber().equals(currentPD[j].getPhoneNumber())) {
					entries[i].setExistingEntry(true);
					break;
				}
			}
		}
		
		String call = "{call subscriber_pref_pkg.updatesubscriber_ph_dir (?,?) }";
		
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
			
			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				Connection dbConnection = cs.getConnection();
				
	            StructDescriptor contactDescriptor = StructDescriptor.createDescriptor("PD_ENTRIES_O", dbConnection);

				Object[] pdEntries = new Object[entries.length];

				for (int i = 0; i < entries.length; i++) {
					logger.debug("Phone Directory Entry " + i + " : [" + 
							entries[i].getPhoneNumber()	+ ", " + 
							entries[i].getNickName() + "]");

					Object[] attr = new Object[4];
					attr[0] = entries[i].getPhoneNumber();
					attr[1] = entries[i].getNickName();
					if (entries[i].getEffectiveDate() != null) {
						attr[2] = new java.sql.Timestamp(entries[i].getEffectiveDate().getTime());
					}else {
						attr[2] = new java.sql.Timestamp((new java.util.Date()).getTime());
					}
					attr[3] = entries[i].isExistingEntry()?"Y":"N";

					pdEntries[i] = new STRUCT(contactDescriptor, dbConnection, attr);
				}

				// create array descriptor
				ArrayDescriptor pdEntriesArrayDesc = ArrayDescriptor.createDescriptor("PD_ENTRIES_T", dbConnection);

				// create Oracle array of Phone Directory Entries
				ARRAY pdEntriesArray = new ARRAY(pdEntriesArrayDesc, dbConnection, pdEntries);

				cs.setLong(1, subscriptionID);
				cs.setArray(2, pdEntriesArray);
				
				cs.execute();

				return null;
			}
		});
	}

	public void deletePhoneDirectoryEntries(final long subscriptionID, final PhoneDirectoryEntry[] entries) throws DataAccessException {
		logger.debug("Expire Phone Directory Entries for Subscriber : [" + subscriptionID + "]");

		String call = "{call subscriber_pref_pkg.deletesubscriber_pd_entries (?,?) }";
		
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
			
			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				Connection dbConnection = cs.getConnection();
				
				StructDescriptor contactDescriptor = StructDescriptor.createDescriptor("PD_ENTRIES_O", dbConnection);

				Object[] pdEntries = new Object[entries.length];

				for (int i = 0; i < entries.length; i++) {
					logger.debug("Phone Directory Entry " + i + " : [" + 
							entries[i].getPhoneNumber()	+ ", " + 
							entries[i].getNickName() + "]");

					Object[] attr = new Object[4];
					attr[0] = entries[i].getPhoneNumber();
					attr[1] = entries[i].getNickName();
					attr[2] = new java.sql.Timestamp(0);
					attr[3] = "Y";

					pdEntries[i] = new STRUCT(contactDescriptor, dbConnection, attr);
				}

				// create array descriptor
				ArrayDescriptor pdEntriesArrayDesc = ArrayDescriptor.createDescriptor("PD_ENTRIES_T", dbConnection);

				// create Oracle array of Phone Directory Entries
				ARRAY pdEntriesArray = new ARRAY(pdEntriesArrayDesc, dbConnection, pdEntries);

				cs.setLong(1, subscriptionID);
				cs.setArray(2, pdEntriesArray);
				
				cs.execute();

				return null;
			}
		});
	}
}
