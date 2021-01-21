package ke.tracom.ufs.services.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tracom.ufs.entities.UfsCountries;
import ke.tracom.ufs.repositories.UfsCountryRepository;
import ke.tracom.ufs.services.CountriesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@Transactional
public class CountriesServiceTemplate implements CountriesService {

    private final UfsCountryRepository ufsCountryRepository;

    public CountriesServiceTemplate(UfsCountryRepository ufsCountryRepository) {
        this.ufsCountryRepository = ufsCountryRepository;
    }

    @Override
    public UfsCountries saveCountry(UfsCountries countries) {
        return ufsCountryRepository.save(countries);
    }

    @Override
    public UfsCountries findByIdAndIntrash(BigDecimal id) {
        return ufsCountryRepository.findByIdAndIntrash(id, AppConstants.NO);
    }
}
