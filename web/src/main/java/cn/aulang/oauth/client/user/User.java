package cn.aulang.oauth.client.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aulang
 * @email aulang@qq.com
 * @date 2020-10-22 10:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String token;
}
