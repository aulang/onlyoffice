package cn.aulang.oauth.client.config;

import cn.aulang.oauth.client.core.OAuthProperties;
import cn.aulang.oauth.client.core.OAuthTemplate;
import cn.aulang.oauth.client.filter.HeaderTokenFilter;
import cn.aulang.oauth.client.user.UserDetailsService;
import cn.aulang.oauth.client.user.impl.DefaultDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 23:35
 */
@Configuration
@ComponentScan("cn.aulang.oauth.client.controller")
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfig {
    @Autowired
    private OAuthProperties properties;

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Autowired
    public OAuthTemplate oAuthTemplate(RestTemplate restTemplate) {
        return new OAuthTemplate(restTemplate, properties);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService() {
        return new DefaultDetailsService();
    }

    @Bean
    @Autowired
    public FilterRegistrationBean registerHeaderTokenFilter(UserDetailsService userDetailsService, OAuthTemplate authTemplate) {
        HeaderTokenFilter authFilter = new HeaderTokenFilter(authTemplate, userDetailsService);

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setOrder(1);
        registration.setFilter(authFilter);
        registration.setName("headerTokenFilter");
        registration.addUrlPatterns(properties.getAuthedUrls());

        return registration;
    }
}
