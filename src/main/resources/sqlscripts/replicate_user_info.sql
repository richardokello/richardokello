CREATE OR REPLACE PROCEDURE REPLICATE_USER_INFO 
(
  userEmail IN VARCHAR2 
) 
AS
 USERID_ number;
 EMAIL_  varchar(100 BYTE);
 PASSWORD_ varchar(100 BYTE);
 PASSWORD_STATUS number;
 AUTH_TYPE NUMBER;
 WKGRP NUMBER;
BEGIN
INSERT INTO CRDB_BURUNDI.UFS_USER(ACTION_STATUS,
                                  CREATION_DATE ,
                                  EMAIL ,
                                  FULL_NAME ,
                                  INTRASH ,
                                  PHONE_NUMBER ,
                                  USER_TYPE ,
                                  TENANT_ID ,
                                  GENDER ,
                                  DEPARTMENT_ID ,
                                  AVATAR ,
                                  ACTION ,
                                  DATE_OF_BIRTH ,
                                  STATUS ,
                                  COUNTY_ID) SELECT
                                                 ACTION_STATUS ,
                                                 CREATION_DATE ,
                                                 EMAIL ,
                                                 FULL_NAME ,
                                                 INTRASH ,
                                                 PHONE_NUMBER ,
                                                 USER_TYPE ,
                                                 TENANT_ID ,
                                                 GENDER ,
                                                 DEPARTMENT_ID ,
                                                 AVATAR ,
    ACTION ,
    DATE_OF_BIRTH ,
    STATUS ,
    COUNTY_ID  FROM UFS_CRDB_UAT.UFS_USER WHERE EMAIL=userEmail;
COMMIT;

-- USER DETAILS
SELECT USER_ID into USERID_ FROM CRDB_BURUNDI.UFS_USER WHERE EMAIL=userEmail;
select USERNAME,PASSWORD,PASSWORD_STATUS,AUTHENTICATION_TYPE INTO EMAIL_,PASSWORD_,PASSWORD_STATUS,AUTH_TYPE FROM UFS_CRDB_UAT.UFS_AUTHENTICATION WHERE USERNAME=userEmail;

-- USER AUTHENTICATION CREDS
INSERT INTO CRDB_BURUNDI.UFS_AUTHENTICATION(USER_,USERNAME,AUTHENTICATION_TYPE,PASSWORD,PASSWORD_STATUS) VALUES (USERID_,EMAIL_,AUTH_TYPE,PASSWORD_,PASSWORD_STATUS);
COMMIT;

-- USER WORKGROUP
SELECT WORKGROUP INTO WKGRP FROM UFS_CRDB_UAT.UFS_USER_WORKGROUP WHERE USER_=(SELECT USER_ID FROM UFS_CRDB_UAT.UFS_USER WHERE EMAIL=userEmail);
INSERT INTO CRDB_BURUNDI.UFS_USER_WORKGROUP(USER_,WORKGROUP) VALUES (USERID_,WKGRP);
COMMIT;
END REPLICATE_USER_INFO;
