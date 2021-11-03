/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsCurrency;
import ke.co.tra.ufs.tms.repository.CurrencyRepository;
import ke.co.tra.ufs.tms.service.CurrencyService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author OworiJuma
 */
@Service
@Transactional
public class CurrencyServiceTemplate implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceTemplate(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public UfsCurrency findByCurrencyCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public UfsCurrency saveCurrency(UfsCurrency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public Page<UfsCurrency> findByintrash(String intrash, Pageable pg) {
        return currencyRepository.findByintrash(intrash, pg);
    }

    @Override
    public Optional<UfsCurrency> findCurrency(BigDecimal currencyId) {
        return currencyRepository.findById(currencyId);
    }

    @Override
    public Page<UfsCurrency> fetchCurrenciesExclude(String status, String actionStatus, Date from, Date to, Pageable pg) {
        return currencyRepository.findAll(actionStatus, status, AppConstants.NO, pg);
    }

}
