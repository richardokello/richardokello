package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.service.UfsCustomerOutletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UfsCustomerOutletServiceTempl implements UfsCustomerOutletService {

    private final UfsCustomerOutletRepository customerOutletRepository;

    public UfsCustomerOutletServiceTempl(UfsCustomerOutletRepository customerOutletRepository) {
        this.customerOutletRepository = customerOutletRepository;
    }

    @Override
    public UfsCustomerOutlet findByCustomerId(BigDecimal customerId) {
        return this.customerOutletRepository.findByCustomerIds(customerId);
    }

    @Override
    public UfsCustomerOutlet findById(Long outletId) {
        return customerOutletRepository.findByIdAndIntrash(outletId, AppConstants.NO);
    }

    @Override
    public UfsCustomerOutlet saveOutlet(UfsCustomerOutlet outlet) {
        return customerOutletRepository.save(outlet);
    }

    @Override
    public UfsCustomerOutlet findByOutletId(Long id) {
        return customerOutletRepository.findByIdAndIntrash(id, AppConstants.NO);
    }

    @Override
    public List<UfsCustomerOutlet> findByCustomerId(BigDecimal customerId, String intrash) {
        return customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerId, intrash);
    }

    @Override
    public List<UfsCustomerOutlet> findByCustomerIdIn(List<BigDecimal> customerId, String intrash) {
        return customerOutletRepository.findOutletsByCustomerIdsInAndIntrash(customerId, intrash);
    }

    @Override
    public void deleteByCustomerId(BigDecimal customerId) {
        List<UfsCustomerOutlet> customerOutlets = customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerId, AppConstants.NO);
        List<UfsCustomerOutlet> customerOutletsUpdated = customerOutlets.parallelStream().peek(x -> {
            x.setIntrash(AppConstants.NO);
        }).collect(Collectors.toList());
        customerOutletRepository.saveAll(customerOutletsUpdated);
    }

    @Override
    public Page<UfsCustomerOutlet> getOutletByCustomerId(String actionStatus, String customerIds, Date from, Date to, String needle, Pageable pg) {
        return this.customerOutletRepository.findAllByCustomerId(actionStatus, customerIds,from,to, needle.toLowerCase(), AppConstants.NO,  pg);

    }
}
