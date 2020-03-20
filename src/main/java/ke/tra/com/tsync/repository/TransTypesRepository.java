/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.repository;

import java.math.BigDecimal;
import java.util.Optional;
import ke.tra.com.tsync.entities.TransactionTypes;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tracom
 */

@Repository
public interface TransTypesRepository extends JpaRepository<TransactionTypes, BigDecimal>{

    /**
     * 
     * @param mti
     * @param procode // configure table to ensure a unique combination of procode and MTIs
     * @return 
     */
    TransactionTypes findByTxnMtiAndTxnProcodeAndActionStatusAndIntrash(String mti, String procode, String actionstatus , String intrash) throws JDBCConnectionException;
}
