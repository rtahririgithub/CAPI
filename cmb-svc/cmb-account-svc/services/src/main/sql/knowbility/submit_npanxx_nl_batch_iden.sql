variable jobno number;

begin
	dbms_job.submit(:jobno, 'nl_ngp_pkg.NPANXX_NL_Batch_IDEN;',to_date(to_char(sysdate,'mmddyyyy')||' 01:00','mmddyyyy hh24:mi'),'sysdate + 1/24');
commit;
end;
/

print jobno