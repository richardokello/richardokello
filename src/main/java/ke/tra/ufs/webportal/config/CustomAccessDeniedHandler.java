/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.config;

import com.cm.projects.spring.resource.chasis.wrappers.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Cornelius M
 */
@Component
//@Primary
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ade) throws IOException, ServletException {
        response.setStatus(403);
        response.setHeader("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ResponseWrapper responseObject = new ResponseWrapper();
        responseObject.setCode(403);
        responseObject.setMessage("You don't have permissions to access this resource");
        String responseMsg = mapper.writeValueAsString(responseObject);
        response.getWriter().write(responseMsg);
    }

}
