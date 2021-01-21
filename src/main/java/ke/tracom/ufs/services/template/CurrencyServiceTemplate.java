package ke.tracom.ufs.services.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tracom.ufs.entities.UfsCurrency;
import ke.tracom.ufs.repositories.UfsCurrencyRepository;
import ke.tracom.ufs.services.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@Transactional
public class CurrencyServiceTemplate implements CurrencyService {

    private final UfsCurrencyRepository currencyRepository;

    public CurrencyServiceTemplate(UfsCurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public UfsCurrency saveCurrency(UfsCurrency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public UfsCurrency findByIdAndIntrash(BigDecimal id) {
        return currencyRepository.findByIdAndIntrash(id, AppConstants.NO);
    }

    @Override
    public UfsCurrency findByNameAndIntrash(String name, String intrash) {
        return currencyRepository.findByNameAndIntrash(name,intrash);
    }
}
