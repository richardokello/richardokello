/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsSysConfig;
import ke.tracom.ufs.entities.wrapper.PasswordConfig;
import ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Owori Juma
 */
public interface SysConfigService {

    /**
     * Used to fetch all System Config and sort them by page
     *
     * @param pg
     * @return
     */
    public Page<UfsSysConfig> fetchSysConfigs(Pageable pg);

    /**
     * Used to fetch all system config
     *
     * @param pg
     * @param excludePasswordConfig indicates if to exclude password
     *                              configurations
     * @return
     */
    public Page<UfsSysConfig> getSysConfigs(Pageable pg, boolean excludePasswordConfig);

    /**
     * Filter system configs excluding password configurations
     *
     * @param actionStatus
     * @param entity
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsSysConfig> getSysConfigs(String actionStatus, String entity, String needle, Pageable pg);

    /**
     * Used to search all system configurations
     *
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsSysConfig> searchSysConfigs(String needle, Pageable pg);

    /**
     * Used to fetch System Config by Id
     *
     * @param sysConfig
     * @return
     */
    public UfsSysConfig fetchSysConfigById(BigDecimal sysConfig);

    /**
     * Used to fetch System Config by entity and parameter
     *
     * @param entity
     * @param parameter
     * @return
     */
    public UfsSysConfig fetchSysConfig(String entity, String parameter);

    /**
     * Used to check if System Config exist by id, entity and parameter should
     * return a null if not
     *
     * @param id
     * @param entity
     * @param parameter
     * @return
     */
    public UfsSysConfig fetchSysConfigByEntityAndParameter(BigDecimal id, String entity, String parameter);

    /**
     * Used to save System Config by sysCon
     *
     * @param sysCon
     * @return
     */
    public UfsSysConfig saveSysConfig(UfsSysConfig sysCon);

    /**
     * Used to fetch all system config using entity
     *
     * @param pg
     * @param entity
     * @param excludePasswordConfig indicates if to exclude password
     *                              configurations
     * @return
     */
    public Page<UfsSysConfig> getSysConfigs(Pageable pg, String entity, boolean excludePasswordConfig);

    /**
     * Used to parse variables into the message
     *
     * @param message
     * @param variables
     * @return
     */
    public String previewTemplateMessage(String message, Map<String, Object> variables);

    /**
     * Get mail configuration
     *
     * @return
     */
    public List<UfsSysConfigWrapper> getMailConfig();

    /**
     * Used to fetch password configurations
     *
     * @return
     */
    public PasswordConfig passwordConfigs();


}
