CREATE OR REPLACE PACKAGE history_utility_pkg
AS
------------------------------------------------------------------------------------------------
-- description: Package history_utility_pkg containing procedures
--    for Financial history data retrieval from Knowbility database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20160 to -20169   ( 10 codes )
--
------------------------------------------------------------------------------------------------
-- Date        Version				Developer               Modifications
------------------------------------------------------------------------------------------------
-- 02-11-2005  						Marina Kuper         Added function GetPaymentHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetBilledCharges
-- 17-11-2005  						Vladimir Tsitrin           Added function GetDepositHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetInvoiceHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetPaymentMethodChangeHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetRefundHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetRelatedCreditsForCharge
-- 17-11-2005  						Vladimir Tsitrin           Added function GetSubscriberDepositHistory
-- 17-11-2005  						Vladimir Tsitrin           Added function GetSubscriberHistory
-- 22-11-2005  						Michael Qin       Added procedure retrieve_subscriber_taxes
-- 02-12-2005  						Vladimir Tsitrin           Modified function GetSubscriberHistory
-- 02-07-2006  						Michael Qin       Added procedure retrieve_sub_charge
-- 03-02-2006  						Richard Fong         Modified procedure getpaymenthistory
-- 03-03-2006  						Richard Fong         Added function GetPaymentActivities
-- 04-11-2006  						Michael Qin       Modified function GetBilledCharges
-- 04-14-2006  						Kuhan Paramsothy        Modified function GetUnbilledCreditByFollowUpId
-- 04-19-2006  						Richard Fong         Modified function GetBilledCharges
-- 07-10-2006  						Roman Tov            Modified function getpaymenthistory
-- 07-12-2006  						Kuhan Paramsothy        Added GetChargesForCredit and modifie various charge/credit fields
-- 07-19-2006  						Kuhan Paramsothy        Modified function GetBilledCharges
-- 08-01-2006  						Kuhan Paramsothy        Added procedure getaccountdepassessedhistory
-- Aug 23,2006 						Tsz Chung Tong          Modified function getbilledcredits and getunbilledcredits (sorting)
-- 09-05-2006  						Roman Tov         Modified getaccountdepassessedhistory
-- 09-08-2006  						Roman Tov         Modified getaccountdepassessedhistory
-- 09-26-2006  						Kuhan Paramsothy        Added procedure GetOrgAccDepAssessedHistory
-- 09-28-2006  						Kuhan Paramsothy        Added procedure GetOrgAccDepAssessedHistory
-- Nov 21,2006 						Tsz Chung Tong         Added brand indicator to GetSubscriberHistory
-- 07-12-2006  						Marina Kuper           Modified GetPaymentActivities to remove dynamic SQL
-- 07-12-2006  						Marina Kuper           Modified GetInvoiceHistory to remove dynamic SQL
-- 07-12-2006  						Marina Kuper           Modified GetPaymentMethodChangeHistory to remove dynamic SQL
-- 08-12-2006  						Marina Kuper           Modified GetRefundHistory to remove dynamic SQL
-- 12-12-2006  						Marina Kuper           Modified GetRelatedChargesForCredit to remove dynamic SQL
-- 12-12-2006  						Marina Kuper           Modified GetRelatedCreditsForCharge to remove dynamic SQL
-- 12-13-2006  						Marina Kuper           Modified GetSubscriberHistory to remove dynamic SQL
-- 12-13-2006  						Marina Kuper           Modified GetSubscriberDepositHistory to remove dynamic SQL
-- 12-14-2006  						Marina Kuper           Modified GetAccountDepositHistory to remove dynamic SQL
-- 01-22-2006  						Roman Tov              Modified GetAccountDepAssessedHistory to add user_full_name field
-- June 12,2007 					Michael Liao          Added procedure getCallingCirclePhoneHistory
-- June 26,2007 					Michael Liao          Added procedure getFtrParamByParamNames
-- Oct 25,2007 						Michael Liao           Modified getFtrParamByParamNames and getCallingCirclePhoneHistory to add more filtering
-- Feb 19,2009 						Tsz Chung Tong         Modified getFtrParamByParamNames to execute a different query if v_parameter_names is null or empty. 
-- April 06,2010 					Michael Liao         Modified GetPaymentActivities to return credit card authorization code - for Aug release, PCI phase 4
-- April 27,2010 					Michael Liao         Modified GetPaymentMethodChangeHistory to return first 6 / last 4 digits of creditcard 
--                                    					from BAN_Direct_DEBIT - for Aug release, PCI phase 4
-- May 04,2010  					Michael Liao          Modifyed GetPaymentHistory - PCI phase 4
--                                      				1) retrieve first 6 / last 4 digits of credit card from PAYMENT table
--                                      				2) modify the query so that can return last activity's CC authCode and correct acutal amount 
--                                         					for each payment.
-- Sept 03,2010 					Michael Liao          Modified the query in GetPaymentHistory to return correct isBanaceIgnoreFlag, right now the result is 
--                                    					not alway align with CSM online
-- Nov 25, 2010 					Michael Liao          Fix GetPaymentMethodChangeHistory's query to match DAO's logic.
-- Apr 12, 2012 					Michael Liao          Modify queries in getFtrParamByParamNames - add sfp.sys_update_date into selection list.
-- May 04,2012 	   3.19.1			M.Liao         Modify query in both getCallingCirclePhoneHistory and getFtrParamByParamNames, change effective criteria
-- May 29,2012 	   3.19.2			M.Liao         Modify query in getFtrParamByParamNames - add three more fields in the selection list:
--                                        			soc_seq_no, FTR_SOC_VER_NO, SERVICE_FTR_SEQ_NO   
-- Jan 31,2013 		3.22.3 			Naresh Annabathula     fixed reason code issue for getbilledcredits and getunbilledcredits functions
-- Mar 20, 2013 	3.23.1			Chung          Refactored subQueries in RA_UTILITY_PKG.getaccountinfobyban to this package. Added:
--                                    			getLast12MthsCollection, getCollectionStep, getWrittenOffDate, getDishonouredPaymentCounts, getLastPayment                                                          
-- March 27,2013  	3.23.2			Mahan Razaghzadeh   Added two new enhanced/tuned new functions : getUnbilledCreditsEnhanced,getPendingChargesEnhanced 
-- April 10,2013  	3.23.3			Mahan Razaghzadeh   Added new enhanced/tuned new functions : getBilledCreditsEnhanced 
-- April 11,2013  	3.23.4			Tsz Chung Tong     Convert getPendingChargesEnhanced to static SQL
-- April 11,2013  	3.23.5			Tsz Chung Tong     Minor fix in getPendingChargesEnhanced
-- April 12,2013    3.23.6          Tsz Chung Tong     Convert getBilledCreditsEnhanced and getUnbilledCreditsEnhanced to static SQL
-- May 2, 2013      3.23.7          Tsz Chung Tong     Fix in getPendingChargesEnhanced, getBilledCreditsEnhanced and getUnbilledCreditsEnhanced on matching trimming reason code
-- 2017-12-05		2018.04			Emerson Cho			Removed GetBilledCredits, GetUnbilledCredits, GetPendingCharges since they are not used
-----------------------------------------------------------------------------------------------------------------
   bannotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (bannotfound, -20101);

   TYPE refcursor IS REF CURSOR;

-- search constants
   search_all          CONSTANT VARCHAR2 (1)   := '*';
-- result constants
   numeric_true        CONSTANT NUMBER (1)     := 0;
   numeric_false       CONSTANT NUMBER (1)     := 1;
-- error messages
   err_invalid_input   CONSTANT VARCHAR2 (50) := 'Input parameters are invalid or NULL.';
   err_no_data_found   CONSTANT VARCHAR2 (30) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (30) := 'Other PL/SQL error.';
-- absolute maximum for the number of accounts to be retrieved
   max_maximorum       CONSTANT NUMBER (4)     := 1000;
   version_no          CONSTANT VARCHAR2(10)       := '2018.04';

   FUNCTION getletterrequests (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_subscriber_no   IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      i_has_more        OUT      NUMBER,
      v_err_msg         OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getpaymenthistory (
      i_ban         IN       NUMBER,
      o_from_date   IN       VARCHAR2,
      o_to_date     IN       VARCHAR2,
      o_payments    OUT      refcursor
   );

   FUNCTION getbilledcharges (
      i_ban             IN       NUMBER,
      i_bill_seq_no     IN       NUMBER,
      v_phone_no        IN       VARCHAR2,
      v_date_from       IN       DATE,
      v_date_to         IN       DATE,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getaccountdeposithistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getinvoicehistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getpaymentmethodchangehistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getrefundhistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getrelatedcreditsforcharge (
      i_ban             IN       NUMBER,
      i_charge_seq_no   IN       NUMBER,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubscriberdeposithistory (
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubscriberhistory (
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE retrieve_subscriber_taxes (
      ban_id          IN       NUMBER,
      subscriber_id   IN       VARCHAR2,
      bill_seq_no     IN       NUMBER,
      c_results       OUT      refcursor
   );

   PROCEDURE retrieve_sub_charge (
      ban_id         IN       NUMBER,
      bill_seq_num   IN       NUMBER,
      c_result       OUT      refcursor
   );

   FUNCTION getpaymentactivities (
      i_ban              IN       NUMBER,
      i_payment_seq_no   IN       NUMBER,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getunbilledcreditbyfollowupid (
      i_follow_up_id   IN       NUMBER,
      o_credit         OUT      refcursor
   );

   FUNCTION getrelatedchargesforcredit (
      i_ban             IN       NUMBER,
      i_charge_seq_no   IN       NUMBER,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getaccountdepassessedhistory (
      i_ban   IN       NUMBER,
      o_dep   OUT      refcursor
   );

   PROCEDURE getorgaccdepassessedhistory (i_ban IN NUMBER, o_dep OUT refcursor);

   PROCEDURE getCallingCirclePhoneHistory (
      c_cursor          OUT      refcursor,
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_product_type    IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2
   );
   
   PROCEDURE getFtrParamByParamNames (
      c_cursor          OUT      refcursor,
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_product_type    IN       VARCHAR2,
      v_parameter_names IN       T_PARAMETER_NAME_ARRAY,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2
   );
   
   PROCEDURE getLast12MthsCollection(
   	pi_ban IN NUMBER,
   	pi_logical_date IN DATE, 
   	c_collection OUT refcursor
   );

   PROCEDURE getCollectionStep(
   	pi_ban IN NUMBER,
   	po_col_step                    OUT      NUMBER,
    po_col_actv_code               OUT      VARCHAR2,
    po_col_actv_date               OUT      DATE
   	);

	PROCEDURE getWrittenOffDate(
		pi_ban IN NUMBER,
		po_col_written_off_date OUT DATE
	);
							
	PROCEDURE getDishonouredPaymentCounts(
		pi_ban IN NUMBER,
		c_dck  OUT refcursor,
		c_finance_history  OUT refcursor
		);
         
		 
     PROCEDURE getLastPayment(
     	pi_ban IN NUMBER,
     	po_last_payment_amnt           OUT      NUMBER,
       po_last_payment_date           OUT      DATE,
     	po_last_payment_actual_amt       OUT      NUMBER,
     	po_last_payment_actv_code      OUT      VARCHAR2
     );
   
   FUNCTION getVersion RETURN VARCHAR2;
   
  FUNCTION getUnbilledCreditsEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_operator_id     IN       VARCHAR2,
      v_subscriber_no   IN       VARCHAR2,
      v_reason_code     IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      V_ERR_MSG         OUT      varchar2
   )
   RETURN NUMBER;

   FUNCTION getPendingChargesEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_subscriber_no   IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
   RETURN NUMBER;

   FUNCTION getBilledCreditsEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_operator_id     IN       VARCHAR2,
      v_subscriber_no   IN       VARCHAR2,
      v_reason_code     IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
   RETURN NUMBER;   
   
   
END;
/
SHO err

CREATE OR REPLACE PACKAGE BODY history_utility_pkg
AS
   FUNCTION getBilledCreditsEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_operator_id     IN       VARCHAR2,
      v_subscriber_no   IN       VARCHAR2,
      v_reason_code     IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result            NUMBER (1);
      i_max               NUMBER (4);
   BEGIN
      BEGIN
         -- 
         IF i_maximum > 0 AND i_maximum < max_maximorum
         THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         i_max := i_max + 1;
         OPEN O_CREDITS
         FOR
		SELECT *
  		FROM (  SELECT a.*,
                 (CASE
                     WHEN (COUNT (*) OVER ()) > i_maximum THEN numeric_true
                     ELSE numeric_false
                  END)
                    AS has_more
            FROM (SELECT adj.adj_creation_date,
                         adj.actv_date,
                         adj.actv_code,
                         adj.actv_reason_code,
                         adj.balance_impact_code,
                         adj.subscriber_no,
                         adj.product_type,
                         adj.operator_id,
                         adj.actv_amt,
                         adj.tax_gst_amt,
                         adj.tax_pst_amt,
                         adj.tax_hst_amt,
                         adj.soc,
                         adj.feature_code,
                         adj.ent_seq_no,
                         'Y',
                         adj.bl_ignore_ind,
                         adj.charge_seq_no,
                         adj.tax_roaming_amt
                    FROM adjustment adj, bill b
                   WHERE     adj.ban = i_ban
                         AND adj.actv_bill_seq_no IS NOT NULL
                         AND adj.adj_creation_date BETWEEN o_from_date
                                                       AND o_to_date
                         AND adj.ban = b.ban
                         AND adj.actv_bill_seq_no = b.bill_seq_no
                         AND b.bill_conf_status = 'C'
                         AND (   RTRIM(adj.actv_reason_code) = RTRIM(v_reason_code)
                              OR v_reason_code IS NULL)
                         AND (   adj.operator_id = v_operator_id
                              OR v_operator_id IS NULL)
                         AND (   (    adj.subscriber_no IS NULL
                                  AND i_is_ban_level = numeric_true
                                  AND i_is_sub_level = numeric_false)
                              OR (    adj.subscriber_no = v_subscriber_no
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NOT NULL)
                              OR (    adj.subscriber_no IS NOT NULL
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NULL)
                              OR (i_is_ban_level = i_is_sub_level))) a
           WHERE ROWNUM <= i_max
        ORDER BY adj_creation_date DESC, ent_seq_no DESC)
 		WHERE ROWNUM <= (i_max - 1);

         i_result := numeric_true;
      
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_err_msg := err_no_data_found;
         WHEN OTHERS
         THEN
            v_err_msg := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   end getBilledCreditsEnhanced;

   FUNCTION getletterrequests (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_subscriber_no   IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      i_has_more        OUT      NUMBER,
      v_err_msg         OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_stmt              VARCHAR2 (32767);
      v_stmt_sub_clause   VARCHAR2 (32767);
      i_result            NUMBER (1);
      i_max               NUMBER (4);
      i_count             NUMBER (4);
   BEGIN
      BEGIN
         -- first count records
         -- this is done since we cannot create temporary table and modified array of records to client
         -- as we are basically opening the actual data cursor for the client to parse
         IF i_maximum > 0 AND i_maximum < max_maximorum
         THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         i_max := i_max + 1;
         v_stmt :=
               'SELECT count(1) '
            || ' FROM lms_requests '
            || ' WHERE   ban = '
            || i_ban
            || ' AND     (letter_req_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''''
            || ' or letter_prod_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''')';

         -- subscriber clause
         IF i_is_ban_level = numeric_true AND i_is_sub_level = numeric_false
         THEN
            v_stmt_sub_clause := ' AND subscriber is null ';
         ELSIF     i_is_sub_level = numeric_true
               AND i_is_ban_level = numeric_false
               AND v_subscriber_no IS NOT NULL
         THEN
            v_stmt_sub_clause :=
                            ' AND subscriber = ''' || v_subscriber_no || '''';
         ELSIF i_is_sub_level = numeric_true
               AND i_is_ban_level = numeric_false
         THEN
            v_stmt_sub_clause := ' AND subscriber is not null ';
         ELSE
            v_stmt_sub_clause := '';
         END IF;

         v_stmt := v_stmt || v_stmt_sub_clause;
         v_stmt := v_stmt || ' AND rownum <= ' || i_max;
         v_stmt :=
               v_stmt
            || ' union '
            || ' SELECT count(1) '
            || ' FROM LMS_REQUESTS_HIS '
            || ' where ban = '
            || i_ban
            || ' AND     (letter_req_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''''
            || ' or letter_prod_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''')';
         v_stmt := v_stmt || v_stmt_sub_clause;
         v_stmt := v_stmt || ' AND rownum <= ' || i_max;
         v_stmt :=
            'select * from (' || v_stmt || ') where rownum <= '
            || (i_max + 1);

         OPEN o_credits
          FOR v_stmt;

         FETCH o_credits
          INTO i_count;

         i_max := i_count;

         FETCH o_credits
          INTO i_count;

         i_max := i_max + i_count;

         CLOSE o_credits;

         IF i_max > i_maximum
         THEN
            i_has_more := numeric_true;
         ELSE
            i_has_more := numeric_false;
         END IF;

         -- secondly perform actual query
         IF i_maximum > 0 AND i_maximum < max_maximorum
         THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         v_stmt :=
               'SELECT REQ_NO, OPERATOR_ID, LETTER_CAT, LETTER_CODE, '
            || ' SUBSCRIBER, LETTER_REQ_DATE, LETTER_STATUS, LETTER_PROD_DATE, VARS1, LETTER_VER '
            || ' FROM lms_requests '
            || ' WHERE   ban = '
            || i_ban
            || ' AND     (letter_req_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''''
            || ' or letter_prod_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''')';
         v_stmt := v_stmt || v_stmt_sub_clause;
         v_stmt := v_stmt || ' AND rownum <= ' || i_max;
         v_stmt :=
               v_stmt
            || ' union '
            || ' select REQ_NO, OPERATOR_ID, LETTER_CAT, LETTER_CODE, '
            || '  SUBSCRIBER, LETTER_REQ_DATE, LETTER_STATUS, '
            || ' LETTER_PROD_DATE, VARS1, LETTER_VER '
            || ' FROM LMS_REQUESTS_HIS '
            || ' where ban = '
            || i_ban
            || ' AND     (letter_req_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''''
            || ' or letter_prod_date between '''
            || o_from_date
            || ''' and '''
            || o_to_date
            || ''')';
         v_stmt := v_stmt || v_stmt_sub_clause;
         v_stmt := v_stmt || ' AND rownum <= ' || i_max;
         v_stmt :=
                  'select * from (' || v_stmt || ') where rownum <= ' || i_max;

         OPEN o_credits
          FOR v_stmt;

         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_err_msg := err_no_data_found;
         WHEN OTHERS
         THEN
            v_err_msg := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getletterrequests;

   PROCEDURE getpaymenthistory (
      i_ban         IN       NUMBER,
      o_from_date   IN       VARCHAR2,
      o_to_date     IN       VARCHAR2,
      o_payments    OUT      refcursor
   )
   IS
   BEGIN
      OPEN o_payments
       FOR
          SELECT DISTINCT pa.actv_date, p.pym_method, p.pym_sub_method,
                          NVL (p.original_amt, 0), NVL (p.amt_due, 0),
                          p.source_type, p.source_id, p.bank_code,
                          p.bank_account_no, p.bank_branch_number,
                          p.check_no, p.cr_card_no, 
                          TO_CHAR (p.cr_card_exp_date, 'mm'),
                          TO_CHAR (p.cr_card_exp_date, 'yyyy'),
                          p.deposit_date, SUM (NVL (parf.actv_amt, 0)),
                          b.bill_date, pa1.actv_code, pa1.actv_reason_code,
                          pa1.bl_ignore_ind, pa.ent_seq_no,
                          pa1.cr_card_auth_code, p.original_ban,
                          p.file_seq_no, p.batch_no, p.batch_line_no,
						  p.PYMT_CARD_FIRST_SIX_STR, p.PYMT_CARD_LAST_FOUR_STR --new for PCI
                     FROM payment p,
                          payment_activity pa, --the first activity
              			  payment_activity pa1, --the last activity
                          bill b,
                          payment_activity parf --for caculate the actual amount
                    WHERE p.ban = i_ban
                      AND pa.ban = p.ban
                      AND pa.ent_seq_no = p.ent_seq_no
                      AND pa.actv_code IN ('PYM', 'FNTT') --this criteria limit to payment only
                      AND pa.actv_date BETWEEN TO_DATE (o_from_date,'mm/dd/yyyy') AND TO_DATE (o_to_date,'mm/dd/yyyy')
                      AND pa1.ban = p.ban
                      AND pa1.ent_seq_no = p.ent_seq_no
					  AND pa1.actv_seq_no =(select max(pa3.actv_seq_no) from payment_activity pa3 where pa3.ban=p.ban and pa3.ent_seq_no=p.ent_seq_no)
                      AND pa.ban = b.ban
                      AND pa.actv_bill_seq_no = b.bill_seq_no
                      AND parf.ban = p.ban
                      AND parf.ent_seq_no = p.ent_seq_no
                      /*
                      AND parf.actv_code IN ('PYM', 'FNTT', 'FNTF')
                      AND parf.actv_date BETWEEN TO_DATE (o_from_date,
                                                          'mm/dd/yyyy')
                                             AND TO_DATE (o_to_date,
                                                          'mm/dd/yyyy')
                      */
                 GROUP BY pa.actv_date,
                          p.pym_method,
                          p.pym_sub_method,
                          NVL (p.original_amt, 0),
                          NVL (p.amt_due, 0),
                          p.source_type,
                          p.source_id,
                          p.bank_code,
                          p.bank_account_no,
                          p.bank_branch_number,
                          p.check_no,
                          p.cr_card_no,
                          TO_CHAR (p.cr_card_exp_date, 'mm'),
                          TO_CHAR (p.cr_card_exp_date, 'yyyy'),
                          p.deposit_date,
                          b.bill_date,
                          pa1.actv_code,
                          pa1.actv_reason_code,
                          pa1.bl_ignore_ind,
                          pa.ent_seq_no,
                          pa1.cr_card_auth_code,
                          p.original_ban,
                          p.file_seq_no,
                          p.batch_no,
                          p.batch_line_no,
                          p.PYMT_CARD_FIRST_SIX_STR, p.PYMT_CARD_LAST_FOUR_STR;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_payments%ISOPEN)
         THEN
            CLOSE o_payments;
         END IF;

         -- return NULL cursor
         OPEN o_payments
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_payments%ISOPEN)
         THEN
            CLOSE o_payments;
         END IF;

         raise_application_error (-20160,
                                     'Get Payment History Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END getpaymenthistory;

   FUNCTION getbilledcharges (
      i_ban             IN       NUMBER,
      i_bill_seq_no     IN       NUMBER,
      v_phone_no        IN       VARCHAR2,
      v_date_from       IN       DATE,
      v_date_to         IN       DATE,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL AND i_bill_seq_no IS NOT NULL
         THEN
            v_cursor_text :=
                  'SELECT ent_seq_no, chg_creation_date, effective_date, actv_code, actv_reason_code, feature_code, '
               || '       ftr_revenue_code, balance_impact_code, subscriber_no, product_type, operator_id, '
               || '       actv_amt, tax_gst_amt, tax_pst_amt, tax_hst_amt, bl_ignore_ind, soc, priod_cvrg_st_date, '
               || '	 	 priod_cvrg_nd_date, tax_roaming_amt, tax_gst_exmp_src, tax_pst_exmp_src, tax_hst_exmp_src, tax_roam_exmp_src '
               || '  FROM charge '
               || ' WHERE ban = '
               || i_ban
               || '   AND actv_bill_seq_no = '
               || i_bill_seq_no;

            IF     v_phone_no IS NOT NULL
               AND LENGTH (RTRIM (LTRIM (v_phone_no))) > 0
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || '   AND subscriber_no = '''
                  || v_phone_no
                  || '''';
            END IF;

            IF v_date_from IS NOT NULL AND v_date_to IS NOT NULL
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || ' AND chg_creation_date BETWEEN '''
                  || v_date_from
                  || ''' AND '''
                  || v_date_to
                  || '''';
            END IF;

            v_cursor_text := v_cursor_text || ' ORDER BY ent_seq_no DESC';

            OPEN c_cursor
             FOR v_cursor_text;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getbilledcharges;

   FUNCTION getaccountdeposithistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_date_from IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_from))) > 0
            AND v_date_to IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_to))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   inv_creation_date, inv_due_date, inv_status,
                         charges_amt, dep_paid_amt, dep_paid_date,
                         dep_return_date, dep_return_mthd, dep_terms_code,
                         cancel_date, cancel_rsn_code, pym_exp_ind,
                         subscriber_no, operator_id, dep_return_amt,
                         charges_amt
                    FROM invoice_item
                   WHERE ban = i_ban
                     AND inv_type = 'D'
                     AND inv_creation_date BETWEEN TO_DATE (v_date_from,
                                                            'mm/dd/yyyy')
                                               AND TO_DATE (v_date_to,
                                                            'mm/dd/yyyy')
                ORDER BY inv_creation_date DESC, sys_creation_date DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getaccountdeposithistory;

   FUNCTION getinvoicehistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_date_from IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_from))) > 0
            AND v_date_to IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_to))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   cycle_close_date, bill_due_date, bill_delivery_ind,
                         prev_balance_amt, total_due_amt, actual_balance_amt,
                         pym_received_amt,
                           NVL (total_billed_adjust, 0)
                         + NVL (curr_credit_amt, 0),
                         past_due_amt, late_pym_chrg_amt, curr_charge_amt,
                           NVL (tax_gst_amt, 0)
                         + NVL (tax_pst_amt, 0)
                         + NVL (tax_hst_amt, 0)
                         + NVL (tax_rm_amt, 0),
                         bill_seq_no, cycle_run_year, cycle_run_month,
                         cycle_code, bill_conf_status, ctns_num_home_calls,
                         ctns_num_rm_calls, ctns_num_home_mins,
                         ctns_num_rm_mins, curr_rc_chrg_amt,
                           NVL (local_toll_amt, 0)
                         + NVL (local_at_amt, 0)
                         + NVL (local_ac_amt, 0),
                         curr_oc_chrg_amt,
                           NVL (zone_toll_amt, 0)
                         + NVL (zone_at_amt, 0)
                         + NVL (zone_ac_amt, 0),
                           NVL (eha_toll_amt, 0)
                         + NVL (eha_at_amt, 0)
                         + NVL (eha_ac_amt, 0)
                    FROM bill
                   WHERE ban = i_ban
                     AND cycle_close_date BETWEEN TO_DATE (v_date_from,
                                                           'mm/dd/yyyy')
                                              AND TO_DATE (v_date_to,
                                                           'mm/dd/yyyy')
                ORDER BY cycle_run_year DESC, cycle_run_month DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getinvoicehistory;

   FUNCTION getpaymentmethodchangehistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_date_from IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_from))) > 0
            AND v_date_to IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_to))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT NVL (sys_update_date, sys_creation_date),
                       credit_card_type, credit_card_no,
                       TO_CHAR (expiration_date, 'mmyy'), dd_status,
                       bnk_code, bnk_acct_number, bnk_branch_number
                       ,PYMT_CARD_FIRST_SIX_STR, PYMT_CARD_LAST_FOUR_STR --PCI changes
                  FROM ban_direct_debit
                 WHERE ban = i_ban
                   AND (   sys_creation_date BETWEEN TO_DATE (v_date_from,
                                                              'mm/dd/yyyy')
                                                 AND TO_DATE (v_date_to,
                                                              'mm/dd/yyyy')
                        OR sys_update_date BETWEEN TO_DATE (v_date_from,
                                                            'mm/dd/yyyy')
                                               AND TO_DATE (v_date_to,
                                                            'mm/dd/yyyy')
                       );

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getpaymentmethodchangehistory;

   FUNCTION getrefundhistory (
      i_ban             IN       NUMBER,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_date_from IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_from))) > 0
            AND v_date_to IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_to))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   actv_date, actv_code, actv_reason_code, actv_amt,
                         extract_to_ap_ind, extract_to_ap_date,
                         ref_payment_method, bank_code, bank_acct_no,
                         check_no, cr_card_no, cr_card_auth_code,
                         cr_card_exp_date, coupon_gift_no
                    FROM refund
                   WHERE ban = i_ban
                     AND actv_date BETWEEN TO_DATE (v_date_from, 'mm/dd/yyyy')
                                       AND TO_DATE (v_date_to, 'mm/dd/yyyy')
                ORDER BY actv_date DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getrefundhistory;

   FUNCTION getrelatedcreditsforcharge (
      i_ban             IN       NUMBER,
      i_charge_seq_no   IN       NUMBER,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL AND i_charge_seq_no IS NOT NULL
         THEN
            OPEN c_cursor
             FOR
                SELECT   ent_seq_no, adj_creation_date, effective_date,
                         actv_code, actv_reason_code, balance_impact_code,
                         subscriber_no, product_type, operator_id, actv_amt,
                         tax_gst_amt, tax_pst_amt, tax_hst_amt, soc,
                         'Y' approval_status, bl_ignore_ind, charge_seq_no
                    FROM adjustment
                   WHERE ban = i_ban AND charge_seq_no = i_charge_seq_no
                UNION
                SELECT   ent_seq_no, adj_creation_date, effective_date,
                         actv_code, actv_reason_code, balance_impact_code,
                         subscriber_no, product_type, operator_id, actv_amt,
                         tax_gst_amt, tax_pst_amt, tax_hst_amt, soc,
                         'Y' approval_status, bl_ignore_ind, charge_seq_no
                    FROM pending_adjustment
                   WHERE ban = i_ban
                     AND charge_seq_no = i_charge_seq_no
                     AND approval_status = 'P'
                ORDER BY adj_creation_date DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getrelatedcreditsforcharge;

   FUNCTION getrelatedchargesforcredit (
      i_ban             IN       NUMBER,
      i_charge_seq_no   IN       NUMBER,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL AND i_charge_seq_no IS NOT NULL
         THEN
            OPEN c_cursor
             FOR
                SELECT ent_seq_no, chg_creation_date, effective_date,
                       actv_code, actv_reason_code, feature_code,
                       ftr_revenue_code, balance_impact_code, subscriber_no,
                       product_type, operator_id, actv_amt, tax_gst_amt,
                       tax_pst_amt, tax_hst_amt, bl_ignore_ind, soc,
                       priod_cvrg_st_date, priod_cvrg_nd_date,
                       tax_roaming_amt, tax_gst_exmp_src, tax_pst_exmp_src,
                       tax_hst_exmp_src, tax_roam_exmp_src, actv_bill_seq_no,
                       1 AS is_billed
                  FROM charge
                 WHERE ban = i_ban AND ent_seq_no = i_charge_seq_no
                UNION
                SELECT ent_seq_no, chg_creation_date, effective_date,
                       actv_code, actv_reason_code, feature_code,
                       ftr_revenue_code, balance_impact_code, subscriber_no,
                       product_type, operator_id, actv_amt, tax_gst_amt,
                       tax_pst_amt, tax_hst_amt, bl_ignore_ind, soc,
                       priod_cvrg_st_date, priod_cvrg_nd_date,
                       tax_roaming_amt, tax_gst_exmp_src, tax_pst_exmp_src,
                       tax_hst_exmp_src, tax_roam_exmp_src, actv_bill_seq_no,
                       NULL AS is_billed
                  FROM pending_charge
                 WHERE ban = i_ban AND ent_seq_no = i_charge_seq_no;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getrelatedchargesforcredit;

   FUNCTION getsubscriberdeposithistory (
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_subscriber_no IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_subscriber_no))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   inv_creation_date, inv_due_date, inv_status,
                         charges_amt, dep_paid_amt, dep_paid_date,
                         dep_return_date, dep_return_mthd, dep_terms_code,
                         cancel_date, cancel_rsn_code, pym_exp_ind,
                         subscriber_no, operator_id
                    FROM invoice_item
                   WHERE ban = i_ban
                     AND inv_type = 'D'
                     AND subscriber_no = v_subscriber_no
                ORDER BY inv_creation_date DESC, sys_creation_date DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getsubscriberdeposithistory;

   FUNCTION getsubscriberhistory (
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND v_subscriber_no IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_subscriber_no))) > 0
            AND v_date_from IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_from))) > 0
            AND v_date_to IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_date_to))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   sub_status_date, sub_status, sub_status_last_act,
                         sub_status_rsn_code, prv_ban, next_ban,
                         NVL (brand_id, 1) brand_ind
                    FROM subscriber
                   WHERE customer_id = i_ban
                     AND subscriber_no = v_subscriber_no
                     AND (    TRUNC (effective_date) >=
                                           TO_DATE (v_date_from, 'mm/dd/yyyy')
                          AND TRUNC (effective_date) <=
                                             TO_DATE (v_date_to, 'mm/dd/yyyy')
                         )
                UNION ALL
                SELECT   sub_status_date, sub_status, sub_status_last_act,
                         sub_status_rsn_code, prv_ban, next_ban,
                         NVL (brand_id, 1) brand_ind
                    FROM subscriber_history
                   WHERE customer_id = i_ban
                     AND subscriber_no = v_subscriber_no
                     AND (    TRUNC (effective_date) >=
                                           TO_DATE (v_date_from, 'mm/dd/yyyy')
                          AND TRUNC (effective_date) <=
                                             TO_DATE (v_date_to, 'mm/dd/yyyy')
                         )
                ORDER BY 1 DESC;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getsubscriberhistory;

   /* retrieve subscriber taxes */
   PROCEDURE retrieve_subscriber_taxes (
      ban_id          IN       NUMBER,
      subscriber_id   IN       VARCHAR2,
      bill_seq_no     IN       NUMBER,
      c_results       OUT      refcursor
   )
   IS
   BEGIN
      OPEN c_results
       FOR
          SELECT   c.ftr_revenue_code, SUM (NVL (c.actv_amt, 0)),
                   SUM (NVL (c.tax_gst_amt, 0)),
                   SUM (NVL (c.tax_pst_amt, 0)),
                   SUM (NVL (c.tax_hst_amt, 0)),
                   SUM (NVL (c.tax_roaming_amt, 0)),
                   MIN (c.tax_gst_exmp_src), MIN (c.tax_pst_exmp_src),
                   MIN (c.tax_hst_exmp_src), MIN (c.tax_roam_exmp_src),
                   SUM ((SELECT SUM (  NVL (a.tax_gst_amt, 0)
                                     + NVL (a.tax_pst_amt, 0)
                                     + NVL (a.tax_hst_amt, 0)
                                     + NVL (a.tax_roaming_amt, 0))
                           FROM adjustment a
                          WHERE a.ban = c.ban
                            AND a.charge_seq_no = c.ent_seq_no))
              FROM charge c
             WHERE c.ban = ban_id
               AND c.subscriber_no = subscriber_id
               AND c.actv_bill_seq_no = bill_seq_no
          GROUP BY c.ftr_revenue_code;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN c_results
          FOR
             SELECT NULL
               FROM DUAL;
      WHEN OTHERS
      THEN
         raise_application_error
            (-20161,
                'ERROR: history_utility_pkg.retrieve_subscriber_taxes(). SQL ['
             || SQLCODE
             || '] Error ['
             || SQLERRM
             || ']',
             TRUE
            );
   END retrieve_subscriber_taxes;

   PROCEDURE retrieve_sub_charge (
      ban_id         IN       NUMBER,
      bill_seq_num   IN       NUMBER,
      c_result       OUT      refcursor
   )
   IS
   BEGIN
      OPEN c_result
       FOR
          SELECT NVL (product_type, ''), subscriber_no, NVL (pp_code, ''),
                 CONCAT (NVL (comp_bill_name_line1, ''),
                         NVL (comp_bill_name_line2, '')),
                 NVL (curr_charge_amt, 0), NVL (curr_credit_amt, 0),
                 NVL (tax_gst_amt, 0), NVL (tax_pst_amt, 0),
                 NVL (tax_hst_amt, 0), NVL (tax_rm_amt, 0),
                   NVL (curr_charge_amt, 0)
                 - NVL (curr_credit_amt, 0)
                 + NVL (tax_gst_amt, 0)
                 + NVL (tax_pst_amt, 0)
                 + NVL (tax_hst_amt, 0)
                 + NVL (tax_rm_amt, 0)
            FROM bill_subscriber
           WHERE ban = ban_id AND bill_seq_no = bill_seq_num;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN c_result
          FOR
             SELECT NULL
               FROM DUAL;
      WHEN OTHERS
      THEN
         raise_application_error
               (-20162,
                   'ERROR: history_utility_pkg.retrieve_sub_charge(). SQL ['
                || SQLCODE
                || '] Error ['
                || SQLERRM
                || ']',
                TRUE
               );
   END retrieve_sub_charge;

   FUNCTION getpaymentactivities (
      i_ban              IN       NUMBER,
      i_payment_seq_no   IN       NUMBER,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL AND i_payment_seq_no IS NOT NULL
         THEN
            OPEN c_cursor
             FOR
                SELECT pa.actv_code, pa.actv_reason_code, pa.actv_date,
                       pa.actv_amt, b.bill_date, pa.fnt_ban,
                       pa.dck_chg_group_id, pa.exception_rsn_code,
                       pa.operator_id, pa.bl_ignore_ind,
                       pa.cr_card_auth_code
                  FROM payment_activity pa, bill b
                 WHERE pa.ban = i_ban
                   AND pa.ent_seq_no = i_payment_seq_no
                   AND pa.ban = b.ban
                   AND pa.actv_bill_seq_no = b.bill_seq_no;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getpaymentactivities;

   PROCEDURE getunbilledcreditbyfollowupid (
      i_follow_up_id   IN       NUMBER,
      o_credit         OUT      refcursor
   )
   IS
   BEGIN
      OPEN o_credit
       FOR
          SELECT adj_creation_date, effective_date, actv_code,
                 actv_reason_code, balance_impact_code, subscriber_no,
                 product_type, operator_id, actv_amt, tax_gst_amt,
                 tax_pst_amt, tax_hst_amt, soc, feature_code, ent_seq_no,
                 approval_status, bl_ignore_ind, charge_seq_no, ban,
                 tax_roaming_amt
            FROM pending_adjustment
           WHERE fu_id = i_follow_up_id AND approval_status = 'P';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_credit%ISOPEN)
         THEN
            CLOSE o_credit;
         END IF;

         -- return NULL cursor
         OPEN o_credit
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_credit%ISOPEN)
         THEN
            CLOSE o_credit;
         END IF;

         raise_application_error
                           (-20160,
                               'Unbilled Credit By FollowUp Failed. Oracle:(['
                            || SQLCODE
                            || '] ['
                            || SQLERRM
                            || '])');
   END getunbilledcreditbyfollowupid;

   PROCEDURE getaccountdepassessedhistory (i_ban IN NUMBER, o_dep OUT refcursor)
   IS
   BEGIN
      OPEN o_dep
       FOR
          SELECT   NVL (c.gen_desc, 'Reason Not Specified') gen_desc,
                   a.operator_id, a.credit_date, b.product_type,
                   NVL (b.deposit_amt, 0) deposit_amt,
                   NVL (c.gen_desc_f, 'Raison Non Indiquée') gen_desc_f, u.user_full_name
              FROM credit_history a,
                   crd_deposit b, users_ltd u,
                   (SELECT   gen_code, gen_desc, gen_desc_f
                        FROM (SELECT 'A' AS force_order, gc.*
                                FROM generic_codes gc
                               WHERE gen_type = 'DEP_CHG_RSN'
                              UNION
                              SELECT 'B' AS force_order, gc.*
                                FROM generic_codes gc
                               WHERE gen_type <> 'DEP_CHG_RSN')
                    ORDER BY force_order) c
             WHERE a.ban = i_ban
               AND a.deposit_seq_no = b.deposit_seq_no
               AND TRIM (a.dep_chg_rsn_cd) = c.gen_code(+)
               AND u.user_id=a.operator_id
          -- AND b.deposit_amt IS NOT NULL
          ORDER BY a.crd_seq_no, b.product_type DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_dep%ISOPEN)
         THEN
            CLOSE o_dep;
         END IF;

         -- return NULL cursor
         OPEN o_dep
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_dep%ISOPEN)
         THEN
            CLOSE o_dep;
         END IF;

         raise_application_error
              (-20160,
                  'Getting Account Deposit Assessed History Failed. Oracle:(['
               || SQLCODE
               || '] ['
               || SQLERRM
               || '])');
   END getaccountdepassessedhistory;

   PROCEDURE getorgaccdepassessedhistory (i_ban IN NUMBER, o_dep OUT refcursor)
   IS
   BEGIN
      OPEN o_dep
       FOR
          SELECT   NULL, a.operator_id, a.credit_date, b.product_type,
                   NVL (b.deposit_amt, 0) deposit_amt, NULL
              FROM credit_history a, crd_deposit b
             WHERE a.ban = i_ban
               AND a.deposit_seq_no = b.deposit_seq_no
               -- AND b.deposit_amt IS NOT NULL
               AND a.crd_seq_no = (SELECT MIN (crd_seq_no) AS min_crd_seq_no
                                     FROM credit_history
                                    WHERE ban = i_ban)
          ORDER BY a.crd_seq_no, b.product_type DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_dep%ISOPEN)
         THEN
            CLOSE o_dep;
         END IF;

         -- return NULL cursor
         OPEN o_dep
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_dep%ISOPEN)
         THEN
            CLOSE o_dep;
         END IF;

         raise_application_error
              (-20160,
                  'Getting Account Deposit Assessed History Failed. Oracle:(['
               || SQLCODE
               || '] ['
               || SQLERRM
               || '])');
   END getorgaccdepassessedhistory;

   PROCEDURE getCallingCirclePhoneHistory (
      c_cursor          OUT      refcursor,
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_product_type    IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2
   )
   IS
   BEGIN
     OPEN c_cursor FOR
        SELECT sfp.parameter_value, sfp.effective_date, sfp.expiration_date
          FROM service_feature_parameters sfp
         WHERE sfp.ban = i_ban
           AND sfp.subscriber_no = v_subscriber_no
           AND sfp.product_type = v_product_type
           AND (TRUNC (sfp.effective_date) >=TO_DATE (v_date_from, 'mm/dd/yyyy')
               AND TRUNC (sfp.effective_date) <=TO_DATE (v_date_to, 'mm/dd/yyyy')
               )
           AND (sfp.expiration_date is null OR sfp.expiration_date >= sfp.effective_date)
           AND sfp.parameter_name = 'CALLING-CIRCLE'
           ORDER BY sfp.effective_date desc;
     EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (c_cursor%ISOPEN)
         THEN
            CLOSE c_cursor;
         END IF;

         -- return NULL cursor
         OPEN c_cursor FOR SELECT NULL FROM DUAL WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (c_cursor%ISOPEN)
         THEN
            CLOSE c_cursor;
         END IF;

         raise_application_error (-20160,
                                     'Get Calling Circle Phone List History. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getCallingCirclePhoneHistory;

   PROCEDURE getFtrParamByParamNames (
      c_cursor          OUT      refcursor,
      i_ban             IN       NUMBER,
      v_subscriber_no   IN       VARCHAR2,
      v_product_type    IN       VARCHAR2,
      v_parameter_names IN       T_PARAMETER_NAME_ARRAY,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2
   )
   IS
   BEGIN
	 IF v_parameter_names IS NULL OR v_parameter_names.COUNT = 0 THEN
	     OPEN c_cursor FOR --This is for retrieving ftr param transaction history
	        SELECT sfp.soc, sfp.feature_code, sfp.parameter_name, sfp.parameter_value, sfp.sys_creation_date, sfp.effective_date, sfp.expiration_date, sfp.operator_id, sfp.application_id
	        	, sfp.sys_update_date, sfp.soc_seq_no, sfp.ftr_soc_ver_no, sfp.service_ftr_seq_no	
	          FROM service_feature_parameters sfp
	         WHERE sfp.ban = i_ban
	           AND sfp.subscriber_no = v_subscriber_no
	           AND sfp.product_type = v_product_type
	           AND (TRUNC (sfp.sys_creation_date) >=TO_DATE (v_date_from, 'mm/dd/yyyy')
	               AND TRUNC (sfp.sys_creation_date) <=TO_DATE (v_date_to, 'mm/dd/yyyy')
	               )
	           ORDER BY sfp.sys_creation_date desc;
	 ELSE
	     OPEN c_cursor FOR --This is for retrieving effective ftr param history
	        SELECT sfp.soc, sfp.feature_code, sfp.parameter_name, sfp.parameter_value, sfp.sys_creation_date, sfp.effective_date, sfp.expiration_date, sfp.operator_id, sfp.application_id
	        	, sfp.sys_update_date, sfp.soc_seq_no, sfp.ftr_soc_ver_no, sfp.service_ftr_seq_no	
	          FROM service_feature_parameters sfp
	         WHERE sfp.ban = i_ban
	           AND sfp.subscriber_no = v_subscriber_no
	           AND sfp.product_type = v_product_type
	           AND sfp.parameter_name in 
	             ( SELECT * FROM TABLE (CAST  (v_parameter_names AS T_PARAMETER_NAME_ARRAY ) ) )
	           AND (TRUNC (sfp.effective_date) >=TO_DATE (v_date_from, 'mm/dd/yyyy')
	               AND TRUNC (sfp.effective_date) <=TO_DATE (v_date_to, 'mm/dd/yyyy')
	               )
	           AND (sfp.expiration_date is null OR sfp.expiration_date >= sfp.effective_date)
	           ORDER BY sfp.effective_date desc;
     END IF;
     EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (c_cursor%ISOPEN)
         THEN
            CLOSE c_cursor;
         END IF;

         -- return NULL cursor
         OPEN c_cursor FOR SELECT NULL FROM DUAL WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (c_cursor%ISOPEN)
         THEN
            CLOSE c_cursor;
         END IF;

         raise_application_error (-20160,
                                     'Get Feature Parameter History. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getFtrParamByParamNames;
   
   PROCEDURE getLast12MthsCollection(
   	pi_ban IN NUMBER,
   	pi_logical_date IN DATE, 
   	c_collection OUT refcursor
   )
   IS
   BEGIN
		OPEN c_collection
		FOR
		SELECT   c.col_actv_code, c.col_actv_date,
                  TO_CHAR (c.col_actv_date, 'mm') actvDateMM, ca.severity_level
             FROM collection c, collection_act ca 
            WHERE c.ban = pi_ban
              AND c.col_actv_code = ca.col_activity_code
              AND TRUNC (c.col_actv_date) > ADD_MONTHS (LAST_DAY (TRUNC (pi_logical_date)), -12)
         ORDER BY TO_CHAR (c.col_actv_date, 'mm'), ca.severity_level;
   END getLast12MthsCollection;

   PROCEDURE getCollectionStep(
   	pi_ban IN NUMBER,
   	po_col_step                    OUT      NUMBER,
    po_col_actv_code               OUT      VARCHAR2,
    po_col_actv_date               OUT      DATE
   	)
   	IS
   	CURSOR c_col_activity
      IS
         SELECT c1.col_step_num, c1.col_actv_code, c1.col_actv_date
           FROM collection c1
          WHERE c1.ban = pi_ban
            AND c1.ent_seq_no =
                       (SELECT MAX (ent_seq_no)
                          FROM collection c2
                         WHERE c2.ban = c1.ban AND c2.col_actv_type_ind = 'A');
   	BEGIN
	 OPEN c_col_activity;
	 FETCH c_col_activity
	 INTO po_col_step, po_col_actv_code, po_col_actv_date;
	 CLOSE c_col_activity;
	END getCollectionStep;

	PROCEDURE getWrittenOffDate(
		pi_ban IN NUMBER,
		po_col_written_off_date OUT DATE
	)
	IS
	 CURSOR c_written_off_date
      IS
         SELECT c1.col_actv_date
           FROM collection c1
          WHERE c1.ban = pi_ban
            AND c1.ent_seq_no =
                           (SELECT MAX (ent_seq_no)
                              FROM collection c2
                             WHERE c2.ban = c1.ban AND c2.col_actv_code = 'W');
	BEGIN
		OPEN c_written_off_date;
		FETCH c_written_off_date INTO po_col_written_off_date;
		CLOSE c_written_off_date;
	END getWrittenOffDate;
							
	PROCEDURE getDishonouredPaymentCounts(
		pi_ban IN NUMBER,
		c_dck  OUT refcursor,
		c_finance_history  OUT refcursor
		)
	IS
	BEGIN
	OPEN c_dck
	FOR
	 SELECT   TO_CHAR (pa.actv_date, 'mm') mm_dck, COUNT (*) nbr
	 FROM payment p, payment_activity pa, backout_rsn_code brc
            WHERE p.ban = pi_ban
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code = 'BCK'
              AND pa.actv_reason_code = brc.bck_code
              AND brc.dck_history_ind = 'D'
              AND TRUNC (pa.actv_date) > ADD_MONTHS (LAST_DAY (TRUNC (SYSDATE)), -12)
         GROUP BY TO_CHAR (pa.actv_date, 'mm');
         
	OPEN c_finance_history
	FOR 
	SELECT   TO_CHAR (fh.act_date, 'mm') mm_dck, COUNT (*) nbr
             FROM finance_history fh, backout_rsn_code brc
            WHERE fh.ban = pi_ban
              AND fh.act_code = 'R'
              AND brc.bck_code = fh.act_reason_code
              AND brc.dck_history_ind = 'D'
              AND TRUNC (fh.act_date) > ADD_MONTHS (LAST_DAY (TRUNC (SYSDATE)), -12)
         GROUP BY TO_CHAR (fh.act_date, 'mm');
         
     END getDishonouredPaymentCounts;
         
		 
     PROCEDURE getLastPayment(
     	pi_ban IN NUMBER,
     	po_last_payment_amnt           OUT      NUMBER,
       po_last_payment_date           OUT      DATE,
     	po_last_payment_actual_amt       OUT      NUMBER,
     	po_last_payment_actv_code      OUT      VARCHAR2
     )
     IS
     v_ent_seq_no   NUMBER (9, 0);
     
     CURSOR c_payment
      IS
         SELECT   p.original_amt, p.deposit_date, pa.ent_seq_no
             FROM payment p, payment_activity pa
            WHERE p.ban = pi_ban
              --AND (p.designation = 'B' OR p.designation IS NULL)
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code IN ('PYM', 'FNTT')
         ORDER BY pa.actv_date DESC, pa.ent_seq_no DESC;
         
	CURSOR c_pymt_actual_amt 
      IS
         select sum( actv_amt) 
           from payment_activity
          where ban= pi_ban and ent_seq_no=v_ent_seq_no
       group by ban,ent_seq_no;

     CURSOR c_pymt_last_activity 
      IS
         select actv_code
           from payment_activity
          where ban= pi_ban and ent_seq_no=v_ent_seq_no
         ORDER BY actv_seq_no DESC;
         
      
    BEGIN 
	    OPEN c_payment;
	    FETCH c_payment
	  INTO po_last_payment_amnt, po_last_payment_date, v_ent_seq_no;
	  CLOSE c_payment;
		 
	  OPEN c_pymt_actual_amt;
	  FETCH c_pymt_actual_amt INTO po_last_payment_actual_amt;
	  CLOSE c_pymt_actual_amt;
	   
	  OPEN c_pymt_last_activity;
      FETCH c_pymt_last_activity INTO po_last_payment_actv_code;
      CLOSE c_pymt_last_activity;
         
   END getLastPayment;

   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;

 --------------------------------------
 FUNCTION getUnbilledCreditsEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_operator_id     IN       VARCHAR2,
      v_subscriber_no   IN       VARCHAR2,
      v_reason_code     IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result              NUMBER (1);
      i_max                 NUMBER (4);
   BEGIN
      BEGIN
         -- first count records
         -- this is done since we cannot create temporary table and modified array of records to client
         -- as we are basically opening the actual data cursor for the client to parse
         IF i_maximum > 0 AND i_maximum < max_maximorum THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         i_max := i_max + 1;
        
         OPEN O_CREDITS
         FOR
		SELECT *
  		FROM (  SELECT a.*,
                 (CASE WHEN (COUNT (*) OVER ()) > i_maximum THEN numeric_true ELSE numeric_false END)
                    AS has_more
            FROM (SELECT adj_creation_date,
                         effective_date,
                         actv_code,
                         actv_reason_code,
                         balance_impact_code,
                         subscriber_no,
                         product_type,
                         operator_id,
                         actv_amt,
                         tax_gst_amt,
                         tax_pst_amt,
                         tax_hst_amt,
                         soc,
                         feature_code,
                         ent_seq_no,
                         approval_status,
                         bl_ignore_ind,
                         charge_seq_no,
                         tax_roaming_amt
                    FROM pending_adjustment
                   WHERE     ban = i_ban
                         AND approval_status = 'P'
                         AND adj_creation_date BETWEEN o_from_date
                                                   AND o_to_date
                         AND (   (    subscriber_no IS NULL
                                  AND i_is_ban_level = numeric_true
                                  AND i_is_sub_level = numeric_false)
                              OR (    subscriber_no = v_subscriber_no
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NOT NULL)
                              OR (    subscriber_no IS NOT NULL
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NULL)
                              OR (i_is_ban_level = i_is_sub_level))
                         AND (   RTRIM(actv_reason_code) = RTRIM(v_reason_code)
                              OR v_reason_code IS NULL)
                         AND (   operator_id = v_operator_id
                              OR v_operator_id IS NULL)
                         AND ROWNUM <= i_max
                  UNION
                  SELECT adj_creation_date,
                         effective_date,
                         actv_code,
                         actv_reason_code,
                         balance_impact_code,
                         subscriber_no,
                         product_type,
                         operator_id,
                         actv_amt,
                         tax_gst_amt,
                         tax_pst_amt,
                         tax_hst_amt,
                         soc,
                         feature_code,
                         ent_seq_no,
                         'Y',
                         bl_ignore_ind,
                         charge_seq_no,
                         tax_roaming_amt
                    FROM adjustment
                   WHERE     ban = i_ban
                         AND actv_bill_seq_no IS NULL
                         AND adj_creation_date BETWEEN o_from_date
                                                   AND o_to_date
                         AND (   (    subscriber_no IS NULL
                                  AND i_is_ban_level = numeric_true
                                  AND i_is_sub_level = numeric_false)
                              OR (    subscriber_no = v_subscriber_no
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NOT NULL)
                              OR (    subscriber_no IS NOT NULL
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NULL)
                              OR (i_is_ban_level = i_is_sub_level))
                         AND (   RTRIM(actv_reason_code) = RTRIM(v_reason_code)
                              OR v_reason_code IS NULL)
                         AND (   operator_id = v_operator_id
                              OR v_operator_id IS NULL)
                         AND ROWNUM <= i_max
                  UNION
                  SELECT adj.adj_creation_date,
                         adj.effective_date,
                         adj.actv_code,
                         adj.actv_reason_code,
                         adj.balance_impact_code,
                         adj.subscriber_no,
                         adj.product_type,
                         adj.operator_id,
                         adj.actv_amt,
                         adj.tax_gst_amt,
                         adj.tax_pst_amt,
                         adj.tax_hst_amt,
                         adj.soc,
                         adj.feature_code,
                         adj.ent_seq_no,
                         'Y',
                         adj.bl_ignore_ind,
                         adj.charge_seq_no,
                         adj.tax_roaming_amt
                    FROM adjustment adj, bill b
                   WHERE     adj.ban = i_ban
                         AND adj.adj_creation_date BETWEEN o_from_date
                                                       AND o_to_date
                         AND adj.ban = b.ban
                         AND adj.actv_bill_seq_no = b.bill_seq_no
                         AND b.bill_conf_status = 'T'
                         AND (   (    adj.subscriber_no IS NULL
                                  AND i_is_ban_level = numeric_true
                                  AND i_is_sub_level = numeric_false)
                              OR (    adj.subscriber_no = v_subscriber_no
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NOT NULL)
                              OR (    adj.subscriber_no IS NOT NULL
                                  AND i_is_sub_level = numeric_true
                                  AND i_is_ban_level = numeric_false
                                  AND v_subscriber_no IS NULL)
                              OR (i_is_ban_level = i_is_sub_level))
                         AND (   RTRIM(adj.actv_reason_code) = RTRIM(v_reason_code)
                              OR v_reason_code IS NULL)
                         AND (   adj.operator_id = v_operator_id
                              OR v_operator_id IS NULL)
                         AND ROWNUM <= i_max) a
           WHERE ROWNUM <= i_max
        ORDER BY adj_creation_date DESC, ent_seq_no DESC)
 	WHERE ROWNUM <= (i_max - 1);

	i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_err_msg := err_no_data_found;
         WHEN OTHERS
         THEN
            v_err_msg := SQLERRM;
            i_result := numeric_false;
      END;

      return I_RESULT;
   END getUnbilledCreditsEnhanced;

---------------------------------------

FUNCTION getPendingChargesEnhanced (
      i_ban             IN       NUMBER,
      o_from_date       IN       DATE,
      o_to_date         IN       DATE,
      v_subscriber_no   IN       VARCHAR2,
      i_is_ban_level    IN       NUMBER,
      i_is_sub_level    IN       NUMBER,
      i_maximum         IN       NUMBER,
      o_credits         OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result            NUMBER (1);
      i_max               NUMBER (4);
   BEGIN
      BEGIN
         IF i_maximum > 0 AND i_maximum < max_maximorum
         THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         
      OPEN O_CREDITS FOR
         SELECT *
           FROM (SELECT a.*,
                        (CASE
                            WHEN (COUNT (*) OVER ()) > i_maximum THEN numeric_true
                            ELSE numeric_false
                         END)
                           AS has_more
                   FROM (SELECT ent_seq_no,
                                chg_creation_date,
                                effective_date,
                                actv_code,
                                actv_reason_code,
                                feature_code,
                                ftr_revenue_code,
                                balance_impact_code,
                                subscriber_no,
                                product_type,
                                operator_id,
                                actv_amt,
                                tax_gst_amt,
                                tax_pst_amt,
                                tax_hst_amt,
                                approval_status,
                                bl_ignore_ind,
                                soc,
                                tax_roaming_amt,
                                tax_gst_exmp_src,
                                tax_pst_exmp_src,
                                tax_hst_exmp_src,
                                tax_roam_exmp_src
                           FROM pending_charge
                          WHERE     ban = i_ban
                                AND approval_status = 'P'
                                AND chg_creation_date BETWEEN o_from_date
                                                          AND o_to_date
                                AND (   (    subscriber_no IS NULL
                                         AND i_is_ban_level = numeric_true
                                         AND i_is_sub_level = numeric_false)
                                     OR (    subscriber_no = v_subscriber_no
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NOT NULL)
                                     OR (    subscriber_no IS NOT NULL
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NULL)
                                     OR (i_is_ban_level = i_is_sub_level))
                                AND ROWNUM <= i_max + 1
                         UNION
                         SELECT ent_seq_no,
                                chg_creation_date,
                                effective_date,
                                actv_code,
                                actv_reason_code,
                                feature_code,
                                ftr_revenue_code,
                                balance_impact_code,
                                subscriber_no,
                                product_type,
                                operator_id,
                                actv_amt,
                                tax_gst_amt,
                                tax_pst_amt,
                                tax_hst_amt,
                                'Y',
                                bl_ignore_ind,
                                soc,
                                tax_roaming_amt,
                                tax_gst_exmp_src,
                                tax_pst_exmp_src,
                                tax_hst_exmp_src,
                                tax_roam_exmp_src
                           FROM charge
                          WHERE     ban = i_ban
                                AND actv_bill_seq_no IS NULL
                                AND chg_creation_date BETWEEN o_from_date
                                                          AND o_to_date
                                AND (   (    subscriber_no IS NULL
                                         AND i_is_ban_level = numeric_true
                                         AND i_is_sub_level = numeric_false)
                                     OR (    subscriber_no = v_subscriber_no
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NOT NULL)
                                     OR (    subscriber_no IS NOT NULL
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NULL)
                                     OR (i_is_ban_level = i_is_sub_level))
                                AND ROWNUM <= i_max + 1
                         UNION
                         SELECT c.ent_seq_no,
                                c.chg_creation_date,
                                c.effective_date,
                                c.actv_code,
                                c.actv_reason_code,
                                c.feature_code,
                                c.ftr_revenue_code,
                                c.balance_impact_code,
                                c.subscriber_no,
                                c.product_type,
                                c.operator_id,
                                c.actv_amt,
                                c.tax_gst_amt,
                                c.tax_pst_amt,
                                c.tax_hst_amt,
                                'Y',
                                c.bl_ignore_ind,
                                c.soc,
                                c.tax_roaming_amt,
                                c.tax_gst_exmp_src,
                                c.tax_pst_exmp_src,
                                c.tax_hst_exmp_src,
                                c.tax_roam_exmp_src
                           FROM charge c, bill b
                          WHERE     c.ban = i_ban
                                AND c.chg_creation_date BETWEEN o_from_date
                                                            AND o_to_date
                                AND c.ban = b.ban
                                AND c.actv_bill_seq_no = b.bill_seq_no
                                AND b.bill_conf_status = 'T'
                                AND (   (    subscriber_no IS NULL
                                         AND i_is_ban_level = numeric_true
                                         AND i_is_sub_level = numeric_false)
                                     OR (    subscriber_no = v_subscriber_no
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NOT NULL)
                                     OR (    subscriber_no IS NOT NULL
                                         AND i_is_sub_level = numeric_true
                                         AND i_is_ban_level = numeric_false
                                         AND v_subscriber_no IS NULL)
                                     OR (i_is_ban_level = i_is_sub_level))
                                AND ROWNUM <= i_max+1
                         ORDER BY chg_creation_date DESC) a
                  WHERE ROWNUM <= i_max + 1)
          WHERE ROWNUM <= i_max;

         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_err_msg := err_no_data_found;
         WHEN OTHERS
         THEN
            v_err_msg := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;

   END getPendingChargesEnhanced;


END;
/

SHO err