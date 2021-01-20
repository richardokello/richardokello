package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.repository.BusinessUnitItemRepository;
import ke.co.tra.ufs.tms.repository.BusinessUnitRepository;
import ke.co.tra.ufs.tms.service.BusinessUnitService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class BusinessUnitServiceTemplate implements BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;
    private final BusinessUnitItemRepository businessUnitItemRepository;

    public BusinessUnitServiceTemplate(BusinessUnitRepository businessUnitRepository, BusinessUnitItemRepository businessUnitItemRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitItemRepository = businessUnitItemRepository;
    }

    @Override
    public TmsEstateHierarchy findByUnitName(String unitName, UfsProduct productId) {
        return businessUnitRepository.findByunitNameAndProductIdAndIntrash(unitName, productId, AppConstants.NO);
    }

    @Override
    public TmsEstateHierarchy saveUnit(TmsEstateHierarchy businessUnit) {
        return businessUnitRepository.save(businessUnit);
    }

    @Override
    public Optional<TmsEstateHierarchy> getUnit(BigDecimal unitId) {
        return businessUnitRepository.findById(unitId);
    }

    @Override
    public Page<TmsEstateHierarchy> fetchBusinessUnitsExclude(String status, String actionStatus, Date from, Date to, String productId, Pageable pg) {
        return businessUnitRepository.findAll(status, actionStatus, from, to, AppConstants.NO, productId, pg);
    }

    @Override
    public TmsEstateHierarchy findByLevelNo(BigInteger levelNo, UfsProduct productId) {
        return businessUnitRepository.findBylevelNoAndProductId(levelNo, productId);
    }

    @Override
    public TmsEstateItem findByLevelAndName(TmsEstateHierarchy unitId, String name) {
        return businessUnitItemRepository.findByLevelAndName(unitId, name, AppConstants.NO);
    }

    @Override
    public TmsEstateItem saveUnitItem(TmsEstateItem businessUnitItem) {
        return businessUnitItemRepository.save(businessUnitItem);
    }

    @Override
    public Page<TmsEstateItem> findUnitsItemExclude(TmsEstateHierarchy unitId, String status, String actionStatus, Date from, Date to, Pageable pg) {
        return businessUnitItemRepository.findByunitIdAndintrash(unitId, AppConstants.NO, pg);
    }

    @Override
    public Optional<TmsEstateItem> getUnitItem(BigDecimal unitItemId) {
        return businessUnitItemRepository.findById(unitItemId);
    }

    @Override
    public List<TmsEstateHierarchy> businessUnitsList() {
        return businessUnitRepository.findByintrash(AppConstants.NO);
    }

    @Override
    public List<TmsEstateItem> businessUnitItemById(TmsEstateHierarchy unitId) {
        return businessUnitItemRepository.findbyunitId(unitId, AppConstants.NO);
    }

    @Override
    public List<TmsEstateItem> businessUnitItemByparentId(TmsEstateItem parentId) {
        return businessUnitItemRepository.findbyparentId(parentId, AppConstants.NO);
    }

    @Override
    public List<TmsEstateItem> getParentsUnitItemId() {
        return businessUnitItemRepository.findbyAllRootParents(AppConstants.NO);
    }

    @Override
    public Page<TmsEstateHierarchy> getUnitItem(UfsProduct productId, Pageable pg) {
        return businessUnitRepository.findByproductIdOrderByUnitIdAsc(productId, pg);
    }

    @Override
    public List<TmsEstateHierarchy> findByproductIdOrderByLevelNo(UfsProduct productId) {
        return businessUnitRepository.findByproductIdOrderByLevelNoDesc(productId);
    }

    @Override
    public TmsEstateHierarchy findByUnitId(BigDecimal unitId, UfsProduct productId) {
        return businessUnitRepository.findByunitIdAndProductIdAndIntrash(unitId, productId, AppConstants.NO);
    }
}
