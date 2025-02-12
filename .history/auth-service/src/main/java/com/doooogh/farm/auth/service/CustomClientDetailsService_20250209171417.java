package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.entity.OAuthClient;
import com.doooogh.farm.auth.mapper.OAuthClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

    private final OAuthClientMapper oAuthClientMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OAuthClient client = oAuthClientMapper.selectById(clientId);
        if (client == null) {
            throw new ClientRegistrationException("Client not found: " + clientId);
        }

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());
        
        // 使用HashSet替代Set.of
        details.setAuthorizedGrantTypes(new HashSet<>(
            Arrays.asList(client.getAuthorizedGrantTypes().split(","))
        ));
        
        details.setScope(new HashSet<>(
            Arrays.asList(client.getScope().split(","))
        ));
        
        // 如果有多个重定向URI，按逗号分隔
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
        if (client.getAuthorities() != null) {
            details.setAuthorities(new HashSet<>(
                Arrays.asList(client.getAuthorities().split(","))
            ));
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