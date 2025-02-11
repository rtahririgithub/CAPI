CREATE OR REPLACE package history_utility_pkg as
BANNotFound		Exception;
PRAGMA			EXCEPTION_INIT(BANNotFound, -20101);
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
FUNCTION GetBilledCredits 	(i_ban			  	IN	  	number
                            ,o_from_date		IN	  	date
                            ,o_to_date			IN	  	date
				 			,v_operator_id		IN		varchar2
							,v_subscriber_no	IN		varchar2
				 			,v_reason_code		IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)
							RETURN NUMBER;
FUNCTION GetUnbilledCredits (i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
							,v_operator_id		IN		varchar2
				 			,v_subscriber_no	IN		varchar2
				 			,v_reason_code		IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)
							RETURN NUMBER;
FUNCTION GetPendingCharges 	(i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
				 			,v_subscriber_no	IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)
							RETURN NUMBER;
FUNCTION GetLetterRequests 	(i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
				 			,v_subscriber_no	IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)
							RETURN NUMBER;
END;
/

CREATE OR REPLACE package body history_utility_pkg as

FUNCTION GetBilledCredits 	(i_ban			  	IN	  	number
                            ,o_from_date		IN	  	date
                            ,o_to_date			IN	  	date
				 			,v_operator_id		IN		varchar2
							,v_subscriber_no	IN		varchar2
				 			,v_reason_code		IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)

							RETURN NUMBER IS

	v_stmt					VARCHAR2(32767);
	v_stmt_sub_clause		VARCHAR2(32767);
	i_Result                NUMBER(1);
	i_max					number(4);
	i_count			NUMBER(4);

BEGIN
  BEGIN
  	   -- first count records
	   -- this is done since we cannot create temporary table and modified array of records to client
	   -- as we are basically opening the actual data cursor for the client to parse
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;
	i_max := i_max + 1;

	v_stmt := 'SELECT count(1) '
	  || ' FROM adjustment a, '
	  || ' bill b '
	  || ' WHERE   a.ban = ' || i_ban
	  || ' AND     a.actv_bill_seq_no is not null '
      || ' AND     a.adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' AND      a.ban = b.ban '
	  || ' AND      a.actv_bill_seq_no = b.bill_seq_no '
	  || ' AND      b.bill_conf_status = ''C''';

  	   -- subscriber clause
	IF i_is_ban_level = NUMERIC_TRUE and i_is_sub_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND a.subscriber_no is null ';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE AND v_subscriber_no IS NOT NULL THEN
	  v_stmt_sub_clause := ' AND a.subscriber_no = ''' || v_subscriber_no || '''';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND a.subscriber_no is not null ';
	ELSE
	  v_stmt_sub_clause := '';
	END IF;

	IF v_reason_code is not null THEN
	  v_stmt := v_stmt || ' AND a.actv_reason_code = ' || v_reason_code;
	END IF;

	IF v_operator_id is not null THEN
	  v_stmt := v_stmt || ' AND a.operator_id = ' || v_operator_id;
	END IF;

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    open o_credits for v_stmt;
	fetch o_credits into i_count;
	close o_credits;

	i_max := i_count;

	IF i_max > i_maximum THEN
	  i_has_more := NUMERIC_TRUE;
	ELSE
	  i_has_more := NUMERIC_FALSE;
    END IF;

  	   -- secondly perform actual query
    v_stmt := 'SELECT a.adj_creation_date, '
      || ' a.actv_date, '
      || ' a.actv_code, '
	  || ' a.actv_reason_code,'
	  || ' a.balance_impact_code, '
	  || ' a.subscriber_no,'
	  || ' a.product_type,'
	  || ' a.operator_id,'
	  || ' a.actv_amt, '
	  || ' a.tax_gst_amt, '
	  || ' a.tax_pst_amt, '
	  || ' a.tax_hst_amt, '
	  || ' a.soc, '
	  || ' a.feature_code,'
	  || ' a.ent_seq_no, '
	  || ' ''Y'', '
	  || ' a.bl_ignore_ind, '
	  || ' a.charge_seq_no '
	  || ' FROM adjustment a, '
	  || ' bill b '
	  || ' WHERE   a.ban = ' || i_ban
	  || ' AND     a.actv_bill_seq_no is not null '
      || ' AND     a.adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' AND      a.ban = b.ban '
	  || ' AND      a.actv_bill_seq_no = b.bill_seq_no '
	  || ' AND      b.bill_conf_status = ''C'' ';

	IF v_reason_code is not null THEN
	  v_stmt := v_stmt || ' AND a.actv_reason_code = ' || v_reason_code;
	END IF;

	IF v_operator_id is not null THEN
	  v_stmt := v_stmt || ' AND a.operator_id = ' || v_operator_id;
	END IF;

	v_stmt := v_stmt || v_stmt_sub_clause;

	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;

	v_stmt := v_stmt || ' AND rownum <= ' || i_max;
    open o_credits for v_stmt;
    i_Result := NUMERIC_TRUE;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_err_msg := ERR_NO_DATA_FOUND;

    WHEN OTHERS THEN
      v_err_msg := SQLERRM;

    i_Result := NUMERIC_FALSE;
  END;

  RETURN i_Result;
END GetBilledCredits;

FUNCTION GetUnbilledCredits (i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
							,v_operator_id		IN		varchar2
				 			,v_subscriber_no	IN		varchar2
				 			,v_reason_code		IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)

							RETURN NUMBER IS

	v_stmt		  			varchar2(32767);
	v_stmt_2				varchar2(32767);
	v_stmt_3				varchar2(32767);
	v_stmt_sub_clause		varchar2(32767);
	v_stmt_sub_clause_2		varchar2(32767);
	i_Result                NUMBER(1);
	i_max					number(4);
	i_count			NUMBER(4);
BEGIN
  BEGIN
  	   -- first count records
	   -- this is done since we cannot create temporary table and modified array of records to client
	   -- as we are basically opening the actual data cursor for the client to parse
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;
	i_max := i_max + 1;

    v_stmt := 'SELECT count(1) '
	  || ' FROM pending_adjustment '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND approval_status = ''P'' '
      || ' AND     actv_bill_seq_no is not null '
	  || ' AND     adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';

    v_stmt_2 := 'SELECT count(1) '
	  || ' FROM adjustment '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND     actv_bill_seq_no is null '
	  || ' AND     adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';

    v_stmt_3 := 'SELECT count(1) '
	  || ' FROM adjustment a, '
	  || ' bill b '
	  || ' WHERE   a.ban = ' || i_ban
	  || ' AND     a.adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' AND      a.ban = b.ban '
	  || ' AND      a.actv_bill_seq_no = b.bill_seq_no '
	  || ' AND      b.bill_conf_status = ''T'' ';

  	   -- subscriber clause
	IF i_is_ban_level = NUMERIC_TRUE and i_is_sub_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber_no is null ';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE AND v_subscriber_no IS NOT NULL THEN
	  v_stmt_sub_clause := ' AND subscriber_no = ''' || v_subscriber_no || '''';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber_no is not null ';
	ELSE
	  v_stmt_sub_clause := '';
	END IF;

	IF i_is_ban_level = NUMERIC_TRUE and i_is_sub_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause_2 := ' AND a.subscriber_no is null ';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE AND v_subscriber_no IS NOT NULL THEN
	  v_stmt_sub_clause_2 := ' AND a.subscriber_no = ''' || v_subscriber_no || '''';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause_2 := ' AND a.subscriber_no is not null ';
	ELSE
	  v_stmt_sub_clause_2 := '';
	END IF;

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt_2 := v_stmt_2 || v_stmt_sub_clause;
	v_stmt_3 := v_stmt_3 || v_stmt_sub_clause_2;

    IF v_operator_id is not null THEN
  	  v_stmt := v_stmt || ' AND operator_id = ' || v_operator_id
	    || ' AND rownum <= ' || i_max || ' union ' ||  v_stmt_2
	    || ' and operator_id = ' || v_operator_id
		|| ' AND rownum <= ' || i_max || ' union ' || v_stmt_3
		|| ' and a.operator_id = ' || v_operator_id || ' AND rownum <= ' || i_max;
  	ELSE
  	  v_stmt := v_stmt || ' AND rownum <= ' || (i_max + 1) || ' union ' ||  v_stmt_2
		|| ' AND rownum <= ' || i_max || ' union ' || v_stmt_3 || ' AND rownum <= ' || i_max;
	END IF;


    open o_credits for v_stmt;
	fetch o_credits into i_count;
	i_max := i_count;
fetch o_credits into i_count;
	i_max := i_max + i_count;
	fetch o_credits into i_count;
	i_max := i_max + i_count;
	close o_credits;

	IF i_max > i_maximum THEN
	  i_has_more := NUMERIC_TRUE;
	ELSE
	  i_has_more := NUMERIC_FALSE;
    END IF;

  	   -- secondly perform actual query
    v_stmt := 'SELECT adj_creation_date, '
      || ' effective_date, '
      || ' actv_code, '
	  || ' actv_reason_code,'
	  || ' balance_impact_code, '
	  || ' subscriber_no,'
	  || ' product_type,'
	  || ' operator_id,'
	  || ' actv_amt, '
	  || ' tax_gst_amt, '
	  || ' tax_pst_amt, '
	  || ' tax_hst_amt, '
	  || ' soc, '
	  || ' feature_code,'
	  || ' ent_seq_no, '
	  || ' approval_status, '
	  || ' bl_ignore_ind, '
	  || ' charge_seq_no '
	  || ' FROM pending_adjustment '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND approval_status = ''P'' '
      || ' AND     actv_bill_seq_no is not null '
	  || ' AND     adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';

    v_stmt_2 := 'SELECT adj_creation_date, '
      || ' effective_date, '
      || ' actv_code, '
	  || ' actv_reason_code,'
	  || ' balance_impact_code, '
	  || ' subscriber_no,'
	  || ' product_type,'
	  || ' operator_id,'
	  || ' actv_amt, '
	  || ' tax_gst_amt, '
	  || ' tax_pst_amt, '
	  || ' tax_hst_amt, '
	  || ' soc, '
	  || ' feature_code,'
	  || ' ent_seq_no, '
	  || ' ''Y'', '
	  || ' bl_ignore_ind, '
	  || ' charge_seq_no '
	  || ' FROM adjustment '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND     actv_bill_seq_no is null '
	  || ' AND     adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';

    v_stmt_3 := 'SELECT a.adj_creation_date, '
      || ' a.effective_date, '
      || ' a.actv_code, '
	  || ' a.actv_reason_code,'
	  || ' a.balance_impact_code, '
	  || ' a.subscriber_no,'
	  || ' a.product_type,'
	  || ' a.operator_id,'
	  || ' a.actv_amt, '
	  || ' a.tax_gst_amt, '
	  || ' a.tax_pst_amt, '
	  || ' a.tax_hst_amt, '
	  || ' a.soc, '
	  || ' a.feature_code,'
	  || ' a.ent_seq_no, '
	  || ' ''Y'', '
	  || ' a.bl_ignore_ind, '
	  || ' a.charge_seq_no '
	  || ' FROM adjustment a, '
	  || ' bill b '
	  || ' WHERE   a.ban = ' || i_ban
	  || ' AND     a.adj_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' AND      a.ban = b.ban '
	  || ' AND      a.actv_bill_seq_no = b.bill_seq_no '
	  || ' AND      b.bill_conf_status = ''T'' ';

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt_2 := v_stmt_2 || v_stmt_sub_clause;
	v_stmt_3 := v_stmt_3 || v_stmt_sub_clause_2;

	IF v_reason_code is not null THEN
	  v_stmt := v_stmt || ' AND reason_code = ' || v_reason_code;
	  v_stmt_2 := v_stmt_2 || ' AND reason_code = ' || v_subscriber_no;
	  v_stmt_3 := v_stmt_3 || ' AND a.reason_code = ' || v_subscriber_no;
    END IF;


	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;

    IF v_operator_id is not null THEN
  	  v_stmt := v_stmt || ' AND operator_id = ' || v_operator_id
	    || ' AND rownum <= ' || i_max || ' union ' ||  v_stmt_2
	    || ' and operator_id = ' || v_operator_id
		|| ' AND rownum <= ' || i_max || ' union ' || v_stmt_3
		|| ' and a.operator_id = ' || v_operator_id || ' AND rownum <= ' || i_max;
  	ELSE
  	  v_stmt := v_stmt
		|| ' AND rownum <= ' || i_max || ' union ' ||  v_stmt_2
		|| ' AND rownum <= ' || i_max || ' union ' || v_stmt_3;
	END IF;


    open o_credits for v_stmt;

    i_Result := NUMERIC_TRUE;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_err_msg := ERR_NO_DATA_FOUND;

    WHEN OTHERS THEN
      v_err_msg := SQLERRM;

    i_Result := NUMERIC_FALSE;
  END;

  RETURN i_Result;
END GetUnbilledCredits;

FUNCTION GetPendingCharges 	(i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
				 			,v_subscriber_no	IN		varchar2
				 			,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)

							RETURN NUMBER IS

	v_stmt					VARCHAR2(32767);
	v_stmt_sub_clause		VARCHAR2(32767);
	i_Result                NUMBER(1);
	i_max					number(4);
	i_count		NUMBER(4);
BEGIN
  BEGIN
  	   -- first count records
	   -- this is done since we cannot create temporary table and modified array of records to client
	   -- as we are basically opening the actual data cursor for the client to parse
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;
	i_max := i_max + 1;

    v_stmt := 'SELECT count(1) '
	  || ' FROM     pending_charge '
      || ' WHERE ban = ' || i_ban
      || ' AND approval_status = ''P'' '
      || ' AND      chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';

  	   -- subscriber clause
	IF i_is_ban_level = NUMERIC_TRUE and i_is_sub_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber_no is null ';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE AND v_subscriber_no IS NOT NULL THEN
	  v_stmt_sub_clause := ' AND subscriber_no = ''' || v_subscriber_no || '''';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber_no is not null ';
	ELSE
	  v_stmt_sub_clause := '';
	END IF;

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

	v_stmt := v_stmt || ' UNION '
      || ' SELECT count(1) '
      || ' FROM     charge '
      || ' WHERE    ban = ' || i_ban
      || ' AND      actv_bill_seq_no is null '
      || ' AND      chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';
	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

	v_stmt := v_stmt || ' UNION '
      || ' SELECT count(1) '
      || ' FROM			charge c, bill b '
      || ' WHERE    c.ban = ' || i_ban
      || ' AND      c.chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
      || ' AND			 c.ban = b.ban '
      || ' AND			 c.actv_bill_seq_no = b.bill_seq_no '
      || ' AND      b.bill_conf_status = ''T'' ';
	v_stmt := v_stmt || v_stmt_sub_clause;

	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    open o_credits for v_stmt;
	fetch o_credits into i_count;
	i_max := i_count;
fetch o_credits into i_count;
	i_max := i_max + i_count;
	fetch o_credits into i_count;
	i_max := i_max + i_count;
	close o_credits;

	IF i_max > i_maximum THEN
	  i_has_more := NUMERIC_TRUE;
	ELSE
	  i_has_more := NUMERIC_FALSE;
    END IF;

  	   -- secondly perform actual query
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;

    v_stmt := 'SELECT   ent_seq_no, chg_creation_date, effective_date, actv_code, actv_reason_code, feature_code, '
  	  || ' ftr_revenue_code, balance_impact_code,  subscriber_no, product_type, operator_id, '
      || ' actv_amt, tax_gst_amt, tax_pst_amt, tax_hst_amt, approval_status, bl_ignore_ind, soc '
	  || ' FROM     pending_charge '
      || ' WHERE ban = ' || i_ban
      || ' AND approval_status = ''P'' '
      || ' AND      chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';
	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

	v_stmt := v_stmt || ' UNION '
      || ' SELECT   ent_seq_no, chg_creation_date, effective_date, actv_code, actv_reason_code, feature_code, '
      || '               ftr_revenue_code, balance_impact_code, subscriber_no, product_type, operator_id, '
      || '               actv_amt, tax_gst_amt, tax_pst_amt, tax_hst_amt, ''Y'',  bl_ignore_ind, soc '
      || ' FROM     charge '
      || ' WHERE    ban = ' || i_ban
      || ' AND      actv_bill_seq_no is null '
      || ' AND      chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || '''';
	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    v_stmt := v_stmt || ' UNION '
      || ' SELECT   c.ent_seq_no, c.chg_creation_date, c.effective_date, c.actv_code, c.actv_reason_code, c.feature_code, '
      || ' c.ftr_revenue_code, c.balance_impact_code, c.subscriber_no, c.product_type, c.operator_id, '
      || '               c.actv_amt, c.tax_gst_amt, c.tax_pst_amt, c.tax_hst_amt, ''Y'',  c.bl_ignore_ind, c.soc '
      || ' FROM			charge c, bill b '
      || ' WHERE    c.ban = ' || i_ban
      || ' AND      c.chg_creation_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
      || ' AND			 c.ban = b.ban '
      || ' AND			 c.actv_bill_seq_no = b.bill_seq_no '
      || ' AND      b.bill_conf_status = ''T'' ';
	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    v_stmt := v_stmt || ' ORDER BY chg_creation_date desc ';
    open o_credits for v_stmt;
    i_Result := NUMERIC_TRUE;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_err_msg := ERR_NO_DATA_FOUND;

    WHEN OTHERS THEN
      v_err_msg := SQLERRM;

    i_Result := NUMERIC_FALSE;
  END;

  RETURN i_Result;
END GetPendingCharges;

FUNCTION GetLetterRequests 	(i_ban				IN		number
                            ,o_from_date		IN		date
                            ,o_to_date			IN		date
				 			,v_subscriber_no	IN		varchar2
							,i_is_ban_level		IN		number
				 			,i_is_sub_level		IN		number
				 			,i_maximum          IN      number
			         		,o_credits          OUT     RefCursor
			         		,i_has_more			OUT     number
				 			,v_err_msg			OUT		varchar2)

							RETURN NUMBER IS

	v_stmt					VARCHAR2(32767);
	v_stmt_sub_clause		VARCHAR2(32767);
	i_Result                NUMBER(1);
	i_max					number(4);
	i_count			NUMBER(4);
BEGIN
  BEGIN
  	   -- first count records
	   -- this is done since we cannot create temporary table and modified array of records to client
	   -- as we are basically opening the actual data cursor for the client to parse
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;
	i_max := i_max + 1;

    v_stmt := 'SELECT count(1) '
	  || ' FROM lms_requests '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND     (letter_req_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' or letter_prod_date between ''' || o_from_date || ''' and ''' || o_to_date || ''')';

  	   -- subscriber clause
	IF i_is_ban_level = NUMERIC_TRUE and i_is_sub_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber is null ';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE AND v_subscriber_no IS NOT NULL THEN
	  v_stmt_sub_clause := ' AND subscriber = ''' || v_subscriber_no || '''';
	ELSIF i_is_sub_level = NUMERIC_TRUE and i_is_ban_level = NUMERIC_FALSE THEN
	  v_stmt_sub_clause := ' AND subscriber is not null ';
	ELSE
	  v_stmt_sub_clause := '';
	END IF;

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

	v_stmt := v_stmt || ' union '
      || ' SELECT count(1) '
	  || ' FROM LMS_REQUESTS_HIS '
	  || ' where ban = ' || i_ban
	  || ' AND     (letter_req_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' or letter_prod_date between ''' || o_from_date || ''' and ''' || o_to_date || ''')';

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    open o_credits for v_stmt;
	fetch o_credits into i_count;
	i_max := i_count;
fetch o_credits into i_count;
	i_max := i_max + i_count;
	close o_credits;

	IF i_max > i_maximum THEN
	  i_has_more := NUMERIC_TRUE;
	ELSE
	  i_has_more := NUMERIC_FALSE;
    END IF;

  	   -- secondly perform actual query
	IF i_maximum > 0 AND i_maximum < MAX_MAXIMORUM THEN
	  i_max := i_maximum;
	ELSE
	  i_max := MAX_MAXIMORUM;
	END IF;

    v_stmt := 'SELECT REQ_NO, OPERATOR_ID, LETTER_CAT, LETTER_CODE, '
      || ' SUBSCRIBER, LETTER_REQ_DATE, LETTER_STATUS, LETTER_PROD_DATE, VARS1, LETTER_VER '
	  || ' FROM lms_requests '
	  || ' WHERE   ban = ' || i_ban
	  || ' AND     (letter_req_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' or letter_prod_date between ''' || o_from_date || ''' and ''' || o_to_date || ''')';

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

	v_stmt := v_stmt || ' union '
      || ' select REQ_NO, OPERATOR_ID, LETTER_CAT, LETTER_CODE, '
      || '  SUBSCRIBER, LETTER_REQ_DATE, LETTER_STATUS, '
	  || ' LETTER_PROD_DATE, VARS1, LETTER_VER '
	  || ' FROM LMS_REQUESTS_HIS '
	  || ' where ban = ' || i_ban
	  || ' AND     (letter_req_date between ''' || o_from_date || ''' and ''' || o_to_date || ''''
	  || ' or letter_prod_date between ''' || o_from_date || ''' and ''' || o_to_date || ''')';

	v_stmt := v_stmt || v_stmt_sub_clause;
	v_stmt := v_stmt || ' AND rownum <= ' || i_max;

    open o_credits for v_stmt;
    i_Result := NUMERIC_TRUE;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
   	  v_err_msg := ERR_NO_DATA_FOUND;

    WHEN OTHERS THEN
      v_err_msg := SQLERRM;

    i_Result := NUMERIC_FALSE;
  END;

  RETURN i_Result;
END GetLetterRequests;

END;
/
