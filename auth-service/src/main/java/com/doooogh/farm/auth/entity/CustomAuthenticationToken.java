package com.doooogh.farm.auth.entity;

import com.doooogh.farm.auth.dto.LoginRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/23
 * @description
 */

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String authMethod;
    private String phone;
    private String smsCode;
    private String captcha;


    public CustomAuthenticationToken(LoginRequest loginRequest){
        super(loginRequest.getUsername(),loginRequest.getPassword());
        this.authMethod = loginRequest.getAuthMethod();
        this.phone = loginRequest.getPhone();
        this.smsCode = loginRequest.getSmsCode();
        this.captcha = loginRequest.getCaptcha();
    }
}
