/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tracom.ufs.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cornelius M
 */
@Component
@Primary
public class CustomOAuth2ExceptionRenderer extends DefaultOAuth2ExceptionRenderer {
    
    private final Logger log;

    public CustomOAuth2ExceptionRenderer() {
        super();
        this.log = LoggerFactory.getLogger(this.getClass());
    }    
}
