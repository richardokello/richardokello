package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.wrappers.CustomerIndexRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigKeysService {
    List<BigDecimal> save(List<CustomerIndexRequest> indices);
}
