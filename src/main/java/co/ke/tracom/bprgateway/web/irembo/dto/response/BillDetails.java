package co.ke.tracom.bprgateway.web.irembo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BillDetails {
    @JsonProperty("biller_code")
    private String billerCode;

    @JsonProperty("bill_number")
    private String billNumber;

    @JsonProperty("description")
    private String description;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("customer_id_number")
    private String customerIdNumber;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("received_date")
    private String receivedDate;

    @JsonProperty("business_date")
    private String businessDate;

    @JsonProperty("rra_account_number")
    private String rraAccountNumber;

    @JsonProperty("rra_account_name")
    private String rraAccountName;

    @JsonProperty("service_name")
    private String serviceName;

}
