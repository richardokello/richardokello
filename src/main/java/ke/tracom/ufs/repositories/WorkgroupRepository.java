/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsWorkgroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author eli.muraya
 */
@Repository
public interface WorkgroupRepository extends CrudRepository<UfsWorkgroup, Long> {

    Page<UfsWorkgroup> findAll(Pageable pg);

    @Override
    Optional<UfsWorkgroup> findById(Long aLong);

    public UfsWorkgroup findByGroupIdAndIntrash(Long groupId,String intrash);
    UfsWorkgroup findByUserId(long userid);
}
