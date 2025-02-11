CREATE OR REPLACE
TYPE T_PARAMETER_NAME_ARRAY AS TABLE OF VARCHAR2(40);
/

COMMIT ;

/*
after creation of the type, we need to grant excute permission to user BASREAD,
then on BASREAD schema, create synonym point to this new type
The following is example for D3 environment

--GRANT EXECUTE ON AMDOCS_EXTD3.T_PARAMETER_NAME_ARRAY TO BASREAD3;
--CREATE SYNONYM T_PARAMETER_NAME_ARRAY FOR AMDOCS_EXTD3.T_PARAMETER_NAME_ARRAY;
*/
