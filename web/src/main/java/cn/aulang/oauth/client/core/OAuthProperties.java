package cn.aulang.oauth.client.core;

import lombok.Data;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 23:01
 */
@Data
public class OAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;

    private String tokenUrl;
    private String profileUrl;
}
