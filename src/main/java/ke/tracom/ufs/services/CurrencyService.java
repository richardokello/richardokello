package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsCurrency;

import java.math.BigDecimal;

public interface CurrencyService {

    UfsCurrency saveCurrency(UfsCurrency currency);

    UfsCurrency findByIdAndIntrash(BigDecimal id);

    UfsCurrency findByNameAndIntrash(String name,String intrash);
}
