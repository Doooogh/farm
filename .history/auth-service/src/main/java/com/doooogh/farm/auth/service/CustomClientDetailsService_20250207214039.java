package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.entity.OAuth2Client;
import com.doooogh.farm.auth.repository.OAuth2ClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final OAuth2ClientRepository clientRepository;

    public CustomClientDetailsService(OAuth2ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OAuth2Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientRegistrationException("Client not found: " + clientId));

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());
        details.setScope(Arrays.asList(client.getScope().split(",")));
        details.setAuthorizedGrantTypes(Arrays.asList(client.getAuthorizedGrantTypes().split(",")));
        details.setRegisteredRedirectUri(Set.of(client.getWebServerRedirectUri()));
        details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
        
        return details;
    }
} 