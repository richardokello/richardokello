package co.ke.tracom.bprgateway.web.util;

public interface PaymentProcessorInterface {

    void inquiry();

    void authorize();

    void checkProcessingStatus();
}
