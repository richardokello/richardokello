/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Owori Juma
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
//    private final LoggerServiceLocal loggerService;
//    
//    public CustomAccessDeniedHandler(LoggerServiceLocal loggerService){
//        this.loggerService = loggerService;
//    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ade) throws IOException, ServletException {
//        String baseUrl = request.getContextPath();
//        response.sendRedirect(baseUrl + "/access-denied");

        response.setStatus(403);
        response.setHeader("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ResponseWrapper responseObject = new ResponseWrapper();
        responseObject.setMessage("Access denied, this may due to permissions or page has expired reload the page and try again");
        responseObject.setCode(403);
        String responseMsg = mapper.writeValueAsString(responseObject);
        response.getWriter().write(responseMsg);

    }

}
