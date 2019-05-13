package ke.tra.ufs.webportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

/**
*
* @author Cornelius M
*/
@Component
@Primary
public class CustomTokenEnhancer implements TokenEnhancer {

   public Logger log = LoggerFactory.getLogger(this.getClass());

   @Override
   public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//       User user = (User) authentication.getPrincipal();
//       Map<String, Object> additionalInfo = new HashMap<>();
//
//       additionalInfo.put("authorities", authentication.getAuthorities());

      // ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
       return accessToken;
   }

}
