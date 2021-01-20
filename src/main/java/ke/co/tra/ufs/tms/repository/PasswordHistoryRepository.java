/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsPasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface PasswordHistoryRepository extends CrudRepository<UfsPasswordHistory, BigDecimal> {

    /**
     *
     * @param user
     * @param pg
     * @return
     */
    List<UfsPasswordHistory> findByuserId(BigInteger userId, Pageable pg);
}
