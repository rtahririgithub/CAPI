package com.telus.cmb.account.informationhelper.dao;

import java.sql.SQLException;
import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.CollectionHistoryInfo;

public interface CollectionDao {

    /**
     * retrieveCollectionHistoryInfo
     * @param banId 		billing account number (BAN)
     * @param fromDate Date
     * @param toDate Date
     * @throws SQLException
     * @throws ApplicationException
     * @return CollectionHistoryInfo[]
     *
     */
    CollectionHistoryInfo[] retrieveCollectionHistoryInfo(int banId, Date fromDate, Date toDate) throws ApplicationException;
	
}
