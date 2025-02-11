-- delete Business Connect Migration Types 
DELETE FROM GENERIC_CODES_EXT WHERE GENERIC_CODE in ('MSPO','POMS');

COMMIT;