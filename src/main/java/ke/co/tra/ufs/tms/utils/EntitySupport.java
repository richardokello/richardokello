/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import ke.co.tra.ufs.tms.utils.annotations.EditDataWrapper;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
/**
  * @deprecated This class is not compatible with this project use {@link ke.co.tracom.gulf.dcc.repository.SupportRepository}
 * @author Cornelius M
 */
@Deprecated
public class EntitySupport<T> {

    private Logger log = LoggerFactory.getLogger(this.getClass());
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
     * Check if any changes were submitted; if true, update the entity, else
     * do-nothing and notify calling method
     *
     * @param request
     * @return boolean
     */
    public boolean handleEditRequest(T t) {
        //Get an instance of the entity with new values
        Map<String, Object> map;
//        T t = handleRequests(request);
        Long index = 0L;
        boolean isModified = false;
        String editWrapperField = null;

        try {
            if (null != t) {
                //Check if entity has been modified
                for (Field field : t.getClass().getDeclaredFields()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (field.isAnnotationPresent(Id.class)) {
                        index = (Long) field.get(t);
                    } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                        editWrapperField = field.getName();
                    }
                }
                if (index == 0L) {
                    //Do nothing
                } else {
                    _entity = (T) getSession().get(_mapping, index);

                    //Compare values
                    map = fetchChanges(_entity, t);
                    List<Object> result = (List<Object>) map.get("data");
                    isModified = (result.size() > 0);

                    //If there are changes, update this field
                    if (isModified) {
                        BeanWrapper wrapper = new BeanWrapperImpl(_entity);
                        ObjectMapper mapper = new ObjectMapper();
                        String data = mapper.writeValueAsString(t);

                        wrapper.setPropertyValue(editWrapperField, data);
                        _entity = (T) wrapper.getWrappedInstance();
                    }
                }
            }
        } catch (IllegalArgumentException | JsonProcessingException | IllegalAccessException ex) {
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>" + ex.getMessage());
            System.err.println("Cause >>" + ex.getCause());
            ex.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return isModified;
    }

    /**
     * Fetch the changes yet to be persisted to an object
     *
     * @param index Unique index of the current object to be modified
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchChanges(String index) {
        T t = (T) getSession().get(_mapping, Long.valueOf(index));

        final Field[] allFields = t.getClass().getDeclaredFields();
        String data = null;
        try {
            for (Field field : allFields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(EditDataWrapper.class)) {
                    data = (String) field.get(t);
                }
            }

            if (null != data) {
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                T newbean = mapper.readValue(data, _mapping);
                return fetchChanges(t, newbean);
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>" + e.getMessage());
            System.err.println("Cause >>" + e.getCause());
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return null;
    }

    /**
     * Update a persisted object with new values previously persisted into a a
     * column of this entity
     *
     * @param index Unique key index of this entity
     * @return T
     */
    public T mergeChanges(String index) {
        T t = (T) getSession().get(_mapping, Long.valueOf(index));

        final Field[] allFields = t.getClass().getDeclaredFields();
        String data = null, editWrapperField = null;
        try {
            for (Field field : allFields) {

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(EditDataWrapper.class)) {
                    data = (String) field.get(t);
                    editWrapperField = field.getName();
                    break;
                }
            }

            if (null != data) {
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                T newbean = mapper.readValue(data, _mapping);
                BeanWrapper wrapper = new BeanWrapperImpl(t);

                for (Field field : allFields) {

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (field.isAnnotationPresent(ModifiableField.class)) {
                        wrapper.setPropertyValue(field.getName(), field.get(newbean));
                    }
                }
                //Reset this column
                wrapper.setPropertyValue(editWrapperField, null);

                return (T) wrapper.getWrappedInstance();
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>" + e.getMessage());
            System.err.println("Cause >>" + e.getCause());
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return null;
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
     */
    public Map<String, Object> fetchChanges(T oldbean, T newbean) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (newbean.getClass() != oldbean.getClass()) {
                throw new IllegalArgumentException("The beans must be of the same class");
            }

            List<Object> changes = new ArrayList<Object>();
            final Field[] allFields = oldbean.getClass().getDeclaredFields();
            for (Field field : allFields) {
                //Manage the fields that we need only
                if (field.isAnnotationPresent(ModifiableField.class)) {
                    Map<String, Object> rmap = new HashMap<String, Object>();
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
                            rmap.put("field", field.getAnnotation(ModifiableField.class).name());
                            changes.add(rmap);
                        }
                    }
                }
            }
            map.put("data", changes);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>" + e.getMessage());
            System.err.println("Cause >>" + e.getCause());
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return map;
    }
}
