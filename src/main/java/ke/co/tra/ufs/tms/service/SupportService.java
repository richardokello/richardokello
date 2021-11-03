/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Owori Juma
 */
public interface SupportService {
    /**
     * Used to save edited changes
     * @param record
     * @return 
     */
    public UfsModifiedRecord saveEditedChanges(UfsModifiedRecord record);
    /**
     * Used to clear existing data before saving
     * @param record
     * @return 
     */
    public UfsModifiedRecord clearAndSaveEditedChanges(UfsModifiedRecord record);
    /**    
     * Used to fetch edit entity
     * @param entity
     * @param entityId
     * @return 
     */
    public UfsModifiedRecord fetchByEntityAndEntityId(String entity, String entityId);
    /**
     * Used to deleted edited record
     * @param record 
     */
    public void delete(UfsModifiedRecord record);    
    /**
     * Used to move cash collection request from temp to actual table. Implement using cron job preferably
     */
    public void updateCompletedRequests();
    
    /**
     * search for entities using value key
     * @param <T>
     * @param entity
     * @param valueKey
     * @param clazz
     * @param canRemoveRecord
     * @return 
     */
    public <T> Set<T> searchEntities(String entity, String valueKey, Class<T> clazz, boolean canRemoveRecord);
    
    /**
     * Fetch editted record by entity and value search string
     * @param cmsEntity
     * @param valueSnippet
     * @return 
     */
    public List<UfsModifiedRecord> getRecords(String cmsEntity, String valueSnippet);

}
