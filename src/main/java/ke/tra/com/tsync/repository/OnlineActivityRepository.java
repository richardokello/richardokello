package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.OnlineActivity;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineActivityRepository extends JpaRepository<OnlineActivity, BigDecimal> {
}
