package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.entity.ClientDetailsEntity;
import com.doooogh.farm.auth.repository.ClientDetailsRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final ClientDetailsRepository clientDetailsRepository;

    public CustomClientDetailsService(ClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetailsEntity client = clientDetailsRepository.findById(clientId)
                .orElseThrow(() -> new ClientRegistrationException("Client not found: " + clientId));

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());
        details.setAuthorizedGrantTypes(List.of(client.getAuthorizedGrantTypes().split(",")));
        details.setScope(List.of(client.getScopes().split(",")));
        details.setRegisteredRedirectUri(Set.of(client.getRedirectUris().split(",")));
        details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());

        return details;
    }
} 