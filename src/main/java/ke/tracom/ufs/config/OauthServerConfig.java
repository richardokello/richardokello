package ke.tracom.ufs.config;

import ke.tracom.ufs.utils.ResponseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
//@EnableWebSecurity
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final AuthenticationManager authenticationManager;
    private final WebResponseExceptionTranslator exceptionTranslator;
    private final UserDetailsService userDetailsService;
    private final TokenStore tokenStore;
    private final ResponseFilter responseFilter;
    private final AccessDeniedHandler accessDeniedHandler;

    public OauthServerConfig(DataSource dataSource, AuthenticationManager authenticationManager,
                             WebResponseExceptionTranslator exceptionTranslator, UserDetailsService userDetailsService,
                             TokenStore tokenStore, ResponseFilter responseFilter, AccessDeniedHandler accessDeniedHandler) {
        this.dataSource = dataSource;
        this.exceptionTranslator = exceptionTranslator;
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.responseFilter = responseFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .addTokenEndpointAuthenticationFilter(responseFilter);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore)
                .exceptionTranslator(exceptionTranslator)
                .userDetailsService(userDetailsService)
                //				.tokenEnhancer(tokenEnhancer())
                .tokenServices(getTokenServices())
                ;

    }

    /**
     * Configure clients with jdbc DataSource
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .jdbc(dataSource);
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices getTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
//		tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

}
