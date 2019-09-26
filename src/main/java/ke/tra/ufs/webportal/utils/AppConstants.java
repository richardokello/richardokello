package ke.tra.ufs.webportal.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class AppConstants {

    public static final Short STATUS_PENDING = 1;
    public static final Short STATUS_ACTIVE = 2;
    public static final Short STATUS_SUCCESS = 3;
    public static final Short STATUS_FAILED = 4;
    public static final Short STATUS_EXPIRED = 5;
    public static final Short STATUS_LOCKED = 6;
    public static final String YES = "YES";
    public static final String NO = "NO";
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
    public static final String ACTIVITY_CONFIRMATION = "Confirmation";
    public static final String ACTIVITY_FORWARD = "Forwarding";
    public static final String ACTIVITY_TALLYING = "Tallying";
    public static final String ACTIVITY_INITIATING = "Initiating";
    public static final String ACTIVITY_AUTHENTICATION = "Authentication";
    public static final String ACTIVITY_STATUS_FAILED = "Failed";
    public static final String ACTIVITY_SUSPEND = "Suspend";
    public static final String ACTIVITY_TERMINATION = "Termination";
    public static final String ACTIVITY_UNLOCK = "Unlock";
    public static final String ACTIVITY_LOCK = "Lock";
    public static final String ACTIVITY_AMEND = "Amend";


    //status
    public static final String STATUS_INACTIVE = "Inactive";
    public static final String STATUS_DISABLED = "Disabled";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_UNAPPROVED = "Unapproved";
    public static final String STATUS_NEW = "New";
    public static final String STATUS_REJECTED = "Rejected";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_DEACTIVATED = "Deactivated";
    public static final String STATUS_VERIFIED = "Verified";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_REPAIR = "Repair";
    public static final String STATUS_REALLOCATE = "Reallocate";
    public static final String STATUS_UNCONFIRMED = "Unconfirmed";
    public static final String INTRASH_NO = "NO";
    public static final String INTRASH_YES = "YES";

    public static final String STATUS_STRING_PENDING = "Pending";




    //    USER TYPES
    public static final String USER_TYPE_BACKOFFICE_USER = "Back Office User";
    public static final String USER_TYPE_REGIONAL_MANAGER = "Regional Manager";
    public static final String USER_TYPE_AGENT_SUPERVISOR = "Agent Supervisor";
    public static final String USER_TYPE_HEAD_OF_DISTRIBUTION = "Head Of Distribution";
    public static final String USER_TYPE_BRANCH_MANAGER = "Branch Manager";

    //    AUTHENTICATION TYPES
    public static final String AUTH_TYPE_PASSWORD = "PASSWORD";

    //    FILES URLs
    public static final String PROFILE_PIC_PARAM = "profilePictureUpload";
    public static final String PROFILE_PIC_ENTITY = "Global Configuration";

    public static final Marker AUDIT_LOG = MarkerFactory.getMarker("AUDIT_LOG");

    public static final String PARAMETER_OTP_EXPIRY = "otpExpiry";
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
    public static final String ENTITY_SYSTEM_INTEGRATION = "System Integration";
    public static final String ENTITY_GLOBAL_INTEGRATION = "Global Configuration";
    public static final String ENTITY_MESSAGE_TEMPLATES = "Message Templates";

    //transaction type
    public static final String TRANSACTION_TYPE_WITHDRAWAL = "Withdrawal";
    public static final String TRANSACTION_TYPE_DEPOSIT = "Deposit";

}
