


-- Package MEMO_UTILITY_PKG

CREATE OR REPLACE
package Memo_Utility_pkg as
 ------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--		for MEMO's data retrieval from Knowbility database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20160 to -20169   ( 10 codes )
--
--
-- Date	   		Developer     	  	  Modifications
-- 10-03-2005	Marina Kuper     	  Changed GetMemosByCriteria retrieval
-- 30-03-2005   Marina Kuper          Added memoId to the memo retrievals
-- 14-07-2005   Marina Kuper          Chanhed GetLastMemo
-------------------------------------------------------------------------
MemoNotFound	Exception;
PRAGMA							EXCEPTION_INIT(MemoNotFound, -20160);



-- Reference to a Cursor
TYPE RefCursor Is Ref Cursor;

--  Variables

     v_sql_select			VARCHAR2(500);
     v_sql_from		    	VARCHAR2(500);
     v_sql_where			VARCHAR2(750);
     v_row_num             VARCHAR2(20);


-------------------------------------------------------------------------
-- Get Last Memo  by Ban or by Ban and Subscriber
--------------------------------------------------------------------------
Procedure GetLastMemo(
		  		  pi_ban				IN	    number
				 ,pi_subscriber_no		IN	    varchar2
				 ,pi_memo_type          IN      varchar2
				 ,po_memo    			OUT     RefCursor) ;


--------------------------------------------------------------------------
-- Get Memo's  by Ban
--------------------------------------------------------------------------

Procedure GetMemos(
		  		  pi_ban				IN	    number
				 ,pi_counter   			IN   	number
				 ,po_memo    			OUT     RefCursor);


---------------------------------------------------------------------------
-- Get Memo's  by Ban and search criteria
--------------------------------------------------------------------------

Procedure GetMemosByCriteria(
		  		  pi_ban				IN    	number
				 ,pi_subscriber_no   	IN   	varchar2
				 ,pi_memo_types    		IN   	varchar2
                 ,pi_manual_text        IN   	varchar2
                 ,pi_system_text        IN   	varchar2
                 ,pi_from_date          IN   	varchar2
                 ,pi_to_date            IN   	varchar2
				 ,po_memo    			OUT     RefCursor);



-----------------------------------------------------------------------------------------------
-- Create SQL for Account Last Memo
----------------------------------------------------------------------------------------------

Function getSQLForAccountLastMemo ( pi_ban NUMBER ,pi_memo_type VARCHAR2) Return VARCHAR2;


------------------------------------------------------------------
-- Create SQL for Subscriber Last Memo
------------------------------------------------------------------

Function getSQLForSubscriberLastMemo ( pi_ban NUMBER ,pi_subscriber_no VARCHAR2 ,pi_memo_type VARCHAR2 ) Return VARCHAR2;


-----------------------------------------------------------------------------------------------
-- Get quoted values
----------------------------------------------------------------------------------------------

Function getQuotedValues ( orig_values VARCHAR2) Return VARCHAR2;


End MEMO_UTILITY_PKG;
/

sho err

-- End of DDL script for MEMO_UTILITY_PKG





-- Start of DDL script for MEMO_UTILITY_PKG

-- Package body MEMO_UTILITY_PKG

CREATE OR REPLACE
Package Body MEMO_UTILITY_PKG As


--################################################################
-- Procedures/Functions
--################################################################


Function getSQLForAccountLastMemo ( pi_ban NUMBER ,pi_memo_type VARCHAR2) Return VARCHAR2

Is
------------------------------------------------------------------
-- Create SQL for Account Last Memo
------------------------------------------------------------------

v_SQL		         	VARCHAR2(10000);
v_memo_type             VARCHAR2(20);



Begin


If ( pi_memo_type = 'CreditCheck' ) Then
    v_memo_type := 'in (''DPCH'',''REFC'')';
Else
  	v_memo_type :=  ' = ''' || pi_memo_type || ''' ' ;
End If ;


v_sql_where :=
    				 ' where ' ||
				     ' memo_ban = ' || pi_ban ||
    				 ' and memo_type  '|| v_memo_type ;

v_SQL := v_sql_select || v_sql_from || v_sql_where || v_row_num;

Return v_SQL;

End; -- Function


-------------------------------------------------------------------

Function getSQLForSubscriberLastMemo ( pi_ban NUMBER, pi_subscriber_no VARCHAR2 ,pi_memo_type VARCHAR2)  Return VARCHAR2

Is
------------------------------------------------------------------
-- Create SQL for Subscriber Last Memo
------------------------------------------------------------------

v_SQL			VARCHAR2(10000);


Begin



  	v_sql_where :=
    				 ' where ' ||
    				 ' memo_ban = ' || pi_ban ||
			     	 ' and memo_subscriber  = ''' || pi_subscriber_no ||''' '||
    				 ' and memo_type  = ''' || pi_memo_type || ''' ';

v_SQL := v_sql_select || v_sql_from || v_sql_where || v_row_num ;


Return v_SQL;

End; -- Function
------------------------------------------------------------------------------------------------------------------------------
Function getQuotedValues ( orig_values VARCHAR2) Return VARCHAR2

Is


memo_values			VARCHAR2(10000);
pi_values			VARCHAR2(10000);
m_value			    VARCHAR2(30);
sep_pos             NUMBER(30);

Begin

    pi_values := orig_values;

    LOOP
          sep_pos := INSTR(pi_values,',');
          EXIT WHEN ( sep_pos = 0 );
          m_value := SUBSTR ( pi_values,1,sep_pos - 1);
          m_value := ''''|| m_value || ''' ';
          memo_values := memo_values || ',' || m_value;
          pi_values := SUBSTR ( pi_values,sep_pos + 1);

     END LOOP;
 --  dbms_output.put_line('after l' );
    m_value := ''''|| pi_values || ''' ';
    memo_values := memo_values || ',' || m_value;
    memo_values := SUBSTR (memo_values,2);

    Return memo_values;

End; -- Function


--------------------------------------------------------------------------
--------------------------------------------------------------------------

Procedure GetLastMemo(
		  		  pi_ban				IN	number
				 ,pi_subscriber_no		IN	varchar2
				 ,pi_memo_type          IN      varchar2
				 ,po_memo    			OUT     RefCursor)
Is

 v_sql_statement        VARCHAR2(10000);
 

Begin

  v_row_num := ' and rownum <=1';


	 ------------------------------------------------------------------
	 -- Prepare  SQL statement to retrieve last memo
	 ------------------------------------------------------------------

v_sql_select :=
' select /*+ INDEX_DESC ( MEMO MEMO_PK ) */ ' ||
' memo_date , '  ||
' memo_type, ' ||
' memo_subscriber, ' ||
' memo_product_type, ' ||
' memo_manual_txt, ' ||
' memo_system_txt, ' ||
' sys_update_date, ' ||
' operator_id, ' ||
' memo_id ' ;

v_sql_from :=
				 ' from memo ' ;



If (pi_subscriber_no is null ) Then

  v_sql_statement := getSQLForAccountLastMemo ( pi_ban ,pi_memo_type );

Else

  v_sql_statement := getSQLForSubscriberLastMemo ( pi_ban ,pi_subscriber_no,pi_memo_type );

End If;


	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_memo for v_sql_statement ;


Exception

When NO_DATA_FOUND Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;

     -- return NULL cursor
     open po_memo for
	 	  select NULL from dual where 1=0;

When Others Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;
	Raise_Application_Error(-20160, 'Memo Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );


End GetLastMemo;

--------------------------------------------------------------------------

--------------------------------------------------------------------------

Procedure GetMemos(
		  		  pi_ban				IN	number
				 ,pi_counter   			IN   	number
				 ,po_memo    			OUT     RefCursor)
Is

v_select_count		   VARCHAR2(100);
v_sql_statement        VARCHAR2(10000);


Begin

	 --------------------------------------------------------------------
	 -- Prepare  SQL statement to retrieve memos
	 --------------------------------------------------------------------


If (pi_counter > 0) Then
  v_select_count := ' where ROWNUM <=' || pi_counter;
Else
  v_select_count  := '';
End If;

	 v_sql_select := '  select * ' ;

	 v_sql_from :=
	               	' from '||
	               	'  ( select /*+ INDEX_DESC ( MEMO MEMO_PK ) */ ' ||
			'  memo_date , '  ||
			'  memo_type, ' ||
			'  memo_subscriber, ' ||
			'  memo_product_type, ' ||
			'  memo_manual_txt, ' ||
			'  memo_system_txt, ' ||
			'  sys_update_date, ' ||
			'  operator_id, '||
            '  memo_id ' ||
			'  from memo ' ||
			'  where  memo_ban = ' || pi_ban ||
            '  ) ';
--			'  order by memo_id desc ) ';

         v_sql_statement := v_sql_select || v_sql_from || v_select_count ;

     -----------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 -----------------------------------------------------------------------
  	 open  po_memo for v_sql_statement ;


Exception

When NO_DATA_FOUND Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;

     -- return NULL cursor

     open po_memo for
	 	  select NULL from dual where 1=0;

When Others Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;
	Raise_Application_Error(-20160, 'Memo Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );


End GetMemos;


------------------------------------------------------------------------------
------------------------------------------------------------------------------

Procedure GetMemosByCriteria(
		  		  pi_ban				IN    	number
				 ,pi_subscriber_no   	IN   	varchar2
				 ,pi_memo_types    		IN   	varchar2
                 ,pi_manual_text        IN   	varchar2
                 ,pi_system_text        IN   	varchar2
                 ,pi_from_date          IN   	varchar2
                 ,pi_to_date            IN   	varchar2
				 ,po_memo    			OUT     RefCursor)

Is


v_sql_statement       VARCHAR2(10000);
v_sql_order_by        VARCHAR2(50);
types                 VARCHAR2(10000);
subscribers           VARCHAR2(10000);

DETAILED_DATE_PATTERN CONSTANT VARCHAR2(25) := 'mm/dd/yyyy hh24:mi:ss';


Begin

	 -----------------------------------------------------------------------
	 -- Prepare  SQL statement to retrieve memos
	 -----------------------------------------------------------------------



	v_sql_select :=
                ' select /*+ INDEX_DESC ( MEMO MEMO_PK ) */ ' ||
                ' memo_date , '  ||
                ' memo_type, ' ||
                ' memo_subscriber, ' ||
                ' memo_product_type, ' ||
                ' memo_manual_txt, ' ||
                ' memo_system_txt, ' ||
                ' sys_update_date, ' ||
                ' operator_id, ' ||
                ' memo_id '  ;

    v_sql_from :=
				 ' from memo' ;


    v_sql_where :=
    			 ' where ' ||
                 ' memo_ban = ' || pi_ban  ;

    If (pi_subscriber_no is not null ) Then

         if ( INSTR(pi_subscriber_no,',') = 0 ) Then

         v_sql_where  :=
                   v_sql_where || ' and memo_subscriber = ''' || pi_subscriber_no || ''' ' ;
        Else

            subscribers := getQuotedValues(pi_subscriber_no);

         v_sql_where  :=
                     v_sql_where || ' and memo_subscriber in  (' || subscribers || ')' ;

         End If;



    End If;

    If ( pi_memo_types is not null ) Then


      if ( INSTR(pi_memo_types,',') = 0 ) Then

        v_sql_where  :=
                   v_sql_where || ' and memo_type = ''' || pi_memo_types || ''' ' ;
      Else

        types := getQuotedValues(pi_memo_types);

        v_sql_where  :=
                  v_sql_where || ' and memo_type in  (' || types || ')' ;

      End If;

     End If;

    If (pi_manual_text is not null ) Then

      v_sql_where  :=
                   v_sql_where || ' and ( upper(memo_manual_txt) like ''%' || upper(trim(pi_manual_text)) ||'%'' ' ;

      v_sql_where  :=
                   v_sql_where || ' or upper(memo_system_txt) like ''%' || upper(trim(pi_manual_text)) ||'%'' )' ;

    End If;

    If (pi_from_date is not null ) Then

      v_sql_where  :=
                   v_sql_where || ' and memo_date between to_date(''' || pi_from_date || ''' '|| ','''||DETAILED_DATE_PATTERN ||''')'||
                   ' and to_date(''' || pi_to_date || ''' '|| ','''||DETAILED_DATE_PATTERN ||''')';
     End If;



    v_sql_statement := v_sql_select || v_sql_from || v_sql_where ;

    -- dbms_output.put_line('v_sql_where'|| v_sql_where );
     -----------------------------------------------------------------------
	 -- Create reference to a cursor  using a  SQL statement
	 -----------------------------------------------------------------------
  	 open  po_memo for v_sql_statement ;


Exception

When NO_DATA_FOUND Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;

     -- return NULL cursor

     open po_memo for
	 	  select NULL from dual where 1=0;

When Others Then
	If ( po_memo%ISOPEN ) Then
	   close po_memo;
	End If;
	Raise_Application_Error(-20160, 'Memo Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );


End GetMemosByCriteria;

-----------------------------------------------------------------------------------------------------------------------------------------


End;

-- MEMO_UTILITY_PKG
/

sho err

-- End of DDL script for MEMO_UTILITY_PKG
