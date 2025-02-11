CREATE OR REPLACE PACKAGE SUB_RETRIEVAL_PKG
AS
   --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   -- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   -- Please increment the version_no variable value when you make changes to this package
   -- to reflect version changes.
   ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   -- Date           Version Developer           Modifications
   -- Mar 26,2013   3.23.1 Tsz Chung Tong      Refactored query from getAccountInfoByBAN from RA_UTILITY_PKG to this package
   -- Mar 27,2013   3.23.2 Mahan Razaghzadeh   Added New enhanced function: GetsubscriberlistbybanEnhanced
   -- Apr 3, 2013   3.23.3 Tsz Chung Tong      Refactored procedure getSubscriberListByBAN with getSubListByBAN,  getSubListByBANAndSubID, getSubListBySubID and getSubListByBANAndOrSubIDs
   -- Apr 3, 2013   3.23.4 Tsz Chung Tong      Added getSubIDsByPhoneNumbers and getSubListByPhoneNumbers
   -- Apr 10, 2013  3.23.5 Tsz Chung Tong      Moved getSubIDsByPhoneNumbers to SUB_ATTR_RETRIEVAL and updated getSubListByPhoneNumbers
   -- Apr 11, 2013  3.23.6 Tsz Chung Tong      Added getSubListByBanAndPhoneNumber
   -- May 22, 2013  3.23.7 Tsz Chung Tong      Added /*+ cardinality( t 10 ) */ hint to getSubListByBANAndOrSubIDs
   -- May 22, 2013  3.23.8 Tsz Chung Tong      Overloaded getSubListByBANAndOrSubID to accept a single sub ID only
   -- May 24, 2013  3.23.9 Tsz Chung Tong      Fixed performance issue in getSubListByBANAndOrSubID and refactored it.
   -- Jun 5, 2013   3.23.10 Tsz Chung Tong     Fixed defect 13522  on where getSubListByBanAndPhoneNumber is not invoking the right stored proc 
   -- March 1, 2014 3.28.1 Naresh Annabathula  Added new functions getSubListByBanAndSeatNumber  and getResourceByBanAndOrPhoneNum .
    --March 1, 2014 3.28.1 Naresh Annabathula  updated getSubListByBAN ,getSubListByBANAndSubIDs,getSubListBySubIDs,getLwSubListByBan to retrive seatType and seatGroup .
   -- May 24, 2014 3.28.3 Naresh Annabathula fixed getSubListByBanAndSeatNumber search issue,  added check to support seat search with/without ban.
   -- 2017-12-05	2018.04	Emerson Cho			Removed hint from getLwSubListByBan per DBA (Anar) advice
   -- May 28, 2018 2018.07.1 Tsz Chung Tong    Changed queries with UNION to use UNION ALL
   -- May 31, 2018 2018.07.7 Tsz Chung Tong    Added /*+ rule */ hint to getSubListBySubIDs to fix performance issue with DBA (Anar)'s advice. The functionalities are best to be moved to inline sql for Oracle to work better with cost-based execution
   ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

   TYPE REFCURSOR IS REF CURSOR;

   version_no          CONSTANT VARCHAR2 (10) := '2018.07.7';

   -- result constants
   NUMERIC_TRUE        CONSTANT NUMBER (1) := 0;
   NUMERIC_FALSE       CONSTANT NUMBER (1) := 1;

   -- error messages
   err_invalid_input   CONSTANT VARCHAR2 (50) := 'Input parameters are invalid or NULL.' ;
   err_no_data_found   CONSTANT VARCHAR2 (30) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (30) := 'Other PL/SQL error.';
   
   DEFAULT_SUB_MAXIMUM CONSTANT NUMBER(3) := 100;

   FUNCTION getVersion
      RETURN VARCHAR2;


   PROCEDURE getSubListByBAN (i_ban                 IN     NUMBER,
                              i_include_cancelled   IN     NUMBER,
                              i_maximumResult       IN     NUMBER,
                              c_subscribers            OUT refcursor,
                              v_error_message          OUT VARCHAR2);

   PROCEDURE getSubListByBANAndSubID (i_ban                 IN     NUMBER,
                                      v_subscriber_id       IN     VARCHAR2,
                                      i_include_cancelled   IN     NUMBER,
                                      i_maximumResult       IN     NUMBER,
                                      c_subscribers            OUT refcursor,
                                      v_error_message          OUT VARCHAR2);

   PROCEDURE getSubListBySubID (v_subscriber_id       IN     VARCHAR2,
                                i_include_cancelled   IN     NUMBER,
                                i_maximumResult       IN     NUMBER,
                                c_subscribers            OUT refcursor,
                                v_error_message          OUT VARCHAR2);

   FUNCTION getSubByBANandPhoneNumber (
   	  i_ban                 IN     NUMBER,
      v_phone_number        IN     VARCHAR2,
      i_include_cancelled   IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2)
      RETURN NUMBER;


  FUNCTION getSubListByPhoneNumbers (
      a_phone_numbers       IN     t_phone_num_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2)
   RETURN NUMBER;
   
   FUNCTION getSubListByBanAndPhoneNumber (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      i_maximum             IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )RETURN NUMBER;
      
    FUNCTION getLwSubListByBan(
      i_ban                 IN       NUMBER,
      i_isIDEN              IN       NUMBER,
      i_include_cancelled   IN       NUMBER,
      i_maximumResult       IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )RETURN NUMBER;
      
    FUNCTION getSubListByBanAndSeatNumber (
      i_ban                 IN       NUMBER,
      i_seat_number     IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      i_maximum             IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )RETURN NUMBER;
   
    PROCEDURE getResourceByBanAndOrPhoneNum (
      i_ban                 IN       NUMBER,
      i_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_resource        OUT      REFCURSOR
   );
END SUB_RETRIEVAL_PKG;
/

SHO ERR


CREATE OR REPLACE PACKAGE BODY SUB_RETRIEVAL_PKG
AS
   PROCEDURE getSubListByBANAndSubIDs (
      i_ban                 IN     NUMBER,
      a_subscriber_ids      IN     t_subscriber_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT NOCOPY VARCHAR2);
      
  PROCEDURE getSubListBySubIDs (
      a_subscriber_ids      IN     t_subscriber_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2);

   FUNCTION getVersion
      RETURN VARCHAR2
   IS
   BEGIN
      RETURN version_no;
   END getVersion;

   -----------------------------------------------
   PROCEDURE getSubListByBAN (i_ban                 IN     NUMBER,
                              i_include_cancelled   IN     NUMBER,
                              i_maximumResult       IN     NUMBER,
                              c_subscribers            OUT refcursor,
                              v_error_message          OUT VARCHAR2)
   IS
   BEGIN
      IF i_include_cancelled = numeric_true
      THEN
         OPEN C_SUBSCRIBERS FOR
            SELECT *
              FROM (SELECT DISTINCT
                           s.customer_id,
                           s.subscriber_no,
                           s.sub_status,
                           nd.first_name,
                           nd.middle_initial,
                           nd.last_business_name,
                           pd.unit_esn,
                           s.product_type,
                           SUBSTR (s.dealer_code, 1, 10),
                           sa.soc,
                           s.email_address,
                           NVL (s.init_activation_date, s.effective_date),
                           s.init_activation_date,
                           SUBSTR (s.dealer_code, 11),
                           s.sub_status_rsn_code,
                           s.sub_status_last_act,
                           s.sub_alias,
                           DECODE (
                              INSTR (user_seg, '@'),
                              0, '',
                              SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                           sub_lang_pref,
                           s.next_ctn,
                           s.next_ctn_chg_date,
                           s.prv_ctn,
                           s.prv_ctn_chg_date,
                           DECODE (s.sub_status, 'S', 'B', s.sub_status),
                           s.tax_gst_exmp_ind,
                           s.tax_pst_exmp_ind,
                           s.tax_hst_exmp_ind,
                           s.tax_gst_exmp_eff_dt,
                           s.tax_pst_exmp_eff_dt,
                           s.tax_hst_exmp_eff_dt,
                           s.tax_gst_exmp_exp_dt,
                           s.tax_pst_exmp_exp_dt,
                           s.tax_hst_exmp_exp_dt,
                           s.tax_gst_exmp_rf_no,
                           s.tax_pst_exmp_rf_no,
                           s.tax_hst_exmp_rf_no,
                           s.sub_status_date,
                           s.calls_sort_order,
                           s.commit_reason_code,
                           s.commit_orig_no_month,
                           s.commit_start_date,
                           s.commit_end_date,
                           nd.name_suffix,
                           nd.additional_title,
                           nd.name_title,
                           s.hot_line_ind,
                           nd.name_format,
                           s.migration_type,
                           s.migration_date,
                           s.tenure_date,
                           s.req_deposit_amt,
                           s.port_type,
                           s.port_date,
                           s.brand_id,
                           NVL (s.external_id, 0) AS subscription_id,
                          s.seat_type,
                           s.seat_group 
                      FROM subscriber s,
                           address_name_link anl,
                           name_data nd,
                           physical_device pd,
                           service_agreement sa
                     WHERE  s.customer_id = i_ban
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
                                    WHERE     sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                 sa.subscriber_no
                                          AND sa1.product_type =
                                                 sa.product_type
                                          AND sa1.service_type = 'P')
                           AND pd.customer_id = s.customer_id
                           AND pd.subscriber_no = s.subscriber_no
                           AND pd.product_type = s.product_type
                           AND pd.esn_seq_no =
                                  (SELECT MAX (esn_seq_no)
                                     FROM physical_device pd1
                                    WHERE     pd1.customer_id =
                                                 pd.customer_id
                                          AND pd1.subscriber_no =
                                                 pd.subscriber_no
                                          AND pd1.product_type =
                                                 pd.product_type
                                          AND NVL (pd1.esn_level, 1) = 1)
                    UNION ALL
                    SELECT DISTINCT 
                           s.customer_id,
                           s.subscriber_no,
                           s.sub_status,
                           nd.first_name,
                           nd.middle_initial,
                           nd.last_business_name,
                           pd.unit_esn,
                           s.product_type,
                           SUBSTR (s.dealer_code, 1, 10),
                           sa.soc,
                           s.email_address,
                           NVL (s.init_activation_date, s.effective_date),
                           s.init_activation_date,
                           SUBSTR (s.dealer_code, 11),
                           s.sub_status_rsn_code,
                           s.sub_status_last_act,
                           s.sub_alias,
                           DECODE (
                              INSTR (user_seg, '@'),
                              0, '',
                              SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                           sub_lang_pref,
                           s.next_ctn,
                           s.next_ctn_chg_date,
                           s.prv_ctn,
                           s.prv_ctn_chg_date,
                           DECODE (s.sub_status, 'S', 'B', s.sub_status),
                           s.tax_gst_exmp_ind,
                           s.tax_pst_exmp_ind,
                           s.tax_hst_exmp_ind,
                           s.tax_gst_exmp_eff_dt,
                           s.tax_pst_exmp_eff_dt,
                           s.tax_hst_exmp_eff_dt,
                           s.tax_gst_exmp_exp_dt,
                           s.tax_pst_exmp_exp_dt,
                           s.tax_hst_exmp_exp_dt,
                           s.tax_gst_exmp_rf_no,
                           s.tax_pst_exmp_rf_no,
                           s.tax_hst_exmp_rf_no,
                           s.sub_status_date,
                           s.calls_sort_order,
                           s.commit_reason_code,
                           s.commit_orig_no_month,
                           s.commit_start_date,
                           s.commit_end_date,
                           nd.name_suffix,
                           nd.additional_title,
                           nd.name_title,
                           s.hot_line_ind,
                           nd.name_format,
                           s.migration_type,
                           s.migration_date,
                           s.tenure_date,
                           s.req_deposit_amt,
                           s.port_type,
                           s.port_date,
                           s.brand_id,
                           NVL (s.external_id, 0) AS subscription_id,
                            s.seat_type,
                           s.seat_group 
                      FROM subscriber s,
                           address_name_link anl,
                           name_data nd,
                           physical_device pd,
                           service_agreement sa,
                           logical_date ld
                     WHERE s.customer_id = i_ban
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
                                OR sa.expiration_date IS NULL)
                           AND ld.logical_date_type = 'O'
                           AND sa.effective_date =
                                  (SELECT MIN (sa1.effective_date)
                                     FROM service_agreement sa1,
                                          logical_date ld
                                    WHERE     sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                 sa.subscriber_no
                                          AND sa1.product_type =
                                                 sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                     TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL)
                                          AND ld.logical_date_type = 'O')
                           AND pd.customer_id = s.customer_id
                           AND pd.subscriber_no = s.subscriber_no
                           AND pd.product_type = s.product_type
                           AND pd.esn_level = 1
                           AND pd.expiration_date IS NULL
                    ORDER BY 24, 12 DESC)
             WHERE ROWNUM <= i_maximumResult;
      ELSE
         OPEN C_SUBSCRIBERS FOR
            SELECT *
              FROM (  SELECT DISTINCT 
                             s.customer_id,
                             s.subscriber_no,
                             s.sub_status,
                             nd.first_name,
                             nd.middle_initial,
                             nd.last_business_name,
                             pd.unit_esn,
                             s.product_type,
                             SUBSTR (s.dealer_code, 1, 10),
                             sa.soc,
                             s.email_address,
                             NVL (s.init_activation_date, s.effective_date),
                             s.init_activation_date,
                             SUBSTR (s.dealer_code, 11),
                             s.sub_status_rsn_code,
                             s.sub_status_last_act,
                             s.sub_alias,
                             DECODE (
                                INSTR (user_seg, '@'),
                                0, '',
                                SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                             sub_lang_pref,
                             s.next_ctn,
                             s.next_ctn_chg_date,
                             s.prv_ctn,
                             s.prv_ctn_chg_date,
                             DECODE (s.sub_status, 'S', 'B', s.sub_status),
                             s.tax_gst_exmp_ind,
                             s.tax_pst_exmp_ind,
                             s.tax_hst_exmp_ind,
                             s.tax_gst_exmp_eff_dt,
                             s.tax_pst_exmp_eff_dt,
                             s.tax_hst_exmp_eff_dt,
                             s.tax_gst_exmp_exp_dt,
                             s.tax_pst_exmp_exp_dt,
                             s.tax_hst_exmp_exp_dt,
                             s.tax_gst_exmp_rf_no,
                             s.tax_pst_exmp_rf_no,
                             s.tax_hst_exmp_rf_no,
                             s.sub_status_date,
                             s.calls_sort_order,
                             s.commit_reason_code,
                             s.commit_orig_no_month,
                             s.commit_start_date,
                             s.commit_end_date,
                             nd.name_suffix,
                             nd.additional_title,
                             nd.name_title,
                             s.hot_line_ind,
                             nd.name_format,
                             s.migration_type,
                             s.migration_date,
                             s.tenure_date,
                             s.req_deposit_amt,
                             s.port_type,
                             s.port_date,
                             s.brand_id,
                             NVL (s.external_id, 0) AS subscription_id,
                             s.seat_type,
                             s.seat_group
                        FROM subscriber s,
                             address_name_link anl,
                             name_data nd,
                             physical_device pd,
                             service_agreement sa,
                             logical_date ld
                       WHERE s.customer_id = i_ban
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
                                  OR sa.expiration_date IS NULL)
                             AND ld.logical_date_type = 'O'
                             AND sa.effective_date =
                                    (SELECT MIN (sa1.effective_date)
                                       FROM service_agreement sa1,
                                            logical_date ld
                                      WHERE     sa1.ban = sa.ban
                                            AND sa1.subscriber_no =
                                                   sa.subscriber_no
                                            AND sa1.product_type =
                                                   sa.product_type
                                            AND sa1.service_type = 'P'
                                            AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                                 OR sa1.expiration_date IS NULL)
                                            AND ld.logical_date_type = 'O')
                             AND pd.customer_id = s.customer_id
                             AND pd.subscriber_no = s.subscriber_no
                             AND pd.product_type = s.product_type
                             AND pd.esn_level = 1
                             AND pd.expiration_date IS NULL
                    ORDER BY 24, 12 DESC)
             WHERE ROWNUM <= i_maximumResult;
      END IF;
   END getSubListByBAN;


   PROCEDURE getSubListByBANAndSubIDs (
      i_ban                 IN     NUMBER,
      a_subscriber_ids      IN     t_subscriber_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT NOCOPY VARCHAR2)
   IS
   BEGIN
      IF i_include_cancelled = numeric_true
      THEN
         OPEN C_SUBSCRIBERS FOR
            SELECT *
              FROM (SELECT DISTINCT
                           s.customer_id,
                           s.subscriber_no,
                           s.sub_status,
                           nd.first_name,
                           nd.middle_initial,
                           nd.last_business_name,
                           pd.unit_esn,
                           s.product_type,
                           SUBSTR (s.dealer_code, 1, 10),
                           sa.soc,
                           s.email_address,
                           NVL (s.init_activation_date, s.effective_date),
                           s.init_activation_date,
                           SUBSTR (s.dealer_code, 11),
                           s.sub_status_rsn_code,
                           s.sub_status_last_act,
                           s.sub_alias,
                           DECODE (
                              INSTR (user_seg, '@'),
                              0, '',
                              SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                           sub_lang_pref,
                           s.next_ctn,
                           s.next_ctn_chg_date,
                           s.prv_ctn,
                           s.prv_ctn_chg_date,
                           DECODE (s.sub_status, 'S', 'B', s.sub_status),
                           s.tax_gst_exmp_ind,
                           s.tax_pst_exmp_ind,
                           s.tax_hst_exmp_ind,
                           s.tax_gst_exmp_eff_dt,
                           s.tax_pst_exmp_eff_dt,
                           s.tax_hst_exmp_eff_dt,
                           s.tax_gst_exmp_exp_dt,
                           s.tax_pst_exmp_exp_dt,
                           s.tax_hst_exmp_exp_dt,
                           s.tax_gst_exmp_rf_no,
                           s.tax_pst_exmp_rf_no,
                           s.tax_hst_exmp_rf_no,
                           s.sub_status_date,
                           s.calls_sort_order,
                           s.commit_reason_code,
                           s.commit_orig_no_month,
                           s.commit_start_date,
                           s.commit_end_date,
                           nd.name_suffix,
                           nd.additional_title,
                           nd.name_title,
                           s.hot_line_ind,
                           nd.name_format,
                           s.migration_type,
                           s.migration_date,
                           s.tenure_date,
                           s.req_deposit_amt,
                           s.port_type,
                           s.port_date,
                           s.brand_id,
                           NVL (s.external_id, 0) AS subscription_id,
                           s.seat_type,
                           s.seat_group 
                      FROM subscriber s,
                           address_name_link anl,
                           name_data nd,
                           physical_device pd,
                           service_agreement sa
                     WHERE  s.customer_id = i_ban
                           AND (   s.subscriber_no IN
                                      (SELECT /*+ cardinality( t 1 ) */ *
                                         FROM TABLE (
                                                 CAST (
                                                    a_subscriber_ids AS t_subscriber_array)) t where rownum >= 0)
                                )
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
                                    WHERE     sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                 sa.subscriber_no
                                          AND sa1.product_type =
                                                 sa.product_type
                                          AND sa1.service_type = 'P')
                           AND pd.customer_id = s.customer_id
                           AND pd.subscriber_no = s.subscriber_no
                           AND pd.product_type = s.product_type
                           AND pd.esn_seq_no =
                                  (SELECT MAX (esn_seq_no)
                                     FROM physical_device pd1
                                    WHERE     pd1.customer_id =
                                                 pd.customer_id
                                          AND pd1.subscriber_no =
                                                 pd.subscriber_no
                                          AND pd1.product_type =
                                                 pd.product_type
                                          AND NVL (pd1.esn_level, 1) = 1)
                    UNION ALL
                    SELECT DISTINCT 
                           s.customer_id,
                           s.subscriber_no,
                           s.sub_status,
                           nd.first_name,
                           nd.middle_initial,
                           nd.last_business_name,
                           pd.unit_esn,
                           s.product_type,
                           SUBSTR (s.dealer_code, 1, 10),
                           sa.soc,
                           s.email_address,
                           NVL (s.init_activation_date, s.effective_date),
                           s.init_activation_date,
                           SUBSTR (s.dealer_code, 11),
                           s.sub_status_rsn_code,
                           s.sub_status_last_act,
                           s.sub_alias,
                           DECODE (
                              INSTR (user_seg, '@'),
                              0, '',
                              SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                           sub_lang_pref,
                           s.next_ctn,
                           s.next_ctn_chg_date,
                           s.prv_ctn,
                           s.prv_ctn_chg_date,
                           DECODE (s.sub_status, 'S', 'B', s.sub_status),
                           s.tax_gst_exmp_ind,
                           s.tax_pst_exmp_ind,
                           s.tax_hst_exmp_ind,
                           s.tax_gst_exmp_eff_dt,
                           s.tax_pst_exmp_eff_dt,
                           s.tax_hst_exmp_eff_dt,
                           s.tax_gst_exmp_exp_dt,
                           s.tax_pst_exmp_exp_dt,
                           s.tax_hst_exmp_exp_dt,
                           s.tax_gst_exmp_rf_no,
                           s.tax_pst_exmp_rf_no,
                           s.tax_hst_exmp_rf_no,
                           s.sub_status_date,
                           s.calls_sort_order,
                           s.commit_reason_code,
                           s.commit_orig_no_month,
                           s.commit_start_date,
                           s.commit_end_date,
                           nd.name_suffix,
                           nd.additional_title,
                           nd.name_title,
                           s.hot_line_ind,
                           nd.name_format,
                           s.migration_type,
                           s.migration_date,
                           s.tenure_date,
                           s.req_deposit_amt,
                           s.port_type,
                           s.port_date,
                           s.brand_id,
                           NVL (s.external_id, 0) AS subscription_id,                           
                           s.seat_type,
                           s.seat_group 
                      FROM subscriber s,
                           address_name_link anl,
                           name_data nd,
                           physical_device pd,
                           service_agreement sa,
                           logical_date ld
                     WHERE  s.customer_id = i_ban
                           AND (   s.subscriber_no IN
                                      (SELECT /*+ cardinality( t 1 ) */ *
                                         FROM TABLE (
                                                 CAST (
                                                    a_subscriber_ids AS t_subscriber_array)) t where rownum >= 0)
                                )
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
                                OR sa.expiration_date IS NULL)
                           AND ld.logical_date_type = 'O'
                           AND sa.effective_date =
                                  (SELECT MIN (sa1.effective_date)
                                     FROM service_agreement sa1,
                                          logical_date ld
                                    WHERE     sa1.ban = sa.ban
                                          AND sa1.subscriber_no =
                                                 sa.subscriber_no
                                          AND sa1.product_type =
                                                 sa.product_type
                                          AND sa1.service_type = 'P'
                                          AND (   TRUNC (sa1.expiration_date) >
                                                     TRUNC (ld.logical_date)
                                               OR sa1.expiration_date IS NULL)
                                          AND ld.logical_date_type = 'O')
                           AND pd.customer_id = s.customer_id
                           AND pd.subscriber_no = s.subscriber_no
                           AND pd.product_type = s.product_type
                           AND pd.esn_level = 1
                           AND pd.expiration_date IS NULL
                    ORDER BY 24, 12 DESC)
             WHERE ROWNUM <= i_maximumResult;
      ELSE
         OPEN C_SUBSCRIBERS FOR
            SELECT *
              FROM (  SELECT DISTINCT 
                             s.customer_id,
                             s.subscriber_no,
                             s.sub_status,
                             nd.first_name,
                             nd.middle_initial,
                             nd.last_business_name,
                             pd.unit_esn,
                             s.product_type,
                             SUBSTR (s.dealer_code, 1, 10),
                             sa.soc,
                             s.email_address,
                             NVL (s.init_activation_date, s.effective_date),
                             s.init_activation_date,
                             SUBSTR (s.dealer_code, 11),
                             s.sub_status_rsn_code,
                             s.sub_status_last_act,
                             s.sub_alias,
                             DECODE (
                                INSTR (user_seg, '@'),
                                0, '',
                                SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)),
                             sub_lang_pref,
                             s.next_ctn,
                             s.next_ctn_chg_date,
                             s.prv_ctn,
                             s.prv_ctn_chg_date,
                             DECODE (s.sub_status, 'S', 'B', s.sub_status),
                             s.tax_gst_exmp_ind,
                             s.tax_pst_exmp_ind,
                             s.tax_hst_exmp_ind,
                             s.tax_gst_exmp_eff_dt,
                             s.tax_pst_exmp_eff_dt,
                             s.tax_hst_exmp_eff_dt,
                             s.tax_gst_exmp_exp_dt,
                             s.tax_pst_exmp_exp_dt,
                             s.tax_hst_exmp_exp_dt,
                             s.tax_gst_exmp_rf_no,
                             s.tax_pst_exmp_rf_no,
                             s.tax_hst_exmp_rf_no,
                             s.sub_status_date,
                             s.calls_sort_order,
                             s.commit_reason_code,
                             s.commit_orig_no_month,
                             s.commit_start_date,
                             s.commit_end_date,
                             nd.name_suffix,
                             nd.additional_title,
                             nd.name_title,
                             s.hot_line_ind,
                             nd.name_format,
                             s.migration_type,
                             s.migration_date,
                             s.tenure_date,
                             s.req_deposit_amt,
                             s.port_type,
                             s.port_date,
                             s.brand_id,
                             NVL (s.external_id, 0) AS subscription_id,
                             s.seat_type,
                             s.seat_group 
                        FROM subscriber s,
                             address_name_link anl,
                             name_data nd,
                             physical_device pd,
                             service_agreement sa,
                             logical_date ld
                       WHERE s.customer_id = i_ban
                             AND (   s.subscriber_no IN
                                        (SELECT /*+ cardinality( t 1 ) */*
                                           FROM TABLE (
                                                   CAST (
                                                      a_subscriber_ids AS t_subscriber_array)) t where rownum >= 0)
                                 )
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
                                  OR sa.expiration_date IS NULL)
                             AND ld.logical_date_type = 'O'
                             AND sa.effective_date =
                                    (SELECT MIN (sa1.effective_date)
                                       FROM service_agreement sa1,
                                            logical_date ld
                                      WHERE     sa1.ban = sa.ban
                                            AND sa1.subscriber_no =
                                                   sa.subscriber_no
                                            AND sa1.product_type =
                                                   sa.product_type
                                            AND sa1.service_type = 'P'
                                            AND (   TRUNC (sa1.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                                 OR sa1.expiration_date IS NULL)
                                            AND ld.logical_date_type = 'O')
                             AND pd.customer_id = s.customer_id
                             AND pd.subscriber_no = s.subscriber_no
                             AND pd.product_type = s.product_type
                             AND pd.esn_level = 1
                             AND pd.expiration_date IS NULL
                    ORDER BY 24, 12 DESC)
             WHERE ROWNUM <= i_maximumResult;
      END IF;
   END getSubListByBANAndSubIDs;
   

   PROCEDURE getSubListByBANAndSubID (i_ban                 IN     NUMBER,
                                      v_subscriber_id       IN     VARCHAR2,
                                      i_include_cancelled   IN     NUMBER,
                                      i_maximumResult       IN     NUMBER,
                                      c_subscribers            OUT refcursor,
                                      v_error_message          OUT VARCHAR2)
   IS
   	a_subscriber_ids   t_subscriber_array;
   BEGIN
      IF i_ban > 0 AND LENGTH (RTRIM (LTRIM (v_subscriber_id))) > 0
      THEN
      	 a_subscriber_ids := t_subscriber_array (v_subscriber_id);
         getSubListByBANAndSubIDs (i_ban,
                                     a_subscriber_ids,
                                     i_include_cancelled,
                                     i_maximumResult,
                                     c_subscribers,
                                     v_error_message);
      END IF;
   END getSubListByBANAndSubID;

   PROCEDURE getSubListBySubID (v_subscriber_id       IN     VARCHAR2,
                                i_include_cancelled   IN     NUMBER,
                                i_maximumResult       IN     NUMBER,
                                c_subscribers            OUT refcursor,
                                v_error_message          OUT VARCHAR2)
   IS
   a_subscriber_ids   t_subscriber_array;
   BEGIN
      IF     v_subscriber_id IS NOT NULL
         AND LENGTH (RTRIM (LTRIM (v_subscriber_id))) > 0
      THEN
      	a_subscriber_ids := t_subscriber_array (v_subscriber_id);
         getSubListBySubIDs (a_subscriber_ids,
                             i_include_cancelled,
                             i_maximumResult,
                             c_subscribers,
                             v_error_message);
      END IF;
   END getSubListBySubID;
   
   	PROCEDURE getSubListBySubIDs (
      a_subscriber_ids      IN     t_subscriber_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2)
    IS
    BEGIN
	     IF a_subscriber_ids IS NOT NULL AND a_subscriber_ids.COUNT > 0
         THEN
            IF i_include_cancelled = numeric_true
            THEN
               OPEN c_subscribers
                FOR
                   SELECT   /*+rule*/ s.customer_id, s.subscriber_no, s.sub_status,
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
                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type, s.seat_group 
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
                               (SELECT /*+rule*/ MAX (sa1.soc_seq_no)
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
                   UNION ALL
                   SELECT   /*+rule*/ s.customer_id, s.subscriber_no, s.sub_status,
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
                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type,s.seat_group 
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
                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type, s.seat_group 
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
         ELSE
            v_error_message := err_invalid_input;
         END IF;
	END getSubListBySubIDs;

   FUNCTION getSubByBANandPhoneNumber (
      i_ban                 IN     NUMBER,
      v_phone_number        IN     VARCHAR2,
      i_include_cancelled   IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2)
      RETURN NUMBER
   IS
   BEGIN
      RETURN getSubListByBanAndPhoneNumber (i_ban, v_phone_number, i_include_cancelled, 1, c_subscribers, v_error_message);
   END getSubByBANandPhoneNumber;


   FUNCTION getSubListByPhoneNumbers (
      a_phone_numbers       IN     t_phone_num_array,
      i_include_cancelled   IN     NUMBER,
      i_maximumResult       IN     NUMBER,
      c_subscribers            OUT refcursor,
      v_error_message          OUT VARCHAR2)
   RETURN NUMBER
   IS
    i_result    NUMBER (1);
     a_sub_ids   t_subscriber_array;
   BEGIN
       i_result :=
         SUB_ATTRIB_RETRIEVAL_PKG.getSubIDListByPhoneNumbers (a_phone_numbers,
                                  i_include_cancelled,
                                  a_sub_ids,
                                  v_error_message
                                 );

      IF i_result = numeric_true THEN
         getSubListBySubIDs (a_sub_ids,
                                        i_include_cancelled,
                                        i_maximumResult,
                                        c_subscribers,
                                        v_error_message
                                       );
      END IF;
      
      RETURN i_result;
   END getSubListByPhoneNumbers;
   
   FUNCTION getSubListByBanAndPhoneNumber (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      i_maximum             IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
   BEGIN
      i_result :=
         SUB_ATTRIB_RETRIEVAL_PKG.getSubIDListByBanAndPhone (i_ban,
                                       v_phone_number,
                                       i_include_cancelled,
                                       a_sub_ids,
                                       v_error_message
                                      );

      IF i_result = numeric_true
      THEN
       	getSubListByBANAndSubIDs (i_ban,
       						a_sub_ids,
                            i_include_cancelled,
                            i_maximum,
                            c_subscribers,
                            v_error_message
                            );
      END IF;
      
      RETURN i_result;

   END getSubListByBanAndPhoneNumber;
	
   FUNCTION getLwSubListByBan(
      i_ban                 IN       NUMBER,
      i_isIDEN              IN       NUMBER,
      i_include_cancelled   IN       NUMBER,
      i_maximumResult       IN       NUMBER,
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
               IF i_isIDEN = numeric_true THEN
                  -- mike , not cancelled
                  OPEN c_subscribers  FOR
                  SELECT * FROM (
                     SELECT DISTINCT
                          s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type,s.seat_type, s.seat_group,
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
                        AND (s.sub_status != 'C' OR i_include_cancelled = numeric_true)
                        ORDER BY sort1, sort2 DESC
                     ) WHERE ROWNUM <= i_maximumResult;
                ELSE
                  -- non mike, not cancelled: without subscriber_rsource table outer join
                  OPEN c_subscribers  FOR
                  SELECT * FROM (
                     SELECT 
                        DISTINCT
                          s.customer_id, s.subscriber_no, s.product_type, s.sub_status, s.network_type, s.seat_type, s.seat_group,
                          nd.first_name, nd.last_business_name
                          ,DECODE(s.sub_status, 'S', 'B', s.sub_status) sort1
                          ,NVL(s.init_activation_date, s.effective_date) sort2
                          , NVL(s.external_id, 0) external_id
                      FROM subscriber s
                        left outer join address_name_link anl on anl.ban = s.customer_id and anl.subscriber_no = s.subscriber_no AND anl.expiration_date IS NULL
                        left outer join name_data nd on nd.name_id = anl.name_id
                      WHERE
                        s.customer_id = i_ban
                         AND (s.sub_status != 'C' OR i_include_cancelled = numeric_true)
                        ORDER BY sort1, sort2 DESC
                      ) WHERE ROWNUM <= i_maximumResult;
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
   END getLwSubListByBan;
   
    FUNCTION getSubListByBanAndSeatNumber (
      i_ban                 IN       NUMBER,
      i_seat_number         IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      i_maximum             IN       NUMBER,
      c_subscribers         OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result    NUMBER (1);
      a_sub_ids   t_subscriber_array;
   BEGIN
      i_result := SUB_ATTRIB_RETRIEVAL_PKG.getSubIdsByBanAndOrSeatNum(i_ban,
                                       i_seat_number,
                                       i_include_cancelled,
                                       a_sub_ids,
                                       v_error_message
                                      );

     IF i_result = numeric_true
      THEN
            IF(i_ban is NULL OR i_ban = 0) THEN
            getSubListBySubIDs( a_sub_ids,
                            i_include_cancelled,
                            i_maximum,
                            c_subscribers,
                            v_error_message
                            );
            ELSE
            getSubListByBANAndSubIDs (i_ban,
                               a_sub_ids,
                            i_include_cancelled,
                            i_maximum,
                            c_subscribers,
                            v_error_message
                            );
           END IF;
      END IF;
      
      RETURN i_result;

   END getSubListByBanAndSeatNumber;
   
     PROCEDURE getResourceByBanAndOrPhoneNum (
      i_ban                 IN       NUMBER,
      i_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_resource        OUT      REFCURSOR
   )
     IS
     BEGIN
         OPEN c_sub_resource
         FOR
            SELECT DISTINCT sr.resource_type,sr.resource_number,sr.resource_status, DECODE (sr.resource_status, 'S', 'B', sr.resource_status) decodedStatus 
             FROM subscriber s2,subscriber_rsource sr 
	     	WHERE sr.subscriber_no = i_phone_no AND (ban = i_ban OR i_ban is NULL OR i_ban = 0)
	     	AND (resource_status != 'C' OR i_include_cancelled = numeric_true) AND s2.subscriber_no = sr.subscriber_no and sr.RECORD_EXP_DATE=TO_DATE( '47001231000000' ,  'YYYYMMDDHH24MISS' )
	     	 ORDER BY decodedStatus, 1 DESC;           
        EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                IF c_sub_resource%ISOPEN THEN
                    CLOSE c_sub_resource;
                END IF;
             WHEN OTHERS
             THEN
                   IF c_sub_resource%ISOPEN THEN
                    CLOSE c_sub_resource;
                END IF;
     END getResourceByBanAndOrPhoneNum; 
     

END SUB_RETRIEVAL_PKG;
/

SHO ERR