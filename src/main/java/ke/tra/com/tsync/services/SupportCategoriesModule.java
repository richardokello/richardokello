package ke.tra.com.tsync.services;

import com.google.gson.*;
import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
import ke.tra.com.tsync.wrappers.support.SupportCategories;
import netscape.javascript.JSObject;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.List;

@Service
public class SupportCategoriesModule {

    @Value("${support_categories_url}")
    private String supportCategoriesUrl;

    @Autowired
    private RestTemplate myRestTemplate;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SupportCategoriesModule.class);

    // Get All support Categories
    public ISOMsg getAllSupportCategories (ISOMsg isoMsg){
        try {

            String[] field72final ={""};
            ResponseEntity<String> responseEntity =
                    myRestTemplate.getForEntity(supportCategoriesUrl,String.class);
            JsonParser parser = new JsonParser();
            JsonElement content= parser.parse(responseEntity.getBody());
            JsonObject  jobject = content.getAsJsonObject().getAsJsonObject("data");
            JsonArray ja  = jobject.getAsJsonArray("content");

            ja.iterator().forEachRemaining(
                    (item)->{
                        String id = item.getAsJsonObject().get("id").getAsString();
                        String nature = item.getAsJsonObject().get("nature").getAsString();
                       field72final[0] += id + "*"  + nature+ "#";
                    }
            );

            isoMsg.set(72,field72final[0]);
            isoMsg.set(39,"00");

        }catch (Exception e){
            e.printStackTrace();
        }
        return  isoMsg;
    }





}
