package com.telus.ait.integration.ncr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class NcrDao {
    @Autowired
    @Qualifier("ncrJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Long> longMapper = new RowMapper() {
        @Override
        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong(1);
        }
    };

    public long getOfferId(Long offerItemId) {
        List<Long> offerIdList = jdbcTemplate.query("select parent from NCR_OFFER_COMP_ASSOC where child = ?", new Object[]{new Long(offerItemId)}, longMapper);
        return offerIdList.get(0);
    }

    public Long checkDataSource() {
        List<Long> offerIdList = jdbcTemplate.query("select ? from dual", new Object[]{new Long(2)}, longMapper);
        return offerIdList.get(0);
    }
}
