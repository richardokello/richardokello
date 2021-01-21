/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.services;

import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.exceptions.NotFoundException;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Owori Juma
 */
public interface UserService {

    public void authenticateOTP(String code, String userId, Integer expiry) throws NotFoundException, ExpectationFailed;

    public ResponseWrapper editUser(UfsUser user);

    UfsUser findByUserId(Long id);

    UfsUser findByUserIdAndIntrash(Long id);

    UfsUser findByfullName(String name);

    public void save(UfsUser there);

    Optional<UfsUser> findById(Long id);

    void processApprove(List<UfsUser> users);

    public UfsAuthentication findByusernameIgnoreCase(String username);

    public UfsAuthentication findByusername(String username);

    public Page<UfsUser> fetchUsersExclude(UfsUser user, String actionStatus, Date from, Date to, String needle, Pageable pg);

    public Page<UfsUser> fetchUsersExcludes(UfsUser user, String actionStatus, Date from, Date to, String needle,Short statsus, Pageable pg);

    public Page<UfsUser> fetchUsersAction(UfsUser user, String actionStatus, Date from, Date to, String needle,String action, Pageable pg);

    public UfsAuthentication findByuserId(Long userId);

    public UfsAuthentication findByauthenticationId(Long id);

    public UfsAuthentication saveAuthentication(UfsAuthentication authentication);

    public Page<UfsUser> fetchUsersUserType(UfsUser user, String actionStatus, Date from, Date to, String needle, BigDecimal userTypeId, Pageable pg);
}
