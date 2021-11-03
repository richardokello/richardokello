/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsUser;



/**
 * @author Owori Juma
 */
public interface UserService {


    UfsUser findByUserId(Long id);



}
