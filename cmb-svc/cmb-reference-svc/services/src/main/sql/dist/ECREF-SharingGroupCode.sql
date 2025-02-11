-- Remove all entries
truncate table ALLOW_SHARING_GROUP;
COMMIT;

-- To be able to insert French characters
SET DEFINE OFF;

-- Domestric Data Sharing Entry
INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('CAD_DATA', 'Canada Data sharing', 'Partage de données utilisées au Canada', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('US_DATA', 'US Roaming data sharing', 'Partage de données utilisées en itinérance aux États-Unis', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('N_America_Data', 'North America data sharing', 'Partage de données utilisées en Amérique du Nord', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('Int_Data', 'International roaming data sharing', 'Partage de données utilisées en itinérance à l''étranger', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('CAD_TXT', 'Canada text messaging sharing', 'Partage de messagerie texte utilisée au Canada', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('US_TXT', 'US roaming text messaging', 'Partage de messagerie texte utilisée en itinérance aux États-Unis', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('N_America_TXT', 'North America text messaging', 'Partage de messagerie texte utilisée en Amérique du Nord', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('Int_Txt', 'International roaming text messaging', 'Partage de messagerie texte utilisée en itinérance à l''étranger', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('CAD_iPad', 'Canada IPAD Data', 'Canada IPAD Data', 'x108373', sysdate, 'x108373', sysdate); 

INSERT INTO ALLOW_SHARING_GROUP (ALLOW_SHARING_GROUP_CD, ALLOW_SHARING_GROUP_TXT, ALLOW_SHARING_GROUP_FR_TXT, CREATE_USER_ID,
CREATE_TS, LAST_UPDT_USER_ID, LAST_UPDT_TS) 
VALUES ('WORLD_DATA', 'World Data', 'World Data', 'x108373', sysdate, 'x108373', sysdate); 

COMMIT;


