/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.math.BigDecimal;

/**
 *
 * @author Owori Juma
 */
public class AppConstants {

    public static final Marker AUDIT_LOG = MarkerFactory.getMarker("AUDIT_LOG");

    public static final String YES = "YES";
    public static final String NO = "NO";

    public static final String PARAMETER_OTP_EXPIRY = "otpExpiry";
    public static final String PARAMETER_POS_PIN_LENGTH = "posPin";
    public static final String PARAMETER_EXPIRY = "expiry";
    public static final String PARAMETER_ATTEMPTS = "attempts";
    public static final String PARAMETER_OTP_ATTEMPTS = "otpAttempts";
    public static final String PARAMETER_PASSWORD_LENGTH = "passwordLength";
    public static final String PARAMETER_CHAR_NUMBER = "charLength";
    public static final String PARAMETER_NUMERIC_NUMBER = "numericLength";
    public static final String PARAMETER_SPECIAL_CHAR_NUMBER = "specialCharacter";
    public static final String PARAMETER_UPPERCASE_NUMBER = "upperCase";
    public static final String PARAMETER_LOWERCASE_NUMBER = "lowerCase";
    public static final String PARAMETER_SCAPI_URL = "scapiUrl";
    public static final String PARAMETER_SCAPI_USERNAME = "scapiUsername";
    public static final String PARAMETER_SCAPI_PASSWORD = "scapiPassword";
    public static final String PARAMETER_SCAPI_SYSTEM_CODE = "scapiSystemCode";
    public static final String PARAMETER_SCAPI_ACCOUNT_SERVICE_CODE = "scapiAccServiceCode";
    public static final String PARAMETER_SCAPI_TRX_SERVICE_CODE = "trxServiceCode";
    public static final String PARAMETER_SMS_API_URL = "smsApiUrl";
    public static final String PARAMETER_SMS_API_USERNAME = "smsUsername";
    public static final String PARAMETER_SMS_API_PASSWORD = "smsPassword";
    public static final String PARAMETER_BASE_CURRENCY = "baseCurrency";
    public static final String PARAMETER_REUSE_COUNT = "reuseCount";
    public static final String PARAMETER_VEHICLE_CAPACITY = "vehicleCapacity";
    public static final String PARAMETER_UPLOAD_DIR = "fileUploadDir";
    public static final String PARAMETER_EMAIL_TEMPLATE_URL = "emailTemplateUrl";
    public static final String PARAMETER_BANK_GROUP_EMAIL = "bankGroupEmail";
    public static final String PARAMETER_BANK_GROUP_MSISDN = "bankGroupMsisdn";

    public static final String PARAM_TYPE_PASSWORD = "PASSWORD";
    //entities
    public static final String ENTITY_PASSWORD_POLICY = "Password Policy";
    public static final String ENTITY_POS_CONFIGURATION = "Pos Configuration";
    public static final String ENTITY_SYSTEM_INTEGRATION = "System Integration";
    public static final String ENTITY_GLOBAL_INTEGRATION = "Global Configuration";
    public static final String ENTITY_MESSAGE_TEMPLATES = "Message Templates";
    //    public static final CustomEntry<String, String> ENTITY_INTEGRATION = new CustomEntry("INTEGRATION", "Integration");
    public static final CustomEntry<String, String> ENTITY = new CustomEntry("ENTITY", "Entity");
    //    public static final CustomEntry<String, String> ENTITY_SYSTEM = new CustomEntry("SYSTEM", "System");
    //public static final CustomEntry<String, String> ENTITY_PASSWORD = new CustomEntry("PASSWORD", "Password");

    //password status
    public static final String PASS_LOCKED_STATUS = "Locked";
    public static final String PASS_EXPIRED_STATUS = "Expired";
    public static final String PASS_ACTIVE_STATUS = "Active";

    //Schedule type    
    public static final String MANUAL_SCHEDULE = "Manual";
    public static final String AUTO_SCHEDULE = "Auto";

    //Download type
    public static final String DOWNLOAD_APP_ONLY = "App Only";
    public static final String DOWNLOAD_APP_AND_FILES = "App and Files";
    public static final String DOWNLOAD_FILES_ONLY = "Files Only";

    //account status
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    public static final String STATUS_LOCKED = "Locked";
    public static final String STATUS_DISABLED = "Disabled";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_UNAPPROVED = "Unapproved";
    public static final String STATUS_FAILED = "Failed";
    public static final String STATUS_NEW = "New";
    public static final String STATUS_DECLINED = "Rejected";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_EXPIRED = "Expired";
    public static final String STATUS_DEACTIVATED = "Deactivated";
    public static final String STATUS_VERIFIED = "Verified";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_REPAIR = "Repair";
    public static final String STATUS_REALLOCATE= "Reallocate";
    public static final String STATUS_UNCONFIRMED = "Unconfirmed";
//    public static final String STATUS_CANCELLED= "Unapproved";

    //Action Status
    public static final String ACTION_STATUS_UNCONFIRMED = "Unconfirmed";
    public static final String ACTION_STATUS_REJECTED = "Rejected";
    public static final String ACTION_STATUS_APPROVED = "Approved";
    //activity type
    public static final String ACTIVITY_READ = "Read";
    public static final String ACTIVITY_CREATE = "Creation";
    public static final String ACTIVITY_UPDATE = "Update";
    public static final String ACTIVITY_DELETE = "Deletion";
    public static final String ACTIVITY_APPROVE = "Approve";
    public static final String ACTIVITY_DECLINE = "Decline";
    public static final String ACTIVITY_DEACTIVATE = "Deactivate";
    public static final String ACTIVITY_ACTIVATION = "Activation";
    public static final String ACTIVITY_SCHEDULE = "Schedule";
    public static final String ACTIVITY_TASK = "CreateTask";
    public static final String ACTIVITY_CONFIRMATION = "Confirmation";
    public static final String ACTIVITY_FORWARD = "Forwarding";
    public static final String ACTIVITY_TALLYING = "Tallying";
    public static final String ACTIVITY_INITIATING = "Initiating";
    public static final String ACTIVITY_AUTHENTICATION = "Authentication";
    public static final String ACTIVITY_UNLOCK = "Unlock";
    public static final String ACTIVITY_LOCK = "Lock";
    public static final String ACTIVITY_AMEND = "Amend";
    public static final String ACTIVITY_RELEASE = "Release";
    public static final String ACTIVITY_DECOMMISSION = "Decommission";
    public static final String ACTIVITY_CANCEL ="Cancel";
    public static final String ACTIVITY_UPDATE_FILE ="UpdateCustomer";
    public static final String ACTIVITY_RESET_POS_PIN ="Reset Pin";
    public static final String ACTIVITY_ASSIGN_DEVICE ="Assign Device";

    public static final String AUTH_PASSWORD = "PASSWORD";

    public static final String SCAPI_CURRENCY_1 = "KES";
    public static final String SCAPI_CURRENCY_6 = "USD";
    public static final String SCAPI_CURRENCY_7 = "EUR";
    public static final String SCAPI_CURRENCY_8 = "GBP";

    public static final String SCAPI_ACC_STATUS_ACTIVE = "Active";
    public static final String SCAPI_ACC_STATUS_INACTIVE = "Inactive";
    public static final String SCAPI_ACC_STATUS_SUSPENDED = "Suspended";
    public static final String SCAPI_ACC_STATUS_OPEN = "Open";

    public static final String REQUEST_STAGE_INTIATED = "Initiated";
    public static final String REQUEST_STAGE_FORWARDED = "Forwarded";
    public static final String REQUEST_STAGE_CREW_SCHEDULING = "Crew Scheduled";
    public static final String REQUEST_STAGE_CREW_CONFIRMED = "Crew Confirmed";
    public static final String REQUEST_STAGE_AMEND = "Amended";
    public static final String REQUEST_STAGE_TALLYING = "Tallied";
    public static final String REQUEST_STAGE_POSTED = "Posted";

    public static final String REQUEST_TYPE_OFFLINE = "Offline";

    public static final String USER_TYPE_BANK_ADMIN = "Bank Admin";
    public static final String USER_TYPE_BANK_OPERATOR = "Bank Operator";
    public static final String USER_TYPE_CIT_AGENT = "CIT AgentWrapper";
    public static final String USER_TYPE_CUSTOMER = "Customer";
    public static final String USER_TYPE_TELLER = "CIT Teller";

    public static final String ROLE_BANK_ADMIN = "ROLE_BANK_ADMIN";
    public static final String ROLE_BANK_OPERATOR = "ROLE_BANK_OPERATOR";
    public static final String ROLE_CIT_AGENT = "ROLE_CIT_AGENT";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_CIT_TELLER = "ROLE_CIT_TELLER";
    
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_OPERATOR = "ROLE_OPERATOR";

    public static final BigDecimal CREW_TELLER_CATEGORY = new BigDecimal(4);
    public static final BigDecimal CREW_DRIVER_CATEGORY = new BigDecimal(3);
    public static final BigDecimal CREW_COMMANDER_CATEGORY = new BigDecimal(2);
//    public static final BigDecimal CREW_TELLER_CATEGORY = new BigDecimal(4);

    public static final String VEHICLE_LEAD_CAR = "Lead Car";
    public static final String VEHICLE_CHASE_CAR = "Chase Car";

    public static final String CHECKER_GENERAL_ERROR = "Some Actions could not be processed successfully check audit logs for more details";
    public static final String MSG_ACCOUNT_EXPIRED = "Sorry password has expired";
    public static final String MSG_EMAIL_VALIDATION = "Wrong email format expects username@domain.com";

    public static final String FILE_CATEGORY_CREW = "CIT_CREW";
    public static final String FILE_CATEGORY_VEHICLE = "CIT_VEHICLE";

    public static final String AMEND_TYPE_AMOUNT = "Request Amount";
    public static final String AMEND_TYPE_COUNTED = "Counted Amount";

    public static final String PERMISSION_VIEW_ALL_AUDIT_LOGS = "VIEW_ALL_AUDIT_LOGS";

    public static final String MSG_TEMPLATE_KEY_CUSTOMER_NAME = "${customerName}";
    public static final String MSG_TEMPLATE_KEY_TRANS_REF = "${transactionRef}";
    public static final String MSG_TEMPLATE_KEY_TRANS_AMOUNT = "${transactionAmount}";
    public static final String MSG_TEMPLATE_KEY_TRANS_CURRENCY = "${transactionCurrency}";
    public static final String MSG_TEMPLATE_KEY_NO_OF_VEHICLES = "${numberOfVehicles}";
    //    public static final String MSG_TEMPLATE_KEY_VEHICLE_REG = "${vehicleReg}";
    public static final String MSG_TEMPLATE_KEY_CHASE_REG = "${chaseCarReg}";
    public static final String MSG_TEMPLATE_KEY_LEAD_REG = "${leadCarReg}";
    //    public static final String MSG_TEMPLATE_KEY_STAFF_ID = "${staffId}";
    public static final String MSG_TEMPLATE_KEY_COMMANDER_STAFF_ID = "${commanderStaffId}";
    public static final String MSG_TEMPLATE_KEY_TELLER_STAFF_ID = "${tellerStaffId}";
    public static final String MSG_TEMPLATE_KEY_DRIVER_STAFF_ID = "${driverStaffId}";
    public static final String MSG_TEMPLATE_KEY_ACCOUNT_NUMBER = "${accountNumber}";
    public static final String MSG_TEMPLATE_KEY_ACCOUNT_NAME = "${accountName}";
    public static final String MSG_TEMPLATE_KEY_LOCATION = "${outletLocation}";
    public static final String MSG_TEMPLATE_KEY_REQUEST_DATE = "${requestDate}";
    public static final String MSG_TEMPLATE_KEY_COLLECTION_DATE = "${collectionDate}";
    public static final String MSG_TEMPLATE_KEY_INTIATED_BY = "${intiatedBy}";
    public static final String MSG_TEMPLATE_KEY_COLLECTED_BY = "${collectedBy}";
    public static final String MSG_TEMPLATE_KEY_AUTHORIZED_BY = "${authorizedBy}";
    public static final String MSG_TEMPLATE_KEY_CIT_AGENT_NAME = "${citAgentName}";
    public static final String MSG_TEMPLATE_KEY_CREW_DETAILS = "${crewDetails}";
    public static final String MSG_TEMPLATE_KEY_REQ_REF = "${requestRef}";
    public static final String MSG_TEMPLATE_KEY_REQ_AMOUNT = "${requestAmount}";
    public static final String MSG_TEMPLATE_KEY_REQ_CURRENCY = "${requestCurrency}";
    public static final String MSG_TEMPLATE_KEY_USERNAME = "${username}";
    public static final String MSG_TEMPLATE_KEY_PASSWORD = "${password}";

    public static final String DOC_ID = "ID";
    public static final String DOC_PASSPORT = "Passport";

    public static final String MSG_TYPE_EMAIL = "Email";
    public static final String MSG_TYPE_SMS = "SMS";

    //pin status
    public static final String PIN_STATUS_ACTIVE = "Active";
    public static final String PIN_STATUS_INACTIVE = "Inactive";

    //pos User constants
    public static final String POS_USER_TYPE = "Pos Agent";
    public static final String POS_USER_GENDER = "OTHER";
    public static final String POS_AGENT_DEPARTMENT = "POS AGENT";
    public static final String POS_USER_WORKGROUP = "Pos Admin";

    public static final String POS_SUPERVISOR_ROLE = "Supervisor";
    public static final String CRDB_BILLER_PREFIX = "orgPrefix";
    public static final String CRDB_HOSPITAL_CUSTOMER_TYPE = "Hospitals";
    public static final String CRDB_CUSTOMER_KEY_NAME = "businessName";
    public static final String CRDB_CUSTOMER_OUTLET_KEY_NAME = "outletName";


    public static final String TENANT_JSON_FILE_NAME = "tenant.json";
    public static final String APPLICATION_NOT_ATTACHED = "APPLICATION_NOT_ATTACHED";
    public static final String APPLICATION_NAME_ALREADY_EXIST = "APPLICATION_NAME_ALREADY_EXIST";
    public static final String FILE_NAME_NOT_SUPPORTED = "FILE_NAME_NOT_SUPPORTED";
    public static final String ZIP_FILE_NOT_CONTAIN_SUB_DIRECTORY = "ZIP_FILE_NOT_CONTAIN_SUB_DIRECTORY";
    public static final String UPLOAD_SYSTEM_ERROR = "UPLOAD_SYSTEM_ERROR";
    public static final String APP_ID_NOT_FOUND = "APP_ID_NOT_FOUND";
    public static final String FILE_EXTENSION_NOT_SUPPORTED = "FILE_EXTENSION_NOT_SUPPORTED";
    public static final String SUCCESS = "SUCCESS";
    public static final String MAKER_CANNOT_APPROVE_RECORD = "MAKER_CANNOT_APPROVE_RECORD";
    public static final String CSV_OR_EXCEL_REQUIRED = "CSV_OR_EXCEL_REQUIRED";
    public static final String UNIT_LEVEL_IN_USE = "UNIT_LEVEL_IN_USE";
    public static final String FAILED_TO_APPROVE_UNIT = "FAILED_TO_APPROVE_UNIT";
    public static final String MAKER_CANNOT_APPROVE_RECORD_WITH_ID = "MAKER_CANNOT_APPROVE_RECORD_WITH_ID";
    public static final String FAILED_TO_REJECT_HIERARCHY = "FAILED_TO_REJECT_HIERARCHY";
    public static final String BUSINESS_WITH_ID_NOT_FOUND = "BUSINESS_WITH_ID_NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String HIERARCHY_LIMIT_REACHED = "HIERARCHY_LIMIT_REACHED";
    public static final String ESTATE_NAME_IN_USE = "ESTATE_NAME_IN_USE";
    public static final String FAILED_TO_REJECT_BUSINESS_UNIT_ITEM = "FAILED_TO_REJECT_BUSINESS_UNIT_ITEM";
    public static final String RECORD_WITH_ID_NOT_FOUND = "RECORD_WITH_ID_NOT_FOUND";
    public static final String DEVICE_MAKE_NOT_MODIFIED = "DEVICE_MAKE_NOT_MODIFIED";
    public static final String NO_APPROPRIATE_ACTION = "NO_APPROPRIATE_ACTION";
    public static final String CHECKER_ERROR = "CHECKER_ERROR";
    public static final String FAILED_TO_APPROVE = "FAILED_TO_APPROVE";
    public static final String CONTACT_ADMIN = "CONTACT_ADMIN";
    public static final String DECLINE_FAIL_CONTACT_ADMIN = "DECLINE_FAIL_CONTACT_ADMIN";
    public static final String ENTITY_LACKS_ID = "ENTITY_LACKS_ID";
    public static final String CURRENCY_CODE_IN_USE = "CURRENCY_CODE_IN_USE";
    public static final String RECORD_WITH_SIMILAR_NAME_EXIST = "RECORD_WITH_SIMILAR_NAME_EXIST";
    public static final String RECORD_NOT_MODIFIED = "RECORD_NOT_MODIFIED";
    public static final String SOME_RECORDS_ID_NOT_FOUND = "SOME_RECORDS_ID_NOT_FOUND";
    public static final String MAKER_CANNOT_DECLINE_OWN_RECORD = "MAKER_CANNOT_DECLINE_OWN_RECORD";
    public static final String SUCCESS_STRING = "SUCCESS_STRING";
    public static final String RECORD_HAS_ALREADY_BEEN = "RECORD_HAS_ALREADY_BEEN";
    public static final String YOUR_USERNAME = "YOUR_USERNAME";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String TO_LOGIN_TO_POS = "TO_LOGIN_TO_POS";
    public static final String DEVICE_HAS_UNAPPROVED_ACTION = "DEVICE_HAS_UNAPPROVED_ACTION";
    public static final String DEVICE_HAS_BEEN_RELEASED = "DEVICE_HAS_BEEN_RELEASED";
    public static final String NO_APPROPRIATE_SERIAL_NO = "NO_APPROPRIATE_SERIAL_NO";
    public static final String AGENT_WRAPPER_WITH_SIMILAR_TID = "AGENT_WRAPPER_WITH_SIMILAR_TID";
    public static final String CUSTOMER_DETAILS_AGENT_NOT_FOUND = "CUSTOMER_DETAILS_AGENT_NOT_FOUND";
    public static final String DATA_NOT_FOUND = "DATA_NOT_FOUND";
}
