package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ke.co.tra.ufs.tms.utils.annotations.NickName;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Used to perform simple actions; create an entity, update, delete, approve
 * actions and decline actions
 *
 * @version 0.0.1
 * @author Cornelius M
 * @param <T> action entity
 */
public class ChasisResourceLocal<T> {

    protected LoggerServiceLocal loggerService;
    protected EntityManager entityManager;
    protected SupportRepository supportRepo;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public ChasisResourceLocal(LoggerServiceLocal loggerService, EntityManager entityManager,
                               SupportRepository supportRepo) {
        this.loggerService = loggerService;
        this.entityManager = entityManager;
        this.supportRepo = supportRepo;
    }

    /**
     * Create a new entity
     *
     * @param t
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Create New Record")
    public ResponseEntity<ResponseWrapper<T>> create(@Valid @RequestBody T t) {
        log.debug("Creating new record for entity " + this.getClass());
        ResponseWrapper response = new ResponseWrapper();
        //check if relational entities exists
        PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                Object relEntity = accessor.getPropertyValue(field.getName());
                for (Field f2 : relEntity.getClass().getDeclaredFields()) {
                    if (f2.isAnnotationPresent(Id.class)) {
                        BigDecimal id = (BigDecimal) accessor.getPropertyValue(f2.getName());
                        if (entityManager.find(relEntity.getClass(), id) == null) {
                            NickName nickName = relEntity.getClass().getDeclaredAnnotation(NickName.class);
                            if (nickName != null) {
                                this.loggerService.logCreate(nickName.name() + " with id " + id + " doesn't exist", t.getClass().getSimpleName(), null, AppConstants.STATUS_FAILED);
                                response.setMessage(nickName.name() + " with id " + id + " doesn't exist");
                            } else {
                                this.loggerService.logCreate("Record with id " + id + " doesn't exist", t.getClass().getSimpleName(), null, AppConstants.STATUS_FAILED);
                                response.setMessage("Record with id " + id + " doesn't exist");
                            }
                            response.setCode(404);
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                        }
                    }
                }
            }
        }
        accessor.setPropertyValue("actionStatus", AppConstants.STATUS_UNAPPROVED);
        accessor.setPropertyValue("action", AppConstants.ACTIVITY_CREATE);
        accessor.setPropertyValue("intrash", AppConstants.NO);
        NickName nickName = t.getClass().getDeclaredAnnotation(NickName.class);
        String recordName = (nickName == null) ? "Record" : nickName.name();
        entityManager.persist(t);
        this.loggerService.logCreate("Created " + recordName + " successfully",
                t.getClass().getSimpleName(), SharedMethods.getEntityIdValue(t), AppConstants.STATUS_COMPLETED);
        response.setData(t);
        response.setCode(201);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Fetch single record using record id")
    public ResponseEntity<ResponseWrapper<T>> getEntity(@PathVariable("id") BigDecimal modelId) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.fetchEntity(modelId));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Update record")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Record not found")
            ,
            @ApiResponse(code = 417, message = "Record has unapproved actions or if record has not been modified")
    })
    @Transactional
    public ResponseEntity<ResponseWrapper<T>> updateEntity(@RequestBody @Valid T t) throws IllegalAccessException, JsonProcessingException {
        ResponseWrapper<T> response = new ResponseWrapper();
        NickName nickName = t.getClass().getDeclaredAnnotation(NickName.class);
        String recordName = (nickName == null) ? "Record" : nickName.name();
        T dbT = this.fetchEntity(SharedMethods.getEntityIdValue(t));
        if (dbT == null) {
            loggerService.logUpdate("Updating " + recordName + " failed due to record doesn't exist", t.getClass().getSimpleName(),
                    null, AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate record with the specified id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(dbT);
        /*if ((AppConstants.STATUS_UNAPPROVED).equalsIgnoreCase((String) accessor.getPropertyValue("actionStatus"))) {
            loggerService.logUpdate("Updating " + recordName + " failed due to record has unapproved actions",
                    t.getClass().getSimpleName(), SharedMethods.getEntityIdValue(t), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry record has Unapproved actions");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }*/
        accessor.setPropertyValue("action", AppConstants.ACTIVITY_UPDATE);
        accessor.setPropertyValue("actionStatus", AppConstants.STATUS_UNAPPROVED);
        this.entityManager.merge(dbT);

        this.supportRepo.setMapping(t.getClass());
        if (supportRepo.handleEditRequest(t, UfsModifiedRecord.class) == false) {
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry device make has not been modified");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        } else {
            response.setData(t);
        }
        return ResponseEntity.ok(response);

    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete record")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Some records could not be processed successfully")
    })
    public ResponseEntity deleteEntity(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        Class clazz = SharedMethods.getGenericClasses(this.getClass()).get(0);
        NickName nickName = (NickName) clazz.getDeclaredAnnotation(NickName.class);
        String recordName = (nickName == null) ? "Record" : nickName.name();
        for (BigDecimal id : actions.getIds()) {
            T t = this.fetchEntity(id);
            if (t == null) {
                loggerService.logDelete("Deleting " + recordName + " failed due to record doesn't exist", clazz.getSimpleName(),
                        null, AppConstants.STATUS_FAILED);
                errors.add(recordName + " with id " + id + " doesn't exist");
                continue;
            }
            PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
            /*if (accessor.getPropertyValue("actionStatus").toString().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                loggerService.logDelete("Failed to delete " + recordName + ". Record has unapproved actions",
                        t.getClass().getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Record has unapproved actions");
            } else {
                accessor.setPropertyValue("action", AppConstants.ACTIVITY_DELETE);
                accessor.setPropertyValue("actionStatus", AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted " + recordName + " successfully", clazz.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
            }*/
            accessor.setPropertyValue("action", AppConstants.ACTIVITY_DELETE);
            accessor.setPropertyValue("actionStatus", AppConstants.STATUS_UNAPPROVED);
            loggerService.logDelete("Deleted " + recordName + " successfully", clazz.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }

    }

    @ApiOperation(value = "Approve Record Actions")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Some records could not be processed successfully")
    })
    @RequestMapping(value = {"/approve-actions", "/confirm-actions"}, method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();

        Class clazz = SharedMethods.getGenericClasses(this.getClass()).get(0);
        NickName nickName = (NickName) clazz.getDeclaredAnnotation(NickName.class);
        String recordName = (nickName == null) ? "Record" : nickName.name();
        List<String> errors = new ErrorList();

        for (BigDecimal id : actions.getIds()) {
            T t = this.fetchEntity(id);
            try {
                if (t == null) {
                    loggerService.logApprove("Failed to approve " + recordName + ". Failed to locate record with specified id",
                            clazz.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(recordName + " with id " + id + " doesn't exist");
                    continue;
                }

                PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
                String action = accessor.getPropertyValue("action").toString();
                String actionStatus = accessor.getPropertyValue("actionStatus").toString();
//                log.debug("==================> Proccessing entity action {} and status {}", action, actionStatus);

                if (loggerService.isInitiator(clazz.getSimpleName(), id, action) && !actionStatus.equalsIgnoreCase("Unconfirmed")) {
                    errors.add("Sorry maker can't approve their own record ");
                    loggerService.logApprove("Failed to approve " + recordName + ". Maker can't approve their own record",
                            SharedMethods.getEntityName(clazz), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    this.processApproveNew(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_APPROVED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processApproveChanges(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_APPROVED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processApproveDeletion(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_APPROVED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.ACTION_STATUS_UNCONFIRMED)) {
                    this.processConfirm(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_UNAPPROVED);
                } else {
                    loggerService.logApprove("Failed to approve " + recordName + ". Record doesn't have approve actions",
                            clazz.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add("Record doesn't have approve actions");
                }
                this.entityManager.merge(t);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveNew(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        loggerService.logApprove("Done approving new  " + nickName + "",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveChanges(BigDecimal id, T entity, String notes, String nickName) throws ExpectationFailed {
        try {
            supportRepo.setMapping(entity.getClass());
            if (supportRepo.mergeChanges(id, UfsModifiedRecord.class) == null) {
                throw new ExpectationFailed("Failed to approve " + nickName + ". Changes not found");
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to approve record changes", ex);
            throw new ExpectationFailed("Failed to approve record changes please contact the administrator for more help");
        }
        loggerService.logApprove("Done approving " + nickName + " changes",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveDeletion(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(entity);
        accessor.setPropertyValue("intrash", AppConstants.YES);
        loggerService.logApprove("Done approving " + nickName + " deletion.",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    private void processConfirm(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        loggerService.logApprove("Done confirmation " + nickName + ".",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    @ApiOperation(value = "Decline Record Actions")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Some records could not be processed successfully")
    })
    @RequestMapping(value = {"/decline-actions","/deny-actions"}, method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();

        Class clazz = SharedMethods.getGenericClasses(this.getClass()).get(0);
        NickName nickName = (NickName) clazz.getDeclaredAnnotation(NickName.class);
        String recordName = (nickName == null) ? "Record" : nickName.name();
        List<String> errors = new ErrorList();

        for (BigDecimal id : actions.getIds()) {
            T t = this.fetchEntity(id);
            try {
                if (t == null) {
                    loggerService.logApprove("Failed to decline " + recordName + ". Failed to locate record with specified id",
                            clazz.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(recordName + " with id " + id + " doesn't exist");
                    continue;
                }

                PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
                String action = accessor.getPropertyValue("action").toString();
                String actionStatus = accessor.getPropertyValue("actionStatus").toString();

                if (loggerService.isInitiator(clazz.getSimpleName(), id, action) && !action.equalsIgnoreCase("Unconfirmed")) {
                    errors.add("Sorry maker can't approve their own record ");
                    loggerService.logApprove("Failed to approve " + recordName + ". Maker can't approve their own record",
                            SharedMethods.getEntityName(clazz), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    this.processDeclineNew(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_DECLINED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processDeclineChanges(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_DECLINED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processDeclineDeletion(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("actionStatus", AppConstants.STATUS_DECLINED);
                } else if (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && actionStatus.equalsIgnoreCase(AppConstants.ACTION_STATUS_UNCONFIRMED)) {
                    this.processDeclineConfirmation(id, t, actions.getNotes(), recordName);
                    accessor.setPropertyValue("intrash", AppConstants.YES);
                    accessor.setPropertyValue("actionStatus", AppConstants.ACTION_STATUS_REJECTED);
                } else {
                    loggerService.logApprove("Failed to decline " + recordName + ". Record doesn't have approve actions",
                            clazz.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add("Record doesn't have approve actions");
                }
                this.entityManager.merge(t);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineNew(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        loggerService.logApprove("Declined new " + nickName + "",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineChanges(BigDecimal id, T entity, String notes, String nickName) throws ExpectationFailed {
        try {
            supportRepo.setMapping(entity.getClass());
            supportRepo.declineChanges(entity, UfsModifiedRecord.class);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to decline record changes", ex);
            throw new ExpectationFailed("Failed to decline record changes please contact the administrator for more help");
        }
        loggerService.logApprove("Done declining " + nickName + " changes",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineDeletion(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        loggerService.logApprove("Done declining " + nickName + " deletion.",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    private void processDeclineConfirmation(Object id, T entity, String notes, String nickName) throws ExpectationFailed {
        loggerService.logApprove("Declined ocnfirmation " + nickName + ".",
                entity.getClass().getSimpleName(), id, AppConstants.STATUS_COMPLETED, notes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/changes")
    @ApiOperation(value = "Fetch  Changes")
    public ResponseEntity<ResponseWrapper<List<String>>> fetchChanges(@PathVariable("id") BigDecimal id) {
        ResponseWrapper<List<String>> response = new ResponseWrapper();
        supportRepo.setMapping(SharedMethods.getGenericClasses(this.getClass()).get(0));
        response.setData(supportRepo.fetchChanges(id, UfsModifiedRecord.class));
        return ResponseEntity.ok(response);
    }

    /**
     *
     /*
     * Fetch entity excluding entities in trash
     *
     * @param id
     * @return
     */
    private T fetchEntity(Object id) {
        Class clazz = SharedMethods.getGenericClasses(this.getClass()).get(0);
        //get id field name
        String fieldId = null;
        boolean hasIntrash = false;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldId = field.getName();
            }
            if (field.getName().equalsIgnoreCase("intrash")) {
                hasIntrash = true;
            }
        }

        if (fieldId == null) {
            throw new RuntimeException("Entity doesn't have an id field");
        }

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        if (hasIntrash) {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(fieldId), id),
                    criteriaBuilder.equal(root.get("intrash"), AppConstants.NO)));
        } else {
            criteriaQuery.where(criteriaBuilder.equal(root.get(fieldId), id));
        }
        try {
            return this.entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    private T getEntity(BigDecimal id, Map<String, Object> properties) {
        Class clazz = SharedMethods.getGenericClasses(this.getClass()).get(0);
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.where(criteriaBuilder.equal(root.get("modelId"), id));
//        response.setData(this.entityManager.createQuery(criteriaQuery).getSingleResult());   
        try {
            this.entityManager.createQuery(criteriaQuery).getResultList();
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

}
