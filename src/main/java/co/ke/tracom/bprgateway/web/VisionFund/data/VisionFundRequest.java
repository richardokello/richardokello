package co.ke.tracom.bprgateway.web.VisionFund.data;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.Credentials;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VisionFundRequest {
    @JsonProperty("bill")
    private String bill;

    @JsonProperty("billSubCategory")
    private String billSubCategory;

    @JsonProperty("tnxType")
    private String tnxType;

    @JsonProperty("data")
    private List<TransactionData> data = null;

    @JsonProperty("credentials")
    private Credentials credentials;

    @JsonProperty("svcCode")
    private String svcCode;

    @JsonProperty("field")
    private String field;

    @JsonProperty("value")
    private String value;
}
