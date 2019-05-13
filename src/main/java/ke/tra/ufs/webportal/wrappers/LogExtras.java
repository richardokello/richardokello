package ke.tra.ufs.webportal.wrappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/*
@Author eli.muraya

- service used to set extra log parameters that arent provided.
- used in logger service implementation
 */
@Service
public class LogExtras {

    @Value("${baseUrl}")
    private String url;
    @Value("${client-id}")
    private String clientId;
//    @Autowired
//    AuthenticationRepository urepo;

    //    Authentication auth;
    private final HttpServletRequest request;


    public LogExtras(HttpServletRequest request) {
        this.request = request;

    }

    /*
    source - source of the request eg, browser, postman
     */
    public String getSource() {
//        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
//        return source;
        return null;
    }

    /*
    - IP Address of the machine making the request
     */
    public String getIpAddress() {
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
    }

    /*
    -client id is the oauth2 clint id assigned to the maker of the request
     */
    public String getClientId() {
        return clientId;
    }

    /*
    - id of the currently logged in user
    - id of msker
     */
    public Long getUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println("LOGGED IN USER : " + username);
//        return urepo.findByusernameIgnoreCase(username).getUserId();
        return null;
    }


}
