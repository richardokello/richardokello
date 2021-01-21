package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsCountries;

import java.math.BigDecimal;

public interface CountriesService {

    UfsCountries saveCountry(UfsCountries countries);

    UfsCountries findByIdAndIntrash(BigDecimal id);
}
