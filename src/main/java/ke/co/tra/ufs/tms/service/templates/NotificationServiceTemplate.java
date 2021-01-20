/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper;
import ke.co.tra.ufs.tms.service.ConfigService;
import ke.co.tra.ufs.tms.service.NotificationService;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.thymeleaf.context.Context;

/**
 * @author Owori Juma
 */
@Service
public class NotificationServiceTemplate implements NotificationService {

    private final Logger log;
    private final ConfigService configService;
    private final SysConfigService sysConfigService;
    private JavaMailSender emailSender;
    private Locale locale;
    private TemplateEngine templateEngine;
    private String logo;
    private Context ctx;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.template.tempPath}")
    private String tempPath;

    @Value("${app.template.tempFileName}")
    private String templateName;

    private String smsPass;
    private String smsUser;
    private String smsUrl;

    public NotificationServiceTemplate(ConfigService configService, SysConfigService sysConfigService) {
        log = LoggerFactory.getLogger(this.getClass());
        locale = new Locale("en", "ke");
        this.configService = configService;
        //this.configMailSender(this.configService);
        this.sysConfigService = sysConfigService;

    }

    /**
     * Used to intialize configuration from the database
     */
    private void intializeConfigs() {

    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Autowired
    public void injectEmailDependencies(TemplateEngine templateEngine/*, JavaMailSender emailSender */) {
//        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }


    @Override
    @Async
    public void sendAsyncSms(String message, String phoneNumber) {

    }

    private MimeMessageHelper intializeHelper(MimeMessage mimeMessage, String title, String content) throws MessagingException {
        ctx = new Context(locale);
        ctx.setVariable("title", title);
        ctx.setVariable("content", content);
        String htmlContent = this.templateEngine.process("email/general-template", ctx);
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

        message.setSubject(title);
        message.setText(htmlContent, true);
        message.setFrom(from);
        return message;
    }

    @Override
    public void sendEmail(String email, String title, String content) {
        UfsSysConfig syconf = this.sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "microsoftExchange");
        if (syconf.getValue().equals("1")) {
            final String username = sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "mailUsername").getValue(); // User id you use to login to
            String domain = sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "exchangeDomain").getValue();
            final String password = sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "mailPassword").getValue();
            final String mail = sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "mailUsername").getValue();
            try {
                ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
                ExchangeCredentials credentials = new WebCredentials(username, password, domain);
                service.setCredentials(credentials);
                service.setUrl(new URI(sysConfigService.fetchSysConfig(AppConstants.ENTITY_SYSTEM_INTEGRATION, "exchangeUrl").getValue()));
                EmailMessage msg = new EmailMessage(service);
                msg.setSubject(title);
                msg.setBody(MessageBody.getMessageBodyFromText(content));
                msg.getToRecipients().add(email);
                msg.send();

                log.debug("Sending mail to {} and content {}", email, content);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);

            }
        } else {
            try {
                emailSender = this.mailConfiguration();
                log.debug("\n=============== SENDING EMAIL =================\n"
                        + "Title: {}\n"
                        + "Recipient: {}\n"
                        + "Content: {} \n", title, email, content);

                ctx = new Context(locale);
//            String imageResourceName = "thymeleafIcon";
                ctx.setVariable("title", title);
                ctx.setVariable("content", content);
//            ctx.setVariable("imageResourceName", imageResourceName);
                UfsSysConfig tempConfig = this.sysConfigService.fetchSysConfig(AppConstants.ENTITY_GLOBAL_INTEGRATION, AppConstants.PARAMETER_EMAIL_TEMPLATE_URL);
                String htmlContent;
                File file = new File(tempConfig.getValue());
                if (file.exists() && file.isFile() && file.canRead()) {
                    TemplateEngine templateEngine = new TemplateEngine();
                    FileTemplateResolver fResolver = new FileTemplateResolver();
                    log.debug("Thymeleaf parent dir {} and file {}", file.getParent(), file.getName());
                    fResolver.setPrefix(file.getParent() + File.separator);
                    fResolver.setSuffix("");
                    fResolver.setCacheable(false);
                    templateEngine.setTemplateResolver(fResolver);
                    htmlContent = templateEngine.process(file.getName(), ctx);
                } else {
                    log.warn(AppConstants.AUDIT_LOG, "Email template file ({}) not "
                            + "found/not readable. Resolving to default file template", tempConfig.getValue());
                    htmlContent = this.templateEngine.process("email/general-template", ctx);
                }
//            String htmlContent = this.templateEngine.process("email/general-template", ctx);
//        log.info("Resolved html content " + htmlContent);

//        SimpleMailMessage message = new SimpleMailMessage();
                MimeMessage mimeMessage = emailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

                message.setTo(email);
                message.setSubject(title);
                message.setText(htmlContent, true);
                message.setFrom(from);
                emailSender.send(mimeMessage);
            } catch (MessagingException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

    }

    @Override
    public void sendEmail(String[] emails, String title, String content) {
        for (String email : emails) {
            this.sendEmailAsync(email, title, content);
        }
        /**
         * Disabled because the local server failed to send email if an error
         * occurred on one recipient try { emailSender =
         * this.mailConfiguration(); List<CmsOutgoingMessage> messages = new
         * ArrayList<>(); for (String email : emails) {
         * messages.add(this.outgoingMsgRepo.save( new
         * CmsOutgoingMessage(AppConstants.MSG_TYPE_EMAIL, "", email, content,
         * false))); } log.debug("\n======================== SEND BULK EMAIL
         * ========================\n" + "Title: {}\n" + "Recepients: {}\n" +
         * "Content: {}\n", title, Arrays.toString(emails), content);
         * MimeMessage mimeMessage = emailSender.createMimeMessage();
         * MimeMessageHelper message = this.intializeHelper(mimeMessage, title,
         * content); message.setTo(emails); emailSender.send(mimeMessage);
         * messages.forEach(msg -> { msg.setSentStatus(true);
         * msg.setTimeSent(SharedMethods.currentDate()); });
         *
         *
         * } catch (MessagingException ex) {
         * java.util.logging.Logger.getLogger(NotificationServiceTemplate.class.getName()).log(Level.SEVERE,
         * null, ex); }
         */
    }

    @Override
    @Async
    public void sendEmailAsync(String[] emails, String title, String content) {
        this.sendEmail(emails, title, content);
    }

    @Override
    @Async
    public void sendEmailAsync(String email, String title, String content) {
        try {
            this.sendEmail(email, title, content);
        } catch (Exception ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to send email to " + email, ex);
        }
    }

//    public void sendResetPassword(Users user, String email, String token) {
//        try {
//            log.info(AppConstants.AUDIT_LOG, "Sending password reset email to ");
//            this.ctx = new Context(locale);
//
//           // ctx.setVariable("imageResourceName", logo);
//            ctx.setVariable("resetUrl", this.resetUrl + "/" + token);
//            ctx.setVariable("name", user.getLastName());
//            String content = this.templateEngine.process("mail/password-reset", ctx);
//            this.packageAndSendEmail(email, "Password Reset", content);
//        } catch (MessagingException ex) {
//            java.util.logging.Logger.getLogger(EmailServiceTemplate.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    /**
     * Used to send all email of the current service, it assumes that you have
     * set logo parameter and is an html email
     *
     * @param to
     * @param subject
     * @param content
     */
    private void packageAndSendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper message
                = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content, true);
        message.setFrom(from);

        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
//        Resource imageSource = new ClassPathResource("static/img/icon.png");
//        message.addInline(logo, imageSource);
        emailSender.send(mimeMessage);
    }

    @Override
    public String generateOTPEmailContent(String receiverName, String otp) {

        String fileName = tempPath + templateName;
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line, newLine = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("<name>") || line.contains("<OTP>") || line.contains("<senderDetails>")) {
                    newLine = line.replace("<name>", receiverName).replace("<OTP>", otp);
                    sb.append(newLine).append("<br/>");
                } else {
                    sb.append(line).append("<br/>");
                }

            }
            System.out.println(sb.toString());
        } catch (IOException ex) {
            System.out.println("Exception - " + ex);
            log.error(ex.getMessage(), ex);
        }

        return sb.toString();

    }

    @Transactional(readOnly = true)
    JavaMailSender mailConfiguration() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties props = mailSender.getJavaMailProperties();
        List<UfsSysConfigWrapper> configs = sysConfigService.getMailConfig();
        configs.forEach((config) -> {
            if (config.getParameter().equalsIgnoreCase("mailHost")) {
                mailSender.setHost(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailPort")) {
                mailSender.setPort(Integer.valueOf(config.getValue()));
            } else if (config.getParameter().equalsIgnoreCase("mailUsername")) {
                mailSender.setUsername(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailPassword")) {
                mailSender.setPassword(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailSSL")) {
                String sslEnabled = "0".equals(config.getValue()) ? "false" : "true";
                props.put("mail.smtp.starttls.enable", sslEnabled);
                props.put("mail.smtp.ssl.enable", sslEnabled);
            } else if (config.getParameter().equalsIgnoreCase("mailAuth")) {
                String auth = "0".equals(config.getValue()) ? "false" : "true";
                props.put("mail.smtp.auth", auth);
            }
        });
//        mailSender.setPort(25);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.fallback", "true");
        log.debug("\n========================= Mail Configurations ====================\n"
                + "host: {} \n username: {} \n password: {}", mailSender.getHost());
        return mailSender;
    }

}
