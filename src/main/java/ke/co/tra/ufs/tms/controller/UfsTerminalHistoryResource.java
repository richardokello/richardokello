package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.UfsTerminalHistory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping("/terminal-history")
public class UfsTerminalHistoryResource extends ChasisResource<UfsTerminalHistory,Long, UfsEdittedRecord> {

    public UfsTerminalHistoryResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
