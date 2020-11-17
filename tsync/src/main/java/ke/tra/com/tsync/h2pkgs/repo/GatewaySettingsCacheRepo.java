package ke.tra.com.tsync.h2pkgs.repo;

import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewaySettingsCacheRepo extends CrudRepository<GeneralSettingsCache, Long> {}
