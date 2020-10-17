package cn.aulang.office.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 21:28
 */
@Data
@ConfigurationProperties("onlyoffice")
public class OnlyOfficeProperties {
    private String secret;
}
