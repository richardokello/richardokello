/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.security;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.service.SysConfigService;
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

/**
 * @author Owori Juma
 */
//@Component
public class OTPFilter extends OAuth2ClientAuthenticationProcessingFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private TokenStore tokenStore;
    private final SysConfigService configService;

    public OTPFilter(TokenStore tokenStore, SysConfigService configService) {
        super("/");
        this.tokenStore = tokenStore;
        this.configService = configService;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean otpEnabled = isOtpEnabled(configService);
        if (otpEnabled) {
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
                    || fi.getRequestUrl().contains("/oauth/logout") || fi.getRequestUrl().contains("/otp/verification") || fi.getRequestUrl().contains("/user/me")
                    || (accessToken != null && accessToken instanceof OtpOAuth2AccessToken)) {
                log.warn("OTP Enabled ----->");
                chain.doFilter(request, response);
            } else {
                log.warn("Current user is not OTP Authenticated (Accessed resource '{}')", fi.getRequestUrl());
                fi.getHttpResponse().sendRedirect(baseUrl + "/otp/error");
                fi.getHttpResponse().setStatus(401);
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    public static boolean isOtpEnabled(SysConfigService configService) {
        UfsSysConfig config = configService.fetchSysConfig("System Integration", "otpEnabled");
        boolean otpEnabled;
        if (Objects.nonNull(config)) {
            if (config.getValue().equals("1")) {
                otpEnabled = true;
            } else {
                otpEnabled = false;
            }
        } else {
            otpEnabled = false;
        }
        return otpEnabled;
    }

    @Override
    public void destroy() {
    }

}
