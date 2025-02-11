package com.telus.ait.integration.kb.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.telus.ait.integration.kb.dao.impl.ResultSetIterator;

@Service
public class KnowbilityDao {
    public static class Subscriber {
        private int ban;
        private long subscriptionId;
        private String subscriberNumber;

        public Subscriber(int ban, String subscriberNumber, long subscriptionId) {
            this.ban = ban;
            this.subscriberNumber = subscriberNumber;
            this.subscriptionId = subscriptionId;
        }

        public int getBan() {
            return ban;
        }

        public String getSubscriberNumber() {
            return subscriberNumber;
        }

        public long getSubscriptionId() {
            return subscriptionId;
        }
    }

    @Autowired
    @Qualifier("kbJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Subscriber> subscriberMapper = new RowMapper<Subscriber>() {

        public Subscriber mapRow(ResultSet resultSet, int i) throws SQLException {
            Subscriber subscriber = new Subscriber(resultSet.getInt(2), resultSet.getString(1), resultSet.getLong(3));
            return subscriber;
        }
    };

    private static final RowMapper<Long> longMapper = new RowMapper<Long>() {

        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong(1);
        }
    };

    private static final RowMapper<String> stringMapper = new RowMapper<String>() {

        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString(1);
        }
    };
    
    public Iterator<Subscriber> getPostpaidSubscribers() {
        String sql = "select SUBSCRIBER_NO, CUSTOMER_ID, EXTERNAL_ID from subscriber s " +
                "where s.customer_id in (select a.ban from BILLING_ACCOUNT a " +
                "where account_type = 'I' and account_sub_type = 'R' " +
                "and brand_id = 1 " +
                "and ban_status = 'O' " +
                "and sys_creation_date > sysdate - 60)";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<Subscriber>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<Subscriber> getPrepaidSubscribers() {
        String sql = "select SUBSCRIBER_NO, CUSTOMER_ID, EXTERNAL_ID from subscriber s\n" +
                "where s.customer_id in (select a.ban from BILLING_ACCOUNT a\n" +
                "where account_type = 'I' and account_sub_type = 'Q'\n" +
                "and brand_id = 1\n" +
                "and ban_status = 'O' \n" +
                "and sys_creation_date > sysdate - 365)";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<Subscriber>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<Subscriber> getMikeSubscribers() {
        String sql = "select SUBSCRIBER_NO, CUSTOMER_ID, EXTERNAL_ID from subscriber s\n" +
                "where s.customer_id in (select a.ban from BILLING_ACCOUNT a\n" +
                "where account_type = 'C' and account_sub_type = '1'\n" +
                "and brand_id = 1\n" +
                "and ban_status = 'O' \n" +
                "and sys_creation_date > sysdate - 800)";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<Subscriber>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<Subscriber> getBusinessAnywhereSubscribers() {
        String sql = "select SUBSCRIBER_NO, CUSTOMER_ID, EXTERNAL_ID from subscriber s\n" +
                "where s.customer_id in (select a.ban from BILLING_ACCOUNT a\n" +
                "where a.account_type = 'B' and a.account_sub_type in ('A', 'N')\n" +
                "and a.brand_id = 1\n" +
                "and a.ban_status = 'O') \n" +
                "order by sys_creation_date desc";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<Subscriber>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<Subscriber> getBusinessConnectSubscribers() {
        String sql = "select SUBSCRIBER_NO, CUSTOMER_ID, EXTERNAL_ID from subscriber s\n" +
                "where s.customer_id in (select a.ban from BILLING_ACCOUNT a\n" +
                "where a.account_type = 'B' and a.account_sub_type in ('F', 'G')\n" +
                "and a.brand_id = 1\n" +
                "and external_id is not null\n" +
                "and a.ban_status = 'O') \n" +
                "order by sys_creation_date desc";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<Subscriber>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public static class AccountInfo {
        private String accountType;
        private String accountSubType;

        public AccountInfo(String accountType, String accountSubType) {
            this.accountType = accountType;
            this.accountSubType = accountSubType;
        }

        public String getAccountType() {
            return accountType;
        }

        public String getAccountSubType() {
            return accountSubType;
        }
    }

    private static final RowMapper<AccountInfo> accountInfoMapper = new RowMapper<AccountInfo>() {
        public AccountInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            AccountInfo acctInfo = new AccountInfo(resultSet.getString(1), resultSet.getString(2));
            return acctInfo;
        }
    };

    public AccountInfo getAccountInfo(String ban) {
        String sql = "SELECT account_type, account_sub_type FROM BILLING_ACCOUNT where ban = ? and ban_status = 'O'";
        List<AccountInfo> list = jdbcTemplate.query(sql, new Object[]{new Long(ban)}, accountInfoMapper);
        return list.size() > 0 ? list.get(0) : null;
    }

    public boolean checkDataSource() {
        List<Long> offerIdList = jdbcTemplate.query("select ? from dual", new Object[]{new Long(2)}, longMapper);
        return offerIdList.size() == 1 && offerIdList.get(0).equals(new Long(2));
    }
    
    public String getNGPByNPA(String npa) {
    	String sql = "SELECT NGP FROM MARKET_NPA_NXX_LR WHERE NPA = ? GROUP BY NGP ORDER BY COUNT(*) DESC";
    	List<String> result = jdbcTemplate.query(sql, new Object[]{npa}, stringMapper);
    	if (result != null && !result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }
    
    public boolean isUSimUsed(String imsi) {
    	String sql = "SELECT COUNT(*) FROM SUBSCRIBER_RSOURCE WHERE IMSI_NUMBER = ?";
    	List<Long> result = jdbcTemplate.query(sql, new Object[]{imsi}, longMapper);
    	return result.size() > 0 && result.get(0) > 0;
    }
}
