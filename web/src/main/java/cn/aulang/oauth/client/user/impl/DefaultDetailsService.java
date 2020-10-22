package cn.aulang.oauth.client.user.impl;

import cn.aulang.oauth.client.model.Profile;
import cn.aulang.oauth.client.user.User;
import cn.aulang.oauth.client.user.UserDetailsService;

/**
 * @author aulang
 * @email aulang@qq.com
 * @date 2020-10-22 10:49
 */
public class DefaultDetailsService implements UserDetailsService {
    @Override
    public User loadUserByProfile(Profile profile) {
        return new User(profile.getId(), profile.getNickname(), null);
    }
}
