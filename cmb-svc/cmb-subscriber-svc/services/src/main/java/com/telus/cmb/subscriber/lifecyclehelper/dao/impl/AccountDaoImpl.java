package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.api.account.AccountManager;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AccountDao;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class AccountDaoImpl extends MultipleJdbcDaoTemplateSupport  implements AccountDao {

	private final Logger logger = Logger.getLogger(AccountDaoImpl.class);

	@Override
	public SubscriberInfo retrieveBanForPartiallyReservedSub(final String phoneNumber) {
		String callString="{? = call SUBSCRIBER_PKG.GetBanForPartuallyReservedSub(?, ?, ?)}";
		return  super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<SubscriberInfo>(){
			@Override
			public SubscriberInfo doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				ResultSet result=null;
				SubscriberInfo subscriberInfo = new SubscriberInfo();
				try{
				      callable.registerOutParameter(1, OracleTypes.NUMBER);
				      callable.setString(2, phoneNumber);
				      callable.registerOutParameter(3, OracleTypes.CURSOR);
				      callable.registerOutParameter(4, OracleTypes.VARCHAR);

				      callable.execute();

				      boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

				      if (success) {
				        result = (ResultSet) callable.getObject(3);
				        while (result.next()) {
				        	subscriberInfo.setSubscriberId(result.getString(1));
				        	subscriberInfo.setStatus(result.getString(2).charAt(0));
				        	subscriberInfo.setBanId(Integer.parseInt(result.getString(3)));
				        }
				        
				      }
				      else {
				        String errorMessage = callable.getString(4);
				        logger.debug("Procedure failed "+errorMessage);
				      //  throw new TelusApplicationException("APP20002", "Subscribers not found.");
				      }

				}finally{
					if (result != null)
						result.close();
				}
				return subscriberInfo;
			}
		});
	}

	@Override
	public int retrieveBanIdByPhoneNumber(final String phoneNumber) {
		String callString="{? = call SUBSCRIBER_PKG.getBanIdByPhoneNumber(?)}";
		return super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<Integer>(){
			@Override
			public Integer doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{
				int ban=0;
				      callable.registerOutParameter(1, OracleTypes.NUMBER);
				      callable.setString(2, phoneNumber);
				      callable.execute();
				      ban = callable.getInt(1);
				return ban;
			}
		});
	}

}
