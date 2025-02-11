CREATE OR REPLACE package NL_NGP_pkg as
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package NL_NGP_pkg containing procedures
--		for Number Location, Number Group Batch process
--
--
-- Date	   		Developer     	  	  Modifications
-- 10-18-2001		Ludmila Pomirche	  created
-- 06-24-2002		Ludmila Pomirche	  added logic for mike
-- 07-18-2003		Ludmila Pomirche	  added logic for pager
-- 02-15-2006		Ludmila Pomirche	  added logic for Autotel
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-------------------------------------------------------------------------

Procedure NPANXX_NL_Batch;

Procedure NPANXX_NL_Batch_IDEN;
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
   
    FUNCTION getVersion RETURN VARCHAR2;

------------------------------------------------------------------------------
-- description: Procedure Write_Error_Log for writing error log into
--		the batch_error_log table
------------------------------------------------------------------------------

Procedure Write_Error_Log (pi_process		varchar2
			   ,pi_error_code	number
			   ,pi_error_msg	varchar2);
End;
/


CREATE OR REPLACE package body NL_NGP_pkg as

FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;

Procedure NPANXX_NL_Batch is

cursor c_ctn is
   select substr(ctn,1,6) npanxx
          , nl
          ,ngp
          ,product_type
          ,count(*) aa_sts_cnt
   from ctn_inv
   where ctn_status = 'AA'
   and   product_type in ('C','P','T')
   and   nl in ('TLS','PPE','TGO','EAR')
   group by substr(ctn,1,6), nl,ngp, product_type ;

  -- npanxx_rec   c_ctn%ROWTYPE;

cursor c_npanxx(pi_npanxx 	 varchar2
		,pi_nl     	 varchar2
		,pi_ngp     	 varchar2
		,pi_product_type varchar2) is
   select rowid
   from   npanxx_nl_ngp
   where  npanxx=pi_npanxx
   and    nl=pi_nl
   and    ngp=pi_ngp
   and product_type =pi_product_type;

v_rowid    rowid;


Begin
--dbms_output.enable(1000000);
--dbms_output.put_line('Start');
update npanxx_nl_ngp
set batch_ind='N'
where product_type in ('C','P','T');
commit;
dbms_output.put_line('End commit');
For npanxx_rec in c_ctn loop
	dbms_output.put_line('selected :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt||npanxx_rec.product_type);
	exit when c_ctn%NotFound;
	open c_npanxx(npanxx_rec.npanxx, npanxx_rec.nl,npanxx_rec.ngp, npanxx_rec.product_type );
		fetch c_npanxx into v_rowid;
		If c_npanxx%Found Then
 			update npanxx_nl_ngp
 			set  batch_ind='Y'
 			,aa_sts_cnt=npanxx_rec.aa_sts_cnt
 			,modify_date = sysdate
 			where rowid=v_rowid;
 			commit;
 		dbms_output.put_line('update :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt);
		Else
        		insert into npanxx_nl_ngp
                    	(npanxx
                    	,nl
                    	,ngp
                    	,aa_sts_cnt
                    	,available_ind
		    	,batch_ind
	            	,create_date
	            	,product_type
                    	)
                    	values
                    	( npanxx_rec.npanxx
                    	, npanxx_rec.nl
                    	, npanxx_rec.ngp
                    	, npanxx_rec.aa_sts_cnt
                    	,'N'
                    	,'Y'
                    	,sysdate
                    	,npanxx_rec.product_type
                    	);
         	commit;
         	dbms_output.put_line('insert :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt||','||npanxx_rec.product_type);
 		End If;
 	close c_npanxx;
End loop;
update npanxx_nl_ngp
set available_ind=batch_ind
where product_type in ('C','P','T');
commit;
Exception
When Others Then
 	rollback;
 	Write_Error_Log('NPANXX_NL_Batch',SQLCODE,SQLERRM);
End NPANXX_NL_Batch;
-------------------------------------------------------------------------
-------------------------------------------------------------------------
Procedure NPANXX_NL_Batch_IDEN is

cursor c_ctn is
   select npa||nxx npanxx
          , nl
          ,ngp
          ,count(*) aa_sts_cnt
   from ptn_inv
   where resource_status = 'AA'
   and   (nl='TLS' or nl='VPN')
   group by npa||nxx, nl,ngp ;

  -- npanxx_rec   c_ctn%ROWTYPE;

cursor c_npanxx(pi_npanxx varchar2
		,pi_nl     varchar2
		,pi_ngp     varchar2) is
   select rowid
   from   npanxx_nl_ngp
   where  npanxx=pi_npanxx
   and    nl=pi_nl
   and    ngp=pi_ngp
   and    product_type='I';

v_rowid    rowid;


Begin
--dbms_output.enable(1000000);
--dbms_output.put_line('Start');
update npanxx_nl_ngp
set batch_ind='N'
where   product_type='I';
commit;
dbms_output.put_line('End commit');
For npanxx_rec in c_ctn loop
	--dbms_output.put_line('selected :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt);
	exit when c_ctn%NotFound;
	open c_npanxx(npanxx_rec.npanxx, npanxx_rec.nl,npanxx_rec.ngp);
		fetch c_npanxx into v_rowid;
		If c_npanxx%Found Then
 			update npanxx_nl_ngp
 			set  batch_ind='Y'
 			,aa_sts_cnt=npanxx_rec.aa_sts_cnt
 			,modify_date = sysdate
 			where rowid=v_rowid;
 			commit;
 		--dbms_output.put_line('update :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt);
		Else
        		insert into npanxx_nl_ngp
                    	(npanxx
                    	,nl
                    	,ngp
                    	,aa_sts_cnt
                    	,available_ind
		    	,batch_ind
	            	,create_date
	            	,product_type
                    	)
                    	values
                    	( npanxx_rec.npanxx
                    	, npanxx_rec.nl
                    	, npanxx_rec.ngp
                    	, npanxx_rec.aa_sts_cnt
                    	,'N'
                    	,'Y'
                    	,sysdate
                    	,'I');
         	commit;
         	--dbms_output.put_line('insert :'||npanxx_rec.npanxx||','||npanxx_rec.nl||','||npanxx_rec.aa_sts_cnt);
 		End If;
 	close c_npanxx;
End loop;
update npanxx_nl_ngp
set available_ind=batch_ind
where    product_type='I';
commit;
Exception
When Others Then
 	rollback;
 	Write_Error_Log('NPANXX_NL_Batch',SQLCODE,SQLERRM);
End NPANXX_NL_Batch_IDEN;
---------------------------------------------------------------------------
---------------------------------------------------------------------------
Procedure Write_Error_Log (pi_process		varchar2
			  ,pi_error_code	number
			  ,pi_error_msg		varchar2) is

Begin
insert	into batch_error_log
 		(error_id
  		,package_name
  		,process
  		,error_code
  		,error_message
  		,create_date
  		,create_user)
  	 values
  	 	(batch_error_seq.nextval
  	 	,'NL_NGP_PKG'
  	 	,pi_process
  	 	 ,pi_error_code
		 ,pi_error_msg
		 ,sysdate
		 ,user);
Exception
When Others Then
null;
End Write_Error_Log;
End;
/
