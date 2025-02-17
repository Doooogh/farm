package com.doooogh.farm.auth.token;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/17
 * @description
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

        private final Object principal;  // 用户名或手机号
        private final Object credentials;  // 密码或验证码
        private final String authMethod;  // 认证方式（账号密码、短信、其他）


        public CustomAuthenticationToken( Object principal, Object credentials, String authMethod) {
                super(null);
                this.principal = principal;
                this.credentials = credentials;
                this.authMethod = authMethod;
        }

        @Override
        public Object getCredentials() {
                return credentials;
        }

        @Override
        public Object getPrincipal() {
                return principal;
        }

        public String getAuthMethod() {
                return authMethod;
        }

        @Override
        public void setDetails(Object details) {
                super.setDetails(details);
        }
}
