/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author eli.muraya
 */
@Repository
public interface UserRepository extends CrudRepository<UfsUser, Long> {
    UfsUser findByUserId(Long ids);
    UfsUser findByEmail(String email);
    UfsUser findByfullName(String name);

    UfsUser findByuserId(Long id);

    public void save(Optional<UfsUser> there);

    UfsUser findByUserIdAndIntrash(Long id, String intrash);

    @Query("SELECT u FROM #{#entityName} u WHERE  u.actionStatus like ?1% "
            + "AND u.creationDate BETWEEN ?2 AND ?3 AND "
            + "(lower(u.fullName) LIKE %?4% OR lower(u.phoneNumber) LIKE %?4% OR lower(u.email) LIKE %?4%) AND lower(u.intrash) = lower(?5) AND u != ?6")
    public Page<UfsUser> findAll(String actionStatus,Date from, Date to, String needle, String intrash,UfsUser user, Pageable pg);

    @Query("SELECT u FROM #{#entityName} u WHERE  u.actionStatus like ?1% "
            + "AND u.creationDate BETWEEN ?2 AND ?3 AND "
            + "(lower(u.fullName) LIKE %?4% OR lower(u.phoneNumber) LIKE %?4% OR lower(u.email) LIKE %?4%) AND lower(u.intrash) = lower(?5) AND u != ?6 AND u.status = ?7")
    public Page<UfsUser> findAllByStatus(String actionStatus,Date from, Date to, String needle, String intrash,UfsUser user,Short status, Pageable pg);

    @Query("SELECT u FROM #{#entityName} u WHERE  u.actionStatus like ?1% "
            + "AND u.creationDate BETWEEN ?2 AND ?3 AND "
            + "(lower(u.fullName) LIKE %?4% OR lower(u.phoneNumber) LIKE %?4% OR lower(u.email) LIKE %?4%) AND lower(u.intrash) = lower(?5) AND u != ?6 AND u.action = ?7")
    public Page<UfsUser> findAllByAction(String actionStatus,Date from, Date to, String needle, String intrash,UfsUser user,String action, Pageable pg);

    @Query("SELECT u FROM #{#entityName} u WHERE  u.actionStatus like ?1% "
            + "AND u.creationDate BETWEEN ?2 AND ?3 AND "
            + "(lower(u.fullName) LIKE %?4% OR lower(u.phoneNumber) LIKE %?4% OR lower(u.email) LIKE %?4%) AND lower(u.intrash) = lower(?5) AND u != ?6 AND u.userTypeId = ?7")
    Page<UfsUser> findAllByUserTypeId(String actionStatus, Date from, Date to, String toLowerCase, String no, UfsUser user, BigDecimal userTypeId, Pageable pg);
    @Procedure("REPLICATE_USER_INFO")
    void replicateUserInfo(@Param("userEmail") String email);
}
