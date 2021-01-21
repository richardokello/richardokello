package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsRoleRepository extends CrudRepository<UfsRole, Long> {

    public UfsRole findByRoleIdAndIntrash(Long roleId, String intrash);

}
