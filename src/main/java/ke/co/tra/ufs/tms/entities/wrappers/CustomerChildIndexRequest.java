package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CustomerChildIndexRequest implements Comparable<CustomerChildIndexRequest> {
    BigDecimal config;
    Integer configIndex;

    @Override
    public int compareTo(CustomerChildIndexRequest customerChildIndexRequest) {
        return config.compareTo(customerChildIndexRequest.getConfig());
    }
}
