package ke.tracom.ufs.wrappers;

import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.repositories.UserRepository;
import ke.tracom.ufs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class LogExtras {

    @Value("${client-id}")
    private String clientId;
    @Autowired
    AuthenticationRepository urepo;
    private final HttpServletRequest request;
    private final UserService userService;

    private String fullname;


    public LogExtras(HttpServletRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;
    }


    public String getSource() {
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        return source;
    }


    public String getIpAddress() {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            System.out.println("X-FORWARDED-FOR===>" + remoteAddr);
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            if (remoteAddr.contains(",")) {
                String[] ips = remoteAddr.split(",");
                remoteAddr = Stream.of(ips).filter(x -> !x.equals("127.0.0.1")).filter(x -> !x.equals("0:0:0:0:0:0:0:1")).findAny().orElse(request.getRemoteAddr());
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
        Optional<UfsUser> ufsUser  = userService.findById(userAuth.getUserId());
        if(ufsUser.isPresent()){
            this.setFullname(ufsUser.get().getFullName());

        }
        return userAuth.getUserId();

    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullName() {
        return fullname;
    }

}
