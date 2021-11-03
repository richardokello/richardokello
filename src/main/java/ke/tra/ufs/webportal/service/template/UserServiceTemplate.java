/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsUser;
import ke.tra.ufs.webportal.repository.UserRepository;
import ke.tra.ufs.webportal.service.UserService;

import org.springframework.stereotype.Service;

/**

 *
 * @author eli.muraya
 */
@Service
public class UserServiceTemplate implements UserService {

    private final UserRepository userRepo;

      public UserServiceTemplate(UserRepository userRepo) {
          this.userRepo = userRepo;
     }

    @Override
    public UfsUser findByUserId(Long id) {
        return this.userRepo.findByUserId(id);
    }
}
