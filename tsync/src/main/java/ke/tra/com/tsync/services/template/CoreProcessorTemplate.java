/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services.template;

import entities.TransactionTypes;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.hibernate.exception.JDBCConnectionException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.util.Optional;

/**
 *
 * @author Vincent
 */
public interface CoreProcessorTemplate {

    Optional<TransactionTypes> getTxnTypebyMTIAndProcodeAndActionstatusAndIntrash(String mti, String procode, String actionstatus, String intrash) throws JDBCConnectionException;

    ISOMsg setResponseMTI(ISOMsg isoMsg);

    ISOMsg setResponseDescription(ISOMsg isoMsg);

    PosUserWrapper terminalDataTLVMap(String fielddata);

    void saveOnlineActivity(ISOMsg isoMsg);

    ISOMsg processTransactionsbyMTI(ISOMsg isoMsg) throws ISOException , JDBCConnectionException;

    ISOMsg processTransactionAdvice(ISOMsg isoMsg);

}
