/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface ProductService {

    /**
     *
     * @param productName
     * @return
     */
    public UfsProduct findByProductName(String productName);

    /**
     *
     * @param product
     * @return
     */
    public UfsProduct saveProduct(UfsProduct product);

    /**
     *
     * @param productId
     * @return
     */
    public Optional<UfsProduct> getProduct(BigDecimal productId);

    /**
     *
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param pg
     * @return
     */
    public Page<UfsProduct> fetchProductsExclude(String status, String actionStatus, Date from, Date to, Pageable pg);

    /**
     *
     * @param productId
     * @return
     */
    public UfsProduct updateDeletedProducts(UfsProduct productId);
    
   
}
