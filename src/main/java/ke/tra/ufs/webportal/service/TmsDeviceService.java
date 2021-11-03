package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface TmsDeviceService {

    TmsDevice findByDeviceId(BigDecimal deviceId);

    List<TmsDevice> findByOutletIds(List<BigDecimal> outletIds);

    List<TmsDeviceTids> findByDeviceIds(Long deviceIds);

    List<TmsDeviceTidCurrency> findByDeviceIds(TmsDevice deviceIds);

    public TmsDevice findByDeviceIdAndIntrash(BigDecimal id);

    public Integer findAllActiveDevices();

    void activateDevicesByOutlets(List<UfsCustomerOutlet> customerOutlets, String notes);

    void activateDevicesByOutletsIds(List<Long> customerOutlets, String notes);

    void approveContactPersons(Long customerId, String notes);

    void updateDeviceOwnerByOutletId(List<Long> customerOutlets);

    void updateContactPersonsDetails(UfsCustomer entity);

<<<<<<< HEAD
    void saveDevice(TmsDevice device);
=======
    void addDevicesTaskByOutletsIds(List<Long> outletsIds);

    void deActivateDevicesByOutlets(List<UfsCustomerOutlet> customerOutlets, String notes);

    void delineContactPersons(Long id, String notes);

    void updateDeviceOwnersByContactPersons(List<Long> contactPersonId);

    Integer findByMidCount(String mid);

    Integer findByListMidCount(Set<String> mid);
>>>>>>> brb-webportal
}
