package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParMenuItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParMenuItemRepository extends JpaRepository<ParMenuItems, BigDecimal> {
    List<ParMenuItems> findAllByIdIn(List<BigDecimal> items);
    Optional<ParMenuItems> findDistinctByNameAndCustomerTypeId(String name, BigDecimal type);
    List<ParMenuItems> findAllByParentIds(BigDecimal id);
    ParMenuItems findAllByIdAndIntrash(BigDecimal id, String intrash);

    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.customerTypeId, -1)) LIKE ?2% AND  STR(COALESCE(u.menuLevel,-1)) LIKE ?3% "
            + "AND u.dateCreated BETWEEN ?4 AND ?5 "
            + "AND (u.name LIKE %?6% OR u.description LIKE %?6% ) AND lower(u.intrash) = lower(?7) ")
    Page<ParMenuItems> findAllMenuItemsByCustomerType(String actionStatus, String customerTypeId,String menuLevel, Date from, Date to, String toLowerCase, String no, Pageable pg);
}
