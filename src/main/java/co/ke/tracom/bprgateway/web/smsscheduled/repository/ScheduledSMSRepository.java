package co.ke.tracom.bprgateway.web.smsscheduled.repository;

import co.ke.tracom.bprgateway.web.smsscheduled.entities.ScheduledSMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduledSMSRepository extends CrudRepository<ScheduledSMS, Long> {
    Optional<ScheduledSMS> findBySentstatus(int aLong);
}
