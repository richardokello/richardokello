package co.ke.tracom.bprgatewaygen2.web.ltss.service;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;

public class LtssServiceTestHelper {

    public static NationalIDValidationRequest generateNationalIdRequestObj() {
        NationalIDValidationRequest requestObj = new NationalIDValidationRequest();
        requestObj.setIdentification("1198680013535151");
        return requestObj;
    }

    public static NationalIDValidationResponse generateNationalIdResponseObj() {
        NationalIDValidationResponse responseObj = new NationalIDValidationResponse();
        responseObj.setIdentification("1198680013535151");
        responseObj.setName("RWABUHUNGU Eric");
        return responseObj;
    }

    public static PaymentContributionRequest generatePaymentContributionRequestObj() {
        PaymentContributionRequest requestObj = new PaymentContributionRequest();
        requestObj.setAmount("5000");
        NationalIDValidationRequest beneficiary = new NationalIDValidationRequest();
        beneficiary.setIdentification("1198680013535151");
        requestObj.setBeneficiary(beneficiary);
        requestObj.setDescription("Contribution testing");
        requestObj.setIntermediary("XYZ LTD");
        requestObj.setExtReferenceNo("REFNO-0015F002");
        requestObj.setPaymentDate("2018-05-24 12:25:44");
        return requestObj;
    }

    public static PaymentContributionResponse generatePaymentContributionResponseObj() {
        PaymentContributionResponse responseObj = new PaymentContributionResponse();

        return responseObj;
    }

    public static NewSubscriberRequest generateSubscriberRequestObj() {
        NewSubscriberRequest requestObj = new NewSubscriberRequest();
        requestObj.setIdentification("1198770015450184");
        requestObj.setPhone("0731112523");
        requestObj.setOccupation("1");
        requestObj.setFrequency(1);
        requestObj.setAmount(5000);



        return requestObj;
    }

    public static NewSubscriberResponse generateSubscriberResponseObj() {
        NewSubscriberResponse responseObj = new NewSubscriberResponse();
        responseObj.setStatus("200");
        responseObj.setMessage("OK");
        responseObj.setIdentification("1198770015450184");
        responseObj.setPhone("0731112523");
        responseObj.setFrequency(1);
        responseObj.setOccupation("1");
        return responseObj;
    }

    public static CheckPaymentRequest generateCheckPaymentRequestObj() {
        CheckPaymentRequest requestObj = new CheckPaymentRequest();

        return requestObj;
    }
}
