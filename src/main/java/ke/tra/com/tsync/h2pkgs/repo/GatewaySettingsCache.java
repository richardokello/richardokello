package ke.tra.com.tsync.h2pkgs.repo;

import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewaySettingsCache extends CrudRepository<GeneralSettingsCache, Long> {


   // @Query(value = "SELECT 2 AS key, current_database() AS database_name ", nativeQuery=true)
    //Db2Entity getDb2Entity();

}