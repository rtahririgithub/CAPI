package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.cmb.account.informationhelper.dao.FleetDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;

public class FleetDaoImpl extends MultipleJdbcDaoTemplateSupport implements FleetDao {

	@Override
	public Collection<FleetInfo> retrieveFleetsByBan(final int ban) {
		String callString = "{ call fleet_utility_pkg.GetFleetsByBan (?,?) }";		
		
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<FleetInfo>>() {

			@Override
			public Collection<FleetInfo> doInCallableStatement(
					CallableStatement stmt) throws SQLException,
					DataAccessException {
				ResultSet result = null;				
				Collection<FleetInfo> fleetInfoList = new ArrayList<FleetInfo>();

				try {

					stmt.setInt(1, ban);
					stmt.registerOutParameter(2, ORACLE_REF_CURSOR);
					stmt.execute();
					result = (ResultSet)stmt.getObject(2);

					while (result.next()) {
						FleetInfo fleetInfo = new FleetInfo();
						int urbanId = result.getInt(2);
						int fleetId = result.getInt(3);
						fleetInfo.getIdentity0().setUrbanId(urbanId);
						fleetInfo.getIdentity0().setFleetId(fleetId);
						fleetInfo.setExpectedSubscribers(result.getInt(4));
						fleetInfo.setExpectedTalkGroups(result.getInt(5));
						fleetInfo.setName(result.getString(6));
						fleetInfo.setType(result.getString(7).charAt(0));
						fleetInfo.setNetworkId(result.getInt(8));
						fleetInfo.setFleetClass(result.getString(9));
						fleetInfo.setDAPId(result.getInt(10));
						fleetInfo.setBanId0(result.getInt(11));
						fleetInfo.setOwnerName(AttributeTranslator.emptyFromNull(result.getString(12)));						
						fleetInfoList.add(fleetInfo);
					}
				} finally {
					if (result != null ) {
						result.close();
					}
				}
				
				return fleetInfoList;
			}
		});
	}

	@Override
	public int retrieveAssociatedAccountsCount(final int urbanId, final int fleetId) {
		String callString = "{ call fleet_utility_pkg.GetNumberAssociatedAccounts (?,?,?) }";
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement stmt)
					throws SQLException, DataAccessException {
				stmt.setInt(1,urbanId);
                stmt.setInt(2,fleetId);
                stmt.registerOutParameter(3,Types.NUMERIC);
                stmt.execute();
                return stmt.getInt(3);
			}
		});
	}
	
	@Override
	public int retrieveAssociatedTalkGroupsCount(
			final FleetIdentityInfo pFleeIdentity, final int ban) {
		String call = "{ call fleet_utility_pkg.GetNumberAccociatedTalkGroups (?,?,?,?) }";
		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				int count=0;
				callable.setInt(1,pFleeIdentity.getUrbanId());
				callable.setInt(2,pFleeIdentity.getFleetId());
				callable.setInt(3,ban);
				callable.registerOutParameter(4,Types.NUMERIC);
				callable.execute();

                count = callable.getInt(4);
				return count;
			}
		});

	}

	@Override
	public int retrieveAttachedSubscribersCountForTalkGroup(final int urbanID,
			final int fleetID, final int talkGroupId, final int ban) {
		String call = "{ call fleet_utility_pkg.GetAttachedSubsCountForTgBan (?,?,?,?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				callable.setInt(1,urbanID);
				callable.setInt(2,fleetID);
				callable.setInt(3,talkGroupId);
				callable.setInt(4,ban);
				callable.registerOutParameter(5,Types.NUMERIC);
				callable.execute();

                return callable.getInt(5);			

			}
		});
	}
	@Override
	public List<TalkGroupInfo> retrieveTalkGroupsByBan(final int ban) {
		String call = "{ call fleet_utility_pkg.GetTalkGroupsByBan (?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<TalkGroupInfo>>() {

			@Override
			public List<TalkGroupInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
			    ResultSet result = null;
			    List<TalkGroupInfo> talkGroupInfoList = new ArrayList<TalkGroupInfo>();
			    try{
				callable.setInt(1, ban);
				callable.registerOutParameter(2, ORACLE_REF_CURSOR);
				callable.execute();
		        result = (ResultSet)callable.getObject(2);
					while (result.next()) {
					       TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
					       talkGroupInfo.getFleetIdentity().setUrbanId(result.getInt(2));
					       talkGroupInfo.getFleetIdentity().setFleetId(result.getInt(3));
					       talkGroupInfo.setTalkGroupId(result.getInt(4));
					       talkGroupInfo.setName(result.getString(5));
					       talkGroupInfo.setPriority(result.getInt(6));
					       talkGroupInfo.setOwnerBanId(result.getInt(7));
					       talkGroupInfoList.add(talkGroupInfo);
					}
			    }finally{
			    	if (result != null ) {
						result.close();
					}
			    }
				return talkGroupInfoList;	
			}
		});
	}
}
