package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.wrapper.PosUserWrapper;

public interface PosUserIdGenerator {
    String generateUsername(PosUserWrapper wrapper);
}
