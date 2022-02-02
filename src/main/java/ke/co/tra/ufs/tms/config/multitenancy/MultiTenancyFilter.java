package ke.co.tra.ufs.tms.config.multitenancy;

import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.ui.context.Theme;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MultiTenancyFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(httpServletRequest.getMethod())){
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Authorization, Content-Type,X-TenantID,X-Language,Accept,Accept-Language,X_FORWARDED_FOR,X-CSRF-TOKEN");
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"POST,DELETE,GET,PUT,OPTIONS");
            httpServletResponse.setStatus(httpServletResponse.SC_OK);
        }
        else {
            // Implement your logic to extract the Tenant Name here. Another way would be to
            // parse a JWT and extract the Tenant Name from the Claims in the Token. In the
            // example code we are just extracting a Header value:
            System.err.println("== Calling multi-tenant filter ==");
            String tenantName = httpServletRequest.getHeader("X-TenantID");
            String language = httpServletRequest.getHeader("X-Language");

            // Always set the Tenant Name, so we avoid leaking Tenants between Threads even in the scenario, when no
            // Tenant is given. I do this because if somehow the afterCompletion Handler isn't called the Tenant Name
            // could still be persisted within the ThreadLocal:
            ThreadLocalStorage.setTenantName(tenantName);
            ThreadLocalStorage.setLanguage(language);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
