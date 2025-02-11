package com.telus.cmb.common.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * This class is a simple utility that helps to build dynamic prepared statement.
 * 
 * Sometimes, people need to build SQL and bind parameter base on runtime information. This will become
 * very difficult to track the parameter position in the SQL when the query is very large and dynamic.
 * 
 * This class is pretty coarse, does not have much validation, use with caution.
 *
 */
/**
 * @deprecated We should not be using this preparedStatementBuilder. I'm moving this over for legacy code reasons.
 * @author Hilton Poon (t859517)
 * @since November 24 2010
 */
public class PreparedStatementBuilder {

	private static Logger logger = Logger.getLogger(PreparedStatementBuilder.class);

	private StringBuffer sql = new StringBuffer();
	private ArrayList<Object> params = new ArrayList<Object>(); 

	/**
	 * This method append the given SQL fragment to the prepared statement. 
	 * @param sqlFragment a SQL fragment, shall not contain any parameter place holder (the question mark)
	 * @return this builder itself
	 */
	public PreparedStatementBuilder append( String sqlFragment ) {
		sql.append( sqlFragment );
		return this;
	}

	/**
	 * This method append the SQL fragment, then append one parameter place holder( the question mark) and associate parameter value 
	 * to the prepared statement.
	 * 
	 * @param sqlFragment  a criteria fragment, shall not contain any parameter place holder - the question mark
	 * @param paramValue the parameter value that will be bound to the given SQL fragment, shall not be Null.
	 * @return this builder itself
	 */
	public PreparedStatementBuilder append( String sqlFragment, Object paramValue ) {
		sql.append( sqlFragment );
		sql.append(" ? ");
		params.add( paramValue );
		return this;
	}

	/**
	 * This method append a criteria fragment in form of " IN ( ?,?..) " and associate the parameter values to the prepared statement. 
	 * The number of parameter place holder will be same as the number of parameter values. 
	 * 
	 * @param paramValues the parameter values that will be bound to the IN (?,?..) clause, shall not be Null nor empty array. and array 
	 * element shall not be null.
	 * @return this builder itself
	 */
	public PreparedStatementBuilder in( Object[] paramValues ) {
		sql.append( " IN (" );		 
		for( int i=0; i<paramValues.length; i++ ) {
			sql.append(" ? ");
			params.add( paramValues[i] );
			if ( i<paramValues.length-1) sql.append(",");
			else sql.append(") ");
		}		
		return this;
	}

	/**
	 * This method append a criteria fragment in form of " BETWEEN ? AND ? " and associate the lower/upper boundary value to 
	 * the prepared statement 
	 * @param lowerBoundary the lower boundary value shall not be null
	 * @param upperBoundary the upper boundary value shall not be null
	 * @return this builder itself
	 */
	public PreparedStatementBuilder between( Object lowerBoundary, Object upperBoundary ) {
		sql.append( " BETWEEN ? and ? " );
		params.add( lowerBoundary );
		params.add( upperBoundary );
		return this;
	}

	/**
	 * Return the PreparedStatment that has all parameters bound and ready to be executed.
	 * 
	 * @param conn a live java.sql.Connection
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getStatement(Connection conn) throws SQLException {

		String query = sql.toString(); 

		logger.debug( "SQL: " + query);

		PreparedStatement pstmt = conn.prepareStatement( query );

		for( int i=0; i<params.size(); i++ ) {
			Object value = params.get(i);
			logger.debug( "  bind param " + (i+1) + ": [" + value + "]");

			pstmt.setObject(i+1, value);
		}
		return pstmt;
	}

	/**
	 * @return The underlying SQL  - help to integrate with Spring JdbcTemplate.query(...)
	 */
	public String getQuery() {
		return sql.toString();
	}

	/**
	 * @return The parameter values - help to integrate with Spring JdbcTemplate.query(...)
	 */
	public Object[] getParameters() {
		return params.toArray();
	}
}