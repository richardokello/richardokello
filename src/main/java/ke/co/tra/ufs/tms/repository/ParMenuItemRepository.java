package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParMenuItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParMenuItemRepository extends JpaRepository<ParMenuItems, BigDecimal> {
    List<ParMenuItems> findAllByIdIn(List<BigDecimal> items);
    Optional<ParMenuItems> findDistinctByNameAndCustomerTypeId(String name, BigDecimal type);
    List<ParMenuItems> findAllByParentIds(BigDecimal id);
    ParMenuItems findAllByIdAndIntrash(BigDecimal id, String intrash);
}
