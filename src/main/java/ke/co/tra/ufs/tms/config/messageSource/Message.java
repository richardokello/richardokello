package ke.co.tra.ufs.tms.config.messageSource;

import ke.co.tra.ufs.tms.config.multitenancy.ThreadLocalStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class Message {
  private final MessageSourceConfig messageSourceConfig;
  public String setMessage(String msgKey){
      String lang = ThreadLocalStorage.getLanguage();
      return messageSourceConfig.messageSource().getMessage(msgKey,new Object[0], new Locale(lang));
  }
}
