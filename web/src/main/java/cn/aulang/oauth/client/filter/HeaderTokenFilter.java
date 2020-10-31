package cn.aulang.oauth.client.filter;

import cn.aulang.oauth.client.core.OAuthTemplate;
import cn.aulang.oauth.client.model.Profile;
import cn.aulang.oauth.client.user.User;
import cn.aulang.oauth.client.user.UserDetailsService;
import cn.aulang.oauth.client.user.UserHolder;
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
    private UserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getParameter(Constants.ACCESS_TOKEN);
        String authorization = request.getHeader(Constants.AUTHORIZATION);

        /**
         * 以Header为准
         */
        if (!StringUtils.isEmpty(authorization)) {
            token = StringUtils.delete(authorization, Constants.BEARER);
        }

        if (StringUtils.isEmpty(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = getSessionUser(request);
        if (user != null && token.equals(user.getToken())) {
            UserHolder.set(user);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Profile profile = template.getProfile(token);
            user = userService.loadUserByProfile(profile);

            user.setToken(token);
            UserHolder.set(user);

            setSessionUser(request, user);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("获取用户信息失败！", e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void setSessionUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("SESSION_USER", user);
    }

    public User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("SESSION_USER");
    }
}
