/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface BusinessUnitService {

    /**
     *
     * @param unitName
     * @param productId
     * @return
     */
    public TmsEstateHierarchy findByUnitName(String unitName, UfsProduct productId);
    
    /**
     *
     * @param unitId
     * @param productId
     * @return
     */
    public TmsEstateHierarchy findByUnitId(BigDecimal unitId, UfsProduct productId);

    /**
     *
     * @param productId
     * @param pg
     * @return
     */
    public Page<TmsEstateHierarchy> getUnitItem(UfsProduct productId, Pageable pg);

    /**
     *
     * @param businessUnit
     * @return
     */
    public TmsEstateHierarchy saveUnit(TmsEstateHierarchy businessUnit);

    /**
     *
     * @param unitId
     * @return
     */
    public Optional<TmsEstateHierarchy> getUnit(BigDecimal unitId);

    /**
     *
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param productId
     * @param pg
     * @return
     */
    public Page<TmsEstateHierarchy> fetchBusinessUnitsExclude(String status, String actionStatus, Date from, Date to, String productId, Pageable pg);
    
    /**
     *
     * @param levelNo
     * @param ufsProduct
     * @return
     */
    public TmsEstateHierarchy findByLevelNo(BigInteger levelNo, UfsProduct ufsProduct);

    /**
     *
     * @param unitId
     * @param name
     * @param intrash
     * @return
     */
    public TmsEstateItem findByLevelAndName(TmsEstateHierarchy unitId, String name);

    /**
     *
     * @param businessUnitItem
     * @return
     */
    public TmsEstateItem saveUnitItem(TmsEstateItem businessUnitItem);

    /**
     *
     * @param unitId
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param pg
     * @return
     */
    public Page<TmsEstateItem> findUnitsItemExclude(TmsEstateHierarchy unitId, String status, String actionStatus, Date from, Date to, Pageable pg);

    /**
     *
     * @param unitItemId
     * @return
     */
    public Optional<TmsEstateItem> getUnitItem(BigDecimal unitItemId);

    /**
     *
     * @return
     */
    public List<TmsEstateHierarchy> businessUnitsList();

    /**
     *
     * @param unitId
     * @return
     */
    public List<TmsEstateItem> businessUnitItemById(TmsEstateHierarchy unitId);

    /**
     *
     * @param parentId
     * @return
     */
    public List<TmsEstateItem> businessUnitItemByparentId(TmsEstateItem parentId);

    /**
     *
     * @return
     */
    public List<TmsEstateItem> getParentsUnitItemId();

    /**
     *
     * @param productId
     * @return
     */
    public List<TmsEstateHierarchy> findByproductIdOrderByLevelNo(UfsProduct productId);

}
