package ke.tra.ufs.webportal.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.wrapper.MccWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Id;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Owori Juma
 */
@Component
public class SharedMethods {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    @Value("${app.template.uploadPath}")
//    private String uploadPath;    

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static final String key = "Trx#279!DTCeioc?";

    /**
     * Used to get all errors each mapped to the relevant field. Quiet handy
     * when returning api error responses
     *
     * @param validation
     * @return a {@link Map} of all errors
     */
    public static Map<String, String> getFieldMapErrors(BindingResult validation) {
        return validation.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    public static String getFieldErrorsString(Errors error) {
        String eString = "";
        eString = error.getFieldErrors().stream().map((fError) -> fError.getField() + " => " + fError.getDefaultMessage() + ", ").reduce(eString, String::concat);
        return eString;
    }

    public boolean generateParamField(String content, String fileName, String filePath) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(content.getBytes());
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem writing to the file " + filePath + fileName);
            //log.error(AppConstants.AUDIT_LOG, "Creating new Device Task failed", e);
            /* loggerService.logCreate("Creating new Device Task failed. This may be "
                    + "due to  msg:" + e.getMessage(), SharedMethods.getEntityName(TmsDeviceTask.class), null,
                    AppConstants.STATUS_FAILED);*/
            return false;
        }
    }
    /**
     * Used to fetch current date. At a later stage it may be to a relevant
     * timezone
     *
     * @return current date
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * Used to Read MultipartFile resource e.g a CSV file
     *
     * @param uploadPath
     * @return
     * @throws FileNotFoundException, IOException when errors occur searching
     *                                for the file or reading the file
     * @throws IOException
     */
    public void deleteDirectory(String uploadPath) throws FileNotFoundException, IOException {
        log.info("deleting directory " + uploadPath);
        File file = new File(uploadPath);
        deleteFile(file);
    }

    public void deleteFile(File element) {
        if (element.isDirectory()) {
            log.info("deleting directory " + element.getAbsolutePath());
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }

    public String store(MultipartFile resource, String uploadPath) throws FileNotFoundException, IOException {
        log.info("Processing file storage for file " + resource.getOriginalFilename());
        AppCommons appCommons = new AppCommons();
        String generatedFileName = appCommons.getRandomFileName(resource.getOriginalFilename());

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileUrl = uploadPath + generatedFileName;

        log.info("Path  " + fileUrl);

        BufferedOutputStream stream
                = new BufferedOutputStream(new FileOutputStream(new File(fileUrl)));
        stream.write(resource.getBytes());
        stream.close();
        return fileUrl;
    }

    /**
     * Used to write multipart file in syste file
     *
     * @param resource
     * @param uploadPath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public File storeFile(MultipartFile resource, String uploadPath) throws FileNotFoundException, IOException {
        log.info("Processing file storage for file " + resource.getOriginalFilename());

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileUrl = uploadPath + "/" + resource.getOriginalFilename();
        File file = new File(fileUrl);
        log.debug("Initialized new file space {} ", file.getTotalSpace());
        BufferedOutputStream stream
                = new BufferedOutputStream(new FileOutputStream(file));
        stream.write(resource.getBytes());
        stream.close();
        log.debug("Saved new file in system directory space {} ", file.getTotalSpace());
        return file;
    }

    public void moveFile(String src, String target, String directory) throws FileNotFoundException, IOException {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Path source = Paths.get(src);
        Path destination = Paths.get(target);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

        log.debug("Saved new file in system directory space {} ", target);

        unZipIt(src, directory);
        File transferedapp = new File(target);
        transferedapp.delete();
    }

    public void unZipIt(String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];

        try {
            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void generateParamField(String content, String fileName, String filePath, LoggerService loggerService) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(content.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem writing to the file " + filePath + fileName);
            //log.error(AppConstants.AUDIT_LOG, "Creating new Device Task failed", e);
            /* loggerService.logCreate("Creating new Device Task failed. This may be "
                    + "due to  msg:" + e.getMessage(), SharedMethods.getEntityName(TmsDeviceTask.class), null,
                    AppConstants.STATUS_FAILED);*/
        }
    }

    /**
     * Used to Convert a MultipartFile resource e.g a CSV file to Data which
     * will be stored to the DB
     *
     * @param <T>
     * @param entity Entity class that maps the file columns
     * @param file   MultipartFile resource to be read
     * @return
     * @throws IOException when errors occur during storing the file data
     */
    public <T> List<T> convertCsv(Class<T> entity, MultipartFile file) throws IOException {
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
//        MappingIterator<T> readValues = mapper.readerFor(entity).with(bootstrapSchema)
//                .readValues(file.getInputStream());
//
//        return readValues.readAll();
        return this.convertCsv(entity, file.getInputStream());
    }

    public <T> List<T> convertCsv(Class<T> entity, File file) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<T> readValues = mapper.readerFor(entity).with(bootstrapSchema)
                .readValues(file);

        return readValues.readAll();
    }

    public <T> List<T> convertXls(Class<T> entity, File file) throws IOException, InvalidFormatException {
        List<T> ts = new ArrayList<T>();
        Workbook workbook = WorkbookFactory.create(file);
        DataFormatter dataFormatter = new DataFormatter();
        workbook.forEach(sheet -> {
            sheet.forEach(row -> {
                row.forEach(cell -> {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (!cellValue.equals("Serial Number")) {
                        /*WhitelistDetails details = new WhitelistDetails();
                        details.setSerialNo(cellValue);
                        ts.add((T) details);*/
                    }
                });
                System.out.println();
            });
        });
        return ts;
    }

    public <T> Set<T> convertXlsMcc(Class<T> entity, File file) throws IOException, InvalidFormatException {
        Set<T> ts = new HashSet<>();
        Workbook workbook = WorkbookFactory.create(file);
        DataFormatter dataFormatter = new DataFormatter();
        workbook.forEach(sheet -> {
            sheet.forEach(row -> {
                String[] rowData = new String[2];
                int count = 0;
                for(Cell cell: row){
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (!cellValue.equals("MCC")&&count==0) {
                        rowData[0] = cellValue;
                    }
                    if (!cellValue.equals("MCC Title")&&count==1) {
                        rowData[1] = cellValue;
                    }
                    count++;
                }
                if(count>0){
                    MccWrapper mcc =  new MccWrapper();
                    mcc.setMcc(rowData[0]);
                    mcc.setMccTitle(rowData[1]);
                    ts.add((T) mcc);
                }
            });
        });
        return ts;
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Used to Convert a MultipartFile resource e.g a CSV file to Data which
     * will be stored to the DB
     *
     * @param <T>
     * @param entity Entity class that maps the file columns
     * @param stream resource to be read
     * @return
     * @throws IOException
     */
    public <T> List<T> convertCsv(Class<T> entity, InputStream stream) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<T> readValues = mapper.readerFor(entity).with(bootstrapSchema)
                .readValues(stream);

        return readValues.readAll();

    }

    /**
     * Used to Convert a MultipartFile resource e.g a CSV file to Data which
     * will be stored to the DB
     *
     * @param <T>
     * @param entity Entity class that maps the file columns
     * @param bytes  resource to be read
     * @return
     * @throws IOException
     */
    public <T> List<T> convertCsv(Class<T> entity, byte[] bytes) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<T> readValues = mapper.readerFor(entity).with(bootstrapSchema)
                .readValues(bytes);

        return readValues.readAll();

    }

    /**
     * Used to split string by camel case and convert it to title case e.g.
     * "camelCase" will be "Camel Case"
     *
     * @param camelString
     * @return
     */
    public static String splitCamelString(String camelString) {
        return StringUtils.capitalize(StringUtils.join(
                camelString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"), ' '));
    }

    /**
     * Split car registration number e.i KBC345K to KBC 345K
     *
     * @param regNumber
     * @return
     */
    public static String splitCarReg(String regNumber) {
        String refString;
        int nIndex = 0;
        for (char c : regNumber.toCharArray()) {//search first number index
            if (Character.isDigit(c)) {
                break;
            }
            nIndex++;
        }
        refString = regNumber.substring(0, nIndex) + " " + regNumber.substring(nIndex);
        return refString;
    }

    /**
     * Get entity name
     *
     * @param <T>
     * @param entity
     * @return
     */
    public static <T> String getEntityName(Class<T> entity) {
//        Table table = entity.getAnnotation(Table.class);
//        return table.name();
        return entity.getSimpleName();
    }

    /**
     * Encrypt text
     *
     * @param text
     * @return base64 encoded string
     */
    public static String encryptText(String text) {
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
//            return new String(encrypted);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            java.util.logging.Logger.getLogger(SharedMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }

    /**
     * Decrypt text
     *
     * @param encryptedText base64 encoded
     * @return
     */
    public static String decryptText(String encryptedText) {

        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
//            return new String(cipher.doFinal(encryptedText.getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        } catch (IllegalArgumentException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            java.util.logging.Logger.getLogger(SharedMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedText;
    }

    /**
     * Get the id value of an entity
     *
     * @param entity
     * @return
     */
    public static Object getEntityIdValue(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return PropertyAccessorFactory.forBeanPropertyAccess(entity).getPropertyValue(field.getName());
            }
        }
        return null;
    }

    /**
     * Used to get generic classes declared by a template
     *
     * @param clazz
     * @return
     */
    public static List<Class> getGenericClasses(Class clazz) {
        List<Class> classes = new ArrayList<>();
        ParameterizedType t = (ParameterizedType) clazz.getGenericSuperclass();
        for (Type type : t.getActualTypeArguments()) {
            classes.add((Class<?>) type);
        }
        return classes;
    }
}
