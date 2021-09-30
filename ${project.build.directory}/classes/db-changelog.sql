-- File changes on database
-- Document key
-- ----------------------------------------------
-- Environment: Prod | UAT | Dev
-- Date: Indicates date query has been deployed to above environment parameter
-- Prod is the last environment and if it has been set, code should not be run in prod again


-- FDI SMS Changes
-- BPR initial SMS API stopped working and they therefore gave access to a new API for implementation
-- https://fdisms.docs.apiary.io/#introduction
-- It has authentication api for fetching token that is used to send SMS request
-- TODO: A table for managing the token should be created
-- TODO: Change the table_index values according to the environment

-- Environment: UAT
-- Date: 23rd July 2021

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(72,'FDI_SMS_API_USERNAME','510D89CC-9C80-4405-870C-6CBCA7EED0A8','FDI SMS User');

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(73,'FDI_SMS_API_PASSWORD','635EA5FF-2EF9-46DC-BF3D-DC679B00F6AB','FDI SMS Pass');

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(74,'FDI_SMS_API_AUTHENTICATION_URL','https://messaging.fdibiz.com/api/v1/auth','FDI SMS Auth Uri');

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(75,'FDI_SMS_API_PASSWORD','635EA5FF-2EF9-46DC-BF3D-DC679B00F6AB','FDI SMS Pass');

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(76,'FDI_SMS_SEND_SINGLE_MT_URL','https://messaging.fdibiz.com/api/v1/mt/single','FDI SMS Send');

INSERT INTO XSWITCH_PARAMETER(TABLE_INDEX, PARAM_NAME, PARAM_VALUE, PARAM_MISC) VALUES
(77,'FDI_SMS_SENDER_ID','BPR DG','FDI SMS ID');
