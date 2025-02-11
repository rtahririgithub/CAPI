package com.telus.ait.integration.kb.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.telus.ait.integration.BaseDao;
import com.telus.ait.integration.kb.dao.impl.ResultSetIterator;
import com.telus.ait.integration.kb.info.AccountInfo;
import com.telus.ait.integration.kb.info.ChargeInfo;
import com.telus.ait.integration.kb.info.SubscriberInfo;

@Service
public class KnowbilityDao extends BaseDao {
   
    @Autowired
    @Qualifier("kbJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    public Iterator<SubscriberInfo> getPostpaidSubscribers() {
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

            return new ResultSetIterator<SubscriberInfo>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<SubscriberInfo> getPrepaidSubscribers() {
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

            return new ResultSetIterator<SubscriberInfo>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<SubscriberInfo> getMikeSubscribers() {
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

            return new ResultSetIterator<SubscriberInfo>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<SubscriberInfo> getBusinessAnywhereSubscribers() {
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

            return new ResultSetIterator<SubscriberInfo>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<SubscriberInfo> getBusinessConnectSubscribers() {
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

            return new ResultSetIterator<SubscriberInfo>(resultSet, subscriberMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public AccountInfo getAccountInfo(String ban) {
        String sql = "SELECT account_type, account_sub_type, ban_status FROM BILLING_ACCOUNT where ban = ?";
        List<AccountInfo> list = jdbcTemplate.query(sql, new Object[]{new Long(ban)}, accountInfoMapper);
        return list.size() > 0 ? list.get(0) : null;
    }

    public boolean checkDataSource() {
        List<Long> offerIdList = jdbcTemplate.query("select ? from dual", new Object[]{new Long(2)}, longMapper);
        return offerIdList.size() == 1 && offerIdList.get(0).equals(new Long(2));
    }
    
    public List<String> getNGPByNPA(String npa) {
    	String sql = "SELECT NGP FROM MARKET_NPA_NXX_LR WHERE NPA = ? GROUP BY NGP ORDER BY COUNT(*) DESC";    	
    	return jdbcTemplate.query(sql, new Object[]{npa}, stringMapper);
    }
    
    public boolean isUSimUsed(String imsi) {
    	String sql = "SELECT COUNT(*) FROM SUBSCRIBER_RSOURCE WHERE IMSI_NUMBER = ?";
    	List<Long> result = jdbcTemplate.query(sql, new Object[]{imsi}, longMapper);
    	return result.size() > 0 && result.get(0) > 0;
    }
    
    public String getSubscriberStatus(String phoneNumber) {
    	String sql = "SELECT SUB_STATUS FROM SUBSCRIBER WHERE SUBSCRIBER_NO = ?";
    	List<String> result = jdbcTemplate.query(sql, new Object[]{phoneNumber}, stringMapper);
    	if (result != null && !result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }
    
    public String getCTNStatus(String phoneNumber) {
    	String sql = "SELECT CTN_STATUS FROM CTN_INV WHERE CTN = ?";
    	List<String> result = jdbcTemplate.query(sql, new Object[]{phoneNumber}, stringMapper);
    	if (result != null && !result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }

    public ChargeInfo getChargeInfo(String ban, String sequenceNumber) {
        String sql = "SELECT tax_gst_amt, tax_pst_amt, tax_hst_amt, tax_gst_taxbl_amt, tax_pst_taxbl_amt, tax_hst_taxbl_amt FROM CHARGE where ban = ? and ent_seq_no = ?";
        List<ChargeInfo> list = jdbcTemplate.query(sql, new Object[]{ban, sequenceNumber}, chargeInfoMapper);
        return list.size() > 0 ? list.get(0) : null;
    }

    public String getChargeSequenceNumber(String ban, String adjustmentId) {
        String sql = "SELECT charge_seq_no FROM ADJUSTMENT where ban = ? and ent_seq_no = ?";
        List<String> list = jdbcTemplate.query(sql, new Object[]{ban, adjustmentId}, stringMapper);
        return list.size() > 0 ? list.get(0) : null;
    }
        
    public String findAnyBan(String banStatus, String accountType, String accountSubType) {
        String sql = "SELECT ban FROM BILLING_ACCOUNT where ban_status = ? and account_type = ? and account_sub_type = ? order by sys_creation_date desc";
        List<String> list = jdbcTemplate.query(sql, new Object[]{banStatus, accountType, accountSubType}, stringMapper);
        return list.size() > 0 ? list.get(0) : null;
    }
}
