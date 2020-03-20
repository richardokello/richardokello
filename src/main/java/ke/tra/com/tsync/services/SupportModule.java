package ke.tra.com.tsync.services;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
import ke.tra.com.tsync.wrappers.support.Content;
import ke.tra.com.tsync.wrappers.support.SupportModuleWrap;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SupportModule {

    @Value("${support_module_url}")
    private String supportModuleUrl;

    @Value("${support_feedback_url}")
    private String supportFeedbackUrl;

    @Autowired
    private RestTemplate myRestTemp;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SupportModule.class);

    public ISOMsg getAllSupportCases(ISOMsg isoMsg) {
        try {
            TrcmGeneralRestWrapper trcmGeneralRestWrapper = myRestTemp
                    .getForObject(supportModuleUrl, TrcmGeneralRestWrapper.class);
            SupportModuleWrap supportModuleWrap = (SupportModuleWrap) trcmGeneralRestWrapper.getObject();
            List<Content> content = supportModuleWrap.getContent();
            final String[] f72Response = {""};
            content.forEach(
                    (item) -> {
                        f72Response[0] += item.getId() + "|" + item.getNatureId() + "|" + item.getComplaintName() + "#";
                    }
            );

            isoMsg.set(72, f72Response[0]);
            isoMsg.set(39, "00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isoMsg;
    }
    //
    public ISOMsg getSupportCaseByRefNo(ISOMsg isoMsg) {

        //check if has de72 and has data
        String caseref = "";
        if (isoMsg.hasField(72) && !(isoMsg.getString(72).length() > 0)) {
            isoMsg.set(39, "96");
            isoMsg.set(72, "Empty or Invalid Request Data");
            return isoMsg;
        }
        caseref = isoMsg.getString(72);
        String url = supportModuleUrl + "/refCode?refCode=" + caseref;
        //fetch support case and return to POS
        System.out.println("supportModuleUrl " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> responseEntity = myRestTemp.exchange(url, HttpMethod.GET, entity, String.class);

        //fetch whatevers needed here
        System.out.println(responseEntity.toString());

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            isoMsg.set(39, "00");
            JsonParser parser = new JsonParser();
            JsonElement bcontent= parser.parse(responseEntity.getBody());
            JsonObject jobject = bcontent.getAsJsonObject().getAsJsonObject("data");
            String remedialAction = jobject.getAsJsonObject().get("remedialAction").getAsString();
            //System.out.println("Whats this " +jobject);
          //  System.out.printf("\nCreated refcode for {s}  -> {s}\n" ,isoMsg.getString(37), refcode);
            isoMsg.set(72, remedialAction);
        }else{
            isoMsg.set(72,"System Error - Kindly try again or contact SBM customer care desk");
        }
        return isoMsg;
    }

    public ISOMsg createSupportCase(ISOMsg isoMsg, HashMap<String, Object> dataMap) {
        try {
            String[] supportData = isoMsg.getString(72).split("#");
            String natureOfSupport = supportData[0];
            String location = supportData[1];
            String caseDate = supportData[2];


            String username = (String) dataMap.get("userName");

            System.out.println("Case Date Incoming" + caseDate);
            SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
            Date date1=formatter1.parse(caseDate);

            //Wed Feb 05 00:00:00 EAT 2020
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            String date2 = sdf.format(date1);
            String narration = supportData[3];
            System.out.println("Case Date Outgoing" + date2);


            Map map = Map.of(
                "details",narration,
                "location",location,
                "agentID",username,
                "natureId",Integer.parseInt(natureOfSupport),
                "occuranceTime","2019-12-16T06:17:50.092Z"
            );

            System.out.println("Map Outgoing" + map.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.set("Authorization", "Basic XXXXXXXXXXXXXXXX=");
           HttpEntity<Map> entity = new HttpEntity<Map>(map, headers);
            ResponseEntity<String> responseEntity =
                    myRestTemp.postForEntity(
                            supportModuleUrl,
                            entity,
                            String.class
                    );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                isoMsg.set(39, "00");
                JsonParser parser = new JsonParser();
                JsonElement bcontent= parser.parse(responseEntity.getBody());
                JsonObject jobject = bcontent.getAsJsonObject().getAsJsonObject("data");
                String refcode = jobject.getAsJsonObject().get("refCode").getAsString();
                System.out.println("Whats this " +jobject);
                System.out.printf("\nCreated refcode for {s}  -> {s}\n" ,isoMsg.getString(37), refcode);
                isoMsg.set(72, refcode);
            }else{
                isoMsg.set(72,"System Error");
            }

        }catch (Exception e){
            isoMsg.set(72, "System Error");
            e.printStackTrace();
        }
        return isoMsg;
    }


    public ISOMsg addFeedBackByCaseRef(ISOMsg isoMsg, HashMap<String, Object> dataMap) {
        try {
            String username = (String) dataMap.get("userName");
            String[] supportData = isoMsg.getString(72).split("#");
            String feedback = supportData[1];
            String caseref = supportData[0];

            Map map = Map.of(
                    "agentId",username,
                    "feedback",feedback,
                    "caseref",caseref
            );

            System.out.println("Feedback to API Outgoing" + map.toString());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.set("Authorization", "Basic XXXXXXXXXXXXXXXX=");
            HttpEntity<Map> entity = new HttpEntity<Map>(map, headers);
            ResponseEntity<String> responseEntity =
                    myRestTemp.postForEntity(
                            supportFeedbackUrl,
                            entity,
                            String.class
                    );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                isoMsg.set(39, "00");
                System.out.println("Whats UP " +responseEntity.getBody());
            }else{
                isoMsg.set(72,"System Error");
            }

        }catch (Exception e){
            isoMsg.set(72, "System Error");
            e.printStackTrace();
        }
        return isoMsg;
    }



    // Get All support Categories
    public ISOMsg getAllSupportCasesByAgentNo (ISOMsg isoMsg, HashMap<String, Object> dataMap){
        String username = (String) dataMap.getOrDefault("userName","");


        System.out.println("Ageny usermname " + username);
        try {
            if(!username.isEmpty()) {
                String[] field72final = {""};
                ResponseEntity<String> responseEntity =
                        myRestTemp.getForEntity(supportModuleUrl +"?agentID=" + username.trim(), String.class);
                JsonParser parser = new JsonParser();
                JsonElement content = parser.parse(responseEntity.getBody());
                JsonObject jobject = content.getAsJsonObject().getAsJsonObject("data");
                JsonArray ja = jobject.getAsJsonArray("content");
                ja.iterator().forEachRemaining(
                        (item) -> {
                            field72final[0] += item.getAsJsonObject().get("refCode").getAsString()+"#";
                        }
                );

                isoMsg.set(72, field72final[0]);
                isoMsg.set(39, "00");
            }else{
                isoMsg.set(39, "06");
                isoMsg.set(72, "INVALID AGENT DATA");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  isoMsg;
    }

}
