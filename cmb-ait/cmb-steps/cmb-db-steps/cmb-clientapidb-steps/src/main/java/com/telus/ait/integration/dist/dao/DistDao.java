package com.telus.ait.integration.dist.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.telus.ait.integration.dist.dao.impl.ResultSetIterator;

@Service
public class DistDao {
    protected final Logger logger = LoggerFactory.getLogger(DistDao.class);

    @Autowired
    @Qualifier("distJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<String> stringMapper = new RowMapper<String>() {
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString(1);
        }
    };

    private static final RowMapper<Map<String, String>> keyValueMapper = new RowMapper<Map<String, String>>() {
    	public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
    		Map<String, String> keyValue = new HashMap<String, String>();
    		keyValue.put(resultSet.getString(1), resultSet.getString(2));
    		return keyValue;
    	}
    };
    
    public Long checkDataSource() {
        Long value = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", new RowMapper<Long>() {
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong(1);
            }
        });
        return value;
    }

    public Iterator<String> getImeiListBySku(String sku) {
        String sql = "select SERIAL_NO from pcs_equipment where product_id = (select product_id from product where product_cd = ?) " +
                "and customer_id is null and network_service_life_days_cnt is null";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, sku);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<String>(resultSet, stringMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<String> getEligibleImeiListBySku(String sku) {
        String sql = "select pcs_equipment.serial_no from distadm.pcs_equipment" +
                "        where pcs_equipment.product_id=(select product_id from product where product_cd = ?)" +
                "        and pcs_equipment.seq_no=0" +
                "        and pcs_equipment.serial_no not in (select eh.serial_no from distadm.pcs_equipment_hardware_disc eh where eh.product_id=(select product_id from product where product_cd = ?)" +
                "     union select channel_org_pcs_equip.serial_no from distadm.channel_org_pcs_equip where channel_org_pcs_equip.seq_no=0 and channel_org_pcs_equip.channel_org_equip_type_id=2" +
                "     union select distinct(usim_pcs_device_assoc.handset_serial_no) from distadm.usim_pcs_device_assoc where usim_pcs_device_assoc.usim_pcs_device_assoc_seq_no=0)";
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, sku);
            preparedStatement.setString(2, sku);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<String>(resultSet, stringMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public Iterator<String> getImeiList(List<String> skuList, List<String> excludeSkuList, Boolean inUse, boolean nslExceeded, Boolean previouslyDiscounted, int count) {
        String sql = "select SERIAL_NO from pcs_equipment pe where 1 = 1 " +
                getSkuSql(skuList, false) +
                getSkuSql(excludeSkuList, true) +
                // (inUse == null ? "" : "and customer_id is " + (inUse ? "not " : "") + "null ") +
                (nslExceeded ? "and network_service_life_days_cnt > 90 " : "and network_service_life_days_cnt is null ") +
                (previouslyDiscounted == null ? "" : " and " + (previouslyDiscounted ? "" : "not ") + " EXISTS (SELECT 1 FROM pcs_equipment_hardware_disc pehd where PE.SERIAL_NO = PEHD.SERIAL_NO) ");

        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            return new ResultSetIterator<String>(resultSet, stringMapper);
        } catch (Exception e) {
            throw new RuntimeException("Unable to query", e);
        }
    }

    public String getSkuSql(List<String> skuList, boolean not) {
        if (skuList == null || skuList.size() == 0) return "";

        StringBuffer buf = new StringBuffer("and " +  (not ? "not " : "") + "exists (select 1 from product p where pe.product_id = p.product_id and product_cd in (");
        for (String sku: skuList) {
            buf.append("'").append(sku).append("',");
        }

        return buf.substring(0, buf.length() - 1) + ")) ";
    }

    public List<String> getAppleSkus() {
        String sql = "SELECT PRODUCT_CD from PRODUCT WHERE PRODUCT_CD like '%HAPPLE%'";
        List<String> skuList = jdbcTemplate.query(sql, stringMapper);
        return skuList;
    }

    public List<String> getAvailableSimNumbers() {
    	String sql = "SELECT SIM_ID FROM SIM WHERE CUSTOMER_ID IS NULL";
    	List<String> simList = jdbcTemplate.query(sql, stringMapper);
        return simList;
    }
    
    /**
     * Returns the latest list of key-value pairs of USIM-ISMI numbers from DIST
     * @param brandId
     * @return
     */
    public List<Map<String,String>> getLatestUSimAndImsis(String brandId) {
    	String sql = "SELECT U.USIM_ID, II.IMSI_NUM FROM USIM U, CATALOGUE_ITEM C, CI_MODIFIER CM, IMSI_INVENTORY II "
    			+ "WHERE U.PRODUCT_ID = C.SUB_TYPE_ID AND C.CATALOGUE_ITEM_ID = CM.CATALOGUE_ITEM_ID AND II.USIM_ID = U.USIM_ID "
    			+ "AND CM.CI_MODIFIER_ID = '" + brandId + "' AND U.CUSTOMER_ID IS NULL "
    			+ "AND U.USIM_ID IN (SELECT USIM_ID FROM USIM_STATUS WHERE EQUIPMENT_STATUS_TYPE_ID = 16 AND EQUIPMENT_STATUS_ID = 187) "
				+ "ORDER BY U.UPDATE_DT DESC";
    	List<Map<String,String>> uSimList = jdbcTemplate.query(sql, keyValueMapper);
        return uSimList;
    }

}
