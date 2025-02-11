exec dbms_output.disable;
/* set serverout OFF */
exec dbms_output.enable(1000000);
set serverout ON size 1000000 ; 

VARIABLE rc REFCURSOR

declare 
return_flag BOOLEAN;
message varchar2 (2000);

BEGIN
   rule_utility_pkg.getRules(:rc);
END;

/

print rc

