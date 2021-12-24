package ke.tracom.ufs.utils;

import ke.tracom.ufs.config.messageSource.MessageSourceConfig;
import ke.tracom.ufs.config.multitenancy.ThreadLocalStorage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class MessageParser {
    private final MessageSourceConfig messageSourceConfig;

    public String parse(String message){
        return messageSourceConfig.message().getMessage(message,null,new Locale(ThreadLocalStorage.getLanguage()));
    }
}
