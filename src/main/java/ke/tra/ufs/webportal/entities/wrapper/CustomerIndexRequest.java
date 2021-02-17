package ke.tra.ufs.webportal.entities.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerIndexRequest implements Comparable<CustomerIndexRequest> {
    BigDecimal config;
    Integer configIndex;
    List<CustomerChildIndexRequest> childIndices;

    @Override
    public int compareTo(CustomerIndexRequest customerIndexRequest) {
        return config.compareTo(customerIndexRequest.config);
    }
}
