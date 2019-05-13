/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author emuraya
 */
@Service
public class CommunicationService {

    @Value("${baseUrl}")
    public String communicationUrl;

    public void sendEmail(EmailBody mail) {
//           call send email code  here
        RestTemplate template = new RestTemplate();
        HttpEntity<EmailBody> request = new HttpEntity<>(mail);


        System.out.println(
                "Sending email..." + mail.getMessage().toString());
        try {
//        ResponseEntity<EmailBody> responses = template.postForObject(communicationUrl, mail, EmailBody.class);
            template.exchange(communicationUrl + "miliki-communications-service/communication/send-email", HttpMethod.POST, request, EmailBody.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println("Communication service is unreachable ...");
        }
//        if sending email fails, queue and try again later

    }
}
