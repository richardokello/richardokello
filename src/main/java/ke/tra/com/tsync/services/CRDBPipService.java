package ke.tra.com.tsync.services;


import ke.tra.com.tsync.wrappers.crdb.CRDBWrapper;
import ke.tra.com.tsync.wrappers.crdb.SessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CRDBPipService {


    @Value("${get_session_pip}")
    private String get_session_pip;

    @Autowired
    private RestTemplate restTemplate;

    // validate Control Number
    private void getControlNumberDetailsCron()
    {
        String result = restTemplate.getForObject(get_session_pip, String.class);
        System.out.println(result);
    }

    public  String getSessionDetails()
    {
        String result="";
        try {
            CRDBWrapper crdbWrapper = restTemplate.getForObject(get_session_pip, CRDBWrapper.class);
           // trcmGeneralRestWrapper.getObject();

            if(crdbWrapper.getStatus()==200){
                SessionResponse sessionResponse= (SessionResponse) crdbWrapper.getData();
                result=sessionResponse.getSessionToken();
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
