package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.ParDeviceSelectedOptions;

import java.math.BigDecimal;

public interface ParDeviceSelectedOptionsService {
    ParDeviceSelectedOptions save(ParDeviceSelectedOptions selectedOption);

    void deleteAll(BigDecimal deviceId);

    Iterable<ParDeviceSelectedOptions> saveAll(Iterable<ParDeviceSelectedOptions> selectedOptions);
}
