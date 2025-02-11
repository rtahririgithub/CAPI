package com.telus.cmb.utility.queueevent.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.api.ApplicationException;
import com.telus.cmb.utility.queueevent.dao.QueueEventDao;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;



public class QueueEventDaoImpl extends JdbcDaoSupport implements QueueEventDao {

	@Override
	public void createNewEvent(final long connectionId, final String phoneNumber,
			final long subscriptionId, final int userId, final String queueName,
			final int thresholdSeconds) throws ApplicationException {
		String call =  "{ call cc_event_pkg.createNewEvent(?,?,?,?,?,?) }";
		
		getJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setLong(1, connectionId);
				callable.setString(2, phoneNumber);
				callable.setLong(3, subscriptionId);
				callable.setInt(4, userId);
				callable.setString(5, queueName);
				callable.setInt(6, thresholdSeconds);

				callable.execute();
				return null;
			}
		});
		
	}

	@Override
	public QueueThresholdEventInfo getEvent(final long connectionId) throws ApplicationException {
		String call =  "{ call cc_event_pkg.selecteventsbyconnectionid(?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<QueueThresholdEventInfo>() {

			@Override
			public QueueThresholdEventInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				QueueThresholdEventInfo eventInfo = null;
				ResultSet result = null;

				callable.setLong(1, connectionId);
				callable.registerOutParameter(2, OracleTypes.CURSOR);
				callable.execute();

				try {
					result = (ResultSet) callable.getObject(2);

					if (result.next()) {
						eventInfo = new QueueThresholdEventInfo();
						eventInfo.setId(result.getLong(1));
						eventInfo.setCallCentreConnectionId(result.getLong(2));
						eventInfo.setCallCentreQueue(result.getString(3));
						eventInfo.setDate(result.getTimestamp(4));
						eventInfo.setAssociatedPhoneNumber(result.getString(5));
						Timestamp startTs = result.getTimestamp(6);
						Timestamp endTs = result.getTimestamp(7);
						if ((startTs == null) || (endTs == null))
							eventInfo.setWaitTime(0);
						else
							eventInfo.setWaitTime((int) ((endTs.getTime() - startTs.getTime()) / 1000));
						eventInfo.setEvaluatedForCredit(result.getTimestamp(8) != null);
						eventInfo.setTeamMemberId(result.getInt(9));
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}

				return eventInfo;
			}
		});

	}

	@Override
	public SearchResultsInfo getEvents(final long subscriptionId, final Date fromDate, final Date toDate) throws ApplicationException {
		String call;
		if (fromDate == null) {
			call = "{ call cc_event_pkg.selecteventsbysubscriptionid(?,?,?) }";
		} else {
			call = "{ call cc_event_pkg.selecteventsbysubscriptionid(?,?,?,?) }";
		}
	    final Date queryToDate = toDate!=null? toDate:new Date();
	    
		return getJdbcTemplate().execute(call, new CallableStatementCallback<SearchResultsInfo>() {

			@Override
			public SearchResultsInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				ResultSet result = null;
			  	SearchResultsInfo searchResults = new SearchResultsInfo();
			    searchResults.setItems(new QueueThresholdEventInfo[0]);
			    Collection<QueueThresholdEventInfo> eventList = new ArrayList<QueueThresholdEventInfo>();

			    
				callable.setLong(1, subscriptionId);
			      if (fromDate==null) {
			          callable.setString(2, dateFormat.format(queryToDate));
				      callable.registerOutParameter(3, OracleTypes.CURSOR);
			      } else {
				      callable.setString(2, dateFormat.format(fromDate));
				      callable.setString(3, dateFormat.format(queryToDate));
				      callable.registerOutParameter(4, OracleTypes.CURSOR);
			      }
			      callable.execute();
			      
			      try {
			      result = (ResultSet) callable.getObject(fromDate==null?3:4);

			      while (result.next()) {
			      	QueueThresholdEventInfo eventInfo = new QueueThresholdEventInfo();
			      	eventInfo.setId(result.getLong(1));
			      	eventInfo.setCallCentreConnectionId(result.getLong(2));
			      	eventInfo.setCallCentreQueue(result.getString(3));
			      	eventInfo.setDate(result.getTimestamp(4));
			      	eventInfo.setAssociatedPhoneNumber(result.getString(5));
			      	Timestamp startTs = result.getTimestamp(6);
			      	Timestamp endTs = result.getTimestamp(7);
			      	if ((startTs==null)||(endTs==null))
						eventInfo.setWaitTime(0);
					else
						eventInfo.setWaitTime((int)((endTs.getTime()-startTs.getTime())/1000));
			      	eventInfo.setEvaluatedForCredit(result.getTimestamp(8)!=null);
			      	eventInfo.setTeamMemberId(result.getInt(9));
			        eventList.add(eventInfo);
			      }
			      } finally {
			    	  if (result != null) {
					      result.close();
			    	  }
			      }

			    if (eventList.size() > 0) {
			    	QueueThresholdEventInfo[] events = new QueueThresholdEventInfo[eventList.size()];
			    	events = (QueueThresholdEventInfo[]) eventList.toArray(events);

			      searchResults.setItems(events);
			      searchResults.setHasMore(false);
			    } else {
			      searchResults.setItems(new QueueThresholdEventInfo[0]);
			      searchResults.setHasMore(false);
			    }
			    
			    return searchResults;
			}
		});
	}

	@Override
	public void updateEvent(final long interactionId, final long subscriptionId, final String phoneNumber, final int teamMemberId, final int userId) throws ApplicationException {
		String call =  "{ call cc_event_pkg.updateEvent(?,?,?,?,?) }";
		
		getJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setLong(1, interactionId);
				callable.setLong(2, subscriptionId);
				callable.setString(3, phoneNumber);
				callable.setInt(4, teamMemberId);
				callable.setInt(5, userId);

				callable.execute();
				return null;
			}
			
		});
	}


}
