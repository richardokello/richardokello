package repository;

import java.math.BigDecimal;

import entities.OnlineActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineActivityRepository extends JpaRepository<OnlineActivity, BigDecimal> {
}
