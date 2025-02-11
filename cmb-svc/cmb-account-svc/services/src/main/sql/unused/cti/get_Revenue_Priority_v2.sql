CREATE OR REPLACE PROCEDURE CTIADM.GET_REVENUE_PRIORITY_V2
(  dn_num_in	  	in   varchar2,
   cust_code_in	  	in   varchar2,
   trace_flg            in   char, 
   return_value         out  varchar2
) 
-- * --------------------------------------------------------------------------------------- * --
-- * GET_REVENUE_PRIORITY
-- *
-- *	this procedure will return the revenue priority from the revenue priority table based on
-- *    the input arguments 
-- *
-- *
-- * 	in      Directory Number                (i.e. 4165551212)
-- * 	in	Customer Code / Acct #		(Customer code, i.e. 5.35542.00.00.100056 )
-- *	in	Trace				('Y' or 'N'; used for debugging only)
-- * 
-- *	out	Revenue Priority Code		(1 digit code)
-- *                                            or
-- *                                            'NotF' (Not Found) 
-- *                                            or
-- *                                            'NoIn' (No Invoices) 
--*                            ClientName
-- *
-- *  Note: - input values that are not know can be passed in as null or empty string('')
-- *        - possible Reasons for 'NotF':   - subscriber not active
-- *-- Date	   		Developer     	  	  Modifications
-- *--10-Sep-2001		Ludmila Pomirche	  Changed remote procedure call
--							  Client data retrieved from CRDB instead of BSCS
--  *--11-Feb-2002		Ludmila Pomirche	  Added logic for Prestige Clients routing
-- * --------------------------------------------------------------------------------------- * --
as
-- * --------------------------------------------------------------------------------------- * --
-- * Declarations 
-- * --------------------------------------------------------------------------------------- * --

-- * Variables
   vReturnValue    		varchar2(105) := '';
   vInputType                   varchar2(10) ;
   dToday			date;
   cExceptionRaised             char(1) := 'N';
   iCount                       integer;
   n3mthAverage                 number(11,2) := 0;
   clientName                   varchar2(100);
   ratePlan			varchar2(100);
   
-- * Cursors
   CURSOR         cur_revenue_priority IS
                  SELECT *
                  FROM   ccti.revenue_priority
                  WHERE  n3mthAverage between from_dollar and to_dollar
                  AND    effective_date <= dToday
                  AND    delete_flg = 'N'
                  ORDER BY effective_date desc;

   revenue_priority_row		cur_revenue_priority%ROWTYPE;

-- * --------------------------------------------------------------------------------------- * --
-- * Main Process: Initializations 
-- * --------------------------------------------------------------------------------------- * --
begin

   clientName := '';

-- Get System date
   SELECT sysdate
   INTO   dToday
   FROM   dual;

   if trace_flg = 'Y' then dbms_output.put_line('Trace is on ..'); end if;

   if trace_flg = 'Y' then
     dbms_output.put_line('Function: get_revenue_priority');
     dbms_output.put_line('--------------------------');
     dbms_output.put_line('');
     dbms_output.put_line('Input Parameters (orginal):');
     dbms_output.put_line('- Directory Number:' || dn_num_in );
     dbms_output.put_line('- Cust Code / Acct #:' || cust_code_in );
   end if;

-- Get 3 month invoice Amount averaged by # of Active Subscribers
BEGIN
-- lp 12-Sep-2001
  -- n3mthAverage := get_3mth_invoice_avg(dn_num_in,cust_code_in,trace_flg) ;
  client_info.GetClientInfo(dn_num_in,clientName,n3mthAverage,ratePlan);
/*-------------------Exception Handler------------------------*/
EXCEPTION
   WHEN OTHERS
   THEN
       vReturnValue := 'rating:NotF|client_name:|rate_plan:';
       goto end_of_process ;  
END;

--Get client Name
--lp 12-Sep-2001
--  clientName  := ltrim(GetClientName(dn_num_in));

   dbms_output.put_line('');
   dbms_output.put_line('3mth Average:' || TO_CHAR(n3mthAverage,'999,999.99'));
-- Prestige Client 
   if n3mthAverage = -99999 then 
     vReturnValue := 'rating:5|client_name:' || clientName||'|rate_plan:'||ratePlan;
     goto end_of_process ; 
   end if;
   
-- Invoices are not found for the client
   if n3mthAverage = -2 then 
     vReturnValue := 'rating:NoIn|client_name:' || clientName||'|rate_plan:'||ratePlan;
     goto end_of_process ; 
   end if;
   
   if n3mthAverage < 0 then 
     vReturnValue := 'rating:NotF|client_name:' || clientName||'|rate_plan:'||ratePlan;
     goto end_of_process ; 
   end if;

-- Get Revenue Priority Code from Revenue Priority table
   OPEN cur_revenue_priority;
   FETCH cur_revenue_priority INTO revenue_priority_row;

   IF cur_revenue_priority%NOTFOUND then
     vReturnValue := 'rating:NotF|client_name:' || clientName||'|rate_plan:'||ratePlan;
     goto end_of_process ; 
   end if;

   CLOSE cur_revenue_priority;

   vReturnValue :=  'rating:' || revenue_priority_row.revenue_priority_cd || '|client_name:' || clientName||'|rate_plan:'||ratePlan;
 
-- * --------------------------------------------------------------------------------------- * --
-- * End of Process: return 
-- * --------------------------------------------------------------------------------------- * --
<<end_of_process>>

   if trace_flg = 'Y' then
     dbms_output.put_line('');
     dbms_output.put_line( vReturnValue);
   end if; 

   return_value := vReturnValue ;         


end ;
/
show errors;

