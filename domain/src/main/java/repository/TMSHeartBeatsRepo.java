package repository;

import entities.TmsDeviceHeartbeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TMSHeartBeatsRepo extends JpaRepository<TmsDeviceHeartbeat, Long> {
}
