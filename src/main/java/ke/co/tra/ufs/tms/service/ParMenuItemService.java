package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParMenuItems;
import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ParMenuItemService {
    void updateParents(List<BigDecimal> menuItems);
    Optional<ParMenuItems> findByNameAndCustomerType(String name, BigDecimal type);
    List<ParMenuItems> getAllChildren(BigDecimal id);
    void deleteChildren(List<ParMenuItems> parMenuItemsList);

    Page<ParMenuItems> getMenuItemByCustomertype(String actionStatus, String customerTypeId,String menuLevel, Date from, Date to, String needle, Pageable pg);
}
