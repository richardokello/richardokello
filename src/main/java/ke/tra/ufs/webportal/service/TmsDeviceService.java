package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;

import java.math.BigDecimal;
import java.util.List;

public interface TmsDeviceService {

    TmsDevice findByDeviceId(BigDecimal deviceId);

    List<TmsDevice> findByOutletIds(List<BigDecimal> outletIds);

    public TmsDevice findByDeviceIdAndIntrash(BigDecimal id);

    public Integer findAllActiveDevices();


    void activateDevicesByOutlets(List<UfsCustomerOutlet> customerOutlets, String notes);

    void activateDevicesByOutletsIds(List<Long> customerOutlets, String notes);

    void approveContactPersons(Long customerId, String notes);

    void updateDeviceOwnerByOutletId(List<Long> customerOutlets, String customerOwnerName);

    void updateContactPersonsDetails(UfsCustomer entity);
}
