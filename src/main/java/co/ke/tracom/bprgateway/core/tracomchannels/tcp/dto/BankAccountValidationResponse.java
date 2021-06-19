package co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
@AllArgsConstructor
public class BankAccountValidationResponse {
    private int status;
    private String currency;
    private String accountTitle;
    private String accountName;
    private String customerID;
    private String customerLegalIdentification;
    private String customerTownAndCountry;
    private String customerPhone;
    private String customerDOB;
    private String customerEmail;
    private String customerBusinessLocation;
    private String customerSector;
    private String workingBalance;
    private String availableBalance;
    private String inactiveMaker;
    private String accountPostingRestrictionSetting;
}
