/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.config;

/**
 *
 * @author Owori Juma
 */
/*
import ke.co.tra.ufs.tms.security.CustomTokenEnhancer;
import ke.co.tra.ufs.tms.utils.ResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    private TokenEnhancer accessTokenEnhancer;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Qualifier("customExceptionTranslator")
    private WebResponseExceptionTranslator exceptionTranslator;
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ResponseFilter responseFilter;


    @Value("${app.params.security.ui-client}")
    private String uiClient;
    @Value("${app.params.security.ui-secret}")
    private String uiSecret;
    @Value("${app.params.security.ui-bank-client}")
    private String uiBankClient;
    @Value("${app.params.security.ui-bank-secret}")
    private String uiBankSecret;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
//                .accessTokenConverter(accessTokenConverter)
                .exceptionTranslator(exceptionTranslator)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancer())
                .tokenServices(getTokenServices())
                ;

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(uiClient).secret(uiSecret)
                .authorizedGrantTypes("password", "refresh_token", "client_credentials")
                .scopes("read", "create", "updated", "delete", "openid");
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.addTokenEndpointAuthenticationFilter(responseFilter);
    }
    
    @Bean
    @Primary
    public AuthorizationServerTokenServices getTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }
//    
    @Bean
    public TokenEnhancer tokenEnhancer(){
        return new CustomTokenEnhancer();
    }

}
*/
