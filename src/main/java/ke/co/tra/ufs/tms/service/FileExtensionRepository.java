/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsDeviceFileExt;
import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Cornelius M
 */
public interface FileExtensionRepository extends CrudRepository<TmsDeviceFileExt, BigDecimal> {

    public List<TmsDeviceFileExt> findByappFileExtAndModelId(String fileExt, BigDecimal modelId);

    Optional<TmsDeviceFileExt> findDistinctByModelId(BigDecimal modelId);

    Optional<TmsDeviceFileExt> findByModelId(BigDecimal modelId);

}
