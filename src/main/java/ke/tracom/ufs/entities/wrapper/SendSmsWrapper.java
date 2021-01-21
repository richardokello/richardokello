package ke.tracom.ufs.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class SendSmsWrapper {


    private String content;
    private String timeToSendType;
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date timeToSend;
    private String messageStatus;
    private String selectedrecepients;
    private String enteredrecepients;
    private String successfullySentTo;
    private String failedSentTo;
    private String smsType;
    private String recepientType;

    public SendSmsWrapper() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getRecepientType() {
        return recepientType;
    }

    public void setRecepientType(String recepientType) {
        this.recepientType = recepientType;
    }
}
