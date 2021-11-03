/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.util.Locale;

/**
 *
 * @author Owori Juma
 */
public interface NotificationService {
    /**
     * Send sms in the background
     * @param message
     * @param phoneNumber 
     */
    public void sendAsyncSms(String message, String phoneNumber);
    /**
     * Used to send email using the default email template
     * @param email
     * @param title
     * @param content 
     */
    public void sendEmail(String email, String title, String content);
    /**
     * Used to send bulk emails using the default email template
     * @param emails
     * @param title
     * @param content 
     */
    public void sendEmail(String[] emails, String title, String content);
    /**
     * Send bulk email in the background
     * @param emails
     * @param title
     * @param content 
     */
    public void sendEmailAsync(String[] emails, String title, String content);
    /**
     * Used to send email using the default email template in the background
     * @param email
     * @param title
     * @param content 
     */
    public void sendEmailAsync(String email, String title, String content);
    /**
     * 
     * @param locale 
     */
    public void setLocale(Locale locale);
    
    public String generateOTPEmailContent(String receiverName, String otp);
}
