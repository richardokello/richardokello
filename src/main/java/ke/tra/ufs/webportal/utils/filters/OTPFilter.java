/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.utils.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.security.OTPAuthentication;
import ke.tra.ufs.webportal.security.OtpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author eli.muraya
 */
//@Component
public class OTPFilter extends OAuth2ClientAuthenticationProcessingFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TokenStore tokenStore;

    public OTPFilter(TokenStore tokenStore) {
        super("/");
        this.tokenStore = tokenStore;
    }

    //    @Override
//    public void init(FilterConfig fc) throws ServletException {
//    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String baseUrl = fi.getHttpRequest().getContextPath();

        OAuth2Authentication a = (OAuth2Authentication) auth;
        OAuth2AccessToken accessToken = null;
        if (tokenStore != null && a != null) {
            accessToken = tokenStore.getAccessToken(a);
        }
        if (auth == null || auth instanceof AnonymousAuthenticationToken
                || fi.getRequestUrl().equalsIgnoreCase("/otp") || auth instanceof OTPAuthentication
                || fi.getRequestUrl().contains("/oauth/token") || fi.getRequestUrl().contains("/otp/error")
                || fi.getRequestUrl().contains("/oauth/logout") || fi.getRequestUrl().contains("/otp/verification")
                || (accessToken != null && accessToken instanceof OtpOAuth2AccessToken)
                ||/* fi.getRequestUrl().contains("/user/me") || */ fi.getRequestUrl().contains("/otp/resend") || fi.getRequestUrl().contains("/test")) {
            chain.doFilter(request, response);
        } else {
            log.warn("Current user is not OTP Authenticated (Accessed resource '{}')", fi.getRequestUrl());
            fi.getHttpResponse().setStatus(401);
            fi.getHttpResponse().setHeader("Content-Type", "application/json");
            ObjectMapper mapper = new ObjectMapper();
            ResponseWrapper responseObject = new ResponseWrapper();
            responseObject.setCode(401);
            responseObject.setMessage("You don't have permissions to access this resource");
            String responseMsg = mapper.writeValueAsString(responseObject);
            fi.getHttpResponse().getWriter().write(responseMsg);

        }
    }

    @Override
    public void destroy() {
    }

}
