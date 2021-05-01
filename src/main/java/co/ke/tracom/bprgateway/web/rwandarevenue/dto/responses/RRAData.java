package co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RRAData {
    String taxTypeDescription;
    String declarationDate;
    int taxCentreNo;
    String TIN;
    int RRAOriginNo;
    long declarationID;
    String taxPayerName;
    String requestDate;
    String taxCentreDescription;
    int taxTypeNo;
    long assessNo;
    double amountToPay;
    String RRAReferenceNo;
}
