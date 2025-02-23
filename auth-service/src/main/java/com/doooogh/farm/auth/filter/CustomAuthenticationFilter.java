package com.doooogh.farm.auth.filter;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.entity.CustomAuthenticationToken;
import com.doooogh.farm.common.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/23
 * @description
 */
@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/**");
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            LoginRequest loginRequest = new LoginRequest(
                    request.getParameter("username"),
                    request.getParameter("password"),
                    request.getParameter("phone"),
                    request.getParameter("phone"),
                    request.getParameter("smsCode"),
                    request.getParameter("authMethod")
                    );
            CustomAuthenticationToken customAuthenticationToken = new CustomAuthenticationToken(loginRequest);
            return authenticationManager.authenticate(customAuthenticationToken);

    }
}
