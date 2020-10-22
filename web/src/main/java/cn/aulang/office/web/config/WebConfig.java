package cn.aulang.office.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

/**
 * 运行跨域
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-20 18:19
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor contentInterceptor = new WebContentInterceptor();
        contentInterceptor.setCacheSeconds(0);
        registry.addInterceptor(contentInterceptor).addPathPatterns("/**");
    }
}
