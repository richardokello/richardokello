package ke.tra.com.tsync.services;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ke.tra.com.tsync.wrappers.DataWrapper;
import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;

@Service
public class RatePayerCategories {

    @Value("${rate_payer_categories_url}")
    private String ratePayerCategoriesUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RatePayerCategories.class);


    public ISOMsg getARatePayerCategories(ISOMsg isoMsg){
        try{
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Accept", "application/json");
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(ratePayerCategoriesUrl, HttpMethod.GET, entity, String.class);
            JsonObject jsonObject = new JsonParser().parse(resp.getBody()).getAsJsonObject();
             System.out.println("REEEEEEEEE " + jsonObject.get("code").getAsString());
            if(!jsonObject.get("code").getAsString().equalsIgnoreCase("200")){
                isoMsg.set(72,jsonObject.get("message").getAsString());
            }
            //isoMsg.set(39,"00");
            JsonArray ja  = jsonObject.getAsJsonObject("data").get("content").getAsJsonArray();
            System.out.println(ja.toString());
            Iterator<JsonElement> it = ja.iterator();
            final String[] ratepayerData = {""};
            while(it.hasNext()){
                JsonObject je = it.next().getAsJsonObject();
                System.out.println(je.toString());
                ratepayerData[0] += je.get("id").getAsInt()+ "|";
                ratepayerData[0] += je.get("category").getAsString()+ "|";
                ratepayerData[0] += je.get("code").getAsString()+ "|";
                ratepayerData[0]+= "#";
            }
            System.out.println("RatepayerData " + ratepayerData[0]);
            isoMsg.set(39,"00");
            isoMsg.set(72,ratepayerData[0]);

        }catch(Exception e){

            e.printStackTrace();
        }

        return isoMsg;
    }

}
