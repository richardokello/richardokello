/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.repository.ModifiedRecordRepository;
import ke.co.tra.ufs.tms.repository.SysConfigRepository;
import ke.co.tra.ufs.tms.service.SupportService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class SupportServiceTemplate implements SupportService {

    private final ModifiedRecordRepository editedEntityRepo;
    private final SysConfigRepository configRepo;
    private final Logger log;

    public SupportServiceTemplate(ModifiedRecordRepository editedEntityRepo, SysConfigRepository configRepo) {
        this.editedEntityRepo = editedEntityRepo;
        this.configRepo = configRepo;
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public UfsModifiedRecord saveEditedChanges(UfsModifiedRecord record) {
        return editedEntityRepo.save(record);
    }

    @Override
    public UfsModifiedRecord clearAndSaveEditedChanges(UfsModifiedRecord record) {
        //clear data
        if (record.getId() == null) {
            editedEntityRepo.deleteByUfsEntityAndEntityId(record.getUfsEntity(), record.getEntityId());
        } else {
            editedEntityRepo.deleteById(record.getId());
        }
        return editedEntityRepo.save(record);
    }

    @Override
    public UfsModifiedRecord fetchByEntityAndEntityId(String entity, String entityId) {
        return editedEntityRepo.findByUfsEntityAndEntityId(entity, entityId);
    }

    @Override
    public void delete(UfsModifiedRecord record) {
        editedEntityRepo.delete(record);
    }

    @Override
    @Scheduled(cron = "0 05 01 * * * ")
    public void updateCompletedRequests() {
        log.info(AppConstants.AUDIT_LOG,
                "\n----------------------------------------\n"
                + "Running Cash Collection Update Task\n"
                + "----------------------------------------");

    }

    @Override
    public <T> Set<T> searchEntities(String entity, String valueKey, Class<T> clazz, boolean canRemoveRecord) {
        log.debug("Searching for entity {} using key value {} from Editted Record ", entity, valueKey);
        List<UfsModifiedRecord> results = this.editedEntityRepo.findByUfsEntityAndValuesContaining(entity, valueKey);
        Set<T> entities = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        results.forEach(r -> {
            try {
                entities.add(mapper.readValue(r.getValues(), clazz));
            } catch (IOException ex) {
                log.error(AppConstants.AUDIT_LOG, "Failed to convert string {} to entity {}", r.getValues(), clazz, ex);
            }
        });
        if (canRemoveRecord) {
            this.editedEntityRepo.deleteAll(results);
        }
        return entities;
    }

    @Override
    public List<UfsModifiedRecord> getRecords(String cmsEntity, String valueSnippet) {
        return this.editedEntityRepo.findByUfsEntityAndValuesContaining(cmsEntity, valueSnippet);
    }

}
