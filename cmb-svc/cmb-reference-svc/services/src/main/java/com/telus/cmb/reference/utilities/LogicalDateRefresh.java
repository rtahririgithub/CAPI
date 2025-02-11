/*
 *  Copyright (c) 2017 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.cmb.reference.utilities;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.cmb.reference.svc.impl.ReferenceDataFacadeImpl;
import com.telus.eas.framework.exception.TelusException;

public class LogicalDateRefresh
{
    private static final Log logger = LogFactory.getLog(LogicalDateRefresh.class);
    
    private int timeout;
    private int duration;
    private Date expireTime;
    private ReferenceDataFacadeImpl referenceDataFacade;
    
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> executorFuture;
    
    private class LogicalDateRefreshTask implements Runnable {
        @Override
        public void run() 
        {
            logger.info( "Scheduler Service started with expireTime: " + LogicalDateRefresh.this.expireTime );
            try {
                if ( checkLogicalDateWithExitCondition() ) {
                    logger.info( "Scheduler Service cancelled" );
                    executorFuture.cancel( true );
                }
            }
            catch ( Throwable t ) {
                logger.error( t );
            }
        }
        
        private boolean checkLogicalDateWithExitCondition() throws TelusException
        {
            boolean exitCondition = false;
            Date currentTime = new Date();
            
            if ( currentTime.after( expireTime ) ) {
                logger.info( "Check logical date duration expired." );
                return true;
            }
            Date cacheLogicalDate = referenceDataFacade.getLogicalDate();
            referenceDataFacade.refreshCache( ReferenceDataFacadeImpl.LOGICAL_DATE_CACHE_NAME );
            Date newLogicalDate = referenceDataFacade.getLogicalDate();
            logger.info( "Compare cache logical dates after refreshCache" );
            if ( newLogicalDate.after( cacheLogicalDate ) ) {
                exitCondition = true;
                logger.info( "Logical Date change detected " + newLogicalDate);
            }else if (newLogicalDate.equals(cacheLogicalDate) && cacheLogicalDate.after(currentTime)) { //this is usually true in PT only
            	logger.info("Logical date in cache is same as database and greater than system date.");
            	exitCondition = true;
            }
            logger.info( "check logical date exitCondition: " + exitCondition + ", cacheLogicalDate " + cacheLogicalDate + ", databaseLogicalDate " + newLogicalDate);
            return exitCondition;
        }

    }
    
    public LogicalDateRefresh(int duration, int timeout, ReferenceDataFacadeImpl facade)
    {
        this.duration = duration;
        this.timeout = timeout;
        this.referenceDataFacade = facade;
    }
    
    public void execute() {
        Date currentTime = new Date();
        expireTime = DateUtils.addSeconds( currentTime, duration );
        logger.info("Starting logical date scheduled job...");
        
        executorFuture = executorService.scheduleWithFixedDelay(new LogicalDateRefreshTask(),
                0, timeout, TimeUnit.SECONDS );
        
    }
}
