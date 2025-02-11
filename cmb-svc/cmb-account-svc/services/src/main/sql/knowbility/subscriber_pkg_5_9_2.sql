CREATE OR REPLACE PACKAGE SUBSCRIBER_PKG AS

------------------------------------------------------------------------
-- description: Package SUBSCRIBER_PKG contains procedures and functions
--				for subscriber-related data retrieval from Knowbility 
--				database.
--
-- Date	   		Developer     	  		Modifications
-- 08-03-2005	Vladimir Tsitrin		created
-------------------------------------------------------------------------

TYPE RefCursor IS REF CURSOR;

-- search constants
SEARCH_ALL				CONSTANT	VARCHAR2(1) := '*';

-- result constants
NUMERIC_TRUE			CONSTANT	NUMBER(1) := 0;
NUMERIC_FALSE			CONSTANT	NUMBER(1) := 1;

-- error messages
ERR_INVALID_INPUT		CONSTANT    VARCHAR2(100) := 'Input parameters are invalid or NULL.';
ERR_NO_DATA_FOUND		CONSTANT    VARCHAR2(100) := 'No data found.';
ERR_OTHER       		CONSTANT    VARCHAR2(100) := 'Other PL/SQL error.';

-- absolute maximum for the number of accounts to be retrieved
MAX_MAXIMORUM			CONSTANT	NUMBER(4) := 1000;


------------------------------------------------------------------------------
-- description: Procedure GetSubscribersByID returns a cursor of subscribers
--              with different ESN numbers for a given subscriber ID.
------------------------------------------------------------------------------
FUNCTION GetSubscriberListByID(
		v_Subscriber_ID      IN      VARCHAR2,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;

FUNCTION GetSubscriberListByESN(
		v_Serial_No          IN      VARCHAR2,
		i_Include_Cancelled  IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;		
		
FUNCTION GetSubscriberListByBAN(
		i_BAN                IN      NUMBER,
		v_Subscriber_ID      IN      VARCHAR2,
		i_Include_Cancelled  IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;				
		
FUNCTION GetSubscriberListByBANAndFleet(
		i_BAN                IN      NUMBER,
		i_Urban_ID           IN      NUMBER,
		i_Fleet_ID           IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;				
		
FUNCTION GetSubscriberListByBANAndTG(
		i_BAN                IN      NUMBER,
		i_Urban_ID           IN      NUMBER,
		i_Fleet_ID           IN      NUMBER,
		i_Talk_Group         IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;						
				
FUNCTION GetSecondaryESNs(
		v_Subscriber_ID      IN      VARCHAR2,
   		c_SerialNumbers      OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) RETURN NUMBER;		

End;
/

CREATE OR REPLACE PACKAGE BODY SUBSCRIBER_PKG AS

	FUNCTION GetSubscriberListByID(
		v_Subscriber_ID      IN      VARCHAR2,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		   
		RETURN NUMBER IS

		i_Result                     NUMBER(1);
		
		v_Cursor_Text                VARCHAR2(32767);
		
	BEGIN
		BEGIN
			IF v_Subscriber_ID IS NOT NULL AND LENGTH(RTRIM(LTRIM(v_Subscriber_ID))) > 0 THEN
				v_Cursor_Text := 'SELECT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, nd.middle_initial, '
				              || '       nd.last_business_name, pd.unit_esn, s.product_type, substr(s.dealer_code, 1, 10), '
				              || '       sa.soc, s.email_address, nvl(s.init_activation_date, s.effective_date), '
				              || '       s.init_activation_date, substr(s.dealer_code, 11), s.sub_status_rsn_code, '
				              || '       s.sub_status_last_act, s.sub_alias, decode(instr(user_seg, ''@''), 0, '''', substr(user_seg, instr(user_seg, ''@'') + 1, 1)), '
	                          || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
	                          || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
	                          || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
	                          || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
	                          || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
	                          || '       s.sub_status_date, s.calls_sort_order, pd.esn_level '
	                          || '  FROM subscriber s, '
	                          || '       address_name_link anl, '
	                          || '       name_data nd, '
	                          || '       physical_device pd, '
	                          || '       service_agreement sa, '
	                          || '       logical_date ld '
	                          || ' WHERE s.subscriber_no = ''' || v_Subscriber_ID || ''''
                              || '   AND s.product_type IN (''C'' ,''P'', ''I'') '
                              || '   AND s.sub_status != ''C'' '
                              || '   AND anl.ban(+) = s.customer_id '
                              || '   AND anl.subscriber_no(+) = s.subscriber_no '
                              || '   AND anl.expiration_date IS NULL '
                              || '   AND nd.name_id(+) = anl.name_id '
                              || '   AND sa.ban = s.customer_id '
                              || '   AND sa.subscriber_no = s.subscriber_no '
                              || '   AND sa.product_type = s.product_type '
                              || '   AND sa.service_type = ''P'' '
                              || '   AND sa.effective_date = (SELECT MIN(sa1.effective_date) '
                              || '                              FROM service_agreement sa1 '
                              || '                             WHERE sa1.ban = sa.ban '
                              || '                               AND sa1.subscriber_no = sa.subscriber_no '
                              || '                               AND sa1.product_type = sa.product_type '
                              || '                               AND sa1.service_type = ''P'' '
                              || '                               AND (TRUNC(sa1.expiration_date) > TRUNC(ld.logical_date) OR sa1.expiration_date IS NULL) '
                              || '                               AND ld.logical_date_type = ''O'') '
                              || '   AND (TRUNC(sa.expiration_date) > TRUNC(ld.logical_date) OR sa.expiration_date IS NULL) '
                              || '   AND ld.logical_date_type = ''O'' '
                              || '   AND pd.customer_id = s.customer_id '
                              || '   AND pd.subscriber_no = s.subscriber_no '
                              || '   AND pd.product_type = s.product_type '
                              || '   AND pd.esn_level IS NOT NULL '
                              || '   AND pd.expiration_date IS NULL '
                              || '   ORDER BY pd.esn_level ASC, sa.effective_date DESC ';
				
				OPEN c_Subscribers FOR v_Cursor_Text;
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSubscriberListByID;
	

	FUNCTION GetSubscriberListByESN(
		v_Serial_No          IN      VARCHAR2,
		i_Include_Cancelled  IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		
		RETURN NUMBER IS
		
		i_Result                     NUMBER(1);
		
		v_Cursor_Text                VARCHAR2(32767);
     	v_Select_List                VARCHAR2(1000);
		
	BEGIN
		BEGIN
			IF v_Serial_No IS NOT NULL AND LENGTH(RTRIM(LTRIM(v_Serial_No))) > 0 THEN
				v_Select_List := 'SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, '
                              || '       nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, '
	                          || '       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, '
	                          || '       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, '
	                          || '       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, '
	                          || '       s.sub_alias, DECODE(INSTR(user_seg, ''@''), 0, '''', SUBSTR(user_seg, INSTR(user_seg, ''@'') + 1, 1)), '
	                          || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
 	                          || '       DECODE(s.sub_status, ''S'', ''B'', s.sub_status), DECODE(pd.unit_esn, ''' || v_Serial_No || ''' , 1, 2), '
	                          || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
	                          || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
	                          || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
	                          || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
	                          || '       s.sub_status_date, s.calls_sort_order ';
    			
    			v_Cursor_Text := v_Select_List
    			              || '  FROM physical_device pd, '
							  || '       subscriber s, '
							  || '       address_name_link anl, '
							  || '       name_data nd, '
							  || '       service_agreement sa, '
							  || '       logical_date ld '
							  || ' WHERE pd.unit_esn = ''' || v_Serial_No || ''' ' 
							  || '   AND pd.expiration_date IS NULL '
							  || '   AND s.subscriber_no = pd.subscriber_no '
							  || '   AND s.product_type = pd.product_type '
							  || '   AND s.customer_id = pd.customer_id '
							  || '   AND anl.ban(+) = s.customer_id '
							  || '   AND anl.subscriber_no(+) = s.subscriber_no '
							  || '   AND anl.expiration_date IS NULL '
							  || '   AND nd.name_id(+) = anl.name_id '
							  || '   AND sa.ban = s.customer_id '
							  || '   AND sa.subscriber_no = s.subscriber_no '
							  || '   AND sa.service_type = ''P'' '
							  || '   AND (TRUNC(sa.expiration_date) > TRUNC(ld.logical_date) OR sa.expiration_date IS NULL) '
							  || '   AND ld.logical_date_type = ''O'' '
							  || '   AND sa.effective_date = (SELECT MIN(sa1.effective_date) '
							  || '                              FROM service_agreement sa1 '
							  || '                             WHERE sa1.ban = sa.ban '
							  || '                               AND sa1.subscriber_no = sa.subscriber_no '
							  || '                               AND sa1.product_type = sa.product_type '
							  || '                               AND sa1.service_type = ''P'' '
							  || '                               AND (TRUNC(sa1.expiration_date) > TRUNC(ld.logical_date) OR sa1.expiration_date IS NULL) '
							  || '                               AND ld.logical_date_type = ''O'') ';
							  
				IF i_Include_Cancelled = NUMERIC_TRUE THEN
					v_Cursor_Text := v_Cursor_Text 
					              || ' UNION '
					              || v_Select_List
					              || '  FROM physical_device pd, '
					              || '       subscriber s, '
					              || '       address_name_link anl, '
					              || '       name_data nd, '
					              || '       service_agreement sa, '
					              || '       physical_device pd_2 '
					              || ' WHERE pd.unit_esn = ''' || v_Serial_No || ''' '
					              || '   AND pd.expiration_date IS NOT NULL '
								  || '   AND s.subscriber_no = pd.subscriber_no '
								  || '   AND s.product_type = pd.product_type '
								  || '   AND s.customer_id = pd.customer_id '
								  || '   AND anl.ban(+) = s.customer_id '
								  || '   AND anl.subscriber_no(+) = s.subscriber_no '
								  || '   AND anl.expiration_date IS NULL '
								  || '   AND nd.name_id(+) = anl.name_id '
								  || '   AND sa.ban = s.customer_id '
								  || '   AND sa.subscriber_no = s.subscriber_no '
								  || '   AND sa.service_type = ''P'' '
								  || '   AND sa.soc_seq_no = (SELECT MAX(sa1.soc_seq_no) '
								  || '                          FROM service_agreement sa1 '
								  || '                         WHERE sa1.ban = sa.ban '
								  || '                           AND sa1.subscriber_no = sa.subscriber_no '
								  || '                           AND sa1.product_type = sa.product_type '
								  || '                           AND sa1.service_type = ''P'') '
								  || '   AND pd_2.customer_id = s.customer_id '
								  || '   AND pd_2.subscriber_no = s.subscriber_no '
								  || '   AND pd_2.product_type = s.product_type '
								  || '   AND pd_2.esn_level = 1 '
								  || '   AND pd_2.esn_seq_no = (SELECT MAX(esn_seq_no) '
								  || '                            FROM physical_device pd1 '
								  || '                           WHERE pd1.customer_id = pd_2.customer_id '
								  || '                             AND pd1.subscriber_no = pd_2.subscriber_no '
								  || '                             AND pd1.product_type = pd_2.product_type '
								  || '                             AND pd1.esn_level = 1) '
								  || ' ORDER BY 25, 24, 12 DESC';
					              
				END IF;			  
				
				OPEN c_Subscribers FOR v_Cursor_Text;
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSubscriberListByESN;					


	FUNCTION GetSubscriberListByBAN(
		i_BAN                IN      NUMBER,
		v_Subscriber_ID      IN      VARCHAR2,
		i_Include_Cancelled  IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		
		RETURN NUMBER IS
		
		i_Result                     NUMBER(1);
		i_Ignore_BAN                 NUMBER(1);
		i_Ignore_Sub                 NUMBER(1);

		v_Cursor_Text                VARCHAR2(32767);
     	v_Select_List                VARCHAR2(1000);
		
	BEGIN
		BEGIN
			IF i_BAN IS NOT NULL AND i_BAN > 0 THEN
				i_Ignore_BAN := NUMERIC_FALSE;
			ELSE
				i_Ignore_BAN := NUMERIC_TRUE;
			END IF;
			
			IF v_Subscriber_ID IS NOT NULL AND LENGTH(RTRIM(LTRIM(v_Subscriber_ID))) > 0 THEN
				i_Ignore_Sub := NUMERIC_FALSE;
			ELSE
				i_Ignore_Sub := NUMERIC_TRUE;
			END IF;
		
			IF i_Ignore_BAN = NUMERIC_FALSE OR i_Ignore_Sub = NUMERIC_FALSE THEN
				v_Select_List := 'SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, '
                              || '       nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, '
	                          || '       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, '
	                          || '       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, '
	                          || '       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, '
	                          || '       s.sub_alias, DECODE(INSTR(user_seg, ''@''), 0, '''', SUBSTR(user_seg, INSTR(user_seg, ''@'') + 1, 1)), '
	                          || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
 	                          || '       DECODE(s.sub_status, ''S'', ''B'', s.sub_status), '
	                          || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
	                          || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
	                          || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
	                          || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
	                          || '       s.sub_status_date, s.calls_sort_order ';
			
    			IF i_Include_Cancelled = NUMERIC_TRUE THEN
    				v_Cursor_Text := v_Select_List
    				              || ' FROM subscriber s, '
                                  || ' address_name_link anl, '
	                              || ' name_data nd, '
	                              || ' physical_device pd, '
	                              || ' service_agreement sa ';
	                              
	                IF (i_Ignore_BAN = NUMERIC_FALSE) THEN
						v_Cursor_Text := v_Cursor_Text
						              || 'WHERE s.customer_id = ' || i_BAN;
						              
						IF (i_Ignore_Sub = NUMERIC_FALSE) THEN
							v_Cursor_Text := v_Cursor_Text
						    	          || 'AND s.subscriber_no = ' || v_Subscriber_ID;
						END IF;              
					ELSE
						v_Cursor_Text := v_Cursor_Text
					    	          || 'WHERE s.subscriber_no = ' || v_Subscriber_ID;
					END IF;
					
					v_Cursor_Text := v_Cursor_Text 
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
								  || '                         AND pd1.product_type = pd.product_type) '
								  || ' UNION ';
				END IF;
				
				v_Cursor_Text := v_Cursor_Text || v_Select_List 
				              || '  FROM subscriber s, '
				              || '       address_name_link anl, '
				              || '       name_data nd, '
				              || '       physical_device pd, '
				              || '       service_agreement sa, '
				              || '       logical_date ld ';
				
				IF (i_Ignore_BAN = NUMERIC_FALSE) THEN
					v_Cursor_Text := v_Cursor_Text
					              || 'WHERE s.customer_id = ' || i_BAN;
					              
					IF (i_Ignore_Sub = NUMERIC_FALSE) THEN
						v_Cursor_Text := v_Cursor_Text
					    	          || 'AND s.subscriber_no = ' || v_Subscriber_ID;
					END IF;              
				ELSE
					v_Cursor_Text := v_Cursor_Text
				    	          || 'WHERE s.subscriber_no = ' || v_Subscriber_ID;
				END IF;
					              
				v_Cursor_Text := v_Cursor_Text 
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
				              || '                             AND (TRUNC(sa1.expiration_date) > TRUNC(ld.logical_date) OR sa1.expiration_date IS NULL) '
				              || '                             AND ld.logical_date_type = ''O'') '
				              || ' AND pd.customer_id = s.customer_id '
				              || ' AND pd.subscriber_no = s.subscriber_no '
				              || ' AND pd.product_type = s.product_type '
				              || ' AND pd.esn_level = 1 '
				              || ' AND pd.expiration_date IS NULL '
				              || ' ORDER BY 24, 12 DESC';
				              
				OPEN c_Subscribers FOR v_Cursor_Text;              
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSubscriberListByBAN;
	
	
	FUNCTION GetSubscriberListByBANAndFleet(
		i_BAN                IN      NUMBER,
		i_Urban_ID           IN      NUMBER,
		i_Fleet_ID           IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		
		RETURN NUMBER IS
		
		i_Result                     NUMBER(1);
		
		v_Cursor_Text                VARCHAR2(32767);
     	v_Select_List                VARCHAR2(1000);
		
	BEGIN
		BEGIN
			IF i_BAN IS NOT NULL AND i_Urban_ID IS NOT NULL AND i_Fleet_ID IS NOT NULL THEN
				v_Select_List := 'SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, '
                              || '       nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, '
	                          || '       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, '
	                          || '       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, '
	                          || '       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, '
	                          || '       s.sub_alias, DECODE(INSTR(user_seg, ''@''), 0, '''', SUBSTR(user_seg, INSTR(user_seg, ''@'') + 1, 1)), '
	                          || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
 	                          || '       DECODE(s.sub_status, ''S'', ''B'', s.sub_status), '
	                          || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
	                          || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
	                          || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
	                          || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
	                          || '       s.sub_status_date, s.calls_sort_order ';
			                  
			    v_Cursor_Text := v_Select_List
				              || '  FROM subscriber s, '
				              || '       address_name_link anl, '
				              || '       name_data nd, '
				              || '       physical_device pd, '
				              || '       service_agreement sa, '
				              || '       subscriber_rsource sr '
				              || ' WHERE sr.ban = ' || i_BAN 
				              || '   AND sr.urban_id = ' || i_Urban_ID
				              || '   AND sr.fleet_id = ' || i_Fleet_ID
				              || '   AND sr.resource_type = ''H'' '
				              || '   AND sr.resource_status = ''A'' '
				              || '   AND sr.resource_seq = (SELECT MAX(sr2.resource_seq) '
				              || '                            FROM subscriber_rsource sr2 '
				              || '                           WHERE sr2.subscriber_no = sr.subscriber_no '
				              || '                             AND sr2.resource_type = sr.resource_type '
				              || '                             AND sr2.ban = sr.ban) '
				              || '   AND sr.ban = s.customer_id '
				              || '   AND sr.subscriber_no = s.subscriber_no '
				              || '   AND s.sub_status != ''C'' '
				              || '   AND anl.ban(+) = s.customer_id '
				              || '   AND anl.subscriber_no(+) = s.subscriber_no '
				              || '   AND anl.expiration_date IS NULL '
				              || '   AND nd.name_id(+) = anl.name_id '
				              || '   AND sa.ban = s.customer_id '
				              || '   AND sa.subscriber_no = s.subscriber_no '
				              || '   AND sa.product_type = s.product_type '
				              || '   AND sa.service_type = ''P'' '
				              || '   AND sa.soc_seq_no = (SELECT MAX(sa1.soc_seq_no) '
				              || '                          FROM service_agreement sa1 '
				              || '                         WHERE sa1.ban = sa.ban '
				              || '                           AND sa1.subscriber_no = sa.subscriber_no '
				              || '                           AND sa1.product_type = sa.product_type '
				              || '                           AND sa1.service_type = ''P'') '
				              || '   AND pd.customer_id = s.customer_id '
				              || '   AND pd.subscriber_no = s.subscriber_no '
				              || '   AND pd.product_type = s.product_type '
				              || '   AND pd.esn_seq_no = (SELECT MAX(esn_seq_no) '
				              || '                          FROM physical_device pd1 '
				              || '                         WHERE pd1.customer_id = pd.customer_id '
				              || '                           AND pd1.subscriber_no = pd.subscriber_no '
				              || '                           AND pd1.product_type = pd.product_type) '
				              || ' ORDER BY 24, 12 DESC';
				
				OPEN c_Subscribers FOR v_Cursor_Text;
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSubscriberListByBANAndFleet;


	FUNCTION GetSubscriberListByBANAndTG(
		i_BAN                IN      NUMBER,
		i_Urban_ID           IN      NUMBER,
		i_Fleet_ID           IN      NUMBER,
		i_Talk_Group         IN      NUMBER,
   		c_Subscribers        OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		
		RETURN NUMBER IS
		
		i_Result                     NUMBER(1);
		
		v_Cursor_Text                VARCHAR2(32767);
     	v_Select_List                VARCHAR2(1000);
		
	BEGIN
		BEGIN
			IF i_BAN IS NOT NULL AND i_Urban_ID IS NOT NULL AND i_Fleet_ID IS NOT NULL THEN
				v_Select_List := 'SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, '
                              || '       nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, '
	                          || '       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, '
	                          || '       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, '
	                          || '       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, '
	                          || '       s.sub_alias, DECODE(INSTR(user_seg, ''@''), 0, '''', SUBSTR(user_seg, INSTR(user_seg, ''@'') + 1, 1)), '
	                          || '       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, '
 	                          || '       DECODE(s.sub_status, ''S'', ''B'', s.sub_status), '
	                          || '       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, '
	                          || '       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, '
	                          || '       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, '
	                          || '       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, '
	                          || '       s.sub_status_date, s.calls_sort_order ';
			
    			v_Cursor_Text := v_Select_List 
				              || '  FROM subscriber s, '
				              || '       address_name_link anl, '
				              || '       name_data nd, '
				              || '       physical_device pd, '
				              || '       service_agreement sa, '
				              || '       subscriber_tg_mtrx stm '
				              || ' WHERE stm.ban = ' || i_BAN
				              || '   AND stm.urban_id = ' || i_Urban_ID
				              || '   AND stm.fleet_id = ' || i_Fleet_ID
				              || '   AND stm.tg_id = ' || i_Talk_Group
				              || '   AND stm.ban = s.customer_id '
				              || '   AND stm.subscriber_no = s.subscriber_no '
				              || '   AND s.sub_status != ''C'' '
				              || '   AND anl.ban(+) = s.customer_id '
				              || '   AND anl.subscriber_no(+) = s.subscriber_no '
				              || '   AND anl.expiration_date IS NULL '
				              || '   AND nd.name_id(+) = anl.name_id '
				              || '   AND sa.ban = s.customer_id '
				              || '   AND sa.subscriber_no = s.subscriber_no '
				              || '   AND sa.product_type = s.product_type '
				              || '   AND sa.service_type = ''P'' '
				              || '   AND sa.soc_seq_no = (SELECT MAX(sa1.soc_seq_no) '
				              || '                          FROM service_agreement sa1 '
				              || '                         WHERE sa1.ban = sa.ban '
				              || '                           AND sa1.subscriber_no = sa.subscriber_no '
				              || '                           AND sa1.product_type = sa.product_type '
				              || '                           AND sa1.service_type = ''P'') '
				              || '   AND pd.customer_id = s.customer_id '
				              || '   AND pd.subscriber_no = s.subscriber_no '
				              || '   AND pd.product_type = s.product_type '
				              || '   AND pd.esn_seq_no = (SELECT MAX(esn_seq_no) '
				              || '                          FROM physical_device pd1 '
				              || '                         WHERE pd1.customer_id = pd.customer_id '
				              || '                           AND pd1.subscriber_no = pd.subscriber_no '
				              || '                           AND pd1.product_type = pd.product_type) '
				              || ' ORDER BY 24, 12 DESC';
		
				OPEN c_Subscribers FOR v_Cursor_Text;
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSubscriberListByBANAndTG;
	
	
	FUNCTION GetSecondaryESNs(
		v_Subscriber_ID      IN      VARCHAR2,
   		c_SerialNumbers      OUT     RefCursor,
		v_Error_Message      OUT     VARCHAR2) 
		   
		RETURN NUMBER IS

		i_Result                     NUMBER(1);
		
	BEGIN
		BEGIN
			IF v_Subscriber_ID IS NOT NULL AND LENGTH(RTRIM(LTRIM(v_Subscriber_ID))) > 0 THEN

    			OPEN c_SerialNumbers FOR
    			    SELECT unit_esn
                      FROM physical_device 
                     WHERE subscriber_no = v_Subscriber_ID
                       AND expiration_date IS NULL
                       AND NVL(esn_level, 1) <> 1;
				
    			i_Result := NUMERIC_TRUE;
     		ELSE
				v_Error_Message := ERR_INVALID_INPUT;
     			i_Result := NUMERIC_FALSE;
     		END IF;
        EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		v_Error_Message := ERR_NO_DATA_FOUND;
        		
        	WHEN OTHERS THEN
            	v_Error_Message := SQLERRM;
            	
            i_Result := NUMERIC_FALSE;
    	END;
    	
		RETURN i_Result;
	END GetSecondaryESNs;
	
END;
/

