package ke.tra.com.tsync.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfigs {



    /*
    String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());


     */

    /*
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    } */

   // @Autowired
  //  GatewaySettingsCache gatewaySettingsCache;
   // @PostConstruct
   //public void init(){
        // init code goes here
       // GeneralSettingsCache gs =  new GeneralSettingsCache( "1", GeneralSettingsCache.SignOnState.SIGNON);
       // gatewaySettingsCache.save(gs);
   //}


}
