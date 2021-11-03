/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.annotations.EditDataWrapper;
import ke.co.tra.ufs.tms.utils.annotations.EditEntity;
import ke.co.tra.ufs.tms.utils.annotations.EditEntityId;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;
//import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

/**
 *
 * @author Owori Juma
 * @param <T>
 * @param <E>
 */
@Component
public class SupportRepository<T, E> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private T _entity;
    private Class<T> _mapping;

    public final T getEntity() {
        return _entity;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public final Class<T> getMapping() {
        return _mapping;
    }

    public final void setMapping(Class<T> mapping) {
        _mapping = mapping;
    }

    /**
     * Retrieve an instance of hibernate session from EntityManager
     *
     * @return
     */
    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Used to intialize collections with ids
     *
     * @param ids
     * @return
     */
    public List<T> intializeEntityCollection(List<?> ids) {
        List<T> records = new ArrayList<>();
        if (ids == null) {
            return records;
        }
        ids.forEach(record -> {
            try {
                records.add(_mapping.getConstructor(record.getClass()).newInstance(record));
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //java.util.logging.Logger.getLogger(FunctionsUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
        return records;
    }

    /**
     * Binds an instance of a request(HttpServletRequest) to an entity
     *
     * @param request Current request
     * @return T an instance of the entity in question
     */
    public T handleRequests(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Iterator it = map.entrySet().iterator();
        BeanWrapper wrapper = new BeanWrapperImpl(_mapping);

        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
            String key = entry.getKey();
            String[] values = entry.getValue();

            //Invalid property exception will be thrown if the key above does not exist
            //within the list of 'params' for this class
            //TODO: Check if the key is valid
            //The following method may produce unexpected result: though its choice is driven
            //by its behavior; it doesn't throw exceptions
            if (false != wrapper.isReadableProperty(key)) {

                if (values.length > 1) {
                    //A collection of indices has been found
                    //Handle this according to the design adopted in an application

                } //When the key has a single value
                else {
                    wrapper.setPropertyValue(key, values[0]);
                }
            }
        }

        //Suppose we want to 'auto-validate' our request?
        return (T) wrapper.getWrappedInstance();
    }

    /**
     * Checks if changes were made if true it persists changes to entity storage
     *
     * @param entity current entity
     * @param editEntity entity storage
     * @return boolean true if changes were persisted false if changes were not
     * found
     * @throws java.lang.IllegalAccessException if it fails to locate Id field
     * @throws com.fasterxml.jackson.core.JsonProcessingException if it fails to
     * save the current entity to entity storage
     */
    public boolean _handleEditRequest(T entity, Class<E> editEntity) throws IllegalAccessException, JsonProcessingException {
        //Get an instance of the entity with new values
        Map<String, Object> map;
        BigDecimal index = null;
        boolean isModified = false;

        if (null != entity) {
            //Check if entity has been modified
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    index = (BigDecimal) field.get(entity);
                }
            }
            if (index == null) {
                //Do nothing
            } else {

                T oldEntity = (T) getSession().get(_mapping, index);
                if (oldEntity == null) {
                    return false;
                }
                ///if (this._fetchChanges(oldEntity, entity) )
                Map<String, List> changes = this.fetchChanges(oldEntity, entity);
                isModified = changes != null && !changes.get("data").isEmpty();
                //If there are changes, update this field
                if (true) {
                    BeanWrapper wrapper = new BeanWrapperImpl(editEntity);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.addMixIn(Object.class, DynamicMixIn.class);
                    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    //ignore entities
                    Set<String> ignoreProperties = new HashSet<>();
                    for (Field field : oldEntity.getClass().getDeclaredFields()) {
                        if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class)) {
                            ignoreProperties.add(field.getName());
                        }
                    }

                    FilterProvider filters = new SimpleFilterProvider()
                            .addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
                    mapper.setFilterProvider(filters);

                    String data = mapper.writeValueAsString(oldEntity);
                    Criteria c = getSession().createCriteria(editEntity);

                    for (Field field : editEntity.getDeclaredFields()) {
                        if (field.isAnnotationPresent(EditEntity.class)) {
                            wrapper.setPropertyValue(field.getName(), entity.getClass().getSimpleName());
                            c.add(Restrictions.eq(field.getName(), entity.getClass().getSimpleName()));
                        } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                            wrapper.setPropertyValue(field.getName(), data);
                        } else if (field.isAnnotationPresent(EditEntityId.class)) {
                            if (field.getType().isAssignableFrom(String.class)) {
                                wrapper.setPropertyValue(field.getName(), index.toString());
                                c.add(Restrictions.eq(field.getName(), index.toString()));
                            } else {
                                wrapper.setPropertyValue(field.getName(), index);
                                c.add(Restrictions.eq(field.getName(), index));
                            }

                        }
                    }
                    List<E> results = (List<E>) c.list();
                    results.forEach((e) -> {
                        //clear data
                        getSession().delete(e);
                    });
                    E e = (E) wrapper.getWrappedInstance();
                    //check if exists in the database
                    getSession().save(e);
                    //update current bean
                    getSession().saveOrUpdate(this.updateEdit(oldEntity, entity));
                }
            }
        }

        return isModified;
    }

    public boolean handleEditRequest(T entity, Class<E> editEntity) throws IllegalAccessException, JsonProcessingException {
        //Get an instance of the entity with new values
        Map<String, Object> map;
        BigDecimal index = null;
        boolean isModified = true;

        if (null != entity) {
            //Check if entity has been modified
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    index = (BigDecimal) field.get(entity);
                }
            }
            if (index == null) {
                //Do nothing
            } else {

                T oldEntity = (T) getSession().get(_mapping, index);
                if (oldEntity == null) {
                    return false;
                }
                //Compare values
//                map = fetchChanges(oldEntity, entity);
//                List<Object> result = (List<Object>) map.get("data");
//                isModified = (result.size() > 0);
                //If there are changes, update this field
                if (isModified) {
                    BeanWrapper wrapper = new BeanWrapperImpl(editEntity);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.addMixIn(Object.class, DynamicMixIn.class);
                    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//                    mapper.writer().withoutAttribute("role");
//                    mapper.writer().withoutAttribute("user");
                    //ignore entities
                    Set<String> ignoreProperties = new HashSet<>();
                    for (Field field : oldEntity.getClass().getDeclaredFields()) {
                        if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class)) {
//                            mapper.writer().withoutAttribute(field.getName());
                            ignoreProperties.add(field.getName());
                        }
                    }

                    FilterProvider filters = new SimpleFilterProvider()
                            .addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
                    mapper.setFilterProvider(filters);

                    //String data = mapper.writeValueAsString(entity);
                    String data = mapper.writeValueAsString(oldEntity);
//                    log.debug("Done converting entity to json string: " + data);
                    Criteria c = getSession().createCriteria(editEntity);

                    for (Field field : editEntity.getDeclaredFields()) {
//                        if(field.isAnnotationPresent(Id.class)){
//                            wrapper.setPropertyValue(field.getName(), index);
//                        }
                        if (field.isAnnotationPresent(EditEntity.class)) {
                            wrapper.setPropertyValue(field.getName(), entity.getClass().getSimpleName());
                            c.add(Restrictions.eq(field.getName(), entity.getClass().getSimpleName()));
                        } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                            wrapper.setPropertyValue(field.getName(), data);
                        } else if (field.isAnnotationPresent(EditEntityId.class)) {
                            if (field.getType().isAssignableFrom(String.class)) {
                                wrapper.setPropertyValue(field.getName(), index.toString());
                                c.add(Restrictions.eq(field.getName(), index.toString()));
                            } else {
                                wrapper.setPropertyValue(field.getName(), index);
                                c.add(Restrictions.eq(field.getName(), index));
                            }

                        }
                    }
                    List<E> results = (List<E>) c.list();
                    results.forEach((e) -> {
                        //clear data
                        getSession().delete(e);
                    });
                    E e = (E) wrapper.getWrappedInstance();
//                    getSession().beginTransaction();
                    log.debug("Saving modifiable record {} using session {} ", e, getSession());
                    //check if exists in the database
                    getSession().save(e);
                    //update current bean
                    getSession().saveOrUpdate(this.updateEdit(oldEntity, entity));
//                    getSession().getTransaction().commit();
                }
            }
        }

        return isModified;
    }
    /**
     * Update entity with changes from the new object
     *
     * @param oldbean
     * @param newbean
     * @return updated bean
     * @throws IllegalAccessException
     */
    public T updateEdit(T oldbean, T newbean) throws IllegalAccessException {
//        System.out.println("Comparing old bean " + oldbean + " and new bean " + newbean);
        final Field[] allFields = newbean.getClass().getDeclaredFields();
//        for(Field f : allFields){
//            System.out.println("Found field " + f.getName());
//        }
        BeanWrapper wrapper = new BeanWrapperImpl(oldbean);
        for (Field field : allFields) {
            //Manage the fields that we need only
            if (field.isAnnotationPresent(ModifiableField.class)) {
                //Enable access of this field 
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object oldValue = wrapper.getPropertyValue(field.getName());//field.get(oldbean);
                Object newValue = field.get(newbean);
                System.out.println("New Value " + newValue + " and old value " + oldValue);
                if (oldValue != newValue && newValue != null) {
                    //update value in the oldbean
//                    field.set(oldbean, newValue);
                    wrapper.setPropertyValue(field.getName(), newValue);
                    // }
                }
                if (oldValue != newValue && newValue != null) {
                    //update value in the oldbean
//                    field.set(oldbean, newValue);
                    wrapper.setPropertyValue(field.getName(), newValue);
                    // }
                }
            }
        }
        return oldbean;
    }

    public List<String> fetchChanges(BigDecimal id, Class<E> editEntity) {
        List<String> changes = new ArrayList<>();
        T t = (T) getSession().get(_mapping, id);
        if (t == null) {
            return changes;
        }
        Criteria c = getSession().createCriteria(editEntity);
        String dataField = null;
        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                c.add(Restrictions.eq(field.getName(), t.getClass().getSimpleName()));
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
//                c.add(Restrictions.eq(field.getName(), id));
                if (field.getType().isAssignableFrom(String.class)) {
                    c.add(Restrictions.eq(field.getName(), id.toString()));
                } else {
                    c.add(Restrictions.eq(field.getName(), id));
                }
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }
        String data;
        E e = (E) c.uniqueResult();
        if (e == null) {
            return changes;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);

        if (null != data) {
            try {
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                T newbean = mapper.readValue(data, _mapping);
                changes.add(this.fetchStringChanges(t, newbean));
            } catch (IllegalAccessException | IOException ex) {
                log.error(AppConstants.AUDIT_LOG, "Failed to fetch entity changes", ex);
            }
        }

        return changes;
    }

    /**
     * Retrieves changes from the current Entity and persist changes to storage
     * entity
     *
     * @param id Unique id of the current object to be modified
     * @param editEntity Entity used to store changes class Reference
     *
     * @return
     * @throws java.io.IOException When it can't read the data from the storage
     * entity
     */
    public T _fetchChanges(BigDecimal id, Class<E> editEntity) throws IOException {
        T t = (T) getSession().get(_mapping, id);
        Criteria c = getSession().createCriteria(editEntity);
        String dataField = null;
        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                c.add(Restrictions.eq(field.getName(), t.getClass().getSimpleName()));
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
//                c.add(Restrictions.eq(field.getName(), id));
                if (field.getType().isAssignableFrom(String.class)) {
                    c.add(Restrictions.eq(field.getName(), id.toString()));
                } else {
                    c.add(Restrictions.eq(field.getName(), id));
                }
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }
        String data;
        E e = (E) c.uniqueResult();
        if (e == null) {
            return null;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);

        if (null != data) {
            //Serialize object
            ObjectMapper mapper = new ObjectMapper();
            T newbean = mapper.readValue(data, _mapping);
            return newbean;

        } else {
            return null;
        }
    }

    /**
     * Used to merge entity from Storage
     *
     * @param id Unique key id of this entity
     * @param editEntity Entity used to store changes class Reference
     * @return T new merged changes
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     */
    public T mergeChanges(BigDecimal id, Class<E> editEntity) throws IOException, IllegalArgumentException, IllegalAccessException {
        T t = (T) getSession().get(_mapping, id);

        if (t == null) {
            return null;
        }

//        final Field[] allFields = t.getClass().getDeclaredFields();
//        String data, dataField = null;
        Criteria c = getSession().createCriteria(editEntity);

        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                c.add(Restrictions.eq(field.getName(), t.getClass().getSimpleName()));
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    c.add(Restrictions.eq(field.getName(), id.toString()));
                } else {
                    c.add(Restrictions.eq(field.getName(), id));
                }
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
//                dataField = field.getName();
            }
        }
        E e = (E) c.uniqueResult();
        if (e == null) {
            return null;
        }
//        BeanWrapper wrapper = new BeanWrapperImpl(e);
//        data = (String) wrapper.getPropertyValue(dataField);
//        if (null != data) {
//            //Serialize object
//            ObjectMapper mapper = new ObjectMapper();
//            T newbean = mapper.readValue(data, _mapping);
//            wrapper = new BeanWrapperImpl(t);
//
//            for (Field field : allFields) {
//                if (!field.isAccessible()) {
//                    field.setAccessible(true);
//                }
//                if (field.isAnnotationPresent(ModifiableField.class)) {
//                    wrapper.setPropertyValue(field.getName(), field.get(newbean));
//                }
//            }
//            t = (T) wrapper.getWrappedInstance();
//            getSession().update(t);
//            //clear data
//            getSession().delete(e);
//            t = (T) getSession().get(_mapping, id);
//            return t;
//        }
        getSession().delete(e);
        return t;
    }

    /**
     * Used to decline entity changes. It clears data from the EditEntity and
     * returns the old entity
     *
     * @param id
     * @param editEntity
     * @return
     */
    public T declineChanges(BigDecimal id, Class<E> editEntity) {
        log.debug("Declining entity changes");
        T t = (T) getSession().get(_mapping, id);
        if (t == null) {
            log.debug("Failed to decline entity changes. Entity doesn't exist");
            return null;
        }
//        final Field[] allFields = t.getClass().getDeclaredFields();
        String data, dataField = null;
        Criteria c = getSession().createCriteria(editEntity);
        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                c.add(Restrictions.eq(field.getName(), t.getClass().getSimpleName()));
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    c.add(Restrictions.eq(field.getName(), id.toString()));
                } else {
                    c.add(Restrictions.eq(field.getName(), id));
                }
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }

        E e = (E) c.uniqueResult();
        if (e == null) {
            return t;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);
        if (null != data) {
            try {
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                T oldBean = mapper.readValue(data, _mapping);
                oldBean = this.updateEdit(t, oldBean);
                getSession().delete(e);
                getSession().flush();
                getSession().clear();
                getSession().saveOrUpdate(oldBean);
//                wrapper = new BeanWrapperImpl(t);
//                
//                for (Field field : allFields) {
//                    if (!field.isAccessible()) {
//                        field.setAccessible(true);
//                    }
//                    if (field.isAnnotationPresent(ModifiableField.class)) {
//                        wrapper.setPropertyValue(field.getName(), field.get(newbean));
//                    }
//                }
//                t = (T) wrapper.getWrappedInstance();
//                getSession().update(t);
//                //clear data
//                t = (T) getSession().get(_mapping, id);
//                return t;
                return oldBean;
            } catch (IOException | IllegalAccessException ex) {
                log.error(AppConstants.AUDIT_LOG, "Encountered an error while declining entity changes", ex);
                return null;
            }
        }
        return t;
    }
    /**
     * Used to decline entity changes. It clears data from the EditEntity and
     * returns the old entity
     *
     * @param t
     * @param editEntity
     * @return
     * @throws java.lang.IllegalAccessException
     */
    public T declineChanges(T t, Class<E> editEntity) throws IllegalArgumentException, IllegalAccessException {
        BigDecimal id = null;
        if(t == null){
            throw new RuntimeException("Sorry can't process changes for a null entity");
        }
        for(Field field : t.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Id.class)){
                field.setAccessible(true);
                id = (BigDecimal) field.get(t);
            }
        }
        
        if (id == null) {
            throw new RuntimeException("The entity doesn't have a valid id field");
        }
//        final Field[] allFields = t.getClass().getDeclaredFields();
        String data, dataField = null;
        Criteria c = getSession().createCriteria(editEntity);
        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                c.add(Restrictions.eq(field.getName(), t.getClass().getSimpleName()));
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    c.add(Restrictions.eq(field.getName(), id.toString()));
                } else {
                    c.add(Restrictions.eq(field.getName(), id));
                }
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }

        E e = (E) c.uniqueResult();
        if (e == null) {
            return t;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);
        if (null != data) {
            try {
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                T oldBean = mapper.readValue(data, _mapping);
                oldBean = this.updateEdit(t, oldBean);
                getSession().delete(e);
                getSession().flush();
                getSession().clear();
                getSession().saveOrUpdate(oldBean);
//                wrapper = new BeanWrapperImpl(t);
//                
//                for (Field field : allFields) {
//                    if (!field.isAccessible()) {
//                        field.setAccessible(true);
//                    }
//                    if (field.isAnnotationPresent(ModifiableField.class)) {
//                        wrapper.setPropertyValue(field.getName(), field.get(newbean));
//                    }
//                }
//                t = (T) wrapper.getWrappedInstance();
//                getSession().update(t);
//                //clear data
//                t = (T) getSession().get(_mapping, id);
//                return t;
                return oldBean;
            } catch (IOException | IllegalAccessException ex) {
                log.error(AppConstants.AUDIT_LOG, "Encountered an error while declining entity changes", ex);
                return null;
            }
        }
        return t;
    }
    /**
     * Transform entity into a Map object
     *
     * @param index
     * @return Map<String, Object>
     */
    public Map<String, Object> transformEntity(String index) {
        _entity = (T) getSession().get(_mapping, Long.valueOf(index));
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(_entity, Map.class);
        map.put("status", "00");
        return map;
    }

    /**
     * Compare the 'modifiable' fields of two beans
     *
     * @param oldbean A persisted instance of this class
     * @param newbean A new bean with new values yet to be persisted
     * @return Map<String, Object> array containing the field name, old value,
     * and the new value
     * @throws java.lang.IllegalAccessException
     */
    public Map<String, List> fetchChanges(T oldbean, T newbean) throws IllegalAccessException {
        Map<String, List> map = new HashMap<>();
        if (newbean.getClass() != oldbean.getClass()) {
            throw new IllegalArgumentException("The beans must be of the same class");
        }

        List<Object> changes = new ArrayList<>();
        final Field[] allFields = oldbean.getClass().getDeclaredFields();
        for (Field field : allFields) {
            //Manage the fields that we need only
            if (field.isAnnotationPresent(ModifiableField.class)) {
                Map<String, Object> rmap = new HashMap<>();
                //Enable access of this field 
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object oldValue = field.get(oldbean);
                Object newValue = field.get(newbean);

                if (oldValue != newValue) {
                    if ((oldValue != null && !oldValue.equals(newValue))
                            || (newValue != null && !newValue.equals(oldValue))) {
                        rmap.put("oldvalue", oldValue);
                        rmap.put("newvalue", newValue);
                        rmap.put("field", field.getName());
                        changes.add(rmap);
                    }
                }
            }
        }
        map.put("data", changes);

        return map;
    }

    public String fetchStringChanges(T newbean, T oldbean) throws IllegalAccessException {
//        Map<String, Object> map = new HashMap<>();
        String changes = "";
        if (newbean.getClass() != oldbean.getClass()) {
            log.error(AppConstants.AUDIT_LOG, "Failed to fetch changes for {} and {}. "
                    + "Beans are not of the same class", oldbean.getClass(), newbean.getClass());
            return changes;
        }

//        List<Object> changes = new ArrayList<>();
        final Field[] allFields = oldbean.getClass().getDeclaredFields();
        for (Field field : allFields) {
            //Manage the fields that we need only
            if (field.isAnnotationPresent(ModifiableField.class)) {
                Map<String, Object> rmap = new HashMap<>();
                //Enable access of this field 
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object oldValue = field.get(oldbean);
                Object newValue = field.get(newbean);

                if (oldValue != newValue) {
                    if ((oldValue != null && !oldValue.equals(newValue))
                            || (newValue != null && !newValue.equals(oldValue))) {
                        changes += SharedMethods.splitCamelString(field.getName())
                                + " changed from " + oldValue + " to " + newValue
                                + ", ";
//                        rmap.put("oldvalue", oldValue);
//                        rmap.put("newvalue", newValue);
//                        rmap.put("field", field.getName());
//                        changes.add(rmap);
                    }
                }
            }
        }
//        map.put("data", changes);

        return changes;
    }

    public BigDecimal sumTrxValue(String customerId, Date from, Date to, String needle) {
        needle = needle.toLowerCase();
        String search = "(lower(t.account.accountName) LIKE CONCAT(:needle, '%') OR "
                + "lower(t.account.customer.physicalAddress) LIKE CONCAT(:needle, '%'))";
        String hql = "SELECT COALESCE(SUM(t.trxValue), 0) FROM CmsCollectionTrxVw t WHERE t.trxStatus = '"
                + AppConstants.STATUS_COMPLETED + "'"
                + "AND t.timeInitiated BETWEEN :from AND :to AND "
                + "COALESCE(t.account.customer.customerId, -1) LIKE :customerId AND " + search;

        Query query = this.getSession().createQuery(hql);
        query.setDate("from", from);
        query.setDate("to", to);
        query.setString("needle", needle);
        query.setString("customerId", customerId + "%");
        return (BigDecimal) query.uniqueResult();
    }

    @JsonFilter("dynamicFilter")
    class DynamicMixIn {
    }

}
