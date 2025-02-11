package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;

public interface AdjustmentDao {
   
    List<Double> retrieveAdustmentDetails(int ban, String adjustmentReasonCode,  String subscriberId,  Date searchFromDate, Date searchToDate) throws ApplicationException; 
}
