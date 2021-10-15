package co.ke.tracom.bprgateway.core.tracomchannels.tcp;

import co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto.BankAccountValidationResponse;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto.EUCLMeterValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class T24MessageProcessor {

    public EUCLMeterValidationResponse parseT24ResponseForMeterNoValidation(String t24Response) {
        EUCLMeterValidationResponse response = EUCLMeterValidationResponse.builder().build();
        String[] output = t24Response.split(",");

        if (output.length > 0) {
            if (output[0].equals("0112")) {
                String[] customerData = output[2].split("\t");
                response.setCustomerName(customerData[0].replace("\"", "").trim());
                response.setMeterLocation(customerData[1].replace("\"", "").trim());
                response.setStatus(HttpStatus.SC_OK);
            } else {
                response.setStatus(HttpStatus.SC_BAD_GATEWAY);
                response.setFailNarration(output[3].replace("\"", ""));
            }
        }
        return response;
    }


    public BankAccountValidationResponse parseT24ResponseForBankAccountValidation(String t24Response) {
        BankAccountValidationResponse response = BankAccountValidationResponse.builder().build();
        String[] responseMessageArray = t24Response.substring(4).split(",");
        String[] enquiryResponseArray = responseMessageArray[2].split("\\|");

        for (int i = 0; i < enquiryResponseArray.length - 1; i++) {
            if (i == 0) {
                String[] currencyArray = enquiryResponseArray[0].split("#");
                String currency =
                        currencyArray.length > 1 ? currencyArray[1].toUpperCase() : enquiryResponseArray[0];
                response.setCurrency(currency);

            } else if (i == 1) {
                response.setAccountTitle(enquiryResponseArray[1]);
                response.setAccountName(enquiryResponseArray[1]);

            } else if (i == 2) {
                response.setCustomerID(enquiryResponseArray[2]);
            } else if (i == 3) {
                response.setWorkingBalance(enquiryResponseArray[3]);
                response.setAvailableBalance(enquiryResponseArray[3]);
            } else if (i == 4) {
                response.setInactiveMaker(enquiryResponseArray[4]);
            } else if (i == 5) {
                response.setAccountPostingRestrictionSetting(enquiryResponseArray[5]);
            } else if (i == 6) {
                response.setCustomerLegalIdentification(enquiryResponseArray[6]);
            } else if (i == 7) {
                response.setCustomerTownAndCountry(enquiryResponseArray[7]);
            } else if (i == 8) {
                response.setCustomerPhone(enquiryResponseArray[8]);
            } else if (i == 9) {
                response.setCustomerDOB(enquiryResponseArray[9]);
            } else if (i == 10) {
                response.setCustomerEmail(enquiryResponseArray[10]);
            } else if (i == 11) {
                response.setCustomerBusinessLocation(enquiryResponseArray[11]);
            } else if (i == 12) {
                response.setCustomerSector(enquiryResponseArray[12]);
            }
        }
        if(response.getAccountName() == null || response.getAccountName().isEmpty()) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        } else {
            response.setStatus(HttpStatus.SC_OK);
        }
        log.info("Account validation processing: " + response.toString());
        return response;
    }

}
