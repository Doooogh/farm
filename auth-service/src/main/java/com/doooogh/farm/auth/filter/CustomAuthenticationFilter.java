package com.doooogh.farm.auth.filter;

import com.alibaba.fastjson2.JSONObject;
import com.doooogh.farm.auth.enums.AuthenticationEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/17
 * @description
 */

@Component
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        // 拦截所有请求
        super("/**");
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // 根据请求决定认证方式
        AuthenticationEnum authenticationEnum = determineAuthenticationMethod(request);  // 根据请求判断认证方式
        String username = request.getParameter("username");  // 获取账号
        String password = request.getParameter("password");  // 获取密码
        String phone = request.getParameter("phone");        // 获取手机号
        String smsCode = request.getParameter("smsCode");    // 获取短信验证码
        AbstractAuthenticationToken authRequest = null;
        switch (authenticationEnum) {
            case USERNAME_PASSWORD:
                authRequest = new UsernamePasswordAuthenticationToken(username, password);
                break;
            case SMS:
                authRequest = new UsernamePasswordAuthenticationToken(phone, smsCode);
                break;
            default:
                throw new AuthenticationException("Unsupported authentication method: " + authenticationEnum) {};
        }
        JSONObject details = new JSONObject();
        details.put("authMethod",authenticationEnum);
        //todo 其他的信息
        authRequest.setDetails(details);
        return this.getAuthenticationManager().authenticate(authRequest);  // 将请求交给 AuthenticationManager 进行认证
    }

    // 根据请求头、参数或其他信息判断认证方式
    private AuthenticationEnum determineAuthenticationMethod(HttpServletRequest request) {
        // 假设使用请求头来判断认证方式
        String authMethod = request.getHeader("Auth-Method");
        AuthenticationEnum authenticationEnum = AuthenticationEnum.getAuthenticationType(authMethod);
        return authenticationEnum;
    }
}
