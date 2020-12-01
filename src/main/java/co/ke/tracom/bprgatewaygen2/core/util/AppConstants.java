package co.ke.tracom.bprgatewaygen2.core.util;

public enum AppConstants {

    /**
     * TySync -
     */
    TRANSACTION_SUCCESS_STANDARD("00", "Transaction processed successfully"),
    TRANSACTION_SUCCESS_ISO8583("000", "Transaction processed successfully"),
    EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST("05", "An exception occurred while sending request to external party"),

    /**
     * Academic Bridge API Response codes
     */
    ACADEMIC_BRIDGE_INFO_NO_ERROR("0", "No error"),
    ACADEMIC_BRIDGE_INVALID_CREDENTIALS("1", "No error"),
    ACADEMIC_BRIDGE_STUDENT_NOT_FOUND("2", "Student not found"),
    ACADEMIC_BRIDGE_INFO_INVALID_BILL_NUMBER("3", "Invalid Bill Number"),

    ACADEMIC_BRIDGE_PAYMENT_SAVED("0", "No error, payment saved"),
    ACADEMIC_BRIDGE_PAYMENT_REF_EXISTS("3", "Reference number already exists"),
    ACADEMIC_BRIDGE_PAYMENT_INVALID_BILL_NO("4", "Invalid bill number"),
    ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR("20", "Something went wrong"),

    ACADEMIC_BRIDGE_PAYMENT_STATUS_FOUND("0", "No error, payment found"),
    ACADEMIC_BRIDGE_PAYMENT_STATUS_INVALID_CREDENTIALS("1", "Invalid credentials"),
    ACADEMIC_BRIDGE_PAYMENT_STATUS_NOT_FOUND("2", "Payment of that reference number not found")
    ;

    private final String value;
    private final String reasonPhrase;

    private AppConstants(String value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
