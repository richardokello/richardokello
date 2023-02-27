package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.CaPublicKeys;
import ke.tra.ufs.webportal.repository.CAPublicKeysRepository;
import ke.tra.ufs.webportal.wrappers.CaPublicKeyData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class PublicKeyUpload {
    @Autowired
    private static CAPublicKeysRepository repository;
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Issuer", "Exponent", "RidIndex","RidList","Modulus",
            "KeyLength","SHA1","keyType","Expiry"};
    public void saveKeys(MultipartFile file){
        try{
            List<CaPublicKeyData> caPublicKeys=processCVSFIle(file.getInputStream());
            List<CaPublicKeys> caPublicKeysList=new ArrayList<>();
            for(CaPublicKeyData keyData: caPublicKeys){
                CaPublicKeys keys=new CaPublicKeys(Long.getLong("Id") ,keyData.getIssuer(),
                        keyData.getExponent(),
                        keyData.getRidIndex(),
                        keyData.getRidList(),
                        keyData.getModulus(),
                        keyData.getKeyLength(),
                        keyData.getSHA1(),
                        keyData.getKeyType(),
                        keyData.getExpiry());
                caPublicKeysList.add(keys);
            }

            repository.saveAll(caPublicKeysList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<CaPublicKeyData>caPublicKeyDataList(){
      List<CaPublicKeys> keys=repository.findAll();
      List<CaPublicKeyData> listOFCaKeys=new ArrayList<>();
      for (CaPublicKeys data: keys){
          CaPublicKeyData keyData=new CaPublicKeyData(data.getIssuer(),
                  data.getExponent(), data.getRidIndex(), data.getRidList(), data.getModulus(), data.getKeyLength(),
                  data.getSHA1(), data.getKeyType(), data.getExpiry());
          listOFCaKeys.add(keyData);
      }
      return listOFCaKeys;
    }
    public static List<CaPublicKeyData> processCVSFIle(InputStream fileStream){
        try {
            Reader fileReader = new InputStreamReader(fileStream, "UTF-8");
            CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim();
            CSVParser csvParser = new CSVParser(fileReader, csvFormat);
            List<CaPublicKeyData> listOfKeys = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            // Implement the code to process each CSV record and populate the listOfKeys
            for (CSVRecord csvRecord : csvParser) {
                CaPublicKeyData caPublicKey=new CaPublicKeyData(
                        csvRecord.get("issuer"),
                        csvRecord.get("exponent"),
                        csvRecord.get("ridIndex"),
                        csvRecord.get("ridList"),
                        csvRecord.get("modulus"),
                        csvRecord.get("keyLength"),
                        csvRecord.get("SHA1"),
                        csvRecord.get("keyType"),
                        csvRecord.get("expiry")
                );
                listOfKeys.add(caPublicKey);
            }
            csvParser.close();
            return listOfKeys; // Add a return statement to return the processed data
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("fail to read CSV file: " + e.getMessage());
        }
    }


// ...

//    public static List<CaPublicKeyData> processCSVFile(InputStream fileStream) {
//        try {
//            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));
//
//            CsvParserSettings parserSettings = new CsvParserSettings();
//            parserSettings.getFormat().setLineSeparator("\n");
//            parserSettings.getFormat().setDelimiter(',');
//            parserSettings.setIgnoreLeadingWhitespaces(true);
//            parserSettings.setIgnoreTrailingWhitespaces(true);
//            parserSettings.setHeaderExtractionEnabled(true);
//
//            CsvParser csvParser = new CsvParser(parserSettings);
//            List<CaPublicKeyData> listOfKeys = new ArrayList<>();
//
//            List<String[]> csvRecords = csvParser.parseAll(fileReader);
//
//            // Process the CSV records and populate the listOfKeys
//            for (String[] csvRecord : csvRecords) {
//
//            }
//
//            return listOfKeys; // Add a return statement to return the processed data
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException("fail to read CSV file: " + e.getMessage());
//        }
//    }

}