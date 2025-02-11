package com.telus.ait.integration.emcm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class EMCMDao {
	@Autowired
	@Qualifier("EMCMJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

    public long getContactEventIdFromNotificationEntry(String subscriberNumber, Date creationDate) throws Exception {
        int utcOffset = 4; //works correctly during EDT time (time zone indicator -0400); during winter must be EST (time zone indicator -0500)
        DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        Calendar expectedStartDateCalendar = Calendar.getInstance();
        expectedStartDateCalendar.setTime(creationDate);
        expectedStartDateCalendar.add(expectedStartDateCalendar.HOUR, utcOffset);
        String startTime = formatter.format(expectedStartDateCalendar.getTime());

        expectedStartDateCalendar.add(expectedStartDateCalendar.MINUTE, 2);
        String endTime = formatter.format(expectedStartDateCalendar.getTime());

        String sql = "select * from CONEADM.OUTBND_SMS_CUSTOMER_NOTIF " +
                "where CNTCT_EFFECTIVE_START_TS between to_date('" + startTime + "', 'YYYY-MM-DD HH24:MI:SS') " +
                "and to_date('" + endTime + "', 'YYYY-MM-DD HH24:MI:SS') " +
                "and DEST_ADDR_NUM = '" + subscriberNumber + "'";

        System.out.println("SQL:" + sql);

        DataSource dataSource = jdbcTemplate.getDataSource();
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();

        long contactID = 0;

        if (resultSet.next()) {
            contactID = resultSet.getLong("CNTCT_EVENT_ID");
        }

        System.out.println("CNTCT_EVENT_ID:" + contactID);

        return contactID;
    }
}