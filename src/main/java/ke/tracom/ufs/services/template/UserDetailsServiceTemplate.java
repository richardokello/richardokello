package ke.tracom.ufs.services.template;

import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.security.CustomUserDetails;
import ke.tracom.ufs.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Used to fetch user details using username
 *
 * @author eli.muraya
 *
 */
@Service
public class UserDetailsServiceTemplate implements UserDetailsService {
    
    private final Logger log;
    private final AuthenticationRepository authRepository;
    
    public UserDetailsServiceTemplate(AuthenticationRepository authRepository) {
        super();
        this.log = LoggerFactory.getLogger(this.getClass());
        this.authRepository = authRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Processing authentication for user {}", username);
        UfsAuthentication auth = this.authRepository.findByusernameIgnoreCase(username);
        if (auth == null) {
            throw new UsernameNotFoundException("Sorry user not found");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        auth.getUser().getUfsUserWorkgroupList().forEach(workgroup -> {
            if ((workgroup.getIntrash() == null || workgroup.getIntrash().equals(AppConstants.INTRASH_NO))
                    && (workgroup.getWorkgroup().getActionStatus().equals(AppConstants.STATUS_APPROVED)
                    || workgroup.getWorkgroup().getActionStatus().equals(AppConstants.STATUS_REJECTED))
                    && (workgroup.getWorkgroup().getIntrash() == null || workgroup.getWorkgroup().getIntrash().equals(AppConstants.INTRASH_NO))) {
                workgroup.getWorkgroup().getUfsWorkgroupRoleList().forEach(groupRole -> {
                    if ((groupRole.getIntrash() == null || groupRole.getIntrash().equals(AppConstants.INTRASH_NO))
                            && (groupRole.getRole().getIntrash() == null || groupRole.getRole().getIntrash().equals(AppConstants.INTRASH_NO))
                             ) {
                        groupRole.getRole().getUfsRolePermissionList().forEach(perm -> {
                            authorities.add(new SimpleGrantedAuthority(perm.getPermission().getCaption()));
                        });
                    }
                });
            }
        });
        boolean isEnabled = auth.getUser().getStatus() == AppConstants.STATUS_ACTIVE;
        boolean nonExpired = !Objects.equals(auth.getPasswordStatus(), AppConstants.STATUS_EXPIRED);
        boolean nonLocked = !Objects.equals(auth.getPasswordStatus(), AppConstants.STATUS_LOCKED);
        
        return new CustomUserDetails(username, auth.getPassword(), isEnabled, true, nonExpired,
                nonLocked, authorities, auth.getUser().getUserId(), auth.getUser().getFullName(), auth.getUser().getGender().getGender());
    }
    
}
