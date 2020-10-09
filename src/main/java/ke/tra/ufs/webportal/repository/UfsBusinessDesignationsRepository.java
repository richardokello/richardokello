package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBusinessDesignations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsBusinessDesignationsRepository extends CrudRepository<UfsBusinessDesignations,Long> {

    UfsBusinessDesignations findByDesignationAndIntrash(String designation,String intrash);
}
