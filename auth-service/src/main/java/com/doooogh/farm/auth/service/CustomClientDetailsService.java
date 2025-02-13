package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.entity.ClientDetailsEntity;
import com.doooogh.farm.auth.mapper.ClientDetailsMapper;
import com.google.common.collect.Lists;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义的 ClientDetailsService 实现类，用于从数据库加载客户端详细信息。
 */
@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final ClientDetailsMapper clientDetailsMapper;

    /**
     * CustomClientDetailsService 的构造函数。
     *
     * @param clientDetailsMapper 用于访问数据库中客户端详细信息的映射器
     */
    public CustomClientDetailsService(ClientDetailsMapper clientDetailsMapper) {
        this.clientDetailsMapper = clientDetailsMapper;
    }

    /**
     * 根据客户端ID加载客户端详细信息。
     *
     * @param clientId 客户端ID
     * @return 客户端详细信息
     * @throws ClientRegistrationException 如果找不到客户端
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetailsEntity client = clientDetailsMapper.selectById(clientId);
        if (client == null) {
            throw new ClientRegistrationException("未找到客户端: " + clientId);
        }

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());
        details.setAuthorizedGrantTypes(Arrays.asList(client.getAuthorizedGrantTypes().split(",")));
        details.setScope(Arrays.asList(client.getScopes().split(",")));
        details.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(client.getRedirectUris().split(","))));
        details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());

        return details;
    }
} 