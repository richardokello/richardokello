package ke.tra.com.tsync.services;


import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
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
            TrcmGeneralRestWrapper trcmGeneralRestWrapper = restTemplate.getForObject(get_session_pip, TrcmGeneralRestWrapper.class);
           // trcmGeneralRestWrapper.getObject();

            if(trcmGeneralRestWrapper.getStatus()==200){
                SessionResponse sessionResponse= (SessionResponse) trcmGeneralRestWrapper.getData();
                result=sessionResponse.getSessionToken();
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }




}
