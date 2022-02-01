package ke.co.tra.ufs.tms.config.multitenancy;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MultiTenancyFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(httpServletRequest.getMethod())){
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET,PATCH, OPTIONS, DELETE");
            httpServletResponse.setHeader("Access-Control-Allow-Headers",
                    "X-Requested-With, X-Auth-Token,Authorization,Content-Type," +
                            "Content-Language,X-Language,X-TenantID,Accept,X-FORWARDED-FOR,X-CSRF-TOKEN,Accept-Language,Cache-control");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
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
