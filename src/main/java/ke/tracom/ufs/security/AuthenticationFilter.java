/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.security;

import ke.tracom.ufs.config.multitenancy.ThreadLocalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Cornelius M
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());


//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        FilterInvocation fi = new FilterInvocation(request, response, chain);
//        log.info("Processing AuthenticationFilter....");
//        if (fi.getHttpRequest().getMethod().equalsIgnoreCase("OPTIONS")) {
//            log.warn("================== PLEASE REVIEW SECURITY ISSUES ON OPTIONS HEADERS ====================");
//            fi.getHttpResponse().setStatus(200);
//            fi.getHttpResponse().setHeader("Access-Control-Allow-Origin", "*");
//            fi.getHttpResponse().setHeader("cache-control", "public");
//            fi.getHttpResponse().setHeader("origin", "*");
//            fi.getHttpResponse().setHeader("Access-Control-Allow-Headers", "Authorization, cache-control, Accept, Accept-Language, Content-Language, Content-Type");
//
//        }
//        else{
//            chain.doFilter(request, response);
//        }
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        FilterInvocation fi = new FilterInvocation(httpServletRequest, httpServletResponse, filterChain);
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
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {
    }

}
