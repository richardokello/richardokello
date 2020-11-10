package ke.tra.com.tsync.services;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class RevenueChargesSvc
{


    @Value("${get_revenue_charges}")
    private String getRevenueCharges;

    @Autowired
    private RestTemplate myRestTemplate;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RevenueChargesSvc.class);

    public String fetchAmountByZoneStreamID(String zoneid, String streamid){
        String amount = "0";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getRevenueCharges)
                .queryParam("zoneId", zoneid)
                .queryParam("StreamId", streamid);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            HttpEntity entity = new HttpEntity(headers);
            String url = builder.toUriString();
            ResponseEntity<String> resp = myRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject jsonObject = new JsonParser().parse(resp.getBody()).getAsJsonObject();
            amount = jsonObject.getAsJsonObject("data").get("amount").getAsString();
            //amount = ISOUtil.formatAmount(0l,12);
            Double doubleval =  Double.parseDouble(amount);
            Integer truncatedAmount =  doubleval.intValue();
            amount = String.format("%012d", truncatedAmount);
            System.out.println("ISO AMOUNT"   + amount);

        }catch (Exception e) {
            amount = "000000000000";
            e.printStackTrace();
        }
        return  amount;

    }
}
