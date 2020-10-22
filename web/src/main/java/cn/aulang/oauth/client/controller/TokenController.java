package cn.aulang.oauth.client.controller;

import cn.aulang.oauth.client.core.OAuthTemplate;
import cn.aulang.oauth.client.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-22 11:41
 */
@RestController
public class TokenController {
    @Autowired
    private OAuthTemplate authTemplate;

    @GetMapping(path = "${token.path:/oauth/token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Token token(@RequestParam("code") String code,
                       @RequestParam("redirectUrl") String redirectUrl) {
        return authTemplate.getToken(code, redirectUrl);
    }
}
