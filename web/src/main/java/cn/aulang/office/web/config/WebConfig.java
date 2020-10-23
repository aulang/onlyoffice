package cn.aulang.office.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 运行跨域
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-20 18:19
 */
@Configuration
@EnableConfigurationProperties(MultipartProperties.class)
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private MultipartProperties multipartProperties;


    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

        multipartResolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
        multipartResolver.setMaxUploadSize(multipartProperties.getMaxRequestSize().toBytes());
        multipartResolver.setMaxUploadSizePerFile(multipartProperties.getMaxFileSize().toBytes());

        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor contentInterceptor = new WebContentInterceptor();
        contentInterceptor.setCacheSeconds(0);
        registry.addInterceptor(contentInterceptor).addPathPatterns("/**");
    }
}
