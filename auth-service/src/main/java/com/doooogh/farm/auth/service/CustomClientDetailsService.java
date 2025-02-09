package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.entity.OAuth2Client;
import com.doooogh.farm.auth.mapper.OAuth2ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

    private final OAuth2ClientMapper oAuthClientMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OAuth2Client client = oAuthClientMapper.selectById(clientId);
        if (client == null) {
            throw new ClientRegistrationException("Client not found: " + clientId);
        }

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());
        
        // 设置授权类型
        details.setAuthorizedGrantTypes(new HashSet<>(
            Arrays.asList(client.getAuthorizedGrantTypes().split(","))
        ));
        
        // 设置作用域
        details.setScope(new HashSet<>(
            Arrays.asList(client.getScope().split(","))
        ));
        
        // 设置重定向URI
        details.setRegisteredRedirectUri(new HashSet<>(
            Arrays.asList(client.getWebServerRedirectUri().split(","))
        ));
        
        // 设置资源ID
        if (client.getResourceIds() != null) {
            details.setResourceIds(new HashSet<>(
                Arrays.asList(client.getResourceIds().split(","))
            ));
        }
        
        // 设置权限
        if (!StringUtils.isEmpty(client.getAuthorities())) {
            details.setAuthorities(
                Arrays.stream(client.getAuthorities().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet())
            );
        }
        
        // 设置访问令牌有效期
        details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        
        // 设置刷新令牌有效期
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
        
        // 设置是否自动批准
        details.setAutoApproveScopes(Collections.singleton("true"));

        return details;
    }
} 