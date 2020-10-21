package cn.aulang.oauth.client.filter;

import cn.aulang.oauth.client.core.OAuthTemplate;
import cn.aulang.oauth.client.model.Profile;
import cn.aulang.office.web.common.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-21 22:28
 */
@Slf4j
@AllArgsConstructor
public class HeaderTokenFilter extends OncePerRequestFilter {
    private OAuthTemplate template;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(Constants.AUTHORIZATION);

        if (StringUtils.isEmpty(authorization)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = StringUtils.delete(authorization, Constants.BEARER);

        try {
            Profile profile = template.getProfile(token);
            /**
             * TODO User对象转换扩展
             */
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("获取用户信息失败！", e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
