package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsPosRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsPosRoleRepository extends CrudRepository<UfsPosRole,Long> {

    UfsPosRole findByPosRoleNameAndIntrash(String posRole, String intrash);
}
