package cn.aulang.office.web.config;

import cn.aulang.office.web.properties.OnlyOfficeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:25
 */
@Configuration
@EnableConfigurationProperties(OnlyOfficeProperties.class)
public class OnlyOfficeConfig {
}
