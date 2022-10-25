package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class BulkSendMoneyService {
    public SendMoneyRequest getSendMoneyJson(String sendMoneyRequest, List<MultipartFile> file) throws JsonProcessingException {
        SendMoneyRequest request;
        ObjectMapper objectMapper=new ObjectMapper();
        request=objectMapper.readValue(sendMoneyRequest,SendMoneyRequest.class);
        int fileCount=file.size();
        request.setCount(fileCount);
        return request;
    }
}
