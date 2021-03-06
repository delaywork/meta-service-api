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

    // ????????????????????? password ??????
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    // ?????????????????????token????????????
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

    // ??????????????????
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // ?????????????????????
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // ???????????? client_id ??? client_secret ???????????????
        security.allowFormAuthenticationForClients();
    }

    /**
     * ???????????????
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // ???????????????????????????
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * ???????????????
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ????????????
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
            .tokenGranter(this.getDefaultTokenGranters(endpoints))// ????????????????????????
            .tokenStore(tokenStore())
            .exceptionTranslator(oauthWebResponseExceptionTranslator)// ???????????????????????????
            .reuseRefreshTokens(false)// refresh token???????????????(true?????????????????? refresh token)??????????????????(false??????????????? refresh token)????????????true
            .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);// ?????? GET???POST ???????????? token?????????????????????oauth/token
    }

    /**
     * JWT ??????token ????????????
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap();
            UserOauthVo userOauthVo = (UserOauthVo) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("accountId", userOauthVo.getAccountId().toString());
            additionalInfo.put("accountName", userOauthVo.getAccountName());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * JWT ??????
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwt_signing_key);
        return jwtAccessTokenConverter;
    }

    /**
     * ??????redis ??????token
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }

    /**
     * ?????????????????????
     */
    private TokenGranter getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        // ?????????????????????????????????
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // ???????????????????????????
        WeChatTokenGranter weChatTokenGranter = new WeChatTokenGranter(
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(),
                OauthGrantTypeEnum.WECHAT.getOauthValue()
        );
        // ????????????????????????????????????????????????????????? ??????
        tokenGranters.add(weChatTokenGranter);
        return new CompositeTokenGranter(tokenGranters);
    }

}