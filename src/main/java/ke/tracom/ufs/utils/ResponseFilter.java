/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.utils;

import ke.tracom.ufs.config.multitenancy.ThreadLocalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author Cornelius M
 */
@Component
public class ResponseFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig fc) throws ServletException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);

        log.info("Found filter: url " + fi.getRequestUrl().contains("/oauth/token") + " and method " + fi.getHttpRequest().getMethod());
       fi.getHttpResponse().setHeader("Access-Control-Allow-Origin", "*");
        fi.getHttpResponse().setHeader("Access-Control-Allow-Credentials", "true");
        fi.getHttpResponse().setHeader("cache-control", "public");
        fi.getHttpResponse().setHeader("origin", "*");
        fi.getHttpResponse().setHeader("Vary", "origin");
        fi.getHttpResponse().setHeader("Access-Control-Allow-Headers", "Authorization, cache-control, Accept, Accept-Language, Content-Language, Content-Type");
        if (fi.getRequestUrl().contains("/oauth/token")
                && fi.getHttpRequest().getMethod().equalsIgnoreCase("OPTIONS")) {
            fi.getHttpResponse().setStatus(200);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
