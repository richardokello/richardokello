/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.custominterfaces;

import java.util.Optional;

/**
 *
 * @author Tracom
 */
public interface ScAccountstatementsRepositoryInterfce {

    Optional<String> postToOfflineCBS(
            String in_txnreference,
            String in_txntypeid,
            String in_principalamount,
            String in_taxamount,
            String in_txnchargeamount,
            String in_agentfloataccount,
            String in_agentcommissionaccount,
            String in_debitbranchcode,
            String in_creditaccount
    );
}
