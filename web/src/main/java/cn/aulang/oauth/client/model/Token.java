package cn.aulang.oauth.client.model;

import lombok.Data;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 23:06
 */
@Data
public class Token {
    private String access_token;
    private String refresh_token;
    private int expires_in;
}
