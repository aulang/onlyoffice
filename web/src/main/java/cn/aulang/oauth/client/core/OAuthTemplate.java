package cn.aulang.oauth.client.core;

import cn.aulang.oauth.client.model.Profile;
import cn.aulang.oauth.client.model.Token;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 22:48
 */
@Slf4j
@AllArgsConstructor
public class OAuthTemplate {
    private RestTemplate restTemplate;
    private OAuthProperties properties;

    public Token getToken(String code, String redirectUrl) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(5);

        params.add("client_id", properties.getClientId());
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_secret", properties.getClientSecret());
        params.add("redirect_uri", redirectUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity httpEntity = new HttpEntity(params, httpHeaders);

        try {
            Token token = restTemplate.postForObject(
                    properties.getTokenUrl(),
                    httpEntity,
                    Token.class
            );
            return token;
        } catch (Exception e) {
            log.error("获取OAuth Token失败！", e);
            throw new RuntimeException(e);
        }
    }

    public Profile getProfile(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try {
            ResponseEntity<Profile> responseEntity = restTemplate.exchange(
                    properties.getProfileUrl(),
                    HttpMethod.GET,
                    httpEntity,
                    Profile.class
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("获取OAuth Profile失败！", e);
            throw new RuntimeException(e);
        }
    }
}
