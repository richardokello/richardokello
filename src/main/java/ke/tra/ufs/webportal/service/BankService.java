package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.entities.UfsBanks;

import java.util.List;

public interface BankService {

    void saveAllBins(List<UfsBankBins> bins);

    UfsBanks findByNameOrCode(String bankName, String bankCode);
}
