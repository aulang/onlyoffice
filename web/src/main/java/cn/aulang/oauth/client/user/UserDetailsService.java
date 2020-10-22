package cn.aulang.oauth.client.user;

import cn.aulang.oauth.client.model.Profile;

/**
 * @author aulang
 * @email aulang@qq.com
 * @date 2020-10-22 10:06
 */
public interface UserDetailsService {

    User loadUserByProfile(Profile profile);

}
