package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.SendMoneydata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CSVHelper {
    @Autowired
    BulkSendMoneyService service;
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "senderMobileNo", "senderNationalID", "senderNationalIDType", "recipientMobileNo","amount" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
    public void readFile(File file){
        ObjectMapper mapper =new ObjectMapper();
        CompletableFuture.runAsync(new Runnable() {
                                       @Override
                                       public void run() {
                                           try {
                                               //mapper.readValue(file,SendMoneydata.class);
                                              List<SendMoneydata>requestList= mapper.readValue(file, new TypeReference<List<SendMoneydata>>() {
                                               });
                                           } catch (IOException e) {
                                               throw new RuntimeException(e);
                                           }
                                       }
                                   }
        );
    }

    public static List<SendMoneydata> csvToSendMoney(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<SendMoneydata> tutorials = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                SendMoneydata tutorial = new SendMoneydata(
                        csvRecord.get("senderMobileNo"),
                        csvRecord.get("senderNationalID"),
                        csvRecord.get("senderNationalIDType"),
                        csvRecord.get("recipientMobileNo"),
                        Double.parseDouble(csvRecord.get("amount"))
                        )
                ;

                tutorials.add(tutorial);
            }

            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }


    public CompletableFuture<List<SendMoneyResponse>> saveBulkSendMoney(MultipartFile file, String username) throws IOException {
        List<SendMoneydata> sendMoneydata = CSVHelper.csvToSendMoney(file.getInputStream());
        while(sendMoneydata!=null) {
            // return sendMoneydata;
            String transactionRRN = RRNGenerator.getInstance("SM").getRRN();

            CompletableFuture<List<SendMoneyResponse>> response = service.bulkSendMoneyProcess(sendMoneydata, transactionRRN, username);

            System.out.println("response from T24 =========  " + response);


        return response;}
        return null;
    }
}
