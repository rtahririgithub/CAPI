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
             WHERE ROWNUM <= i_maximumResult

