package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsUserWorkgroup;

import java.util.List;

public interface UserWorkGroupService {
    List<UfsUserWorkgroup> findAllByUserId(Long userid);
}
