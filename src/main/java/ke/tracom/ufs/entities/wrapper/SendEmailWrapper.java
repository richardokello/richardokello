package ke.tracom.ufs.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class SendEmailWrapper {

    private String content;
    private String signature;
    private String timeToSendType;
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date timeToSend;
    private String messageStatus;
    private String selectedrecepients;
    private String enteredrecepients;
    private String successfullySentTo;
    private String failedSentTo;
    private String emailType;
    private String recepientType;
    private String cc;
    private String bcc;
    private MultipartFile[] attachement;

    public SendEmailWrapper() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimeToSendType() {
        return timeToSendType;
    }

    public void setTimeToSendType(String timeToSendType) {
        this.timeToSendType = timeToSendType;
    }

    public Date getTimeToSend() {
        return timeToSend;
    }

    public void setTimeToSend(Date timeToSend) {
        this.timeToSend = timeToSend;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getSelectedrecepients() {
        return selectedrecepients;
    }

    public void setSelectedrecepients(String selectedrecepients) {
        this.selectedrecepients = selectedrecepients;
    }

    public String getEnteredrecepients() {
        return enteredrecepients;
    }

    public void setEnteredrecepients(String enteredrecepients) {
        this.enteredrecepients = enteredrecepients;
    }

    public String getSuccessfullySentTo() {
        return successfullySentTo;
    }

    public void setSuccessfullySentTo(String successfullySentTo) {
        this.successfullySentTo = successfullySentTo;
    }

    public String getFailedSentTo() {
        return failedSentTo;
    }

    public void setFailedSentTo(String failedSentTo) {
        this.failedSentTo = failedSentTo;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getRecepientType() {
        return recepientType;
    }

    public void setRecepientType(String recepientType) {
        this.recepientType = recepientType;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public MultipartFile[] getAttachement() {
        return attachement;
    }

    public void setAttachement(MultipartFile[] attachement) {
        this.attachement = attachement;
    }
}
