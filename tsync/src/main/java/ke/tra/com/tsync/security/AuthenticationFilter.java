package ke.tra.com.tsync.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

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
