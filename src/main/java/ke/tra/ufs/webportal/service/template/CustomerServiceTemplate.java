package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.repository.CustomerRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOwnerRepository;
import ke.tra.ufs.webportal.service.CustomerService;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import ke.tra.ufs.webportal.utils.AppConstants;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@CommonsLog
public class CustomerServiceTemplate implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UfsCustomerOwnerRepository customerOwnerRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;
    private final TmsDeviceService deviceService;

    public CustomerServiceTemplate(CustomerRepository customerRepository, UfsCustomerOwnerRepository customerOwnerRepository,
                                   UfsCustomerOutletRepository customerOutletRepository, TmsDeviceService deviceService) {
        this.customerRepository = customerRepository;
        this.customerOwnerRepository = customerOwnerRepository;
        this.customerOutletRepository = customerOutletRepository;
        this.deviceService = deviceService;
    }

    @Override
    public UfsCustomer findByCustomerId(Long id) {
        return customerRepository.findByCustomerId(id);
    }

    @Override
    public UfsCustomer saveCustomer(UfsCustomer customer) {
        return this.customerRepository.save(customer);
    }


    @Override
    public List<UfsCustomer> getAllTerminatedAgents(String action, String actionStatus) {
        return this.customerRepository.findByActionAndActionStatus(action, actionStatus);
    }

    @Override
    public List<UfsCustomerOwners> getAllCustomerOwners() {
        return ((List<UfsCustomerOwners>) this.customerOwnerRepository.findAll());
    }

    @Override
    public UfsCustomerOutlet findByOutletCode(String outletCode) {
        return customerOutletRepository.findByOutletCode(outletCode);
    }

    @Override
    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerIds) {
        return customerOutletRepository.findByCustomerIdsAndIntrash(customerIds, AppConstants.INTRASH_NO);
    }

    @Override
    public List<UfsCustomerOutlet> findOutletsByCustomerIds(BigDecimal customerIds) {
        return customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerIds, AppConstants.INTRASH_NO);
    }

    @Override
    public void saveOutlet(UfsCustomerOutlet customerOutlet) {
        customerOutletRepository.save(customerOutlet);
    }

    @Override
    @Async
    public void updateCustomersMids() {
        List<UfsCustomer> customers = customerRepository.findAllByMidIsNull();
        log.error("Customers SIze====>" + customers.size());
        customers.parallelStream().forEach(this::updateCustomerMidPerId);
    }

    @Override
    public void updateCustomerMidPerId(UfsCustomer customer) {
        Long id = customer.getId();
        //approving customer outlet
        List<UfsCustomerOutlet> customerOutlets = findOutletsByCustomerIds(new BigDecimal(id));
        List<BigDecimal> outletIds = (customerOutlets.size() > 0) ? customerOutlets.stream().map(x -> new BigDecimal(x.getId())).collect(Collectors.toList()) : new ArrayList<>();
        List<TmsDevice> devices = (outletIds.size() > 0) ? deviceService.findByOutletIds(outletIds) : new ArrayList<>();

        Set<String> mids = new HashSet<>();
        for (TmsDevice device : devices) {
            List<TmsDeviceTidCurrency> tmsDeviceTids = deviceService.findByDeviceIds(device);
            for (TmsDeviceTidCurrency curr : tmsDeviceTids) {
                if (curr.getMid() != null) {
                    mids.add(curr.getMid());
                }
            }
        }
        customer.setMid(String.join(";", mids));
        saveCustomer(customer);
    }

    @Override
    public boolean findIfMidIsActive(String mid, String intrashNo) {
        Set<String> mids = new LinkedHashSet<>();
        if (mid.contains(";")) {
            String[] md = mid.split(";");
            Stream.of(md).forEach(m -> {
                if (!m.isEmpty()) {
                    mids.add(m);
                }
            });
        } else {
            mids.add(mid);
        }
        Integer midCount = deviceService.findByListMidCount(mids);
        return midCount > 0;
    }
}
