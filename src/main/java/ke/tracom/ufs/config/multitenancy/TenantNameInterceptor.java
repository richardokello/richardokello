package ke.tracom.ufs.config.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TenantNameInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Implement your logic to extract the Tenant Name here. Another way would be to
        // parse a JWT and extract the Tenant Name from the Claims in the Token. In the
        // example code we are just extracting a Header value:
        String tenantName = request.getHeader("X-TenantID");
        String language = request.getHeader("X-Language");
        System.err.println("Tenant Name: " + tenantName);
        System.err.println("^^^^^^^^ Language choosen"+language);

        // Always set the Tenant Name, so we avoid leaking Tenants between Threads even in the scenario, when no
        // Tenant is given. I do this because if somehow the afterCompletion Handler isn't called the Tenant Name
        // could still be persisted within the ThreadLocal:
        ThreadLocalStorage.setTenantName(tenantName);
        ThreadLocalStorage.setLanguage(language);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // After completing the request, make sure to erase the Tenant from the current Thread. It's
        // because Spring may reuse the Thread in the Thread Pool and you don't want to leak this
        // information:
        ThreadLocalStorage.setTenantName(null);
    }
}
