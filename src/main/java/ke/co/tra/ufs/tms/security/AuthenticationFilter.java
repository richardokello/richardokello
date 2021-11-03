/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.FilterInvocation;

/**
 *
 * @author Cornelius M
 */
public class AuthenticationFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        log.info("Processing AuthenticationFilter....");
        if (fi.getHttpRequest().getMethod().equalsIgnoreCase("OPTIONS")) {
            log.warn("================== PLEASE REVIEW SECURITY ISSUES ON OPTIONS HEADERS ====================");
            fi.getHttpResponse().setStatus(200);
            fi.getHttpResponse().setHeader("Access-Control-Allow-Origin", "*");
            fi.getHttpResponse().setHeader("cache-control", "public");
            fi.getHttpResponse().setHeader("origin", "*");
            fi.getHttpResponse().setHeader("Access-Control-Allow-Headers", "Authorization, cache-control, Accept, Accept-Language, Content-Language, Content-Type");

        }
        else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
