package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.TmsDeviceHeartbeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TMSHeartBeatsRepo extends JpaRepository<TmsDeviceHeartbeat, Long> {
}
