/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.config;


import ke.tra.com.tsync.repository.PosIrisRepo;
import ke.tra.com.tsync.repository.TmsDeviceRepository;
import ke.tra.com.tsync.repository.UfsPosAuditLogRepository;
import ke.tra.com.tsync.repository.UfsPosUserRepository;
import ke.tra.com.tsync.services.template.CoreProcessorTemplate;

/**
 * @author Owori Juma
 */
public interface SpringContextBridgedServices {

    CoreProcessorTemplate getcoreProcessor();

    UfsPosUserRepository getPOSUserRepo();

    TmsDeviceRepository getTmsDeviceRepo();
    PosIrisRepo getPosIrisRepo();
    UfsPosAuditLogRepository getPosAuditLogRepo();


}
