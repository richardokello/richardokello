/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.utils;

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
    public String baseUrl;

    public void sendEmail(EmailBody mail) {
        RestTemplate template = new RestTemplate();
        HttpEntity<EmailBody> request = new HttpEntity<>(mail);
        System.out.println("Sending email..." + mail.getMessage().toString());
        try {
            template.exchange(baseUrl + "ufs-communication-service/communication/send-email", HttpMethod.POST, request, EmailBody.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Communication service is unreachable ...");
        }
    }

    public void sendSms(EmailBody mail) {
        RestTemplate template = new RestTemplate();
        HttpEntity<EmailBody> request = new HttpEntity<>(mail);
        System.out.println("Sending SMS..." + mail.getMessage().toString());
        try {
            template.exchange(baseUrl + "ufs-communication-service/communication/bonga/send-sms", HttpMethod.POST, request, EmailBody.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Communication service is unreachable ...");
        }

    }
}
