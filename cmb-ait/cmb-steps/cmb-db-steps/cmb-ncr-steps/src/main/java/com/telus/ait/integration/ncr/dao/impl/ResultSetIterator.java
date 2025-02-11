package com.telus.ait.integration.ncr.dao.impl;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * This is used in a short-lived stand-alone application.  Never use this in a long-lived app.
 */
public class ResultSetIterator<T> implements Iterator<T> {
    private ResultSet resultSet;
    private RowMapper<T> rowMapper;
    private int i = 0;

    public ResultSetIterator(ResultSet resultSet, RowMapper<T> rowMapper) {
        this.resultSet = resultSet;
        this.rowMapper = rowMapper;
    }

    private boolean didNext = false;
    private boolean hasNext = false;

    public boolean hasNext() {
        if (!didNext) {
            try {
                hasNext = resultSet.next();
            } catch (SQLException e) {
                if ("Exhausted Resultset".equals(e.getMessage())) {
                    return false;
                }
                throw new RuntimeException("Unable to iterate", e);
            }
            didNext = true;
        }
        return hasNext;
    }

    public T next() {
        try {
            if (!didNext) {
                resultSet.next();
            }
            didNext = false;
            return rowMapper.mapRow(resultSet, i++);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to iterate", e);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("I do not support remove");
    }
}
