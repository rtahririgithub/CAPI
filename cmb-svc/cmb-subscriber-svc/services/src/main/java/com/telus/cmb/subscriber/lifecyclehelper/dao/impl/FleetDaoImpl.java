package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.FleetDao;
import com.telus.eas.account.info.TalkGroupInfo;

public class FleetDaoImpl extends MultipleJdbcDaoTemplateSupport implements FleetDao {

	@Override
	public List<TalkGroupInfo> retrieveTalkGroupsBySubscriber(
			final String subscriberId) {
		String callString="{ call fleet_utility_pkg.GetTalkGroupsBySubscriber (?,?) }";
		return  super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<List<TalkGroupInfo>>(){
			@Override
			public List<TalkGroupInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{
				List<TalkGroupInfo> list = new ArrayList<TalkGroupInfo>();
				ResultSet result = null;
				try{
					callable.setString(1, subscriberId);
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(2);
					while (result.next()) {
						TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
						talkGroupInfo.getFleetIdentity().setUrbanId(result.getInt(2));
						talkGroupInfo.getFleetIdentity().setFleetId(result.getInt(3));
						talkGroupInfo.setTalkGroupId(result.getInt(4));
						talkGroupInfo.setName(result.getString(5));
						talkGroupInfo.setPriority(result.getInt(6));
						talkGroupInfo.setOwnerBanId(result.getInt(7));

						list.add(talkGroupInfo);
					}
				}finally{
					if(result != null)
						result.close();
				}
				return list;
			}
		});	
	}

}
