package cn.aulang.oauth.client.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 23:01
 */
@Data
@ConfigurationProperties("oauth")
public class OAuthProperties {
    private String clientId;
    private String clientSecret;

    private String tokenUrl;
    private String profileUrl;

    private String[] authedUrls;
}
