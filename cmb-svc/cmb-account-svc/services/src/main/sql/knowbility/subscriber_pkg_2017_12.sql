CREATE OR REPLACE PACKAGE subscriber_pkg
AS
------------------------------------------------------------------------
-- description: Package SUBSCRIBER_PKG contains procedures and functions
--          for subscriber-related data retrieval from Knowbility
--          database.
--
-- Date           Developer            Modifications
-- 08-03-2005  Vladimir Tsitrin     created
-- 10-05-2005   Vladimir Tsitrin        updated for Nov '05 release
-- 01-30-2006  Michael Qin    Add a new procedure: retrieve_multiring()
-- 03-02-2006   Michael Qin      Add a new function: getHotlineIndicator()
-- 05-01-2006   Alejandro Silbergleit  Query  in the Subscriber_pkg should be
--                                      changed to use MARKET_NPA_NXX_LR table
--                                      instead of NPA_NXX_LR.
-- 05-03-2006  R. Fong              Added NAME_DATA.NAME_FORMAT field
--                            to all subscriber retrieval functions
-- 05-05-2006   Michael Qin             Added migration_type, migration_date, tenure_date for subscriber retrieval
-- 05-05-2006   Vladimir Tsitrin        Changed search subscriber by phone number
-- 05-24-2006   Vladimir Tsitrin        Fixed error in GetSubscriberListByIDs
-- 06-06-2006   Vladimir Tsitrin        Fixed another error in GetSubscriberListByIDs (eliminated duplicate Sub IDs)
-- 07-11-2006   Jakub Kolakowski        Added IMSI retrieval
-- 07-14-2006   Vladimir Tsitrin        Fixed performance problem in search by phone number (Vive la France!).
-- 08-10-2006   Marina Kuper            Fixed performance problem in get seconary ESN's.
-- 10-10-2006   Marina Kuper            Fixed performance problem in GetSubscriberListByESN
-- 10-12-2006   Marina Kuper            Fixed performance problem in GetSubscriberListByIDs
-- 08-19-2006   Marina Kuper            Fixed cursor issue
-- 08-28-2006   Dimitry Siganevich      Add a new function: getBanIdByPhoneNumber()
-- 09-07-2006   Vladimir Tsitrin        Fixed getIdenResources for degenerate subscribers.
-- 10-19-2006   Marina Kuper            Fixed performance problem in getIdenResources
-- 10-24-2006   Marina Kuper            Fixed performance problem in gePagerEquipmentInfo
-- 11-10-2006   Michael Liao            Added getPaidSecurityDeposit
-- 11-16-2006   Marina Kuper            Fixed getequipmentchangehistory to remove dynamic SQL
-- 11-16-2006   Marina Kuper            Fixed getproductsubscriberlists to remove dynamic SQL
-- 11-18-2006   Marina Kuper            Fixed GetEquipmentSubscribers to remove dynamic SQL
-- 11-21-2006   Marina Kuper            Fixed getsubscriberlistbybanandfleet to remove dynamic SQL
-- 11-22-2006   Marina Kuper            Fixed getsubscriberlistbybanandtg to remove dynamic SQL
-- 11-27-2006   Roman Tov               partially reserved subscribers list by BAN added
-- 12-06-2006   Michael Liao            Added getbansubphonenosbysubstatus
-- 12-11-2006   Marina Kuper            Fixed getIdenResources
-- 12-11-2006   Marina Kuper            Fixed GetEquipmentSubscribers
-- 12-13-2006   Roman Tov               partially reserved subscribers list by BAN changed
-- 01-05-2007   Michael Liao            Fixed GetMarketProvinces: change the filter for market_npa_nxx_lr.effective_date from
-- "< sysdate -1 " to "sysdate+1"
-- 01-30-2007   Dimitry Siganevich      Add a new function: GetBanForPartuallyReservedSub()
-- 06-21-2007  R. Fong              Added brand_id to subscriber retrieval SQL
-- 04-03-2008   Winnie Kwok             Added getbansubidsbysubstatus
-- 04-09-2008   Marina Kuper            Added getlogicaldate, getstartrestrdate, getpromohistory, getpromosocs
-- 04-17-2008   R. Fong                Updated getpromohistory, getpromosocs
-- 04-22-2008   Dimitry Siganevich      Add new functions: GetSubListByPhoneNumbers(), GetSubIdsByPhoneNumbers()
-- 01-08-2009   Marina Kuper            Added getSubscriberIdsByIMSI , getHSPAResources, getSubscriberListByIMSI
-- Feb-03, 2009 Michael Liao            Added getBanAndPhoneNoByIMSI
-- Mar-13, 2009 Michael Liao            Fixed getIdenResources: 
--                                           1) remove max(..) subquery, and use record_exp_date to find the current record
--                                           2) also sort by resource_seq, as such the most current record for type(X,Y,Z) resource 
--                                              will be at last, our DAO will keep up the correct IP resource, This is acutally a bug
--                                              in KB, which will be fixed in 10.0
-- Mar-30, 2009 Michael Liao            Updated getEquipmentChangeHistory to not return any result where the equipment type is U
-- Apr-02, 2009 Mujeeb Waraich		Updated methods getsubidsbyimsi, GetHSPAResources and getBanAndPhoneNoByIMSI
--					to retrive records for record_exp_date '12/31/4700'
-- Apr-06,2009 Michael Liao             fix query error in fucntion gethsparesources 
-- Apr-17, 2009 Roni Chaia				Added function getlastassocsubscriptionid
-- Apr-17, 2009 Roni Chaia				Added logic to retrieve external_id (as subscription_id) from subscriber table
-- Apr 22,2009  Tsz Chung Tong          Put a MAX row limit of 1000 in getIdenResources and getHSPAResources
-- May 12,2009 Belinda Liang	fix defect PROD00139942, modify method getlastassocsubscriptionid() to remove record_exp_date condition and add order by resource_seq
-- May 21,2009  Michael Liao    fix defect PROD00140881, completely rewrite the function GetSubListByIMSI. Previously, the search take two steps:
--                                   step1) getsubidsbyimsi, step2)call getsubscriberlistbyids using result from step1. Step1 can return correct list , but it only 
--                                   return subscriber_no list, when this list pass to step2, that's where the wrong information could be returned.
-- July 09,2009 Michael Liao         modify GetSubListByIMSI to impove performance per Winnie.
-- Aug-19, 2009 Michael Liao            Added getLwSubsByBAN - return light weight subscribers for a BAN
-- Aug-21, 2009 Michael Liao            Modify getLwSubsByBAN - add isIDEN parameter and divide the query for Mike and non-Mike account
-- Aug-25, 2009 Mujeeb Waraich		Modify getLwSubsByBAN - added comments as per provided by DBA for performance
-- AUg-28, 2009 Mujeeb Waraich		Modify getLwSubsByBAN - removed hint when subscriber_rsource table is not used in query
-- Jul 21, 2010 Tsz Chung Tong      Modify getLwSubsByBAN to return external_id
-- September 9, 2010 Hilton Poon	Fixed getLwSubsByBAN - Mike branch was returning subscription_id instead of external_id
-- Nov 14, 2011 Brandon Wen         Added OUT parameter, v_external_id, in getsubidbybanandphonenumber Function to return external_id
-- Apr 24, 2012 Tsz Chung Tong      Added getVersion function and getHotlinedPhoneNoByBan function
-- Apr 26, 2012 Tsz Chung Tong      Added retrieveResourceChangeHistory procedure
-- Aug 27, 2012 Inbaselvan Gandhi	Added for Production Mike Issue in Jan 2013 release
-- Sep 11, 2012 Tsz Chung Tong      3.20.4: Fixed compilation error on missing length in getsubbybanandphonenumber's variable v_sub_id.
-- Sep 11, 2012 Tsz Chung Tong      3.20.5: Fixed "ORA-00907: missing right parenthesis" error in getsubidsbybanandphonenumber
-- Sep 12, 2012 Hassain Shaik		3.21.4  : Fixing up version Number to match sql build Number
-- Sep 14, 2012 Tsz Chung Tong      3.21.5: Changed getsubidsbybanandphonenumber to use binding variable
-- Sep 24, 2012 Inbaselvan Gandhi	3.21.6: Changed getsubidsbybanandphonenumber and getsubbybanandphonenumber for defect fix. 
-- Sep 27, 2012 Jun Yan             3.21.7: Changed getsubidsbybanandphonenumber: "order by" fix
-- May 20, 2014 Naresh Annabathula  3.28.2: updated getproductsubscriberlists ,getsublistbyimsi,getsubscriberlistbyesn to retrive seatType and seatGroup .
-- May 24, 2014 Naresh Annabathula  3.28.3: updated getsubscriberlistbyesn  and GetSubscriberListByBanAndFleet to retrive seatType and seatGroup .
-- Sep 11, 2014 Tsz Chung Tong      3.29.1: Changed gethsparesources to fix defect 28288 where a subscriber can have mixed RSROUCE status. Added resource_seq to order so that our code will always pick the latest one.
-- Feb 14, 2017 Tsz chung Tong   2017.05.1: fixed getsublistbyimsi on duplicate subscriber issue. It was returning non-cancelled sub with the wrong price plan in the second part of query.  (PN0000017461496 / IN0000012813831)
-- Feb 16, 2017 Tsz chung Tong   2017.05.2: update getsublistbyimsi to include MAX(EXPIRATION_DATE) from SERVICE_AGREEMENT to pick the correct PP for a cancelled subscriber
-- Mar 8, 2017 Tsz chung Tong   2017.05.3: added hints to the union query in getsublistbyimsi per DBA (Anar) advice
--------------------------------------------------------------------------------------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;

   v_account_subscriber_tab         account_subscriber_t;
-- search constants
   search_all              CONSTANT VARCHAR2 (1)         := '*';
-- result constants
   numeric_true            CONSTANT NUMBER (1)           := 0;
   numeric_false           CONSTANT NUMBER (1)           := 1;
-- error messages
   err_invalid_input       CONSTANT VARCHAR2 (100)
                                   := 'Input parameters are invalid or NULL.';
   err_no_data_found       CONSTANT VARCHAR2 (100)       := 'No data found.';
   err_other               CONSTANT VARCHAR2 (100)   := 'Other PL/SQL error.';
-- absolute maximum for the number of accounts to be retrieved
   max_maximorum           CONSTANT NUMBER (4)           := 1000;
   unit_of_measure_month   CONSTANT VARCHAR2 (1)         := 'M';
   version_no          	   CONSTANT VARCHAR2(10)       := '2017.05.3';

   PROCEDURE initaccountsubscriberobject (
      pi_account_subscriber_ob   IN OUT   account_subscriber_o
   );

   FUNCTION getsubscriberlistbyesn (
      v_serial_no           IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubscriberlistbyban (
      i_ban                 IN       NUMBER,
      v_subscriber_id       IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubscriberlistbybanandfleet (
      i_ban             IN       NUMBER,
      i_urban_id        IN       NUMBER,
      i_fleet_id        IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubscriberlistbybanandtg (
      i_ban             IN       NUMBER,
      i_urban_id        IN       NUMBER,
      i_fleet_id        IN       NUMBER,
      i_talk_group      IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsublistbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubidbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      v_sub_id              OUT      VARCHAR2,
      i_external_id         OUT      NUMBER,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsecondaryesns (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_serial_numbers   OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getmarketprovinces (
      v_phone_numbers   IN       t_phone_num_array,
      v_provinces       OUT      VARCHAR2,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getidenresources (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_resources        OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getproductsubscriberlists (
      i_ban             IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getpagerequipmentinfo (
      v_serial_no       IN       VARCHAR2,
      c_equipmentinfo   OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getequipmentchangehistory (
      i_ban             IN       NUMBER,
      v_subscriber_id   IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getequipmentsubscribers (
      v_serial_numbers   IN       t_subscriber_array,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE retrieve_multiring (
      subscriber_id   IN       VARCHAR2,
      c_results       OUT      refcursor
   );

   FUNCTION gethotlineindicator (subscriber_id IN VARCHAR2)
      RETURN NUMBER;

   FUNCTION getbanidbyphonenumber (phone_number IN VARCHAR2)
      RETURN NUMBER;

   FUNCTION getpaidsecuritydeposit (
      banid          IN   NUMBER,
      subscriberid   IN   VARCHAR2,
      producttype    IN   VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getreservedsubscriberlistbyban (
      i_ban             IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getbansubphonenosbysubstatus (
      i_banid     IN       NUMBER,
      v_status    IN       VARCHAR2,
      i_maximum   IN       NUMBER,
      c_results   OUT      refcursor
   );

   FUNCTION getbanforpartuallyreservedsub (
      v_phone_no        IN       VARCHAR2,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getbansubidsbysubstatus (
      i_banid     IN       NUMBER,
      v_status    IN       VARCHAR2,
      i_maximum   IN       NUMBER,
      c_results   OUT      refcursor
   );

   FUNCTION getpromohistory (
      i_ban_id          IN       NUMBER,
      v_subscriber_id   IN       VARCHAR2,
      v_service_ids     IN       t_category_code_array,
      c_promos          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   PROCEDURE getpromosocs (
      v_service_ids      IN       t_category_code_array,
      v_soc_category_t   OUT      soc_category_t
   );

   FUNCTION getstartrestrdate (
      unit_of_measure_cd   IN   VARCHAR2,
      period_cnt           IN   NUMBER
   )
      RETURN DATE;

   PROCEDURE getlogicaldate (po_logical_date OUT DATE);

   FUNCTION getsublistbyphonenumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubidsbyphonenumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      t_subscriber_array,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsubidsbyimsi (
      i_imsi                IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      t_subscriber_array,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getsublistbyimsi (
      i_imsi                IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION gethsparesources (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_resources        OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION getBanAndPhoneNoByIMSI (
      i_imsi             IN       VARCHAR2,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER;      
	
   FUNCTION getlastassocsubscriptionid (i_imsi IN VARCHAR2)
	  RETURN VARCHAR2;

   FUNCTION getLwSubsByBan(
      i_ban                 IN       NUMBER,
      i_isIDEN              IN       NUMBER,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER;
      
   FUNCTION getVersion RETURN VARCHAR2;
   
   FUNCTION getHotlinedPhoneNoByBan(
   	  pi_ban	IN NUMBER
   	) RETURN VARCHAR2;
   	
   	PROCEDURE retrieveResourceChangeHistory (
   	  pi_ban			IN	NUMBER,
   	  pi_subscriberID  	IN	VARCHAR2,
   	  pi_resourceType	IN  VARCHAR2,
   	  pi_fromDate		IN  VARCHAR2,
   	  pi_toDate			IN	VARCHAR2,
   	  po_curChangeHistory	OUT	refcursor
   	);
   	
   	FUNCTION getsubbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      v_subscribers         OUT      refcursor,       
      v_error_message       OUT      VARCHAR2
	)	RETURN NUMBER;

   
END;
/

CREATE OR REPLACE PACKAGE BODY subscriber_pkg
AS
   PROCEDURE initaccountsubscriberobject (
      pi_account_subscriber_ob   IN OUT   account_subscriber_o
   )
   IS
   BEGIN
      pi_account_subscriber_ob := account_subscriber_o (NULL, NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                              (-20102,
                                  'Init Account Subscriber Object. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])'
                              );
   END initaccountsubscriberobject;

   /**
    * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getSubListBySubIDs
    */
   FUNCTION getsubscriberlistbyids (
      a_subscriber_ids      IN       t_subscriber_array,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
      i_count    NUMBER (4);
   BEGIN
      BEGIN
         IF a_subscriber_ids IS NOT NULL AND a_subscriber_ids.COUNT > 0
         THEN
            IF i_include_cancelled = numeric_true
            THEN
               OPEN c_subscribers
                FOR
                   SELECT   s.customer_id, s.subscriber_no, s.sub_status,
                            nd.first_name, nd.middle_initial,
                            nd.last_business_name, pd.unit_esn,
                            s.product_type, SUBSTR (s.dealer_code, 1, 10),
                            sa.soc, s.email_address,
                            NVL (s.init_activation_date, s.effective_date),
                            s.init_activation_date,
                            SUBSTR (s.dealer_code, 11),
                            s.sub_status_rsn_code, s.sub_status_last_act,
                            s.sub_alias,
                            DECODE (INSTR (user_seg, '@'),
                                    0, '',
                                    SUBSTR (user_seg,
                                            INSTR (user_seg, '@') + 1,
                                            1
                                           )
                                   ),
                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,
                            s.prv_ctn, s.prv_ctn_chg_date,
                            DECODE (s.sub_status, 'S', 'B', s.sub_status),
                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,
                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,
                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,
                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,
                            s.sub_status_date, s.calls_sort_order,
                            s.commit_reason_code, s.commit_orig_no_month,
                            s.commit_start_date, s.commit_end_date,
                            nd.name_suffix, nd.additional_title,
                            nd.name_title, s.hot_line_ind, nd.name_format,
                            s.migration_type, s.migration_date, s.tenure_date,
                            s.req_deposit_amt, s.port_type, s.port_date,
                            s.brand_id, NVL(s.external_id, 0) as subscription_id
                       FROM subscriber s,
                            address_name_link anl,
                            name_data nd,
                            physical_device pd,
                            service_agreement sa
                      WHERE s.subscriber_no IN (
                               SELECT *
                                 FROM TABLE
                                         (CAST
                                             (a_subscriber_ids AS t_subscriber_array
                                             )
                                         ))
                        AND s.sub_status = 'C'
                        AND anl.ban(+) = s.customer_id
                        AND anl.subscriber_no(+) = s.subscriber_no
                        AND anl.expiration_date IS NULL
                        AND nd.name_id(+) = anl.name_id
                        AND sa.ban = s.customer_id
                        AND sa.subscriber_no = s.subscriber_no
                        AND sa.product_type = s.product_type
                        AND sa.service_type = 'P'
                        AND sa.soc_seq_no =
                               (SELECT MAX (sa1.soc_seq_no)
                                  FROM service_agreement sa1
                                 WHERE sa1.ban = sa.ban
                                   AND sa1.subscriber_no = sa.subscriber_no
                                   AND sa1.product_type = sa.product_type
                                   AND sa1.service_type = 'P')
                        AND pd.customer_id = s.customer_id
                        AND pd.subscriber_no = s.subscriber_no
                        AND pd.product_type = s.product_type
                        AND pd.esn_seq_no =
                               (SELECT MAX (esn_seq_no)
                                  FROM physical_device pd1
                                 WHERE pd1.customer_id = pd.customer_id
                                   AND pd1.subscriber_no = pd.subscriber_no
                                   AND pd1.product_type = pd.product_type
                                   AND NVL (pd1.esn_level, 1) = 1)
                   UNION
                   SELECT   s.customer_id, s.subscriber_no, s.sub_status,
                            nd.first_name, nd.middle_initial,
                            nd.last_business_name, pd.unit_esn,
                            s.product_type, SUBSTR (s.dealer_code, 1, 10),
                            sa.soc, s.email_address,
                            NVL (s.init_activation_date, s.effective_date),
                            s.init_activation_date,
                            SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code,
                            s.sub_status_last_act, s.sub_alias,
                            DECODE (INSTR (user_seg, '@'),
                                    0, '',
                                    SUBSTR (user_seg,
                                            INSTR (user_seg, '@') + 1,
                                            1
                                           )
                                   ),
                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,
                            s.prv_ctn, s.prv_ctn_chg_date,
                            DECODE (s.sub_status, 'S', 'B', s.sub_status),
                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,
                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,
                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,
                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,
                            s.sub_status_date, s.calls_sort_order,
                            s.commit_reason_code, s.commit_orig_no_month,
                            s.commit_start_date, s.commit_end_date,
                            nd.name_suffix, nd.additional_title,
                            nd.name_title, s.hot_line_ind, nd.name_format,
                            s.migration_type, s.migration_date, s.tenure_date,
                            s.req_deposit_amt, s.port_type, s.port_date,
                            s.brand_id, NVL(s.external_id, 0) as subscription_id
                       FROM subscriber s,
                            address_name_link anl,
                            name_data nd,
                            physical_device pd,
                            service_agreement sa,
                            logical_date ld
                      WHERE s.subscriber_no IN (
                               SELECT *
                                 FROM TABLE
                                         (CAST
                                             (a_subscriber_ids AS t_subscriber_array
                                             )
                                         ))
                        AND s.sub_status != 'C'
                        AND anl.ban(+) = s.customer_id
                        AND anl.subscriber_no(+) = s.subscriber_no
                        AND anl.expiration_date IS NULL
                        AND nd.name_id(+) = anl.name_id
                        AND sa.ban = s.customer_id
                        AND sa.subscriber_no = s.subscriber_no
                        AND sa.product_type = s.product_type
                        AND sa.service_type = 'P'
                        AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                             OR sa.expiration_date IS NULL
                            )
                        AND ld.logical_date_type = 'O'
                        AND sa.effective_date =
                               (SELECT MIN (sa1.effective_date)
                                  FROM service_agreement sa1, logical_date ld
                                 WHERE sa1.ban = sa.ban
                                   AND sa1.subscriber_no = sa.subscriber_no
                                   AND sa1.product_type = sa.product_type
                                   AND sa1.service_type = 'P'
                                   AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                        OR sa1.expiration_date IS NULL
                                       )
                                   AND ld.logical_date_type = 'O')
                        AND pd.customer_id = s.customer_id
                        AND pd.subscriber_no = s.subscriber_no
                        AND pd.product_type = s.product_type
                        AND pd.esn_level = 1
                        AND pd.expiration_date IS NULL
                   ORDER BY 24, 12 DESC;
            ELSE
               OPEN c_subscribers
                FOR
                   SELECT   s.customer_id, s.subscriber_no, s.sub_status,
                            nd.first_name, nd.middle_initial,
                            nd.last_business_name, pd.unit_esn,
                            s.product_type, SUBSTR (s.dealer_code, 1, 10),
                            sa.soc, s.email_address,
                            NVL (s.init_activation_date, s.effective_date),
                            s.init_activation_date,
                            SUBSTR (s.dealer_code, 11),
                            s.sub_status_rsn_code, s.sub_status_last_act,
                            s.sub_alias,
                            DECODE (INSTR (user_seg, '@'),
                                    0, '',
                                    SUBSTR (user_seg,
                                            INSTR (user_seg, '@') + 1,
                                            1
                                           )
                                   ),
                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,
                            s.prv_ctn, s.prv_ctn_chg_date,
                            DECODE (s.sub_status, 'S', 'B', s.sub_status),
                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,
                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,
                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,
                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,
                            s.sub_status_date, s.calls_sort_order,
                            s.commit_reason_code, s.commit_orig_no_month,
                            s.commit_start_date, s.commit_end_date,
                            nd.name_suffix, nd.additional_title,
                            nd.name_title, s.hot_line_ind, nd.name_format,
                            s.migration_type, s.migration_date, s.tenure_date,
                            s.req_deposit_amt, s.port_type, s.port_date,
                            s.brand_id, NVL(s.external_id, 0) as subscription_id
                       FROM subscriber s,
                            address_name_link anl,
                            name_data nd,
                            physical_device pd,
                            service_agreement sa,
                            logical_date ld
                      WHERE s.subscriber_no IN (
                               SELECT *
                                 FROM TABLE
                                         (CAST
                                             (a_subscriber_ids AS t_subscriber_array
                                             )
                                         ))
                        AND s.sub_status != 'C'
                        AND anl.ban(+) = s.customer_id
                        AND anl.subscriber_no(+) = s.subscriber_no
                        AND anl.expiration_date IS NULL
                        AND nd.name_id(+) = anl.name_id
                        AND sa.ban = s.customer_id
                        AND sa.subscriber_no = s.subscriber_no
                        AND sa.product_type = s.product_type
                        AND sa.service_type = 'P'
                        AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                             OR sa.expiration_date IS NULL
                            )
                        AND ld.logical_date_type = 'O'
                        AND sa.effective_date =
                               (SELECT MIN (sa1.effective_date)
                                  FROM service_agreement sa1, logical_date ld
                                 WHERE sa1.ban = sa.ban
                                   AND sa1.subscriber_no = sa.subscriber_no
                                   AND sa1.product_type = sa.product_type
                                   AND sa1.service_type = 'P'
                                   AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                        OR sa1.expiration_date IS NULL
                                       )
                                   AND ld.logical_date_type = 'O')
                        AND pd.customer_id = s.customer_id
                        AND pd.subscriber_no = s.subscriber_no
                        AND pd.product_type = s.product_type
                        AND pd.esn_level = 1
                        AND pd.expiration_date IS NULL
                   ORDER BY 24, 12 DESC;
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getsubscriberlistbyids;

   FUNCTION getsubscriberlistbyesn (
      v_serial_no           IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF     v_serial_no IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_serial_no))) > 0
         THEN
            IF i_include_cancelled = numeric_false
            THEN
               OPEN c_subscribers
                FOR
                   SELECT DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd_1.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   DECODE (pd.unit_esn, v_serial_no, 1, 2),
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group
                              FROM physical_device pd,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   logical_date ld,
                                   physical_device pd_1
                             WHERE pd.unit_esn = v_serial_no
                               AND pd.expiration_date IS NULL
                               AND s.subscriber_no = pd.subscriber_no
                               AND s.product_type = pd.product_type
                               AND s.customer_id = pd.customer_id
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                    OR sa.expiration_date IS NULL
                                   )
                               AND ld.logical_date_type = 'O'
                               AND sa.effective_date =
                                      (SELECT MIN (sa1.effective_date)
                                         FROM service_agreement sa1
                                        WHERE sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                          AND sa1.product_type =
                                                               sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL
                                              )
                                          AND ld.logical_date_type = 'O')
                               AND pd_1.customer_id = s.customer_id
                               AND pd_1.subscriber_no = s.subscriber_no
                               AND pd_1.product_type = s.product_type
                               AND pd_1.esn_level = 1
                               AND pd_1.expiration_date IS NULL;
            ELSE
               OPEN c_subscribers
                FOR
                   SELECT DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd_1.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   DECODE (pd.unit_esn, v_serial_no, 1, 2),
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group
                              FROM physical_device pd,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   logical_date ld,
                                   physical_device pd_1
                             WHERE pd.unit_esn = v_serial_no
                               AND pd.expiration_date IS NULL
                               AND s.subscriber_no = pd.subscriber_no
                               AND s.product_type = pd.product_type
                               AND s.customer_id = pd.customer_id
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                    OR sa.expiration_date IS NULL
                                   )
                               AND ld.logical_date_type = 'O'
                               AND sa.effective_date =
                                      (SELECT MIN (sa1.effective_date)
                                         FROM service_agreement sa1
                                        WHERE sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                          AND sa1.product_type =
                                                               sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL
                                              )
                                          AND ld.logical_date_type = 'O')
                               AND pd_1.customer_id = s.customer_id
                               AND pd_1.subscriber_no = s.subscriber_no
                               AND pd_1.product_type = s.product_type
                               AND pd_1.esn_level = 1
                               AND pd_1.expiration_date IS NULL
                   UNION
                   SELECT DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd_2.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   DECODE (pd.unit_esn, v_serial_no, 1, 2),
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group
                              FROM physical_device pd,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   physical_device pd_2
                             WHERE pd.unit_esn = v_serial_no
                               AND pd.expiration_date IS NOT NULL
                               AND s.subscriber_no = pd.subscriber_no
                               AND s.product_type = pd.product_type
                               AND s.customer_id = pd.customer_id
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND sa.soc_seq_no =
                                      (SELECT MAX (sa1.soc_seq_no)
                                         FROM service_agreement sa1
                                        WHERE sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                          AND sa1.product_type =
                                                               sa.product_type
                                          AND sa1.service_type = 'P')
                               AND pd_2.customer_id = s.customer_id
                               AND pd_2.subscriber_no = s.subscriber_no
                               AND pd_2.product_type = s.product_type
                               AND (   (    s.sub_status = 'C'
                                        AND pd_2.esn_seq_no =
                                               (SELECT MAX (esn_seq_no)
                                                  FROM physical_device pd1
                                                 WHERE pd1.customer_id =
                                                              pd_2.customer_id
                                                   AND pd1.subscriber_no =
                                                            pd_2.subscriber_no
                                                   AND pd1.product_type =
                                                             pd_2.product_type
                                                   AND NVL (pd1.esn_level, 1) =
                                                                             1)
                                       )
                                    OR (    s.sub_status != 'C'
                                        AND pd_2.esn_level = 1
                                        AND pd_2.expiration_date IS NULL
                                       )
                                   )
                          ORDER BY 25, 24, 12 DESC;
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getsubscriberlistbyesn;

   /**
    * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getSubListByBAN
    */
   FUNCTION getsubscriberlistbyban (
      i_ban                 IN       NUMBER,
      v_subscriber_id       IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result        NUMBER (1);
      i_ignore_ban    NUMBER (1);
      i_ignore_sub    NUMBER (1);
      v_cursor_text   VARCHAR2 (32767);
      v_select_list   VARCHAR2 (5000);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL AND i_ban > 0
         THEN
            i_ignore_ban := numeric_false;
         ELSE
            i_ignore_ban := numeric_true;
         END IF;

         IF     v_subscriber_id IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_subscriber_id))) > 0
         THEN
            i_ignore_sub := numeric_false;
         ELSE
            i_ignore_sub := numeric_true;
         END IF;

         IF i_ignore_ban = numeric_false OR i_ignore_sub = numeric_false
         THEN
            v_select_list :=
                  'SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, '
               || '       nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, '
               || '       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, '
               || '       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, '
               || '       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, '
               || '       s.sub_alias, DECODE(INSTR(user_seg, ''@''), 0, '''', SUBSTR(user_seg, INSTR(user_seg, ''@'') + 1, 

1)), '
               || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
               || '       DECODE(s.sub_status, ''S'', ''B'', s.sub_status), '
               || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
               || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
               || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
               || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
               || '       s.sub_status_date, s.calls_sort_order, '
               || '       s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, s.commit_end_date, '
               || '       nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, '
               || '		  s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, s.port_type, s.port_date, '
               || '		  s.brand_id, NVL(s.external_id, 0) as subscription_id ';

            IF i_include_cancelled = numeric_true
            THEN
               v_cursor_text :=
                     v_select_list
                  || ' FROM subscriber s, '
                  || ' address_name_link anl, '
                  || ' name_data nd, '
                  || ' physical_device pd, '
                  || ' service_agreement sa ';

               IF (i_ignore_ban = numeric_false)
               THEN
                  v_cursor_text :=
                          v_cursor_text || ' WHERE s.customer_id = ' || i_ban;

                  IF (i_ignore_sub = numeric_false)
                  THEN
                     v_cursor_text :=
                           v_cursor_text
                        || ' AND s.subscriber_no = '''
                        || v_subscriber_id
                        || ''' ';
                  END IF;
               ELSE
                  v_cursor_text :=
                        v_cursor_text
                     || ' WHERE s.subscriber_no = '''
                     || v_subscriber_id
                     || ''' ';
               END IF;

               v_cursor_text :=
                     v_cursor_text
                  || ' AND s.sub_status = ''C'' '
                  || ' AND anl.ban(+) = s.customer_id '
                  || ' AND anl.subscriber_no(+) = s.subscriber_no '
                  || ' AND anl.expiration_date IS NULL '
                  || ' AND nd.name_id(+) = anl.name_id '
                  || ' AND sa.ban = s.customer_id '
                  || ' AND sa.subscriber_no = s.subscriber_no '
                  || ' AND sa.product_type = s.product_type '
                  || ' AND sa.service_type = ''P'' '
                  || ' AND sa.soc_seq_no = (SELECT MAX(sa1.soc_seq_no) '
                  || '                        FROM service_agreement sa1 '
                  || '                       WHERE sa1.ban = sa.ban '
                  || '                         AND sa1.subscriber_no = sa.subscriber_no '
                  || '                         AND sa1.product_type = sa.product_type '
                  || '                         AND sa1.service_type = ''P'') '
                  || ' AND pd.customer_id = s.customer_id '
                  || ' AND pd.subscriber_no = s.subscriber_no '
                  || ' AND pd.product_type = s.product_type '
                  || ' AND pd.esn_seq_no = (SELECT MAX(esn_seq_no) '
                  || '                        FROM physical_device pd1 '
                  || '                       WHERE pd1.customer_id = pd.customer_id '
                  || '                         AND pd1.subscriber_no = pd.subscriber_no '
                  || '                         AND pd1.product_type = pd.product_type '
                  || '                         AND NVL(pd1.esn_level, 1) = 1) '
                  || ' UNION ';
            END IF;

            v_cursor_text :=
                  v_cursor_text
               || v_select_list
               || '  FROM subscriber s, '
               || '       address_name_link anl, '
               || '       name_data nd, '
               || '       physical_device pd, '
               || '       service_agreement sa, '
               || '       logical_date ld ';

            IF (i_ignore_ban = numeric_false)
            THEN
               v_cursor_text :=
                          v_cursor_text || ' WHERE s.customer_id = ' || i_ban;

               IF (i_ignore_sub = numeric_false)
               THEN
                  v_cursor_text :=
                        v_cursor_text
                     || ' AND s.subscriber_no = '''
                     || v_subscriber_id
                     || ''' ';
               END IF;
            ELSE
               v_cursor_text :=
                     v_cursor_text
                  || ' WHERE s.subscriber_no = '''
                  || v_subscriber_id
                  || ''' ';
            END IF;

            v_cursor_text :=
                  v_cursor_text
               || ' AND s.sub_status != ''C'' '
               || ' AND anl.ban(+) = s.customer_id '
               || ' AND anl.subscriber_no(+) = s.subscriber_no '
               || ' AND anl.expiration_date IS NULL '
               || ' AND nd.name_id(+) = anl.name_id '
               || ' AND sa.ban = s.customer_id '
               || ' AND sa.subscriber_no = s.subscriber_no '
               || ' AND sa.product_type = s.product_type '
               || ' AND sa.service_type = ''P'' '
               || ' AND (TRUNC(sa.expiration_date) > TRUNC(ld.logical_date) OR sa.expiration_date IS NULL) '
               || ' AND ld.logical_date_type = ''O'' '
               || ' AND sa.effective_date = (SELECT MIN(sa1.effective_date) '
               || '                            FROM service_agreement sa1, '
               || '                                 logical_date ld '
               || '                           WHERE sa1.ban = sa.ban '
               || '                             AND sa1.subscriber_no = sa.subscriber_no '
               || '                             AND sa1.product_type = sa.product_type '
               || '                             AND sa1.service_type = ''P'' '
               || '                             AND (TRUNC(sa1.expiration_date) > TRUNC(ld.logical_date) OR 

sa1.expiration_date IS NULL) '
               || '                             AND ld.logical_date_type = ''O'') '
               || ' AND pd.customer_id = s.customer_id '
               || ' AND pd.subscriber_no = s.subscriber_no '
               || ' AND pd.product_type = s.product_type '
               || ' AND pd.esn_level = 1 '
               || ' AND pd.expiration_date IS NULL '
               || ' ORDER BY 24, 12 DESC';

            OPEN c_subscribers
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
   END getsubscriberlistbyban;

   FUNCTION getsubscriberlistbybanandfleet (
      i_ban             IN       NUMBER,
      i_urban_id        IN       NUMBER,
      i_fleet_id        IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result        NUMBER (1);
      v_cursor_text   VARCHAR2 (32767);
      v_select_list   VARCHAR2 (5000);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND i_urban_id IS NOT NULL
            AND i_fleet_id IS NOT NULL
         THEN
            OPEN c_subscribers
             FOR
                SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status,
                                nd.first_name, nd.middle_initial,
                                nd.last_business_name, pd.unit_esn,
                                s.product_type,
                                SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                s.email_address,
                                NVL (s.init_activation_date,
                                     s.effective_date),
                                s.init_activation_date,
                                SUBSTR (s.dealer_code, 11),
                                s.sub_status_rsn_code, s.sub_status_last_act,
                                s.sub_alias,
                                DECODE (INSTR (user_seg, '@'),
                                        0, '',
                                        SUBSTR (user_seg,
                                                INSTR (user_seg, '@') + 1,
                                                1
                                               )
                                       ),
                                sub_lang_pref, s.next_ctn,
                                s.next_ctn_chg_date, s.prv_ctn,
                                s.prv_ctn_chg_date,
                                DECODE (s.sub_status, 'S', 'B', s.sub_status),
                                s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,
                                s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,
                                s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,
                                s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,
                                s.sub_status_date, s.calls_sort_order,
                                s.commit_reason_code, s.commit_orig_no_month,
                                s.commit_start_date, s.commit_end_date,
                                nd.name_suffix, nd.additional_title,
                                nd.name_title, s.hot_line_ind, nd.name_format,
                                s.migration_type, s.migration_date,
                                s.tenure_date, s.req_deposit_amt, s.port_type,
                                s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group
                           FROM subscriber s,
                                address_name_link anl,
                                name_data nd,
                                physical_device pd,
                                service_agreement sa,
                                subscriber_rsource sr
                          WHERE sr.ban = i_ban
                            AND sr.urban_id = i_urban_id
                            AND sr.fleet_id = i_fleet_id
                            AND sr.resource_type = 'H'
                            AND sr.resource_status = 'A'
                            AND sr.resource_seq =
                                   (SELECT MAX (sr2.resource_seq)
                                      FROM subscriber_rsource sr2
                                     WHERE sr2.subscriber_no =
                                                              sr.subscriber_no
                                       AND sr2.resource_type =
                                                              sr.resource_type
                                       AND sr2.ban = sr.ban)
                            AND sr.ban = s.customer_id
                            AND sr.subscriber_no = s.subscriber_no
                            AND s.sub_status != 'C'
                            AND anl.ban(+) = s.customer_id
                            AND anl.subscriber_no(+) = s.subscriber_no
                            AND anl.expiration_date IS NULL
                            AND nd.name_id(+) = anl.name_id
                            AND sa.ban = s.customer_id
                            AND sa.subscriber_no = s.subscriber_no
                            AND sa.product_type = s.product_type
                            AND sa.service_type = 'P'
                            AND sa.soc_seq_no =
                                   (SELECT MAX (sa1.soc_seq_no)
                                      FROM service_agreement sa1
                                     WHERE sa1.ban = sa.ban
                                       AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                       AND sa1.product_type = sa.product_type
                                       AND sa1.service_type = 'P')
                            AND pd.customer_id = s.customer_id
                            AND pd.subscriber_no = s.subscriber_no
                            AND pd.product_type = s.product_type
                            AND pd.esn_level = 1
                            AND pd.expiration_date IS NULL
                       ORDER BY 24, 12 DESC;

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
   END getsubscriberlistbybanandfleet;

   FUNCTION getsubscriberlistbybanandtg (
      i_ban             IN       NUMBER,
      i_urban_id        IN       NUMBER,
      i_fleet_id        IN       NUMBER,
      i_talk_group      IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result        NUMBER (1);
      v_cursor_text   VARCHAR2 (32767);
      v_select_list   VARCHAR2 (5000);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND i_urban_id IS NOT NULL
            AND i_fleet_id IS NOT NULL
         THEN
            OPEN c_subscribers
             FOR
                SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status,
                                nd.first_name, nd.middle_initial,
                                nd.last_business_name, pd.unit_esn,
                                s.product_type,
                                SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                s.email_address,
                                NVL (s.init_activation_date,
                                     s.effective_date),
                                s.init_activation_date,
                                SUBSTR (s.dealer_code, 11),
                                s.sub_status_rsn_code, s.sub_status_last_act,
                                s.sub_alias,
                                DECODE (INSTR (user_seg, '@'),
                                        0, '',
                                        SUBSTR (user_seg,
                                                INSTR (user_seg, '@') + 1,
                                                1
                                               )
                                       ),
                                sub_lang_pref, s.next_ctn,
                                s.next_ctn_chg_date, s.prv_ctn,
                                s.prv_ctn_chg_date,
                                DECODE (s.sub_status, 'S', 'B', s.sub_status),
                                s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,
                                s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,
                                s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,
                                s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,
                                s.sub_status_date, s.calls_sort_order,
                                s.commit_reason_code, s.commit_orig_no_month,
                                s.commit_start_date, s.commit_end_date,
                                nd.name_suffix, nd.additional_title,
                                nd.name_title, s.hot_line_ind, nd.name_format,
                                s.migration_type, s.migration_date,
                                s.tenure_date, s.req_deposit_amt, s.port_type,
                                s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group
                           FROM subscriber s,
                                address_name_link anl,
                                name_data nd,
                                physical_device pd,
                                service_agreement sa,
                                subscriber_tg_mtrx stm
                          WHERE stm.ban = i_ban
                            AND stm.urban_id = i_urban_id
                            AND stm.fleet_id = i_fleet_id
                            AND stm.tg_id = i_talk_group
                            AND stm.ban = s.customer_id
                            AND stm.subscriber_no = s.subscriber_no
                            AND s.sub_status != 'C'
                            AND anl.ban(+) = s.customer_id
                            AND anl.subscriber_no(+) = s.subscriber_no
                            AND anl.expiration_date IS NULL
                            AND nd.name_id(+) = anl.name_id
                            AND sa.ban = s.customer_id
                            AND sa.subscriber_no = s.subscriber_no
                            AND sa.product_type = s.product_type
                            AND sa.service_type = 'P'
                            AND sa.soc_seq_no =
                                   (SELECT MAX (sa1.soc_seq_no)
                                      FROM service_agreement sa1
                                     WHERE sa1.ban = sa.ban
                                       AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                       AND sa1.product_type = sa.product_type
                                       AND sa1.service_type = 'P')
                            AND pd.customer_id = s.customer_id
                            AND pd.subscriber_no = s.subscriber_no
                            AND pd.product_type = s.product_type
                            AND pd.esn_level = 1
                            AND pd.expiration_date IS NULL
                       ORDER BY 24, 12 DESC;

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
   END getsubscriberlistbybanandtg;

   FUNCTION getsecondaryesns (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_serial_numbers   OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text             VARCHAR2 (32767);
      i_index                   BINARY_INTEGER;
      i_result                  NUMBER (1);
      v_account_subscriber_ob   account_subscriber_o;
   BEGIN
      BEGIN
         IF     v_subscriber_ids.COUNT > 0
            AND i_bans.COUNT = v_subscriber_ids.COUNT
         THEN
            v_account_subscriber_tab := account_subscriber_t ();
            i_index := 1;

            WHILE i_index <= v_subscriber_ids.COUNT
            LOOP
               initaccountsubscriberobject (v_account_subscriber_ob);
               v_account_subscriber_ob.account_id := i_bans (i_index);
               v_account_subscriber_ob.subscriber_id :=
                                                   v_subscriber_ids (i_index);
               v_account_subscriber_tab.EXTEND;
               v_account_subscriber_tab (v_account_subscriber_tab.LAST) :=
                                                      v_account_subscriber_ob;
               i_index := i_index + 1;
            END LOOP;

            OPEN c_serial_numbers
             FOR
                SELECT   pd.subscriber_no, pd.customer_id, pd.unit_esn
                    FROM physical_device pd, subscriber s
                   WHERE (pd.customer_id, pd.subscriber_no) IN (
                            SELECT *
                              FROM TABLE
                                      (CAST
                                          (v_account_subscriber_tab AS account_subscriber_t
                                          )
                                      ))
                     AND NVL (pd.esn_level, 1) <> 1
                     AND s.customer_id = pd.customer_id
                     AND s.subscriber_no = pd.subscriber_no
                     AND (   (    s.sub_status = 'C'
                              AND pd.expiration_date IS NOT NULL
                             )
                          OR (    s.sub_status != 'C'
                              AND pd.expiration_date IS NULL
                             )
                         )
                ORDER BY subscriber_no;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;

         v_account_subscriber_tab.DELETE;
         v_account_subscriber_ob := NULL;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_serial_numbers%ISOPEN)
            THEN
               CLOSE c_serial_numbers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_serial_numbers%ISOPEN)
            THEN
               CLOSE c_serial_numbers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getsecondaryesns;

   FUNCTION getmarketprovinces (
      v_phone_numbers   IN       t_phone_num_array,
      v_provinces       OUT      VARCHAR2,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_phone_number   VARCHAR2 (10);
      v_province       VARCHAR2 (2);
      d_eff_date       DATE;
      i_index          BINARY_INTEGER;
      i_result         NUMBER (1);

      CURSOR c_market_provinces
      IS
         SELECT   m.province, nnl.effective_date
             FROM market_npa_nxx_lr nnl, market m
            WHERE nnl.npa = SUBSTR (v_phone_number, 1, 3)
              AND nnl.nxx = SUBSTR (v_phone_number, 4, 3)
              AND nnl.begin_line_range <= SUBSTR (v_phone_number, 7, 4)
              AND nnl.end_line_range >= SUBSTR (v_phone_number, 7, 4)
              AND TRUNC (effective_date) < TRUNC (SYSDATE + 1)
              AND m.market_code = nnl.sub_market_code
         ORDER BY nnl.effective_date DESC;
   BEGIN
      BEGIN
         IF v_phone_numbers.COUNT > 0
         THEN
            v_provinces := '';
            i_index := 1;

            WHILE i_index <= v_phone_numbers.COUNT
            LOOP
               v_phone_number := RTRIM (LTRIM (v_phone_numbers (i_index)));

               IF     v_phone_number IS NOT NULL
                  AND LENGTH (RTRIM (LTRIM (v_phone_number))) = 10
               THEN
                  BEGIN
                     OPEN c_market_provinces;

                     FETCH c_market_provinces
                      INTO v_province, d_eff_date;

                     IF     NOT c_market_provinces%NOTFOUND
                        AND v_province IS NOT NULL
                        AND LENGTH (RTRIM (LTRIM (v_province))) = 2
                     THEN
                        v_provinces :=
                                  v_provinces || v_phone_number || v_province;
                     END IF;

                     CLOSE c_market_provinces;
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        CLOSE c_market_provinces;
                  END;
               END IF;

               i_index := i_index + 1;
            END LOOP;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getmarketprovinces;

   FUNCTION getidenresources (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_resources        OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text             VARCHAR2 (32767);
      i_index                   BINARY_INTEGER;
      i_result                  NUMBER (1);
      v_account_subscriber_ob   account_subscriber_o;
   BEGIN
      BEGIN
         IF     v_subscriber_ids.COUNT > 0
            AND i_bans.COUNT = v_subscriber_ids.COUNT
         THEN
            v_account_subscriber_tab := account_subscriber_t ();
            i_index := 1;

            WHILE i_index <= v_subscriber_ids.COUNT AND i_index <= max_maximorum
            LOOP
               initaccountsubscriberobject (v_account_subscriber_ob);
               v_account_subscriber_ob.account_id := i_bans (i_index);
               v_account_subscriber_ob.subscriber_id :=
                                                   v_subscriber_ids (i_index);
               v_account_subscriber_tab.EXTEND;
               v_account_subscriber_tab (v_account_subscriber_tab.LAST) :=
                                                      v_account_subscriber_ob;
               i_index := i_index + 1;
            END LOOP;

            OPEN c_resources
             FOR
                SELECT   subscriber_no, ban, urban_id, fleet_id, member_id,
                         sub_market, resource_number, resource_type,
                         resource_status, imsi_number, resource_seq
                    FROM subscriber_rsource sr
                   WHERE (sr.ban, sr.subscriber_no) IN (
                            SELECT *
                              FROM TABLE
                                      (CAST
                                          (v_account_subscriber_tab AS account_subscriber_t
                                          )
                                      ))
                     AND (   
                            sr.RECORD_EXP_DATE = TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
                            AND  sr.RESOURCE_TYPE IN ( 'N' ,'H', 'X' , 'Y' , 'Z' )
                         )
                ORDER BY subscriber_no, ban, resource_seq;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_resources%ISOPEN)
            THEN
               CLOSE c_resources;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_resources%ISOPEN)
            THEN
               CLOSE c_resources;
            END IF;

            --v_error_message := SQLERRM;
            v_error_message := v_cursor_text;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getidenresources;

   FUNCTION getproductsubscriberlists (
      i_ban             IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF i_ban IS NOT NULL
         THEN
            OPEN c_subscribers
             FOR
                SELECT   product_type, sub_status, subscriber_no, NVL(external_id, 0) external_id, brand_id,seat_type, seat_group 
                    FROM subscriber
                   WHERE customer_id = i_ban
                         AND sub_status IN ('A', 'S', 'C')
                UNION
                SELECT   s.product_type, s.sub_status, s.subscriber_no, NVL(s.external_id, 0) external_id, s.brand_id,s.seat_type, s.seat_group
                    FROM subscriber s, physical_device p
                   WHERE s.customer_id = i_ban
                     AND s.sub_status = 'R'
                     AND p.customer_id = s.customer_id
                     AND p.subscriber_no = s.subscriber_no
                     AND p.product_type = s.product_type
                     AND p.esn_level = 1
                     AND p.expiration_date IS NULL
                ORDER BY product_type, sub_status;

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
   END getproductsubscriberlists;

   FUNCTION getpagerequipmentinfo (
      v_serial_no       IN       VARCHAR2,
      c_equipmentinfo   OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF     v_serial_no IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_serial_no))) > 0
         THEN
            OPEN c_equipmentinfo
             FOR
                SELECT coverage_region, encoding_format, curr_possession,
                       equipment_type
                  FROM serial_item_inv
                 WHERE serial_number = v_serial_no;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF c_equipmentinfo%ISOPEN
            THEN
               CLOSE c_equipmentinfo;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF c_equipmentinfo%ISOPEN
            THEN
               CLOSE c_equipmentinfo;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getpagerequipmentinfo;

   /**
    * DO NOT USE. Migrated to SUB_ATTRIB_RETRIEVAL_PKG.getSubIDListByBanAndPhone
    */
   FUNCTION getsubidsbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      t_subscriber_array,
      i_ext_id              OUT      NUMBER,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      c_cursor        refcursor;
      i_result        NUMBER (1);
      i_count         NUMBER (4);
      c_sub_status    CHAR (1);
      i_customer_id   NUMBER (9);
      i_external_id   NUMBER (9);
      decoded_status  CHAR (1);
   BEGIN
      BEGIN
         IF v_phone_no IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_no))) > 0
         THEN
	     OPEN c_cursor FOR
	     	SELECT s1.subscriber_no, s1.external_id , s1.sub_status, s1.customer_id,
	     		   DECODE (s1.sub_status, 'S', 'B', s1.sub_status) decodedStatus 
	     	FROM subscriber s1 
	     	WHERE s1.subscriber_no = v_phone_no
	     	      AND (s1.customer_id = i_ban OR i_ban is NULL OR i_ban = 0)
	     	AND s1.product_type <> 'I'	     	      
	     	AND (s1.sub_status != 'C' OR i_include_cancelled = numeric_true)
   	      UNION
	     	SELECT s2.subscriber_no, s2.external_id, sr.resource_status, s2.customer_id,
	     	       DECODE (s2.sub_status, 'S', 'B', s2.sub_status) decodedStatus
	     	       FROM subscriber s2,
	     	       		subscriber_rsource sr
	     	       WHERE sr.resource_number = v_phone_no
	     	       		AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
	     	       		AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
	     	       		AND sr.resource_type = 'N'
               			AND sr.resource_seq = (SELECT MAX(sr2.resource_seq)
               				                   FROM subscriber_rsource sr2
               		                           WHERE sr2.subscriber_no = sr.subscriber_no
               		                             AND sr2.ban = sr.ban 
               		                             AND sr2.resource_type = 'N')
            AND s2.subscriber_no = sr.subscriber_no
            AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
           ORDER BY decodedStatus, 2 DESC;
               		
            a_sub_ids := t_subscriber_array (NULL);
            i_count := 1;

            LOOP
               a_sub_ids.EXTEND;

               FETCH c_cursor
                INTO a_sub_ids (i_count), i_external_id, c_sub_status, i_customer_id, decoded_status;
                
               IF (i_count = 1) THEN
                 i_ext_id := i_external_id;
               END IF;

               EXIT WHEN c_cursor%NOTFOUND;
               i_count := i_count + 1;
            END LOOP;

            IF c_cursor%ISOPEN
            THEN
               CLOSE c_cursor;
            END IF;

            IF i_count > 1
            THEN
               WHILE a_sub_ids (a_sub_ids.COUNT) IS NULL
               LOOP
                  a_sub_ids.TRIM;
               END LOOP;
            END IF;

            IF a_sub_ids.COUNT > 0
            THEN
               i_result := numeric_true;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
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
   END getsubidsbybanandphonenumber;

   /**
    * DO NOT USE. Migrated to SUB_ATTRIB_RETRIEVAL_PKG.getIdentifierByBanAndPhone
    */
   FUNCTION getsubidbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      v_sub_id              OUT      VARCHAR2,
      i_external_id         OUT      NUMBER,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
      i_ext_id    NUMBER(9);
   BEGIN
      IF v_phone_no IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_no))) > 0
      THEN
         i_result :=
            getsubidsbybanandphonenumber (i_ban,
                                          v_phone_no,
                                          i_include_cancelled,
                                          a_sub_ids,
                                          i_ext_id,
                                          v_error_message
                                         );

         IF i_result = numeric_true
         THEN
            IF a_sub_ids IS NOT NULL AND a_sub_ids.COUNT > 0
            THEN
               v_sub_id := a_sub_ids (1);
               i_external_id := i_ext_id;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
         END IF;
      ELSE
         v_error_message := err_invalid_input;
         i_result := numeric_false;
      END IF;

      RETURN i_result;
   END getsubidbybanandphonenumber;

   /**
    * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getSubListByBanAndPhoneNumber
    */
   FUNCTION getsublistbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
      i_ext_id    NUMBER(9);
   BEGIN
      i_result :=
         getsubidsbybanandphonenumber (i_ban,
                                       v_phone_number,
                                       i_include_cancelled,
                                       a_sub_ids,
                                       i_ext_id,
                                       v_error_message
                                      );

      IF i_result = numeric_true
      THEN
         RETURN getsubscriberlistbyids (a_sub_ids,
                                        i_include_cancelled,
                                        c_subscribers,
                                        v_error_message
                                       );
      ELSE
         RETURN i_result;
      END IF;
   END getsublistbybanandphonenumber;

   FUNCTION getequipmentchangehistory (
      i_ban             IN       NUMBER,
      v_subscriber_id   IN       VARCHAR2,
      v_date_from       IN       VARCHAR2,
      v_date_to         IN       VARCHAR2,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF     i_ban IS NOT NULL
            AND i_ban > 0
            AND v_subscriber_id IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (v_subscriber_id))) > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT   pd.product_type, pd.unit_esn, pd.effective_date,
                         pd.expiration_date, pd.esn_level,
                         si.encoding_format, pd.esn_seq_no
                    FROM physical_device pd, serial_item_inv si
                   WHERE pd.customer_id = i_ban
                     AND pd.subscriber_no = v_subscriber_id
                     AND (       TRUNC (pd.effective_date) >=
                                           TO_DATE (v_date_from, 'mm/dd/yyyy')
                             AND TRUNC (pd.effective_date) <=
                                             TO_DATE (v_date_to, 'mm/dd/yyyy')
                          OR     TRUNC (pd.expiration_date) >=
                                           TO_DATE (v_date_from, 'mm/dd/yyyy')
                             AND TRUNC (pd.expiration_date) <=
                                             TO_DATE (v_date_to, 'mm/dd/yyyy')
                         )
                     AND si.serial_number(+) = pd.unit_esn
                     AND si.equipment_type <> 'U'
                ORDER BY pd.esn_seq_no DESC;

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

      -- v_error_message := v_cursor_text; ???
      RETURN i_result;
   END getequipmentchangehistory;

   FUNCTION getequipmentsubscribers (
      v_serial_numbers   IN       t_subscriber_array,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF v_serial_numbers.COUNT > 0
         THEN
            OPEN c_cursor
             FOR
                SELECT subscriber_no, ban, 0
                  FROM physical_device pd
                 WHERE pd.unit_esn IN (
                          SELECT *
                            FROM TABLE
                                    (CAST
                                        (v_serial_numbers AS t_subscriber_array
                                        )
                                    ))
                   AND pd.expiration_date IS NULL
                   AND (pd.product_type = 'C' OR pd.product_type = 'P')
                UNION
                SELECT *
                  FROM (SELECT sr.resource_number, pd.ban,
                               RANK () OVER (PARTITION BY sr.subscriber_no, sr.resource_type, sr.resource_status ORDER BY sr.resource_seq DESC)
                                                                       virt_1
                          FROM physical_device pd, subscriber_rsource sr
                         WHERE pd.unit_esn IN (
                                  SELECT *
                                    FROM TABLE
                                            (CAST
                                                (v_serial_numbers AS t_subscriber_array
                                                )
                                            ))
                           AND pd.expiration_date IS NULL
                           AND pd.product_type = 'I'
                           AND sr.subscriber_no = pd.subscriber_no
                           AND sr.resource_status != 'C'
                           AND sr.resource_type = 'N')
                 WHERE virt_1 = 1;

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
   END getequipmentsubscribers;

   /* retrieve_multiring_phone_numbers */
   PROCEDURE retrieve_multiring (
      subscriber_id   IN       VARCHAR2,
      c_results       OUT      refcursor
   )
   IS
   BEGIN
      OPEN c_results
       FOR
          SELECT   member_tn
              FROM sub_group_members
             WHERE primary_sub = subscriber_id
          ORDER BY member_order ASC;
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
                     (-20261,
                         'ERROR: subscriber_pkg.retrieve_multiring(). SQL ['
                      || SQLCODE
                      || '] Error ['
                      || SQLERRM
                      || ']',
                      TRUE
                     );
   END retrieve_multiring;

   FUNCTION gethotlineindicator (subscriber_id IN VARCHAR2)
      RETURN NUMBER
   IS
      ind      CHAR (1);
      RESULT   NUMBER (1) := 0;
   BEGIN
      --begin
      SELECT hot_line_ind
        INTO ind
        FROM subscriber
       WHERE subscriber_no = subscriber_id;

      IF (ind = 'Y')
      THEN
         RESULT := 1;
      END IF;

      --end;
      RETURN RESULT;
   END gethotlineindicator;

   FUNCTION getbanidbyphonenumber (phone_number IN VARCHAR2)
      RETURN NUMBER
   IS
      ban_number   NUMBER (9) := 0;

      CURSOR c_pcs
      IS
         SELECT customer_id
           FROM subscriber
          WHERE subscriber_no = phone_number;

      CURSOR c_mike
      IS
         SELECT ban
           FROM subscriber_rsource
          WHERE resource_number = phone_number;
   BEGIN
      OPEN c_pcs;

      FETCH c_pcs
       INTO ban_number;

      CLOSE c_pcs;

      IF ban_number = 0
      THEN
         OPEN c_mike;

         FETCH c_mike
          INTO ban_number;

         CLOSE c_mike;
      END IF;

      RETURN ban_number;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         ban_number := 0;
         RETURN ban_number;
      WHEN OTHERS
      THEN
         raise_application_error
                  (-20261,
                      'ERROR: subscriber_pkg.getBanIdByPhoneNumber(). SQL ['
                   || SQLCODE
                   || '] Error ['
                   || SQLERRM
                   || ']',
                   TRUE
                  );
         ban_number := 0;
         RETURN ban_number;
   END getbanidbyphonenumber;

   FUNCTION getpaidsecuritydeposit (
      banid          IN   NUMBER,
      subscriberid   IN   VARCHAR2,
      producttype    IN   VARCHAR2
   )
      RETURN NUMBER
   IS
      total_paid_amt   NUMBER (11, 2) := 0;
   BEGIN
      SELECT NVL (SUM (dep_paid_amt), 0)
        INTO total_paid_amt
        FROM invoice_item
       WHERE ban = banid
         AND subscriber_no = subscriberid
         AND product_type = producttype
         AND (inv_type = 'E' OR inv_type = 'D')
         AND (   inv_status = 'O'
              OR (NVL (dep_paid_amt, 0) > 0 AND dep_return_date IS NULL)
             );

      RETURN total_paid_amt;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         total_paid_amt := 0;
         RETURN total_paid_amt;
      WHEN OTHERS
      THEN
         raise_application_error
                 (-20261,
                     'ERROR: subscriber_pkg.getPaidSecurityDeposit(). SQL ['
                  || SQLCODE
                  || '] Error ['
                  || SQLERRM
                  || ']',
                  TRUE
                 );
         total_paid_amt := 0;
         RETURN total_paid_amt;
   END getpaidsecuritydeposit;

   FUNCTION getreservedsubscriberlistbyban (
      i_ban             IN       NUMBER,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         OPEN c_subscribers
          FOR
             SELECT DISTINCT s.subscriber_no, s.product_type
                        FROM subscriber s,
                             physical_device pd,
                             service_agreement sa
                       WHERE s.customer_id = i_ban
                         AND s.sub_status = 'R'
                         AND sa.ban(+) = s.customer_id
                         AND sa.subscriber_no(+) = s.subscriber_no
                         AND sa.soc IS NULL
                         AND pd.customer_id(+) = s.customer_id
                         AND pd.subscriber_no(+) = s.subscriber_no
                         AND pd.unit_esn IS NULL;

         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getreservedsubscriberlistbyban;

   PROCEDURE getbansubphonenosbysubstatus (
      i_banid     IN       NUMBER,
      v_status    IN       VARCHAR2,
      i_maximum   IN       NUMBER,
      c_results   OUT      refcursor
   )
   IS
   BEGIN
      OPEN c_results
       FOR
          SELECT *
            FROM (SELECT s1.subscriber_no
                    FROM subscriber s1
                   WHERE s1.customer_id = i_banid
                     AND s1.product_type = 'C'
                     AND s1.sub_status = v_status
                  UNION
                  SELECT sr.resource_number
                    FROM subscriber s2, subscriber_rsource sr
                   WHERE s2.customer_id = i_banid
                     AND s2.product_type = 'I'
                     AND s2.sub_status = v_status
                     AND sr.subscriber_no = s2.subscriber_no
                     AND sr.resource_status = 'A'
                     AND sr.resource_type = 'N'
                     AND sr.resource_seq =
                            (SELECT MAX (sr2.resource_seq)
                               FROM subscriber_rsource sr2
                              WHERE sr2.subscriber_no = sr.subscriber_no
                                AND sr2.resource_type = 'N'
                                AND sr2.resource_status = 'A'))
           WHERE ROWNUM < i_maximum + 1;
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
             (-20261,
                 'ERROR: subscriber_pkg.getbansubphonenosbysubstatus. SQL ['
              || SQLCODE
              || '] Error ['
              || SQLERRM
              || ']',
              TRUE
             );
   END getbansubphonenosbysubstatus;

   FUNCTION getbanforpartuallyreservedsub (
      v_phone_no        IN       VARCHAR2,
      c_subscribers     OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         OPEN c_subscribers
          FOR
             SELECT   s1.subscriber_no, s1.sub_status, s1.customer_id
                 FROM subscriber s1
                WHERE s1.subscriber_no = v_phone_no
                  AND s1.product_type <> 'I'
                  AND s1.sub_status != 'C'
             UNION
             SELECT   s2.subscriber_no, sr.resource_status, sr.ban
                 FROM subscriber s2, subscriber_rsource sr
                WHERE sr.resource_number = v_phone_no
                  AND sr.resource_status != 'C'
                  AND sr.resource_type = 'N'
                  AND sr.resource_seq =
                         (SELECT MAX (sr2.resource_seq)
                            FROM subscriber_rsource sr2
                           WHERE sr2.subscriber_no = sr.subscriber_no
                             AND sr2.ban = sr.ban
                             AND sr2.resource_type = 'N')
                  AND s2.subscriber_no = sr.subscriber_no
                  AND s2.sub_status != 'C'
             ORDER BY 2, 3 DESC;

         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getbanforpartuallyreservedsub;

   PROCEDURE getbansubidsbysubstatus (
      i_banid     IN       NUMBER,
      v_status    IN       VARCHAR2,
      i_maximum   IN       NUMBER,
      c_results   OUT      refcursor
   )
   IS
   BEGIN
      OPEN c_results
       FOR
          SELECT *
            FROM (SELECT subscriber_no
                    FROM subscriber
                   WHERE customer_id = i_banid AND sub_status = v_status)
           WHERE ROWNUM < i_maximum + 1;
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
                  (-20261,
                      'ERROR: subscriber_pkg.getbansubidsbysubstatus. SQL ['
                   || SQLCODE
                   || '] Error ['
                   || SQLERRM
                   || ']',
                   TRUE
                  );
   END getbansubidsbysubstatus;

   FUNCTION getpromohistory (
      i_ban_id          IN       NUMBER,
      v_subscriber_id   IN       VARCHAR2,
      v_service_ids     IN       t_category_code_array,
      c_promos          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text      VARCHAR2 (32767);
      i_index            BINARY_INTEGER;
      i_result           NUMBER (1);
      v_soc_category_t   soc_category_t;
   BEGIN
      BEGIN
         IF v_service_ids IS NOT NULL AND v_service_ids.COUNT > 0
         THEN
            getpromosocs (v_service_ids, v_soc_category_t);

            IF v_soc_category_t IS NOT NULL AND v_soc_category_t.COUNT > 0
            THEN
               OPEN c_promos
                FOR
                   SELECT   sc.category_code, sa.soc, sa.effective_date,
                            sa.expiration_date, sa.operator_id,
                            sa.application_id,
                            SUBSTR (sa.dealer_code, 1, 10),
                            SUBSTR (sa.dealer_code, 11)
                       FROM service_agreement sa,
                            TABLE (CAST (v_soc_category_t AS soc_category_t)) sc
                      WHERE ban = i_ban_id
                        AND subscriber_no = v_subscriber_id
                        AND sa.soc = sc.soc
                        AND TRUNC (effective_date) >=
                                                   TRUNC (sc.restr_start_date)
                        AND service_type NOT IN ('P', 'O', 'T')
                   ORDER BY 1;
            ELSE
               RAISE NO_DATA_FOUND;
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_promos%ISOPEN)
            THEN
               CLOSE c_promos;
            END IF;

            OPEN c_promos
             FOR
                SELECT NULL
                  FROM DUAL
                 WHERE 1 = 0;

            v_error_message := NULL;
            i_result := numeric_true;
         WHEN OTHERS
         THEN
            IF (c_promos%ISOPEN)
            THEN
               CLOSE c_promos;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getpromohistory;

   PROCEDURE getpromosocs (
      v_service_ids      IN       t_category_code_array,
      v_soc_category_t   OUT      soc_category_t
   )
   IS
   BEGIN
      SELECT   soc_category_o (psc.soc,
                               psc.category_code,
                               getstartrestrdate (vc.unit_of_measure_cd,
                                                  vc.period_cnt
                                                 )
                              )
      BULK COLLECT INTO v_soc_category_t
          FROM vendor_category vc, promo_soc_category psc
         WHERE vc.category_code IN (
                    SELECT *
                      FROM TABLE (CAST (v_service_ids AS t_category_code_array)
                                 ))
           AND vc.restrict_ind = 'Y'
           AND vc.category_code = psc.category_code
           AND vc.service_vendor_id = psc.service_vendor_id
           AND psc.expiry_dt IS NULL
      ORDER BY psc.category_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                          (-20102,
                              'ERROR: subscriber_pkg.getpromosocs. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])'
                          );
   END getpromosocs;

   FUNCTION getstartrestrdate (
      unit_of_measure_cd   IN   VARCHAR2,
      period_cnt           IN   NUMBER
   )
      RETURN DATE
   IS
      v_logical_date           DATE;
      restriction_start_date   DATE;
   BEGIN
      getlogicaldate (v_logical_date);

      IF UPPER (unit_of_measure_cd) = unit_of_measure_month
      THEN
         restriction_start_date := ADD_MONTHS (v_logical_date, -period_cnt);
      ELSE
         restriction_start_date := v_logical_date - period_cnt;
      END IF;

      RETURN restriction_start_date;
   END;

   PROCEDURE getlogicaldate (po_logical_date OUT DATE)
   IS
   BEGIN
      SELECT logical_date
        INTO po_logical_date
        FROM logical_date ld
       WHERE ld.logical_date_type = 'O';
   END getlogicaldate;

   /**
    * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getSubListByPhoneNumbers
    */
   FUNCTION getsublistbyphonenumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
   BEGIN
      i_result :=
         getsubidsbyphonenumbers (a_phone_numbers,
                                  i_include_cancelled,
                                  a_sub_ids,
                                  v_error_message
                                 );

      IF i_result = numeric_true
      THEN
         RETURN getsubscriberlistbyids (a_sub_ids,
                                        i_include_cancelled,
                                        c_subscribers,
                                        v_error_message
                                       );
      ELSE
         RETURN i_result;
      END IF;
   END getsublistbyphonenumbers;

   /**
    * DO NOT USE. Migrated to SUB_ATTRIB_RETRIEVAL_PKG.getSubIDsByPhoneNumbers
    */
   FUNCTION getsubidsbyphonenumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      t_subscriber_array,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text         VARCHAR2 (32767);
      c_cursor              refcursor;
      i_result              NUMBER (1);
      i_count               NUMBER (4);
      c_sub_status          CHAR (1);
      i_customer_id         NUMBER (9);
      v_phone_number        VARCHAR2 (10);
      v_phone_number_list   VARCHAR2 (32767);
      i_index               BINARY_INTEGER;
   BEGIN
      BEGIN
         v_phone_number_list := '';

         IF a_phone_numbers.COUNT > 0
         THEN
            i_index := 1;

            WHILE i_index <= a_phone_numbers.COUNT
            LOOP
               v_phone_number := RTRIM (LTRIM (a_phone_numbers (i_index)));

               IF i_index = 1
               THEN
                  v_phone_number_list := '''' || v_phone_number || '''';
               ELSE
                  v_phone_number_list :=
                     v_phone_number_list || ',' || '''' || v_phone_number
                     || '''';
               END IF;

               i_index := i_index + 1;
            END LOOP;

            v_cursor_text :=
                  'SELECT s1.subscriber_no, s1.sub_status, s1.customer_id, s1.req_deposit_amt, s1.port_type, s1.port_date '
               || '  FROM subscriber s1 '
               || ' WHERE s1.subscriber_no in ('
               || v_phone_number_list
               || ')';
            v_cursor_text :=
                            v_cursor_text || '   AND s1.product_type <> ''I''';

            IF i_include_cancelled = numeric_false
            THEN
               v_cursor_text :=
                             v_cursor_text || '   AND s1.sub_status != ''C''';
            END IF;

            v_cursor_text :=
                  v_cursor_text
               || ' UNION '
               || 'SELECT s2.subscriber_no, sr.resource_status, sr.resource_seq, s2.req_deposit_amt, s2.port_type, s2.port_date '
               || '  FROM subscriber s2, '
               || '       subscriber_rsource sr '
               || ' WHERE sr.resource_number in ('
               || v_phone_number_list
               || ')';

            IF i_include_cancelled = numeric_false
            THEN
               v_cursor_text :=
                        v_cursor_text || '   AND sr.resource_status != ''C''';
            END IF;

            v_cursor_text :=
                  v_cursor_text
               || '   AND sr.resource_type = ''N'''
               || '   AND sr.resource_seq = (SELECT MAX(sr2.resource_seq) '
               || '                            FROM subscriber_rsource sr2 '
               || '                           WHERE sr2.subscriber_no = sr.subscriber_no '
               || '                             AND sr2.ban = sr.ban '
               || '                             AND sr2.resource_type = ''N'') '
               || '   AND s2.subscriber_no = sr.subscriber_no ';

            IF i_include_cancelled = numeric_false
            THEN
               v_cursor_text :=
                             v_cursor_text || '   AND s2.sub_status != ''C''';
            END IF;

            v_cursor_text := v_cursor_text || ' ORDER BY 2, 3 DESC';

            OPEN c_cursor
             FOR v_cursor_text;

            a_sub_ids := t_subscriber_array (NULL);
            i_count := 1;

            LOOP
               a_sub_ids.EXTEND;

               FETCH c_cursor
                INTO a_sub_ids (i_count), c_sub_status, i_customer_id;

               EXIT WHEN c_cursor%NOTFOUND;
               i_count := i_count + 1;
            END LOOP;

            IF c_cursor%ISOPEN
            THEN
               CLOSE c_cursor;
            END IF;

            IF i_count > 1
            THEN
               WHILE a_sub_ids (a_sub_ids.COUNT) IS NULL
               LOOP
                  a_sub_ids.TRIM;
               END LOOP;
            END IF;

            IF a_sub_ids.COUNT > 0
            THEN
               i_result := numeric_true;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
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
   END getsubidsbyphonenumbers;

-------------------------------------------
   FUNCTION getsublistbyimsi (
      i_imsi                IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF     i_imsi IS NOT NULL
            AND LENGTH (RTRIM (LTRIM (i_imsi))) > 0
         THEN
            IF i_include_cancelled = numeric_false
            THEN
            OPEN c_subscribers
                FOR
				SELECT DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   pd.unit_esn,
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group 
                              FROM physical_device pd,
							  	   subscriber_rsource sr,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   logical_date ld
                              WHERE sr.resource_type = 'Q'
				AND sr.imsi_number = i_imsi
				AND sr.record_exp_date = TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
                               AND s.subscriber_no = sr.subscriber_no
                               AND s.customer_id = sr.ban
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                    OR sa.expiration_date IS NULL
                                   )
                               AND ld.logical_date_type = 'O'
                               AND sa.effective_date =
                                      (SELECT MIN (sa1.effective_date)
                                         FROM service_agreement sa1
                                        WHERE sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                          AND sa1.product_type =
                                                               sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL
                                              )
                                          AND ld.logical_date_type = 'O')
                               AND pd.customer_id = s.customer_id
                               AND pd.subscriber_no = s.subscriber_no
                               AND pd.product_type = s.product_type
                               AND pd.esn_level = 1
                               AND pd.expiration_date IS NULL;		   
						   
            ELSE
            OPEN c_subscribers
                FOR
                SELECT /*+first_rows*/ DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd_1.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   pd_1.unit_esn,
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group 
                              FROM subscriber_rsource sr,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   logical_date ld,
                                   physical_device pd_1
                             WHERE sr.resource_type = 'Q'
                               and sr.imsi_number = i_imsi
                               and sr.record_exp_date = TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
                               AND s.subscriber_no = sr.subscriber_no
                               AND s.customer_id = sr.ban
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                    OR sa.expiration_date IS NULL
                                   )
                               AND ld.logical_date_type = 'O'
                               AND sa.effective_date =
                                      (SELECT MIN (sa1.effective_date)
                                         FROM service_agreement sa1
                                        WHERE sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                              sa.subscriber_no
                                          AND sa1.product_type =
                                                               sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL
                                              )
                                          AND ld.logical_date_type = 'O')
                               AND pd_1.customer_id = s.customer_id
                               AND pd_1.subscriber_no = s.subscriber_no
                               AND pd_1.product_type = s.product_type
                               AND pd_1.esn_level = 1
                               AND pd_1.expiration_date IS NULL
                   UNION
                   SELECT /*+first_rows*/ DISTINCT s.customer_id, s.subscriber_no,
                                   s.sub_status, nd.first_name,
                                   nd.middle_initial, nd.last_business_name,
                                   pd_2.unit_esn, s.product_type,
                                   SUBSTR (s.dealer_code, 1, 10), sa.soc,
                                   s.email_address,
                                   NVL (s.init_activation_date,
                                        s.effective_date
                                       ),
                                   s.init_activation_date,
                                   SUBSTR (s.dealer_code, 11),
                                   s.sub_status_rsn_code,
                                   s.sub_status_last_act, s.sub_alias,
                                   DECODE (INSTR (user_seg, '@'),
                                           0, '',
                                           SUBSTR (user_seg,
                                                   INSTR (user_seg, '@') + 1,
                                                   1
                                                  )
                                          ),
                                   sub_lang_pref, s.next_ctn,
                                   s.next_ctn_chg_date, s.prv_ctn,
                                   s.prv_ctn_chg_date,
                                   DECODE (s.sub_status,
                                           'S', 'B',
                                           s.sub_status
                                          ),
                                   pd_2.unit_esn,
                                   s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,
                                   s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,
                                   s.tax_pst_exmp_eff_dt,
                                   s.tax_hst_exmp_eff_dt,
                                   s.tax_gst_exmp_exp_dt,
                                   s.tax_pst_exmp_exp_dt,
                                   s.tax_hst_exmp_exp_dt,
                                   s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no,
                                   s.tax_hst_exmp_rf_no, s.sub_status_date,
                                   s.calls_sort_order, s.commit_reason_code,
                                   s.commit_orig_no_month,
                                   s.commit_start_date, s.commit_end_date,
                                   nd.name_suffix, nd.additional_title,
                                   nd.name_title, s.hot_line_ind,
                                   nd.name_format, s.migration_type,
                                   s.migration_date, s.tenure_date,
                                   s.req_deposit_amt, s.port_type,
                                   s.port_date, s.brand_id, NVL(s.external_id, 0) as subscription_id,s.seat_type, s.seat_group 
                              FROM subscriber_rsource sr,
                                   subscriber s,
                                   address_name_link anl,
                                   name_data nd,
                                   service_agreement sa,
                                   physical_device pd_2
                             WHERE sr.resource_type = 'Q'
                               and sr.imsi_number = i_imsi
                               and sr.record_exp_date = TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
                               AND s.subscriber_no = sr.subscriber_no
                               AND s.customer_id = sr.ban
                               AND anl.ban(+) = s.customer_id
                               AND anl.subscriber_no(+) = s.subscriber_no
                               AND anl.expiration_date IS NULL
                               AND nd.name_id(+) = anl.name_id
                               AND sa.ban = s.customer_id
                               AND sa.subscriber_no = s.subscriber_no
                               AND sa.service_type = 'P'
                               AND sa.effective_date =
                                              (SELECT MAX (sa1.effective_date)
                                                  FROM service_agreement sa1
                                                  WHERE sa1.ban = sa.ban
                                                      AND sa1.subscriber_no =  sa.subscriber_no
                                                      AND sa1.product_type = sa.product_type
                                                      AND sa1.service_type = sa.service_type)
 							   AND sa.expiration_date =  (SELECT MAX (sa1.expiration_date)
		                                                  FROM service_agreement sa1
		                                                  WHERE sa1.ban = sa.ban
	                                                      AND sa1.subscriber_no =  sa.subscriber_no
	                                                      AND sa1.product_type = sa.product_type
	                                                      AND sa1.service_type = sa.service_type)
                               AND pd_2.customer_id = s.customer_id
                               AND pd_2.subscriber_no = s.subscriber_no
                               AND pd_2.product_type = s.product_type
                               AND (s.sub_status = 'C'
                                        AND pd_2.esn_seq_no =
                                               (SELECT MAX (esn_seq_no)
                                                  FROM physical_device pd1
                                                 WHERE pd1.customer_id =
                                                              pd_2.customer_id
                                                   AND pd1.subscriber_no =
                                                            pd_2.subscriber_no
                                                   AND pd1.product_type =
                                                             pd_2.product_type
                                                   AND NVL (pd1.esn_level, 1) = 1)
                                   )
                          ORDER BY 25, 24, 12 DESC;
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
    END getsublistbyimsi;

---------------------------------------
   FUNCTION getsubidsbyimsi (
      i_imsi                IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      t_subscriber_array,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      c_cursor        refcursor;
      i_result        NUMBER (1);
      i_count         NUMBER (4);
      c_sub_status    CHAR (1);
      i_customer_id   NUMBER (9);
   BEGIN
      BEGIN
         IF i_include_cancelled = numeric_true
         THEN
            OPEN c_cursor
             FOR
                SELECT   sr1.subscriber_no, sr1.resource_status,
                         sr1.resource_seq
                    FROM subscriber_rsource sr1
                   WHERE sr1.imsi_number = i_imsi
                     AND sr1.resource_type = 'Q'
                     AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')
                ORDER BY 2, 3 DESC;
         ELSE
            OPEN c_cursor
             FOR
                SELECT   sr1.subscriber_no, sr1.resource_status,
                         sr1.resource_seq
                    FROM subscriber_rsource sr1
                   WHERE sr1.imsi_number = i_imsi
                     AND sr1.resource_type = 'Q'
                     AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')
                     AND sr1.resource_status != 'C'
                ORDER BY 2, 3 DESC;
         END IF;

         a_sub_ids := t_subscriber_array (NULL);
         i_count := 1;

         LOOP
            a_sub_ids.EXTEND;

            FETCH c_cursor
             INTO a_sub_ids (i_count), c_sub_status, i_customer_id;

            EXIT WHEN c_cursor%NOTFOUND;
            i_count := i_count + 1;
         END LOOP;

         IF c_cursor%ISOPEN
         THEN
            CLOSE c_cursor;
         END IF;

         IF i_count > 1
         THEN
            WHILE a_sub_ids (a_sub_ids.COUNT) IS NULL
            LOOP
               a_sub_ids.TRIM;
            END LOOP;
         END IF;

         IF a_sub_ids.COUNT > 0
         THEN
            i_result := numeric_true;
         ELSE
            v_error_message := err_no_data_found;
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
   END getsubidsbyimsi;

---------------------------------------------------------------------------------------
   FUNCTION gethsparesources (
      i_bans             IN       t_ban_array,
      v_subscriber_ids   IN       t_subscriber_array,
      c_resources        OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text             VARCHAR2 (32767);
      i_index                   BINARY_INTEGER;
      i_result                  NUMBER (1);
      v_account_subscriber_ob   account_subscriber_o;
   BEGIN
      BEGIN
         IF     v_subscriber_ids.COUNT > 0
            AND i_bans.COUNT = v_subscriber_ids.COUNT
         THEN
            v_account_subscriber_tab := account_subscriber_t ();
            i_index := 1;

            WHILE i_index <= v_subscriber_ids.COUNT AND i_index <= max_maximorum
            LOOP
               initaccountsubscriberobject (v_account_subscriber_ob);
               v_account_subscriber_ob.account_id := i_bans (i_index);
               v_account_subscriber_ob.subscriber_id :=
                                                   v_subscriber_ids (i_index);
               v_account_subscriber_tab.EXTEND;
               v_account_subscriber_tab (v_account_subscriber_tab.LAST) :=
                                                      v_account_subscriber_ob;
               i_index := i_index + 1;
            END LOOP;

            OPEN c_resources
             FOR
                SELECT   subscriber_no, ban, resource_number, resource_type,
                         resource_status, imsi_number, resource_seq
                    FROM subscriber_rsource sr
                   WHERE (sr.ban, sr.subscriber_no) IN (
                            SELECT *
                              FROM TABLE
                                      (CAST
                                          (v_account_subscriber_tab AS account_subscriber_t
                                          )
                                      ))
                     AND sr.resource_type = 'Q'
                     AND sr.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')
                ORDER BY subscriber_no, ban, resource_seq;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_resources%ISOPEN)
            THEN
               CLOSE c_resources;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_resources%ISOPEN)
            THEN
               CLOSE c_resources;
            END IF;

           -- v_error_message := v_cursor_text;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END gethsparesources;
----------------------------------------------------------------------------------------
   FUNCTION getBanAndPhoneNoByIMSI (
      i_imsi             IN       VARCHAR2,
      c_cursor           OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result                  NUMBER (1);
   BEGIN
      BEGIN
            OPEN c_cursor
             FOR
                SELECT   sr1.subscriber_no, sr1.ban
                    FROM subscriber_rsource sr1
                   WHERE sr1.imsi_number = i_imsi
                     AND sr1.resource_type = 'Q'
                     AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')
                     AND sr1.resource_status != 'C';
                     
            i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

           -- v_error_message := v_cursor_text;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getBanAndPhoneNoByIMSI;
---------------------------------------------------------------------------------------------------------

	FUNCTION getlastassocsubscriptionid (i_imsi IN VARCHAR2)
	   RETURN VARCHAR2
	IS
	   v_subscription_id   VARCHAR2 (20) := '0';
	   v_row_count         NUMBER (2)    := 0;
	
	   CURSOR c_sub_id
	   IS
			SELECT s.external_id AS subscription_id
			FROM subscriber s, subscriber_rsource sr1
			WHERE 	sr1.imsi_number = i_imsi
			AND sr1.resource_type = 'Q'	
			AND sr1.subscriber_no = s.subscriber_no
			AND sr1.ban = s.CUSTOMER_BAN
			ORDER BY resource_seq DESC;	
									  
	BEGIN
	   OPEN c_sub_id;
	
	   FETCH c_sub_id
	    INTO v_subscription_id;
	
	   v_row_count := c_sub_id%ROWCOUNT;
	
	   IF v_row_count = 0
	   THEN
	      v_subscription_id := '0';
	   END IF;
	
		IF v_subscription_id IS NULL
	    THEN
	         v_subscription_id := '0';
	    END IF;
		
		IF (c_sub_id%ISOPEN)
        THEN
               CLOSE c_sub_id;
        END IF;
	   RETURN v_subscription_id;
	END getlastassocsubscriptionid;

	/**
	 * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getLwSubListByBan
	 */
   FUNCTION getLwSubsByBan(
      i_ban                 IN       NUMBER,
      i_isIDEN              IN       NUMBER,
      i_include_cancelled   IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result   NUMBER (1);
   BEGIN
      BEGIN
         IF  i_ban IS NOT NULL and i_ban >0
         THEN
            IF i_include_cancelled = numeric_false THEN
               IF i_isIDEN = numeric_true THEN
                  -- mike , not cancelled
                  OPEN c_subscribers  FOR
                     SELECT /*+ INDEX(sr SUBSCRIBER_RSOURCE_4IX) */
	                    DISTINCT
	                      s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type,
	                      nd.first_name, nd.last_business_name
	                      ,DECODE(s.sub_status, 'S', 'B', s.sub_status) sort1
	                      ,NVL(s.init_activation_date, s.effective_date) sort2
	                      , sr.resource_number phone_number
	                      , NVL(s.external_id, 0) external_id
	                  FROM subscriber s
	                    left outer join address_name_link anl on anl.ban = s.customer_id and anl.subscriber_no = s.subscriber_no AND anl.expiration_date IS NULL
	                    left outer join name_data nd on nd.name_id = anl.name_id
	                    left outer join subscriber_rsource sr on sr.ban=s.customer_id AND sr.subscriber_no=s.subscriber_no AND sr.resource_type='N' and sr.RECORD_EXP_DATE=TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
	                  WHERE
	                    s.customer_id = i_ban
	                    AND s.sub_status != 'C'
	                    ORDER BY sort1, sort2 DESC;
	            ELSE
                  -- non mike, not cancelled: without subscriber_rsource table outer join
                  OPEN c_subscribers  FOR
                     SELECT 
	                    DISTINCT
	                      s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type,
	                      nd.first_name, nd.last_business_name
	                      ,DECODE(s.sub_status, 'S', 'B', s.sub_status) sort1
	                      ,NVL(s.init_activation_date, s.effective_date) sort2
	                      , NVL(s.external_id, 0) external_id
	                  FROM subscriber s
	                    left outer join address_name_link anl on anl.ban = s.customer_id and anl.subscriber_no = s.subscriber_no AND anl.expiration_date IS NULL
	                    left outer join name_data nd on nd.name_id = anl.name_id
	                  WHERE
	                    s.customer_id = i_ban
	                    AND s.sub_status != 'C'
	                    ORDER BY sort1, sort2 DESC;
	             END IF;
            ELSE
               IF i_isIDEN = numeric_true THEN
                  -- mike , include cancelled
                  OPEN c_subscribers  FOR
                     SELECT /*+ INDEX(sr SUBSCRIBER_RSOURCE_4IX) */
	                    DISTINCT
	                      s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type,
	                      nd.first_name, nd.last_business_name
	                      ,DECODE(s.sub_status, 'S', 'B', s.sub_status) sort1
	                      ,NVL(s.init_activation_date, s.effective_date) sort2
	                      , sr.resource_number phone_number
	                      , NVL(s.external_id, 0) external_id
	                  FROM subscriber s
	                    left outer join address_name_link anl on anl.ban = s.customer_id and anl.subscriber_no = s.subscriber_no AND anl.expiration_date IS NULL
	                    left outer join name_data nd on nd.name_id = anl.name_id
	                    left outer join subscriber_rsource sr on sr.ban=s.customer_id AND sr.subscriber_no=s.subscriber_no AND sr.resource_type='N' and sr.RECORD_EXP_DATE=TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
	                  WHERE
	                    s.customer_id = i_ban
	                    ORDER BY sort1, sort2 DESC;
	            ELSE
                  -- non mike, include cancelled: without subscriber_rsource table outer join
                  OPEN c_subscribers  FOR
                     SELECT 
	                    DISTINCT
	                      s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type,
	                      nd.first_name, nd.last_business_name
	                      ,DECODE(s.sub_status, 'S', 'B', s.sub_status) sort1
	                      ,NVL(s.init_activation_date, s.effective_date) sort2
	                      , NVL(s.external_id, 0) external_id
	                  FROM subscriber s
	                    left outer join address_name_link anl on anl.ban = s.customer_id and anl.subscriber_no = s.subscriber_no AND anl.expiration_date IS NULL
	                    left outer join name_data nd on nd.name_id = anl.name_id
	                  WHERE
	                    s.customer_id = i_ban
	                    ORDER BY sort1, sort2 DESC;
	             END IF;
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_subscribers%ISOPEN)
            THEN
               CLOSE c_subscribers;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getLwSubsByBan;

   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;

	FUNCTION getHotlinedPhoneNoByBan(
   	  pi_ban	IN NUMBER
   	) RETURN VARCHAR2
   	IS
   		po_phoneNumber VARCHAR2(10) := '';
   	BEGIN
		   	SELECT phoneNumber INTO po_phoneNumber FROM (
			   	select s1.subscriber_no as phoneNumber
			   	from   subscriber s1 
			   	where  s1.customer_id = pi_ban
			   	and    s1.product_type = 'C'
			   	and    s1.sub_status = 'A'
			   	and    s1.hot_line_ind = 'Y'
			   	union 
			   	select sr.resource_number as phoneNumber
			   	from   subscriber s2, subscriber_rsource sr 
				where  s2.customer_id = pi_ban 
				and    s2.product_type = 'I' 
				and    s2.sub_status = 'A' 
				and    s2.hot_line_ind = 'Y' 
				and    sr.subscriber_no = s2.subscriber_no 
				and    sr.resource_status = 'A' 
				and    sr.resource_type = 'N' 
				and    sr.resource_seq = (select max(sr2.resource_seq) 
											from   subscriber_rsource sr2 
											where  sr2.subscriber_no = sr.subscriber_no 
											and    sr2.resource_type = 'N' 
											and    sr2.resource_status = 'A')
			);

		RETURN po_phoneNumber;
		
		EXCEPTION
	         WHEN NO_DATA_FOUND
	         THEN
	            RETURN '';
	         WHEN OTHERS
	         THEN
	            RETURN '';
   
	END getHotlinedPhoneNoByBan;

   	PROCEDURE retrieveResourceChangeHistory (
   	  pi_ban			IN	NUMBER,
   	  pi_subscriberID  	IN	VARCHAR2,
   	  pi_resourceType	IN  VARCHAR2,
   	  pi_fromDate		IN  VARCHAR2,
   	  pi_toDate			IN	VARCHAR2,
   	  po_curChangeHistory	OUT	refcursor
   	)
   	IS
   	BEGIN
	   	IF pi_resourceType = '*' THEN
		   	OPEN po_curChangeHistory FOR
			   	SELECT resource_type, resource_number, resource_status, res_sts_date, operator_id, application_id
				FROM subscriber_rsource
				WHERE subscriber_no  = pi_subscriberID and ban = pi_ban
					and res_sts_date between to_date(pi_fromDate,'mm/dd/yyyy') 
					and to_date(pi_toDate,'mm/dd/yyyy')
					ORDER BY resource_seq;
	   	ELSE
	   		OPEN po_curChangeHistory FOR
			   	SELECT resource_type, resource_number, resource_status, res_sts_date, operator_id, application_id
				FROM subscriber_rsource
				WHERE subscriber_no  = pi_subscriberID and ban = pi_ban
					and resource_type = pi_resourceType
					and res_sts_date between to_date(pi_fromDate,'mm/dd/yyyy') 
					and to_date(pi_toDate,'mm/dd/yyyy')
					ORDER BY resource_seq;	   	
	   	END IF;
	   	
		EXCEPTION
	         WHEN NO_DATA_FOUND
	         THEN
	            IF po_curChangeHistory%ISOPEN THEN
	            	CLOSE po_curChangeHistory;
	            END IF;
	         WHEN OTHERS
	         THEN
	           	IF po_curChangeHistory%ISOPEN THEN
	            	CLOSE po_curChangeHistory;
	            END IF;
	END retrieveResourceChangeHistory;
	
	/**
	 * DO NOT USE. Migrated to SUB_RETRIEVAL_PKG.getSubByBANandPhoneNumber
	 */
	FUNCTION getsubbybanandphonenumber (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      v_subscribers         OUT      refcursor,       
      v_error_message       OUT      VARCHAR2
    )    RETURN NUMBER

	IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
      i_ext_id	NUMBER(9);
      v_sub_id	VARCHAR2 (20);
      
   	BEGIN
      IF v_phone_no IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_no))) > 0
      THEN
          i_result :=
            getsubidsbybanandphonenumber (i_ban,
                                          v_phone_no,
                                          i_include_cancelled,
                                          a_sub_ids,
                                          i_ext_id,
                                          v_error_message);

         IF i_result = numeric_true
         THEN
            IF a_sub_ids IS NOT NULL AND a_sub_ids.COUNT > 0
            THEN
                v_sub_id := a_sub_ids (1);
                i_result := 
                  getsubscriberlistbyban(i_ban, 
                						v_sub_id, 
                						i_include_cancelled, 
                						v_subscribers, 
                						v_error_message); 
                IF i_result = numeric_true
                THEN   
                    IF v_subscribers%ROWCOUNT < 0
                    THEN
                        v_error_message := err_no_data_found;
                        i_result := numeric_false;
                    END IF;
                END IF;

            ELSE
                v_error_message := err_no_data_found;
                i_result := numeric_false;
            END IF;
         END IF;
      ELSE
          v_error_message := err_invalid_input;
          i_result := numeric_false;
      END IF;

      RETURN i_result;
       
    END getsubbybanandphonenumber;
	
	
END;
/
