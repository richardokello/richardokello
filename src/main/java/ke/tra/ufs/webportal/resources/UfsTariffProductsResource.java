package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsTariffProducts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigInteger;

@RestController
@RequestMapping("/tariff_products")
public class UfsTariffProductsResource extends ChasisResource<UfsTariffProducts, BigInteger, UfsEdittedRecord> {

    public UfsTariffProductsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @PostMapping
    @Override
    public ResponseEntity<ResponseWrapper<UfsTariffProducts>> create(@Valid @RequestBody UfsTariffProducts ufsTariffProducts) {
        // generate product code
        ufsTariffProducts.setProductCode(generateProductCode(ufsTariffProducts));
        return super.create(ufsTariffProducts);
    }

    private String generateProductCode(UfsTariffProducts product) {
        return "UB-"+ product.getName().trim() + "-001";
    }
}
