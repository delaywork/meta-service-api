package com.meta.configure;

import com.meta.model.enums.OauthGrantTypeEnum;
import com.meta.model.request.vo.UserOauthVo;
import com.meta.service.ClientServiceImpl;
import com.meta.service.UserDetailsServiceImpl;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.*;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Value("${JWT_SIGNING_KEY:pbiRBFxfdeDVm4VDVrTy72v9DC+2L5vbclMNM2iO2SE=}")
    private String jwt_signing_key;

    // 该对象用来支持 password 模式
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    // 该对象将为刷新token提供支持
    @Autowired
    UserDetailsService userDetailsService;

    @Resource
    private ClientServiceImpl clientService;

    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Resource
    private OauthWebResponseExceptionTranslator oauthWebResponseExceptionTranslator;

    @Resource
    private MetaPasswordEncoder metaPasswordEncoder;

    // 配置授权模式
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 获取客户端配置
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 表示支持 client_id 和 client_secret 做登录认证
        security.allowFormAuthenticationForClients();
    }

    /**
     * 认证提供器
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 用户不存在异常抛出
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // super.configure(endpoints);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancerList = new ArrayList<>();
        tokenEnhancerList.add(tokenEnhancer());
        tokenEnhancerList.add(accessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);

        endpoints.tokenEnhancer(tokenEnhancerChain)
            .authenticationManager(authenticationManager)
            .accessTokenConverter(accessTokenConverter())
            .userDetailsService(userDetailsServiceImpl)
            .tokenGranter(this.getDefaultTokenGranters(endpoints))// 获取所有认证类型
            .tokenStore(tokenStore())
            .exceptionTranslator(oauthWebResponseExceptionTranslator)// 认证异常自定义处理
            .reuseRefreshTokens(false)// refresh token：重复使用(true：不生成新的 refresh token)、非重复使用(false：生成新的 refresh token)，默认为true
            .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);// 允许 GET、POST 请求获取 token，即访问端点：oauth/token
    }

    /**
     * JWT 生成token 定制处理
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap();
            UserOauthVo userOauthVo = (UserOauthVo) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("accountId", userOauthVo.getAccountId());
            additionalInfo.put("accountName", userOauthVo.getAccountName());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * JWT 加密
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwt_signing_key);
        return jwtAccessTokenConverter;
    }

    /**
     * 使用redis 存取token
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }

    /**
     * 自定义认证类型
     */
    private TokenGranter getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        // 获取原有默认的授权类型
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 创建自定义认证类型
        WeChatTokenGranter weChatTokenGranter = new WeChatTokenGranter(
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(),
                OauthGrantTypeEnum.WECHAT.getOauthValue()
        );
        // 将自定义认证类型加入到原有的认证集合中 返回
        tokenGranters.add(weChatTokenGranter);
        return new CompositeTokenGranter(tokenGranters);
    }

}