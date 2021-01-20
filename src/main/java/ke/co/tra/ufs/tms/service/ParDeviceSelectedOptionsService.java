package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParDeviceSelectedOptions;

import java.math.BigDecimal;

public interface ParDeviceSelectedOptionsService {
    ParDeviceSelectedOptions save(ParDeviceSelectedOptions selectedOption);

    void deleteAll(BigDecimal deviceId);

    Iterable<ParDeviceSelectedOptions> saveAll(Iterable<ParDeviceSelectedOptions> selectedOptions);
}
