package ke.tra.ufs.webportal.wrappers;

import ke.tra.ufs.webportal.entities.UfsAuthentication;
import ke.tra.ufs.webportal.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/*
@Author Kenny

- service used to set extra log parameters that arent provided.
- used in logger service implementation
 */
@Service
public class LogExtras {

    @Value("${baseUrl}")
    private String url;
    @Value("${client-id}")
    private String clientId;
    @Autowired
    AuthenticationRepository urepo;
    String fullName = null;
    private final HttpServletRequest request;


    public LogExtras(HttpServletRequest request) {
        this.request = request;
    }

    public String getSource() {
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        return source;
    }


    public String getIpAddress() {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    public String getClientId() {
        return clientId;
    }

    public Long getUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        UfsAuthentication userAuth = urepo.findByusernameIgnoreCase(username);
        fullName = userAuth.getUser().getFullName();
        return userAuth.getUserId();
    }

    public String getFullName() {
        return fullName;
    }
}
