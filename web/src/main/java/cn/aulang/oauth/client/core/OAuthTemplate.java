package cn.aulang.oauth.client.core;

import cn.aulang.oauth.client.model.Profile;
import cn.aulang.oauth.client.model.Token;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 22:48
 */
@AllArgsConstructor
public class OAuthTemplate {
    private RestTemplate restTemplate;
    private OAuthProperties properties;

    public Token getToken(String code, String redirectUrl) {
        Map<String, String> uriVariables = new HashMap<>(5);
        uriVariables.put("client_id", code);
        uriVariables.put("grant_type", "authorization_code");
        uriVariables.put("code", code);
        uriVariables.put("client_secret", properties.getClientSecret());
        uriVariables.put("redirect_uri", redirectUrl);

        Token token = restTemplate.postForObject(
                properties.getTokenUrl(),
                null,
                Token.class,
                uriVariables
        );

        return token;
    }

    public Profile getProfile(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<Profile> responseEntity = restTemplate.exchange(
                properties.getProfileUrl(),
                HttpMethod.GET,
                httpEntity,
                Profile.class
        );

        return responseEntity.getBody();
    }
}
