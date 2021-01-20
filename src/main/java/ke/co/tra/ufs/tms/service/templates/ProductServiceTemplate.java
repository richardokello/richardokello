/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.repository.BusinessUnitItemRepository;
import ke.co.tra.ufs.tms.repository.BusinessUnitRepository;
import ke.co.tra.ufs.tms.repository.ProductRepository;
import ke.co.tra.ufs.tms.service.ProductService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class ProductServiceTemplate implements ProductService {

    private final ProductRepository productRepository;
    private final BusinessUnitItemRepository businessUnitItemRepository;
    private final BusinessUnitRepository businessUnitRepository;

    public ProductServiceTemplate(ProductRepository productRepository, BusinessUnitItemRepository businessUnitItemRepository, BusinessUnitRepository businessUnitRepository) {
        this.productRepository = productRepository;
        this.businessUnitItemRepository = businessUnitItemRepository;
        this.businessUnitRepository = businessUnitRepository;
    }

    @Override
    public UfsProduct findByProductName(String productName) {
        return productRepository.findByproductNameIgnoreCaseAndIntrash(productName, AppConstants.NO);
    }

    @Override
    public UfsProduct saveProduct(UfsProduct product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<UfsProduct> getProduct(BigDecimal productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Page<UfsProduct> fetchProductsExclude(String status, String actionStatus, Date from, Date to, Pageable pg) {
        return productRepository.findAll(status, actionStatus, from, to, AppConstants.NO, pg);
    }

    @Override
    @Async
    public UfsProduct updateDeletedProducts(UfsProduct productId) {
        businessUnitRepository.findByproductIdOrderByLevelNoDesc(productId).forEach(businessUnit -> {
            List<TmsEstateItem> bunit = businessUnitItemRepository.findbyunitId(businessUnit, AppConstants.NO);
            bunit.forEach(item -> {
                item.setIntrash(AppConstants.YES);
                businessUnitItemRepository.save(item);
            });
        });
        return productId;
    }

}
