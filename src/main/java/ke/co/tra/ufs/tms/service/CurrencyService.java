/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsCurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface CurrencyService {

    /**
     *
     * @param currencyCode
     * @return
     */
    public UfsCurrency findByCurrencyCode(String currencyCode);

    /**
     *
     * @param currency
     * @return
     */
    public UfsCurrency saveCurrency(UfsCurrency currency);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<UfsCurrency> findByintrash(String intrash, Pageable pg);

    /**
     *
     * @param currencyId
     * @return
     */
    public Optional<UfsCurrency> findCurrency(BigDecimal currencyId);

    /**
     *
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param pg
     * @return
     */
    public Page<UfsCurrency> fetchCurrenciesExclude(String status, String actionStatus, Date from, Date to, Pageable pg);

}
