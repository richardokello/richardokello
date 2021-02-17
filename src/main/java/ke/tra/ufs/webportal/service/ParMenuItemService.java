package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.ParMenuItems;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ParMenuItemService {
    void updateParents(List<BigDecimal> menuItems);
    Optional<ParMenuItems> findByNameAndCustomerType(String name, BigDecimal type);
    List<ParMenuItems> getAllChildren(BigDecimal id);
    void deleteChildren(List<ParMenuItems> parMenuItemsList);
}
