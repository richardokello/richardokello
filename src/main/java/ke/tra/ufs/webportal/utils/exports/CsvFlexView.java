/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.utils.exports;

import ke.axle.chassis.utils.SharedMethods;
import ke.tra.ufs.webportal.utils.annotations.ExportField;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cornelius M
 * @param <T>
 */
public class CsvFlexView<T> extends AbstractCsvView {

    private final Class<T> clazz;
    private List<T> entities;
    private String fileName;
    private String dateFormat = "dd-MM-yyyy HH:mm:ss";

    public CsvFlexView(Class<T> clazz) {
        this.clazz = clazz;
    }

    public CsvFlexView(Class<T> clazz, List<T> entities) {
        this.clazz = clazz;
        this.entities = entities;
    }

    public CsvFlexView(Class<T> clazz, List<T> entities, String fileName) {
        this.clazz = clazz;
        this.entities = entities;
        this.fileName = fileName;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        String fileName = (this.fileName == null ? SharedMethods.splitCamelString(this.clazz.getSimpleName()).toLowerCase()
                : this.fileName);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".csv\"");

//        List<User> users = (List<User>) model.get("users");
        BeanWrapper classWrapper = new BeanWrapperImpl(clazz);
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> mappingHeaders = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {//Get column titles from field name or annotation
            if (field.isAnnotationPresent(ExportField.class)
                    && classWrapper.isReadableProperty(field.getName())) {//check if the field has export annotation
                ExportField exportField = field.getAnnotation(ExportField.class);
                if (exportField.name().isEmpty()) {//check if field name has been given, if not retrieve from the field
                    headers.add(SharedMethods.splitCamelString(field.getName()));
                } else {
                    headers.add(exportField.name());
                }
                mappingHeaders.add(field.getName());
            }
        }

        String[] header = headers.toArray(new String[headers.size()]);//(String[]) headers.toArray();// {"Firstname", "LastName", "LastName", "JobTitle", "Company", "Address", "City", "Country", "PhoneNumber"};
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
//                CsvPreference.STANDARD_PREFERENCE);
        CsvCustomBeanWriter csvWriter = new CsvCustomBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormat);
        for (T entity : this.entities) {//Generate rows from entities list
            BeanWrapper wrapper = new BeanWrapperImpl(entity);
            List<String> row = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {//retrieve values from the base field
                if (field.isAnnotationPresent(ExportField.class) && wrapper.isReadableProperty(field.getName())) {
                    ExportField exportField = field.getAnnotation(ExportField.class);
                    if (exportField.entityField().fieldName().isEmpty()) {//check if the current field is an entity
                        if (field.getType() == Date.class) {
                            String value = wrapper.getPropertyValue(field.getName()) == null ? "" : simpleDateFormat.format(wrapper.getPropertyValue(field.getName()));
                            row.add(value);
                        } else {
                            String value = wrapper.getPropertyValue(field.getName()) == null ? "" : "" + wrapper.getPropertyValue(field.getName());
                            row.add(value);
                        }
                    } else {//process the entity field
                        Object entity2 = wrapper.getPropertyValue(field.getName());
                        if (entity2 != null) {
                            BeanWrapper wrapper2 = new BeanWrapperImpl(entity2);
                            if (field.getType() == Date.class) {
                                String value = wrapper2.getPropertyValue(field.getName()) == null ? "" : simpleDateFormat.format(wrapper2.getPropertyValue(field.getName()));
                                row.add(simpleDateFormat.format(value));
                            } else {
                                String value = wrapper2.getPropertyValue(field.getName()) == null ? "" : "" + wrapper2.getPropertyValue(field.getName());
                                row.add(value);
                            }
//                            row.add((String) wrapper2.getPropertyValue(exportField.entityField().fieldName()));
                        }
                    }
                }
            }
            csvWriter.writeFromList(row, mappingHeaders.toArray(new String[headers.size()]));
        }
        csvWriter.close();

    }
}
