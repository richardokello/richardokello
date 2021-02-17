package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.wrapper.CustomerIndexRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigKeysService {
    List<BigDecimal> save(List<CustomerIndexRequest> indices);
}
