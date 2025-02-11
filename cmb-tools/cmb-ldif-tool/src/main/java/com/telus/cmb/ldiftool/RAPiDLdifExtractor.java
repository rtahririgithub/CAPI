package com.telus.cmb.ldiftool;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.telus.cmb.ldiftool.utilities.LdifUtilities;

public class RAPiDLdifExtractor implements LdifSourceExtractor {

	private static Logger logger = Logger.getLogger(RAPiDLdifExtractor.class);
	
	private static final String RAPID_CONNECTION_STRING = "jdbc:mysql://emtools.tmi.telus.com:3306/";
	private static final String RAPID_USER = "envmatrixall";
	private static final String RAPID_PASSWORD = "envmatrixall";
	private static final String QUERY_STRING = "select buildpath from rapidpr.rpd_deployment where release_id = ? and application_id in ('550', '445', '563') and valid = 1 and buildpath like '%ldif'";

	private int releaseId;
	
	public RAPiDLdifExtractor(int releaseId) {
		super();
		this.releaseId = releaseId;
	}
	
	@Override
	public List<String> getLdifBuildFiles(String environment, boolean saveLdifIndicator) {
		Connection connection = null;
		List<String> ldifList = new ArrayList<String>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(RAPID_CONNECTION_STRING, RAPID_USER, RAPID_PASSWORD);
			PreparedStatement stmt = (PreparedStatement)connection.prepareStatement (QUERY_STRING);
			stmt.setInt(1, releaseId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (saveLdifIndicator) {
					ldifList.add(LdifUtilities.saveLdifFile(environment, rs.getString("buildpath")));	
				} else {
					ldifList.add(LdifUtilities.getLdifUrl(environment, rs.getString("buildpath")));					
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		
		return ldifList;
	}
	
}
