package cn.aulang.office.web.service;

import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-20 16:10
 */
@Slf4j
@Service
public class UserService {
    public User currentUser() {
        return new User(Constants.OWNER, Constants.OWNER_NAME);
    }

    public User getById(String id) {
        return new User(Constants.OWNER, Constants.OWNER_NAME);
    }
}
