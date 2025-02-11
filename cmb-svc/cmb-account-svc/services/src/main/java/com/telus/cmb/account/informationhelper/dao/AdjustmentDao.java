package com.telus.cmb.account.informationhelper.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.account.Charge;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;

public interface AdjustmentDao {
	
    /**
     * Returns credits for a given account.
     *
     * Depending on the parameter passed in either only billed credits,
     * only unbilled credits or all credits are being returned.
     *
     * @param pBan
     * @param pFromDate
     * @param pToDate
     * @param pBillState
     * @param level
     * @param pKnowbilityOperatorId
     * @param pSubscriber
     * @param maximum
     * 
     */
	CreditInfoHolder retrieveBilledCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, String knowbilityOperatorId, String reasonCode, char level, String subscriber, int maximum);
    
    /**
     * Returns credits for a given account .
     *
     * Depending on the parameter passed in either only billed credits,
     * only unbilled credits or all credits are being returned.
     *
     * @param   int       billing account number (BAN)
     * @param   Date      from Date
     * @param   Date      to Date
     * @param   String    bill state (mandatory)
     *                    where:
     *                    'B' billed
     *                    'U' un-billed
     *                    'A' all
     * @param   String    Knowbility operator ID (optional - default all)
     * @param   String    Reason Code
     * @param   String    Subscriber Number
     *
     * @returns CreditInfo[] array of CreditInfo (not null but could be empty)
     *
     */
	CreditInfoHolder retrieveUnbilledCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, String knowbilityOperatorId, String reasonCode, char level, String subscriber, int maximum);
	
	public class CreditInfoHolder {
		 private  Collection<CreditInfo> creditInfo;
		 private  boolean hasMore;
		 
		 public CreditInfoHolder(Collection<CreditInfo> creditInfo,boolean hasMore){
			 this.creditInfo=creditInfo;
			 this.hasMore=hasMore;
		 }

		public CreditInfoHolder() {
			
		}

		public Collection<CreditInfo> getCreditInfo() {
			return creditInfo;
		}

		public boolean hasMore() {
			return hasMore;
		}
	}
	
	 /**
     * Returns the credits based on the follow-up id
     *
     *
     * @param int followup id
     */
    List<CreditInfo> retrieveCreditByFollowUpId(int followUpId);
    
    List<ChargeInfo> retrieveCharges(int ban, String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum);
    
    /**
     * Returns related charges for a credit
     *
     * @param   pBan       		billing account number (BAN)
     * @param   pChargeSeqNo    charge sequence number
     * @return  List<ChargeInfo> 	Collection of ChargeInfo (not null but could be empty)
     *
     */
    List<ChargeInfo> retrieveRelatedChargesForCredit (int pBan, double pChargeSeqNo);
 
    /**
     * Returns related credits for a charge
     *
     * @param   pBan       		billing account number (BAN)
     * @param   pChargeSeqNo    charge sequence number
     * @return 	List<CreditInfo> 	Collection of CreditInfo (not null but could be empty)
     *
     */
    List<CreditInfo> retrieveRelatedCreditsForCharge (int pBan, double pChargeSeqNo);
    
    /**
     * Returns pending charges history
     *
     * @param   pBan       	billing account number (BAN)
     * @param   pFromDate	from Date
     * @param   pToDate     to Date
     * @param level			ChargeType.CHARGE_LEVEL_ACCOUNT, ChargeType.CHARGE_LEVEL_SUBSCRIBER, AccountManager.Search_All
     * @param pSubscriber
     * @param maximum		maximum results returned
     * 
     * @return SearchResults 	which includes  array of ChargeInfo (not null but could be empty)
     *
     */
    SearchResultsInfo retrievePendingChargeHistory (int pBan, Date pFromDate, Date pToDate, char level, String pSubscriber, int maximum);
    
    List<Double> retrieveAdjustedAmounts(int ban, String adjustmentReasonCode,  String subscriberId,  Date searchFromDate, Date searchToDate) throws ApplicationException;

	List<CreditInfo> retrieveApprovedCreditByFollowUpId(int banId,	int followUpId);

	CreditInfo retrieveCreditById(int banId, int entSeqNo); 
    
}
