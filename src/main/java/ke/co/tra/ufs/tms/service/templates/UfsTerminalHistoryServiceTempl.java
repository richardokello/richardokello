package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.UfsTerminalHistory;
import ke.co.tra.ufs.tms.repository.UfsTerminalHistoryRepository;
import ke.co.tra.ufs.tms.service.UfsTerminalHistoryService;
import org.springframework.stereotype.Service;


@Service
public class UfsTerminalHistoryServiceTempl implements UfsTerminalHistoryService {

    private final UfsTerminalHistoryRepository terminalHistoryRepository;

    public UfsTerminalHistoryServiceTempl(UfsTerminalHistoryRepository terminalHistoryRepository) {
        this.terminalHistoryRepository = terminalHistoryRepository;
    }

    @Override
    public void saveHistory(UfsTerminalHistory ufsTerminalHistory) {
         terminalHistoryRepository.save(ufsTerminalHistory);
    }
}
