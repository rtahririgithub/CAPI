package com.telus.ait.integration;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telus.ait.integration.kb.info.AccountInfo;
import com.telus.ait.integration.kb.info.ChargeInfo;
import com.telus.ait.integration.kb.info.SubscriberInfo;

public class BaseDao {

	protected static final RowMapper<SubscriberInfo> subscriberMapper = new RowMapper<SubscriberInfo>() {
        public SubscriberInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        	SubscriberInfo subscriber = new SubscriberInfo(resultSet.getInt(2), resultSet.getString(1), resultSet.getLong(3));
            return subscriber;
        }
    };

    protected static final RowMapper<Long> longMapper = new RowMapper<Long>() {
        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong(1);
        }
    };

    protected static final RowMapper<String> stringMapper = new RowMapper<String>() {
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString(1);
        }
    };
	
    protected static final RowMapper<AccountInfo> accountInfoMapper = new RowMapper<AccountInfo>() {
        public AccountInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            AccountInfo acctInfo = new AccountInfo(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            return acctInfo;
        }
    };
	
    protected static final RowMapper<ChargeInfo> chargeInfoMapper = new RowMapper<ChargeInfo>() {
        public ChargeInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        	ChargeInfo chargeInfo = new ChargeInfo(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), 
        			resultSet.getString(6));
            return chargeInfo;
        }
    };
    
}
