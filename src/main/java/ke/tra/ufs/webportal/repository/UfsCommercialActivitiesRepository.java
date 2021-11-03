package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCommercialActivities;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsCommercialActivitiesRepository extends CrudRepository<UfsCommercialActivities,Long> {
    UfsCommercialActivities findByCommercialActivityAndIntrash(String commercialActivity,String intrash);
}
