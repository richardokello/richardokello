/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Owori Juma
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    public static final String BEARER_AUTHENTICATION = "Bearer ";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        String token = request.getHeader("authorization");
        String baseUrl = request.getContextPath();
        if ((token != null && token.startsWith(BEARER_AUTHENTICATION))) {
            token = StringUtils.substringAfter(token, BEARER_AUTHENTICATION);
        } else if ((token = request.getParameter("access_token")) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken != null) {
            tokenStore.removeAccessToken(oAuth2AccessToken);
            response.setStatus(HttpServletResponse.SC_OK);
            //response.sendRedirect(baseUrl + "/logout");
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
